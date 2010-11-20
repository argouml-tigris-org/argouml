/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2009 The Regents of the University of California. All
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
import org.argouml.model.Model;
import org.argouml.model.StateMachinesFactory;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.TransitionNotation;

/**
 * UML Notation for the text shown next to a Transition.
 *
 * @author Michiel van der Wulp
 */
public class TransitionNotationUml extends TransitionNotation {

    /**
     * The constructor.
     *
     * @param transition the transition represented by this notation
     */
    public TransitionNotationUml(Object transition) {
        super(transition);
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#parse(java.lang.Object, java.lang.String)
     */
    public void parse(Object modelElement, String text) {
        try {
            parseTransition(modelElement, text);
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.transition";
            Object[] args = {
                pe.getLocalizedMessage(),
                Integer.valueOf(pe.getErrorOffset()),
            };
            ArgoEventPump.fireEvent(new ArgoHelpEvent(
                    ArgoEventTypes.HELP_CHANGED, this,
                    Translator.messageFormat(msg, args)));
        }
    }

    /**
     * Parse a transition description line of the form:<pre>
     *    "event-signature [guard-condition] / action-expression".
     * </pre>
     *
     * A ";" is not interpreted as having any special meaning. <p>
     *
     * The "event-signature" may be one of the 4
     * formats:<ul>
     * <li> ChangeEvent: "when(condition)"
     * <li> TimeEvent: "after(duration)"
     * <li> CallEvent: "a(parameter-list)".
     * <li> SignalEvent: any string without ().
     * </ul>
     *
     * Remark: The UML standard does not make a distinction between
     * the syntax of a CallEvent and SignalEvent:
     * both may have parameters between ().
     * For simplicity and user-friendliness, we chose for this distinction.
     * If a user wants parameters for a SignalEvent,
     * then he may add them in the properties panels, but not on the diagram.
     * <p>
     *
     * An alternative solution would be to create a CallEvent by default,
     * and when editing an existing event, do not change the type.<p>
     *
     * TODO: This function fails when the event-signature contains a "["
     * or a "/". See issue 5983 for other cases that were 
     * a problem in the past.
     *
     * @param trans the transition object to which this string applies
     * @param s     the string to be parsed
     * @return      the transition object
     * @throws ParseException when no matching [] are found
     */
    protected Object parseTransition(Object trans, String s)
        throws ParseException {
        s = s.trim();

        int a = s.indexOf("[");
        int b = s.indexOf("]");
        int c = s.indexOf("/");
        if (((a < 0) && (b >= 0)) || ((b < 0) && (a >= 0)) || (b < a)) {
            String msg = "parsing.error.transition.no-matching-square-brackets";
            throw new ParseException(Translator.localize(msg), 0);
        }
        if ((c >= 0) && (c < b) && (c > a) && (a > 0)) {
            String msg = "parsing.error.transition.found-bracket-instead-slash";
            throw new ParseException(Translator.localize(msg), 0);
        }

        String[] s1 = s.trim().split("/", 2);
        String eg = s1[0].trim();
        String[] s2 = eg.split("\\[", 2);
        
        if (s2[0].trim().length() > 0) {
            parseTrigger(trans, s2[0].trim());
        }
        if (s2.length > 1)  {
            if (s2[1].trim().endsWith("]")) {
                String g = s2[1].trim();
                g = g.substring(0, g.length() - 1).trim();
                if (g.length() > 0) {
                    parseGuard(trans, g);
                }
            }
        }
        if (s1.length > 1)  {
            if (s1[1].trim().length() > 0) {
                parseEffect(trans, s1[1].trim()); 
            }
        }
        return trans;
    }

    /**
     * Parse the Event that is the trigger of the given transition.
     *
     * @param trans the transition which is triggered by the given event
     * @param trigger the given trigger
     * @throws ParseException
     */
    private void parseTrigger(Object trans, String trigger)
        throws ParseException {
        // let's look for a TimeEvent, ChangeEvent, CallEvent or SignalEvent
        String s = "";
        boolean timeEvent = false;
        boolean changeEvent = false;
        boolean callEvent = false;
        boolean signalEvent = false;
        trigger = trigger.trim();

        StringTokenizer tokenizer = new StringTokenizer(trigger, "()");
        String name = tokenizer.nextToken().trim();
        if (name.equalsIgnoreCase("after")) {
            timeEvent = true;
        } else if (name.equalsIgnoreCase("when")) {
            changeEvent = true;
        } else {
            // the part after the || is for when there's nothing between the ()
            if (tokenizer.hasMoreTokens()
                    || (trigger.indexOf("(") > 0)
                    || (trigger.indexOf(")") > 1)) {
                callEvent = true;
                if (!trigger.endsWith(")") || !(trigger.indexOf("(") > 0)) {
                    String msg =
                    	"parsing.error.transition.no-matching-brackets";
                    throw new ParseException(
                            Translator.localize(msg), 0);
                }
            } else {
                signalEvent = true;
            }
        }
        if (timeEvent || changeEvent || callEvent) {
            if (tokenizer.hasMoreTokens()) {
                s = tokenizer.nextToken().trim();
            } // else the empty s will do
        }

        /*
         * We can distinguish between 4 cases:
         * 1. A trigger is given. None exists yet.
         * 2. The trigger was present, and it is the same type,
         * or a different type, and its text is changed, or the same.
         * 3. A trigger is not given. None exists yet.
         * 4. The name of the trigger was present, but is removed.
         * The reaction in these cases should be:
         * 1. Find the referred trigger (issue 5988) or create a new one, and hook it to the transition.
         * 2. Rename the trigger.
         * 3. Nop.
         * 4. Unhook and erase the existing trigger.
         */
        Object evt = Model.getFacade().getTrigger(trans);
        /* It is safe to give a null to the next function,
         * since a statemachine is always composed by a model anyhow. */
        Object ns =
            Model.getStateMachinesHelper()
                .findNamespaceForEvent(trans, null);
        StateMachinesFactory sMFactory =
                Model.getStateMachinesFactory();
        boolean weHaveAnEvent = false;
        if (trigger.length() > 0) {
            // case 1 and 2
            if (evt == null) {
                // case 1
                if (timeEvent) { // after(...)
                    evt = findOrBuildTimeEvent(s, ns);
                    /* Do not set the name. */
                }
                if (changeEvent) { // when(...)
                    evt = findOrBuildChangeEvent(s, ns);
                    /* Do not set the name. */
                }
                if (callEvent) { // operation(paramlist)
                    String triggerName =
                        trigger.indexOf("(") > 0
                        ? trigger.substring(0, trigger.indexOf("(")).trim()
                        : trigger;
                    /* This case is a bit different, because of the parameters. 
                     * If the event already exists, the parameters are ignored. */
                    evt = findCallEvent(triggerName, ns);
                    if (evt == null) {
                        evt = sMFactory.buildCallEvent(trans, triggerName, ns);
                        // and parse the parameter list
                        NotationUtilityUml.parseParamList(evt, s, 0);
                    }
                }
                if (signalEvent) { // signalname
                    evt = findOrBuildSignalEvent(trigger, ns);
                }
                weHaveAnEvent = true;
            } else {
                // case 2
                if (timeEvent) {
                    if (Model.getFacade().isATimeEvent(evt)) {
                        /* Just change the time expression */
                        Object timeExpr = Model.getFacade().getWhen(evt);
                        if (timeExpr == null) {
                            // we have an event without expression
                            timeExpr = Model.getDataTypesFactory().createTimeExpression("", s);
                            Model.getStateMachinesHelper().setWhen(evt, timeExpr);
                        } else {
                            Model.getDataTypesHelper().setBody(timeExpr, s);
                        }
                    } else {
                        /* It's a time-event now,
                         * but was of another type before! */
                        delete(evt); /* TODO: What if used elsewhere? */
                        evt = sMFactory.buildTimeEvent(s, ns);
                        weHaveAnEvent = true;
                    }
                }
                if (changeEvent) {
                    if (Model.getFacade().isAChangeEvent(evt)) {
                        /* Just change the ChangeExpression */
                        Object changeExpr =
                            Model.getFacade().getChangeExpression(evt);
                        if (changeExpr == null) {
                            /* Create a new expression: */
                            changeExpr = Model.getDataTypesFactory()
                                .createBooleanExpression("", s);
                            Model.getStateMachinesHelper().setExpression(evt,
                                    changeExpr);
                        } else {
                            Model.getDataTypesHelper().setBody(changeExpr, s);
                        }
                    } else {
                        /* The parsed text describes a change-event,
                         * but the model contains another type! */
                        delete(evt); /* TODO: What if used elsewhere? */
                        evt = sMFactory.buildChangeEvent(s, ns);
                        weHaveAnEvent = true;
                    }
                }
                if (callEvent) {
                    if (Model.getFacade().isACallEvent(evt)) {
                        /* Just change the Name and linked operation */
                        String triggerName =
                            trigger.indexOf("(") > 0
                            ? trigger.substring(0, trigger.indexOf("(")).trim()
                                    : trigger;
                        if (!Model.getFacade().getName(evt)
                                .equals(triggerName)) {
                            Model.getCoreHelper().setName(evt, triggerName);
                        }
                            /* TODO: Change the linked operation. */
                    } else {
                        delete(evt); /* TODO: What if used elsewhere? */
                        evt = sMFactory.buildCallEvent(trans, trigger, ns);
                        // and parse the parameter list
                        NotationUtilityUml.parseParamList(evt, s, 0);
                        weHaveAnEvent = true;
                    }
                }
                if (signalEvent) {
                    if (Model.getFacade().isASignalEvent(evt)) {
                        /* Just change the Name and linked signal */
                        if (!Model.getFacade().getName(evt).equals(trigger)) {
                            Model.getCoreHelper().setName(evt, trigger);
                        }
                        /* TODO: link to the Signal. */
                    } else {
                        delete(evt); /* TODO: What if used elsewhere? */
                        evt = sMFactory.buildSignalEvent(trigger, ns);
                        weHaveAnEvent = true;
                    }
                }
            }
            if (weHaveAnEvent && (evt != null)) {
                Model.getStateMachinesHelper().setEventAsTrigger(trans, evt);
            }
        } else {
            // case 3 and 4
            if (evt == null) {
                /* case 3 */
            } else {
                // case 4
                delete(evt); // erase it
            }
        }
    }
    
    protected Object findOrBuildSignalEvent(String trigger, Object ns) {
        StateMachinesFactory sMFactory = Model.getStateMachinesFactory();
        if ((trigger == null) || ("".equals(trigger.trim()))) {
            return sMFactory.buildSignalEvent(trigger, ns);
        }
        Object result = null;
        Object type = Model.getMetaTypes().getSignalEvent();
        Collection events = Model.getModelManagementHelper()
            .getAllModelElementsOfKind(ns, type);
        for (Object event : events) {
            if (trigger.equals(Model.getFacade().getName(event))) {
                result = event;
                break;
            }
        }
        if (result == null) {
            result = sMFactory.buildSignalEvent(trigger, ns);
        }
        return result;
    }
    
    protected Object findOrBuildTimeEvent(String timeexpr, Object ns) {
        StateMachinesFactory sMFactory = Model.getStateMachinesFactory();
        if ((timeexpr == null) || ("".equals(timeexpr.trim()))) {
            return sMFactory.buildTimeEvent(timeexpr, ns);
        }
        Object result = null;
        Object type = Model.getMetaTypes().getTimeEvent();
        Collection events = Model.getModelManagementHelper()
            .getAllModelElementsOfKind(ns, type);
        for (Object event : events) {
            Object expression = Model.getFacade().getExpression(event);
            if (expression != null) {
                if (timeexpr.equals(Model.getFacade().getBody(expression))) {
                    result = event;
                    break;
                }
            }
        }
        if (result == null) {
            result = sMFactory.buildTimeEvent(timeexpr, ns);
        }
        return result;
    }
    
    protected Object findOrBuildChangeEvent(String changeexpr, Object ns) {
        StateMachinesFactory sMFactory = Model.getStateMachinesFactory();
        if ((changeexpr == null) || ("".equals(changeexpr.trim()))) {
            return sMFactory.buildChangeEvent(changeexpr, ns);
        }
        Object result = null;
        Object type = Model.getMetaTypes().getChangeEvent();
        Collection events = Model.getModelManagementHelper()
            .getAllModelElementsOfKind(ns, type);
        for (Object event : events) {
            Object expression = Model.getFacade().getExpression(event);
            if (expression != null) {
                if (changeexpr.equals(Model.getFacade().getBody(expression))) {
                    result = event;
                    break;
                }
            }
        }
        if (result == null) {
            result = sMFactory.buildChangeEvent(changeexpr, ns);
        }
        return result;
    }
    
    protected Object findCallEvent(String callexpr, Object ns) {
        if ((callexpr == null) || ("".equals(callexpr.trim()))) {
            return null;
        }
        Object result = null;
        Object type = Model.getMetaTypes().getCallEvent();
        Collection events = Model.getModelManagementHelper()
            .getAllModelElementsOfKind(ns, type);
        for (Object event : events) {
            if (callexpr.equals(Model.getFacade().getName(event))) {
                /* Do not check if the parameters match. */
                result = event;
                break;
            }
        }
        return result;
    }

    /**
     * Handle the Guard of a Transition.<p>
     *
     * We can distinct between 4 cases:<ol>
     * <li>A guard is given. None exists yet.
     * <li>The expression of the guard was present, but is altered.
     * <li>A guard is not given. None exists yet.
     * <li>The expression of the guard was present, but is removed.
     * </ol>
     *
     * The reaction in these cases should be:<ol>
     * <li>Create a new guard, set its name, language & expression,
     *     and hook it to the transition.
     * <li>Change the guard's expression. Leave the name & language
     *     untouched. See also issue 2742.
     * <li>Nop.
     * <li>Unhook and erase the existing guard.
     * </ol>
     *
     * @param trans the UML element transition
     * @param guard the string that represents the guard expression
     */
    private void parseGuard(Object trans, String guard) {
        Object g = Model.getFacade().getGuard(trans);
        if (guard.length() > 0) {
            if (g == null) {
                // case 1
                /*TODO: In the next line, I should use buildGuard(),
                 * but it doesn't show the guard on the diagram...
                 * Why? (MVW)
                 */
                g = Model.getStateMachinesFactory().createGuard();
                if (g != null) {
                    Model.getStateMachinesHelper().setExpression(g,
                            Model.getDataTypesFactory()
                                .createBooleanExpression("", guard));
                    Model.getCoreHelper().setName(g, "anon");
                    Model.getCommonBehaviorHelper().setTransition(g, trans);

                    // NSUML does this (?)
                    // Model.getFacade().setGuard(trans, g);
                }
            } else {
                // case 2
                Object expr = Model.getFacade().getExpression(g);
                String language = "";

                /* TODO: This does not work! (MVW)
                 Model.getFacade().setBody(expr,guard);
                 Model.getFacade().setExpression(g,expr); */

                //hence a less elegant workaround that works:
                if (expr != null) {
                    language = Model.getDataTypesHelper().getLanguage(expr);
                }
                Model.getStateMachinesHelper().setExpression(g,
                        Model.getDataTypesFactory()
                                .createBooleanExpression(language, guard));
                /* TODO: In this case, the properties panel
                 is not updated with the changed expression! */
            }
        } else {
            if (g == null) {
                /* case 3 */
            } else {
                // case 4
                delete(g); // erase it
            }
        }
    }

    /**
     * Handle the Effect (Action) of a Transition.<p>
     *
     * We can distinct between 4 cases:<ul>
     * <li>1. An effect is given. None exists yet.
     * <li>2. The expression of the effect was present, but is altered.
     * <li>3. An effect is not given. None exists yet.
     * <li>4. The expression of the effect was present, but is removed.
     * </ul>
     *
     * The reaction in these cases should be:<ul>
     * <li>1. Create a new CallAction, set its name, language &
     * expression, and hook it to the transition.
     * <li>2. Change the effect's expression. Leave the actiontype, name
     * & language untouched.
     * <li>3. Nop.
     * <li>4. Unhook and erase the existing effect.
     * </ul>
     *
     * @param actions the string to be parsed
     * @param trans the transition that causes the effect (actions)
     */
    private void parseEffect(Object trans, String actions) {
        Object effect = Model.getFacade().getEffect(trans);
        if (actions.length() > 0) {
            if (effect == null) { // case 1
                effect =
                    Model.getCommonBehaviorFactory()
                        .createCallAction();
                /* And hook it to the transition immediately,
                 * so that an exception can not cause it to remain dangling: */
                Model.getStateMachinesHelper().setEffect(trans, effect);
                Model.getCommonBehaviorHelper().setScript(effect,
                        Model.getDataTypesFactory()
                                .createActionExpression(""/*language*/,
                                                        actions));
                Model.getCoreHelper().setName(effect, "anon");
            } else { // case 2
                Object script = Model.getFacade().getScript(effect);
                String language = (script == null) ? null
                        : Model.getDataTypesHelper().getLanguage(script);
                Model.getCommonBehaviorHelper().setScript(effect,
                        Model.getDataTypesFactory()
                                .createActionExpression(language, actions));
            }
        } else { // case 3 & 4
            if (effect == null) {
                // case 3
            } else {
                // case 4
                delete(effect); // erase it
            }
        }
    }

    /**
     * This deletes modelelements, and swallows null without barking.
     *
     * @author Michiel van der Wulp
     * @param obj
     *            the modelelement to be deleted
     */
    private void delete(Object obj) {
        if (obj != null) {
            Model.getUmlFactory().delete(obj);
        }
    }

    /*
     * @see org.argouml.uml.notation.NotationProvider#getParsingHelp()
     */
    public String getParsingHelp() {
        return "parsing.help.fig-transition";
    }

    @Override
    public String toString(Object modelElement, NotationSettings settings) {
        return toString(modelElement);
    }

    private String toString(Object modelElement) {
        Object trigger = Model.getFacade().getTrigger(modelElement);
    	Object guard = Model.getFacade().getGuard(modelElement);
        Object effect = Model.getFacade().getEffect(modelElement);
        String t = generateEvent(trigger);
        String g = generateGuard(guard);
        String e = NotationUtilityUml.generateActionSequence(effect);
        if (g.length() > 0) {
            t += " [" + g + "]";
        }
        if (e.length() > 0) {
            t += " / " + e;
        }
        return t;
    }

    /**
     * Generates the text for a (trigger) event.
     *
     * @param m Object of any MEvent kind
     * @return the string representing the event
     */
    private String generateEvent(Object m) {
        if (m == null) {
            return "";
        }
        StringBuffer event = new StringBuffer();
        if (Model.getFacade().isAChangeEvent(m)) {
            event.append("when(");
            event.append(
                    generateExpression(Model.getFacade().getExpression(m)));
            event.append(")");
        } else if (Model.getFacade().isATimeEvent(m)) {
            event.append("after(");
            event.append(
                    generateExpression(Model.getFacade().getExpression(m)));
            event.append(")");
        } else if (Model.getFacade().isASignalEvent(m)) {
            event.append(Model.getFacade().getName(m));
        } else if (Model.getFacade().isACallEvent(m)) {
            event.append(Model.getFacade().getName(m));
            event.append(generateParameterList(m));
        }
        return event.toString();
    }

    /**
     * Generates a string representing the given Guard. <p>
     *
     * If there is an expression, then its body text is returned.
     * Else, a 0 length string is returned. <p>
     *
     * Apparently, the AndroMDA people are convinced that the
     * name of the guard should be shown on the diagram, while this
     * is not correct according the UML standard.
     * ArgoUML does not support this feature because of its down-side:
     * The name would end up in the expression
     * if you edit the transition text on the diagram,
     * while the name remains containing the old value.
     *
     * @param m the UML Guard object
     * @return a string
     */
    private String generateGuard(Object m) {
        if (m != null) {
            if (Model.getFacade().getExpression(m) != null) {
                return generateExpression(Model.getFacade().getExpression(m));
            }
        }
        return "";
    }

    /**
     * Generates a list of parameters. The parameters belong to the
     * given object.  The returned string will have the following
     * syntax:<p>
     *
     * (param1, param2, param3, ..., paramN)<p>
     *
     * If there are no parameters, then "()" is returned.
     *
     * @param parameterListOwner the 'owner' of the parameters
     * @return the generated parameter list
     */
    private String generateParameterList(Object parameterListOwner) {
        Iterator it =
            Model.getFacade().getParameters(parameterListOwner).iterator();
        StringBuffer list = new StringBuffer();
        list.append("(");
        if (it.hasNext()) {
            while (it.hasNext()) {
                Object param = it.next();
                list.append(generateParameter(param));
                if (it.hasNext()) {
                    list.append(", ");
                }
            }
        }
        list.append(")");
        return list.toString();
    }

    private String generateExpression(Object expr) {
        if (Model.getFacade().isAExpression(expr)) {
            Object body = Model.getFacade().getBody(expr);
            if (body != null) {
                return (String) body;
            }
        }
        return "";
    }

    /**
     * Generates the representation of a parameter on the display
     * (diagram). The string to be returned will have the following
     * syntax:<p>
     *
     * kind name : type-expression = default-value
     *
     * @param parameter the parameter
     * @return the generated text
     */
    public String generateParameter(Object parameter) {
        StringBuffer s = new StringBuffer();
        s.append(generateKind(Model.getFacade().getKind(parameter)));
        if (s.length() > 0) {
            s.append(" ");
        }
        s.append(Model.getFacade().getName(parameter));
        String classRef =
            generateClassifierRef(Model.getFacade().getType(parameter));
        if (classRef.length() > 0) {
            s.append(" : ");
            s.append(classRef);
        }
        String defaultValue =
            generateExpression(Model.getFacade().getDefaultValue(parameter));
        if (defaultValue.length() > 0) {
            s.append(" = ");
            s.append(defaultValue);
        }
        return s.toString();
    }

    private String generateKind(Object /*Parameter etc.*/ kind) {
        StringBuffer s = new StringBuffer();
        if (kind == null /* "in" is the default */
                || kind == Model.getDirectionKind().getInParameter()) {
            s.append(/*"in"*/ ""); /* See issue 3421. */
        } else if (kind == Model.getDirectionKind().getInOutParameter()) {
            s.append("inout");
        } else if (kind == Model.getDirectionKind().getReturnParameter()) {
            // return nothing
        } else if (kind == Model.getDirectionKind().getOutParameter()) {
            s.append("out");
        }
        return s.toString();
    }

    /**
     * Generate the type of a parameter, i.e. a reference to a classifier.
     *
     * @param cls the classifier
     * @return the generated text
     */
    private String generateClassifierRef(Object cls) {
        if (cls == null) {
            return "";
        }
        return Model.getFacade().getName(cls);
    }

}
