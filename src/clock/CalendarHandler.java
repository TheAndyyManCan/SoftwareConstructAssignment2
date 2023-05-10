
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
 * Class to handle iCalendar events and files using the iCal4J library
 * This class is not finished, has not been implemented and therefore has not been included in testing
 * @author andy
 */
public class CalendarHandler {
    
    Calendar calendar;
    
    public CalendarHandler(){
        this.calendar = new Calendar();
    }
    
    /**
     * Creates a new VEvent object and adds it to the calendar object
     * @param alarm the alarm object to create an event for
     * @param index the index of the storage array from the priority queue
     */
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
    
    /**
     * Reads a calendar file from a FileInputStream object
     * @param fin FileInputStream object to read calendar file from 
     * @throws IOException 
     */
    public void readCalendarFile(FileInputStream fin) throws IOException {
        
        try {
            CalendarBuilder builder = new CalendarBuilder();
            this.calendar = builder.build(fin);
        } catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    /**
     * Returns a FileOutputStream with a calendar object converted into an ics file
     * @return FileOutputStream with a calendar object converted into an ics file
     * @throws IOException 
     */
    public FileOutputStream outputCalendarFile() throws IOException{
        
        FileOutputStream fout = new FileOutputStream("myalarms.ics");
        
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(this.calendar, fout);
        
        return fout;
        
    }
    
}
