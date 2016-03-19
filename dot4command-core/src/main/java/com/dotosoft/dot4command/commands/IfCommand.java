package com.dotosoft.dot4command.commands;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.impl.ChainBase;
import com.dotosoft.dot4command.utils.BeanUtils;
import com.dotosoft.dot4command.utils.EqualsHelper;

public class IfCommand<K extends String, V extends Object, C extends Map<K, V>> extends ChainBase<K, V, C> {

	private static final String regexStr = "(?=[!=&|][=&|])|(?<=[!=&|][=&|])";
	private static boolean ifFlag = true;
	
	private String evaluate;
	
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
	
	public static boolean getIfCommandKey() {
		return IfCommand.ifFlag;
	}

	@Override
	public Processing execute(C context) {
		IfCommand.ifFlag = true;
		evaluate = evaluate.replaceAll("\\s+", "");
		String[] parts = evaluate.split(regexStr);
		boolean isValid = false;
		try {
			isValid = evaluate(context, parts);
		} catch (Exception ex) {
			isValid = false;
		}

		Processing result = Processing.CONTINUE;
		if (isValid) {
			result = super.execute(context);
		} else {
			IfCommand.ifFlag = false;
		}

		return result;
	}

	private boolean evaluate(C context, String[] parts) throws Exception {
		boolean result = false;
		V obj1 = extractValue(context, parts[0]);
		String op = parts[1];
		V obj2 = extractValue(context, parts[2]);

		switch (op) {
		case "!=":
			result = !(EqualsHelper.equals(obj1, obj2));
			break;
		case "==":
			result = (EqualsHelper.equals(obj1, obj2));
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
	
	Pattern pattern;
	public IfCommand() {
		pattern = Pattern.compile("([\"'])((?:(?=(\\\\?))\\3.)*?)\\1");
	}
	
	private V extractValue(C context, String part) {
		// "(["'])(?:(?=(\\?))\2.)*?\1"
		Object result = null;
		Matcher matcher = pattern.matcher(part);
		if(matcher.find()) {
			result = matcher.group(2);
		} else if("null".equalsIgnoreCase(part)) {
			result = null;
		} else {
			result = BeanUtils.getProperty(context, part);
		}
		return (V) result;
	}

}
