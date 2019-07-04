package com.automation.support;

import java.util.ArrayList;

public class CustomArrayList<E> extends ArrayList<E> implements CList<E>
{

	private static final long serialVersionUID = 1L;

	public CustomArrayList(){
	super();
	}

	public E get(String objectValue, int index){
	return super.get(index);
	}

	public CustomArrayList<E> getList(String objectValue){
	return this;
	}
}