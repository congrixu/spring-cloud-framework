package com.rxv5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 注意当前类所在的位置。要与调用feign同包或者父包下。否则@EnableFeignClients添加basePackages
 * @EnableFeignClients(basePackages="")
 * @author congrixu
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class AdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}
}
