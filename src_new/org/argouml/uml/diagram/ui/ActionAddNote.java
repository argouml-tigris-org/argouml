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

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * Action to add a note.
 *
 * @stereotype singleton
 */
public class ActionAddNote extends UMLAction {

    private static final int DISTANCE = 80;

    ////////////////////////////////////////////////////////////////
    // static variables

    private static ActionAddNote singleton = new ActionAddNote();

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public ActionAddNote() {
        super("New Note", true, HAS_ICON);
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        Object target = TargetManager.getInstance().getModelTarget();

        if (target == null || !(ModelFacade.isAModelElement(target))) {
            return;
        }
        Object model = ProjectManager.getManager().getCurrentProject()
            .getModel();
        Object comment =
            Model.getCoreFactory().buildComment(target, model);

        // calculate the position of the comment
        ArgoDiagram diagram =
            ProjectManager.getManager().getCurrentProject().getActiveDiagram();
        Fig elemFig = diagram.presentationFor(target);
        if (elemFig == null)
            return;
        int x = 0;
        int y = 0;
        Rectangle drawingArea =
            ProjectBrowser.getInstance().getEditorPane().getBounds();
        FigComment fig = new FigComment(diagram.getGraphModel(), comment);
        if (elemFig instanceof FigNode) {
            x = elemFig.getX() + elemFig.getWidth() + DISTANCE;
            y = elemFig.getY();
            if (x + fig.getWidth() > drawingArea.getX()) {
                x = elemFig.getX() - fig.getWidth() - DISTANCE;
                if (x < 0) {
                    x = elemFig.getX();
                    y = elemFig.getY() - fig.getHeight() - DISTANCE;
                    if (y < 0) {
                        y = elemFig.getY() + elemFig.getHeight() + DISTANCE;
                        if (y + fig.getHeight() > drawingArea.getHeight()) {
                            Model.getUmlFactory().delete(comment);
                            return;
                        }
                    }
                }
            }
        } else if (elemFig instanceof FigEdge) {
            /* We cannot attach a Comment to an Edge yet since we have to
             * modify all our edges probably */
            /*
	      Point startPoint = new Point(elemFig.getX(), elemFig.getY());
	      Point endPoint = new Point(elemFig.getX() + elemFig.getWidth(),
	          elemFig.getY() + elemFig.getHeight());
            */
            Model.getUmlFactory().delete(comment);
            return;
        }
        fig.setLocation(x, y);
        Layer lay = diagram.getLayer();
        lay.add(fig);
        FigEdgeNote edge = new FigEdgeNote(target, comment);
        lay.add(edge);
        lay.sendToBack(edge);
        edge.damage();
        fig.damage();
        elemFig.damage();

        super.actionPerformed(ae);
        TargetManager.getInstance().setTarget(fig.getOwner());
    }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Object target = TargetManager.getInstance().getModelTarget();

        if (ProjectManager.getManager().getCurrentProject().getActiveDiagram()
            == null)
            return false;

        return super.shouldBeEnabled()
            && (ModelFacade.isAModelElement(target))
            && (!(ModelFacade.isAComment(target)))
            && (ProjectManager.getManager().getCurrentProject()
                .getActiveDiagram().presentationFor(target)
                instanceof FigNode);
    }

    /**
     * @return Returns the singleton.
     */
    public static ActionAddNote getSingleton() {
        return singleton;
    }
} /* end class ActionAddNote */
