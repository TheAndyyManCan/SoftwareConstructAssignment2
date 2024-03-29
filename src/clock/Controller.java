package clock;

import java.awt.event.*;
import javax.swing.Timer;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Controller {
    
    ActionListener listener;
    Timer timer;
    
    Model model;
    View view;
    
    public Controller(Model m, View v) {
        model = m;
        view = v;
        
        listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.update();
                boolean alarm = checkNextAlarm();
                
                if(alarm){
                    
                    view.showAlarm();
                    
                    try {
                        model.alarmQueue.remove();
                    } catch (Exception x){
                        x.printStackTrace();
                    }
                }
                
                view.updateAlarmToggle(model);
            }
        };
        
        timer = new Timer(100, listener);
        timer.start();
        
    }
    
    /**
     * Checks the head of the alarm queue to see if it is ready to go off
     * @return true if the alarm is due to go off 
     */
    public boolean checkNextAlarm(){
        
        try {
            
            Alarm nextAlarm = model.alarmQueue.head();
            LocalTime currentTime = LocalTime.now();
        
            return currentTime.until(nextAlarm.getTime(), ChronoUnit.SECONDS) == 0;
            
        } catch (Exception e){
           return false;
        }
        
    }
}