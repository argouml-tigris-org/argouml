/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.sequence2.diagram;

import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactoryInterface;
import org.argouml.uml.diagram.DiagramFactoryInterface2;
import org.argouml.uml.diagram.DiagramSettings;

/**
 * This factory creates a sequence2 Sequence Diagram.
 * @see org.argouml.uml.diagram.DiagramFactory
 * @author penyaskito
 */
public class SequenceDiagramFactory
        implements DiagramFactoryInterface, DiagramFactoryInterface2 {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(SequenceDiagramFactory.class.getName());

    /**
     * Factory method to create a new instance of an ArgoDiagram,
     * including the sequence2 Sequence Diagram.
     *
     * @param namespace The namespace that (in)directly
     *                        owns the elements on the diagram
     * @param machine The StateMachine for the diagram
     *                         (only: statemachine - activitygraph)
     * @return the newly instantiated class diagram
     */
    public ArgoDiagram createDiagram(
            final Object namespace,
            final Object machine) {

        final ArgoDiagram diagram = new UMLSequenceDiagram(namespace);
        return diagram;
    }

    /**
     * Factory method to create a new instance of an ArgoDiagram.
     *
     * @param owner the owning element. This can be the owning namespace for a
     *            Class diagram or an owning Statemachine for a State Diagram or
     *            any other interpretation that the diagram type wants to apply.
     * @param name the name of the diagram. This may be null if the caller would
     *            like the factory to provide a default name.
     * @param settings default rendering settings for the diagram
     * @return the newly instantiated diagram
     */
    public ArgoDiagram createDiagram(Object owner, String name,
            DiagramSettings settings) {
        final ArgoDiagram diagram = new UMLSequenceDiagram(owner);
        if (name != null) {
            try {
                diagram.setName(name);
            } catch (PropertyVetoException e) {
                LOG.log(Level.SEVERE, "Cannot set the name " + name +
                        " to the diagram just created: "+ diagram.getName(), e);
            }
        }
        return diagram;
    }
}
