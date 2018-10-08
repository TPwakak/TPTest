package activitiTest;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

public class ActivitiTest3 {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();	

	/**
	  * 历史任务查询 
	  */
	@Test
	public void historyTaskList(){    
		List<HistoricTaskInstance> list=processEngine.getHistoryService() // 历史任务Service           
				.createHistoricTaskInstanceQuery() // 创建历史任务实例查询        
				.taskAssignee("java13") // 指定办理人           
				.finished() // 查询已经完成的任务          
				.list();   
		for(HistoricTaskInstance hti:list){   
			System.out.println("任务ID:"+hti.getId());     
			System.out.println("流程实例ID:"+hti.getProcessInstanceId());  
			System.out.println("班里人："+hti.getAssignee());     
			System.out.println("创建时间："+hti.getCreateTime());  
			System.out.println("结束时间："+hti.getEndTime());  
			System.out.println("===========================");    
		}
	}
	
	/*
	 *查询历史任务
	 */	
	@Test	
	public void findHistoryTask(){		
		String processInstanceId = "67501";		
		List<HistoricTaskInstance> list = (List<HistoricTaskInstance>) processEngine.getHistoryService()//与历史数据（历史表）相关的Service		
				.createHistoricTaskInstanceQuery()//创建历史任务实例查询		
				.processInstanceId(processInstanceId)//		
				.orderByHistoricTaskInstanceStartTime().asc()	
				.list();	
		if(list!=null && list.size()>0){	
			for(HistoricTaskInstance hti:list){	
				System.out.println(hti.getId()+"    "+hti.getName()+"    "+hti.getProcessInstanceId()+"   "+hti.getStartTime()+"   "+hti.getEndTime()+"   "+hti.getDurationInMillis());		
				System.out.println("################################");	
			}	
		}
	}

	
	/** 
	 * 查询历史流程实例
	 */
	@Test
	public void getHistoryProcessInstance(){    
		HistoricProcessInstance hpi= processEngine.getHistoryService() // 历史任务Service     
				.createHistoricProcessInstanceQuery() // 创建历史流程实例查询   
				.processInstanceId("67501") // 指定流程实例ID      
				.singleResult();  
		System.out.println("流程实例ID:"+hpi.getId());   
		System.out.println("创建时间："+hpi.getStartTime());  
		System.out.println("结束时间："+hpi.getEndTime());
	}

	/**     
	 * 历史活动查询   
	 */   
	@Test   
	public void historyActInstanceList(){      
		List<HistoricActivityInstance> list=(List<HistoricActivityInstance>) processEngine.getHistoryService() // 历史任务Service    
				.createHistoricActivityInstanceQuery() // 创建历史活动实例查询      
				.processInstanceId("20001") // 指定流程实例id      
				.finished() // 查询已经完成的任务    
				.list();     
		for(HistoricActivityInstance hai:list){    
			System.out.println("任务ID:"+hai.getId());    
			System.out.println("流程实例ID:"+hai.getProcessInstanceId());  
			System.out.println("活动名称："+hai.getActivityName());   
			System.out.println("办理人："+hai.getAssignee());        
			System.out.println("开始时间："+hai.getStartTime());      
			System.out.println("结束时间："+hai.getEndTime());        
			System.out.println("==========================="); 
		} 
	}


	/**
	 * 查询历史活动-->某一次流程的执行一共经历了多少个活动
	 */	
	@Test
	public void findHistoryActiviti(){	
		String processInstanceId = "20001";	
		List<HistoricActivityInstance> list = (List<HistoricActivityInstance>) processEngine.getHistoryService()	
				.createHistoricActivityInstanceQuery()//创建历史活动实例的查询			
				.processInstanceId(processInstanceId)
				.orderByHistoricActivityInstanceStartTime().asc()
				.list();		
		if(list!=null && list.size()>0){	
			for(HistoricActivityInstance hai:list){		
				System.out.println(hai.getId()+"   "+hai.getProcessInstanceId()+"   "+hai.getActivityType()+"  "+hai.getStartTime()+"   "+hai.getEndTime()+"   "+hai.getDurationInMillis());	
				System.out.println("#####################");	
			}		
		}	
	}

	/*
	 * 查询历史流程变量
	 */	
	@Test	
	public void findHistoryProcessVariables(){
		String processInstanceId = "30001";		
		List<HistoricVariableInstance> list = (List<HistoricVariableInstance>) processEngine.getHistoryService()
				.createHistoricVariableInstanceQuery()//创建一个历史的流程变量查询对象	
				.processInstanceId(processInstanceId)
				.list();	
		if(list!=null && list.size()>0){	
			for(HistoricVariableInstance hvi:list){	
				System.out.println(hvi.getId()+"   "+hvi.getProcessInstanceId()+"   "+hvi.getVariableName()+"   "+hvi.getVariableTypeName()+"    "+hvi.getValue());		
				System.out.println("###############################################");	
			}	
		}	
	}





}
