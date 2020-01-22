package com.peddle.digital.cobot.controller;


/**
 * @author Srinivasa Reddy Challa, Raj Kumar
 * java Bean class for representing status of a particular job
 * 
 */

public class Status {
	
	String jobId;
	String status;
	String reason;
	
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

}
