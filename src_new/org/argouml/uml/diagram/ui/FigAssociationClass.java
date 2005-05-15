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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCircle;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;


/**
 * Class to display an association edge in an Association Class.
 * It is considered the main Fig in the group of Figs that composes
 * an Association Class. It must be associated with a FigEdgeAssociationClass
 * and a FigClassAssociationClass. <p>
 *
 * This implementation is GEF-independent: Without changing GEF features,
 * it is an edge and a node at the same time.
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

    ////////////////////////////////////////////////////////////////
    // constants
    private static final int DISTANCE = 80;

    private FigAssociationClassTee tee;
    private FigEdgeAssociationClass edge;
    private FigClassAssociationClass fig;
    private int oldMin = -1;
    private int oldMax = -1;
    private boolean damageLock = false;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Construct a new AssociationClass. Use the same layout as for
     * other edges.
     */
    public FigAssociationClass() {
        super();
        setBetweenNearestPoints(true);
        ((FigPoly) _fig).setRectilinear(false);
        setDashed(false);
        
        final FigAssociationClass thisFig = this;
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Layer lay = thisFig.getLayer();

                Editor editor = Globals.curEditor();

                if (thisFig.getOwner() == null) {
                    thisFig.removeFromDiagram();
                    lay.remove(thisFig);
                    editor.remove(thisFig);
                } else {
                    thisFig.removePathItem(getMiddleGroup());

                    GraphModel graphModel = editor.getGraphModel();
                    
                    if (graphModel instanceof DeploymentDiagramGraphModel
                            || graphModel instanceof ClassDiagramGraphModel) {
                        
                        MutableGraphModel mutableGraphModel =
                            (MutableGraphModel) graphModel;
                        mutableGraphModel.addNode(thisFig.getOwner());

                        Rectangle drawingArea = ProjectBrowser.getInstance()
                                .getEditorPane().getBounds();
                        Iterator nodes = lay.getContentsNoEdges().iterator();
                        while (nodes.hasNext()) {
                            Fig auxFig = (Fig) nodes.next();
                            if (auxFig.getOwner().equals(thisFig.getOwner())) {
                                if (auxFig instanceof FigClassAssociationClass) {
                                    fig = (FigClassAssociationClass) auxFig;
                                    fig.setMainFig(thisFig);
                                } else if (auxFig instanceof FigAssociationClassTee) {
                                    tee = (FigAssociationClassTee) auxFig;
                                    addPathItem(tee, new PathConvPercent(thisFig, 50, 0));
                                }
                                if (tee != null && fig != null) {
                                    break;
                                }
                            }
                        }
                    
                        if (tee == null) {
                            tee = new FigAssociationClassTee();
                            lay.add(tee);
                            tee.setOwner(thisFig.getOwner());
                            addPathItem(tee, new PathConvPercent(thisFig, 50, 0));
                            thisFig.calcBounds();
                        }
                        
                        int x = tee.getX();
                        int y = tee.getY();
                        
                        if (fig == null) {
                            fig = new FigClassAssociationClass(thisFig);
                            fig.setOwner(thisFig.getOwner());
                            y = y - DISTANCE;
                            if (y < 0)
                                y = tee.getY() + fig.getHeight() + DISTANCE;
                            if (x + fig.getWidth() > drawingArea.getWidth())
                                x = tee.getX() - DISTANCE;
                            fig.setLocation(x, y);
                            lay.add(fig);
                        }
                        
                        Iterator edges = lay.getContentsEdgesOnly().iterator();
                        while (edges.hasNext()) {
                            Fig auxFig = (Fig) edges.next();
                            if (auxFig instanceof FigEdgeAssociationClass && auxFig.getOwner() == null) {
                                lay.remove(auxFig);
                                break;
                            }
                        }
                        edge =
                            new FigEdgeAssociationClass(tee, fig, thisFig);
                        
                        edge.setOwner(thisFig.getOwner());
                        lay.add(edge);
                        edge.damage();
                        fig.damage();
                    }
                }
            }
        });
    }
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
    
    public String getTipString(MouseEvent me) {
        return "Contains " + _fig.getClass().getName() + " " + _fig.getId() + " and " + tee.getClass().getName() + tee.getId();
    }
    
    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    public void setFig(Fig f) {
        super.setFig(f);
        _fig.setDashed(false);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#canEdit(org.tigris.gef.presentation.Fig)
     */
    protected boolean canEdit(Fig f) { return true; }

    /**
     * Calculate the bounds.
     *
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    public void calcBounds() {
        super.calcBounds();
        if (edge != null && edge instanceof FigEdgeAssociationClass) {
            edge.computeRoute();
            edge.damage();
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#damage()
     */
    public void damage() {
        figDamaged();
        super.damage();
    }

    /**
     * This method should be called whenever a FigAssociationClass
     * or a FigClassAssociationClass or a
     * FigEdgeAssociationClass is damaged. It changes the position
     * of these three Figs so that the
     * FigClassAssociationClass is the first in the Editor's layer.
     * Then the FigAssociationClass and finally the FigEdgeAssociationClass.
     */
    public void figDamaged() {
        if (!damageLock) {
            damageLock = true;

            Editor editor = Globals.curEditor();
            Layer lay = editor.getLayerManager().getActiveLayer();
            if (!lay.equals(null)
                    && lay instanceof LayerPerspective
                    && fig != null && edge != null
                    && lay.equals(fig.getLayer())
                    && lay.equals(edge.getLayer())) {
                LayerPerspective layP = (LayerPerspective) lay;
                int classFigIndex = layP.indexOf(fig);
                int myIndex = layP.indexOf(this);
                int edgeFigIndex = layP.indexOf(edge);
                int minIndex = Math.min(Math.min(myIndex, classFigIndex),
                        edgeFigIndex);
                int maxIndex = Math.max(Math.max(myIndex, classFigIndex),
                        edgeFigIndex);
                /* The figs have been just created */
                if (oldMin == -1 && oldMax == -1) {
                    layP.insertAt(fig, minIndex);
                    oldMin = minIndex;
                    oldMax = minIndex + 2;
                } else if (myIndex == -1
                        || edgeFigIndex == -1
                        || classFigIndex == -1) {
                    ;
                }
                /* Everything is fine*/
                else if (classFigIndex == (myIndex - 1)
                        && classFigIndex == (edgeFigIndex - 2)) {
                    oldMin = minIndex;
                    oldMax = maxIndex;
                }
                /* Class is lower but one of the edges has been moved up */
                else if (classFigIndex < myIndex
                        && classFigIndex < edgeFigIndex
                        && classFigIndex == oldMin) {
                    int aux = Math.max(myIndex, edgeFigIndex);
                    if (aux == (layP.getContents().size() - 1))
                        layP.add(edge);
                    else
                        layP.insertAt(edge, aux);
                    layP.insertAt(this, aux - 1);
                    layP.insertAt(fig, aux - 2);
                    oldMin = aux;
                    oldMax = aux + 2;
                }
                /* Class has been moved down */
                else if (classFigIndex < myIndex
                        && classFigIndex < edgeFigIndex
                        && classFigIndex < oldMin) {
                    layP.insertAt(edge, classFigIndex + 1);
                    layP.insertAt(this, classFigIndex + 1);
                    oldMin = classFigIndex;
                    oldMax = classFigIndex + 2;
                }
                /* They have changed their positions */
                else if ((classFigIndex > myIndex
                        || classFigIndex > edgeFigIndex)
                        && minIndex == oldMin
                        && classFigIndex <= oldMax) {
                    layP.insertAt(edge, minIndex);
                    layP.insertAt(this, minIndex);
                    layP.insertAt(fig, minIndex);
                }
                /* Class has been moved up */
                else if ((classFigIndex > myIndex
                        || classFigIndex > edgeFigIndex)
                        && minIndex == oldMin
                        && classFigIndex > oldMax) {
                    if (classFigIndex == (layP.getContents().size() - 1))
                        layP.add(edge);
                    else
                        layP.insertAt(edge, classFigIndex + 1);
                    layP.insertAt(this, classFigIndex);
                    oldMin = classFigIndex;
                    oldMax = classFigIndex + 2;
                }
                /* Any edge has been moved deeper than the class */
                else if ((classFigIndex > myIndex
                        || classFigIndex > edgeFigIndex)
                        && minIndex < oldMin) {
                    layP.insertAt(edge, minIndex);
                    layP.insertAt(this, minIndex);
                    layP.insertAt(fig, minIndex);
                    oldMin = minIndex;
                    oldMax = minIndex + 2;
                }
            }
            damageLock = false;
        }
    }

    /**
     *It is used to remove itself and also its associated figs.
     */
    public void removeFromDiagram() {
        if (edge != null)
            edge.removeThisFromDiagram();
        if (fig != null)
            fig.removeThisFromDiagram();
        if (tee != null) {
            tee.removeFromDiagram();
            TargetManager.getInstance().removeHistoryElement(tee);
        }
        Editor ce = Globals.curEditor();
        ce.getSelectionManager().deselect(this);
        super.removeFromDiagram();
    }

} /* end class FigAssociationClass */

