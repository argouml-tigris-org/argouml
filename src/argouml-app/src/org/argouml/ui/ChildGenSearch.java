/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.util.ChildGenerator;

/**
 * ChildGenerator that returns the "children" of any given part of the project.
 * It traverses a Project to Diagrams and Models, then uses
 * getModelElementContents to traverse the Models.
 * 
 * @author jrobbins
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class ChildGenSearch implements ChildGenerator {
    
    /**
     * Reply a Collection of the children of the given Object
     * {@inheritDoc}
     */
    public Iterator childIterator(Object o) {
        // TODO: This could be made more efficient by working with iterators
        // directly and creating a composite iterator made up of all the 
        // various sub iterators.
        List res = new ArrayList();
        if (o instanceof Project) {
            Project p = (Project) o;
            res.addAll(p.getUserDefinedModelList());
            res.addAll(p.getDiagramList());
        } else if (o instanceof ArgoDiagram) {
            ArgoDiagram d = (ArgoDiagram) o;
            res.addAll(d.getGraphModel().getNodes());
            res.addAll(d.getGraphModel().getEdges());
        } else if (Model.getFacade().isAModelElement(o)) {
            res.addAll(Model.getFacade().getModelElementContents(o));
        }
        
	return res.iterator();
    }

}
