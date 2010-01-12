/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2009 The Regents of the University of California. All
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

package org.argouml.notation.providers.uml;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.CallStateNotation;



/**
 * The UML notation for a CallState. <p>
 * 
 * A call state is shown with the name of the operation being called 
 * in the symbol, along with the name of the classifier 
 * that hosts the operation in parentheses under it. <p>
 * 
 * Despite being shown on 2 lines, this is considered 1 text. 
 * The user may enter the text in 1 or 2 lines, but ArgoUML 
 * shows it as 2 lines.  
 *
 * @author Michiel
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

    /*
     * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            parseCallState(modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.callstate";
            Object[] args = {pe.getLocalizedMessage(),
                             Integer.valueOf(pe.getErrorOffset()), };
            ArgoEventPump.fireEvent(new ArgoHelpEvent(
                    ArgoEventTypes.HELP_CHANGED, this,
                    Translator.messageFormat(msg, args)));
        }
    }

    protected Object parseCallState(Object callState, String s1)
        throws ParseException {

        String s = s1.trim();

        int a = s.indexOf("(");
        int b = s.indexOf(")");
        if (((a < 0) && (b >= 0)) || ((b < 0) && (a >= 0)) || (b < a)) {
            throw new ParseException("No matching brackets () found.", 0);
        }

        /* First we decode the string: */
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

        /* Secondly we check the previous model structure: */
        String oldOperationName = null;
        String oldClassName = null;
        Object entry = Model.getFacade().getEntry(callState);
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
        
        /* And 3rd, we adapt the model: */
        boolean found = false;
        if ((newClassName != null) 
                && newClassName.equals(oldClassName)
                && (newOperationName != null) 
                && !newOperationName.equals(oldOperationName)) {
            // Same class, other operation
            for ( Object op : Model.getFacade().getOperations(clazz)) {
                if (newOperationName.equals(
                        Model.getFacade().getName(op))) {
                    Model.getCommonBehaviorHelper().setOperation(entry, op);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new ParseException(
                        "Operation " + newOperationName 
                        + " not found in " + newClassName + ".", 0);
            }

        } else if ((newClassName != null) 
                && !newClassName.equals(oldClassName)
                && (newOperationName != null)) {
            // Other class
            Object model = 
                ProjectManager.getManager().getCurrentProject().getRoot();
            Collection c =
                Model.getModelManagementHelper().
                    getAllModelElementsOfKind(model,
                                Model.getMetaTypes().getClassifier());
            Iterator i = c.iterator();
            Object classifier = null;
            while (i.hasNext()) {
                Object cl = i.next();
                String cn = Model.getFacade().getName(cl);
                if (cn.equals(newClassName)) {
                    classifier = cl;
                    break;
                }
            }
            if (classifier == null) {
                throw new ParseException(
                        "Classifier " + newClassName + " not found.", 0);
            }
            // We found the classifier, now go find the operation:
            if (classifier != null) {
                Collection ops = Model.getFacade().getOperations(classifier);
                Iterator io = ops.iterator();
                while (io.hasNext()) {
                    Object op = io.next();
                    if (newOperationName.equals(
                            Model.getFacade().getName(op))) {
                        /* Here we located the new classifier 
                         * with its operation. */
                        found = true;
                        if (!Model.getFacade().isACallAction(entry)) {
                            entry = Model.getCommonBehaviorFactory()
                                .buildCallAction(op, "ca");
                            Model.getStateMachinesHelper().setEntry(
                                    callState, entry);
                        } else {
                            Model.getCommonBehaviorHelper().setOperation(
                                    entry, op);
                        }
                        break;
                    }
                }
                if (!found) {
                    throw new ParseException(
                            "Operation " + newOperationName 
                            + " not found in " + newClassName + ".", 0);
                }
            }
        }
        if (!found) {
            throw new ParseException(
                    "Incompatible input found.", 0);
        }

        return callState;
    }

    /*
     * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-callstate";
    }

    private String toString(Object modelElement) {
        String ret = "";
        Object action = Model.getFacade().getEntry(modelElement);
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
                if (n != null && !n.equals("")) {
                    ret += "\n(" + (String) n + ")";
                }
            }
        }

        if (ret == null) {
            return "";
        }
        return ret;
    }

    @Override
    public String toString(Object modelElement, NotationSettings settings) {
        return toString(modelElement);
    }

}
