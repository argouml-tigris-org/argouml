// $Id:TestStateBodyNotationUml.java 12485 2007-05-03 05:59:35Z linus $
// Copyright (c) 2004-2007 The Regents of the University of California. All
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

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;

/**
 * Test StateBodyNotationUml (formerly ParserDisplay): parsing state body.
 *
 * @author Michiel
 */
public class TestStateBodyNotationUml extends TestCase {
    private Object aClass;
    private Object aStateMachine;
    private Object aState;

    /**
     * The constructor.
     *
     * @param str the name
     */
    public TestStateBodyNotationUml(String str) {
        super(str);
        InitializeModel.initializeDefault();
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        Object model =
            Model.getModelManagementFactory().createModel();
        Project p = ProjectManager.getManager().getCurrentProject();
        aClass = Model.getCoreFactory().buildClass(model);
        Object returnType = p.getDefaultReturnType();
        Model.getCoreFactory().buildOperation2(aClass, returnType, "myOper");
        aStateMachine =
            Model.getStateMachinesFactory().buildStateMachine(aClass);
        Object top = Model.getFacade().getTop(aStateMachine);
        aState =
            Model.getStateMachinesFactory().buildCompositeState(top);
    }

    /**
     * Test the parseStateBody() method: succesful creation.
     */
    public final void testParseStateBodyCreateIndividually() {
        checkGenerated(aState, "",
                false, false, false, 0, false);
        checkGenerated(aState, "entry/test6",
                true, false, false, 0, false);
        checkGenerated(aState, "exit/test7",
                false, true, false, 0, false);
        checkGenerated(aState, "do/test8",
                false, false, true, 0, false);
        checkGenerated(aState, "test9", //this is not a very useful transition
                false, false, false, 1, false);
    }

    /**
     * Test the parseStateBody() method: succesful creation.
     */
    public final void testParseStateBodyCreateCombined() {
        /* One of everything */
        checkGenerated(aState, "entry/test1\nexit/b\ndo/it\ninternal/activity",
                true, true, true, 1, false);
        /* Upper / Lower case */
        checkGenerated(aState, "\n\nEntry/a\n\nExit/b\n\nDo/it\n\n\n",
                true, true, true, 0, false);
        /* Mixed case */
        checkGenerated(aState, "ENTRY/test2\nexIT/b\nDO/it",
                true, true, true, 0, false);
        /* A complex internal transition */
        checkGenerated(aState, "internal(test3:int=5,b=4.0:float)[g]/"
                + "activity(params, p),act(ee);",
                false, false, false, 1, false);
        /* These are NOT entry/exit/do activities! */
        checkGenerated(aState, "entrys/test4\nexiting/b\ndone/it",
                false, false, false, 3, false);
        /* Long list of internals */
        checkGenerated(aState,
                "a/a\nb/b\nc/c\nd/d\ne/e\nf/f\ng/g\nh/h\ni/i\nj/j\n"
                + "k/a\nl/b\nm/c\nn/d\no/e\np/f\nq/g\nr/h\ns/i\nt/j",
                false, false, false, 20, false);
    }

    /**
     * Test the parseStateBody() method: syntax errors.
     */
    public final void testParseStateBodySyntaxError() {
        checkGenerated(aState, "do[a/b]test10",
                false, false, false, 1, true); // or should the 1 be 0 ?
    }

    /**
     * Test the parseStateBody() method: changing the signature.
     */
    public final void testParseStateBodyRemove() {
        Object st;

        st =
            checkGenerated(aState,
                "entry/test1\nexit/b\ndo/it\ninternal/activity",
                true, true, true, 1, false);
        checkChanged(st, "", // deleting it all
                false, false, false, 0, false);

        st =
            checkGenerated(aState,
                "int1/act1\nexit/test2\nint2/act2\ndo/b\nentry/it",
                true, true, true, 2, false);
        checkChanged(st, // changing the sequence only
                "entry/it\nexit/test2\ndo/b\nint1/act1\nint2/act2",
                true, true, true, 2, false);
    }

    /**
     * Test the parseStateBody() method: changing the signature.
     */
    public final void testParseStateBodyRemoveInternals() {
        Object st;

        st =
            checkGenerated(aState,
                "int1/act1\nint2/act2\nint3/act3\nint4/act4\nint5/act5",
                false, false, false, 5, false);
        checkChanged(st,
                "int1/act1\nint5/act5\nint4/act4",
                false, false, false, 3, false);
        checkChanged(st,
                "int5/act5\nint4/act4",
                false, false, false, 2, false);
        checkChanged(st,
                "int6/act6\nint4/act4",
                false, false, false, 2, false);
    }

    /**
     * Check if the elements are generated or not.
     *
     * @param st          the parent state in which a substate is to be created
     * @param text        the text to be parsed
     * @param entryAction true if an entry action is expected to be created
     * @param exitAction  true if an exit action is expected to be created
     * @param doAction    true if an do activity is expected to be created
     * @param internals   the number of internal transitions
     *                    expected to be created
     * @param exception   true if there is a syntax error
     *                    and an exception is expected
     * @return            the internal state in which elements are created
     */
    private Object checkGenerated(Object st, String text, boolean entryAction,
            boolean exitAction, boolean doAction,
            int internals, boolean exception) {
        Object sst =
            Model.getStateMachinesFactory().buildSimpleState(st);
        checkChanged(sst, text, entryAction, exitAction, doAction,
                internals, exception);
        return sst;
    }

    /**
     * Check if the elements are changed or not.
     *
     * @param sst         the state to test
     * @param text        the text to be parsed
     * @param entryAction true if an entry action is expected to be created
     * @param exitAction  true if an exit action is expected to be created
     * @param doAction    true if an do activity is expected to be created
     * @param internals   the number of internal transitions
     *                    expected to be created
     * @param exception   true if there is a syntax error
     *                    and an exception is expected
     */
    private void checkChanged(Object sst, String text,
            boolean entryAction, boolean exitAction,
            boolean doAction, int internals, boolean exception) {
        try {
            StateBodyNotationUml sbn = new StateBodyNotationUml(sst);
            sbn.parseStateBody(sst, text);
            assertTrue("Expected exception did not happen.", !exception);
        } catch (ParseException e) {
            assertTrue("Unexpected exception for " + text, exception);
        }
        if (entryAction) {
            assertTrue("Entry Action was not generated for " + text,
                    Model.getFacade().getEntry(sst) != null);
        } else {
            assertTrue("Entry Action was generated for " + text,
                    Model.getFacade().getEntry(sst) == null);
        }
        if (exitAction) {
            assertTrue("Exit Action was not generated for " + text,
                    Model.getFacade().getExit(sst) != null);
        } else {
            assertTrue("Exit Action was generated for " + text,
                    Model.getFacade().getExit(sst) == null);
        }
        if (doAction) {
            assertTrue("Do Action was not generated for " + text,
                    Model.getFacade().getDoActivity(sst) != null);
        } else {
            assertTrue("Do Action was generated for " + text,
                    Model.getFacade().getDoActivity(sst) == null);
        }
        Collection c = Model.getFacade().getInternalTransitions(sst);
        assertTrue("Incorrect number of internal transitions (" + c.size()
                + ") found for " + text,
                c.size() == internals);
    }

    /**
     * Test if help is correctly provided.
     */
    public void testGetHelp() {
        StateBodyNotationUml notation = new StateBodyNotationUml(aState);
        String help = notation.getParsingHelp();
        assertTrue("No help at all given", help.length() > 0);
        assertTrue("Parsing help not conform for translation", 
                help.startsWith("parsing."));
    }

}
