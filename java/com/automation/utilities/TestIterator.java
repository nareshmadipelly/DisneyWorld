package com.automation.utilities;

import java.util.HashMap;

public class TestIterator {
	
	public static HashMap<String, Integer> iterator;
	
	public TestIterator(){
		iterator = new HashMap<String,Integer>();
	}
	
	public static synchronized void intitalizeIterator(String testId){
		iterator.put(testId, 0);
	}
	
	public static synchronized void setIterator(String testId, int index){
		iterator.put(testId, index);
	}
	
	public static synchronized int getIterator(String testId){
		return iterator.get(testId);
	}
}
