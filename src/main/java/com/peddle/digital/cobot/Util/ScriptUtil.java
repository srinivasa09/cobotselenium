package com.peddle.digital.cobot.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * 
 * @authors Srinivasa Reddy Challa, Raj Kumar
 * 
 * Util Class to Transform, Compile And Execute Script File 
 */

public class ScriptUtil {

	final static Logger logger = Logger.getLogger(ScriptUtil.class);
	final static String APP_NAME="cobot";

	@Autowired
	private Environment env;


	/**
	 * Method to Transform the Raw Selenium File to Parameterized File 
	 * so that it can be made generic  & reusable and run with different params 
	 *  
	 * @param scriptFile: Raw Selenium Script File
	 * @param scriptsRepoDir: Directory location where Raw Scripts are stored
	 * @param scriptRootDir: Directory location where Parameterized Scripts are  
	 *                        stored after transformation
	 */
	public static void transformScriptFile(String scriptFile, String scriptsRepoDir, String scriptRootDir)
	{

		String scriptFromRepo = scriptsRepoDir+scriptFile;
		String transformedJavaFile = scriptRootDir+"/"+scriptFile;

		File file = new File(transformedJavaFile);

		if(!file.getParentFile().exists())
		{
			file.getParentFile().mkdirs();
		}

		JavaCodeUtil.convertSeleniumCode(new File(scriptFromRepo),new File(transformedJavaFile));
	}

	/**
	 * Method to compile the  Parameterized Script file
	 * @param scriptFile: Name for the Parameterized Script file
	 * @param scriptRootDir: Directory location where Parameterized Scripts are located
	 * @throws Exception
	 */
	public static void compileScriptFile(String scriptFile, String scriptRootDir,String executeMode) throws Exception
	{

		String transformedJavaFile = scriptRootDir+"/"+scriptFile;

		String classpath = "";

		if("servlet-container".equals(executeMode))
		{
			String catilanaHome = System.getProperty("catalina.home");
			if(catilanaHome!=null) {
				classpath = catilanaHome+"/webapps/"+APP_NAME+"/WEB-INF/lib/*";
			}
		}
		else
		{
			classpath = System.getProperty("java.class.path");
		}

		String osName = System.getProperty("os.name");

		if(StringUtils.startsWithIgnoreCase(osName.trim(), "win"))
		{
			classpath = "\"" + classpath +"\"";
		}


		Process process = Runtime.getRuntime().exec("javac -cp " + classpath +"  " + transformedJavaFile);

		StreamReader errorReader = new 
				StreamReader(process.getErrorStream(), "ERROR");            

		StreamReader outputReader = new 
				StreamReader(process.getInputStream(), "OUTPUT");

		errorReader.start();
		outputReader.start();

		process.waitFor();
		logger.info("compilation Finished");

	}

	/**
	 * Method To execute Script class in a separate java process
	 * 
	 * @param scriptFileName: Name modified selenium script file to be executed
	 * @param data: Input data for the script while executing 
	 * @param scriptRootDir: Directory location where transformed script files are located
	 * @param statusCallBackUrl: Cobot API for the script to update the status of 
	 *                           script execution
	 * @param appJobID: Task ID assigned for the script 
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public static void runScriptFile(String scriptFileName, String data, String scriptRootDir,
			String statusCallBackUrl, String appJobID) throws IOException, InterruptedException {

		String scriptclassFile = scriptFileName.replace(".java","");

		String classpath = System.getProperty("java.class.path");
		String cromeDriverpath = System.getProperty("webdriver.chrome.driver");

		Process runscript = Runtime.getRuntime().exec("java -cp \"" + classpath +";"+
				scriptRootDir+" \" " +scriptclassFile + " "+data+" "+
				cromeDriverpath+" "+statusCallBackUrl+" "+ appJobID);

		runscript.waitFor();

		InputStream errorStream = runscript.getErrorStream();
		StringWriter scriptRunWriter = new StringWriter();
		IOUtils.copy(errorStream, scriptRunWriter, "UTF-8");
		String runErrorStr = scriptRunWriter.toString();
		errorStream.close();
		logger.error(runErrorStr);

	}

	/**
	 * Method to execute script class by dynamically loading the script class 
	 * file into the current loader
	 * 
	 * @param scriptFileName: Name modified selenium script file to be executed
	 * @param data: Input data for the script while executing 
	 * @param scriptRootDir: Directory location where transformed script files are located
	 * @param statusCallBackUrl: Cobot API for the script to update the status of 
	 *                           script execution
	 * @param appJobID: Task ID assigned for the script 
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public static void loadAndRunClass(String scriptFileName, String content, String scriptRootDir,
			String statusCallBackUrl, String appJobID) throws Exception 
	{

		File root = new File(scriptRootDir); 

		JSONObject obj = new JSONObject(content);
		JSONArray jsonArray = obj.getJSONArray("data");
		List<Object> list = jsonArray.toList();
		List<String> data= new ArrayList<String> ();

		for(Object a: list){
			data.add(String.valueOf(a));
		} 

		URLClassLoader child = new URLClassLoader(
				new URL[] { root.toURI().toURL() }, 
				ScriptUtil.class.getClassLoader()
				);

		String scriptclassFile = scriptFileName.replace(".java","");

		Class classToLoad = Class.forName(scriptclassFile, true, child);
		Method method =  classToLoad.getMethod("runTest", java.util.List.class,String.class,String.class);
		Object instance = classToLoad.newInstance();
		Object result = method.invoke(instance,data,statusCallBackUrl,appJobID);
	}
}
