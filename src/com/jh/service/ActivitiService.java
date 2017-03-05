package com.jh.service;

public interface ActivitiService {

	/**
	 * 第一步：部署流程
	 * @Param processPath 流程图的路径，如：diagrams/test_process.bpmn
	 */
	public void deployProcess(String processPath);
	
	/**
	 * 第二步：启动流程
	 * @param processId 流程的id，流程图中配置好的id，如：testProcess
	 */
	public void startProcess(String processId);
	
	/**
	 * 第三步：指定任务
	 * @param userEmail 指定做任务的人的邮箱
	 */
	public void useTask(String userEmail);
	
	/**
	 * 第四步：执行任务
	 * @param userEmail 执行任务的人的邮箱
	 */
	public void compeleteTask(String userEmail);
}
