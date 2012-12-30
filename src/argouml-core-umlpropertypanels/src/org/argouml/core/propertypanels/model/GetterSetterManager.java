/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling - Post GSOC improvements
 *******************************************************************************
 */

package org.argouml.core.propertypanels.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.argouml.kernel.Command;

public abstract class GetterSetterManager {
    
    /**
     * The list of boolean property getter/setters
     */
    protected final Map<String, GetterSetter> getterSetterByPropertyName =
        new HashMap<String, GetterSetter>();
    
    /**
     * Set a UML property by property name
     * @param handle the element to which a property must be set
     * @param value the new property value
     * @param propertyName the property name
     */
    public abstract void set(Object umlElement, Object value, String propertyName);
    
    /**
     * Get a UML property by property name
     * @param umlElement the element from which a property must be return
     * @param propertyName the property name
     * @return the UML element or property
     */
    public abstract Object get(Object umlElement, String propertyName, Class<?> type);
    
    public abstract Object create(String propertyName, String language, String body);

    public abstract Collection getOptions(Object umlElement, String propertyName, Collection<Class<?>> types);
    
    public abstract Object getMetaType(String propertyName);
    
    public abstract boolean isValidElement(String propertyName, Collection<Class<?>> types, Object umlElement);
    
    public abstract Command getRemoveCommand(String propertyName, Object umlElement, Object objectToRemove);
    
    public abstract Command getAddCommand(String propertyName, Object umlElement);
    
    public abstract List<Command> getAdditionalCommands(String propertyName, Object umlElement);
    
    /** This forces component to fully rebuild when items are added and removed
     *  Used for pragmatic purposes but not advised long term we should remove this in time
     */
    public abstract boolean isFullBuildOnly(String propertyName);
    
    public boolean contains(String propertyName) {
        return getterSetterByPropertyName.containsKey(propertyName);
    }
    
    public static GetterSetterManager getGetterSetter(Class<?> type) {
        return new GetterSetterManagerImpl(type);
    }
    
    protected abstract class GetterSetter {
        
        abstract Object get(Object modelElement, Class<?> type);
        abstract void set(Object modelElement, Object value);
    }
    
    protected abstract class ExpressionGetterSetter extends GetterSetter {
        abstract Object create(String language, String body);
    }
    
    
    protected abstract class OptionGetterSetter extends GetterSetter {
        
        private Collection options;

        protected void setOptions(final Collection options) {
            this.options = options;
        }

        protected Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return options;
        }
    }
    
    
    protected abstract class ListGetterSetter extends OptionGetterSetter {
        abstract boolean isValidElement(Object modelElement, Collection<Class<?>> types);
        abstract Object getMetaType();
        boolean isFullBuildOnly() {
        	return false;
        }
        /**
         * Returns additional commands that cannot be deduced from the panel
         * xml or other means. This is currently only used by
         * SubvertexGetterSetter and should be removed as soon as we have some
         * configurable way to replace.
         * @param modelElement TODO
         */
        public List<Command> getAdditionalCommands(Object modelElement) {
        	return Collections.emptyList();
        }
    }
}
