// Copyright (c) 1996-99 The Regents of the University of California. All
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


package org.argouml.uml;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import java.lang.reflect.*;

import java.util.*;

/**
 *   This class represents a stereotype that is known in a profile
 *   but is only added to the model on use.
 *
 *   @author Curt Arnold
 */
public final class ProfileStereotype {
    
    private String _name;
    private String _baseClass;
    
    /**
     *    constructs a new profile stereotype
     *     @param name name of classifier.
     *     @param baseClass name of base class (for example, Classifier or Abstraction)
     */
    public ProfileStereotype(String name,String baseClass) {
        _name = name;
        _baseClass = baseClass;
    }
    
    
    /**
     *   Returns name of stereotype
     */
    public String toString() {
        return _name;
    }
    
    /**
     *   Creates the stereotype
     *   @param model current model.
     */
    public MStereotype createStereotype(MModel model) {
        MStereotype stereotype = getStereotype(model);
        
        if(stereotype == null) {
            stereotype = new MStereotypeImpl();
            stereotype.setName(_name);
            stereotype.setBaseClass(_baseClass);
            model.addOwnedElement(stereotype);
        }
        
        return stereotype;
    }
    
    /**
     *   Retrieves (but does not create) a corresponding model stereotype
     *   @param ns 
     **/
    public MStereotype getStereotype(MNamespace ns) {
    
        MStereotype stereotype = null;
    
        Collection ownedElements = ns.getOwnedElements();
        if(ownedElements != null) {
            Iterator iter = ownedElements.iterator();
            Object element;
            MStereotype current;
            String name;
            String baseClass;
            while(iter.hasNext()) {
                element = iter.next();
                if(element instanceof MStereotype) {
                    if(equals((MStereotype) element)) {
                        stereotype = (MStereotype) element;
                    }
                }
                else {
                    if(element instanceof MNamespace) {
                        stereotype = getStereotype((MNamespace) element);
                        if(stereotype != null) break;
                    }
                }
            }
        }
        return stereotype;
    }
    
    
    
    public boolean exists(MModel model) {
        return getStereotype(model) != null;
    }
    
    public boolean equals(MStereotype stereotype) {
        String name = stereotype.getName();
        String baseClass = stereotype.getBaseClass();
        if(name != null && baseClass != null &&
            _name.equals(name) && _baseClass.equals(_baseClass)) {
                return true;
        }
        return false;
    }
    
    public boolean isNamed(String name) {
        if(_name != null && name != null && _name.equals(name)) {
            return true;
        }
        return false;
    }
    
}