// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;


/**
 * The properties panel of an Object.
 */
public class PropPanelObject extends PropPanelInstance {

    /**
     * Construct a property panel for UML Object elements.
     */
    public PropPanelObject() {
	super("label.object", lookupIcon("Object"));

	addField(Translator.localize("label.name"), getNameTextField());

	addField(Translator.localize("label.namespace"),
		     getNamespaceSelector());

        addSeparator();

	addField(Translator.localize("label.stimili-sent"),
            getStimuliSenderScroll());

	addField(Translator.localize("label.stimili-received"),
            getStimuliReceiverScroll());

	addSeparator();

	AbstractActionAddModelElement action =
	    new ActionAddInstanceClassifier(
                    Model.getMetaTypes().getClassifier());
	JScrollPane classifierScroll =
	    new JScrollPane(
	            new UMLMutableLinkedList(
	                    new UMLInstanceClassifierListModel(),
	                    action, null, null, true));
	addField(Translator.localize("label.classifiers"),
            classifierScroll);


	addAction(new ActionNavigateNamespace());
	addAction(new ActionNewStereotype());
	addAction(getDeleteAction());

    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3594423150761388537L;
}
