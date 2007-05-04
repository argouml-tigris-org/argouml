// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import javax.swing.Action;
import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.ui.SetModeAction;
import org.tigris.gef.base.ModeCreatePolyEdge;


/**
 * The ActionAddAssociation class is for creating a dummy link with a
 * stimulus and a given action type. This is done in one step when a
 * new edge between two nodes is instanciated
 *
 * Created on 15 February 2003, 01:01
 *
 * @author Bob Tarling
 */
public class ActionAddAssociationRole extends SetModeAction {

    /**
     * Serial versoin generated for rev 1.14
     */
    private static final long serialVersionUID = -2842826831538374107L;

    /**
     * Construct a new ActionAddAssociationRole.
     *
     * @param aggregationKind the required aggregation for the association.
     * @param unidirectional true if this is to create a unidirectional
     *        association
     * @param name the action description
     */
    public ActionAddAssociationRole(Object aggregationKind,
                                    boolean unidirectional,
                                    String name) {
        super(ModeCreatePolyEdge.class,
              "edgeClass",
              Model.getMetaTypes().getAssociationRole(),
              name);
        modeArgs.put("aggregation", aggregationKind);
        modeArgs.put("unidirectional", Boolean.valueOf(unidirectional));
    }

    /**
     * The constructor.
     *
     * @param aggregationKind the required aggregation for the association.
     * @param unidirectional true if this is to create a unidirectional
     *        association
     * @param name the action description
     * @param iconName the name of the icon file
     */
    public ActionAddAssociationRole(Object aggregationKind,
            boolean unidirectional,
            String name,
            String iconName) {
        super(ModeCreatePolyEdge.class,
                "edgeClass",
                Model.getMetaTypes().getAssociationRole(),
                name);
        modeArgs.put("aggregation", aggregationKind);
        modeArgs.put("unidirectional", Boolean.valueOf(unidirectional));
        Icon icon = ResourceLoaderWrapper.lookupIconResource(iconName, 
                iconName);
        if (icon != null) {
            putValue(Action.SMALL_ICON, icon);
        }
    }
}
