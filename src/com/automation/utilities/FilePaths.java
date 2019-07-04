package com.automation.utilities;

import java.io.File;

public class FilePaths {
	
	public static File propertyFilePath = new File("./Files/config.properties");
	public static File objectRepositoryPath = new File("./ObjectRepository/");
	public static File testDataSheetPath = new File("./Files/");
	public static File ReportOutput = new File("./Report/Output/");
	public static File scenarioListPath = new File("");
	public static File testcasesListPath = new File("");
	public static File iEDriverServer = new File("src/test/resources/IEDriverServer.exe");
	public static File chromeDriverServer = new File("src/test/resources/chromedriver.exe");
	public static File phantomJSServer = new File("src/test/resources/phantomjs.exe");
	public static File finalResultOutput = new File("./Report/Execution_Report/FinalResult");
	public static File log4jConfigPath = new File("./Files/log4j-config.xml");

}
