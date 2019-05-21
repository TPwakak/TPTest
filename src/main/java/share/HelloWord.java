package share;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class HelloWord {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**部署流程定义*/
	@Test
	public void deploymentProcessDefinition(){
		//创建一个与流程定义和部署对象相关的Service
		RepositoryService repositoryService = processEngine.getRepositoryService();
		//获取一个部署对象
		DeploymentBuilder deploymentBuilder= repositoryService.createDeployment();
		//添加部署的名称
		deploymentBuilder.name("helloworld入门程序");
		//加载资源文件，一次只能加载一个文件
		deploymentBuilder.addClasspathResource("diagrams/helloWOrd.bpmn");
		//deploymentBuilder.addClasspathResource("diagrams/helloWOrd.png");
		//完成部署
		Deployment deployment = deploymentBuilder.deploy();
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
	}
	
	/**启动流程实例*/
	@Test
	public void startProcessInstance(){
		//流程定义的key
		String processDefinitionKey = "开始活动节点";
		//创建正在执行的流程实例和执行对象相关的Service
		
		RuntimeService runtimeService = processEngine.getRuntimeService();
		/*使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值,
		也可通过runtimeService.startProcessInstanceById(processDefinitionId)启动实例，
		processDefinitionId可以通过部署id查询流程定义获取流程定义Id
		这里为了方便显示，使用InstanceByKey启动*/
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey);
		System.out.println("流程实例ID:"+pi.getId());
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());
	}
	
	
	/**查询当前人的个人任务*/
	@Test
	public void findMyPersonalTask(){
		String assignee = "java13";
		//获取与正在执行的任务管理相关的Service
		TaskService taskService = processEngine.getTaskService();
		//创建任务查询对象
		TaskQuery  taskQuery = taskService.createTaskQuery();
		//指定个人任务查询，指定办理人,查询任务列表
		List<Task> list = taskQuery.taskAssignee(assignee).list();
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
				System.out.println("########################################################");
			}
		}
	}
	
	/**完成我的任务*/
	@Test
	public void completeMyPersonalTask(){
		//任务ID
		String taskId = "67505";
		//与正在执行的任务管理相关的Service
		processEngine.getTaskService().complete(taskId);
		System.out.println("完成任务：任务ID："+taskId);
	}
	
}
