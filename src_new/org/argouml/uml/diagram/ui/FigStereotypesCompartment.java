// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * A Fig designed to be the child of some FigNode or FigEdge to display the
 * stereotypes of the model element represented by the parent Fig.
 * Currently display of multiple stereotypes are stacked one on top of the
 * each enclosed by guillemets.<p>
 * 
 * The minimum width of this fig is the largest minimum width of its child
 * figs.<p>
 * 
 * The minimum height of this fig is the total minimum height of its child
 * figs.<p>
 * 
 * TODO: Allow for UML2 style display where all stereotypes are displayed in
 * the same guillemet pair and are delimited by commas. The style should be
 * changeable by calling getOrientation(Orientation). The swidget Orientation
 * class can be used for this.
 * @author Bob Tarling
 */
public class FigStereotypesCompartment extends FigCompartment {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -1696363445893406130L;
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(FigStereotypesCompartment.class);

    /**
     * One UML keyword is allowed. These are not strictly stereotypes but are
     * displayed as such. e.g. &lt;&lt;interface&gt;&gt;
     */
    private String keyword;

    private int stereotypeCount = 0;
    
    private boolean hidingStereotypesWithIcon = false;
    
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

    /*
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    @Override
    public void setOwner(Object own) {
        if (own != null) {
            super.setOwner(own);
            populate();
            Model.getPump().addModelEventListener(this, own, "stereotype");
        }
    }
    
    /*
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    @Override
    public void removeFromDiagram() {
        Model.getPump()
                .removeModelEventListener(this, getOwner(), "stereotype");
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event instanceof AddAssociationEvent) {
            AddAssociationEvent aae = (AddAssociationEvent) event;
            if (event.getPropertyName().equals("stereotype")) {
                Object stereotype = aae.getChangedValue();
                if (findFig(stereotype) == null) {
                    Fig bigPort = this.getBigPort();
                    FigText stereotypeTextFig =
                        new FigStereotype(
                                bigPort.getX() + 1,
                                bigPort.getY() + 1
                                + (++stereotypeCount)
                                * FigNodeModelElement.ROWHEIGHT,
                                0,
                                FigNodeModelElement.ROWHEIGHT - 2,
                                bigPort,
                                stereotype);
                    stereotypeTextFig.setJustification(FigText.JUSTIFY_CENTER);
                    stereotypeTextFig.setEditable(false);
                    stereotypeTextFig.setText(
                            Model.getFacade().getName(stereotype));
                    stereotypeTextFig.setOwner(stereotype);
                    addFig(stereotypeTextFig);
                    reorderStereotypeFigs();
                    damage();
                }
            } else {
                LOG.warn("Unexpected property " + event.getPropertyName());
            }
        }
        if (event instanceof RemoveAssociationEvent) {
            if (event.getPropertyName().equals("stereotype")) {
                RemoveAssociationEvent rae = (RemoveAssociationEvent) event;
                Object stereotype = rae.getChangedValue();
                Fig f = findFig(stereotype);
                if (f != null) {
                    removeFig(f);
                    --stereotypeCount;
                }
            } else {
                LOG.warn("Unexpected property " + event.getPropertyName());
            }
        }
    }

    /**
     * Keep the Figs which are likely invisible at the end of the list.
     */
    private void reorderStereotypeFigs() {
	List<Fig> allFigs = getFigs();
	List<Fig> figsWithIcon = new ArrayList<Fig>();
	List<Fig> figsWithOutIcon = new ArrayList<Fig>();
	List<Fig> others = new ArrayList<Fig>();

	for (Fig f : allFigs) {
            if (f instanceof FigStereotype) {
                FigStereotype s = (FigStereotype) f;
                if (getIconForStereotype(s) != null) {
                    figsWithIcon.add(s);
                } else {
                    figsWithOutIcon.add(s);
                }
            } else {
                others.add(f);
            }
        }

	List<Fig> n = new ArrayList<Fig>();
	
	n.addAll(others);
	n.addAll(figsWithOutIcon);
	n.addAll(figsWithIcon);	
	
	setFigs(n);
    }

    private Fig findFig(Object stereotype) {
        for (Iterator it = getFigs().iterator(); it.hasNext(); ) {
            Fig f = (Fig) it.next();
            if (f.getOwner() == stereotype) {
                return f;
            }
        }
        return null;
    }
    
    /**
     * TODO: This should become private and only called from setOwner
     *
     * @see org.argouml.uml.diagram.ui.FigEditableCompartment#populate()
     */
    public void populate() {
       
        stereotypeCount = 0;
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
                ++stereotypeCount;
                stereotypeTextFig =
                    new FigStereotype(
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
            stereotypeTextFig.setText(keyword);
            acounter++;
        }

        Collection stereos = Model.getFacade().getStereotypes(modelElement);
        if (stereos != null) {
            Iterator iter = stereos.iterator();
            while (iter.hasNext()) {
                Object stereotype = iter.next();
                if (figs.size() <= acounter) {
                    ++stereotypeCount;
                    stereotypeTextFig =
                        new FigStereotype(
                                xpos + 1,
                                ypos + 1
                                + (acounter - 1)
                                	* FigNodeModelElement.ROWHEIGHT,
                                0,
                                FigNodeModelElement.ROWHEIGHT - 2,
                                bigPort,
                                stereotype);
                    // bounds not relevant here
                    stereotypeTextFig.setJustification(FigText.JUSTIFY_CENTER);
                    stereotypeTextFig.setEditable(false);
                    addFig(stereotypeTextFig);
                } else {
                    stereotypeTextFig =
                        (CompartmentFigText) figs.get(acounter);
                }
                stereotypeTextFig.setOwner(stereotype);

                acounter++;
            }
            if (figs.size() > acounter) {
                //cleanup of unused FigText's
                for (int i = figs.size() - 1; i >= acounter; i--) {
                    removeFig((Fig) figs.get(i));
                }
            }

            reorderStereotypeFigs();
            
            // remove all stereotypes that have a graphical icon
            updateHiddenStereotypes();
        }
    }

    private void updateHiddenStereotypes() {
        List<Fig> figs = getFigs();
        for (Fig f : figs) {
            if (f instanceof FigStereotype) {
                FigStereotype fs = (FigStereotype) f;
                fs.setVisible(getIconForStereotype(fs) == null
                        || !isHidingStereotypesWithIcon());
            }
        }
    }

    private Image getIconForStereotype(FigStereotype fs) {
        Project project = getProject();
        if (project == null) {
            LOG.warn("getProject() returned null");
            return null;
        }
        return project.getProfileConfiguration().getFigNodeStrategy()
                .getIconForStereotype(fs.getOwner());
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
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

    protected void createModelElement() {
    }

    /**
     * @return true if textual stereotypes are being hidden in preference to
     *         displaying icon.
     */
    public boolean isHidingStereotypesWithIcon() {
        return hidingStereotypesWithIcon;
    }

    /**
     * Turn on/off textual stereotype display in preference to icon.
     * 
     * @param hidingStereotypesWithIcon true to hide textual stereotypes and
     *                show icon instead.
     */
    public void setHidingStereotypesWithIcon(boolean hidingStereotypesWithIcon) {
        this.hidingStereotypesWithIcon = hidingStereotypesWithIcon;
        updateHiddenStereotypes();
    }
}
