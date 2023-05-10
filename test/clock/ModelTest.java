/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package clock;

import java.time.LocalTime;
import javax.swing.JButton;
import org.junit.Test;
import static org.junit.Assert.*;
import queuemanager.QueueUnderflowException;
import queuemanager.PriorityItem;

/**
 *
 * @author andy
 */
public class ModelTest {

    /**
     * Test of addAlarm method, of class Model.
     */
    @Test
    public void testAddAlarm() throws QueueUnderflowException {
        System.out.println("addAlarm");
        LocalTime time = LocalTime.of(12,35);
        Model instance = new Model();
        instance.addAlarm(time);
        assertEquals(time, instance.alarmQueue.head());
    }

    /**
     * Test of reorganizeAlarmQueue method, of class Model.
     * Test result will change based on the time of day as the methods use the current system time
     */
    @Test
    public void testReorganizeAlarmQueue() throws QueueUnderflowException {
        System.out.println("reorganizeAlarmQueue");
        LocalTime time1 = LocalTime.of(10, 40);
        LocalTime time2 = LocalTime.of(16, 45);
        LocalTime time3 = LocalTime.of(22, 10);
        Model instance = new Model();
        instance.addAlarm(time1);
        instance.addAlarm(time2);
        instance.addAlarm(time3);
        Alarm expResult = new Alarm(LocalTime.of(22, 10));
        assertEquals(expResult, instance.alarmQueue.head());
    }

    /**
     * Test of getAlarmsAsButtons method, of class Model.
     */
    @Test
    public void testGetAlarmsAsButtons() {
        System.out.println("getAlarmsAsButtons");
        LocalTime time1 = LocalTime.of(10, 40);
        LocalTime time2 = LocalTime.of(16, 45);
        LocalTime time3 = LocalTime.of(22, 10);
        Model instance = new Model();
        instance.addAlarm(time1);
        instance.addAlarm(time2);
        instance.addAlarm(time3);
        JButton expResult = new JButton("22:10");
        JButton[] result = instance.getAlarmsAsButtons();
        assertEquals(expResult, result);
    }

    /**
     * Test of editAlarm method, of class Model.
     */
    @Test
    public void testEditAlarm() throws QueueUnderflowException {
        System.out.println("editAlarm");
        LocalTime time1 = LocalTime.of(10, 30);
        LocalTime time2 = LocalTime.of(12, 45);
        int index = 0;
        Model instance = new Model();
        instance.addAlarm(time1);
        Alarm newAlarm = new Alarm(time2);
        instance.editAlarm(newAlarm, index);
        assertEquals(newAlarm, instance.alarmQueue.head());
    }

    /**
     * Test of deleteAlarm method, of class Model.
     */
    @Test
    public void testDeleteAlarm() {
        System.out.println("deleteAlarm");
        LocalTime time1 = LocalTime.of(10, 40);
        LocalTime time2 = LocalTime.of(16, 45);
        LocalTime time3 = LocalTime.of(22, 10);
        Model instance = new Model();
        instance.addAlarm(time1);
        instance.addAlarm(time2);
        instance.addAlarm(time3);
        int index = 1;
        instance.deleteAlarm(index);
        Alarm expResult =  new Alarm(time2);
        assertEquals(expResult, ((PriorityItem<Alarm>)instance.alarmQueue.getStorage()[index]).getItem());
    }
    
}
