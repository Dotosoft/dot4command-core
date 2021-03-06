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

package com.dotosoft.dot4command.testutils;

import static com.dotosoft.dot4command.testutils.HasLog.hasLog;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.chain.Processing;

/**
 * @version $Id$
 */
public class NonDelegatingCommandTestCase {

    private static final String ID = UUID.randomUUID().toString();

    private NonDelegatingCommand command;
    private Context<String, Object> context;

    @Before
    public void setUp() throws Exception {
        command = new NonDelegatingCommand(ID);
        context = new TestContext();
    }

    @Test(expected = IllegalArgumentException.class)
    public void executeNullThrowsException() throws Exception {
        command.execute(null);
    }

    @Test
    public void createsLogInEmptyContext() throws Exception {
        execute();

        assertThat(context.keySet(), hasSize(1));
        assertThat(context, hasKey("log"));
        assertThat(context, hasLog(ID));
    }

    @Test
    public void existingLogIsReused() throws Exception {
        context.put("log", new StringBuilder("some content"));
        execute();

        assertThat(context, hasLog("some content/" + ID));
    }

    private void execute() {
        assertEquals(Processing.FINISHED, command.execute(context));
    }
}
