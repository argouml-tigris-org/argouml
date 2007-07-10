// $Id$
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

package org.argouml.uml.profile;

import java.util.Iterator;

import org.argouml.model.Model;

/**
 * The Formating Strategy based on Java naming conventions 
 *
 * @author Marcos Aurélio
 */
public class JavaFormatingStrategy implements FormatingStrategy {
    	
    /**
     * @param element the element to be formated
     * @param namespace the element's namespace
     * @return the formated string
     * @see org.argouml.uml.profile.FormatingStrategy#formatElement(java.lang.Object, java.lang.Object)
     */
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
	} else {
	    name = "unknown type";
	}
	Object mult = Model.getFacade().getMultiplicity(assocEnd);
	if (mult != null) {
	    StringBuffer buf = new StringBuffer(name);
	    buf.append("[");
	    buf.append(Integer.toString(Model.getFacade().getLower(mult)));
	    buf.append("..");
	    int upper = Model.getFacade().getUpper(mult);
	    if (upper >= 0) {
		buf.append(Integer.toString(upper));
	    } else {
		buf.append("*");
	    }
	    buf.append("]");
	    name = buf.toString();
	}
	return name;
    }

    /**
     * This function creates a default association name from its ends.
     *
     * @param assoc the given association
     * @param ns the namespace
     * @return the default association name
     */
    protected String defaultAssocName(Object assoc, Object ns) {
	StringBuffer buf = new StringBuffer();
	Iterator iter = Model.getFacade().getConnections(assoc).iterator();
	for (int i = 0; iter.hasNext(); i++) {
	    if (i != 0) {
		buf.append("-");
	    }
	    buf.append(defaultAssocEndName(iter.next(), ns));
	}
	return buf.toString();
    }

    /**
     * @param gen the given Generalization
     * @param namespace the namespace
     * @return the default generalization name
     */
    protected String defaultGeneralizationName(Object gen, Object namespace) {
	Object child = Model.getFacade().getChild(gen);
        Object parent = Model.getFacade().getParent(gen);
	StringBuffer buf = new StringBuffer();
	buf.append(formatElement(child, namespace));
	buf.append(" extends ");
	buf.append(formatElement(parent, namespace));
	return buf.toString();
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
            name = "anon";
        }
	return name;
    }

    /**
     * @return the path separator (currently ".")
     */
    protected String getPathSeparator() {
	return ".";
    }

    /**
     * @param buffer (out) the buffer that will contain the path build
     * @param element the given modelelement
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
	return "[empty]";
    }

    /**
     * @param iter the iterator of the collection
     * @param namespace the namespace to be applied on the elements
     * @return the formated string
     * @see org.argouml.uml.profile.FormatingStrategy#formatCollection(java.util.Iterator, java.lang.Object)
     */
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
