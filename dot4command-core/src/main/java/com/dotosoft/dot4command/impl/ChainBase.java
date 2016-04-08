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

import com.dotosoft.dot4command.base.BaseObject;
import com.dotosoft.dot4command.chain.Chain;
import com.dotosoft.dot4command.chain.ChainException;
import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.chain.Filter;
import com.dotosoft.dot4command.chain.Processing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;

/**
 * <p>Convenience base class for {@link Chain} implementations.</p>
 *
 * @param <K> the type of keys maintained by the context associated with this chain
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this chain
 *
 * @version $Id$
 */
public class ChainBase<K extends String, V extends Object, C extends Map<K, V>> extends BaseObject implements Chain<K, V, C> {

    // ----------------------------------------------------------- Constructors

    /**
     * <p>Construct a {@link Chain} with no configured {@link Command}s.</p>
     */
    public ChainBase() {
    }

    /**
     * <p>Construct a {@link Chain} configured with the specified
     * {@link Command}.</p>
     *
     * @param command The {@link Command} to be configured
     *
     * @exception IllegalArgumentException if <code>command</code>
     *  is <code>null</code>
     */
    public ChainBase(Command<K, V, C> command) {
        addCommand(command);
    }

    /**
     * <p>Construct a {@link Chain} configured with the specified
     * {@link Command Commands}.</p>
     *
     * @param commands The {@link Command Commands} to be configured
     *
     * @exception IllegalArgumentException if <code>commands</code>,
     *  or one of the individual {@link Command} elements,
     *  is <code>null</code>
     */
    public ChainBase(Command<K, V, C>... commands) {
        if (commands == null) {
            throw new IllegalArgumentException();
        }
        for (Command<K, V, C> command : commands) {
            addCommand(command);
        }
    }

    /**
     * <p>Construct a {@link Chain} configured with the specified
     * {@link Command}s.</p>
     *
     * @param commands The {@link Command}s to be configured
     *
     * @exception IllegalArgumentException if <code>commands</code>,
     *  or one of the individual {@link Command} elements,
     *  is <code>null</code>
     */
    public ChainBase(Collection<Command<K, V, C>> commands) {
        if (commands == null) {
            throw new IllegalArgumentException();
        }
        for (Command<K, V, C> command : commands) {
            addCommand( command );
        }
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * <p>The list of {@link Command}s configured for this {@link Chain}, in
     * the order in which they may delegate processing to the remainder of
     * the {@link Chain}.</p>
     */
    private final List<Command<K, V, C>> commands = new ArrayList<Command<K, V, C>>();

    /**
     * <p>Flag indicating whether the configuration of our commands list
     * has been frozen by a call to the <code>execute()</code> method.</p>
     */
    private boolean frozen = false;

    // ---------------------------------------------------------- Chain Methods

    /**
     * See the {@link Chain} JavaDoc.
     *
     * @param <CMD> the {@link Command} type to be added in the {@link Chain}
     * @param command The {@link Command} to be added
     *
     * @exception IllegalArgumentException if <code>command</code>
     *  is <code>null</code>
     * @exception IllegalStateException if no further configuration is allowed
     */
    public <CMD extends Command<K, V, C>> void addCommand(CMD command) {
        if (command == null) {
            throw new IllegalArgumentException();
        }
        if (frozen) {
            throw new IllegalStateException();
        }
        commands.add( command );
    }
    
//    public void modifyAttributes(Map valuesMap) {
//    	super.modifyAttributes(valuesMap);
//    	
//		List<Command<K, V, C>> listOfCommands = getCommands();
//		for(Command comm : listOfCommands) {
//			comm.modifyAttributes(valuesMap);
//		}
//	}
    
    

    /**
     * See the {@link Chain} JavaDoc.
     *
     * @param context The {@link Context} to be processed by this
     *  {@link Chain}
     *
     *  in this {@link Chain} but not handled by a <code>postprocess()</code>
     *  method of a {@link Filter}
     * @throws IllegalArgumentException if <code>context</code>
     *  is <code>null</code>
     *
     * @return {@link Processing#FINISHED} if the processing of this contex
     *  has been completed. Returns {@link Processing#CONTINUE} if the processing
     *  of this context should be delegated to a subsequent command in an 
     *  enclosing chain.
     */
    public Processing execute(C context) {
        // Verify our parameters
        if (context == null) {
            throw new IllegalArgumentException("Can't execute a null context");
        }

        // Freeze the configuration of the command list
        frozen = true;

        // Execute the commands in this list until one returns something else 
        // than Processing.CONTINUE or throws an exception
        Processing saveResult = Processing.FINISHED;
        Exception saveException = null;
        int i = 0;
        int n = commands.size();
        Command<K, V, C> lastCommand = null;
        for (i = 0; i < n; i++) {
            try {
                lastCommand = commands.get(i);
                saveResult = lastCommand.execute(context);
                if(saveResult == null) {
                    String format = String.format("The command '%s' returned an invalid processing value: '%s'",
                            lastCommand.getClass().getName(), saveResult);
                    throw new ChainException(format);
                } else if (saveResult == Processing.BREAK || saveResult == Processing.CONTINUE) {
                    break;
                } else if (saveResult == Processing.TERMINATE) {
                    System.exit(0);
                }
            } catch (Exception e) {
                saveException = e;
                break;
            }
        }

        // Call postprocess methods on Filters in reverse order
        if (i >= n) { // Fell off the end of the chain
            i--;
        }
        boolean handled = false;
        boolean result;
        for (int j = i; j >= 0; j--) {
            if (commands.get(j) instanceof Filter) {
                try {
                    result =
                        ((Filter<K, V, C>) commands.get(j)).postprocess(context,
                                                           saveException);
                    if (result) {
                        handled = true;
                    }
                } catch (Exception e) {
                      // Silently ignore
                }
            }
        }

        // Return the exception or result state from the last execute()
        if (saveException != null && !handled) {
            // Wrap and rethrow exception
            throw wrapUnhandledExceptions(saveException, context,
                    lastCommand);
        }
        return saveResult;
    }

    /**
     * Wraps an unhandled exception in a {@link ChainException} class.
     * @param unhandled Unhandled exception as passed from failing command
     * @param context Context that was passed to failing command
     * @param failedCommand The command that failed
     * @return Original unhandled exception wrapped in a ChainException
     */
    protected ChainException wrapUnhandledExceptions(Throwable unhandled, C context, Command<K, V, C> failedCommand) {
        /* There should not be a reason to rewrap an exception that is already
         * wrapped in a ChainException because the first wrapping preserves
         * the {@link Context} necessary for debugging. Adding more wrapped
         * exceptions would just make the debugging process more difficult. */
        if (unhandled instanceof ChainException) {
            return (ChainException) unhandled;
        }

        String msg = failedCommand == null ?
                        "An error occurred when executing the chain" :
                        String.format("An error occurred when executing the command %s in the chain",
                               failedCommand.getClass().getName());

        return new ChainException(msg, unhandled, context, failedCommand);
    }

    /**
     * Returns true, if the configuration of our commands list
     * has been frozen by a call to the <code>execute()</code> method,
     * false otherwise.
     *
     * @return true, if the configuration of our commands list
     * has been frozen by a call to the <code>execute()</code> method,
     * false otherwise.
     * @since 2.0
     */
    public boolean isFrozen() {
        return frozen;
    }

    // -------------------------------------------------------- Package Methods

    /**
     * <p>Return an array of the configured {@link Command}s for this
     * {@link Chain}.  This method is package private, and is used only
     * for the unit tests.</p>
     */
    public List<Command<K, V, C>> getCommands() {
        return commands;
    }

}
