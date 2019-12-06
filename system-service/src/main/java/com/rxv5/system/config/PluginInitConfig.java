package com.rxv5.system.config;

import org.springframework.context.annotation.Bean;

import com.rxv5.http.HttpClientConfig;
import com.rxv5.http.HttpClientUtil;
import com.rxv5.quartz.config.QuartzConfig;

public class PluginInitConfig {

	@Bean
	public void initHttpClient() {
		HttpClientConfig config = HttpClientConfig.init();
		HttpClientUtil.init(config);
	}
	
	@Bean
	public void initQuartz() {
		QuartzConfig qc = new QuartzConfig();
		qc.start();
	}

}
