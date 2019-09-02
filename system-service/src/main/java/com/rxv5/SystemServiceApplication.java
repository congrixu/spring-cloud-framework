package com.rxv5;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@MapperScan("com.rxv5.system.dao")
@SpringBootApplication
//@EnableDiscoveryClient
@EnableEurekaClient
public class SystemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemServiceApplication.class, args);
	}

}
