package org.example;

import org.junit.*;

public class ActionTest {

    @Test
    public void testSetGetDescription() {
        String testmessage  = "xyz";

        Action testaction = new Action();
        testaction.setDescription(testmessage);
        Assert.assertEquals(testaction.getDescription(), testmessage);
    }

    @Test
    public void testSetGetLocation() {
        Integer testpriority  = 1;

        Action testaction = new Action();
        testaction.setPriority(testpriority);
        Assert.assertEquals(testaction.getPriority(), testpriority);
    }

    @Test(expected = AssertionError.class)
    public void testTestMe() {
        Action testaction = new Action();
        Assert.assertEquals(1,2);
    }
}
