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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;

/**
 * Action to add a note aka comment. This action adds a Comment to 0..*
 * modelelements.
 */
public class ActionAddNote extends UMLAction {

    private static final int DISTANCE = 80;

    /**
     * The constructor. This action is not global, since it is never disabled.
     */
    public ActionAddNote() {
        super("action.new-comment", false, HAS_ICON);
        putValue(Action.SMALL_ICON, ResourceLoaderWrapper
                .lookupIconResource("New Note"));
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    private boolean containsAModelElement(Collection c) {
        Iterator i = c.iterator();
        while (i.hasNext()) {
            if (Model.getFacade().isAModelElement(i.next()))
                return true;
        }
        return false;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        Collection targets = TargetManager.getInstance().getModelTargets();

        //Let's build the comment first, unlinked.
        Diagram diagram = ProjectManager.getManager().getCurrentProject()
            .getActiveDiagram();
        Object comment = Model.getCoreFactory().buildComment(null,
                ((UMLDiagram) diagram).getNamespace());

        //Now, we link it to the modelelements which are represented by FigNode
        Object firstTarget = null;
        Iterator i = targets.iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            Fig destFig = diagram.presentationFor(obj);
            if (destFig instanceof FigEdgeModelElement) {
                FigEdgeModelElement destEdge = (FigEdgeModelElement) destFig;
                destEdge.makeCommentPort();
                destFig = destEdge.getCommentPort();
                destEdge.calcBounds();
            }
            if (Model.getFacade().isAModelElement(obj)
                    && (destFig instanceof FigNode)
                    && (!(Model.getFacade().isAComment(obj)))) {
                if (firstTarget == null) firstTarget = obj;
                /* Prevent e.g. AssociationClasses from being added trice: */
                if (!Model.getFacade().getAnnotatedElements(comment)
                        .contains(obj)) {
                    Model.getCoreHelper().addAnnotatedElement(comment, obj);
                }
            }
        }

        //Create the Node Fig for the comment itself and draw it
        ((MutableGraphModel) diagram.getGraphModel()).addNode(comment);
        Fig noteFig = diagram.presentationFor(comment); // remember the fig for later

        //Create the comment links and draw them
        MutableGraphModel mgm =
            (MutableGraphModel) Globals.curEditor().getGraphModel();

        i = Model.getFacade().getAnnotatedElements(comment).iterator();
        while (i.hasNext()) {
            Object obj = i.next();
            CommentEdge commentEdge = new CommentEdge(comment, obj);
            ((MutableGraphModel) diagram.getGraphModel()).addEdge(commentEdge);
            FigEdge fe = (FigEdge) diagram.presentationFor(commentEdge);
            FigPoly fp = (FigPoly) fe.getFig();
            fp.setComplete(true);
        }

        //Calculate the position of the comment, based on the 1st target only
        int x = 20;
        int y = 20;
        if (firstTarget != null) {
            Fig elemFig = diagram.presentationFor(firstTarget);
            if (elemFig == null)
                return;
            if (elemFig instanceof FigEdgeModelElement) {
                elemFig = ((FigEdgeModelElement) elemFig).getCommentPort();
            }
            if (elemFig instanceof FigNode) {
                // TODO: We need a better algorithm.
                x = elemFig.getX() + elemFig.getWidth() + DISTANCE;
                y = elemFig.getY();
                Rectangle drawingArea =
                    ProjectBrowser.getInstance().getEditorPane().getBounds();
                if (x + noteFig.getWidth() > drawingArea.getX()) {
                    x = elemFig.getX() - noteFig.getWidth() - DISTANCE;
                    if (x < 0) {
                        x = elemFig.getX();
                        y = elemFig.getY() - noteFig.getHeight() - DISTANCE;
                        if (y < 0) {
                            y = elemFig.getY() + elemFig.getHeight() + DISTANCE;
                            if (y + noteFig.getHeight() > drawingArea.getHeight()) {
                                x = 0;
                                y = 0;
                            }
                        }
                    }
                }
            }
        }

        //Place the comment Fig on the nicest spot on the diagram
        noteFig.setLocation(x, y);

        //Select the new comment as target
        TargetManager.getInstance().setTarget(noteFig.getOwner());
        super.actionPerformed(ae); //update all tools' enabled status
    }

} /* end class ActionAddNote */
