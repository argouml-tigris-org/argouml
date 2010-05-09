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

package org.argouml.diagram.uml2;


import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.ui.PropPanelDiagram;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.PropPanelFactory;

/**
 * Factory implementation for create a sequence diagram prop panel.
 *
 * @author Bob Tarling
 */
class ClassDiagram2PropPanelFactory implements PropPanelFactory {

    public PropPanel createPropPanel(Object object) {
        if (object instanceof UMLClassDiagram2) {
            return new PropPanelUMLClassDiagram();
        }
        return null;
    }

    /**
     * The properties panel for a class diagram.
     */
    class PropPanelUMLClassDiagram extends PropPanelDiagram {

        /**
         * Constructor for PropPanelUMLClassDiagram.
         */
        public PropPanelUMLClassDiagram() {
            super(Translator.localize("label.class-diagram"),
                    lookupIcon("ClassDiagram"));
        }

    }
}
