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

public class ArithmeticCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	private static final String regexStr = "(?=[-/*+])|(?<=[-/*+])";
	
	private String evaluate;
	private String toKey;

	@Override
	public Processing onExecute(C context) {
		evaluate = evaluate.replaceAll("\\s+", "");
		String[] parts = evaluate.split(regexStr);
		V result = calculate(parts);
		context.put((K) toKey, result);
		
		return Processing.FINISHED;
	}
	
	private V calculate(String[] parts) {
		Double result = Double.parseDouble(parts[0]);

		for (int i = 1; i < parts.length; i += 2) {
		    String op = parts[i];
		    double val = Double.parseDouble(parts[i+1]);
		    switch (op) {
		        case "*" :
		            result *= val;
		            break;
		        case "/" :
		            result /= val;
		            break;
		        case "+" :
		            result += val;
		            break;
		        case "-" :
		            result -= val;
		            break;
		    }
		}
		return (V) result;
	}

	
	public String getEvaluate() {
		return evaluate;
	}
	
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	public String getToKey() {
		return toKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

}