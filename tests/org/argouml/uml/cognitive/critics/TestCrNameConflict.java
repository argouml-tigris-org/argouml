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

public class TestCrNameConflict extends TestCase {

    private CrNameConflict critic = null;

    private Object c1, c2, c3, ns1, ns2;

    public TestCrNameConflict(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        ns1 = Model.getModelManagementFactory().buildPackage("P1", null);
        ns2 = Model.getModelManagementFactory().buildPackage("P2", null);
        c1 = Model.getCoreFactory().buildClass("A", ns1);
        c2 = Model.getCoreFactory().buildClass("A", ns1);
        c3 = Model.getCoreFactory().buildClass("B", ns1);
        Model.getCoreFactory().buildClass("A", ns2);
        critic = new CrNameConflict();

    }

    public void testPredicate2() {
        // {A, A} are offenders
        assertTrue(critic.predicate2(ns1, null));
        assertTrue(critic.computeOffenders(ns1).size() == 2);

        // {} no offenders
        assertFalse(critic.predicate2(ns2, null));
        assertTrue(critic.computeOffenders(ns2).size() == 0);

        // {A,A,B,B} are offenders
        Model.getCoreFactory().buildInterface("B", ns1);
        assertTrue(critic.predicate2(ns1, null));
        assertTrue(critic.computeOffenders(ns1).size() == 4);

    }

    public void testGeneralizations() {
        // generalizations are not required to have unique names within a
        // namespace
        Object g1 = Model.getCoreFactory().buildGeneralization(c2, c1);
        Model.getCoreHelper().setName(g1, "gen1");
        Object g2 = Model.getCoreFactory().buildGeneralization(c3, c1);
        Model.getCoreHelper().setName(g1, "gen1");
        assertFalse(critic.predicate2(ns2, null));
        assertTrue(critic.computeOffenders(ns2).size() == 0);

    }

}
