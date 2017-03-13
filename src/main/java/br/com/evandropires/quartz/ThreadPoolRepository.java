package br.com.evandropires.quartz;

import br.com.evandropires.quartz.impl.DynamicThreadPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by evandro on 13/03/17.
 */
public class ThreadPoolRepository {

    private static final Map<String, DynamicThreadPool> INSTANCES = new ConcurrentHashMap<>();

    private static ThreadPoolRepository instance = new ThreadPoolRepository();

    public static ThreadPoolRepository getInstance() {
        return instance;
    }

    public void bind(String schedulerName, DynamicThreadPool threadPool) {
        INSTANCES.put(schedulerName, threadPool);
    }

    public DynamicThreadPool lookup(String schedulerName) {
        return INSTANCES.get(schedulerName);
    }
}
