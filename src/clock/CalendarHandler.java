
package clock;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.data.CalendarOutputter;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import net.fortuna.ical4j.model.Date;

/**
 *
 * @author andy
 */
public class CalendarHandler {
    
    Calendar calendar;
    
    public CalendarHandler(){
        this.calendar = new net.fortuna.ical4j.model.Calendar();
    }
    
    public void createAlarmEventAndAddToCalendar(Alarm alarm, int index){
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        
        int dayOfMonth = currentDate.getDayOfMonth();
        
        if(currentTime.until(alarm.getTime(), ChronoUnit.SECONDS) < 0){
            dayOfMonth = dayOfMonth + 1;
        } 
        
        cal.set(java.util.Calendar.MONTH, currentDate.getMonthValue());
        cal.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(java.util.Calendar.HOUR_OF_DAY, alarm.getTime().getHour());
        cal.set(java.util.Calendar.MINUTE, alarm.getTime().getMinute());
        
        VEvent alarmEvent = new VEvent(new Date(cal.getTime()), "Alarm" + index);
        
        this.calendar.getComponents().add(alarmEvent);
        
    } 
    
    public void readCalendarFile(FileInputStream fin) throws IOException {
        
        try {
            CalendarBuilder builder = new CalendarBuilder();
            this.calendar = builder.build(fin);
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public FileOutputStream outputCalendarFile() throws IOException{
        
        FileOutputStream fout = new FileOutputStream("myalarms.ics");
        
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(this.calendar, fout);
        
        return fout;
        
    }
    
}
