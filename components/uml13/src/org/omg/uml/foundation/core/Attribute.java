package org.omg.uml.foundation.core;

/**
 * Attribute object instance interface.
 */
public interface Attribute extends org.omg.uml.foundation.core.StructuralFeature {
    /**
     * Returns the value of attribute initialValue.
     * @return Value of attribute initialValue.
     */
    public org.omg.uml.foundation.datatypes.Expression getInitialValue();
    /**
     * Sets the value of initialValue attribute. See {@link #getInitialValue} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setInitialValue(org.omg.uml.foundation.datatypes.Expression newValue);
    /**
     * Returns the value of reference associationEnd.
     * @return Value of reference associationEnd.
     */
    public org.omg.uml.foundation.core.AssociationEnd getAssociationEnd();
    /**
     * Sets the value of reference associationEnd. See {@link #getAssociationEnd} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setAssociationEnd(org.omg.uml.foundation.core.AssociationEnd newValue);
}
