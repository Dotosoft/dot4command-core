package com.dotosoft.dot4command.commands;

import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.ExpressionTools;

public class SetObjectPropertyCommand<K extends String, V extends Object, C extends Map<K, V>>
		extends CommandBase<K, V, C> {

	private String key;
	private String property;
	private String value;

	@Override
	public Processing onExecute(C context) throws Exception {
		Object objKey = getProperty(context, key);
		Object objValue = ExpressionTools.extractValue(context, value);
		setProperty(objKey, property, objValue);

		return Processing.FINISHED;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
