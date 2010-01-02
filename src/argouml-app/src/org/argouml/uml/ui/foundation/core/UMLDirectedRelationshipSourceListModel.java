/* $Id$
 *******************************************************************************
 * Copyright (c) 2009,2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation
 *******************************************************************************
 */
package org.argouml.uml.ui.foundation.core;

import java.util.Collection;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

/**
 * List model for sources of a UML 2.x DirectedRelationship.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class UMLDirectedRelationshipSourceListModel extends UMLModelElementListModel2 {

    /**
     * Construct a model for sources of a DirectedRelationship
     */
    public UMLDirectedRelationshipSourceListModel() {
        // TODO: What's our property name here
        super("source");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            setAllElements(getContents());
        }
    }

    protected boolean isValidElement(Object o) {
        return Model.getFacade().isAElement(o) && getContents().contains(o);
    }
    
    private Collection getContents() {
        return Model.getFacade().getSources(getTarget());
    }

}
