
package clock;

import java.time.LocalTime;

/**
 * Class to handle data to store alarms
 * @author andy 
 */
public class Alarm {
    
    private LocalTime time;
    
    public Alarm(LocalTime time){
        this.time = time;
    }
    
    //class getters
    public LocalTime getTime(){return this.time;}
    
    /**
     * Displays the time of the alarm as a string
     * @return String representation of alarm time 
     */
    @Override
    public String toString(){
        return this.time.getHour() + ":" + this.time.getMinute();
    }
}
