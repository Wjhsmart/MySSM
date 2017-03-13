package com.jh.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

	@Resource
	private TaskService taskService;

	private String processId; // 流程ID
	private String taskId; // 流程任务ID
	private String processKey; // 流程的key

	@RequestMapping(value = "login_page", method = RequestMethod.GET)
	public String loginPage() {
		return "activiti/login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ModelAndView login(User user, HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		Subject subject = SecurityUtils.getSubject(); // 获取到Shiro的subject对象，用于做用户登录操作
		UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), user.getPwd());
		try {
			subject.login(token);
			Session session = subject.getSession();
			session.setAttribute("user", user);
			if (subject.hasRole("admin") || subject.hasRole("teacher")) {
				String identity = "candidateGroup";
				String emailOrRole = null;
				if (subject.hasRole("admin")) {
					emailOrRole = "admin";
				} else if (subject.hasRole("teacher")) {
					emailOrRole = "teacher";
				}

				// 获取任务的三种方式
				// List<Task> tasks = activitiService.getTask(emailOrRole, identity); // 方法一：根据用户的邮箱获取代理人的所有任务
				// List<Task> tasks = activitiService.getTask(emailOrRole, identity); // 方法二：根据用户的邮箱获取候选人的所有任务
				List<Task> tasks = activitiService.getTask(emailOrRole, identity); // 方法三：根据角色的名称获取该角色的所有任务
				if (tasks != null && tasks.size() > 0) {
					String taskName = tasks.get(0).getName();
					String money = activitiService.getTaskReason(emailOrRole, "money", identity);
					String des = activitiService.getTaskReason(emailOrRole, "reason", identity);
					String email = activitiService.getTaskReason(emailOrRole, "email", identity);
					String title = activitiService.getTaskReason(emailOrRole, "title", identity);
					String days = activitiService.getTaskReason(emailOrRole, "days", identity);
					
					mav.addObject("taskName", taskName);
					mav.addObject("reason", des);
					mav.addObject("email", email);
					mav.addObject("title", title);
					mav.addObject("money", money);
					mav.addObject("days", days);
				}
				mav.setViewName("activiti/boss_home");
			} else if (subject.hasRole("customer")) {
				String identity = "candidateGroup";
				String emailOrRole = "customer";
				List<Task> tasks = activitiService.getTask(emailOrRole, identity);
				if (tasks != null && tasks.size() > 0) {
					String taskName = tasks.get(0).getName();
					String des = activitiService.getTaskReason(emailOrRole, "reason", identity);
					String email = activitiService.getTaskReason(emailOrRole, "email", identity);
					String days = activitiService.getTaskReason(emailOrRole, "days", identity);
					
					mav.addObject("taskName", taskName);
					mav.addObject("reason", des);
					mav.addObject("email", email);
					mav.addObject("days", days);
				}
				mav.setViewName("activiti/emp_home");
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
	@RequestMapping(value = "deploy", method = RequestMethod.GET)
	public ControllerResult deploy(String processImgName, String processName) {
		processId = activitiService.deployProcess("activiti_diagrams/" + processImgName + ".bpmn", processName); // 部署流程
		return ControllerResult.getSuccessResult("流程已经成功部署，可以开始流程任务");
	}

	@ResponseBody
	@RequestMapping(value = "del_process", method = RequestMethod.GET)
	public ControllerResult delProcess() {
		activitiService.deleteProcess(processId);
		return ControllerResult.getSuccessResult("您已经成功删除流程");
	}

	@ResponseBody
	@RequestMapping(value = "del_task", method = RequestMethod.GET)
	public ControllerResult delTask() {
		activitiService.deleteTask(taskId, "删除流程任务");
		return ControllerResult.getSuccessResult("您已经成功删除流程任务");
	}

	@ResponseBody
	@RequestMapping(value = "leave", method = RequestMethod.POST)
	public ControllerResult leave(String des, HttpSession session) {
		if (des != null && !des.equals("")) {
			processKey = "leaveProcess";
			User user = (User) session.getAttribute("user");
			taskId = activitiService.startProcess(processKey); // 启动流程
			activitiService.useTask(user.getEmail()); // 指定任务
			String identity = "assignee";
			String emailOrRole = user.getEmail();
			Map<String, Object> variables = new HashMap<String, Object>();

			variables.put("reason", des);
			variables.put("email", user.getEmail());
			activitiService.compeleteTask(emailOrRole, identity, variables); // 执行任务
			// 到了下一个任务， 应该在此处指派任务由谁来处理
			// activitiService.useNextTask("test2@126.com");
			return ControllerResult.getSuccessResult("申请请假成功，请等待老板审核");
		}
		return ControllerResult.getFailResult("申请请假失败，请输入请假信息");
	}

	@ResponseBody
	@RequestMapping(value = "compelete", method = RequestMethod.GET)
	public ControllerResult compelete() {
		activitiService.compeleteTaskAll(); // 执行任务
		return ControllerResult.getSuccessResult("您已经同意了申请");
	}

	@ResponseBody
	@RequestMapping(value = "apply", method = RequestMethod.POST)
	public ControllerResult apply(String des, String money, String title, HttpSession session) {
		if (des != null && !"".equals(des) && money != null && !"".equals(money) && title != null
				&& !"".equals(title)) {
			processKey = "goodsApplyProcess";
			User user = (User) session.getAttribute("user");
			taskId = activitiService.startProcess(processKey); // 启动流程
			activitiService.useTask(user.getEmail()); // 指定任务
			String identity = "assignee";
			String emailOrRole = user.getEmail();
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("money", money);
			variables.put("reason", des);
			variables.put("title", title);
			variables.put("email", user.getEmail());
			activitiService.compeleteTask(emailOrRole, identity, variables); // 执行任务
			return ControllerResult.getSuccessResult("申请购买物品成功，请等待老板审核");
		}
		return ControllerResult.getFailResult("申请申购失败，请输入申购的信息");
	}

	@ResponseBody
	@RequestMapping(value = "leave1", method = RequestMethod.POST)
	public ControllerResult leave1(String des, String days, HttpSession session) {
		if (des != null && !des.equals("") && days != null && !"".equals(days)) {
			String processKey = "leaveProc";
			User user = (User) session.getAttribute("user");
			taskId = activitiService.startProcess(processKey); // 启动流程
			activitiService.useTask(user.getEmail()); // 指定任务
			String identity = "assignee";
			String emailOrRole = user.getEmail();
			Map<String, Object> variables = new HashMap<String, Object>();

			variables.put("reason", des);
			variables.put("days", days);
			variables.put("email", user.getEmail());
			activitiService.compeleteTask(emailOrRole, identity, variables); // 执行任务
			return ControllerResult.getSuccessResult("申请请假成功，请等待老板审核");
		}
		return ControllerResult.getFailResult("申请请假失败，请输入请假信息");
	}

	@ResponseBody
	@RequestMapping(value = "tourism", method = RequestMethod.POST)
	public ControllerResult tourism(String des, String days, HttpSession session) {
		if (des != null && !des.equals("") && days != null && !"".equals(days)) {
			processKey = "tourismProcess";
			User user = (User) session.getAttribute("user");
			taskId = activitiService.startProcess(processKey); // 启动流程
			activitiService.useTask(user.getEmail()); // 指定任务
			String identity = "assignee";
			String emailOrRole = user.getEmail();
			Map<String, Object> variables = new HashMap<String, Object>();

			variables.put("submit", true);
			variables.put("reason", des);
			variables.put("days", days);
			variables.put("email", user.getEmail());
			activitiService.compeleteTask(emailOrRole, identity, variables); // 执行任务
			return ControllerResult.getSuccessResult("申请成功，请等待老板审核");
		}
		return ControllerResult.getFailResult("申请失败，请输入申请信息");
	}

	@ResponseBody
	@RequestMapping(value = "approval/{result}", method = RequestMethod.GET)
	public ControllerResult approval(@PathVariable("result") String result, HttpSession session) {
		User user = (User) session.getAttribute("user");
		String identity = "candidateGroup";
		String emailOrRole = "admin";
		Map<String, Object> variables = new HashMap<String, Object>();

		String res = "";
		if (result.equals("agree")) {
			variables.put("approval", true);
			res = "您已经同意了旅游申请";
		} else if (result.equals("refuse")) {
			variables.put("approval", false);
			res = "您已经拒绝了旅游申请";
		}
		variables.put("email", user.getEmail());
		activitiService.compeleteTask(emailOrRole, identity, variables); // 执行任务
		return ControllerResult.getSuccessResult(res);
	}
	
	@RequestMapping(value = "view_proc", method = RequestMethod.GET)
	public void viewProc(HttpServletResponse resp) {
		activitiService.getViewProcess(processKey, resp);
	}

	@RequestMapping(value = "view_proc_now", method = RequestMethod.GET)
	public void viewProcNow(HttpServletResponse resp) {
		activitiService.getViewProcessNow(processKey, resp);
	}

}
