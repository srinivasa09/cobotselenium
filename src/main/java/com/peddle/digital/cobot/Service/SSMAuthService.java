package com.peddle.digital.cobot.Service;

import org.json.JSONObject;

public interface SSMAuthService {

	String getAuthToken();
	JSONObject sendTaskCompleteResponseBackToSSM(Long jobId,String accessToken);
	JSONObject createConnectionJSON(JSONObject jsonObjecte,String accessToken,String connectionName);
	JSONObject createSecuritySystem(JSONObject jsonObjecte,String accessToken);
}