// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.reveng.classfile;

import antlr.*;


/**
 * A special AST node, that holds a Java object.
 */
class ObjectAST extends CommonAST {

    /////////////////
    // Instance vars.

    // The buffer.
    private Object _object = null;

    
    ///////////////
    // Constructors

    /**
     * Create a new instance.
     *
     * @param type The type of the AST node.
     * @param val The Object.
     */
    ObjectAST(int type, Object val) {
        super();
	setType(type);
        setObjectValue(val);
    }


    //////////
    // Methods

    /**
     * Get the value of the buffer.
     *
     * @return The value of the buffer.
     */
    final Object getObjectValue() {
	return _object;
    }

    /**
     * Set the value of the buffer.
     *
     * @param val The new value for the buffer.
     */
    final void setObjectValue(Object val) {
        _object = val;
    }

    /**
     * Get the object buffer as an int.
     *
     * @return The value of the buffer as a int.
     */
    final int getIntegerValue() {
	return ((Integer) _object).intValue();
    }

    /**
     * Get the object buffer as a long.
     *
     * @return The value of the buffer as a long.
     */
    final long getLongValue() {
	return ((Long) _object).longValue();
    }

    /**
     * Get the object buffer as a float.
     *
     * @return The value of the buffer as a float.
     */
    final float getFloatValue() {
	return ((Float) _object).shortValue();
    }

    /**
     * Get the object buffer as a double.
     *
     * @return The value of the buffer as a double.
     */
    final double getDoubleValue() {
	return ((Double) _object).doubleValue();
    }
}
