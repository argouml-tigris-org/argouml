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

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;

/**
 * An action that makes all edges on the selected node visible/not visible
 * on the diagram.
 *
 * @author David Manura
 * @since 0.13.5
 */
public class ActionEdgesDisplay extends UndoableAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    // compartments
    private static UndoableAction showEdges = new ActionEdgesDisplay(true,
                Translator.localize("menu.popup.add.all-relations"));
    private static UndoableAction hideEdges = new ActionEdgesDisplay(false,
                Translator.localize("menu.popup.remove.all-relations"));

    private boolean show;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     *
     * @param showEdge to show or not to show
     * @param desc the name
     */
    protected ActionEdgesDisplay(boolean showEdge, String desc) {
        super(desc, null);
		// Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, desc);
        show = showEdge;
    }


    // //////////////////////////////////////////////////////////////
    // main methods

    /*
     * TODO: Support commentEdges.
     * TODO: Support associations to self.
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
    	super.actionPerformed(ae);
        ArgoDiagram d = ProjectManager.getManager()
                .getCurrentProject().getActiveDiagram();
        Editor ce = Globals.curEditor();
        MutableGraphModel mgm = (MutableGraphModel) ce.getGraphModel();

        Enumeration e = ce.getSelectionManager().selections().elements();
        while (e.hasMoreElements()) {
            Selection sel = (Selection) e.nextElement();
            Object owner = sel.getContent().getOwner();

            if (show) { // add
                mgm.addNodeRelatedEdges(owner);
//                Collection c = Model.getFacade().getComments(owner);
//                Iterator i = c.iterator();
//                while (i.hasNext()) {
//                    Object annotatedElement = i.next();
//                    Fig f = d.presentationFor(annotatedElement);
//                    // and now what? How do I add it to the diagram?
//                }
            } else { // remove
                List edges = mgm.getInEdges(owner);
                edges.addAll(mgm.getOutEdges(owner));
                Iterator e2 = edges.iterator();
                while (e2.hasNext()) {
                    Object edge = e2.next();
                    if (Model.getFacade().isAAssociationEnd(edge)) {
                        edge = Model.getFacade().getAssociation(edge);
                    }
                    Fig fig = d.presentationFor(edge);
                    if (fig != null)
                        fig.removeFromDiagram();
                }
                //The next does not yet work for comment edges:
//                Collection c = Model.getFacade().getComments(owner);
//                Iterator i = c.iterator();
//                while (i.hasNext()) {
//                    Object annotatedElement = i.next();
//                    Fig f = d.presentationFor(annotatedElement);
//                    if (f != null) f.removeFromDiagram();
//                }
            }
        }
    }

    /**
     * @return true if the action is enabled
     * @see org.tigris.gef.undo.UndoableAction#isEnabled()
     */
    public boolean isEnabled() {
        return true;
    }


    /**
     * @return Returns the showEdges.
     */
    public static UndoableAction getShowEdges() {
        return showEdges;
    }


    /**
     * @return Returns the hideEdges.
     */
    public static UndoableAction getHideEdges() {
        return hideEdges;
    }

} /* end class ActionEdgesDisplay */



