package com.rxv5.quartz.config;

import java.util.Enumeration;
import java.util.Properties;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class QuartzConfig {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String JOB = "job";

	private SchedulerFactory sf;

	private Scheduler sched;

	private String config = "job.properties";

	private Properties properties;

	@SuppressWarnings("unchecked")
	public void start() {
		try {
			sf = new StdSchedulerFactory();
			sched = sf.getScheduler();
			loadProperties();
			Enumeration<Object> enums = properties.keys();
			while (enums.hasMoreElements()) {
				String key = enums.nextElement() + "";
				if (!key.endsWith(JOB) || !isEnableJob(enable(key))) {
					continue;
				}
				String jobClassName = properties.get(key) + "";
				String jobCronExp = properties.getProperty(cronKey(key)) + "";
				Class<Job> clazz = (Class<Job>) Class.forName(jobClassName);
				JobDetail job = JobBuilder.newJob(clazz).withIdentity(jobClassName, "group_1").build();
				CronTrigger trigger = null;
				trigger = (CronTrigger) TriggerBuilder.newTrigger().withIdentity(jobClassName, "group_1")
						.withSchedule(CronScheduleBuilder.cronSchedule(jobCronExp)).build();
				if (!sched.checkExists(job.getKey())) {
					sched.scheduleJob(job, trigger);
				} else {
					sched.rescheduleJob(trigger.getKey(), trigger);
				}
			}
			sched.start();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private String enable(String key) {
		return key.substring(0, key.lastIndexOf(JOB)) + "enable";
	}

	private String cronKey(String key) {
		return key.substring(0, key.lastIndexOf(JOB)) + "cron";
	}

	private boolean isEnableJob(String enableKey) {
		Object enable = properties.get(enableKey);
		if (enable != null && "false".equalsIgnoreCase((enable + "").trim())) {
			return false;
		}
		return true;
	}

	private void loadProperties() throws Exception {
		properties = PropertiesLoaderUtils.loadAllProperties(config);
		logger.info("------------load Propteries---------------");
		logger.info(properties.toString());
		logger.info("------------------------------------------");
	}

	public boolean stop() {
		try {
			sched.shutdown();
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}
}
