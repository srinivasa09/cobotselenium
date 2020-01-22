package com.peddle.digital.cobot.Service;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.peddle.digital.cobot.Util.RemoteAgentUtils;
import com.peddle.digital.cobot.Util.ScriptUtil;
import com.peddle.digital.cobot.model.Job;
import com.peddle.digital.cobot.repository.JobRepository;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 * Service that dispatches the  service requests to 
 * different target systems for execution
 *
 */

@Component
public class DispatcherService {

	final static Logger logger = Logger.getLogger(DispatcherService.class);

	@Autowired
	JobRepository jobRepository;

	@Value("${cobot.status.callback.url}")
	String cobotStatusCallbackURL;

	@Value("${scripts.dir}")
	String scriptsRootDir;

	@Value("${testcases.root}")
	String testcasesRoot;
	
    @Value("${agent.port}")
	String agentSystemPort;
    
    @Value("${agent.scriptUploadUrl}")
	String agentScriptUploadUrl;

	@Autowired
	private Environment env;

	/**
	 * Execute the submitted {@code Job} which contains the script details
	 * that needs to be executed
	 * @param string 
	 * @param data: data for the script to be executed
	 * @param appJobID: Application JOB Id  which script uses to call back Cobot API
	 *                  to update the status of the executed script
	 * @param job: {@code Job} which contains all the details of the script to be executed
	 * @throws Exception
	 */

	public void processJob(String data, String appJobID, Job job, String remoteIP) throws Exception {

		String statusCallBackUrl = cobotStatusCallbackURL;

		if(job.getScriptFileName() != null)
		{

			//WebDriverManager.firefoxdriver().setup();
			WebDriverManager.chromedriver().setup();

			String executeMode = env.getProperty("execute.mode");
			logger.info("Execution Mode "+ executeMode);

			String scriptsDir = scriptsRootDir;
			String scriptRootDir = testcasesRoot;

			ScriptUtil.transformScriptFile(job.getScriptFileName(), scriptsDir, scriptRootDir);
			ScriptUtil.compileScriptFile(job.getScriptFileName(), scriptRootDir,executeMode);
			//ScriptUtil.runScriptFile(job.getScriptFileName(), content, scriptRootDir,statusCallBackUrl,appJobID);
			if(remoteIP == null)
			{
				ScriptUtil.loadAndRunClass(job.getScriptFileName(), data, scriptRootDir,statusCallBackUrl,appJobID);
			}
			else
			{
				String fileName = job.getScriptFileName().replace(".java", ".class");
                RemoteAgentUtils.executeRemoteJob(testcasesRoot, agentSystemPort, agentScriptUploadUrl, 
                		statusCallBackUrl, appJobID, data, fileName, remoteIP);
			}
		}
	}
}
