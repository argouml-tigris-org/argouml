// $Id$
// Copyright (c) 2003-2005 The Regents of the University of California. All
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

package org.argouml.model;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Test against the UML model.
 */
public class TestAgainstUmlModel extends TestCase {

    /**
     * List of element references and the factories that create them.
     *
     * This contains a complete list of the model elements
     * that are <strong>expected</strong> to be found
     * in the model.
     */
    private static Hashtable refs;

    /**
     * The constructor.
     *
     * @param n the name
     */
    public TestAgainstUmlModel(String n) { super(n); }

    /**
     * @throws SAXException when things go wrong with SAX
     * @throws IOException when there's an IO error
     * @throws ParserConfigurationException when the parser finds wrong syntax
     */
    public void testDataModel()
	throws SAXException,
	       IOException,
	       ParserConfigurationException {
	Document doc = prepareDocument();
	if (doc == null) {
	    return;		// Could not find model.
	}

	NodeList list = doc.getElementsByTagName("Model:Class");

	assertEquals(refs.size(), list.getLength());

	for (int i = 0; i < list.getLength(); i++) {
	    processClassNode ("", list.item(i));
	}
    }


    /**
     * Print a message that this test case is inconclusive because of
     * the UML file missing.
     *
     * @param message that is to me printed.
     */
    private static void printInconclusiveMessage(String message) {
	System.out.println(TestAgainstUmlModel.class.getName()
                + ": WARNING: INCONCLUSIVE TEST!");
	System.out.println(message);
	System.out.println("You will have to fetch the model using the command"
			   + " ant junit-get-model");
    }

    /**
     * Make all preparations for the tests by preparing the document.
     *
     * @return the document or null if not available.
     * @throws SAXException when things go wrong with SAX
     * @throws IOException when there's an IO error
     * @throws ParserConfigurationException when the parser finds wrong syntax
     */
    private static Document prepareDocument()
	throws ParserConfigurationException, SAXException, IOException {
	DocumentBuilder builder =
	    DocumentBuilderFactory.newInstance().newDocumentBuilder();
	String fileName = System.getProperty("test.model.uml");
	if (fileName == null) {
	    printInconclusiveMessage("The property test.model.uml "
				     + "is not set.");
	    return null;
	}
	File file = new File(fileName);
	if (!file.exists()) {
	    printInconclusiveMessage("The file " + fileName
				     + " cannot be found.");
	    return null;
	}
	return builder.parse(file);
    }


    /**
     * Walk through the UML Classes found.
     *
     * Though some of the DOM methods such as getAttributes
     * may return null values under other conditions,
     * in the context of this test
     * and assuming a valid XMI file
     * none should occur.
     *
     * Hence there is no special checking for those abnormal
     * cases, allowing the test to fail simply with a
     * NullPointerException, with this comment indicating that
     * either the input data is incorrect or the test needs
     * to be improved.
     */
    private void processClassNode(String indent, Node node) {
        String umlclass =
            node.getAttributes().getNamedItem("name").getNodeValue();
        Object factory = refs.get(umlclass);
        assertNotNull("Unable to find factory '" + umlclass + "' in references",
                      factory);
        System.out.println ("Class:" + umlclass);
        if (factory instanceof CannotTestThisClass) {
            System.out.println ("Explicitly not checking for " + umlclass);
        } else if (factory instanceof UmlFactory) {
            String[] classarg = {umlclass, null};
            CheckUMLModelHelper.createAndRelease(factory, classarg);
        } else {
            fail("Test is invalid for uml method '" + umlclass + "'");
        }
    }

    /**
     * Initialize the lookup map to link the uml class names
     * to the factories.
     *
     * This brute force method should be investigated
     * in favor of determining the Uml Class namespace from
     * the UML metamodel and computing the factory
     * at run time.
     *
     * Certain classes that cannot be tested directly in this way
     * should be calculated.  Event and StateVertex, for example,
     * are marked abstract in the model.  But we need to make sure
     * that the reverse is true, that there are no elements
     * marked abstract in the model that in fact are instantiable
     * by the model subsystem.
     */

    static {
        refs = new Hashtable(127);
    }

    static {
        refs.put("Multiplicity",          new CannotTestFactoryMethod());
        refs.put("MultiplicityRange",     new CannotTestFactoryMethod());
        refs.put("Expression",            new CannotTestFactoryMethod());
        refs.put("ObjectSetExpression",   new CannotTestFactoryMethod());
        refs.put("TimeExpression",        new CannotTestFactoryMethod());
        refs.put("BooleanExpression",     new CannotTestFactoryMethod());
        refs.put("ActionExpression",      new CannotTestFactoryMethod());
        refs.put("IterationExpression",   new CannotTestFactoryMethod());
        refs.put("TypeExpression",        new CannotTestFactoryMethod());
        refs.put("ArgListsExpression",    new CannotTestFactoryMethod());
        refs.put("MappingExpression",     new CannotTestFactoryMethod());
        refs.put("ProcedureExpression",   new CannotTestFactoryMethod());
    }

    static {
        refs.put("Element",               new CannotTestClassIsAbstract());
        refs.put("ModelElement",          new CannotTestClassIsAbstract());
        refs.put("GeneralizableElement",  new CannotTestClassIsAbstract());
        refs.put("Namespace",             new CannotTestClassIsAbstract());
        refs.put("Classifier",            new CannotTestClassIsAbstract());
        refs.put("Class",                 Model.getCoreFactory());
        refs.put("DataType",              Model.getCoreFactory());
        refs.put("Primitive",             Model.getCoreFactory());
        refs.put("Enumeration",           Model.getCoreFactory());
        refs.put("EnumerationLiteral",    Model.getCoreFactory());
        refs.put("ProgrammingLanguageDataType",  Model.getCoreFactory());
        refs.put("Feature",               new CannotTestClassIsAbstract());
        refs.put("StructuralFeature",     new CannotTestClassIsAbstract());
        refs.put("AssociationEnd",        Model.getCoreFactory());
        refs.put("Interface",             Model.getCoreFactory());
        refs.put("Constraint",            Model.getCoreFactory());
        refs.put("Relationship",          new CannotTestClassIsAbstract());
        refs.put("Association",           Model.getCoreFactory());
        refs.put("Attribute",             Model.getCoreFactory());
        refs.put("BehavioralFeature",     new CannotTestClassIsAbstract());
        refs.put("Operation",             Model.getCoreFactory());
        refs.put("Parameter",             Model.getCoreFactory());
        refs.put("Method",                Model.getCoreFactory());
        refs.put("Generalization",        Model.getCoreFactory());
        refs.put("AssociationClass",      Model.getCoreFactory());
        refs.put("Dependency",            Model.getCoreFactory());
        refs.put("Abstraction",           Model.getCoreFactory());
    }
    
    static {
        refs.put("PresentationElement",   new CannotTestClassIsAbstract());
        refs.put("Usage",                 Model.getCoreFactory());
        refs.put("Binding",               Model.getCoreFactory());
        refs.put("Component",             Model.getCoreFactory());
        refs.put("Node",                  Model.getCoreFactory());
        refs.put("Artifact",              Model.getCoreFactory());
        refs.put("Permission",            Model.getCoreFactory());
        refs.put("Comment",               Model.getCoreFactory());
        refs.put("Flow",                  Model.getCoreFactory());
        refs.put("ElementResidence",      Model.getCoreFactory());
        refs.put("TemplateParameter",     Model.getCoreFactory());
        refs.put("TemplateArgument",      Model.getCoreFactory());
        refs.put("Stereotype", 
        		new CannotTestFactoryMethod());
        refs.put("TaggedValue",
		 Model.getExtensionMechanismsFactory());
        refs.put("TagDefinition",
                 Model.getExtensionMechanismsFactory());
        // Instance changed from concrete to abstract between UML 1.3 & 1.4
        refs.put("Instance",              new CannotTestClassIsAbstract());
        refs.put("Signal",                Model.getCommonBehaviorFactory());
        refs.put("Action",                new CannotTestClassIsAbstract());
        refs.put("CreateAction",          Model.getCommonBehaviorFactory());
        refs.put("DestroyAction",         Model.getCommonBehaviorFactory());
        refs.put("UninterpretedAction",   Model.getCommonBehaviorFactory());
    }

    static {
        refs.put("AttributeLink",         Model.getCommonBehaviorFactory());
        refs.put("Object",                Model.getCommonBehaviorFactory());
        refs.put("Link",                  Model.getCommonBehaviorFactory());
        refs.put("LinkObject",            Model.getCommonBehaviorFactory());
        refs.put("DataValue",             Model.getCommonBehaviorFactory());
        refs.put("CallAction",            Model.getCommonBehaviorFactory());
        refs.put("SendAction",            Model.getCommonBehaviorFactory());
        refs.put("ActionSequence",        Model.getCommonBehaviorFactory());
        refs.put("Argument",              Model.getCommonBehaviorFactory());
        refs.put("Reception",             Model.getCommonBehaviorFactory());
        refs.put("LinkEnd",               Model.getCommonBehaviorFactory());
        refs.put("ReturnAction",          Model.getCommonBehaviorFactory());
        refs.put("TerminateAction",       Model.getCommonBehaviorFactory());
        refs.put("Stimulus",              Model.getCommonBehaviorFactory());
        refs.put("Exception",             Model.getCommonBehaviorFactory());
        refs.put("ComponentInstance",     Model.getCommonBehaviorFactory());
        refs.put("NodeInstance",          Model.getCommonBehaviorFactory());
        refs.put("SubsystemInstance",     Model.getCommonBehaviorFactory());
    }

    static {
        refs.put("UseCase",               Model.getUseCasesFactory());
        refs.put("Actor",                 Model.getUseCasesFactory());
        refs.put("UseCaseInstance",       Model.getUseCasesFactory());
        refs.put("Extend",                Model.getUseCasesFactory());
        refs.put("Include",               Model.getUseCasesFactory());
        refs.put("ExtensionPoint",        Model.getUseCasesFactory());
    }

    static {
        refs.put("StateMachine",          Model.getStateMachinesFactory());
        refs.put("Event",                 new CannotTestClassIsAbstract());
        refs.put("StateVertex",           new CannotTestClassIsAbstract());
        // State changed from concrete to abstract between UML 1.3 & 1.4
    	refs.put("State",                 new CannotTestClassIsAbstract());
        refs.put("TimeEvent",             Model.getStateMachinesFactory());
        refs.put("CallEvent",             Model.getStateMachinesFactory());
        refs.put("SignalEvent",           Model.getStateMachinesFactory());
        refs.put("Transition",            Model.getStateMachinesFactory());
        refs.put("CompositeState",        Model.getStateMachinesFactory());
        refs.put("ChangeEvent",           Model.getStateMachinesFactory());
        refs.put("Guard",                 Model.getStateMachinesFactory());
        refs.put("Pseudostate",           Model.getStateMachinesFactory());
        refs.put("SimpleState",           Model.getStateMachinesFactory());
        refs.put("SubmachineState",       Model.getStateMachinesFactory());
        refs.put("SynchState",            Model.getStateMachinesFactory());
        refs.put("StubState",             Model.getStateMachinesFactory());
        refs.put("FinalState",            Model.getStateMachinesFactory());
    }

    static {
        refs.put("Collaboration",         Model.getCollaborationsFactory());
        refs.put("CollaborationInstanceSet", Model.getCollaborationsFactory());
        refs.put("ClassifierRole",        Model.getCollaborationsFactory());
        refs.put("AssociationRole",       Model.getCollaborationsFactory());
        refs.put("AssociationEndRole",    Model.getCollaborationsFactory());
        refs.put("Message",               Model.getCollaborationsFactory());
        refs.put("Interaction",           Model.getCollaborationsFactory());
        refs.put("InteractionInstanceSet",  Model.getCollaborationsFactory());
        refs.put("ActivityGraph",         Model.getActivityGraphsFactory());
        refs.put("Partition",             Model.getActivityGraphsFactory());
        refs.put("SubactivityState",      Model.getActivityGraphsFactory());
        refs.put("ActionState",           Model.getActivityGraphsFactory());
        refs.put("CallState",             Model.getActivityGraphsFactory());
        refs.put("ObjectFlowState",       Model.getActivityGraphsFactory());
        refs.put("ClassifierInState",     Model.getActivityGraphsFactory());
        refs.put("Package",               Model.getModelManagementFactory());
        refs.put("Model",                 Model.getModelManagementFactory());
        refs.put("Subsystem",             Model.getModelManagementFactory());
        refs.put("ElementImport",         Model.getModelManagementFactory());
    }

    /**
     * Returns the refs.
     *
     * TODO: Is this method used? Can it be removed? /Linus September 2005
     *
     * @return Hashtable
     */
    public static Hashtable getRefs() {
        return refs;
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        /* Running the tests here causes instantiation errors from the
         * navigator pane.  This is a temporary hack until the object
         * model is cleaned up.
         */
//      NavigatorPane.setInstance(null);
//      assertNull("Still getting NavigatorPane", NavigatorPane.getInstance());
    }

    /**
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite =
	    new TestSuite("Tests for "
			  + TestAgainstUmlModel.class.getPackage().getName());

	Document doc = null;
	try {
	    doc = prepareDocument();
	} catch (ParserConfigurationException e) {
	    e.printStackTrace();
	} catch (SAXException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	if (doc == null) {
	    return suite;	// Could not find model.
	}

        NodeList list = doc.getElementsByTagName("Model:Class");

        //assertEquals(refs.size(), list.getLength());

        for (int i = 0; i < list.getLength(); i++) {
	    Node name = list.item(i).getAttributes().getNamedItem("name");
	    String nameVal = name.getNodeValue();
            suite.addTest(new TestAgainstUmlModel(nameVal));
        }
        return suite;
    }

    /**
     * Test a specific element.
     *
     * @see junit.framework.TestCase#runTest()
     */
    protected void runTest() throws Throwable {
        String umlclass = getName();
        Object factory = refs.get(umlclass);
        assertNotNull("Unable to find factory '" + umlclass + "' in references",
                      factory);
        System.out.println ("Class:" + umlclass);
        if (factory instanceof CannotTestThisClass) {
            System.out.println ("Explicitly not checking for " + umlclass);
        } else if (factory instanceof AbstractModelFactory) {
            String[] classarg = {umlclass, null};
            CheckUMLModelHelper.createAndRelease(factory, classarg);
        } else {
            fail("Test is invalid for uml method '" + umlclass + "'");
        }
    }


}

/**
 * Token for keeping track of what to test.
 */
interface CannotTestThisClass {
}

/**
 * Token with reason.
 */
class CannotTestFactoryMethod implements CannotTestThisClass {
}

/**
 * Token with reason.
 */
class CannotTestClassIsAbstract implements CannotTestThisClass {
}

