package com.dotosoft.dot4command.commands;

import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.BeanUtils;
import com.dotosoft.dot4command.utils.SingletonFactory;
import com.dotosoft.dot4command.utils.StringUtils;

public class CreateObjectCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	private K objectClass;
	private K argumentsKey;
	private K toKey;

	public void setObjectClass(K objectClass) {
		this.objectClass = objectClass;
	}
	
	public void setArgumentsKey(K argumentsKey) {
		this.argumentsKey = argumentsKey;
	}

	public void setToKey(K toKey) {
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

		context.put(toKey, webClient);
		
		return Processing.CONTINUE;
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
	    		Object param = BeanUtils.getProperty(context, key);
	    		obj[i] = param;
	    	}
	    	return obj;
    	}
    	
        return null;
    }

}
