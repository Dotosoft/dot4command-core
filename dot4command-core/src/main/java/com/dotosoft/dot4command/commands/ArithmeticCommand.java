package com.dotosoft.dot4command.commands;

import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;

public class ArithmeticCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	private static final String regexStr = "(?=[-/*+])|(?<=[-/*+])";
	
	private String evaluate;
	private K toKey;

	public void setEvaluate(K evaluate) {
		this.evaluate = evaluate;
	}

	public void setToKey(K toKey) {
		this.toKey = toKey;
	}

	@Override
	public Processing onExecute(C context) {
		evaluate = evaluate.replaceAll("\\s+", "");
		String[] parts = evaluate.split(regexStr);
		V result = calculate(parts);
		context.put(toKey, result);
		
		return Processing.CONTINUE;
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

}
