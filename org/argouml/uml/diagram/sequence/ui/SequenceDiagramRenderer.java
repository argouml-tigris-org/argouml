// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

import java.util.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.ui.*;


public class SequenceDiagramRenderer
    implements GraphNodeRenderer, GraphEdgeRenderer {
    protected static Logger cat = 
        Logger.getLogger(SequenceDiagramRenderer.class);
						     
    /** Return a Fig that can be used to represent the given node */
    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
	if (org.argouml.model.ModelFacade.isAObject(node)) return new FigSeqObject(gm, node);
	if (org.argouml.model.ModelFacade.isAStimulus(node)) return new FigSeqStimulus(gm, node);
	cat.debug("TODO SequenceDiagramRenderer getFigNodeFor");
	return null;
    }
							 
    /** Return a Fig that can be used to represent the given edge */
    /** Generally the same code as for the ClassDiagram, since its
	very related to it. */
    public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {
	if (org.argouml.model.ModelFacade.isALink(edge)) {
	    Object ml = /*(MLink)*/ edge;
	    FigSeqLink mlFig = new FigSeqLink(ml);
	    Collection connections = ModelFacade.getConnections(ml);
	    if (connections == null) {
		cat.debug("null connections....");
		return null;
	    }
	    Object/*MLinkEnd*/ fromEnd = ((Object[]) connections.toArray())[0];
	    Object fromInst = /*(MInstance)*/ ModelFacade.getInstance(fromEnd);
	    Object/*MLinkEnd*/ toEnd = ((Object[]) connections.toArray())[1];
	    Object toInst = /*(MInstance)*/ ModelFacade.getInstance(toEnd);
	    FigNode fromFN = (FigNode) lay.presentationFor(fromInst);
	    FigNode toFN = (FigNode) lay.presentationFor(toInst);
	    mlFig.setSourcePortFig(fromFN);
	    mlFig.setSourceFigNode(fromFN);
	    mlFig.setDestPortFig(toFN);
	    mlFig.setDestFigNode(toFN);
	    return mlFig;
	}
			       
	cat.debug("TODO SequenceDiagramRenderer getFigEdgeFor");
	return null;
    }
							     
} /* end class SequenceDiagramRenderer */