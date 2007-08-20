// $Id:ActionDeleteConcurrentRegion.java 11455 2006-11-12 08:25:22Z linus $
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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.state.ui.FigCompositeState;
import org.argouml.uml.diagram.state.ui.FigConcurrentRegion;
import org.argouml.uml.diagram.state.ui.FigStateVertex;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;

/**
 * Delete a concurrent region of a concurrent composite state
 *
 * @author pepargouml@yahoo.es
 */
public class ActionDeleteConcurrentRegion extends UndoableAction {

    /**
     * Construct an action to delete the concurrent region of a concurrent
     * composite state.
     */
    public ActionDeleteConcurrentRegion() {
        super(Translator.localize("action.delete-concurrent-region"),
                ResourceLoaderWrapper.lookupIcon(
                        "action.delete-concurrent-region"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION,
                Translator.localize("action.delete-concurrent-region"));
    }

    /**
     * @return <code>true</code> if the action is enabled.
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        Object target = TargetManager.getInstance().getModelTarget();
        if (Model.getStateMachinesHelper().isTopState(target)) return false;
        if (Model.getFacade().isAConcurrentRegion(target)) {
            return TargetManager.getInstance().getModelTargets().size() < 2;
        }
        return false;
    }

    /**
     * @param ae The event.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);

        /*
         * Actions to delete a region. We assume the only figs enclosed in a
         * concurrent composite state are concurrent region figs.
         */
        Fig f = TargetManager.getInstance().getFigTarget();

        Project p = ProjectManager.getManager().getCurrentProject();

        if (Model.getFacade().isAConcurrentRegion(f.getOwner())) {
            Fig encloser = f.getEnclosingFig();

            Vector nodesInside = ((Vector) encloser.getEnclosedFigs().clone());
            int index = nodesInside.indexOf(f);
            Rectangle r = f.getBounds();
            Rectangle encBound = encloser.getBounds();
            if (Model.getFacade().isAConcurrentRegion(f.getOwner())) {
        	p.moveToTrash(f.getOwner());
            }

            int height = 0;

            // Adjust the position of the remaining nodes
            if (index < nodesInside.size() - 1) {
        	Rectangle rFig =
        	    ((Fig) nodesInside.elementAt(index + 1)).getBounds();
        	height = rFig.y - r.y;
        	for (int i = ++index; i < nodesInside.size();  i++) {
        	    ((FigNodeModelElement) nodesInside.elementAt(i))
        	    .displace(0, -height);
        	}
            } else {
        	height = r.height + 4;
            }

            ((FigCompositeState) encloser).setBounds(encBound.height - height);
            ((FigConcurrentRegion) (encloser.getEnclosedFigs())
        	    .elementAt(0)).setLineColor(Color.white);

            // When only one concurrent region remains it must be erased and
            // the composite state set to non-concurrent

            if ((encloser.getEnclosedFigs()).size() == 1) {
        	f = ((Fig) encloser.getEnclosedFigs().elementAt(0));
        	nodesInside = f.getEnclosedFigs();
        	Model.getStateMachinesHelper().setConcurrent(
        		encloser.getOwner(), false);
        	if (!nodesInside.isEmpty()) {
        	    for (int i = 0; i < nodesInside.size(); i++) {
        		FigStateVertex curFig =
        		    (FigStateVertex) nodesInside.elementAt(i);
        		curFig.setEnclosingFig(encloser);
        	    }
        	}
        	p.moveToTrash(f.getOwner());

            }
        }
    }
}
