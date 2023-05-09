
package clock;

import java.time.LocalTime;

/**
 *
 * @author andy
 */
public class Alarm {
    
    private LocalTime time;
    private LocalTime currentTime;
    
    public Alarm(LocalTime time){
        this.time = time;
        this.currentTime = LocalTime.now();
    }
    
    //class setters
    private void setTime(LocalTime time){this.time = time;}
    private void setCurrentTime(){this.currentTime = LocalTime.now();}
    
    //class getters
    public LocalTime getTime(){return this.time;}
    public LocalTime getCurrentTime(){return this.currentTime;}
    
    public boolean checkAlarm(){
        this.setCurrentTime();
        return ((this.time.getHour() == this.currentTime.getHour()) || (this.time.getMinute() == this.currentTime.getMinute()));
    }
    
    @Override
    public String toString(){
        return this.time.getHour() + ":" + this.time.getMinute();
    }
}
