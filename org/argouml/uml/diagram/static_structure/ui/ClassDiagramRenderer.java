// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigAssociationClass;
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

/** 
 * This class defines a renderer object for UML Class Diagrams. In a
 * Class Diagram the following UML objects are displayed with the
 * following Figs: <p>
 * 
 * <pre>
 *  UML Object       ---  Fig
 *  ---------------------------------------
 *  Class            ---  FigClass
 *  Interface        ---  FigInterface
 *  Instance         ---  FigInstance
 *  Model            ---  FigModel
 *  Subsystem        ---  FigSubsystem
 *  Package          ---  FigPackage
 *  Comment          ---  FigComment
 *  (CommentEdge)    ---  FigEdgeNote
 *  Generalization   ---  FigGeneralization
 *  Realization      ---  FigRealization
 *  Permission       ---  FigPermission
 *  Usage            ---  FigUsage
 *  Dependency       ---  FigDependency
 *  Association      ---  FigAssociation
 *  AssociationClass ---  FigAssociationClass
 *  Dependency       ---  FigDependency
 *  Link             ---  FigLink
 * </pre>
 *
 * @author jrobbins
 */
public class ClassDiagramRenderer
    implements GraphNodeRenderer, GraphEdgeRenderer {

    private static final Logger LOG = 
        Logger.getLogger(ClassDiagramRenderer.class);

    /**
     * @see org.tigris.gef.graph.GraphNodeRenderer#getFigNodeFor(
     *         org.tigris.gef.graph.GraphModel, 
     *         org.tigris.gef.base.Layer, java.lang.Object)
     *
     * Return a Fig that can be used to represent the given node.
     */
    public FigNode getFigNodeFor(GraphModel gm, Layer lay, Object node) {
        if (ModelFacade.isAClass(node)) {
            return new FigClass(gm, node);
        } else if (ModelFacade.isAInterface(node)) {
            return new FigInterface(gm, node);
        } else if (ModelFacade.isAInstance(node)) {
            return new FigInstance(gm, node);
        } else if (ModelFacade.isAModel(node)) {
            return new FigModel(gm, node);
        } else if (ModelFacade.isASubsystem(node)) {
            return new FigSubsystem(gm, node);
        } else if (ModelFacade.isAPackage(node)) {
            return new FigPackage(gm, node);
        } else if (ModelFacade.isAComment(node)) {
            return new FigComment(gm, node);
        }
        LOG.error("TODO: ClassDiagramRenderer getFigNodeFor " + node);
        return null;
    }

    /**
     * Return a Fig that can be used to represent the given edge.
     * Throws IllegalArgumentException if the edge is not of an expected type.
     * Throws IllegalStateException if the edge generated has no source 
     *                               or dest port.
     *
     * @see org.tigris.gef.graph.GraphEdgeRenderer#getFigEdgeFor(
     * org.tigris.gef.graph.GraphModel, org.tigris.gef.base.Layer, 
     * java.lang.Object)
     */
    public FigEdge getFigEdgeFor(GraphModel gm, Layer lay, Object edge) {
        if (LOG.isDebugEnabled() ) {
            LOG.debug("making figedge for " + edge);
        }
        if (edge == null) {
            throw new IllegalArgumentException("A model edge must be supplied");
        }
        FigEdge newEdge = null;
        if (ModelFacade.isAAssociationClass(edge)) {
            FigAssociationClass ascCFig = new FigAssociationClass(edge, lay);
            return ascCFig;
        } else if (ModelFacade.isAAssociation(edge)) {
            FigAssociation ascFig = new FigAssociation(edge, lay);
            newEdge = ascFig;
        } else if (ModelFacade.isALink(edge)) {
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
            newEdge = lnkFig;
        } else if (ModelFacade.isAGeneralization(edge)) {
            FigGeneralization genFig = new FigGeneralization(edge, lay);
            newEdge = genFig;
        } else if (ModelFacade.isAPermission(edge)) {
            FigPermission perFig = new FigPermission(edge, lay);
            newEdge = perFig;
        } else if (ModelFacade.isAUsage(edge)) {
            FigUsage usageFig = new FigUsage(edge, lay);
            newEdge = usageFig;
        } else if (ModelFacade.isADependency(edge)) {
            Object stereotype = null;

            if (ModelFacade.getStereotypes(edge).size() > 0) {
                stereotype = ModelFacade.getStereotypes(edge).iterator().next();
            }
            if (LOG.isDebugEnabled() ) {
            	if (stereotype != null) {
                    LOG.debug("stereotype: " + ModelFacade.getName(stereotype));
                } else {
                    LOG.debug("stereotype is null");
                }
            } 
            if (stereotype != null
                    && Model.getExtensionMechanismsHelper().isStereotypeInh(
                            stereotype, "realize", "Abstraction")) {
                if (LOG.isDebugEnabled() ) {
                    LOG.debug("is a realisation");
                }
                FigRealization realFig = new FigRealization(edge);

                Object supplier = 
                    ((ModelFacade.getSuppliers(edge).toArray())[0]);
                Object client = ((ModelFacade.getClients(edge).toArray())[0]);

                FigNode supFN = (FigNode) lay.presentationFor(supplier);
                FigNode cliFN = (FigNode) lay.presentationFor(client);

                realFig.setSourcePortFig(cliFN);
                realFig.setSourceFigNode(cliFN);
                realFig.setDestPortFig(supFN);
                realFig.setDestFigNode(supFN);
                realFig.getFig().setLayer(lay);
                newEdge = realFig;
            } else {
                FigDependency depFig = new FigDependency(edge, lay);
                newEdge = depFig;
            }
        } else if (edge instanceof CommentEdge) {
            newEdge = new FigEdgeNote(edge, lay);
        }
        if (newEdge == null) {
            throw new IllegalArgumentException(
                    "Don't know how to create FigEdge for model type " 
                    + edge.getClass().getName());
        } else {
            if (newEdge.getSourcePortFig() == null) {
                setSourcePort(newEdge, (FigNode) lay.presentationFor(
                        Model.getUmlHelper().getSource(edge)));
            }
            if (newEdge.getDestPortFig() == null) {
                setDestPort(newEdge, (FigNode) lay.presentationFor(
                        Model.getUmlHelper().getDestination(edge)));
            }
            if (newEdge.getSourcePortFig() == null 
                    || newEdge.getDestPortFig() == null ) {
                throw new IllegalStateException("Edge of type "
                    + newEdge.getClass().getName() 
                    + " created with no source or destination port");
            }
        }
        return newEdge;
    }

    private void setSourcePort(FigEdge edge, FigNode source) {
        edge.setSourcePortFig(source);
        edge.setSourceFigNode(source);
    }

    private void setDestPort(FigEdge edge, FigNode dest) {
        edge.setDestPortFig(dest);
        edge.setDestFigNode(dest);
    }

    static final long serialVersionUID = 675407719309039112L;

} /* end class ClassDiagramRenderer */
