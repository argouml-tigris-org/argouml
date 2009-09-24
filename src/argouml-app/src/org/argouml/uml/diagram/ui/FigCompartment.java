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
import java.util.Collection;
import java.util.List;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;

/**
 * Presentation logic for a UML List Compartment. <p>
 * 
 * The UML defines a Name Compartment, and a List Compartment. 
 * This class implements the latter.<p>
 * 
 * A List Compartment is a boxed compartment,
 * containing vertically stacked figs,
 * which is common to e.g. an operations
 * compartment and an attributes compartment.<p>
 * 
 * The bigPort is filled, but has no border. All other figs contained 
 * in this group may not be filled, but can have a border. <p>
 * 
 * The size calculation done here supports vertically 
 * stacked sub-figs of this group and supports all 
 * compartment specializations.
 * 
 * @author Bob Tarling
 */
public abstract class FigCompartment extends ArgoFigGroup {

    private Fig bigPort;

    /**
     * The constructor.
     *
     * @param x x
     * @param y y
     * @param w width
     * @param h height
     * @deprecated for 0.27.3 by tfmorris.  Use 
     * {@link #FigCompartment(Object, Rectangle, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigCompartment(int x, int y, int w, int h) {
        constructFigs(x, y, w, h);
    }

    private void constructFigs(int x, int y, int w, int h) {
        bigPort = new FigRect(x, y, w, h, LINE_COLOR, FILL_COLOR);
        bigPort.setFilled(true);
        bigPort.setLineWidth(0);

        addFig(bigPort);
    }
    
    /**
     * Construct a new FigCompartment.
     * 
     * @param owner owning UML element
     * @param bounds rectangle describing bounds of compartment
     * @param settings render settings
     */
    public FigCompartment(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, settings);
        constructFigs(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * @return the bigPort
     */
    public Fig getBigPort() {
        return bigPort;
    }

    /**
     * The minimum width is the minimum width of the child with the widest
     * minimum width.
     * The minimum height is the total minimum height of all child figs plus a
     * 2 pixel padding.
     * @return the minimum width
     */
    @Override
    public Dimension getMinimumSize() {
        int minWidth = 0;
        int minHeight = 0;
        for (Fig fig : (Collection<Fig>) getFigs()) {
            if (fig.isVisible() && fig != getBigPort()) {
                int fw = fig.getMinimumSize().width;
                if (fw > minWidth) {
                    minWidth = fw;
                }
                minHeight += fig.getMinimumSize().height;
            }
        }

        minHeight += 2; // 2 Pixel padding after compartment
        return new Dimension(minWidth, minHeight);
    }

    @Override
    protected void setBoundsImpl(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();

        Dimension minimumSize = getMinimumSize();
        int newW = Math.max(w, minimumSize.width);
        int newH = Math.max(h, minimumSize.height);

        int currentHeight = 0;

        for  (Fig fig : (List<Fig>) getFigs()) {
            if (fig.isVisible() && fig != getBigPort()) {
                int fh = fig.getMinimumSize().height;

                fig.setBounds(x, y + currentHeight, newW, fh);
                currentHeight += fh;
            }
        }
        getBigPort().setBounds(x, y, newW, newH);
        calcBounds();
        firePropChange("bounds", oldBounds, getBounds());
    }
    
    /**
     * Create a new model element for the compartment.
     */
    protected abstract void createModelElement();

    @Override
    public void setLineWidth(int w) {
        super.setLineWidth(w);
        bigPort.setLineWidth(0);
    }

    @Override
    public void setFilled(boolean f) {
        // Only the bigPort may be filled
        super.setFilled(false);
        bigPort.setFilled(f);
    }

    @Deprecated //see parent
    @Override
    public boolean getFilled() {
        return isFilled();
    }

    @Override
    public boolean isFilled() {
        return bigPort.isFilled();
    }

    /**
     * This operation shall return a name unique for this type of 
     * compartment. Potential use: show at the top in the compartment 
     * as described in the UML, or as an identification string for 
     * the compartment type. <p>
     * See UML 1.4.2 OMG, chapter 5.24.1.2: Compartment name.
     * 
     * @return the name of the compartment 
     */
    public abstract String getName();
    
}
