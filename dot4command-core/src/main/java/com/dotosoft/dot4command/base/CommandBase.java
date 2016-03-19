package com.dotosoft.dot4command.base;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dotosoft.dot4command.chain.Filter;
import com.dotosoft.dot4command.chain.Processing;

public abstract class CommandBase<K extends String, V extends Object, C extends Map<K, V>> implements Filter<K, V, C> {

	private Logger log;
	public Logger getLog() {
		return log;
	}
	
	public CommandBase() {
		log = LoggerFactory.getLogger(getClass().getName());
	}
	
	public void onStart(C context)	{ /* DO NOTHING */ }
	public void onLeave(C context)	{ /* DO NOTHING */ }
	public abstract Processing onExecute(C context) throws Exception;
	
	public void onSuccess(C context)				{ /* DO NOTHING */ }
	public void onError(C context, Exception ex)	{ /* DO NOTHING */ }

	@Override
	public boolean postprocess(C context, Exception exception) {
		return false;
	}
	
	@Override
	public final Processing execute(C context) {
		// log.info(">> START");
		onStart(context);
		Processing result = Processing.CONTINUE;
		try {
			log.info(">> EXECUTE");
			result = onExecute(context);
			// log.info(">> SUCCESS");
			onSuccess(context);
		} catch (Exception ex) {
			// log.info(">> ERROR");
			onError(context, ex);
		}
		// log.info(">> LEAVE");
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
