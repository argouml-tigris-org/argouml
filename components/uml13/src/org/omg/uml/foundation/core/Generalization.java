package org.omg.uml.foundation.core;

/**
 * Generalization object instance interface.
 */
public interface Generalization extends org.omg.uml.foundation.core.Relationship {
    /**
     * Returns the value of attribute discriminator.
     * @return Value of attribute discriminator.
     */
    public java.lang.String getDiscriminator();
    /**
     * Sets the value of discriminator attribute. See {@link #getDiscriminator} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setDiscriminator(java.lang.String newValue);
    /**
     * Returns the value of reference child.
     * @return Value of reference child.
     */
    public org.omg.uml.foundation.core.GeneralizableElement getChild();
    /**
     * Sets the value of reference child. See {@link #getChild} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setChild(org.omg.uml.foundation.core.GeneralizableElement newValue);
    /**
     * Returns the value of reference parent.
     * @return Value of reference parent.
     */
    public org.omg.uml.foundation.core.GeneralizableElement getParent();
    /**
     * Sets the value of reference parent. See {@link #getParent} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setParent(org.omg.uml.foundation.core.GeneralizableElement newValue);
    /**
     * Returns the value of reference powertype.
     * @return Value of reference powertype.
     */
    public org.omg.uml.foundation.core.Classifier getPowertype();
    /**
     * Sets the value of reference powertype. See {@link #getPowertype} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setPowertype(org.omg.uml.foundation.core.Classifier newValue);
}
