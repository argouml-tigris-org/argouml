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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.undo.UndoableAction;

/**
 * Abstract class that is the parent of all actions adding diagrams to ArgoUML.
 * The children of this class should implement createDiagram to do any specific
 * actions for creating a diagram and isValidNamespace that checks if some
 * namespace is valid to add the diagram to.
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class ActionAddDiagram extends UndoableAction {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionAddDiagram.class);

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

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
    	super.actionPerformed(e);
        Project p = ProjectManager.getManager().getCurrentProject();
        Object ns = findNamespace();

        if (ns != null && isValidNamespace(ns)) {
            super.actionPerformed(e);
            UMLDiagram diagram = createDiagram(ns);
            p.addMember(diagram);
            //TODO: make the explorer listen to project member property
            //changes...  to eliminate coupling on gui.
            ExplorerEventAdaptor.getInstance().modelElementAdded(ns);
            TargetManager.getInstance().setTarget(diagram);
        } else {
            LOG.error("No valid namespace found");
            throw new IllegalStateException("No valid namespace found");
        }
    }

    /**
     * Find the right namespace for the diagram.
     *
     * @return the namespace or null
     */
    private Object findNamespace() {
        Project p = ProjectManager.getManager().getCurrentProject();
        Object target = TargetManager.getInstance().getModelTarget();
        Object ns = null;
        if (target == null || !Model.getFacade().isAModelElement(target)) {
            target = p.getRoot();
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
     */
    public abstract UMLDiagram createDiagram(Object ns);
}
