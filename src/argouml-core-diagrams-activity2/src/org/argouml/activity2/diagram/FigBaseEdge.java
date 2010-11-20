/* $Id: $
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
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

import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;

/**
 * The Fig for all edge diagram elements. All specialist edge diagram elements
 * decorate this to get specialist behaviour 
 * @author Bob Tarling
 */
class FigBaseEdge extends FigEdgeModelElement {

    FigBaseEdge(Object owner, DiagramSettings settings) {
        super(owner, settings);
    }
}
