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

public class FilterCollectionCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C>{

	private String fromKey;
	private String toKey;
	private String filterExpression;
	private String filterValue;

	public String getFromKey() {
		return fromKey;
	}

	public String getToKey() {
		return toKey;
	}

	public String getFilterExpression() {
		return filterExpression;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFromKey(String fromKey) {
		this.fromKey = fromKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	@Override
	public Processing onExecute(C context) {

		Collection dataCollection = (Collection) getProperty(context, fromKey);
		BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate(
				filterExpression, filterValue);
		CollectionUtils.filter(dataCollection, predicate);
		
		if(dataCollection.isEmpty()) {
			context.remove(toKey);
		} else {
			context.put((K) toKey, (V) dataCollection);
		}

		return Processing.FINISHED;
	}

}
