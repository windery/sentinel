package sentinel.slots.degrade;

import cn.windery.learning.base.sentinel.Aph;
import cn.windery.learning.base.sentinel.AphInitializer;
import cn.windery.learning.base.sentinel.Tracer;
import cn.windery.learning.base.sentinel.exception.BlockException;
import cn.windery.learning.base.sentinel.slots.flow.RuleManager;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DegradeSlotTest {

    static String resource = "test_resource";

    @BeforeClass
    public static void setUp() {
        DegradeRule degradeRule = new DegradeRule(resource);
        degradeRule.setMaxRt(10);
        degradeRule.setMinRequests(10);
        degradeRule.setThreshold(0.5);
        degradeRule.setRetryWaitTime(1000);
        RuleManager.addRule(resource, degradeRule);

        AphInitializer.initialize();
    }

    @Test
    public void entry() throws InterruptedException {

        boolean blocked = false;
        long noBlockCount = 0;


        for (int i = 0; i < 2000; i++) {
            try {
                try {
                    Aph.entry("test_resource");
                    if (!blocked) {
                        System.out.println("wait 15ms");
                        Thread.sleep(15);
                    } else {
                        noBlockCount++;
                        if (noBlockCount > 200) {
                            blocked = false;
                            noBlockCount = 0;
                        }
                    }
                    System.out.println("==========  main logic =========");
                } finally {
                    Aph.exit("test_resource");
                }
            } catch (BlockException be) {
                System.out.println("blocked...");
                blocked = true;
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw e;
            } catch (Exception e) {
                Tracer.trace(e);
            }
        }


    }
}