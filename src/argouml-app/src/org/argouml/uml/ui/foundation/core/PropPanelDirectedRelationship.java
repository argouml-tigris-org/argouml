/* $Id$
 *******************************************************************************
 * Copyright (c) 2009,2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation
 *******************************************************************************
 */
package org.argouml.uml.ui.foundation.core;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLLinkedList;

/**
 * The properties panel for a DirectedRelationship.
 * 
 * @since UML 2.x 
 */
public class PropPanelDirectedRelationship extends PropPanelRelationship {

    private JComponent sourceList;
    private JComponent targetList;

    /**
     * Construct a property panel for a DirectedRelationship.
     */
    public PropPanelDirectedRelationship() {
        super("label.relationship", lookupIcon("Relationship"));
        
        sourceList = new JScrollPane(new UMLLinkedList(
                new UMLDirectedRelationshipSourceListModel(), true));
        targetList = new JScrollPane( new UMLLinkedList(
                new UMLDirectedRelationshipTargetListModel(), true));
        
        addField(Translator.localize("label.name"), getNameTextField());
        addField(Translator.localize("label.namespace"), getNamespaceSelector());

        addSeparator();

        addField(Translator.localize("label.sources"), getSourceList());
        addField(Translator.localize("label.targets"), getTargetList());

        addAction(new ActionNavigateNamespace());
        addAction(getDeleteAction());
    }


    /**
     * @return Return the GUI component containing the list of targets.
     */
    protected JComponent getTargetList() {
        return targetList;
    }

    /**
     * @return Returns the GUI component containing the list of sources.
     */
    protected JComponent getSourceList() {
        return sourceList;
    }

}

