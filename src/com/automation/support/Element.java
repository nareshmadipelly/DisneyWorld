package com.automation.support;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

import com.automation.functionallibrary.CustomElement;

@ImplementedBy(CustomElement.class)
public interface Element extends WebElement, WrapsElement, Locatable {
	boolean elementWired();
	
	@DynamicObject
	WebElement getElement(String orValue);
	
	@DynamicObject
	@DynamicTimeOut
	WebElement getElement(String orValue, int timeOut);
	
	@DynamicTimeOut
	WebElement getElement(int timeOut);
	
	String getElementName();
	
}
