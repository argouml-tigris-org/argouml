/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;

/**
 * Abstract class that is the parent of all actions adding diagrams to ArgoUML.
 * The children of this class should implement createDiagram to do any specific
 * actions for creating a diagram and isValidNamespace that checks if some
 * namespace is valid to add the diagram to. <p>
 *
 * ArgoUML shall never create a diagram for a read-only modelelement.<p>
 *
 * TODO: This class should be merged with ActionNewDiagram.
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class ActionAddDiagram extends UndoableAction {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionAddDiagram.class.getName());

    /**
     * Constructor for ActionAddDiagram.
     *
     * @param s the name for this action
     */
    public ActionAddDiagram(String s) {
        super(Translator.localize(s),
                ResourceLoaderWrapper.lookupIcon(s));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION,
                Translator.localize(s));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: The project should be bound to the action when it is created?
        Project p = ProjectManager.getManager().getCurrentProject();
        Object ns = findNamespace();

        if (ns != null && isValidNamespace(ns)) {
            super.actionPerformed(e);
            DiagramSettings settings =
                p.getProjectSettings().getDefaultDiagramSettings();
            // TODO: We should really be passing the default settings to
            // the diagram factory so they get set at creation time
            ArgoDiagram diagram = createDiagram(ns, settings);

            p.addMember(diagram);
            //TODO: make the explorer listen to project member property
            //changes...  to eliminate coupling on gui.
            ExplorerEventAdaptor.getInstance().modelElementAdded(ns);
            TargetManager.getInstance().setTarget(diagram);
        } else {
            LOG.log(Level.SEVERE, "No valid namespace found");
            throw new IllegalStateException("No valid namespace found");
        }
    }

    /**
     * Find the right namespace for the diagram.
     *
     * @return the namespace or null
     */
    protected Object findNamespace() {
        Project p = ProjectManager.getManager().getCurrentProject();
        Object target = TargetManager.getInstance().getModelTarget();
        Object ns = null;
        if (target == null || !Model.getFacade().isAModelElement(target)
                || Model.getModelManagementHelper().isReadOnly(target)) {
            // get the first editable extent (which is OK unless there is more
            // than one editable extent)
            target = null;
            Iterator iter = p.getRoots().iterator();
            while (iter.hasNext()) {
                Object o = iter.next();
                if (!Model.getModelManagementHelper().isReadOnly(o)) {
                    target = o;
                    break;
                }
            }
            if (target == null) {
                // no way, we have to give up
                return null;
            }
        }
        if (Model.getFacade().isANamespace(target)) {
            ns = target;
        } else {
            Object owner = null;
            if (Model.getFacade().isAOperation(target)) {
                owner = Model.getFacade().getOwner(target);
                if (owner != null && Model.getFacade().isANamespace(owner)) {
                    ns = owner;
                }
            }
            if (ns == null && Model.getFacade().isAModelElement(target)) {
                owner = Model.getFacade().getNamespace(target);
                if (owner != null && Model.getFacade().isANamespace(owner)) {
                    ns = owner;
                }
            }
        }
        if (ns == null) {
            ns = p.getRoot();
        }
        return ns;
    }

    /**
     * Test if the given namespace is a valid namespace to add the diagram to.
     *
     * @param ns the namespace to check
     * @return Returns <code>true</code> if valid.
     */
    public abstract boolean isValidNamespace(Object ns);

    /**
     * Creates the diagram. Classes derived from this class should implement any
     * specific behaviour to create the diagram.
     *
     * @param ns The namespace the UMLDiagram should get.
     * @return UMLDiagram
     * @deprecated for 0.27.3 by tfmorris. Subclasses should override
     *             {@link #createDiagram(Object, DiagramSettings)}.  This method
     *             is no longer abstract, so implementing classes may remove it.
     */
    @Deprecated
    public ArgoDiagram createDiagram(@SuppressWarnings("unused") Object ns) {
        // Do nothing during the deprecation period, then it can be removed.
        return null;
    }

    /**
     * Create a new diagram. To be implemented by subclasses. It will become
     * abstract after 0.28 to enforce this requirement.
     *
     * @param owner owner of the diagram. May be a namespace, statemachine, or
     *            collaboration depending on the type of diagram.
     * @param settings default rendering settings for all figs in the new
     *            diagram
     * @return newly created diagram
     */
    public ArgoDiagram createDiagram(Object owner, DiagramSettings settings) {
        ArgoDiagram d = createDiagram(owner);
        d.setDiagramSettings(settings);
        return d;
    }

}
