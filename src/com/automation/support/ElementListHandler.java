package com.automation.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.automation.functionallibrary.CustomElement;

import static com.automation.support.ImplementedProcess.getWrapperClass;

public class ElementListHandler implements InvocationHandler {

	private final CustomElementLocator locator;
	@SuppressWarnings("unused")
	private final Class<?> wrappingType;

	public <T> ElementListHandler(Class<T> interfaceType, ElementLocator locator) {
		this.locator = (CustomElementLocator) locator;
		if (!Element.class.isAssignableFrom(interfaceType)) {
			throw new RuntimeException("interface not assignable to Element.");
		}

		this.wrappingType = getWrapperClass(interfaceType);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		try {
			List<WebElement> elements;
			CList<Object> elementList = new CustomArrayList<Object>();
			if (method.getAnnotation(DynamicObject.class) == null)
				elements = locator.findElements();
			else
				elements = locator.findElements(args[0].toString());
			if ("getWrappedElement".equals(method.getName())) {
				return elements;
			}
			for (WebElement element : elements) {
				@SuppressWarnings("rawtypes")
				Constructor cons = wrappingType
						.getConstructor(WebElement.class);
				Object thing = cons.newInstance(element);
				if (wrappingType.cast(thing) instanceof CustomElement) {
					Method elementNameMethod = wrappingType.cast(thing)
							.getClass()
							.getMethod("setElementName", String.class);
					elementNameMethod.invoke(wrappingType.cast(thing),
							locator.elementName);
				}
				elementList.add(thing);
			}
			return method.invoke(elementList, args);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}
}
