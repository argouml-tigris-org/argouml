package org.omg.uml.modelmanagement;

/**
 * ElementImport object instance interface.
 */
public interface ElementImport extends javax.jmi.reflect.RefObject {
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
     * Returns the value of attribute alias.
     * @return Value of attribute alias.
     */
    public java.lang.String getAlias();
    /**
     * Sets the value of alias attribute. See {@link #getAlias} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setAlias(java.lang.String newValue);
    /**
     * Returns the value of reference package.
     * @return Value of reference package.
     */
    public org.omg.uml.modelmanagement.UmlPackage getUmlPackage();
    /**
     * Sets the value of reference package. See {@link #getUmlPackage} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setUmlPackage(org.omg.uml.modelmanagement.UmlPackage newValue);
    /**
     * Returns the value of reference modelElement.
     * @return Value of reference modelElement.
     */
    public org.omg.uml.foundation.core.ModelElement getModelElement();
    /**
     * Sets the value of reference modelElement. See {@link #getModelElement} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setModelElement(org.omg.uml.foundation.core.ModelElement newValue);
}
