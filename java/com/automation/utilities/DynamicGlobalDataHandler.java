package com.automation.utilities;

import java.util.HashMap;

public class DynamicGlobalDataHandler {
	
	public static HashMap<String,HashMap<String,Object>> dynamicGlobalData;
	public static HashMap<String,Object> dynamicData;
	
	public DynamicGlobalDataHandler(){
		dynamicGlobalData = new HashMap<>();
		dynamicData=new HashMap<>();
	}
	
	public static synchronized void intitalizeDynamicGlobalData(String globalDataKey){
		dynamicGlobalData.put(globalDataKey, null);
	}
	
	public static synchronized void setDynamicGlobalDataHashMap(String globalDataKey, String hashMapDataKey,Object object){
		dynamicData.put(hashMapDataKey,object);
		dynamicGlobalData.put(globalDataKey,dynamicData);
	}
	
	public static synchronized Object getDynamicGlobalDataHashMap(String globalDataKey,String hashMapDataKey){
		HashMap<String,Object>data=dynamicGlobalData.get(globalDataKey);
		return data.get(hashMapDataKey);
	}
	
	
	
	
}
