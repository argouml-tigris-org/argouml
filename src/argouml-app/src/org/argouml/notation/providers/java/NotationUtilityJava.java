/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2009 The Regents of the University of California. All
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

package org.argouml.notation.providers.java;

import java.util.Stack;

import org.argouml.model.Model;

/**
 * This class is a utility for the Java notation.
 *  
 * @author michiel
 */
public class NotationUtilityJava {

    /**
     * The constructor - nothing to construct.
     */
    NotationUtilityJava() {
    }

    /**
     * Returns a visibility String either for a VisibilityKind, but also 
     * for a model element,
     * because if it is a Feature, then the tag 'src_visibility' is to be
     * taken into account for generating language dependent visibilities.
     *
     * @param o the object which may be a VisibilityKind or a ModelElelement
     * @return the generated visibility string
     */
    static String generateVisibility(Object o) {
        if (Model.getFacade().isAFeature(o)) {
            // TODO: The src_visibility tag doesn't appear to be created
            // anywhere by ArgoUML currently
            Object tv = Model.getFacade().getTaggedValue(o, "src_visibility");
            if (tv != null) {
                Object tvValue = Model.getFacade().getValue(tv);
                /* Not all taggedvalues are string - see issue 4322: */
                if (tvValue instanceof String) {
                    String tagged = (String) tvValue;
                    if (tagged != null) {
                        if (tagged.trim().equals("")
                            || tagged.trim().toLowerCase().equals("package")
                            || tagged.trim().toLowerCase().equals("default")) {
                            return "";
                        }
                        return tagged + " ";
                    }
                }
            }
        }
        if (Model.getFacade().isAModelElement(o)) {
            if (Model.getFacade().isPublic(o)) {
                return "public ";
            }
            if (Model.getFacade().isPrivate(o)) {
                return "private ";
            }
            if (Model.getFacade().isProtected(o)) {
                return "protected ";
            }
            if (Model.getFacade().isPackage(o)) {
                return "";
            }
        }
        if (Model.getFacade().isAVisibilityKind(o)) {
            if (Model.getVisibilityKind().getPublic().equals(o)) {
                return "public ";
            }
            if (Model.getVisibilityKind().getPrivate().equals(o)) {
                return "private ";
            }
            if (Model.getVisibilityKind().getProtected().equals(o)) {
                return "protected ";
            }
            if (Model.getVisibilityKind().getPackage().equals(o)) {
                return "";
            }
        }
        return "";
    }
    
    static String generateScope(Object f) {
        if (Model.getFacade().isStatic(f)) {
            return "static ";
        }
        return "";
    }
    
    /**
     * Generate "final" keyword for final operations or attributes.
     */
    static String generateChangeability(Object obj) {
        if (Model.getFacade().isAAttribute(obj)) {
            if (Model.getFacade().isReadOnly(obj)) {
                return "final ";
            }
        } else {
            if (Model.getFacade().isAOperation(obj)) {
                if (Model.getFacade().isLeaf(obj)) {
                    return "final ";
                }
            }
        }
        return "";
    }
    
    static String generateClassifierRef(Object cls) {
        if (cls == null) {
            return "";
        }
        return Model.getFacade().getName(cls);
    }
    
    static String generateExpression(Object expr) {
        if (Model.getFacade().isAExpression(expr)) {
            return generateUninterpreted(
                    (String) Model.getFacade().getBody(expr));
        } else if (Model.getFacade().isAConstraint(expr)) {
            return generateExpression(Model.getFacade().getBody(expr));
        }
        return "";
    }
    
    static String generateUninterpreted(String un) {
        if (un == null) {
            return "";
        }
        return un;
    }
    
    static String generateParameter(Object parameter) {
        StringBuffer sb = new StringBuffer(20);
        //TODO: qualifiers (e.g., const)
        //TODO: stereotypes...
        sb.append(generateClassifierRef(Model.getFacade().getType(parameter)));
        sb.append(' ');
        sb.append(Model.getFacade().getName(parameter));
        //TODO: initial value
        return sb.toString();
    }

    /**
     * @param modelElement the UML element
     * @param args a set of arguments that may influence 
     * the generated notation
     * @return a string which represents abstractness
     */
    static String generateAbstract(Object modelElement) {
        if (Model.getFacade().isAbstract(modelElement)) {
            return "abstract ";
        }
        return "";
    }

    /**
     * @param modelElement the UML element
     * @param args a set of arguments that may influence 
     * the generated notation
     * @return a string which represents leaf
     */
    static String generateLeaf(Object modelElement) {
        if (Model.getFacade().isLeaf(modelElement)) {
            return "final ";
        }
        return "";
    }

    static String generatePath(Object modelElement) {
        StringBuilder s = new StringBuilder();
        Stack<String> stack = new Stack<String>();
        Object ns = Model.getFacade().getNamespace(modelElement);
        // TODO: Use Model.getModelManagementHelper().getPathList(modelElement);
        // TODO: This will fail with nested Models
        while (ns != null && !Model.getFacade().isAModel(ns)) {
            stack.push(Model.getFacade().getName(ns));
            ns = Model.getFacade().getNamespace(ns);
        }
        while (!stack.isEmpty()) {
            s.append(stack.pop()).append(".");
        }

        if (s.length() > 0 && !(s.lastIndexOf(".") == s.length() - 1)) {
            s.append(".");
        }
        return s.toString();
    }
    
    /**
     * @param modelElement the UML element
     * @param settings  settings which influence the generated notation
     * @return a string which represents the visibility
     */
//    static String generateVisibility(Object modelElement, 
//            NotationSettings settings) {
//        String s = "";
//        if (settings.isShowVisibility()) {
//            s = NotationUtilityJava.generateVisibility(modelElement);
//        }
//        return s;
//    }
}
