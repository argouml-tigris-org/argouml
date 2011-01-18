/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2004-2008 The Regents of the University of California. All
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

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationSettings;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Test UML Notation: parsing transitions.
 *
 * @author MVW
 */
public class TestTransitionNotationUml extends TestCase {
    private Object model;
    private Object aClass;
    private Object returnType;
    private Object aStateMachine;
    private Object aState;
    private Object aOper;

    /**
     * The constructor.
     *
     * @param str the name
     */
    public TestTransitionNotationUml(String str) {
        super(str);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        InitializeModel.initializeDefault();
        assertTrue("Model subsystem init failed.", Model.isInitiated());
        new InitProfileSubsystem().init();
        Project p = ProjectManager.getManager().makeEmptyProject();
        returnType = p.getDefaultReturnType();

        model = Model.getModelManagementFactory().createModel();
        aClass = Model.getCoreFactory().buildClass("A", model);
        aOper = 
            Model.getCoreFactory().buildOperation2(
                    aClass, returnType, "myOper");
        aStateMachine =
            Model.getStateMachinesFactory().buildStateMachine(aClass);
        Object top = Model.getFacade().getTop(aStateMachine);
        aState = Model.getStateMachinesFactory().buildCompositeState(top);
    }

    @Override
    protected void tearDown() throws Exception {
        ProjectManager.getManager().removeProject(
                ProjectManager.getManager().getCurrentProject());
        ProfileFacade.reset();
        super.tearDown();
    }
    
    /**
     * Test the reuse of events when a transition notation is parsed. 
     * See issue 5988.
     */
    public void testParseEventsReuse() {
        // reuse signal event:
        Object t1s = checkGenerated(aState, "trigger1[guard]/effect", 
                true, true, true, false);
        Object t2s = checkGenerated(aState, "trigger1[guard]/effect", 
                true, true, true, false);
        Object t3s = checkGenerated(aState, "trigger2[guard]/effect", 
                true, true, true, false);
        Object trigger1s = Model.getFacade().getTrigger(t1s);
        Object trigger2s = Model.getFacade().getTrigger(t2s);
        Object trigger3s = Model.getFacade().getTrigger(t3s);
        assertTrue("No reuse of a signal event.", trigger1s == trigger2s);
        assertTrue("Unexpected reuse of a signal event.",
                trigger1s != trigger3s);
        
        // reuse call event:
        Object t1c = checkGenerated(aState, "trigger1()[guard]/effect", 
                true, true, true, false);
        Object t2c = checkGenerated(aState, "trigger1()[guard]/effect", 
                true, true, true, false);
        Object t3c = checkGenerated(aState, "trigger2()[guard]/effect", 
                true, true, true, false);
        Object trigger1c = Model.getFacade().getTrigger(t1c);
        Object trigger2c = Model.getFacade().getTrigger(t2c);
        Object trigger3c = Model.getFacade().getTrigger(t3c);
        assertTrue("No reuse of a call event.", trigger1c == trigger2c);
        assertTrue("Unexpected reuse of a call event.", trigger1c != trigger3c);
        
        // reuse time event:
        Object t1t = checkGenerated(aState, "after(1s)[guard]/effect", 
                true, true, true, false);
        Object t2t = checkGenerated(aState, "after(1s)[guard]/effect", 
                true, true, true, false);
        Object t3t = checkGenerated(aState, "after(2s)[guard]/effect", 
                true, true, true, false);
        Object trigger1t = Model.getFacade().getTrigger(t1t);
        Object trigger2t = Model.getFacade().getTrigger(t2t);
        Object trigger3t = Model.getFacade().getTrigger(t3t);
        assertTrue("No reuse of a time event.", trigger1t == trigger2t);
        assertTrue("Unexpected reuse of a time event.", trigger1t != trigger3t);
        
        // reuse change event:
        Object t1g = checkGenerated(aState, "when(condition1)[guard]/effect", 
                true, true, true, false);
        Object t2g = checkGenerated(aState, "when(condition1)[guard]/effect", 
                true, true, true, false);
        Object t3g = checkGenerated(aState, "when(condition2)[guard]/effect", 
                true, true, true, false);
        Object trigger1g = Model.getFacade().getTrigger(t1g);
        Object trigger2g = Model.getFacade().getTrigger(t2g);
        Object trigger3g = Model.getFacade().getTrigger(t3g);
        assertTrue("No reuse of a change event.", trigger1g == trigger2g);
        assertTrue("Unexpected reuse of a change event.", 
                trigger1g != trigger3g);
    }

    /**
     * Test for the parseTransition() method.
     * These should NOT generate a ParseException.
     */
    public void testParseTransitionCreate() {
        checkGenerated(aState, "trigger[guard]/effect", true, true, true,
                false);
        checkGenerated(aState, "trigger[]/effect", true, false, true, false);
        checkGenerated(aState, " tri gg er ( w ) [ ] / e ffect ", true, false,
                true, false);
        checkGenerated(aState, "trigger / effect", true, false, true, false);
        checkGenerated(aState, "/effect", false, false, true, false);
        checkGenerated(aState, "trigger", true, false, false, false);
        checkGenerated(aState, "t(a:int=3, b=4.0:double)", true, false, false,
                false);
        checkGenerated(aState, "", false, false, false, false);
        checkGenerated(aState, "[]/", false, false, false, false);
        checkGenerated(aState, "[ guard ]", false, true, false, false);
        checkGenerated(aState, "trigger()/", true, false, false, false);
        checkGenerated(aState, "tr/i[gg]er", true, false, true, false);
        // Inspired by issue 5983:
        checkGenerated(aState, "ev1 / printf(\"got [i==1]\");", true, false, 
                true, false);
    }

    /**
     * Test for the parseTransition() method.
     * These should generate a ParseException.
     */
    public void testParseTransitionNotCreate() {
        checkGenerated(aState, "trigger[guard/effect", false, false, false,
                true);
        checkGenerated(aState, "trigger(", false, false, false, true);
        checkGenerated(aState, "trigger)/eff()", false, false, false, true);
        checkGenerated(aState, "trigger(/)", false, false, false, true);
        checkGenerated(aState, "tr]jhgf[ijh", false, false, false, true);
        checkGenerated(aState, "tr]/e", false, false, false, true);
        checkGenerated(aState, "tri[g/g]er", false, false, false, true);
    }

    /**
     * Check if the transition parts are generated or not. <p>
     *
     * An internal transition is chosen to test these cases,
     * instead of a transition between states. Reason: simplicity.
     *
     * @param st the state in which to create an internal transition
     * @param text the text to be parsed
     * @param trigger true if we expect a trigger to be generated
     * @param guard true if we expect a guard to be generated
     * @param effect true if we expect an effect to be generated
     * @param exception true if we expect an exception to be thrown
     * @return the generated internal transition
     */
    private Object checkGenerated(Object st, String text, boolean trigger,
            boolean guard, boolean effect, boolean exception) {
        Object it =
            Model.getStateMachinesFactory().buildInternalTransition(st);
        TransitionNotationUml notation = new TransitionNotationUml(it);
        try {
            notation.parseTransition(it, text);
            assertTrue("Expected exception did not happen.", !exception);
        } catch (ParseException e) {
            assertTrue("Unexpected exception: " + e.getMessage(),
                    exception);
        }
        if (trigger) {
            assertTrue("Trigger was not generated for " + text,
                    Model.getFacade().getTrigger(it) != null);
        } else {
            assertTrue("Trigger was generated for " + text,
                    Model.getFacade().getTrigger(it) == null);
        }
        if (guard) {
            assertTrue("Guard was not generated for " + text,
                    Model.getFacade().getGuard(it) != null);
        } else {
            assertTrue("Guard was generated for " + text,
                    Model.getFacade().getGuard(it) == null);
        }
        if (effect) {
            assertTrue("Effect (action) was not generated for " + text,
                    Model.getFacade().getEffect(it) != null);
        } else {
            assertTrue("Effect (action) was generated for " + text,
                    Model.getFacade().getEffect(it) == null);
        }
        return it;
    }

    /**
     * Test creating modelelements from a given notation, and then 
     * re-generate the notation-string again, and check if they are equal.
     * Some of these tests are not very usefulll, 
     * since they may fail on white space differences. 
     * TODO: White space should best be ignored. 
     */
    public void testRoundTrip() {
        checkGenerateRoundTrip(aState, "trigger [guard] / effect");
        checkGenerateRoundTrip(aState, "trigger / effect");
        checkGenerateRoundTrip(aState, "trigger [guard]");
        checkGenerateRoundTrip(aState, " [guard] / effect");
        checkGenerateRoundTrip(aState, " / effect");
        checkGenerateRoundTrip(aState, "trigger");
        checkGenerateRoundTrip(aState, " [guard]");
        checkGenerateRoundTrip(aState, "");
        checkGenerateRoundTrip(aState, "t(a : int = 3, b : double = 4.0)");
        checkGenerateRoundTrip(aState, " / effect(a:2,r=6)");
        checkGenerateRoundTrip(aState, "trigger [guard] / eff1; eff2; eff3");
        /* Issue 5983: */
        checkGenerateRoundTrip(aState, "ev1 [i==1] / printf(\"got [i==1\");");
        checkGenerateRoundTrip(aState, "ev1 [i==1] / printf(\"got [i==1]\");");
    }

    private void checkGenerateRoundTrip(Object st, String text) {
        Object it =
            Model.getStateMachinesFactory().buildInternalTransition(st);
        TransitionNotationUml notation = new TransitionNotationUml(it);
        try {
            notation.parseTransition(it, text);
        } catch (ParseException e) {
            assertTrue("Unexpected exception: " + e.getMessage(), true);
        }
        // try creating a string from the generated modelelements:
        String notationStr = notation.toString(it, 
                NotationSettings.getDefaultSettings()); 
        assertTrue("Notation not correctly generated for " + text + "\n"
                + "Resulted in: '" + notationStr + "'", 
                text.equals(notationStr));
    }

    /**
     * Test for the parseTrigger() method: TimeEvent.
     * Also changing triggertype is tested.
     */
    public void testParseTriggerTimeEvent() {
        Object trans;
        Object trig;
        String text;
        Object expr;

        //try creating a TimeEvent
        text = "after(a while)";
        trans =
            checkGenerated(aState, text, true, false, false, false);
        trig = Model.getFacade().getTrigger(trans);
        assertTrue("Unexpected triggertype found instead of TimeEvent for "
                + text, Model.getFacade().isATimeEvent(trig));
        expr = Model.getFacade().getExpression(trig);
        assertTrue("Incorrectly set TimeExpression for" + text,
                Model.getFacade().getBody(expr).equals("a while"));


        //try changing the triggertype to ChangeEvent
        text = "when(it happens)";
        NotationProvider notation = new TransitionNotationUml(trans);
        notation.parse(trans, text);
        trig = Model.getFacade().getTrigger(trans);
        assertTrue("Unexpected triggertype found instead of ChangeEvent",
                Model.getFacade().isAChangeEvent(trig));
    }

    /**
     * Test for the parseTrigger() method: ChangeEvent.
     * Testing creation and deletion.
     */
    public void testParseTriggerChangeEvent() {
        Object trans;
        Object trig;
        String text;
        Object expr;

        text = "when(it changed)/effect";
        trans = checkGenerated(aState, text, true, false, true, false);
        trig = Model.getFacade().getTrigger(trans);
        assertTrue("Unexpected triggertype found instead of ChangeEvent for "
                + text, Model.getFacade().isAChangeEvent(trig));
        expr = Model.getFacade().getExpression(trig);
        assertTrue("Incorrectly set ChangeExpression for" + text,
                Model.getFacade().getBody(expr).equals("it changed"));

        text = "/effect";
        TransitionNotationUml notation = new TransitionNotationUml(trans);
        try {
            notation.parseTransition(trans, text);
        } catch (ParseException e) {
            assertTrue("Unexpected exception when removing ChangeEvent trigger",
                    true);
        }
        trig = Model.getFacade().getTrigger(trans);
        assertTrue("Trigger not deleted", trig != null);
    }

    /**
     * Test for the parseTrigger() method: CallEvent.
     */
    public void testParseTriggerCallEvent() {
        Object trans;
        Object trig;
        String text;

        text = "call(a method)";
        trans = checkGenerated(aState, text, true, false, false, false);
        trig = Model.getFacade().getTrigger(trans);
        assertTrue("Unexpected triggertype found instead of CallEvent for "
                + text, Model.getFacade().isACallEvent(trig));

        text = "call()";
        trans = checkGenerated(aState, text, true, false, false, false);
        trig = Model.getFacade().getTrigger(trans);
        assertTrue("Unexpected triggertype found instead of CallEvent for "
                + text, Model.getFacade().isACallEvent(trig));
    }

    /**
     * Test for the parseTrigger() method: SignalEvent.
     */
    public void testParseTriggerSignalEvent() {
        Object trans;
        Object trig;
        String text;

        text = "signal";
        trans = checkGenerated(aState, text, true, false, false, false);
        trig = Model.getFacade().getTrigger(trans);
        assertTrue("Unexpected triggertype found instead of SignalEvent",
                Model.getFacade().isASignalEvent(trig));

        text = "/effect";
        TransitionNotationUml notation = new TransitionNotationUml(trans);
        try {
            notation.parseTransition(trans, text);
        } catch (ParseException e) {
            assertTrue("Unexpected exception when removing SignalEvent trigger",
                    true);
        }
        trig = Model.getFacade().getTrigger(trans);
        assertTrue("Trigger not deleted", trig != null);
    }

    /**
     * Test for the parseTrigger() method:
     * linking of an Operation for a CallEvent.
     * The operation resides on the class that is the context of the 
     * statemachine:
     */
    public void testParseTriggerCallEventOperation1() {
        checkLinkingOfOperationToCallEvent("myOper()", aOper);
    }

    /**
     * Test for the parseTrigger() method:
     * linking of an Operation for a CallEvent.
     * With the operation residing on another class within the same 
     * namespace:
     */
    public void testParseTriggerCallEventOperation2() {
        Object bClass = Model.getCoreFactory().buildClass("B", model);
        Object bOper = Model.getCoreFactory().buildOperation2(bClass, 
                returnType, "bOper");
        checkLinkingOfOperationToCallEvent("bOper()", bOper);
    }

    /**
     * Test for the parseTrigger() method:
     * linking of an Operation for a CallEvent.
     * When the context is a behavioral feature and 
     * the operation = context:
     */
    public void testParseTriggerCallEventOperation3() {
        Model.getStateMachinesHelper().setContext(aStateMachine, aOper);
        checkLinkingOfOperationToCallEvent("myOper()", aOper);
    }

    /**
     * Test for the parseTrigger() method:
     * linking of an Operation for a CallEvent.
     * When the context is a behavioral feature and the operation differs 
     * from the context:
     */
    public void testParseTriggerCallEventOperation4() {
        Model.getStateMachinesHelper().setContext(aStateMachine, aOper);
        Object bClass = Model.getCoreFactory().buildClass("B", model);
        Model.getCoreFactory().buildOperation2(bClass, returnType, "cOper");
        Object dOper = Model.getCoreFactory().buildOperation2(bClass, 
                returnType, "dOper");
        checkLinkingOfOperationToCallEvent("dOper()", dOper);
    }
    
    /**
     * Test for the parseTrigger() method:
     * linking of an Operation for a CallEvent.
     * When the context is a package and the operation is on a class 
     * within that package:
     */
    public void testParseTriggerCallEventOperation5() {
        Object aPack = Model.getModelManagementFactory().buildPackage("pack1");
        Model.getCoreHelper().setNamespace(aPack, model);
        aClass = Model.getCoreFactory().buildClass("A", aPack);
        aOper = 
            Model.getCoreFactory().buildOperation2(
                    aClass, returnType, "myOper");
        aStateMachine =
            Model.getActivityGraphsFactory().buildActivityGraph(aPack);
       
        Object top = Model.getFacade().getTop(aStateMachine);
        aState = Model.getStateMachinesFactory().buildCompositeState(top);
        
        Object bClass = Model.getCoreFactory().buildClass("B", aPack);
        Model.getCoreFactory().buildOperation2(bClass, returnType, "cOper");
        Object dOper = Model.getCoreFactory().buildOperation2(bClass, 
                returnType, "dOper");
        checkLinkingOfOperationToCallEvent("dOper()", dOper);
    }
    
    /**
     * Test for the parseTrigger() method:
     * linking of an Operation for a CallEvent.
     * When the context is a nested class and the operation is on a class 
     * within the containing package:
     */
    public void testParseTriggerCallEventOperation6() {
        Object aPack = Model.getModelManagementFactory().buildPackage("pack1");
        Model.getCoreHelper().setNamespace(aPack, model);
        aClass = Model.getCoreFactory().buildClass("A", aPack);
        // nested class:
        Object bClass = Model.getCoreFactory().buildClass("B", aClass);
        Object cClass = Model.getCoreFactory().buildClass("C", bClass);
        
        aStateMachine =
            Model.getActivityGraphsFactory().buildActivityGraph(cClass);
        Object top = Model.getFacade().getTop(aStateMachine);
        aState = Model.getStateMachinesFactory().buildCompositeState(top);
        
        Model.getCoreFactory().buildOperation2(cClass, returnType, "cOper");
        Object dOper = Model.getCoreFactory().buildOperation2(aClass, 
                returnType, "dOper");
        checkLinkingOfOperationToCallEvent("dOper()", dOper);
    }
    
    /**
     * Test for the parseTrigger() method:
     * linking of an Operation for a CallEvent.
     * When the context is a nested class and the operation is on the same 
     * nested class:
     */
    public void testParseTriggerCallEventOperation7() {
        Object aPack = Model.getModelManagementFactory().buildPackage("pack1");
        Model.getCoreHelper().setNamespace(aPack, model);
        aClass = Model.getCoreFactory().buildClass("A", aPack);
        // nested class:
        Object bClass = Model.getCoreFactory().buildClass("B", aClass);
        Object cClass = Model.getCoreFactory().buildClass("C", bClass);
        
        aStateMachine =
            Model.getActivityGraphsFactory().buildActivityGraph(cClass);
        Object top = Model.getFacade().getTop(aStateMachine);
        aState = Model.getStateMachinesFactory().buildCompositeState(top);
        
        Model.getCoreFactory().buildOperation2(cClass, returnType, "cOper");
        Object dOper = Model.getCoreFactory().buildOperation2(cClass, 
                returnType, "dOper");
        checkLinkingOfOperationToCallEvent("dOper()", dOper);
    }
    
    /**
     * This method only uses the "aState" variable.
     */
    private void checkLinkingOfOperationToCallEvent(
            String text, Object operation) {
        Object trans;
        Object trig;
        Object myOp;
        trans = checkGenerated(aState, text, true, false, false, false);
        trig = Model.getFacade().getTrigger(trans);
        assertTrue("Unexpected triggertype found instead of CallEvent for "
                + text, Model.getFacade().isACallEvent(trig));
        myOp = Model.getFacade().getOperation(trig);
        assertTrue("Operation of CallEvent not linked", myOp != null);
        assertTrue("Wrong operation linked to callevent", myOp == operation);
    }

    /**
     * Test if help is correctly provided.
     */
    public void testGetHelp() {
        Object it =
            Model.getStateMachinesFactory().buildInternalTransition(aState);
        TransitionNotationUml notation = new TransitionNotationUml(it);
        String help = notation.getParsingHelp();
        assertTrue("No help at all given", help.length() > 0);
        assertTrue("Parsing help not conform for translation", 
                help.startsWith("parsing."));
    }
    
    /**
     * Test if the notationProvider refuses to instantiate 
     * without showing it the right UML element.
     */
    public void testValidObjectCheck() {
        try {
            new TransitionNotationUml(aState);
            fail("The NotationProvider did not throw for a wrong UML element.");
        } catch (IllegalArgumentException e) {
            /* Everything fine... */
        } 
    }
}
