// $Id: UMLModelElementTaggedValueDocument.java 12385 2007-04-13 18:00:16Z mvw $
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.argouml.model.Model;

/**
 * This class provides a text field that can be used to access
 * tagged values of a ModelElement object.
 * UMLModelElementTaggedValueDocument is especially useful when
 * using LabelledLayout.
 *
 * @since 5th April 2003
 * @author Raphael Langerhorst (raphael-langerhorst@gmx.at)
 */
public class UMLModelElementTaggedValueDocument extends UMLPlainTextDocument {

    /**
     * Creates a UMLPlainTextDocument object that represents a tagged value of
     * an ModelElement object.
     *
     * @param taggedValue the tagged value
     */
    public UMLModelElementTaggedValueDocument(String taggedValue) {
        //stores the action command into the UMLPlainTextDocument
        //class which is also used
        //for setProperty and getProperty
        
        // TODO: This appears to expect that the UML 1.3 tag name
        // will appear as a property name in an event, but with the
        // UML 1.4 switch to TagDefinitions, this won't work
        super(taggedValue);
    }

    /**
     * Sets the tagged value to given String.
     *
     * @param text the property
     */
    protected void setProperty(String text) {
        if (getTarget() != null) {
            Model.getCoreHelper().setTaggedValue(
                    getTarget(),
                    getEventName(),
                    text);
        }
    }

    /**
     *
     * @return the value of the tagged value
     */
    protected String getProperty() {
        String eventName = getEventName();
        if (Model.getFacade().isAModelElement(getTarget())) {
            Object taggedValue =
                Model.getFacade().getTaggedValue(getTarget(), eventName);
            if (taggedValue != null) {
                return Model.getFacade().getValueOfTag(taggedValue);
            } 
        }
        return "";
    }
}
