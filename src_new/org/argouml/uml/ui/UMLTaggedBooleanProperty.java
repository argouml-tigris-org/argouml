// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import org.argouml.model.uml.UmlFactory;

import java.lang.reflect.*;
import java.util.*;
import org.argouml.model.ModelFacade;

/** An UMLTaggedBooleanProperty is a property which maintains a tagged value
 *  as a boolean field. It is e.g. used to work with UMLCheckbox.
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by ?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLTaggedBooleanProperty extends UMLBooleanProperty {
    private String _tagName;
    
    /** Creates new BooleanChangeListener */
    public UMLTaggedBooleanProperty(String tagName) {
        super(null);
        _tagName = tagName;
    }
    
    
    public void setProperty(Object obj, boolean newState) {
        if (org.argouml.model.ModelFacade.isAModelElement(obj)) {
            Object/*MModelElement*/ element = obj;
            Iterator iter = ModelFacade.getTaggedValues(element);
            boolean found = false;
            if (iter != null) {
                Object/*MTaggedValue*/ taggedValue;
                
                while (iter.hasNext()) {
                    taggedValue = iter.next();
                    if (_tagName.equals(ModelFacade.getTag(taggedValue))) {
                        if (newState) {
                            ModelFacade.setValue(taggedValue, "true");
                        } else {
                            ModelFacade.setValue(taggedValue, "false");
                        }
                        found = true;
                        break;
                    }
                }
            }
                
            if (!found) {
		Object/*MTaggedValue*/ taggedValue = UmlFactory.getFactory().getExtensionMechanisms().createTaggedValue();
		ModelFacade.setTag(taggedValue, _tagName);
		if (newState) {
		    ModelFacade.setValue(taggedValue, "true");
		} else {
		    ModelFacade.setValue(taggedValue, "false");
		}
		ModelFacade.addTaggedValue(element, taggedValue);
            }
        }
    }
                    
    
    public boolean getProperty(Object obj) {
        boolean state = false;
        if (ModelFacade.isAModelElement(obj)) {
            Object/*MModelElement*/ element = obj;
            Iterator iter = ModelFacade.getTaggedValues(element);
            if (iter != null) {
                Object/*MTaggedValue*/ taggedValue;
                while (iter.hasNext()) {
                    taggedValue = iter.next();
                    if (_tagName.equals(ModelFacade.getTag(taggedValue))) {
                        String value = (String)ModelFacade.getValue(taggedValue);
                        if ("true".equals(value)) {
                            state = true;
                        }
                        break;
                    }
                }
            }
        }
        return state;
    }
    
}
