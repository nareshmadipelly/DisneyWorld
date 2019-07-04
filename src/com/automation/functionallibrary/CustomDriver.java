package com.automation.functionallibrary;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.json.JSONException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.support.CList;
import com.automation.support.Element;
import com.automation.utilities.HashData;
import com.automation.utilities.ReportStatus;
import com.automation.utilities.UserDefinedException;
import com.google.common.base.Function;

/**
 * CustomDriver class is the base class.This class contains common reusable
 * methods to interact with elements.
 */
public abstract class CustomDriver extends HashData {

	public static long TimeOutValue;
	public static long PollingInterval;
	protected WebDriver driver;
	Actions action;
	ReportStatus report;


	/**
	 * Custom driver constructor initialize the driver
	 * 
	 * @param driver2
	 * @param mobile
	 */
	public CustomDriver(WebDriver driver2, boolean mobile,
			HashMap<String, String> data, ReportStatus report) {
		super(data, report);
		this.report=report;
		this.driver = driver2;
		action = new Actions(driver);
	}

	/***
	 * waitForElementDisplay method wait till the element display
	 * 
	 * @param element
	 * @param objectValue
	 * @param timeOut
	 * @throws UserDefinedException
	 * @throws IOException 
	 * @throws JSONException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void waitForElementDisplay(final Element element,
			final String objectValue, final int timeOut) throws UserDefinedException, JSONException, IOException {
		try {
			FluentWait fWait = new FluentWait(element).pollingEvery(500,
					TimeUnit.MILLISECONDS).withTimeout(timeOut,
							TimeUnit.SECONDS);
			fWait.until(new Function<Element, Boolean>() {
				@Override
				public Boolean apply(Element input) {
					try {
						return isDisplayed(element, objectValue,timeOut);
					} catch (NoSuchElementException x) {
						System.out.println("waiting for Object to load "
								+ input.toString());
						return false;
					}
				}
			});
		} catch (Exception e) {
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;
		}
	}

	/**
	 * getElement - Used to retrieve the element value
	 * 
	 * @param element
	 *            represents the element value using which element needs to be
	 *            retrieved
	 * 
	 * @param objectValue
	 *            represents the value which needs to be used when dynamic
	 *            xpaths are used
	 * 
	 */
	protected WebElement getElement(Element element, String objectValue) {
		try {
			return element.getElement(objectValue);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * getElement - Used to retrieve the element value
	 * 
	 * @param element
	 *            represents the element value using which element needs to be
	 *            retrieved
	 * 
	 * @param objectValue
	 *            represents the value which needs to be used when dynamic
	 *            xpaths are used
	 *            
	 * @param Time
	 *            represents the time which overrides the global timeout variable
	 *            
	 * 
	 */
	protected WebElement getElement(Element element, String objectValue,int Time) {
		try {
			return element.getElement(objectValue,Time);
		} catch (Exception e) {

			return null;
		}
	}

	/**
	 * getelements To get the list of WebElements
	 * 
	 * @param element
	 *            represents the element from which list of elements needs to be
	 *            retrieved
	 * 
	 * @param objectValue
	 *            represents the String which needs to be passed when dynamic
	 *            xpath values are given
	 * 
	 * @param by
	 *            represents the String using which by value needs to be
	 *            retrieved
	 * 
	 * 
	 */
	public CList<Element> getElementList(CList<Element> element,String objectValue) 
	{
		return element.getList(objectValue);
	}

	/**
	 * ClickUsingJavaScript - Method is used to click on element using Java
	 * 
	 * @param element
	 *            represents the element value which needs to be clicked using
	 *            Java
	 * 
	 * @param objectValue
	 *            represents the value which needs to be used when dynamic
	 *            xpaths are used
	 * 
	 */
	public void clickUsingJavaScript(Element element, String objectValue)
			throws Exception {


		WebElement element1 = getElement(element, objectValue);

		if (element1 != null) {
			JavascriptExecutor executer = (JavascriptExecutor) driver;
			executer.executeScript("arguments[0].click();", element1);
		} else {

			report.updateMainReport("xpathDetails", "Click Element: "+element.getElementName().replace("<<<>>>", objectValue));
			throw new Exception(
					"Selenium Unable to find the object, Check xpath or object not visible in screen - "
							+ objectValue);
		}
	}

	/**
	 * Click - Method is used to click on element
	 * 
	 * @param element
	 *            represents the element value which needs to be clicked
	 * 
	 * @param objectValue
	 *            represents the value which needs to be used when dynamic
	 *            xpaths are used
	 * @throws Exception 
	 * 
	 */
	public void click(Element element, String objectValue) throws Exception 
	{
		WebElement element1 =getElement(element, objectValue);

		if (element1 != null) 
		{
			element1.click();
		} else {

			report.updateMainReport("xpathDetails", "Click Element: "+element.getElementName().replace("<<<>>>", objectValue));
			throw new Exception(
					"Selenium Unable to find the object, Check xpath or object not visible in screen - "
							+ objectValue);
		}
	}

	/**
	 * Click - Method is used to click on element
	 * 
	 * @param element
	 *            - Element value which needs to be clicked
	 * 
	 */
	public void click(WebElement element) {

		element.click();
	}

	/**
	 * waitforSpinner - Method is used to wait until the spinner is loaded and
	 * page is visible for operation
	 * 
	 * 
	 * @param elementValue
	 *            represents the value which needs to be passed as an input to
	 *            check whether spinner is active
	 * 
	 * @return elementValue represents the string for which fluent needs to wait
	 */

	public void waitforSpinner(final String elementValue) {
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)

					.withTimeout(10, TimeUnit.SECONDS)

					.pollingEvery(2, TimeUnit.SECONDS)

					.ignoring(NoSuchElementException.class);

			WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {

					while (driver.findElement(By.xpath(elementValue))
							.getAttribute("class").contains("active")) {

					}
					return driver.findElement(By.xpath(elementValue));

				}

			});
		} catch (Exception e) {

			throw e;
		}
	}

	/**
	 * SwitchToWindow method can be used for switching to window based on Title
	 * 
	 * @param title
	 *            represents the name of window title
	 * 
	 * @throws Exception
	 * 
	 */
	public void switchToWindow(String title) throws Exception {

		WebDriver nextWindow = null;
		Set<String> windowIterator = driver.getWindowHandles();
		int noOfWindows = windowIterator.size();
		int i = 0;
		System.out.println("No of windows :  " + noOfWindows);

		for (String s : windowIterator) {

			String windowHandle = s;
			System.out.println("Window Id = " + windowHandle);
			nextWindow = driver.switchTo().window(windowHandle);
			System.out.println("Window Title : " + nextWindow.getTitle());
			System.out.println("Window Url : " + nextWindow.getCurrentUrl());
			if (nextWindow.getTitle().equals(title)) {
				System.out.println("Selected Window Title : "
						+ nextWindow.getTitle());
				break;
			} else {
				i++;
				continue;
			}

		}
		if (i == noOfWindows) {
			throw new UserDefinedException(
					"Windows Title is not present in the list of opened windows");
		}
	}

	/**
	 * SwitchToWindow method can be used for switching to window based on Title
	 * 
	 * @param title
	 *            represents the name of window title
	 * 
	 * @param element
	 *            represent the element which needs to verified when the switch
	 *            is done
	 * 
	 */
	public void switchToWindow(String title, Element element) throws Exception {
		WebDriver nextWindow = null;
		Set<String> windowIterator = driver.getWindowHandles();
		int noOfWindows = windowIterator.size();
		int i = 0;
		for (String s : windowIterator) {
			String windowHandle = s;
			nextWindow = driver.switchTo().window(windowHandle);
			if (nextWindow.getTitle().equals(title) && isDisplayed(element)) {
				break;
			} else {
				i++;
				continue;
			}

		}
		if (i == noOfWindows) {
			throw new UserDefinedException(
					"Windows Title is not present in the list of opened windows");
		}
	}

	/**
	 * openWebsite Open the url based on given input
	 * 
	 * @param url
	 *            which needs to be opened
	 */
	public void openWebsite(String url) {
		driver.get(url);
	}

	/**
	 * setText Used to set the text in given element
	 * 
	 * @param element
	 *            represents the element for which the text needs to be given
	 * 
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            xpaths are used
	 * 
	 * @param textvalue
	 *            represents the string which needs to be entered into the
	 *            element
	 * @throws UserDefinedException
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public void setText(Element element, String objectValue, String textValue)
			throws UserDefinedException, JSONException, IOException {
		try {
			getElement(element, objectValue).sendKeys(textValue);
		} catch (Exception e) {
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;

		}
	}

	/**
	 * clearText
	 * 
	 * @param element
	 *            represents the element for which the text needs to be given
	 * 
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            xpaths are used
	 * 
	 */
	public void clearText(Element element, String objectValue) throws Exception 
	{	
		try {
			getElement(element, objectValue).sendKeys(Keys.HOME,
					Keys.chord(Keys.SHIFT, Keys.END, Keys.DELETE));
		} catch (Exception e) {
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;

		}

	}

	/**
	 * Switch to default content
	 * 
	 * @throws InterruptedException
	 */
	public void switchToDefaultcontent() throws InterruptedException {
		driver.switchTo().defaultContent();
	}

	/**
	 * switchToGivenFrame To switch to a given frame
	 * 
	 * @param frameName
	 *            represents the framename to which control needs to be
	 *            transfered
	 * 
	 */
	public void switchToFrame(String frameName) throws InterruptedException 
	{
		try {
			driver.switchTo().frame(frameName);
		} catch (Exception e) {
			System.out.println("Switching to Frame was not successful");
		}
	}

	/**
	 * 
	 * closeSession To close the given session
	 * 
	 */
	public void closeActiveWindow() {
		driver.close();
	}

	/**
	 * 
	 * maximizeBrowserWindow To maximize the browser window
	 * 
	 */
	public void maximizeBrowserWindow() {
		driver.manage().window().maximize();
	}

	/**
	 * 
	 * ReturnToPreviousPage To go to previous page
	 * 
	 */
	public void goToPreviousPage() {
		driver.navigate().back();
	}

	/**
	 * 
	 * goToLastActivePageGo To go to next active page
	 * 
	 */
	public void goToNextActivePage() {
		driver.navigate().forward();
	}

	/**
	 * 
	 * returnURL To get the current url from page
	 * 
	 * @return
	 */
	public String returnURL() {
		return driver.getCurrentUrl();
	}

	/**
	 * selectDropDown To select the particular text from dropdown
	 * 
	 * @param element
	 *            represents the element which needs to be selected to trigger
	 *            the dropdown
	 * 
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            xpath is given
	 * 
	 * @param SelectString
	 *            represents the string which needs to be selected from dropdown
	 * 
	 */
	public void selectDropDownUsingVisibleText(Element element,
			String objectValue, String selectVisibleText) throws Exception {
		try {
			Select select = new Select(getElement(element, objectValue));
			select.selectByVisibleText(selectVisibleText);

		} catch (Exception e) {
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;
		}
	}

	/**
	 * selectDropDown To select the particular text from dropdown
	 * 
	 * @param element
	 *            represents the element which needs to be selected to trigger
	 *            the dropdown
	 * 
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            xpath is given
	 * 
	 * @param selectValue
	 *            represents the value which needs to be selected from dropdown
	 * 
	 */
	public void selectDropDownUsingValue(Element element, String objectValue,
			String selectValue) throws Exception {
		try {
			Select select = new Select(getElement(element, objectValue));
			select.selectByValue(selectValue);

		} catch (Exception e) {
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;
		}
	}

	/**
	 * selectDropDown To select the particular text from dropdown
	 * 
	 * @param element
	 *            represents the element which needs to be selected to trigger
	 *            the dropdown
	 * 
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            xpath is given
	 * 
	 * @param selectValue
	 *            represents the index of value which needs to be selected from dropdown
	 * 
	 */
	public void selectDropDownUsingIndex(Element element, String objectValue,
			int selectIndex) throws Exception {
		try {
			Select select = new Select(getElement(element, objectValue));
			select.selectByIndex(selectIndex);

		} catch (Exception e) {
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;
		}
	}

	/**
	 * getTextFromElement To get the text from the given element
	 * 
	 * @param element
	 *            represents the element from which text to be retrieved
	 * 
	 * @param objectValue
	 *            represents the string which needs to be used when dynamic
	 *            x-path is given
	 * 
	 * @return Return the element's text value
	 * 
	 * @throws Exception
	 */
	public String getTextFromElement(Element element, String objectValue)
			throws Exception {

		WebElement webElement=getElement(element,objectValue);
		String elementText;
		try {
			elementText = webElement.getText();
		} catch (Exception e) {
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;
		}
		return elementText;

	}

	/**
	 * isEnabled Verify whether the element is enabled or not
	 * 
	 * @param element
	 *            represents the element which needs to be verified whether its
	 *            enabled
	 * 
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            xpath is given
	 * 
	 */
	public boolean isEnabled(Element element, String objectValue)
			throws Exception {
		try {
			if (getElement(element, objectValue).isEnabled()) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchElementException e) {
			return false;
		} catch (NullPointerException e1) {
			return false;
		} 
	}

	/**
	 * isEnabled Verify whether the element is enabled or not
	 * 
	 * @param element
	 *            represents the element which needs to be verified whether its
	 *            enabled
	 * 
	 * @param Time
	 *            represents the integer which needs to be given to set fluent
	 *            wait time
	 * 
	 */
	public boolean isEnabled(WebElement element) throws Exception {
		try {

			if (element.isEnabled()) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchElementException e) {
			return false;
		} catch (NullPointerException e1) {
			return false;
		} 
	}

	/**
	 * isSelected Verify whether the element is selected or not
	 * 
	 * @param element
	 *            represents the element which needs to be verified whether its
	 *            enabled
	 * 
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            xpath is given
	 * 
	 * @return
	 */
	public boolean isSelected(Element element, String objectValue)
			throws Exception {
		try {
			if (getElement(element, objectValue).isSelected())
				return true;
			else
				return false;
		} catch (NoSuchElementException e) {
			return false;
		} catch (NullPointerException e1) {
			return false;
		} 
	}

	/**
	 * isSelected Verify whether the element is selected or not
	 * 
	 * @param element
	 *            represents the element which needs to be verified whether its
	 *            enabled
	 * 
	 * @return
	 */
	public boolean isSelected(WebElement element) throws Exception {
		try {
			if (element.isSelected())
				return true;
			else
				return false;
		} catch (NoSuchElementException e) {
			return false;
		} catch (NullPointerException e1) {
			return false;
		} 
	}

	/**
	 * isDisplayed Verify whether the element is displayed or not
	 * 
	 * @param element
	 *            represents the element which needs to be verified whether the
	 *            element is displayed
	 * 
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            xpath is given
	 * 
	 * @return
	 */

	public boolean isDisplayed(WebElement element) throws Exception {
		try {
			if (element.isDisplayed())
				return true;
			else
				return false;
		} catch (NoSuchElementException e) {
			return false;
		} catch (NullPointerException e1) {
			return false;
		}
	}

	/**
	 * isDisplayed Verify whether the element is selected or not
	 * 
	 * @param element
	 *            represents the element which needs to be verified whether its
	 *            displayed or not
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            xpath is given
	 *            
	 * @return
	 */
	public boolean isDisplayed(Element element, String objectValue) {
		try {

			if (getElement(element, objectValue).isDisplayed())
				return true;
			else
				return false;
		} catch (NoSuchElementException e) {
			return false;
		} catch (NullPointerException e1) {
			return false;
		}

	}


	/**
	 * isDisplayed Verify whether the element is selected or not
	 * 
	 * @param element
	 *            represents the element which needs to be verified whether its
	 *            displayed or not
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            x-path is given
	 *            
	 * @param Time
	 *            represents the Time which overrides the global element timeout
	 * 
	 * @return
	 */
	public boolean isDisplayed(Element element, String objectValue, int Time) {
		WebElement element1 = getElement(element, objectValue, Time);
		try {

			WebDriverWait wait = new WebDriverWait(driver, Time);
			WebElement ele=wait.until(ExpectedConditions.visibilityOf(element1));
			if (ele.isDisplayed())
				return true;
			else
				return false;

		} catch (Exception e) {
			return false;
		}
	}

	/*boolean isDisplayedChrome(WebElement element) {
		try {
			if (element != null
					&& (boolean) ((JavascriptExecutor) driver)
					.executeScript(
							"var elem=arguments[0]; { return elem.offsetWidth>0; }",
							element)
							&& (boolean) ((JavascriptExecutor) driver)
							.executeScript(
									"var elem=arguments[0]; { return elem.offsetHeight>0; }",
									element))
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}*/

	/**
	 * mouseclick To click on the element based on actions
	 * 
	 * @param element
	 *            represents the element which needs to be clicked
	 * 
	 */
	public void mouseclick(WebElement element) throws Exception {
		try {
			action.moveToElement(element).click().build().perform();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * mouseclick To click on the element based on actions
	 * 
	 * @param element
	 *            represents the element which needs to be clicked
	 * 
	 * @param objectValue
	 *            represents the string which needs to be passed when dynamic
	 *            xpaths are given
	 * 
	 */
	public void mouseclick(Element element, String objectValue)
			throws Exception {
		try {
			action.moveToElement(getElement(element, objectValue)).click()
			.build().perform();
		} catch (Exception e) {
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;
		}
	}

	/**
	 * mouseOver To move the mouse control over an element
	 * 
	 * @param element
	 *            represents the element over which mouse needs to be hovered
	 * 
	 * @param objectValue
	 *            represents the String which needs to be passed when dynamic
	 *            xpath values are given
	 * 
	 */
	public void mouseOver(Element element, String objectValue) throws Exception {
		try
		{
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			((JavascriptExecutor) driver).executeScript(mouseOverScript,
					getElement(element, objectValue));
		}
		catch(Exception e)
		{
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;
		}
	}

	/**
	 * mouseOver To move the mouse control over an element
	 * 
	 * @param element
	 *            represents the element over which mouse needs to be hovered
	 * 
	 * 
	 */
	public void mouseOver(WebElement element) throws Exception {
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		((JavascriptExecutor) driver).executeScript(mouseOverScript, element);
	}

	/**
	 * pageScroll To scroll till the element is visible
	 * 
	 * @param element
	 *            represents the element till which scroll needs to be performed
	 * 
	 * @param objectValue
	 *            represents the String which needs to be passed when dynamic
	 *            xpath values are given
	 * 
	 */
	public void pageScroll(Element element, String objectValue, Boolean pageScrollView)
			throws Exception {
			try
			{
			((JavascriptExecutor) driver).executeScript(
			"arguments[0].scrollIntoView("+pageScrollView+");",
			getElement(element, objectValue));
			}
			catch(Exception e)
			{
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;
			}
			}

	/**
	 * pageScroll To scroll till the element is visible
	 * 
	 * @param element
	 *            represents the element over which mouse needs to be hovered
	 * 
	 * 
	 */
	public void pageScroll(WebElement element) throws Exception {
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);", element);
	}

	/**
	 * getAttribute To get the attribute from the element
	 * 
	 * @param element
	 *            represents the element from which attributes should be
	 *            retrieved
	 * 
	 * @param objectValue
	 *            represents the String which needs to be passed when dynamic
	 *            xpath values are given
	 * 
	 * @param propertyName
	 *            represents the String which gets the property name for which
	 *            attribute value needs to be retrieved
	 * @throws IOException 
	 * @throws JSONException 
	 * 
	 * 
	 */
	public String getAttribute(Element element, String objectValue,
			String propertyName) throws JSONException, IOException {
		try {
			return getElement(element, objectValue).getAttribute(propertyName);
		} catch (NoSuchElementException e) {
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;
		}
	}

	/**
	 * 
	 * pause() To hold the control of driver for 30secs
	 * 
	 * 
	 */
	public static void pause() {
		try {
			Thread.sleep(20000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * switchToAlert To switch to a given alert based on status
	 * 
	 * @param status To check whether user needs to accept or dismiss the alert
	 * 
	 * @throws InterruptedException
	 * @throws UserDefinedException 
	 */
	public void AlertUsingOptions(String status) throws InterruptedException, UserDefinedException {
		try {

			//Switch to alert
			Alert alert = driver.switchTo().alert();

			// Accepting or dismissing the alert based on input
			if (status.equalsIgnoreCase("accept")) {
				alert.accept();
			} else if (status.equalsIgnoreCase("dismiss")) {
				alert.dismiss();
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * switchToAlert To switch to a given alert based on status after giving the input message
	 * 
	 * @param status To check whether user needs to accept or dismiss the alert
	 * 
	 * @param strMessage Value which needs to be given as input to alert
	 * 
	 * @throws InterruptedException
	 * @throws UserDefinedException 
	 * 
	 */
	public void AlertUsingText(String status, String strMessage)
			throws InterruptedException, UserDefinedException {
		try {

			//Switch to alert
			Alert alert = driver.switchTo().alert();

			// Give the message in alert box
			alert.sendKeys(strMessage);

			// Accepting or dismissing the alert based on input
			if (status.equalsIgnoreCase("accept")) {
				alert.accept();
			} else if (status.equalsIgnoreCase("dismiss")) {
				alert.dismiss();
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * getAttribute To get the attribute from the element
	 * 
	 * @param element
	 *            represents the element from which attributes should be
	 *            retrieved
	 * 
	 * @param objectValue
	 *            represents the String which needs to be passed when dynamic
	 *            xpath values are given
	 * 
	 * @param propertyName
	 *            represents the String which gets the property name for which
	 *            attribute value needs to be retrieved
	 *            
	 * @param time 
	 *            To overwrite the global element timeout variable for this method
	 *            
	 * @throws IOException 
	 * @throws JSONException 
	 * 
	 * 
	 */
	public String getAttribute(Element element, String objectValue,
			String propertyName,int time) throws JSONException, IOException {
		try {
			return getElement(element, objectValue,time).getAttribute(propertyName);
		} catch (NoSuchElementException e) {
			report.updateMainReport("xpathDetails", element.getElementName().replace("<<<>>>", objectValue));
			throw e;
		}
	}

	/**
	 * 
	 * getThreadLogger To get individual log details in Report folder based on test-case   
	 * 
	 */
	public static Logger getThreadLogger(Thread currentThread, String testcaseName)
	{

		Logger log = Logger.getLogger(currentThread.getName());

		RollingFileAppender rollingAppender = new RollingFileAppender();

		rollingAppender.setName(currentThread.getName());
		rollingAppender.setAppend(false);
		rollingAppender.setLayout(new PatternLayout("%d{dd-MMM-yyyy/HH:mm:ss : }  %m%n"));

		File requiredPath = new File(ReportStatus.getCurrentExecutionPath().getAbsolutePath()+"/Logs/");
		requiredPath.mkdirs();
		rollingAppender.setFile(requiredPath.getAbsolutePath()+"/"+testcaseName + ".log");

		rollingAppender.activateOptions();
		log.addAppender(rollingAppender);
		return log;
	}


}
