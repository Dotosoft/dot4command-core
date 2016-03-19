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

import java.util.Collection;
import java.util.Map;

import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.impl.ChainBase;
import com.dotosoft.dot4command.utils.BeanUtils;
import com.dotosoft.dot4command.utils.StringUtils;

public class LoopCommand<K extends String, V extends Object, C extends Map<K, V>> extends ChainBase<K, V, C> {

	private boolean doWhile = false;
	private K checkKey;
	private K checkCollectionKey;
	private int loopTime = 0;
	private K indexKey;

	public void setCheckCollectionKey(K checkCollectionKey) {
		this.checkCollectionKey = checkCollectionKey;
	}

	public void setIndexKey(K indexKey) {
		this.indexKey = indexKey;
	}

	public void setDoWhile(boolean doWhile) {
		this.doWhile = doWhile;
	}

	public void setLoopTime(int loopTime) {
		this.loopTime = loopTime;
	}

	public void setCheckKey(K checkKey) {
		this.checkKey = checkKey;
	}

	@Override
	public Processing execute(C context) {
		
		if(StringUtils.hasValue(checkCollectionKey)) {
			Collection collection = (Collection) BeanUtils.getProperty(context, checkCollectionKey);
			loopTime = collection.size();
		}
		
		Processing result = Processing.CONTINUE;
		Integer index = (Integer) BeanUtils.getProperty(context, indexKey, 0);
		context.put(indexKey, (V) index);
		
		int loopTimeCheck = loopTime;
		
		boolean isLoopTime = (loopTimeCheck > 0);
		if (doWhile) {
			result = super.execute(context);
			if(isLoopTime) loopTimeCheck -= 1;
			index++;
			context.put(indexKey, (V) index);
		}
		while((isLoopTime && loopTimeCheck > 0) || (StringUtils.hasValue(checkKey) && BeanUtils.getProperty(context, checkKey) != null)) {
			result = super.execute(context);
			if (isLoopTime) loopTimeCheck -= 1;
			if (result == Processing.BREAK) break;
			if (result == Processing.CONTINUE) continue;
			index++;
			context.put(indexKey, (V) index);
		}
		
		context.remove(indexKey);
		
		return result;
	}

}
