package org.omg.uml.foundation.datatypes;

/**
 * Expression class proxy interface.
 */
public interface ExpressionClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Expression createExpression();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param language 
     * @param body 
     * @return The created instance object.
     */
    public Expression createExpression(java.lang.String language, java.lang.String body);
}
