package com.peddle.digital.cobot.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class RemoteAgentUtils {

	final static Logger logger = Logger.getLogger(RemoteAgentUtils.class);

	public static void executeRemoteJob(String scriptsRootDir,String agentSystemPort, String agentScriptUploadUrl, 
			String cobotStatusCallbackURL,String  appJobID, String content,String fileName,String remoteIP) throws Exception
	{

			CloseableHttpClient httpclient = HttpClients.createDefault();
			String scriptsDir = scriptsRootDir;
			String agentPort = agentSystemPort;
			String scriptUpload = agentScriptUploadUrl;
			String statusCallBackUrl = cobotStatusCallbackURL;

			File file = new File(scriptsDir+File.separatorChar+fileName);

			String remoteHostUrl="http://"+remoteIP+":"+agentPort+scriptUpload;
			logger.info("Remote Host URL "+remoteHostUrl);
			FileInputStream input = new FileInputStream(file);
			byte[] byteArray = IOUtils.toByteArray(input);

			HttpEntity entity = MultipartEntityBuilder
					.create()
					.addTextBody("JobId", appJobID)
					.addTextBody("CallBackURL", statusCallBackUrl)
					.addTextBody("Body", content)
					.addTextBody("FileName", fileName)
					.addBinaryBody("File",byteArray, ContentType.create("application/octet-stream"), "filename")
					.build();

			HttpPost httpPost = new HttpPost(remoteHostUrl);
			httpPost.setEntity(entity);
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity result = response.getEntity();
			logger.info("job status "+EntityUtils.toString(result));
			logger.info("job "+appJobID+" is successfully completed");
		}
		

}
