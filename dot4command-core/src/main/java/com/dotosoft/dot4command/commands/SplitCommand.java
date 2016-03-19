package com.dotosoft.dot4command.commands;

import java.util.Arrays;
import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.BeanUtils;

public class SplitCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	private K fromKey;
	private K separator;
	private K toKey;

	public void setFromKey(K fromKey) {
		this.fromKey = fromKey;
	}

	public void setSeparator(K separator) {
		this.separator = separator;
	}

	public void setToKey(K toKey) {
		this.toKey = toKey;
	}

	@Override
	public Processing onExecute(C context) throws Exception {
		String data = (String) BeanUtils.getProperty(context, fromKey);
		String[] results = data.split(separator);
		context.put(toKey, (V) Arrays.asList(results));
		
		return Processing.CONTINUE;
	}
}
