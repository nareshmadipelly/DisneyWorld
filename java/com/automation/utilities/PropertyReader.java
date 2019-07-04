package com.automation.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
	static Properties property;
	public PropertyReader() throws IOException{
		InputStream configFilePath;
		try {
			property = new Properties();
			configFilePath = new FileInputStream(FilePaths.propertyFilePath);
			property.load(configFilePath);
		} catch (IOException e1) {
			System.out.println("Object Repository got IO Exception please review : "+e1);
			throw e1;
		}
	}
	
	public static String getProperty(String propertyValue) throws Exception{
		if(property.getProperty(propertyValue) != null){
			return property.getProperty(propertyValue);
		} else {
			throw new Exception("The Object Property is not found in OR file for the object : "+propertyValue);
		}
	}
}
