package clock;

import java.util.Calendar;
import java.util.Observable;
import queuemanager.SortedArrayPriorityQueue;
import queuemanager.PriorityItem;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
//import java.util.GregorianCalendar;

public class Model extends Observable {
    
    int hour = 0;
    int minute = 0;
    int second = 0;
    
    int oldSecond = 0;
    
    SortedArrayPriorityQueue<Alarm> alarmQueue = new SortedArrayPriorityQueue<Alarm>(50);
    
    public Model() {
        update();
    }
    
    public void update() {
        Calendar date = Calendar.getInstance();
        hour = date.get(Calendar.HOUR);
        minute = date.get(Calendar.MINUTE);
        oldSecond = second;
        second = date.get(Calendar.SECOND);
        if (oldSecond != second) {
            setChanged();
            notifyObservers();
        }
    }
    
    public void addAlarm(LocalTime time){
        
        Alarm alarm = new Alarm(time);
        LocalTime currentTime = LocalTime.now();
        int priority = (int)currentTime.until(time, ChronoUnit.MINUTES);
        
        if(priority < 0){
            priority = (int)(currentTime.until(LocalTime.MAX, ChronoUnit.MINUTES) + (LocalTime.MIDNIGHT.until(time, ChronoUnit.MINUTES)));
        }
        
        try {
            this.alarmQueue.add(alarm, priority);
            this.reorganizeAlarmQueue();
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void reorganizeAlarmQueue(){
        
        int[] newPriorities = new int[this.alarmQueue.getCapacity()];
        Object[] storage = this.alarmQueue.getStorage();
        LocalTime currentTime = LocalTime.now();
        
        for(int i=0; i < storage.length; i++){
            
            long newPriority = currentTime.until(((PriorityItem<Alarm>) storage[i]).getItem().getTime(), ChronoUnit.MINUTES);
            
            if(newPriority < 0){
                newPriority = ((currentTime.until(LocalTime.MAX, ChronoUnit.MINUTES)) + (LocalTime.MIDNIGHT.until(((PriorityItem<Alarm>) storage[i]).getItem().getTime(), ChronoUnit.MINUTES))) + 1;
            }
            
            newPriorities[i] = (int)newPriority;
            
        }
        
        this.alarmQueue.reorganizeQueue(newPriorities);
        
    }
    
}
