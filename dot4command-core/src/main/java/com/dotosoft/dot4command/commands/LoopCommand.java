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
import java.util.Collection;
import java.util.Map;

import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.impl.ChainBase;
import com.dotosoft.dot4command.utils.StringUtils;

public class LoopCommand<K extends String, V extends Object, C extends Map<K, V>> extends ChainBase<K, V, C> {

	private boolean doWhile = false;
	private String checkKey;
	private String checkCollectionKey;
	private int loopTime = 0;
	private String indexKey;

	@Override
	public Processing execute(C context) {
		
		if(StringUtils.hasValue(checkCollectionKey)) {
			Object collection = (Object) getProperty(context, checkCollectionKey);
			if(collection != null) {
				if(collection.getClass().isArray()) { 
					loopTime = Array.getLength(collection);
				} else if(collection instanceof Collection) {
					loopTime = ((Collection) collection).size();
				}
			}
		}
		
		Processing result = Processing.FINISHED;
		Integer index = 0;
		if(StringUtils.hasValue(indexKey)) {
			index = (Integer) getProperty(context, indexKey, 0);
			context.put((K) indexKey, (V) index);
		}
		
		int loopTimeCheck = loopTime;
		
		boolean isLoopTime = (loopTimeCheck > 0);
		if (doWhile) {
			result = super.execute(context);
			if (result == Processing.TERMINATE) System.exit(0);
			if(isLoopTime) loopTimeCheck -= 1;
			
			index++;
			if(StringUtils.hasValue(indexKey)) {
				context.put((K) indexKey, (V) index);
			}
		}
		while((isLoopTime && loopTimeCheck > 0) || (StringUtils.hasValue(checkKey) && getProperty(context, checkKey) != null)) {
			result = super.execute(context);
			if (isLoopTime) loopTimeCheck -= 1;
			if (result == Processing.BREAK) break;
			if (result == Processing.CONTINUE) continue;
			if (result == Processing.TERMINATE) System.exit(0);
			
			index++;
			if(StringUtils.hasValue(indexKey)) {
				context.put((K) indexKey, (V) index);
			}
		}
		
		if(StringUtils.hasValue(indexKey)) {
			context.remove(indexKey);
		}
		
		return result;
	}

	public boolean isDoWhile() {
		return doWhile;
	}
	
	public void setDoWhile(boolean doWhile) {
		this.doWhile = doWhile;
	}

	public void setCheckKey(String checkKey) {
		this.checkKey = checkKey;
	}
	
	public String getCheckKey() {
		return checkKey;
	}

	public void setCheckCollectionKey(String checkCollectionKey) {
		this.checkCollectionKey = checkCollectionKey;
	}
	
	public String getCheckCollectionKey() {
		return checkCollectionKey;
	}
	
	public void setLoopTime(int loopTime) {
		this.loopTime = loopTime;
	}

	public int getLoopTime() {
		return loopTime;
	}

	public String getIndexKey() {
		return indexKey;
	}
	
	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
	}
}
