// $Id$
// Copyright (c) 1996-2009 The Regents of the University of California. All
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.model.InvalidElementException;
import org.argouml.notation.NotationProvider;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;

/**
 * Presentation logic for a boxed compartment,
 * which is common to e.g. an operations
 * compartment and an attributes compartment.<p>
 * 
 * This class is adds the possibility to 
 * make the whole compartment invisible, and
 * a NotationProvider is used to handle (generate and parse) 
 * the texts shown in the compartment, i.e. 
 * the compartment texts are editable by the user. <p>
 * 
 * This FigGroup shall only contain its bigPort, 
 * and Figs of type FigSeparator, and CompartmentFigText.
 */
public abstract class FigEditableCompartment extends FigCompartment {

    private static final Logger LOG = Logger.getLogger(FigCompartment.class);

    private static final int MIN_HEIGHT = 21;

    private FigSeperator compartmentSeperator;

    /**
     * The constructor. <p>
     * 
     * Two figs are added to this FigGroup:
     * The bigPort (i.e. a box that encloses all compartments),
     * and a separator.
     *
     * @param x x
     * @param y y
     * @param w width
     * @param h height
     * @deprecated for 0.27.3 by tfmorris.  Use 
     * {@link #FigEditableCompartment(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigEditableCompartment(int x, int y, int w, int h) {
        super(x, y, w, h); // This adds bigPort, i.e. number 1
        constructFigs();
    }

    private void constructFigs() {
        compartmentSeperator = new FigSeperator(X0, Y0, 11);
        addFig(compartmentSeperator); // number 2
    }
    
    /**
     * Construct a new FigGroup containing a "bigPort" or rectangle which
     * encloses the entire group for use in attaching edges, etc and a
     * separator.
     * 
     * @param owner owning UML element
     * @param bounds bounding rectangle of fig
     * @param settings render settings
     */
    public FigEditableCompartment(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings); // This adds bigPort, i.e. number 1
        constructFigs();
    }
    
    /**
     * @return separator figure
     */
    protected FigSeperator getSeperatorFig() {
        return compartmentSeperator;
    }

    /**
     * If a boxed compartment is set to invisible then remove all its
     * children.
     * This is to save on resources and increase efficiency as multiple
     * figs need not exist and be resized, moved etc if they are not visible.
     * If a compartment is later made visible then its child figs are rebuilt
     * from the model.
     * {@inheritDoc}
     */
    @Override
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
                if (f instanceof CompartmentFigText) {
                    removeFig(f);
                }
            }
        }
    }

    /*
     * @see org.tigris.gef.presentation.FigGroup#addFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void addFig(Fig fig) {
        if (fig != getBigPort()
                && !(fig instanceof CompartmentFigText)
                && !(fig instanceof FigSeperator)) {
            LOG.error("Illegal Fig added to a FigEditableCompartment");
            throw new IllegalArgumentException(
                    "A FigEditableCompartment can only "
                    + "contain CompartmentFigTexts, "
                    + "received a " + fig.getClass().getName());
        }
        super.addFig(fig);
    }

    /**
     * @return the collection of UML objects 
     *              on which this compartment is based
     */
    protected abstract Collection getUmlCollection();

    /**
     * @return the type of the notationProvider 
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

        List<Fig> figs = getElementFigs();
        // We remove all of them:
        for (Fig f : figs) {
            removeFig(f);    
        }

        // We are going to add the ones still valid & new ones
        // in the right sequence:
        FigSingleLineTextWithNotation comp = null;
        try {
            int acounter = -1;
            for (Object umlObject : getUmlCollection()) {
                comp = findCompartmentFig(figs, umlObject);
                acounter++;                

                // TODO: Some of these magic numbers probably assume a line
                // width of 1.  Replace with appropriate constants/variables.
                
                // If we don't have a fig for this UML object, we'll need to add
                // one. We set the bounds, but they will be reset later.
                if (comp == null) {
                    comp = createFigText(umlObject, new Rectangle(
                            xpos + 1 /*?LINE_WIDTH?*/,
                            ypos + 1 /*?LINE_WIDTH?*/ + acounter
                            * ROWHEIGHT,
                            0,
                            ROWHEIGHT - 2 /*? 2*LINE_WIDTH? */), 
                            getSettings());
                } else {
                    /* This one is still usable, so let's retain it, */
                    /* but its position may have been changed: */
                    Rectangle b = comp.getBounds();
                    b.y = ypos + 1 /*?LINE_WIDTH?*/ + acounter * ROWHEIGHT;
                    // bounds not relevant here, but I am perfectionist...
                    comp.setBounds(b);
                }
                /* We need to set a new notationprovider, since 
                 * the Notation language may have been changed:  */
                comp.initNotationProviders();
                addFig(comp); // add it again (but now in the right sequence)

                // Now put the text in
                // We must handle the case where the text is null
                String ftText = comp.getNotationProvider().toString(umlObject,
                        comp.getNotationSettings());
                if (ftText == null) {
                    ftText = "";
                }
                comp.setText(ftText);

                comp.setBotMargin(0);
            }
        } catch (InvalidElementException e) {
            LOG.debug("Attempted to populate a FigEditableCompartment" 
                    + " using a deleted model element - aborting", e);
        } 

        if (comp != null) {
            comp.setBotMargin(6); // the last one needs extra space below it
        }
    }

    /* Find the compartment fig for this umlObject: */
    private CompartmentFigText findCompartmentFig(List<Fig> figs, 
            Object umlObject) {
        for (Fig fig : figs) {
            if (fig instanceof CompartmentFigText) {
                CompartmentFigText candidate = (CompartmentFigText) fig;
                if (candidate.getOwner() == umlObject) {
                    return candidate;
                }
            }
        }
        return null;
    }

    private List<Fig> getElementFigs() {
        List<Fig> figs = new ArrayList<Fig>(getFigs());
        // TODO: This is fragile and depends on the behavior of the super class
        // not changing
        if (figs.size() > 1) {
            // Ignore the first 2 figs:
            figs.remove(1); // the separator
            figs.remove(0); // the bigPort
        }
        return figs;
    }

    /**
     * @return null
     * @deprecated for 0.27.3 by tfmorris.  Subclasses must implement
     * {@link #createFigText(Object, Rectangle, DiagramSettings, 
     * NotationProvider)}
     * which will become abstract in the future when this deprecated method is
     * removed.
     */
    @Deprecated
    protected FigSingleLineTextWithNotation createFigText(
	    int x, int y, int w, int h, Fig aFig, NotationProvider np) {
        // No longer abstract to allow subclasses to remove, so we provide a
        // null default implementation
        return null;
    }

    /**
     * Factory method to create a FigSingleLineTextWithNotation 
     * which must be implemented by all subclasses. 
     * It will become abstract after the release of 0.28 to
     * enforce this requirement.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     * @param np notation provider
     * @return a FigSingleLineText which can be used to display the text.
     */
    @SuppressWarnings("deprecation")
    protected FigSingleLineTextWithNotation createFigText(Object owner, 
            Rectangle bounds, 
            @SuppressWarnings("unused") DiagramSettings settings, 
            NotationProvider np) {

        // If this is not overridden it will revert to the old behavior
        // All internal subclasses have been updated, but this if for 
        // compatibility of non-ArgoUML extensions.
        FigSingleLineTextWithNotation comp = createFigText(
                    bounds.x,
                    bounds.y,
                    bounds.width,
                    bounds.height,
                    this.getBigPort(),
                    np);
        comp.setOwner(owner);
        return comp;
    }
    
    /**
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings the render settings
     * @return a FigSingleLineText with notation provider 
     *                  which can be used to display the text
     */
    abstract FigSingleLineTextWithNotation createFigText(Object owner, 
            Rectangle bounds, 
            DiagramSettings settings);
    
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
     * The minimum width is the minimum width of the widest child element.
     * The minimum height is the total minimum height of all child figs but no
     * less than MINIMUM_HEIGHT (21) pixels.
     * @return the minimum width
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension d = super.getMinimumSize();
        if (d.height < MIN_HEIGHT) {
            d.height = MIN_HEIGHT;
        }
        return d;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
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
                // TODO: Some of these magic numbers probably assume a line
                // width of 1.  Replace with appropriate constants/variables.
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
     * Fig representing separator for compartment.
     * This is a horizontal line.
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
            super(x, y, (x + len) - 1, y, LINE_COLOR);
            setLineWidth(LINE_WIDTH);
        }

        /*
         * @see org.tigris.gef.presentation.Fig#getSize()
         */
        @Override
        public Dimension getSize() {
            return new Dimension((_x2 - _x1) + 1, getLineWidth());
        }

        /*
         * @see org.tigris.gef.presentation.Fig#getMinimumSize()
         */
        @Override
        public Dimension getMinimumSize() {
            return new Dimension(0, getLineWidth());
        }

        /*
         * @see org.tigris.gef.presentation.Fig#setBoundsImpl(
         *         int, int, int, int)
         */
        @Override
        public void setBoundsImpl(int x, int y, int w, int h) {
            setX1(x);
            setY1(y);
            setX2((x + w) - 1);
            setY2(y);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -2222511596507221760L;
    }
}
