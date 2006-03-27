// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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
import java.util.Vector;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.generator.ParserDisplay;
import org.argouml.uml.notation.ClassifierRoleNotation;

/**
 * The UML notation for a ClassifierRole. <p>
 * 
 * The following is supported: <p>
 * 
 * <pre>
 * baselist := [base] [, base]*
 * classifierRole := [name] [/ role] [: baselist]
 * </pre>
 *
 * The <code>role </code> and <code>baselist</code> can be given in
 * any order.<p>
 *
 * This syntax is compatible with the UML 1.3 and 1.4 specification.
 * 
 * @author Michiel
 */
public class ClassifierRoleNotationUml extends ClassifierRoleNotation {


    /**
     * The Constructor.
     * 
     * @param classifierRole the UML ClassifierRole
     */
    public ClassifierRoleNotationUml(Object classifierRole) {
        super(classifierRole);
    }

    /**
     * @see org.argouml.notation.NotationProvider4#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-classifierrole";
    }

    /**
     * @see org.argouml.notation.NotationProvider4#parse(java.lang.String)
     */
    public String parse(String text) {
        try {
            parseClassifierRole(myClassifierRole, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.classifierrole";
            Object[] args = {pe.getLocalizedMessage(),
                             new Integer(pe.getErrorOffset()), };
            ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
        }
        return toString();
    }
    
    private Object parseClassifierRole(Object classifierRole, String s)
    throws ParseException {
        
        ParserDisplay.SINGLETON.parseClassifierRole(classifierRole, s);
        
        return classifierRole;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String nameString = Model.getFacade().getName(myClassifierRole);
        if (nameString == null) nameString = "";
        nameString = nameString.trim();
        String baseString = "";

        // Loop through all base classes, building a comma separated list

        Collection c = Model.getFacade().getBases(myClassifierRole);
        if (c != null && c.size() > 0) {
            Vector bases = new Vector(c);
            baseString += Model.getFacade().getName(bases.elementAt(0));

            for (int i = 1; i < bases.size(); i++) {
                baseString +=
                    ", " + Model.getFacade().getName(bases.elementAt(i));
            }
        }
        baseString = baseString.trim();

        // Build the final string
        if (nameString.length() != 0) nameString = "/" + nameString;
        if (baseString.length() != 0) baseString = ":" + baseString;
        return nameString + baseString;
    }

}
