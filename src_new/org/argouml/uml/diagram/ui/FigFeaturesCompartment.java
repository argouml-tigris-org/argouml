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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.InvalidElementException;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.static_structure.ui.FigFeature;
import org.argouml.uml.notation.NotationProvider;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;

/**
 * Presentation logic which is common to both an operations
 * compartment and an attributes compartment.<p>
 *
 * TODO: This is really just a generic list compartment.  It doesn't have
 * any behavior specific to features.  Rename to something more appropriate and
 * create a named list compartment variant. - tfm - 20060313
 * 
 * TODO: Investigate if this could be renamed to AbstractFigFeaturesCompartment?
 * @author Bob Tarling
 */
public abstract class FigFeaturesCompartment extends FigCompartment {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(FigCompartment.class);

    private FigSeperator compartmentSeperator;

    /**
     * The constructor. <p>
     * 
     * Two figs are added to this FigGroup:
     * The bigPort (i.e. a box that encloses all compartments),
     * and a seperator.
     *
     * @param x x
     * @param y y
     * @param w width
     * @param h height
     */
    public FigFeaturesCompartment(int x, int y, int w, int h) {
        super(x, y, w, h); // This adds bigPort, i.e. number 1
        compartmentSeperator = new FigSeperator(10, 10, 11);
        addFig(compartmentSeperator); // number 2
    }

    /**
     * @return separator figure
     */
    protected FigSeperator getSeperatorFig() {
        return compartmentSeperator;
    }

    /**
     * If a features compartment is set to invisible then remove all its
     * children.
     * This is to save on resources and increase efficiency as multiple
     * figs need not exist and be resized, moved etc if they are not visible.
     * If a compartment is later made visible the its child figs are rebuilt
     * from the model.
     * {@inheritDoc}
     */
    public void setVisible(boolean visible) {
        if (isVisible() == visible) {
            return;
        }
        super.setVisible(visible);
        if (visible) {
            populate();
        } else {
            for (int i = getFigs().size() - 1; i >= 0; --i) {
                Fig f = getFigAt(i);
                if (f instanceof FigFeature) {
                    removeFig(f);
                }
            }
        }
    }

    /*
     * @see org.tigris.gef.presentation.FigGroup#addFig(org.tigris.gef.presentation.Fig)
     */
    public void addFig(Fig fig) {
        if (fig != getBigPort()
                && !(fig instanceof FigFeature)
                && !(fig instanceof FigSeperator)) {
            LOG.error("Illegal Fig added to a FigFeature");
            throw new IllegalArgumentException(
                    "A FigFeaturesCompartment can only contain FigFeatures, "
                    + "received a " + fig.getClass().getName());
        }
        super.addFig(fig);
    }

    /**
     * @return the collection of UML objects 
     *              on which this compartment of features is based
     */
    protected abstract Collection getUmlCollection();

    /**
     * @return the type of the notationprovider 
     *              used to handle the text in the compartment 
     */
    protected abstract int getNotationType();
    
    /**
     * Fills the Fig by adding all figs within.
     */
    public void populate() {
        if (!isVisible()) {
            return;
        }

        Fig bigPort = this.getBigPort();
        int xpos = bigPort.getX();
        int ypos = bigPort.getY();

        Vector figs = new Vector(getFigs());
        if (figs.size() > 1) {
            // Ignore the first 2 figs:
            figs.removeElementAt(1); // the seperator
            figs.removeElementAt(0); // the bigPort
        }
        // We remove all of them:
        Iterator i = figs.iterator();
        while (i.hasNext()) {
            Fig f = (Fig) i.next();
            removeFig(f);    
        }

        // We are going to add the ones still valid & new ones
        // in the right sequence:
        FigSingleLineText comp = null;
        try {
            Collection umlObjects = getUmlCollection();
            int acounter = -1;
            Iterator iter = umlObjects.iterator();
            while (iter.hasNext()) {
                Object umlObject = iter.next();
                comp = null; // find the (next) compartment
                acounter++;                
                
                /* Find the compartment fig for this umlObject: */
                Iterator it = figs.iterator();
                while (it.hasNext()) {
                    CompartmentFigText candidate;
                    Object fig = it.next();
                    if (fig instanceof CompartmentFigText) {
                        candidate = (CompartmentFigText) fig;
                        if (candidate.getOwner() == umlObject) {
                            comp = candidate;
                            break;
                        }
                    }
                }
                
                NotationProvider np = 
                    NotationProviderFactory2.getInstance()
                        .getNotationProvider(getNotationType(), umlObject);

                // If we don't have a fig for this feature, we'll need to add
                // one. We set the bounds, but they will be reset later.
                if (comp == null) {
                    comp = createFigText(
                                xpos + 1,
                                ypos + 1 + acounter
                                * FigNodeModelElement.ROWHEIGHT,
                                0,
                                FigNodeModelElement.ROWHEIGHT - 2,
                                bigPort,
                                np);
                    // bounds not relevant here
                    comp.setOwner(umlObject);
                    
                } else {
                    /* This one is still useable, so let's retain it, */
                    /* but its position may have been changed: */
                    Rectangle b = comp.getBounds();
                    b.y = ypos + 1 + acounter * FigNodeModelElement.ROWHEIGHT;
                    // bounds not relevant here, but I am perfectionist...
                    comp.setBounds(b);
                    /* We need to set a new notationprovider, since 
                     * the Notation language may have been changed:  */
                    comp.setNotationProvider(np);
                }
                addFig(comp); // add it again (but now in the right sequence)
                
                // Now put the text in
                // We must handle the case where the text is null
                String ftText =
                        comp.getNotationProvider().toString(umlObject, null);
                if (ftText == null) {
                    ftText = "";
                }
                comp.setText(ftText);
                
                addExtraVisualisations(umlObject, comp);
                comp.setBotMargin(0);
            }
        } catch (InvalidElementException e) {
            LOG.debug("Attempted to populate a FigFeatureCompartment" 
                    + " using a deleted model element - aborting", e);
        } 

        if (comp != null) {
            comp.setBotMargin(6); // the last one needs extra space below it
        }
    }
    
    protected FigSingleLineText createFigText(
	    int x, int y, int w, int h, Fig aFig, NotationProvider np) {
        return new FigFeature(x, y, w, h, aFig, np);
    }

    /**
     * Add extra decorations to the compartment fig 
     * to visualise an UML object property.
     * TODO: This is being used to decorate the child Figs not this Fig
     * The child Figs should control their own visualization so lets get rid
     * of this.
     * @param umlObject the UML object shown in the given compartment fig
     * @param comp the given compartment fig to be decorated
     */
    protected void addExtraVisualisations(Object umlObject, 
            FigSingleLineText comp) {
        /* By default there are none.
         * Overrule if needed. */
    }
    
    /**
     * Returns the new size of the FigGroup (either attributes or
     * operations) after calculation new bounds for all sub-figs,
     * considering their minimal sizes; FigGroup need not be
     * displayed; no update event is fired.<p>
     *
     * This method has side effects that are sometimes used.
     *
     * @param x x
     * @param y y
     * @param w w
     * @param h h
     * @return the new dimension
     */
    public Dimension updateFigGroupSize(
                       int x,
                       int y,
                       int w,
                       int h,
                       boolean checkSize,
                       int rowHeight) {
        return getMinimumSize();
    }

    /**
     * The minimum width is the minimum width of the widest child feature.
     * The minimum height is the total minimum height of all child figs but no
     * less than 21 pixels.
     * @return the minimum width
     */
    public Dimension getMinimumSize() {
        Dimension d = super.getMinimumSize();
        if (d.height < 21) {
            d.height = 21;
        }
        return d;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {
        int newW = w;
        int newH = h;

        Iterator figs = iterator();
        Fig fig;
        int fw;
        int yy = y;
        while (figs.hasNext()) {
            fig = (Fig) figs.next();
            if (fig.isVisible() && fig != getBigPort()) {
                if (fig instanceof FigSeperator) {
                    fw = w;
                } else {
                    fw = fig.getMinimumSize().width;
                }
                fig.setBounds(x + 1, yy + 1, fw, fig.getMinimumSize().height);
                if (newW < fw + 2) {
                    newW = fw + 2;
                }
                yy += fig.getMinimumSize().height;
            }
        }
        getBigPort().setBounds(x + 1, y + 1, newW - 3, newH - 1);
        calcBounds();
    }

    /**
     * Create a new feature.
     */
    public abstract void createFeature();

    /**
     * Fig representing separator for compartment.
     */
    protected static class FigSeperator extends FigLine {
        /**
         * Constructor.
         *
         * @param x
         * @param y
         * @param len
         */
        FigSeperator(int x, int y, int len) {
            super(x, y, (x + len) - 1, y);
        }

        /*
         * @see org.tigris.gef.presentation.Fig#getSize()
         */
        public Dimension getSize() {
            return new Dimension((_x2 - _x1) + 1, getLineWidth());
        }

        /*
         * @see org.tigris.gef.presentation.Fig#getMinimumSize()
         */
        public Dimension getMinimumSize() {
            return new Dimension(0, getLineWidth());
        }

        /*
         * @see org.tigris.gef.presentation.Fig#setBoundsImpl(
         *         int, int, int, int)
         */
        public void setBoundsImpl(int x, int y, int w, int h) {
            setX1(x);
            setY1(y);
            setX2((x + w) - 1);
            setY2((y + h) - 1);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -2222511596507221760L;
    }
}
