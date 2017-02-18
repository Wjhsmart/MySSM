package com.jh.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jh.bean.User;
import com.jh.service.UserService;

import ch.qos.logback.classic.Logger;

@Controller
@RequestMapping("user")
public class UserController {

	@Resource
	private UserService userService;
	
	private Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);
	
	@ResponseBody
	@RequestMapping(value="all", method=RequestMethod.GET)
	public List<User> queryAll() {
		logger.info("查询所有用户信息");
		return userService.queryAll();
	}
}
