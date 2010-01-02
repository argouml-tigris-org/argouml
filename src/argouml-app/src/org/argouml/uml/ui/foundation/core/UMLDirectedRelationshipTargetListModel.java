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
 * List model for targets of a UML 2.x DirectedRelationship.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class UMLDirectedRelationshipTargetListModel extends UMLModelElementListModel2 {

    /**
     * Construct a model for sources of a DirectedRelationship
     */
    public UMLDirectedRelationshipTargetListModel() {
        // TODO: What's our property name here
        super("target");
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
        return Model.getFacade().getTargets(getTarget());
    }

}
