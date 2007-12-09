// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.internal.InitProfileSubsystem;
import org.argouml.uml.reveng.java.JavaLexer;
import org.argouml.uml.reveng.java.JavaRecognizer;
import org.argouml.uml.reveng.java.Modeller;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * Test case to test the import of a Java source file. The content of the Java
 * source file is a private constant at the bottom of the source of this
 * class. The test methods are specially designed for this Java source
 * constant. Feeding of the diagram subsystem is disabled; only model elements
 * are created and checked. For testing with another Java source file, copy this
 * test case, change the Java source constant and modify the test method (the
 * setUp method need not be changed).<p>
 */
public class TestJavaImportInterface extends TestCase {
    /*
     * @see junit.framework.TestCase#TestCase(String)
     */
    public TestJavaImportInterface(String str) {
        super(str);
        InitializeModel.initializeDefault();
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
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
        new InitProfileSubsystem().init();

        Modeller modeller =
                new Modeller(parsedModel, new DummyImportSettings(),
                    "TestInterface.java");
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
     * Test if the package was processed correctly.
     */
    public void testPackage() {
        parsedPackage = Model.getFacade().lookupIn(parsedModel, "testpackage");
        assertNotNull("No package \"testpackage\" found in model.",
                parsedPackage);
        assertEquals("Inconsistent package name.",
                "testpackage", Model.getFacade().getName(parsedPackage));
        assertEquals("The namespace of the package should be the model.",
                parsedModel, Model.getFacade().getNamespace(parsedPackage));
        assertTrue("The package should be recognized as a package.",
                Model.getFacade().isAPackage(parsedPackage));
    }

    /**
     * Test if the import was processed correctly.
     */
    public void testImport() {
        if (parsedPackage == null) {
            parsedPackage =
                Model.getFacade().lookupIn(parsedModel, "testpackage");
            assertNotNull("No package \"testpackage\" found in model.",
                    parsedPackage);
        }
        Collection ownedElements =
            Model.getFacade().getOwnedElements(parsedPackage);
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
            "TestInterface.java", Model.getFacade().getName(component));
        Collection dependencies =
            Model.getFacade().getClientDependencies(component);
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
            "TestInterface.java -> Observer",
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
            parsedModel, Model.getFacade().getNamespace(namespace));
    }

    /**
     * Test if the import was processed correctly.
     */
    public void testInterface() {
        if (parsedPackage == null) {
            parsedPackage =
                Model.getFacade().lookupIn(parsedModel, "testpackage");
            assertNotNull("No package \"testpackage\" found in model.",
                    parsedPackage);
        }
        parsedInterface =
            Model.getFacade().lookupIn(parsedPackage, "TestInterface");
        assertNotNull("No interface \"TestInterface\" found.", parsedInterface);
        assertEquals("Inconsistent interface name.",
            "TestInterface", Model.getFacade().getName(parsedInterface));
        assertEquals("The namespace of the interface should be "
                + "\"testpackage\".",
            parsedPackage, Model.getFacade().getNamespace(parsedInterface));
        assertTrue("The interface should be recognized as a interface.",
                Model.getFacade().isAInterface(parsedInterface));
        assertTrue("The interface should be public.",
                Model.getFacade().isPublic(parsedInterface));
        Collection generalizations =
            Model.getFacade().getGeneralizations(parsedInterface);
        assertNotNull("No generalizations found for interface.",
                generalizations);
        Object generalization = null;
        Iterator iter = generalizations.iterator();
        if (iter.hasNext()) {
            generalization = iter.next();
        }
        assertNotNull("No generalization found for interface.", generalization);
        assertEquals("The generalization name is wrong.",
            "TestInterface -> Observer",
            Model.getFacade().getName(generalization));
        assertEquals("The child of the generalization should be the interface.",
            parsedInterface, Model.getFacade().getSpecific(generalization));
        assertEquals(
                "The parent of the generalization should be \"Observer\".",
                "Observer", Model.getFacade().getName(
                        Model.getFacade().getGeneral(generalization)));
    }

    /**
     * Test if the operations were processed correctly.
     */
    public void testOperations() {
        if (parsedPackage == null) {
            parsedPackage =
                Model.getFacade().lookupIn(parsedModel, "testpackage");
            assertNotNull("No package \"testpackage\" found in model.",
                    parsedPackage);
        }
        if (parsedInterface == null) {
            parsedInterface =
                Model.getFacade().lookupIn(parsedPackage, "TestInterface");
            assertNotNull("No interface \"TestInterface\" found.",
                    parsedInterface);
        }
        Collection operations =
            Model.getFacade().getOperations(parsedInterface);
        assertNotNull("No operations found ib interface.", operations);
        assertEquals("Number of operations is wrong", 2, operations.size());
        Object operation = null;
        Object operationForx = null;
        Object operationFory = null;
        Iterator iter = operations.iterator();
        while (iter.hasNext()) {
            operation = iter.next();
            assertTrue("The operation should be recognized as an operation.",
                    Model.getFacade().isAOperation(operation));
            if ("x".equals(Model.getFacade().getName(operation))) {
                operationForx = operation;
            } else if ("y".equals(Model.getFacade().getName(operation))) {
                operationFory = operation;
            }
        }
        assertTrue("The operations have wrong names.",
                operationForx != null && operationFory != null);
        assertTrue("Operation x should be public.",
                Model.getFacade().isPublic(operationForx));
        assertTrue("Operation y should be public.",
                Model.getFacade().isPublic(operationFory));
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
    private static Object parsedInterface;

    /**
     * Test input for the parser. It's the content of a Java source file. It's
     * hardcoded here, because this test case strongly depends on this.
     */
    private static final String PARSERINPUT =
              "package testpackage;\n"
            + "import java.util.Observer;\n"
            + "/** A Javadoc comment */\n"
            + "public interface TestInterface extends Observer {\n"
            + "    /** Another Javadoc comment */\n"
            + "    public void x(String a);\n"
            + "    public int y();\n"
            + "}";
}
