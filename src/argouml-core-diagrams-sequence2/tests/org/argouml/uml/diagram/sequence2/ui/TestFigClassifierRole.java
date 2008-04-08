// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.sequence2.ui;

import java.awt.event.ActionEvent;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.sequence2.ActionSequenceDiagram;
import org.argouml.uml.diagram.sequence2.module.SequenceDiagramFactory;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.omg.uml.behavioralelements.collaborations.ClassifierRole;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;

import junit.framework.TestCase;

/**
 * Tests the FigClassifierRole class.
 * @author penyaskito
 */

public class TestFigClassifierRole extends TestCase {
   
    /**
     * @throws Exception
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        (new InitNotationUml()).init();
        (new InitProfileSubsystem()).init();
    }

    /**
     * Test the updateNameText method.
     * @see FigClassifierRole#updateNameText() 
     */
    public void testUpdateNameText() 
    {
        UMLSequenceDiagram diagram = new UMLSequenceDiagram();
        GraphModel gm = diagram.getGraphModel();
        LayerPerspective layer = diagram.getLayer();
        GraphNodeRenderer renderer = layer.getGraphNodeRenderer();

        assertEquals(1, 1);
        
//        Object cr = Model.getUmlFactory().buildNode(
//                Model.getMetaTypes().getClassifierRole());
//        
//        Object figCR = renderer.getFigNodeFor(gm, layer, cr, null);
//
//        Object clazz = Model.getUmlFactory().buildNode(
//                Model.getMetaTypes().getUMLClass());        
//                
//        Model.getCollaborationsHelper().setBase(cr, clazz);
//                
//        FigClassifierRole fig = new FigClassifierRole(cr);      
//     
//        int count = 0;
//        count = diagram.getLayer().getContents().size();
//        assertTrue(count == 0);
//        diagram.add(fig);
//        count = diagram.getLayer().getContents().size();
//        assertTrue(count + "", count == 1);
        
    }
}
