package com.jh.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;

public interface ActivitiService {

	/**
	 * 第一步：部署流程
	 * @param processPath 流程图的路径，如：diagrams/test_process.bpmn
	 * @param processName 流程的名字，如：请假流程
	 * @return 返回流程部署的ID
	 */
	public String deployProcess(String processPath, String processName);
	
	/**
	 * 第二步：启动流程
	 * @param processId 流程的id，流程图中配置好的key，如：testProcess
	 * @return 返回流程任务的ID
	 */
	public String startProcess(String processKey);
	
	/**
	 * 第三步：指定任务
	 * @param userEmail 指定做任务的人的邮箱
	 */
	public void useTask(String userEmail);
	
	/**
	 * 第四步：执行任务
	 * @param emailOrRole 执行任务的人的邮箱或角色
	 * @param identity 身份：代理人(assignee),候选人（CandidateUser）,候选组，角色（CandidateGroup）
	 * @param variables 传递给下一个执行任务的人的信息，比如：请假申请信息：我要请假
	 */
	public void compeleteTask(String emailOrRole, String identity, Map<String, Object> variables);
	
	/**
	 * 第五步：指定下一个任务的执行人
	 * @param userEmail 下一个任务执行人的邮箱
	 */
	public void useNextTask(String userEmail);
	
	/**
	 * 第六步：管理员审核任务,部分什么身份，直接执行任务
	 */
	public void compeleteTaskAll();
	
	/**
	 * 第七步：删除流程任务
	 * @param taskId 任务的ID
	 * @param taskDes 删除流程任务的描述，比如：删除任务
	 */
	public void deleteTask(String taskId, String taskDes);
	
	/**
	 * 第八步：删除部署流程
	 * @param processId 流程的ID
	 */
	public void deleteProcess(String processId);
	
	/**
	 * 根据指定的邮箱获取指定身份的人的所有任务，
	 * 有代理人(assignee),候选人（CandidateUser）,候选组，角色（CandidateGroup）,
	 * 默认是assignee代理人
	 * @param emailOrRole 代理人的邮箱或者角色
	 * @param identity 身份：代理人(assignee),候选人（CandidateUser）,候选组，角色（CandidateGroup）
	 * @return 返回所有该用户的所有任务
	 */
	public List<Task> getTask(String emailOrRole, String identity);
	
	
	/**
	 * 获取任务的信息，比如是请假信息
	 * @param emailOrRole 代理人的邮箱或者角色
	 * @param identity 身份：代理人(assignee),候选人（CandidateUser）,候选组，角色（CandidateGroup）
	 * @param desKey 信息的key值
	 * @return
	 */
	public String getTaskReason(String emailOrRole, String reason, String identity);
	
	/**
	 * 获取流程图
	 * @param processKey 流程key
	 * @param resp Http响应
	 */
	public void getViewProcess(String processKey, HttpServletResponse resp);
	
	/**
	 * 获取流程图，并且高亮显示当前任务进度
	 * @param processKey 流程的key
	 * @param resp Http响应
	 */
	public void getViewProcessNow(String processKey, HttpServletResponse resp);
}
