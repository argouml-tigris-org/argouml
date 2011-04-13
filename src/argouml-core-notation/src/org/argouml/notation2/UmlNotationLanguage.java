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

import org.argouml.model.Model;

class UmlNotationLanguage implements NotationLanguage {

    @Override
    public String getName() {
        return "UML";
    }

    @Override
    public NotationText createNotationText(NotatedItem item) {
        
        NameUmlNotation nt = new NameUmlNotation(item);
        Model.getPump().addModelEventListener(
                nt, item.getOwner());
        
        return nt;
    }

}
