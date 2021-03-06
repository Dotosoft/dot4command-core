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

package com.dotosoft.dot4command.config.xml;

import com.dotosoft.dot4command.base.BaseObject;
import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.chain.Processing;

/**
 * <p>
 * Test implementation of <code>Command</code> that exposes configurable
 * properties.
 * </p>
 */

public class TestCommand extends BaseObject implements
		Command<String, Object, Context<String, Object>> {

	private String bar = null;

	public String getBar() {
		return (this.bar);
	}

	public void setBar(String bar) {
		this.bar = bar;
	}

	private String foo = null;

	public String getFoo() {
		return (this.foo);
	}

	public void setFoo(String foo) {
		this.foo = foo;
	}

	public Processing execute(Context<String, Object> context) {
		return Processing.FINISHED;
	}

}
