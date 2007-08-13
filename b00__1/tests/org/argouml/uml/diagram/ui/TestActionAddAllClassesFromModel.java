// $Id:TestActionAddAllClassesFromModel.java 12987 2007-07-05 14:28:59Z mvw $
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import junit.framework.TestCase;

import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;

/**
 * @author Timothy M. Lebo
 * @since November 3, 2003
 */
public class TestActionAddAllClassesFromModel extends TestCase {

    private ActionAddAllClassesFromModel action;
    private UMLClassDiagram diagram;

    /**
     * Constructor for TestActionAddAllClassesFromModel.
     * @param arg0 test case name
     */
    public TestActionAddAllClassesFromModel(String arg0) {
        super(arg0);
    }

    /**
     * @throws Exception if something goes wrong.
     */
    protected void setUp() throws Exception {
        super.setUp();
        (new InitNotation()).init();
        (new InitNotationUml()).init();
        (new InitNotationJava()).init();

        diagram = new UMLClassDiagram();
	action = new ActionAddAllClassesFromModel("Add all classes from model",
						  diagram);
    }

    /**
     * @author Timothy M. Lebo
     * @since November 3, 2003
     */
    public void testConstruction() {
	new ActionAddAllClassesFromModel("Add all classes from model",
					 diagram);
    }

    /**
     * Makes sure the option is enabled.
     *
     * ActionAddAllClassesFromModel expects to receive a
     * UMLClassDiagram in its constructor. If the Class of the class
     * diagrams in argoUML changes, this needs to change also.
     *
     * @author Timothy M. Lebo
     * @since November 3, 2003
     */
    public void testShouldBeEnabled() {
	assertTrue(action.isEnabled());
    }

    /**
     * Makes sure the option is not enabled if its diagram isn't
     * UMLClassDiagram.
     *
     * @author Timothy M. Lebo
     * @since November 3, 2003
     */
    public void testShouldNotBeEnabled() {
	action = new ActionAddAllClassesFromModel("Add all classes from model",
						   new Object());
	assertEquals(action.isEnabled(), false);
    }
}
