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

package com.dotosoft.dot4command.commands;

import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.SingletonFactory;
import com.dotosoft.dot4command.utils.StringUtils;

public class CreateObjectCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	private String objectClass;
	private String argumentsKey;
	private String toKey;

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}
	
	public void setArgumentsKey(String argumentsKey) {
		this.argumentsKey = argumentsKey;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public String getArgumentsKey() {
		return argumentsKey;
	}

	public String getToKey() {
		return toKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	@Override
	public Processing onExecute(C context) throws Exception {
		Class objectClazz = Class.forName(objectClass);

		V webClient;
		if (StringUtils.hasValue(argumentsKey)) {
			webClient = (V) SingletonFactory.getInstance(objectClazz, getObjects(context));
		} else {
			webClient = (V) SingletonFactory.getInstance(objectClazz);
		}

		context.put((K) toKey, webClient);
		
		return Processing.FINISHED;
	}
	
	public boolean postprocess(C context, Exception exception) {
		if (exception == null) return false;
		exception.printStackTrace();
		System.err.println("Exception " + exception.getMessage() + " occurred.");
		return true;
	}
	
	/**
     * Return a <code>Class[]</code> describing the expected signature of the method.
     * @return The method signature.
     */
    protected Object[] getObjects(C context) {
    	
    	if(StringUtils.hasValue(argumentsKey)) {
	    	String[] keys = argumentsKey.split(",");
	    	Object[] obj = new Object[keys.length];
	    	for(int i = 0; i<keys.length; i++) {
	    		String key = keys[i];
	    		Object param = getProperty(context, key);
	    		obj[i] = param;
	    	}
	    	return obj;
    	}
    	
        return null;
    }

}
