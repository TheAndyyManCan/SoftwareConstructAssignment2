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
    
    /**
     * Creates a new Alarm objet and adds it to the alarm queue
     * Works out the priority value based on the amount of time until the alarm goes off 
     * @param time LocalTime the time the alarm should go off
     */
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
    
    /**
     * Creates an array of new priority values by calculating the time until each alarm from the current time
     * Passes this array to the Priority Queue class which will then reorganise the queue with the new priority values
     */
    public void reorganizeAlarmQueue(){
        
        int[] newPriorities = new int[this.alarmQueue.getTailIndex() + 1];
        Object[] storage = this.alarmQueue.getStorage();
        LocalTime currentTime = LocalTime.now();
        
        for(int i=0; i <= this.alarmQueue.getTailIndex(); i++){
            
            //Calculate priority as the amount of seconds between now and the time the alarm should go off 
            long newPriority = currentTime.until(((PriorityItem<Alarm>) storage[i]).getItem().getTime(), ChronoUnit.SECONDS);
            
            /**
            * If the priority is below 0 then the alarm is due to go off the next day.
            * Priority should then be calculated as (time from now until 23.59.59) + (time from midnight until the alarm time) + 1 second to make up for the second lost between 23.59.59 and midnight  
            */
            if(newPriority < 0){
                newPriority = ((currentTime.until(LocalTime.MAX, ChronoUnit.SECONDS)) + (LocalTime.MIDNIGHT.until(((PriorityItem<Alarm>) storage[i]).getItem().getTime(), ChronoUnit.SECONDS))) + 1;
            }
            
            newPriorities[i] = (int)newPriority;
            
        }
        
        this.alarmQueue.reorganizeQueue(newPriorities);
        
    }
    
    /**
     * Creates an array of JButton objects to be used to create a list of currently active alarms
     * Also gives functionality to each button to edit and delete each alarm by creating actionListener for each object
     * @return JButton[] array of JButton objects with actionListeners attached 
     */
    public JButton[] getAlarmsAsButtons(){
        
        JButton[] buttonArray = new JButton[this.alarmQueue.getTailIndex() + 1];
        
        //check if alarm queue is not empty
        if(!this.alarmQueue.isEmpty()){
            
            //Loop through each alarm object in the queue
            for(int i=0; i <= this.alarmQueue.getTailIndex(); i++){
                
                //create a new button for alamr object with the alarm time as the button text
                JButton button = new JButton(((PriorityItem<Alarm>)this.alarmQueue.getStorage()[i]).getItem().toString());
                
                final int index = i;
                
                button.addActionListener(new ActionListener(){
                    
                    @Override
                    public void actionPerformed(ActionEvent e){
                        
                       SpinnerModel hourSpinnerModel = new SpinnerNumberModel(
                            (int)((PriorityItem<Alarm>)alarmQueue.getStorage()[index]).getItem().getTime().getHour(),
                            0,
                            23,
                            1
                        );
               
                        SpinnerModel minuteSpinnerModel = new SpinnerNumberModel(
                            (int)((PriorityItem<Alarm>)alarmQueue.getStorage()[index]).getItem().getTime().getMinute(),
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
                        
                        Object[] options = {
                            "Edit",
                            "Delete",
                            "Cancel"
                        };
               
                        int editAlarm = JOptionPane.showOptionDialog(
                            null,
                            fields,
                            "Edit Alarm: ",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                                options,
                            null
                        );
                        
                        /**
                         * If the edit button is pressed, store the new alarm time in the queue
                         * If the delete button is pressed, delete the alarm from the queue
                         */
                        if(editAlarm == JOptionPane.OK_OPTION){
                            Alarm alarm = new Alarm(LocalTime.of((int)hourSpinner.getValue(), (int)minuteSpinner.getValue()));
                            editAlarm(alarm, index);
                        } else if (editAlarm == 1) {
                            deleteAlarm(index);
                        }
                        
                    }
                    
                });
                
                //add new button to the array of JButton objects
                buttonArray[i] = button;
            } 
        }
        
        return buttonArray;
    }
    
    /**
     * Accepts a new Alarm item to replace the Alarm item being edited and creates a new priority value for the alarm
     * Passes Alarm item, priority value and array index to be replaced to the Priority Queue class to replace the Alarm being edited
     * Calls the reorganizeAlarmQueue method to reorganize the queue
     * @param alarm new Alarm item to be inserted into the queue
     * @param index index of the Alarm item being edited in the storage array
     */
    public void editAlarm(Alarm alarm, int index){
        
        int priority = 0;
        LocalTime currentTime = LocalTime.now();
        
        //Calculate priority as the amount of seconds between now and the time the alarm should go off
        priority = (int)currentTime.until(alarm.getTime(), ChronoUnit.SECONDS);
        
        /**
         * If the priority is below 0 then the alarm is due to go off the next day.
         * Priority should then be calculated as (time from now until 23.59.59) + (time from midnight until the alarm time) + 1 second to make up for the second lost between 23.59.59 and midnight  
         */
        if(priority < 0){
            priority = (int)(currentTime.until(LocalTime.MAX, ChronoUnit.SECONDS)) + (int)(LocalTime.MIDNIGHT.until(alarm.getTime(), ChronoUnit.SECONDS)) + 1;
        }
        
        this.alarmQueue.editItemInQueue(alarm, priority, index);
        this.reorganizeAlarmQueue();
        
    }
    
    /**
     * Calls the deleteItemInQueue function in the PriorityQueue class and passes the index in the storage array to be deleted
     * Calls the reorganizeAlarmQueue class to reorganise the queue once the item has been deleted
     * @param index Index of storage array to be deleted
     */
    public void deleteAlarm(int index){
        this.alarmQueue.deleteItemInQueue(index);
        this.reorganizeAlarmQueue();
    }
    
}
