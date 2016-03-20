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

import java.util.Map;

import com.dotosoft.dot4command.chain.Processing;

/**
 * <p>Copy a specified literal value, or a context attribute stored under
 * the <code>fromKey</code> (if any), to the <code>toKey</code>.</p>
 *
 * @param <K> the type of keys maintained by the context associated with this command
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this command
 *
 * @version $Id$
 */
public class CopyCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

    // -------------------------------------------------------------- Properties

    private K fromKey = null;

    /**
     * <p>Return the context attribute key for the source attribute.</p>
     * @return The source attribute key.
     */
    public K getFromKey() {
        return this.fromKey;
    }

    /**
     * <p>Set the context attribute key for the source attribute.</p>
     *
     * @param fromKey The new key
     */
    public void setFromKey(K fromKey) {
        this.fromKey = fromKey;
    }

    private K toKey = null;

    /**
     * <p>Return the context attribute key for the destination attribute.</p>
     * @return The destination attribute key.
     */
    public K getToKey() {
        return this.toKey;
    }

    /**
     * <p>Set the context attribute key for the destination attribute.</p>
     *
     * @param toKey The new key
     */
    public void setToKey(K toKey) {
        this.toKey = toKey;
    }

    // ---------------------------------------------------------- Filter Methods

    /**
     * <p>Copy a specified literal value, or a context attribute stored under
     * the <code>fromKey</code> (if any), to the <code>toKey</code>.</p>
     *
     * @param context {@link com.dotosoft.dot4command.chain.Context Context} in which we are operating.
     *
     * @return {@link Processing#CONTINUE} so that processing will continue.
     * @throws com.dotosoft.dot4command.chain.ChainException in the if an error occurs during execution.
     */
    @Override
    public Processing onExecute(C context) throws Exception {
        if (containsKeys(context)) {
            V value = context.get(getFromKey());
            context.put(getToKey(), value);
        }
        return Processing.CONTINUE;
    }

    private boolean containsKeys(C context) {
        return context.containsKey(getFromKey()) && context.containsKey(getToKey());
    }

}