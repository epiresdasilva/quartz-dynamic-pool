package br.com.evandropires.quartz;

import br.com.evandropires.quartz.impl.DynamicThreadPool;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by evandro on 13/03/17.
 */
@RunWith(JUnit4.class)
public class ThreadPoolRegisterTest {

    private static final String SCHED_NAME = "SchedTest";

    @Test
    public void register() {
        registerInternal();
    }

    private void registerInternal() {
        ThreadPoolRepository.getInstance().bind(SCHED_NAME, new DynamicThreadPool());
    }

    @Test
    public void getThreadPool() {
        registerInternal();

        DynamicThreadPool threadPool = ThreadPoolRepository.getInstance().lookup(SCHED_NAME);
        Assert.assertNotNull(threadPool);
    }

    @Test
    public void getWrongThreadPool() {
        DynamicThreadPool threadPool = ThreadPoolRepository.getInstance().lookup("WrongSchedTest");
        Assert.assertNull(threadPool);
    }
}
