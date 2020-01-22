package com.peddle.digital.cobot.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.Modifying;

import com.peddle.digital.cobot.model.Job;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 *
 */
public class JobRepositoryImpl implements JobRepositoryCustom {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * update the status of the {@code Job} with given  id
	 * @param status: status of the job execution
	 * @param id: id of the {@code Job}
	 * @return updated status
	 */
	@Override
	@Transactional
	public int updateStatus(String status, Long id) {

		Job job = entityManager.find(Job.class, id);
		job.setStatus(status);

		return 0;
	}

	/**
	 * update the status & reason for the status of the {@code Job} with given  id
	 * @param status: status of the job execution 
	 * @param reason : reason for this status 
	 * @param id: id of the {@code Job}
	 * @return updated status
	 */

	@Override
	@Transactional
	public int updateStatusAndReason(String status, String reason, Long id) {

		Job job = entityManager.find(Job.class, id);
		job.setStatus(status);
		if(reason!=null)
		{
			job.setStatusCause(reason); 
		}
		return 0;
	}
}
