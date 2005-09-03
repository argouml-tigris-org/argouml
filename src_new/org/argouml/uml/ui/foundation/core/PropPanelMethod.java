// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import javax.swing.JTextField;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateOwner;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.util.ConfigLoader;

/**
 * A property panel for methods.
 *
 * @author thn@tigris.org
 */
public class PropPanelMethod extends PropPanelFeature {

    private JTextField languageTextField;
    private UMLModelElementLanguageDocument languageDocument =
        new UMLModelElementLanguageDocument();

    ////////////////////////////////////////////////////////////////
    // contructors
    /**
     * The constructor.
     */
    public PropPanelMethod() {
        super("Method", lookupIcon("Method"), ConfigLoader
                .getTabPropsOrientation());
        UMLPlainTextDocument uptd = new UMLMethodBodyDocument();

        addField(Translator.localize("label.name"),
                getNameTextField());

        addField(Translator.localize("label.language"),
                getLanguageTextField());

        addField(Translator.localize("label.owner"),
                getOwnerScroll());

        addSeperator();

        UMLTextArea2 bodyArea = new UMLTextArea2(uptd);
        bodyArea.setLineWrap(true);
        bodyArea.setRows(5);
        JScrollPane pane = new JScrollPane(bodyArea);
        addField(Translator.localize("label.body"), pane);

        addButton(new PropPanelButton2(new ActionNavigateOwner()));
        addButton(new PropPanelButton2(new ActionDeleteSingleModelElement(),
                lookupIcon("Delete")));
    }

    /**
     * @return a textfield for the name
     */
    protected JTextField getLanguageTextField() {
        if (languageTextField == null) {
            languageTextField = new UMLTextField2(languageDocument);
        }
        return languageTextField;
    }

    private class UMLModelElementLanguageDocument extends UMLPlainTextDocument {

        /**
         * Constructor for UMLModelElementNameDocument.
         */
        public UMLModelElementLanguageDocument() {
             super("name");
        }

        /**
         * @see org.argouml.uml.ui.UMLPlainTextDocument#setProperty(java.lang.String)
         */
        protected void setProperty(String text) {
            Object meth = getTarget();
            if (Model.getFacade().isAMethod(meth)) {
                Object expr = Model.getFacade().getBody(meth);
                if (expr != null) {
                    String body = (String)Model.getFacade().getBody(expr);
                    Model.getCoreHelper().setBody(meth,
                            Model.getDataTypesFactory()
                                .createProcedureExpression(text, body));
                }
            }
        }

        /**
         * @see org.argouml.uml.ui.UMLPlainTextDocument#getProperty()
         */
        protected String getProperty() {
            Object expr = Model.getFacade().getBody(getTarget());
            return Model.getDataTypesHelper().getLanguage(expr);
        }

    }

    private class UMLMethodBodyDocument extends UMLPlainTextDocument {
        /**
         * Constructor for UMLMethodBodyDocument.
         */
        public UMLMethodBodyDocument() {
            super("body");
            /*
             * TODO: This is probably not the right location
             * for switching off the "filterNewlines".
             * The setting gets lost after selecting a different
             * ModelElement in the diagram.
             * BTW, see how it is used in
             * javax.swing.text.PlainDocument.
             * See issue 1812.
             */
            putProperty("filterNewlines", Boolean.FALSE);
        }

        /**
         * @see org.argouml.uml.ui.UMLPlainTextDocument#setProperty(java.lang.String)
         */
        protected void setProperty(String text) {
            Object meth = getTarget();
            if (Model.getFacade().isAMethod(meth)) {
                Object expr = Model.getFacade().getBody(meth);
                if (expr != null) {
                    String lang = Model.getDataTypesHelper().getLanguage(expr);
                    Model.getCoreHelper().setBody(text,
                            Model.getDataTypesFactory()
                                .createProcedureExpression(lang, text));
                }
            }
        }

        /**
         * @see org.argouml.uml.ui.UMLPlainTextDocument#getProperty()
         */
        protected String getProperty() {
            Object expr = Model.getFacade().getBody(getTarget());
            return (String)Model.getFacade().getBody(expr);
        }
    }
} /* end class PropPanelMethod */
