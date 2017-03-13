package com.jh.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
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
	public String deployProcess(String processPath, String processName) {
		Deployment deployment = repositoryService.createDeployment().name(processName).addClasspathResource(processPath).deploy();
		return deployment.getId();
	}

	@Override
	public String startProcess(String processKey) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(processKey);
		return pi.getId();
	}

	@Override
	public void useTask(String userEmail) {
		List<Task> tasks = taskService.createTaskQuery().list();
		for (Task task : tasks) {
			taskService.setAssignee(task.getId(), userEmail);
		}
	}

	@Override
	public void compeleteTask(String emailOrRole, String identity, Map<String, Object> variables) {
		if (identity.equals("candidateUser")) {
			taskService.complete(taskService.createTaskQuery().taskCandidateUser(emailOrRole).list().get(0).getId(),
					variables);
		} else if (identity.equals("candidateGroup")) {
			taskService.complete(taskService.createTaskQuery().taskCandidateGroup(emailOrRole).list().get(0).getId(),
					variables);
		} else if (identity.equals("assignee")) {
			taskService.complete(taskService.createTaskQuery().taskAssignee(emailOrRole).list().get(0).getId(),
					variables);
		}
	}

	@Override
	public void useNextTask(String userEmail) {
		taskService.setAssignee(taskService.createTaskQuery().list().get(0).getId(), userEmail);
	}

	@Override
	public void compeleteTaskAll() {
		taskService.complete(taskService.createTaskQuery().list().get(0).getId());
	}

	@Override
	public List<Task> getTask(String emailOrRole, String identity) {
		if (identity.equals("candidateUser")) {
			return taskService.createTaskQuery().taskCandidateUser(emailOrRole).list();
		} else if (identity.equals("candidateGroup")) {
			return taskService.createTaskQuery().taskCandidateGroup(emailOrRole).list();
		}
		return taskService.createTaskQuery().taskAssignee(emailOrRole).list();

	}

	@Override
	public String getTaskReason(String emailOrRole, String desKey, String identity) {
		Task task = null;
		if (identity.equals("candidateUser")) {
			task = taskService.createTaskQuery().taskCandidateUser(emailOrRole).list().get(0);
		} else if (identity.equals("candidateGroup")) {
			task = taskService.createTaskQuery().taskCandidateGroup(emailOrRole).list().get(0);
		} else if (identity.equals("assignee")) {
			task = taskService.createTaskQuery().taskAssignee(emailOrRole).list().get(0);
		}
		String desValue = (String) taskService.getVariable(task.getId(), desKey);
		return desValue;
	}

	@Override
	public void deleteTask(String taskId, String reason) {
		runtimeService.deleteProcessInstance(taskId, reason);
	}

	@Override
	public void deleteProcess(String processId) {
		repositoryService.deleteDeployment(processId);

	}

	@Override
	public void getViewProcess(String processKey, HttpServletResponse resp) {
		try {
			processKey = runtimeService.createProcessInstanceQuery().list().get(0).getProcessDefinitionKey();
			// 获取到所有正在运行的流程实例
			List<ProcessInstance> processInstantces = runtimeService.createProcessInstanceQuery().processDefinitionKey(processKey).list();
			String processId = processInstantces.get(0).getDeploymentId();
			// 根据流程id获取到所有流程的名字，但其实只有一个
			List<String> names = repositoryService.getDeploymentResourceNames(processId);
			String imageName = null;
			for (String name : names) {
				if (name.indexOf(".png") >= 0) {
					imageName = name;
				}
			}
			// 将图片输入到输入流
			InputStream in = repositoryService.getResourceAsStream(processId, imageName);
			// 获取到一个输出流
			OutputStream out = resp.getOutputStream();
			byte[] b = new byte[1024];
			for (int len = -1; (len = in.read(b)) != -1;) {
				out.write(b, 0, len); // 输出图片
			}
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void getViewProcessNow(String processKey, HttpServletResponse resp) {
		try {
			processKey = runtimeService.createProcessInstanceQuery().list().get(0).getProcessDefinitionKey();
			// 获取到所有的流程实例
			List<ProcessInstance> processInstantces = runtimeService.createProcessInstanceQuery().processDefinitionKey(processKey).list();
	        //得到流程执行对象
	        List<Execution> executions = runtimeService.createExecutionQuery()
	                .processInstanceId(processInstantces.get(0).getId()).list();
	        //得到正在执行的Activity的Id
	        List<String> activityIds = new ArrayList<String>();
	        for (Execution exe : executions) {
	            List<String> ids = runtimeService.getActiveActivityIds(exe.getId()); // 获取正在执行的任务
	            activityIds.addAll(ids);
	        }
	        InputStream in = new DefaultProcessDiagramGenerator().generateDiagram(repositoryService.getBpmnModel(processInstantces.get(0).getProcessDefinitionId()), "png",
	                activityIds);
	        OutputStream out = resp.getOutputStream();
	        byte[] b = new byte[1024];
	        for (int len = -1; (len= in.read(b))!=-1;) {
	            out.write(b, 0, len);
	        }
	        out.close();
	        in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
