/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
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
import org.tigris.gef.presentation.ArrowHeadGreater;

class FigActivityEdge extends FigBaseEdge {

    private ArrowHeadGreater endArrow = new ArrowHeadGreater();

    FigActivityEdge(Object owner, DiagramSettings settings) {
        super(owner, settings);
        
        setDestArrowHead(endArrow);
    }
}
