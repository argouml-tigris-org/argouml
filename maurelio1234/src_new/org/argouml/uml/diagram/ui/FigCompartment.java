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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigRect;

/**
 * @author Bob Tarling
 */
public abstract class FigCompartment extends FigGroup {

    private Fig bigPort;

    /**
     * The constructor.
     *
     * @param x x
     * @param y y
     * @param w width
     * @param h height
     */
    public FigCompartment(int x, int y, int w, int h) {
        bigPort = new FigRect(x, y, w, h, Color.black, Color.white);
        bigPort.setFilled(true);
        setFilled(true);

        bigPort.setLineWidth(0);
        setLineWidth(0);
        addFig(bigPort);
    }

    /**
     * @return the bigport
     */
    public Fig getBigPort() {
        return bigPort;
    }

    /**
     * The minimum width is the minimum width of the child with the widest
     * miniumum width.
     * The minimum height is the total minimum height of all child figs plus a
     * 2 pixel padding.
     * @return the minimum width
     */
    public Dimension getMinimumSize() {
        int minWidth = 0;
        int minHeight = 0;
        //set new bounds for all included figs
        Iterator figs = iterator();
        Fig fig;
        while (figs.hasNext()) {
            fig = (Fig) figs.next();
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
                fw = fig.getMinimumSize().width;
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
    
    /**
     * Create a new model element for the compartment.
     */
    protected abstract void createModelElement();
}
