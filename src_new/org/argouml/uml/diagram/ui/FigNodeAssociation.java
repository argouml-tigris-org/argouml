/**
 * Class to display graphics for N-ary association (association node)
 *
 * @author pepargouml@yahoo.es
 */

package org.argouml.uml.diagram.ui;

import org.argouml.model.ModelFacade;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigDiamond;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigText;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class FigNodeAssociation extends FigNodeModelElement {

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int X = 0;
    private static final int Y = 0;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigDiamond head;


    public FigNodeAssociation() {
        setBigPort(new FigDiamond(0,0,70,70,Color.cyan, Color.cyan));
        head = new FigDiamond(0,0,70,70,Color.black, Color.white);
        // Add the following to allow name editing on the diagram
        setNameFig(new FigText(X + 10, Y + 22, 0, 21, true));
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        getNameFig().setFont(getLabelFont());
        getNameFig().setTextColor(Color.black);
        getNameFig().setMultiLine(false);
        getNameFig().setAllowsTab(false);
        getNameFig().setJustificationByName("center");

        setStereotypeFig(new FigText(X + 10, Y + 22, 0, 21, true));
        getStereotypeFig().setFilled(false);
        getStereotypeFig().setLineWidth(0);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(head);
        addFig(getNameFig());
        addFig(getStereotypeFig());

        setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
        setBounds(r);
        setResizable(true);
    }

    public FigNodeAssociation(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigNodeAssociation figClone = (FigNodeAssociation) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigDiamond) it.next());
        figClone.head = (FigDiamond) it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.setStereotypeFig((FigText) it.next());
        return figClone;
    }


    /**
     * Used when a n-ary association becomes a binary association
     *
     * @param mee
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee == null || mee.getPropertyName().equals("isAbstract")) {
            updateAbstract();
            damage();
        }
        if (mee.getPropertyName().equals("connection")) {
            Collection cNew = (Collection) mee.getNewValue();
            Collection cOld = (Collection) mee.getOldValue();
            if (cNew.size() == 2 && cOld.size() == 3) {
                final Object association = getOwner();
                final Fig oldNodeFig = this;
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        oldNodeFig.setOwner(null);
                        FigEdge figEdge = null;
                        Editor editor = Globals.curEditor();
                        GraphModel gm = (GraphModel)editor.getGraphModel();
                        GraphEdgeRenderer renderer = editor.getGraphEdgeRenderer();
                        Layer lay = editor.getLayerManager().getActiveLayer();
                        figEdge = renderer.getFigEdgeFor(gm, lay, association);
                        editor.add(figEdge);
                        if (gm instanceof MutableGraphModel) {
                            MutableGraphModel mutableGraphModel = (MutableGraphModel) gm;
                            mutableGraphModel.removeNode(association);
                            mutableGraphModel.addEdge(association);
                        }
                        oldNodeFig.removeFromDiagram();
                        editor.getSelectionManager().deselectAll();
                        editor.damageAll();
                    }
                });
            }

        }
    }

    /**
     * Updates the name if modelchanged receives an "isAbstract" event
     */
    protected void updateAbstract() {
        Rectangle rect = getBounds();
        if (getOwner() == null)
            return;
        Object assoc =  getOwner();
        if (ModelFacade.isAbstract(assoc))
            getNameFig().setFont(getItalicLabelFont());
        else
            getNameFig().setFont(getLabelFont());
        super.updateNameText();
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Makes sure that the edges stick to the outline of the fig.
     * @see org.tigris.gef.presentation.Fig#getGravityPoints()
     */
    public List getGravityPoints() {
        return getBigPort().getGravityPoints();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        head.setLineColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return head.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        head.setFillColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return head.getFillColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return true;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        head.setLineWidth(w);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return head.getLineWidth();
    }

    public void setBounds(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();
        getBigPort().setBounds(x, y, w, h);
        head.setBounds(x, y, w, h);
        getNameFig().setBounds(x, y + h / 2 - 8, w, 15);
        getStereotypeFig().setBounds(x, y + h / 2 - 20, w, 15);
        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    public void removeFromDiagram() {
        Object owner = getOwner();
        super.removeFromDiagram();
        Editor editor = Globals.curEditor();
        GraphModel gm = (GraphModel)editor.getGraphModel();
        if (gm instanceof MutableGraphModel) {
            MutableGraphModel mutableGraphModel = (MutableGraphModel) gm;
            mutableGraphModel.removeEdge(owner);
        }
    }
    ////////////////////////////////////////////////////////////////
    // Event handlers

    /**
     * Block any textentry on the diagram - there is nothing to edit!
     *
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) { }

} /* end class FigNodeAssociation */


