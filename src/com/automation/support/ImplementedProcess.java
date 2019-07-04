package com.automation.support;

public class ImplementedProcess {
	ImplementedProcess(){
		
	}
	public static <T> Class<?> getWrapperClass(Class<T> interfaceType) {
        if (interfaceType.isAnnotationPresent(ImplementedBy.class)) {
            ImplementedBy annotation = interfaceType.getAnnotation(ImplementedBy.class);
            Class<?> clazz = annotation.value();
            if (Element.class.isAssignableFrom(clazz)) {
                return annotation.value();
            }
        }
        throw new UnsupportedOperationException("Apply @ImplementedBy interface to your Interface " + interfaceType.getCanonicalName() + " if you want to extend ");
	}
}
