package com.rxv5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SSOApplication {
	public static void main(String[] args) {
		
		BCryptPasswordEncoder pw = new BCryptPasswordEncoder();
		String a = pw.encode("nalipei-secret");
		System.out.println(pw.matches("nalipei-secret", a)+"___"+a);
		SpringApplication.run(SSOApplication.class, args);
	}
}
