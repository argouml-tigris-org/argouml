// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.InputEvent;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlHelper;

/**
 * This class extends the default Argo JTree with Drag and drop capabilities.
 *
 * <p>$Id$
 *
 * @author  alexb
 * @since Created on 16 April 2003
 */
public class DnDExplorerTree 
    extends ExplorerTree 
    implements DragGestureListener, DragSourceListener {
    
    private static final Logger LOG = Logger.getLogger(DnDExplorerTree.class);
    
    /** the selected node */
    private TreePath selectedTreePath;
    
    /** dnd source */
    private DragSource dragSource;
    
    /** Creates a new instance of DnDArgoJTree */
    public DnDExplorerTree() {
        
        super();
        
        this.addTreeSelectionListener(new DnDTreeSelectionListener());
        
        dragSource = DragSource.getDefaultDragSource();
        
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
        DropTarget dropTarget =
	    new DropTarget(this, new ArgoDropTargetListener());
    }
    
    /**
     * recognises the start of the drag
     *
     * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
     */
    public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
        
        //Get the selected node from the JTree
        selectedTreePath = getSelectionPath();
        if (selectedTreePath == null) return;
        Object dragNode = ((DefaultMutableTreeNode) selectedTreePath
                                .getLastPathComponent()).getUserObject();
        if (dragNode != null) {
            
            //Get the Transferable Object
            Transferable transferable = new TransferableModelElement(dragNode);
            
            //Select the appropriate cursor;
            Cursor cursor = DragSource.DefaultCopyNoDrop;
            int action = dragGestureEvent.getDragAction();
            if (action == DnDConstants.ACTION_MOVE)
                cursor = DragSource.DefaultMoveDrop;
            
            //begin the drag
            dragSource.startDrag(dragGestureEvent, cursor, transferable, this);
        }
    }
    
    /**
     *
     */
    class ArgoDropTargetListener implements DropTargetListener {
        
        public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
            LOG.debug("dragEnter");
	}
        
        public void dragExit(DropTargetEvent dropTargetEvent) {
            LOG.debug("dragExit");
            //dropTargetEvent.getDropTargetContext().getComponent()
            //        .setCursor(Cursor.getDefaultCursor());
        }
        
        /**
         * set correct cursor.
         */
        public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
            
            // _cat.debug("dragOver");
            //set cursor location. Needed in setCursor method
            Point cursorLocationBis = dropTargetDragEvent.getLocation();
            TreePath destinationPath =
		getPathForLocation(cursorLocationBis.x, cursorLocationBis.y);
            
            // if destination path is okay accept drop...
            // can't drag to a descendant
            // there will be other rules.
            if (!selectedTreePath.isDescendant(destinationPath)) {
                dropTargetDragEvent
		    .acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
            }
            // ...otherwise reject drop
            else
            {
		dropTargetDragEvent.rejectDrag();
	    }
        }
        
        /**
         * what is done when drag is released.
         */
        public void drop(java.awt.dnd.DropTargetDropEvent dropTargetDropEvent) {
            
            LOG.debug("dropping ... ");
            try {
                Transferable tr = dropTargetDropEvent.getTransferable();
                
                //flavor not supported, reject drop
                if (!tr.isDataFlavorSupported( 
			     TransferableModelElement.ELEM_FLAVOR)) {
                    LOG.debug("! isDataFlavorSupported");
                    dropTargetDropEvent.rejectDrop();
                }
                
                //get the model element that is being transfered.
                Object modelElement =
		    tr.getTransferData(TransferableModelElement.ELEM_FLAVOR );
                
                LOG.debug("transfer data = " + modelElement);
                
                //get new parent node
                Point loc = dropTargetDropEvent.getLocation();
                TreePath destinationPath = getPathForLocation(loc.x, loc.y);
                
                final String msg = isValidDropTarget(destinationPath, 
                                                     selectedTreePath);
                if (msg != null) {
                    dropTargetDropEvent.rejectDrop();
                    
                    SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    JOptionPane.showMessageDialog(
			            null,
				    msg,
				    "Error Dialog", 
				    JOptionPane.ERROR_MESSAGE);
			}
		    });
                    // reset the cursor.
                    //dropTargetDropEvent.getDropTargetContext()
                    //  .getComponent().setCursor(Cursor.getDefaultCursor());
                    return;
                }
                
                Object destinationModelElement =
		    ((DefaultMutableTreeNode) destinationPath
		             .getLastPathComponent()).getUserObject();
                
                //get old parent node
                Object oldParentME =
		    ((DefaultMutableTreeNode) selectedTreePath.getParentPath()
		             .getLastPathComponent()).getUserObject();
                
                int action = dropTargetDropEvent.getDropAction();
                boolean copyAction = (action == DnDConstants.ACTION_COPY);
                boolean moveAction = (action == DnDConstants.ACTION_MOVE);
                
                try {
                    if (moveAction) {
                        LOG.debug("move " + modelElement);
                        ModelFacade.setNamespace(modelElement,
                                                 destinationModelElement);
                    }
                    
                    if (copyAction)
                        dropTargetDropEvent
			    .acceptDrop(DnDConstants.ACTION_COPY);
                    else
                        dropTargetDropEvent
			    .acceptDrop(DnDConstants.ACTION_MOVE);
                }
                catch (java.lang.IllegalStateException ils) {
                    LOG.debug("drop IllegalStateException");
                    dropTargetDropEvent.rejectDrop();
                }
                
                dropTargetDropEvent.getDropTargetContext().dropComplete(true);
            }
            catch (IOException io) {
                LOG.debug("drop IOException");
                dropTargetDropEvent.rejectDrop();
            }
            catch (UnsupportedFlavorException ufe) {
                LOG.debug("drop UnsupportedFlavorException");
                dropTargetDropEvent.rejectDrop();
            }
            catch (Exception e) {
                LOG.debug("drop Exception");
                dropTargetDropEvent.rejectDrop();
            }
        }
        
        /** @return a string message if dest is valid,
         *          else returns null.
         */
        private String isValidDropTarget(TreePath destinationPath,
					 TreePath sourceTreePath) {
            
            if (destinationPath == null
		|| sourceTreePath == null) {
                return null;
            }
            
            Object dest =
		((DefaultMutableTreeNode) destinationPath
		        .getLastPathComponent()).getUserObject();
            Object src =
		((DefaultMutableTreeNode) sourceTreePath
		        .getLastPathComponent()).getUserObject();
            
            boolean isValid =
		UmlHelper.getHelper().getCore().isValidNamespace(src, dest);
            
            if (isValid) {
                return null;
            } else {
                return "you can't drag there.";
	    }
        }
        
        /** empty implementation - not used */
        public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent)
	{
	}
        
    }
    
    /** 
     * DragSourceListener empty implementation - not used
     * 
     * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
     */
    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) { }
    
    /** 
     * DragSourceListener empty implementation - not used
     * 
     * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) { }
    
    /** 
     * DragSourceListener empty implementation - not used
     * 
     * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
     */
    public void dragExit(DragSourceEvent dragSourceEvent) { }
    
    /** 
     * DragSourceListener empty implementation - not used
     * 
     * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragOver(DragSourceDragEvent dragSourceDragEvent) { }
    
    /** 
     * DragSourceListener empty implementation - not used
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

/**
 * Encapsulates a UML Model element for data transfer.
 */
class TransferableModelElement implements Transferable {
    
    public static final DataFlavor ELEM_FLAVOR =
	new DataFlavor(Object.class, "UML Model Element");
    
    private static DataFlavor flavors[] = {ELEM_FLAVOR };
    
    private Object theModelElement;
    
    public TransferableModelElement(Object modelElement) {
        
        theModelElement = modelElement;
    }
    
    public Object getTransferData(java.awt.datatransfer.DataFlavor dataFlavor)
	throws UnsupportedFlavorException,
	       java.io.IOException 
    {
        
        if (dataFlavor.match(ELEM_FLAVOR)) {
            return theModelElement;
        }
        else throw new UnsupportedFlavorException(dataFlavor);
    }
    
    public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }
    
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        
        return dataFlavor.match(ELEM_FLAVOR);
    }
    
}
