/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */
// $Id$
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

package org.argouml.uml.diagram.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.kernel.Project;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * A Fig designed to be the child of some FigNode or FigEdge to display the
 * stereotypes of the model element represented by the parent Fig.
 * Currently, multiple stereotypes are shown stacked one on top of the other,
 * each enclosed by guillemets.<p>
 *
 * The minimum width of this fig is the largest minimum width of its child
 * figs. The minimum height of this fig is the total minimum height of its child
 * figs.<p>
 *
 * The owner of this Fig is the UML element that is extended
 * with the stereotypes. We are listening to changes to the model:
 * addition and removal of stereotypes. <p>
 *
 * This fig supports showing one keyword
 * as the first "stereotype" in the list. <p>
 *
 * There is no way to remove a keyword fig, once added. <p>
 *
 * TODO: Allow for UML2 style display where all stereotypes are displayed in
 * the same guillemet pair and are delimited by commas. The style should be
 * changeable by calling getOrientation(Orientation). The swidget Orientation
 * class can be used for this.
 * @author Bob Tarling
 */
public class FigStereotypesGroup extends ArgoFigGroup {

    private Fig bigPort;

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(FigStereotypesGroup.class.getName());

    /**
     * One UML keyword is allowed. These are not strictly stereotypes but are
     * displayed as such. e.g. &lt;&lt;interface&gt;&gt;
     */
    private String keyword;

    private int stereotypeCount = 0;

    private boolean hidingStereotypesWithIcon = false;

    private void constructFigs(int x, int y, int w, int h) {
        bigPort = new FigRect(x, y, w, h, LINE_COLOR, FILL_COLOR);
        addFig(bigPort);
        bigPort.setFilled(false);

        /* Do not show border line, make transparent: */
        setLineWidth(0);
        setFilled(false);
    }

    /**
     * The constructor.
     *
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     */
    public FigStereotypesGroup(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, settings);
        constructFigs(bounds.x, bounds.y, bounds.width, bounds.height);
        Model.getPump().addModelEventListener(this, owner, "stereotype");
        populate();
    }


    /*
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    @Override
    public void removeFromDiagram() {
        /* Remove all items in the group,
         * otherwise the model event listeners remain:
         * TODO: Why does a FigGroup not do this? */
        for (Object f : getFigs()) {
            ((Fig) f).removeFromDiagram();
        }
        super.removeFromDiagram();
        Model.getPump()
                .removeModelEventListener(this, getOwner(), "stereotype");
    }

    /**
     * @return the bigport
     * @deprecated for 0.27.2. For backward compatibility only. The visibility
     *             of this method will be changed to private in the next release
     *             when FigStereotypesCompartment is removed.
     */
    @Deprecated
    protected Fig getBigPort() {
        return bigPort;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event instanceof AddAssociationEvent) {
            AddAssociationEvent aae = (AddAssociationEvent) event;
            if (event.getPropertyName().equals("stereotype")) {
                Object stereotype = aae.getChangedValue();
                if (findFig(stereotype) == null) {
                    FigText stereotypeTextFig =
                        new FigStereotype(stereotype,
                                getBoundsForNextStereotype(),
                                getSettings());
                    stereotypeCount++;
                    addFig(stereotypeTextFig);
                    reorderStereotypeFigs();
                    damage();
                }
            } else {
                LOG.log(Level.WARNING, "Unexpected property " + event.getPropertyName());
            }
        }
        if (event instanceof RemoveAssociationEvent) {
            if (event.getPropertyName().equals("stereotype")) {
                RemoveAssociationEvent rae = (RemoveAssociationEvent) event;
                Object stereotype = rae.getChangedValue();
                Fig f = findFig(stereotype);
                if (f != null) {
                    removeFig(f);
                    f.removeFromDiagram(); // or vice versa?
                    --stereotypeCount;
                }
            } else {
                LOG.log(Level.WARNING, "Unexpected property " + event.getPropertyName());
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

	// TODO: This doesn't do anything special with keywords.
	// They should probably go first.
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

    private FigStereotype findFig(Object stereotype) {
        for (Object f : getFigs()) {
            if (f instanceof FigStereotype) {
                FigStereotype fs = (FigStereotype) f;
                if (fs.getOwner() == stereotype) {
                    return fs;
                }
            }
        }
        return null;
    }

    /**
     * Get all the child figs that represent the individual stereotypes
     * @return a List of the stereotype Figs
     */
    List<FigStereotype> getStereotypeFigs() {
        final List<FigStereotype> stereotypeFigs =
            new ArrayList<FigStereotype>();
        for (Object f : getFigs()) {
            if (f instanceof FigStereotype) {
                FigStereotype fs = (FigStereotype) f;
                stereotypeFigs.add(fs);
            }
        }
        return stereotypeFigs;
    }

    private FigKeyword findFigKeyword() {
        for (Object f : getFigs()) {
            if (f instanceof FigKeyword) {
                return (FigKeyword) f;
            }
        }
        return null;
    }

    /**
     * TODO: This should become private and only called from constructor
     *
     * @see org.argouml.uml.diagram.ui.FigCompartment#populate()
     */
    public void populate() {

        stereotypeCount = 0;
        Object modelElement = getOwner();
        if (modelElement == null) {
            // TODO: This block can be removed after issue 4075 is tackled
            LOG.log(Level.FINE, "Cannot populate the stereotype compartment "
                     + "unless the parent has an owner.");
            return;
        }

        if (LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "Populating stereotypes compartment for {0}",
                    Model.getFacade().getName(modelElement));
        }

        /* This will contain the Figs that we do not need anymore: */
        Collection<Fig> removeCollection = new ArrayList<Fig>(getFigs());

        //There is one fig more in the group than (stereotypes + keyword):
        if (keyword != null) {
            FigKeyword keywordFig = findFigKeyword();
            if (keywordFig == null) {
                // The keyword fig does not exist yet.
                // Let's create one:
                keywordFig =
                    new FigKeyword(keyword,
                            getBoundsForNextStereotype(),
                            getSettings());
                // bounds not relevant here
                addFig(keywordFig);
            } else {
                // The keyword fig already exists.
                removeCollection.remove(keywordFig);
            }
            ++stereotypeCount;
        }

        for (Object stereo : Model.getFacade().getStereotypes(modelElement)) {
            FigStereotype stereotypeTextFig = findFig(stereo);
            if (stereotypeTextFig == null) {
                stereotypeTextFig =
                    new FigStereotype(stereo,
                            getBoundsForNextStereotype(),
                            getSettings());
                // bounds not relevant here
                addFig(stereotypeTextFig);
            } else {
             // The stereotype fig already exists.
                removeCollection.remove(stereotypeTextFig);
            }
            ++stereotypeCount;
        }

        //cleanup of unused FigText's
        for (Fig f : removeCollection) {
            if (f instanceof FigStereotype || f instanceof FigKeyword) {
                removeFig(f);
            }
        }

        reorderStereotypeFigs();

        // remove all stereotypes that have a graphical icon
        updateHiddenStereotypes();

    }

    /**
     * Get the number of stereotypes contained in this group
     * @return the number of stereotypes in this group
     */
    public int getStereotypeCount() {
        return stereotypeCount;
    }

    private Rectangle getBoundsForNextStereotype() {
        return new Rectangle(
                bigPort.getX() + 1,
                bigPort.getY() + 1
                + (stereotypeCount
                * ROWHEIGHT),
                0,
                ROWHEIGHT - 2);
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
        // TODO: Find a way to replace this dependency on Project
        Project project = getProject();
        if (project == null) {
            LOG.log(Level.WARNING, "getProject() returned null");
            return null;
        }
        Object owner = fs.getOwner();
        if (owner == null) {
            // keywords which look like a stereotype (e.g. <<interface>>) have
            // no owner
            return null;
        } else {
            return project.getProfileConfiguration().getFigNodeStrategy()
                    .getIconForStereotype(owner);
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
    protected void setBoundsImpl(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();

        Dimension minimumSize = getMinimumSize();
        int newW = Math.max(w, minimumSize.width);
        int newH = Math.max(h, minimumSize.height);

        int yy = y;
        for  (Fig fig : (Collection<Fig>) getFigs()) {
            if (fig != bigPort) {
                fig.setBounds(x, yy, newW,
                              fig.getMinimumSize().height);
                yy += fig.getMinimumSize().height;
            }
        }
        bigPort.setBounds(x, y, newW, newH);
        calcBounds();
        firePropChange("bounds", oldBounds, getBounds());
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
        populate();
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
     * @param hideStereotypesWithIcon true to hide textual stereotypes and
     *                show icon instead.
     */
    public void setHidingStereotypesWithIcon(boolean hideStereotypesWithIcon) {
        this.hidingStereotypesWithIcon = hideStereotypesWithIcon;
        updateHiddenStereotypes();
    }

    @Override
    public Dimension getMinimumSize() {
        // if there are no stereotypes, we return (0,0), preventing
        // double lines in the class (see issue 4939)
        Dimension dim = null;
        Object modelElement = getOwner();

        if (modelElement != null) {
            List<FigStereotype> stereos = getStereotypeFigs();
            if (stereos.size() > 0 || keyword != null) {
                int minWidth = 0;
                int minHeight = 0;
                //set new bounds for all included figs
                for (Fig fig : (Collection<Fig>) getFigs()) {
                    if (fig.isVisible() && fig != bigPort) {
                        int fw = fig.getMinimumSize().width;
                        if (fw > minWidth) {
                            minWidth = fw;
                        }
                        minHeight += fig.getMinimumSize().height;
                    }
                }

                minHeight += 2; // 2 Pixel padding after compartment
                dim = new Dimension(minWidth, minHeight);
            }
        }
        if (dim == null) {
            dim = new Dimension(0, 0);
        }
        return dim;
    }
}
