/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2003-2007 The Regents of the University of California. All
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

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;

/**
 * @author JBranderhorst
 */
public abstract class AbstractTestActionAddDiagram extends TestCase {

    /**
     * The action to be tested.
     */
    private ActionAddDiagram action;

    /**
     * The namespace a created diagram should have.
     */
    private Object ns;

    /**
     * A list with namespaces that should be valid for the diagram to be
     * created.
     */
    private List validNamespaces;

    private Project project;

    /**
     * Constructor for AbstractTestActionAddDiagram.
     * @param arg0 test case name
     */
    public AbstractTestActionAddDiagram(String arg0) {
        super(arg0);
        InitializeModel.initializeDefault();
    }

    /**
     * Preparations for all test cases.
     */
    protected void setUp() {
        (new InitNotation()).init();
        (new InitNotationUml()).init();
        (new InitNotationJava()).init();
        (new InitProfileSubsystem()).init();

        project = ProjectManager.getManager().makeEmptyProject();

	action = getAction();

	ns = getNamespace();
	validNamespaces = getValidNamespaceClasses();
    }

    /**
     * Cleanup after all test cases.
     */
    protected void tearDown() {
        action = null;
        ns = null;
        validNamespaces = null;

        ProjectManager.getManager().removeProject(project);
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
    protected abstract Object getNamespace();

    /**
     * Should return a list with classes that implement Namespace
     * and that are valid to use at creating the diagram.
     * @return List
     */
    protected abstract List getValidNamespaceClasses();

    /**
     * Tests if a created diagram complies to the following conditions.<ul>
     * <li>Has a valid namespace
     * <li>Has a MutableGraphModel
     * <li>Has a proper name
     * </ul>
     */
    public void testCreateDiagram() {
        Model.getPump().flushModelEvents();
	assertTrue("The test case has a non-valid namespace",
		   action.isValidNamespace(ns));

	ArgoDiagram diagram = action.createDiagram(ns);
        Model.getPump().flushModelEvents();
	checkDiagram(diagram);

        diagram = action.createDiagram(ns, new DiagramSettings());
        Model.getPump().flushModelEvents();
        checkDiagram(diagram);
    }

    private void checkDiagram(ArgoDiagram diagram) {
        assertNotNull(
        	      "The diagram has no namespace",
        	      diagram.getNamespace());
        checkNamespace(diagram);
        assertNotNull(
        	      "The diagram has no graphmodel",
        	      diagram.getGraphModel());
        assertTrue("The graphmodel of the diagram is not a "
        	   + "UMLMutableGraphSupport",
        	   diagram.getGraphModel() instanceof UMLMutableGraphSupport);
        assertNotNull("The diagram has no name", diagram.getName());
    }

    /**
     * Test if the namespace is correct for the diagram.<p>
     *
     * This is the default implementation if the namespace of the
     * created diagram is the same as the one where the diagram is
     * created. If not, the specialized test will override this.
     *
     * @param diagram The diagram to check the namespace for.
     */
    protected void checkNamespace(ArgoDiagram diagram) {
        assertTrue(
        	   "The diagram has a non-valid namespace",
        	   action.isValidNamespace(diagram.getNamespace()));
    }


    /**
     * Tests if the namespace created by getNamespace() is a valid namespace for
     * the diagram.
     */
    public void testValidTestNamespace() {
	assertTrue(
		   "The namespace with this test is not valid for this diagram",
		   action.isValidNamespace(ns));
    }

    /**
     * Tests if the list with namespaces defined in getValidNamespaceClasses
     * contains only valid namespaces.
     */
    public void testValidNamespaces() {
	Iterator it = validNamespaces.iterator();
	while (it.hasNext()) {
	    Object type = it.next();

	    Object o = Model.getUmlFactory().buildNode(type);
	    String objDesc = "" + o;
	    if (o != null) {
	        objDesc += " (" + o.getClass() + ")";
	    }
	    assertTrue(
		       objDesc
		       + " is not valid namespace for the diagram",
		       action.isValidNamespace(o));
	}
    }

}
