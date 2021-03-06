package share;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ExclusiveGateWayTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();	
	
	/**部署流程定义（从inputStream）*/	
	@Test	
	public void deploymentProcessDefinition_inputStream(){		
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("/diagrams/gateway.bpmn");		
		InputStream inputStreamPng = this.getClass().getResourceAsStream("/diagrams/gateway.png");	
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service			
				.createDeployment()//创建一个部署对象	
				.name("排他网关")//添加部署的名称		
				.addInputStream("gateway.bpmn", inputStreamBpmn)	
				.addInputStream("gateway.png", inputStreamPng)		
				.deploy();//完成部署	
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
	}	
	
	/**启动流程实例*/
	@Test	
	public void startProcessInstance(){	
		//流程定义的key		
		String processDefinitionKey = "gatewayProcess";	
		ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service		
				.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动	
		System.out.println("流程实例ID:"+pi.getId());//流程实例ID   
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());//流程定义ID   
	}		
	
	/**查询当前人的个人任务*/
	@Test	
	public void findMyPersonalTask(){	
		//String assignee = "王小五";		
		//与正在执行的任务管理相关的Service	
		List<Task> list = processEngine.getTaskService()
				.createTaskQuery()//创建任务查询对象		
				/**查询条件（where部分）*/
				//指定个人任务查询，指定办理人	
				//.taskAssignee(assignee)
				//使用流程实例ID查询
				.processInstanceId("112501")
				/**排序*/						
				.orderByTaskCreateTime().asc()//使用创建时间的升序排列			
				/**返回结果集*/
				.list();//返回列表	
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
		String taskId = "112505";	
		//完成任务的同时，设置流程变量，使用流程变量用来指定完成任务后，下一个连线，对应exclusiveGateWay.bpmn文件中${money>1000}	
		Map<String, Object> variables = new HashMap<String, Object>();	
		variables.put("money", 200);	
		processEngine.getTaskService()//与正在执行的任务管理相关的Service		
		.complete(taskId,variables);	
		System.out.println("完成任务：任务ID："+taskId);	
	}
	
	
	/*说明：
	  1).一个排他网关对应一个以上的顺序流
	  2).由排他网关流出的顺序流都有个conditionExpression元素，在内部维护返回boolean类型的决策结果。
	  3).决策网关只会返回一条结果。当流程执行到排他网关时，流程引擎会自动检索网关出口，从上到下检索如果发现第一条决策结果为true或者没有设置条件的(默认为成立)，则流出。
	  4).如果没有任何一个出口符合条件，则抛出异常
	  5).使用流程变量，设置连线的条件，并按照连线的条件执行工作流，如果没有条件符合的条件，则以默认的连线离开。
	 */
	
	//以下为并行网关
	
	/**部署流程定义（从inputStream）*/	
	@Test	
	public void deploymentProcessDefinition_inputStream2(){	
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("/diagrams/并行gateway.bpmn");	
		InputStream inputStreamPng = this.getClass().getResourceAsStream("/diagrams/并行gateway.png");	
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service			
				.createDeployment()//创建一个部署对象		
				.name("并行网关")//添加部署的名称		
				.addInputStream("并行gateway.bpmn", inputStreamBpmn)	
				.addInputStream("并行gateway.png", inputStreamPng)
				.deploy();//完成部署	
				System.out.println("部署ID："+deployment.getId());	
				System.out.println("部署名称："+deployment.getName());
		}	
	
	
	/**启动流程实例*/	
	@Test
	public void startProcessInstance2(){	
		//流程定义的key		
		String processDefinitionKey = "并行网关";	
		//与正在执行的流程实例和执行对象相关的Service	
		ProcessInstance pi = processEngine.getRuntimeService()
				.startProcessInstanceByKey(processDefinitionKey);
		//流程实例ID
		System.out.println("流程实例ID:"+pi.getId()); 
		//流程定义ID
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());  
	}	
	
	/**查询当前人的个人任务*/
	@Test
	public void findMyPersonalTask2(){	
		//String assignee = "商家";		
		List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service	
				.createTaskQuery()//创建任务查询对象				
				/**查询条件（where部分）*/				
				//.taskAssignee(assignee)//指定个人任务查询，指定办理人
				.processInstanceId("125001")//使用流程实例ID查询
				/**排序*/						
				.orderByTaskCreateTime().asc()//使用创建时间的升序排列			
				/**返回结果集*/
				//.singleResult()//返回惟一结果集
				//.count()//返回结果集的数量
				//.listPage(firstResult, maxResults);//分页查询	
				.list();//返回列表		
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
	public void completeMyPersonalTask2(){		
		//任务ID	
		String taskId = "125007";	
		processEngine.getTaskService()//与正在执行的任务管理相关的Service		
		.complete(taskId);
		System.out.println("完成任务：任务ID："+taskId);
	}
	
/*	 1).一个流程中流程实例只有1个，执行对象有多个
	  2).并行网关的功能是基于进入和外出的顺序流的：
	     分支(fork)： 并行后的所有外出顺序流，为每个顺序流都创建一个并发分支。
	     汇聚(join)： 所有到达并行网关，在此等待的进入分支， 直到所有进入顺序流的分支都到达以后， 流程就会通过汇聚网关。
	  3).并行网关的进入和外出都是使用相同节点标识
	  4).如果同一个并行网关有多个进入和多个外出顺序流， 它就同时具有分支和汇聚功能。 这时，网关会先汇聚所有进入的顺序流，然后再切分成多个并行分支。
	  5).并行网关不会解析条件。 即使顺序流中定义了条件，也会被忽略。
	  6).并行网关不需要是“平衡的”（比如， 对应并行网关的进入和外出节点数目不一定相等）*/

}
