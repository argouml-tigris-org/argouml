package org.omg.uml.foundation;

/**
 * Foundation package interface.
 */
public interface FoundationPackage extends javax.jmi.reflect.RefPackage {
    /**
     * Returns nested package DataTypes.
     * @return Proxy object related to nested package DataTypes.
     */
    public org.omg.uml.foundation.datatypes.DataTypesPackage getDataTypes();
    /**
     * Returns nested package Core.
     * @return Proxy object related to nested package Core.
     */
    public org.omg.uml.foundation.core.CorePackage getCore();
    /**
     * Returns nested package ExtensionMechanisms.
     * @return Proxy object related to nested package ExtensionMechanisms.
     */
    public org.omg.uml.foundation.extensionmechanisms.ExtensionMechanismsPackage getExtensionMechanisms();
}
