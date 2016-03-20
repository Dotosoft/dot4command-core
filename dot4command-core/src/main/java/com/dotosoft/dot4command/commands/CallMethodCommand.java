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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.BeanUtils;
import com.dotosoft.dot4command.utils.ReflectionsUtil;
import com.dotosoft.dot4command.utils.StringUtils;

public class CallMethodCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	/** Cache of methods */
	private Map methods = new WeakHashMap();

	/** Method Properties */
	private K method;
	private K methodKey;
	private K argumentsKey;
	private boolean staticFlag = false;

	private K toKey;

	/**
	 * Look up the method specified by either "method" or "methodKey" and invoke
	 * it, returning a boolean value as interpreted by
	 * <code>evaluateResult</code>.
	 * 
	 * @param context
	 *            The Context to be processed by this Command.
	 * @return the result of method being dispatched to.
	 * @throws IllegalStateException
	 *             if neither 'method' nor 'methodKey' properties are defined
	 * @throws Exception
	 *             if any is thrown by the invocation. Note that if invoking the
	 *             method results in an InvocationTargetException, the cause of
	 *             that exception is thrown instead of the exception itself,
	 *             unless the cause is an <code>Error</code> or other
	 *             <code>Throwable</code> which is not an <code>Exception</code>
	 *             .
	 */
	@Override
	public Processing onExecute(C context) throws Exception {

		if (this.getMethod() == null && this.getMethodKey() == null) {
			throw new IllegalStateException(
					"Neither 'method' nor 'methodKey' properties are defined ");
		}

		Method methodObject = extractMethod(context);
		if(methodObject != null) {
			try {
				V returnValue = null;
				if (staticFlag) {
					returnValue = (V) methodObject.invoke(null, getArguments(context));
				} else {
					Object objectValue = BeanUtils.getProperty(context, methodKey);
					returnValue = (V) methodObject.invoke(objectValue, getArguments(context));
				}
				
				if(StringUtils.hasValue(toKey)) {
					context.put(toKey, returnValue);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		
		return Processing.FINISHED;
	}

	/**
	 * Extract the dispatch method. The base implementation uses the command's
	 * <code>method</code> property as the name of a method to look up, or, if
	 * that is not defined, looks up the the method name in the Context using
	 * the <code>methodKey</code>.
	 *
	 * @param context
	 *            The Context being processed by this Command.
	 * @return The method to execute
	 * @throws NoSuchMethodException
	 *             if no method can be found under the specified name.
	 * @throws NullPointerException
	 *             if no methodName cannot be determined
	 */
	protected Method extractMethod(C context) throws Exception {

		Method theMethod = null;
		
		Class objectClass = getClassObject(context);
		if(objectClass != null) {
			try {			
				theMethod = ReflectionsUtil.method(objectClass, getMethod(), getSignature(context));
			} catch (Exception ex) {
				try {
					theMethod = ReflectionsUtil.method(objectClass, getMethod(), getSignatureObject(context));
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		}
		return theMethod;
	}

	private Class getClassObject(C context) throws Exception {
		if (staticFlag) {
			return Class.forName(getMethodKey());
		}

		Object objectValue = BeanUtils.getProperty(context, methodKey);
		if(objectValue != null) { 
			return objectValue.getClass();
		}
		
		return null;
	}

	/**
	 * Evaluate the result of the method invocation as a boolean value. Base
	 * implementation expects that the invoked method returns boolean
	 * true/false, but subclasses might implement other interpretations.
	 * 
	 * @param o
	 *            The result of the methid execution
	 * @return The evaluated result/
	 */
	protected boolean evaluateResult(Object o) {

		Boolean result = (Boolean) o;
		return (result != null && result.booleanValue());

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
	protected Class[] getSignature(C context) throws Exception {

		if (StringUtils.hasValue(getArgumentsKey())) {
			String[] keys = getArgumentsKey().split(",");
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
	
	protected Class[] getSignatureObject(C context) {

		if (StringUtils.hasValue(getArgumentsKey())) {
			String[] keys = getArgumentsKey().split(",");
			Class[] clazz = new Class[keys.length];
			for (int i = 0; i < keys.length; i++) {
				clazz[i] = Object.class;
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
	protected Object[] getArguments(C context) throws Exception {
		if (StringUtils.hasValue(getArgumentsKey())) {
			String[] keys = getArgumentsKey().split(",");
			Object[] objects = new Object[keys.length];
			for (int i = 0; i < keys.length; i++) {
				String key = keys[i];
				Object param = BeanUtils.getProperty(context, key);
				objects[i] = param;
			}
			return objects;
		}

		return null;
	}

	/**
	 * Return the method name.
	 * 
	 * @return The method name.
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Return the Context key for the method name.
	 * 
	 * @return The Context key for the method name.
	 */
	public String getMethodKey() {
		return methodKey;
	}

	/**
	 * Set the method name.
	 * 
	 * @param method
	 *            The method name.
	 */
	public void setMethod(K method) {
		this.method = method;
	}

	/**
	 * Set the Context key for the method name.
	 * 
	 * @param methodKey
	 *            The Context key for the method name.
	 */
	public void setMethodKey(K methodKey) {
		this.methodKey = methodKey;
	}

	public void setToKey(K toKey) {
		this.toKey = toKey;
	}

	public void setMethods(Map methods) {
		this.methods = methods;
	}

	public void setArgumentsKey(K argumentsKey) {
		this.argumentsKey = argumentsKey;
	}

	public Map getMethods() {
		return methods;
	}

	public K getToKey() {
		return toKey;
	}

	public K getArgumentsKey() {
		return argumentsKey;
	}

	public void setStaticFlag(boolean staticFlag) {
		this.staticFlag = staticFlag;
	}

}