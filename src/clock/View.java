package clock;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;

public class View implements Observer {
    
    ClockPanel panel;
    
    public View(Model model) {
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
        JMenu helpMenu = new JMenu("Help");
        
        //Create File Menu items
        JMenuItem fileMenuItem1 = new JMenuItem("New Alarm");
        JMenuItem fileMenuItem2 = new JMenuItem("Save Alarms");
        JMenuItem fileMenuItem3 = new JMenuItem("Save Alarms As...");
        JMenuItem fileMenuItem4 = new JMenuItem("Load Alarm File");
    
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
        
        fileMenuItem4.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
            }
        });
        
        //Create help menu items
        JMenuItem helpMenuItem1 = new JMenuItem("About");
        
        //Create help menu item action listeners
        helpMenuItem1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(frame, "Assignment 3: Alarm Clock");
            }
        });
        
        //Add fileMenuItems to fileMenu
        fileMenu.add(fileMenuItem1);
        fileMenu.add(fileMenuItem2);
        fileMenu.add(fileMenuItem3);
        fileMenu.add(fileMenuItem4);
        
        //Add helpMenuItems to helpMenu
        helpMenu.add(helpMenuItem1);
        
        //Add menus to menubar
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        frame.setJMenuBar(menuBar);
         
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
         
        JButton button = new JButton("Button 3 (LINE_START)");
        pane.add(button, BorderLayout.LINE_START);
         
        button = new JButton("Long-Named Button 4 (PAGE_END)");
        pane.add(button, BorderLayout.PAGE_END);
         
        button = new JButton("5 (LINE_END)");
        pane.add(button, BorderLayout.LINE_END);
        
        // End of borderlayout code
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
}
