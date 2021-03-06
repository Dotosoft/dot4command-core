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
import com.dotosoft.dot4command.chain.CatalogFactory;
import com.dotosoft.dot4command.impl.CatalogFactoryBase;

import java.util.Map;

/**
 * <p>Digester rule that will cause the top-most element on the Digester
 * stack (if it is a {@link com.dotosoft.dot4command.chain.Catalog} to be registered with the
 * {@link com.dotosoft.dot4command.impl.CatalogFactoryBase} instance for our application.  If the attribute
 * specified to our constructor has a value, that will be used as the name
 * under which to register this {@link com.dotosoft.dot4command.chain.Catalog}.  Otherwise, this will
 * become the default {@link com.dotosoft.dot4command.chain.Catalog} for this application.</p>
 *
 * @version $Id: ConfigCatalogRule.java 1363305 2012-07-19 11:42:53Z simonetripodi $
 */
class ConfigCatalogRule extends Rule {

    // ----------------------------------------------------------- Constructors

    /**
     * <p>Construct a new instance of this rule that looks for an attribute
     * with the specified name.</p>
     *
     * @param nameAttribute Name of the attribute containing the name under
     *  which this command should be registered
     * @param catalogClass Name of the implementation class for newly
     *  created {@link com.dotosoft.dot4command.chain.Catalog} instances
     */
    public ConfigCatalogRule(String nameAttribute, String catalogClass) {
        super();
        this.nameAttribute = nameAttribute;
        this.catalogClass = catalogClass;
    }

    // ----------------------------------------------------- Instance Variables

    /**
     * <p>The fully qualified class name of a {@link com.dotosoft.dot4command.chain.Catalog} class to use for
     * instantiating new instances.</p>
     */
    private final String catalogClass;

    /**
     * <p>The name of the attribute under which we can retrieve the name
     * this catalog should be registered with (if any).</p>
     */
    private final String nameAttribute;

    // --------------------------------------------------------- Public Methods

    /**
     * <p>Retrieve or create a {@link com.dotosoft.dot4command.chain.Catalog} with the name specified by
     * the <code>nameAttribute</code> attribute, or the default {@link com.dotosoft.dot4command.chain.Catalog}
     * if there is no such attribute defined.  Push it onto the top of the
     * stack.</p>
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
        // Retrieve any current Catalog with the specified name
        Catalog<String, Object, Map<String, Object>> catalog;
        CatalogFactory<String, Object, Map<String, Object>> factory = CatalogFactoryBase.getInstance();
        String nameValue = attributes.getValue(nameAttribute);
        if (nameValue == null) {
            catalog = factory.getCatalog();
        } else {
            catalog = factory.getCatalog(nameValue);
        }

        // Create and register a new Catalog instance if necessary
        if (catalog == null) {
            Class<?> clazz = getDigester().getClassLoader().loadClass(catalogClass);

            /* Convert catalog pulled from digester to default generic signature
             * with the assumption that the Catalog returned from digester will
             * comply with the the historic chain contract. */
            @SuppressWarnings("unchecked")
            Catalog<String, Object, Map<String, Object>> digesterCatalog =
                    (Catalog<String, Object, Map<String, Object>>) clazz.newInstance();

            catalog = digesterCatalog;

            if (nameValue == null) {

                factory.setCatalog(catalog);
            } else {
                factory.addCatalog(nameValue, catalog);
            }
        }

        // Push this Catalog onto the top of the stack
        getDigester().push(catalog);
    }

}
