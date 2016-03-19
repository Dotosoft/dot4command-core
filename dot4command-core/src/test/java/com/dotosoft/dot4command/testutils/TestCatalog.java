package com.dotosoft.dot4command.testutils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.dotosoft.dot4command.chain.Catalog;
import com.dotosoft.dot4command.chain.Command;
import com.dotosoft.dot4command.chain.Context;

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
