package org.omg.uml.behavioralelements.collaborations;

/**
 * AssociationRole object instance interface.
 */
public interface AssociationRole extends org.omg.uml.foundation.core.UmlAssociation {
    /**
     * Returns the value of attribute multiplicity.
     * @return Value of attribute multiplicity.
     */
    public org.omg.uml.foundation.datatypes.Multiplicity getMultiplicity();
    /**
     * Sets the value of multiplicity attribute. See {@link #getMultiplicity} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setMultiplicity(org.omg.uml.foundation.datatypes.Multiplicity newValue);
    /**
     * Returns the value of reference base.
     * @return Value of reference base.
     */
    public org.omg.uml.foundation.core.UmlAssociation getBase();
    /**
     * Sets the value of reference base. See {@link #getBase} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setBase(org.omg.uml.foundation.core.UmlAssociation newValue);
    /**
     * Returns the value of reference message.
     * @return Value of reference message.
     */
    public java.util.Collection getMessage();
}
