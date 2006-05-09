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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.notation.NotationHelper;
import org.argouml.uml.diagram.static_structure.ui.FigFeature;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * A Fig designed to be the child of some FigNode or FigEdge to display the
 * stereotypes of the model element represented by the parent Fig.
 * Currently display of multiple stereotypes are stacked one on top of the
 * each enclosed by guillemots.<p>
 * 
 * The minimum width of this fig is the largest minimum width of its child
 * figs.<p>
 * 
 * The minimum height of this fig is the total minimum height of its child
 * figs.<p>
 * 
 * TODO: Allow for UML2 style display where all stereotypes are displayed in
 * the same guillemot pair and are delimited by commas. The style should be
 * changable by calling getOrientation(Orientation). The swidget Orientation
 * class can be used for this.
 * @author Bob Tarling
 */
public class FigStereotypesCompartment extends FigCompartment {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionAddStereotype.class);

    /**
     * One UML keyword is allowed. These are not strictly stereotypes but are
     * displayed as such. e.g. &lt;&lt;interface&gt;&gt;
     */
    private String keyword;

    /**
     * The constructor.
     *
     * @param x x
     * @param y y
     * @param w width
     * @param h height
     */
    public FigStereotypesCompartment(int x, int y, int w, int h) {
        super(x, y, w, h);
        setFilled(false);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object own) {
        super.setOwner(own);
        populate();
    }

    /**
     * TODO: This needs more work!
     *
     * @see org.argouml.uml.diagram.ui.FigFeaturesCompartment#populate()
     */
    public void populate() {
        
        Object modelElement = getOwner();
        if (modelElement == null) {
            // TODO: This block can be removed after issue 4075 is tackled
            LOG.debug("Cannot populate the stereotype compartment "
                     + "unless the parent has an owner.");
            return;
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("Populating stereotypes compartment for "
                    + Model.getFacade().getName(modelElement));
        }
        
        int acounter = 1;
        Fig bigPort = this.getBigPort();
        int xpos = bigPort.getX();
        int ypos = bigPort.getY();

        List figs = getFigs();
        CompartmentFigText stereotypeTextFig;

        if (keyword != null) {
            if (figs.size() <= acounter) {
                stereotypeTextFig =
                    new FigFeature(
                            xpos + 1,
                            ypos + 1
                            + (acounter - 1)
                                * FigNodeModelElement.ROWHEIGHT,
                            0,
                            FigNodeModelElement.ROWHEIGHT - 2,
                            bigPort,
                            null);
                // bounds not relevant here
                stereotypeTextFig.setJustification(FigText.JUSTIFY_CENTER);
                stereotypeTextFig.setEditable(false);
                addFig(stereotypeTextFig);
            } else {
                stereotypeTextFig =
                    (CompartmentFigText) figs.get(acounter);
            }
            stereotypeTextFig.setText(
                    NotationHelper.getLeftGuillemot()
                    + keyword
                    + NotationHelper.getRightGuillemot());
            acounter++;
        }

        Collection stereos = Model.getFacade().getStereotypes(modelElement);
        if (stereos != null) {
            Iterator iter = stereos.iterator();
            while (iter.hasNext()) {
                Object stereotype = iter.next();
                if (figs.size() <= acounter) {
                    stereotypeTextFig =
                        new FigFeature(
                                xpos + 1,
                                ypos + 1
                                + (acounter - 1)
                                	* FigNodeModelElement.ROWHEIGHT,
                                0,
                                FigNodeModelElement.ROWHEIGHT - 2,
                                bigPort,
                                null);
                    // bounds not relevant here
                    stereotypeTextFig.setJustification(FigText.JUSTIFY_CENTER);
                    stereotypeTextFig.setEditable(false);
                    addFig(stereotypeTextFig);
                } else {
                    stereotypeTextFig =
                        (CompartmentFigText) figs.get(acounter);
                }
                stereotypeTextFig.setText(NotationHelper.getLeftGuillemot()
                        + Model.getFacade().getName(stereotype)
                        + NotationHelper.getRightGuillemot());
                stereotypeTextFig.setOwner(stereotype);

                acounter++;
            }
            if (figs.size() > acounter) {
                //cleanup of unused FigText's
                for (int i = figs.size() - 1; i >= acounter; i--) {
                    removeFig((Fig) figs.get(i));
                }
            }
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {
        Fig fig;
        int yy = y;
        Iterator figs = iterator();
        while (figs.hasNext()) {
            fig = (Fig) figs.next();
            if (fig != getBigPort()) {
                fig.setBounds(x + 1, yy + 1, w - 2,
                              fig.getMinimumSize().height);
                yy += fig.getMinimumSize().height;
            }
        }
        getBigPort().setBounds(x, y, w, h);
        calcBounds();
    }

    /**
     * Allows a parent Fig to specify some keyword text to display amongst the
     * stereotypes.
     * An example of this usage is to display &lt;&lt;interface&gt;&gt;
     * on FigInterface.
     * @param word the text of the pseudo stereotype
     */
    public void setKeyword(String word) {
        keyword = word;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -1696363445893406130L;
}
