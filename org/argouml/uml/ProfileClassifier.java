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
 *   This class represents a classifier that is known in a profile
 *   but is only added to the model on use.
 *
 *   @author Curt Arnold
 */
public class ProfileClassifier {
    
    private String _name;
    private String[] _packageNames;
    private Class _classifierImpl;
    private Class _classifierType;
    
    /**
     *    constructs a new profile classifier
     *     @param classifierImpl implementation class, for example MDataType.class.
     *     @param name name of classifier.
     *     @param packageNames names of parent packages, for example: new String[] { "java", "lang" }.
     */
    public ProfileClassifier(Class classifierType,Class classifierImpl,String name,String[] packageNames) {
        _classifierType = classifierType;
        _classifierImpl = classifierImpl;
        _name = name;
        _packageNames = packageNames;
    }
    
    
    /**
     *   Returns name of classifier.
     */
    public String toString() {
        return _name;
    }
    
    /**
     *   Creates the classifier, may create needed packages also.
     *   @param model current model.
     */
    public MClassifier createClassifier(MModel model) {
        MClassifier classifier = null;
        //
        //   if these are both null, then it is a "void" classifier.
        //
        if(_classifierType != null && _classifierImpl != null) {
            try {
                MPackage mpackage = model;
                if(_packageNames != null) {
                    mpackage = createPackage(model,_packageNames,0);
                }

                Collection ownedElements = mpackage.getOwnedElements();
                Object obj;
                if(ownedElements != null) {
                    Iterator iter = ownedElements.iterator();
                    String otherName;
                    while(iter.hasNext()) {
                        obj = iter.next();
                        if(_classifierType.isAssignableFrom(obj.getClass())) {
                            otherName = ((MClassifier) obj).getName();
                            if(otherName != null && otherName.equals(_name)) {
                                classifier = (MClassifier) obj;
                                break;
                            }
                        }
                    }
                }

                if(classifier == null) {
                    Class[] noClasses = {};
                    Constructor constructor = _classifierImpl.getConstructor(noClasses);
                    Object[] noArgs = {};
                    classifier = (MClassifier) constructor.newInstance(noArgs);
                    classifier.setName(_name);
                    mpackage.addOwnedElement(classifier);
                }
            }
            catch(Exception e) {
                System.out.println(e.toString() + " in ProfileClassifier.createClassifier()");
            }
        }
        return classifier;
    }
    
    
    /**
     *   Gets the classifier, does not create model elements.
     *   @param model current model.
     */
    public MClassifier getClassifier(MModel model) {
        MClassifier classifier = null;
        MPackage mpackage = model;
        if(_packageNames != null) {
            mpackage = getPackage(model,_packageNames,0);
        }

        if(mpackage != null) {
            Collection ownedElements = mpackage.getOwnedElements();
            Object obj;
            if(ownedElements != null) {
                Iterator iter = ownedElements.iterator();
                while(iter.hasNext()) {
                    obj = iter.next();
                    if(_classifierType.isAssignableFrom(obj.getClass())) {
                        if(_name.equals(((MClassifier) obj).getName())) {
                            classifier = (MClassifier) obj;
                            break;
                        }
                    }
                }
            }
        }    
        return classifier;
    }
    

    /**
     *   This method finds the specified package.
     *   @return null if package does not exist
     */
    private MPackage getPackage(MPackage model,String[] packageNames,int offset) {
        MPackage pack = null;
        Collection ownedElements = model.getOwnedElements();
        String packageName = packageNames[offset];
        if(ownedElements != null) {
            Iterator iter = ownedElements.iterator();
            Object obj;
            while(iter.hasNext()) {
                obj = iter.next();
                if(obj instanceof MPackage) {
                    if(packageName.equals(((MPackage) obj).getName())) {
                        pack = (MPackage) obj;
                        break;
                    }
                }
            }
        }
        if(pack != null && offset < packageNames.length-1) {
            pack = getPackage(pack,packageNames,offset+1);
        }
        return pack;
    }
                
    
    /**
     *   This method creates any necessary packages
     */
    private MPackage createPackage(MPackage model,String[] packageNames,int offset) {
        MPackage pack = null;
        Collection ownedElements = model.getOwnedElements();
        String packageName = packageNames[offset];
        if(ownedElements != null) {
            Iterator iter = ownedElements.iterator();
            Object obj;
            while(iter.hasNext()) {
                obj = iter.next();
                if(obj instanceof MPackage) {
                    if(packageName.equals(((MPackage) obj).getName())) {
                        pack = (MPackage) obj;
                        break;
                    }
                }
            }
        }
        if(pack == null) {
             pack = new MPackageImpl();
             pack.setName(packageName);
             model.addOwnedElement(pack);
        }
        if(offset < packageNames.length-1) {
            pack = createPackage(pack,packageNames,offset+1);
        }
        return pack;
    }
    
        
    public Class getClassInterface() {
        return _classifierType;
    }
    
    public String getFullName() {
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < _packageNames.length; i++) {
            buf.append(_packageNames[i]);
            buf.append(".");
        }
        buf.append(_name);
        return buf.toString();
    }
    
    
    public boolean exists(MModel model) {
        boolean exists = false;
        MPackage pack = getPackage(model,_packageNames,0);
        if(pack != null) {
            Collection ownedElements = pack.getOwnedElements();
            if(ownedElements != null) {
                Iterator iter = ownedElements.iterator();
                Object obj;
                while(iter.hasNext()) {
                    obj = iter.next();
                    if(_classifierType.isAssignableFrom(obj.getClass())) {
                        if(_name.equals(((MClassifier) obj).getName())) {
                            exists = true;
                            break;
                        }
                    }
                }
            }
        }
        return exists;
    }
    
    boolean equals(MClassifier classifier) {
        boolean value = false;
        if(_name.equals(classifier.getName()) &&
            _classifierType.isAssignableFrom(classifier.getClass())) {
            MModel model = classifier.getModel();
            MPackage pack = getPackage(model,_packageNames,0);
            if(pack == classifier.getNamespace()) {
                value = true;
            }
        }
        return value;
    }
    
}