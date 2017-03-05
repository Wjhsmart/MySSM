package com.jh.service;

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

@Service
public class ActivitiServiceImpl implements ActivitiService {
	
	@Resource
	private RepositoryService repositoryService;
	
	@Resource
	private RuntimeService runtimeService;
	
	@Resource
	private TaskService taskService;

	@Override
	public void deployProcess(String processPath) {
		repositoryService.createDeployment().addClasspathResource(processPath).deploy();
	}

	@Override
	public void startProcess(String processId) {
		runtimeService.startProcessInstanceByKey(processId).getId();
	}

	@Override
	public void useTask(String userEmail) {
		List<Task> tasks = taskService.createTaskQuery().list();
		for (Task task : tasks) {
			taskService.setAssignee(task.getId(), userEmail);
		}
	}

	@Override
	public void compeleteTask(String userEmail) {
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(userEmail).list(); // 获取指定用户的任务
		for (Task task : tasks) {
			taskService.complete(task.getId()); // 执行指定编号的任务
		}
	}

}
