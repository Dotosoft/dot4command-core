package com.dotosoft.dot4command.utils;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtils {
	
	public static <T extends Object> T getProperty(Object data, String expression) {
		return (T) getProperty(data, expression, null);
	}
	
	public static <T extends Object> T getProperty(Object data, String expression, T defaultValue) {
		try {
			T returnValue = (T) PropertyUtils.getProperty(data, expression);
			if(returnValue != null) {
				return returnValue;
			}
		} catch (Exception ex) {}
		
		return defaultValue;
	}
	
}
