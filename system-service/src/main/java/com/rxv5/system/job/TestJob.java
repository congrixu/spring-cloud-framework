package com.rxv5.system.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rxv5.system.dao.UserMapper;
import com.rxv5.util.ApplicationUtil;


@SuppressWarnings("unused")
public class TestJob implements Job{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	
	private UserMapper mapper;//使用@Autowired得到不对象
	
	private void init() {
		mapper = ApplicationUtil.getBean("userMapper");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("TestJob execute.............");
		
		init();
		
		//TODO Some things
	}

}
