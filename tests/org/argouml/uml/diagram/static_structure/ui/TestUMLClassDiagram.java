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

package org.argouml.uml.diagram.static_structure.ui;

import java.util.Collections;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.InitProfileSubsystem;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * General test methods for UMLClassDiagrams
 */
public class TestUMLClassDiagram extends TestCase {

    private static Object[] nodeTypes;

    /**
     * The constructor.
     * 
     * @param name
     *            the test name
     */
    public TestUMLClassDiagram(String name) {
        super(name);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
	super.setUp();
        (new InitNotation()).init();
        (new InitNotationUml()).init();
        (new InitNotationJava()).init();

        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        nodeTypes =
                new Object[] {
                        Model.getCoreFactory().createClass(),
                        Model.getCoreFactory().createComment(),
                        Model.getCoreFactory().createDataType(),
                        Model.getCoreFactory().createEnumeration(),
                        Model.getCommonBehaviorFactory().createException(),
                        Model.getCoreFactory().createInterface(),
                        Model.getModelManagementFactory().createModel(),
                        Model.getModelManagementFactory().createPackage(),
                        Model.getCommonBehaviorFactory().createSignal(),
                        Model.getExtensionMechanismsFactory()
                                .createStereotype(),
                        Model.getModelManagementFactory().createSubsystem(),
                        Model.getUseCasesFactory().createActor(),
                        Model.getUseCasesFactory().createUseCase(),
                        Model.getCommonBehaviorFactory().createObject(),
                        Model.getCommonBehaviorFactory()
                                .createComponentInstance(),
                        Model.getCommonBehaviorFactory().createNodeInstance(),
                };
    }

    /**
     * Test the UMLClassDiagram empty constructor. The graph model should always
     * be a ClassDiagramGraphModel
     */
    public void testUMLClassDiagram() {
        UMLClassDiagram diagram = new UMLClassDiagram();
        assertTrue(diagram.getGraphModel() instanceof ClassDiagramGraphModel);
    }

    /**
     * Test ClassDiagramRenderer lookup mechanism for resolving model element
     * types to a Fig which can be added to the diagram.
     */
    public void testClassDiagramRenderer() {
        UMLClassDiagram diagram = new UMLClassDiagram();
        ClassDiagramRenderer renderer = new ClassDiagramRenderer();
        GraphModel gm = new ClassDiagramGraphModel();
        for (int i = 0; i < nodeTypes.length; i++) {
            Fig fig =
                    renderer.getFigNodeFor(
                            gm, diagram.getLayer(), nodeTypes[i],
                            Collections.EMPTY_MAP);
            assertNotNull("failed to get Fig for " + nodeTypes[i], fig);
            diagram.add(fig);
        }

    }
}
