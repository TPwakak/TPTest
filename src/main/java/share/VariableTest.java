package share;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class VariableTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	
	/*
	 *部署流程定义（从InputStream）
	 */	
	@Test	
	public void deploymentProcessDefinition_inputStream(){	
		// 引擎配置   
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		InputStream inputStreambpmn = this.getClass().getResourceAsStream("/diagrams/请假流程2.bpmn");		
		InputStream inputStreampng = this.getClass().getResourceAsStream("/diagrams/请假流程2.png");		
		//创建一个与流程定义和部署对象相关的Service
		RepositoryService repositoryService = processEngine.getRepositoryService();
		//获取一个部署对象
		DeploymentBuilder deploymentBuilder= repositoryService.createDeployment();
		//添加部署的名称
		deploymentBuilder.name("请假流程2");
		//使用资源文件的名称（要求：与资源文件的名称要一致），和输入流完成部署
		deploymentBuilder.addInputStream("请假流程2.bpmn", inputStreambpmn);					
		deploymentBuilder.addInputStream("请假流程2.png", inputStreampng);	
		//完成部署	
		Deployment deployment = deploymentBuilder.deploy();
		System.out.println("部署ID："+deployment.getId());//	
		System.out.println("部署名称："+deployment.getName());//	
	}


	/*
	 *启动流程实例
	 */	
	@Test	
	public void startProcessInstance(){		
		//流程定义的key	
		String processDefinitionKey = "holloWord2Process";	
		//与正在执行的流程实例和执行对象相关的Service	
		ProcessInstance pi = processEngine.getRuntimeService()			
				.startProcessInstanceByKey(processDefinitionKey);
		//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值		
		System.out.println("流程实例ID:"+pi.getId()); 
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId()); 
	}
	
	/**
	 * 查找任务
	 */
	@Test
	public void getTasks() {
		String processDefinitionId = "holloWord2Process:5:202504";
		TaskService taskService = processEngine.getTaskService();	
		List<Task> list = taskService.createTaskQuery().
				processDefinitionId(processDefinitionId).
				orderByTaskCreateTime().asc().list();
		
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
	
	/*
	 *设置流程变量
	*/	
	@Test	
	public void setVariables(){		
		/**与任务（正在执行）*/		
		TaskService taskService = processEngine.getTaskService();	
		//任务ID		
		String taskId = "205005"; 	
		/**一：设置流程变量，使用基本数据类型*///		
		taskService.setVariableLocal(taskId, "请假天数", 5);//与任务ID绑定//	
		taskService.setVariable(taskId, "请假日期", new Date());	
		taskService.setVariable(taskId, "请假原因", "回家探亲，一起吃个饭");		
		/**二：设置流程变量，使用javabean类型*/		
		/**		
		  * 当一个javabean（实现序列号）放置到流程变量中，要求javabean的属性不能再发生变化		
		  * 如果发生变化，再获取的时候，抛出异常		 
		  * 解决方案：在Person对象中添加：		 
		  * private static final long serialVersionUID = 6757393795687480331L;		 
		  *同时实现Serializable 		
		  	
		Perseron p = new Perseron();	
		p.setId(20);	
		p.setName("翠花");	
		taskService.setVariable(taskId, "人员信息(添加固定版本)", p);		*/	
		System.out.println("设置流程变量成功！");
	}
	
	/*
	 *获取流程变量
	 */
	@Test	
	public void getVariables(){		
		/**与任务（正在执行）*/		
		TaskService taskService = processEngine.getTaskService();		
		//任务ID	
		String taskId = "205005"; 		
		/**一：获取流程变量，使用基本数据类型*/	
		Integer days = (Integer) taskService.getVariable(taskId, "请假天数");
		Date date = (Date) taskService.getVariable(taskId, "请假日期");
		String resean = (String) taskService.getVariable(taskId, "请假原因");
		System.out.println("请假天数："+days);
		System.out.println("请假日期："+date);
		System.out.println("请假原因："+resean);	
		/**二：获取流程变量，使用javabean类型		
		Perseron p = (Perseron)taskService.getVariable(taskId, "人员信息(添加固定版本)");	
	    System.out.println(p.getId()+"        "+p.getName());	*/
	}
	
	/*
	 *查询流程变量的历史表
	 */	
	@Test	
	public void findHistoryProcessVariables(){	
		//创建一个历史service
		HistoryService historyService =  processEngine.getHistoryService();
		//创建一个历史的流程变量查询对象
		HistoricVariableInstanceQuery historicVariableInstanceQuery = historyService.createHistoricVariableInstanceQuery();
		List<HistoricVariableInstance> list = historicVariableInstanceQuery.variableName("请假天数").list();	
		if(list!=null && list.size()>0){	
			for(HistoricVariableInstance hvi:list){		
				System.out.println(hvi.getId()+"   "+hvi.getProcessInstanceId()+"   "+hvi.getVariableName()+"   "+hvi.getVariableTypeName()+"    "+hvi.getValue());		
				System.out.println("###############################################");		
			}		
		}	
	}
}
