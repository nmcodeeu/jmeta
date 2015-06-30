/*
 *	JMeta - Meta's java implementation
 *	Copyright (C) 2013 JMeta
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the
 *	License, or (at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Affero General Public License for more details.
 *	
 *	You should have received a copy of the GNU Affero General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.meta.configuration;

import java.util.Properties;

/**
 *
 * @author nico
 */
public abstract class BaseConfiguration {

    /**
     * The properties class related to the configuration file.
     */
    protected Properties properties;

    /**
     * Initializes the configuration from the configuration file. If some
     * entries are not present, uses default values instead.
     *
     * @param properties The properties instance related to the configuration
     * file.
     */
    public BaseConfiguration(Properties properties) {
        this.properties = properties;
    }

    /**
     * Empty initialization.
     */
    public BaseConfiguration() {
    }

    /**
     * Initializes this configuration content with the given properties.
     */
    abstract void initFromProperties();

    /**
     * @param propKey The key in the property file to fetch.
     *
     * @return the value associated wit the given property key or null
     */
    public String getValue(String propKey) {
        if (this.properties == null || !this.properties.containsKey(propKey)) {
            return null;
        }
        return this.properties.getProperty(propKey);
    }

    /**
     *
     * @param propKey The key in the property file.
     *
     * @return the short value or null if not found.
     */
    public Short getShort(String propKey) {
        String val = this.getValue(propKey);

        if (val == null) {
            return null;
        }
        try {
            return Short.valueOf(val);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     *
     * @param propKey The key in the property file.
     *
     * @return the boolean value or null if not found.
     */
    public Boolean getBoolean(String propKey) {
        String val = this.getValue(propKey);

        if (val == null) {
            return null;
        }
        return Boolean.valueOf(val);
    }
}