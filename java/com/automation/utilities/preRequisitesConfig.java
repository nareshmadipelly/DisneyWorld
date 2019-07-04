package com.automation.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class preRequisitesConfig {

	public static String[][] devices;

	@BeforeSuite
	public void configPreRequisites() throws Exception{
		
		ReportStatus.initializeReportStatus();
		new PropertyReader();
		new TestIterator();
		new GlobalDataHandler();
		//new DynamicGlobalDataHandler();
		new FileWriter("./Report/output.log").close();
		
	}

	@AfterSuite
	public void updateHTML() throws Exception{
		ReportStatus.updateHTMLResult();

		if(PropertyReader.getProperty("createIR").toUpperCase().equalsIgnoreCase("yes"))
		{
			IRcreation.IrLogic();
		}

		if(PropertyReader.getProperty("transferToDB").toUpperCase().equalsIgnoreCase("yes"))
		{
			try {
				ConnectDB.readExcelDataAndUpdateDB();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException
					| IOException e) {
				e.printStackTrace();
			}
		}

	}
		
}
