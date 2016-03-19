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

import com.dotosoft.dot4command.chain.ChainException;
import com.dotosoft.dot4command.chain.Command;

/**
 * Runtime Exception that wraps an underlying exception thrown during the
 * execution of a {@link com.dotosoft.dot4command.chain.Command} or {@link com.dotosoft.dot4command.chain.Chain}.
 *
 * @version $Id$
 */
public class DispatchException extends ChainException {

    /**
     *
     */
    private static final long serialVersionUID = 20120724L;

    public DispatchException(String message) {
        super(message);
    }

    public DispatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public <K extends String, V extends Object, C extends Map<K, V>> DispatchException(String message, Throwable cause,
    		C context, Command<K, V, C> failedCommand) {
        super(message, cause, context, failedCommand);
    }

}
