package com.automation.utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.openqa.selenium.WebDriver;


public class ScriptDriver {

	public static synchronized Set<Object> uniqueInstance(String packageName, List<String> pageName, Object driver, Object testId, Object report, HashMap<String, String> data)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		// Class.forName("pack1.Example1");
		GlobalDataHandler.setGlobalDataHashMap(String.valueOf(testId), data);
		Class<?>[] clsArgument={WebDriver.class, String.class, ReportStatus.class, HashMap.class};
		Object[] objectArgument = {driver, testId, report, data};
		Set<String> myUnique = new TreeSet<String>();
		Set<Object> myUniqueInstance = new LinkedHashSet<Object>();
		for(int x=0; x< pageName.size(); x++){	
			if(!pageName.get(x).split(";")[0].equalsIgnoreCase("common"))
				myUnique.add(pageName.get(x).split(";")[0]);
			//myUnique.add(pageName.get(x));
		}
		System.out.println(myUnique);
		Iterator<String> iterate = myUnique.iterator();
		while(iterate.hasNext()){
			myUniqueInstance.add(Class.forName(packageName + "." + iterate.next()).getDeclaredMethod("initialize", clsArgument).invoke(null, objectArgument));
		}
		System.out.println(myUniqueInstance);
//		Method methodBack = objInstance.getClass().getDeclaredMethod("navigateBack", null);
//		Method methodForward = objInstance.getClass().getDeclaredMethod("navigateForward", null);

		return myUniqueInstance;

	}
	
	public void runner(List<String> scenario,	Set<Object> scenarioObject) throws Exception{
		Class<?>[] clsArgument={};
		Object[] object = {};
		for(String str : scenario){
			String[] temp = str.split(";");
			String pageName = temp[0];
			String methodName = temp[1];
			for(Object obj: scenarioObject){
				if(obj.getClass().getName().contains(pageName)){
					obj.getClass().getDeclaredMethod(methodName, clsArgument).invoke(obj, object);
				}
			}
		}
	}
}