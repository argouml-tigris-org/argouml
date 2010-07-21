/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.core.propertypanels.ui;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import org.argouml.core.propertypanels.model.GetterSetterManager;
import org.argouml.model.Model;

/**
 * Creates the XML Property panels
 * @author Bob Tarling
 */
class SingleListFactory implements ComponentFactory {
    
    public JComponent createComponent(
            final Object modelElement,
            final String propName,
            final List<Class<?>> types) {
        
        DefaultListModel model = null;
        
        if ("owner".equals(propName)) {
            model = new UMLFeatureOwnerListModel(modelElement, propName);
        } else if ("behavioralFeature".equals(propName)) {
            model = new UMLParameterBehavioralFeatListModel(modelElement, propName);
        } else if ("parent".equals(propName)) {
            model = new UMLGeneralizationParentListModel(modelElement, propName);
        } else if ("child".equals(propName)) {
            model = new UMLGeneralizationChildListModel(modelElement, propName);
        } else if ("feature".equals(propName)) {
            model = new UMLParameterBehavioralFeatListModel(modelElement, propName);
        } else if ("enumeration".equals(propName)) {
            model = new EnumerationListModel(modelElement, propName);
        } else if ("association".equals(propName)) {
            model = new UMLAssociationEndAssociationListModel(modelElement, propName);
        } else if ("base".equals(propName)) {
            model = new UMLExtendBaseListModel(modelElement, propName);
        } else if ("extension".equals(propName)) {
            model = new UMLExtendExtensionListModel(modelElement, propName);
        } else if ("addition".equals(propName)) {
            model = new UMLIncludeAdditionListModel(modelElement, propName);
        } else if ("useCase".equals(propName)) {
            model = new UMLExtensionPointUseCaseListModel(modelElement, propName);
        } else if ("interaction".equals(propName)) {
            if (Model.getFacade().isAMessage(modelElement)) {
                model = new UMLMessageInteractionListModel(modelElement, propName);
            } else {
                model = new UMLCollaborationInteractionListModel(modelElement, propName);
            }
        } else if ("sender".equals(propName)) {
            if (Model.getFacade().isAMessage(modelElement)) {
                model = new UMLMessageSenderListModel(modelElement, propName);
            }
        } else if ("receiver".equals(propName)) {
            if (Model.getFacade().isAMessage(modelElement)) {
                model = new UMLMessageReceiverListModel(modelElement, propName);
            }
        } else if ("action".equals(propName)) {
            model = new UMLMessageActionListModel(modelElement, propName);
        } else if ("context".equals(propName)) {
            model = new UMLInteractionContextListModel(modelElement, propName);
        } else if ("stateMachine".equals(propName)) {
            model = new UMLTransitionStatemachineListModel(modelElement, propName);
        } else if ("state".equals(propName)) {
            model = new UMLTransitionStateListModel(modelElement, propName);
        } else if ("source".equals(propName)) {
            model = new UMLTransitionSourceListModel(modelElement, propName);
        } else if ("target".equals(propName)) {
            model = new UMLTransitionTargetListModel(modelElement, propName);
        } else if ("transition".equals(propName)) {
            model = new UMLGuardTransitionListModel(modelElement, propName);
        } else if ("container".equals(propName)) {
            model = new UMLStateVertexContainerListModel(modelElement, propName);
        } else if ("activityGraph".equals(propName)) {
            model = new UMLPartitionActivityGraphListModel(modelElement, propName);
        } else if ("template".equals(propName)) {
            model = new UMLTemplateParameterTemplateListModel(modelElement, propName);
        } else if ("parameter".equals(propName)) {
            model = new UMLTemplateParameterParameterListModel(modelElement, propName);
        }
        
        if (model == null) {
            final GetterSetterManager getterSetterManager =
                GetterSetterManager.getGetterSetter(types.get(0));
            if (getterSetterManager.contains(propName)) {
                model = new SimpleListModel(propName, types, modelElement, getterSetterManager);
            }
        }
        
        
        
        if (model != null) {
            return new RowSelector(model, false, false);
        }
        
        return null;
    }
}
