package clock;

import java.util.Calendar;
import java.util.Observable;
import queuemanager.SortedArrayPriorityQueue;
import queuemanager.PriorityItem;
import javax.swing.JButton;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
//import java.util.GregorianCalendar;

public class Model extends Observable {
    
    int hour = 0;
    int minute = 0;
    int second = 0;
    
    int oldSecond = 0;
    
    SortedArrayPriorityQueue<Alarm> alarmQueue = new SortedArrayPriorityQueue(50);
    
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
        int priority = (int)currentTime.until(time, ChronoUnit.SECONDS);
        
        if(priority < 0){
            priority = (int)(currentTime.until(LocalTime.MAX, ChronoUnit.SECONDS) + (LocalTime.MIDNIGHT.until(time, ChronoUnit.SECONDS)));
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
            
            long newPriority = currentTime.until(((PriorityItem<Alarm>) storage[i]).getItem().getTime(), ChronoUnit.SECONDS);
            
            if(newPriority < 0){
                newPriority = ((currentTime.until(LocalTime.MAX, ChronoUnit.SECONDS)) + (LocalTime.MIDNIGHT.until(((PriorityItem<Alarm>) storage[i]).getItem().getTime(), ChronoUnit.SECONDS))) + 1;
            }
            
            newPriorities[i] = (int)newPriority;
            
        }
        
        this.alarmQueue.reorganizeQueue(newPriorities);
        
    }
    
    public JButton[] getAlarmsAsButtons(){
        
        JButton[] buttonArray = new JButton[this.alarmQueue.getStorage().length];
        
        if(this.alarmQueue.getStorage().length > 0){
            
            for(int i=0; i < this.alarmQueue.getStorage().length; i++){
                
                JButton button = new JButton(((PriorityItem<Alarm>)this.alarmQueue.getStorage()[i]).getItem().toString());
                
                final int index = i;
                
                button.addActionListener(new ActionListener(){
                    
                    @Override
                    public void actionPerformed(ActionEvent e){
                        
                       SpinnerModel hourSpinnerModel = new SpinnerNumberModel(
                            (int)LocalTime.now().getHour(),
                            0,
                            23,
                            1
                        );
               
                        SpinnerModel minuteSpinnerModel = new SpinnerNumberModel(
                            (int)LocalTime.now().getMinute(),
                            0,
                            59,
                            1
                        );
               
                        JSpinner hourSpinner = new JSpinner(hourSpinnerModel);
                        JSpinner minuteSpinner = new JSpinner(minuteSpinnerModel);
               
                        Object[] fields = {
                            "Hours: ", hourSpinner,
                            "Minutes: ", minuteSpinner
                        };
               
                        int editAlarm = JOptionPane.showOptionDialog(
                            null,
                            fields,
                            "Set Alarm: ",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            null,
                            null
                        );
                        
                        if(editAlarm == JOptionPane.OK_OPTION){
                            Alarm alarm = new Alarm(LocalTime.of((int)hourSpinner.getValue(), (int)minuteSpinner.getValue()));
                            editAlarm(alarm, index);
                        }
                    }
                    
                });
                
                buttonArray[i] = button;
            } 
        }
        
        return buttonArray;
    }
    
    public void editAlarm(Alarm alarm, int index){
        
        int priority = 0;
        LocalTime currentTime = LocalTime.now();
        
        priority = (int)currentTime.until(alarm.getTime(), ChronoUnit.SECONDS);
        
        if(priority < 0){
            priority = (int)(currentTime.until(LocalTime.MAX, ChronoUnit.SECONDS)) + (int)(LocalTime.MIDNIGHT.until(alarm.getTime(), ChronoUnit.SECONDS)) + 1;
        }
        
        this.alarmQueue.editItemInQueue(alarm, priority, index);
        this.reorganizeAlarmQueue();
        
    }
    
}
