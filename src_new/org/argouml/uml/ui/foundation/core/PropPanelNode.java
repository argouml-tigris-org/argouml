// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 *
 * @author 5eichler
 */
public class PropPanelNode extends PropPanelClassifier {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 2681345252220104772L;

    /**
     * Construct a property panel for a UML Node element.
     */
    public PropPanelNode() {
        super("Node", lookupIcon("Node"),
                ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());

        add(getModifiersPanel());

        addSeparator();

        addField("Generalizations:", getGeneralizationScroll());

        addField("Specializations:", getSpecializationScroll());

        addSeparator();

        JList resList = new UMLLinkedList(
                new UMLNodeDeployedComponentListModel());
        addField(Translator.localize("label.deployedcomponents"),
                new JScrollPane(resList));

        addAction(new ActionNavigateContainerElement());
        addAction(getActionNewReception());
        addAction(new ActionNewStereotype());
        addAction(new ActionDeleteSingleModelElement());
    }


} /* end class PropPanelNode */

class UMLNodeDeployedComponentListModel extends UMLModelElementListModel2 {
    
    /**
     * The serial version.
     */
    private static final long serialVersionUID = -7137518645846584922L;

    /**
     * Construct a list model for the deployed components of a Node.
     */
    public UMLNodeDeployedComponentListModel() {
        super("deployedComponent");
    }
    
    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (Model.getFacade().isANode(getTarget())) {
            setAllElements(
                    Model.getFacade().getDeployedComponents(getTarget()));
        }
    }
    
    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object o) {
        return (Model.getFacade().isAComponent(o));
    }
    
}