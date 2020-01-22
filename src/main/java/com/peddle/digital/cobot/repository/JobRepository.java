package com.peddle.digital.cobot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.peddle.digital.cobot.model.Job;

/**
 * @author Srinivasa Reddy Challa, Raj Kumar
 *
 */

@Repository
public interface JobRepository extends JpaRepository<Job, Long>,JobRepositoryCustom {
	
	/**
	 * get first one {@code Job} object where RemoteAgent IP is not null
	 * This is used for executing scripts on remote agent. 
	 * @param status
	 * @return {@code Job}
	 */
	Job findFirst1ByStatusAndRemoteAgentIPIsNotNull(String status);
	
	/**
	 * get first one {@code Job} object where RemoteAgent IP is  null
	 * This is used for executing scripts on same machine where cobot
	 * server is running
	 *   
	 * @param status
	 * @return {@code Job}
	 */
	
	Job findFirst1ByStatusAndRemoteAgentIPIsNull(String status);
}


