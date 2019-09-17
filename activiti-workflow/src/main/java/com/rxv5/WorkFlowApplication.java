package com.rxv5;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 工作流单体工程
 * 
 * @author rxv5
 *
 */
@MapperScan("com.rxv5.workflow.dao")
@SpringBootApplication
@EnableEurekaClient
public class WorkFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkFlowApplication.class, args);
	}

}