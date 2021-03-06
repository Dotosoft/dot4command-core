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

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.dotosoft.dot4command.utils.BeanUtils;

public class BaseObject implements Cloneable, Serializable {
	
	private String nodeId;
	private Object parent;
	private Logger logger;
	private Boolean showLog;

	public BaseObject() {
		this.nodeId = UUID.randomUUID().toString();
	}
	
	public String getNodeId() {
		return nodeId;
	}
	
	public String getParentId() {
		return (String) getProperty(getParent(), "nodeId");
	}

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
	
	public final void modify(ModifierHandler handler, Object...params) {
		handler.modifier(params);
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
	
	protected final void setProperty(Object data, String name, Object value) {
		BeanUtils.setPropertyValue(data, name, value);
	}

	protected final <T extends Object> T getProperty(Object data, String expression) {
		return BeanUtils.getProperty(data, expression, null);
	}
	
	protected final <T extends Object> T getProperty(Object data, String expression, T defaultValue) {
		return BeanUtils.getProperty(data, expression, defaultValue);
	}

	@Override
	public Object clone() {
		return SerializationUtils.clone(this);
	}
}
