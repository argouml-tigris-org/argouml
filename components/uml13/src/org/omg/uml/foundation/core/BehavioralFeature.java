package org.omg.uml.foundation.core;

/**
 * BehavioralFeature object instance interface.
 */
public interface BehavioralFeature extends org.omg.uml.foundation.core.Feature {
    /**
     * Returns the value of attribute isQuery.
     * @return Value of attribute isQuery.
     */
    public boolean isQuery();
    /**
     * Sets the value of isQuery attribute. See {@link #isQuery} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setQuery(boolean newValue);
    /**
     * Returns the value of reference parameter.
     * @return Value of reference parameter.
     */
    public java.util.List getParameter();
}
