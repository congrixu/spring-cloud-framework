package com.rxv5.system.config;

import org.springframework.context.annotation.Bean;

import com.rxv5.http.HttpClientConfig;
import com.rxv5.http.HttpClientUtil;

public class PluginInitConfig {

	@Bean
	public void initHttpClient() {
		HttpClientConfig config = HttpClientConfig.init();
		HttpClientUtil.init(config);
	}

}
