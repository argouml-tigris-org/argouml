package org.omg.uml.foundation.core;

/**
 * Feature object instance interface.
 */
public interface Feature extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of attribute ownerScope.
     * @return Value of attribute ownerScope.
     */
    public org.omg.uml.foundation.datatypes.ScopeKind getOwnerScope();
    /**
     * Sets the value of ownerScope attribute. See {@link #getOwnerScope} for 
     * description on the attribute.
     * @param newValue New value to be set.
     */
    public void setOwnerScope(org.omg.uml.foundation.datatypes.ScopeKind newValue);
    /**
     * Returns the value of reference owner.
     * @return Value of reference owner.
     */
    public org.omg.uml.foundation.core.Classifier getOwner();
    /**
     * Sets the value of reference owner. See {@link #getOwner} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setOwner(org.omg.uml.foundation.core.Classifier newValue);
}
