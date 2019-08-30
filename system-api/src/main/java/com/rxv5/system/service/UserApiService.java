package com.rxv5.system.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rxv5.system.vo.UserVo;

@FeignClient(name = "system-service")
public interface UserApiService {

	@RequestMapping(value = "/system/user/find")
	public List<UserVo> find(@RequestBody UserVo search);

	@RequestMapping(value = "/system/user/get/{id}")
	public UserVo get(@RequestParam("id") Integer id);
}
