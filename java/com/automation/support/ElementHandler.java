package com.automation.support;

import com.automation.functionallibrary.CustomElement;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import static com.automation.support.ImplementedProcess.getWrapperClass;

public class ElementHandler implements InvocationHandler {
	private final CustomElementLocator locator;
	private final Class<?> wrappingType;

	public <T> ElementHandler(Class<T> interfaceType, ElementLocator locator) {
		this.locator = (CustomElementLocator) locator;
		if (!Element.class.isAssignableFrom(interfaceType)) {
			throw new RuntimeException("interface not assignable to Element.");
		}

		this.wrappingType = getWrapperClass(interfaceType);
	}

	@Override
	public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
		try {
		WebElement element;
		String orValue="";
		int temp = 0;
		@SuppressWarnings("rawtypes")
		Constructor cons = wrappingType.getConstructor(WebElement.class);
		if(method.getAnnotation(DynamicObject.class)==null)
			if(method.getAnnotation(DynamicTimeOut.class)==null)
				element = locator.findElement();
			else
				element = locator.findElement(Integer.valueOf(objects[1].toString()));
		else
			if(method.getAnnotation(DynamicTimeOut.class)!=null)
				element = locator.findElement(objects[0].toString(), Integer.valueOf(objects[1].toString()));
			else
				element = locator.findElement(objects[0].toString());

		if ("getWrappedElement".equals(method.getName())) {
			return element;

		}
		Object thing = cons.newInstance(element);
		if (wrappingType.cast(thing) instanceof CustomElement) 
		{
			Method elementNameMethod = wrappingType.cast(thing)
					.getClass()
					.getMethod("setElementName", String.class);
			elementNameMethod.invoke(wrappingType.cast(thing),
					locator.elementName);
		}
			return method.invoke(wrappingType.cast(thing), objects);

		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}
}