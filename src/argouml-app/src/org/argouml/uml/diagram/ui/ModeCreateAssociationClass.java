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

// Copyright (c) 2006 The Regents of the University of California. All
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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.FigEdge;

/**
 * Mode to manage interaction while user is drawing the pseudo-edge that will
 * become an Association Class. The only difference from a basic edge is that
 * the completion action is extended to draw the extra associated node and edge
 * that are part of this composite figure.
 *
 * @author Bob Tarling
 */
public class ModeCreateAssociationClass extends ModeCreateAssociation {

    private static final long serialVersionUID = -8656139458297932182L;

    private static final Logger LOG =
        Logger.getLogger(ModeCreateAssociationClass.class.getName());

    private static final int DISTANCE = 80;

    public Object getMetaType() {
        return Model.getMetaTypes().getAssociationClass();
    }

    @Override
    protected void endAttached(FigEdge fe) {
        Layer lay = editor.getLayerManager().getActiveLayer();
        FigAssociationClass thisFig =
            (FigAssociationClass) lay.presentationFor(getNewEdge());
        buildParts(editor, thisFig, lay);
    }

    /**
     * Build the complex representation of an AssociationClass in the active
     * layer of the current editor. This is a convenience function which is used
     * when the pseudo-edge is added to a diagram via drag-and-drop or by using
     * the "Add to Diagram" menu item.
     *
     * @param editor
     *            the GEF editor
     * @param element
     *            the model element
     */
    public static void buildInActiveLayer(Editor editor, Object element) {
        Layer layer = editor.getLayerManager().getActiveLayer();
        FigAssociationClass thisFig =
            (FigAssociationClass) layer.presentationFor(element);
        if (thisFig != null) {
            buildParts(editor, thisFig, layer);
        }
    }

    private static void buildParts(Editor editor, FigAssociationClass thisFig,
            Layer lay) {

        thisFig.removePathItem(thisFig.getMiddleGroup());

        MutableGraphModel mutableGraphModel =
            (MutableGraphModel) editor.getGraphModel();
        mutableGraphModel.addNode(thisFig.getOwner());

        // TODO: This can't depend on ProjectBrowser.  It needs to get
        // the current drawing area from the Diagram subsystem or GEF
        Rectangle drawingArea =
            ProjectBrowser.getInstance()
                .getEditorPane().getBounds();
        // Perhaps something like the following would workd.  If not, then
        // traverse up the component hierarchy to a MultEditorPane
//        Rectangle drawingArea =
//            Globals.curEditor().getJComponent().getVisibleRect();

        thisFig.makeEdgePort();
        FigEdgePort tee = thisFig.getEdgePort();
        thisFig.calcBounds();

        int x = tee.getX();
        int y = tee.getY();

        DiagramSettings settings = ((ArgoDiagram) ((LayerPerspective) lay)
                .getDiagram()).getDiagramSettings();

        LOG.log(Level.INFO, "Creating Class box for association class");
        
        FigClassAssociationClass figNode =
            new FigClassAssociationClass(thisFig.getOwner(),
            		new Rectangle(x, y, 0, 0),
            		settings);
        y = y - DISTANCE;
        if (y < 0) {
            y = tee.getY() + figNode.getHeight() + DISTANCE;
        }
        if (x + figNode.getWidth() > drawingArea.getWidth()) {
            x = tee.getX() - DISTANCE;
        }
        figNode.setLocation(x, y);
        lay.add(figNode);

        FigEdgeAssociationClass dashedEdge =
            new FigEdgeAssociationClass(figNode, thisFig, settings);
        lay.add(dashedEdge);

        dashedEdge.damage();
        figNode.damage();
    }
}
