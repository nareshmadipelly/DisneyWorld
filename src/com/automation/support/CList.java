package com.automation.support;

import java.util.List;

@SuppressWarnings("hiding")
public interface CList<Object> extends List<Object>{
	@DynamicObject
	public Object get(String objectValue, int index);

	@DynamicObject
	public CList<Object> getList(String objectValue);
}