package org.omg.uml.behavioralelements.usecases;

/**
 * ExtensionPoint object instance interface.
 */
public interface ExtensionPoint extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of attribute location.
     * @return Value of attribute location.
     */
    public java.lang.String getLocation();
    /**
     * Sets the value of location attribute. See {@link #getLocation} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setLocation(java.lang.String newValue);
    /**
     * Returns the value of reference useCase.
     * @return Value of reference useCase.
     */
    public org.omg.uml.behavioralelements.usecases.UseCase getUseCase();
    /**
     * Sets the value of reference useCase. See {@link #getUseCase} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setUseCase(org.omg.uml.behavioralelements.usecases.UseCase newValue);
    /**
     * Returns the value of reference extend.
     * @return Value of reference extend.
     */
    public java.util.Collection getExtend();
}
