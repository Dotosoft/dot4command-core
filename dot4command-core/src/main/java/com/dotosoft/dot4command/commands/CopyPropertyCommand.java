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

package com.dotosoft.dot4command.commands;

import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.ExpressionTools;

public class CopyPropertyCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {
	
    // -------------------------------------------------------------- Properties
    
	private String fromKey = null;
    private String toKey = null;

    // ---------------------------------------------------------- Filter Methods
    /**
     * <p>Copy a specified literal value, or a context attribute stored under
     * the <code>fromKey</code> (if any), to the <code>toKey</code>.</p>
     *
     * @param context {@link Context} in which we are operating
     *
     * @return <code>false</code> so that processing will continue
     * @throws Exception in the if an error occurs during execution.
     */
    public Processing onExecute(C context) throws Exception {

    	Object value = ExpressionTools.extractValue(context, fromKey);
        if (value != null) {
            context.put((K) toKey, (V) value);
        } else {
            context.remove(toKey);
        }

        return Processing.FINISHED;

    }

    /**
     * <p>Return the context attribute key for the source attribute.</p>
     * @return The source attribute key.
     */
    public String getFromKey() {
    	return (this.fromKey);
    }


    /**
     * <p>Set the context attribute key for the source attribute.</p>
     *
     * @param fromKey The new key
     */
    public void setFromKey(String fromKey) {
    	this.fromKey = fromKey;
    }
    
    /**
     * <p>Return the context attribute key for the destination attribute.</p>
     * @return The destination attribute key.
     */
    public String getToKey() {
    	return (this.toKey);
    }

    /**
     * <p>Set the context attribute key for the destination attribute.</p>
     *
     * @param toKey The new key
     */
    public void setToKey(String toKey) {
    	this.toKey = toKey;
    }
}
