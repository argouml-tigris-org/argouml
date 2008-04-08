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

package org.argouml.uml.diagram.sequence2.module;


import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.uml.diagram.ArgoDiagram;

import junit.framework.TestCase;

/**
 * Tests the TestSequenceDiagramFactory class.
 * @author penyaskito
 */
public class TestSequenceDiagramFactory extends TestCase {

    private Class sequence2DiagramClass = 
        org.argouml.uml.diagram.sequence2.ui.UMLSequenceDiagram.class;
    private Class sequenceDiagramClass = 
        org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram.class;
        
    protected void setUp() throws Exception {        
        super.setUp();
        InitializeModel.initializeDefault();
        (new InitProfileSubsystem()).init();
    }
    
    /**
     * Tests than the created diagrams are of the correct type
     * @see org.argouml.uml.diagram.sequence2.module.SequenceDiagramFactory#createDiagram(Class, Object, Object)
     */
    public void testCreateDiagram() {
        Object col = null;
        Object ns = ProjectManager.getManager().getCurrentProject().
            getCurrentNamespace();
        col = Model.getCollaborationsFactory().buildCollaboration(ns);        
        
        ArgoDiagram newDiagram = SequenceDiagramFactory.getInstance().
            createDiagram(sequence2DiagramClass, col, col);
        assertEquals(newDiagram.getClass(), sequence2DiagramClass);
        
        ArgoDiagram oldDiagram = SequenceDiagramFactory.getInstance().
            createDiagram(sequenceDiagramClass, col, col);
        assertEquals(oldDiagram.getClass(), sequenceDiagramClass);
                
    }
}
