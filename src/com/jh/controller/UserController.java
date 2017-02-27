package com.jh.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jh.bean.User;
import com.jh.bean.Users;
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
	
	@RequestMapping(value="login_page", method=RequestMethod.GET)
	public String loginPage() {
		return "shiro/login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	public ModelAndView login(Users users, HttpServletRequest req) {
		ModelAndView mav = new ModelAndView("shiro/home");
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(users.getUsername(), users.getPassword());
		try {  
	        subject.login(token);  
	        Session session = subject.getSession();
	        session.setAttribute("username", subject.getPrincipal().toString());
	        String role = "admin";
	        System.out.println("判断当前登录的用户是否有" + role + "的角色：" + subject.hasRole(role));
	        return mav;
	    } catch (AuthorizationException e) { // 没有权限
	    	req.setAttribute("error", "没有权限");
	    } catch (UnknownAccountException e) { // 未知的账户异常
	    	req.setAttribute("error", "没有该账户");
	    } catch (IncorrectCredentialsException e) { // 不正确的凭证异常
	    	req.setAttribute("error", "密码错误");
	    } catch (AuthenticationException e) { // 账号验证失败
	    	req.setAttribute("error", "账号验证失败");
	    } 
		mav.setViewName("shiro/login");
		return mav;
	}
	
	@RequestMapping(value="logout", method=RequestMethod.GET)
	public String logout(HttpServletRequest req) {
		Subject subject = SecurityUtils.getSubject();
		req.setAttribute("error", "您已经成功退出登录");
		subject.logout();
		return "shiro/login";
	}
}
