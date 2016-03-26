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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

import com.dotosoft.dot4command.base.ModifierHandler;
import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.impl.ChainBase;
import com.google.common.base.Splitter;

public class CallTemplateCommand <K extends String, V extends Object, C extends Map<K, V>> extends LookupCommand<K, V, C> {
	
	private Map keyMap;
	
	@Override
	public Processing onExecute(C context) throws Exception {
		Command<K, V, C> command = getCommand(context);
        if (command != null) {
        	Command newTemp = (Command) command.clone();
        	modifyKeyMap(newTemp);
        	
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
	
	private void modifyKeyMap(Command newTemp) {
		ModifierHandler modifier = new ModifierHandler() {
			StrSubstitutor sub;
			
			@Override
			public void modifier(Object... params) {
				Command valueCommand = (Command) params[0];
				Map valuesMap = (Map) params[1]; 
				sub = new StrSubstitutor(valuesMap);
				modifyField(valueCommand);
			}
			
			private void modifyField(Command valueCommand) {
				List<Field> allFields = listAllFields(valueCommand);
			    for (Field field : allFields) {
			    	try {
				    	Object value = getProperty(valueCommand, field.getName());
				    	if(value instanceof String) {
				    		setProperty(valueCommand, field.getName(), sub.replace(value));
				    	}
			    	} catch (Exception ex) {}
				}
			    
			    if(valueCommand instanceof ChainBase) {
			    	ChainBase chainBase = (ChainBase) valueCommand;
				    List<Command<K, V, C>> listOfCommands = chainBase.getCommands();
					for(Command comm : listOfCommands) {
						modifyField(comm);
					}
			    }
			}
			
			private List<Field> listAllFields(Object objReflection) {
				List<Field> fieldList = new ArrayList<Field>();
			    Class tmpClass = objReflection.getClass();
			    while (tmpClass != null) {
			        fieldList.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
			        tmpClass = tmpClass .getSuperclass();
			    }
			    return fieldList;
			}
		};
		
		newTemp.modify(modifier, newTemp, keyMap);
	}
	
	public void setKeyMap(String inputString) {
		inputString = inputString.replaceAll("\n", "").replaceAll("\r", "").replaceAll(" ", "");
		keyMap = Splitter.on(",").withKeyValueSeparator(":").split(inputString);
	}
}
