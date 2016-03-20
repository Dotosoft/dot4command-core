/*
	Copyright 2015 Denis Prasetio
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

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
