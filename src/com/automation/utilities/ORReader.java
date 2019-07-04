package com.automation.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//Class Description - used to read the object repository property from Object Repository 
//Author - Ramesh M
//Date - 31/03/2016

public class ORReader {
	
	static Properties property;
	
	public static void initializeOR() throws IOException{
		InputStream oRPropertyFilePath;
		try {
			property = new Properties();
			File file = new File(FilePaths.objectRepositoryPath.toString());
			String[] filelist = file.list();
			for(int i=0; i<filelist.length; i++){
				oRPropertyFilePath = new FileInputStream(FilePaths.objectRepositoryPath+"\\"+filelist[i]);
				property.load(oRPropertyFilePath);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Object Repository not found at the given location : "+e);
			throw e;
		} catch (IOException e1) {
			System.out.println("Object Repository got IO Exception please review : "+e1);
			throw e1;
		}
	}

	public static String getOR(String propertyValue) throws Exception{
		if(property.getProperty(propertyValue) != null){
			return property.getProperty(propertyValue);
		} else {
			throw new Exception("The Object Property is not found in OR file for the object : "+propertyValue);
		}
	}
	
	public static synchronized String getORReplaced(String objectProperty, String replaceString) throws Exception{
		return getOR(objectProperty).replace("<<<>>>", replaceString);
	}
}
