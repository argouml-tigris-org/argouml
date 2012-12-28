/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.notation2;

import java.beans.PropertyChangeListener;

import org.argouml.model.Model;

class UmlNotationLanguage implements NotationLanguage {

    public String getName() {
        return "UML";
    }

    public NotationText createNotationText(NotatedItem item) {
        
        final NotationText nt;
        if (Model.getCoreHelper().isSubType(
                Model.getMetaTypes().getStereotype(), item.getMetaType())) {
            nt = new StereotypeUmlNotation(item);
        } else {
            nt = new NameUmlNotation(item);
        }
        Model.getPump().addModelEventListener(
                (PropertyChangeListener) nt, item.getOwner());
        
        // As soon as we've created a notation and have registered it listener
        // force an event to go to the listener so it draws for the first time.
        ((PropertyChangeListener) nt).propertyChange(null);
        
        return nt;
    }
}
