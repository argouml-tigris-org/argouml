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

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.sequence2;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.sequence2.SequenceDiagramPropPanelFactory.PropPanelUMLSequenceDiagram;
import org.argouml.sequence2.diagram.UMLSequenceDiagram;
import org.argouml.uml.ui.PropPanel;

public class TestSequenceDiagramPropPanelFactory extends TestCase {

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
    }

    public void testCreatePropPanel() {
        SequenceDiagramPropPanelFactory factory = 
            new SequenceDiagramPropPanelFactory();
        PropPanel pp = null;
        pp = factory.createPropPanel(null);
        assertEquals("Created a proppanel for a null object",
                null, pp);
                
        Object o = new Object();
        pp = factory.createPropPanel(o);
        assertEquals("Created a proppanel for an object that is not "
                + "a sequence diagram.",
                null, pp);
        
        UMLSequenceDiagram seqDiagram = new UMLSequenceDiagram();
        pp = factory.createPropPanel(seqDiagram);
        assertNotNull("Null proppanel for a valid diagram", pp);
        assertEquals("Invalid proppanel",
                PropPanelUMLSequenceDiagram.class, pp.getClass());
    }

}
