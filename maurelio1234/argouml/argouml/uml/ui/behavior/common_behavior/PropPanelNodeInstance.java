// $Id: PropPanelNodeInstance.java 10787 2006-06-17 19:54:31Z bobtarling $
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

package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.UMLContainerResidentListModel;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel of a NodeInstance.
 *
 * TODO: this property panel needs refactoring to remove dependency on old gui
 * components.
 */
public class PropPanelNodeInstance extends PropPanelInstance {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -3391167975804021594L;

    /**
     * Construct a property panel for Node Instance elements.
     */
    public PropPanelNodeInstance() {
        super("Node Instance", lookupIcon("NodeInstance"),
                ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"), getNameTextField());
        addField(Translator.localize("label.namespace"),
                getNamespaceSelector());

        addSeparator();

        addField(Translator.localize("label.stimili-sent"),
                getStimuliSenderScroll());

        addField(Translator.localize("label.stimili-received"),
                getStimuliReceiverScroll());

        JList resList = new UMLLinkedList(new UMLContainerResidentListModel());
        addField(Translator.localize("label.residents"),
                new JScrollPane(resList));

        addSeparator();
        AbstractActionAddModelElement a =
            new ActionAddInstanceClassifier(Model.getMetaTypes().getNode());
        JScrollPane classifierScroll =
                new JScrollPane(new UMLMutableLinkedList(
	                new UMLInstanceClassifierListModel(),
	            a, null, null, true));
	addField(Translator.localize("label.classifiers"),
                    classifierScroll);


        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }
}
