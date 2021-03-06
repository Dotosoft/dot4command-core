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

import com.dotosoft.dot4command.chain.Catalog;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.testutils.AddingCommand;
import com.dotosoft.dot4command.testutils.DelegatingCommand;
import com.dotosoft.dot4command.testutils.HasCommandCount;

/**
 * @version $Id$
 */
public class HasCommandCountTestCase {

    private Catalog<String, Object, Context<String, Object>> catalog;
    private HasCommandCount matcher;

    @Before
    public void setUp() throws Exception {
        catalog = new TestCatalog<String, Object, Context<String, Object>>();
        matcher = new HasCommandCount(2);
    }

    @Test
    public void wrongCountFails() throws Exception {
        assertFalse(matcher.matchesSafely(catalog, new StringDescription()));
    }

    @Test
    public void correctCount() throws Exception {
        catalog.addCommand("addingCMD", new AddingCommand());
        catalog.addCommand("DelegatingCMD", new DelegatingCommand());

        assertTrue(matcher.matchesSafely(catalog, new StringDescription()));
    }

    @Test(expected = IllegalStateException.class)
    public void inconsistentCatalogThrowsException() throws Exception {
        catalog.addCommand("key for null", null);

        matcher.matchesSafely(catalog, new StringDescription());
    }
}
