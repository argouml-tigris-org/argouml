package org.argouml.swingext;

/**
 * A property that can be displayed and edited within a PropertyTable.
 * 
 * @author Jeremy Jones
**/
public class Property implements Comparable {
    private String      _name;
    private Class       _valueType;
    private Object      _initialValue;
    private Object      _currentValue;
    private Object[]    _availableValues;

    /**
     * Constructs a new Property. This version of the constructor does
     * not specify a finite set of available values.
     * 
     * @param name          the property name
     * @param valueType     the value type class
     * @param initialValue  the initial value
    **/
    public Property(String name, Class valueType, Object initialValue) {
        this(name, valueType, initialValue, null);
    }

    /**
     * Constructs a new Property. This version of the constructor does
     * not specify a finite set of available values.
     * 
     * @param name          the property name
     * @param valueType     the value type class
     * @param initialValue  the initial value
     * @param values        the set of available values to choose from
    **/
    public Property(
            String name,
            Class valueType,
            Object initialValue,
            Object[] values) {
        _name = name;
        _valueType = valueType;
        _initialValue = initialValue;
        _availableValues = values;
        _currentValue = _initialValue;
    }

    /**
     * Returns the property name.
     * 
     * @return property name
    **/
    public String getName() {
        return _name;
    }

    /**
     * Property editors should be configured to edit objects of this type.
     * 
     * @return  the property value class
    **/
    public Class getValueType() {
        return _valueType;
    }

    /**
     * Returns the initial property value.
     * 
     * @return  initial property value
    **/
    public Object getInitialValue() {
        return _initialValue;
    }

    /**
     * Returns the set of available property values, or null if no such
     * finite set exists.
     * 
     * @return set of available property values
    **/
    public Object[] getAvailableValues() {
        return _availableValues;
    }

    /**
     * Returns the currently selected property value.
     * 
     * @return current property value
    **/
    public Object getCurrentValue() {
        return _currentValue;
    }

    /**
     * Sets the currently selected property value.
     * 
     * @param value new property value
    **/
    public void setCurrentValue(Object value) {
        _currentValue = value;
    }
    
    /**
     * Compares two Properties by comparing their names.
    **/
    public int compareTo(Object o) {
        return _name.compareTo(((Property) o)._name);    
    }
}
