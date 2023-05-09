package clock;

import java.awt.event.*;
import javax.swing.Timer;
import java.time.LocalTime;

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
            }
        };
        
        timer = new Timer(100, listener);
        timer.start();
        
    }
    
    public void addNewAlarm(int hour, int minutes){
        
    }
}