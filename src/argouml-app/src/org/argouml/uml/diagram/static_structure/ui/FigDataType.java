/* $Id$
 *******************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;

/**
 * Class to display graphics for a UML DataType in a diagram.
 * (much of this code is cloned from FigInterface - perhaps we need
 * a more generic class provide this common code).
 * <p>
 * A DataType shows a keyword, a name, stereotypes and
 * an operations compartment. Attributes are not supported
 * in ArgoUML. <p>
 *
 * Every DataType shows a keyword, but it is not
 * always <<datatype>>, e.g. for an Enumeration. <p>
 *
 * There is no need for a specific minimal width for this Fig,
 * since its width will most of the time be determined by the keyword.<p>
 *
 * This Fig supports a compartment for operations at the bottom.
 * Other compartments may be added above it by specializations
 * of the DataType Fig by overriding the addExtraCompartments() method.
 */
public class FigDataType extends FigClassifierBox {

    private static final Logger LOG =
        Logger.getLogger(FigDataType.class.getName());

    private void constructFigs(Rectangle bounds) {
        enableSizeChecking(false);
        setSuppressCalcBounds(true);

        getStereotypeFig().setKeyword(getKeyword());
        getStereotypeFig().setVisible(true);
        /* The next line is needed so that we have the right dimension
         * when drawing this Fig on the diagram by pressing down
         * the mouse button, even before releasing the mouse button: */
        getNameFig().setTopMargin(
                getStereotypeFig().getMinimumSize().height);

        addFig(getBigPort());
        addFig(getNameFig());
        addFig(getStereotypeFig());
        createCompartments();

        // Make all the parts match the main fig
        setFilled(true);
        setFillColor(FILL_COLOR);
        setLineColor(LINE_COLOR);
        setLineWidth(LINE_WIDTH);

        /* Set the drop location in the case of D&D: */
        if (bounds != null) {
            setLocation(bounds.x, bounds.y);
        }

        setSuppressCalcBounds(false);

        setBounds(getBounds());
        enableSizeChecking(true);
    }

    protected  void addExtraCompartments() {
        /* Do nothing by default. */
    }

    /**
     * Primary constructor for a {@link FigDataType}.
     *
     * Parent {@link org.argouml.uml.diagram.ui.FigNodeModelElement}
     * will have created the main box {@link #getBigPort()} and
     * its name {@link #getNameFig()} and stereotype
     * (@link #getStereotypeFig()}. The FigClassifierBox
     * created a box for the operations.<p>
     *
     * The properties of all these graphic elements are adjusted
     * appropriately.
     *
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     */
    public FigDataType(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        constructFigs(bounds);
    }

    /**
     * This function shall return the keyword to be used by the constructor. <p>
     *
     * Subclasses of DataType shall
     * override this method to set their own keyword.
     *
     * @return the string to be used as the keyword
     */
    protected String getKeyword() {
        return "datatype";
    }

    @Override
    public Selection makeSelection() {
        return new SelectionDataType(this);
    }

    @Override
    public void setEnclosingFig(Fig encloser) {
        Fig oldEncloser = getEnclosingFig();

        if (encloser == null
                || (encloser != null
                && !Model.getFacade().isAInstance(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }
        if (!(Model.getFacade().isAUMLElement(getOwner()))) {
            return;
        }
        /* If this fig is not visible, do not adapt the UML model!
         * This is used for deleting. See issue 3042.
         */
        if  (!isVisible()) {
            return;
        }
        Object me = getOwner();
        Object m = null;

        try {
            // If moved into an Package
            if (encloser != null
                    && oldEncloser != encloser
                    && Model.getFacade().isAPackage(encloser.getOwner())) {
                Model.getCoreHelper().setNamespace(me, encloser.getOwner());
            }

            // If default Namespace is not already set
            if (Model.getFacade().getNamespace(me) == null
                    && (TargetManager.getInstance().getTarget()
                    instanceof ArgoDiagram)) {
                m =
                    ((ArgoDiagram) TargetManager.getInstance().getTarget())
                        .getNamespace();
                Model.getCoreHelper().setNamespace(me, m);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE,
                    "could not set package due to:" + e
                    + "' at " + encloser, e);
        }

    }

}
