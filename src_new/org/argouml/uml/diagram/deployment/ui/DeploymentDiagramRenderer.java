// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

package org.argouml.uml.diagram.deployment.ui;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.uml.diagram.ui.*;
import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.static_structure.ui.*;

public class DeploymentDiagramRenderer
    implements GraphNodeRenderer, GraphEdgeRenderer 
{
    protected static Logger cat =
	Logger.getLogger(DeploymentDiagramRenderer.class);

    /** Return a Fig that can be used to represent the given node */

    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
	if (org.argouml.model.ModelFacade.isANode(node)) return new FigMNode(gm, node);
	else if (org.argouml.model.ModelFacade.isANodeInstance(node))
	    return new FigMNodeInstance(gm, node);
	else if (org.argouml.model.ModelFacade.isAComponent(node))
	    return new FigComponent(gm, node); 
	else if (org.argouml.model.ModelFacade.isAComponentInstance(node))
	    return new FigComponentInstance(gm, node);
	else if (org.argouml.model.ModelFacade.isAClass(node)) return new FigClass(gm, node); 
	else if (org.argouml.model.ModelFacade.isAInterface(node))
	    return new FigInterface(gm, node); 
	else if (org.argouml.model.ModelFacade.isAObject(node)) return new FigObject(gm, node);
	cat.debug("TODO DeploymentDiagramRenderer getFigNodeFor");
	return null;
    }

    /** Return a Fig that can be used to represent the given edge */
    public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {

	if (org.argouml.model.ModelFacade.isAAssociation(edge)) {
	    Object asc = /*(MAssociation)*/ edge;
	    FigAssociation ascFig = new FigAssociation(asc, lay);
	    return ascFig;
	}
	if (org.argouml.model.ModelFacade.isALink(edge)) {
	    Object lnk = /*(MLink)*/ edge;
	    FigLink lnkFig = new FigLink(lnk);
	    Collection linkEnds = ModelFacade.getConnections(lnk);
	    if (linkEnds == null) cat.debug("null linkRoles....");
	    Object[] leArray = linkEnds.toArray();
	    MLinkEnd fromEnd = (MLinkEnd) leArray[0];
	    MInstance fromInst = fromEnd.getInstance();
	    MLinkEnd toEnd = (MLinkEnd) leArray[1];
	    MInstance toInst = toEnd.getInstance();
	    FigNode fromFN = (FigNode) lay.presentationFor(fromInst);
	    FigNode toFN = (FigNode) lay.presentationFor(toInst);
	    lnkFig.setSourcePortFig(fromFN);
	    lnkFig.setSourceFigNode(fromFN);
	    lnkFig.setDestPortFig(toFN);
	    lnkFig.setDestFigNode(toFN);
	    return lnkFig;
	}
	if (org.argouml.model.ModelFacade.isADependency(edge)) {
	    Object dep = /*(MDependency)*/ edge;
	    FigDependency depFig = new FigDependency(dep);

	    Object supplier =
		/*(MModelElement)*/ ((ModelFacade.getSuppliers(dep).toArray())[0]);
	    Object client =
		/*(MModelElement)*/ ((ModelFacade.getClients(dep).toArray())[0]);

	    FigNode supFN = (FigNode) lay.presentationFor(supplier);
	    FigNode cliFN = (FigNode) lay.presentationFor(client);

	    depFig.setSourcePortFig(cliFN);
	    depFig.setSourceFigNode(cliFN);
	    depFig.setDestPortFig(supFN);
	    depFig.setDestFigNode(supFN);
	    depFig.getFig().setDashed(true);
	    return depFig;
	}

	return null;
    }

    static final long serialVersionUID = 8002278834226522224L;

}