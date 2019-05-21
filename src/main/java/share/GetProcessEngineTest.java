package share;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

public class GetProcessEngineTest {

	/** 
	  * 创建工作流数据库表
	 */
	@Test    
	public void testCreateTable() {        
		// 引擎配置       
		ProcessEngineConfiguration pec=ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();       
		pec.setJdbcDriver("com.mysql.jdbc.Driver");      
		pec.setJdbcUrl("jdbc:mysql://localhost:3306/test_111?useUnicode=true&characterEncoding=utf8");     
		pec.setJdbcUsername("root");    
		pec.setJdbcPassword("root123");            
		/**         
		  * DB_SCHEMA_UPDATE_FALSE 不能自动创建表，需要表存在        
		  * create-drop 先删除表再创建表        
		  * DB_SCHEMA_UPDATE_TRUE 如何表不存在，自动创建和更新表   
		 */   
		pec.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);                 
		// 获取流程引擎对象   
		ProcessEngine processEngine=pec.buildProcessEngine(); 
		System.out.println(processEngine.getName());
		}
	
	/**
	 * 读取配置文件获取工作流引擎
	 */
	@Test
	public void testCreateTableWithXml(){   
		// 引擎配置   
		ProcessEngineConfiguration pec=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");  
		// 获取流程引擎对象    
		ProcessEngine processEngine=pec.buildProcessEngine();
		System.out.println(processEngine.getName());
	}
	
	/***获取默认引擎对象*/
	@Test
	public void getprocessEngineDefault(){   
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		System.out.println(processEngine.getName());
	}
}
