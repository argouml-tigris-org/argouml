package org.omg.uml.foundation.datatypes;

/**
 * MultiplicityRange class proxy interface.
 */
public interface MultiplicityRangeClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public MultiplicityRange createMultiplicityRange();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param lower 
     * @param upper 
     * @return The created instance object.
     */
    public MultiplicityRange createMultiplicityRange(int lower, int upper);
}
