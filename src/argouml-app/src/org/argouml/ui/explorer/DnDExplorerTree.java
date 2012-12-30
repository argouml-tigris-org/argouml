/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.argouml.model.Model;
import org.argouml.ui.TransferableModelElements;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.Relocatable;
import org.argouml.uml.diagram.ui.ActionSaveDiagramToClipboard;

/**
 * This class extends the default Argo JTree with Drag and drop capabilities.<p>
 * See <a
 * href="http://java.sun.com/j2se/1.4.2/docs/guide/dragndrop/spec/dnd1.html">
 * dnd1</a> and <a
 * href="http://java.sun.com/products/jfc/tsc/articles/dragndrop/index.html">
 * dnd2</a><p>
 *
 * And it adds the 'copy to clipboard' capability for diagrams. See
 * <a href="http://java.sun.com/j2se/1.3/docs/guide/swing/KeyBindChanges.html">
 * KeyBindChanges</a><p>
 *
 * The ghosted images code originates from <p><a
 * href="http://www.javaworld.com/javaworld/javatips/jw-javatip114.html">
 * javatip114</a><p>
 *
 * Interesting may also be the following: <p><a
 * href="http://forum.java.sun.com/thread.jspa?threadID=296255&start=30">
 * thread</a>
 *
 * @author  alexb
 * @since Created on 16 April 2003
 */
public class DnDExplorerTree
    extends ExplorerTree
    implements DragGestureListener,
        DragSourceListener,
        Autoscroll {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(DnDExplorerTree.class.getName());

    private static final String DIAGRAM_TO_CLIPBOARD_ACTION =
        "export Diagram as GIF";

    /**
     * Where, in the drag image, the mouse was clicked.
     */
    private Point	clickOffset = new Point();

    /**
     * The path being dragged.
     */
    private TreePath		sourcePath;

    /**
     * The 'drag image'.
     */
    private BufferedImage	ghostImage;


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

        /*
         * The drag gesture recognizer fires events
         * in response to drag gestures in a component.
         */
        DragGestureRecognizer dgr =
	    dragSource
	        .createDefaultDragGestureRecognizer(
		    this,
		    DnDConstants.ACTION_COPY_OR_MOVE, //specifies valid actions
		    this);

        // Eliminates right mouse clicks as valid actions
        dgr.setSourceActions(
        		dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);

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
     * The typical response is to initiate a drag by invoking
     * DragSource.startDrag().
     * <p>
     *
     * TODO: find a way to show a different image when multiple elements are
     * dragged.
     *
     * @param dragGestureEvent
     *            the DragGestureEvent describing the gesture that has just
     *            occurred
     * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
     */
    public void dragGestureRecognized(
            DragGestureEvent dragGestureEvent) {

        /*
         * Get the selected targets (UML ModelElements)
         * from the TargetManager.
         */
        Collection targets = TargetManager.getInstance().getModelTargets();
        if (targets.size() < 1) {
            return;
        }

        LOG.log(Level.FINE, "Drag: start transferring {0} targets.", targets.size());

        TransferableModelElements tf =
            new TransferableModelElements(targets);

        Point ptDragOrigin = dragGestureEvent.getDragOrigin();
        TreePath path =
            getPathForLocation(ptDragOrigin.x, ptDragOrigin.y);
        if (path == null) {
            return;
        }
        Rectangle raPath = getPathBounds(path);
        clickOffset.setLocation(ptDragOrigin.x - raPath.x,
                ptDragOrigin.y - raPath.y);

        /*
         * Get the cell renderer (which is a JLabel)
         * for the path being dragged.
         */
        JLabel lbl =
            (JLabel) getCellRenderer().getTreeCellRendererComponent(
                    this,                        // tree
                    path.getLastPathComponent(), // value
                    false,	// isSelected	(dont want a colored background)
                    isExpanded(path), 		 // isExpanded
                    getModel().isLeaf(path.getLastPathComponent()), // isLeaf
                    0, 		// row	(not important for rendering)
                    false	// hasFocus (dont want a focus rectangle)
            );
        /* The layout manager would normally do this: */
        lbl.setSize((int) raPath.getWidth(), (int) raPath.getHeight());

        // Get a buffered image of the selection for dragging a ghost image
        ghostImage =
            new BufferedImage(
                (int) raPath.getWidth(), (int) raPath.getHeight(),
                BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g2 = ghostImage.createGraphics();

        /*
         * Ask the cell renderer to paint itself into the BufferedImage.
         * Make the image ghostlike.
         */
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC, 0.5f));
        lbl.paint(g2);

        /*
         * Now paint a gradient UNDER the ghosted JLabel text
         * (but not under the icon if any).
         */
        Icon icon = lbl.getIcon();
        int nStartOfText =
            (icon == null) ? 0
                : icon.getIconWidth() + lbl.getIconTextGap();
        /* Make the gradient ghostlike: */
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.DST_OVER, 0.5f));
        g2.setPaint(new GradientPaint(nStartOfText,	0,
                SystemColor.controlShadow,
                getWidth(), 0, new Color(255, 255, 255, 0)));
        g2.fillRect(nStartOfText, 0, getWidth(), ghostImage.getHeight());

        g2.dispose();

        /*
         * Remember the path being dragged (because if it is being moved,
         * we will have to delete it later).
         */
        sourcePath = path;

        /*
         * We pass our drag image just in case
         * it IS supported by the platform.
         */
        dragGestureEvent.startDrag(null, ghostImage,
                new Point(5, 5), tf, this);
    }

    private boolean isValidDrag(TreePath destinationPath,
    		Transferable tf) {
        if (destinationPath == null) {
            LOG.log(Level.FINE, "No valid Drag: no destination found.");
            return false;
        }
        if (selectedTreePath.isDescendant(destinationPath)) {
            LOG.log(Level.FINE, "No valid Drag: move to descendent.");
            return false;
        }
        if (!tf.isDataFlavorSupported(
                TransferableModelElements.UML_COLLECTION_FLAVOR)) {
            LOG.log(Level.FINE, "No valid Drag: flavor not supported.");
            return false;
        }
        Object dest =
            ((DefaultMutableTreeNode) destinationPath
                .getLastPathComponent()).getUserObject();

        /* TODO: support other types of drag.
         * Here you set the owner by dragging into a namespace.
         * An alternative could be to drag states into composite states...
         */

        /* If the destination is not a NameSpace, then abort: */
        if (!Model.getFacade().isANamespace(dest)) {
            LOG.log(Level.FINE, "No valid Drag: not a namespace.");
            return false;
        }

        /* We are sure "dest" is a Namespace now. */
        if (Model.getModelManagementHelper().isReadOnly(dest)) {
            LOG.log(Level.FINE, "No valid Drag: "
                    + "this is not an editable UML element (profile?).");
            return false;
        }

        /* If the destination is a DataType, then abort: */

        // TODO: Any Namespace can contain other elements.  Why don't we allow
        // this? - tfm
        /*
         * MVW: These are the WFRs for DataType:
         * [1] A DataType can only contain Operations,
         * which all must be queries.
         * self.allFeatures->forAll(f |
         *  f.oclIsKindOf(Operation) and f.oclAsType(Operation).isQuery)
         * [2] A DataType cannot contain any other ModelElements.
         *  self.allContents->isEmpty
         *  IMHO we should enforce these WFRs here.
         *  ... so it is still possible to copy or move query operations,
         *  hence we should allow this.
         */
        if (Model.getFacade().isADataType(dest)) {
            LOG.log(Level.FINE, "No valid Drag: destination is a DataType.");
            return false;
        }

        /*
         * Let's check all dragged elements - if one of these
         * may be dropped, then the drag is valid.
         * The others will be ignored when dropping.
         */
        try {
            Collection transfers =
                (Collection) tf.getTransferData(
                    TransferableModelElements.UML_COLLECTION_FLAVOR);
            for (Object element : transfers) {
                if (Model.getFacade().isAUMLElement(element)) {
                    if (!Model.getModelManagementHelper().isReadOnly(element)) {
                        if (Model.getFacade().isAModelElement(dest)
                                && Model.getFacade().isANamespace(element)
                                && Model.getCoreHelper().isValidNamespace(
                                        element, dest)) {
                            LOG.log(Level.FINE, "Valid Drag: namespace {0}", dest);
                            return true;
                        }
                        if (Model.getFacade().isAFeature(element)
                                && Model.getFacade().isAClassifier(dest)) {
                            return true;
                        }
                    }
                }
                if (element instanceof Relocatable) {
                    Relocatable d = (Relocatable) element;
                    if (d.isRelocationAllowed(dest)) {
                        LOG.log(Level.FINE, "Valid Drag: diagram {0}", dest);
                        return true;
                    }
                }
            }
        } catch (UnsupportedFlavorException e) {
            LOG.log(Level.FINE, "", e);
        } catch (IOException e) {
            LOG.log(Level.FINE, "", e);
        }
        LOG.log(Level.FINE, "No valid Drag: not a valid namespace.");
        return false;
    }

    /*
     * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
     */
    public void dragDropEnd(
    		DragSourceDropEvent dragSourceDropEvent) {
        sourcePath = null;
        ghostImage = null;
    }

    /*
     * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
        // empty implementation - not used.
    }

    /*
     * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
     */
    public void dragExit(DragSourceEvent dragSourceEvent) {
        // empty implementation - not used.
    }

    /*
     * This is not the correct location to set the cursor.
     * The commented out code illustrates the calculation
     * of coordinates.
     *
     * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
     */
    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
//        Transferable tf =
//            dragSourceDragEvent.getDragSourceContext().getTransferable();
//        /* This is the mouse location on the screen: */
//        Point dragLoc = dragSourceDragEvent.getLocation();
//        /* This is the JTree location on the screen: */
//        Point treeLoc = getLocationOnScreen();
//        /* Now substract to find the location within the JTree: */
//        dragLoc.translate(- treeLoc.x, - treeLoc.y);
//        TreePath destinationPath =
//        	getPathForLocation(dragLoc.x, dragLoc.y);
//         if (isValidDrag(destinationPath, tf)) {
////           dragSourceDragEvent.getDragSourceContext()
////           .setCursor(DragSource.DefaultMoveDrop);
//        } else {
////          dragSourceDragEvent.getDragSourceContext()
////          .setCursor(DragSource.DefaultCopyNoDrop);
//        }
    }

    /*
     * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
     */
    public void dropActionChanged(
    		DragSourceDragEvent dragSourceDragEvent) {
        // empty implementation - not used.
    }

    /**
     * records the selected path for later use during dnd.
     */
    class DnDTreeSelectionListener implements TreeSelectionListener {
        public void valueChanged(
        		TreeSelectionEvent treeSelectionEvent) {
            selectedTreePath = treeSelectionEvent.getNewLeadSelectionPath();
        }
    }


//  Autoscroll Interface...
//  The following code was borrowed from the book:
// 		Java Swing
// 		By Robert Eckstein, Marc Loy & Dave Wood
// 		Paperback - 1221 pages 1 Ed edition (September 1998)
// 		O'Reilly & Associates; ISBN: 156592455X
 //
//  The relevant chapter of which can be found at:
// 		http://www.oreilly.com/catalog/jswing/chapter/dnd.beta.pdf

    private static final int AUTOSCROLL_MARGIN = 12;

    /*
     * Ok, we've been told to scroll because the mouse cursor is in our
     * scroll zone.
     * @see java.awt.dnd.Autoscroll#autoscroll(java.awt.Point)
     */
    public void autoscroll(Point pt) {
        // Figure out which row we're on.
        int nRow = getRowForLocation(pt.x, pt.y);

        // If we are not on a row then ignore this autoscroll request
        if (nRow < 0) {
            return;
        }

        Rectangle raOuter = getBounds();
        // Now decide if the row is at the top of the screen or at the
        // bottom. We do this to make the previous row (or the next
        // row) visible as appropriate. If were at the absolute top or
        // bottom, just return the first or last row respectively.

        // Is row at top of screen?
        nRow =
            (pt.y + raOuter.y <= AUTOSCROLL_MARGIN)
                ?
                // Yes, scroll up one row
                (nRow <= 0 ? 0 : nRow - 1)
                :
                    // No, scroll down one row
                    (nRow < getRowCount() - 1 ? nRow + 1 : nRow);

        scrollRowToVisible(nRow);
    }

    /*
     * Calculate the insets for the *JTREE*, not the viewport the tree is in.
     * This makes it a bit messy.
     *
     * @see java.awt.dnd.Autoscroll#getAutoscrollInsets()
     */
    public Insets getAutoscrollInsets() {
        Rectangle raOuter = getBounds();
        Rectangle raInner = getParent().getBounds();
        return new Insets(
                raInner.y - raOuter.y + AUTOSCROLL_MARGIN,
                raInner.x - raOuter.x + AUTOSCROLL_MARGIN,
                raOuter.height - raInner.height
                - raInner.y + raOuter.y + AUTOSCROLL_MARGIN,
                raOuter.width - raInner.width
                - raInner.x + raOuter.x + AUTOSCROLL_MARGIN);
    }

    /**
     * The DropTargetListener.
     * Handles drop target events including the drop itself.
     */
    class ArgoDropTargetListener implements DropTargetListener {

        private TreePath	 lastPath;
        private Rectangle2D cueLine = new Rectangle2D.Float();
        private Rectangle2D ghostRectangle = new Rectangle2D.Float();
        private Color cueLineColor;
        private Point lastMouseLocation = new Point();
        private Timer hoverTimer;

        /**
         * The constructor.
         */
        public ArgoDropTargetListener() {
            cueLineColor =
                new Color(
                    SystemColor.controlShadow.getRed(),
                    SystemColor.controlShadow.getGreen(),
                    SystemColor.controlShadow.getBlue(),
                    64
                );

            /* Set up a hover timer, so that a node will be
             * automatically expanded or collapsed
             * if the user lingers on it for more than a short time.
             */
            hoverTimer =
                new Timer(1000, new ActionListener() {
                    /*
                     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
                     */
                    public void actionPerformed(ActionEvent e) {
                        if (getPathForRow(0).equals/*isRootPath*/(lastPath)) {
                            return;
                        }
                        if (isExpanded(lastPath)) {
                            collapsePath(lastPath);
                        } else {
                            expandPath(lastPath);
                        }
                    }
                });
            hoverTimer.setRepeats(false);	// Set timer to one-shot mode
        }

        /*
         * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
         */
        public void dragEnter(
                DropTargetDragEvent dropTargetDragEvent) {
            LOG.log(Level.FINE, "dragEnter");
            if (!isDragAcceptable(dropTargetDragEvent)) {
                dropTargetDragEvent.rejectDrag();
            } else {
                dropTargetDragEvent.acceptDrag(
                        dropTargetDragEvent.getDropAction());
            }
        }

        /*
         * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
         */
        public void dragExit(DropTargetEvent dropTargetEvent) {
            LOG.log(Level.FINE, "dragExit");
            if (!DragSource.isDragImageSupported()) {
                repaint(ghostRectangle.getBounds());
            }
        }

        /**
         * Called when a drag operation is ongoing, while the mouse pointer
         * is still over the operable part of the drop site
         * for the <code>DropTarget</code> registered with this listener.
         *
         * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
         */
        public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
            Point pt = dropTargetDragEvent.getLocation();
            if (pt.equals(lastMouseLocation)) {
                return;
            }
            /* Many many of these events .. this slows things down: */
//            LOG.log(Level.FINE, "dragOver");

            lastMouseLocation = pt;

            Graphics2D g2 = (Graphics2D) getGraphics();

            /*
             * The next condition becomes false when dragging in
             * something from another application.
             */
            if (ghostImage != null) {
                /*
                 * If a drag image is not supported by the platform,
                 * then draw my own drag image.
                 */
                if (!DragSource.isDragImageSupported()) {
                    /* Rub out the last ghost image and cue line: */
                    paintImmediately(ghostRectangle.getBounds());
                    /* And remember where we are about to draw
                     * the new ghost image:
                     */
                    ghostRectangle.setRect(pt.x - clickOffset.x,
                            pt.y - clickOffset.y,
                            ghostImage.getWidth(),
                            ghostImage.getHeight());
                    g2.drawImage(ghostImage,
                            AffineTransform.getTranslateInstance(
                                    ghostRectangle.getX(),
                                    ghostRectangle.getY()), null);
                } else {
                    // Just rub out the last cue line
                    paintImmediately(cueLine.getBounds());
                }
            }

            TreePath path = getPathForLocation(pt.x, pt.y);
            if (!(path == lastPath)) {
                lastPath = path;
                hoverTimer.restart();
            }

            /*
             * In any case draw (over the ghost image if necessary)
             * a cue line indicating where a drop will occur.
             */
            Rectangle raPath = getPathBounds(path);
            if (raPath != null) {
                cueLine.setRect(0,
                        raPath.y + (int) raPath.getHeight(),
                        getWidth(),
                        2);
            }

            g2.setColor(cueLineColor);
            g2.fill(cueLine);

            // And include the cue line in the area to be rubbed out next time
            ghostRectangle = ghostRectangle.createUnion(cueLine);

            /* Testcase: drag something from another
             * application into ArgoUML,
             * and the explorer shows the drop icon, instead of the noDrop.
             */
            try {
                if (!dropTargetDragEvent.isDataFlavorSupported(
                        TransferableModelElements.UML_COLLECTION_FLAVOR)) {
                    dropTargetDragEvent.rejectDrag();
                    return;
                }
            } catch (NullPointerException e) {
                dropTargetDragEvent.rejectDrag();
                return;
            }

            if (path == null) {
                dropTargetDragEvent.rejectDrag();
                return;
            }
            // to prohibit dropping onto the drag source:
            if (path.equals(sourcePath)) {
                dropTargetDragEvent.rejectDrag();
                return;
            }
            if (selectedTreePath.isDescendant(path)) {
                dropTargetDragEvent.rejectDrag();
                return;
            }

            Object dest =
                ((DefaultMutableTreeNode) path
                    .getLastPathComponent()).getUserObject();

            /* If the destination is not a NameSpace, then reject: */
            if (!Model.getFacade().isANamespace(dest)) {
                if (LOG.isLoggable(Level.FINE)) {
                    String name;
                    if (Model.getFacade().isAUMLElement(dest)) {
                        name = Model.getFacade().getName(dest);
                    } else if (dest == null) {
                        name = "<null>";
                    } else {
                        name = dest.toString();
                    }
                    LOG.log(Level.FINE, "No valid Drag: "
                            + (Model.getFacade().isAUMLElement(dest)
                                    ? name + " not a namespace."
                                    :  " not a UML element."));
                }
                dropTargetDragEvent.rejectDrag();
                return;
            }
            /* We are sure "dest" is a Namespace now. */

            if (Model.getModelManagementHelper().isReadOnly(dest)) {
                LOG.log(Level.FINE, "No valid Drag: "
                        + "not an editable UML element (profile?).");
                return;
            }

            /* If the destination is a DataType, then reject: */
            if (Model.getFacade().isADataType(dest)) {
                LOG.log(Level.FINE, "No valid Drag: destination is a DataType.");
                dropTargetDragEvent.rejectDrag();
                return;
            }

            dropTargetDragEvent.acceptDrag(
                    dropTargetDragEvent.getDropAction());
        }

        /**
         * The drop: what is done when the mousebutton is released.
         *
         * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
         */
        public void drop(DropTargetDropEvent dropTargetDropEvent) {
            LOG.log(Level.FINE, "dropping ... ");
            /* Prevent hover timer from doing an unwanted
             * expandPath or collapsePath:
             */
            hoverTimer.stop();

            /* Clear the ghost image: */
            repaint(ghostRectangle.getBounds());

            if (!isDropAcceptable(dropTargetDropEvent)) {
                dropTargetDropEvent.rejectDrop();
                return;
            }

            try {
                Transferable tr = dropTargetDropEvent.getTransferable();
                //get new parent node
                Point loc = dropTargetDropEvent.getLocation();
                TreePath destinationPath = getPathForLocation(loc.x, loc.y);
                
                LOG.log(Level.FINE, "Drop location: x={0} y= {1}" , new Object[]{loc.x,loc.y});

                if (!isValidDrag(destinationPath, tr)) {
                    dropTargetDropEvent.rejectDrop();
                    return;
                }

                //get the model elements that are being transfered.
                Collection modelElements =
                    (Collection) tr.getTransferData(
                        TransferableModelElements.UML_COLLECTION_FLAVOR);
                
                LOG.log(Level.FINE, "transfer data = {0}", modelElements);

                Object dest =
                    ((DefaultMutableTreeNode) destinationPath
                        .getLastPathComponent()).getUserObject();
                Object src =
                    ((DefaultMutableTreeNode) sourcePath
                        .getLastPathComponent()).getUserObject();

                int action = dropTargetDropEvent.getDropAction();
                /* The user-DropActions are:
                 * Ctrl + Shift -> ACTION_LINK
                 * Ctrl         -> ACTION_COPY
                 * Shift        -> ACTION_MOVE
                 * (none)       -> ACTION_MOVE
                 */
                boolean copyAction =
                    (action == DnDConstants.ACTION_COPY);
                boolean moveAction =
                    (action == DnDConstants.ACTION_MOVE);

                if (!(moveAction || copyAction)) {
                    dropTargetDropEvent.rejectDrop();
                    return;
                }

                if (Model.getFacade().isAUMLElement(dest)) {
                    if (Model.getModelManagementHelper().isReadOnly(dest)) {
                        dropTargetDropEvent.rejectDrop();
                        return;
                    }
                }
                if (Model.getFacade().isAUMLElement(src)) {
                    if (Model.getModelManagementHelper().isReadOnly(src)) {
                        dropTargetDropEvent.rejectDrop();
                        return;
                    }
                }

                // TODO: Really should be Element/ModelElement, but we don't
                // have a type which is portable for this
                Collection<Object> newTargets = new ArrayList<Object>();
                try {
                    dropTargetDropEvent.acceptDrop(action);
                    for (Object me : modelElements) {
                        if (Model.getFacade().isAUMLElement(me)) {
                            if (Model.getModelManagementHelper().isReadOnly(me)) {
                                continue;
                            }
                        }
                        
                        LOG.log(Level.FINE,(moveAction ? "move " : "copy ") + me);
                        
                        if (Model.getCoreHelper().isValidNamespace(me, dest)) {
                            if (moveAction) {
                                Model.getCoreHelper().setNamespace(me, dest);
                                newTargets.add(me);
                            }
                            if (copyAction) {
                                try {
                                    newTargets.add(Model.getCopyHelper()
                                            .copy(me, dest));
                                } catch (RuntimeException e) {
                                    /* TODO: The copy function is not yet
                                     * completely implemented - so we will
                                     * have some exceptions here and there.*/
                                    LOG.log(Level.SEVERE, "Exception", e);
                                }
                            }
                        }
                        if (me instanceof Relocatable) {
                            Relocatable d = (Relocatable) me;
                            if (d.isRelocationAllowed(dest)) {
                                if (d.relocate(dest)) {
                                    ExplorerEventAdaptor.getInstance()
                                        .modelElementChanged(src);
                                    ExplorerEventAdaptor.getInstance()
                                        .modelElementChanged(dest);
                                    /*TODO: Make the tree refresh and expand
                                     * really work in all cases!
                                     */
                                    makeVisible(destinationPath);
                                    expandPath(destinationPath);
                                    newTargets.add(me);
                                }
                            }
                        }
                        if (Model.getFacade().isAFeature(me)
                                && Model.getFacade().isAClassifier(dest)) {
                            if (moveAction) {
                                Model.getCoreHelper().removeFeature(
                                        Model.getFacade().getOwner(me), me);
                                Model.getCoreHelper().addFeature(dest, me);
                                newTargets.add(me);
                            }
                            if (copyAction) {
                                newTargets.add(
                                        Model.getCopyHelper().copy(me, dest));
                            }
                        }
                    }
                    dropTargetDropEvent.getDropTargetContext()
                        .dropComplete(true);
                    TargetManager.getInstance().setTargets(newTargets);
                } catch (java.lang.IllegalStateException ils) {
                    LOG.log(Level.FINE, "drop IllegalStateException");
                    dropTargetDropEvent.rejectDrop();
                }

                dropTargetDropEvent.getDropTargetContext()
                    .dropComplete(true);
            } catch (IOException io) {
                LOG.log(Level.FINE, "drop IOException");
                dropTargetDropEvent.rejectDrop();
            } catch (UnsupportedFlavorException ufe) {
                LOG.log(Level.FINE, "drop UnsupportedFlavorException");
                dropTargetDropEvent.rejectDrop();
            }
        }

        /*
         * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
         */
        public void dropActionChanged(
                DropTargetDragEvent dropTargetDragEvent) {
            if (!isDragAcceptable(dropTargetDragEvent)) {
                dropTargetDragEvent.rejectDrag();
            } else {
                dropTargetDragEvent.acceptDrag(
                        dropTargetDragEvent.getDropAction());
            }
        }

        /**
         * @param dropTargetEvent the droptargetevent
         * @return true if the drag is acceptable
         */
        public boolean isDragAcceptable(
                DropTargetDragEvent dropTargetEvent) {
            // Only accept COPY or MOVE gestures (ie LINK is not supported)
            if ((dropTargetEvent.getDropAction()
                    & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
                return false;
            }

            // Do this if you want to prohibit dropping onto the drag source...
            Point pt = dropTargetEvent.getLocation();
            TreePath path = getPathForLocation(pt.x, pt.y);
            if (path == null) {
                return false;
            }
            if (path.equals(sourcePath)) {
                return false;
            }
            return true;
        }

        /**
         * @param dropTargetDropEvent the droptargetdropevent
         * @return true if the drop is acceptable
         */
        public boolean isDropAcceptable(
                DropTargetDropEvent dropTargetDropEvent) {
            // Only accept COPY or MOVE gestures (ie LINK is not supported)
            if ((dropTargetDropEvent.getDropAction()
                    & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
                return false;
            }

            // Do this if you want to prohibit dropping onto the drag source...
            Point pt = dropTargetDropEvent.getLocation();
            TreePath path = getPathForLocation(pt.x, pt.y);
            if (path == null) {
                return false;
            }
            if (path.equals(sourcePath)) {
                return false;
            }
            return true;
        }

    } /* end class */


    /**
     * The UID.
     */
    private static final long serialVersionUID = 6207230394860016617L;
}
