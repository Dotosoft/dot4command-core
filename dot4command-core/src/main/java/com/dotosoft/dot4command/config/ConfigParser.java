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

package com.dotosoft.dot4command.config;

import java.net.URL;
import java.util.Map;

import com.dotosoft.dot4command.chain.Catalog;
import com.dotosoft.dot4command.chain.CatalogFactory;

/**
 * <p>Facade class to abstract the functionality of parsing an arbitrary
 * configuration file that defines and configures commands and command chains
 * to be registered in a {@link Catalog}. It is legal to call the
 * <code>parse()</code> method more than once, in order
 * to parse more than one configuration document.</p>
 *
 * @version $Id$
 */
public interface ConfigParser {

    // --------------------------------------------------------- Public Methods

    /**
     * <p>Parse the configuration at the specified URL using the configured
     * rule set, registering catalogs with nested chains and
     * commands as they are encountered.  Use this method <strong>only</strong>
     * if you have included one or more <code>factory</code> elements in your
     * configuration resource.</p>
     *
     * @param <K> the type of keys maintained by the context associated with this command
     * @param <V> the type of mapped values
     * @param <C> Type of the context associated with this command
     * @param url <code>URL</code> of the configuration document to be parsed
     * @return a CatalogFactory instance parsed from the given location
     * @exception ChainConfigurationException if a parsing error occurs
     */
	<K extends String, V extends Object, C extends Map<K, V>> CatalogFactory<K, V, C> parse(URL url) throws ChainConfigurationException;

}
