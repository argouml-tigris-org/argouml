package org.omg.uml.behavioralelements.commonbehavior;

/**
 * Reception object instance interface.
 */
public interface Reception extends org.omg.uml.foundation.core.BehavioralFeature {
    /**
     * Returns the value of attribute specification.
     * @return Value of attribute specification.
     */
    public java.lang.String getSpecification();
    /**
     * Sets the value of specification attribute. See {@link #getSpecification} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setSpecification(java.lang.String newValue);
    /**
     * Returns the value of attribute isRoot.
     * @return Value of attribute isRoot.
     */
    public boolean isRoot();
    /**
     * Sets the value of isRoot attribute. See {@link #isRoot} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setRoot(boolean newValue);
    /**
     * Returns the value of attribute isLeaf.
     * @return Value of attribute isLeaf.
     */
    public boolean isLeaf();
    /**
     * Sets the value of isLeaf attribute. See {@link #isLeaf} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setLeaf(boolean newValue);
    /**
     * Returns the value of attribute isAbstract.
     * @return Value of attribute isAbstract.
     */
    public boolean isAbstract();
    /**
     * Sets the value of isAbstract attribute. See {@link #isAbstract} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setAbstract(boolean newValue);
    /**
     * Returns the value of reference signal.
     * @return Value of reference signal.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Signal getSignal();
    /**
     * Sets the value of reference signal. See {@link #getSignal} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setSignal(org.omg.uml.behavioralelements.commonbehavior.Signal newValue);
}
