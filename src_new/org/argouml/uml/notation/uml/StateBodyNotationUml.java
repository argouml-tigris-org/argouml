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

import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.notation.StateBodyNotation;

/**
 * UML notation for the body of a state.
 * 
 * @author mvw@tigris.org
 */
public class StateBodyNotationUml extends StateBodyNotation {

    /**
     * The constructor.
     *
     * @param state the state represented by the notation
     */
    public StateBodyNotationUml(Object state) {
        super(state);
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            parseStateBody(modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.statebody";
            Object[] args = {
                pe.getLocalizedMessage(),
                new Integer(pe.getErrorOffset()),
            };
            ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
        }
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-statebody";
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#toString(java.lang.Object, java.util.HashMap)
     */
    public String toString(Object modelElement, HashMap args) {
        StringBuffer s = new StringBuffer();

        Object entryAction = Model.getFacade().getEntry(modelElement);
        Object exitAction = Model.getFacade().getExit(modelElement);
        Object doAction = Model.getFacade().getDoActivity(modelElement);
        if (entryAction != null) {
            String entryStr = 
                NotationUtilityUml.generateActionSequence(entryAction);
            s.append("entry /").append(entryStr);
        }
        if (doAction != null) {
            String doStr = NotationUtilityUml.generateActionSequence(doAction);
            if (s.length() > 0) {
                s.append("\n");
            }
            s.append("do /").append(doStr);

        }
        if (exitAction != null) {
            String exitStr = 
                NotationUtilityUml.generateActionSequence(exitAction);
            if (s.length() > 0) {
                s.append("\n");
            }
            s.append("exit /").append(exitStr);
        }
        Collection internaltrans =
            Model.getFacade().getInternalTransitions(modelElement);
        if (internaltrans != null) {
            Iterator iter = internaltrans.iterator();
            while (iter.hasNext()) {
                if (s.length() > 0) {
                    s.append("\n");
                }
                Object trans = iter.next();
                /* TODO: Is this a good way of handling nested notation? */
                s.append((new TransitionNotationUml(trans))
                            .toString(trans, null));
            }
        }
        return s.toString();
    }

    /**
     * Parse user input for state bodies and assign the individual lines to
     * according actions or transistions. The user input consists of multiple
     * lines like:<pre>
     *   action-label / action-expression
     * </pre> or the format of a regular
     * transition - see parseTransition(). <p>
     *
     * "action-label" stands for one of "entry", "do" and "exit".
     * The words "entry", "do" and "exit" are case-independent.
     *
     * @param st  The State object.
     * @param s   The string to parse.
     * @throws ParseException when there is a syntax problem,
     *         e.g. non-matching brackets () or []
     */
    protected void parseStateBody(Object st, String s) throws ParseException {
        boolean foundEntry = false;
        boolean foundExit = false;
        boolean foundDo = false;

        /* Generate all the existing internal transitions,
         * so that we can compare them as text with the newly entered ones.
         */
        ModelElementInfoList internalsInfo =
            new ModelElementInfoList(
                    Model.getFacade().getInternalTransitions(st));

        StringTokenizer lines = new StringTokenizer(s, "\n\r");
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            /* Now let's check if the new line is already present in
             * the old list of internal transitions; if it is, then
             * mark the old one to be retained (i.e. do not create a new one),
             * if it isn't, continue with parsing:
             */
            if (!internalsInfo.checkRetain(line)) {
                if (line.toLowerCase().startsWith("entry")
                        && line.substring(5).trim().startsWith("/")) {
                    parseStateEntryAction(st, line);
                    foundEntry = true;
                } else if (line.toLowerCase().startsWith("exit")
                        && line.substring(4).trim().startsWith("/")) {
                    parseStateExitAction(st, line);
                    foundExit = true;
                } else if (line.toLowerCase().startsWith("do")
                        && line.substring(2).trim().startsWith("/")) {
                    parseStateDoAction(st, line);
                    foundDo = true;
                } else {
                    Object t =
                        Model.getStateMachinesFactory()
                                .buildInternalTransition(st);
                    if (t == null) {
                        continue;
                    }
                    /* TODO: If the next line trows an exception, then what
                     * do we do with the remainder of the
                     * parsed/to be parsed lines?
                     */
                    /* TODO: Is this a good way of handling nested notation?
                     * The following fails the tests:
                     * new TransitionNotationUml(t).parse(line);
                     */
                    new TransitionNotationUml(t).parseTransition(t, line);
                    /* Add this new one, and mark it to be retained: */
                    internalsInfo.add(t, true);
                }
            }
        }

        if (!foundEntry) {
            delete(Model.getFacade().getEntry(st));
        }
        if (!foundExit) {
            delete(Model.getFacade().getExit(st));
        }
        if (!foundDo) {
            delete(Model.getFacade().getDoActivity(st));
        }

        /* Process the final list of internal transitions,
         * and hook it to the state:
         */
        Model.getStateMachinesHelper().setInternalTransitions(st,
                internalsInfo.finalisedList());
    }

    /**
     * This class manages a list of UML modelelements that existed
     * before and after the parseXxxxx() function was called.
     * It has all the knowledge to deal with additions and removals.
     *
     * @author MVW
     */
    class ModelElementInfoList {
        /**
         * The list that we maintain.
         */
        private Collection theList;

        /**
         * An item in a list, maintains all info about one UML object,
         * its generated version (i.e. textual representation),
         * and if it needs to be retained after parsing.<p>
         *
         * @author MVW
         */
        class InfoItem {
            private TransitionNotationUml generator;
            private Object umlObject;
            private boolean retainIt;

            /**
             * The constructor.
             * @param obj the uml object
             */
            InfoItem(Object obj) {
                umlObject = obj;
                generator = new TransitionNotationUml(obj);
            }

            /**
             * The constructor.
             *
             * @param obj the uml object.
             * @param r
             */
            InfoItem(Object obj, boolean r) {
                this(obj);
                retainIt = r;
            }

            /**
             * @return the generated string representation
             */
            String getGenerated() {
                return generator.toString();
            }

            /**
             * @return the UML Object
             */
            Object getUmlObject() {
                return umlObject;
            }

            /**
             * Retain this UML object.
             */
            void retain() {
                retainIt = true;
            }

            /**
             * @return true if the UML object is to be retained,
             *         false if it is to be deleted
             */
            boolean isRetained() {
                return retainIt;
            }
        }

        /**
         * The constructor.
         *
         * @param c the collection of the UML objects
         *          that were present before
         */
        ModelElementInfoList(Collection c) {
            theList = new ArrayList();
            Iterator i = c.iterator();
            while (i.hasNext()) {
                theList.add(new InfoItem(i.next()));
            }
        }

        /**
         * @param obj the UML object
         * @param r true if this UML object needs to be retained
         */
        void add(Object obj, boolean r) {
            theList.add(new InfoItem(obj, r));
        }

        /**
         * Check the given textual description,
         * and if already present in the list, then retain it.
         * @param line the given textual description
         * @return true if the item was already present in the list
         */
        boolean checkRetain(String line) {
            Iterator i = theList.iterator();
            while (i.hasNext()) {
                InfoItem tInfo =
                    (InfoItem) i.next();
                if (tInfo.getGenerated().equals(line)) {
                    tInfo.retain();
                    return true;
                }
            }
            return false;
        }

        /**
         * Finish the procedure, by deleting the UML model items
         * that are not to be retained, and return a collection
         * of the ones to be retained.
         * This method should only be called once!
         * @return the UML objects that survive.
         */
        Collection finalisedList() {
            // don't forget to remove old internals!
            Collection newModelElementsList = new ArrayList();
            Iterator i = theList.iterator();
            while (i.hasNext()) {
                InfoItem tInfo =
                    (InfoItem) i.next();
                if (tInfo.isRetained()) {
                    newModelElementsList.add(tInfo.getUmlObject());
                } else {
                    delete(tInfo.getUmlObject());
                }
            }
            // Make next accesses to this instance predictable:
            theList.clear();
            // and hook in the new ones:
            return newModelElementsList;
        }
    }


    /**
     * Parse a line of the form: "entry /action" and create an action.
     * We do not need to check for the presence of the word "entry" - that
     * is done by the caller.
     *
     * @param st  the state object
     * @param s   the string to be parsed
     */
    public void parseStateEntryAction(Object st, String s) {
        if (s.indexOf("/") > -1) {
            s = s.substring(s.indexOf("/") + 1).trim();
        }
        Object oldEntry = Model.getFacade().getEntry(st);
        if (oldEntry == null) {
            Model.getStateMachinesHelper().setEntry(st, buildNewCallAction(s));
        } else {
            updateAction(oldEntry, s);
        }
    }

    /**
     * Parse a line of the form: "exit /action" and create an action.
     * We do not need to check for the presence of the word "exit" - that
     * is done by the caller.
     *
     * @param st
     *            the state object
     * @param s
     *            the string to be parsed
     */
    public void parseStateExitAction(Object st, String s) {
        if (s.indexOf("/") > -1) {
            s = s.substring(s.indexOf("/") + 1).trim();
        }
        Object oldExit = Model.getFacade().getExit(st);
        if (oldExit == null) {
            Model.getStateMachinesHelper().setExit(st, buildNewCallAction(s));
        } else {
            updateAction(oldExit, s);
        }
    }

    /**
     * Parse a line of the form: "do /action" and create an action.
     * We do not need to check for the presence of the word "do" - that
     * is done by the caller.
     *
     * @param st  the state object
     * @param s   the string to be parsed
     */
    public void parseStateDoAction(Object st, String s) {
        if (s.indexOf("/") > -1) {
            s = s.substring(s.indexOf("/") + 1).trim();
        }
        Object oldDo = Model.getFacade().getDoActivity(st);
        if (oldDo == null) {
            Model.getStateMachinesHelper().setDoActivity(st,
                    buildNewCallAction(s));
        } else {
            updateAction(oldDo, s);
        }
    }

    /**
     * This builds a CallAction with default attributes. But without Operation!
     *
     * @author MVW
     * @param s
     *            string representing the Script of the Action
     * @return The newly created CallAction.
     */
    private Object buildNewCallAction(String s) {
        Object a =
            Model.getCommonBehaviorFactory().createCallAction();
        Object ae =
            Model.getDataTypesFactory().createActionExpression("Java", s);
        Model.getCommonBehaviorHelper().setScript(a, ae);
        Model.getCoreHelper().setName(a, "anon");
        return a;
    }

    /**
     * Update an existing Action with a new Script.
     *
     * @author MVW
     * @param old the Action
     * @param s   a string representing a new Script for the ActionExpression
     */
    private void updateAction(Object old, String s) {
        Object ae = Model.getFacade().getScript(old); // the ActionExpression
        String language = "Java";
        if (ae != null) {
            language = Model.getDataTypesHelper().getLanguage(ae);
            String body = (String) Model.getFacade().getBody(ae);
            if (body.equals(s)) {
                return;
            }
        }
        ae = Model.getDataTypesFactory().createActionExpression(language, s);
        Model.getCommonBehaviorHelper().setScript(old, ae);
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

    /*
     * @see org.argouml.uml.notation.NotationProvider#addListener(java.beans.PropertyChangeListener, java.lang.Object)
     */
    public void initialiseListener(PropertyChangeListener listener, 
            Object modelElement) {
        addElementListener(listener, modelElement);
//      register for internal transitions:
        Iterator it =
            Model.getFacade().getInternalTransitions(modelElement).iterator();
        while (it.hasNext()) {
            addListenersForTransition(listener, it.next());
        }
        // register for the doactivity etc.
        Object doActivity = Model.getFacade().getDoActivity(modelElement);
        addListenersForAction(listener, doActivity);
        Object entryAction = Model.getFacade().getEntry(modelElement);
        addListenersForAction(listener, entryAction);
        Object exitAction = Model.getFacade().getExit(modelElement);
        addListenersForAction(listener, exitAction);
    }

    private void addListenersForAction(PropertyChangeListener listener, 
            Object action) {
        if (action != null) {
            addElementListener(listener, action,
                    new String[] {
                        "script", "actualArgument", "action"
                    });
            Collection args = Model.getFacade().getActualArguments(action);
            Iterator i = args.iterator();
            while (i.hasNext()) {
                Object argument = i.next();
                addElementListener(listener, argument, "value");
            }
            if (Model.getFacade().isAActionSequence(action)) {
                Collection subactions = Model.getFacade().getActions(action);
                i = subactions.iterator();
                while (i.hasNext()) {
                    Object a = i.next();
                    addListenersForAction(listener, a);
                }
            }
        }
    }

    private void addListenersForEvent(PropertyChangeListener listener, 
            Object event) {
        if (event != null) {
            addElementListener(listener, event,
                    new String[] {
                        "parameter", "name",
                    });
            Collection prms = Model.getFacade().getParameters(event);
            Iterator i = prms.iterator();
            while (i.hasNext()) {
                Object parameter = i.next();
                addElementListener(listener, parameter);
            }
        }
    }
    
    private void addListenersForTransition(PropertyChangeListener listener, 
            Object transition) {
        addElementListener(listener, transition, 
                new String[] {"guard", "trigger", "effect"});

        Object guard = Model.getFacade().getGuard(transition);
        if (guard != null) {
            addElementListener(listener, guard, "expression");
        }

        Object trigger = Model.getFacade().getTrigger(transition);
        addListenersForEvent(listener, trigger);

        Object effect = Model.getFacade().getEffect(transition);
        addListenersForAction(listener, effect);
    }    

}
