/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thn
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.notation.providers;

import java.beans.PropertyChangeEvent;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.NotationProvider;

/**
 * This abstract class forms the basis of all Notation providers
 * for the text shown in the operation compartment of a Class.
 * Subclass this for all languages.
 *
 * @author Michiel van der Wulp
 */
public abstract class OperationNotation extends NotationProvider {

    /**
     * The constructor.
     *
     * @param operation The operation.
     */
    public OperationNotation(Object operation) {
        if (!Model.getFacade().isAOperation(operation) 
                && !Model.getFacade().isAReception(operation)) {
            throw new IllegalArgumentException(
                    "This is not an Operation or Reception.");
        }
    }

    @Override
    public void initialiseListener(Object modelElement) {
        addElementListener(modelElement);
        if (Model.getFacade().isAOperation(modelElement)) {
            // We also show stereotypes
            for (Object uml : Model.getFacade().getStereotypes(modelElement)) {
                addElementListener(uml);
            }
            // We also show parameters
            for (Object uml : Model.getFacade().getParameters(modelElement)) {
                addElementListener(uml);
                // We also show the type (of which e.g. the name may change)
                Object type = Model.getFacade().getType(uml);
                if (type != null) {
                    addElementListener(type);
                }
            }
            // We also show tagged values for UML 1
            // TODO: what to do for UML2 here?
            if ( Model.getFacade().getUmlVersion().charAt(0) == '1') {
                for (Object uml : Model.getFacade().getTaggedValuesCollection(
                        modelElement)) {
                    addElementListener(uml);     
                }   
            }
        }
    }

    @Override
    public void updateListener(Object modelElement, PropertyChangeEvent pce) {
        if (pce.getSource() == modelElement
                && ("stereotype".equals(pce.getPropertyName()) 
                        || "parameter".equals(pce.getPropertyName())
                        || "taggedValue".equals(pce.getPropertyName()))) {
            if (pce instanceof AddAssociationEvent) {
                addElementListener(pce.getNewValue());
            }
            if (pce instanceof RemoveAssociationEvent) {
                removeElementListener(pce.getOldValue());
            }
        }
        if (!Model.getUmlFactory().isRemoved(modelElement)) {
            //  We also show types of parameters
            for (Object param : Model.getFacade().getParameters(modelElement)) {
                if (pce.getSource() == param
                        && ("type".equals(pce.getPropertyName()))) {
                    if (pce instanceof AddAssociationEvent) {
                        addElementListener(pce.getNewValue());
                    }
                    if (pce instanceof RemoveAssociationEvent) {
                        removeElementListener(pce.getOldValue());
                    }
                }
            }
        }
    }

}
