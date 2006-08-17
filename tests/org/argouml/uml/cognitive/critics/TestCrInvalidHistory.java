// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.critics;

import junit.framework.TestCase;

import org.argouml.model.Model;

public class TestCrInvalidHistory extends TestCase {

    private CrInvalidHistory crinvhis = null;

    private Object statemachine;

    private Object compositestate;

    private Object history;

    private Object trans1, trans2;

    private Object state1, state2;

    public TestCrInvalidHistory(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        crinvhis = new CrInvalidHistory();
        statemachine = Model.getStateMachinesFactory().createStateMachine();
        compositestate = Model.getStateMachinesFactory()
                .buildCompositeStateOnStateMachine(statemachine);
        history = Model.getStateMachinesFactory().buildPseudoState(
                compositestate);
        state1 = Model.getStateMachinesFactory().buildSimpleState(
                compositestate);
        state2 = Model.getStateMachinesFactory().buildSimpleState(
                compositestate);

    }

    public void testShallowHistoryKind() {
        Model.getCoreHelper().setKind(history,
                Model.getPseudostateKind().getShallowHistory());
        assertFalse(crinvhis.predicate2(history, null));
        Model.getStateMachinesFactory().buildTransition(history, state1);
        assertFalse(crinvhis.predicate2(history, null));
        Model.getStateMachinesFactory().buildTransition(history, state2);
        assertTrue(crinvhis.predicate2(history, null));
    }

    public void testDeepHistoryKind() {
        Model.getCoreHelper().setKind(history,
                Model.getPseudostateKind().getDeepHistory());
        assertFalse(crinvhis.predicate2(history, null));
        Model.getStateMachinesFactory().buildTransition(history, state1);
        assertFalse(crinvhis.predicate2(history, null));
        Model.getStateMachinesFactory().buildTransition(history, state2);
        assertTrue(crinvhis.predicate2(history, null));
    }

}
