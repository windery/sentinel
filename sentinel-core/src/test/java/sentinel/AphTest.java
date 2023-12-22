package sentinel;

import cn.windery.learning.base.sentinel.exception.BlockException;
import cn.windery.learning.base.sentinel.slots.flow.FlowRule;
import cn.windery.learning.base.sentinel.slots.flow.RuleManager;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class AphTest {

    @BeforeClass
    public static void setUp() {
        FlowRule flowRule = new FlowRule("test_resource", 70);
        RuleManager.addRule("test_resource", flowRule);

        AphInitializer.initialize();
    }

    @Test
    public void entry() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            try {
                try {
                    Aph.entry("test_resource");
                    System.out.println("==========  main logic =========");
                    if (i % 10 == 0) {
                        throw new RuntimeException("qqq");
                    }
                } finally {
                    Aph.exit("test_resource");
                }
            } catch (BlockException be) {
                System.out.println("blocked...");
            } catch (Exception e) {
                Tracer.trace(e);
            } finally {
                Thread.sleep(10);
            }
        }
    }

    @Test
    public void exit() {

    }

}