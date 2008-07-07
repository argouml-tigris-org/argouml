// $Id$
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

package org.argouml.uml.diagram.static_structure.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Class in a diagram.<p>
 * 
 * A Class may show compartments for stereotypes,
 * attributes and operations.
 */
public class FigClass extends FigClassifierBoxWithAttributes
        implements AttributesCompartmentContainer {

    /**
     * Logger.
     */
    //private static final Logger LOG = Logger.getLogger(FigClass.class);

    /**
     * Constructor for a {@link FigClass} during file load.<p>
     *
     * Parent {@link org.argouml.uml.diagram.ui.FigNodeModelElement}
     * will have created the main box {@link #getBigPort()} and its
     * name {@link #getNameFig()} and stereotype
     * (@link #getStereotypeFig()}. This constructor
     * creates a box for the attributes and operations.<p>
     *
     * The properties of all these graphic elements are adjusted
     * appropriately. The main boxes are all filled and have
     * outlines.<p>
     *
     * <em>Warning</em>. Much of the graphics positioning is hard
     * coded. The overall figure is placed at location (10,10). The
     * name compartment (in the parent
     * {@link org.argouml.uml.diagram.ui.FigNodeModelElement} is
     * 21 pixels high. The stereotype compartment is created 15 pixels
     * high in the parent, but we change it to 19 pixels, 1 more than
     * ({@link #STEREOHEIGHT} here. The attribute and operations boxes
     * are created at 19 pixels, 2 more than {@link #ROWHEIGHT}.<p>
     * 
     * @param modelElement model element to be represented by this fig.
     * @param x x-position
     * @param y y-position
     * @param w width
     * @param h height
     */
    public FigClass(Object modelElement, int x, int y, int w, int h) {
        this(null, modelElement);
        setBounds(x, y, w, h);
    }

    /**
     * Constructor for a {@link FigClass} by diagram interaction.<p>
     *
     * Parent {@link org.argouml.uml.diagram.ui.FigNodeModelElement}
     * will have created the main box {@link #getBigPort()} and its
     * name {@link #getNameFig()} and stereotype
     * (@link #getStereotypeFig()}. This constructor
     * creates a box for the attributes and operations.<p>
     *
     * The properties of all these graphic elements are adjusted
     * appropriately. The main boxes are all filled and have
     * outlines.<p>
     *
     * <em>Warning</em>. Much of the graphics positioning is hard
     * coded. The overall figure is placed at location (10,10). The
     * name compartment (in the parent
     * {@link org.argouml.uml.diagram.ui.FigNodeModelElement} is
     * 21 pixels high. The stereotype compartment is created 15 pixels
     * high in the parent, but we change it to 19 pixels, 1 more than
     * ({@link #STEREOHEIGHT} here. The attribute and operations boxes
     * are created at 19 pixels, 2 more than {@link #ROWHEIGHT}.<p>
     *
     * @param gm   Not actually used in the current implementation
     *
     * @param node The UML object being placed.
     */
    public FigClass(GraphModel gm, Object node) {
        super();
        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(getOperationsFig());
        addFig(getAttributesFig());
        addFig(borderFig);
        setOwner(node);
    }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigClass figClone = (FigClass) super.clone();
        Iterator thisIter = this.getFigs().iterator();
        Iterator cloneIter = figClone.getFigs().iterator();
        while (thisIter.hasNext()) {
            Fig thisFig = (Fig) thisIter.next();
            Fig cloneFig = (Fig) cloneIter.next();
            if (thisFig == borderFig) {
                figClone.borderFig = thisFig;
            }
        }
        return figClone;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionClass(this);
    }

    protected Object buildModifierPopUp() {
        return buildModifierPopUp(ABSTRACT | LEAF | ROOT | ACTIVE);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        borderFig.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return borderFig.getLineWidth();
    }

    /**
     * @param fgVec the FigGroup
     * @param ft    the Figtext
     * @param i     get the fig before fig i
     * @return the FigText
     */
    protected FigText getPreviousVisibleFeature(FigGroup fgVec,
						FigText ft, int i) {
        if (fgVec == null || i < 1) {
            return null;
        }
        FigText ft2 = null;
        List figs = fgVec.getFigs();
        if (i >= figs.size() || !((FigText) figs.get(i)).isVisible()) {
            return null;
        }
        do {
            i--;
            while (i < 1) {
                if (fgVec == getAttributesFig()) {
                    fgVec = getOperationsFig();
                } else {
                    fgVec = getAttributesFig();
                }
                figs = fgVec.getFigs();
                i = figs.size() - 1;
            }
            ft2 = (FigText) figs.get(i);
            if (!ft2.isVisible()) {
                ft2 = null;
            }
        } while (ft2 == null);
        return ft2;
    }

    /**
     * @param fgVec the FigGroup
     * @param ft    the Figtext
     * @param i     get the fig after fig i
     * @return the FigText
     */
    protected FigText getNextVisibleFeature(FigGroup fgVec, FigText ft, int i) {
        if (fgVec == null || i < 1) {
            return null;
        }
        FigText ft2 = null;
        List v = fgVec.getFigs();
        if (i >= v.size() || !((FigText) v.get(i)).isVisible()) {
            return null;
        }
        do {
            i++;
            while (i >= v.size()) {
                if (fgVec == getAttributesFig()) {
                    fgVec = getOperationsFig();
                } else {
                    fgVec = getAttributesFig();
                }
                v = new ArrayList(fgVec.getFigs());
                i = 1;
            }
            ft2 = (FigText) v.get(i);
            if (!ft2.isVisible()) {
                ft2 = null;
            }
        } while (ft2 == null);
        return ft2;
    }

    public void setEnclosingFig(Fig encloser) {
        if (encloser == getEncloser()) {
            return;
        }
        if (encloser == null
                || (encloser != null
                && !Model.getFacade().isAInstance(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }
        if (!(Model.getFacade().isAUMLElement(getOwner()))) {
            return;
        }
        if (encloser != null
                && (Model.getFacade().isAComponent(encloser.getOwner()))) {
            moveIntoComponent(encloser);
            super.setEnclosingFig(encloser);
        }

    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        super.updateNameText();
        calcBounds();
        setBounds(getBounds());
    }
    
} /* end class FigClass */
