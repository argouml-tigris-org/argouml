// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import org.apache.log4j.Logger;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * A specialist FigText for display stereotypes.
 *
 * @author Bob Tarling
 */
public class FigStereotypeText extends FigGroup {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(FigComment.class);

    /**
     * Height in pixels of stereotype text.
     */
    protected static final int STEREOHEIGHT = 18;

    private String pseudoStereotype;

    /**
     * The constructor.
     *
     * @param x
     *            x
     * @param y
     *            y
     * @param w
     *            width
     * @param h
     *            height
     * @param expandOnly
     *            true if the fig can only grow, not shrink
     */
    public FigStereotypeText(int x, int y, int w, int h, boolean expandOnly) {
        super();
        addStereotypeText("stereo", x, y);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int arg0) {
        super.setLineWidth(0);
    }

    /**
     * Allows a parent Fig to specify some stereotype text to display that is
     * not actually contained by its owner.
     * An example of this usage is to display <<interface>> as a stereotype
     * on FigInterface.
     * @param stereotype the text of the pseudo stereotype
     */
    public void setPseudoSereotype(String stereotype) {
        pseudoStereotype = stereotype;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object modelElement) {
        super.setOwner(modelElement);

        if (modelElement == null) {
            setVisible(false);
            return;
        }

        setVisible(true);

        this.removeAll();


//        int xPosn = getX();
//        int yPosn = getY();

//        if (pseudoStereotype != null) {
//            addStereotypeText(pseudoStereotype, xPosn, yPosn);
//            yPosn += STEREOHEIGHT;
//        }
//
//        Iterator it = Model.getFacade().getStereotypes(getOwner()).iterator();
//        while (it.hasNext()) {
//            Object stereotype = it.next();
//            addStereotypeText(Model.getFacade().getName(stereotype), xPosn, 
//                              yPosn);
//            yPosn += STEREOHEIGHT;
//        }
    }

    private void addStereotypeText(String text, int xPosn, int yPosn) {
        FigSingleLineText singleStereotype = new FigSingleLineText(xPosn,
                yPosn, getWidth(), STEREOHEIGHT, true);
        singleStereotype.setEditable(false);
        singleStereotype.setJustification(FigText.JUSTIFY_CENTER);
        singleStereotype.setLineWidth(0);
        singleStereotype.setFilled(true);
        singleStereotype.setVisible(true);
        singleStereotype.setFont(FigNodeModelElement.getLabelFont());
        singleStereotype.setTextColor(Color.black);
        // TODO: Use message formatting here
        singleStereotype
                .setText("<<" + (text == null ? "(anon)" : text) + ">>");
        LOG.info("Adding " + singleStereotype.getText() + " to Fig");
        addFig(singleStereotype);
    }
}
