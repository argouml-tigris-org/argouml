// $Id$
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Category;
import org.argouml.xml.xmi.XMIReader;
import org.xml.sax.InputSource;

import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.model_management.MModel;

/**
 *   This class implements the abstract class Profile for use in modelling
 *   Java language projects.  Eventually, this class may be replaced by
 *   a configurable profile.
 *
 *   @author Curt Arnold
 */
public class ProfileJava extends Profile {
    protected static Category cat = Category.getInstance(ProfileJava.class);
    private static ProfileJava _instance = null;
    public static ProfileJava getInstance() {
	if (_instance == null)
	    _instance = new ProfileJava();
	return _instance;
    }

    MModel _defaultModel;

    private ProfileJava() {
	getProfileModel();
    }

    public String formatElement(MModelElement element, MNamespace namespace) {
	String value = null;
	if (element == null) {
	    value = "";
	} else {
	    MNamespace elementNs = element.getNamespace();
	    //
	    //   if element is an AssociationEnd use
	    //      the namespace of containing association
	    //
	    if (element instanceof MAssociationEnd) {
		MAssociation assoc =
		    ((MAssociationEnd) element).getAssociation();
		if (assoc != null) {
		    elementNs = assoc.getNamespace();
		}
	    }
	    if (elementNs == namespace) {
		value = element.getName();
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

    protected String defaultAssocEndName(
					 MAssociationEnd assocEnd,
					 MNamespace namespace) {
	String name = null;
	MClassifier type = assocEnd.getType();
	if (type != null) {
	    name = formatElement(type, namespace);
	} else {
	    name = "unknown type";
	}
	MMultiplicity mult = assocEnd.getMultiplicity();
	if (mult != null) {
	    StringBuffer buf = new StringBuffer(name);
	    buf.append("[");
	    buf.append(Integer.toString(mult.getLower()));
	    buf.append("..");
	    int upper = mult.getUpper();
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

    protected String defaultAssocName(MAssociation assoc, MNamespace ns) {
	StringBuffer buf = new StringBuffer();
	Iterator iter = assoc.getConnections().iterator();
	for (int i = 0; iter.hasNext(); i++) {
	    if (i != 0) {
		buf.append("-");
	    }
	    buf.append(defaultAssocEndName((MAssociationEnd) iter.next(), ns));
	}
	return buf.toString();
    }

    protected String defaultGeneralizationName(
					       MGeneralization gen,
					       MNamespace ns) {
	MGeneralizableElement child = gen.getChild();
	MGeneralizableElement parent = gen.getParent();
	StringBuffer buf = new StringBuffer();
	buf.append(formatElement(child, ns));
	buf.append(" extends ");
	buf.append(formatElement(parent, ns));
	return buf.toString();
    }

    protected String defaultName(MModelElement element, MNamespace namespace) {
	String name = null;
	if (element instanceof MAssociationEnd) {
	    name = defaultAssocEndName((MAssociationEnd) element, namespace);
	} else {
	    if (element instanceof MAssociation) {
		name = defaultAssocName((MAssociation) element, namespace);
	    }
	    if (element instanceof MGeneralization) {
		name =
		    defaultGeneralizationName(
					      (MGeneralization) element,
					      namespace);
	    }
	}
	if (name == null)
	    name = "anon";
	return name;
    }

    protected String getPathSeparator() {
	return ".";
    }

    private void buildPath(
			   StringBuffer buffer,
			   MModelElement element,
			   String pathSep) {
	if (element != null) {
	    MNamespace parent = element.getNamespace();
	    if (parent != null && parent != element) {
		buildPath(buffer, parent, pathSep);
		buffer.append(pathSep);
	    }
	    String name = element.getName();
	    if (name == null || name.length() == 0) {
		name = defaultName(element, null);
	    }
	    buffer.append(name);
	}
    }

    protected String getElementSeparator() {
	return ", ";
    }

    protected String getEmptyCollection() {
	return "[empty]";
    }

    public String formatCollection(Iterator iter, MNamespace namespace) {
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
		if (obj instanceof MModelElement) {
		    buffer.append(
				  formatElement((MModelElement) obj,
						namespace));
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

    public MModel getProfileModel() {
	if (_defaultModel == null) {
	    _defaultModel = loadProfileModel();
	}
	return _defaultModel;
    }
	
    public static MModel loadProfileModel() {
	//
	//    get a file name for the default model
	//
	String defaultModelFileName =
	    System.getProperty("argo.defaultModel");
	//
	//   if the property was set
	//
	InputStream is = null;
	if (defaultModelFileName != null) {
	    //
	    //  try to find a file with that name
	    //
	    try {
		is = new FileInputStream(defaultModelFileName);
	    }
	    //
	    //   no file found, try looking in the resources
	    //
	    catch (FileNotFoundException ex) {
		is =
		    new Object().getClass().getResourceAsStream(defaultModelFileName);
		if (is == null) {
		    cat.error(
			      "Value of property argo.defaultModel ("
			      + defaultModelFileName
			      + ") did not correspond to an available file.\n");
		}
	    }
	}

	//
	//   either no name specified or file not found
	//        load the default
	if (is == null) {
	    defaultModelFileName = "/org/argouml/default.xmi";
	    is =
		new Object().getClass().getResourceAsStream(defaultModelFileName);
	    if (is == null) {
		try {
		    is =
			new FileInputStream(
					    defaultModelFileName.substring(1));
		} catch (FileNotFoundException ex) {
		    cat.error("Default model ("
			      + defaultModelFileName
			      + ") not found.\n", ex);
						
		}
	    }
	}
	if (is != null) {
	    try {
		XMIReader xmiReader = new XMIReader();
		//
		//   would really like to turn validation off to save
		//      a lot of scary messages
		MModel model = xmiReader.parseToModel(new InputSource(is));
		// 2002-07-18 Jaap Branderhorst changed the loading of
		// the projectfiles to solve hanging of argouml if a
		// project is corrupted. Issue 913 Created xmireader
		// with method getErrors to check if parsing went well
		if (xmiReader.getErrors()) {
		    throw new IOException(
					  "XMI file "
					  + defaultModelFileName
					  + " could not be parsed.");
		}
		return model;
	    } catch (Exception ex) {
		cat.error("Error reading " + defaultModelFileName + "\n", ex);
	    }
	}
	return null;
    }
		
}
