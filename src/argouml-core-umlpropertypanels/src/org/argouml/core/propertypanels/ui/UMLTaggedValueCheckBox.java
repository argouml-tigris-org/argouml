// $Id: UMLTaggedValueCheckBox.java 12518 2007-05-04 07:11:31Z tfmorris $
// Copyright (c) 2007 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;

/**
 * Checkbox who's state is tied to a boolean tagged value of a specific
 * name on the current target ModelElement.  Currently the value is constrained
 * to be a string with the value "true" or "false".
 * 
 * @author tfmorris
 */
public class UMLTaggedValueCheckBox extends UMLCheckBox {

    private String tagName;
    
    public UMLTaggedValueCheckBox(String name) {
        super(Translator.localize("checkbox." + name + "-lc"), 
                new ActionBooleanTaggedValue(name), 
                name);
        tagName = name;
    }

    /**
     * Set the checkbox according to the tagged values of the target.
     * 
     * @see org.argouml.uml.ui.UMLCheckBox#buildModel()
     */
    public void buildModel() {
        Object tv = Model.getFacade().getTaggedValue(getTarget(), tagName);
        if (tv != null) {
            String tag = Model.getFacade().getValueOfTag(tv);
            if ("true".equals(tag)) {
                setSelected(true);
                return;
            }
        }
        setSelected(false);
    }

    /**
     * An action which can be used to create arbitrary tagged values which hold
     * boolean data. It is designed (and implicitly) relies on a UMLCheckBox.
     *
     * @see UMLCheckBox
     * @author mkl
     */
    private static class ActionBooleanTaggedValue extends UndoableAction {

        /**
         * The class uid
         */
        private static final long serialVersionUID = 4195056245309443576L;
        
        private String tagName;

        /**
         * The constructor takes the name of the tagged value as a string, which
         * will hold boolean data.
         *
         * @param theTagName
         *            the name of the taggedvalue containing boolean values.
         */
        public ActionBooleanTaggedValue(String theTagName) {
            super(Translator.localize("Set"), null);
            // Set the tooltip string:
            putValue(Action.SHORT_DESCRIPTION, 
                    Translator.localize("Set"));
            tagName = theTagName;
        }

        /**
         * set the taggedvalue according to the condition of the checkbox. The
         * taggedvalue will be created if not existing.
         *
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            if (!(e.getSource() instanceof UMLCheckBox)) {
                return;
            }

            UMLCheckBox source = (UMLCheckBox) e.getSource();
            Object obj = source.getTarget();

            if (!Model.getFacade().isAModelElement(obj)) {
                return;
            }

            boolean newState = source.isSelected();

            Object taggedValue = Model.getFacade().getTaggedValue(obj, tagName);
            if (taggedValue == null) {
                taggedValue =
                        Model.getExtensionMechanismsFactory().buildTaggedValue(
                                tagName, "");
                // TODO: Rework to use UML 1.4 TagDefinitions - tfm
                Model.getExtensionMechanismsHelper().addTaggedValue(
                        obj, taggedValue);
            }
            if (newState) {
                Model.getCommonBehaviorHelper().setValue(taggedValue, "true");
            } else {
                Model.getCommonBehaviorHelper().setValue(taggedValue, "false");
            }
        }
    }
}