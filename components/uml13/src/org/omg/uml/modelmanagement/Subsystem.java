package org.omg.uml.modelmanagement;

/**
 * Subsystem object instance interface.
 */
public interface Subsystem extends org.omg.uml.modelmanagement.UmlPackage, org.omg.uml.foundation.core.Classifier {
    /**
     * Returns the value of attribute isInstantiable.
     * @return Value of attribute isInstantiable.
     */
    public boolean isInstantiable();
    /**
     * Sets the value of isInstantiable attribute. See {@link #isInstantiable} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setInstantiable(boolean newValue);
}
