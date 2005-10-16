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

import java.awt.Dimension;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.kernel.SingleStereotypeEnabler;
import org.argouml.uml.diagram.static_structure.ui.FigFeature;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;

/**
 * Presentation logic which is common to both an operations
 * compartment and an attributes compartment.<p>
 *
 * TODO: Investicate if this could be renamed to AbstractFigFeaturesCompartment?
 * @author Bob Tarling
 */
public abstract class FigFeaturesCompartment extends FigCompartment {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(FigCompartment.class);

    private FigSeperator compartmentSeperator;
    
    /**
     * The constructor.
     *
     * @param x x
     * @param y y
     * @param w width
     * @param h height
     */
    public FigFeaturesCompartment(int x, int y, int w, int h) {
        super(x, y, w, h);
        if (!SingleStereotypeEnabler.isEnabled()) {
            compartmentSeperator = new FigSeperator(10, 10, 11);
            addFig(compartmentSeperator);
        }
    }
    
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
     * @see org.tigris.gef.presentation.Fig#setVisible(boolean)
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

    /**
     * @see org.tigris.gef.presentation.FigGroup#addFig(org.tigris.gef.presentation.Fig)
     */
    public void addFig(Fig fig) {
        if (fig != getBigPort() && 
                !(fig instanceof FigFeature) && 
                !(fig instanceof FigSeperator)) {
            LOG.error("Illegal Fig added to a FigFeature");
            throw new IllegalArgumentException(
                    "A FigFeaturesCompartment can only contain FigFeatures, "
                    + "received a " + fig.getClass().getName());
        }
        super.addFig(fig);
    }

    /**
     * Fills the Fig by adding all figs within.<p>
     *
     * TODO: Check that this is correct?
     */
    public abstract void populate();
    
    /**
     * Returns the new size of the FigGroup (either attributes or
     * operations) after calculation new bounds for all sub-figs,
     * considering their minimal sizes; FigGroup need not be
     * displayed; no update event is fired.<p>
     *
     * This method has side effects that are sometimes used.
     *
     * @param fg the FigGroup to be updated
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
        if (!SingleStereotypeEnabler.isEnabled()) {
            return getMinimumSize();
        }
        int newW = w;
        int n = getFigs().size() - 1;
        int newH = checkSize ? Math.max(h, rowHeight * Math.max(1, n) + 2) : h;
        int step = (n > 0) ? (newH - 1) / n : 0;
        // width step between FigText objects int maxA =
        //Toolkit.getDefaultToolkit().getFontMetrics(LABEL_FONT).getMaxAscent();

        //set new bounds for all included figs
        Iterator figs = iterator();
        Fig fi;
        int fw, yy = y;
        while (figs.hasNext()) {
            fi = (Fig) figs.next();
            if (fi != getBigPort()) {
                fw = fi.getMinimumSize().width;
                if (!checkSize && fw > newW - 2) {
                    fw = newW - 2;
                }
                fi.setBounds(x + 1, yy + 1, fw, Math.min(rowHeight, step) - 2);
                if (checkSize && newW < fw + 2) {
                    newW = fw + 2;
                }
                yy += step;
            }
        }
        getBigPort().setBounds(x, y, newW, newH);
        // rectangle containing all following FigText objects
        calcBounds();
        return new Dimension(newW, newH);
    }
    
    
    /**
     * The minimum width is the minimum width of the widest child
     * The minium height is the total minimum height of all child figs.
     * @return the minimum width
     */
    public Dimension getMinimumSize() {
        if (super.getMinimumSize().height > 21) {
            return super.getMinimumSize();
        }
        return new Dimension(0, 20);
    }
    
    protected void setBoundsImpl(int x, int y, int w, int h) {
        if (SingleStereotypeEnabler.isEnabled()) {
            super.setBoundsImpl(x, y, w, h);
        } else {
            int newW = w;
            int n = getFigs().size() - 1;
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
            getBigPort().setBounds(x, y, newW, newH);
            calcBounds();
        }
    }
    
    /**
     * Create a new feature
     */
    abstract public void createFeature();
    
    protected class FigSeperator extends FigLine {
        FigSeperator(int x, int y, int len) {
            super(x, y, (x + len) - 1, y);
        }
        
        public Dimension getSize() {
            return new Dimension((_x2 - _x1) +1, getLineWidth());
        }
        
        public Dimension getMinimumSize() {
            return new Dimension(0, getLineWidth());
        }
        
        public void setBoundsImpl(int x, int y, int w, int h) {
            setX1(x);
            setY1(y);
            setX2((x + w) - 1);
            setY2((y + h) - 1);
        }
    }
}
