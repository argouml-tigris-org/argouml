/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram;

import org.argouml.model.DiDiagram;
import org.argouml.model.DiElement;
import org.argouml.model.Model;
import org.tigris.gef.graph.GraphEvent;
import org.tigris.gef.graph.GraphListener;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * Adapts changes in the Diagram subsystem (the graph presentation layer)
 * to changes in the Model subsyetm (diagram interchange model).
 * The curent implementaion does this by listening to graph events and
 * forwarding those as specific calls to the DiagramInterchangeModel.
 * This should be changed to a more standard Adapter architecture that
 * provides an interface for Figs and GraphModels to call only when required.
 *
 * @author Bob Tarling
 * @stereotype singleton
 */
public final class GraphChangeAdapter implements GraphListener {
    /**
     * The instance.
     */
    private static final GraphChangeAdapter INSTANCE =
        new GraphChangeAdapter();

    /**
     * The getter for the instance.
     *
     * @return The instance.
     */
    public static GraphChangeAdapter getInstance() {
        return INSTANCE;
    }

    /**
     * The constructor of a singleton is private.
     */
    private GraphChangeAdapter() {
        // singleton, no instantiation
    }

    public DiDiagram createDiagram(Class type, Object owner) {
        if (Model.getDiagramInterchangeModel() != null) {
            return Model.getDiagramInterchangeModel()
                .createDiagram(type, owner);
        }
        return null;
    }


    public void removeDiagram(DiDiagram dd) {
        if (Model.getDiagramInterchangeModel() != null) {
            Model.getDiagramInterchangeModel().deleteDiagram(dd);
        }
    }

    public DiElement createElement(GraphModel gm, Object node) {
        if (Model.getDiagramInterchangeModel() != null) {
            return Model.getDiagramInterchangeModel().createElement(
                ((UMLMutableGraphSupport) gm).getDiDiagram(), node);
        }
        return null;
    }

    public void removeElement(DiElement element) {
        if (Model.getDiagramInterchangeModel() != null) {
            Model.getDiagramInterchangeModel().deleteElement(element);
        }
    }

    /*
     * @see org.tigris.gef.graph.GraphListener#nodeAdded(org.tigris.gef.graph.GraphEvent)
     */
    public void nodeAdded(GraphEvent e) {
        Object source = e.getSource();
        Object arg = e.getArg();
        if (source instanceof Fig) {
            source = ((Fig) source).getOwner();
        }
        if (arg instanceof Fig) {
            arg = ((Fig) arg).getOwner();
        }
        Model.getDiagramInterchangeModel().nodeAdded(source, arg);
    }

    /*
     * @see org.tigris.gef.graph.GraphListener#edgeAdded(org.tigris.gef.graph.GraphEvent)
     */
    public void edgeAdded(GraphEvent e) {
        Object source = e.getSource();
        Object arg = e.getArg();
        if (source instanceof Fig) {
            source = ((Fig) source).getOwner();
        }
        if (arg instanceof Fig) {
            arg = ((Fig) arg).getOwner();
        }
        Model.getDiagramInterchangeModel().edgeAdded(source, arg);
    }

    /*
     * @see org.tigris.gef.graph.GraphListener#nodeRemoved(org.tigris.gef.graph.GraphEvent)
     */
    public void nodeRemoved(GraphEvent e) {
        Object source = e.getSource();
        Object arg = e.getArg();
        if (source instanceof Fig) {
            source = ((Fig) source).getOwner();
        }
        if (arg instanceof Fig) {
            arg = ((Fig) arg).getOwner();
        }
        Model.getDiagramInterchangeModel().nodeRemoved(source, arg);
    }

    /*
     * @see org.tigris.gef.graph.GraphListener#edgeRemoved(org.tigris.gef.graph.GraphEvent)
     */
    public void edgeRemoved(GraphEvent e) {
        Object source = e.getSource();
        Object arg = e.getArg();
        if (source instanceof Fig) {
            source = ((Fig) source).getOwner();
        }
        if (arg instanceof Fig) {
            arg = ((Fig) arg).getOwner();
        }
        Model.getDiagramInterchangeModel().edgeRemoved(source, arg);
    }

    /*
     * @see org.tigris.gef.graph.GraphListener#graphChanged(org.tigris.gef.graph.GraphEvent)
     */
    public void graphChanged(GraphEvent e) {
        Object source = e.getSource();
        Object arg = e.getArg();
        if (source instanceof Fig) {
            source = ((Fig) source).getOwner();
        }
        if (arg instanceof Fig) {
            arg = ((Fig) arg).getOwner();
        }
        Model.getDiagramInterchangeModel().graphChanged(source, arg);
    }
}
