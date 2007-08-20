// $Id:PropPanelConstraint.java 11516 2006-11-25 04:30:15Z tfmorris $
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

import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * Proppanel for Constraints . <p>

 */
public class PropPanelConstraint extends PropPanelModelElement {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -7621484706045787046L;

    /**
     * Construct a property panel for Constraint elements.
     */
    public PropPanelConstraint() {
        super("Constraint", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());

//        addField(Translator.localize("label.language"), Model.getFacade()
//                .getLanguage(Model.getFacade().getBody(getTarget())));

        addField(Translator.localize("label.constrained-elements"),
            new JScrollPane(new UMLLinkedList(
                    new UMLConstraintConstrainedElementListModel())));

        addSeparator();

        UMLTextArea2 text = new UMLTextArea2(new UMLConstraintBodyDocument());
        text.setEditable(false);
        text.setLineWrap(false);
        text.setRows(5);
        JScrollPane pane = new JScrollPane(text);
        addField(Translator.localize("label.constraint.body"), pane);

        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }
}

// TODO: replace this with TabConstraint code...
class UMLConstraintBodyDocument extends UMLPlainTextDocument {
    
    /**
     * Construct a document for a constraint body.
     */
    public UMLConstraintBodyDocument() {
        super("body"); 
    }
    
    /*
     * @see org.argouml.uml.ui.UMLPlainTextDocument#setProperty(java.lang.String)
     */
    protected void setProperty(String text) {
        //Model.getCoreHelper().setBody(getTarget(), text);
    }
    
    /*
     * @see org.argouml.uml.ui.UMLPlainTextDocument#getProperty()
     */
    protected String getProperty() {
        return (String) Model.getFacade().getBody(
                Model.getFacade().getBody(getTarget()));
    }
    
}
