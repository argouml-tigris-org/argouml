package org.omg.uml.foundation.extensionmechanisms;

/**
 * Stereotype object instance interface.
 */
public interface Stereotype extends org.omg.uml.foundation.core.GeneralizableElement {
    /**
     * Returns the value of attribute icon.
     * @return Value of attribute icon.
     */
    public java.lang.String getIcon();
    /**
     * Sets the value of icon attribute. See {@link #getIcon} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setIcon(java.lang.String newValue);
    /**
     * Returns the value of attribute baseClass.
     * @return Value of attribute baseClass.
     */
    public java.lang.String getBaseClass();
    /**
     * Sets the value of baseClass attribute. See {@link #getBaseClass} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setBaseClass(java.lang.String newValue);
    /**
     * Returns the value of reference requiredTag.
     * @return Value of reference requiredTag.
     */
    public java.util.Collection getRequiredTag();
    /**
     * Returns the value of reference extendedElement.
     * @return Value of reference extendedElement.
     */
    public java.util.Collection getExtendedElement();
    /**
     * Returns the value of reference stereotypeConstraint.
     * @return Value of reference stereotypeConstraint.
     */
    public java.util.Collection getStereotypeConstraint();
}
