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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;

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

//TODO: There is a cyclic dependency between this class and FigConcurrentRegion

/**
 * Delete a concurrent region of a concurrent composite state
 * TODO: Pretty much everything done in this action does not belong here.
 * It either belongs in the model subsystem or the Figs should be listening
 * for change to the model and acting accordingly.
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
    @Override
    public void actionPerformed(ActionEvent ae) {
        final Fig f = TargetManager.getInstance().getFigTarget();
        if (!Model.getFacade().isAConcurrentRegion(f.getOwner())) {
            // This will never be true if called from the proppanel
            // If called from ActionDeleteModelElement then this
            // is very dodgy as this class assumes only when target
            // selected and ActionDeleteModelElement assumes multiple.
            return;
        }
        
        super.actionPerformed(ae);

        /*
         * Actions to delete a region. We assume the only figs enclosed in a
         * concurrent composite state are concurrent region figs.
         */

        Project p = ProjectManager.getManager().getCurrentProject();

        Fig encloser = f.getEnclosingFig();

        List<Fig> nodesInside =
            ((List<Fig>) encloser.getEnclosedFigs().clone());
        int index = nodesInside.indexOf(f);
        Rectangle r = f.getBounds();
        Rectangle encBound = encloser.getBounds();
        
        p.moveToTrash(f.getOwner());

        int height = 0;

        // Adjust the position of the remaining nodes
        // FigCompositeState should be listening for delete events
        // of its children and act accordingly.
        if (index < nodesInside.size() - 1) {
            Rectangle rFig = nodesInside.get(index + 1).getBounds();
            height = rFig.y - r.y;
            for (int i = ++index; i < nodesInside.size();  i++) {
                ((FigNodeModelElement) nodesInside.get(i)).displace(0, -height);
            }
        } else {
            height = r.height + 4;
        }

        ((FigCompositeState) encloser).setBounds(encBound.height - height);
        ((FigConcurrentRegion) (encloser.getEnclosedFigs())
    	    .elementAt(0)).setLineColor(Color.white);

        // When only one concurrent region remains it must be erased and
        // the composite state set to non-concurrent

        // TODO: The model subsystem should detect that there is only
        // one concurrent region remaining and so delete it.
        // As mentioned above, FigCompositeState should be listening
        // for delete events of its children and act accordingly.
        if ((encloser.getEnclosedFigs()).size() == 1) {
            final Fig firstEnclosed =
                ((Fig) encloser.getEnclosedFigs().elementAt(0));
            nodesInside = firstEnclosed.getEnclosedFigs();
            Model.getStateMachinesHelper().setConcurrent(
            	encloser.getOwner(), false);
            if (!nodesInside.isEmpty()) {
                for (int i = 0; i < nodesInside.size(); i++) {
                    FigStateVertex curFig =
                        (FigStateVertex) nodesInside.get(i);
                    curFig.setEnclosingFig(encloser);
                }
            }
            p.moveToTrash(firstEnclosed.getOwner());
        }
    }
}
