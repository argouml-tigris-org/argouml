/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.core.propertypanels.module;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.core.propertypanels.ui.XMLPropPanelFactory;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.uml.ui.PropPanelFactory;
import org.argouml.uml.ui.PropPanelFactoryManager;

/**
 * Defines the XMLPropertyPanels module.
 *
 * @author penyaskito
 */
public class XmlPropertyPanelsModule implements ModuleInterface {

    /**
     * The logger.
     */
    private static final Logger LOG =
        Logger.getLogger(XmlPropertyPanelsModule.class.getName());

    public boolean enable() {
        try {
            PropPanelFactory elementFactory = XMLPropPanelFactory.getInstance();
            PropPanelFactoryManager.addPropPanelFactory(elementFactory);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception caught", e);
            return false;
        }
    }

    public boolean disable() {
        /* Set up the property panels for UML elements: */
        try {
            PropPanelFactory elementFactory = XMLPropPanelFactory.getInstance();
            PropPanelFactoryManager.removePropPanelFactory(elementFactory);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception caught", e);
            return false;
        }
    }

    public String getInfo(int type) {
        switch (type) {
        case AUTHOR:
            return "ArgoUML Team";
        case DESCRIPTION:
            return "Module for adding property panels based on XML";
        case DOWNLOADSITE:
            return "Not released. Prototyping yet.";
        case VERSION:
            return "0.1";
        default:
            return null;
        }
    }

    public String getName() {
        return "Xml Property Panels Module";
    }
}
