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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.testutils.HasLog;

/**
 * @version $Id$
 */
public class HasLogTestCase {

    private Context<String, Object> context;
    private HasLog matcher;

    @Before
    public void setUp() throws Exception {
        context = new TestContext<String, Object>();
        matcher = new HasLog("content");
    }

    @Test
    public void noLogFails() throws Exception {
        assertFalse(matcher.matchesSafely(context, new StringDescription()));
    }

    @Test
    public void logWithWrongContentFails() throws Exception {
        context.put("log", new StringBuilder("wrong content"));
        assertFalse(matcher.matchesSafely(context, new StringDescription()));
    }

    @Test
    public void correctContent() throws Exception {
        context.put("log", new StringBuilder("content"));
        assertTrue(matcher.matchesSafely(context, new StringDescription()));
    }

}
