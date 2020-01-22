package com.peddle.digital.cobot.Schedulers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Scheduler to pick up Failed jobs at certain interval from Database and process them 
 *
 */

@Component
@EnableAsync
public class ProcessFailedRecords {
	final static Logger logger = Logger.getLogger(ProcessFailedRecords.class);
    @Autowired
    DispatcherService dispatcherService;
    
    @Autowired
    JobRepository jobRepository;
	
	@Async
	@Scheduled(fixedRateString ="${reTryJobInterval}", initialDelay=1000)
    public void scheduleFixedRateTaskAsync() throws Exception {
		try {
			Job job = jobRepository.findFirst1ByStatusAndRemoteAgentIPIsNull(STATUS.ExecutionTimedout.toString());
			if(job!=null)
			{
				try {
					jobRepository.updateStatus(STATUS.Inprocess.toString(),job.getId());
					String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
					logger.info("started job "+job.getId());
					dispatcherService.processJob(job.getContent(), appJobID,job,null);
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
