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

public class TestCrAssocNameConflict extends TestCase {

    private CrNameConflict critic = null;

    private Object c1, c2, c3, c4, ns1, ns2;

    /**
     * Constructor.
     * @param name test case name
     */
    public TestCrAssocNameConflict(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        ns1 = Model.getModelManagementFactory().buildPackage("P1", null);
        ns2 = Model.getModelManagementFactory().buildPackage("P2", null);
        c1 = Model.getCoreFactory().buildClass("A", ns1);
        c2 = Model.getCoreFactory().buildClass("A", ns1);
        c3 = Model.getCoreFactory().buildClass("B", ns1);
        c4 = Model.getCoreFactory().buildClass("A", ns2);
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
        Model.getCoreHelper().setName(c1, "C1");
        Model.getCoreHelper().setName(c2, "C2");
        Model.getCoreHelper().setName(c3, "C3");
        Model.getCoreHelper().setName(c4, "C4");
        Object g1 = Model.getCoreFactory().buildGeneralization(c2, c1);
        Model.getCoreHelper().setName(g1, "gen1");
        Object g2 = Model.getCoreFactory().buildGeneralization(c3, c1);
        Model.getCoreHelper().setName(g2, "gen1");
        assertFalse(critic.predicate2(ns1, null));
        assertTrue(critic.computeOffenders(ns1).size() == 0);

    }
    
    public void testAssociations() {
        CrAssocNameConflict critic = new CrAssocNameConflict();
        Model.getCoreHelper().setNamespace(c4, ns1);
        Object a1, a2, a3;
        Model.getCoreHelper().setName(c1, "C1");
        Model.getCoreHelper().setName(c2, "C2");
        Model.getCoreHelper().setName(c3, "C3");
        Model.getCoreHelper().setName(c4, "C4");
        a1 = Model.getCoreFactory().buildAssociation(c1, c2);
        a2 = Model.getCoreFactory().buildAssociation(c3, c4);
        Model.getCoreHelper().setName(a1, "A1");
        Model.getCoreHelper().setName(a2, "A2");
        
        assertTrue(critic.getAllTypes(a1).contains(c1));
        assertTrue(critic.getAllTypes(a1).contains(c2));
        
        // everything ok
        assertFalse(critic.predicate2(ns1, null));
        assertTrue(critic.computeOffenders(ns1).size() == 0);
        
        // same name, different classes, everything ok
        Model.getCoreHelper().setName(a1, "A2");
        assertFalse(critic.predicate2(ns1, null));
        assertEquals(0, critic.computeOffenders(ns1).size());
        
        // same name, same participants, two offenders
        a3 = Model.getCoreFactory().buildAssociation(c3, c4);
        Model.getCoreHelper().setName(a3, "A2");
        assertEquals(2, critic.getAllTypes(a2).size());
        assertEquals(2, critic.getAllTypes(a3).size());
        assertTrue(critic.getAllTypes(a3).containsAll(critic.getAllTypes(a2)));
        assertTrue(critic.getAllTypes(a2).containsAll(critic.getAllTypes(a3)));

        assertEquals(2, critic.computeOffenders(ns1).size());
    }

}
