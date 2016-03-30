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

import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.impl.ChainBase;
import com.dotosoft.dot4command.utils.ExpressionTools;

public class IfCommand<K extends String, V extends Object, C extends Map<K, V>> extends ChainBase<K, V, C> {
	
	private static boolean ifFlag = true;
	
	protected String evaluate;

	@Override
	public Processing execute(C context) {
		IfCommand.ifFlag = true;
		try {
			IfCommand.ifFlag = ExpressionTools.evaluate(context, evaluate);
		} catch (Exception ex) {
			IfCommand.ifFlag = false;
		}

		Processing result = Processing.FINISHED;
		if (IfCommand.ifFlag) {
			result = super.execute(context);
		}

		return result;
	}
	
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
	
	public String getEvaluate() {
		return evaluate;
	}

	public static boolean getIfCommandKey() {
		return ifFlag;
	}
}
