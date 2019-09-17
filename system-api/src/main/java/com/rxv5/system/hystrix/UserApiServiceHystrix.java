package com.rxv5.system.hystrix;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.rxv5.system.service.UserApiService;
import com.rxv5.system.vo.UserVo;
import com.rxv5.vo.Page;

@Component
public class UserApiServiceHystrix implements UserApiService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<UserVo> find(UserVo search) {
		logger.error("UserApiService.find ERROR");
		return null;
	}

	@Override
	public UserVo get(Integer id) {
		logger.error("UserApiService.get ERROR");
		return null;
	}

	@Override
	public Page<UserVo> query(UserVo search, Integer pageNum, Integer pageSize) {
		logger.error("UserApiService.query ERROR");
		return null;
	}

}
