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
 * Scheduler to pick up new jobs at certain interval from Database and process them 
 *
 */

@Component
@EnableAsync
public class ProcessNewRecords {

	final static Logger logger = Logger.getLogger(ProcessNewRecords.class);

	@Autowired
	DispatcherService dispatcherService;

	@Autowired
	JobRepository jobRepository;

	@Async
	@Scheduled(fixedRateString ="${newJobInterval}", initialDelay=1000)
	public void scheduleFixedRateTaskAsync()  {
		Job job= null;
		try {
			job = jobRepository.findFirst1ByStatusAndRemoteAgentIPIsNull(STATUS.Submitted.toString());
			if(job!=null)
			{
				logger.info("started job "+job.getId());
				jobRepository.updateStatus(STATUS.Inprocess.toString(),job.getId());
				String appJobID = JobUtil.getAppJobIDFromDBJobID(job.getId());
				dispatcherService.processJob(job.getContent(), appJobID,job, null);
				logger.info("job "+job.getId()+" is successfully completed");
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			try
			{
				if(job != null) {
					jobRepository.updateStatusAndReason(STATUS.Failed.toString(), e.getMessage(), job.getId());
				}
			}
			catch (Exception ex)
			{
				logger.error(ex.getMessage(),ex);
			}
		}
	}
}
