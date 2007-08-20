// $Id:AssociationRoleNotation.java 12991 2007-07-06 17:05:03Z mvw $
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

import java.beans.PropertyChangeListener;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;

/**
 * This abstract class forms the basis of all Notation providers
 * for the name of an association-role.
 * Subclass this for all languages.
 * 
 * @author michiel
 */
public abstract class AssociationRoleNotation extends NotationProvider {
    
    /**
     * The constructor.
     *
     * @param role the given associationRole
     */
    public AssociationRoleNotation(Object role) {
        if (!Model.getFacade().isAAssociationRole(role)) {
            throw new IllegalArgumentException(
                    "This is not an AssociationRole.");
        }
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#initialiseListener(java.beans.PropertyChangeListener, java.lang.Object)
     */
    public void initialiseListener(PropertyChangeListener listener, 
            Object modelElement) {
        addElementListener(listener, modelElement, 
            new String[] {"name", "base"});
        Object assoc = Model.getFacade().getBase(modelElement);
        if (assoc != null) {
            addElementListener(listener, assoc, "name");
        }
    }

}
