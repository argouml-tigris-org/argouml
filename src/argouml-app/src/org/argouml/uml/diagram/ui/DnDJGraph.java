/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2009 The Regents of the University of California. All
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

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.kernel.Owned;
import org.argouml.ui.TransferableModelElements;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.ConnectionConstrainer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

/**
 * This is a JGraph with Drag and Drop capabilities.
 *
 * @author mvw
 */
class DnDJGraph
    extends JGraph
    implements DropTargetListener {

    private static final Logger LOG =
        Logger.getLogger(DnDJGraph.class.getName());

    /**
     * The constructor.
     *
     */
    public DnDJGraph() {
        super();
        makeDropTarget();
    }

    /**
     * The constructor.
     *
     * @param cc The ConnectionConstrainer.
     */
    public DnDJGraph(ConnectionConstrainer cc) {
        super(cc);
        makeDropTarget();
    }

    /**
     * The constructor.
     *
     * @param d The Diagram.
     */
    public DnDJGraph(Diagram d) {
        super(d);
        makeDropTarget();
    }

    /**
     * The constructor.
     *
     * @param gm The GraphModel.
     */
    public DnDJGraph(GraphModel gm) {
        super(gm);
        makeDropTarget();
    }

    /**
     * The constructor.
     *
     * @param ed The Editor.
     */
    public DnDJGraph(Editor ed) {
        super(ed);
        makeDropTarget();
    }

    private void makeDropTarget() {
        new DropTarget(this,
                DnDConstants.ACTION_COPY_OR_MOVE,
                this);
    }

    /*
     * @see java.awt.dnd.DropTargetListener#dragEnter(
     *         java.awt.dnd.DropTargetDragEvent)
     */
    public void dragEnter(DropTargetDragEvent dtde) {
    	try {
    	    if (dtde.isDataFlavorSupported(
    	            TransferableModelElements.UML_COLLECTION_FLAVOR)) {
    	        dtde.acceptDrag(dtde.getDropAction());
    	        return;
    	    }
    	} catch (NullPointerException e) {
//			System.err.println("NullPointerException ignored.");
    	}
    	dtde.rejectDrag();
    }

    /*
     * @see java.awt.dnd.DropTargetListener#dragOver(
     *         java.awt.dnd.DropTargetDragEvent)
     */
    public void dragOver(DropTargetDragEvent dtde) {
    	try {
    	    ArgoDiagram dia = DiagramUtils.getActiveDiagram();
    	    if (dia instanceof UMLDiagram
                /*&& ((UMLDiagram) dia).doesAccept(dtde.getSource())*/) {
    	        dtde.acceptDrag(dtde.getDropAction());
    	        return;
    	    }
    	    if (dtde.isDataFlavorSupported(
    	            TransferableModelElements.UML_COLLECTION_FLAVOR)) {
    	        dtde.acceptDrag(dtde.getDropAction());
    	        return;
    	    }
    	} catch (NullPointerException e) {
//    		System.err.println("NullPointerException ignored.");
    	}
    	dtde.rejectDrag();
    }

    /*
     * @see java.awt.dnd.DropTargetListener#dropActionChanged(
     *         java.awt.dnd.DropTargetDragEvent)
     */
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // ignored
    }

    /*
     * @see java.awt.dnd.DropTargetListener#dragExit(
     *         java.awt.dnd.DropTargetEvent)
     */
    public void dragExit(DropTargetEvent dte) {
        // ignored
    }

    /*
     * @see java.awt.dnd.DropTargetListener#drop(
     *         java.awt.dnd.DropTargetDropEvent)
     */
    public void drop(DropTargetDropEvent dropTargetDropEvent) {

        Transferable tr = dropTargetDropEvent.getTransferable();
        if (!tr.isDataFlavorSupported(
                     TransferableModelElements.UML_COLLECTION_FLAVOR)) {
            dropTargetDropEvent.rejectDrop();
            return;
        }

        dropTargetDropEvent.acceptDrop(dropTargetDropEvent.getDropAction());
        try {
            final UMLDiagram diagram =
                (UMLDiagram) DiagramUtils.getActiveDiagram();
            final Editor editor = Globals.curEditor();
            final Layer layer = editor.getLayerManager().getActiveLayer();
            final MutableGraphModel gm =
                (MutableGraphModel) diagram.getGraphModel();
            final Point point = dropTargetDropEvent.getLocation();
            final double scale = editor.getScale();

            int dx = getViewPosition().x;
            int dy = getViewPosition().y;
            point.translate(dx, dy);

            double xp = point.getX();
            double yp = point.getY();
            point.translate(
                    (int) Math.round((xp / scale) - point.x),
                    (int) Math.round((yp / scale) - point.y));

            //get the model elements that are being transfered.
            Collection modelElements =
                (Collection) tr.getTransferData(
                    TransferableModelElements.UML_COLLECTION_FLAVOR);

            Iterator i = modelElements.iterator();
            while (i.hasNext()) {
                /* TODO: Why not call UMLDiagram.doesAccept() first,
                 * like in ClassDiagramRenderer?  */
                final DiagramElement figNode = diagram.drop(i.next(), point);

                if (figNode != null && figNode instanceof Owned) {
                    Object owner = ((Owned) figNode).getOwner();
                    if (!gm.getNodes().contains(owner)) {
                        gm.getNodes().add(owner);
                    }

                    layer.add((Fig) figNode);
                    if (figNode instanceof FigNode && figNode instanceof Owned) {
                        gm.addNodeRelatedEdges(((Owned) figNode).getOwner());
                    }
                }
            }

            dropTargetDropEvent.getDropTargetContext().dropComplete(true);
        } catch (UnsupportedFlavorException e) {
            LOG.log(Level.SEVERE, "Exception caught", e);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Exception caught", e);
        }
    }


    /**
     * The UID.
     */
    private static final long serialVersionUID = -5753683239435014182L;
}
