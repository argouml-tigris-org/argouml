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

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectSettings;

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
     */
    public FigMultiLineTextWithBold(int x, int y, int w, int h,
            boolean expandOnly) {
        super(x, y, w, h, expandOnly);
    }

    @Override
    protected int getFigFontStyle() {
        boolean showBoldName = false;
        Project p = getProject();
        if (p != null) {
            ProjectSettings ps = p.getProjectSettings();
            showBoldName = ps.getShowBoldNamesValue();
        }
        int boldStyle =  showBoldName ? Font.BOLD : Font.PLAIN;

        return super.getFigFontStyle() | boldStyle;
    }
}
