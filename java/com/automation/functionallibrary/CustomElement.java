package com.automation.functionallibrary;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;

import com.automation.support.Element;

public class CustomElement implements Element {

	private final WebElement element;
	private String elementName;

	public CustomElement(final WebElement element) {
		this.element = element;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public String getElementName() {
		return elementName;
	}

	public void click() {
		element.click();
	}

	public void submit() {

	}

	public WebElement getElement(String orValue) {
		return this.element;
	}

	public void sendKeys(CharSequence... keysToSend) {
		element.sendKeys(keysToSend);
	}

	public String getTagName() {
		return element.getTagName();
	}

	public String getAttribute(String name) {
		return element.getAttribute(name);
	}

	public boolean isSelected() {
		return element.isSelected();
	}

	public boolean isEnabled() {
		return element.isEnabled();
	}

	public String getText() {
		return element.getText();
	}

	public List<WebElement> findElements(By by) {
		return element.findElements(by);
	}

	public WebElement findElement(By by) {
		return element.findElement(by);
	}

	public boolean isDisplayed() {
		return element.isDisplayed();
	}

	public Point getLocation() {
		return element.getLocation();
	}

	public Dimension getSize() {
		return element.getSize();
	}

	public String getCssValue(String propertyName) {
		return element.getCssValue(propertyName);
	}

	public WebElement getWrappedElement() {
		return null;
	}

	public Coordinates getCoordinates() {
		return null;
	}

	public boolean elementWired() {
		return false;
	}

	public void clear() {

	}

	public Rectangle getRect() {
		// TODO Auto-generated method stub
		return null;
	}

	public <X> X getScreenshotAs(OutputType<X> arg0) throws WebDriverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebElement getElement(String orValue, int timeOut) {
		return this.element;
	}

	@Override
	public WebElement getElement(int timeOut) {
		return this.element;
	}
	
	

}
