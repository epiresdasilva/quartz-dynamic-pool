package br.com.evandropires.quartz.impl;

import br.com.evandropires.quartz.ThreadPoolRepository;
import org.quartz.SchedulerConfigException;
import org.quartz.spi.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by evandro on 13/03/17.
 */
public class DynamicThreadPool implements ThreadPool {

    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private int threadCount;
    private String instanceId;
    private String instanceName;

    @Override
    public boolean runInThread(Runnable runnable) {
        if (executor == null) {
            return false;
        }
        executor.submit(runnable);
        return true;
    }

    @Override
    public int blockForAvailableThreads() {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
        return threadPoolExecutor.getCorePoolSize() - threadPoolExecutor.getActiveCount();
    }

    @Override
    public void initialize() throws SchedulerConfigException {
        if (threadCount < 0) {
            throw new SchedulerConfigException("You can't define a pool less then 0.");
        }
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
        threadPoolExecutor.setCorePoolSize(threadCount);
    }

    @Override
    public void shutdown(boolean b) {
        if (b) {
            executor.shutdown();
        } else {
            executor.shutdownNow();
        }
    }

    @Override
    public int getPoolSize() {
        return getThreadCount();
    }

    @Override
    public void setInstanceId(String s) {
        this.instanceId = s;
    }

    @Override
    public void setInstanceName(String s) {
        this.instanceName = s;
        ThreadPoolRepository.getInstance().bind(this.instanceName, this);
    }

}
