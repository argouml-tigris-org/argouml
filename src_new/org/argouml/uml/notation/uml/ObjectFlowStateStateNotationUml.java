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

package org.argouml.uml.notation.uml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.notation.ObjectFlowStateStateNotation;

/**
 * @author Michiel
 *
 */
public class ObjectFlowStateStateNotationUml extends
        ObjectFlowStateStateNotation {

    /**
     * The constructor.
     *
     * @param objectflowstate
     *            the ObjectFlowState represented by this notation
     */
    public ObjectFlowStateStateNotationUml(Object objectflowstate) {
        super(objectflowstate);
    }

    /**
     * @see org.argouml.notation.NotationProvider4#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-objectflowstate2";
    }

    /**
     * @see org.argouml.notation.NotationProvider4#parse(java.lang.String)
     */
    public String parse(String text) {
        try {
            parseObjectFlowState2(myObjectFlowState, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.objectflowstate";
            Object[] args = {pe.getLocalizedMessage(),
                             new Integer(pe.getErrorOffset()), };
            ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
        }
        return toString();
    }

    /**
     * Do the actual parsing.
     *
     * @param objectFlowState
     *            the given element to be altered
     * @param s
     *            the new string
     * @return the altered ObjectFlowState
     * @throws ParseException
     *             when the given text was rejected
     */
    protected Object parseObjectFlowState2(Object objectFlowState, String s)
        throws ParseException {
        s = s.trim();
        /* Let's not be picky about the brackets - just remove them: */
        if (s.startsWith("[")) {
            s = s.substring(1);
        }
        if (s.endsWith("]")) {
            s = s.substring(0, s.length() - 1);
        }
        s = s.trim();
        Object c = Model.getFacade().getType(objectFlowState); // get the
                                                                // classifier
        if (c != null) {
            if (Model.getFacade().isAClassifierInState(c)) {
                Object classifier = Model.getFacade().getType(c);
                if ((s == null) || "".equals(s)) {
                    // the State of a ClassifierInState is removed,
                    // so let's reduce it to a Classifier.
                    Model.getCoreHelper().setType(objectFlowState, classifier);
                    delete(c);
                    Model.getCoreHelper().setType(objectFlowState, classifier);
                    return objectFlowState; // the model is changed - job done
                }
                Collection states =
                    new ArrayList(Model.getFacade()
                        .getInStates(c));
                Collection statesToBeRemoved = new ArrayList(states);
                Collection namesToBeAdded = new ArrayList(); // Strings
                StringTokenizer tokenizer = new StringTokenizer(s, ",");
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken().trim();
                    boolean found = false;
                    Iterator i = states.iterator();
                    while (i.hasNext()) {
                        Object state = i.next();
                        if (Model.getFacade().getName(state) == nextToken) {
                            found = true;
                            statesToBeRemoved.remove(state);
                        }
                    }
                    if (!found) {
                        namesToBeAdded.add(nextToken);
                    }
                }
                /* Remove the states that did not match. */
                states.removeAll(statesToBeRemoved);

                Iterator i = namesToBeAdded.iterator();
                while (i.hasNext()) {
                    String name = (String) i.next();
                    /*
                     * Now we have to see if any state in any statemachine of
                     * classifier is named [name]. If so, then we only have to
                     * link the state to c.
                     */
                    Object state =
                        Model.getActivityGraphsHelper()
                            .findStateByName(classifier, name);
                    if (state != null) {
                        states.add(state);
                        // the model is changed - our job is done
                    } else {
                        // no state named s is found, so we have to
                        // reject the user's input
                        String msg = 
                            "parsing.error.object-flow-state.state-not-found";
                        Object[] args = {s};
                        throw new ParseException(Translator.localize(msg, args),
                                0);
                    }
                }

                /* Finally, do the adaptations: */
                Model.getActivityGraphsHelper().setInStates(c, states);

            } else { // then c is a "normal" Classifier
                Collection statesToBeAdded = new ArrayList(); // UML states

                StringTokenizer tokenizer = new StringTokenizer(s, ",");
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken().trim();
                    Object state =
                        Model.getActivityGraphsHelper()
                            .findStateByName(c, nextToken);
                    if (state != null) {
                        statesToBeAdded.add(state);
                    } else {
                        // no state with the given name is found, so we have to
                        // reject the complete user's input
                    	String msg = 
                    	    "parsing.error.object-flow-state.state-not-found";
                        Object[] args = {s};
                        throw new ParseException(Translator.localize(msg, args),
                                0);
                    }
                }

                // let's create a new ClassifierInState with the correct links
                Object cis =
                    Model.getActivityGraphsFactory()
                        .buildClassifierInState(c, statesToBeAdded);
                Model.getCoreHelper().setType(myObjectFlowState, cis);
                // the model is changed - our job is done
            }
        } else {
            // if no classifier has been set, then entering a state is
            // not useful, so the user's input has to be rejected.
            String msg = 
        	    "parsing.error.object-flow-state.classifier-not-found";
            throw new ParseException(Translator.localize(msg), 
                    0);
        }
        return objectFlowState;
    }

    /**
     * This deletes modelelements, and swallows null without barking.
     *
     * @author mvw@tigris.org
     * @param obj
     *            the modelelement to be deleted
     */
    private void delete(Object obj) {
        if (obj != null) {
            ProjectManager.getManager().getCurrentProject().moveToTrash(obj);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer theNewText = new StringBuffer("");
        Object cis = Model.getFacade().getType(myObjectFlowState);
        if (Model.getFacade().isAClassifierInState(cis)) {
            theNewText.append("[ ");
            Collection states = Model.getFacade().getInStates(cis);
            Iterator i = states.iterator();
            boolean first = true;
            while (i.hasNext()) {
                if (!first) {
                    theNewText.append(", ");
                }
                first = false;
                Object state = i.next();
                theNewText.append(Model.getFacade().getName(state));
            }
            theNewText.append(" ]");
        }
        return theNewText.toString();
    }
}
