package org.omg.uml.foundation.core;

/**
 * ModelElement object instance interface.
 */
public interface ModelElement extends org.omg.uml.foundation.core.Element {
    /**
     * Returns the value of attribute name.
     * @return Value of attribute name.
     */
    public java.lang.String getName();
    /**
     * Sets the value of name attribute. See {@link #getName} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setName(java.lang.String newValue);
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
     * Returns the value of attribute isSpecification.
     * @return Value of attribute isSpecification.
     */
    public boolean isSpecification();
    /**
     * Sets the value of isSpecification attribute. See {@link #isSpecification} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setSpecification(boolean newValue);
    /**
     * Returns the value of reference namespace.
     * @return Value of reference namespace.
     */
    public org.omg.uml.foundation.core.Namespace getNamespace();
    /**
     * Sets the value of reference namespace. See {@link #getNamespace} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setNamespace(org.omg.uml.foundation.core.Namespace newValue);
    /**
     * Returns the value of reference clientDependency.
     * @return Value of reference clientDependency.
     */
    public java.util.Collection getClientDependency();
    /**
     * Returns the value of reference constraint.
     * @return Value of reference constraint.
     */
    public java.util.Collection getConstraint();
    /**
     * Returns the value of reference supplierDependency.
     * @return Value of reference supplierDependency.
     */
    public java.util.Collection getSupplierDependency();
    /**
     * Returns the value of reference presentation.
     * @return Value of reference presentation.
     */
    public java.util.Collection getPresentation();
    /**
     * Returns the value of reference targetFlow.
     * @return Value of reference targetFlow.
     */
    public java.util.Collection getTargetFlow();
    /**
     * Returns the value of reference sourceFlow.
     * @return Value of reference sourceFlow.
     */
    public java.util.Collection getSourceFlow();
    /**
     * Returns the value of reference templateParameter3.
     * @return Value of reference templateParameter3.
     */
    public java.util.Collection getTemplateParameter3();
    /**
     * Returns the value of reference binding.
     * @return Value of reference binding.
     */
    public org.omg.uml.foundation.core.Binding getBinding();
    /**
     * Sets the value of reference binding. See {@link #getBinding} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setBinding(org.omg.uml.foundation.core.Binding newValue);
    /**
     * Returns the value of reference comment.
     * @return Value of reference comment.
     */
    public java.util.Collection getComment();
    /**
     * Returns the value of reference elementResidence.
     * @return Value of reference elementResidence.
     */
    public java.util.Collection getElementResidence();
    /**
     * Returns the value of reference templateParameter.
     * @return Value of reference templateParameter.
     */
    public java.util.List getTemplateParameter();
    /**
     * Returns the value of reference templateParameter2.
     * @return Value of reference templateParameter2.
     */
    public java.util.Collection getTemplateParameter2();
}
