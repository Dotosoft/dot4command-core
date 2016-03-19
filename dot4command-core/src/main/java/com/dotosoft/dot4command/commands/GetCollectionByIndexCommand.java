package com.dotosoft.dot4command.commands;

import java.util.Collection;
import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.BeanUtils;

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

		Collection collection = (Collection) BeanUtils.getProperty(context, collectionKey);
		
		Object index = BeanUtils.getProperty(context, indexKey);
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

		return Processing.CONTINUE;

	}

}
