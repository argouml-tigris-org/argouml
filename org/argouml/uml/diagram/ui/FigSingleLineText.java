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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;

import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.providers.NotationProvider;
import org.tigris.gef.presentation.FigText;

/**
 * A SingleLine FigText to provide consistency across Figs displaying single
 * lines of text.<ul>
 * <li>The display area is transparent
 * <li>Text is center justified
 * <li>There is no line border
 * <li>There is space below the line for a "Clarifier",
 * i.e. a red squiggly line.
 * </ul>
 *
 * @author Bob Tarling
 */
public class FigSingleLineText extends FigText {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5611216741181499679L;

    /**
     * The properties of 'owner' that this is interested in
     */
    private String[] properties;
    
    /**
     * The notation provider for the text shown in this compartment.
     */
    private NotationProvider notationProvider;

    /*
     * @see org.tigris.gef.presentation.FigText#FigText(
     *         int, int, int, int, boolean)
     */
    public FigSingleLineText(int x, int y, int w, int h, boolean expandOnly) {
        super(x, y, w, h, expandOnly);

        setFont(FigNodeModelElement.getLabelFont());
        setTextColor(Color.black);
        setFilled(false);
        setTabAction(FigText.END_EDITING);
        setReturnAction(FigText.END_EDITING);
        setLineWidth(0);
    }

    /*
     * @see org.tigris.gef.presentation.FigText#FigText(
     *         int, int, int, int, boolean)
     */
    public FigSingleLineText(int x, int y, int w, int h, boolean expandOnly, 
            String property) {
        this(x, y, w, h, expandOnly, new String[] {property});
    }

    /*
     * @see org.tigris.gef.presentation.FigText#FigText(
     *         int, int, int, int, boolean)
     */
    public FigSingleLineText(int x, int y, int w, int h, boolean expandOnly, 
            String[] allProperties) {
        this(x, y, w, h, expandOnly);
        this.properties = allProperties;
    }



    public Dimension getMinimumSize() {
        Dimension d = new Dimension();

        Font font = getFont();

        if (font == null) {
            return d;
        }
        int maxW = getFontMetrics().stringWidth(getText());
        int maxH = 0;
        //int maxDescent = _fm.getMaxDescent();
        if (getFontMetrics() == null) {
            maxH = font.getSize();
        } else {
            maxH = getFontMetrics().getHeight();
        }
        int overallH = (maxH + getTopMargin() + getBotMargin());
        int overallW = maxW + getLeftMargin() + getRightMargin();
        d.width = overallW;
        d.height = overallH;
        return d;
    }

    protected boolean isStartEditingKey(KeyEvent ke) {
        if ((ke.getModifiers()
	     & (KeyEvent.META_MASK | KeyEvent.ALT_MASK)) == 0) {
            return super.isStartEditingKey(ke);
        } else {
            return false;
        }
    }
    
    public void setOwner(Object owner) {
        super.setOwner(owner);
        if (owner != null && properties != null) {
            Model.getPump().addModelEventListener(
                    this, 
                    owner, 
                    properties);
            setText();
        }
    }
    
    public void removeFromDiagram() {
        if (getOwner() != null && properties != null) {
            Model.getPump().removeModelEventListener(
                    this, 
                    getOwner(), 
                    properties);
        }
    }
    
    public void propertyChange(PropertyChangeEvent pce) {
        if (getOwner() != null
                && properties != null
                && pce instanceof AttributeChangeEvent) {
            assert Arrays.asList(properties).contains(pce.getPropertyName());
            setText();
        }
    }

    /**
     * This function without parameter shall
     * determine the text of the Fig taking values from the owner,
     * and then call {@link #setText(String)}.
     * TO be implemented as required by sub classes.
     */
    protected void setText() {
    }
    

    /**
     * @return Returns the notationProvider for the text in this compartment.
     */
    public NotationProvider getNotationProvider() {
        return notationProvider;
    }

    /**
     * @param np The notationProvider to set.
     */
    void setNotationProvider(NotationProvider np) {
        this.notationProvider = np;
    }
}
