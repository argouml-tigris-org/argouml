// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.MutableGraphSupport;
import org.tigris.gef.presentation.Connecter;
import org.tigris.gef.presentation.Fig;


/**
 * Removes an modelelement from the diagram, but not from the model.
 *
 * @stereotype singleton
 */
public class ActionDeleteFromDiagram extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    /** logger */
    private static final Logger LOG =
        Logger.getLogger(ActionDeleteFromDiagram.class);

    private static ActionDeleteFromDiagram singleton =
        new ActionDeleteFromDiagram();

    ////////////////////////////////////////////////////////////////
    // constructors

    private ActionDeleteFromDiagram() {
        super("action.remove-from-diagram", true, HAS_ICON);
        String localMnemonic =
	    Translator.localize("action.remove-from-diagram.mnemonic");
        if (localMnemonic != null && localMnemonic.length() == 1) {
            putValue(Action.MNEMONIC_KEY,
		     new Integer(localMnemonic.charAt(0)));
        }
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * Tells if this action shall be enabled or not. <p>
     *
     * Remove from diagram is not allowed when the diagram
     * is a statechart. This because the diagram = the
     * statemachine according UML. Use a submachinestate
     * to split a big diagram in parts instead.
     * And because it leads to unsolvable problems with
     * concurrency.
     *
     * @return true if it shall be enabled.
     */
    public boolean shouldBeEnabled() {
        int size = 0;
        // return false if current diagram is a statechart diagram
        Object targetP = ProjectManager.getManager()
            .getCurrentProject().getActiveDiagram();
        if ((targetP instanceof UMLStateDiagram) 
                || (targetP instanceof UMLActivityDiagram)) {
                return false;
        }
        try {
            Editor ce = Globals.curEditor();
	    if (ce == null) {
		return false;
	    }
	    if (ce.getSelectionManager() == null) {
		return false;
	    }
            Vector figs = ce.getSelectionManager().getFigs();
            size = figs.size();
        } catch (Exception e) {
            LOG.error("could not determine number of figs", e);
        }
        return super.shouldBeEnabled() && (size > 0);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        int size = 0;
        Editor ce = Globals.curEditor();
        MutableGraphSupport graph = (MutableGraphSupport) ce.getGraphModel();
        try {
            Vector figs = ce.getSelectionManager().getFigs();
            size = figs.size();
            for (int i = 0; i < size; i++) {
                Fig f = (Fig) figs.elementAt(i);
                if (!(f.getOwner() instanceof CommentEdge)) {
                    if (f instanceof Connecter) {
                        f.removeFromDiagram();
                    } else {
                        graph.removeFig(f);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error(
                ex);
        }
    }

    /**
     * @return Returns the singleton.
     */
    public static ActionDeleteFromDiagram getSingleton() {
        return singleton;
    }

}
