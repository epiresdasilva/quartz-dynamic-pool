package br.com.evandropires.quartz;

import br.com.evandropires.quartz.impl.ExecutorServiceThreadPool;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by evandro on 13/03/17.
 */
@RunWith(JUnit4.class)
public class DynamicThreadPoolRegisterTest {

    private static final String SCHED_NAME = "SchedTest";

    @Test
    public void testBind() {
        bind();
    }

    private void bind() {
        DynamicThreadPoolRepository.getInstance().bind(SCHED_NAME, new ExecutorServiceThreadPool());
    }

    @Test
    public void testLookupThreadPool() {
        bind();

        DynamicThreadPool threadPool = DynamicThreadPoolRepository.getInstance().lookup(SCHED_NAME);
        Assert.assertNotNull(threadPool);
    }

    @Test
    public void testLookupWrongThreadPool() {
        DynamicThreadPool threadPool = DynamicThreadPoolRepository.getInstance().lookup("WrongSchedTest");
        Assert.assertNull(threadPool);
    }
}
