// $Id$
// Copyright (c) 2005-2008 The Regents of the University of California. All
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
import java.beans.PropertyChangeListener;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.NotationProvider;

/**
 * This abstract class forms the basis of all Notation providers
 * for the text shown in the attribute compartment of a Class.
 * Subclass this for all languages.
 *
 * @author Michiel
 */
public abstract class AttributeNotation extends NotationProvider {

    /**
     * The constructor.
     */
    protected AttributeNotation() {
    }

    @Override
    public void initialiseListener(PropertyChangeListener listener, 
            Object modelElement) {
        addElementListener(listener, modelElement);
        if (Model.getFacade().isAAttribute(modelElement)) {
            // We also show stereotypes
            for (Object uml : Model.getFacade().getStereotypes(modelElement)) {
                addElementListener(listener, uml);
            }
            // We also show the type (of which e.g. the name may change)
            Object type = Model.getFacade().getType(modelElement);
            if (type != null) {
                addElementListener(listener, type);
            }
        }
    }

    @Override
    public void updateListener(PropertyChangeListener listener, 
            Object modelElement, PropertyChangeEvent pce) {
        if (pce.getSource() == modelElement
                && ("stereotype".equals(pce.getPropertyName())
                        || ("type".equals(pce.getPropertyName())))) {
            if (pce instanceof AddAssociationEvent) {
                addElementListener(listener, pce.getNewValue());
            }
            if (pce instanceof RemoveAssociationEvent) {
                removeElementListener(listener, pce.getOldValue());
            }
        }
    }
}
