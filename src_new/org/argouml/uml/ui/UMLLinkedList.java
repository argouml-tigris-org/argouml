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

// $header$
package org.argouml.uml.ui;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
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

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;
import org.argouml.ui.TransferableModelElements;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * An UMLList2 that implements 'jump' behaviour. As soon as the user
 * doubleclicks on an element in the list, that element is selected in
 * argouml. <p>
 *
 * Also, it allows showing an icon with the text items in the list.<p>
 *
 * And, in case the listed item has no name, a default name is generated.
 *
 * It accepts a drop of a model element onto this list and attempts to
 * create a model element to connect the target of this list with the
 * dropped item.
 * 
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLLinkedList extends UMLList2 implements DropTargetListener {

    private static final Logger LOG = Logger.getLogger(UMLLinkedList.class);
    
    /**
     * Constructor for UMLLinkedList.
     *
     * @param dataModel the data model
     * @param showIcon true if an icon should be shown
     */
    public UMLLinkedList(ListModel dataModel, boolean showIcon) {
        super(dataModel, new UMLLinkedListCellRenderer(showIcon));
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setForeground(Color.blue);
        setSelectionForeground(Color.blue.darker());
        UMLLinkMouseListener mouseListener = new UMLLinkMouseListener(this);
        addMouseListener(mouseListener);
        makeDropTarget();
    }

    /**
     * The constructor.
     *
     * @param dataModel the data model
     */
    public UMLLinkedList(ListModel dataModel) {
        this(dataModel, true);
    }
    
    
    public void dragEnter(DropTargetDragEvent dtde) {
	// Do nothing
    }

    public void dragExit(DropTargetEvent dte) {
	// Do nothing
    }

    public void dragOver(DropTargetDragEvent dtde) {
	// Do nothing
    }

    public void drop(DropTargetDropEvent dropTargetDropEvent) {
	LOG.info("Dropped elements");
        ListModel model = getModel();
        if (!(model instanceof UMLModelElementListModel2)) {
            LOG.info("Drop rejected because not correct model");
            dropTargetDropEvent.rejectDrop();
            return;
        }
        UMLModelElementListModel2 listModel =
            (UMLModelElementListModel2) model;
        
        Object metaType = listModel.getMetaType();
        if (metaType == null) {
            LOG.info("Drop rejected because model has no meta type");
            dropTargetDropEvent.rejectDrop();
            return;
        }
        Transferable tr = dropTargetDropEvent.getTransferable();
        //if the flavor is not supported, then reject the drop:
        if (!tr.isDataFlavorSupported(
                     TransferableModelElements.UML_COLLECTION_FLAVOR)) {
            LOG.info(
        	    "Drop rejected because it doesn't contain model elements");
            dropTargetDropEvent.rejectDrop();
            return;
        }
        
        dropTargetDropEvent.acceptDrop(dropTargetDropEvent.getDropAction());
        
        try {
            Collection oldTargets =
                TargetManager.getInstance().getTargets();
            Collection modelElements =
                (Collection) tr.getTransferData(
                    TransferableModelElements.UML_COLLECTION_FLAVOR);
            Iterator it = modelElements.iterator();
            while (it.hasNext()) {
                if (listModel.isReverseDropConnection()) {
                    buildConnection(metaType, it.next(), getTarget());
                } else {
                    buildConnection(metaType, getTarget(), it.next());
                }
            }
            TargetManager.getInstance().setTargets(oldTargets);
            dropTargetDropEvent.getDropTargetContext().dropComplete(true);
        } catch (UnsupportedFlavorException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        }
    }
    
    private void buildConnection(
	    Object elementType, 
	    Object source, 
	    Object dest) {
        try {
	    Model.getUmlFactory().buildConnection(
	    	elementType,
	            source,
	            null,
	            dest,
	            null,
	            null,
	            Model.getFacade().getNamespace(source));
	} catch (IllegalModelElementConnectionException e) {
	    // Ignore, we expect the user to make such an error.
	}
    }
    
    public void dropActionChanged(DropTargetDragEvent dtde) {
	// Do nothing
    }

    private void makeDropTarget() {
        // TODO: This test is *solely*  to allow tests to complete without
        // error in current environment.  Tests need to be reorganize to
        // accommodate DnD (and to provide test coverage for it).
        if (!GraphicsEnvironment.isHeadless()) {
            new DropTarget(this,
                    DnDConstants.ACTION_COPY_OR_MOVE,
                    this);
        }
    }
}
