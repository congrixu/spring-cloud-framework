package com.rxv5.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

	@RequestMapping("/main")
	public String main() {
		return "/main";
	}
}
