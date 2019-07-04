package com.automation.utilities;

public class StackMessage {
	public static synchronized String getStackTraceMessage(StackTraceElement[] stackTraceElements){
		String trace = "";
		System.out.println(stackTraceElements.length);
		for(int i=0; i < stackTraceElements.length; i++){
			if(stackTraceElements[i].getClassName().contains("com.automation"))
			trace = trace +"["+stackTraceElements[i].getClassName() 
					+"."+ stackTraceElements[i].getMethodName() 
					+" : Line no -  "+ stackTraceElements[i].getLineNumber()
					+ "] <br>";
		}
		return trace;
	}
}
