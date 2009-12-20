package org.argouml.core.propertypanels.ui;

import java.util.HashMap;
import java.util.Map;

abstract class GetterSetter {
    
    /**
     * The list of boolean property getter/setters
     */
    protected final Map<String, BooleanGetterSetter> getterSetterByPropertyName =
        new HashMap<String, BooleanGetterSetter>();
    
    /**
     * Set a UML property by property name
     * @param handle the element to which a property must be set
     * @param value the new property value
     * @param propertyName the property name
     */
    abstract void set(Object handle, Object value, String propertyName);
    
    /**
     * Get a UML property by property name
     * @param handle the element from which a property must be return
     * @param value the new property value
     * @param propertyName the property name
     */
    abstract Object get(Object handle, String propertyName);
    
    boolean contains(String propertyName) {
        return getterSetterByPropertyName.containsKey(propertyName);
    }
    
    static GetterSetter getGetterSetter() {
        return new GetterSetterImpl();
    }
    
    protected abstract class BooleanGetterSetter {
        
        abstract String getPropertyName();
        abstract Boolean get(Object modelElement);
        abstract void set(Object modelElement, Boolean value);
    }
    
}