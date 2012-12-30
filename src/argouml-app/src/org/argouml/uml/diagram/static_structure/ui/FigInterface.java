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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;

/**
 * Class to display graphics for a UML Interface in a diagram.
 * <p>
 * An Interface may show stereotypes and a compartment for
 * operations. Attributes are not supported in ArgoUML.
 */
public class FigInterface extends FigClassifierBox {

    private static final Logger LOG =
        Logger.getLogger(FigInterface.class.getName());

    /**
     * Initialization common to multiple constructors.
     */
    private void initialize(Rectangle bounds) {
        // Put all the bits together, suppressing bounds calculations until
        // we're all done for efficiency.
        enableSizeChecking(false);
        setSuppressCalcBounds(true);

        getStereotypeFig().setKeyword("interface");
        getStereotypeFig().setVisible(true);
        /* The next line is needed so that we have the right dimension
         * when drawing this Fig on the diagram by pressing down
         * the mouse button, even before releasing the mouse button: */
        getNameFig().setTopMargin(
                getStereotypeFig().getMinimumSize().height);

        addFig(getBigPort());
        addFig(getNameFig());
        // stereotype fig covers the name fig:
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

        // Set the bounds of the figure to the total of the above
        setBounds(getBounds());
        enableSizeChecking(true);
    }

    /**
     * Construct an Interface fig
     *
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigInterface(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initialize(bounds);
    }

    @Override
    public Selection makeSelection() {
        return new SelectionInterface(this);
    }

    @Override
    public void setEnclosingFig(Fig encloser) {
        Fig oldEncloser = getEnclosingFig();

        if (encloser == null
                || (encloser != null
                && !Model.getFacade().isAInstance(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }
        if (!(Model.getFacade().isAModelElement(getOwner()))) {
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
                    "could not set package at " + encloser,
                    e);
        }

        // The next if-clause is important for the Deployment-diagram
        // it detects if the enclosing fig is a component, in this case
        // the container will be set for the owning Interface
        if (encloser != null
                && (Model.getFacade().isAComponent(encloser.getOwner()))) {
            moveIntoComponent(encloser);
            super.setEnclosingFig(encloser);
        }
    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     * TODO: Is this not duplicate with the parent?
     */
    @Override
    public String classNameAndBounds() {
        return super.classNameAndBounds()
                + "operationsVisible="
                + isCompartmentVisible(Model.getMetaTypes().getOperation());
    }

    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        Set<Object[]> listeners = new HashSet<Object[]>();

        // Collect the set of model elements that we want to listen to
        if (newOwner != null) {
            // TODO: Because we get called on each and every change event, when
            // the model is in a state of flux, we'll often get an
            // InvalidElementException before we finish this collection. The
            // only saving grace is that we're called SO many times that on the
            // last time, things should be stable again and we'll get a good set
            // of elements for the final update.  We need a better mechanism.

            // add the listeners to the newOwner
            listeners.add(new Object[] {newOwner, null});

            // and its stereotypes
            // TODO: Aren't stereotypes handled elsewhere?
            for (Object stereotype
                    : Model.getFacade().getStereotypes(newOwner)) {
                listeners.add(new Object[] {stereotype, null});
            }

            // and its features
            for (Object feat : Model.getFacade().getFeatures(newOwner)) {
                listeners.add(new Object[] {feat, null});
                // and the stereotypes of its features
                for (Object stereotype
                        : Model.getFacade().getStereotypes(feat)) {
                    listeners.add(new Object[] {stereotype, null});
                }
                // and the parameter of its operations
                if (Model.getFacade().isAOperation(feat)) {
                    for (Object param : Model.getFacade().getParameters(feat)) {
                        listeners.add(new Object[] {param, null});
                        /* Testing: Add a parameter to an operation on an Interface.
                         * Does the Interface Fig adapt its width? */
                    }
                }
            }
        }

        // Update the listeners to match the desired set using the minimal
        // update facility
        updateElementListeners(listeners);
    }

}
