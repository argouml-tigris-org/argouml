package org.omg.uml.foundation.core;

/**
 * Class object instance interface.
 */
public interface UmlClass extends org.omg.uml.foundation.core.Classifier {
    /**
     * Returns the value of attribute isActive.
     * @return Value of attribute isActive.
     */
    public boolean isActive();
    /**
     * Sets the value of isActive attribute. See {@link #isActive} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setActive(boolean newValue);
}
