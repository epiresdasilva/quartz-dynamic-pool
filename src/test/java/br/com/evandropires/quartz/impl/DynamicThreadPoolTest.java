package br.com.evandropires.quartz.impl;

import br.com.evandropires.quartz.ThreadPoolRepository;
import br.com.evandropires.quartz.job.DummyJob;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by evandro on 13/03/17.
 */
@RunWith(JUnit4.class)
public class DynamicThreadPoolTest {

    private static final String QUARTZ_SCHEDULER = "QuartzScheduler";
    private static final Integer POOL_SIZE = 5;
    private static final Integer INCREASE_POOL_SIZE = 10;
    private static final Integer DECREASE_POOL_SIZE = 2;

    private Scheduler scheduler;
    private AtomicInteger jobSequence = new AtomicInteger(1);

    /**
     * Configura o quartz com o {@link DynamicThreadPool}
     *
     * @throws SchedulerException
     */
    @Before
    public void beforeTests() throws SchedulerException {
        Properties properties = new Properties();
        properties.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS,
                DynamicThreadPool.class.getName());
        properties.setProperty("org.quartz.threadPool.threadCount",
                POOL_SIZE.toString());

        scheduler = new StdSchedulerFactory(properties).getScheduler();
        scheduler.start();
    }

    /**
     * Encerra e espera os jobs finalizarem
     *
     * @throws SchedulerException
     */
    @After
    public void afterTests() throws SchedulerException {
        scheduler.shutdown(true);
    }

    /**
     * Dipara um job no quartz
     *
     * @param classe
     * @throws SchedulerException
     */
    private void triggerJob(Class<? extends Job> classe)
            throws SchedulerException {
        Integer sequence = jobSequence.getAndIncrement();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dummyTriggerName" + sequence, "group1")
                .startNow().build();

        JobDetail job = JobBuilder.newJob(classe)
                .withIdentity("dummyJobName" + sequence, "group1").build();

        scheduler.scheduleJob(job, trigger);
    }

    /**
     * Testa o aumento do pool
     *
     * @throws SchedulerException
     */
    @Test
    public void increaseThreadPool() throws SchedulerException {
        for (int i = 0; i < 200; i++) {
            triggerJob(DummyJob.class);
        }

        int volta = 1;
        while (!scheduler.getTriggerGroupNames().isEmpty()) {
            List<JobExecutionContext> currentlyExecutingJobs = scheduler
                    .getCurrentlyExecutingJobs();

            if (volta <= 2) {
                Assert.assertEquals(POOL_SIZE.intValue(),
                        currentlyExecutingJobs.size());
            } else {
                Assert.assertEquals(INCREASE_POOL_SIZE.intValue(),
                        currentlyExecutingJobs.size());
                break;
            }

            if (volta == 2) {
                DynamicThreadPool threadPool = ThreadPoolRepository.getInstance().lookup(QUARTZ_SCHEDULER);
                threadPool.setThreadCount(INCREASE_POOL_SIZE);
            }

            waitValidation();

            volta++;
        }
    }

    /**
     * Espera no laco de validacao
     *
     * @throws SchedulerException
     */
    private void waitValidation() throws SchedulerException {
        try {
            Thread.sleep(1500L);
        } catch (InterruptedException e) {
            throw new SchedulerException(e);
        }
    }

    /**
     * Testa a diminuicao do pool
     *
     * @throws SchedulerException
     */
    @Test
    public void decreaseThreadPool() throws SchedulerException {
        for (int i = 0; i < 200; i++) {
            triggerJob(DummyJob.class);
        }

        int volta = 1;
        while (!scheduler.getTriggerGroupNames().isEmpty()) {
            List<JobExecutionContext> currentlyExecutingJobs = scheduler
                    .getCurrentlyExecutingJobs();

            if (volta <= 2) {
                Assert.assertEquals(POOL_SIZE.intValue(),
                        currentlyExecutingJobs.size());
            } else {
                Assert.assertEquals(DECREASE_POOL_SIZE.intValue(),
                        currentlyExecutingJobs.size());
                break;
            }

            if (volta == 2) {
                DynamicThreadPool threadPool = ThreadPoolRepository.getInstance().lookup(QUARTZ_SCHEDULER);
                threadPool.setThreadCount(DECREASE_POOL_SIZE);
            }

            waitValidation();

            volta++;
        }
    }

    /**
     * Testa o consumo todo de uma fila
     *
     * @throws SchedulerException
     */
    @Test
    public void consumeAllQueue() throws SchedulerException {
        for (int i = 0; i < 30; i++) {
            triggerJob(DummyJob.class);
        }

        Assert.assertEquals(POOL_SIZE.intValue(), scheduler
                .getCurrentlyExecutingJobs().size());

        while (!scheduler.getTriggerGroupNames().isEmpty()) {
        }

        Assert.assertEquals(0, scheduler.getCurrentlyExecutingJobs().size());
    }

}
