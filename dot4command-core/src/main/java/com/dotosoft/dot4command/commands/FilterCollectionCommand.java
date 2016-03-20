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

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.BeanUtils;

public class FilterCollectionCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C>{

	private K fromKey;
	private K toKey;
	private K filterExpression;
	private K filterValue;

	public void setFromKey(K fromKey) {
		this.fromKey = fromKey;
	}

	public void setToKey(K toKey) {
		this.toKey = toKey;
	}

	public void setFilterExpression(K filterExpression) {
		this.filterExpression = filterExpression;
	}

	public void setFilterValue(K filterValue) {
		this.filterValue = filterValue;
	}

	@Override
	public Processing onExecute(C context) {

		Collection dataCollection = (Collection) BeanUtils.getProperty(context, fromKey);
		BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate(
				filterExpression, filterValue);
		CollectionUtils.filter(dataCollection, predicate);
		
		if(dataCollection.isEmpty()) {
			context.remove(toKey);
		} else {
			context.put(toKey, (V) dataCollection);
		}

		return Processing.FINISHED;
	}

}
