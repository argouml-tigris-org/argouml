/* $Id$
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

public class StateDiagramGraphModel extends org.argouml.uml.diagram.state.StateDiagramGraphModel {

    
    public void setHomeModel(Object ns) {
        super.setHomeModel(ns);
        setMachine(ns);
    }
    
}
