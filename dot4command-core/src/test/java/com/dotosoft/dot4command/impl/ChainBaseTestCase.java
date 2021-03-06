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

package com.dotosoft.dot4command.impl;

import static com.dotosoft.dot4command.testutils.HasLog.hasLog;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dotosoft.dot4command.chain.Chain;
import com.dotosoft.dot4command.chain.ChainException;
import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.impl.ChainBase;
import com.dotosoft.dot4command.impl.ContextBase;
import com.dotosoft.dot4command.testutils.AddingCommand;
import com.dotosoft.dot4command.testutils.DelegatingCommand;
import com.dotosoft.dot4command.testutils.DelegatingFilter;
import com.dotosoft.dot4command.testutils.ExceptionCommand;
import com.dotosoft.dot4command.testutils.ExceptionFilter;
import com.dotosoft.dot4command.testutils.NonDelegatingCommand;
import com.dotosoft.dot4command.testutils.NonDelegatingFilter;
import com.dotosoft.dot4command.testutils.NullReturningCommand;


/**
 * <p>Test case for the <code>ChainBase</code> class.</p>
 *
 * @version $Id$
 */

public class ChainBaseTestCase {


    // ---------------------------------------------------- Instance Variables


    /**
     * The {@link Chain} instance under test.
     */
    protected Chain<String, Object, Context<String, Object>> chain = null;


    /**
     * The {@link Context} instance on which to execute the chain.
     */
    protected Context<String, Object> context = null;


    // -------------------------------------------------- Overall Test Methods


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() {
        chain = new ChainBase<String, Object, Context<String, Object>>();
        context = new ContextBase();
    }

    /**
     * Tear down instance variables required by this test case.
     */
    @After
    public void tearDown() {
        chain = null;
        context = null;
    }


    // ------------------------------------------------ Individual Test Methods


    @Test (expected = ChainException.class)
    public void nullReturningCommandForcesException() {
        chain.addCommand(new DelegatingCommand("BeforeNullReturningCommand"));
        chain.addCommand(new NullReturningCommand());
        chain.addCommand(new NonDelegatingCommand("AfterNullReturningCommand"));
        
        chain.execute(context);
    }
    
    // Test the ability to add commands
    @Test
    public void testCommands() {

        checkCommandCount(0);

        Command<String, Object, Context<String, Object>> command1 = new NonDelegatingCommand("1");
        chain.addCommand(command1);
        checkCommandCount(1);

        Command<String, Object, Context<String, Object>> command2 = new DelegatingCommand("2");
        chain.addCommand(command2);
        checkCommandCount(2);

        Command<String, Object, Context<String, Object>> command3 = new ExceptionCommand("3");
        chain.addCommand(command3);
        checkCommandCount(3);

    }


    // Test execution of a single non-delegating command
    @Test
    public void testExecute1a() {
        chain.addCommand(new NonDelegatingCommand("1"));
        try {
            assertEquals(Processing.FINISHED, chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1"));
    }


    // Test execution of a single delegating command
    @Test
    public void testExecute1b() {
        chain.addCommand(new DelegatingCommand("1"));
        try {
            assertEquals(Processing.FINISHED, chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1"));
    }


    // Test execution of a single exception-throwing command
    @Test
    public void testExecute1c() {
        chain.addCommand(new ExceptionCommand("1"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "1", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1"));
    }


    // Test execution of an attempt to add a new Command while executing
    @Test
    public void testExecute1d() {
        chain.addCommand(new AddingCommand("1", chain));
        try {
            chain.execute(context);
        } catch (IllegalStateException e) {
            // Expected result
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1"));
    }


    // Test execution of a chain that should return true
    @Test
    public void testExecute2a() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new NonDelegatingCommand("3"));
        try {
            assertEquals(Processing.FINISHED, chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/2/3"));
    }


    // Test execution of a chain that should return false
    @Test
    public void testExecute2b() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new DelegatingCommand("3"));
        try {
            assertEquals(Processing.FINISHED, chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/2/3"));
    }


    // Test execution of a chain that should throw an exception
    @Test
    public void testExecute2c() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new ExceptionCommand("3"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "3", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/2/3"));
    }


    // Test execution of a chain that should throw an exception in the middle
    @Test
    public void testExecute2d() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new ExceptionCommand("2"));
        chain.addCommand(new NonDelegatingCommand("3"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "2", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/2"));
    }


    // Test execution of a single non-delegating filter
    @Test
    public void testExecute3a() {
        chain.addCommand(new NonDelegatingFilter("1", "a"));
        try {
            assertEquals(Processing.FINISHED, chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/a"));
    }


    // Test execution of a single delegating filter
    @Test
    public void testExecute3b() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        try {
            assertEquals(Processing.FINISHED, chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/a"));
    }


    // Test execution of a single exception-throwing filter
    @Test
    public void testExecute3c() {
        chain.addCommand(new ExceptionFilter("1", "a"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "1", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/a"));
    }


    // Test execution of a chain that should return true
    @Test
    public void testExecute4a() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        chain.addCommand(new DelegatingCommand("2"));
        chain.addCommand(new NonDelegatingFilter("3", "c"));
        try {
            assertEquals(Processing.FINISHED, chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/2/3/c/a"));
    }


    // Test execution of a chain that should return false
    @Test
    public void testExecute4b() {
        chain.addCommand(new DelegatingCommand("1"));
        chain.addCommand(new DelegatingFilter("2", "b"));
        chain.addCommand(new DelegatingCommand("3"));
        try {
            assertEquals(Processing.FINISHED, chain.execute(context));
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/2/3/b"));
    }


    // Test execution of a chain that should throw an exception
    @Test
    public void testExecute4c() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        chain.addCommand(new DelegatingFilter("2", "b"));
        chain.addCommand(new ExceptionFilter("3", "c"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "3", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/2/3/c/b/a"));
    }


    // Test execution of a chain that should throw an exception in the middle
    @Test
    public void testExecute4d() {
        chain.addCommand(new DelegatingFilter("1", "a"));
        chain.addCommand(new ExceptionFilter("2", "b"));
        chain.addCommand(new NonDelegatingFilter("3", "c"));
        try {
            chain.execute(context);
        } catch (ArithmeticException e) {
            assertEquals("Correct exception id", "2", e.getMessage());
        } catch (Exception e) {
            fail("Threw exception: " + e);
        }
        assertThat(context, hasLog("1/2/b/a"));
    }


    // Test state of newly created instance
    @Test
    public void testNewInstance() {
        checkCommandCount(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorCommandNull() throws Exception {
        new ChainBase<String, Object, Context<String, Object>>((Command<String, Object, Context<String, Object>>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorVarArgsNull() throws Exception {
        new ChainBase<String, Object, Context<String, Object>>((Command<String, Object, Context<String, Object>>[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorVarArgsWithNullElements() throws Exception {
        new ChainBase<String, Object, Context<String, Object>>(
                new DelegatingFilter("1", "a"),
                null,
                new ExceptionFilter("2", "b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorCollectionNull() throws Exception {
        new ChainBase<String, Object, Context<String, Object>>((Collection<Command<String, Object, Context<String, Object>>>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorCollectionWithNullElements() throws Exception {
        List<Command<String, Object, Context<String, Object>>> commands = new ArrayList<Command<String, Object, Context<String, Object>>>();
        commands.add(new DelegatingFilter("1", "a"));
        commands.add(null);
        commands.add(new ExceptionFilter("2", "b"));
        new ChainBase<String, Object, Context<String, Object>>(commands);
    }


    // -------------------------------------------------------- Support Methods


    // Verify the number of configured commands
    protected void checkCommandCount(int expected) {
        if (chain instanceof ChainBase) {
            List<Command<String, Object, Context<String, Object>>> commands =
                ((ChainBase<String, Object, Context<String, Object>>) chain).getCommands();
            assertNotNull("getCommands() returned null", commands);
            assertEquals("Command count doesn't match", expected, commands.size());
        }
    }

}
