package com.automation.support;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.automation.utilities.ReportStatus;


public class ElementFactory extends PageFactory {
    public static <T> T initElements(WebDriver driver, Class<T> pageClassToProxy, String testId, ReportStatus report, HashMap<String, String> data) {
        try {
            T page = pageClassToProxy.getConstructor(WebDriver.class, String.class, ReportStatus.class, HashMap.class).newInstance(driver, testId,report,data);
            PageFactory.initElements(new ElementDecorator(new CustomElementLocatorFactory(driver)), page);
            return page;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}