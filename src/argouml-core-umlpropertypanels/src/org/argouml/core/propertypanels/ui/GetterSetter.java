package org.argouml.core.propertypanels.ui;

abstract class GetterSetter {
    
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
    
    static GetterSetter getGetterSetter() {
        return new GetterSetterImpl();
    }
}