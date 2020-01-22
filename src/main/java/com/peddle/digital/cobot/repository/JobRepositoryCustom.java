package com.peddle.digital.cobot.repository;

import com.peddle.digital.cobot.model.Job;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 *
 */

public interface JobRepositoryCustom {
	
	/**
	 * update the status of the {@code Job} with given  id
	 * @param status: status of the job execution 
	 * @param id: id of the {@code Job}
	 * @return updated status
	 */
	int updateStatus( String status,  Long id);
	
	/**
	 * update the status & reason for the status of the {@code Job} with given  id
	 * @param status: status of the job execution  
	 * @param reason : reason for this status 
	 * @param id: id of the {@code Job}
	 * @return updated status
	 */

	int updateStatusAndReason(String status, String reason, Long id);
}
