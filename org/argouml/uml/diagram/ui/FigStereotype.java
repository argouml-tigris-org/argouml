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

import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * A specialist FigText for display stereotypes.
 * @author Bob Tarling
 */
public class FigStereotype extends FigGroup {

    private FigSingleLineText singleStereotype;

    /**
     * The constructor.
     *
     * @param x x
     * @param y y
     * @param w width
     * @param h height
     * @param expandOnly true if the fig can only grow, not shrink
     */
    public FigStereotype(int x, int y, int w, int h, boolean expandOnly) {
        super();
        singleStereotype = new FigSingleLineText(x, y, w, h, true);
        singleStereotype.setEditable(false);
        singleStereotype.setJustification(FigText.JUSTIFY_CENTER);
        singleStereotype.setLineWidth(0);
        singleStereotype.setFilled(true);
        singleStereotype.setVisible(true);
        singleStereotype.setFont(FigNodeModelElement.getLabelFont());
        singleStereotype.setTextColor(Color.black);
        addFig(singleStereotype);
    }


    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int arg0) {
        super.setLineWidth(0);
    }
    
    public void setText(String text) {
        singleStereotype.setText(text);
    }
    
    public int getStereotypeCount() {
        if (singleStereotype.getText() == null
                || singleStereotype.getText().trim().length() == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#isVisible()
     */
    public boolean isVisible() {
        if (singleStereotype.getText() == null
                || singleStereotype.getText().trim().length() == 0) {
            return false;
        }
        return super.isVisible();
    }
}
