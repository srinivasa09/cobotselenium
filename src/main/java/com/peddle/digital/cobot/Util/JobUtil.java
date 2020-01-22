package com.peddle.digital.cobot.Util;

import org.apache.log4j.Logger;

import com.peddle.digital.cobot.constants.STATUS;
import com.peddle.digital.cobot.controller.JobResponse;
import com.peddle.digital.cobot.model.Job;

/**
 * 
 * @authors Srinivasa Reddy Challa, Raj Kumar
 * 
 * For each Script JOB submitted to server we maintain two ID's, one is 
 * DB ID an other is Application Job ID derived from DB ID. This class
 * Handles the extraction of DB ID from JobID or vice versa.
 * 
 * Also this class Creates  {@code JobResponse} from the {@code Job} used by  
 * Rest API
 * 
 */
public class JobUtil {
	final static Logger logger = Logger.getLogger(JobUtil.class);
	public static final String JOB_PREFIX="JOB-";
	
	/**
	 * This creates {@code JobResponse} from the {@code Job} 
	 * @param Job: Job object constructed from DB
	 * @param appJobId : Application Job ID create from {@link Job} id property
	 * @return  {@code JobResponse}
	 */
	public static JobResponse updateResponse(Job Job,String appJobId)
	{
		JobResponse jobResponse = new JobResponse();
		if(Job == null)
		{
			jobResponse.setJobid(appJobId);
			jobResponse.setJobStatus(STATUS.UNKNOWN.toString());
			jobResponse.setJobStatusCode(STATUS.UNKNOWN.getID());
		}
		
		jobResponse.setJobid(JOB_PREFIX+Job.getId());
		jobResponse.setJobStatus(Job.getStatus().toString());
		jobResponse.setJobStatusCode(Job.getJobStatusCode());
		jobResponse.setJobUpdatedAt(Job.getUpdatedAt());
		
		return jobResponse;
	}
	
	/**
	 * create Application Job ID from {@link Job} id property
	 * @param dbJobId: {@link Job} id property
	 * @return Application Job ID
	 */
	
	public static String getAppJobIDFromDBJobID(Long dbJobId)
	{
		return JOB_PREFIX+dbJobId;
	}
	
	/**
	 * Extract {@link Job} id property from Application Job ID 
	 * @param appJobId : Application Job Id
	 * @return {@link Job} id property
	 */
	
	public static Long getDBJobIDFromAppJobID(String appJobId)
	{
		if( appJobId != null)
		{
			String arr[] =appJobId.split("-");
			if(arr.length!=2)
			{
				logger.error("invalid appJobId" +appJobId );
				return -1L;
			}
			return Long.parseLong(arr[1]);
		}
		else
		{
			logger.error("invalid appJobId" +appJobId );
			return -1L;
		}
	}
}
