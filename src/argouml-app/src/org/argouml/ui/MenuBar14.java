/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996,2009 The Regents of the University of California. All
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

package org.argouml.ui;

import javax.swing.JMenuItem;

import org.argouml.ui.cmd.GenericArgoMenuBar;
import org.argouml.ui.cmd.ShortcutMgr;
import org.argouml.uml.ui.ActionActivityDiagram;
import org.argouml.uml.ui.ActionClassDiagram;
import org.argouml.uml.ui.ActionCollaborationDiagram;
import org.argouml.uml.ui.ActionDeploymentDiagram;
import org.argouml.uml.ui.ActionSequenceDiagram;
import org.argouml.uml.ui.ActionStateDiagram;
import org.argouml.uml.ui.ActionUseCaseDiagram;

/**
 * The Menu and Toolbar for UML1.4
 */
public class MenuBar14 extends GenericArgoMenuBar {
    
    /**
     * Build the menu "Create" and the toolbar for diagram creation. These are
     * build together to guarantee that the same items are present in both, and
     * in the same sequence.
     * <p>
     * 
     * The sequence of these items was determined by issue 1821.
     */
    protected void initMenuCreate() {
        super.initMenuCreate();
        getCreateDiagramMenu().removeAll();
        getCreateDiagramToolbar().removeAll();
        JMenuItem usecaseDiagram = getCreateDiagramMenu()
                .add(new ActionUseCaseDiagram());
        setMnemonic(usecaseDiagram, "Usecase Diagram");
        getCreateDiagramToolbar().add((new ActionUseCaseDiagram()));
        ShortcutMgr.assignAccelerator(usecaseDiagram,
                ShortcutMgr.ACTION_USE_CASE_DIAGRAM);

        JMenuItem classDiagram =
            getCreateDiagramMenu().add(new ActionClassDiagram());
        setMnemonic(classDiagram, "Class Diagram");
        getCreateDiagramToolbar().add((new ActionClassDiagram()));
        ShortcutMgr.assignAccelerator(classDiagram,
                ShortcutMgr.ACTION_CLASS_DIAGRAM);

        JMenuItem sequenzDiagram =
            getCreateDiagramMenu().add(new ActionSequenceDiagram());
        setMnemonic(sequenzDiagram, "Sequenz Diagram");
        getCreateDiagramToolbar().add((new ActionSequenceDiagram()));
        ShortcutMgr.assignAccelerator(sequenzDiagram,
                ShortcutMgr.ACTION_SEQUENCE_DIAGRAM);
        
        JMenuItem collaborationDiagram =
            getCreateDiagramMenu().add(new ActionCollaborationDiagram());
        setMnemonic(collaborationDiagram, "Collaboration Diagram");
        getCreateDiagramToolbar().add((new ActionCollaborationDiagram()));
        ShortcutMgr.assignAccelerator(collaborationDiagram,
                ShortcutMgr.ACTION_COLLABORATION_DIAGRAM);

        JMenuItem stateDiagram =
            getCreateDiagramMenu().add(new ActionStateDiagram());
        setMnemonic(stateDiagram, "State Diagram");
        getCreateDiagramToolbar().add((new ActionStateDiagram()));
        ShortcutMgr.assignAccelerator(stateDiagram,
                ShortcutMgr.ACTION_STATE_DIAGRAM);

        JMenuItem activityDiagram =
            getCreateDiagramMenu().add(new ActionActivityDiagram());
        setMnemonic(activityDiagram, "Activity Diagram");
        getCreateDiagramToolbar().add((new ActionActivityDiagram()));
        ShortcutMgr.assignAccelerator(activityDiagram,
                ShortcutMgr.ACTION_ACTIVITY_DIAGRAM);

        JMenuItem deploymentDiagram =
            getCreateDiagramMenu().add(new ActionDeploymentDiagram());
        setMnemonic(deploymentDiagram, "Deployment Diagram");
        getCreateDiagramToolbar().add((new ActionDeploymentDiagram()));
        ShortcutMgr.assignAccelerator(deploymentDiagram,
                ShortcutMgr.ACTION_DEPLOYMENT_DIAGRAM);

        getCreateDiagramToolbar().setFloatable(true);
    }
}
