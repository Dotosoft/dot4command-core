package com.dotosoft.dot4command.commands;

import static com.dotosoft.dot4command.chain.Processing.CONTINUE;

import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Processing;

public class ContinueCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

	@Override
	public Processing onExecute(C context) {
		return CONTINUE;
	}

}
