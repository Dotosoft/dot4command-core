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

import com.dotosoft.dot4command.chain.Chain;
import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Context;

/**
 * <p>Implementation of {@link Command} that logs its identifier and
 * and attempts to add a new {@link Command} to the {@link Chain}.  This
 * should cause an IllegalStateException if the {@link Chain} implementation
 * subclasses <code>ChainBase</code>.</p>
 *
 * @version $Id$
 */
public class AddingCommand extends NonDelegatingCommand {


    // ------------------------------------------------------------ Constructor


    public AddingCommand() {
        this("", null);
    }

    // Construct an instance that will log the specified identifier
    public AddingCommand(String id, Chain<String, Object, Context<String, Object>> parent) {
        super(id);
        this.parent = parent;
    }


    // The parent Chain
    private Chain<String, Object, Context<String, Object>> parent = null;


    // -------------------------------------------------------- Command Methods


    // Execution method for this Command
    public boolean execute(Context<String, Object> context, Chain<String, Object, Context<String, Object>> chain) {

        super.execute(context);
        parent.addCommand(new NonDelegatingCommand("NEW")); // Should cause ISE
        return (true);

    }


}
