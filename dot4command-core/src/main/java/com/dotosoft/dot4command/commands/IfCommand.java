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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.impl.ChainBase;
import com.dotosoft.dot4command.utils.EqualsHelper;

public class IfCommand<K extends String, V extends Object, C extends Map<K, V>> extends ChainBase<K, V, C> {

	private static Pattern pattern = Pattern.compile("([\"'])((?:(?=(\\\\?))\\3.)*?)\\1");
	private static final String regexStr = "(?=[!=&|][=&|])|(?<=[!=&|][=&|])";
	private static boolean ifFlag = true;
	
	private String evaluate;

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

		Processing result = Processing.FINISHED;
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
	
	private V extractValue(C context, String part) {
		// "(["'])(?:(?=(\\?))\2.)*?\1"
		Object result = null;
		Matcher matcher = pattern.matcher(part);
		if(matcher.find()) {
			result = matcher.group(2);
		} else if("null".equalsIgnoreCase(part)) {
			result = null;
		} else if("true".equalsIgnoreCase(part)) {
			result = true;
		} else if("false".equalsIgnoreCase(part)) {
			result = false;
		} else {
			result = getProperty(context, part);
		}
		return (V) result;
	}
	
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
	
	public String getEvaluate() {
		return evaluate;
	}

	public static boolean getIfCommandKey() {
		return ifFlag;
	}
}
