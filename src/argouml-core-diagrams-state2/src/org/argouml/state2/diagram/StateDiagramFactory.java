/* $Id:$
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

package org.argouml.state2.diagram;

import java.beans.PropertyVetoException;

import org.apache.log4j.Logger;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactoryInterface2;
import org.argouml.uml.diagram.DiagramSettings;

public class StateDiagramFactory
        implements DiagramFactoryInterface2{

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(StateDiagramFactory.class);
    
    public ArgoDiagram createDiagram(Object owner, String name,
            DiagramSettings settings) {
        final ArgoDiagram diagram = new UMLStateDiagram(owner);
        if (name != null) {
            try {
                diagram.setName(name);
            } catch (PropertyVetoException e) {            
                LOG.error("Cannot set the name " + name + 
                        " to the diagram just created: "+ diagram.getName(), e);
            }
        }
        return diagram;  
    }
}
