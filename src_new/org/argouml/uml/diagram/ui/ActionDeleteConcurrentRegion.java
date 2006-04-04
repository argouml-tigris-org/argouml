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
import java.util.Vector;

import javax.swing.Action;

import org.apache.log4j.Logger;
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

    ////////////////////////////////////////////////////////////////
    // static variables

	/** logger */
    private static final Logger LOG =
        Logger.getLogger(ActionDeleteConcurrentRegion.class);

    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionDeleteConcurrentRegion() {
        super(Translator.localize("action.delete-concurrent-region"),
                ResourceLoaderWrapper.lookupIcon("action.delete-concurrent-region"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.delete-concurrent-region"));
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
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
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        try {
            /*Here the actions to delete a region.
            We assume the only figs enclosed in a concurrent composite state
            are concurrent region figs*/
            Fig f = (Fig) TargetManager.getInstance().getFigTarget();
            Fig encloser = null;

            int height = 0;
            Rectangle encBound;
            Project p = ProjectManager.getManager().getCurrentProject();

            if (Model.getFacade().isAConcurrentRegion(f.getOwner()))
                encloser = f.getEnclosingFig();

            Vector nodesInside;
            nodesInside = ((Vector) encloser.getEnclosedFigs().clone());
            int index = nodesInside.indexOf(f);
            Rectangle r = f.getBounds();
            encBound = encloser.getBounds();
            if (Model.getFacade().isAConcurrentRegion(f.getOwner()))
                p.moveToTrash(f.getOwner());
            //It wasnt the last region
            if (index < nodesInside.size() - 1) {
                Rectangle rFig =
                    ((Fig) nodesInside.elementAt(index + 1)).getBounds();
                height = rFig.y - r.y;
                for (int i = ++index; i < nodesInside.size();  i++)
                    ((FigNodeModelElement) nodesInside.elementAt(i))
                        .displace(0, -height);
            }
            //It was the last region
            else
                height = r.height + 4;

            ((FigCompositeState) encloser).setBounds(encBound.height - height);
            ((FigConcurrentRegion) ((Vector) encloser.getEnclosedFigs())
                    .elementAt(0)).setLineColor(Color.white);

            /*When only one concurrent region remains it must be erased and the
              composite state sets non concurent*/
            if (((Vector) encloser.getEnclosedFigs()).size() == 1) {
                f = ((Fig) encloser.getEnclosedFigs().elementAt(0));
                nodesInside = ((Vector) f.getEnclosedFigs());
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



        } catch (Exception ex) {
            LOG.error(
                ex);
        }
    }

}
