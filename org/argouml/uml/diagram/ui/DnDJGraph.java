// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.TransferableModelElements;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.ConnectionConstrainer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.graph.presentation.JGraph;

/**
 * This is a JGraph with Drag and Drop capabilities.
 *
 * @author mvw@tigris.org
 */
class DnDJGraph
    extends JGraph
    implements DropTargetListener {
    
    private static final Logger LOG = Logger.getLogger(DnDJGraph.class);

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
        //if the flavor is not supported, then reject the drop:
        if (!tr.isDataFlavorSupported(
                     TransferableModelElements.UML_COLLECTION_FLAVOR)) {
            dropTargetDropEvent.rejectDrop();
            return;
        }

        dropTargetDropEvent.acceptDrop(dropTargetDropEvent.getDropAction());
        //get the model elements that are being transfered.
        Collection modelElements;
        MutableGraphModel gm =
            (MutableGraphModel) ProjectManager.getManager().
                getCurrentProject().getActiveDiagram().getGraphModel();
        try {
            Collection oldTargets = TargetManager.getInstance().getTargets();
            modelElements =
                (Collection) tr.getTransferData(
                    TransferableModelElements.UML_COLLECTION_FLAVOR);
            int count = 0;
            Iterator i = modelElements.iterator();
            while (i.hasNext()) {
                Object me = i.next();
                if (Model.getFacade().isAModelElement(me)) {
                    if (gm.canAddEdge(me)) {
                        gm.addEdge(me);
                        if (Model.getFacade().isAAssociationClass(me)) {
                            Layer lay =
                                getEditor().getLayerManager().getActiveLayer();
                            FigAssociationClass thisFig =
                                (FigAssociationClass) lay.presentationFor(me);
                            ModeCreateAssociationClass.buildParts(
                                    getEditor(), 
                                    thisFig, 
                                    lay);
                        }
                    } else if (gm.canAddNode(me)) {
                        AddExistingNodeCommand cmd =
                            new AddExistingNodeCommand(me, dropTargetDropEvent,
                                    count++);
                        cmd.execute();
                    }
                }
            }
            TargetManager.getInstance().setTargets(oldTargets);
            dropTargetDropEvent.getDropTargetContext().dropComplete(true);
        } catch (UnsupportedFlavorException e) {
            LOG.debug(e);
        } catch (IOException e) {
            LOG.debug(e);
        }
    }


    /**
     * The UID.
     */
    private static final long serialVersionUID = -5753683239435014182L;
}
