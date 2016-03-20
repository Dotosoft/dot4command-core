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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.dotosoft.dot4command.chain.Catalog;
import com.dotosoft.dot4command.chain.Command;

/**
 * @version $Id$
 */
public class TestCatalog<K extends String, V extends Object, C extends Map<K, V>> implements Catalog<K, V, C> {

    Map<String, Command> commands = new HashMap<String, Command>();

    @Override
    public <CMD extends Command<K, V, C>> void addCommand(String name, CMD command) {
        commands.put(name, command);
    }

    @Override
    public <CMD extends Command<K, V, C>> CMD getCommand(String name) {
        return (CMD) commands.get(name);
    }

    @Override
    public Iterator<String> getNames() {
        return commands.keySet().iterator();
    }

}
