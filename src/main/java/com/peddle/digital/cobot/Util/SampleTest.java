package com.peddle.digital.cobot.Util;

// Generated by Selenium IDE
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;

import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class SampleTest{
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
public void test(java.util.List<String> data) {
	 int[] count={0}; 

    driver.navigate().to("http://www.testyou.in/Login.aspx");
	 driver.manage().window().maximize();
    driver.manage().window().setSize(new Dimension(1536, 832));
click("id","ctl00_CPHContainer_txtUserLogin",data,count);
click("cssSelector",".content_wrapper",data,count);
    driver.findElement(By.id("ctl00_CPHContainer_txtUserLogin")).sendKeys(data.get(count[0]++));
click("id","ctl00_CPHContainer_txtPassword",data,count);
click("cssSelector",".content",data,count);
    driver.findElement(By.id("ctl00_CPHContainer_txtPassword")).sendKeys(data.get(count[0]++));
click("id","ctl00_CPHContainer_btnLoginn",data,count);
click("cssSelector",".test_category_img",data,count);
click("cssSelector","#ctl00_CPHContainer_dlCategoryTest_ctl00_rptrTestList_ctl00_hyprlnkTestName > .cat_test_name",data,count);
  }
  
  
  public static void main(String[] args)
  {
	  SampleTest sampleTest = new SampleTest();
	  
	  JSONObject obj = new JSONObject("{\"data\":[\"srinivas\",\"password\"]}");
		JSONArray jsonArray = obj.getJSONArray("data");
		List<Object> list = jsonArray.toList();
		List<String> data= new ArrayList<String> ();

		for(Object a: list){
			data.add(String.valueOf(a));
		} 
		
		String url="http://localhost:9090/api/job/updatestatus";
	    String jobid = "JOB-1";
	    System.setProperty("webdriver.chrome.driver","C:/Users/hp/.m2/repository/webdriver/chromedriver/win32/79.0.3945.36/chromedriver.exe");
	    
	    sampleTest.runTest(data, url, jobid);
  }
  
 
	public void runTest(List<String> data, String callbackUrl,String jobid)
	{

		try {  
			setUp();
			test(data);
			tearDown();

			JsonObject response = Json.createObjectBuilder().
					add("jobId", jobid).add("status","Success").add("reason", "").
					build();

			sendExecutionStatus(callbackUrl,response.toString());

		} catch(Exception e) {
			e.printStackTrace();
			
            try
            {
            	driver.quit();
            } catch(Exception driverEx)
            {
            	driverEx.printStackTrace();
            }
			JsonObject response = Json.createObjectBuilder().
					add("jobId", jobid).add("status","Failed").add("reason", e.getMessage()).
					build();
			try {
				sendExecutionStatus(callbackUrl,response.toString());
				
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
        
        
     public void click(String locatorName, String elementId, java.util.List<String> data, int[] count)
     {
	    String type = getElementType(locatorName, elementId).getAttribute("type");
	    if (type != null && type.equals("radio")) {
			if (driver.findElements(By.xpath("//label[contains(.,'" + data.get(count[0]) + "')]/input[@type='radio'][@"
					+ locatorName + "='" + elementId + "']")).size() != 0) {
				driver.findElements(By.xpath("//label[contains(.,'" + data.get(count[0]++)
						+ "')]/input[@type='radio'][@" + locatorName + "='" + elementId + "']")).get(0).click();
			} else if (driver.findElements(By.xpath("//input[contains(.,'" + data.get(count[0]) + "')][@type='radio'][@"
					+ locatorName + "='" + elementId + "']")).size() != 0) {
				driver.findElements(By.xpath("//input[contains(.,'" + data.get(count[0]++) + "')][@type='radio'][@"
						+ locatorName + "='" + elementId + "']")).get(0).click();
			}
		} else if(type != null && type.equals("checkbox")) {
			if(driver.findElements(By.xpath("//label[contains(.,'" + data.get(count[0]) + "')]")).size() != 0) {
				driver.findElement(By.xpath("//label[contains(.,'" + data.get(count[0]++) + "')]")).click();
			} else if(driver.findElements(By.xpath("//input[contains(.,'" + data.get(count[0]) + "')]")).size() != 0) {
				driver.findElement(By.xpath("//input[contains(.,'" + data.get(count[0]++) + "')]")).click();
			}
	    } else {
			getElementType(locatorName, elementId).click();
		}
	}

	public WebElement getElementType(String locatorName, String elemeintID) {
		if (StringUtils.equals(locatorName, "name")) {
			return driver.findElement(By.name(elemeintID));
		} else if (StringUtils.equals(locatorName, "xpath")) {
			return driver.findElement(By.xpath(elemeintID));
		} else if (StringUtils.equals(locatorName, "cssSelector")) {
			return driver.findElement(By.cssSelector(elemeintID));
		} else if (StringUtils.equals(locatorName, "linkText")) {
			return driver.findElement(By.linkText(elemeintID));
		} else if (StringUtils.equals(locatorName, "partialLinkText")) {
			return driver.findElement(By.partialLinkText(elemeintID));
		} else if (StringUtils.equals(locatorName, "id")) {
			return driver.findElement(By.id(elemeintID));
		} else if (StringUtils.equals(locatorName, "class")) {
			return driver.findElement(By.className(elemeintID));
		}
		return null;
	}
     
    public static void sendExecutionStatus(String postUrl, String inputJson) throws Exception {

		URL url = new URL (postUrl);

		System.out.println(inputJson);

		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");

		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");

		con.setDoOutput(true);

		try(OutputStream os = con.getOutputStream()){
	    	byte[] input = inputJson.getBytes("utf-8");
	    	os.write(input, 0, input.length);			
		}

		int code = con.getResponseCode();
		System.out.println(code);

		try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
	    	StringBuilder response = new StringBuilder();
	    	String responseLine = null;
	    	while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
	    	}
	    	System.out.println(response.toString());
		}
		catch(Exception e)
		{
	    	e.printStackTrace();
		}
    }
           
}
