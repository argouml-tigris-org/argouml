package org.omg.uml.foundation.core;

/**
 * Abstraction object instance interface.
 */
public interface Abstraction extends org.omg.uml.foundation.core.Dependency {
    /**
     * Returns the value of attribute mapping.
     * @return Value of attribute mapping.
     */
    public org.omg.uml.foundation.datatypes.MappingExpression getMapping();
    /**
     * Sets the value of mapping attribute. See {@link #getMapping} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setMapping(org.omg.uml.foundation.datatypes.MappingExpression newValue);
}
