package com.automation.utilities;

import java.util.HashMap;

public class GlobalDataHandler {
	
	public static HashMap<String, HashMap<String,String>> globalData;
	
	public GlobalDataHandler(){
		globalData = new HashMap<String,HashMap<String,String>>();
	}
	
	public static synchronized void intitalizeGlobalData(String testId){
		globalData.put(testId, null);
	}
	
	public static synchronized void setGlobalDataHashMap(String testId, HashMap<String,String> index){
		globalData.put(testId, index);
	}
	
	public static synchronized HashMap<String,String> getGlobalDataHashMap(String testId){
		return globalData.get(testId);
	}
	
	public static synchronized void setGlobalData(String testId, String globalDataName,String globalDataValue){
		globalData.get(testId).put(globalDataName,globalDataValue);
	}
	
	public static synchronized String getGlobalData(String testId,String globalDataName)
	{	
		return getGlobalDataHashMap(testId).get(globalDataName);
	}
	
	
}
