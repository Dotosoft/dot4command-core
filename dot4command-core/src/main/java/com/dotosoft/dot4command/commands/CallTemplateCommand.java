package com.dotosoft.dot4command.commands;

import java.util.Map;

import com.dotosoft.dot4command.base.LookupCommand;
import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Processing;
import com.google.common.base.Splitter;

public class CallTemplateCommand <K extends String, V extends Object, C extends Map<K, V>> extends LookupCommand<K, V, C> {
	
	private Map keyMap;
	public void setKeyMap(String inputString) {
		keyMap = Splitter.on(",").withKeyValueSeparator(":").split(inputString);
	}
	
	@Override
	public Processing onExecute(C context) throws Exception {
		Command<K, V, C> command = getCommand(context);
        if (command != null && command instanceof TemplateCommand) {
        	command.modifyAttributes(keyMap);
            Processing result = command.execute(context);
            if (isIgnoreExecuteResult()) {
                return Processing.CONTINUE;
            }
            return result;
        }
        return Processing.CONTINUE;
	}
}
