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

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

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
    
    
    public void setProperty(Object obj,boolean newState) {
        if(obj instanceof MModelElement) {
            MModelElement element = (MModelElement) obj;
            Collection taggedValues = element.getTaggedValues();
            boolean found = false;
            if(taggedValues != null) {
                MTaggedValue taggedValue;
                Iterator iter = taggedValues.iterator();
                
                while(iter.hasNext()) {
                    taggedValue = (MTaggedValue) iter.next();
                    if(_tagName.equals(taggedValue.getTag())) {
                        if(newState) {
                            taggedValue.setValue("true");
                        }
                        else {
                            taggedValue.setValue("false");
                        }
                        found = true;
                        break;
                    }
                }
            }
                
            if(!found) {
                 MTaggedValue taggedValue = UmlFactory.getFactory().getExtensionMechanisms().createTaggedValue();
                 taggedValue.setTag(_tagName);
                 if(newState) {
                      taggedValue.setValue("true");
                 }
                 else {
                      taggedValue.setValue("false");
                 }
                 element.addTaggedValue(taggedValue);
            }
        }
    }
                    
    
    public boolean getProperty(Object obj) {
        boolean state = false;
        if(obj instanceof MModelElement) {
            MModelElement element = (MModelElement) obj;
            Collection taggedValues = element.getTaggedValues();
            if(taggedValues != null) {
                MTaggedValue taggedValue;
                Iterator iter = taggedValues.iterator();
                while(iter.hasNext()) {
                    taggedValue = (MTaggedValue) iter.next();
                    if(_tagName.equals(taggedValue.getTag())) {
                        String value = taggedValue.getValue();
                        if("true".equals(value)) {
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


