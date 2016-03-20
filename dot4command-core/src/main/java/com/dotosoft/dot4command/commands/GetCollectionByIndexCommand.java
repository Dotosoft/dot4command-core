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

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;

public class GetCollectionByIndexCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	private K collectionKey;
	private K indexKey;
	private K toKey;

	public void setCollectionKey(K collectionKey) {
		this.collectionKey = collectionKey;
	}

	public void setIndexKey(K indexKey) {
		this.indexKey = indexKey;
	}

	public void setToKey(K toKey) {
		this.toKey = toKey;
	}

	public Processing onExecute(C context) throws Exception {

		Collection collection = (Collection) getProperty(context, collectionKey);
		
		Object index = getProperty(context, indexKey);
		Integer indexCollection;
		if(index instanceof Integer) {
			indexCollection = (Integer) index;
		} else {
			indexCollection = Integer.parseInt(String.valueOf(index));
		}

		if (collection != null && indexCollection < collection.size()) {
			context.put(toKey, (V) collection.toArray()[indexCollection]);
		} else {
			context.remove(toKey);
		}

		return Processing.FINISHED;

	}

}
