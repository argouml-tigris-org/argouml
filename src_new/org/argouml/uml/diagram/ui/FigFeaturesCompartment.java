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

import org.apache.log4j.Logger;
import org.argouml.uml.diagram.static_structure.ui.FigFeature;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;

/**
 * Presentation logic which is common to both an operations
 * compartment and an attributes compartment.
 * @author Bob Tarling
 */
public abstract class FigFeaturesCompartment extends FigCompartment {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(FigCompartment.class);

    private Fig bigPort;

    /**
     * The constructor.
     *
     * @param x x
     * @param y y
     * @param w width
     * @param h height
     */
    public FigFeaturesCompartment(int x, int y, int w, int h) {
        bigPort = new FigRect(x, y, w, h, Color.black, Color.white);
        bigPort.setFilled(true);
        bigPort.setLineWidth(1);
        setFilled(true);
        setLineWidth(1);
        addFig(bigPort);
    }

    /**
     * USED BY PGML.tee.
     *
     * @see org.tigris.gef.presentation.Fig#classNameAndBounds()
     */
    public String classNameAndBounds() {
        if (isVisible()) {
            return super.classNameAndBounds();
        }
        return getClass().getName() + "[]";
    }

    /**
     * @return the bigport
     */
    public Fig getBigPort() {
        return bigPort;
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
            while (getFigs().size() > 1) {
                Fig f = getFigAt(1);
                removeFig(f);
            }
        }
    }
    
    
    /**
     * @see org.tigris.gef.presentation.FigGroup#addFig(org.tigris.gef.presentation.Fig)
     */
    public void addFig(Fig fig) {
        if (fig != bigPort && !(fig instanceof FigFeature)) {
            LOG.error("Illegal Fig added to a FigFeature");
            throw new IllegalArgumentException("A FigFeaturesCompartment can only contain FigFeatures, received a " + fig.getClass().getName());
        }
        super.addFig(fig);
    }
    
    abstract public void populate();
}
