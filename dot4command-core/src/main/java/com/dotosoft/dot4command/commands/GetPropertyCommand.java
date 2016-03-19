package com.dotosoft.dot4command.commands;

import java.util.Map;

import com.dotosoft.dot4command.base.CommandBase;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.chain.Processing;
import com.dotosoft.dot4command.utils.BeanUtils;


public class GetPropertyCommand<K extends String, V extends Object, C extends Map<K, V>> extends CommandBase<K, V, C> {

    // -------------------------------------------------------------- Properties
    private K fromKey = null;

    /**
     * <p>Return the context attribute key for the source attribute.</p>
     * @return The source attribute key.
     */
    public K getFromKey() {
    	return (this.fromKey);
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
    	return (this.toKey);
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
     * @param context {@link Context} in which we are operating
     *
     * @return <code>false</code> so that processing will continue
     * @throws Exception in the if an error occurs during execution.
     */
    public Processing onExecute(C context) throws Exception {

        V value = BeanUtils.getProperty(context, fromKey);

        if (value != null) {
            context.put(toKey, value);
        } else {
            context.remove(toKey);
        }

        return Processing.CONTINUE;

    }


}
