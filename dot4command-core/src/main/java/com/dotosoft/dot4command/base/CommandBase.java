package com.dotosoft.dot4command.base;

import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.beanutils.PropertyUtils;

import com.dotosoft.dot4command.chain.Filter;
import com.dotosoft.dot4command.chain.Processing;

public abstract class CommandBase<K, V, C extends Map<K, V>> implements Filter<K, V, C> {

	private Logger log;
	
	public CommandBase() {
		log = Logger.getLogger(getClass().getName());
	}
	
	public void onStart(C context)	{ /* DO NOTHING */ }
	public void onLeave(C context)	{ /* DO NOTHING */ }
	public abstract Processing onExecute(C context);

	@Override
	public boolean postprocess(C context, Exception exception) {
		return false;
	}
	
	@Override
	public final Processing execute(C context) {
		log.info(getClass().getName() + " is processing");
		onStart(context);
		Processing result = onExecute(context);
		onLeave(context);
		return result;
	}
	
	public final Object getProperty(Object data, String expression) {
		return getProperty(data, expression, null);
	}
	
	public final Object getProperty(Object data, String expression, Object defaultValue) {
		try {
			Object returnValue = PropertyUtils.getProperty(data, expression);
			if(returnValue != null) {
				return returnValue;
			}
		} catch (Exception ex) {}
		
		return defaultValue;
	}
}
