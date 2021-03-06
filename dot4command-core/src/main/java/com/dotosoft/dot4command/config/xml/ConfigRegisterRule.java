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

package com.dotosoft.dot4command.config.xml;

import org.apache.commons.digester3.Rule;
import org.xml.sax.Attributes;

import com.dotosoft.dot4command.chain.Catalog;
import com.dotosoft.dot4command.chain.Chain;
import com.dotosoft.dot4command.chain.Command;

import java.util.Map;

/**
 * <p>Digester rule that will cause the top-most element on the Digester
 * stack (if it is a {@link com.dotosoft.dot4command.chain.Command} to be registered with the next-to-top
 * element on the Digester stack (if it is a {@link com.dotosoft.dot4command.chain.Catalog} or {@link com.dotosoft.dot4command.chain.Chain}).
 * To be registered with a {@link com.dotosoft.dot4command.chain.Catalog}, the top-most element must contain
 * a value for the specified attribute that contains the name under which
 * it should be registered.</p>
 *
 * @version $Id: ConfigRegisterRule.java 1363305 2012-07-19 11:42:53Z simonetripodi $
 */
class ConfigRegisterRule extends Rule {

    // ----------------------------------------------------------- Constructors

    /**
     * <p>Construct a new instance of this rule that looks for an attribute
     * with the specified name.</p>
     *
     * @param nameAttribute Name of the attribute containing the name under
     *  which this command should be registered
     */
    public ConfigRegisterRule(String nameAttribute) {
        super();
        this.nameAttribute = nameAttribute;
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * <p>The name of the attribute under which we can retrieve the name
     * this command should be registered with.</p>
     */
    private final String nameAttribute;

    // --------------------------------------------------------- Public Methods

    /**
     * <p>Register the top {@link com.dotosoft.dot4command.chain.Command} if appropriate.</p>
     *
     * @param namespace the namespace URI of the matching element, or an
     *   empty string if the parser is not namespace aware or the element has
     *   no namespace
     * @param name the local name if the parser is namespace aware, or just
     *   the element name otherwise
     * @param attributes The attribute list of this element
     */
    @Override
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        // Is the top object a Command?
        Object top = getDigester().peek(0);
        if ((top == null)
            || !(top instanceof Command)) {
            return;
        }

        /* All commands can consume a generic context. Here we depend on
         * the configuration being correct because the rule binding is
         * dynamic. */
        Command<String, Object, Map<String, Object>> command =
                (Command<String, Object, Map<String, Object>>) top;

        // Is the next object a Catalog or a Chain?
        Object next = getDigester().peek(1);
        if (next == null) {
            return;
        }

        // Register the top element appropriately
        if (next instanceof Catalog) {
            String nameValue = attributes.getValue(nameAttribute);
            if (nameValue != null) {
                /* We are dynamically building a catalog and assigning
                 * generics to the most base types possible. */
                Catalog<String, Object, Map<String, Object>> catalog =
                        (Catalog<String, Object, Map<String, Object>>) next;
                command.setParent(catalog);
                catalog.addCommand(nameValue, command);
            }
        } else if (next instanceof Chain) {
            /* Like above - the chain is being dynamically generated,
             * so we can add a generic context signature at compile-time. */
            Chain<String, Object, Map<String, Object>> chain = (Chain<String, Object, Map<String, Object>>) next;
            command.setParent(chain);
            chain.addCommand(command);
        }
    }

}
