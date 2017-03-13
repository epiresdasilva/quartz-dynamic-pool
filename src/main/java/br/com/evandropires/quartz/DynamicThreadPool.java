package br.com.evandropires.quartz;

/**
 * Created by evandro on 13/03/17.
 */
public interface DynamicThreadPool {

	void doResize(Integer threadPoolSize);

}
