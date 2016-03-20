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

import com.dotosoft.dot4command.base.LookupCommand;
import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.BeanUtils;

public class LoopByLookupCommand<K extends String, V extends Object, C extends Map<K, V>> extends LookupCommand<K, V, C> {

	private boolean doWhile = false;
	private K checkKey;
	private int loopTime = 0;
	private K indexKey;

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
	public Processing onExecute(C context) throws Exception {
		
		Command command = getCommand(context);
		if (command != null) {
			Processing result = Processing.FINISHED;
			Integer index = (Integer) BeanUtils.getProperty(context, indexKey, 0);
			context.put(indexKey, (V) index);
			
			boolean isLoopTime = (loopTime > 0);
			if (doWhile) {
				result = command.execute(context);
				if (result == Processing.TERMINATE) System.exit(0);
				if(isLoopTime) loopTime -= 1;
				index++;
				context.put(indexKey, (V) index);
			}
			while((isLoopTime && loopTime > 0) || BeanUtils.getProperty(context, checkKey) != null) {
				result = command.execute(context);
				if (isLoopTime) loopTime -= 1;
				if (result == Processing.BREAK) break;
				if (result == Processing.CONTINUE) continue;
				if (result == Processing.TERMINATE) System.exit(0);
				index++;
				context.put(indexKey, (V) index);
			}
			return result;
		} else {
			return Processing.FINISHED;
		}
	}

}
