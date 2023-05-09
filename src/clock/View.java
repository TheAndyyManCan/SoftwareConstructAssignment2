package clock;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalTime;
import java.util.Observer;
import java.util.Observable;

public class View implements Observer {
    
    ClockPanel panel;
    
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
        
        JInternalFrame alarmList = new JInternalFrame("Alarm List");
        //JButton[] buttonArray = model.getAlarmsAsButtons();
        Container alarmListPane = frame.getContentPane();
        //for(int i = 0; i < buttonArray.length; i++){
            //alarmListPane.add(buttonArray[i]);
        //}
        
        pane.add(alarmList, BorderLayout.LINE_START);
        alarmList.setVisible(true);
        //JButton button = new JButton("Button 3 (LINE_START)");
        //pane.add(button, BorderLayout.LINE_START);
         
        //button = new JButton("Long-Named Button 4 (PAGE_END)");
        //pane.add(button, BorderLayout.PAGE_END);
         
        //button = new JButton("5 (LINE_END)");
        //pane.add(button, BorderLayout.LINE_END);
        
        // End of borderlayout code
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
    
    public void showAlarm(){
        JOptionPane.showMessageDialog(null, "ALARM");
    }
}
