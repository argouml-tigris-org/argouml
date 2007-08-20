// $Id:AssociationEndNameNotation.java 12991 2007-07-06 17:05:03Z mvw $
// Copyright (c) 2006-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.NotationProvider;

/**
 * This abstract class forms the basis of all Notation providers
 * for the text shown next to the end of an association.
 * Subclass this for all languages.
 * 
 * @author michiel
 */
public abstract class AssociationEndNameNotation extends NotationProvider {

    /**
     * The constructor. 
     */
    protected AssociationEndNameNotation() {
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#addListener(java.beans.PropertyChangeListener, java.lang.Object)
     */
    public void initialiseListener(PropertyChangeListener listener, 
            Object modelElement) {
        addElementListener(
                listener, 
                modelElement, 
                new String[] {"name", "visibility", "stereotype"});
        Collection stereotypes =
                Model.getFacade().getStereotypes(modelElement);
        Iterator iter = stereotypes.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            addElementListener(
                    listener, 
                    o, 
                    new String[] {"name", "remove"});
        }
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#updateListener(java.beans.PropertyChangeListener, java.lang.Object, java.beans.PropertyChangeEvent)
     */
    public void updateListener(PropertyChangeListener listener, 
            Object modelElement,
            PropertyChangeEvent pce) {
        Object obj = pce.getSource();
        if ((obj == modelElement) 
                && "stereotype".equals(pce.getPropertyName())) {
            if (pce instanceof AddAssociationEvent 
                    && Model.getFacade().isAStereotype(pce.getNewValue())) {
                // new stereotype
                addElementListener(
                        listener, 
                        pce.getNewValue(), 
                        new String[] {"name", "remove"});
            }
            if (pce instanceof RemoveAssociationEvent 
                    && Model.getFacade().isAStereotype(pce.getOldValue())) {
                // removed stereotype
                removeElementListener(
                        listener, 
                        pce.getOldValue());
            }
        }
    }

}
