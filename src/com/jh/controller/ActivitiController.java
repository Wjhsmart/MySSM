package com.jh.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jh.bean.User;
import com.jh.common.bean.ControllerResult;
import com.jh.service.ActivitiService;

@Controller
@RequestMapping("activiti")
public class ActivitiController {

	@Resource
	private ActivitiService activitiService;

	@RequestMapping(value = "login_page", method = RequestMethod.GET)
	public String loginPage() {
		return "activiti/login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ModelAndView login(User user, HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), user.getPwd());
		try {
			subject.login(token);
			Session session = subject.getSession();
			session.setAttribute("user", user);
			if (subject.hasRole("admin")) {
				mav.setViewName("activiti/emp_home");
			} else if (subject.hasRole("customer")) {
				mav.setViewName("activiti/boss_home");
			} else {
				mav.setViewName("index/index");
			}
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
		mav.setViewName("activiti/login");
		return mav;
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest req) {
		Subject subject = SecurityUtils.getSubject();
		req.setAttribute("error", "您已经成功退出登录");
		subject.logout();
		return "activiti/login";
	}

	@ResponseBody
	@RequestMapping(value = "leave", method = RequestMethod.POST)
	public ControllerResult leave(String des, HttpSession session) {
		if (des != null && !des.equals("")) {
			User user = (User) session.getAttribute("user");
			activitiService.deployProcess("activiti_diagrams/leave_process.bpmn"); // 部署流程
			activitiService.startProcess("leaveProcess"); // 启动流程
			activitiService.useTask(user.getEmail()); // 指定任务
			activitiService.compeleteTask(user.getEmail()); // 执行任务
			return ControllerResult.getSuccessResult("申请请假成功，请等待老板审核");
		}
		return ControllerResult.getFailResult("申请请假失败，请输入请假信息");
	}

	@ResponseBody
	@RequestMapping(value = "check", method = RequestMethod.GET)
	public ControllerResult check(HttpSession session) {
		User user = (User) session.getAttribute("user");
		activitiService.useTask(user.getEmail()); // 指定任务
		activitiService.compeleteTask(user.getEmail()); // 执行任务
		return ControllerResult.getSuccessResult("您已经同意了请假");
	}

}
