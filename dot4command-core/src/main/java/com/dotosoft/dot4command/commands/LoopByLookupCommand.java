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

import java.util.Map;

import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Processing;

public class LoopByLookupCommand<K extends String, V extends Object, C extends Map<K, V>> extends LookupCommand<K, V, C> {

	private boolean doWhile = false;
	private int loopTime = 0;
	private String checkKey;
	private String indexKey;

	@Override
	public Processing onExecute(C context) throws Exception {
		
		Command command = getCommand(context);
		if (command != null) {
			Processing result = Processing.FINISHED;
			Integer index = (Integer) getProperty(context, indexKey, 0);
			context.put((K) indexKey, (V) index);
			
			boolean isLoopTime = (loopTime > 0);
			if (doWhile) {
				result = command.execute(context);
				if (result == Processing.TERMINATE) System.exit(0);
				if(isLoopTime) loopTime -= 1;
				index++;
				context.put((K) indexKey, (V) index);
			}
			while((isLoopTime && loopTime > 0) || getProperty(context, checkKey) != null) {
				result = command.execute(context);
				if (isLoopTime) loopTime -= 1;
				if (result == Processing.BREAK) break;
				if (result == Processing.CONTINUE) continue;
				if (result == Processing.TERMINATE) System.exit(0);
				index++;
				context.put((K) indexKey, (V) index);
			}
			return result;
		} else {
			return Processing.FINISHED;
		}
	}

	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
	}
	
	public String getIndexKey() {
		return indexKey;
	}

	public void setDoWhile(boolean doWhile) {
		this.doWhile = doWhile;
	}
	
	public boolean isDoWhile() {
		return doWhile;
	}

	public void setLoopTime(int loopTime) {
		this.loopTime = loopTime;
	}
	
	public int getLoopTime() {
		return loopTime;
	}

	public void setCheckKey(String checkKey) {
		this.checkKey = checkKey;
	}

	public String getCheckKey() {
		return checkKey;
	}
}
