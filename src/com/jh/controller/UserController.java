package com.jh.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jh.bean.User;
import com.jh.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	@Resource
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value="all", method=RequestMethod.GET)
	public List<User> queryAll() {
		return userService.queryAll();
	}
}
