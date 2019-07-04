package com.automation.functionallibrary;

import java.io.File;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.automation.utilities.FilePaths;
import com.automation.utilities.PropertyReader;

public class InitiateDriver {

	public static boolean windows;
	WebDriver driver = null;

	public InitiateDriver(String browser) throws Exception {

		String env =browser.trim().toUpperCase(); 

		try {
			if(env.trim().toUpperCase().contains("DOCKER"))
			{
				DesiredCapabilities caps = new DesiredCapabilities(); 
	            caps.setPlatform(org.openqa.selenium.Platform.LINUX);
	            caps = DesiredCapabilities.chrome();   
        	    ChromeOptions opts1 = new ChromeOptions();
        	    opts1.addArguments("start-maximized"); 
        	    opts1.addArguments("chrome.switches","--disable-extensions");
        	    opts1.addArguments("--disable-popup-blocking"); 
            	caps.setCapability(ChromeOptions.CAPABILITY, opts1);
            	driver = new RemoteWebDriver(new URL(PropertyReader.getProperty("DockerURL")),caps);
			}
			else{
			switch (env) {

			case "IE": 
			{
				System.setProperty("webdriver.ie.driver", new File(
						"src/test/resources/IEDriverServer.exe")
				.getAbsolutePath());

				DesiredCapabilities caps = DesiredCapabilities
						.internetExplorer();
				caps.setCapability("ignoreZoomSetting", true);
				caps.setCapability(
						InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				caps.setCapability("enablePersistentHover", false);
				caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
						UnexpectedAlertBehaviour.ACCEPT);
				driver = new InternetExplorerDriver(caps);
				driver.manage().window().maximize();
				driver.manage().deleteAllCookies();

				break;
			}
			
			case "CHROME":

				Proxy proxy = new Proxy();
				proxy.setAutodetect(false);
				proxy.setProxyType(Proxy.ProxyType.PAC);
				proxy.setProxyAutoconfigUrl("http://autoproxy.verizon.com/cgi-bin/getproxy");
				System.setProperty("webdriver.chrome.driver", new File("src/test/resources/chromedriver.exe").getAbsolutePath());
				DesiredCapabilities ieCapabilities = DesiredCapabilities.chrome();
				ieCapabilities.setCapability(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, FilePaths.chromeDriverServer.getAbsolutePath());
				ieCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
				ieCapabilities.setCapability(CapabilityType.PROXY,proxy);
				ChromeOptions opts = new ChromeOptions();
				opts.addArguments("start-maximized");
				// To disable developer mode extension in chrome browser
				opts.addArguments("chrome.switches","--disable-extensions");
				/*if(privateBrowsing){
				opts.addArguments("--incognito");
				ieCapabilities.setCapability("chrome.switches", Arrays.asList("-incognito"));
				}*/
				ieCapabilities.setCapability(ChromeOptions.CAPABILITY, opts);
				driver = new ChromeDriver(ieCapabilities);
				break;
				
			case "GRID":
				DesiredCapabilities capability = DesiredCapabilities.internetExplorer(); 
				capability.setBrowserName("internet explorer");
				capability.setCapability("DevOpsTrackInfo","CMB-EIQV-ITOAutomationTeam-Jenkins-High level Regression Suite");
				if(PropertyReader.getProperty("gridPlatform").equalsIgnoreCase("Vista"))
				{
					capability.setPlatform(Platform.VISTA);
				}
				else if(PropertyReader.getProperty("gridPlatform").equalsIgnoreCase("Windows10"))
				{
					capability.setPlatform(Platform.WIN10);
				}
				else if(PropertyReader.getProperty("gridPlatform").equalsIgnoreCase("Windows8"))
				{
					capability.setPlatform(Platform.WIN8);
				}
				else
				{
					capability.setPlatform(Platform.ANY);
				}
				capability.setCapability("ignoreProtectedModeSettings",true); 
				driver = new RemoteWebDriver(new URL(PropertyReader.getProperty("gridURL")),capability);
				break;

			case "GRID_CHROME":
				DesiredCapabilities capability1 = DesiredCapabilities.chrome(); 
				capability1.setBrowserName("chrome");
				capability1.setCapability("DevOpsTrackInfo","CMB-EIQV-ITOAutomationTeam-Jenkins-High level Regression Suite");

				if(PropertyReader.getProperty("gridPlatform").equalsIgnoreCase("Vista"))
				{
					capability1.setPlatform(Platform.VISTA);
				}
				else if(PropertyReader.getProperty("gridPlatform").equalsIgnoreCase("Windows10"))
				{
					capability1.setPlatform(Platform.WIN10);
				}
				else if(PropertyReader.getProperty("gridPlatform").equalsIgnoreCase("Windows8"))
				{
					capability1.setPlatform(Platform.WIN8);
				}
				driver = new RemoteWebDriver(new URL(PropertyReader.getProperty("gridURL")),capability1);
				break;
				
			/*case "GRID_CHROME_DOCKER":
				DesiredCapabilities caps = new DesiredCapabilities(); 
				caps.setPlatform(org.openqa.selenium.Platform.LINUX);
				caps = DesiredCapabilities.chrome();   
				    ChromeOptions opts1 = new ChromeOptions();
				    opts1.addArguments("start-maximized"); 
				    opts1.addArguments("chrome.switches","--disable-extensions");
				    opts1.addArguments("--disable-popup-blocking"); 
				caps.setCapability(ChromeOptions.CAPABILITY, opts1);
				driver = new RemoteWebDriver(new URL(PropertyReader.getProperty("gridURL")),caps);   
				break;*/
			}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	public WebDriver getDriver() {
		return driver;
	}

}
