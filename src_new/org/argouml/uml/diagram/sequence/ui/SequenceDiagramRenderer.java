// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

// File: SequenceDiagramRenderer.java
// Classes: SequenceDiagramRenderer
// Original Author: 5eichler@inormatik.uni-hamburg.de

// $Id$


package org.argouml.uml.diagram.sequence.ui;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * This class renders a sequence diagram.
 *
 */
public class SequenceDiagramRenderer
	implements GraphNodeRenderer, GraphEdgeRenderer {
    private static final Logger LOG =
	Logger.getLogger(SequenceDiagramRenderer.class);

    /** 
     * Return a Fig that can be used to represent the given node.
     * 
     * @see org.tigris.gef.graph.GraphNodeRenderer#getFigNodeFor(
     * org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer, 
     * java.lang.Object)
     */
    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
	if (ModelFacade.isAObject(node))
	    return new FigObject(node);
	// if (node instanceof MStimulus) return new FigSeqStimulus(gm, node);
	// TODO: Something here.
	LOG.debug("SequenceDiagramRenderer getFigNodeFor");
	return null;
    }

    /** 
     * Return a Fig that can be used to represent the given edge.
     * 
     * @see org.tigris.gef.graph.GraphEdgeRenderer#getFigEdgeFor(
     * org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer, 
     * java.lang.Object)
     */
    public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {
	if (ModelFacade.isALink(edge)) {
	    Object stimulus = ModelFacade.getStimuli(edge).iterator().next();
	    Object action = ModelFacade.getDispatchAction(stimulus);
	    if (ModelFacade.isACallAction(action)) {
		return new FigCallActionLink(edge);
	    } else
		if (ModelFacade.isAReturnAction(action)) {
		    return new FigReturnActionLink(edge);
		} else
		    if (ModelFacade.isADestroyAction(edge)) {
			return new FigDestroyActionLink(edge);
		    } else
			if (ModelFacade.isACreateAction(edge)) {
			    return new FigCreateActionLink(edge);
			}
	}		
	// TODO: Something here.
	LOG.debug("SequenceDiagramRenderer getFigEdgeFor");
	return null;
    }


} /* end class SequenceDiagramRenderer */

