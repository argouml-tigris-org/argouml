package org.omg.uml.foundation.core;

/**
 * StructuralFeature object instance interface.
 */
public interface StructuralFeature extends org.omg.uml.foundation.core.Feature {
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
     * Returns the value of attribute changeability.
     * @return Value of attribute changeability.
     */
    public org.omg.uml.foundation.datatypes.ChangeableKind getChangeability();
    /**
     * Sets the value of changeability attribute. See {@link #getChangeability} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setChangeability(org.omg.uml.foundation.datatypes.ChangeableKind newValue);
    /**
     * Returns the value of attribute targetScope.
     * @return Value of attribute targetScope.
     */
    public org.omg.uml.foundation.datatypes.ScopeKind getTargetScope();
    /**
     * Sets the value of targetScope attribute. See {@link #getTargetScope} for 
     * description on the attribute.
     * @param newValue New value to be set.
     */
    public void setTargetScope(org.omg.uml.foundation.datatypes.ScopeKind newValue);
    /**
     * Returns the value of reference type.
     * @return Value of reference type.
     */
    public org.omg.uml.foundation.core.Classifier getType();
    /**
     * Sets the value of reference type. See {@link #getType} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setType(org.omg.uml.foundation.core.Classifier newValue);
}
