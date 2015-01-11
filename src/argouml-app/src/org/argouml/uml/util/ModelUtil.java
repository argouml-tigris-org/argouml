/* $Id$
 *******************************************************************************
 * Copyright (c) 2015 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.uml.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.argouml.kernel.Project;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;

/**
 * A general utility class for manipulating the UML metamodel via the model interface package
 */
public class ModelUtil {

    private ModelUtil () {
    }
    
    /**
     * Create package dependencies based on classifier relationships.
     * Basically, if class A depends on class B then the package containing class A
     * will be made to depend on the package containing class B.
     * @param project
     * @throws IllegalModelElementConnectionException
     */
    public static void generatePackageDependencies(Project project) throws IllegalModelElementConnectionException {
        List models = project.getUserDefinedModelList();
        
        for (Object model : models) {
            
            for (Object classifier : Model.getModelManagementHelper().getAllModelElementsOfKindWithModel(model, Model.getMetaTypes().getClassifier())) {
                
                Object namespace = Model.getFacade().getNamespace(classifier);
                
                if (Model.getFacade().getNamespace(namespace) != null) {
                    Set dependentNamespaces = new HashSet();
                    
                    for (Object dependency : Model.getFacade().getClientDependencies(classifier)) {
                        for (Object dependentClass : Model.getFacade().getSuppliers(dependency)) {
                            dependentNamespaces.add(Model.getFacade().getNamespace(dependentClass));
                        }
                    }
                    for (Object generalization : Model.getFacade().getGeneralizations(classifier)) {
                        Object superClass = Model.getFacade().getGeneral(generalization);
                        dependentNamespaces.add(Model.getFacade().getNamespace(superClass));
                    }
                    
                    for (Object closeEnd : Model.getFacade().getAssociationEnds(classifier)) {
                        Object assoc = Model.getFacade().getAssociation(closeEnd);
                        for (Object assEnd : Model.getFacade().getOtherAssociationEnds(closeEnd)) {
                            if (Model.getFacade().isNavigable(assEnd)) {
                                Object associatedClassifier = Model.getFacade().getClassifier(assEnd);
                                dependentNamespaces.add(Model.getFacade().getNamespace(associatedClassifier));
                            }
                        }
                    }
    
                    for (Object dependentNamespace : dependentNamespaces) {
                        
                        if (Model.getFacade().getNamespace(dependentNamespace) != null
                                && namespace != dependentNamespace
                                && Model.getCoreHelper().getDependencies(dependentNamespace, namespace).isEmpty()) {
                            Model.getUmlFactory().buildConnection(Model.getMetaTypes().getDependency(), namespace, null, dependentNamespace, null, true, namespace);
                        }
                    }
                }
            }
        }
    }
    
}
