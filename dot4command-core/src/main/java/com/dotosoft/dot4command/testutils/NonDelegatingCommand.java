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


import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.chain.Processing;


/**
 * <p>Implementation of {@link Command} that simply logs its identifier
 * and returns.</p>
 *
 * @version $Id$
 */

public class NonDelegatingCommand extends CommandBase<String, Object, Context<String, Object>> {


    // ------------------------------------------------------------ Constructor


    public NonDelegatingCommand() {
        this("");
    }


    // Construct an instance that will log the specified identifier
    public NonDelegatingCommand(String id) {
        this.id = id;
    }


    // ----------------------------------------------------- Instance Variables


    // The identifier to log for this Command instance
    protected String id = null;

    String getId() {
        return (this.id);
    }

    public void setId(String id) {
    this.id = id;
    }


    // -------------------------------------------------------- Command Methods


    // Execution method for this Command
    public Processing onExecute(Context<String, Object> context) {

        if (context == null) {
            throw new IllegalArgumentException();
        }
        log(context, id);
        return Processing.FINISHED;

    }



    // ------------------------------------------------------ Protected Methods


    /**
     * <p>Log the specified <code>id</code> into a StringBuffer attribute
     * named "log" in the specified <code>context</code>, creating it if
     * necessary.</p>
     *
     * @param context The {@link Context} into which we log the identifiers
     * @param id The identifier to be logged
     */
    protected void log(Context<String, Object> context, String id) {
        StringBuilder sb = (StringBuilder) context.get("log");
        if (sb == null) {
            sb = new StringBuilder();
            context.put("log", sb);
        }
        if (sb.length() > 0) {
            sb.append('/');
        }
        sb.append(id);
    }


}
