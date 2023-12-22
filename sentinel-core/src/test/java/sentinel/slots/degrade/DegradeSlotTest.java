package sentinel.slots.degrade;

import cn.windery.sentinel.Aph;
import cn.windery.sentinel.AphInitializer;
import cn.windery.sentinel.Tracer;
import cn.windery.sentinel.exception.BlockException;
import cn.windery.sentinel.slots.degrade.DegradeRule;
import cn.windery.sentinel.slots.degrade.DegradeRuleManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import sentinel.TimeBasedTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DegradeSlotTest extends TimeBasedTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DegradeSlotTest.class);

    static String resource = "test_resource";

    @BeforeAll
    public static void setUp() {
        DegradeRule degradeRule = new DegradeRule(resource);
        degradeRule.setMaxRt(10);
        degradeRule.setMinRequests(10);
        degradeRule.setThreshold(0.5);
        degradeRule.setRetryWaitTime(1000);
        degradeRule.setWindowLengthInMs(1000);
        degradeRule.setRecoverRequests(10);
        DegradeRuleManager.getInstance().addRule(degradeRule);

        AphInitializer.initialize();
    }

    @Test
    public void entry() {

        String resource = "test_resource";
        long shortWait = 1;
        long longWait = 15;


        for (int i = 0; i < 10; i++) {
            entryAndSleepFor(resource, longWait);
        }

        System.out.println("after breaker open");
        assertFalse(entryAndSleepFor(resource, longWait));

        sleep(1000);
        for (int i = 0; i < 10; i++) {
            entryAndSleepFor(resource, longWait);
        }

        System.out.println("after breaker recover failed");
        assertFalse(entryAndSleepFor(resource, longWait));

        sleep(1000);

        for (int i = 0; i < 10; i++) {
            assertTrue(entryAndSleepFor(resource, shortWait));
        }

        System.out.println("after breaker recover success");
        assertTrue(entryAndSleepFor(resource, longWait));

    }
}