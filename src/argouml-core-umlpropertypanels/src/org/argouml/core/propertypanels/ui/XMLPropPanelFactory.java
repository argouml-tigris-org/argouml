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

package org.argouml.core.propertypanels.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.argouml.core.propertypanels.model.MetaDataCache;
import org.argouml.core.propertypanels.model.PanelData;
import org.argouml.model.Model;
import org.argouml.uml.ui.PropPanelFactory;

/**
 *
 * @author penyaskito
 */
public class XMLPropPanelFactory implements PropPanelFactory {

    private static final Logger LOG =
        Logger.getLogger(XMLPropPanelFactory.class.getName());

    /**
     * new cache
     */
    private final MetaDataCache metaDataCache = new MetaDataCache();

    private static XMLPropPanelFactory instance;

    public static synchronized XMLPropPanelFactory getInstance()
        throws Exception {
        if (instance == null) {
            instance = new XMLPropPanelFactory();
        }
        return instance;
    }

    private XMLPropPanelFactory() throws Exception {
    }

    /**
     * Create the XML driven property panel for the given target
     */
    public JPanel createPropPanel(Object target) {
        if (Model.getFacade().isAElement(target) || Model.getFacade().isATemplateParameter(target)) {
            JPanel panel =
                new XmlPropertyPanel();
            build(panel, target);
            return panel;
        } else {
            return null;
        }
    }

    private void build(JPanel panel, Object target) {
        // if we have anything or multiple elements selected,
        // we don't do anything
        // TODO: We need to support multiple selection.
        // See issue 2552: http://argouml.tigris.org/issues/show_bug.cgi?id=2552
        panel.removeAll();
        if (target == null){
            return;
        }

        try {
            // TODO: This references the concrete factory
            // We need a factories factory
            SwingUIFactory builder = new SwingUIFactory();
            builder.createGUI(target, panel);
        } catch (Exception e) {
            // TODO: Auto-generated catch block
            LOG.log(Level.SEVERE, "Exception", e);
        }
    }

    public PanelData getPropertyPanelsData (Class<?> clazz) {
        return metaDataCache.get(clazz);
    }

}
