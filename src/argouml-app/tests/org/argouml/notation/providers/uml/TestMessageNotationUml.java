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

// Copyright (c) 1996-2009 The Regents of the University of California. All
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
import java.util.Iterator;

import junit.framework.TestCase;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.SDNotationSettings;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Test class to test the parsing of a message.<p>
 *
 * Formerly called TestParseMessage. <p>
 *
 * For some reason these tests require the ProjectBrowser loaded when running.
 * For that reason they cannot be run in Headless mode.
 */
public class TestMessageNotationUml extends TestCase {

    private Object cl1;
    private Object cl2;
    private Object cl3;
    private Object cl4;
    private Object cl5;
    
    private Object r1to2, r2to3, r3to4, r4to5, r3to1, r5to3;
    private Object pack, coll, inter;

    private SDNotationSettings npSettings;

    /**
     * The constructor.
     * 
     * @param str the  name
     * @see junit.framework.TestCase#TestCase(String)
     */
    public TestMessageNotationUml(String str) {
        super(str);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
        assertTrue("Model subsystem init failed.", Model.isInitiated());
        (new InitProfileSubsystem()).init();
        (new InitNotation()).init();
        (new InitNotationUml()).init();
        
        npSettings = new SDNotationSettings();
        ProjectManager.getManager().makeEmptyProject();
    }

    
    @Override
    protected void tearDown() throws Exception {
        ProjectManager.getManager().removeProject(
                ProjectManager.getManager().getCurrentProject());
        ProfileFacade.reset();
        super.tearDown();
    }
    
    private void setupModel1() {
        pack = Model.getModelManagementFactory().buildPackage("p1");
        coll = Model.getCollaborationsFactory().buildCollaboration(pack);
        inter = Model.getCollaborationsFactory().buildInteraction(coll);

        cl1 = Model.getCollaborationsFactory().buildClassifierRole(coll);
        cl2 = Model.getCollaborationsFactory().buildClassifierRole(coll);
        cl3 = Model.getCollaborationsFactory().buildClassifierRole(coll);
        cl4 = Model.getCollaborationsFactory().buildClassifierRole(coll);
        cl5 = Model.getCollaborationsFactory().buildClassifierRole(coll);

        r1to2 =
            Model.getCollaborationsFactory().buildAssociationRole(cl1, cl2);
        r2to3 =
            Model.getCollaborationsFactory().buildAssociationRole(cl2, cl3);
        r3to4 =
            Model.getCollaborationsFactory().buildAssociationRole(cl3, cl4);
        r4to5 =
            Model.getCollaborationsFactory().buildAssociationRole(cl4, cl5);
        r3to1 =
            Model.getCollaborationsFactory().buildAssociationRole(cl3, cl1);
        r5to3 =
            Model.getCollaborationsFactory().buildAssociationRole(cl5, cl3);
    }

    /**
     * Static test of the signature.
     *
     * @throws ParseException if the parsing fails.
     */
    public void compileTestParseMessage() throws ParseException {
        Object m = Model.getCollaborationsFactory().createMessage();
        MessageNotationUml mnu = new MessageNotationUml(m);
        mnu.parseMessage(m, "hej");
    }

    /**
     * Dummy test.
     */
    public void testDummy() { }

    /**
     * Test the parsing of a message.
     *
     * @throws ParseException if the parsing was in error.
     */
    public void testParseMessage() throws ParseException {
        setupModel1();

        /* START TESTING STUFF */

        Object m1 =
             Model.getCollaborationsFactory()
            	.buildMessage(inter, r1to2);
        assertTrue(Model.getFacade().getSender(m1) == cl1);
        assertTrue(Model.getFacade().getReceiver(m1) == cl2);
        assertTrue(Model.getFacade().getInteraction(m1) == inter);
        assertNull(Model.getFacade().getActivator(m1));
        Object action = Model.getFacade().getAction(m1);
        assertTrue(
            action == null || Model.getFacade().getRecurrence(action) == null);
        assertTrue(Model.getFacade().getPredecessors(m1).size() == 0);
        Model.getCoreHelper().setName(m1, "m1");

        Object m2 =
             Model.getCollaborationsFactory()
            	.buildMessage(inter, r2to3);
        assertTrue(Model.getFacade().getSender(m2) == cl2);
        assertTrue(Model.getFacade().getReceiver(m2) == cl3);
        assertTrue(Model.getFacade().getActivator(m2) == m1);
        action = Model.getFacade().getAction(m2);
        assertTrue(
            action == null || Model.getFacade().getRecurrence(action) == null);
        assertTrue(Model.getFacade().getPredecessors(m2).size() == 0);
        Model.getCoreHelper().setName(m2, "m2");

        Object m3 =
             Model.getCollaborationsFactory()
            	.buildMessage(inter, r2to3);
        assertTrue(Model.getFacade().getActivator(m3) == m1);
        assertTrue(
            Model.getFacade().getPredecessors(m3).iterator().next() == m2
                && Model.getFacade().getPredecessors(m3).size() == 1);
        Model.getCoreHelper().setName(m3, "m3");

        /* TRY MOVING IN A SIMPLE MANER */

        trySimpleMoving(m1, m2, m3);

        /* TRY SOME ERRORS */

        trySomeErrors(m3);

        /* TRY SOME MORE COMPLEX MOVING */

        trySomeMoreComplexMoving(m1, m2, m3);

        /* TRY SOME MORE ERRORS */

        checkParseException(m1, " 1.1 : ");
        checkParseException(m2, " 1.1.1 : ");

        /* TRY GUARD/ITERATOR SYNTAX */

        tryGuardAndIteratorSyntax(m3);

        /* TRY SOME GUARD/ITERATOR ERRORS */

        trySomeGuardAndIteratorErrors(m3);

        /* TRY THE ACTIONS */

        tryTheActions(m3);

        /* TRY SOME ACTION ERRORS */

        trySomeActionErrors(m3);

        /* TRY THE PREDECESSORS */

        checkParseException(m3, "1.2.1 / 1.2 :");

        Object m4 =
             Model.getCollaborationsFactory()
            	.buildMessage(inter, r3to4);
        Object m5 =
             Model.getCollaborationsFactory()
            	.buildMessage(inter, r4to5);
        Object m6 =
             Model.getCollaborationsFactory()
            	.buildMessage(inter, r5to3);
        Object m7 =
             Model.getCollaborationsFactory()
            	.buildMessage(inter, r3to1);

        checkParseException(m6, "1.2.2 :");

        /* Try Changing the direction of m7 */
        tryChangingDirection(m1, m6, m7);

        /* TRY PREDECESSORS */
        tryPredecessors(m1, m3, m4, m5, m7);

        /* TRY SOME PREDECESSOR ERRORS */
        trySomePredecessorErrors(m2, m3);
    }

    private void tryChangingDirection(Object m1, Object m6, Object m7)
        throws ParseException {
        parseMessage(m7, "2:");
        assertTrue(Model.getFacade().getSender(m7) == cl1);
        assertTrue(Model.getFacade().getReceiver(m7) == cl3);
        assertNull(Model.getFacade().getActivator(m7));
        assertTrue(Model.getFacade().getPredecessors(m7).iterator().next()
                == m1);
        assertTrue(Model.getFacade().getPredecessors(m7).size() == 1);

        parseMessage(m7, "1.2.1.1.1.1:");
        assertTrue(Model.getFacade().getSender(m7) == cl3);
        assertTrue(Model.getFacade().getReceiver(m7) == cl1);
        assertTrue(Model.getFacade().getActivator(m7) == m6);
        assertTrue(Model.getFacade().getPredecessors(m7).size() == 0);
    }

    /**
     * @param m1 message to be tested
     * @param m3 message to be tested
     * @param m4 message to be tested
     * @param m5 message to be tested
     * @param m7 message to be tested
     * @throws ParseException if the parser found a syntax error
     */
    private void tryPredecessors(Object m1, Object m3, Object m4,
            Object m5, Object m7) throws ParseException {
        Object m;
        Iterator it;
        assertNull(Model.getFacade().getActivator(m1));
        assertTrue(Model.getFacade().getPredecessors(m1).size() == 0);
        assertTrue(Model.getFacade().getActivator(m4) == m3);
        assertTrue(Model.getFacade().getPredecessors(m4).size() == 0);
        assertTrue(Model.getFacade().getActivator(m3) == m1);
        assertTrue(Model.getFacade().getPredecessors(m3).size() == 1);

        parseMessage(m7, "1.2.1 / 1.2.1.1.1.1:");
        assertTrue(Model.getFacade().getPredecessors(m7).iterator().next()
                == m4);
        assertTrue(Model.getFacade().getPredecessors(m7).size() == 1);

        parseMessage(m7, "1.2.1, 1.2.1.1 / 1.2.1.1.1.1:");
        boolean pre1 = false;
        boolean pre2 = false;
        it = Model.getFacade().getPredecessors(m7).iterator();
        m =  it.next();
        if (m == m4) {
            pre1 = true;
        } else if (m == m5) {
            pre2 = true;
        } else {
            assertTrue("Strange message found", false);
        }
        m =  it.next();
        if (m == m4) {
            pre1 = true;
        } else if (m == m5) {
            pre2 = true;
        } else {
            assertTrue("Strange message found", false);
        }
        assertTrue(pre1);
        assertTrue(pre2);
        assertTrue(!it.hasNext());
    }

    /**
     * @param m2 message to be tested
     * @param m3 message to be tested
     */
    private void trySomePredecessorErrors(Object m2, Object m3) {
        checkParseException(m2, "1.2 / 1.1 :");
        checkParseException(m2, "1.2.1 / 1.1 :");
        checkParseException(m3, "1.2.1 / 1.2 :");
    }

    /**
     * @param m3 message to be tested
     */
    private void trySomeActionErrors(Object m3) {
        checkParseException(m3, "1.2 : func() ()");
        checkParseException(m3, "1.2 : func() foo()");
        checkParseException(m3, "1.2 : func(), foo()");
        checkParseException(m3, "1.2 : var() = func()");
        checkParseException(m3, "1.2 : var = func(), foo()");
        checkParseException(m3, "1.2 : func() foo()");
        checkParseException(m3, "1.2 : var = ()");
        checkParseException(m3, "1.2 : var = () foo");
        checkParseException(m3, "1.2 : var = (foo(), bar())");
        checkParseException(m3, "1.2 : func(");
        checkParseException(m3, "1.2 : func(a, b");
    }

    /**
     * @param m3 message to be tested
     * @throws ParseException if the parser found a syntax error
     */
    private void tryTheActions(Object m3) throws ParseException {

        parseMessage(m3, " 1.2 : func() ");
        Object script =
            Model.getFacade().getScript(Model.getFacade().getAction(m3));
        Object body = Model.getFacade().getBody(script);
        assertEquals("func", body);

        parseMessage(m3, " 1.2 ");
        script = Model.getFacade().getScript(Model.getFacade().getAction(m3));
        body = Model.getFacade().getBody(script);
        assertEquals("func", body);

        parseMessage(m3, " 1.2 : ");
        script = Model.getFacade().getScript(Model.getFacade().getAction(m3));
        body = Model.getFacade().getBody(script);
        assertEquals("", body);

        parseMessage(m3, " 1.2 : var := func() ");
        script = Model.getFacade().getScript(Model.getFacade().getAction(m3));
        body = Model.getFacade().getBody(script);
        assertEquals("var := func", body);

        parseMessage(m3, " 1.2 : var = func() ");
        script = Model.getFacade().getScript(Model.getFacade().getAction(m3));
        body = Model.getFacade().getBody(script);
        assertEquals("var := func", body);

        parseMessage(m3, "1.2:var2:=func2()");
        script = Model.getFacade().getScript(Model.getFacade().getAction(m3));
        body = Model.getFacade().getBody(script);
        assertEquals("var2 := func2", body);

        parseMessage(m3, " 1.2 : var, var2, var3 := func() ");
        script = Model.getFacade().getScript(Model.getFacade().getAction(m3));
        body = Model.getFacade().getBody(script);
        assertEquals("var, var2, var3 := func", body);

        parseMessage(m3, "1.2 : load_the_accumulating_taxes");
        script = Model.getFacade().getScript(Model.getFacade().getAction(m3));
        body = Model.getFacade().getBody(script);
        assertEquals("load_the_accumulating_taxes", body);
    }

    /**
     * @param m3 message to be tested
     */
    private void trySomeGuardAndIteratorErrors(Object m3) {
        checkParseException(m3, " [x < 5] 1.2 [x > 6] : ");
        checkParseException(m3, " 1 [x < 5] / 1.2 : ");
        checkParseException(m3, " 1 * / 1.2 : ");
        checkParseException(m3, " 1 // / 1.2 : ");
        checkParseException(m3, " 1 , 2 [x < 5] / 1.2 : ");
        checkParseException(m3, " 1 , 2 * / 1.2 : ");
        checkParseException(m3, " 1 , 2 // / 1.2 : ");
        checkParseException(m3, "/ 1.2 , 2 : ");
        checkParseException(m3, "/1.2,2:");
    }

    /**
     * @param m3 message to be tested
     * @throws ParseException if the parser found a syntax error
     */
    private void tryGuardAndIteratorSyntax(Object m3) throws ParseException {
        parseMessage(m3, " 1.2 [ x < 5 ] : ");
        Object action = Model.getFacade().getAction(m3);
        assertNotNull(action);
        Object recurrence = Model.getFacade().getRecurrence(action);
        Object body = Model.getFacade().getBody(recurrence);
        assertNotNull(recurrence);
        assertTrue("[x < 5]".equals(body));

        parseMessage(m3, " 1.2 * [ i = 1..10 ] : ");
        action = Model.getFacade().getAction(m3);
        recurrence = Model.getFacade().getRecurrence(action);
        body = Model.getFacade().getBody(recurrence);
        assertEquals("*[i = 1..10]", body);

        parseMessage(m3, " 1.2 *// : ");
        action = Model.getFacade().getAction(m3);
        recurrence = Model.getFacade().getRecurrence(action);
        body = Model.getFacade().getBody(recurrence);
        assertEquals("*[i = 1..10]", body);

        parseMessage(m3, " * // [i=1..] 1.2 : ");
        action = Model.getFacade().getAction(m3);
        recurrence = Model.getFacade().getRecurrence(action);
        body = Model.getFacade().getBody(recurrence);
        assertEquals("*//[i=1..]", body);

        parseMessage(m3, " 1.2 : ");
        action = Model.getFacade().getAction(m3);
        recurrence = Model.getFacade().getRecurrence(action);
        body = Model.getFacade().getBody(recurrence);
        assertEquals("*//[i=1..]", body);
    }

    /**
     * @param m1 message to be tested
     * @param m2 message to be tested
     * @param m3 message to be tested
     * @throws ParseException if the parser found a syntax error
     */
    private void trySomeMoreComplexMoving(Object m1, Object m2,
            Object m3) throws ParseException {
        assertTrue(Model.getFacade().getSender(m3) == cl2);
        assertTrue(Model.getFacade().getReceiver(m3) == cl3);

        // This swaps the direction of m3:
        parseMessage(m3, " 1.1.1 : ");
        assertTrue(Model.getFacade().getActivator(m3) == m2);
        assertTrue(Model.getFacade().getPredecessors(m3).size() == 0);
        assertTrue(Model.getFacade().getSender(m3) == cl3);
        assertTrue(Model.getFacade().getReceiver(m3) == cl2);

        // This swaps the direction of m3 back:
        // the colon is obliged, but the / not and the 2nd dot also not
        parseMessage(m3, " / 1..2 : ");
        assertTrue(Model.getFacade().getActivator(m3) == m1);
        assertTrue(Model.getFacade().getPredecessors(m2).size() == 0);
        assertTrue(
            Model.getFacade().getPredecessors(m3).iterator().next() == m2
                && Model.getFacade().getPredecessors(m3).size() == 1);
        assertTrue(Model.getFacade().getSender(m3) == cl2);
        assertTrue(Model.getFacade().getReceiver(m3) == cl3);

        // Notation allows to add or modify things, not remove.
        // Hence, this does nothing:
        parseMessage(m3, "");
        assertTrue(Model.getFacade().getActivator(m3) == m1);
        assertTrue(
            Model.getFacade().getPredecessors(m3).iterator().next() == m2
                && Model.getFacade().getPredecessors(m3).size() == 1);
        assertTrue(Model.getFacade().getSender(m3) == cl2);
        assertTrue(Model.getFacade().getReceiver(m3) == cl3);
    }

    /**
     * @param m3 message to be tested
     */
    private void trySomeErrors(Object m3) {
        checkParseException(m3, " 2.1 : ");
        checkParseException(m3, " 1.2 : 1.2 :");
        checkParseException(m3, " / / 1.2 : ");
    }

    /**
     * @param m1 message to be tested
     * @param m2 message to be tested
     * @param m3 message to be tested
     * @throws ParseException if the parser found a syntax error
     */
    private void trySimpleMoving(Object m1, Object m2, Object m3)
        throws ParseException {
        parseMessage(m3, " \t1.1 : ");
        assertTrue(Model.getFacade().getActivator(m3) == m1);
        assertTrue(Model.getFacade().getPredecessors(m3).size() == 0);
        assertTrue(
            Model.getFacade().getPredecessors(m2).iterator().next() == m3
                && Model.getFacade().getPredecessors(m2).size() == 1);

        parseMessage(m3, " / 1.2\t: ");
        assertTrue(Model.getFacade().getActivator(m3) == m1);
        assertTrue(Model.getFacade().getPredecessors(m2).size() == 0);
        assertTrue(
            Model.getFacade().getPredecessors(m3).iterator().next() == m2
                && Model.getFacade().getPredecessors(m3).size() == 1);
    }

    private void parseMessage(Object m, String s) throws ParseException {
        MessageNotationUml mnu = new MessageNotationUml(m);
        mnu.parseMessage(m, s);
    }

    private void checkParseException(Object m, String s) {
        try {
            MessageNotationUml mnu = new MessageNotationUml(m);
            mnu.parseMessage(m, s);
            assertTrue("Didn't throw for \"" + s + "\" in " + m, false);
        } catch (ParseException pe) {
            // The expected exception is thrown.
        }
    }

    /**
     * Test if the Notation generates the correct text.
     */
    public void testGenerateSequenceNumbers() {
        setupModel1();

        Object m1 =
             Model.getCollaborationsFactory()
                .buildMessage(inter, r1to2);

        /* Both diagram types shall show sequence numbers when requested: */
        npSettings.setShowSequenceNumbers(true);
        checkGenerateCD(m1, "1 : ", npSettings);
        checkGenerateSD(m1, "1 : ", npSettings);
        /* But the collaboration diagram refuses to leave them out: */
        npSettings.setShowSequenceNumbers(false);
        checkGenerateCD(m1, "1 : ", npSettings);
        checkGenerateSD(m1, "", npSettings);
    }

    /**
     * Test if the Notation generates the correct text.<p>
     * 
     * This is the specification tested here:
     * If obtaining the Script of the Action returns an empty string, 
     * then an alternative representation is given:
     * If the action is a CallAction, use the name of its Operation, 
     * and if it is a SendAction, the name of its Event.
     * If also this returns no string, then we display the name of the Message.
     */
    public void testGenerateIssue5150() {
        setupModel1();
        npSettings.setShowSequenceNumbers(false);

        Object m1 = Model.getCollaborationsFactory().buildMessage(inter, r1to2);

        /* If the message has a name, but no action, then show the name: */
        Model.getCoreHelper().setName(m1, "m1-name");
        checkGenerateCD(m1, "1 : m1-name", npSettings);
        checkGenerateSD(m1, "m1-name", npSettings);

        Object clazzA = Model.getCoreFactory().buildClass("A", pack);
        Object clazzB = Model.getCoreFactory().buildClass("B", pack);
        Object oper = 
            Model.getCoreFactory().buildOperation2(clazzA, clazzB, "oper");
        Object action = 
            Model.getCommonBehaviorFactory().buildCallAction(oper, "action");
        Model.getCoreHelper().setNamespace(action, coll);
        Model.getCollaborationsHelper().setAction(m1, action);
        Model.getCommonBehaviorHelper().setOperation(action, oper);

        /* If a message has a name and a named operation,
         * then show the operation: */
        checkGenerateCD(m1, "1 : oper()", npSettings);
        checkGenerateSD(m1, "oper()", npSettings);

        Object aExpr = Model.getDataTypesFactory().createActionExpression(
                "aELanguage", "aEBody");
        Model.getCommonBehaviorHelper().setScript(action, aExpr);
        /* If a message has a name and a named operation and a script,
         * then show the script: */
        /* TODO: Should there really be () here? */
        checkGenerateCD(m1, "1 : aEBody()", npSettings);
        checkGenerateSD(m1, "aEBody()", npSettings);

        aExpr = Model.getDataTypesFactory().createActionExpression(
                "aELanguage2", "");
        Model.getCommonBehaviorHelper().setScript(action, aExpr);
        /* If a message has a name and a named operation and an empty script,
         * then show the operation: */
        checkGenerateCD(m1, "1 : oper()", npSettings);
        checkGenerateSD(m1, "oper()", npSettings);

        Model.getCommonBehaviorHelper().setScript(action, null);
        /* If a message has a name and a named operation 
         * and an expression with no script,
         * then show the operation: */
        checkGenerateCD(m1, "1 : oper()", npSettings);
        checkGenerateSD(m1, "oper()", npSettings);

        aExpr = Model.getDataTypesFactory().createActionExpression(
                "aELanguage", "aEBody");
        Model.getCommonBehaviorHelper().setScript(action, aExpr);
        Object argum1 = Model.getCommonBehaviorFactory().createArgument();
        Object umlExpression = Model.getDataTypesFactory().createExpression(
                    "aOElanguage",
                    "argument-value");
        Model.getCommonBehaviorHelper().setValue(argum1, umlExpression);
        Model.getCommonBehaviorHelper().addActualArgument(action, argum1);
        /* If a message has a name and a named operation 
         * and a named script with parameters,
         * then show the script: */
        /* TODO: Why is there a space before the ( here, 
         * and not in case of the operation name? */
        checkGenerateCD(m1, "1 : aEBody (argument-value)", npSettings);
        checkGenerateSD(m1, "aEBody (argument-value)", npSettings);

        aExpr = Model.getDataTypesFactory().createActionExpression(
                "aELanguage", "");
        Model.getCommonBehaviorHelper().setScript(action, aExpr);
        /* The action still has the argument from above. */
        /* If a message has a name and a named operation 
         * and a script with parameters but without name,
         * then show the operation name: */
        checkGenerateCD(m1, "1 : oper()", npSettings);
        checkGenerateSD(m1, "oper()", npSettings);
    }

    private void checkGenerateCD(Object message, String text, 
            NotationSettings settings) {
        AbstractMessageNotationUml notation = 
            new MessageNotationUml(message);
        assertEquals("Incorrect generation for CD", 
                text, notation.toString(message, settings));
    }

    private void checkGenerateSD(Object message, String text, 
            NotationSettings settings) {
        AbstractMessageNotationUml notation = 
            new SDMessageNotationUml(message); 
        assertEquals("Incorrect generation for SD", 
                text, notation.toString(message, settings));
    }

    /**
     * Test if help is correctly provided.
     */
    public void testGetHelp() {
        Object m = Model.getCollaborationsFactory().createMessage();

        MessageNotationUml notation = new MessageNotationUml(m);
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
        Object collaboration = 
            Model.getCollaborationsFactory().createCollaboration();
        Object interaction = 
            Model.getCollaborationsFactory().buildInteraction(collaboration);
        try {
            new MessageNotationUml(interaction);
            fail("The NotationProvider did not throw for a wrong UML element.");
        } catch (IllegalArgumentException e) {
            /* Everything fine... */
        } 
    }
}
