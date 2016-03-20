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

package com.dotosoft.dot4command.base;

import java.util.Map;

import com.dotosoft.dot4command.chain.Filter;
import com.dotosoft.dot4command.chain.Processing;

public abstract class CommandBase<K extends String, V extends Object, C extends Map<K, V>> extends BaseObject implements Filter<K, V, C> {
	
	public CommandBase() {}
	
	public void onStart(C context)	{ /* DO NOTHING */ }
	public void onLeave(C context)	{ /* DO NOTHING */ }
	public abstract Processing onExecute(C context) throws Exception;
	
	public void onSuccess(C context)				{ /* DO NOTHING */ }
	public void onError(C context, Exception ex)	{
		getLogger().error(ex.getMessage(), ex);
	}

	@Override
	public boolean postprocess(C context, Exception exception) {
		return false;
	}
	
	@Override
	public final Processing execute(C context) {
		// log(">> START");
		onStart(context);
		Processing result = Processing.CONTINUE;
		try {
			// log(">> EXECUTE");
			result = onExecute(context);
			// log(">> SUCCESS");
			onSuccess(context);
		} catch (Exception ex) {
			// log(">> ERROR");
			onError(context, ex);
		}
		// log(">> LEAVE");
		onLeave(context);
		return result;
	}
}
