package org.omg.uml.behavioralelements.commonbehavior;

/**
 * ComponentInstance object instance interface.
 */
public interface ComponentInstance extends org.omg.uml.behavioralelements.commonbehavior.Instance {
    /**
     * Returns the value of reference nodeInstance.
     * @return Value of reference nodeInstance.
     */
    public org.omg.uml.behavioralelements.commonbehavior.NodeInstance getNodeInstance();
    /**
     * Sets the value of reference nodeInstance. See {@link #getNodeInstance} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setNodeInstance(org.omg.uml.behavioralelements.commonbehavior.NodeInstance newValue);
    /**
     * Returns the value of reference resident.
     * @return Value of reference resident.
     */
    public java.util.Collection getResident();
}
