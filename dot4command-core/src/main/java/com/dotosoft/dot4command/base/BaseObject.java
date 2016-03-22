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

package com.dotosoft.dot4command.base;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class BaseObject implements Cloneable{
	
	private Object parent;
	private Logger logger;
	private Boolean showLog;

	//-- Private Method
	public Logger getLogger() {
		if (logger == null) {
			logger = LoggerFactory.getLogger(getClass().getName());
		}
		return logger;
	}
	
	//-- Public Method
	public Boolean isShowLog() {
		if(showLog == null) {
			if(parent != null && parent instanceof BaseObject) {
				showLog = (((BaseObject)parent).isShowLog() != null) ? ((BaseObject) parent).isShowLog() : false;
			} else {
				showLog = false;
			}
		}
		return showLog;
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public void setShowLog(Boolean showLog) {
		this.showLog = showLog;
	}
	
	public void modifyAttributes(Map valuesMap) {
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		Field[] allFields = getClass().getDeclaredFields();
	    for (Field field : allFields) {
	    	try {
		    	Object value = getProperty(this, field.getName());
		    	if(value instanceof String) {
		    		setProperty(this, field.getName(), sub.replace(value));
		    	}
	    	} catch (Exception ex) {}
	    }
	}
	
	public final void print(String message) {
		if(isShowLog()) {
			log(Level.INFO, message);
		} else {
			System.out.println(message);
		}
	}

	public final void log(String message) {
		log(Level.INFO, message);
	}
	
	public final void log(Level level, String message) {
		if(isShowLog()) {
			if(level == Level.DEBUG) {
				getLogger().info(message);
			} else if(level == Level.ERROR) {
				getLogger().error(message);
			} else if(level == Level.TRACE) {
				getLogger().trace(message);
			} else if(level == Level.WARN) {
				getLogger().warn(message);
			} else {
				getLogger().info(message);
			}
		}
	}
	
	public final void setPropertyValue(Object data, String name, Object value) {
		try {
			PropertyUtils.setProperty(data, name, value);
		}catch (Exception ex) {}
	}

	public final Object getProperty(Object data, String expression) {
		return getProperty(data, expression, null);
	}
	
	public final void setProperty(Object data, String expression, Object value) {
		try {
			PropertyUtils.setProperty(data, expression, value);
		} catch (Exception e) {}
	}

	public final Object getProperty(Object data, String expression,
			Object defaultValue) {
		try {
			Object returnValue = PropertyUtils.getProperty(data, expression);
			if (returnValue != null) {
				return returnValue;
			}
		} catch (Exception ex) {
		}

		return defaultValue;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
