// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Font;
import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;

/**
 * A FigMultiLineText that handles cases where the projectsettings 
 * indicate that the node name should be in bold. <p>
 * 
 * Since this Fig follows the setting "Show name of NODES in bold font",
 * it would be wise to use it for nodes only. See issue 5013.
 *
 * @author Michiel
 */
public class FigMultiLineTextWithBold extends FigMultiLineText {

    /**
     * @param x location x
     * @param y location y
     * @param w width
     * @param h height
     * @param expandOnly impacts behavior
     * @deprecated for 0.27.3 by tfmorris.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigMultiLineTextWithBold(int x, int y, int w, int h,
            boolean expandOnly) {
        super(x, y, w, h, expandOnly);
    }

    /**
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     * @param expandOnly true if fig should never shrink
     */
    public FigMultiLineTextWithBold(Object owner, Rectangle bounds, 
            DiagramSettings settings, boolean expandOnly) {
        super(owner, bounds, settings, expandOnly);
    }
    
    @Override
    protected int getFigFontStyle() {
        boolean showBoldName = getSettings().isShowBoldNames();
        int boldStyle =  showBoldName ? Font.BOLD : Font.PLAIN;

        return super.getFigFontStyle() | boldStyle;
    }
}
