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

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;

/**
 * Presentation logic which is common to both an operations
 * compartment and an attributes compartment.
 * @author Bob Tarling
 */
public abstract class FigFeaturesCompartment extends FigCompartment {

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
     * USED BY PGML.tee
     * @see org.tigris.gef.presentation.Fig#getNameAndBounds()
     */
    public String classNameAndBounds() {
        if (isVisible()) {
            return super.classNameAndBounds();
        } else {
            return getClass().getName() + "[]";
        }
    }

    /**
     * @return the bigport
     */
    public Fig getBigPort() {
        return bigPort;
    }
}
