// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import junit.framework.TestCase;

/**
 * Tests for the Undo stuff in the org.argouml.model.
 *
 * @author Linus Tolke
 */
public class TestCoreHelperWithMock extends TestCase {
    /**
     * The Mock ModelImplementation.
     */
    private MockModelImplementation mockMI;

    /**
     * The ModelMemento that we got from the Model.
     */
    private ModelMemento memo;

    /**
     * Constructor.
     *
     * @param arg0 Name of the test case.
     */
    public TestCoreHelperWithMock(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        mockMI = InitializeModel.initializeMock();

        // Must be called after setImplementation.
        mockMI.reset();

        // Registering memento.
        Model.setMementoCreationObserver(new MementoCreationObserver() {
            /*
             * @see org.argouml.model.MementoCreationObserver#mementoCreated(org.argouml.model.ModelMemento)
             */
            public void mementoCreated(ModelMemento memento) {
                memo = memento;
            }
        });
    }

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() {
        mockMI.verify();
    }

    /**
     * Test {@link org.argouml.model.CoreHelper#setAbstract(Object, boolean)}.
     */
    public void testSetAbstract() {
        final Object o = new Object();

        // Record for doing it.
        mockMI.getFacade().isAbstract(o);
        mockMI.getFacadeControl().setReturnValue(false);
        mockMI.getCoreHelper().setAbstract(o, true);
        mockMI.replay();

        // Doing it.
        Model.getCoreHelper().setAbstract(o, true);

        mockMI.verify();

        // Record for undoing it.
        mockMI.reset();
        mockMI.getCoreHelper().setAbstract(o, false);
        mockMI.replay();

        // Undoing it.
        memo.undo();

        // Record for redoing it.
        mockMI.reset();
        mockMI.getCoreHelper().setAbstract(o, true);
        mockMI.replay();

        // Redoing it.
        memo.redo();
    }
}
