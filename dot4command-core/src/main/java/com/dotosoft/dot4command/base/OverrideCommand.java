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
 * <p>Override any context attribute stored under the <code>key</code> with <code>value</code>.</p>
 *
 * @param <K> the type of keys maintained by the context associated with this catalog
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this command
 *
 * @version $Id$
 */
public class OverrideCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

    // -------------------------------------------------------------- Properties

    private K key = null;
    private V value = null;

    /**
     * <p>Return the context attribute key for the attribute to override.</p>
     * @return The context attribute key.
     */
    public K getKey() {
        return key;
    }

    /**
     * <p>Set the context attribute key for the attribute to override.</p>
     *
     * @param key The new key
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * <p>Return the value that should override context attribute with key <code>key</code>.</p>
     * @return The value.
     */
    public V getValue() {
        return value;
    }

    /**
     * <p>Set the value that should override context attribute with key <code>key</code>.</p>
     *
     * @param value The new value
     */
    public void setValue(V value) {
        this.value = value;
    }

    // ---------------------------------------------------------- Filter Methods

    /**
     * <p>Override the attribute specified by <code>key</code> with <code>value</code>.</p>
     *
     * @param context {@link com.dotosoft.dot4command.chain.Context} in which we are operating
     *
     * @return {@link Processing#CONTINUE} so that {@link Processing} will continue.
     * @throws com.dotosoft.dot4command.chain.ChainException if and error occurs.
     */
    @Override
    public Processing onExecute(C context) throws Exception {
        if (context.containsKey(getKey())) {
            context.put(getKey(), getValue());
        }
        return Processing.CONTINUE;
    }

}
