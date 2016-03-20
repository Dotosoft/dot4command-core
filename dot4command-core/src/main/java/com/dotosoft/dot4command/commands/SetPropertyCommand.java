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

public class SetPropertyCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	/** Primitive type name -> class map. */
	private static final Map PRIMITIVE_NAME_TYPE_MAP = new HashMap();

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
	
	private K type;
	private K value;
	private K toKey;
	
	public void setType(K type) {
		this.type = type;
	}

	public void setValue(K value) {
		this.value = value;
	}

	public void setToKey(K toKey) {
		this.toKey = toKey;
	}

	@Override
	public Processing onExecute(C context) throws Exception {
		if(PRIMITIVE_NAME_TYPE_MAP.containsKey(type.toLowerCase())) {
			Class clazz = (Class) PRIMITIVE_NAME_TYPE_MAP.get(type.toLowerCase());
			Object returnValue = clazz.getConstructor(String.class).newInstance(value);
			context.put(toKey, (V) returnValue);
		} else {
			context.put(toKey, (V) value);
		}
		
		return Processing.FINISHED;
	}

}
