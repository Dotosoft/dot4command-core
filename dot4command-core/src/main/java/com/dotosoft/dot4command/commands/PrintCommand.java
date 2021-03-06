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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.ExpressionTools;
import com.dotosoft.dot4command.utils.StringUtils;

public class PrintCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {
	
	private String message;
	private String key;

	@Override
	public Processing onExecute(C context) throws Exception {

		String messageInfo = "";
		
		if (StringUtils.hasValue(message)) {
			messageInfo = message;
		}
		
		List paramMessages = new ArrayList();
		if(StringUtils.hasValue(key)) {
			String[] splitKeys = key.split(",");
			for(String splitKey : splitKeys) {
				Object param = ExpressionTools.extractValue(context, splitKey);
				if(param.getClass().isArray()) {
					paramMessages.add(Arrays.toString((Object[])param));
				} else {
					paramMessages.add(String.valueOf(param));
				}
			}
		}
		
		if(StringUtils.hasValue(messageInfo)) {
			print( String.format(messageInfo, paramMessages.toArray()) );
		} else {
			print( Arrays.toString(paramMessages.toArray() ));
		}

		return Processing.FINISHED;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
}
