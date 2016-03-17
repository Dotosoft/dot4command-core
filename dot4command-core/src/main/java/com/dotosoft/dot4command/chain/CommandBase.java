package com.dotosoft.dot4command.chain;

import java.util.Map;
import java.util.logging.Logger;

public abstract class CommandBase<K, V, C extends Map<K, V>> implements Filter<K, V, C> {
	private Logger log;
	
	public CommandBase() {
		log = Logger.getLogger(getClass().getName());
	}
	
	public final Processing execute(C context) {
		log.info(getClass().getName() + " is processing");
		onStart(context);
		Processing result =  onExecute(context);
		onLeave(context);
		return result;
	}
	
	public abstract Processing onExecute(C context);

	public void onStart(C context) { /* DO NOTHING */ }
	public void onLeave(C context) { /* DO NOTHING */ }
	public boolean postprocess(C context, Exception exception) { 
		return false;
	}
}
