package org.omg.uml.behavioralelements.activitygraphs;

/**
 * ObjectFlowState object instance interface.
 */
public interface ObjectFlowState extends org.omg.uml.behavioralelements.statemachines.SimpleState {
    /**
     * Returns the value of attribute isSynch.
     * @return Value of attribute isSynch.
     */
    public boolean isSynch();
    /**
     * Sets the value of isSynch attribute. See {@link #isSynch} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setSynch(boolean newValue);
    /**
     * Returns the value of reference parameter.
     * @return Value of reference parameter.
     */
    public java.util.Collection getParameter();
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
