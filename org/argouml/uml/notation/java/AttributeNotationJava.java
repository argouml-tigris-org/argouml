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

import java.util.HashMap;

import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.notation.AttributeNotation;

/**
 * @author michiel
 */
public class AttributeNotationJava extends AttributeNotation {

    /**
     * The constructor.
     */
    protected AttributeNotationJava() {
        super();
    }

    private static final AttributeNotationJava INSTANCE =
        new AttributeNotationJava();

    public static final AttributeNotationJava getInstance() {
        return INSTANCE;
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
//        return "parsing.java.help.attribute";
        return "Parsing in Java not yet supported";
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        ProjectBrowser.getInstance().getStatusBar().showStatus(
            "Parsing in Java not yet supported");
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#toString(java.lang.Object, java.util.HashMap)
     */
    public String toString(Object modelElement, HashMap args) {
        StringBuffer sb = new StringBuffer(80);
        sb.append(NotationUtilityJava.generateVisibility(modelElement));
        sb.append(NotationUtilityJava.generateScope(modelElement));
        sb.append(NotationUtilityJava.generateChangeability(modelElement));
        Object type = Model.getFacade().getType(modelElement);
        Object multi = Model.getFacade().getMultiplicity(modelElement);
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

        sb.append(Model.getFacade().getName(modelElement));
        Object init = Model.getFacade().getInitialValue(modelElement);
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
