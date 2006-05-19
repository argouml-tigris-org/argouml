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

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;

import org.argouml.kernel.DelayedVChangeListener;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigPoly;


/**
 * Class to display an association edge in an Association Class.
 * It is considered the main Fig in the group of Figs that composes
 * an Association Class. It must be associated with a FigEdgeAssociationClass
 * and a FigClassAssociationClass. <p>
 *
 * The Association Class is composed of a FigAssociationClass (edge),
 * a FigEdgeAssociationClass (linking edge) and
 * a FigClassAssociationClass (node).
 * The whole fig is built as an edge but is handled as a node
 * to allow for example that an Association Class is bound to
 * a Class with a simple association. The three Figs have the same rank. <p>
 *
 * To handle it, the restriction that must be fulfilled is
 * that the FigClassAssociationClass must be always the first
 * in the Editor's layer, and several methods are overridden and implemented:
 * removeFromDiagram, damage, figdamaged, etc.
 *
 * @author pepargouml
 */
public class FigAssociationClass
        extends FigAssociation
        implements VetoableChangeListener,
        DelayedVChangeListener,
        MouseListener,
        KeyListener,
        PropertyChangeListener {

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3643715304027095083L;

    /**
     * Construct a new AssociationClass. Use the same layout as for
     * other edges.
     */
    public FigAssociationClass() {
        super();
        setBetweenNearestPoints(true);
        ((FigPoly) getFig()).setRectilinear(false);
        setDashed(false);
        // Remove this and the inner class
        // Build all the figs and connect them in ModeCreateAssociationClass
//        SwingUtilities.invokeLater(new RunFigAssociation(this));
    }

//    /**
//     * Class to finish the initialization of this fig.
//     */
//    class RunFigAssociation implements Runnable {
//        /**
//         * The fig to initialize.
//         */
//        private FigAssociationClass thisFig;
//
//        /**
//         * Constructor.
//         *
//         * @param fac The fig to initialize.
//         */
//        RunFigAssociation(FigAssociationClass fac) {
//            thisFig = fac;
//        }
//
//        /**
//         * @see java.lang.Runnable#run()
//         */
//        public void run() {
//            Layer lay = thisFig.getLayer();
//
//            Editor editor = Globals.curEditor();
//
//            if (thisFig.getOwner() == null) {
//                thisFig.removeFromDiagram();
//                lay.remove(thisFig);
//                editor.remove(thisFig);
//            } else {
//                thisFig.removePathItem(getMiddleGroup());
//
//                GraphModel graphModel = editor.getGraphModel();
//
//                MutableGraphModel mutableGraphModel =
//                    (MutableGraphModel) graphModel;
//                mutableGraphModel.addNode(thisFig.getOwner());
//
//                Rectangle drawingArea =
//                    ProjectBrowser.getInstance()
//                    	.getEditorPane().getBounds();
//                Iterator nodes = lay.getContents().iterator();
//                while (nodes.hasNext()) {
//                    Fig auxFig = (Fig) nodes.next();
//                    if (thisFig.getOwner().equals(auxFig.getOwner())) {
//                        if (auxFig instanceof FigClassAssociationClass) {
//                            figNode = (FigClassAssociationClass) auxFig;
//                            figNode.setMainFig(thisFig);
//                        } else if (auxFig
//                            instanceof FigAssociationClassTee) {
//                            tee = (FigAssociationClassTee) auxFig;
//                            addPathItem(tee,
//                                    new PathConvPercent(thisFig, 50, 0));
//                        } else if (auxFig
//                            instanceof FigEdgeAssociationClass) {
//                            dashedEdge = (FigEdgeAssociationClass) auxFig;
//                            dashedEdge.setMainFig(thisFig);
//                            // addPathItem(tee,
//                            //         new PathConvPercent(thisFig, 50, 0));
//                        }
//                        if (tee != null && figNode != null
//                            && dashedEdge != null) {
//                            break;
//                        }
//                    }
//                }
//
//                if (tee == null) {
//                    tee = new FigAssociationClassTee();
//                    lay.add(tee);
//                    tee.setOwner(thisFig.getOwner());
//                    addPathItem(tee, new PathConvPercent(thisFig, 50, 0));
//                    thisFig.calcBounds();
//                }
//
//                int x = tee.getX();
//                int y = tee.getY();
//
//                if (figNode == null) {
//                    figNode = new FigClassAssociationClass(thisFig);
//                    figNode.setOwner(thisFig.getOwner());
//                    y = y - DISTANCE;
//                    if (y < 0) {
//                        y = tee.getY() + figNode.getHeight() + DISTANCE;
//                    }
//                    if (x + figNode.getWidth() > drawingArea.getWidth()) {
//                        x = tee.getX() - DISTANCE;
//                    }
//                    figNode.setLocation(x, y);
//                    lay.add(figNode);
//                }
//
//                if (dashedEdge == null) {
//                    dashedEdge =
//                        new FigEdgeAssociationClass(tee, figNode, thisFig);
//                    dashedEdge.setOwner(thisFig.getOwner());
//                    lay.add(dashedEdge);
//                }
//
//                dashedEdge.damage();
//                figNode.damage();
//            }
//        }
//    }

    /**
     * Construct a new AssociationClass. Use the same layout as for
     * other edges.
     *
     * @param ed the edge
     * @param lay the layer
     */
    public FigAssociationClass(Object ed, Layer lay) {
        this();
        setLayer(lay);
        setOwner(ed);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.tigris.gef.presentation.FigEdge#setFig(
     *         org.tigris.gef.presentation.Fig)
     */
    public void setFig(Fig f) {
        super.setFig(f);
        getFig().setDashed(false);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#canEdit(
     *         org.tigris.gef.presentation.Fig)
     */
    protected boolean canEdit(Fig f) { return true; }

} /* end class FigAssociationClass */

