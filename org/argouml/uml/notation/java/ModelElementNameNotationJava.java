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

package org.argouml.uml.notation.java;

import java.util.Stack;

import org.argouml.language.java.generator.GeneratorJava;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.notation.ModelElementNameNotation;

/**
 * @author mvw@tigris.org
 */
public class ModelElementNameNotationJava extends ModelElementNameNotation {

    /**
     * The constructor.
     */
    public ModelElementNameNotationJava(Object name) {
        super(name);
    }

    /**
     * @see org.argouml.notation.NotationProvider4#parse(java.lang.String)
     */
    public String parse(String text) {
        ProjectBrowser.getInstance().getStatusBar().showStatus(
            "Parsing in Java not yet supported");
        return toString();
    }

    /**
     * @see org.argouml.notation.NotationProvider4#getParsingHelp()
     */
    public String getParsingHelp() {
        if (Model.getFacade().isAStateVertex(myModelElement)) {
            return "parsing.help.fig-statename";
        }
        return "parsing.help.fig-nodemodelelement";
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String name;
        name = Model.getFacade().getName(myModelElement);
        if (name == null) name = "";
        return generateVisibility() + generatePath() + name;
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
                s += (String) stack.pop() + ".";
            }

            if (s.length() > 0 && !s.endsWith(".")) {
                s += ".";
            }
        }
        return s;
    }

    /**
     * @return a string which represents the visibility
     */
    protected String generateVisibility() {
        String s = "";
        if (isValue("visibilityVisible")) {
            s = NotationUtilityJava.generateVisibility(myModelElement);
        }
        return s;
    }
}
