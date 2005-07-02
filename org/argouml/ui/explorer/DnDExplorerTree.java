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

package org.argouml.ui.explorer;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.ui.TransferableModelElements;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ActionSaveDiagramToClipboard;

/**
 * This class extends the default Argo JTree with Drag and drop capabilities.<p>
 * See:
 * http://java.sun.com/j2se/1.4.2/docs/guide/dragndrop/spec/dnd1.html
 * http://java.sun.com/products/jfc/tsc/articles/dragndrop/index.html
 *
 * And it adds the 'copy to clipboard' capability for diagrams. See: <p>
 * http://java.sun.com/j2se/1.3/docs/guide/swing/KeyBindChanges.html
 *
 *
 * @author  alexb
 * @since Created on 16 April 2003
 */
public class DnDExplorerTree
    extends ExplorerTree
    implements DragGestureListener, DragSourceListener {

    private static final Logger LOG = Logger.getLogger(DnDExplorerTree.class);

    private static final String DIAGRAM_TO_CLIPBOARD_ACTION =
        "export Diagram as GIF";

    /**
     * The selected node.
     */
    private TreePath selectedTreePath;

    /**
     * dnd source.
     */
    private DragSource dragSource;

    /**
     * Creates a new instance of DnDArgoJTree.
     */
    public DnDExplorerTree() {

        super();

        this.addTreeSelectionListener(new DnDTreeSelectionListener());

        dragSource = DragSource.getDefaultDragSource();

        /* The drag gesture recognizer fires events 
         * in response to drag gestures in a component. */
        DragGestureRecognizer dgr =
	    dragSource
	        .createDefaultDragGestureRecognizer(
		    this,
		    DnDConstants.ACTION_COPY_OR_MOVE, //specifies valid actions
		    this);

        // Eliminates right mouse clicks as valid actions
        dgr.setSourceActions(dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);

        // First argument:  Component to associate the target with
        // Second argument: DropTargetListener
        new DropTarget(this, new ArgoDropTargetListener());

        KeyStroke ctrlC = KeyStroke.getKeyStroke("control C");
        this.getInputMap().put(ctrlC, DIAGRAM_TO_CLIPBOARD_ACTION);
        this.getActionMap().put(DIAGRAM_TO_CLIPBOARD_ACTION,
                new ActionSaveDiagramToClipboard());
    }

    /**
     * The drag gesture listener is notified of drag gestures by a recognizer. 
     * The typical response is to initiate a drag 
     * by invoking DragSource.startDrag().
     *
     * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
     */
    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {

        //Get the selected targets (UML ModelElements) from the TargetManager
        Collection targets = TargetManager.getInstance().getModelTargets();
        if (targets.size() < 1) return;
        LOG.debug("Drag: start transferring " + targets.size() + " targets.");
        Transferable tf = new TransferableModelElements(targets);
        
        //Select the appropriate initial cursor:
        Cursor cursor = DragSource.DefaultCopyNoDrop;
        
        //begin the drag
        dragGestureEvent.startDrag(cursor, tf, this);
    }

    private boolean isValidDrag(TreePath destinationPath, Transferable tf) {
        if (destinationPath == null) {
            LOG.debug("No valid Drag: no destination found.");
            return false;
        }
        if (selectedTreePath.isDescendant(destinationPath)) {
            LOG.debug("No valid Drag: move to descendent.");
            return false;
        }
        if (! tf.isDataFlavorSupported(
                TransferableModelElements.UML_COLLECTION_FLAVOR)) {
            LOG.debug("No valid Drag: flavor not supported.");
            return false;
        }
        Object dest = ((DefaultMutableTreeNode) destinationPath
                .getLastPathComponent()).getUserObject();
        
        /* TODO: support other types of drag. 
         * Here you set the owner by dragging into a namespace. 
         * An alternative could be to drag states into composite states... */
        
        /* If the destination is not a NameSpace, then abort: */
        if (!Model.getFacade().isANamespace(dest)) {
            LOG.debug("No valid Drag: not a namespace.");
            return false;
        }
        
        /* If the destination is a DataType, then abort: */
        if (Model.getFacade().isADataType(dest)) {
            LOG.debug("No valid Drag: destination is a DataType.");
            return false;
        }
        
        /* Let's check all dragged elements - if one of these may be dropped, 
         * then the drag is valid. The others will be ignored when dropping.*/
        Collection c;
        try {
            c = (Collection) tf.getTransferData(
                    TransferableModelElements.UML_COLLECTION_FLAVOR);
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Object me = i.next();
                if (Model.getCoreHelper().isValidNamespace(me, dest)) {
                    LOG.debug("Valid Drag: namespace " + dest);
                    return true;
                }
            }
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.debug("No valid Drag: not a valid namespace.");
        return false;
    }
    
    /**
     * The DropTargetListener. 
     * Handles drop target events including the drop itself.
     */
    class ArgoDropTargetListener implements DropTargetListener {

        public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
            LOG.debug("dragEnter");
	}

        public void dragExit(DropTargetEvent dropTargetEvent) {
            LOG.debug("dragExit");
        }

        /** 
         * Called when a drag operation is ongoing, while the mouse pointer 
         * is still over the operable part of the drop site 
         * for the <code>DropTarget</code> registered with this listener.
         */
        public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
            // LOG.debug("dragOver"); //many many of these!
            
            /* TODO: The next only works from Java 1.5 onwards :-( */
            /* Testcase: drag something from another application into ArgoUML,
             * and the explorer shows the drop icon, instead of the noDrop.*/
            /*
            Transferable tf = dropTargetDragEvent.getTransferable();
            if (tf.isDataFlavorSupported(
                     TransferableModelElements.UML_COLLECTION_FLAVOR)) {
                dropTargetDragEvent.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
            } else {
                dropTargetDragEvent.rejectDrag();
            } 
            */
        }

        /**
         * The drop: what is done when the mousebutton is released.
         */
        public void drop(DropTargetDropEvent dropTargetDropEvent) {

            LOG.debug("dropping ... ");
            try {
                Transferable tr = dropTargetDropEvent.getTransferable();
                //get new parent node
                Point loc = dropTargetDropEvent.getLocation();
                TreePath destinationPath = getPathForLocation(loc.x, loc.y);
                LOG.debug("Drop location: x=" + loc.x + " y=" + loc.y);

                if (!isValidDrag(destinationPath, tr)) {
                    dropTargetDropEvent.rejectDrop();
                    return;
                }

                //get the model elements that are being transfered.
                Collection modelElements = (Collection) tr.getTransferData(
                            TransferableModelElements.UML_COLLECTION_FLAVOR);
                LOG.debug("transfer data = " + modelElements);
                
                Object dest = ((DefaultMutableTreeNode) destinationPath
		             .getLastPathComponent()).getUserObject();

                int action = dropTargetDropEvent.getDropAction();
                /* The user-DropActions are: 
                 * Ctrl + Shift -> ACTION_LINK
                 * Ctrl         -> ACTION_COPY
                 * Shift        -> ACTION_MOVE  
                 * (none)       -> ACTION_MOVE
                 */
                boolean copyAction = (action == DnDConstants.ACTION_COPY);
                boolean moveAction = (action == DnDConstants.ACTION_MOVE);

                if(!(moveAction || copyAction)) {
                    dropTargetDropEvent.rejectDrop();
                    return;
                }
                try {
                    dropTargetDropEvent.acceptDrop(action);
                    Iterator i = modelElements.iterator();
                    while (i.hasNext()) {
                        Object me = i.next();
                        LOG.debug((moveAction ? "move " : "copy ") + me);
                        if (Model.getCoreHelper().isValidNamespace(me, dest)) {
                            Model.getCoreHelper().setNamespace(me, dest);
                        }
                    }
                    dropTargetDropEvent.getDropTargetContext()
                        .dropComplete(true);
                } catch (java.lang.IllegalStateException ils) {
                    LOG.debug("drop IllegalStateException");
                    dropTargetDropEvent.rejectDrop();
                }

                dropTargetDropEvent.getDropTargetContext().dropComplete(true);
            } catch (IOException io) {
                LOG.debug("drop IOException");
                dropTargetDropEvent.rejectDrop();
            } catch (UnsupportedFlavorException ufe) {
                LOG.debug("drop UnsupportedFlavorException");
                dropTargetDropEvent.rejectDrop();
            } catch (Exception e) {
                LOG.debug("drop Exception");
                dropTargetDropEvent.rejectDrop();
            }
        }

        /**
         * empty implementation - not used.
         */
        public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
	}

    }

    /**
     * DragSourceListener empty implementation - not used.
     *
     * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
     */
    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) { 
    }

    /**
     * DragSourceListener empty implementation - not used.
     *
     * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) { 
    }

    /**
     * DragSourceListener empty implementation - not used.
     *
     * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
     */
    public void dragExit(DragSourceEvent dragSourceEvent) { 
    }

    /**
     * Set the cursor.
     *
     * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragOver(DragSourceDragEvent dragSourceDragEvent) { 
        Transferable tf = 
            dragSourceDragEvent.getDragSourceContext().getTransferable();
        /* This is the mouse location on the screen: */
        Point dragLoc = dragSourceDragEvent.getLocation();
//        LOG.debug("Drag over location: x=" + dragLoc.x + " y=" + dragLoc.y);
        /* This is the JTree location on the screen: */
        Point treeLoc = getLocationOnScreen();
        /* Now substract to find the location within the JTree: */
        dragLoc.translate(- treeLoc.x, - treeLoc.y);
//        LOG.debug("Drag location on tree: x=" + dragLoc.x + " y=" + dragLoc.y);
        TreePath destinationPath = getPathForLocation(dragLoc.x, dragLoc.y);
         if (!isValidDrag(destinationPath, tf)) {
            dragSourceDragEvent.getDragSourceContext()
                .setCursor(DragSource.DefaultCopyNoDrop);
        } else {
            dragSourceDragEvent.getDragSourceContext()
                .setCursor(DragSource.DefaultMoveDrop);
        }

    }

    /**
     * DragSourceListener empty implementation - not used.
     *
     * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
     */
    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) { }

    /**
     * records the selected path for later use during dnd.
     */
    class DnDTreeSelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent treeSelectionEvent) {

            selectedTreePath = treeSelectionEvent.getNewLeadSelectionPath();
        }

    }
}


