/* $Id: $
 *****************************************************************************
 * Copyright (c) 2014 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************/

package org.argouml.ui.cmd;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.argouml.kernel.ActionList;
import org.argouml.model.Model;
import org.argouml.ui.ContextActionFactory;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramUtils;
import org.tigris.gef.presentation.Fig;

/**
 * A factory for creating context menu actions to place related nodes and edges of
 * an existing FigNode.
 *
 * @author Bob Tarling
 */
public class RelationshipActionFactory implements ContextActionFactory {

    public List<Action> createContextPopupActions(final Object element) {
        
        List<Action> list = new ArrayList<Action>();
        
        ArgoDiagram diagram = DiagramUtils.getActiveDiagram();
        
        ActionList al1 = getAssociationFromActions(element, diagram);
        if (!al1.isEmpty()) {
            list.add(al1);
        }
        ActionList al2 = getAssociationToActions(element, diagram);
        if (!al2.isEmpty()) {
            list.add(al2);
        }
        ActionList al3 = getDependancyFromActions(element, diagram);
        if (!al3.isEmpty()) {
            list.add(al3);
        }
        ActionList al4 = getDependancyToActions(element, diagram);
        if (!al4.isEmpty()) {
            list.add(al4);
        }
        ActionList al5 = getGeneralizationFromActions(element, diagram);
        if (!al5.isEmpty()) {
            list.add(al5);
        }
        ActionList al6 = getGeneralizationToActions(element, diagram);
        if (!al6.isEmpty()) {
            list.add(al6);
        }
        
        return list;
    }
    
    private ActionList getAssociationFromActions(final Object element, ArgoDiagram diagram) {
        ActionList al= new ActionList("Add Associations from ");
        if (Model.getFacade().isAClass(element)) {
            Collection associationEnds = Model.getFacade().getAssociationEnds(element);
            
            for (Object associationEnd : associationEnds) {
                Object association = Model.getFacade().getAssociation(associationEnd);
                
                // Only show actions for associations not already on diagram
                if (diagram.presentationFor(association) == null) {
                    Collection connections = Model.getFacade().getConnections(association);
                    if (connections.size() == 2) {
                        for (Object connection : connections) {
                            if (connection != associationEnd) {
                                final String direction;
                                if (Model.getFacade().isNavigable(associationEnd)) {
                                    Object oppositeClass = Model.getFacade().getClassifier(connection);
                                    al.add(new AddAssociationAction(
                                            "Add association from " +
                                            Model.getFacade().getName(oppositeClass),
                                            diagram, element, association, oppositeClass));
                                }
                            } 
                        }
                    }
                }
            }
        }
        return al;
    }
    
    private ActionList getAssociationToActions(final Object element, ArgoDiagram diagram) {
        ActionList al= new ActionList("Add Associations to ");
        if (Model.getFacade().isAClass(element)) {
            Collection associationEnds = Model.getFacade().getAssociationEnds(element);
            
            for (Object associationEnd : associationEnds) {
                Object association = Model.getFacade().getAssociation(associationEnd);
                
                // Only show actions for associations not already on diagram
                if (diagram.presentationFor(association) == null) {
                    Collection connections = Model.getFacade().getConnections(association);
                    if (connections.size() == 2) {
                        for (Object connection : connections) {
                            if (connection != associationEnd) {
                                final String direction;
                                if (!Model.getFacade().isNavigable(associationEnd)) {
                                    Object oppositeClass = Model.getFacade().getClassifier(connection);
                                    al.add(new AddAssociationAction(
                                            "Add association to " +
                                            Model.getFacade().getName(oppositeClass),
                                            diagram, element, association, oppositeClass));
                                }
                            } 
                        }
                    }
                }
            }
        }
        return al;
    }
    
    private ActionList getAssociations(final Object element, ArgoDiagram diagram) {
        ActionList al= new ActionList("Add Associations");
        if (Model.getFacade().isAClass(element)) {
            Collection associationEnds = Model.getFacade().getAssociationEnds(element);
            
            for (Object associationEnd : associationEnds) {
                Object association = Model.getFacade().getAssociation(associationEnd);
                
                // Only show actions for associations not already on diagram
                if (diagram.presentationFor(association) == null) {
                    Collection connections = Model.getFacade().getConnections(association);
                    if (connections.size() == 2) {
                        for (Object connection : connections) {
                            if (connection != associationEnd) {
                                final String direction;
                                if (Model.getFacade().isNavigable(associationEnd)) {
                                    direction = "from";
                                } else {
                                    direction = "to";
                                }
                                Object oppositeClass = Model.getFacade().getClassifier(connection);
                                al.add(new AddAssociationAction(
                                        "Add association " + direction + " " +
                                        Model.getFacade().getName(oppositeClass),
                                        diagram, element, association, oppositeClass));
                            } 
                        }
                    }
                }
            }
        }
        return al;
    }
    
    private ActionList getDependancyToActions(final Object element, ArgoDiagram diagram) {
        ActionList al= new ActionList("Add Dependancies to ");
        if (Model.getFacade().isAClass(element)) {
            
            Collection dependenciesTo = Model.getFacade().getClientDependencies(element);
            for (Object dependency : dependenciesTo) {
                
                // Only show actions for associations not already on diagram
                if (diagram.presentationFor(dependency) == null) {
                    Collection suppliers = Model.getFacade().getSuppliers(dependency);
                    for (Object supplier : suppliers) {
                        al.add(new AddUsageAction(
                                "Add dependancy to "+ Model.getFacade().getName(supplier),
                                diagram, element, dependency, supplier));
                    }
                }
            }
        }
        return al;
    }
    
    private ActionList getDependancyFromActions(final Object element, ArgoDiagram diagram) {
        ActionList al= new ActionList("Add Dependancies from ");
        if (Model.getFacade().isAClass(element)) {
            
            Collection dependenciesFrom = Model.getFacade().getSupplierDependencies(element);
            for (Object dependency : dependenciesFrom) {
                
                // Only show actions for associations not already on diagram
                if (diagram.presentationFor(dependency) == null) {
                    Collection clients = Model.getFacade().getClients(dependency);
                    for (Object client : clients) {
                        al.add(new AddUsageAction(
                                "Add dependancy from "+ Model.getFacade().getName(client),
                                diagram, element, dependency, client));
                    }
                }
            }
        }
        return al;
    }
    
    private ActionList getGeneralizationToActions(final Object element, ArgoDiagram diagram) {
        ActionList al= new ActionList("Add Generalizations to ");
        Collection generalizations = Model.getFacade().getGeneralizations(element);
        for (Object generalization : generalizations) {
            
            // Only show actions for associations not already on diagram
            if (diagram.presentationFor(generalization) == null) {
                Object general = Model.getFacade().getGeneral(generalization);
                al.add(new AddGeneralAction(
                        "Add Generalization to "+ Model.getFacade().getName(general),
                        diagram, element, generalization, general));
            }
        }
        return al;
    }
    
    private ActionList getGeneralizationFromActions(final Object element, ArgoDiagram diagram) {
        ActionList al= new ActionList("Add Generalizations from ");
        Collection specializations = Model.getFacade().getSpecializations(element);
        for (Object specialization : specializations) {
            
            // Only show actions for associations not already on diagram
            if (diagram.presentationFor(specialization) == null) {
                Object specific = Model.getFacade().getSpecific(specialization);
                al.add(new AddSpecialAction(
                        "Add Specialization from "+ Model.getFacade().getName(specific),
                        diagram, element, specialization, specific));
            }
        }
        return al;
    }
    
    private class AddAssociationAction extends AbstractAction {
        
        final Object element;
        final Object association;
        final Object oppositeClass;
        final ArgoDiagram diagram;

        public AddAssociationAction(
                final String name,
                final ArgoDiagram diagram,
                final Object element,
                final Object association,
                final Object oppositeClass) {
            super(name);
            this.element = element;
            this.association = association;
            this.diagram = diagram;
            this.oppositeClass = oppositeClass;
        }
        
        public void actionPerformed(ActionEvent arg0) {
            if (diagram.presentationFor(oppositeClass) == null) {
                Rectangle rect = diagram.presentationFor(element).getBounds();
                addToDiagram(
                        diagram, 
                        oppositeClass, 
                        new Rectangle(rect.x + rect.width + 100, rect.y, 0, 0));
            }
            addToDiagram(diagram, association, new Rectangle());
        }
        
    }
    
    private class AddUsageAction extends AbstractAction {
        
        final Object element;
        final Object usage;
        final Object oppositeClass;
        final ArgoDiagram diagram;

        public AddUsageAction(
                final String name,
                final ArgoDiagram diagram,
                final Object element,
                final Object usage,
                final Object oppositeClass) {
            super(name);
            this.element = element;
            this.usage = usage;
            this.diagram = diagram;
            this.oppositeClass = oppositeClass;
        }
        
        public void actionPerformed(ActionEvent arg0) {
            if (diagram.presentationFor(oppositeClass) == null) {
                Rectangle rect = diagram.presentationFor(element).getBounds();
                addToDiagram(
                        diagram, 
                        oppositeClass, 
                        new Rectangle(rect.x + rect.width + 100, rect.y, 0, 0));
            }
            addToDiagram(diagram, usage, new Rectangle());
        }
        
    }
    
    
    private class AddGeneralAction extends AbstractAction {
        
        final Object element;
        final Object generalization;
        final Object generalClass;
        final ArgoDiagram diagram;

        public AddGeneralAction(
                final String name,
                final ArgoDiagram diagram,
                final Object element,
                final Object generalization,
                final Object generalClass) {
            super(name);
            this.element = element;
            this.generalization = generalization;
            this.diagram = diagram;
            this.generalClass = generalClass;
        }
        
        public void actionPerformed(ActionEvent arg0) {
            if (diagram.presentationFor(generalClass) == null) {
                Rectangle rect = diagram.presentationFor(element).getBounds();
                addToDiagram(
                        diagram, 
                        generalClass, 
                        new Rectangle(rect.x, rect.y - (rect.height + 100), 0, 0));
            }
            addToDiagram(diagram, generalization, new Rectangle());
        }
        
    }
    
    private class AddSpecialAction extends AbstractAction {
        
        final Object element;
        final Object generalization;
        final Object specialClass;
        final ArgoDiagram diagram;

        public AddSpecialAction(
                final String name,
                final ArgoDiagram diagram,
                final Object element,
                final Object generalization,
                final Object specialClass) {
            super(name);
            this.element = element;
            this.generalization = generalization;
            this.diagram = diagram;
            this.specialClass = specialClass;
        }
        
        public void actionPerformed(ActionEvent arg0) {
            if (diagram.presentationFor(specialClass) == null) {
                Rectangle rect = diagram.presentationFor(element).getBounds();
                addToDiagram(
                        diagram, 
                        specialClass, 
                        new Rectangle(rect.x, rect.y + rect.height + 100, 0, 0));
            }
            addToDiagram(diagram, generalization, new Rectangle());
        }
    }
    
    
    private boolean addToDiagram(ArgoDiagram diagram, Object element, Rectangle bounds) {
        
        if (diagram.presentationFor(element) == null) {
            
            final DiagramElement de = diagram.createDiagramElement(element, bounds);
            
            Fig fig = (Fig) de;
            diagram.add(fig);
            return true;
        }
        return false;
    }
    
}
