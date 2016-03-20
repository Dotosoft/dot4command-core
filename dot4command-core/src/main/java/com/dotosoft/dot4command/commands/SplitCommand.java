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

import java.util.Arrays;
import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.BeanUtils;

public class SplitCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	private K fromKey;
	private K separator;
	private K toKey;

	public void setFromKey(K fromKey) {
		this.fromKey = fromKey;
	}

	public void setSeparator(K separator) {
		this.separator = separator;
	}

	public void setToKey(K toKey) {
		this.toKey = toKey;
	}

	@Override
	public Processing onExecute(C context) throws Exception {
		String data = (String) BeanUtils.getProperty(context, fromKey);
		String[] results = data.split(separator);
		context.put(toKey, (V) Arrays.asList(results));
		
		return Processing.FINISHED;
	}
}
