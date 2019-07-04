package com.automation.page;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.automation.functionallibrary.CustomDriver;
import com.automation.support.Element;
import com.automation.support.ElementFactory;
import com.automation.utilities.ReportStatus;
import com.automation.utilities.UserDefinedException;

public class SamplePage extends CustomDriver {
	@FindBy(xpath="//input[@id='<<<>>>']")
	private Element element1;
	@FindBy(xpath="//input[@name='<<<>>>']")
	private Element element2;

	public SamplePage(WebDriver driver2, String mobile, ReportStatus report, HashMap<String, String> data) {
		super(driver2, false, data, report);
	}
	
	public static SamplePage initialize(WebDriver driver, String testId, ReportStatus report, HashMap<String, String> data) {
		return ElementFactory.initElements(driver, SamplePage.class, testId, report, data);
	}
	
	public void start() throws Exception{
		openWebsite("http://www.google.com");
		setText(element1, "lst-ib", "find by working");
		click(element2, "btnK");
		driver.quit();
		System.out.println();
	}
}
