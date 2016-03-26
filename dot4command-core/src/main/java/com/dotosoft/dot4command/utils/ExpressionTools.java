package com.dotosoft.dot4command.utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionTools {
	
	public static final String EVALUATE_REGEX_STRING = "(?=[-/*+]|[!=&|][=&|])|(?<=[-/*+]|[!=&|][=&|])";
	public static final String EXPRESSION_REGEX_STRING = "([\"'])((?:(?=(\\\\?))\\3.)*?)\\1";
	public static final String ARRAY_REGEX_STRING = "(?<=\\[).+?(?=\\])";
	
	/** Primitive type name -> class map. */
	private static final Map<String, Class> PRIMITIVE_NAME_TYPE_MAP = new HashMap<String, Class>();
	
	private static Pattern evaluatePattern;
	private static Pattern expressionPattern;
	private static Pattern arrayPattern;
	
	static {
		evaluatePattern  = Pattern.compile(EVALUATE_REGEX_STRING);
		expressionPattern  = Pattern.compile(EXPRESSION_REGEX_STRING);
		arrayPattern = Pattern.compile(ARRAY_REGEX_STRING);
		
		/** Setup the primitives map. */
		PRIMITIVE_NAME_TYPE_MAP.put("boolean", Boolean.class);
		PRIMITIVE_NAME_TYPE_MAP.put("boolean[]", Boolean[].class);
		PRIMITIVE_NAME_TYPE_MAP.put("byte", Byte.class);
		PRIMITIVE_NAME_TYPE_MAP.put("byte[]", Byte[].class);
		PRIMITIVE_NAME_TYPE_MAP.put("char", Character.class);
		PRIMITIVE_NAME_TYPE_MAP.put("char[]", Character[].class);
		PRIMITIVE_NAME_TYPE_MAP.put("short", Short.class);
		PRIMITIVE_NAME_TYPE_MAP.put("short[]", Short[].class);
		PRIMITIVE_NAME_TYPE_MAP.put("int", Integer.class);
		PRIMITIVE_NAME_TYPE_MAP.put("int[]", Integer[].class);
		PRIMITIVE_NAME_TYPE_MAP.put("long", Long.class);
		PRIMITIVE_NAME_TYPE_MAP.put("long[]", Long[].class);
		PRIMITIVE_NAME_TYPE_MAP.put("float", Float.class);
		PRIMITIVE_NAME_TYPE_MAP.put("float[]", Float[].class);
		PRIMITIVE_NAME_TYPE_MAP.put("double", Double.class);
		PRIMITIVE_NAME_TYPE_MAP.put("double[]", Double[].class);
		PRIMITIVE_NAME_TYPE_MAP.put("object", Object.class);
		PRIMITIVE_NAME_TYPE_MAP.put("object[]", Object[].class);
		PRIMITIVE_NAME_TYPE_MAP.put("collection", Collection.class);
		PRIMITIVE_NAME_TYPE_MAP.put("collection[]", Collection[].class);
		PRIMITIVE_NAME_TYPE_MAP.put("map", Map.class);
		PRIMITIVE_NAME_TYPE_MAP.put("map[]", Map[].class);
	}
	
	public static final Class getClass(String type) {
		Class clazz = null;
		if(ExpressionTools.PRIMITIVE_NAME_TYPE_MAP.containsKey(type.toLowerCase())) {
			clazz = (Class) ExpressionTools.PRIMITIVE_NAME_TYPE_MAP.get(type.toLowerCase()); 
		}
		return clazz;
	}
	
	public static final <T extends Boolean> T evaluate(Object context, String evaluate) throws Exception {
		String[] parts = extractEvaluationExpression(evaluate, ExpressionTools.EVALUATE_REGEX_STRING);
		
		Boolean result = false;
		Object obj1 = extractValue(context, parts[0]);
		String op = parts[1];
		Object obj2 = extractValue(context, parts[2]);

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
		return (T) result;
	}
	
	public static final <T extends Double> T calculate(Object context, String evaluate) {
		String[] parts = extractEvaluationExpression(evaluate, ExpressionTools.EVALUATE_REGEX_STRING);
		
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
		return (T) result;
	}
	
	private static final String[] extractEvaluationExpression(String evaluate, String regexPattern) {
		evaluate = evaluate.replaceAll("\\s+", "");
		return evaluate.split(regexPattern);
	}
	
	public static final Object extractValue(Object context, String evaluate) {
		Object result = null;
		
		Matcher matcher = expressionPattern.matcher(evaluate);
		if(matcher.find()) {
			result = matcher.group(2);
		} else if("null".equalsIgnoreCase(evaluate)) {
			result = null;
		} else if("true".equalsIgnoreCase(evaluate)) {
			result = true;
		} else if("false".equalsIgnoreCase(evaluate)) {
			result = false;
		} else if(evaluate.indexOf(":") > 0) {
			String keys[] = evaluate.split(":");
			Map resultMap = new HashMap();
			resultMap.put(String.valueOf(extractValue(context, keys[0])), extractValue(context, keys[1]));
			result = resultMap;
		} else {
			matcher = arrayPattern.matcher(evaluate);
			if(matcher.find()) {
				String key = evaluate.substring(0, evaluate.indexOf("["));
	    		String argumentKey = matcher.group(0);
	    		
	    		Object valueTmp = BeanUtils.getProperty(context, key);
	    		if(valueTmp != null) {
	    			Object parameterKey = BeanUtils.getProperty(context, argumentKey);
	    			if(valueTmp.getClass().isArray()) {
	    				int indexKey = Integer.parseInt(String.valueOf(parameterKey));
	    				result = Array.get(valueTmp, indexKey);
	    			}
	    			else if(valueTmp instanceof Collection) {
	    				Collection collection = (Collection) valueTmp;
	    				Integer index;
	    				if(parameterKey instanceof Integer) {
	    					index = (Integer) parameterKey;
	    				} else {
	    					index = Integer.parseInt(String.valueOf(parameterKey));
	    				}
	    				result = collection.toArray()[index];
	    			} else {
	    				result = BeanUtils.getProperty(valueTmp, String.valueOf(parameterKey));
	    			}
	    		}
			} else {
				result = BeanUtils.getProperty(context, evaluate);
			}
		}
		return result;
	}
	
	
	/**
	 * Return a <code>Class[]</code> describing the expected signature of the
	 * method.
	 * 
	 * @return The method signature.
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static Class[] getSignature(Object context, String argumentsKey) throws Exception {

		if (StringUtils.hasValue(argumentsKey)) {
			String[] keys = argumentsKey.split(",");
			Class[] clazz = new Class[keys.length];
			for (int i = 0; i < keys.length; i++) {
				String key = keys[i];
				Object param = BeanUtils.getProperty(context, key);
				clazz[i] = param.getClass();
			}
			return clazz;
		}

		return null;
	}
	
	/**
	 * Get the arguments to be passed into the dispatch method. Default
	 * implementation simply returns the context which was passed in, but
	 * subclasses could use this to wrap the context in some other type, or
	 * extract key values from the context to pass in. The length and types of
	 * values returned by this must coordinate with the return value of
	 * <code>getSignature()</code>
	 * 
	 * @param context
	 *            The Context being processed by this Command.
	 * @return The method arguments.
	 */
	public static Object[] getArgumentsObject(Object context, String argumentsKey) {

		if (StringUtils.hasValue(argumentsKey)) {
			String[] keys = argumentsKey.split(",");
			Object[] objects = new Object[keys.length];
			for (int i = 0; i < keys.length; i++) {
				objects[i] = extractValue(context, keys[i]);
			}
			return objects;
		}

		return null;
	}
}
