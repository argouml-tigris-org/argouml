package org.omg.uml.behavioralelements.usecases;

/**
 * Include object instance interface.
 */
public interface Include extends org.omg.uml.foundation.core.Relationship {
    /**
     * Returns the value of reference addition.
     * @return Value of reference addition.
     */
    public org.omg.uml.behavioralelements.usecases.UseCase getAddition();
    /**
     * Sets the value of reference addition. See {@link #getAddition} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setAddition(org.omg.uml.behavioralelements.usecases.UseCase newValue);
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
}
