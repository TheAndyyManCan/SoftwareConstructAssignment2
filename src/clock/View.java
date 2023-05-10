package clock;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalTime;
import java.util.Observer;
import java.util.Observable;

public class View implements Observer {
    
    ClockPanel panel;
    JButton alarmToggle;
    
    public View(final Model model) {
        final JFrame frame = new JFrame();
        panel = new ClockPanel(model);
        //frame.setContentPane(panel);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Start of border layout code
        
        // I've just put a single button in each of the border positions:
        // PAGE_START (i.e. top), PAGE_END (bottom), LINE_START (left) and
        // LINE_END (right). You can omit any of these, or replace the button
        // with something else like a label or a menu bar. Or maybe you can
        // figure out how to pack more than one thing into one of those
        // positions. This is the very simplest border layout possible, just
        // to help you get started.
        
        Container pane = frame.getContentPane();
        
        //Add new menu bar
        JMenuBar menuBar = new JMenuBar();
        pane.add(menuBar, BorderLayout.PAGE_START);
        
        //Create menus
        JMenu fileMenu = new JMenu("File");
        JMenu alarmMenu = new JMenu("Alarm");
        JMenu helpMenu = new JMenu("Help");
        
        //Create File Menu items
        JMenuItem fileMenuItem1 = new JMenuItem("Save Alarms");
        JMenuItem fileMenuItem2 = new JMenuItem("Save Alarms As...");
        JMenuItem fileMenuItem3 = new JMenuItem("Load Alarm File");
    
        //Create file menu item action listeners
        fileMenuItem1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
            }
        });
        
        fileMenuItem2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
            }
        });
        
        fileMenuItem3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
            }
        });
        
        //Create alarm menu items
        JMenuItem alarmMenuItem1 = new JMenuItem("New Alarm");
        
        alarmMenuItem1.addActionListener(new ActionListener(){
            
           @Override
           public void actionPerformed(ActionEvent e){
               
               //Create spinner models to be used to input hours and minutes of alarms
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
               
               //Create JSpinner objects based on the models created
               JSpinner hourSpinner = new JSpinner(hourSpinnerModel);
               JSpinner minuteSpinner = new JSpinner(minuteSpinnerModel);
               
               //The items to be viewed in the JOptionPane object
               Object[] fields = {
                   "Hours: ", hourSpinner,
                   "Minutes: ", minuteSpinner
               };
               
               /**
                * Creates a JOptionPane Option Dialog box to collect user input to create an alarm
                * The button chosen by the user is returned as an int value
                */
               int addAlarm = JOptionPane.showOptionDialog(
                       frame,
                       fields,
                       "Set Alarm: ",
                       JOptionPane.OK_CANCEL_OPTION,
                       JOptionPane.QUESTION_MESSAGE,
                       null,
                       null,
                       null
               );
               
               //Check the user has clicked the 'OK' button and add a new alarm if they have
               if(addAlarm == JOptionPane.OK_OPTION){
                   LocalTime alarmTime = LocalTime.of((int)hourSpinner.getValue(), (int)minuteSpinner.getValue());
                   model.addAlarm(alarmTime);
               }
           }
           
        });
        
        //Create help menu items
        JMenuItem helpMenuItem1 = new JMenuItem("About");
        
        //Create help menu item action listeners
        helpMenuItem1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(frame, "Assignment 3: Alarm Clock\nAndy McDonald 12001649");
            }
        });
        
        //Add fileMenuItems to fileMenu
        fileMenu.add(fileMenuItem1);
        fileMenu.add(fileMenuItem2);
        fileMenu.add(fileMenuItem3);
        
        //Add alarm menu items to alarmMenu
        alarmMenu.add(alarmMenuItem1);
        
        //Add helpMenuItems to helpMenu
        helpMenu.add(helpMenuItem1);
        
        //Add menus to menubar
        menuBar.add(fileMenu);
        menuBar.add(alarmMenu);
        menuBar.add(helpMenu);
        
        frame.setJMenuBar(menuBar);
         
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
        
        //Create a new panal to display current active alarms
        final JPanel alarmList = new JPanel();
        //Create vertical layout to display alarms
        final BoxLayout alarmListLayout = new BoxLayout(alarmList, BoxLayout.Y_AXIS);
        //Add the panel to the frame
        pane.add(alarmList, BorderLayout.LINE_START);
        
        //Create a button to toggle the current alarm panel
        alarmToggle = new JButton("View Alarms");
        pane.add(alarmToggle, BorderLayout.PAGE_END);
        
        //Add action listener to handle button being clicked
        alarmToggle.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e){
               
                //Get JButton array to display current alarms and allow them to be edited/deleted
                JButton[] buttonArray = model.getAlarmsAsButtons();
                
                //Call toggleCurrentAlarms to toggle the visibility of the alarm list panel
                toggleCurrentAlarms(buttonArray, alarmList, alarmListLayout);
            }
            
        });
        
        // End of borderlayout code
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
    
    /**
     * Show the alarm when the alarm is due to go off
     * This is called from the Controller class
     */
    public void showAlarm(){
        JOptionPane.showMessageDialog(null, "ALARM");
    }
    
    /**
     * Toggles the visibility of the alarm list panel
     * checks if the panel is visible and sets the visibility to the opposite of what is currently set
     * @param buttonArray JButton array showing current alarms set with action listeners to edit/delete alarms
     * @param alarmList JPanel to display current alarms
     * @param alarmListLayout BoxLayout for alarm list panel 
     */
    public void toggleCurrentAlarms(JButton[] buttonArray, JPanel alarmList, BoxLayout alarmListLayout){
        
        if(alarmList.isVisible()){
            alarmList.setVisible(false);
            alarmList.removeAll();
        } else {
            
            alarmList.setLayout(alarmListLayout);
            alarmList.add(Box.createVerticalGlue());
            
            for(int i=0; i < buttonArray.length; i++){
                alarmList.add(buttonArray[i]);
            }
        
            alarmList.setVisible(true);
            
        }
        
    }
    
    /**
     * Updates the alarm toggle button with the current alarm status
     * Will display the time of the next alarm if there is an alarm set
     * @param model Model class to get time of next alarm from alarm queue
     */
    public void updateAlarmToggle(Model model){
        
        if(model.alarmQueue.isEmpty()){
            alarmToggle.setText("No Alarm Set");
        } else {
            
            try {
                alarmToggle.setText("View Alarms NEXT ALARM: " + model.alarmQueue.head().toString());
            } catch (Exception e){
                e.printStackTrace();
            }
            
        }
    }
}
