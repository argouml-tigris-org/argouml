/*
 * IllegalModelElementConnection.java
 *
 * Created on 16 March 2003, 12:42
 */

package org.argouml.model.uml;

/**
 *
 * @author  Administrator
 */
public class IllegalModelElementConnectionException extends UmlException {
    
    /**
     * Creates a new instance of <code>IllegalModelElementConnection</code> without detail message.
     */
    public IllegalModelElementConnectionException() {
    }
    
    
    /**
     * Constructs an instance of <code>IllegalModelElementConnection</code> with the specified detail message.
     * @param msg the detail message.
     */
    public IllegalModelElementConnectionException(String msg) {
        super(msg);
    }
    
    
    /**
     * Constructs an instance of <code>IllegalModelElementConnection</code> with the specified detail message.
     * @param msg the detail message.
     */
    public IllegalModelElementConnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
