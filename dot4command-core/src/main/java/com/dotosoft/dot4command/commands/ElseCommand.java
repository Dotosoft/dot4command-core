package com.dotosoft.dot4command.commands;

import static com.dotosoft.dot4command.commands.IfCommand.getIfCommandKey;

import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.impl.ChainBase;

public class ElseCommand<K extends String, V extends Object, C extends Map<K, V>> extends ChainBase<K, V, C> {
	
	@Override
	public Processing execute(C context) {
		Processing result = Processing.FINISHED;
		if(!getIfCommandKey()) {
			result = super.execute(context);
		}
		return result;
	}
	
}
