// $Id: ObjectNotation.java 12546 2007-05-05 16:54:40Z linus $
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
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;

/**
 * This abstract class forms the basis of all Notation providers
 * for the text shown in the Fig that represents an Object.
 * Subclass this for all languages.
 *
 * @author mvw@tigris.org
 */
public abstract class ObjectNotation extends NotationProvider {

    /**
     * The constructor.
     *
     * @param theObject the UML element
     */
    public ObjectNotation(Object theObject) {
        if (!Model.getFacade().isAObject(theObject)) {
            throw new IllegalArgumentException("This is not an Object.");
        }
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#initialiseListener(java.beans.PropertyChangeListener, java.lang.Object)
     */
    public void initialiseListener(PropertyChangeListener listener, 
            Object modelElement) {
        addElementListener(listener, modelElement, 
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
            addElementListener(listener, st, "name");
        }
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#updateListener(java.beans.PropertyChangeListener, java.lang.Object, java.beans.PropertyChangeEvent)
     */
    public void updateListener(PropertyChangeListener listener, 
            Object modelElement, PropertyChangeEvent pce) {
        if (pce instanceof AttributeChangeEvent
                && pce.getSource() == modelElement
                && "classifier".equals(pce.getPropertyName())) {
            if (pce.getOldValue() != null) {
                removeElementListener(listener, pce.getOldValue());
            }
            if (pce.getNewValue() != null) {
                addElementListener(listener, pce.getNewValue(), "name");
            }
        }
    }

}
