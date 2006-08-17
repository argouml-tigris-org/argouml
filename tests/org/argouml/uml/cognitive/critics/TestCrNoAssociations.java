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

import org.argouml.cognitive.Designer;
import org.argouml.model.Model;

public class TestCrNoAssociations extends TestCase {

    private CrNoAssociations crNoAssoc = null;

    Object useCase1;

    Object useCase2;

    Object useCase3;

    Object ns;

    Object actor;

    Designer des;

    public TestCrNoAssociations(String arg0) {
        super(arg0);
    }

    // simply create 3 usecases and an actor in a package with individual names
    protected void setUp() throws Exception {
        super.setUp();
        crNoAssoc = new CrNoAssociations();
        des = Designer.theDesigner();
        ns = Model.getModelManagementFactory().buildPackage("Test", null);
        useCase1 = Model.getUseCasesFactory().createUseCase();
        Model.getCoreHelper().setName(useCase1, "A");
        Model.getCoreHelper().setNamespace(useCase1, ns);
        useCase2 = Model.getUseCasesFactory().createUseCase();

        Model.getCoreHelper().setName(useCase2, "B");
        Model.getCoreHelper().setNamespace(useCase2, ns);

        useCase3 = Model.getUseCasesFactory().createUseCase();
        Model.getCoreHelper().setName(useCase3, "C");
        Model.getCoreHelper().setNamespace(useCase3, ns);

        actor = Model.getUseCasesFactory().createActor();
        Model.getCoreHelper().setName(actor, "Actor");
        Model.getCoreHelper().setNamespace(actor, ns);
    }

    public void testUseCaseNormal() {
        // All use cases and actors require associations
        // hence all predicates evalulate to true
        assertTrue(crNoAssoc.predicate2(useCase1, des));
        assertTrue(crNoAssoc.predicate2(useCase2, des));
        assertTrue(crNoAssoc.predicate2(useCase3, des));
        assertTrue(crNoAssoc.predicate2(actor, des));
    }

    public void testUseCaseAssociation() {
        // build association
        // build association between actor and top use case.
        Model.getCoreFactory().buildAssociation(useCase1, actor);
        assertFalse(crNoAssoc.predicate2(useCase1, des));
        assertTrue(crNoAssoc.predicate2(useCase2, des));
        assertTrue(crNoAssoc.predicate2(useCase3, des));
        assertFalse(crNoAssoc.predicate2(actor, des));
    }

    public void testUseCaseExtend() {
        // build extend
        // these parameters seem to be in correct order
        // add an extend. We do not need an assoc on the extending use case
        Model.getUseCasesFactory().buildExtend(useCase1, useCase3);
        assertTrue(crNoAssoc.predicate2(useCase1, des));
        assertTrue(crNoAssoc.predicate2(useCase2, des));
        assertFalse(crNoAssoc.predicate2(useCase3, des));
        assertTrue(crNoAssoc.predicate2(actor, des));
    }

    public void testUseCaseInclude() {
        // build include
        // it seems that the parameters for build include are just the other way
        // round
        // add an include: we do not need to have associtions on one
        Model.getUseCasesFactory().buildInclude(useCase2, useCase1);
        assertTrue(crNoAssoc.predicate2(useCase1, des));
        assertFalse(crNoAssoc.predicate2(useCase2, des));
        assertTrue(crNoAssoc.predicate2(useCase3, des));
        assertTrue(crNoAssoc.predicate2(actor, des));
    }

}
