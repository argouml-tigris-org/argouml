// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.collaboration.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigStereotypesCompartment;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML classifier role in a  collaboration
 * diagram.<p>
 *
 * Stereotype (if there is one) and name are displayed in the centre of the
 * box.
 *
 * @author 10 Apr 2002. Jeremy Bennett (mail@jeremybennett.com). Modifications
 *         to ensure stereotypes are handled correctly.
 *
 * @author agauthie
 */
public class FigClassifierRole extends FigNodeModelElement {

    /**
     * The minimum padding top and bottom.
     */
    private static final int PADDING = 5;

    private NotationProvider notationProvider;

    /**
     * The fig that is used for the complete classifier role.
     * Identical in size to {@link FigNodeModelElement#bigPort}.<p>
     */
    private FigRect cover;

    /**
     * Constructor for a new classifier role.<p>
     *
     * An invisible {@link FigRect} as the point of contact for
     * connections ({@link FigNodeModelElement#bigPort}), with
     * matching rectangle providing the graphic rendering ({@link
     * #cover}). Stereotype and name are rendered centrally in the
     * rectangle.<p>
     */
    public FigClassifierRole() {
	// TODO: I (Linus Tolke) don't understand why I get a warning
	// on the 'cover' link in the javadoc (jdk1.4.2). I think everything
	// is correct. I hope that we can eventually solve it.

        // The big port and cover. Color of the big port is irrelevant

        setBigPort(new FigRect(10, 10, 90, 50, Color.cyan, Color.cyan));
        cover   = new FigRect(10, 10, 90, 50, Color.black, Color.white);

        // The stereotype. Width is the same as the cover, height is whatever
        // its minimum permitted is. The text should be centred.

        Dimension stereoMin = getStereotypeFig().getMinimumSize();

        getStereotypeFig().setLineWidth(0);
        getStereotypeFig().setVisible(true);
        //getStereotypeFig().setFilled(false);
        getStereotypeFig().setFillColor(Color.red);
        getStereotypeFig().setBounds(10, 10, 90, stereoMin.height);

        // The name. Width is the same as the cover, height is whatever its
        // minimum permitted is. The text of the name will be centred by
        // default. In the same place as the stereotype, since at this stage
        // the stereotype is not displayed. Being a classifier role it is
        // underlined

        Dimension nameMin = getNameFig().getMinimumSize();

        getNameFig().setLineWidth(0);
        getNameFig().setReturnAction(FigText.END_EDITING);
        getNameFig().setFilled(false);
        getNameFig().setUnderline(true);

        getNameFig().setBounds(10, 10, 90, nameMin.height);

        // add Figs to the FigNode in back-to-front order

        addFig(getBigPort());
        addFig(cover);
        addFig(getStereotypeFig());
        addFig(getNameFig());

        // Set our bounds to those we are given.

        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * Variant constructor that associates the classifier role with a
     * particular model element.<p>
     *
     * Classifier role is constructed with {@link #FigClassifierRole()}.<p>
     *
     * @param gm    The graph model to use. Ignored in this implementation.
     * @param lay   The layer
     * @param node  The model element object to associate with this Fig.
     */
    public FigClassifierRole(GraphModel gm, Layer lay, Object node) {
        this();
        setLayer(lay);
        setOwner(node);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#initNotationProviders(java.lang.Object)
     */
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isAClassifierRole(own)) {
            notationProvider =
                NotationProviderFactory2.getInstance().getNotationProvider(
                    NotationProviderFactory2.TYPE_CLASSIFIERROLE, own);
        }
    }

    /**
     * Return the default name to use for this classifier role.<p>
     *
     * Seems to be immediately overwritten by the empty string, but may be
     * useful in defining the default name size?<p>
     *
     * @return  The string to use ("new Classifier Role" in this case).
     */
    public String placeString() {
        return "new Classifier Role";
    }

    /**
     * Version of the clone to ensure all sub-figs are copied.<p>
     *
     * Uses the generic superclass clone which gives a vector of all the
     * figs. Then initialize our instance variables from this vector.<p>
     *
     * @return  A new copy of the the current fig.
     */

    public Object clone() {
        FigClassifierRole figClone = (FigClassifierRole) super.clone();
        Iterator it = figClone.getFigs().iterator();

        figClone.setBigPort((FigRect) it.next());
        figClone.cover   = (FigRect) it.next();
        it.next();
        figClone.setNameFig((FigText) it.next());

        return figClone;
    }

    /**
     * Update the stereotype text.<p>
     *
     * If the stereotype text is non-existant, we must make sure it is
     * marked not displayed, and update the display accordingly.<p>
     *
     * Similarly if there is text, we must make sure it is marked
     * displayed.<p>
     */
    protected void updateStereotypeText() {
        Rectangle rect = getBounds();

        int stereotypeHeight = 0;
        if (getStereotypeFig().isVisible()) {
            stereotypeHeight = getStereotypeFig().getHeight();
        }
        int heightWithoutStereo = getHeight() - stereotypeHeight;

        getStereotypeFig().setOwner(getOwner());
        ((FigStereotypesCompartment) getStereotypeFig()).populate();

        stereotypeHeight = 0;
        if (getStereotypeFig().isVisible()) {
            stereotypeHeight = getStereotypeFig().getHeight();
        }
        
        int minWidth = this.getMinimumSize().width;
        if (minWidth > rect.width) {
            rect.width = minWidth;
        }

        setBounds(
                rect.x,
                rect.y,
                rect.width,
                heightWithoutStereo + stereotypeHeight);
        calcBounds();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) { cover.setLineColor(col); }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() { return cover.getLineColor(); }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) { cover.setFillColor(col); }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() { return cover.getFillColor(); }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) { cover.setFilled(f); }

    /*
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() { return cover.getFilled(); }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) { cover.setLineWidth(w); }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() { return cover.getLineWidth(); }


    /**
     * Work out the minimum size that this Fig can be.<p>
     *
     * This should be the size of the stereotype + name + padding. However
     * we allow for the possible case that the cover or big port could be
     * bigger still.<p>
     *
     * @return  The minimum size of this fig.
     */

    public Dimension getMinimumSize() {

        Dimension stereoMin  = getStereotypeFig().getMinimumSize();
        Dimension nameMin    = getNameFig().getMinimumSize();

        Dimension newMin    = new Dimension(nameMin.width, nameMin.height);

        if (getStereotypeFig().isVisible()) {
            newMin.width   = Math.max(newMin.width, stereoMin.width);
            newMin.height += stereoMin.height;
        }
        
        newMin.height += PADDING;

        return newMin;
    }


    /**
     * Override setBounds to keep shapes looking right.<p>
     *
     * Set the bounds of all components of the Fig. The stereotype (if any)
     * and name are centred in the fig.<p>
     *
     * We allow for the requested bounds being too small, and impose our
     * minimum size if necessary.<p>
     *
     * @param x  X coordinate of upper left corner
     *
     * @param y  Y coordinate of upper left corner
     *
     * @param w  width of bounding box
     *
     * @param h  height of bounding box
     *
     * @author 10 Apr 2002. Jeremy Bennett (mail@jeremybennett.com). Patch to
     *         allow for stereotype as well.
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {

        // In the rather unlikely case that we have no name, we give up.

        if (getNameFig() == null) {
            return;
        }

        // Remember where we are at present, so we can tell GEF later. Then
        // check we are as big as the minimum size

        Rectangle oldBounds = getBounds();
        Dimension minSize   = getMinimumSize();

        int newW = (minSize.width  > w) ? minSize.width  : w;
        int newH = (minSize.height > h) ? minSize.height : h;

        Dimension stereoMin = getStereotypeFig().getMinimumSize();
        Dimension nameMin   = getNameFig().getMinimumSize();

        // Work out the padding each side, depending on whether the stereotype
        // is displayed and set bounds accordingly

        if (getStereotypeFig().isVisible()) {
            int extraEach = (h - nameMin.height - stereoMin.height) / 2;

            getStereotypeFig().setBounds(x, y + extraEach, w, stereoMin.height);
            getNameFig().setBounds(x, y + stereoMin.height + extraEach, w,
				   nameMin.height);
        } else {
            int extraEach = (h - nameMin.height) / 2;

            getNameFig().setBounds(x, y + extraEach, w, nameMin.height);
        }

        // Set the bounds of the bigPort and cover

        getBigPort().setBounds(x, y, newW, newH);
        cover.setBounds(x, y, newW, newH);

        // Record the changes in the instance variables of our parent, tell GEF
        // and trigger the edges to reconsider themselves.

        _x = x;
        _y = y;
        _w = newW;
        _h = newH;

        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    /**
     * Called after text has been edited directly on the screen.<p>
     *
     * @param ft  The text that was edited.
     * @throws PropertyVetoException by the parser
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        if (ft == getNameFig()) {
            notationProvider.parse(getOwner(), ft.getText());
            ft.setText(notationProvider.toString(getOwner(), null));
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProvider.getParsingHelp());
        }
    }

    /**
     * Adjust the fig in the light of some change to the model.<p>
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        if (notationProvider != null) {
            getNameFig().setText(notationProvider.toString(getOwner(), null));
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AddAssociationEvent
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
            damage();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object, java.lang.Object)
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        super.updateListeners(oldOwner, newOwner);
        /* Now, let's register for all events from all modelelements
         * that may change the text: 
         */
        if (newOwner != null) {
            addElementListener(newOwner, 
                    new String[] {"name", "base", "remove"});
            Collection bases = Model.getFacade().getBases(newOwner);
            Iterator i = bases.iterator();
            while (i.hasNext()) {
                Object base = i.next();
                addElementListener(base, "name");
            }
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionClassifierRole(this);
    }

} /* end class FigClassifierRole */
