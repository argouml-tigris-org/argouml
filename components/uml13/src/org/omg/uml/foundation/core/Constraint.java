package org.omg.uml.foundation.core;

/**
 * Constraint object instance interface.
 */
public interface Constraint extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of attribute body.
     * @return Value of attribute body.
     */
    public org.omg.uml.foundation.datatypes.BooleanExpression getBody();
    /**
     * Sets the value of body attribute. See {@link #getBody} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setBody(org.omg.uml.foundation.datatypes.BooleanExpression newValue);
    /**
     * Returns the value of reference constrainedElement.
     * @return Value of reference constrainedElement.
     */
    public java.util.List getConstrainedElement();
}
