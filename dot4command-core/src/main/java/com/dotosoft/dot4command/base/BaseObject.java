package com.dotosoft.dot4command.base;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class BaseObject {
	
	private Object parent;
	private Logger logger;
	private Boolean showLog;

	//-- Private Method
	private Logger getLogger() {
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
}
