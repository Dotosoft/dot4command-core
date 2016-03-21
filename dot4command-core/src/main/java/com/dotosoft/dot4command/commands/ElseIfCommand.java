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

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;

public class ElseIfCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	private static final String regexStr = "(?=[!=&|][=&|])|(?<=[!=&|][=&|])";

	private String evaluate;

	public static String getRegexstr() {
		return regexStr;
	}

	public String getEvaluate() {
		return evaluate;
	}

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	@Override
	public Processing onExecute(C context) {

		Processing result = Processing.FINISHED;
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
		Object obj1 = getProperty(context, parts[0]);
		String op = parts[1];
		Object obj2 = getProperty(context, parts[2]);

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
