/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

package org.argouml.activity2.diagram;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.argouml.uml.diagram.UMLMutableGraphSupport;

class ActivityDiagramGraphModel extends UMLMutableGraphSupport {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActivityDiagramGraphModel.class.getName());

    ActivityDiagramGraphModel() {
        super();
    }

    /*
     * @see org.tigris.gef.graph.GraphModel#getPorts(java.lang.Object)
     */
    public List getPorts(Object nodeOrEdge) {
        List res = new ArrayList();
        return res;
    }

    public List getInEdges(Object port) {
        // TODO: Auto-generated method stub
        return null;
    }

    public List getOutEdges(Object port) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object getOwner(Object port) {
        return port;
    }
}
