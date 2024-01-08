package sentinel;

import cn.windery.sentinel.Aph;
import cn.windery.sentinel.Tracer;
import cn.windery.sentinel.exception.BlockException;
import cn.windery.sentinel.slots.degrade.circuitbreak.CircuitBreakerManager;
import org.slf4j.Logger;

public class TimeBasedTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TimeBasedTest.class);

    protected boolean entryAndSleepFor(String resource, long sleepTime) {
        try {
            Aph.entry(resource);
            Thread.sleep(sleepTime);
        } catch (BlockException be) {
            log.info("block by degrade rule: {}", be.getMessage());
            return false;
        } catch (Exception e) {
            Tracer.trace(e);
        } finally {
            Aph.exit(resource);
        }
        return true;
    }

    protected void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException("sleep interrupted");
        }
    }

}
