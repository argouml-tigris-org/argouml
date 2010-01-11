/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

import java.awt.Rectangle;

import junit.framework.TestCase;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.uml.diagram.DiagramSettings;

/**
 * Tests whether Figs in state.ui are clonable,
 * apart from FigStateVertex which is abstract.
 */
public class TestFigClonable extends TestCase {
    // Arbitrary settings - not used used for testing
    private DiagramSettings settings = new DiagramSettings();
    private Rectangle bounds = new Rectangle(10, 10, 20, 20);

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
        ProjectManager.getManager().makeEmptyProject();
        new InitNotation().init();
        new InitNotationUml().init();
    }

    /**
     * Try to clone {@link FigBranchState}.
     */
    public void testBranchStateClonable() {
        Object psc = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(psc, Model.getPseudostateKind().getChoice());
	FigBranchState fig = new FigBranchState(psc, bounds, settings);
	FigBranchState figClone = (FigBranchState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigCompositeState}.
     */
    public void testCompositeStateClonable() {
        Object cs = Model.getStateMachinesFactory().createCompositeState();
	FigCompositeState fig = new FigCompositeState(cs, bounds, settings);
	FigCompositeState figClone = (FigCompositeState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigDeepHistoryState}.
     */
    public void testDeepHistoryStateClonable() {
        Object psd = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(psd, Model.getPseudostateKind().getDeepHistory());
	FigDeepHistoryState fig = new FigDeepHistoryState(psd, bounds, settings);
	FigDeepHistoryState figClone = (FigDeepHistoryState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigFinalState}.
     */
    public void testFinalStateClonable() {
        Object fs = Model.getStateMachinesFactory().createFinalState();
	FigFinalState fig = new FigFinalState(fs, bounds, settings);
	FigFinalState figClone = (FigFinalState) fig.clone();
        assertNotNull(figClone);
    }


    /**
     * Try to clone {@link FigForkState}.
     */
    public void testForkStateClonable() {
        Object psf = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(psf, Model.getPseudostateKind().getFork());
	FigForkState fig = new FigForkState(psf, bounds, settings);
	FigForkState figClone = (FigForkState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigInitialState}.
     */
    public void testInitialStateClonable() {
        Object psi = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(psi, Model.getPseudostateKind().getInitial());
	FigInitialState fig = new FigInitialState(psi, bounds, settings);
	FigInitialState figClone = (FigInitialState) fig.clone();
        assertNotNull(figClone);
    }


    /**
     * Try to clone {@link FigJoinState}.
     */
    public void testJoinStateClonable() {
        Object psj = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(psj, Model.getPseudostateKind().getJoin());
	FigJoinState fig = new FigJoinState(psj, bounds, settings);
	FigJoinState figClone = (FigJoinState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigShallowHistoryState}.
     */
    public void testShallowHistoryStateClonable() {
        Object pss = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(pss, Model.getPseudostateKind().getShallowHistory());
	FigShallowHistoryState fig = new FigShallowHistoryState(pss, bounds, settings);
	FigShallowHistoryState figClone = (FigShallowHistoryState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@linkFigState}.
     */
    public void testSimpleStateClonable() {
        Object ss = Model.getStateMachinesFactory().createSimpleState();
	FigSimpleState fig = new FigSimpleState(ss, bounds, settings);
	FigSimpleState figClone = (FigSimpleState) fig.clone();
        assertNotNull(figClone);
    }


    /**
     * Try to clone {@link FigTransition}.
     */
    public void testTransitionClonable() {
        Object t = Model.getStateMachinesFactory().createTransition();
	FigTransition fig = new FigTransition(t, settings);
	FigTransition figClone = (FigTransition) fig.clone();
        assertNotNull(figClone);
    }


}
