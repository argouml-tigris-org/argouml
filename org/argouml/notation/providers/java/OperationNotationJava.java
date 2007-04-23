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

package org.argouml.notation.providers.java;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.model.Model;
import org.argouml.notation.providers.OperationNotation;

/**
 * The notation for an operation.
 * 
 * @author michiel
 */
public class OperationNotationJava extends OperationNotation {

    /**
     * Logger.
     */
    private static final Logger LOG = 
        Logger.getLogger(OperationNotationJava.class);

    /**
     * The constructor.
     * 
     * @param operation the operation we represent
     */
    public OperationNotationJava(Object operation) {
        super(operation);
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        ArgoEventPump.fireEvent(new ArgoHelpEvent(
                ArgoEventTypes.HELP_CHANGED, this,
            "Parsing in Java not yet supported"));
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "Parsing in Java not yet supported";
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#toString(java.lang.Object, java.util.HashMap)
     */
    public String toString(Object modelElement, HashMap args) {
        StringBuffer sb = new StringBuffer(80);
        String nameStr = null;
        boolean constructor = false;

        Iterator its = 
            Model.getFacade().getStereotypes(modelElement).iterator();
        String name = "";
        while (its.hasNext()) {
            Object o = its.next();
            name = Model.getFacade().getName(o);
            if ("create".equals(name)) {
                break;
            }
        }
        if ("create".equals(name)) {
            // constructor
            nameStr = Model.getFacade().getName(
                    Model.getFacade().getOwner(modelElement));
            constructor = true;
        } else {
            nameStr = Model.getFacade().getName(modelElement);
        }

        sb.append(generateConcurrency(modelElement));
        sb.append(generateAbstractness(modelElement));
        sb.append(NotationUtilityJava.generateChangeability(modelElement));
        sb.append(NotationUtilityJava.generateScope(modelElement));
        sb.append(NotationUtilityJava.generateVisibility(modelElement));

        // pick out return type
        Collection returnParams = 
            Model.getCoreHelper().getReturnParameters(modelElement);
        Object rp;
        if (returnParams.size() == 0) {
            rp = null;
        } else {
            rp = returnParams.iterator().next();
        }
        if (returnParams.size() > 1)  {
            LOG.warn("Java generator only handles one return parameter"
                    + " - Found " + returnParams.size()
                    + " for " + Model.getFacade().getName(modelElement));
        }
        if (rp != null && !constructor) {
            Object returnType = Model.getFacade().getType(rp);
            if (returnType == null) {
                sb.append("void ");
            } else {
                sb.append(NotationUtilityJava.generateClassifierRef(returnType))
                    .append(' ');
            }
        }

        // name and params
        Vector params = new Vector(
                Model.getFacade().getParameters(modelElement));
        params.remove(rp);

        sb.append(nameStr).append('(');

        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(NotationUtilityJava.generateParameter(
                        params.elementAt(i)));
            }
        }

        sb.append(')');

        Collection c = Model.getFacade().getRaisedSignals(modelElement);
        if (!c.isEmpty()) {
            Iterator it = c.iterator();
            boolean first = true;
            while (it.hasNext()) {
                Object signal = it.next();

                if (!Model.getFacade().isAException(signal)) {
                    continue;
                }

                if (first) {
                    sb.append(" throws ");
                } else {
                    sb.append(", ");
                }

                sb.append(Model.getFacade().getName(it.next()));
                first = false;
            }
        }

        return sb.toString();
    }

    /**
     * Generates "synchronized" keyword for guarded operations.
     * @param op The operation
     * @return String The synchronized keyword if the operation is guarded,
     *                else "".
     */
    private static String generateConcurrency(Object op) {
        if (Model.getFacade().getConcurrency(op) != null
            && Model.getConcurrencyKind().getGuarded().equals(
                    Model.getFacade().getConcurrency(op))) {
            return "synchronized ";
        }
        return "";
    }

    /**
     * Generate "abstract" keyword for an abstract operation.
     *
     * @param op the operation
     * @return the generated string
     */
    private static String generateAbstractness(Object op) {
        if (Model.getFacade().isAbstract(op)) {
            return "abstract ";
        }
        return "";
    }
}
