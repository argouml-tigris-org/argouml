// $Id$
// Copyright (c) 1996-2009 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.awt.Polygon;
import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigPoly;

/** 
 * Class to display graphics for a UML model in a class diagram. 
 */
public class FigModel extends FigPackage {

    private FigPoly figPoly = new FigPoly(LINE_COLOR, SOLID_FILL_COLOR);

    private void constructFigs() {
        int[] xpoints = {125, 130, 135, 125};
        int[] ypoints = {45, 40, 45, 45};
        Polygon polygon = new Polygon(xpoints, ypoints, 4);
        figPoly.setPolygon(polygon);
        figPoly.setFilled(false);
        addFig(figPoly);

        setBounds(getBounds());
        
        updateEdges();
    }
    
    /**
     * Construct a Model fig
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigModel(Object owner, Rectangle bounds, DiagramSettings settings) {
        super(owner, bounds, settings);
        constructFigs();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    @Override
    protected void setStandardBounds(int x, int y, int w, int h) {

        if (figPoly != null) {
            Rectangle oldBounds = getBounds();
            figPoly.translate((x - oldBounds.x) + (w - oldBounds.width), y
                    - oldBounds.y);

        }
        super.setStandardBounds(x, y, w, h);
    }


}
