/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2009 The Regents of the University of California. All
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

package org.argouml.profile.internal;

import java.util.Iterator;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.profile.FormatingStrategy;

/**
 * The Formating Strategy based on UML naming conventions.
 *
 * @author Marcos Aur�lio
 */
public class FormatingStrategyUML implements FormatingStrategy {
    
    public String formatElement(Object element, Object namespace) {
        String value = null;
        if (element == null) {
            value = "";
        } else {
            Object elementNs = Model.getFacade().getNamespace(element);
            //
            //   if element is an AssociationEnd use
            //      the namespace of containing association
            //
            if (Model.getFacade().isAAssociationEnd(element)) {
                Object assoc = Model.getFacade().getAssociation(element);
                if (assoc != null) {
                    elementNs = Model.getFacade().getNamespace(assoc);
                }
            }
            if (elementNs == namespace) {
                value = Model.getFacade().getName(element);
                if (value == null || value.length() == 0) {
                    value = defaultName(element, namespace);
                }
            } else {
                StringBuffer buffer = new StringBuffer();
                String pathSep = getPathSeparator();
                buildPath(buffer, element, pathSep);
                value = buffer.toString();
            }
        }
        return value;
    }

    /**
     * Create a default association end name from the type of assocEnd. 
     * Follows the conventions in UML 2.2 Infrastructure, 
     * 6.2.1. "Diagram format".
     * 
     * @param assocEnd the given association end name
     * @param namespace the namespace
     * @return the default name for the given associationend
     */
    protected String defaultAssocEndName(Object assocEnd,
                                         Object namespace) {
        String name = null;
        Object type = Model.getFacade().getType(assocEnd);
        if (type != null) {
            name = formatElement(type, namespace);
            name = ensureFirstCharLowerCase(name);
        } else {
            name = Translator.localize("profile.unknown-type");
        }
        Object mult = Model.getFacade().getMultiplicity(assocEnd);
        if (mult != null) {
            int lower = Model.getFacade().getLower(mult);
            int upper = Model.getFacade().getUpper(mult);
            if (lower == upper && lower == 1) {
                // simply use name as it is
            }
            else {
                StringBuffer buf = new StringBuffer(name);
                buf.append("[");
                buf.append(Integer.toString(lower));
                buf.append("..");
                if (upper >= 0) {
                    buf.append(Integer.toString(upper));
                } else {
                    buf.append("*");
                }
                buf.append("]");
                name = buf.toString();
            }
        }
        return name;
    }

    String ensureFirstCharLowerCase(String s) {
        if (s.length() > 0) {
            return s.substring(0, 1).toLowerCase() + s.substring(1);
        }
        return s;
    }

    /**
     * Create a default association name from its ends. Follows the conventions
     * in UML 2.2 Infrastructure, 6.2.1. "Diagram format".
     *
     * @param assoc the given association
     * @param ns the namespace
     * @return the default association name
     */
    protected String defaultAssocName(Object assoc, Object ns) {
        StringBuffer buf = new StringBuffer("A_");
        Iterator iter = Model.getFacade().getConnections(assoc).iterator();
        for (int i = 0; iter.hasNext(); i++) {
            if (i != 0) {
                buf.append("_");
            }
            buf.append(defaultAssocEndName(iter.next(), ns));
        }
        return buf.toString();
    }

    /**
     * Use the term specializes, which is referred some times in the
     * UML 2.2 Infrastructure specification.
     * 
     * @param gen the given Generalization
     * @param namespace the namespace
     * @return the default generalization name
     */
    protected String defaultGeneralizationName(Object gen, Object namespace) {
        Object child = Model.getFacade().getSpecific(gen);
        Object parent = Model.getFacade().getGeneral(gen);
        return Translator.messageFormat(
            "profile.default.specializes.expression",
            new Object[] {formatElement(child, namespace),
                formatElement(parent, namespace), });
    }

    /**
     * @param element the given modelelement
     * @param namespace the namespace
     * @return a default name for this modelelement
     */
    protected String defaultName(Object element, Object namespace) {
        String name = null;
        if (Model.getFacade().isAAssociationEnd(element)) {
            name = defaultAssocEndName(element, namespace);
        } else {
            if (Model.getFacade().isAAssociation(element)) {
                name = defaultAssocName(element, namespace);
            }
            if (Model.getFacade().isAGeneralization(element)) {
                name = defaultGeneralizationName(element, namespace);
            }
        }
        if (name == null) {
            name = Translator.localize("profile.anonymous");
        }
        return name;
    }

    /**
     * @return the path separator (currently "::")
     */
    protected String getPathSeparator() {
        return "::";
    }

    /**
     * @param buffer (out) the buffer that will contain the path build
     * @param element the given model element
     * @param pathSep the path separator character(s)
     */
    private void buildPath(StringBuffer buffer, Object element, 
            String pathSep) {
        if (element != null) {
            Object parent = Model.getFacade().getNamespace(element);
            if (parent != null && parent != element) {
                buildPath(buffer, parent, pathSep);
                buffer.append(pathSep);
            }
            String name = Model.getFacade().getName(element);
            if (name == null || name.length() == 0) {
                name = defaultName(element, null);
            }
            buffer.append(name);
        }
    }

    /**
     * @return the string that separates elements
     */
    protected String getElementSeparator() {
        return ", ";
    }

    /**
     * @return the string that represents an empty collection
     */
    protected String getEmptyCollection() {
        return Translator.localize("profile.empty.collection");
    }


    public String formatCollection(Iterator iter, Object namespace) {
        String value = null;
        if (iter.hasNext()) {
            StringBuffer buffer = new StringBuffer();
            String elementSep = getElementSeparator();
            Object obj = null;
            for (int i = 0; iter.hasNext(); i++) {
                if (i > 0) {
                    buffer.append(elementSep);
                }
                obj = iter.next();
                if (Model.getFacade().isAModelElement(obj)) {
                    buffer.append(formatElement(obj, namespace));
                } else {
                    buffer.append(obj.toString());
                }
            }
            value = buffer.toString();
        } else {
            value = getEmptyCollection();
        }
        return value;
    }

}
