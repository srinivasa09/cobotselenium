package com.peddle.digital.cobot.Schedulers;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.peddle.digital.cobot.Service.DispatcherService;
import com.peddle.digital.cobot.Util.JobUtil;
import com.peddle.digital.cobot.constants.STATUS;
import com.peddle.digital.cobot.model.Job;
import com.peddle.digital.cobot.repository.JobRepository;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 * Scheduler to pick up new jobs for execution by remote agent at certain interval 
 * from Database and process them 
 *
 */

@Component
@EnableAsync
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ProcesRemoteRecords {
	final static Logger logger = Logger.getLogger(ProcesRemoteRecords.class);
    @Autowired
    DispatcherService dispatcherService;
    
    @Autowired
    JobRepository jobRepository;
    
    @Value("${scripts.dir}")
	String scriptsRootDir;
    
    @Value("${agent.port}")
	String agentSystemPort;
    
    @Value("${agent.scriptUploadUrl}")
	String agentScriptUploadUrl;
    
    @Value("${cobot.status.callback.url}")
	String cobotStatusCallbackURL;
    
	@Async
	@Scheduled(fixedRateString ="${remoteJobInterval}", initialDelay=1000)
    public void scheduleFixedRateTaskAsync() throws Exception {
		try {
			Job job = jobRepository.findFirst1ByStatusAndRemoteAgentIPIsNotNull(STATUS.Submitted.toString());
			if(job!=null)
			{
				try {
					logger.info("started job "+job.getId());
					jobRepository.updateStatus(STATUS.Inprocess.toString(),job.getId());
					
					String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
					dispatcherService.processJob(job.getContent(), appJobID,job,job.getRemoteAgentIP());
					logger.info("job "+job.getId()+" is successfully completed");
					
				}catch (Exception e) {
					logger.error(e);
					
				}
				
			}
		}catch (Exception e) {
			logger.error(e);
		}
    }
}
