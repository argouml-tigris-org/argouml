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

package org.argouml.uml.diagram.activity.ui;

import java.util.Collections;
import java.util.List;

import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;
import org.argouml.uml.ui.PropPanelFactory;
import org.argouml.uml.ui.PropPanelFactoryManager;

/**
 * Initialise this subsystem.
 *
 * @author Michiel
 */
public class InitActivityDiagram implements InitSubsystem {

    public List<AbstractArgoJPanel> getDetailsTabs() {
        return Collections.emptyList();
    }

    public List<GUISettingsTabInterface> getProjectSettingsTabs() {
        return Collections.emptyList();
    }

    public List<GUISettingsTabInterface> getSettingsTabs() {
        return Collections.emptyList();
    }

    public void init() {
        /* Set up the property panels for activity diagrams: */
        PropPanelFactory diagramFactory = new ActivityDiagramPropPanelFactory();
        PropPanelFactoryManager.addPropPanelFactory(diagramFactory);
    }

}
