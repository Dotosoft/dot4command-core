/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dotosoft.dot4command.base;

import java.util.Map;

import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.chain.Processing;

/**
 * <p>Remove any context attribute stored under the <code>fromKey</code>.</p>
 *
 * @param <K> the type of keys maintained by the context associated with this catalog
 * @param <V> the type of mapped values
 * @param <C> Type of the context associated with this command
 *
 * @version $Id$
 */
public class RemoveCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

    // -------------------------------------------------------------- Properties

    private K fromKey = null;

    /**
     * <p>Return the context attribute key for the attribute.</p>
     * @return The context attribute key.
     */
    public K getFromKey() {
        return this.fromKey;
    }

    /**
     * <p>Set the context attribute key for the attribute.</p>
     *
     * @param fromKey The new key
     */
    public void setFromKey(K fromKey) {
        this.fromKey = fromKey;
    }

    // ---------------------------------------------------------- Filter Methods

    /**
     * <p>Copy the specified source attribute to the specified destination
     * attribute.</p>
     *
     * @param context {@link Context} in which we are operating
     *
     * @return {@link Processing#CONTINUE} so that processing will continue.
     * @throws com.dotosoft.dot4command.chain.ChainException if and error occurs.
     */
    @Override
    public Processing onExecute(C context) throws Exception {
        context.remove(getFromKey());
        return Processing.CONTINUE;
    }

}
