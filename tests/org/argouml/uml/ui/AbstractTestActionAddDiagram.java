// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.ui.UMLDiagram;

import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * @author JBranderhorst
 */
public abstract class AbstractTestActionAddDiagram extends TestCase {

    /**
     * The action to be tested
     */
    private ActionAddDiagram _action;
    /**
     * The namespace a created diagram should have
     */
    private MNamespace _ns;

    /**
     * A list with namespaces that should be valid for the diagram to be
     * created.
     */
    private List _validNamespaces;

    /**
     * Constructor for AbstractTestActionAddDiagram.
     * @param arg0 test case name
     */
    public AbstractTestActionAddDiagram(String arg0) {
        super(arg0);
    }

    /**
     * Preparations for all test cases.
     */
    protected void setUp() {
	ArgoSecurityManager.getInstance().setAllowExit(true);
	_action = getAction();

	_ns = getNamespace();
	_validNamespaces = getValidNamespaceClasses();
    }

    /**
     * Cleanup after all test cases.
     */
    protected void tearDown() {
        _action = null;
        _ns = null;
        _validNamespaces = null;
    }

    /**
     * Should return the correct action. (for example, ActionClassDiagram for a
     * creation of a classdiagram).
     * @return ActionAddDiagram
     */
    protected abstract ActionAddDiagram getAction();

    /**
     * The namespace.
     *
     * @return a valid namespace for the diagram to be tested
     */
    protected abstract MNamespace getNamespace();

    /**
     * Should return a list with classes that implement MNamespace (or the JMI
     * equivalent in the future) and that are valid to use at creating the
     * diagram.
     * @return List
     */
    protected abstract List getValidNamespaceClasses();

    /**
     * Tests if a created diagram complies to the following conditions:
     * - Has a valid namespace
     * - Has a MutableGraphModel
     * - Has a proper name
     */
    public void testCreateDiagram() {
	UMLDiagram diagram = _action.createDiagram(_ns);
	assertNotNull(
		      "The diagram has no namespace",
		      diagram.getNamespace());
	assertTrue(
		   "The diagram has a non-valid namespace",
		   _action.isValidNamespace(diagram.getNamespace()));
	assertNotNull(
		      "The diagram has no graphmodel",
		      diagram.getGraphModel());
	assertTrue("The graphmodel of the diagram is not a "
		   + "UMLMutableGraphSupport",
		   diagram.getGraphModel() instanceof UMLMutableGraphSupport);
	assertNotNull("The diagram has no name", diagram.getName());
    }

    /**
     * Tests if two diagrams created have different names
     */
    public void testDifferentNames() {
	UMLDiagram diagram1 = _action.createDiagram(_ns);
	UMLDiagram diagram2 = _action.createDiagram(_ns);
	assertTrue(
		   "The created diagrams have the same name",
		   !(diagram1.getName().equals(diagram2.getName())));
    }

    /**
     * Tests if the namespace created by getNamespace() is a valid namespace for
     * the diagram.
     */
    public void testValidTestNamespace() {
	assertTrue(
		   "The namespace with this testis not valid for this diagram",
		   _action.isValidNamespace(_ns));
    }

    /**
     * Tests if the list with namespaces defined in getValidNamespaceClasses
     * contains only valid namespaces.
     *
     * @throws InstantiationException if we cannot create a new object from
     * 	                              the namespace.
     *                                @see java.lang.Class#newInstance()
     * @throws IllegalAccessException if the constructor of the class cannot
     *                                be called.
     *                                @see java.lang.Class#newInstance()
     */
    public void testValidNamespaces() 
	throws InstantiationException, IllegalAccessException
    {
	Iterator it = _validNamespaces.iterator();
	while (it.hasNext()) {
	    Class clazz = (Class) it.next();

	    Object o = clazz.newInstance();
	    assertTrue(
		       clazz.getName()
		       + " is no valid namespace for the diagram",
		       _action.isValidNamespace((MNamespace) o));
	}
    }

}
