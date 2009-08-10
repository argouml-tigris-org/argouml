// $Id: UMLClassDiagram.java 16702 2009-01-25 20:23:28Z bobtarling $
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

package org.argouml.diagram.uml2;

import java.awt.Rectangle;
import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.UMLDiagram;

/**
 * UML Class Diagram.
 * 
 * @author jrobbins@ics.uci.edy
 */
public abstract class UMLStructureDiagram2 extends UMLDiagram {

    private static final long serialVersionUID = -9192325790126361563L;

    private static final Logger LOG =
        Logger.getLogger(UMLStructureDiagram2.class);


    /**
     * Construct a Class Diagram. Default constructor used by PGML parser during
     * diagram load. It should not be used by other callers.
     * @deprecated only for use by PGML parser
     */
    @Deprecated
    public UMLStructureDiagram2(UMLMutableGraphSupport graphmodel) {
        super(graphmodel);
    }


    /**
     * Construct a new class diagram with the given name, owned by the given
     * namespace.
     *
     * @param name the name for the new diagram
     * @param namespace the namespace for the new diagram
     * @param graphmodel the GEF graph model for this diagram
     */
    public UMLStructureDiagram2(
            final String name,
            final Object namespace,
            final UMLMutableGraphSupport graphmodel) {
        super(name, namespace, graphmodel);
    }

    /**
     * Construct Structure Diagram owned by the given namespace.
     * 
     * @param ns the namespace
     * @param graphmodel the graph model for this diagram
     */
    public UMLStructureDiagram2(Object ns, UMLMutableGraphSupport graphmodel) {
        super("", ns, graphmodel);
    }
    
    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {
        
        FigNodeModelElement figNode = null;
        
        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isAAssociation(modelElement)) {
            figNode =
                createNaryAssociationNode(modelElement, bounds, settings);
        } else if (Model.getFacade().isAClass(modelElement)) {
            figNode = new FigClass2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAInterface(modelElement)) {
            figNode = new FigInterface2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAModel(modelElement)) {
            figNode = new FigModel2(modelElement, bounds, settings);
        } else if (Model.getFacade().isASubsystem(modelElement)) {
            figNode = new FigSubsystem2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAPackage(modelElement)) {
            figNode = new FigPackage2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComment(modelElement)) {
            figNode = new FigComment2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAEnumeration(modelElement)) {
            figNode = new FigEnumeration2(modelElement, bounds, settings);
        } else if (Model.getFacade().isADataType(modelElement)) {
            figNode = new FigDataType2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAStereotype(modelElement)) {
            figNode = new FigStereotypeDeclaration2(modelElement, bounds, 
                    settings);
        } else if (Model.getFacade().isAException(modelElement)) {
            figNode = new FigException2(modelElement, bounds, settings);
        } else if (Model.getFacade().isASignal(modelElement)) {
            figNode = new FigSignal2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAActor(modelElement)) {
            figNode = new FigActor2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAUseCase(modelElement)) {
            figNode = new FigUseCase2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAObject(modelElement)) {
            figNode = new FigObject2(modelElement, bounds, settings);
        } else if (Model.getFacade().isANodeInstance(modelElement)) {
            figNode = new FigNodeInstance2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComponentInstance(modelElement)) {
            figNode = new FigComponentInstance2(modelElement, bounds, settings);
        } else if (Model.getFacade().isANode(modelElement)) {
            figNode = new FigMNode2(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComponent(modelElement)) {
            figNode = new FigComponent2(modelElement, bounds, settings);
        }
        
        if (figNode != null) {
            LOG.debug("Model element " + modelElement + " converted to " 
                    + figNode);
        } else {
            LOG.debug("Object NOT added " + figNode);
        }
        return figNode;
    }
}
