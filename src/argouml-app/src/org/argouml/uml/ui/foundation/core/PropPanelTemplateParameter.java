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

package org.argouml.uml.ui.foundation.core;

import javax.swing.JTextField;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLTextField2;

/**
 * PropPanel for a TemplateParameter.
 * @author alepekhin
 *
 */
public class PropPanelTemplateParameter extends PropPanelModelElement {

    private static final long serialVersionUID = 8466952187322427678L;

    public PropPanelTemplateParameter() {
        super("label.template-parameter", null);
        addField(Translator.localize("label.name"), new UMLTextField2(new UMLTemplateParameterDocument(
                "TemplateParameter")));
        // TODO: not implemented yet
        addField("Type", new JTextField(""));
        addField("Default Value", new JTextField(""));
    }

    public static class UMLTemplateParameterDocument extends
            UMLPlainTextDocument {

        private static final long serialVersionUID = -2597673735297949111L;

        public UMLTemplateParameterDocument(String name) {
            super(name);
        }

        private Object getParameter() {
            return Model.getFacade().getParameter(getTarget());
        }

        @Override
        protected void setProperty(String name) {
            Model.getCoreHelper().setName(getParameter(), name);
        }

        @Override
        protected String getProperty() {
            String name = Model.getFacade().getName(getParameter());
            return name;
        }
    }

}
