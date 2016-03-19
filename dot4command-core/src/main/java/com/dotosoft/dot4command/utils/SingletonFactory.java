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
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SingletonFactory {
	private static final Map<String, Object> INSTANCES = new HashMap<>();

	private static class SingletonHolder<T> {
		private static <T> T getInstance(Class<T> clazz)
				throws InstantiationException, IllegalAccessException,
				NoSuchMethodException, SecurityException,
				IllegalArgumentException, InvocationTargetException {
			Constructor<T> constructor = (Constructor<T>) clazz.getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			return constructor.newInstance(null);
		}
		
		private static <T> T getInstance(Class<T> clazz, Object... params)
				throws InstantiationException, IllegalAccessException,
				NoSuchMethodException, SecurityException,
				IllegalArgumentException, InvocationTargetException {
			
			Class[] classParam = new Class[params.length];
			for(int i=0;i<params.length;i++) {
				classParam[i] = params[i].getClass();
			}
			
			Constructor<T> constructor = (Constructor<T>) clazz.getDeclaredConstructor(classParam);
			constructor.setAccessible(true);
			return constructor.newInstance(params);
		}
	}

	public static <T> T getInstance(Class<T> clazz) throws InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {
		if (INSTANCES.containsKey(clazz)) {
			return (T) INSTANCES.get(clazz);
		} else {
			T instance = SingletonHolder.getInstance(clazz);
			INSTANCES.put(clazz.getName(), instance);
			return instance;
		}
	}
	
	public static <T> T getInstance(Class<T> clazz, Object... params) throws InstantiationException,
			IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException {
		if (INSTANCES.containsKey(clazz)) {
			return (T) INSTANCES.get(clazz);
		} else {
			T instance = SingletonHolder.getInstance(clazz, params);
			INSTANCES.put(clazz.getName(), instance);
			return instance;
		}
	}

	public static <T extends Object> void putInstance( Class<T> clazz, T instance ) {
		if (!INSTANCES.containsKey(clazz)) {
			INSTANCES.put(clazz.getName(), instance);
		}
	}
}