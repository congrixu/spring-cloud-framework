package com.rxv5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 这个工程是一个理论工程运行得起来 但可能不可以生效 。主要是学 security基础使用
 * @author congrixu
 * 注意当前类所在的位置。要与调用feign同包或者父包下。否则@EnableFeignClients添加basePackages
 * @EnableFeignClients(basePackages="")
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class AuthzApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthzApplication.class, args);
	}

}
