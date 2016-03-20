package com.dotosoft.dot4command.base;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.dotosoft.dot4command.chain.Filter;
import com.dotosoft.dot4command.chain.Processing;

public abstract class CommandBase<K extends String, V extends Object, C extends Map<K, V>> extends BaseObject implements Filter<K, V, C> {
	
	public CommandBase() {}
	
	public void onStart(C context)	{ /* DO NOTHING */ }
	public void onLeave(C context)	{ /* DO NOTHING */ }
	public abstract Processing onExecute(C context) throws Exception;
	
	public void onSuccess(C context)				{ /* DO NOTHING */ }
	public void onError(C context, Exception ex)	{
		getLogger().error(ex.getMessage(), ex);
	}

	@Override
	public boolean postprocess(C context, Exception exception) {
		return false;
	}
	
	@Override
	public final Processing execute(C context) {
		// log(">> START");
		onStart(context);
		Processing result = Processing.CONTINUE;
		try {
			// log(">> EXECUTE");
			result = onExecute(context);
			// log(">> SUCCESS");
			onSuccess(context);
		} catch (Exception ex) {
			// log(">> ERROR");
			onError(context, ex);
		}
		// log(">> LEAVE");
		onLeave(context);
		return result;
	}
}
