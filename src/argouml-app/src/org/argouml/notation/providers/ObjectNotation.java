/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *    Bob Tarling
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
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;

/**
 * This abstract class forms the basis of all Notation providers
 * for the text shown in the Fig that represents an Object.
 * Subclass this for all languages.
 *
 * @author Michiel van der Wulp
 */
public abstract class ObjectNotation extends NotationProvider {

    /**
     * The constructor.
     *
     * @param theObject the UML element
     */
    public ObjectNotation(Object theObject) {
        if (!Model.getFacade().isAObject(theObject)
                && !Model.getFacade().isAInstanceSpecification(theObject)) {
            throw new IllegalArgumentException("This is not an Object.");
        }
    }

    @Override
    public void initialiseListener(Object modelElement) {
        addElementListener(modelElement, 
                new String[] {"name", "classifier"});

        // Add the following once we show stereotypes:
//      Collection c = Model.getFacade().getStereotypes(newOwner);
//      Iterator i = c.iterator();
//      while (i.hasNext()) {
//          Object st = i.next();
//          addElementListener(st, "name");
//      }

        Collection c = Model.getFacade().getClassifiers(modelElement);
        Iterator i = c.iterator();
        while (i.hasNext()) {
            Object st = i.next();
            addElementListener(st, "name");
        }
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#updateListener(java.beans.PropertyChangeListener, java.lang.Object, java.beans.PropertyChangeEvent)
     */
    public void updateListener(
            Object modelElement, PropertyChangeEvent pce) {
        if (pce instanceof AttributeChangeEvent
                && pce.getSource() == modelElement
                && "classifier".equals(pce.getPropertyName())) {
            if (pce.getOldValue() != null) {
                removeElementListener(pce.getOldValue());
            }
            if (pce.getNewValue() != null) {
                addElementListener(pce.getNewValue(), "name");
            }
        }
    }

}
