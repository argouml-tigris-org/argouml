// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

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
