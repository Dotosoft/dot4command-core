package com.dotosoft.dot4command.commands;

import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.BeanUtils;

public class ElseIfCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	private static final String regexStr = "(?=[!=&|][=&|])|(?<=[!=&|][=&|])";

	private String evaluate;

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	@Override
	public Processing onExecute(C context) {

		Processing result = Processing.CONTINUE;
		if (!IfCommand.getIfCommandKey()) {
			evaluate = evaluate.replaceAll("\\s+", "");
			String[] parts = evaluate.split(regexStr);
			boolean isValid = false;
			try {
				isValid = evaluate(context, parts);
			} catch (Exception ex) { 
				isValid = false;
			}
			if (isValid) {
				result = super.execute(context);
			}
		}

		return result;
	}

	private boolean evaluate(C context, String[] parts) throws Exception {
		boolean result = false;
		Object obj1 = BeanUtils.getProperty(context, parts[0]);
		String op = parts[1];
		Object obj2 = BeanUtils.getProperty(context, parts[2]);

		switch (op) {
		case "!=":
			result = obj1 != obj2;
			break;
		case "==":
			result = obj1 == obj2;
			break;
		case "||":
			result = ((Boolean) obj1 || (Boolean) obj2);
			break;
		case "&&":
			result = ((Boolean) obj1 && (Boolean) obj2);
			break;
		default:
			throw new Exception("Expression is not valid");
		}
		return result;
	}
}
