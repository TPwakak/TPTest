package share;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;

public class ProcessDefinitionTest {
	
	/**
	 * 部署流程定义
	 */
	@Test
	public void testDepoloy(){   
		// 引擎配置   
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 获取部署对象   
		Deployment deployment=processEngine.getRepositoryService() // 部署Service                 
				.createDeployment()  // 创建部署                
				.addClasspathResource("diagrams/helloWOrd.bpmn")  // 从classpath的资源中加载，一次只能加载一个文件                 
				//.addClasspathResource("diagrams/开始活动节点.bpmn") 
				//.addClasspathResource("diagrams/helloWOrd.png")   // 从classpath的资源中加载，一次只能加载一个文件                 
				.name("HelloWorld流程")  // 流程名称                 
				.deploy(); // 完成部署   
		System.out.println("流程部署ID:"+deployment.getId());   
		System.out.println("流程部署Name:"+deployment.getName());
	}
	
	/** 
	  * 部署流程定义使用zip方式
	 */
	@Test
	public void deployWithZip(){    
		InputStream inputStream=this.getClass()  // 获取当前class对象 
				.getClassLoader()   // 获取类加载器                        
				.getResourceAsStream("diagrams/helloWorld.zip"); 
		// 获取指定文件资源流   
		ZipInputStream zipInputStream=new ZipInputStream(inputStream); 
		// 实例化zip输入流对象 
		// 获取部署对象  
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment=processEngine.getRepositoryService() // 部署Service 
				.createDeployment()  // 创建部署    
				.name("HelloWorld流程2")  // 流程名称  
				.addZipInputStream(zipInputStream)  // 添加zip是输入流          
				.deploy(); 
		// 部署    
		System.out.println("流程部署ID:"+deployment.getId());   
		System.out.println("流程部署Name:"+deployment.getName());
	}
	
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
	  * 查询流程定义
	 */         
	@Test          
	public void findProcessDefinition(){ 
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		//与流程定义和部署对象相关的Service 
		RepositoryService repositoryService = processEngine.getRepositoryService();
		//创建流程定义查询对象
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		List<ProcessDefinition> list = processDefinitionQuery        
				/**指定查询条件,where条件*/ 
				//使用部署对象ID查询       
				.deploymentId("200001")  
				/*	使用流程定义的key查询   
				.processDefinitionKey(processDefinitionKey)    
				使用流程定义的名称模糊查询                      
				.processDefinitionNameLike(processDefinitionNameLike)*/                                                    
				//排序                              
				.orderByProcessDefinitionName().desc()//按照流程定义的名称降序排列                  
				/**返回的结果集*/                              
				.list();//返回一个集合列表，封装流程定义                    
		if(list!=null && list.size()>0){             
			for(ProcessDefinition pd:list){          
				System.out.println("流程定义ID:"+pd.getId());
				//流程定义的key+版本+随机生成数 
				System.out.println("流程定义的名称:"+pd.getName());
				//对应helloworld.bpmn文件中的name属性值            
				System.out.println("流程定义的key:"+pd.getKey());
				//对应helloworld.bpmn文件中的id属性值                    
				System.out.println("流程定义的版本:"+pd.getVersion());
				//当流程定义的key值相同的相同下，版本升级，默认1          
				System.out.println("资源名称bpmn文件:"+pd.getResourceName());   
				System.out.println("资源名称png文件:"+pd.getDiagramResourceName());                  
				System.out.println("部署对象ID："+pd.getDeploymentId());            
				System.out.println("#########################################################");         
			}            
		}
	}
	
	
	/*
	 * 删除流程定义
	*/	
	@Test	
	public void deleteProcessDefinition(){	
		//使用部署ID，完成删除	
		String deploymentId = "200001";		
		/**		 
		 * 不带级联的删除		
		 *    只能删除没有启动的流程，如果流程启动，就会抛出异常		 
		*/		
		//processEngine.getRepositoryService()////						
		//.deleteDeployment(deploymentId);				
		/**		
		 * 级联删除		 
		* 	  不管流程是否启动，都能可以删除		
	    */	
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRepositoryService().deleteDeployment(deploymentId, true);	
		System.out.println("删除成功！");
	}
	
}
