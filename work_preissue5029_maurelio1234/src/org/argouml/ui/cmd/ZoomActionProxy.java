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

package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;

import org.argouml.ui.ZoomSliderButton;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ZoomAction;

/**
 * An intermediate class used to check if zoom factor is within range before
 * zooming.
 * 
 * @author bszanto (Bogdan Szanto)
 */
public class ZoomActionProxy extends ZoomAction {

    /**
     * Local instance of the magnitude that alows computation of zoom factor
     * before zooming.
     */
    private double zoomFactor;

    /**
     * Constructor calling the org.tigris.gef.base.ZoomAction constructor.
     * 
     * @param zF The zoom factor.
     */
    public ZoomActionProxy(double zF) {
        super(zF);
        zoomFactor = zF;
    }

    /**
     * Tests if zoom factor values are ok before zooming.
     * 
     * @param arg0 The action event to be transmitted.
     * @see org.tigris.gef.base.ZoomAction#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        Editor ed = Globals.curEditor();
        if (ed == null) return;

        if ((zoomFactor == 0)
                || ((ed.getScale() * zoomFactor 
                    < ZoomSliderButton.MINIMUM_ZOOM / 100) 
                && ed.getScale() * zoomFactor 
                    < ZoomSliderButton.MAXIMUM_ZOOM / 100)) {
            super.actionPerformed(arg0);
        }
    }

}
