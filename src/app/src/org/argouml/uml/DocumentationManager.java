// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.Iterator;

import org.argouml.application.api.Argo;
import org.argouml.model.Model;
import org.argouml.util.MyTokenizer;

/**
 * This class handles the Documentation of ModelElements.
 * Documentation is represented internally by the tagged value "documentation",
 * but it has its own tab-panel to ease user handling.
 *
 */
public class DocumentationManager {

    /**
     * The system's native line-ends, for when things are written to file.
     */
    private static final String LINE_SEPARATOR =
	System.getProperty("line.separator");

    /**
     * This function returns the documentation in C-style comment format.
     *
     * @param o the ModelElement
     * @param indent the current indentation for new lines
     * @return the documentation, as a String
     */
    public static String getDocs(Object o, String indent) {
        return getDocs(o, indent, "/** ", " *  ", " */");
    }

    /**
     * @param o the ModelElement
     * @param indent the current indentation for new lines
     * @param header is the first line
     * @param prefix is inserted at every line before the doc
     * @param footer is the closing line
     * @return the string that represents the documentation
     *         for the given ModelElement
     */
    public static String getDocs(Object o, String indent, String header,
				 String prefix, String footer) {
        String sResult = defaultFor(o, indent);

        if (Model.getFacade().isAModelElement(o)) {
            Iterator iter = Model.getFacade().getTaggedValues(o);
            if (iter != null) {
                while (iter.hasNext()) {
                    Object tv = iter.next();
                    String tag = Model.getFacade().getTagOfTag(tv);
                    if (Argo.DOCUMENTATION_TAG.equals(tag) 
                            || Argo.DOCUMENTATION_TAG_ALT.equals(tag)) {
                        sResult = Model.getFacade().getValueOfTag(tv);
                        // give priority to standard documentation tag
                        if (Argo.DOCUMENTATION_TAG.equals(tag)) {
                            break;
                        }
                    }
                }
            }
        }

        if (sResult == null)
            return "(No comment)";

	StringBuffer result = new StringBuffer();
	if (header != null) {
	    result.append(header).append(LINE_SEPARATOR);
	}

	if (indent != null) {
	    if (prefix != null) {
		prefix = indent + prefix;
	    }

	    if (footer != null) {
		footer = indent + footer;
	    }
	}

	appendComment(result, prefix, sResult, 0);

	if (footer != null) {
	    result.append(footer);
	}

        return result.toString();
    }

    /**
     * @param o the ModelElement. If it is not a ModelElement,
     *          then you'll get a IllegalArgumentException
     * @param s the string representing the documentation
     */
    public static void setDocs(Object o, String s) {
        Object taggedValue =
                Model.getFacade().getTaggedValue(o, Argo.DOCUMENTATION_TAG);
        if (taggedValue == null) {
            taggedValue =
                    Model.getExtensionMechanismsFactory().buildTaggedValue(
                            Argo.DOCUMENTATION_TAG, s);
            Model.getExtensionMechanismsHelper().addTaggedValue(o, taggedValue);
        } else {
            Model.getExtensionMechanismsHelper().setValueOfTag(taggedValue, s);
        }
    }

    /**
     * Determine whether documentation is associated with the given
     * element or not.
     *
     * Added 2001-10-05 STEFFEN ZSCHALER for use by
     * org.argouml.language.java.generator.CodeGenerator
     *
     * @param o The given element.
     * @return true if the given element has docs.
     */
    public static boolean hasDocs(Object o) {
        if (Model.getFacade().isAModelElement(o)) {
            Iterator i = Model.getFacade().getTaggedValues(o);

            if (i != null) {
                while (i.hasNext()) {
                    Object tv = i.next();
                    String tag = Model.getFacade().getTagOfTag(tv);
                    String value = Model.getFacade().getValueOfTag(tv);
                    if ((Argo.DOCUMENTATION_TAG.equals(tag) 
                            || Argo.DOCUMENTATION_TAG_ALT.equals(tag))
                        && value != null && value.trim().length() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Generate default documentation.
     *
     * @param o the ModelElement
     * @param indent the current indentation string for new lines
     * @return the default documentation
     */
    public static String defaultFor(Object o, String indent) {
	if (Model.getFacade().isAClass(o)) {
            // TODO: Needs localization
	    return " A class that represents ...\n\n"
		+ indent + " @see OtherClasses\n"
		+ indent + " @author your_name_here";
	}
	if (Model.getFacade().isAAttribute(o)) {

	    return " An attribute that represents ...";
	}

	if (Model.getFacade().isAOperation(o)) {
	    return " An operation that does...\n\n"
		+ indent + " @param firstParam a description of this parameter";
	}
	if (Model.getFacade().isAInterface(o)) {
	    return " An interface defining operations expected of ...\n\n"
		+ indent + " @see OtherClasses\n"
		+ indent + " @author your_name_here";
	}
	if (Model.getFacade().isAModelElement(o)) {
	    return "\n";
	}

	return null;
    }


    ////////////////////////////////////////////////////////////////
    // comments

    /**
     * Get the comments (the notes in a diagram) for a modelelement.<p>
     *
     * This returns a c-style comments.
     *
     * @param o The modelelement.
     * @return a String.
     */
    public static String getComments(Object o) {
        return getComments(o, "/*", " * ", " */");
    }

    /**
     * Get the comments (the notes in a diagram) for a modelelement.
     *
     * @return a string with the comments.
     * @param o The given modelelement.
     * @param header is the comment header.
     * @param prefix is the comment prefix (on every line).
     * @param footer is the comment footer.
     */
    public static String getComments(Object o,
				     String header, String prefix,
				     String footer) {
	StringBuffer result = new StringBuffer();
	if (header != null) {
	    result.append(header).append(LINE_SEPARATOR);
	}

	if (Model.getFacade().isAUMLElement(o)) {
	    Collection comments = Model.getFacade().getComments(o);
	    if (!comments.isEmpty()) {
		int nlcount = 2;
		for (Iterator iter = comments.iterator(); iter.hasNext();) {
		    Object c = iter.next();
		    String s = Model.getFacade().getName(c);
		    nlcount = appendComment(result,
					    prefix,
					    s,
					    nlcount > 1 ? 0 : 1);
		}
	    } else {
		return "";
	    }
	} else {
	    return "";
	}

	if (footer != null) {
	    result.append(footer).append(LINE_SEPARATOR);
	}

	return result.toString();
    }

    /**
     * Append a string to sb which is chopped into lines and each line
     * prefixed with prefix.
     *
     * @param sb the StringBuffer to append to.
     * @param prefix the prefix to each line.
     * @param comment the text to reformat.
     * @param nlprefix the number of empty lines to prefix the comment with.
     * @return the number of pending empty lines.
     */
    private static int appendComment(StringBuffer sb, String prefix,
				      String comment, int nlprefix) {
	int nlcount = 0;

	for (; nlprefix > 0; nlprefix--) {
	    if (prefix != null)
		sb.append(prefix);
	    sb.append(LINE_SEPARATOR);
	    nlcount++;
	}

	if (comment == null) {
	    return nlcount;
	}

	MyTokenizer tokens = new MyTokenizer(comment,
					     "",
					     MyTokenizer.LINE_SEPARATOR);

	while (tokens.hasMoreTokens()) {
	    String s = tokens.nextToken();
	    if (!s.startsWith("\r") && !s.startsWith("\n")) {
		if (prefix != null)
		    sb.append(prefix);
		sb.append(s);
		sb.append(LINE_SEPARATOR);
		nlcount = 0;
	    } else if (nlcount > 0) {
		if (prefix != null)
		    sb.append(prefix);
		sb.append(LINE_SEPARATOR);
		nlcount++;
	    } else {
		nlcount++;
	    }
	}

	return nlcount;
    }

} /* end class DocumentationManager */




