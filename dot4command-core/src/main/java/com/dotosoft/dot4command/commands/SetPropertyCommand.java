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

import java.util.HashMap;
import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;

/**
 * <p>Set any context property stored under the <code>toKey</code> with <code>value</code>.</p>
 *
 * @param <K> the type of keys maintained by the context associated with this catalog
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this command
 *
 * @version $Id$
 */
public class SetPropertyCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	/** Primitive type name -> class map. */
	private static final Map<String, Class> PRIMITIVE_NAME_TYPE_MAP = new HashMap<String, Class>();
	
	private String type;
	private String value;
	private String key;

	/** Setup the primitives map. */
	static {
		PRIMITIVE_NAME_TYPE_MAP.put("boolean", Boolean.class);
		PRIMITIVE_NAME_TYPE_MAP.put("byte", Byte.class);
		PRIMITIVE_NAME_TYPE_MAP.put("char", Character.class);
		PRIMITIVE_NAME_TYPE_MAP.put("short", Short.class);
		PRIMITIVE_NAME_TYPE_MAP.put("int", Integer.class);
		PRIMITIVE_NAME_TYPE_MAP.put("long", Long.class);
		PRIMITIVE_NAME_TYPE_MAP.put("float", Float.class);
		PRIMITIVE_NAME_TYPE_MAP.put("double", Double.class);
	}

	@Override
	public Processing onExecute(C context) throws Exception {
		if(PRIMITIVE_NAME_TYPE_MAP.containsKey(type.toLowerCase())) {
			Class clazz = (Class) PRIMITIVE_NAME_TYPE_MAP.get(type.toLowerCase());
			Object returnValue = clazz.getConstructor(String.class).newInstance(value);
			context.put((K) key, (V) returnValue);
		} else {
			context.put((K) key, (V) value);
		}
		
		return Processing.FINISHED;
	}

	/**
     * <p>Set the type that should override context attribute with key <code>type</code>.</p>
     *
     * @param value The new value
     */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
     * <p>Return the type that should override context attribute with key <code>type</code>.</p>
     * @return The value.
     */
	public String getType() {
		return type;
	}

	/**
     * <p>Set the value that should override context attribute with key <code>key</code>.</p>
     *
     * @param value The new value
     */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
     * <p>Return the value that should override context attribute with key <code>key</code>.</p>
     * @return The value.
     */
	public String getValue() {
		return value;
	}

	/**
     * <p>Set the context attribute key for the attribute to override.</p>
     *
     * @param key The new key
     */
	public void setKey(String toKey) {
		this.key = toKey;
	}

	/**
     * <p>Return the context attribute key for the attribute to override.</p>
     * @return The context attribute key.
     */
	public String getKey() {
		return key;
	}
}
