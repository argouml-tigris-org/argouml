/* $Id: $
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

package org.argouml.state2.diagram;

import java.awt.Rectangle;
import org.argouml.uml.diagram.DiagramSettings;

public class FigExitPoint extends FigCircleState {

    public FigExitPoint(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
    }
}
