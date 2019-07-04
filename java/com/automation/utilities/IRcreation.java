package com.automation.utilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.poi.ss.usermodel.*;


public class IRcreation {
	
	   static String userName = null;
	   static String password = null;
	   static Properties prop = new Properties();
	   static int statusint,Descriptionint,Summaryint,testNameint,Jiraidfind,oCcurenceCountdetail;
	   static boolean DuplicateFound,Original;
	   static String Validation1,IRNum,Summary,Status;  
	   static Map<String , String> DuplicateIRList = new HashMap<String, String>();
	   static Map<String , String> Transition = new HashMap<String, String>();
	   static Map<String ,Integer> OccurenceCountList = new HashMap<String, Integer>();
	   static HttpGet request = null;
       static String encodingGet,KeyValue;
       static String IROutput=null;
		
	   @SuppressWarnings("resource")
	    public static void IrLogic() {
		   
		       //Reading Properties file	
			     try{
				      InputStream input = null;
				      FileInputStream fileInputStream = null;
			          Workbook workbook = null;
			          try{
		    		       String filePath=new File(System.getProperty("user.dir")).getAbsolutePath()+"/Files/config.properties"; 	    		
		    		       input = new FileInputStream(filePath);
		    		       prop.load(input);
		    	      }
		    	      catch(Exception e){}
		    
			   	      //Decode the User name and Password
		    	        byte[] decodedBytes = Base64.decodeBase64(prop.getProperty("username"));
				        userName = new String(decodedBytes);
				        byte[] decodedBytes1 = Base64.decodeBase64(prop.getProperty("password"));
				        password = new String(decodedBytes1);
		       
				
	                  //Fetching Old IR's List	
		                request = new HttpGet("https://onejira.verizon.com/rest/api/2/search?jql=project=ITT+AND+status!=close+AND+issuetype=Bug+AND+'Sub+Project'~simplex&startAt=0&maxResults=1000");		  
	                    encodingGet = new String(Base64.encodeBase64((userName.toString()+":"+password.toString()).getBytes()));
	                    request.setHeader("Authorization", "Basic " + encodingGet);	   		   
	                    try{
		                     @SuppressWarnings("deprecation")
					          DefaultHttpClient httpClient = new DefaultHttpClient();	        
		                      HttpResponse response = httpClient.execute(request);		    
	                          HttpEntity httpEntity = response.getEntity();
	                          IROutput = EntityUtils.toString(httpEntity);	  
	                          System.out.println(IROutput);
	                          JSONObject jsonObj = new JSONObject(IROutput);
	                          JSONArray projectArray = jsonObj.getJSONArray("issues");              
	                          for (int i = 0; i <= projectArray.length()-1; i++) {	        	
	        	                   JSONObject proj=projectArray.getJSONObject(i);
	        	                   IRNum=proj.get("key").toString();
	        	                   Summary=proj.getJSONObject("fields").get("summary").toString();
	        	                   Status=proj.getJSONObject("fields").getJSONObject("status").getString("name").toString();
	        	                  
	        	                   try{
	        	                   oCcurenceCountdetail=Integer.parseInt(proj.getJSONObject("fields").get("customfield_11304").toString());
	        	                   OccurenceCountList.put(IRNum, oCcurenceCountdetail);
	        	                   Status=proj.getJSONObject("fields").getJSONObject("status").getString("name").toString();
	        	                   Transition.put(IRNum, Status);
	        	                   }
	        	                   catch(Exception e){	        	                	  
	        	                	     oCcurenceCountdetail=0;
	        	                	     OccurenceCountList.put(IRNum, oCcurenceCountdetail);
	        	                	     Transition.put(IRNum, Status);
	        	                   }
	        	                   if(!DuplicateIRList.containsKey(Summary)){
	        	                       DuplicateIRList.put(Summary, IRNum);
	        	                   }
	        	 	          }
	                    }
	                    catch(Exception e){}
	    	   	     			
			         //Read Excel Sheet
	    	           String fileName = new File(System.getProperty("user.dir")).getAbsolutePath()+"/Report/Execution_Report/FinalResult/ExcelReport.xlsx";	   	
	    	           fileInputStream = new FileInputStream(fileName);
			           if (fileName.endsWith(".xlsx")){
				           workbook = new XSSFWorkbook(fileInputStream);
			           }else if (fileName.endsWith(".xls")){
				           workbook = new HSSFWorkbook(fileInputStream);
			           }else if (fileName.endsWith(".xlsm")){
				           workbook = WorkbookFactory.create(fileInputStream);
			           }
			           
			         //Validation  
			           if(workbook != null){			
				          FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				          Sheet sheet = workbook.getSheetAt(0);				
				          int Rowcount=sheet.getLastRowNum()-sheet.getFirstRowNum();				
				          Row row = sheet.getRow(0);
				          for (int j = 0; j < row.getLastCellNum(); j++) {
	           		           if(row.getCell(j).getStringCellValue().equals("status")){
			        	           statusint=j;
			                   }			          
			                   if(row.getCell(j).getStringCellValue().equals("session")){
			        	           Descriptionint=j;
			                   }			          
			                   if(row.getCell(j).getStringCellValue().equals("ErrorMessage")){
			        	           Summaryint=j;
			                   }			          
			                   if(row.getCell(j).getStringCellValue().equals("testId")){
			        	           testNameint=j;
			                   }			          
				          }
				               for(int i=1; i<=Rowcount;i++){
				            	   DuplicateFound=false;
				            	   String Trsns="";
					               Row rownew = sheet.getRow(i);
					               String StatusCheck=rownew.getCell(statusint).getStringCellValue().trim();					
					               String SummaryCheck=rownew.getCell(Summaryint).getStringCellValue().trim();
					               String testNameCheck=rownew.getCell(testNameint).getStringCellValue().trim();
					               String DescriptionCheck=rownew.getCell(Descriptionint).getStringCellValue().trim();
				                   if(StatusCheck.equals("Failed")) {
				                	  //Iterate over HashMap
				                       for(String key: DuplicateIRList.keySet()){
				                           if(key.equals(SummaryCheck)){
				                        	   DuplicateFound=true;
				                        	   KeyValue=DuplicateIRList.get(key);
				                           }
				                       }
				                          
				                       if(!DuplicateFound){ 				                    	  
				                    	   String occurt="1";
				                    	   if(!SummaryCheck.isEmpty()){
				                           String data="{\"fields\":{\"project\":{\"key\":\"ITT\"},\"summary\": \""+SummaryCheck+"\",\"description\": \""+DescriptionCheck+"\",\"customfield_35706\":{\"value\":\"COAC\"},\"customfield_10683\":{\"value\":\"Medium\"},\"customfield_11847\":{\"value\":\"COA (EIQ) - CONSUMER\"},\"customfield_11304\":\""+occurt+"\",\"customfield_10823\":\"Simplex Regression\",\"customfield_48700\":[\"PRI00496052 - eTNI(Enterp Telep Numb Invent) ~ Sales ID ? Primary Place of Use Address Accurac\"],\"customfield_11855\":{\"value\":\"November 2016\"},\"customfield_10500\":{\"value\":\"BAU\"},\"customfield_10677\":{\"value\":\"Automation Testing Team\"},\"customfield_10513\":\""+testNameCheck+"\",\"customfield_11844\":1,\"customfield_10511\":{\"value\":\"Regression\"},\"issuetype\":{\"name\":\"Bug\"}}}";
				                           HttpPost post = new HttpPost("https://onejira.verizon.com/rest/api/2/issue");		      
				    	                   String encoding = new String(Base64.encodeBase64((userName.toString()+":"+password.toString()).getBytes()));
				                           post.setHeader("Authorization", "Basic " + encoding);     
				                           StringEntity input1 = new StringEntity(data, "UTF-8");
				 	                       input1.setContentType("application/json");
				 		                   post.setEntity(input1);
				 		                   HttpResponse response = new DefaultHttpClient().execute(post);
				 		                   int responseCode = response.getStatusLine().getStatusCode();				 		 
				 		                   HttpEntity httpEntity = response.getEntity();	
				 		                   String apiOutput = EntityUtils.toString(httpEntity);
				 		                   System.out.println(apiOutput);
				 		 			       String  Splitoutput=apiOutput.split(",")[1].split(":") [1].replaceAll("\"", "");	
				 		 			       DuplicateIRList.put(SummaryCheck, Splitoutput);
				 		 			       OccurenceCountList.put(Splitoutput, 1);
				 		                   WritetoExcel(i,Splitoutput);}
				                    	   else{
				                    		   WritetoExcel(i,"Error Message Should not be blank for Failed Testcases");
				                    	   }
				                       }
				                       else{
				                    	   int occ=OccurenceCountList.get(KeyValue);
				                    	   Trsns=Transition.get(KeyValue).toString();
				                    	   UpdateEpicLink(KeyValue,DescriptionCheck,occ);
				                    	   WritetoExcel(i,KeyValue);
				                    	   if(!Trsns.equals("New")){
				                    	   UpdateStatus(KeyValue,DescriptionCheck,OccurenceCountList.get(KeyValue));}
				                       }
				                   }    
				                  	   
					   				      				    
				                }
			               }
			
		         }
				 catch(Exception ex){				
			          System.out.println("System error happened. Please contact Administrator: " + ex);
			     }
	    }
	
	 public static void WritetoExcel(int i, String keyIssue) throws IOException{
	
		   String fileNamenew = new File(System.getProperty("user.dir")).getAbsolutePath()+"/Report/Execution_Report/FinalResult/ExcelReport.xlsx";
		   FileInputStream fsIP= new FileInputStream(new File(fileNamenew)); 
           XSSFWorkbook wb = new XSSFWorkbook(fsIP); 
           XSSFSheet worksheet = wb.getSheet("Sheet0");
		   int Rowcount=worksheet.getLastRowNum()-worksheet.getFirstRowNum();
		   Row row = worksheet.getRow(0);
           Cell cell = null;       
           for(int j=0;j<=row.getLastCellNum();j++){
        	   String fetchvalue=row.getCell(j).getRichStringCellValue().toString().trim();
        	   if(fetchvalue.equals("JiraID")){
        		  Jiraidfind=j;
        		  break;
        	   }        	 
           }    
           try{
                cell = worksheet.getRow(i).createCell(Jiraidfind);
            	cell.setCellValue(keyIssue);
           }    
           catch(Exception e){ System.out.println(e.getMessage());}
    	   fsIP.close(); 
           FileOutputStream output_file =new FileOutputStream(fileNamenew); 
           wb.write(output_file); 
           output_file.close();  
	 }
	 
	 public static void UpdateEpicLink(String Key,String Descriptio,int OccurenceCount) throws ClientProtocolException, IOException{
		    
		int OccurenceCountFinal=OccurenceCount+1;
		    String data="{\"fields\": {\"customfield_11304\": \""+OccurenceCountFinal+"\",\"description\": \""+Descriptio+"\"}}";
			try{
			     HttpPut post = new HttpPut("https://onejira.verizon.com/rest/api/2/issue/"+Key);		      
			     String encoding = new String(Base64.encodeBase64((userName.toString()+":"+password.toString()).getBytes()));
		         post.setHeader("Authorization", "Basic " + encoding);     
		       	 StringEntity input1 = new StringEntity(data, "UTF-8");
		 	     input1.setContentType("application/json");
		 		 post.setEntity(input1);
		 		 HttpResponse response = new DefaultHttpClient().execute(post);
		 		 int responseCode = response.getStatusLine().getStatusCode();		 		 
		 		 HttpEntity httpEntity = response.getEntity();	
		 		 String apiOutput = EntityUtils.toString(httpEntity);	
		 	}
			catch(Exception e){}
			
	 }
	 
	 public static void UpdateStatus(String Key,String Descriptio,int OccurenceCount) throws ClientProtocolException, IOException{
		    
			 int OccurenceCountFinal=OccurenceCount+1;
			 String data="{\"update\": {\"comment\": [{\"add\": {\"body\": \"Returning to Dev\"}}] },\"transition\": {\"id\": \"211\"}}";
				try{
				     HttpPost post = new HttpPost("https://onejira.verizon.com/rest/api/2/issue/ITT-288458/transitions?expand=transitions.fields");		      
				     String encoding = new String(Base64.encodeBase64((userName.toString()+":"+password.toString()).getBytes()));
			         post.setHeader("Authorization", "Basic " + encoding);     
			       	 StringEntity input1 = new StringEntity(data, "UTF-8");
			 	     input1.setContentType("application/json");
			 		 post.setEntity(input1);
			 		 HttpResponse response = new DefaultHttpClient().execute(post);
			 		 int responseCode = response.getStatusLine().getStatusCode();		 		 
			 		 HttpEntity httpEntity = response.getEntity();	
			 		 String apiOutput = EntityUtils.toString(httpEntity);
			 		 System.out.println(apiOutput);
			 	}
				catch(Exception e){}
				
		 }
	 
  }

