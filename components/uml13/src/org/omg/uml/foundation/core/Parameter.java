package org.omg.uml.foundation.core;

/**
 * Parameter object instance interface.
 */
public interface Parameter extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of attribute defaultValue.
     * @return Value of attribute defaultValue.
     */
    public org.omg.uml.foundation.datatypes.Expression getDefaultValue();
    /**
     * Sets the value of defaultValue attribute. See {@link #getDefaultValue} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setDefaultValue(org.omg.uml.foundation.datatypes.Expression newValue);
    /**
     * Returns the value of attribute kind.
     * @return Value of attribute kind.
     */
    public org.omg.uml.foundation.datatypes.ParameterDirectionKind getKind();
    /**
     * Sets the value of kind attribute. See {@link #getKind} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setKind(org.omg.uml.foundation.datatypes.ParameterDirectionKind newValue);
    /**
     * Returns the value of reference behavioralFeature.
     * @return Value of reference behavioralFeature.
     */
    public org.omg.uml.foundation.core.BehavioralFeature getBehavioralFeature();
    /**
     * Sets the value of reference behavioralFeature. See {@link #getBehavioralFeature} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setBehavioralFeature(org.omg.uml.foundation.core.BehavioralFeature newValue);
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
