package org.omg.uml.behavioralelements.usecases;

/**
 * Extend object instance interface.
 */
public interface Extend extends org.omg.uml.foundation.core.Relationship {
    /**
     * Returns the value of attribute condition.
     * @return Value of attribute condition.
     */
    public org.omg.uml.foundation.datatypes.BooleanExpression getCondition();
    /**
     * Sets the value of condition attribute. See {@link #getCondition} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setCondition(org.omg.uml.foundation.datatypes.BooleanExpression newValue);
    /**
     * Returns the value of reference base.
     * @return Value of reference base.
     */
    public org.omg.uml.behavioralelements.usecases.UseCase getBase();
    /**
     * Sets the value of reference base. See {@link #getBase} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setBase(org.omg.uml.behavioralelements.usecases.UseCase newValue);
    /**
     * Returns the value of reference extension.
     * @return Value of reference extension.
     */
    public org.omg.uml.behavioralelements.usecases.UseCase getExtension();
    /**
     * Sets the value of reference extension. See {@link #getExtension} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setExtension(org.omg.uml.behavioralelements.usecases.UseCase newValue);
    /**
     * Returns the value of reference extensionPoint.
     * @return Value of reference extensionPoint.
     */
    public java.util.List getExtensionPoint();
}
