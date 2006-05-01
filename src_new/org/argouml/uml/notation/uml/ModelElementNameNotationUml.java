// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.uml.notation.uml;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Vector;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.NotationHelper;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.notation.ModelElementNameNotation;
import org.argouml.util.MyTokenizer;

/**
 * Handles the notation of the name of a modelelement in UML,
 * ie a string on the format:<pre>
 *     [ &lt;&lt; stereotype &gt;&gt;] [+|-|#] [name]
 * </pre>
 *
 * @author mvw@tigris.org
 */
public class ModelElementNameNotationUml extends ModelElementNameNotation {

    /**
     * The constructor.
     */
    public ModelElementNameNotationUml(Object name) {
        super(name);
    }

    /**
     * Parses a model element, ie reads a string that contains
     * the stereotype, visibility and name,
     * and assigns the properties to the passed MModelElement.
     *
     * @see org.argouml.notation.NotationProvider4#parse(java.lang.String)
     */
    public String parse(String text) {
        try {
            parseModelElement(myModelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.node-modelelement";
            Object[] args = {
                pe.getLocalizedMessage(),
                new Integer(pe.getErrorOffset()),
            };
            ProjectBrowser.getInstance().getStatusBar().showStatus(
                Translator.messageFormat(msg, args));
        }
        return toString();
    }

    /**
     * @see org.argouml.notation.NotationProvider4#getParsingHelp()
     */
    public String getParsingHelp() {
        if (Model.getFacade().isAStateVertex(myModelElement)) {
            return "parsing.help.fig-statename";
        }
        if (Model.getFacade().isAUseCase(myModelElement)) {
            return "parsing.help.fig-usecase";
        }
        if (((Boolean) getValue("edge")).booleanValue()) {
            return "parsing.help.fig-edgemodelelement";
        }
        return "parsing.help.fig-nodemodelelement";
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String name = Model.getFacade().getName(myModelElement);
        StringBuffer sb = new StringBuffer("");
        if (isValue("fullyHandleStereotypes")) {
            sb.append(generateStereotypes());
        }
        sb.append(generateVisibility());
        sb.append(generatePath());
        if (name != null) {
            sb.append(name);
        }
        return sb.toString();
    }

    protected String generateStereotypes() {
        Collection c = Model.getFacade().getStereotypes(myModelElement);
        StringBuffer sb = new StringBuffer(50);
        Iterator i = c.iterator();
        boolean first = true;
        while (i.hasNext()) {
            Object o = i.next();
            if (!first) {
                sb.append(',');
            }
            if (o != null) {
                sb.append(Model.getFacade().getName(o));
                first = false;
            }
        }
        return first ? "" : NotationHelper.getLeftGuillemot()
            + sb.toString()
            + NotationHelper.getRightGuillemot();
    }

    /**
     *
     * @return a string which represents the path
     */
    protected String generatePath() {
        String s = "";
        if (isValue("pathVisible")) {
            Object p = myModelElement;
            Stack stack = new Stack();
            Object ns = Model.getFacade().getNamespace(p);
            while (ns != null && !Model.getFacade().isAModel(ns)) {
                stack.push(Model.getFacade().getName(ns));
                ns = Model.getFacade().getNamespace(ns);
            }
            while (!stack.isEmpty()) {
                s += (String) stack.pop() + "::";
            }

            if (s.length() > 0 && !s.endsWith(":")) {
                s += "::";
            }
        }
        return s;
    }

    /**
     * @return a string representing the visibility
     */
    protected String generateVisibility() {
        String s = "";
        if (isValue("visibilityVisible")) {
            Object v = Model.getFacade().getVisibility(myModelElement);
            if (v == null) {
                /* Initially, the visibility is not set in the model.
                 * Still, we want to show the default, i.e. public.
                 */
                v = Model.getVisibilityKind().getPublic();
            }
            s = NotationUtilityUml.generateVisibility(v);
            if (s.length() > 0) {
                s = s + " ";
            }
            /* This for when nothing is generated: omit the space. */
        }
        return s;
    }


    /**
     * @param me   The ModelElement <em>text</em> describes.
     * @param text A String on the above format.
     * @throws ParseException
     *             when it detects an error in the attribute string. See also
     *             ParseError.getErrorOffset().
     */
    protected void parseModelElement(Object me, String text)
        throws ParseException {
        MyTokenizer st;

        Vector path = null;
        String name = null;
        String stereotype = null;
        String token;

        try {
            st = new MyTokenizer(text, "<<,\u00AB,\u00BB,>>,::");
            while (st.hasMoreTokens()) {
                token = st.nextToken();

                if ("<<".equals(token) || "\u00AB".equals(token)) {
                    if (stereotype != null) {
                        throw new ParseException("Element cannot have "
                                + "two groups of stereotypes",
                                st.getTokenIndex());
                    }

                    stereotype = "";
                    while (true) {
                        token = st.nextToken();
                        if (">>".equals(token) || "\u00BB".equals(token)) {
                            break;
                        }
                        stereotype += token;
                    }
                } else if ("::".equals(token)) {
                    if (name != null) {
                        name = name.trim();
                    }

                    if (path != null && (name == null || "".equals(name))) {
                        throw new ParseException("Element cannot have "
                                + "anonymous qualifiers", st.getTokenIndex());
                    }

                    if (path == null) {
                        path = new Vector();
                    }
                    if (name != null) {
                        path.add(name);
                    }
                    name = null;
                } else {
                    if (name != null) {
                        throw new ParseException("Element cannot have "
                                + "two word names or qualifiers", st
                                .getTokenIndex());
                    }

                    name = token;
                }
            }
        } catch (NoSuchElementException nsee) {
            throw new ParseException("Unexpected end of element",
                    text.length());
        } catch (ParseException pre) {
            throw pre;
        }

        if (name != null) {
            name = name.trim();
        }

        if (path != null && (name == null || "".equals(name))) {
            throw new ParseException("Qualified names must end with a name", 0);
        }

        if (name != null && name.startsWith("+")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(me,
                            Model.getVisibilityKind().getPublic());
        }
        if (name != null && name.startsWith("-")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(me,
                            Model.getVisibilityKind().getPrivate());
        }
        if (name != null && name.startsWith("#")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(me,
                            Model.getVisibilityKind().getProtected());
        }
        if (name != null && name.startsWith("~")) {
            name = name.substring(1).trim();
            Model.getCoreHelper().setVisibility(me,
                            Model.getVisibilityKind().getPackage());
        }
        if (name != null) {
            Model.getCoreHelper().setName(me, name);
        }

        NotationUtilityUml.dealWithStereotypes(me, stereotype,
                isValue("fullyHandleStereotypes"));

        if (path != null) {
            Object nspe =
                Model.getModelManagementHelper().getElement(
                        path,
                        Model.getFacade().getModel(me));

            if (nspe == null || !(Model.getFacade().isANamespace(nspe))) {
                throw new ParseException("Unable to resolve namespace", 0);
            }
            Object model =
                ProjectManager.getManager().getCurrentProject().getRoot();
            if (!Model.getCoreHelper().getAllPossibleNamespaces(me, model)
                        .contains(nspe)) {
                throw new ParseException("Invalid namespace for element", 0);
            }

            Model.getCoreHelper().addOwnedElement(nspe, me);
        }
    }

}
