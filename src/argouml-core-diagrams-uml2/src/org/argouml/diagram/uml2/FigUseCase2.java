// $Id: FigUseCase.java 17045 2009-04-05 16:52:52Z mvw $
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
import org.argouml.uml.diagram.use_case.ui.FigUseCase;

/**
 * A fig to display use cases on use case diagrams.<p>
 *
 * Realised as a solid oval containing the name of the use
 * case. Optionally may be split into two compartments, with the lower
 * compartment displaying the extension points for the use case.<p>
 *
 * Implements all interfaces through its superclasses.<p>
 *
 * There is some coordinate geometry to be done to fit rectangular
 * text boxes inside an ellipse. The rectangular text box contains the
 * name and any extension points if shown, and is deemed to be of
 * height <em>2h</em> and width <em>2w</em>. We allow a margin of
 * <em>p</em> above the top and below the bottom of the box, so we
 * know the height of the ellipse, <em>2b</em> = <em>2h</em> +
 * <em>2p</em>.<p>
 *
 * The formula for an ellipse of width <em>2a</em> and height
 * <em>2b</em>, centred on the origin, is<p>
 *
 * <em>x</em>^2/<em>a</em>^2 + <em>y</em>^2/<em>b</em>^2 = 1.<p>
 *
 * We know that a corner of the rectangle is at coordinate
 * (<em>w</em>,<em>h</em>), since the rectangle must also be centered
 * on the origin to fit within the ellipse. Substituting these values
 * for <em>x</em> and <em>y</em> in the formula above, we can compute
 * <em>a</em>, half the width of the ellipse, since we know
 * <em>b</em>.<p>
 *
 * <em>a</em> = <em>wb</em>/sqrt(<em>b</em>^2 - <em>h</em>^2).<p>
 *
 * But <em>b</em> was defined in terms of the height of the rectangle
 * plus agreed padding at the top, so we can write.<p>
 *
 * <em>a</em> = (<em>wh</em> + <em>wb</em>)/
 *                 sqrt(2<em>hp</em> + <em>p</em>^2)<p>
 *
 * Given we now know <em>a</em> and <em>b</em>, we can find the
 * coordinates of any partition line required between use case name
 * and extension points.<p>
 *
 * Finally we need to transform our coordinates, to recognise that the
 * origin is at our top left corner, and the Y coordinates are
 * reversed.<p>
 */
class FigUseCase2 extends FigUseCase {

    /**
     * Construct a use case figure with the given owner, bounds, and rendering 
     * settings.  This constructor is used by the PGML parser.
     * 
     * @param owner owning model element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigUseCase2(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
    }
}
