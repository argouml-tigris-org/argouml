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

package org.argouml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.UndoManager;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramUtils;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.ui.ActionRemoveFromDiagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * Class to manage Project related actions which need to be (or historically
 * have been) managed as singletons.
 * 
 * TODO: It's unclear to me whether all of these actually have to be managed as
 * singletons, but for now I've just moved them from ProjectBrowser as is. - tfm
 * 
 * @author Tom Morris
 */
public final class ProjectActions
        implements TargetListener, PropertyChangeListener {

    private static ProjectActions theInstance;
    
    private ProjectActions() {
        super();
        
        undoAction = new ActionUndo(
                Translator.localize("action.undo"),
                ResourceLoaderWrapper.lookupIcon("Undo"));
        undoAction.setEnabled(false);
        
        redoAction = new ActionRedo(
                Translator.localize("action.redo"),
                ResourceLoaderWrapper.lookupIcon("Redo"));
        redoAction.setEnabled(false);
        
        TargetManager.getInstance().addTargetListener(this);
        Project p = ProjectManager.getManager().getCurrentProject();
        if (p != null) {
            p.getUndoManager().addPropertyChangeListener(this);
        }
            
    }

    /**
     * The action to undo the last user interaction.
     */
    private final ActionUndo undoAction;
    /**
     * The action to redo the last undone action.
     */
    private final AbstractAction redoAction;

    /**
     * Singleton retrieval method for the projectbrowser. Lazely instantiates
     * the projectbrowser.
     * @return the singleton instance of the projectbrowser
     */
    public static synchronized ProjectActions getInstance() {
        if (theInstance == null) {
            theInstance = new ProjectActions();
        }
        return theInstance;
    }

    /**
     * The action to remove the current selected Figs from the diagram.
     */
    private final ActionRemoveFromDiagram removeFromDiagram =
        new ActionRemoveFromDiagram(
                Translator.localize("action.remove-from-diagram"));

    /**
     * Get the action that can undo the last user interaction on this project.
     * @return the undo action.
     */
    public AbstractAction getUndoAction() {
        return undoAction;
    }

    /**
     * Get the action that can redo the last undone action.
     * @return the redo action.
     */
    public AbstractAction getRedoAction() {
        return redoAction;
    }

    /**
     * Get the action that removes selected figs from the diagram.
     * @return the remove from diagram action.
     */
    public AbstractAction getRemoveFromDiagramAction() {
        return removeFromDiagram;
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        determineRemoveEnabled();
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        determineRemoveEnabled();
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        determineRemoveEnabled();
    }
    
    /**
     * Enabled the remove action if an item is selected in anything other then
     * the activity or state diagrams.
     */
    private void determineRemoveEnabled() {
        Editor editor = Globals.curEditor();
        Collection figs = editor.getSelectionManager().getFigs();
        boolean removeEnabled = !figs.isEmpty();
        GraphModel gm = editor.getGraphModel();
        if (gm instanceof UMLMutableGraphSupport) {
            removeEnabled =
                ((UMLMutableGraphSupport) gm).isRemoveFromDiagramAllowed(figs);
        }
        removeFromDiagram.setEnabled(removeEnabled);
    }

    /**
     * Display the diagram which contains the given list of targets and scroll
     * to make them visible.
     *
     * @param targets Collection of targets to show
     */ 
     // TODO: Move to different class?
    public static void jumpToDiagramShowing(List targets) {

        if (targets == null || targets.size() == 0) {
            return;
        }
        Object first = targets.get(0);
        if (first instanceof ArgoDiagram && targets.size() > 1) {
            setTarget(first);
            setTarget(targets.get(1));
            return;
        }
        if (first instanceof ArgoDiagram && targets.size() == 1) {
            setTarget(first);
            return;
        }
        
        // TODO: This should get the containing project from the list of
        // targets, not from some global
        Project project = ProjectManager.getManager().getCurrentProject();
        if (project == null) {
            return;
        }
        
        List<ArgoDiagram> diagrams = project.getDiagramList();
        Object target = TargetManager.getInstance().getTarget();
        if ((target instanceof ArgoDiagram)
            && ((ArgoDiagram) target).countContained(targets) == targets.size()) {
            setTarget(first);
            return;
        }

        ArgoDiagram bestDiagram = null;
        int bestNumContained = 0;
        for (ArgoDiagram d : diagrams) {
            int nc = d.countContained(targets);
            if (nc > bestNumContained) {
                bestNumContained = nc;
                bestDiagram = d;
            }
            if (nc == targets.size()) {
                break;
            }
        }
        if (bestDiagram != null) {
            if (!DiagramUtils.getActiveDiagram().equals(bestDiagram)) {
                setTarget(bestDiagram);
            }
            setTarget(first);
        }
        // making it possible to jump to the modelroots
        if (project.getRoots().contains(first)) {
            setTarget(first);
        }

        // and finally, adjust the scrollbars to show the Fig
        Object f = TargetManager.getInstance().getFigTarget();
        if (f instanceof Fig) {
            Globals.curEditor().scrollToShow((Fig) f);
        }
        
    }

    private static void setTarget(Object o) {
        TargetManager.getInstance().setTarget(o);
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getSource() instanceof UndoManager) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if ("undoLabel".equals(evt.getPropertyName())) {
                        undoAction.putValue(AbstractAction.NAME, evt
                                .getNewValue());
                    }
                    if ("redoLabel".equals(evt.getPropertyName())) {
                        redoAction.putValue(AbstractAction.NAME, evt
                                .getNewValue());
                    }
                    if ("undoable".equals(evt.getPropertyName())) {
                        undoAction.setEnabled((Boolean) evt.getNewValue());
                    }
                    if ("redoable".equals(evt.getPropertyName())) {
                        redoAction.setEnabled((Boolean) evt.getNewValue());
                    }
                }
            });
        }
    }
}
