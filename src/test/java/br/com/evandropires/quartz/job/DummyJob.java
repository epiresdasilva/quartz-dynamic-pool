package br.com.evandropires.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DummyJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			Thread.sleep(200l);
		} catch (InterruptedException e) {
			throw new JobExecutionException(e);
		}
	}
}