package com.automation.support;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.CacheLookup;

import com.automation.utilities.UserDefinedException;

public class TextAnnotation extends Annotations{

	private Field field;

	public TextAnnotation(Field field) {
		super(field);
		this.field = field;
	}

	public boolean isLookupCached() {
		return (field.getAnnotation(CacheLookup.class) != null);
	}

	public String getElementName(){
		return field.getName();
	}

	public By buildBy() {
		assertValidAnnotations();
		By ans = null;
		ans=super.buildBy();
		if (ans == null) {
			ans = buildByFromDefault();
		}
		if (ans == null) {
			throw new IllegalArgumentException("Cannot determine how to locate element " + field);
		}
		return ans;
	}

	public FindBy getFindByValue(){
		return field.getAnnotation(FindBy.class);
	}

	public By getFindBy(FindBy findBy, String objectValue) throws Exception 
	{
		String modifiedObjectProperty=null;

		if (!"".equals(findBy.className()))
		{
			modifiedObjectProperty=getORReplaced(findBy.className(), objectValue);
			return By.className(modifiedObjectProperty);
		}

		if (!"".equals(findBy.css()))
		{
			modifiedObjectProperty=getORReplaced(findBy.css(), objectValue);
			return By.cssSelector(modifiedObjectProperty);
		}

		if (!"".equals(findBy.id()))
		{
			modifiedObjectProperty=getORReplaced(findBy.id(), objectValue);
			return By.id(modifiedObjectProperty);	
		}

		if (!"".equals(findBy.linkText()))
		{
			modifiedObjectProperty=getORReplaced(findBy.linkText(), objectValue);
			return By.linkText(modifiedObjectProperty);	
		}

		if (!"".equals(findBy.name()))
		{
			modifiedObjectProperty=getORReplaced(findBy.name(), objectValue);
			return By.name(modifiedObjectProperty);	
		}

		if (!"".equals(findBy.partialLinkText()))
		{
			modifiedObjectProperty=getORReplaced(findBy.partialLinkText(), objectValue);
			return By.partialLinkText(modifiedObjectProperty);	
		}

		if (!"".equals(findBy.tagName()))
		{
			modifiedObjectProperty=getORReplaced(findBy.tagName(), objectValue);
			return By.tagName(modifiedObjectProperty);	
		}		

		if (!"".equals(findBy.xpath()))
		{
			modifiedObjectProperty=getORReplaced(findBy.xpath(), objectValue);
			return By.xpath(modifiedObjectProperty);
		}


		// Fall through
		return null;
	}

	public static synchronized String getORReplaced(String objectProperty, String replaceString) throws Exception
	{

		//To initialize the variable
		int i=0;

		//To get the number of replacements needed
		//String[] objectPropertyArray=objectProperty.split("<<<>>>");
		String[] replaceStringArray=replaceString.split(";");
		try
		{
			//To match the given data & form customized xpath
			if(replaceStringArray.length>1)
			{
				Pattern p = Pattern.compile("<<<>>>");
				Matcher m = p.matcher(objectProperty);
				StringBuffer sb = new StringBuffer();
				while (m.find()) 
				{	
					m.appendReplacement(sb, replaceStringArray[i]);
					i++;
				}
				m.appendTail(sb);

				System.out.println(sb.toString());
				return sb.toString();
			}
			else 
			{
				return (objectProperty.replace("<<<>>>", replaceString));
			}
		}
		catch(Exception e)
		{
			System.out.println("Xpath '<<<>>>' attributes and 'ObjectValue' values are not same");
			e.printStackTrace();
			return(objectProperty);
		}

		}
	}
