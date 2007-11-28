// $Id$
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

package org.argouml.uml.diagram.state.ui;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;
import org.argouml.profile.InitProfileSubsystem;

/**
 * Tests whether Figs in state.ui are clonable,
 * apart from FigStateVertex which is abstract.
 */
public class TestFigClonable extends TestCase {

    /**
     * The constructor.
     *
     * @param name the test name
     */
    public TestFigClonable(String name) {
        super(name);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
    }

    /**
     * Try to clone {@link FigBranchState}.
     */
    public void testBranchStateClonable() {
	FigBranchState fig = new FigBranchState();
	FigBranchState figClone = (FigBranchState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigCompositeState}.
     */
    public void testCompositeStateClonable() {
	FigCompositeState fig = new FigCompositeState();
	FigCompositeState figClone = (FigCompositeState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigDeepHistoryState}.
     */
    public void testDeepHistoryStateClonable() {
	FigDeepHistoryState fig = new FigDeepHistoryState();
	FigDeepHistoryState figClone = (FigDeepHistoryState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigFinalState}.
     */
    public void testFinalStateClonable() {
	FigFinalState fig = new FigFinalState();
	FigFinalState figClone = (FigFinalState) fig.clone();
        assertNotNull(figClone);
    }


    /**
     * Try to clone {@link FigForkState}.
     */
    public void testForkStateClonable() {
	FigForkState fig = new FigForkState();
	FigForkState figClone = (FigForkState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigInitialState}.
     */
    public void testInitialStateClonable() {
	FigInitialState fig = new FigInitialState();
	FigInitialState figClone = (FigInitialState) fig.clone();
        assertNotNull(figClone);
    }


    /**
     * Try to clone {@link FigJoinState}.
     */
    public void testJoinStateClonable() {
	FigJoinState fig = new FigJoinState();
	FigJoinState figClone = (FigJoinState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigShallowHistoryState}.
     */
    public void testShallowHistoryStateClonable() {
	FigShallowHistoryState fig = new FigShallowHistoryState();
	FigShallowHistoryState figClone = (FigShallowHistoryState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@linkFigState}.
     */
    public void testSimpleStateClonable() {
	FigSimpleState fig = new FigSimpleState();
	FigSimpleState figClone = (FigSimpleState) fig.clone();
        assertNotNull(figClone);
    }


    /**
     * Try to clone {@link FigTransistion}.
     */
    public void testTransitionClonable() {
	FigTransition fig = new FigTransition();
	FigTransition figClone = (FigTransition) fig.clone();
        assertNotNull(figClone);
    }


}
