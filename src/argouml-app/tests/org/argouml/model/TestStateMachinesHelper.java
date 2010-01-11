/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import junit.framework.TestCase;

/**
 * @since Oct 10, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestStateMachinesHelper extends TestCase {

    /**
     * Constructor for TestStateMachinesHelper.
     *
     * @param arg0 is the name of the test case.
     */
    public TestStateMachinesHelper(String arg0) {
	super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        InitializeModel.initializeDefault();
    }

    /**
     * Check if the metamodel name is correct.
     */
    public void testGetMetaModelName() {
	CheckUMLModelHelper.metaModelNameCorrect(
			 Model.getStateMachinesFactory(),
			 TestStateMachinesFactory.getTestableModelElements());
    }

    /**
     * Test creating a stereotype.
     */
    public void testIsValidStereoType() {
	CheckUMLModelHelper.isValidStereoType(
		      Model.getStateMachinesFactory(),
		      TestStateMachinesFactory.getTestableModelElements());
    }
    
    public void testCompositeStates() {
        Object model = Model.getModelManagementFactory().createModel();
        Object classifier = Model.getCoreFactory().buildClass(model);
        Object machine = Model.getStateMachinesFactory().buildStateMachine(classifier);
        Object composite = Model.getStateMachinesFactory().createCompositeState();
        Object state1 = Model.getStateMachinesFactory().createCompositeState();
        Object state2 = Model.getStateMachinesFactory().createCompositeState();
        
        Model.getStateMachinesHelper().addSubvertex(composite, state1);
        Model.getStateMachinesHelper().addSubvertex(composite, state2);
        assertEquals("Wrong number of subvertices ", 2, 
                Model.getFacade().getSubvertices(composite).size());
        Model.getStateMachinesHelper().removeSubvertex(composite, state1);
        assertEquals("Wrong number of subvertices ", 1, 
                Model.getFacade().getSubvertices(composite).size());
        Model.getStateMachinesHelper().setSubvertices(composite, 
                Collections.emptySet());
        assertEquals("Wrong number of subvertices ", 0, 
                Model.getFacade().getSubvertices(composite).size());
        Collection subs = new ArrayList();
        subs.add(state1);
        subs.add(state2);
        Model.getStateMachinesHelper().setSubvertices(composite, subs);
        assertEquals("Wrong number of subvertices ", 2, 
                Model.getFacade().getSubvertices(composite).size());

        Object event = Model.getStateMachinesFactory().createCallEvent();
        Model.getStateMachinesHelper().addDeferrableEvent(composite, event);

        Collection events = Model.getFacade().getDeferrableEvents(composite);
        assertEquals("Wrong number of deferrable events", 1, events.size());
        assertTrue("deferable events doesn't contain our event", events
                .contains(event));

        Model.getUmlFactory().delete(model);

    }
}
