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

import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.dotosoft.dot4command.base.RemoveCommand;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.impl.TestContext;

/**
 * @version $Id�
 */
public class RemoveCommandTestCase {


    private RemoveCommand<String, Object, Context<String, Object>> command;
    private Context<String, Object> context;

    @Before
    public void setUp() throws Exception {
        command = new RemoveCommand<String, Object, Context<String, Object>>();
        context = new TestContext();

        context.put("Key", "Value");
        context.put("another Key", "another Value");
    }

    @Test
    public void nonExistingKeyDoesNotAlterContext() throws Exception {
        command.setFromKey("yet another Key");
        command.execute(context);

        assertThat(context, hasKey("Key"));
        assertThat(context, hasKey("another Key"));
    }

    @Test
    public void existingKeyIsRemoved() throws Exception {
        command.setFromKey("Key");
        command.execute(context);

        assertThat(context, not(hasKey("Key")));
        assertThat(context, hasKey("another Key"));
    }

}
