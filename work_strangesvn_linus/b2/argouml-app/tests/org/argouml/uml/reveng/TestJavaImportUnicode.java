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

import java.util.Collection;
import java.util.Iterator;
import java.io.StringReader;
import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.uml.reveng.java.JavaLexer;
import org.argouml.uml.reveng.java.JavaRecognizer;
import org.argouml.uml.reveng.java.Modeller;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * Test the import of Java sources which have Unicode characters in
 * various locations, including in identifiers.
 *
 * The content of the Java source file is a private constant at the
 * bottom of the source of this class.
 *
 * The constant is written using the ISO8859-1 encoding so this class
 * needs to be compiled using that encoding.
 */
public class TestJavaImportUnicode extends TestCase {
    /*
     * @see junit.framework.TestCase#TestCase(String)
     */
    public TestJavaImportUnicode(String str) {
        super(str);
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        if (isParsed) {
            return;
        }

        // This shouldn't be necessary, but the Modeller is going to look in
        // the project to find the default type for attributes
        Project project = ProjectManager.getManager().getCurrentProject();
        assertNotNull("Failed to get/create project context", project);
        
        JavaLexer lexer = new JavaLexer(new StringReader(PARSERINPUT));
        assertNotNull("Creation of lexer failed.", lexer);

        lexer.setTokenObjectClass("org.argouml.uml.reveng.java.ArgoToken");

        JavaRecognizer parser = new JavaRecognizer(lexer);
        assertNotNull("Creation of parser failed.", parser);

        Object parsedModel = Model.getModelManagementFactory().createModel();
        assertNotNull("Creation of model failed.", parsedModel);

        Model.getModelManagementFactory().setRootModel(parsedModel);
        new InitProfileSubsystem().init();
        
        assertEquals("Setting Root Model failed", 
                Model.getModelManagementFactory().getRootModel(), 
                parsedModel);

        Modeller modeller =
            new Modeller(parsedModel, false, false,
                "TestClass.java");
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
        Object parsedPackage = lookupPackage();
        assertEquals("Inconsistent package name.",
                "téstpackage", Model.getFacade().getName(parsedPackage));
        assertEquals("The namespace of the package should be the model.",
                Model.getModelManagementFactory().getRootModel(), 
                Model.getFacade().getNamespace(parsedPackage));
        assertTrue("The package should be recognized as a package.",
                Model.getFacade().isAPackage(parsedPackage));
    }


    /**
     * Test if the import was processed correctly.
     */
    public void testClass() {
        Object parsedPackage = lookupPackage();
        Object parsedClass = lookupClass(parsedPackage);
        assertEquals("Inconsistent class name.",
            "TéstClass", Model.getFacade().getName(parsedClass));
        assertEquals("The namespace of the class should be \"téstpackage\".",
            parsedPackage, Model.getFacade().getNamespace(parsedClass));
        Collection generalizations =
            Model.getFacade().getGeneralizations(parsedClass);
        assertNotNull("No generalizations found for class.", generalizations);
        Object generalization = null;
        Iterator iter = generalizations.iterator();
        if (iter.hasNext()) {
            generalization = iter.next();
        }
        assertNotNull("No generalization found for class.", generalization);
        assertEquals("The generalization name is wrong.",
            "TéstClass -> Object", Model.getFacade().getName(generalization));

        Collection dependencies =
            Model.getFacade().getClientDependencies(parsedClass);
        assertNotNull("No dependencies found for class.", dependencies);
        Object abstraction = null;
        iter = dependencies.iterator();
        if (iter.hasNext()) {
            abstraction = iter.next();
        }
        assertNotNull("No abstraction found for class.", abstraction);
        assertEquals("The abstraction name is wrong.",
            "TéstClass -> Observer", Model.getFacade().getName(abstraction));

    }

    /**
     * Convenience method to lookup and check Package. 
     */
    private Object lookupPackage() {
        Object parsedPackage = Model.getFacade().lookupIn(
                Model.getModelManagementFactory().getRootModel(),
                "téstpackage");
        assertNotNull("No package \"téstpackage\" found in model.",
                parsedPackage);
        return parsedPackage;
    }

    /**
     * Convenience method to lookup and check Class.
     */
    private Object lookupClass(Object thePackage) {
        Object parsedClass =
            Model.getFacade().lookupIn(thePackage, "TéstClass");
        assertNotNull("No class \"TéstClass\" found.", parsedClass);
        return parsedClass;
    }
    
    /**
     * Test if the attributes were processed correctly.
     */
    public void testAttributes() {
        Object parsedPackage = lookupPackage();
        Object parsedClass = lookupClass(parsedPackage);
        Collection attributes = Model.getFacade().getAttributes(parsedClass);
        assertNotNull("No attributes found in class.", attributes);
        assertEquals("Number of attributes is wrong", 1, attributes.size());
        Object attribute = null;
        Object attributeFors = null;
        Iterator iter = attributes.iterator();
        while (iter.hasNext()) {
            attribute = iter.next();
            assertTrue("The attribute should be recognized as an attribute.",
                    Model.getFacade().isAAttribute(attribute));
            if ("sé".equals(Model.getFacade().getName(attribute))) {
                attributeFors = attribute;
            }
        }
        assertTrue("The attribute sé has the wrong name.",
                 attributeFors != null);

        Object initializer = Model.getFacade().getInitialValue(attributeFors);
        assertTrue("Attribute sé has no initializer.",
                Model.getFacade().isInitialized(attributeFors)
                && initializer != null);
        assertEquals("The initializer of attribute sé is wrong.",
            " \"final String objéct\"", Model.getFacade().getBody(initializer));
    }



    /**
     * Test if the association was processed correctly.
     */
    public void testAssociation() {
        Object parsedPackage = lookupPackage();
        Object parsedClass = lookupClass(parsedPackage);
        Collection associationEnds =
            Model.getFacade().getAssociationEnds(parsedClass);
        assertNotNull("No association ends found in class.", associationEnds);
        assertEquals("Number of association ends is wrong", 2,
                associationEnds.size());
        Object associationEnd = null;
        Object association = null;
        int navigableCount = 0;
        Iterator iter = associationEnds.iterator();
        while (iter.hasNext()) {
            associationEnd = iter.next();
            assertTrue(
                    "The attribute end should be recognized as "
                    + "an attribute end.",
                    Model.getFacade().isAAssociationEnd(associationEnd));
            assertEquals("The type of both association ends must be the class.",
                parsedClass, Model.getFacade().getType(associationEnd));
            if (association == null) {
                association = Model.getFacade().getAssociation(associationEnd);
                assertTrue(
                        "The attribute should be recognized as an attribute.",
                        Model.getFacade().isAAssociation(association));
                assertEquals("The association name is wrong.",
                    "TéstClass -> TéstClass",
                    Model.getFacade().getName(association));
            } else {
                assertEquals(
                        "Association end must belong to the same association.",
                        association,
                        Model.getFacade().getAssociation(associationEnd));
            }
            if (Model.getFacade().isNavigable(associationEnd)) {
                ++navigableCount;
            }
        }
        assertEquals("Only one association end must be navigable.",
                1, navigableCount);
    }

    /**
     * Test if the operations were processed correctly.
     */
    public void testOperations() {
        Object parsedPackage = lookupPackage();
        Object parsedClass = lookupClass(parsedPackage);
        Collection operations = Model.getFacade().getOperations(parsedClass);
        assertNotNull("No operations found in class.", operations);
        assertEquals("Number of operations is wrong", 2, operations.size());
        Object operation = null;
        Object operationForTestClass = null;
        Object operationForupdate = null;
        Iterator iter = operations.iterator();
        while (iter.hasNext()) {
            operation = iter.next();
            assertTrue("The operation should be recognized as an operation.",
                    Model.getFacade().isAOperation(operation));
            if ("TéstClass".equals(Model.getFacade().getName(operation))) {
                operationForTestClass = operation;
            } else if ("updaté".equals(Model.getFacade().getName(operation))) {
                operationForupdate = operation;
            } 
        }
        assertTrue("The operations have wrong names.",
            operationForTestClass != null
            && operationForupdate != null);
        assertEquals("The body of operation update is wrong.",
                BODY1,
                getBody(operationForupdate));

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

    private static final String BODY1 =
        "\n        if (arg instanceof TéstClass) téstClass = (TéstClass)arg;\n";

    /**
     * Test input for the parser. It's the content of a Java source file. It's
     * hardcoded here, because this test case strongly depends on this.
     */
    private static final String PARSERINPUT =
              "package téstpackage;\n"
            + "import java.util.Observer;\n"
            + "/** A Javadoc commént */\n"
            + "public abstract class TéstClass "
            + "extends Object implements Observer {\n"
            + "    protected static TéstClass téstClass;\n"
            + "    public static final String sé = \"final String objéct\";\n"
            + "    protected TéstClass(int n) {"
            + "    }\n"
            + "    public void updaté(java.util.Observable o, Object arg) {"
            + BODY1
            + "    }\n"

            + "}";
}
