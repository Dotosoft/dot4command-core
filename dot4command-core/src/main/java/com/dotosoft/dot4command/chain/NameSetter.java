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

package com.dotosoft.dot4command.chain;

import java.util.Map;

/**
 * Allows specifying a name for a {@link Command} in a {@link Catalog} instance.
 *
 * @param <K> Context key type
 * @param <V> Context value type
 * @param <C> Type of the context associated with this name setter
 * @since 2.0
 * @version $Id$
 */
public interface NameSetter<K extends String, V extends Object, C extends Map<K, V>> {

    /**
     * Specifies a name for a {@link Command} in a {@link Catalog} instance.
     *
     * @param name the name of the previous set {@link Command}
     * @return a new builder to add a new {@link Command}
     */
    NamedCommandSetter<K, V, C> identifiedBy(String name);

}