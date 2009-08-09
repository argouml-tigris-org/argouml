// $Id: FigClass.java 17045 2009-04-05 16:52:52Z mvw $
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

package org.argouml.diagram.uml2;

import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.FigClass;

/**
 * Class to display graphics for a UML Class in a diagram.<p>
 * 
 * A Class may show compartments for stereotypes,
 * attributes and operations.
 */
class FigClass2 extends FigClass {

    /**
     * Constructor for a {@link FigClass2}.<p>
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
     * coded. The overall figure is placed at location (10,10). 
     * The stereotype compartment is created 15 pixels
     * high in the parent, but we change it to 19 pixels, 1 more than
     * ({@link #STEREOHEIGHT} here. The attribute and operations boxes
     * are created at 19 pixels, 2 more than {@link #ROWHEIGHT}.<p>
     * 
     * @param element model element to be represented by this fig.
     * @param bounds rectangle describing bounds
     * @param settings rendering settings
     */
    public FigClass2(Object element, Rectangle bounds, 
            DiagramSettings settings) {
        super(element, bounds, settings);
    }
}
