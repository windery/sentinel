package sentinel;

import cn.windery.sentinel.AphInitializer;
import cn.windery.sentinel.slots.FlowRuleManager;
import cn.windery.sentinel.slots.flow.FlowRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AphTest extends TimeBasedTest {

    @BeforeAll
    public static void setUp() {
        FlowRule flowRule = new FlowRule("test_resource", 70);
        FlowRuleManager.getInstance().addRule(flowRule);

        AphInitializer.initialize();
    }

    @Test
    public void entry() throws InterruptedException {

        for (int i = 0; i < 70; i++) {
            assertTrue(entryAndSleepFor("test_resource", 1));
        }

        assertFalse(entryAndSleepFor("test_resource", 1));

        Thread.sleep(1000);

        assertTrue(entryAndSleepFor("test_resource", 1));

    }

    @Test
    public void exit() {

    }

}