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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.dotosoft.dot4command.commands.SetPropertyCommand;

/**
 * @version $Id$
 */
public class SetPropertyCommandTestCase {


    private SetPropertyCommand<String, Object, Map<String, Object>> command;
    private Map<String, Object> context;

    @Before
    public void setUp() throws Exception {
        command = new SetPropertyCommand<String, Object, Map<String, Object>>();

        context = new HashMap<String, Object>();
        context.put("Key", "Value");
    }

    @Test
    public void nonExistingKeyDoesNotAlterContext() throws Exception {
        command.setKey("another Key");
        command.setValue("another Value");

        command.execute(context);

        assertThat(context, hasEntry("Key", (Object) "Value"));
        assertThat(context.keySet(), hasSize(1));
    }

    @Test
    public void existingKeyWillBeOverridden() throws Exception {
        command.setKey("Key");
        command.setValue("new Value");

        command.execute(context);

        assertThat(context, hasEntry("Key", (Object) "new Value"));
        assertThat(context.keySet(), hasSize(1));
    }
}
