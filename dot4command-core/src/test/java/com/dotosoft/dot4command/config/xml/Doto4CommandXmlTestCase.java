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

package com.dotosoft.dot4command.config.xml;

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.dotosoft.dot4command.chain.Catalog;
import com.dotosoft.dot4command.chain.CatalogFactory;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.impl.CatalogBase;
import com.dotosoft.dot4command.impl.CatalogFactoryBase;
import com.dotosoft.dot4command.impl.ContextBase;

/**
 * Parameterized test case for {@link XmlConfigParser}, that uses config locations as data points.
 *
 * <p><strong>Note:</strong> This test case assumes, that all config files will be parsed to the same catalog
 * and command instances.</p>
 *
 * @version $Id$
 */
@RunWith(Parameterized.class)
public class Doto4CommandXmlTestCase {

    private Catalog<String, Object, Context<String, Object>> catalog = null;
    private Context<String, Object> context = null;
    private XmlConfigParser parser = null;
    private final String configLocation;

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]
            {
                {"/com/dotosoft/dot4command/config/xml/test-config-template.xml"}
            }
        );
    }

    public Doto4CommandXmlTestCase(String configLocation) {
        this.configLocation = configLocation;
    }

    @Before
    public void setUp() throws Exception {
        init();
        load(configLocation);
    }

    private void init() {
        CatalogFactoryBase.clear();
        catalog = new CatalogBase<String, Object, Context<String, Object>>();
        context = new ContextBase();
        parser = new XmlConfigParser();
    }

    @After
    public void tearDown() {
        parser = null;
        context = null;
        catalog = null;
    }

    // Load the default test-config.xml file and examine the results
    @Test
    public void testDefault() throws Exception {
    	
    	assertNotNull(catalog.getCommand("testPrint").execute(context));

    }

    // Load the specified catalog from the specified resource path
    private void load(String path) throws Exception {
        URL url = getClass().getResource(path);

        if (url == null) {
            String msg = String.format("Can't find resource for path: %s", path);
            throw new IllegalArgumentException(msg);
        }

        CatalogFactory<String, Object, Context<String, Object>> catalogFactory = parser.parse(url);
        catalog = catalogFactory.getCatalog("foo");
    }

}
