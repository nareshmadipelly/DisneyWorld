package com.automation.utilities;

import java.util.HashMap;

public abstract class HashData{

	String testId;
	int iterator;
	
	
	protected HashData(HashMap<String,String> data, ReportStatus report){
		this.testId = report.testId;
		this.iterator = TestIterator.getIterator(testId);
	}
	
	public String get(String key){
		try{
			if(iterator == 0)
				return GlobalDataHandler.getGlobalDataHashMap(testId).get(key);
			else if(!key.contains("$"))
				return GlobalDataHandler.getGlobalDataHashMap(testId).get(key+"$*$"+iterator);
			else
				return GlobalDataHandler.getGlobalDataHashMap(testId).get(key);
		}catch(NullPointerException e){
			return "";
		}
	}
	
	public void put(String key, String value){
		GlobalDataHandler.setGlobalData(testId, key, value);
	}
	
	public HashMap<String,String> getHashMap(){
		return GlobalDataHandler.getGlobalDataHashMap(testId);
		
	}
	
	public void setIterator(){
		iterator = TestIterator.getIterator(testId);
	}
}
