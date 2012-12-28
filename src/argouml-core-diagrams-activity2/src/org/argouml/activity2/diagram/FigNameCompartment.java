/* $Id$
 *****************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.activity2.diagram;

import java.awt.Rectangle;

import org.argouml.model.Model;
import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;

public class FigNameCompartment extends FigCompartment {

    public FigNameCompartment(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, settings);
        addFig(new FigCompartment(
                owner, bounds, settings, 
                Model.getMetaTypes().getStereotype(), "stereotype"));
        Fig nameDisplay = new FigNotation(
                owner,
                new Rectangle(0, 0, 0, 0),
                settings,
                NotationType.NAME);
        addFig(nameDisplay);
    }
}
