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

// File: ClassDiagramRenderer.java
// Classes: ClassDiagramRenderer
// Original jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.static_structure.ui;

import java.util.Collection;

import org.apache.log4j.Logger;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsHelper;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigDependency;
import org.argouml.uml.diagram.ui.FigGeneralization;
import org.argouml.uml.diagram.ui.FigPermission;
import org.argouml.uml.diagram.ui.FigRealization;
import org.argouml.uml.diagram.ui.FigUsage;

import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/** This class defines a renderer object for UML Class Diagrams. In a
 *  Class Diagram the following UML objects are displayed with the
 *  following Figs: <p>
 * <pre>
 *  UML Object      ---  Fig
 *  ---------------------------------------
 *  Class         ---  FigClass
 *  Interface       ---  FigClass (TODO?)
 *  Generalization  ---  FigGeneralization
 *  Realization     ---  FigDependency (TODO)
 *  Association     ---  FigAssociation
 *  Dependency      ---  FigDependency
 *  </pre>
 */

public class ClassDiagramRenderer
    implements GraphNodeRenderer, GraphEdgeRenderer {

    /**
     * @deprecated in 0.15.6 use your own logger if you extend this class
     * Bob Tarling 4 June 2004
     */
    protected static Logger cat = 
        Logger.getLogger(ClassDiagramRenderer.class);

    /** Return a Fig that can be used to represent the given node */
    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
        if (ModelFacade.isAClass(node)) return new FigClass(gm, node);
        else if (ModelFacade.isAInterface(node)) return new FigInterface(gm, node);
        else if (ModelFacade.isAInstance(node)) return new FigInstance(gm, node);
        else if (ModelFacade.isAModel(node)) return new FigModel(gm, node);
        else if (ModelFacade.isASubsystem(node)) return new FigSubsystem(gm, node);
        else if (ModelFacade.isAPackage(node)) return new FigPackage(gm, node);
        cat.debug("TODO ClassDiagramRenderer getFigNodeFor " + node);
        return null;
    }

    /** Return a Fig that can be used to represent the given edge */
    public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {
        cat.debug("making figedge for " + edge);
        if (ModelFacade.isAAssociation(edge)) {
            FigAssociation ascFig = new FigAssociation(edge, lay);
            return ascFig;
        }
        if (ModelFacade.isALink(edge)) {
            Object lnk = /*(MLink)*/ edge;
            FigLink lnkFig = new FigLink(lnk);
            Collection linkEndsColn = ModelFacade.getConnections(lnk);
            
            Object[] linkEnds = linkEndsColn.toArray();
            Object fromInst = ModelFacade.getInstance(linkEnds[0]);
            Object toInst = ModelFacade.getInstance(linkEnds[1]);
            
            FigNode fromFN = (FigNode) lay.presentationFor(fromInst);
            FigNode toFN = (FigNode) lay.presentationFor(toInst);
            lnkFig.setSourcePortFig(fromFN);
            lnkFig.setSourceFigNode(fromFN);
            lnkFig.setDestPortFig(toFN);
            lnkFig.setDestFigNode(toFN);
            lnkFig.getFig().setLayer(lay);
            return lnkFig;
        }
        if (ModelFacade.isAGeneralization(edge)) {
            FigGeneralization genFig = new FigGeneralization(edge, lay);
            return genFig;
        }
        if (ModelFacade.isAPermission(edge)) {
            FigPermission perFig = new FigPermission(edge, lay);
            return perFig;
        }
        if (ModelFacade.isAUsage(edge)) {
            FigUsage usageFig = new FigUsage(edge, lay);
            return usageFig;
        }
        if (ModelFacade.isADependency(edge)) {
            Object stereotype = null;

            if (ModelFacade.getStereotypes(edge).size() > 0) {
                stereotype = ModelFacade.getStereotypes(edge).iterator().next();
            }
            cat.debug("stereotype: " + ModelFacade.getName(stereotype));
            if (stereotype != null
                    && ExtensionMechanismsHelper.getHelper().isStereotypeInh(
                            stereotype, "realize", "Abstraction")) {
                cat.debug("is a realisation");
                FigRealization realFig = new FigRealization(edge);

                Object supplier = ((ModelFacade.getSuppliers(edge).toArray())[0]);
                Object client = ((ModelFacade.getClients(edge).toArray())[0]);

                FigNode supFN = (FigNode) lay.presentationFor(supplier);
                FigNode cliFN = (FigNode) lay.presentationFor(client);

                realFig.setSourcePortFig(cliFN);
                realFig.setSourceFigNode(cliFN);
                realFig.setDestPortFig(supFN);
                realFig.setDestFigNode(supFN);
                realFig.getFig().setLayer(lay);
                return realFig;
            } else {
                FigDependency depFig = new FigDependency(edge, lay);
                return depFig;
            }
        }
        cat.debug("TODO ClassDiagramRenderer getFigEdgeFor");
        return null;
    }


    static final long serialVersionUID = 675407719309039112L;

} /* end class ClassDiagramRenderer */