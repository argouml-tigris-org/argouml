package org.omg.uml.foundation.core;

/**
 * Operation object instance interface.
 */
public interface Operation extends org.omg.uml.foundation.core.BehavioralFeature {
    /**
     * Returns the value of attribute concurrency.
     * @return Value of attribute concurrency.
     */
    public org.omg.uml.foundation.datatypes.CallConcurrencyKind getConcurrency();
    /**
     * Sets the value of concurrency attribute. See {@link #getConcurrency} for 
     * description on the attribute.
     * @param newValue New value to be set.
     */
    public void setConcurrency(org.omg.uml.foundation.datatypes.CallConcurrencyKind newValue);
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
     * Returns the value of reference method.
     * @return Value of reference method.
     */
    public java.util.Collection getMethod();
}
