package com.peddle.digital.cobot.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 * Util Class to Transform Raw Selenium script to Parameterized Script
 *
 */
public class JavaCodeUtil {

	final static Logger logger = Logger.getLogger(JavaCodeUtil.class);

	/**
	 * Method to transform Raw Selenium script to Parameterized Script
	 * 
	 * @param src : Source location of the Selenium Script file to be read from
	 * @param dest : File Location the selenium Script has to be written to
	 *               after script transformation
	 */
	public static void convertSeleniumCode(File src, File dest) {
		BufferedReader reader=null;
		PrintWriter writer = null;

		try {

			String fileName = src.getAbsolutePath();
			reader = new BufferedReader(new FileReader(src.getAbsolutePath()));
			writer = new PrintWriter(dest);

			String className = fileName.substring(fileName.lastIndexOf(File.separatorChar)+1).replace(".java","");

			String line = reader.readLine();
			boolean initTestMetnodConvert = false;
			boolean addedImports=false;

			ArrayList<String> javaCodeList = new ArrayList<String>();

			while (line != null) {

				if(initTestMetnodConvert)
				{		   
					line = updateTestMethodContents(line,javaCodeList);
				}

				if( !addedImports && line.startsWith("import ")) {
					addImports(javaCodeList);
					addedImports=true;
				}

				//rename test method

				else if(line.contains("public void "+removeLast(className, 4).replaceFirst(removeLast(className, 4).substring(0,1), removeLast(className, 4).substring(0,1).toLowerCase())))
				{
					initTestMetnodConvert=true;
					line =  "public void test(java.util.List<String> data) {\n\t int[] count={0}; \n";

				}

				else if(line.contains("public class"))
				{   
					line =  "public class "+className +"{";
				}

				else if(line.startsWith("package com.example.tests"))
				{   
					line =  "";
				}
				else if(line.contains("driver = new FirefoxDriver();"))
				{
					line ="\t driver = new ChromeDriver();\ndriver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS) ;";
				}

				javaCodeList.add(line);
				line = reader.readLine();
			}

			reader.close();
			javaCodeList.remove(javaCodeList.size() - 1);

			addMethods(javaCodeList,className);


			javaCodeList.add("}");


			for(String entry: javaCodeList)
			{
				writer.println(entry);
			}

			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(writer != null)
			{
				writer.close();
			}
		}
	}
	
	/**
	 * Add the addition Imports   required with additional code added to 
	 * Raw selenium script file
	 * 
	 * @param javaCodeList
	 */

	private static void addImports(ArrayList<String> javaCodeList)
	{
		BufferedReader javaImportsReader= null;

		try {

			InputStream javaMainstream = JavaCodeUtil.class.getResourceAsStream("/templates/java_imports.txt");
			javaImportsReader = new BufferedReader(new InputStreamReader(javaMainstream));

			String line = javaImportsReader.readLine();

			while (line != null) {
				javaCodeList.add(line);
				line = javaImportsReader.readLine();
			}
		}catch(IOException e) {
			if(javaImportsReader != null)
			{
				try {
					javaImportsReader.close();

				} catch (IOException ex) {

					e.printStackTrace();
				}
			}}
	}
	
	/**
	 * Add the additional Methods to Raw Script file like methods to update 
	 * the script execution status to cobot server ect 
	 * 
	 * @param javaCodeList : Code that contains the transformed selenium code to which 
	 *                       Additional methods are to be added
	 * @param className : Name of the Script file that will be replaced in the method 
	 *                    template placeholder
	 */

	private  static void addMethods(ArrayList<String> javaCodeList, String className)
	{
		BufferedReader javaMainReader=null;
		try {
			InputStream javaMainstream = JavaCodeUtil.class.getResourceAsStream("/templates/java_methods.txt");

			javaMainReader = new BufferedReader(new InputStreamReader(javaMainstream));

			String line = javaMainReader.readLine();

			while (line != null) {

				if(line.contains("<CLASS_NAME>"))
				{
					line = line.replaceAll("<CLASS_NAME>", className) ;
				}

				javaCodeList.add(line);
				line = javaMainReader.readLine();
			}
		}catch(IOException e) {
			if(javaMainReader != null)
			{
				try {
					javaMainReader.close();

				} catch (IOException err) {

					err.printStackTrace();
				}
			}

		}
	}
	
	/**
	 * Takes each line of the RAW selenium test code and parameterize where ever possible  
	 * so that the whole script file generic  
	 * 
	 * @param line: single line in the selenium java code
	 * @param javaCodeList: contains the modified selenium code until this line is reached 
	 * @return modified the line for the line that was sent as argument
	 */

	private static String updateTestMethodContents(String line,ArrayList<String> javaCodeList)
	{
		if(line.contains("driver.get(")) 
		{
			line = line.replace("driver.get(", "driver.navigate().to(");
			javaCodeList.add(line);

			line = "\t driver.manage().window().maximize();";
		}

		else if(line.contains(".sendKeys("))
		{
			String  prefix = line.substring(0, line.lastIndexOf(".sendKeys(")+1);
			String  suffix = line.substring(line.lastIndexOf(".sendKeys(")+1);
			int quoteIndex = suffix.indexOf("\"");
			if(quoteIndex!=-1)
			{
				String ops = suffix.substring(0,suffix.indexOf("\""));
				ops=ops+"data.get(count[0]++));";
				line = prefix+ops;
			}
		}

		else if(line.contains("selectByVisibleText("))
		{
			String  prefix = line.substring(0, line.lastIndexOf(".selectByVisibleText(")+1);
			String  suffix = line.substring(line.lastIndexOf(".selectByVisibleText(")+1);
			int quoteIndex = suffix.indexOf("\"");
			if(quoteIndex!=-1)
			{
				String ops = suffix.substring(0,suffix.indexOf("\""));
				ops=ops+"data.get(count[0]++));";

				line = prefix+ops;
			}
		}

		else if(line.trim().endsWith(".click();"))
		{
			String locatorName= StringUtils.substringsBetween(line.trim(), "driver.findElement(By.", "(")[0];
			String elemantName=line.trim().replace("driver.findElement(By."+ locatorName +"(", "").replace(")).click();", "");
			line="click("+ "\"" +locatorName+"\"" + ","+elemantName+",data,count);";
		}

		return line;
	}
	
	private static String removeLast(String s, int n) {
		if (null != s && !s.isEmpty()) {
			s = s.substring(0, s.length()-n);
		}
		return s;
	}

}
