package com.peddle.digital.cobot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 * Model class for Storing submitted script job for execution
 *
 */

@Entity
@Table(name = "jobs")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Job {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 
    private String status;
    
    @Column(columnDefinition="VARCHAR(3000)")
    private String statusCause;
    
	private int jobStatusCode;
    
	private String remoteAgentIP;
    
    private String scriptFileName;

   
    @Column(columnDefinition="VARCHAR(500)")
    private String content;
    

    @Column(columnDefinition="VARCHAR(500)")
    private String httpparams;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public String getHttpparams() {
		return httpparams;
	}

	public void setHttpparams(String httpparams) {
		this.httpparams = httpparams;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public int getJobStatusCode() {
		return jobStatusCode;
	}

	public void setJobStatusCode(int jobStatusCode) {
		this.jobStatusCode = jobStatusCode;
	}
	
	public String getRemoteAgentIP() {
		return remoteAgentIP;
	}

	public void setRemoteAgentIP(String remoteAgentIP) {
		this.remoteAgentIP = remoteAgentIP;
	}

	public String getScriptFileName() {
		return scriptFileName;
	}

	public void setScriptFileName(String scriptFileName) {
		this.scriptFileName = scriptFileName;
	}
	
    public String getStatusCause() {
		return statusCause;
	}

	public void setStatusCause(String statusCause) {
		this.statusCause = statusCause;
	}

}
