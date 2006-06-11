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

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.uml.notation.ComponentInstanceNotation;

/**
 * @author Michiel
 */
public class ComponentInstanceNotationUml extends ComponentInstanceNotation {

    /**
     * The constructor.
     *
     * @param componentInstance the UML componentInstance
     */
    public ComponentInstanceNotationUml(Object componentInstance) {
        super(componentInstance);
    }

    /**
     * Parse a line of the form: "name : base-component".
     *
     * @see org.argouml.notation.NotationProvider4#parse(java.lang.String)
     */
    public String parse(String text) {
        // strip any trailing semi-colons
        String s = text.trim();
        if (s.length() == 0) {
            return toString();
        }
        if (s.charAt(s.length() - 1) == ';') {
            s = s.substring(0, s.length() - 2);
        }

        String name = "";
        String bases = "";
        StringTokenizer tokenizer = null;

        if (s.indexOf(":", 0) > -1) {
            name = s.substring(0, s.indexOf(":")).trim();
            bases = s.substring(s.indexOf(":") + 1).trim();
        } else {
            name = s;
        }

        tokenizer = new StringTokenizer(bases, ",");

        Vector v = new Vector();
        Object ns = Model.getFacade().getNamespace(myComponentInstance);
        if (ns != null) {
            while (tokenizer.hasMoreElements()) {
                String newBase = tokenizer.nextToken();
                Object cls = Model.getFacade().lookupIn(ns, newBase.trim());
                if (cls != null) {
                    v.add(cls);
                }
            }
        }

        Model.getCommonBehaviorHelper().setClassifiers(myComponentInstance, v);
        Model.getCoreHelper().setName(myComponentInstance, name);

        return toString();
    }

    /**
     * @see org.argouml.notation.NotationProvider4#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-componentinstance";
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String nameStr = "";
        if (Model.getFacade().getName(myComponentInstance) != null) {
            nameStr = Model.getFacade().getName(myComponentInstance).trim();
        }

        // construct bases string (comma separated)
        String baseStr = "";
        Collection col = Model.getFacade().getClassifiers(myComponentInstance);
        if (col != null && col.size() > 0) {
            Iterator it = col.iterator();
            baseStr = Model.getFacade().getName(it.next());
            while (it.hasNext()) {
                baseStr += ", " + Model.getFacade().getName(it.next());
            }
        }
        if ((nameStr.length() == 0) && (baseStr.length() == 0)) {
            return "";
        }
        baseStr = baseStr.trim();
        if (baseStr.length() < 1) {
            return nameStr.trim();
        }
        return nameStr.trim() + " : " + baseStr;
    }

}
