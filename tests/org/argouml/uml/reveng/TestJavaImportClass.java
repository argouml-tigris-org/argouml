// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.Iterator;
import java.io.StringReader;
import junit.framework.TestCase;
import org.argouml.model.Model;
import org.argouml.uml.reveng.java.JavaLexer;
import org.argouml.uml.reveng.java.JavaRecognizer;
import org.argouml.uml.reveng.java.Modeller;

/**
 * Test case to test the import of a Java source file. The content of the Java
 * source file is a private constant at the bottom of the source of this class.
 * The test methods are specially designed for this Java source constant.
 * Feeding of the diagram subsystem is disabled; only model elements are
 * created and checked. For testing with another Java source file, copy this
 * test case, change the Java source constant and modify the test method (the
 * setUp method need not be changed).<p>
 */
public class TestJavaImportClass extends TestCase {
    /**
     * @see junit.framework.TestCase#TestCase(String)
     */
    public TestJavaImportClass(String str) {
        super(str);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        if (_isParsed) {
            return;
        }
        JavaLexer lexer = null;
        JavaRecognizer parser = null;
        Modeller modeller = null;
        try {
            lexer = new JavaLexer(new StringReader(PARSERINPUT));
            lexer.setTokenObjectClass("org.argouml.uml.reveng.java.ArgoToken");
            parser = new JavaRecognizer(lexer);
            _model = Model.getModelManagementFactory().createModel();
            Model.getModelManagementFactory().setRootModel(_model);
            modeller = new Modeller(_model, null, null, false, false,
                    "TestClass.java");
        } catch (Exception ex) {}
        assertNotNull("Creation of lexer failed.", lexer);
        assertNotNull("Creation of parser failed.", parser);
        assertNotNull("Creation of model failed.", _model);
        assertNotNull("Creation of Modeller instance failed.", modeller);
        try {
            parser.compilationUnit(modeller, lexer);
            _isParsed = true;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            fail("Parsing of Java source failed.");
        }
    }

    /**
     * Test if the package was processed correctly.
     */
    public void testPackage() {
        _package = Model.getFacade().lookupIn(_model, "testpackage");
        assertNotNull("No package \"testpackage\" found in model.", _package);
        assertEquals("Inconsistent package name.",
            "testpackage", Model.getFacade().getName(_package));
        assertEquals("The namespace of the package should be the model.",
            _model, Model.getFacade().getNamespace(_package));
        assertTrue("The package should be recognized as a package.", 
                Model.getFacade().isAPackage(_package));
    }

    /**
     * Test if the import was processed correctly.
     */
    public void testImport() {
        if (_package == null) {
            _package = Model.getFacade().lookupIn(_model, "testpackage");
            assertNotNull("No package \"testpackage\" found in model.", 
                    _package);
        }
        Collection ownedElements = Model.getFacade().getOwnedElements(_package);
        assertNotNull("No elements owned by  \"testpackage\".", ownedElements);
        Object component = null;
        Iterator iter = ownedElements.iterator();
        while (iter.hasNext()) {
            Object element = iter.next();
            if (Model.getFacade().isAComponent(element)) {
                component = element;
                break;
            }
        }
        assertNotNull("No component found.", component);
        assertEquals("The component name is wrong.",
            "TestClass.java", Model.getFacade().getName(component));
        Collection dependencies = Model.getFacade().getClientDependencies(
                component);
        assertNotNull("No dependencies found for component.", dependencies);
        Object permission = null;
        iter = dependencies.iterator();
        while (iter.hasNext()) {
            Object element = iter.next();
            if (Model.getFacade().isAPermission(element)) {
                permission = element;
                break;
            }
        }
        assertNotNull("No import found.", permission);
        assertEquals("The import name is wrong.",
            "TestClass.java -> Observer", 
            Model.getFacade().getName(permission));
        Collection suppliers = Model.getFacade().getSuppliers(permission);
        assertNotNull("No suppliers found in import.", suppliers);
        Object supplier = null;
        iter = suppliers.iterator();
        if (iter.hasNext()) {
            supplier = iter.next();
        }
        assertNotNull("No supplier found in import.", supplier);
        assertEquals("The import supplier name is wrong.",
            "Observer", Model.getFacade().getName(supplier));
        Object namespace = Model.getFacade().getNamespace(supplier);
        assertNotNull("The import supplier has no namespace.", namespace);
        assertEquals("Expected namespace name \"util\".",
            "util", Model.getFacade().getName(namespace));
        namespace = Model.getFacade().getNamespace(namespace);
        assertNotNull("The namespace \"util\" has no namespace.", namespace);
        assertEquals("Expected namespace name \"java\".",
            "java", Model.getFacade().getName(namespace));
        assertEquals("The namespace of \"java\" should be the model.",
            _model, Model.getFacade().getNamespace(namespace));
    }

    /**
     * Test if the import was processed correctly.
     */
    public void testClass() {
        if (_package == null) {
            _package = Model.getFacade().lookupIn(_model, "testpackage");
            assertNotNull("No package \"testpackage\" found in model.",
                    _package);
        }
        _class = Model.getFacade().lookupIn(_package, "TestClass");
        assertNotNull("No class \"TestClass\" found.", _class);
        assertEquals("Inconsistent class name.",
            "TestClass", Model.getFacade().getName(_class));
        assertEquals("The namespace of the class should be \"testpackage\".",
            _package, Model.getFacade().getNamespace(_class));
        assertTrue("The class should be recognized as a class.", 
                Model.getFacade().isAClass(_class));
        assertTrue("The class should be abstract.", 
                Model.getFacade().isAbstract(_class));
        assertTrue("The class should be public.", 
                Model.getFacade().isPublic(_class));
        Collection generalizations = 
            Model.getFacade().getGeneralizations(_class);
        assertNotNull("No generalizations found for class.", generalizations);
        Object generalization = null;
        Iterator iter = generalizations.iterator();
        if (iter.hasNext()) {
            generalization = iter.next();
        }
        assertNotNull("No generalization found for class.", generalization);
        assertEquals("The generalization name is wrong.",
            "TestClass -> Object", Model.getFacade().getName(generalization));
        assertEquals("The child of the generalization should be the class.",
            _class, Model.getFacade().getChild(generalization));
        assertEquals("The parent of the generalization should be \"Object\".",
            "Object", 
            Model.getFacade().getName(
                        Model.getFacade().getParent(generalization)));
        Collection dependencies = 
            Model.getFacade().getClientDependencies(_class);
        assertNotNull("No dependencies found for class.", dependencies);
        Object abstraction = null;
        iter = dependencies.iterator();
        if (iter.hasNext()) {
            abstraction = iter.next();
        }
        assertNotNull("No abstraction found for class.", abstraction);
        assertEquals("The abstraction name is wrong.",
            "TestClass -> Observer", Model.getFacade().getName(abstraction));
        try {
            assertEquals("The client of the abstraction should be the class.",
                _class, 
                Model.getFacade().getClients(abstraction)
                            .iterator().next());
        } catch (Exception ex) {
            fail("The implementation dependency has no clients.");
        }
        try {
            assertEquals(
                    "The supplier of the abstraction should be \"Observer\".",
                    "Observer", Model.getFacade().getName(
                            Model.getFacade().getSuppliers(abstraction)
                                    .iterator().next()));
        } catch (Exception ex) {
            fail("The abstraction has no suppliers.");
        }
    }

    /**
     * Test if the attributes were processed correctly.
     */
    public void testAttributes() {
        if (_package == null) {
            _package = Model.getFacade().lookupIn(_model, "testpackage");
            assertNotNull("No package \"testpackage\" found in model.", 
                    _package);
        }
        if (_class == null) {
            _class = Model.getFacade().lookupIn(_package, "TestClass");
            assertNotNull("No class \"TestClass\" found.", _class);
        }
        Collection attributes = Model.getFacade().getAttributes(_class);
        assertNotNull("No attributes found ib class.", attributes);
        assertEquals("Number of attributes is wrong", 2, attributes.size());
        Object attribute = null;
        Object attribute_n = null;
        Object attribute_s = null;
        Iterator iter = attributes.iterator();
        while (iter.hasNext()) {
            attribute = iter.next();
            assertTrue("The attribute should be recognized as an attribute.", 
                    Model.getFacade().isAAttribute(attribute));
            if ("n".equals(Model.getFacade().getName(attribute))) {
                attribute_n = attribute;
            } else if ("s".equals(Model.getFacade().getName(attribute))) {
                attribute_s = attribute;
            }
        }
        assertTrue("The attributes have wrong names.", 
                attribute_n != null && attribute_s != null);
        Object attrib_type = Model.getFacade().getType(attribute_n);
        Object initializer = Model.getFacade().getInitialValue(attribute_n);
        assertTrue("Attribute n should be private.", 
                Model.getFacade().isPrivate(attribute_n));
        assertTrue("Attribute n should not be final.", 
                Model.getFacade().isChangeable(attribute_n));
        assertTrue("Attribute n should have type int.", 
                "int".equals(Model.getFacade().getName(attrib_type)));
        assertTrue("Attribute n has no initializer.", 
                Model.getFacade().isInitialized(attribute_n) 
                && initializer != null);
        assertEquals("The initializer of attribute n is wrong.",
            " 0", Model.getFacade().getBody(initializer));
        attrib_type = Model.getFacade().getType(attribute_s);
        initializer = Model.getFacade().getInitialValue(attribute_s);
        assertTrue("Attribute s should be public.", 
                Model.getFacade().isPublic(attribute_s));
        assertTrue("Attribute s should be static.", 
                Model.getFacade().isClassifierScope(attribute_s));
        assertTrue("Attribute s should be final.", 
                !Model.getFacade().isChangeable(attribute_s));
        assertTrue("Attribute s should have type String.", 
                "String".equals(Model.getFacade().getName(attrib_type)));
        assertTrue("Attribute s has no initializer.", 
                Model.getFacade().isInitialized(attribute_s) 
                && initializer != null);
        assertEquals("The initializer of attribute s is wrong.",
            " \"final String object\"", Model.getFacade().getBody(initializer));
    }

    /**
     * Test if the association was processed correctly.
     */
    public void testAssociation() {
        if (_package == null) {
            _package = Model.getFacade().lookupIn(_model, "testpackage");
            assertNotNull("No package \"testpackage\" found in model.", 
                    _package);
        }
        if (_class == null) {
            _class = Model.getFacade().lookupIn(_package, "TestClass");
            assertNotNull("No class \"TestClass\" found.", _class);
        }
        Collection associationEnds = 
            Model.getFacade().getAssociationEnds(_class);
        assertNotNull("No association ends found ib class.", associationEnds);
        assertEquals("Number of association ends is wrong", 2, 
                associationEnds.size());
        Object associationEnd = null;
        Object association = null;
        boolean unidirectional = false;
        Iterator iter = associationEnds.iterator();
        while (iter.hasNext()) {
            associationEnd = iter.next();
            assertTrue(
                    "The attribute end should be recognized as an attribute end.",
                    Model.getFacade().isAAssociationEnd(associationEnd));
            assertEquals("The type of both association ends must be the class.",
                _class, Model.getFacade().getType(associationEnd));
            if (association == null) {
                association = Model.getFacade().getAssociation(associationEnd);
                assertTrue(
                        "The attribute should be recognized as an attribute.", 
                        Model.getFacade().isAAssociation(association));
                assertEquals("The association name is wrong.",
                    "TestClass -> TestClass", 
                    Model.getFacade().getName(association));
            } else {
                assertEquals(
                        "Association end must belong to the same association.",
                        association, 
                        Model.getFacade().getAssociation(associationEnd));
            }
            if (Model.getFacade().isNavigable(associationEnd)) {
                unidirectional = !unidirectional;
            }
        }
        assertTrue("Only one association end must be navigable.", 
                unidirectional);
    }

    /**
     * Test if the operations were processed correctly.
     */
    public void testOperations() {
        if (_package == null) {
            _package = Model.getFacade().lookupIn(_model, "testpackage");
            assertNotNull("No package \"testpackage\" found in model.", 
                    _package);
        }
        if (_class == null) {
            _class = Model.getFacade().lookupIn(_package, "TestClass");
            assertNotNull("No class \"TestClass\" found.", _class);
        }
        Collection operations = Model.getFacade().getOperations(_class);
        assertNotNull("No operations found ib class.", operations);
        assertEquals("Number of operations is wrong", 4, operations.size());
        Object operation = null;
        Object operation_TestClass = null;
        Object operation_update = null;
        Object operation_getString = null;
        Object operation_x = null;
        Iterator iter = operations.iterator();
        while (iter.hasNext()) {
            operation = iter.next();
            assertTrue("The operation should be recognized as an operation.", 
                    Model.getFacade().isAOperation(operation));
            if ("TestClass".equals(Model.getFacade().getName(operation))) {
                operation_TestClass = operation;
            } else if ("update".equals(Model.getFacade().getName(operation))) {
                operation_update = operation;
            } else if ("getString".equals(Model.getFacade().getName(operation))) {
                operation_getString = operation;
            } else if ("x".equals(Model.getFacade().getName(operation))) {
                operation_x = operation;
            }
        }
        assertTrue("The operations have wrong names.",
            operation_TestClass != null 
            && operation_update != null 
            && operation_getString != null 
            && operation_x != null);
        assertTrue("Operation TestClass should be protected.", 
                Model.getFacade().isProtected(operation_TestClass));
        assertEquals("The body of operation TestClass is wrong.",
            "\n        // A constructor with a single line comment\n        this.n = n;\n", getBody(operation_TestClass));
        assertTrue("Operation update should be public.", 
                Model.getFacade().isPublic(operation_update));
        assertEquals("The body of operation update is wrong.",
            "\n        if (arg instanceof TestClass) testClass = (TestClass)arg;\n", 
            getBody(operation_update));
        assertTrue("Operation getString should be static.", 
                Model.getFacade().isClassifierScope(operation_getString));
        assertTrue("Operation getString should be private.", 
                Model.getFacade().isPrivate(operation_getString));
        assertEquals("The body of operation getString is wrong.",
            "\n        // A static method\n        return s;\n", 
            getBody(operation_getString));
        assertTrue("Operation x should be abstract.", 
                Model.getFacade().isAbstract(operation_x));
        assertTrue("Operation x should be public.", 
                Model.getFacade().isPublic(operation_x));
    }

    /**
     * Gets the (first) body of an operation.
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
    private static boolean _isParsed = false;

    /**
     * Instances of the model and it's components.
     */
    private static Object _model = null;
    private static Object _package = null;
    private static Object _class = null;

    /**
     * Test input for the parser. It's the content of a Java source file. It's
     * hardcoded here, because this test case strongly depends on this.
     */
    private static final String PARSERINPUT =
              "package testpackage;\n"
            + "import java.util.Observer;\n"
            + "/** A Javadoc comment */\n"
            + "public abstract class TestClass extends Object implements Observer {\n"
            + "    private int n = 0;\n"
            + "    protected static TestClass testClass;\n"
            + "    public static final String s = \"final String object\";\n"
            + "    protected TestClass(int n) {\n"
            + "        // A constructor with a single line comment\n"
            + "        this.n = n;\n"
            + "    }\n"
            + "    public void update(java.util.Observable o, Object arg) {\n"
            + "        if (arg instanceof TestClass) testClass = (TestClass)arg;\n"
            + "    }\n" 
            + "    /** Another Javadoc comment */\n"
            + "    private static String getString() {\n"
            + "        // A static method\n" + "        return s;\n"
            + "    }\n" + "    abstract void x();\n" 
            + "    /* A multiline\n"
            + "       comment\n" + "    */\n" + "}";
}