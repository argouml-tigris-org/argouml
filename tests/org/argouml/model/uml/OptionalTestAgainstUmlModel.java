// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.behavioralelements.activitygraphs.ActivityGraphsFactory;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorFactory;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesFactory;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;
import org.argouml.util.CheckUMLModelHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OptionalTestAgainstUmlModel extends TestCase {

    /** List of element references and the factories that create them.
     *
     *  This contains a complete list of the model elements
     *  that are <strong>expected</strong> to be found
     *  in the model.
     */
    static Hashtable refs = null;
 
    public OptionalTestAgainstUmlModel(String n) { super(n); }

    public void testDataModel()
	throws SAXException,
	       IOException,
	       ParserConfigurationException {
	DocumentBuilder builder =
	    DocumentBuilderFactory.newInstance().newDocumentBuilder();
	String fileName = System.getProperty("test.model.uml13");
	if (fileName == null)
	    fail("The property test.model.uml13 is not set.");
	File file = new File(fileName);
	if (!file.exists())
	    fail("The file " + fileName + " cannot be found.");
	Document doc = builder.parse(file);
	NodeList list = doc.getElementsByTagName("Model:Class");

	assertEquals(refs.size(), list.getLength());

	for (int i = 0; i < list.getLength(); i++) {
	    processClassNode ("", list.item(i));
	}
    }

    /** Walk through the UML Classes found.
     *
     *  Though some of the DOM methods such as getAttributes
     *  may return null values under other conditions,
     *  in the context of this test
     *  and assuming a valid XMI file
     *  none should occur.
     *
     *  Hence there is no special checking for those abnormal
     *  cases, allowing the test to fail simply with a
     *  NullPointerException, with this comment indicating that
     *  either the input data is incorrect or the test needs
     *  to be improved.
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
	}
	else if (factory instanceof AbstractUmlModelFactory) {
	    String[] classarg = {umlclass, null};
	    CheckUMLModelHelper.createAndRelease(this,
	                                         (AbstractUmlModelFactory) factory,
			                         classarg);
	}
	else {
	    fail("Test is invalid for uml method '" + umlclass + "'");
	}
    }

    /** Initialize the lookup map to link the uml class names
     *  to the factories.
     *
     *  This brute force method should be investigated
     *  in favor of determining the Uml Class namespace from
     *  the XMI data model and computing the factory
     *  at run time.
     *
     *  Certain classes that cannot be tested directly in this way
     *  should be calculcated.  Event and StateVertex, for example,
     *  are marked abstract in the model.  But we need to make sure
     *  that the reverse is true, that there are no elements
     *  marked abstract in the model that in fact are instantiable
     *  by NSUML.
     */
    static {
        refs = new Hashtable(127);
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
        refs.put("Element",               new CannotTestFactoryMethod());
        refs.put("ModelElement",          new CannotTestFactoryMethod());
        refs.put("GeneralizableElement",  new CannotTestFactoryMethod());
        refs.put("Namespace",             CoreFactory.getFactory());
        refs.put("Classifier",            CoreFactory.getFactory());
        refs.put("Class",                 CoreFactory.getFactory());
        refs.put("DataType",              CoreFactory.getFactory());
        refs.put("Feature",               new CannotTestFactoryMethod());
        refs.put("StructuralFeature",     new CannotTestFactoryMethod());
        refs.put("AssociationEnd",        CoreFactory.getFactory());
        refs.put("Interface",             CoreFactory.getFactory());
        refs.put("Constraint",            CoreFactory.getFactory());
        refs.put("Relationship",          CoreFactory.getFactory());
        refs.put("Association",           CoreFactory.getFactory());
        refs.put("Attribute",             CoreFactory.getFactory());
        refs.put("BehavioralFeature",     new CannotTestFactoryMethod());
        refs.put("Operation",             CoreFactory.getFactory());
        refs.put("Parameter",             CoreFactory.getFactory());
        refs.put("Method",                CoreFactory.getFactory());
        refs.put("Generalization",        CoreFactory.getFactory());
        refs.put("AssociationClass",      CoreFactory.getFactory());
        refs.put("Dependency",            CoreFactory.getFactory());
        refs.put("Abstraction",           CoreFactory.getFactory());
        refs.put("PresentationElement",   new CannotTestFactoryMethod());
        refs.put("Usage",                 CoreFactory.getFactory());
        refs.put("Binding",               CoreFactory.getFactory());
        refs.put("Component",             CoreFactory.getFactory());
        refs.put("Node",                  CoreFactory.getFactory());
        refs.put("Permission",            CoreFactory.getFactory());
        refs.put("Comment",               CoreFactory.getFactory());
        refs.put("Flow",                  CoreFactory.getFactory());
        refs.put("ElementResidence",      CoreFactory.getFactory());
        refs.put("TemplateParameter",     CoreFactory.getFactory());
        refs.put("Stereotype",            ExtensionMechanismsFactory.getFactory());
        refs.put("TaggedValue",           ExtensionMechanismsFactory.getFactory());
        refs.put("Instance",              CommonBehaviorFactory.getFactory());
        refs.put("Signal",                CommonBehaviorFactory.getFactory());
        refs.put("Action",                CommonBehaviorFactory.getFactory());
        refs.put("CreateAction",          CommonBehaviorFactory.getFactory());
        refs.put("DestroyAction",         CommonBehaviorFactory.getFactory());
        refs.put("UninterpretedAction",   CommonBehaviorFactory.getFactory());
        refs.put("AttributeLink",         CommonBehaviorFactory.getFactory());
        refs.put("Object",                CommonBehaviorFactory.getFactory());
        refs.put("Link",                  CommonBehaviorFactory.getFactory());
        refs.put("LinkObject",            CommonBehaviorFactory.getFactory());
        refs.put("DataValue",             CommonBehaviorFactory.getFactory());
        refs.put("CallAction",            CommonBehaviorFactory.getFactory());
        refs.put("SendAction",            CommonBehaviorFactory.getFactory());
        refs.put("ActionSequence",        CommonBehaviorFactory.getFactory());
        refs.put("Argument",              CommonBehaviorFactory.getFactory());
        refs.put("Reception",             CommonBehaviorFactory.getFactory());
        refs.put("LinkEnd",               CommonBehaviorFactory.getFactory());
        refs.put("ReturnAction",          CommonBehaviorFactory.getFactory());
        refs.put("TerminateAction",       CommonBehaviorFactory.getFactory());
        refs.put("Stimulus",              CommonBehaviorFactory.getFactory());
        refs.put("Exception",             CommonBehaviorFactory.getFactory());
        refs.put("ComponentInstance",     CommonBehaviorFactory.getFactory());
        refs.put("NodeInstance",          CommonBehaviorFactory.getFactory());
        refs.put("UseCase",               UseCasesFactory.getFactory());
        refs.put("Actor",                 UseCasesFactory.getFactory());
        refs.put("UseCaseInstance",       UseCasesFactory.getFactory());
        refs.put("Extend",                UseCasesFactory.getFactory());
        refs.put("Include",               UseCasesFactory.getFactory());
        refs.put("ExtensionPoint",        UseCasesFactory.getFactory());
        refs.put("StateMachine",          StateMachinesFactory.getFactory());
        refs.put("Event",                 new CannotTestClassIsAbstract());
        refs.put("StateVertex",           new CannotTestClassIsAbstract());
        refs.put("State",                 StateMachinesFactory.getFactory());
        refs.put("TimeEvent",             StateMachinesFactory.getFactory());
        refs.put("CallEvent",             StateMachinesFactory.getFactory());
        refs.put("SignalEvent",           StateMachinesFactory.getFactory());
        refs.put("Transition",            StateMachinesFactory.getFactory());
        refs.put("CompositeState",        StateMachinesFactory.getFactory());
        refs.put("ChangeEvent",           StateMachinesFactory.getFactory());
        refs.put("Guard",                 StateMachinesFactory.getFactory());
        refs.put("Pseudostate",           StateMachinesFactory.getFactory());
        refs.put("SimpleState",           StateMachinesFactory.getFactory());
        refs.put("SubmachineState",       StateMachinesFactory.getFactory());
        refs.put("SynchState",            StateMachinesFactory.getFactory());
        refs.put("StubState",             StateMachinesFactory.getFactory());
        refs.put("FinalState",            StateMachinesFactory.getFactory());
        refs.put("Collaboration",         CollaborationsFactory.getFactory());
        refs.put("ClassifierRole",        CollaborationsFactory.getFactory());
        refs.put("AssociationRole",       CollaborationsFactory.getFactory());
        refs.put("AssociationEndRole",    CollaborationsFactory.getFactory());
        refs.put("Message",               CollaborationsFactory.getFactory());
        refs.put("Interaction",           CollaborationsFactory.getFactory());
        refs.put("ActivityGraph",         ActivityGraphsFactory.getFactory());
        refs.put("Partition",             ActivityGraphsFactory.getFactory());
        refs.put("SubactivityState",      ActivityGraphsFactory.getFactory());
        refs.put("ActionState",           ActivityGraphsFactory.getFactory());
        refs.put("CallState",             ActivityGraphsFactory.getFactory());
        refs.put("ObjectFlowState",       ActivityGraphsFactory.getFactory());
        refs.put("ClassifierInState",     ActivityGraphsFactory.getFactory());
        refs.put("Package",               ModelManagementFactory.getFactory());
        refs.put("Model",                 ModelManagementFactory.getFactory());
        refs.put("Subsystem",             ModelManagementFactory.getFactory());
        refs.put("ElementImport",         ModelManagementFactory.getFactory());
    }

    /**
     * Returns the refs.
     * @return Hashtable
     */
    public static Hashtable getRefs() {
        return refs;
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
	super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
    }

}

interface CannotTestThisClass {
}

class CannotTestFactoryMethod implements CannotTestThisClass {
}

class CannotTestClassIsAbstract implements CannotTestThisClass {
}

