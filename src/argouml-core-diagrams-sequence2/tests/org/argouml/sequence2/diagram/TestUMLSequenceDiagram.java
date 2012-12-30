/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    linus
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2008 The Regents of the University of California. All
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

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.profile.init.InitProfileSubsystem;
import org.tigris.gef.base.LayerPerspectiveMutable;

/**
 * Tests the UMLSequenceDiagram class.
 * @author penyaskito
 */
public class TestUMLSequenceDiagram extends TestCase {

    private UMLSequenceDiagram theDiagram;
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();

        theDiagram = new UMLSequenceDiagram();        
    }
    
    /**
     * Tests if the graph model is from the correct type.
     * @see UMLSequenceDiagram
     * @see SequenceDiagramRenderer 
     */
    public void testSequenceDiagramGraphModel() {
        assertEquals("Invalid graphModel", 
                SequenceDiagramGraphModel.class,
                theDiagram.getGraphModel().getClass()
                
        );
    }
    
    /**
     * Tests if the renderer is from the correct type.
     * @see UMLSequenceDiagram
     * @see SequenceDiagramRenderer
     */
    public void testSequenceDiagramRenderer() {
        assertEquals("Invalid nodes renderer",
                SequenceDiagramRenderer.class,
                theDiagram.getLayer().getGraphNodeRenderer().getClass()
                
        );
        assertEquals("Invalid edges renderer",
                SequenceDiagramRenderer.class,
                theDiagram.getLayer().
                    getGraphEdgeRenderer().getClass()                
        );
    }
    /**
     * Tests if the layer is from the correct type.
     * @see UMLSequenceDiagram
     * @see org.tigris.gef.base.LayerPerspectiveMutable
     */
    public void testSequenceDiagramLayer() {
        assertEquals("The layer must be a mutable perspective",
                LayerPerspectiveMutable.class,
                theDiagram.getLayer().getClass()               
        );
    }

}
