package org.omg.uml.foundation.core;

/**
 * DataType class proxy interface.
 */
public interface DataTypeClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public DataType createDataType();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isRoot 
     * @param isLeaf 
     * @param isAbstract 
     * @return The created instance object.
     */
    public DataType createDataType(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isRoot, boolean isLeaf, boolean isAbstract);
}
