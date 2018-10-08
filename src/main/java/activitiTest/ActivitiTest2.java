package activitiTest;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import domain.Perseron;

public class ActivitiTest2 {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();	
	
	/*
	 *部署流程定义（从InputStream）
	 */	
	@Test	
	public void deploymentProcessDefinition_inputStream(){	
		InputStream inputStreambpmn = this.getClass().getResourceAsStream("/diagrams/请假流程2.bpmn");		
		InputStream inputStreampng = this.getClass().getResourceAsStream("/diagrams/请假流程2.png");		
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service					
				.createDeployment()//创建一个部署对象			
				.name("流程定义")//添加部署的名称			
				.addInputStream("请假流程2.bpmn", inputStreambpmn)//使用资源文件的名称（要求：与资源文件的名称要一致），和输入流完成部署					
				.addInputStream("请假流程2.png", inputStreampng)//使用资源文件的名称（要求：与资源文件的名称要一致），和输入流完成部署				
				.deploy();//完成部署	
		System.out.println("部署ID："+deployment.getId());//	
		System.out.println("部署名称："+deployment.getName());//	
	}
	
	/**输入流加载 资源文件的3种方式
  		1.InputStream inputStreambpmn = this.getClass().getClassLoader().getResourceAsStream("processVariables.bpmn");
 		从classpath根目录下加载指定名称的文件
  		2.InputStream inputStreambpmn = this.getClass().getResourceAsStream("processVariables.bpmn");
 		从当前包下加载指定名称的文件
  		3.InputStream inputStreambpmn = this.getClass().getResourceAsStream("/processVariables.bpmn");
  		从classpath根目录下加载指定名称的文件
	 * 
	 * */

	/*
	 *启动流程实例
	 */	
	@Test	
	public void startProcessInstance(){		
		//流程定义的key	
		String processDefinitionKey = "holloWord2Process";		
		ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service				
				.startProcessInstanceByKey(processDefinitionKey);
		//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动		
		System.out.println("流程实例ID:"+pi.getId()); //流程实例ID  
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());//流程定义ID  
		}


	/*
	 *设置流程变量
	*/	
	@Test	
	public void setVariables(){		
		/**与任务（正在执行）*/		
		TaskService taskService = processEngine.getTaskService();	
		//任务ID		
		String taskId = "30005"; //act_ru_task表里id		
		/**一：设置流程变量，使用基本数据类型*///		
		//taskService.setVariableLocal(taskId, "请假天数", 5);//与任务ID绑定//	
		//taskService.setVariable(taskId, "请假日期", new Date());	
		//taskService.setVariable(taskId, "请假原因", "回家探亲，一起吃个饭");		
		/**二：设置流程变量，使用javabean类型*/		
		/**		
		  * 当一个javabean（实现序列号）放置到流程变量中，要求javabean的属性不能再发生变化		
		  * 如果发生变化，再获取的时候，抛出异常		 
		  * 解决方案：在Person对象中添加：		 
		  * private static final long serialVersionUID = 6757393795687480331L;		 
		  *同时实现Serializable 		
		  */	
		Perseron p = new Perseron();	
		p.setId(20);	
		p.setName("翠花");	
		taskService.setVariable(taskId, "人员信息(添加固定版本)", p);			
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
		String taskId = "30005";  //act_ru_task表里id		
		/**一：获取流程变量，使用基本数据类型*/
		Integer days = (Integer) taskService.getVariable(taskId, "请假天数");
		Date date = (Date) taskService.getVariable(taskId, "请假日期");
		String resean = (String) taskService.getVariable(taskId, "请假原因");
		System.out.println("请假天数："+days);
		System.out.println("请假日期："+date);
		System.out.println("请假原因："+resean);		
		/**二：获取流程变量，使用javabean类型*/		
		//Perseron p = (Perseron)taskService.getVariable(taskId, "人员信息(添加固定版本)");	
		//System.out.println(p.getId()+"        "+p.getName());	
	}

	
	/*
	 *模拟设置和获取流程变量的场景
	 */	
	public void setAndGetVariables(){		
		/**与流程实例，执行对象（正在执行）*/		
		RuntimeService runtimeService = processEngine.getRuntimeService();	
		/**与任务（正在执行）*/		
		TaskService taskService = processEngine.getTaskService();	
		/**设置流程变量*/
		//		runtimeService.setVariable(executionId, variableName, value)//表示使用执行对象ID，和流程变量的名称，设置流程变量的值（一次只能设置一个值）
		//		runtimeService.setVariables(executionId, variables)//表示使用执行对象ID，和Map集合设置流程变量，map集合的key就是流程变量的名称，map集合的value就是流程变量的值（一次设置多个值）		
		
		//		taskService.setVariable(taskId, variableName, value)//表示使用任务ID，和流程变量的名称，设置流程变量的值（一次只能设置一个值）
		//		taskService.setVariables(taskId, variables)//表示使用任务ID，和Map集合设置流程变量，map集合的key就是流程变量的名称，map集合的value就是流程变量的值（一次设置多个值）	
		
		//		runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);//启动流程实例的同时，可以设置流程变量，用Map集合
		//		taskService.complete(taskId, variables)//完成任务的同时，设置流程变量，用Map集合				
		
		/**获取流程变量*/
		//		runtimeService.getVariable(executionId, variableName);//使用执行对象ID和流程变量的名称，获取流程变量的值
		//		runtimeService.getVariables(executionId);//使用执行对象ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
		//		runtimeService.getVariables(executionId, variableNames);//使用执行对象ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的流程变量的值，值存放到Map集合中		
		
		//		taskService.getVariable(taskId, variableName);//使用任务ID和流程变量的名称，获取流程变量的值
		//		taskService.getVariables(taskId);//使用任务ID，获取所有的流程变量，将流程变量放置到Map集合中，map集合的key就是流程变量的名称，map集合的value就是流程变量的值
		//		taskService.getVariables(taskId, variableNames);//使用任务ID，获取流程变量的值，通过设置流程变量的名称存放到集合中，获取指定流程变量名称的流程变量的值，值存放到Map集合中		
	}

	/*说明：
	  1）.RuntimeService对象可以设置流程变量和获取流程变量
	  2）.TaskService对象可以设置流程变量和获取流程变量
	  3）.流程实例启动的时候可以设置流程变量
	  4）.任务办理完成的时候可以设置流程变量
	  5）.流程变量可以通过名称/值的形式设置单个流程变量
	  6）.流程变量可以通过Map集合，同时设置多个流程变量
	    Map集合的key表示流程变量的名称
	    Map集合的value表示流程变量的值*/


	/*
	 *查询流程变量的历史表
	 */	
	@Test	
	public void findHistoryProcessVariables(){		
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
				.createHistoricVariableInstanceQuery()//创建一个历史的流程变量查询对象	
				.variableName("请假天数")						
				.list();	
		if(list!=null && list.size()>0){	
			for(HistoricVariableInstance hvi:list){		
				System.out.println(hvi.getId()+"   "+hvi.getProcessInstanceId()+"   "+hvi.getVariableName()+"   "+hvi.getVariableTypeName()+"    "+hvi.getValue());		
				System.out.println("###############################################");		
			}		
		}	
	}

	/*说明：
	  1）历史的流程变量查询，指定流程变量的名称，查询act_hi_varinst表（也可以针对，流程实例ID，执行对象ID，任务ID查询）*/
}

/*总结
    1：流程变量
    在流程执行或者任务执行的过程中，用于设置和获取变量，使用流程变量在流程传递的过程中传递业务参数。
  对应的表：
  act_ru_variable：正在执行的流程变量表
  act_hi_varinst：流程变量历史表
    2：扩展知识：setVariable和setVariableLocal的区别
  setVariable：设置流程变量的时候，流程变量名称相同的时候，后一次的值替换前一次的值，而且可以看到TASK_ID的字段不会存放任务ID的值
  setVariableLocal：
  1：设置流程变量的时候，针对当前活动的节点设置流程变量，如果一个流程中存在2个活动节点，对每个活动节点都设置流程变量，即使流程变量的名称相同，后一次的版本的值也不会替换前一次版本的值，它会使用不同的任务ID作为标识，存放2个流程变量值，而且可以看到TASK_ID的字段会存放任务ID的值
例如act_hi_varinst 表的数据：不同的任务节点，即使流程变量名称相同，存放的值也是不同的。
    2：还有，使用setVariableLocal说明流程变量绑定了当前的任务，当流程继续执行时，下个任务获取不到这个流程变量（因为正在执行的流程变量中没有这个数据），所有查询正在执行的任务时不能查询到我们需要的数据，此时需要查询历史的流程变量。
*/
