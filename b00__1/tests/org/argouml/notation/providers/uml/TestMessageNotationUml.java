// $Id:TestMessageNotationUml.java 12483 2007-05-02 20:20:37Z linus $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import org.argouml.model.InitializeModel;

import org.argouml.model.Model;

/**
 * Test class to test the parsing of a message.<p>
 *
 * Formerly called TestParseMessage. <p>
 *
 * For some reason these tests require the ProjectBrowser loaded when running.
 * For that reason they cannot be run in Headless mode.
 */
public class TestMessageNotationUml extends TestCase {

    /*
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
        Object coll = Model.getCollaborationsFactory().createCollaboration();
        Object inter = Model.getCollaborationsFactory().buildInteraction(coll);

        Object cl1 = Model.getCollaborationsFactory().createClassifierRole();
        Object cl2 = Model.getCollaborationsFactory().createClassifierRole();
        Object cl3 = Model.getCollaborationsFactory().createClassifierRole();
        Object cl4 = Model.getCollaborationsFactory().createClassifierRole();
        Object cl5 = Model.getCollaborationsFactory().createClassifierRole();
        Model.getCoreHelper().setNamespace(cl1, coll);
        Model.getCoreHelper().setNamespace(cl2, coll);
        Model.getCoreHelper().setNamespace(cl3, coll);
        Model.getCoreHelper().setNamespace(cl4, coll);
        Model.getCoreHelper().setNamespace(cl5, coll);

        Object r1to2 =
            Model.getCollaborationsFactory().buildAssociationRole(cl1, cl2);
        Object r2to3 =
            Model.getCollaborationsFactory().buildAssociationRole(cl2, cl3);
        Object r3to4 =
            Model.getCollaborationsFactory().buildAssociationRole(cl3, cl4);
        Object r4to5 =
            Model.getCollaborationsFactory().buildAssociationRole(cl4, cl5);
        Object r3to1 =
            Model.getCollaborationsFactory().buildAssociationRole(cl3, cl1);
        Object r5to3 =
            Model.getCollaborationsFactory().buildAssociationRole(cl5, cl3);

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

        /* TRY PREDECESSORS */
        tryPredecessors(m1, m3, m4, m5, m7);

        /* TRY SOME PREDECESSOR ERRORS */
        trySomePredecessorErrors(m2, m3);
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
        //TODO: Why there is not test here ?
        //It's just for resetting the message body ?
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
        parseMessage(m3, " 1.1.1 : ");
        assertTrue(Model.getFacade().getActivator(m3) == m2);
        assertTrue(Model.getFacade().getPredecessors(m3).size() == 0);

        parseMessage(m3, " / 1..2 : ");
        assertTrue(Model.getFacade().getActivator(m3) == m1);
        assertTrue(Model.getFacade().getPredecessors(m2).size() == 0);
        assertTrue(
            Model.getFacade().getPredecessors(m3).iterator().next() == m2
                && Model.getFacade().getPredecessors(m3).size() == 1);
        parseMessage(m3, "");
        assertTrue(Model.getFacade().getActivator(m3) == m1);
        assertTrue(
            Model.getFacade().getPredecessors(m3).iterator().next() == m2
                && Model.getFacade().getPredecessors(m3).size() == 1);
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
}
