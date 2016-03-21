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

import java.util.List;
import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;

public class IterateCollectionCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {
	private String collectionKey;
	private String incrementKey;
	private String toKey;

	public String getCollectionKey() {
		return collectionKey;
	}

	public String getIncrementKey() {
		return incrementKey;
	}

	public String getToKey() {
		return toKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	public void setCollectionKey(String collectionKey) {
		this.collectionKey = collectionKey;
	}

	public void setIncrementKey(String incrementKey) {
		this.incrementKey = incrementKey;
	}

	@Override
	public Processing onExecute(C context) throws Exception {
		List coll = (List) getProperty(context, collectionKey); 
		if(coll != null && !coll.isEmpty()) {
			Integer index = (Integer) context.get(incrementKey);
			if(index == null) {
				index = 0;
			}
			
			if(index >= coll.size()) {
				context.remove(toKey);
				context.remove(incrementKey);
			} else {
				context.put((K) toKey, (V) coll.get(index));
				index++;
				context.put((K) incrementKey, (V) index);
			}
		}
		return Processing.FINISHED;
	}

}
