// $Id: NotationUtilityJava.java 12731 2007-05-30 19:06:18Z tfmorris $
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

import java.util.HashMap;
import java.util.Stack;

import org.argouml.model.Model;
import org.argouml.notation.providers.NotationProvider;

/**
 * This class is a utility for the Java notation.
 *  
 * @author michiel
 */
public class NotationUtilityJava {

    /**
     * The constructor - nothing to construct.
     */
    NotationUtilityJava() { }

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
            if (!Model.getFacade().isReadOnly(obj)) {
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
        if (cls == null)
            return "";
        return Model.getFacade().getName(cls);
    }
    
    static String generateExpression(Object expr) {
        if (Model.getFacade().isAExpression(expr))
            return generateUninterpreted(
                    (String) Model.getFacade().getBody(expr));
        else if (Model.getFacade().isAConstraint(expr))
            return generateExpression(Model.getFacade().getBody(expr));
        return "";
    }
    
    static String generateUninterpreted(String un) {
        if (un == null)
            return "";
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
    static String generateAbstract(Object modelElement, HashMap args) {
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
    static String generateLeaf(Object modelElement, HashMap args) {
        if (Model.getFacade().isLeaf(modelElement)) {
            return "final ";
        }
        return "";
    }

    /**
     * @param modelElement the UML element
     * @param args a set of arguments that may influence 
     * the generated notation
     * @return a string which represents the path
     */
    static String generatePath(Object modelElement, 
            HashMap args) {
        String s = "";
        if (NotationProvider.isValue("pathVisible", args)) {
            Stack stack = new Stack();
            Object ns = Model.getFacade().getNamespace(modelElement);
            while (ns != null && !Model.getFacade().isAModel(ns)) {
                stack.push(Model.getFacade().getName(ns));
                ns = Model.getFacade().getNamespace(ns);
            }
            while (!stack.isEmpty()) {
                s += (String) stack.pop() + ".";
            }

            if (s.length() > 0 && !s.endsWith(".")) {
                s += ".";
            }
        }
        return s;
    }

    /**
     * @param modelElement the UML element
     * @param args a set of arguments that may influence 
     * the generated notation
     * @return a string which represents the visibility
     */
    static String generateVisibility(Object modelElement, 
            HashMap args) {
        String s = "";
        if (NotationProvider.isValue("visibilityVisible", args)) {
            s = NotationUtilityJava.generateVisibility(modelElement);
        }
        return s;
    }
}
