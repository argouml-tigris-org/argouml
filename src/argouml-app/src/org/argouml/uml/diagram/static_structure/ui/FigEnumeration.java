/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Selection;

/**
 * Class to display graphics for a UML Enumeration in a diagram.
 * It depends on FigDataType for most of its behavior.<p>
 * 
 * The Fig for an Enumeration has a compartment for Literals 
 * above the Operations compartment.
 */
public class FigEnumeration extends FigDataType {

    /**
     * Constructor.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     */
    public FigEnumeration(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);

    }

    @Override
    protected String getKeyword() {
        return "enumeration";
    }

    @Override
    public void renderingChanged() {
        super.renderingChanged();

        if (isCompartmentVisible(Model.getMetaTypes().getEnumerationLiteral())) {
            updateCompartment(Model.getMetaTypes().getEnumerationLiteral());
        }
    }

    @Override
    public Selection makeSelection() {
        return new SelectionEnumeration(this);
    }

    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        Set<Object[]> lst = new HashSet<Object[]>();
        if (newOwner != null) {
            // add the listeners to the newOwner
            lst.add(new Object[] {newOwner, null});
            // and its stereotypes
            for (Object stereo : Model.getFacade().getStereotypes(newOwner)) {
                lst.add(new Object[] {stereo, null});                
            }
            // and its features
            for (Object feat : Model.getFacade().getFeatures(newOwner)) {
                lst.add(new Object[] {feat, null});
                // and the stereotypes of its features
                for (Object stereo : Model.getFacade().getStereotypes(feat)) {
                    lst.add(new Object[] {stereo, null});
                }
            }
            // and its enumerationLiterals
            for (Object literal : Model.getFacade().getEnumerationLiterals(
                    newOwner)) {
                lst.add(new Object[] {literal, null});
            }
        }
        // And now add listeners to them all:
        updateElementListeners(lst);

    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    @Override
    public String classNameAndBounds() {
        return super.classNameAndBounds()
                + "enumerationLiteralsVisible="
                + isCompartmentVisible(
                        Model.getMetaTypes().getEnumerationLiteral());
    }
} 
