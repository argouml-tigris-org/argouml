/* $Id$
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

import java.awt.event.ActionEvent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.ui.UndoableAction;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;

class CreateDiagramElementAction extends UndoableAction {
    
    final Object metaType;
    final String style;
    final BaseDiagram diagram;

    CreateDiagramElementAction(
            final Object metaType,
            final String style,
            final String name,
            final BaseDiagram diagram) {
        super(name, ResourceLoaderWrapper.lookupIconResource(
                ResourceLoaderWrapper.getImageBinding(name)));
        this.diagram = diagram;
        this.metaType = metaType;
        this.style = style;
    }
    
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        Mode placeMode = new ModePlaceDiagramElement(
                diagram, metaType, style, "Click to place diagram element");
        Globals.mode(placeMode, false);
    }
}
