/* $Id$
 *****************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.deployment2;

import java.util.logging.Logger;

import org.argouml.deployment2.diagram.DeploymentDiagramFactory;
import org.argouml.model.Model;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramFactory.DiagramType;
import org.argouml.uml.diagram.DiagramFactoryInterface2;
import org.argouml.uml.ui.PropPanelFactoryManager;

public class DeploymentDiagramModule implements ModuleInterface {

    private static final Logger LOG =
        Logger.getLogger(DeploymentDiagramModule.class.getName());

    private DeploymentDiagramPropPanelFactory propPanelFactory;

    public boolean enable() {
        if (Model.getFacade().getUmlVersion().indexOf("2") >= 0) {
            // This module will still register as enabled for UML1.4 but it won't
            // actually do anything.
            propPanelFactory =
                new DeploymentDiagramPropPanelFactory();
            PropPanelFactoryManager.addPropPanelFactory(propPanelFactory);
            DiagramFactory.getInstance().registerDiagramFactory(
                    DiagramType.Deployment,
                    new DeploymentDiagramFactory());
        }
        return true;
    }

    public boolean disable() {

        PropPanelFactoryManager.removePropPanelFactory(propPanelFactory);

        // TODO: Remove the casting to DiagramFactoryInterface2
        // as soon as DiagramFactoryInterface is removed.
        DiagramFactory.getInstance().registerDiagramFactory(
                DiagramType.Deployment, (DiagramFactoryInterface2) null);
        return true;
    }

    public String getName() {
        return "ArgoUML-Deployment";
    }

    public String getInfo(int type) {
        switch (type) {
        case DESCRIPTION:
            return "Deployment diagram implementation";
        case AUTHOR:
            return "ArgoUML Team";
        case VERSION:
            return "0.28";
        case DOWNLOADSITE:
            return "http://argouml.tigris.org";
        default:
            return null;
        }
    }
}
