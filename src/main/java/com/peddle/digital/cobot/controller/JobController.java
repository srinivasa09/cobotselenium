package com.peddle.digital.cobot.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.peddle.digital.cobot.Service.SSMAuthService;
import com.peddle.digital.cobot.Util.JobUtil;
import com.peddle.digital.cobot.constants.STATUS;
import com.peddle.digital.cobot.model.Job;
import com.peddle.digital.cobot.repository.JobRepository;

/**
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 * Rest API related submitting Automated script jobs for execution 
 * 
 */

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class JobController {
	
	final static Logger logger = Logger.getLogger(JobController.class);

    @Autowired
    JobRepository jobRepository;
    
    @Autowired
    SSMAuthService authService;
    
    @Value("${scripts.dir}")
    String scriptsRootDir;
    
    @Value("${cobot.createconnectionjson.url}")
    String cobotCreateconnectionJSONUrl;
    
    /**
     * API to submit job for script execution on remote agent 
     * 
     * @param agentIP: IP address of remote agent
     * @param fileName: Name of the script to be executed 
     * @param content:Data input for the executing script 
     * @return Response with JObID which can be used to know the status of the 
     *         script execution status
     */
    
    @RequestMapping(value = "/job/agent", method = RequestMethod.POST)
	public @ResponseBody
	JobResponse uploadFileHandler(@RequestParam("AgentIP") String agentIP,
			@RequestParam("File") String fileName,
			@RequestParam("Body") String content)
	{
    	Job job = new Job();
    	job.setStatus(STATUS.Submitted.toString());
    	job.setJobStatusCode(STATUS.Submitted.getID());
    	job.setContent(content);
    	job.setRemoteAgentIP(agentIP);
    	job.setScriptFileName(fileName);
    	Job saveJob = jobRepository.save(job);
    	JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
    	return updateResponse;
	}
    
    /**
     * API to submit job for executing automated script
     * @param fileName:  Name of the script to be executed
     * @param content: Data input for the executing script 
     * @return Response with JObID which can be used to know the status of the
     *         script execution status
     */
    
    @RequestMapping(value = "/job/script", method = RequestMethod.POST)
  	public @ResponseBody
  	JobResponse executeScript(
  			@RequestParam("File") String fileName,
  			@RequestParam("Body") String content)
  	{
      	Job job = new Job();
      	job.setStatus(STATUS.Submitted.toString());
      	job.setJobStatusCode(STATUS.Submitted.getID());
      	job.setContent(content);
      	job.setScriptFileName(fileName);
      	Job saveJob = jobRepository.save(job);
      	JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
      	return updateResponse;
  	}
    
    /**
     * API to update the status of the job
     * @param jobStatus: status for the job to be updated
     * @return HTTP code for job  update 
     */
    
    @PostMapping("/job/updatestatus")
    public ResponseEntity<?> updateJobStatus(@RequestBody Status jobStatus) {
    	logger.info("Recieved Job Update Rquest for JOB"  + jobStatus.jobId );
    	Long jobDBId = JobUtil.getDBJobIDFromAppJobID(jobStatus.getJobId());
    	jobRepository.updateStatusAndReason(jobStatus.getStatus(),jobStatus.getReason(), jobDBId);
    	return ResponseEntity.ok().build();
    }
    
    
    @RequestMapping(value = "/job/createjson", method = RequestMethod.POST)
	public @ResponseBody
	String createJson(@RequestParam("content") String content,
			@RequestParam("headers") String headers,@RequestParam("fileName") String fileName,@RequestParam("conntype") String connectionType,@RequestParam("connectionName") String connectionName)
	{
    	
    	String scriptsDir = scriptsRootDir;
    	logger.info(scriptsDir);

    	File file = new File(scriptsDir+fileName);
    	  
    	//Create the file
    	try {
			if (file.createNewFile())
			{
				logger.info("File is created!");
			} else {
				logger.info("File already exists.");
			}
			//Write Content
	    	FileWriter writer = new FileWriter(file);
	    	writer.write(content);
	    	writer.close();
	    	
	    	JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(headers).getAsJsonObject();

	    	JSONObject dataObj = new JSONObject();
	    	JSONObject httpObj = new JSONObject();
	    	JSONArray jsonArrayData = new JSONArray();
	    	JSONArray jsonArraykeys = new JSONArray();
	    	for(Entry<String, JsonElement> entry : o.entrySet()) {
			    String key = entry.getKey();
	            String value = entry.getValue().getAsString();
	            jsonArrayData.put(value);
	            jsonArraykeys.put(key);
			}

//	        String connectionName="cobotresttestconnection";
	        JSONObject connectionJson = new JSONObject();
	        JSONObject responseColsToPropsMap = new JSONObject();
	    	JSONArray call = new JSONArray();
	    	
	    	JSONObject callObj = new JSONObject();
	    	JSONObject httpParams = new JSONObject();
	    	JSONObject httpHeaders = new JSONObject();
	    	
	    	connectionJson.put("accountIdPath", "Call1.message.value[0].taskid");
	    	connectionJson.put("dateFormat", "");
	    	connectionJson.put("responseColsToPropsMap", responseColsToPropsMap);
	    	
	    	callObj.put("name", connectionName);
	    	callObj.put("connection", connectionName);
	    	callObj.put("url", cobotCreateconnectionJSONUrl);
	    	callObj.put("httpMethod", "POST");
	    	callObj.put("httpContentType", "application/json");
	    	
	    	httpParams.put("filename", fileName);
	    	httpParams.put("data", jsonArrayData);
	    	httpParams.put("httpparams", jsonArraykeys);
	    	httpParams.put("taskid", "${arsTasks.id}");
	    	
	    	httpHeaders.put("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=");
	    	
	    	callObj.put("httpParams", httpParams);
	    	callObj.put("httpHeaders", httpHeaders);
	    	
	    	call.put(callObj);
	    	
	    	connectionJson.put("call", call);
	    	logger.info(fileName);
	    	logger.info(jsonArrayData);
	    	String token = authService.getAuthToken();
			JSONObject jsonObject = authService.createConnectionJSON(connectionJson, token,connectionName);
			logger.info(connectionJson);
	    	if(jsonObject!=null) {
	    		return jsonObject.getString("msg");
	    	}else {
	    		return "Unable to create connection";
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}

	}
    
    @PostMapping("/job")
    public JobResponse createJob(HttpServletRequest servletRequest) throws IOException {
    	String content = IOUtils.toString(servletRequest.getReader());
    	Job job = new Job();
    	job.setStatus(STATUS.Submitted.toString());
    	job.setJobStatusCode(STATUS.Submitted.getID());
    	job.setContent(content);
    	Job saveJob = jobRepository.save(job);
    	JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
    	return updateResponse;
    }
    
    @PostMapping("/job/connectionjson")
    public JobResponse connectionjson(HttpServletRequest servletRequest) throws IOException {
    	String content = IOUtils.toString(servletRequest.getReader());
    	JSONObject obj = new JSONObject(content);
    	Job job = new Job();
    	job.setStatus(STATUS.Submitted.toString());
    	job.setJobStatusCode(STATUS.Submitted.getID());
    	job.setContent(content);
    	job.setHttpparams(obj.get("httpparams").toString());
    	job.setScriptFileName(obj.get("filename").toString());
    	Job saveJob = jobRepository.save(job);
    	JobResponse updateResponse = JobUtil.updateResponse(saveJob, null);
    	return updateResponse;
    }
}
