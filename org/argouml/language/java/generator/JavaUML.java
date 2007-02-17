// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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


package org.argouml.language.java.generator;

import org.argouml.model.CoreFactory;
import org.argouml.model.Model;

/**
 * A file of information about the Java language.  This is used to
 * fill in the offered data types in variable and operation
 * declarations.
 *
 * In the end, it would be better to have these in XMI files that are
 * loaded at starting time.
*/
public class JavaUML {

    // java.lang

    private static final Object STRING_CLASS = createClass();

    private static final Object VOID_TYPE = createDataType();
    private static final Object CHAR_TYPE = createDataType();
    private static final Object INT_TYPE = createDataType();
    private static final Object BOOLEAN_TYPE = createDataType();
    private static final Object BYTE_TYPE = createDataType();
    private static final Object LONG_TYPE = createDataType();
    private static final Object FLOAT_TYPE = createDataType();
    private static final Object DOUBLE_TYPE = createDataType();

    private static final Object CHAR_CLASS = createClass();
    private static final Object INT_CLASS = createClass();
    private static final Object BOOLEAN_CLASS = createClass();
    private static final Object BYTE_CLASS = createClass();
    private static final Object LONG_CLASS = createClass();
    private static final Object FLOAT_CLASS = createClass();
    private static final Object DOUBLE_CLASS = createClass();


    // java.awt
    private static final Object RECTANGLE_CLASS = createClass();
    private static final Object POINT_CLASS = createClass();
    private static final Object COLOR_CLASS = createClass();


    // java.util
    private static final Object VECTOR_CLASS = createClass();
    private static final Object HASHTABLE_CLASS = createClass();
    private static final Object STACK_CLASS = createClass();

    /**
     * Get the CoreFactory.
     *
     * @return CoreFactory
     */
    private static CoreFactory getCore() {
	return Model.getCoreFactory();
    }

    /**
     * Create a new class.
     *
     * @return a new class
     */
    private static Object createClass() {
	return getCore().createClass();
    }

    /**
     * Create a new datatype.
     *
     * @return a new datatype
     */
    private static Object createDataType() {
	return getCore().createDataType();
    }

    
    /**
     * This UML Model contains the Java standard elements.
     */
    //private static Object javastandards =
    //    Model.getModelManagementFactory().createModel();

    static {
        Model.getCoreHelper().setName(STRING_CLASS, "String");
        Model.getCoreHelper().setName(CHAR_CLASS, "Character");
        Model.getCoreHelper().setName(INT_CLASS, "Integer");
        Model.getCoreHelper().setName(BOOLEAN_CLASS, "Boolean");
        Model.getCoreHelper().setName(BYTE_CLASS, "Byte");
        Model.getCoreHelper().setName(LONG_CLASS, "Long");
        Model.getCoreHelper().setName(FLOAT_CLASS, "Float");
        Model.getCoreHelper().setName(DOUBLE_CLASS, "Double");
        Model.getCoreHelper().setName(RECTANGLE_CLASS, "Rectangle");
        Model.getCoreHelper().setName(POINT_CLASS, "Point");
        Model.getCoreHelper().setName(COLOR_CLASS, "Color");
        Model.getCoreHelper().setName(VECTOR_CLASS, "Vector");
        Model.getCoreHelper().setName(HASHTABLE_CLASS, "Hashtable");
        Model.getCoreHelper().setName(STACK_CLASS, "Stack");

        Model.getCoreHelper().setName(VOID_TYPE, "void");
        Model.getCoreHelper().setName(CHAR_TYPE, "char");
        Model.getCoreHelper().setName(INT_TYPE, "int");
        Model.getCoreHelper().setName(BOOLEAN_TYPE, "boolean");
        Model.getCoreHelper().setName(BYTE_TYPE, "byte");
        Model.getCoreHelper().setName(LONG_TYPE, "long");
        Model.getCoreHelper().setName(FLOAT_TYPE, "float");
        Model.getCoreHelper().setName(DOUBLE_TYPE, "double");

	//    javastandards.addOwnedElement(STRING_CLASS);
	//    javastandards.addOwnedElement(CHAR_CLASS);
	//    javastandards.addOwnedElement(INT_CLASS);
	//    javastandards.addOwnedElement(BOOLEAN_CLASS);
	//    javastandards.addOwnedElement(BYTE_CLASS);
	//    javastandards.addOwnedElement(LONG_CLASS);
	//    javastandards.addOwnedElement(FLOAT_CLASS);
	//    javastandards.addOwnedElement(DOUBLE_CLASS);
	//    javastandards.addOwnedElement(RECTANGLE_CLASS);
	//    javastandards.addOwnedElement(POINT_CLASS);
	//    javastandards.addOwnedElement(COLOR_CLASS);
	//    javastandards.addOwnedElement(VECTOR_CLASS);
	//    javastandards.addOwnedElement(HASHTABLE_CLASS);
	//    javastandards.addOwnedElement(STACK_CLASS);
	//    javastandards.addOwnedElement(VOID_TYPE);
	//    javastandards.addOwnedElement(CHAR_TYPE);
	//    javastandards.addOwnedElement(INT_TYPE);
	//    javastandards.addOwnedElement(BOOLEAN_TYPE);
	//    javastandards.addOwnedElement(BYTE_TYPE);
	//    javastandards.addOwnedElement(LONG_TYPE);
	//    javastandards.addOwnedElement(FLOAT_TYPE);
	//    javastandards.addOwnedElement(DOUBLE_TYPE);

	//    UUIDManager.SINGLETON.createModelUUIDS(javastandards);
	//    javastandards.setName("Javastandards"); //try {XMIWriter
	//    writer = new XMIWriter(javastandards,
	//    "java.xmi");writer.gen();} catch (Exception e) {}
    }
} /* end class JavaUML */
