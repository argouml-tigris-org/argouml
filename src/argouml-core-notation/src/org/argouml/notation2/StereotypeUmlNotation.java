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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;

import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;

public class StereotypeUmlNotation implements NotationText,
        PropertyChangeListener {

    final private NotatedItem notatedItem;

    StereotypeUmlNotation(NotatedItem item) {
        notatedItem = item;
    }
    
    public Object getMetaType() {
        return notatedItem.getMetaType();
    }

    public NotationType getNotationType() {
        return notatedItem.getNotationType();
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        try {
            final Object owner = notatedItem.getOwner();
            final String name = "<<" + Model.getFacade().getName(owner) + ">>";
            final boolean isAbstract = Model.getFacade().isAbstract(owner);
            
            Runnable doWorkRunnable = new Runnable() {
                public void run() {
                        NotationTextEvent event = new UmlNotationTextEvent(
                                name, 
                                isAbstract, 
                                false, 
                                false);
                        notatedItem.notationTextChanged(event);
                }  
            };
            SwingUtilities.invokeLater(doWorkRunnable);
        } catch (InvalidElementException e) {
        }
    }

}
