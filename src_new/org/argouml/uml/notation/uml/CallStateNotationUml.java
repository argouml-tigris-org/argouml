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
import java.util.Iterator;
import java.util.StringTokenizer;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.notation.CallStateNotation;


/**
 * The UML notation for a CallState.
 *
 * @author mvw@tigris.org
 */
public class CallStateNotationUml extends CallStateNotation {

    /**
     * The constructor.
     *
     * @param callState the UML CallState
     */
    public CallStateNotationUml(Object callState) {
        super(callState);
    }

    /**
     * @see org.argouml.notation.NotationProvider4#parse(java.lang.String)
     */
    public String parse(String text) {
        try {
            parseCallState(myCallState, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.callstate";
            Object[] args = {pe.getLocalizedMessage(),
                             new Integer(pe.getErrorOffset()), };
            ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
        }
        return toString();
    }

    private Object parseCallState(Object callState, String s1)
        throws ParseException {

        String s = s1.trim();

        int a = s.indexOf("(");
        int b = s.indexOf(")");
        if (((a < 0) && (b >= 0)) || ((b < 0) && (a >= 0)) || (b < a)) {
            throw new ParseException("No matching brackets [] found.", 0);
        }

        String newClassName = null;
        String newOperationName = null;
        StringTokenizer tokenizer = new StringTokenizer(s, "(");
        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken().trim();
            if (nextToken.endsWith(")")) {
                newClassName = nextToken.substring(0, nextToken.length() - 1);
            } else {
                newOperationName = nextToken.trim();
            }
        }

        String oldOperationName = null;
        String oldClassName = null;
        Object entry = Model.getFacade().getEntry(myCallState);
        Object operation = null;
        Object clazz = null;
        if (Model.getFacade().isACallAction(entry)) {
            operation = Model.getFacade().getOperation(entry);
            if (Model.getFacade().isAOperation(operation)) {
                oldOperationName = Model.getFacade().getName(operation);
                clazz = Model.getFacade().getOwner(operation);
                oldClassName = Model.getFacade().getName(clazz);
            }
        }

        if (oldClassName != null && oldClassName.equals(newClassName)) {
            if (!(oldOperationName != null
                    && oldOperationName.equals(newOperationName))) {
                // Same class, other operation
                Collection c = Model.getFacade().getOperations(clazz);
                Iterator i = c.iterator();
                while (i.hasNext()) {
                    Object op = i.next();
                    if (newOperationName.equals(
                            Model.getFacade().getName(op))) {
                        Model.getCommonBehaviorHelper().setOperation(entry, op);
                        break;
                    }
                }
            }
            // else nothing was changed.
        }
        // TODO: else Other class

        return callState;
    }

    /**
     * @see org.argouml.notation.NotationProvider4#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-callstate";
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String ret = "";
        Object action = Model.getFacade().getEntry(myCallState);
        if (Model.getFacade().isACallAction(action)) {
            Object operation = Model.getFacade().getOperation(action);
            if (operation != null) {
                Object n = Model.getFacade().getName(operation);
                if (n != null) {
                    ret = (String) n;
                }
                n =
                    Model.getFacade().getName(
                        Model.getFacade().getOwner(operation));
                if (n != null && n.equals("")) {
                    ret += "\n(" + (String) n + ")";
                }
            }
        }

        if (ret == null) {
            return "";
        }
        return ret;
    }

}
