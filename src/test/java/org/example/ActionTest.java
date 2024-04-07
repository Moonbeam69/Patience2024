package org.example;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
public class ActionTest {

    @Test
    public void testSetGetDescription() {
        String testmessage  = "xyz";

        Action testaction = new Action();
        testaction.setDescription(testmessage);
        assertEquals(testaction.getDescription(), testmessage);
    }

    @Test
    public void testSetGetLocation() {
        Integer testpriority  = 1;

        Action testaction = new Action();
        testaction.setPriority(testpriority);
        assertEquals(testaction.getPriority(), testpriority);
    }

}
