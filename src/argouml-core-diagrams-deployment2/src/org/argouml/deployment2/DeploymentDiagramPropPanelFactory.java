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

package org.argouml.deployment2;

import org.argouml.deployment2.diagram.UMLDeploymentDiagram;
import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.ui.PropPanelDiagram;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.PropPanelFactory;

class DeploymentDiagramPropPanelFactory implements PropPanelFactory {

    public PropPanel createPropPanel(Object object) {
        if (object instanceof UMLDeploymentDiagram) {
            return new PropPanelUMLDeploymentDiagram();
        }
        return null;
    }

    class PropPanelUMLDeploymentDiagram extends PropPanelDiagram {

        public PropPanelUMLDeploymentDiagram() {
            super(Translator.localize("label.deployment-diagram"),
                    lookupIcon("DeploymentDiagram"));
        }

    }
}
