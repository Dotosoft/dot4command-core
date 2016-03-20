package com.dotosoft.dot4command.testutils;

import java.util.HashMap;

import com.dotosoft.dot4command.chain.Context;

/**
 * Since we can not import classes from modules that depend on test utils (eg chain2-base)
 * we have to implement a test context here.
 *
 * @version $Id$
 */
public class TestContext<K, V> extends HashMap<K,V> implements Context<K, V> {
    @Override
    public <T extends V> T retrieve(K key) {
        return (T) get(key);
    }
}
