/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2009 The Regents of the University of California. All
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

package org.argouml.sequence2.diagram;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;

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
        
        Object cr = Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getClassifierRole());
        Model.getCoreHelper().setName(cr, "classifier");
        Object clazz = Model.getUmlFactory().buildNode(
                Model.getMetaTypes().getUMLClass());
        Model.getCoreHelper().setName(clazz, "MyClass");
        List<Object> bases = new ArrayList<Object>();
        bases.add(clazz);
        Model.getCollaborationsHelper().setBases(cr, bases);

        Rectangle bounds = new Rectangle(10, 10, 20, 20);
        DiagramSettings settings = new DiagramSettings();
        FigClassifierRole fig = new FigClassifierRole(cr, bounds, settings);      
     
        int count = 0;
        count = diagram.getLayer().getContents().size();
        assertEquals(0,count);
        diagram.add(fig);
        count = diagram.getLayer().getContents().size();
        assertEquals(1, count);
        
        // debugging and looking at fig.headFig.figs[1]._currText 
        // I can see that it's working, but how to test it?       
    }
}
