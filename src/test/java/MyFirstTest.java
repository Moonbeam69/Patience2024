import org.junit.*;
import org.example.*;

public class MyFirstTest {

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
}
