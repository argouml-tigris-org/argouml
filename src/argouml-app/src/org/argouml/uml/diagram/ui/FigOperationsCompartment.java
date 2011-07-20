/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
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

import java.awt.Rectangle;
import java.util.Collection;

import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.FigOperation;

/**
 * The compartment that contains Operations and/or Receptions.
 * 
 * @author Bob Tarling
 */
public class FigOperationsCompartment extends FigCompartment {
    
    /**
     * Constructor for an Operations compartment.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     */
    public FigOperationsCompartment(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        super.populate();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEditableCompartment#getUmlCollection()
     */
    protected Collection getUmlCollection() {
        Object classifier = getOwner();
        return Model.getFacade().getOperationsAndReceptions(classifier);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEditableCompartment#getNotationType()
     */
    protected int getNotationType() {
        return NotationProviderFactory2.TYPE_OPERATION;
    }

    /**     
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     * @return a FigSingleLineTextWithNotation which can be used to 
     *                          display the text.
     */
    @Override
    protected FigSingleLineTextWithNotation createFigText(Object owner, 
            Rectangle bounds,
            DiagramSettings settings) {
        return new FigOperation(owner, bounds, settings);
    }

    @Override
    public String getName() {
        return "operations";
    }
    
    /**
     * Returns the meta type for Operation to indicate the type of model
     * element within this compartment.
     * @return a model element type
     */
    public Object getCompartmentType() {
        return Model.getMetaTypes().getOperation();
    }
}
