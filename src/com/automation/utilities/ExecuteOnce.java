package com.automation.utilities;

import java.lang.reflect.Field;

public class ExecuteOnce {
	static int setValue = 1;
	public static int validateInitialScreen= setValue, 
			validateIndividualPrice = setValue, 
			addRouter = setValue, 
			validateContactInformation=setValue, 
			validateCreditCheck=setValue,
			validateInitialCheckOutScreen=setValue,
			validateHeaderSection=setValue,
		    validateFiosInetHeaderPanel=setValue,
		    validateHomePhoneHeaderPanel=setValue,
		    validateFiosDigitalVoiceHeaderPanel=setValue,
		    validateServiceFiosPlansHeaderTV=setValue,
		    validateServiceFiosPlansHeaderPhone=setValue,
		    validateServiceFiosPlansHeaderDigitalVoice=setValue,
		    validateDigitalAdapter=setValue,
		    validateInitialScreenOnRouter=setValue,
		    validateInitialScreenOnTV=setValue,
		    validateInitialScreenOnInternet=setValue,
		    validateInitialScreenOnFDV=setValue,
		    validateEmployeeFieldValidation=setValue,
		    validateInitialHSIInetPanel=setValue,
		    validateInitialHSIHomePhonePanel=setValue,
		    validateInitialHSITVPanel=setValue,
		    validateInitialHSIPlansHeadAndDescTV=setValue,
		    validateInitialHSIPlansHeadAndDescInet=setValue,
		    validateInitialHSIPlansHeadAndDescHomePhone=setValue,
		    validateInitialHSIAdditionalChannels=setValue,
		    validateInitialHSIEquipmentTV=setValue;
	
	public static synchronized int executeOnce(String value) throws IllegalArgumentException, IllegalAccessException, SecurityException, ClassNotFoundException {
		int result = 0;
		try{
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			Field[] fields = Class.forName(stackTraceElements[1].getClassName()).getDeclaredFields();
			System.out.println(fields.length);
			if(value.equals("validateInitialScreen"))
				System.out.println(stackTraceElements[1].getClassName());
			for (Field field : fields) {
				if(field.getName().equals(value)){
					result = field.getInt(value) + 1;
					field.setInt(value, field.getInt(value) + 1);
					break;
				}
			}
		}catch (IllegalArgumentException e){}
		catch (IllegalAccessException a){}
		return result;
	}
}
