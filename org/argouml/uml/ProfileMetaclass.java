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
 *   This class represents the profile information for a metaclass 
 *        (for example, generalization, association, class) 
 *
 *   @author Curt Arnold
 */
public class ProfileMetaclass {
 
    private Class _metaClass;
    private String _metaName;
    private Collection _stereotypes;
    
    
    /**
     *    constructs a new profile metaclass
     *     @param metaClass meta class (for example, MGeneralization)
     *     @param stereotypeNames array of stereotype names
     */
    public ProfileMetaclass(Class metaClass,String[] stereotypeNames) {
        _metaClass = metaClass;
        _metaName = _metaClass.getName();
        //
        //   now strip off the ru.novosoft.uml....
        //
        int last = _metaName.lastIndexOf(".M");
        if(last >= 0) _metaName = _metaName.substring(last+2);
        
        if(stereotypeNames != null) {
            _stereotypes = new ArrayList(stereotypeNames.length);
            for(int i = 0; i < stereotypeNames.length; i++) {
                _stereotypes.add(new ProfileStereotype(stereotypeNames[i],_metaName));
            }
        }
    }
    
    
    /**
     *   Returns name of metaclass
     */
    public String toString() {
        return _metaName;
    }
    
    public boolean hasStereotype(String name) {
        if(_stereotypes != null) {
            Iterator iter = _stereotypes.iterator();
            while(iter.hasNext()) {
                if(((ProfileStereotype) iter.next()).isNamed(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isAssignableFrom(Class other) {
        return _metaClass.isAssignableFrom(other);
    }

    public void addStereotypes(Collection collection) {
        if(_stereotypes != null) {
            Iterator iter = _stereotypes.iterator();
            while(iter.hasNext()) {
                collection.add(iter.next());
            }
        }
    }
}