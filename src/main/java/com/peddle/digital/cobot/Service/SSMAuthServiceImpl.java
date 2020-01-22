package com.peddle.digital.cobot.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

@Service
public class SSMAuthServiceImpl implements SSMAuthService {
	final static Logger logger = Logger.getLogger(SSMAuthServiceImpl.class);

	@Value("${ssmurl}")
	String ssmServerURL;
	
	@Override
	public String getAuthToken() {
		String token=null;
		try {
			String ssmurl = ssmServerURL;
			URL url = new URL (ssmurl+"/api/login");
			JSONObject inputJson=new JSONObject("{\"username\":\"admin\",\"password\":\"password\"}");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");

			
			con.setDoOutput(true);
			
			try(OutputStream os = con.getOutputStream()){
				byte[] input = inputJson.toString().getBytes("utf-8");
				os.write(input, 0, input.length);			
			}

			int code = con.getResponseCode();
			logger.info(code);
			
			try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				try {
				     JSONObject jsonObject = new JSONObject(response.toString());
				     token = jsonObject.getString("access_token");
				     logger.info(token);
				}catch (JSONException err){
				    logger.error("Unable to parse response to string");
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to get token from SSM");
		}
		return token;
	}

	@Override
	public JSONObject sendTaskCompleteResponseBackToSSM(Long jobId,String accessToken) {
		JSONObject jsonObject =null;
		try {
			String ssmurl = ssmServerURL;
    		String urlParameters = "taskid="+jobId+"&provisioning=true";
        	byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        	int postDataLength = postData.length;
        	URL url = new URL (ssmurl+"/api/v5/completetask");
        	HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
        	conn.setDoOutput(true);
        	conn.setInstanceFollowRedirects(false);
        	conn.setRequestMethod("POST");
        	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
//        	conn.setRequestProperty("charset", "utf-8");
//        	conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
        	conn.setRequestProperty ("Authorization", "Bearer "+accessToken+"");
        	conn.setUseCaches(false);
        	try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
        	   wr.write( postData );
        	}
        	int code = conn.getResponseCode();
        	logger.info(code);
			
        	try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))){
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				try {
				     jsonObject = new JSONObject(response.toString());
				     
				}catch (JSONException err){
				    logger.error("Unable to parse response to string");
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
        	logger.info("Successfully status sent back to SSM");
    	}catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to sent status to SSM");
		}
		return jsonObject;
	}

	@Override
	public JSONObject createConnectionJSON(JSONObject accountJSON, String accessToken,String connectionName) {
		// TODO Auto-generated method stub
		JSONObject jsonObject =null;
		try {
			String ssmurl = ssmServerURL;//"http://localhost:8085/ECM";
    		String urlParameters = "connectiontype=REST&saveconnection=Y&systemname=testSecuritySystem&connectionName="+connectionName+"&ConnectionJSON={\"authentications\":{\""+connectionName+"\":{\"authType\":\"basic\",\"errorPath\":\"401\",\"maxRefreshTryCount\":5,\"properties\":{\"userName\":\"admin\",\"password\":\"password\"},\"authError\":[\"404\"],\"retryFailureStatusCode\":[202]}}}&CreateAccountJSON="+accountJSON;
        	byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        	int postDataLength = postData.length;
        	URL url = new URL (ssmurl+"/api/v5/testConnection");
        	HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
        	conn.setDoOutput(true);
        	conn.setInstanceFollowRedirects(false);
        	conn.setRequestMethod("POST");
        	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
        	conn.setRequestProperty ("Authorization", "Bearer "+accessToken+"");
        	conn.setUseCaches(false);
        	try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
        	   wr.write( postData );
        	}
        	int code = conn.getResponseCode();
        	logger.info(code);
			
        	try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))){
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				try {
				     jsonObject = new JSONObject(response.toString());
				     
				}catch (JSONException err){
				    logger.error("Unable to parse response to string");
				}
			}catch(Exception e)
			{
				e.printStackTrace();
				logger.error(e);
			}
        	
    	}catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to sent status to SSM");
		}
		return jsonObject;
	}

	@Override
	public JSONObject createSecuritySystem(JSONObject jsonObjecte, String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
