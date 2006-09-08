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

import java.util.HashMap;
import java.util.Stack;

import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.notation.ModelElementNameNotation;

/**
 * Java notation for the name of a modelelement.
 * 
 * @author mvw@tigris.org
 */
public class ModelElementNameNotationJava extends ModelElementNameNotation {

    /**
     * The constructor.
     *
     * @param name the modelelement
     */
    public ModelElementNameNotationJava(Object name) {
        super(name);
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        ProjectBrowser.getInstance().getStatusBar().showStatus(
            "Parsing in Java not yet supported");
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-nodemodelelement";
    }

    /**
     * @see org.argouml.uml.notation.NotationProvider#toString(java.lang.Object, java.util.HashMap)
     */
    public String toString(Object modelElement, HashMap args) {
        String name;
        name = Model.getFacade().getName(modelElement);
        if (name == null) name = "";
        return generateVisibility(modelElement, args) 
            + generatePath(modelElement, args) + name;
    }

    /**
     * @param modelElement the UML element
     * @param args a set of arguments that may influence 
     * the generated notation
     * @return a string which represents the path
     */
    protected String generatePath(Object modelElement, 
            HashMap args) {
        String s = "";
        if (isValue("pathVisible", args)) {
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
    protected String generateVisibility(Object modelElement, 
            HashMap args) {
        String s = "";
        if (isValue("visibilityVisible", args)) {
            s = NotationUtilityJava.generateVisibility(modelElement);
        }
        return s;
    }
}
