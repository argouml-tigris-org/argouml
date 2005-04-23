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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display a UML comment in a diagram.
 *
 * @author Andreas Rueckert
 */
public class FigComment
    extends FigNodeModelElement
    implements VetoableChangeListener,
	       DelayedVChangeListener,
	       MouseListener,
	       KeyListener,
	       PropertyChangeListener {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(FigComment.class);

    ////////////////////////////////////////////////////////////////
    // constants

    private int x = 0;
    private int y = 0;
    private int width = 80;
    private int height = 60;
    private int gapY = 10;

    private boolean readyToEdit = true;

    ////////////////////////////////////////////////////////////////
    // instance variables

    // The figure that holds the text of the note.
    private FigText text;

    private FigPoly body;
    private FigPoly urCorner; // the upper right corner

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The main constructor used for file loading.
     */
    public FigComment() {

        body = new FigPoly(Color.black, Color.white);
        body.addPoint(x, y);
        body.addPoint(x + width - 1 - gapY, y);
        body.addPoint(x + width - 1, y + gapY);
        body.addPoint(x + width - 1, y + height - 1);
        body.addPoint(x, y + height - 1);
        body.addPoint(x, y);
        body.setFilled(true);
        body.setLineWidth(1);

        urCorner = new FigPoly(Color.black, Color.white);
        urCorner.addPoint(x + width - 1 - gapY, y);
        urCorner.addPoint(x + width - 1, y + gapY);
        urCorner.addPoint(x + width - 1 - gapY, y + gapY);
        urCorner.addPoint(x + width - 1 - gapY, y);
        urCorner.setFilled(true);
        urCorner.setLineWidth(1);

        setBigPort(new FigRect(x, y, width, height, null, null));
        getBigPort().setFilled(false);
        getBigPort().setLineWidth(0);

        text = new FigText(2, 2, width - 2 - gapY, height - 4, true);
        text.setFont(getLabelFont());
        text.setTextColor(Color.black);
        text.setMultiLine(true);
        text.setAllowsTab(false);
        // _text.setText(placeString());
        text.setJustification(FigText.JUSTIFY_LEFT);
        text.setFilled(false);
        text.setLineWidth(0);
        //_text.setLineColor(Color.white);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(body);
        addFig(urCorner);
        addFig(text);

        setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
        updateEdges();

        readyToEdit = false;
    }

    /**
     * Construct a new comment.
     *
     * @param gm the graphmodel
     * @param node the underlying UML Comment
     */
    public FigComment(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * Get the default text for this figure.
     *
     * @return The default text for this figure.
     */
    public String placeString() {
        String placeString = retrieveNote();
        if (placeString == null) {
            placeString = "new note";
        }
        return placeString;
    }

    /**
     * Clone this figure.
     *
     * @return The cloned figure.
     */
    public Object clone() {
        FigComment figClone = (FigComment) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.body = (FigPoly) it.next();
        figClone.urCorner = (FigPoly) it.next();
        figClone.text = (FigText) it.next();
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /**
     * See FigNodeModelElement.java for more info on these methods.
     */

    /**
     * If the user double clicks on any part of this FigNode, pass it
     * down to one of the internal Figs.  This allows the user to
     * initiate direct text editing.
     *
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        if (!readyToEdit) {
            if (Model.getFacade().isAModelElement(getOwner())) {
                readyToEdit = true;
            } else {
                LOG.debug("not ready to edit note");
                return;
            }
        }
        if (me.isConsumed()) {
            return;
        }
        if (me.getClickCount() >= 2
	    && !(me.isPopupTrigger()
		 || me.getModifiers() == InputEvent.BUTTON3_MASK)) {
            if (getOwner() == null) {
                return;
            }
            Fig f = hitFig(new Rectangle(me.getX() - 2, me.getY() - 2, 4, 4));
            if (f instanceof MouseListener) {
                ((MouseListener) f).mouseClicked(me);
            }
        }
        me.consume();
    }

    /**
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange(PropertyChangeEvent pce) {
        Object src = pce.getSource();
        if (src == getOwner()) {
            DelayedChangeNotify delayedNotify =
		new DelayedChangeNotify(this, pce);
            SwingUtilities.invokeLater(delayedNotify);
        } else {
            LOG.debug("FigNodeModelElement got vetoableChange"
		      + " from non-owner:" + src);
        }
    }

    /**
     * @see org.argouml.kernel.DelayedVChangeListener#delayedVetoableChange(java.beans.PropertyChangeEvent)
     */
    public void delayedVetoableChange(PropertyChangeEvent pce) {
        // update any text, colors, fonts, etc.
        renderingChanged();
        // update the relative sizes and positions of internel Figs
        endTrans();
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pve) {
        Object src = pve.getSource();
        String pName = pve.getPropertyName();
        if (pName.equals("editing")
	    && Boolean.FALSE.equals(pve.getNewValue())) {
            //parse the text that was edited
            textEdited((FigText) src);
            // resize the FigNode to accomodate the new text
            Rectangle bbox = getBounds();
            Dimension minSize = getMinimumSize();
            bbox.width = Math.max(bbox.width, minSize.width);
            bbox.height = Math.max(bbox.height, minSize.height);
            setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
            endTrans();
        } else {
            super.propertyChange(pve);
        }
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) {
        if (!readyToEdit) {
            if (Model.getFacade().isAModelElement(getOwner())) {
                storeNote("");
                readyToEdit = true;
            } else {
                LOG.debug("not ready to edit note");
                return;
            }
        }
        if (ke.isConsumed()) {
            return;
        }
        if (getOwner() == null) {
            return;
        }
        text.keyPressed(ke);
    }

    /**
     * Not used, do nothing.
     *
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent ke) {
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent ke) {
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionComment(this);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        // The _text element has no border, so the line color doesn't matter.
        body.setLineColor(col);
        urCorner.setLineColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return body.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        body.setFillColor(col);
        urCorner.setFillColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return body.getFillColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        text.setFilled(false); // The text is always opaque.
        body.setFilled(f);
        urCorner.setFilled(f);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return body.getFilled();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        text.setLineWidth(0); // Make a seamless integration of the text
        // in the note figure.
        body.setLineWidth(w);
        urCorner.setLineWidth(w);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return body.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // user interaction methods

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) {
        if (ft == text) {
            storeNote(ft.getText());
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig encloser) {
        super.setEnclosingFig(encloser);
    }

    ////////////////////////////////////////////////////////////////
    // accessor methods

    /**
     * Store a note in the associated model element.
     *
     * @param note The note to store.
     */
    public final void storeNote(String note) {
        if (getOwner() != null) {
            Model.getCoreHelper().setName(getOwner(), note);
        }
    }

    /**
     * Retrieve the note from the associated model element.
     *
     * @return The note from the associated model element.
     */
    public final String retrieveNote() {
        return (getOwner() != null)
	    ? Model.getFacade().getName(getOwner())
	    : null;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    public boolean getUseTrapRect() {
        return true;
    }

    /**
     * Get the minimum size for the note figure.
     *
     * @return The minimum size for the note figure.
     */
    public Dimension getMinimumSize() {

        // Get the size of the text field.
        Dimension textMinimumSize = text.getMinimumSize();

        // And add the gaps around the textfield to get the minimum
        // size of the note.
        return new Dimension(textMinimumSize.width + 4 + gapY,
			     textMinimumSize.height + 4);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int px, int py, int w, int h) {
        if (text == null) {
            return;
        }

        Rectangle oldBounds = getBounds();

        // Resize the text figure
        text.setBounds(px + 2, py + 2, w - 4 - gapY, h - 4);

        // Resize the big port around the figure
        getBigPort().setBounds(px, py, w, h);

        // Since this is a complex polygon, there's no easy way to resize it.
        Polygon newPoly = new Polygon();
        newPoly.addPoint(px, py);
        newPoly.addPoint(px + w - 1 - gapY, py);
        newPoly.addPoint(px + w - 1, py + gapY);
        newPoly.addPoint(px + w - 1, py + h - 1);
        newPoly.addPoint(px, py + h - 1);
        newPoly.addPoint(px, py);
        body.setPolygon(newPoly);

        // Just move the corner to it's new position.
        urCorner.setBounds(px + w - 1 - gapY, py, gapY, gapY);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateBounds()
     */
    protected void updateBounds() {
        Rectangle bbox = getBounds();
        Dimension minSize = getMinimumSize();
        bbox.width = Math.max(bbox.width, minSize.width);
        bbox.height = Math.max(bbox.height, minSize.height);
        setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    }

    ///////////////////////////////////////////////////////////////////
    // Internal methods

    /**
     * This is called after any part of the UML ModelElement (the comment) has
     * changed. This method automatically updates the note FigText.
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected final void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);

        String noteStr = retrieveNote();
        if (noteStr != null) {
            text.setText(noteStr);
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        if (getOwner() != null) {
            String t = Model.getFacade().getName(getOwner());
            if (t != null) {
                text.setText(t);
                calcBounds();
                setBounds(getBounds());
            }
        }
    }

} /* end class FigComment */
