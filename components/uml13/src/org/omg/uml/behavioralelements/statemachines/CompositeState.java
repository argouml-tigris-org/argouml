package org.omg.uml.behavioralelements.statemachines;

/**
 * CompositeState object instance interface.
 */
public interface CompositeState extends org.omg.uml.behavioralelements.statemachines.State {
    /**
     * Returns the value of attribute isConcurrent.
     * @return Value of attribute isConcurrent.
     */
    public boolean isConcurrent();
    /**
     * Sets the value of isConcurrent attribute. See {@link #isConcurrent} for 
     * description on the attribute.
     * @param newValue New value to be set.
     */
    public void setConcurrent(boolean newValue);
    /**
     * Returns the value of reference subvertex.
     * @return Value of reference subvertex.
     */
    public java.util.Collection getSubvertex();
}
