// $Id:TestCrDupParamName.java 12483 2007-05-02 20:20:37Z linus $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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
import org.argouml.model.InitializeModel;

import org.argouml.model.Model;

public class TestCrDupParamName extends TestCase {

    private CrUML cr = null;

    public TestCrDupParamName(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();

        cr = new CrDupParamName();
    }

    public void testPredicate2() {
        Object oper = Model.getCoreFactory().createOperation();
        assertFalse(cr.predicate2(oper, null));
        Object p1 = Model.getCoreFactory().createParameter();
        Model.getCoreHelper().setName(p1, "param1");
        Model.getCoreHelper().setKind(p1,
                Model.getDirectionKind().getInParameter());
        assertFalse(cr.predicate2(oper, null));
        Model.getCoreHelper().addParameter(oper, p1);
        assertFalse(cr.predicate2(oper, null));
        Object p2 = Model.getCoreFactory().createParameter();
        Model.getCoreHelper().setName(p2, "param2");
        Model.getCoreHelper().setKind(p2,
                Model.getDirectionKind().getInParameter());
        Model.getCoreHelper().addParameter(oper, p2);
        assertFalse(cr.predicate2(oper, null));
        Model.getCoreHelper().setName(p2, "param1");
        assertTrue(cr.predicate2(oper, null));

        // test for return parameters. they can ALSO fail!
        Model.getCoreHelper().setKind(p1,
                Model.getDirectionKind().getReturnParameter());
        Model.getCoreHelper().setKind(p2,
                Model.getDirectionKind().getReturnParameter());
        assertTrue(cr.predicate2(oper, null));
    }
}
