package org.omg.uml.foundation.core;

/**
 * ElementResidence object instance interface.
 */
public interface ElementResidence extends javax.jmi.reflect.RefObject {
    /**
     * Returns the value of attribute visibility.
     * @return Value of attribute visibility.
     */
    public org.omg.uml.foundation.datatypes.VisibilityKind getVisibility();
    /**
     * Sets the value of visibility attribute. See {@link #getVisibility} for 
     * description on the attribute.
     * @param newValue New value to be set.
     */
    public void setVisibility(org.omg.uml.foundation.datatypes.VisibilityKind newValue);
    /**
     * Returns the value of reference resident.
     * @return Value of reference resident.
     */
    public org.omg.uml.foundation.core.ModelElement getResident();
    /**
     * Sets the value of reference resident. See {@link #getResident} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setResident(org.omg.uml.foundation.core.ModelElement newValue);
    /**
     * Returns the value of reference implementationLocation.
     * @return Value of reference implementationLocation.
     */
    public org.omg.uml.foundation.core.Component getImplementationLocation();
    /**
     * Sets the value of reference implementationLocation. See {@link #getImplementationLocation} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setImplementationLocation(org.omg.uml.foundation.core.Component newValue);
}
