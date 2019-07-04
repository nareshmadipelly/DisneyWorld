package com.automation.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.poi.xwpf.usermodel.Document;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import com.assertthat.selenium_shutterbug.core.PageSnapshot;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.automation.functionallibrary.InitiateDriver;




public class ReportStatus {

	private static final String FILENAME = System.getProperty("user.dir")+"/Report/Resultname.txt";

	public static JSONArray jsonArray;
	final String testId;
	public boolean mobile;
	public boolean windows;
	private static File jsonFilePath;
	private static String startTime, fileName;
	private static String parentPath = System.getProperty("user.dir")+"/Report";
	private static File executionPath, currentExecutionPath, overAllMainReport;
	public File screenshotFilePath;
	WebDriver driver;
	public static long overAllStartTime,overAllEndTime,overAllTime;
	long oldTime, newTime, testStartTime;
	SoftAssert softassert = new SoftAssert();
	static SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm:ss a");

	public static void initializeReportStatus() throws IOException{
		jsonArray = new JSONArray();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy h:mm:ss a");
		startTime = sdf.format(date);
		overAllStartTime = new Date().getTime();
		executionPath = new File(parentPath+"/Execution_Report");
		if(!executionPath.exists())
			executionPath.mkdir();
		fileName = startTime.replace("/", "_").replace(":", "_").replace(",", "_");
		
		/*if(System.getenv("BUILD_ID")==null)
			fileName = startTime.replace("/", "_").replace(":", "_").replace(",", "_");
		else
			fileName = System.getenv("BUILD_ID");*/
		currentExecutionPath = new File(parentPath+"/Execution_Report/"+fileName);
		currentExecutionPath.mkdir();
		jsonFilePath = new File(currentExecutionPath.toString()+"/JSON");
		overAllMainReport = new File(currentExecutionPath.toString()+"/OverAllMainReport.html");
		jsonFilePath.mkdir();
		
		try{
			copyFileUsingStream(new File(System.getProperty("user.dir")+"/Report/Template/OverAllMainReport.html"),overAllMainReport);
		}catch(IOException e){
			e.printStackTrace();
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {

			String content =currentExecutionPath.toString();
             
			bw.write(content);

			// no need to close it.
			//bw.close();

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		}
		
		
	}

	WordprocessingMLPackage wordMLPackage;
	//CustomXWPFDocument document;
	
	public void InitScreenshot(WordprocessingMLPackage wordMLPackage){
		this.wordMLPackage = wordMLPackage;
	}


	private static void copyFileUsingStream(File source,File dest) throws IOException{
		InputStream is = null;
		OutputStream os = null;
		try{
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while((length = is.read(buffer))>0){
				os.write(buffer, 0, length);
			}
		}finally {
			is.close();
			os.close();
		}
	}

	private static void copyFolderAndFiles(File source,File dest) throws IOException{
		if(!dest.exists()){
			dest.mkdir();
			copyFolder(source,dest);
		}
		else
		{
			FileUtils.cleanDirectory(dest);
			dest.mkdir();
			copyFolder(source,dest);
		}
	}

	private static void copyFolder(File src, File dest) throws IOException{
		if(src.isDirectory()){
			if(!dest.exists())
				dest.mkdir();
			String files[] = src.list();
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				copyFolder(srcFile,destFile);
			}
		}
		else {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest); 
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) > 0){
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
	}


	public ReportStatus(WebDriver driver, String testId)
	{
		this.driver = driver;
		this.testId = testId;
		this.windows = InitiateDriver.windows;
		TestIterator.intitalizeIterator(testId);
	}

	public synchronized void report(String feature,HashMap<String, String> data) throws JSONException, IOException {
		oldTime = new Date().getTime();
		testStartTime = oldTime;
		JSONObject jsonObject = new JSONObject();
		JSONArray stepJsonArray = new JSONArray();
		jsonObject.put("testId", testId);
		jsonObject.put("feature", feature);
		jsonObject.put("browserName", data.get("Browser"));
		jsonObject.put("ManualTestCaseName", data.get("TestCase_Name"));
		jsonObject.put("TestResult", "In-Progress");
		jsonObject.put("comments", "");
		jsonObject.put("session", "");
		jsonObject.put("accountNo", data.get("Search_Value"));
		jsonObject.put("totalSteps", 0);
		jsonObject.put("passed", 0);
		jsonObject.put("failed", 0);
		jsonObject.put("xpathDetails","");
		jsonObject.put("startTime", "");
		jsonObject.put("timeTaken", "");
		jsonObject.put("finalScreenShot", "");
		jsonObject.put("Environment", data.get("Environment"));
		jsonObject.put("FlowType", data.get("FlowType"));
		jsonObject.put("Application", data.get("Application"));
		jsonObject.put("IP", data.get("IP"));

		//additional columns for API
		jsonObject.put("ErrorMessage", "");
		jsonObject.put("BundlePrice", "");
		jsonObject.put("BundleNet", "");
		jsonObject.put("MonthlyAmount", "");
		jsonObject.put("MonthlyAmountwithoutPromos", "");
		jsonObject.put("BndPrcflag", "");
		jsonObject.put("BndnetPrcflag", "");
		jsonObject.put("NCOG Order Submission", "");
		jsonObject.put("FRO Validation", "");
		jsonObject.put("SSP Order Submission", "");
		jsonObject.put("Provisioning", "");
		jsonObject.put("Billing Completed", "");
		jsonObject.put("ISOC Validation", "");
		jsonObject.put("SSP Validation", "");
		jsonObject.put("CPEM Validation", "");
		jsonObject.put("IVAPP Validation", "");
		jsonObject.put("Vision Validation", "");
		jsonObject.put("JiraID", "");
		jsonObject.put("TestSessionID", "");
		jsonObject.put("Sub Module", "");
		jsonObject.put("Description", "");
		jsonObject.put("End Time", "");
		jsonObject.put("Application Code", "");
		jsonObject.put("JIRA App Name", "");
		jsonObject.put("JIRA Assignee", "");
		jsonObject.put("vzID", "");
		jsonObject.put("DatapointType", "");
		jsonObject.put("DatapointValue", "");
		jsonObject.put("OrderOrgSystem", "");
		jsonObject.put("ISOC", "");
		jsonObject.put("MON", "");

		updateStaticArray(jsonObject);
		writeReport();
		writeReportTestCase(stepJsonArray, testId);
		File testFile = new File(currentExecutionPath.toString()+"/"+jsonObject.get("testId"));
		testFile.mkdir();
	}

	synchronized static void updateStaticArray(JSONObject jsonObject){
		try{
			jsonArray.put(jsonObject);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void reportPass(String strDescription, String strExpected, String strActual) throws Exception{
		reportSteps(testId,strDescription,strExpected,strActual,"Passed","");
	}

	public void reportFail(String strDescription, String strExpected, String strActual) throws Exception{
		reportSteps(testId,strDescription,strExpected,strActual,"Failed","");
	}

	public synchronized void reportSteps(String testId, String strDescription, String strExpected, String strActual, String strStatus,String strTimeTaken) throws Exception{
		for(int i=0; i<jsonArray.length();i++){
			if(jsonArray.getJSONObject(i).get("testId").equals(testId))
			{
				InputStream is = new FileInputStream(jsonFilePath+"/"+testId+".json");
				String jsonTxt = IOUtils.toString(is);
				JSONArray jsonTestArray = new JSONArray(jsonTxt);
				JSONObject newStep = new JSONObject();
				int currentStep = Integer.valueOf(jsonArray.getJSONObject(i).get("totalSteps").toString())+1;
				int passCount=Integer.valueOf(jsonArray.getJSONObject(i).get("passed").toString());
				int failCount=Integer.valueOf(jsonArray.getJSONObject(i).get("failed").toString());
				newStep.put("stepNo", currentStep);
				newStep.put("stepDescription", strDescription);
				newStep.put("stepExpected", strExpected);
				newStep.put("stepActual", strActual);
				newStep.put("stepStatus", strStatus);
				newTime = new Date().getTime();
				newStep.put("timeTaken", getTimeDifference(oldTime,newTime));
				oldTime = newTime;
				jsonArray.getJSONObject(i).put("timeTaken",getTimeDifference(testStartTime,oldTime));
				if(PropertyReader.getProperty("screenShotOnPass").equalsIgnoreCase("true")){
					/*File screenShot = getFileScreenShot(mobile);
					FileUtils.copyFile(screenShot, new File(currentExecutionPath.toString()+"/"+jsonArray.getJSONObject(i).get("testId")+"/"+newStep.get("stepNo")+".png"));
					newStep.put("screenShot", jsonArray.getJSONObject(i).get("testId")+"/"+newStep.get("stepNo")+".png");*/
					
					File screenShot = getFileScreenShot(mobile);
					File tempimage = new File(currentExecutionPath.toString()+"/"+jsonArray.getJSONObject(i).get("testId")+"/"+newStep.get("stepNo")+".png");
					FileUtils.copyFile(screenShot, tempimage);
					if(PropertyReader.getProperty("screenShotDoc").equalsIgnoreCase("true"))
						addScreenshotToDocument(tempimage);
					newStep.put("screenShot", jsonArray.getJSONObject(i).get("testId")+"/"+newStep.get("stepNo")+".png");
				}

				else if (strStatus.equalsIgnoreCase("Failed")){
					/*File screenShot = getFileScreenShot(mobile);
					FileUtils.copyFile(screenShot, new File(currentExecutionPath.toString()+"/"+jsonArray.getJSONObject(i).get("testId")+"/"+newStep.get("stepNo")+".png"));
					newStep.put("screenShot", jsonArray.getJSONObject(i).get("testId")+"/"+newStep.get("stepNo")+".png");*/
					
					File screenShot = getFileScreenShot(mobile);
					File tempimage = new File(currentExecutionPath.toString()+"/"+jsonArray.getJSONObject(i).get("testId")+"/"+newStep.get("stepNo")+".png");
					FileUtils.copyFile(screenShot, tempimage);
					if(PropertyReader.getProperty("screenShotDoc").equalsIgnoreCase("true"))
						addScreenshotToDocument(tempimage);
					newStep.put("screenShot", jsonArray.getJSONObject(i).get("testId")+"/"+newStep.get("stepNo")+".png");
				}
				else
					newStep.put("screenShot", "");
				jsonTestArray.put(newStep);
				if(strStatus.equalsIgnoreCase("Failed"))
					jsonArray.getJSONObject(i).put("failed", failCount + 1);
				else
					jsonArray.getJSONObject(i).put("passed", passCount + 1);
				jsonArray.getJSONObject(i).put("totalSteps", currentStep);
				if(strStatus.equals("Failed"))
					//Madhu
					//updateMainReport("status","Failed");
					updateMainReport("TestResult","Failed");
				//Madhu
				writeReport();
				writeReportTestCase(jsonTestArray, testId);
				/*				if(strStatus.equalsIgnoreCase("passed"))
					reportStepsAllure(strDescription,true,true);
				else
					reportStepsAllure(strDescription,true,false);*/
			}
		}
	}

	public void getReport() throws JSONException{
		for(int i = 0; i<jsonArray.length();i++){
			System.out.println(jsonArray.get(i));
		}
	}

	public synchronized String getReportValue(String key) throws JSONException
	{
		String value = ""; 
		for(int i=0; i<jsonArray.length(); i++)
		{
			JSONObject object = jsonArray.getJSONObject(i); 
			if( object.get("testId").equals(testId))
			{
				value = object.get(key).toString();
				break;
			}
		}
		return value;
	}


	public synchronized void updateMainReport(String key, String value) throws JSONException, IOException
	{ 
		for(int i=0; i<jsonArray.length(); i++)
		{ 
			JSONObject object = jsonArray.getJSONObject(i); 
			if( object.get("testId").equals(testId))
			{
				if(key.equalsIgnoreCase("screenShot") && driver != null){
					File screenShot = getFileScreenShot(mobile);
					FileUtils.copyFile(screenShot, new File(currentExecutionPath.toString()+"/"+jsonArray.getJSONObject(i).get("testId")+"/finalResult.png"));
					object.put("finalScreenShot", jsonArray.getJSONObject(i).get("testId")+"/finalResult.png");
				} else {
					object.put(key, value);
				}
				writeReport();
				break;
			}
		}
	}

	public synchronized void deleteJSONRow(String testId) throws JSONException, IOException{
		for(int i=0; i<jsonArray.length(); i++)
		{ 
			JSONObject object = jsonArray.getJSONObject(i); 
			if( object.get("testId").equals(testId))
			{
				jsonArray.remove(i);
				writeReport();
				break;
			}
		}
	}

	public synchronized void writeReport() throws IOException, JSONException{
		FileWriter write = new FileWriter(jsonFilePath+"/template.json");
		try{
			jsonArray.write(write);
		} finally {
			write.flush();
			write.close();
		}
	}

	public void writeReportTestCase(JSONArray testArray, String testCaseName) throws IOException, JSONException{
		FileWriter write = new FileWriter(jsonFilePath+"/"+testCaseName+".json");
		try{
			testArray.write(write);
		} finally {
			write.flush();
			write.close();
		}
	}

	public static void updateHTMLResult(){
		String search = "[OverallExecutionTime]";
		//String replace = "$scope.datax="+jsonArray.toString()+";";
		try{
			overAllEndTime = new Date().getTime();
			long stepHr = (overAllEndTime - overAllStartTime)/ (60 * 60 * 1000) % 24;
			long stepMin = (overAllEndTime - overAllStartTime)/ (60 * 1000) % 60;
			long stepSec = (overAllEndTime - overAllStartTime)/ 1000 % 60;
			String replace = stepHr+"h:"+stepMin+"m:"+stepSec+"s";
			FileWriter write = new FileWriter(jsonFilePath+"/time.json");
			JSONObject jobject = new JSONObject();
			jobject.put("overallStartTime", overAllStartTime);
			jobject.put("overallEndTime", overAllEndTime);
			jobject.put("overallExecution", replace);
			jobject.write(write);
			write.flush();
			write.close();
			FileReader fr = new FileReader(overAllMainReport);
			String s;
			String totalStr = "";
			copyFolderAndFiles(currentExecutionPath,new File(executionPath+"/FinalResult"));
			try (BufferedReader br = new BufferedReader(fr)) {
				while ((s = br.readLine()) != null) {
					totalStr += "\n"+s;
				}
				totalStr = totalStr.replace(search, replace);
				FileWriter fw = new FileWriter(overAllMainReport);
				fw.write(totalStr);
				fw.close();
			}
			Report_Excel.updateReportExcel(currentExecutionPath, jsonArray);
			copyFolderAndFiles(currentExecutionPath,new File(executionPath+"/FinalResult"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	String getTimeDifference(long oldTime, long newTime){
		long stepHr = (newTime - oldTime)/ (60 * 60 * 1000) % 24;
		long stepMin = (newTime - oldTime)/ (60 * 1000) % 60;
		long stepSec = (newTime - oldTime)/ 1000 % 60;
		return stepHr+"h:"+stepMin+"m:"+stepSec+"s";
	}



	public void updateAssert(){
		softassert.assertAll();
	}


	File getFileScreenShot(boolean mobile)
	{
		File screenShot = new File("NA");
		
		try{
			screenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		}catch(Exception e){
			System.out.println("Unable to take screenshot due to browser unvailability.");
		}
		
		
		return screenShot;
	}
	
	PageSnapshot getFileScreenShot_test(boolean mobile)
	{
		
		PageSnapshot screenshottest = null;
		
		try{
			screenshottest = Shutterbug.shootPage(driver, ScrollStrategy.BOTH_DIRECTIONS);
		}catch(Exception e){
			
		}


		return screenshottest;
	}

	public static synchronized File getCurrentExecutionPath(){

		return currentExecutionPath;
	}
	
	/* Method name: addScreenshotToWord - This method is used to append images to doc file
	 * @Author : Rathna B
	 * @Date: 03/24/2017
	 * @Param: getImage - used to retrieve image to append
	 */
	public void addScreenshotToDocument(File getImage){
		
			try{  
				
				java.io.InputStream is = new java.io.FileInputStream(getImage );
				long length = getImage.length();    
		        // You cannot create an array using a long type.
		        // It needs to be an int type.
		        if (length > Integer.MAX_VALUE) {
		        	System.out.println("File too large!!");
		        }
		        byte[] bytes = new byte[(int)length];
		        int offset = 0;
		        int numRead = 0;
		        while (offset < bytes.length
		               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		            offset += numRead;
		        }
		        // Ensure all the bytes have been read in
		        if (offset < bytes.length) {
		            System.out.println("Could not completely read file "+getImage.getName());
		        }
		        is.close();
		        
		        String filenameHint = null;
		        String altText = null;
		        int id1 = 0;
		        int id2 = 1;
		        
		        
		        // Image 1: no width specified
		        org.docx4j.wml.P p = newImage( wordMLPackage, bytes, filenameHint, altText, id1, id2 );
		        wordMLPackage.getMainDocumentPart().addObject(p);	
	        }
			catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public static org.docx4j.wml.P newImage( WordprocessingMLPackage wordMLPackage, byte[] bytes, String filenameHint, String altText, int id1, int id2) throws Exception {
		
	    BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
		
	    Inline inline = imagePart.createImageInline( filenameHint, altText, id1, id2, false);
	    
	    // Now add the inline in w:p/w:r/w:drawing
	    
		org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
		org.docx4j.wml.P  p = factory.createP();
		org.docx4j.wml.R  run = factory.createR();		
		p.getContent().add(run);        
		org.docx4j.wml.Drawing drawing = factory.createDrawing();		
		run.getContent().add(drawing);		
		drawing.getAnchorOrInline().add(inline);
		
		return p;
		
	}	

	/* Method name: addScreenshotdocToWord - This method is used to Copy the image document to word
	 * @Author : Rathna B
	 * @Date: 03/24/2017
	 * @Param: NA
	 */
	public void addScreenshotdocToWord(){
		
		try {
			if(PropertyReader.getProperty("screenShotDoc").equalsIgnoreCase("true")){
				File fileObj = new File(currentExecutionPath.toString()+"/"+testId+"/"+testId+".docx");
				wordMLPackage.save(fileObj);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
