package com.automation.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;

import com.automation.utilities.PropertyReader;
import com.google.common.base.Function;

public class CustomElementLocator implements ElementLocator {
	private final SearchContext searchContext;
	private final boolean shouldCache;
	private By by;
	public int Time = 0;
	private TextAnnotation textAnnotation;
	private WebElement cachedElement;
	private List<WebElement> cachedElementList;
	private static By pageLoading;
	private static boolean pageLoadingFlag = false;
	final String elementName;

	public CustomElementLocator(SearchContext searchContext, Field field) {
		this(searchContext, new TextAnnotation(field));
	}

	public CustomElementLocator(SearchContext searchContext,
			TextAnnotation annotations) {
		this.textAnnotation = annotations;
		this.searchContext = searchContext;
		this.shouldCache = annotations.isLookupCached();
		this.by = textAnnotation.buildBy();
		getPageLoadLocator();
		this.Time = getTimeFromPropertyFile();
		this.elementName = textAnnotation.getElementName() + " - "
				+ by.toString();
	}

	public int getTimeFromPropertyFile() {
		try {
			int Time1 = Integer.parseInt(PropertyReader.getProperty(
					"TimeoutValue").toString());
			return Time1;

		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return 0;

	}

	public WebElement findElement() {
		WebElement element = null;
		if (cachedElement != null && shouldCache) {
			return cachedElement;
		}
		try {
			FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(
					(WebDriver) searchContext)
					.pollingEvery(500, TimeUnit.MILLISECONDS)
					.withTimeout(Time, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			try {
				fWait.until(documentReadyWait());
				fWait.until(LoadSymbolExist());
			} catch (Exception x) {
				System.out.println(x);
			}

			element = fWait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver webDriver) {
					return webDriver.findElement(by);
				}
			});
		} catch (TimeoutException e) {

		} catch (NoSuchElementException e1) {

		}
		if (shouldCache) {
			cachedElement = element;
		}
		return element;

	}

	public WebElement findElement(int Time) {
		WebElement element = null;
		if (cachedElement != null && shouldCache) {
			return cachedElement;
		}
		try {
			FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(
					(WebDriver) searchContext)
					.pollingEvery(500, TimeUnit.MILLISECONDS)
					.withTimeout(Time, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			try {
				fWait.until(documentReadyWait());
				fWait.until(LoadSymbolExist());
			} catch (Exception x) {
				System.out.println(x);
			}

			element = fWait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver webDriver) {
					return webDriver.findElement(by);
				}
			});
		} catch (TimeoutException e) {

		} catch (NoSuchElementException e1) {

		}
		if (shouldCache) {
			cachedElement = element;
		}
		return element;

	}

	public WebElement findElement(String objectValue) throws Exception {
		boolean strReplaceFlag = false;
		if (by.toString().contains("<<<>>>")) {
			by = textAnnotation.getFindBy(textAnnotation.getFindByValue(),
					objectValue);
			strReplaceFlag = true;
		}
		WebElement element = null;
		if (cachedElement != null && shouldCache) {
			return cachedElement;
		}
		try {
			FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(
					(WebDriver) searchContext)
					.pollingEvery(500, TimeUnit.MILLISECONDS)
					.withTimeout(Time, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			try {
				fWait.until(documentReadyWait());
				fWait.until(LoadSymbolExist());

			} catch (Exception x) {
				System.out.println(x);
			}

			element = fWait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver webDriver) {
					return webDriver.findElement(by);
				}
			});
		} catch (TimeoutException e) {

		} catch (NoSuchElementException e1) {

		}
		if (shouldCache) {
			cachedElement = element;
		}
		if (strReplaceFlag)
			by = textAnnotation.buildBy();
		return element;
	}

	public WebElement findElement(String objectValue, int Time)
			throws Exception {
		boolean strReplaceFlag = false;
		if (by.toString().contains("<<<>>>")) {
			by = textAnnotation.getFindBy(textAnnotation.getFindByValue(),
					objectValue);
			strReplaceFlag = true;
		}
		WebElement element = null;
		if (cachedElement != null && shouldCache) {
			return cachedElement;
		}
		try {
			FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(
					(WebDriver) searchContext)
					.pollingEvery(500, TimeUnit.MILLISECONDS)
					.withTimeout(Time, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);
			try {
				fWait.until(documentReadyWait());
				fWait.until(LoadSymbolExist());

			} catch (Exception x) {
				System.out.println(x);
			}

			element = fWait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver webDriver) {
					return webDriver.findElement(by);
				}
			});
		} catch (TimeoutException e) {

		} catch (NoSuchElementException e1) {

		}
		if (shouldCache) {
			cachedElement = element;
		}
		if (strReplaceFlag)
			by = textAnnotation.buildBy();
		return element;
	}

	public List<WebElement> findElements() {
		if (cachedElementList != null && shouldCache) {
			return cachedElementList;
		}
		FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(
				(WebDriver) searchContext)
				.pollingEvery(500, TimeUnit.MILLISECONDS)
				.withTimeout(Time, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		try {
			fWait.until(documentReadyWait());
			fWait.until(LoadSymbolExist());

		} catch (Exception x) {
			System.out.println(x);
		}
		List<WebElement> elements = searchContext.findElements(by);
		if (shouldCache) {
			cachedElementList = elements;
		}

		return elements;
	}

	public List<WebElement> findElements(String objectValue) throws Exception {
		boolean strReplaceFlag = false;
		if (by.toString().contains("<<<>>>")) {
			by = textAnnotation.getFindBy(textAnnotation.getFindByValue(),
					objectValue);
			strReplaceFlag = true;
		}
		if (cachedElementList != null && shouldCache) {
			return cachedElementList;
		}
		FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(
				(WebDriver) searchContext)
				.pollingEvery(500, TimeUnit.MILLISECONDS)
				.withTimeout(Time, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		try {
			fWait.until(documentReadyWait());
			fWait.until(LoadSymbolExist());

		} catch (Exception x) {
			System.out.println(x);
		}
		List<WebElement> elements = searchContext.findElements(by);
		if (shouldCache) {
			cachedElementList = elements;
		}
		if (strReplaceFlag)
			by = textAnnotation.buildBy();
		return elements;
	}

	public List<WebElement> findElements(int Time) {
		if (cachedElementList != null && shouldCache) {
			return cachedElementList;
		}
		FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(
				(WebDriver) searchContext)
				.pollingEvery(500, TimeUnit.MILLISECONDS)
				.withTimeout(Time, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		try {
			fWait.until(documentReadyWait());
			fWait.until(LoadSymbolExist());

		} catch (Exception x) {
			System.out.println(x);
		}
		List<WebElement> elements = searchContext.findElements(by);
		if (shouldCache) {
			cachedElementList = elements;
		}

		return elements;
	}

	public List<WebElement> findElements(String objectValue, int Time)
			throws Exception {
		boolean strReplaceFlag = false;
		if (by.toString().contains("<<<>>>")) {
			by = textAnnotation.getFindBy(textAnnotation.getFindByValue(),
					objectValue);
			strReplaceFlag = true;
		}
		if (cachedElementList != null && shouldCache) {
			return cachedElementList;
		}
		FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(
				(WebDriver) searchContext)
				.pollingEvery(500, TimeUnit.MILLISECONDS)
				.withTimeout(Time, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		try {
			fWait.until(documentReadyWait());
			fWait.until(LoadSymbolExist());

		} catch (Exception x) {
			System.out.println(x);
		}
		List<WebElement> elements = searchContext.findElements(by);
		if (shouldCache) {
			cachedElementList = elements;
		}
		if (strReplaceFlag)
			by = textAnnotation.buildBy();
		return elements;
	}

	ExpectedCondition<Boolean> documentReadyWait() {
		ExpectedCondition<Boolean> documentReadyStatus = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver webDriver) {
				String status = ((JavascriptExecutor) webDriver).executeScript(
						"return document.readyState").toString();
				// System.out.println("Complete");
				if (status.equals("complete") | status.equals("interactive"))
					return true;
				else
					return false;
			}
		};
		return documentReadyStatus;
	}

	ExpectedCondition<Boolean> LoadSymbolExist() {
		ExpectedCondition<Boolean> documentReadyStatus = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver webDriver) {
				if (pageLoadingFlag && pageLoading != null) {
					WebElement element = null;
					try {
						if (webDriver.findElements(pageLoading).size() != 0) {
							element = webDriver.findElement(pageLoading);
							if ((boolean) ((JavascriptExecutor) webDriver)
									.executeScript(
											"var elem=arguments[0]; { return elem.offsetWidth>0; }",
											element)
									&& (boolean) ((JavascriptExecutor) webDriver)
											.executeScript(
													"var elem=arguments[0]; { return elem.offsetHeight>0; }",
													element))
								return false;
							else
								return true;
						} else
							return true;
					} catch (NoSuchElementException e) {
						return true;
					} catch (Exception e) {
						return true;
					}
				}
				return true;
			}

		};
		return documentReadyStatus;
	}

	synchronized void getPageLoadLocator() {
		try {
			if (!pageLoadingFlag && pageLoading == null) {
				for (int i = 0; i < Thread.currentThread().getStackTrace().length; i++) {
					if (Thread.currentThread().getStackTrace()[i]
							.getClassName().contains("pages")) {
						getClass();
						Class<?> c = Class.forName(Thread.currentThread()
								.getStackTrace()[i].getClassName());
						while (c != null) {
							if (c.getName().contains("CommonOR")) {
								for (int j = 0; j < c.getDeclaredFields().length; j++) {
									if (c.getDeclaredFields()[j].getName()
											.equalsIgnoreCase("pageloading")) {
										for (Field field : c
												.getDeclaredFields()) {
											for (Annotation annotation : field
													.getDeclaredAnnotations()) {
												Class<? extends Annotation> type = annotation
														.annotationType();
												for (Method method : type
														.getDeclaredMethods()) {
													Object value = method
															.invoke(annotation,
																	(Object[]) null);
													if (!value.equals("")
															&& pageLoading != null) {
														pageLoading = By
																.xpath(value
																		.toString());
														System.out
																.println("Page Loading xpath is set as - "
																		+ pageLoading);
														pageLoadingFlag = true;
														break;
													}
												}
												if (pageLoadingFlag)
													break;
											}
											if (pageLoadingFlag)
												break;
										}
										break;
									} else
										pageLoadingFlag = false;
								}
								break;
							} else {
								c = c.getSuperclass();
							}
						}
					}
				}
			}
			if (!pageLoadingFlag)
				pageLoadingFlag = true;
		} catch (Exception e) {
			e.printStackTrace();
			pageLoadingFlag = false;
		}
	}
}