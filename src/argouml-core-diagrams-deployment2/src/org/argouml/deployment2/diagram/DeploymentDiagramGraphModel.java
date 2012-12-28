/* $Id$
 *****************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 */

package org.argouml.deployment2.diagram;

public class DeploymentDiagramGraphModel extends org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel {

    /*
     * @see org.tigris.gef.graph.MutableGraphModel#addEdge(java.lang.Object)
     */
    @Override
    public void addEdge(Object edge) {
        if (!canAddEdge(edge)) {
            return;
        }
        getEdges().add(edge);
        fireEdgeAdded(edge);
    }
}
