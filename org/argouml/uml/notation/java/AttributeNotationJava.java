// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.notation.java;

import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.notation.AttributeNotation;

/**
 * @author michiel
 */
public class AttributeNotationJava extends AttributeNotation {

    /**
     * The constructor.
     *
     * @param attribute the attribute that is represented
     */
    public AttributeNotationJava(Object attribute) {
        super(attribute);
    }

    /**
     * @see org.argouml.notation.NotationProvider4#getParsingHelp()
     */
    public String getParsingHelp() {
//        return "parsing.java.help.attribute";
        return "Parsing in Java not yet supported";
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
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(80);
        sb.append(NotationUtilityJava.generateVisibility(myAttribute));
        sb.append(NotationUtilityJava.generateScope(myAttribute));
        sb.append(NotationUtilityJava.generateChangeability(myAttribute));
        Object type = Model.getFacade().getType(myAttribute);
        Object multi = Model.getFacade().getMultiplicity(myAttribute);
        // handle multiplicity here since we need the type
        // actually the API of generator is buggy since to generate
        // multiplicity correctly we need the attribute too
        if (type != null && multi != null) {
            if (Model.getFacade().getUpper(multi) == 1) {
                sb.append(NotationUtilityJava.generateClassifierRef(type))
                    .append(' ');
            } else if (Model.getFacade().isADataType(type)) {
                sb.append(NotationUtilityJava.generateClassifierRef(type))
                    .append("[] ");
            } else {
                sb.append("java.util.Vector ");
            }
        }

        sb.append(Model.getFacade().getName(myAttribute));
        Object init = Model.getFacade().getInitialValue(myAttribute);
        if (init != null) {
            String initStr = 
                NotationUtilityJava.generateExpression(init).trim();
            if (initStr.length() > 0) {
                sb.append(" = ").append(initStr);
            }
        }

        return sb.toString();
    }

}
