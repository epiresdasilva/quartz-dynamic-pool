package br.com.evandropires.quartz;

import br.com.evandropires.quartz.impl.ExecutorServiceThreadPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by evandro on 13/03/17.
 */
public class DynamicThreadPoolRepository {

    private static final Map<String, DynamicThreadPool> INSTANCES = new ConcurrentHashMap<>();

    private static DynamicThreadPoolRepository instance = new DynamicThreadPoolRepository();

    public static DynamicThreadPoolRepository getInstance() {
        return instance;
    }

    public void bind(String schedulerName, DynamicThreadPool threadPool) {
        INSTANCES.put(schedulerName, threadPool);
    }

    public DynamicThreadPool lookup(String schedulerName) {
        return INSTANCES.get(schedulerName);
    }
}
