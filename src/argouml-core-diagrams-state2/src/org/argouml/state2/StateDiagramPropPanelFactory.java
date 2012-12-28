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

package org.argouml.state2;

import org.argouml.i18n.Translator;
import org.argouml.state2.diagram.UMLStateDiagram;
import org.argouml.uml.diagram.ui.PropPanelDiagram;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.PropPanelFactory;

class StateDiagramPropPanelFactory implements PropPanelFactory {

    public PropPanel createPropPanel(Object object) {
        if (object instanceof UMLStateDiagram) {
            return new PropPanelUMLStateDiagram();
        }
        return null;
    }

    class PropPanelUMLStateDiagram extends PropPanelDiagram {

        public PropPanelUMLStateDiagram() {
            super(Translator.localize("label.state-chart-diagram"),
                    lookupIcon("StateDiagram"));
        }

    }
}
