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

package org.argouml.deployment2.diagram;

import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactoryInterface2;
import org.argouml.uml.diagram.DiagramSettings;

public class DeploymentDiagramFactory
        implements DiagramFactoryInterface2{

    public ArgoDiagram createDiagram(Object owner, String name,
            DiagramSettings settings) {
        return new UMLDeploymentDiagram(name, owner);
    }
}
