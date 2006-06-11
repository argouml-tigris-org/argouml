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

import java.util.StringTokenizer;
import java.util.Vector;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.notation.ObjectNotation;

/**
 * @author mvw@tigris.org
 *
 */
public class ObjectNotationUml extends ObjectNotation {

    /**
     * The constructor.
     *
     * @param theObject the UML Object
     */
    public ObjectNotationUml(Object theObject) {
        super(theObject);
    }

    /**
     * @see org.argouml.notation.NotationProvider4#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-object";
    }

    /**
     * @see org.argouml.notation.NotationProvider4#parse(java.lang.String)
     */
    public String parse(String text) {
        String s = text.trim();
        if (s.length() == 0) {
            return toString();
        }
        // strip any trailing semi-colons
        if (s.charAt(s.length() - 1) == ';') {
            s = s.substring(0, s.length() - 2);
        }

        String name = "";
        String bases = "";
        StringTokenizer baseTokens = null;

        if (s.indexOf(":", 0) > -1) {
            name = s.substring(0, s.indexOf(":", 0)).trim();
            bases = s.substring(s.indexOf(":", 0) + 1).trim();
            baseTokens = new StringTokenizer(bases, ",");
        } else {
            name = s;
        }

        Model.getCommonBehaviorHelper().setClassifiers(myObject, new Vector());
        if (baseTokens != null) {
            while (baseTokens.hasMoreElements()) {
                String typeString = baseTokens.nextToken();
                Object type =
                /* (MClassifier) */ProjectManager.getManager()
                        .getCurrentProject().findType(typeString);
                Model.getCommonBehaviorHelper().addClassifier(myObject, type);
            }
        }
        /* This updates the diagram - hence as last statement: */
        Model.getCoreHelper().setName(myObject, name);
        return toString();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String nameStr = "";
        if (Model.getFacade().getName(myObject) != null) {
            nameStr = Model.getFacade().getName(myObject).trim();
        }

        Vector bases = new Vector(Model.getFacade().getClassifiers(myObject));

        String baseString = "";

        if (Model.getFacade().getClassifiers(myObject) != null
                && Model.getFacade().getClassifiers(myObject).size() > 0) {

            baseString += Model.getFacade().getName(bases.elementAt(0));
            for (int i = 1; i < bases.size(); i++) {
                baseString +=
                        ", "  + Model.getFacade().getName(bases.elementAt(i));
            }
        }

        if ((nameStr.length() == 0) && (baseString.length() == 0)) {
            return "";
        }
        baseString = baseString.trim();
        if (baseString.length() < 1) {
            return nameStr.trim();
        }
        return nameStr.trim() + " : " + baseString;
    }

}
