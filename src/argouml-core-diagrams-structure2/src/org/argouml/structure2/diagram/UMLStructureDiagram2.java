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

package org.argouml.structure2.diagram;

import java.awt.Point;
import java.awt.Rectangle;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.presentation.FigNode;

/**
 * UML Class Diagram.
 * 
 * @author jrobbins@ics.uci.edy
 */
public abstract class UMLStructureDiagram2 extends UMLDiagram {

    private static final long serialVersionUID = -9192325790126361563L;

    private static final Logger LOG = Logger.getLogger(UMLStructureDiagram2.class);


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
     */
    public UMLStructureDiagram2(String name, Object namespace, UMLMutableGraphSupport graphmodel) {
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
    
    @Override
    public FigNode drop(Object droppedObject, Point location) {        
        FigNode figNode = null;

        // If location is non-null, convert to a rectangle that we can use
        Rectangle bounds = null;
        if (location != null) {
            bounds = new Rectangle(location.x, location.y, 0, 0);
        }

        DiagramSettings settings = getDiagramSettings();
        
        if (Model.getFacade().isAAssociation(droppedObject)) {
            figNode =
                createNaryAssociationNode(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAClass(droppedObject)) {
            figNode = new FigClass2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAInterface(droppedObject)) {
            figNode = new FigInterface2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAModel(droppedObject)) {
            figNode = new FigModel2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isASubsystem(droppedObject)) {
            figNode = new FigSubsystem2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAPackage(droppedObject)) {
            figNode = new FigPackage2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAComment(droppedObject)) {
            figNode = new FigComment2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAEnumeration(droppedObject)) {
            figNode = new FigEnumeration2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isADataType(droppedObject)) {
            figNode = new FigDataType2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAStereotype(droppedObject)) {
            figNode = new FigStereotypeDeclaration2(droppedObject, bounds, 
                    settings);
        } else if (Model.getFacade().isAException(droppedObject)) {
            figNode = new FigException2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isASignal(droppedObject)) {
            figNode = new FigSignal2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAActor(droppedObject)) {
            figNode = new FigActor2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAUseCase(droppedObject)) {
            figNode = new FigUseCase2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAObject(droppedObject)) {
            figNode = new FigObject2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isANodeInstance(droppedObject)) {
            figNode = new FigNodeInstance2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAComponentInstance(droppedObject)) {
            figNode = new FigComponentInstance2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isANode(droppedObject)) {
            figNode = new FigMNode2(droppedObject, bounds, settings);
        } else if (Model.getFacade().isAComponent(droppedObject)) {
            figNode = new FigComponent2(droppedObject, bounds, settings);
        }
        
        if (figNode != null) {
            // if location is null here the position of the new figNode is set
            // after in org.tigris.gef.base.ModePlace.mousePressed(MouseEvent e)
            if (location != null) {
                figNode.setLocation(location.x, location.y);
            }
            LOG.debug("Dropped object " + droppedObject + " converted to " 
                    + figNode);
        } else {
            LOG.debug("Dropped object NOT added " + droppedObject);
        }
        return figNode;
    }
}
