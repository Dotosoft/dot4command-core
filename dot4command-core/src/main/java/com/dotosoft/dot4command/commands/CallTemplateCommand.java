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
import com.google.common.base.Splitter;

public class CallTemplateCommand <K extends String, V extends Object, C extends Map<K, V>> extends LookupCommand<K, V, C> {
	
	private Map keyMap;
	
	@Override
	public Processing onExecute(C context) throws Exception {
		Command<K, V, C> command = getCommand(context);
        if (command != null) {
        	Command newTemp = (Command) command.clone();
        	newTemp.modifyAttributes(keyMap);
            Processing result = newTemp.execute(context);
            newTemp = null;
            System.gc();
            if (isIgnoreExecuteResult()) {
                return Processing.FINISHED;
            }
            return result;
        }
        return Processing.FINISHED;
	}
	
	public void setKeyMap(String inputString) {
		inputString = inputString.replaceAll("\n", "").replaceAll("\r", "").replaceAll(" ", "");
		keyMap = Splitter.on(",").withKeyValueSeparator(":").split(inputString);
	}
}