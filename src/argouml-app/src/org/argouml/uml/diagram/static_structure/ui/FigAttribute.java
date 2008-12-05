// $Id$
// Copyright (c) 2007-2008 The Regents of the University of California. All
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

import java.awt.Rectangle;

import org.argouml.notation.NotationProvider;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;

/**
 * Fig with specific knowledge of Attribute display. <p>
 * 
 * This class does not contain any Attribute specific functionality. 
 * But since the Feature is an abstract class in the UML metamodel,
 * it seems more logical to follow this structure here, 
 * and keep FigFeature abstract, too.
 *
 * @author Michiel
 */
public class FigAttribute extends FigFeature {

    /**
     * Constructor for FigAttribute. 
     * Build a new compartment figText of the given dimensions, within the
     * compartment described by <code>aFig</code>.
     * 
     * @param x x
     * @param y x
     * @param w w
     * @param h h
     * @param aFig the figure describing the whole compartment
     * @param np the notation provider for the text
     * @deprecated for 0.27.3 by tfmorris. Use
     * {@link #FigAttribute(Object, Rectangle, DiagramSettings, NotationProvider)}
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigAttribute(int x, int y, int w, int h, Fig aFig, 
            NotationProvider np) {
        super(x, y, w, h, aFig, np);
    }
    
    /**
     * Construct an Attribute fig.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     * @param np notation provider
     */
    public FigAttribute(Object owner, Rectangle bounds,
            DiagramSettings settings, NotationProvider np) {
        super(owner, bounds, settings, np);
    }
}
