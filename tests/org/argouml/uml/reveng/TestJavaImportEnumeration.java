// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.reveng;

import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.argouml.model.Model;
import org.argouml.uml.reveng.java.JavaLexer;
import org.argouml.uml.reveng.java.JavaRecognizer;
import org.argouml.uml.reveng.java.Modeller;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * Test case to test the import of a Java source file containing a Java 5 enum.
 * <p>
 * The content of the Java source file is a private constant at the bottom of
 * the source of this class. The test methods are specially designed for this
 * Java source constant. Feeding of the diagram subsystem is disabled; only
 * model elements are created and checked.
 * <p>
 * @author Tom Morris
 */
public class TestJavaImportEnumeration extends TestCase {
    /*
     * @see junit.framework.TestCase#TestCase(String)
     */
    public TestJavaImportEnumeration(String str) {
        super(str);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        if (isParsed) {
            return;
        }

        JavaLexer lexer = new JavaLexer(new StringReader(PARSERINPUT));
        assertNotNull("Creation of lexer failed.", lexer);

        lexer.setTokenObjectClass("org.argouml.uml.reveng.java.ArgoToken");

        JavaRecognizer parser = new JavaRecognizer(lexer);
        assertNotNull("Creation of parser failed.", parser);

        parsedModel = Model.getModelManagementFactory().createModel();
        assertNotNull("Creation of model failed.", parsedModel);

        Model.getModelManagementFactory().setRootModel(parsedModel);

        Modeller modeller = new Modeller(parsedModel,
                new DummyImportSettings(), "TestClass.java");
        assertNotNull("Creation of Modeller instance failed.", modeller);

        try {
            parser.compilationUnit(modeller, lexer);
            isParsed = true;
        } catch (RecognitionException e) {
            fail("Parsing of Java source failed." + e);
        } catch (TokenStreamException e) {
            fail("Parsing of Java source failed." + e);
        }
    }

    /**
     * Test if the class with the enumeration stereotype was created
     */
    public void testSimpleEnumeration() {
        if (parsedPackage == null) {
            parsedPackage =
                Model.getFacade().lookupIn(parsedModel, "testpackage");
            assertNotNull("No package \"testpackage\" found in model.",
                    parsedPackage);
        }
        parsedClass = Model.getFacade().lookupIn(parsedPackage, "TestClass");
       
        Object mainClass = parsedClass;
        parsedClass = null;
        parsedClass = getEnumeration(mainClass, "Color");
        
        // Attributes
        
        Collection attributes = Model.getFacade().getAttributes(parsedClass);
        assertNotNull("No attributes found in class.", attributes);
        assertEquals("Number of attributes is wrong", 3, attributes.size());
        Object attribute = null;
        Object attributeGreen = null;
        Object attributeYellow = null;
        Object attributeRed = null;
        
        Iterator iter = attributes.iterator();
        while (iter.hasNext()) {
            attribute = iter.next();
            assertTrue("The attribute should be recognized as an attribute.",
                    Model.getFacade().isAAttribute(attribute));
            if ("GREEN".equals(Model.getFacade().getName(attribute))) {
                attributeGreen = attribute;
            } else if ("YELLOW".equals(Model.getFacade().getName(attribute))) {
                attributeYellow = attribute;
            } else if ("RED".equals(Model.getFacade().getName(attribute))) {
                attributeRed = attribute;
            } 
        }
        assertTrue("The attributes have wrong names.", 
                attributeGreen != null 
                || attributeYellow != null 
                || attributeRed != null);
        assertFalse("Attribute GREEN should be final.",
                Model.getFacade().isChangeable(attributeGreen));
        assertTrue("Attribute GREEN should be static.",
                Model.getFacade().isClassifierScope(attributeGreen));

    }

    /**
     * Look up an enumeration by name in its parent namespace
     * and check that it's properly formed.
     */
    private Object getEnumeration(Object namespace, String name) {
        Object enumeration = Model.getFacade().lookupIn(namespace, name);
        assertNotNull("failed to find enumeration" + name, enumeration);
        assertEquals("Inconsistent class name.", name, 
                Model.getFacade().getName(enumeration));
        Collection stereotypes = Model.getFacade().getStereotypes(enumeration);
        assertNotNull("no stereotypes found", stereotypes);
        assertEquals("wrong number of stereotypes found", 1, 
                stereotypes.size());
        Object stereotype = stereotypes.iterator().next();
        assertEquals("<<enumeration stereotype>> not found", "enumeration",
                Model.getFacade().getName(stereotype));
        return enumeration;
    }

    /**
     * Test if the class with the enumeration stereotype was created
     */
    public void testEnumeration() {
        if (parsedPackage == null) {
            parsedPackage =
                Model.getFacade().lookupIn(parsedModel, "testpackage");
            assertNotNull("No package \"testpackage\" found in model.",
                    parsedPackage);
        }
        parsedClass = Model.getFacade().lookupIn(parsedPackage, "TestClass");
        assertNotNull("No class \"TestClass\" found.", parsedClass);
        assertEquals("Inconsistent class name.",
            "TestClass", Model.getFacade().getName(parsedClass));
        assertEquals("The namespace of the class should be \"testpackage\".",
            parsedPackage, Model.getFacade().getNamespace(parsedClass));
        
        Object mainClass = parsedClass;
        parsedClass = null;
        parsedClass = getEnumeration(mainClass, "MessagePriority");
        
        // Attributes
        
        Collection attributes = Model.getFacade().getAttributes(parsedClass);
        assertNotNull("No attributes found in class.", attributes);
        assertEquals("Number of attributes is wrong", 4, attributes.size());
        Object attribute = null;
        Object attributeVP = null;
        Object attributeLow = null;
        Object attributeNormal = null;
        Object attributeHigh = null;
        
        Iterator iter = attributes.iterator();
        while (iter.hasNext()) {
            attribute = iter.next();
            assertTrue("The attribute should be recognized as an attribute.",
                    Model.getFacade().isAAttribute(attribute));
            if ("valuePersist".equals(Model.getFacade().getName(attribute))) {
                attributeVP = attribute;
            } else if ("LOW".equals(Model.getFacade().getName(attribute))) {
                attributeLow = attribute;
            } else if ("NORMAL".equals(Model.getFacade().getName(attribute))) {
                attributeNormal = attribute;
            } else if ("HIGH".equals(Model.getFacade().getName(attribute))) {
                attributeHigh = attribute;
            } 
        }
        assertTrue("The attributes have wrong names.", 
                attributeVP != null 
                || attributeLow != null 
                || attributeNormal != null
                || attributeHigh != null);
        assertFalse("Attribute LOW should be final.",
                Model.getFacade().isChangeable(attributeLow));

        assertFalse("Attribute valuePersist should not be private.",
                Model.getFacade().isPrivate(attributeVP));
        assertTrue("Attribute valuePersist should not be final.",
                Model.getFacade().isChangeable(attributeVP));
        Object attribType = Model.getFacade().getType(attributeVP);
        assertTrue("Attribute valuePersist should have type short.",
                "short".equals(Model.getFacade().getName(attribType)));

        // Operations

        Collection operations = Model.getFacade().getOperations(parsedClass);
        assertNotNull("No operations found in class.", operations);
        assertEquals("Number of operations is wrong", 3, operations.size());
        Object operation = null;
        Object operationMP = null;
        Object operationCP = null;
        Object operationGVP = null;
        iter = operations.iterator();
        while (iter.hasNext()) {
            operation = iter.next();
            assertTrue("The operation should be recognized as an operation.",
                    Model.getFacade().isAOperation(operation));
            if ("MessagePriority".equals(
                    Model.getFacade().getName(operation))) {
                operationMP = operation;
            } else if ("createPersist".equals(Model.getFacade().getName(
                    operation))) {
                operationCP = operation;
            } else if ("getValuePersist".equals(
                    Model.getFacade().getName(operation))) {
                operationGVP = operation;
            } 
        }
        assertTrue("The operations have wrong names.",
            operationMP != null
            && operationCP != null
            && operationGVP != null);
        assertTrue("Constructor MessagePriority should be private.",
                Model.getFacade().isPrivate(operationMP));
        assertEquals("The body of operation MessagePriority is wrong.",
                BODY2, getBody(operationMP));
        assertTrue("Operation update should be public.",
                Model.getFacade().isPublic(operationCP));
        assertEquals("The body of operation update is wrong.",
                BODY1,
                getBody(operationCP));
        assertTrue("Operation createPersist should be static.",
                Model.getFacade().isClassifierScope(operationCP));
        assertTrue("Operation getString should be public.",
                Model.getFacade().isPublic(operationGVP));
        assertEquals("The body of operation getString is wrong.",
                BODY3,
                getBody(operationGVP));
        
        // TODO: check return types

    }

    /**
     * Gets the (first) body of an operation.
     *
     * @param operation The operation.
     * @return The first body.
     */
    private static String getBody(Object operation) {
        String body = null;
        Collection methods = Model.getFacade().getMethods(operation);
        if (methods != null && !methods.isEmpty()) {
            Object expression =
                Model.getFacade().getBody(methods.iterator().next());
            body = (String) Model.getFacade().getBody(expression);
        }
        return body;
    }

    /**
     * Flag, if the Java source is parsed already.
     */
    private static boolean isParsed;

    /**
     * Instances of the model and it's components.
     */
    private static Object parsedModel;
    private static Object parsedPackage;
    private static Object parsedClass;

    private static final String BODY1 =
        "          switch (val) {\n"
        + "             case 1:\n"
        + "                return LOW;\n"
        + "             case 5:\n"
        + "                return NORMAL;\n"
        + "             case 9:\n"
        + "                return HIGH;\n"
        + "             default:\n"
        + "                throw new Exception (\"MessagePriority unknown\");\n"
        + "          }\n";
    
    private static final String BODY2 = "          this.valuePersist = val;\n";
    private static final String BODY3 = "          return this.valuePersist;\n";

    /**
     * Test input for the parser. It's the content of a Java source file. It's
     * hardcoded here, because this test case strongly depends on this.
     */
    private static final String PARSERINPUT =
              "package testpackage;\n"

            + "public class TestClass {\n"
            + "    public enum Color {GREEN, YELLOW, RED};"
            + "    public enum MessagePriority {\n"
            + "       LOW ((short) 1),\n"
            + "       NORMAL ((short) 5),\n"
            + "       HIGH ((short) 9);\n"
            + "\n"
            + "       public static MessagePriority createPersist "
            + "(final short val) throws Exception {"
            + BODY1
            + "       }\n"
            + "\n"
            + "       short valuePersist;\n"
            + "\n"
            + "       private MessagePriority (final short val) {"
            + BODY2
            + "       }\n"
            + "\n"
            + "       public int getValuePersist () {"
            + BODY3
            + "       }\n"
            + "    }\n"

            + "}";
    
}


