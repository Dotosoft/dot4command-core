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


import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.chain.Processing;


/**
 * <p>Implementation of {@link Command} that logs its identifier and
 * and delegates to the rest of the chain.</p>
 *
 * @version $Id$
 */

public class DelegatingCommand extends NonDelegatingCommand {


    // ------------------------------------------------------------ Constructor


    public DelegatingCommand() {
    this("");
    }


    // Construct an instance that will log the specified identifier
    public DelegatingCommand(String id) {
        super(id);
    }


    // -------------------------------------------------------- Command Methods


    // Execution method for this Command
    @Override
    public Processing onExecute(Context<String, Object> context) {

        super.onExecute(context);
        return Processing.CONTINUE;

    }


}
