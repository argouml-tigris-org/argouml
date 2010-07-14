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

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramSettings;

/**
 * Action to trigger creation of a new activity diagram.<p>
 * 
 * An ActivityGraph specifies the dynamics of<ul>
 * <li> a Package, or
 * <li> a Classifier (including UseCase), or
 * <li> a BehavioralFeature.
 * </ul>
 * 
 * @author michiel
 */
public class ActionActivityDiagram extends ActionNewDiagram {

    /**
     * Constructor.
     */
    public ActionActivityDiagram() {
        super("action.activity-diagram");
    }

    @Override
    protected ArgoDiagram createDiagram(Object namespace, 
            DiagramSettings settings) {
        final Object context = getContext(namespace); 
        final Object activity = 
            Model.getUmlFactory().buildNode(Model.getMetaTypes().getActivity(), context);

        return DiagramFactory.getInstance().create(
                DiagramFactory.DiagramType.Activity,
                activity, settings);
    }

    private Object getContext(Object namespace) {
        Object context = TargetManager.getInstance().getModelTarget();
        
        if (!Model.getActivityGraphsHelper().isAddingActivityGraphAllowed(
                context)
                || Model.getModelManagementHelper().isReadOnly(context)) {
            context = namespace;
        }
        return context;
    }
    
    /**
     * The UID.
     */
    private static final long serialVersionUID = -28844322376391273L;

} 
