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

package com.dotosoft.dot4command.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionsUtil {
	private static Map<String, Method> methods = new HashMap<>();
    private static Map<String, Constructor> constructors = new HashMap<>();

    /**
     * Returns the {@code Method}  present on the class provided by clazz, with the name given on the name
     * parameter and the parameter types specified in params.
     * <p/>
     * The reflection calls are cached in memory so subsequent calls to this method will be fast.
     *
     * @param clazz  class that contains the wanted method
     * @param name   name of the method
     * @param params parameter types of the method
     * @return the desired method from {@code clazz} and {@code name}
     * @throws NoSuchMethodException
     */
    public static Method method(Class clazz, String name, Class<?>... params) throws NoSuchMethodException {
        List<String> paramClassNames = new ArrayList<>();
        if(params != null) {
	        for (Class<?> c : params) {
	            paramClassNames.add(c.getCanonicalName());
	        }
        }

        final String fullName = clazz.getCanonicalName() + "." + name + "(" + join("+", paramClassNames) + ")";
        if (!methods.containsKey(fullName)) {
            Method method = clazz.getMethod(name, params);
            methods.put(fullName, method);
        }
        return methods.get(fullName);
    }
    
    public static Method method(Class clazz, String name, Object... params) throws NoSuchMethodException {
        List<String> paramClassNames = new ArrayList<>();
        if(params != null) {
	        for (Object c : params) {
	            paramClassNames.add(c.getClass().getCanonicalName());
	        }
        }

        final String fullName = clazz.getCanonicalName() + "." + name + "(" + join("+", paramClassNames) + ")";
        if (!methods.containsKey(fullName)) {
            Method method = getSpecificMethodByFilter(clazz, name, params);
            methods.put(fullName, method);
        }
        return methods.get(fullName);
    }
    
    private static Method getSpecificMethodByFilter(Class clazz, String name, Object... params) {
    	Method[] methods = clazz.getDeclaredMethods();
    	Method findMethod = null;
    	for(Method method : methods) {
    		
    		if(!method.getName().equals(name)) continue;
    		
    		boolean isMatch = true;
    		Class[] parameterClass = method.getParameterTypes();
    		if(params.length == parameterClass.length) {
	    		for(int i=0;i<params.length;i++) {
	    			if(!parameterClass[i].isInstance(params[i])) {
	    				isMatch = false;
	    			}
	    		}
    		} else {
    			isMatch = false;
    		}
    		
    		if(isMatch) {
    			findMethod = method;
    			break;
    		}
    	}
    	
    	return findMethod;
    }

    /**
     * Returns the {@code Constructor} for the class provided, with the params given.
     * No params mean the empty constructor.
     * <p/>
     * The reflection calls are cached in memory so subsequent calls to this method will be fast.
     *
     * @param clazz  class where we want to get the constructor
     * @param params parameter types of the constructor, could be empty
     * @return the desired constructor
     * @throws NoSuchMethodException
     */
    public static Constructor constructor(Class clazz, Class<?>... params) throws NoSuchMethodException {
        List<String> paramClassNames = new ArrayList<>();
        for (Class<?> c : params) {
            paramClassNames.add(c.getCanonicalName());
        }
        final String fullName = clazz.getCanonicalName() + "(" + join("+", paramClassNames) + ")";

        if (!constructors.containsKey(fullName)) {
            Constructor constructor = clazz.getConstructor(params);
            constructors.put(fullName, constructor);
        }

        return constructors.get(fullName);
    }

    /**
     * Taken from Android's TextUtils.
     */
    private static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }
}
