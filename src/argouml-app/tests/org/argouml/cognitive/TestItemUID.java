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

// Copyright (c) 2002-2009 The Regents of the University of California. All
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

package org.argouml.cognitive;

// Diagrams
import java.awt.Rectangle;

import junit.framework.TestCase;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.activity.ui.FigActionState;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.FigClassifierRole;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.FigComponent;
import org.argouml.uml.diagram.deployment.ui.FigComponentInstance;
import org.argouml.uml.diagram.deployment.ui.FigMNode;
import org.argouml.uml.diagram.deployment.ui.FigNodeInstance;
import org.argouml.uml.diagram.deployment.ui.FigObject;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.state.ui.FigBranchState;
import org.argouml.uml.diagram.state.ui.FigCompositeState;
import org.argouml.uml.diagram.state.ui.FigDeepHistoryState;
import org.argouml.uml.diagram.state.ui.FigFinalState;
import org.argouml.uml.diagram.state.ui.FigForkState;
import org.argouml.uml.diagram.state.ui.FigInitialState;
import org.argouml.uml.diagram.state.ui.FigJoinState;
import org.argouml.uml.diagram.state.ui.FigShallowHistoryState;
import org.argouml.uml.diagram.state.ui.FigTransition;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.static_structure.ui.FigLink;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.use_case.ui.FigActor;
import org.argouml.uml.diagram.use_case.ui.FigExtend;
import org.argouml.uml.diagram.use_case.ui.FigInclude;
import org.argouml.uml.diagram.use_case.ui.FigUseCase;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.argouml.util.ItemUID;


/**
 * Test the ItemUID class.
 *
 */
public class TestItemUID extends TestCase {

    // Arbitrary settings - not used used for testing
    private DiagramSettings settings = new DiagramSettings();
    private Rectangle bounds = new Rectangle(10, 10, 20, 20);

    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestItemUID(String name) {
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
     * Test the constructors.
     */
    public void testConstructors() {
	String[] strs = {
	    "a", "b", "c", "d", "e",
	};
	ItemUID[] uids = new ItemUID[10];
	ItemUID[] uids2 = new ItemUID[strs.length];
	int i, j;

	for (i = 0; i < uids.length; i++) {
	    uids[i] = new ItemUID();
	    for (j = 0; j < i; j++) {
		assertTrue(
			   "ItemUID() does not generate unique UIDs",
			   !uids[j].toString().equals(uids[i].toString()));
	    }
	}

	for (i = 0; i < strs.length; i++) {
	    uids2[i] = new ItemUID(strs[i]);

	    assertTrue(
		       "ItemUID(sss) does not preserve UIDs",
		       strs[i].equals(uids2[i].toString()));

	    for (j = 0; j < i; j++) {
		assertTrue(
			   "ItemUID(sss) does not generate unique UIDs",
			   !uids2[j].toString().equals(uids2[i].toString()));
	    }
	}
    }

    /**
     * Test assigning IDs to objects.
     */
    public void testAssignIDsToObjects() {

        Object testmc = Model.getCoreFactory().buildClass();
        checkAssignIDToObject(testmc, true, true);
        Model.getUmlFactory().delete(testmc);

        Object namespace = Model.getModelManagementFactory().createModel();
        CommentEdge commentedge = new CommentEdge();
        UMLActivityDiagram actdiag = new UMLActivityDiagram();
        UMLClassDiagram classdiag = new UMLClassDiagram(namespace);
        UMLCollaborationDiagram colldiag = new UMLCollaborationDiagram();
        UMLDeploymentDiagram depdiag = new UMLDeploymentDiagram();
//        UMLSequenceDiagram seqdiag = new UMLSequenceDiagram();
        UMLStateDiagram statediag = new UMLStateDiagram();

        checkAssignIDToObject(commentedge, false, true);
        checkAssignIDToObject(actdiag, true, true);
        checkAssignIDToObject(classdiag, true, true);
        checkAssignIDToObject(colldiag, true, true);
        checkAssignIDToObject(depdiag, true, true);
//        checkAssignIDToObject(seqdiag, true, true);
        checkAssignIDToObject(statediag, true, true);
        
        Model.getUmlFactory().delete(namespace);
        
        Object actionState = Model.getActivityGraphsFactory().createActionState();
        FigActionState figactionstate = new FigActionState(actionState, bounds, settings);

        // FigAssociationRole figassociationrole = new FigAssociationRole();
        Object cr = Model.getCollaborationsFactory().createClassifierRole();
        FigClassifierRole figclassifierrole = new FigClassifierRole(cr, bounds, settings);

        Object c = Model.getCoreFactory().createComponent();
        FigComponent figcomponent = new FigComponent(c, bounds, settings);
        Object ci = Model.getCommonBehaviorFactory().createComponentInstance();
        FigComponentInstance figcomponentinstance = new FigComponentInstance(ci, bounds, settings);
        Object nd = Model.getCoreFactory().createNode();
        FigMNode figmnode = new FigMNode(nd, bounds, settings);
        Object ndi = Model.getCommonBehaviorFactory().createNodeInstance();
        FigNodeInstance figmnodeinstance = new FigNodeInstance(ndi, bounds, settings);
        Object o = Model.getCommonBehaviorFactory().createObject();
        FigObject figobject = new FigObject(o, bounds, settings);

        Object psc = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(psc, Model.getPseudostateKind().getChoice());
        FigBranchState figbranchstate = new FigBranchState(psc, bounds, settings);
        Object cs = Model.getStateMachinesFactory().createCompositeState();
        FigCompositeState figcompositestate = new FigCompositeState(cs, bounds, settings);
        Object psd = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(psd, Model.getPseudostateKind().getDeepHistory());
        FigDeepHistoryState figdeephistorystate = new FigDeepHistoryState(psd, bounds, settings);
        Object fs = Model.getStateMachinesFactory().createFinalState();
        FigFinalState figfinalstate = new FigFinalState(fs, bounds, settings);
        Object psf = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(psf, Model.getPseudostateKind().getFork());
        FigForkState figforkstate = new FigForkState(psf, bounds, settings);
        Object psi = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(psi, Model.getPseudostateKind().getInitial());
        FigInitialState figinitialstate = new FigInitialState(psi, bounds, settings);
        Object psj = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(psj, Model.getPseudostateKind().getJoin());
        FigJoinState figjoinstate = new FigJoinState(psj, bounds, settings);
        Object pss = Model.getStateMachinesFactory().createPseudostate();
        Model.getCoreHelper().setKind(pss, Model.getPseudostateKind().getShallowHistory());
        FigShallowHistoryState figshallowhistorystate =
            new FigShallowHistoryState(pss, bounds, settings);

        Object t = Model.getStateMachinesFactory().createTransition();
        FigTransition figtransition = new FigTransition(t, settings);
        
        Object clazz = Model.getCoreFactory().createClass();
        FigClass figclass = new FigClass(clazz, bounds, settings);
        checkAssignIDToObject(figclass, true, true);
        Model.getUmlFactory().delete(clazz);

        // FigEdgeNote has no UUID
        
        Object iface = Model.getCoreFactory().createInterface();
        FigInterface figinterface = new FigInterface(iface, bounds, settings);
        checkAssignIDToObject(figinterface, true, true);
        Model.getUmlFactory().delete(iface);

        Object link = Model.getCommonBehaviorFactory().createLink();
        FigLink figlink = new FigLink(link, settings);
        checkAssignIDToObject(figlink, true, true);
        Model.getUmlFactory().delete(link);
        
        Object pkg = Model.getModelManagementFactory().createPackage();
        FigPackage figpackage = new FigPackage(pkg, bounds, settings);
        checkAssignIDToObject(figpackage, true, true);
        Model.getUmlFactory().delete(pkg);


        checkAssignIDToObject(figactionstate, true, true);

        //checkAssignIDToObject(figassociationrole, true, true);
        checkAssignIDToObject(figclassifierrole, true, true);

        checkAssignIDToObject(figcomponent, true, true);
        checkAssignIDToObject(figcomponentinstance, true, true);
        checkAssignIDToObject(figmnode, true, true);
        checkAssignIDToObject(figmnodeinstance, true, true);
        checkAssignIDToObject(figobject, true, true);

        checkAssignIDToObject(figbranchstate, true, true);
        checkAssignIDToObject(figcompositestate, true, true);
        checkAssignIDToObject(figdeephistorystate, true, true);
        checkAssignIDToObject(figfinalstate, true, true);
        checkAssignIDToObject(figforkstate, true, true);
        //checkAssignIDToObject(fighistorystate, true, true);
        checkAssignIDToObject(figinitialstate, true, true);
        checkAssignIDToObject(figjoinstate, true, true);
        checkAssignIDToObject(figshallowhistorystate, true, true);
        //checkAssignIDToObject(figstate, true, true);
        checkAssignIDToObject(figtransition, true, true);

    }

    /**
     * Test ID assignment for UseCase diagram and figs
     */
    public void testUseCaseIDs() {
        Object namespace = Model.getModelManagementFactory().createModel();
        UMLUseCaseDiagram ucdiag = new UMLUseCaseDiagram(namespace);
        checkAssignIDToObject(ucdiag, true, true);
        Model.getUmlFactory().delete(namespace);
        
        Object actor = Model.getUseCasesFactory().createActor();
        FigActor figactor = new FigActor(actor, bounds, settings);  
        checkAssignIDToObject(figactor, true, true);
        Model.getUmlFactory().delete(actor);

        Object extend = Model.getUseCasesFactory().createExtend();
        FigExtend figextend = new FigExtend(extend, settings);
        checkAssignIDToObject(figextend, true, true);
        Model.getUmlFactory().delete(extend);
        
        Object include = Model.getUseCasesFactory().createInclude();
        FigInclude figinclude = new FigInclude(include, settings);
        checkAssignIDToObject(figinclude, true, true);
        Model.getUmlFactory().delete(include);

        Object useCase = Model.getUseCasesFactory().createUseCase();
        FigUseCase figusecase = new FigUseCase(useCase, bounds, settings);     
        checkAssignIDToObject(figusecase, true, true);
        Model.getUmlFactory().delete(useCase);
    }

    private void checkAssignIDToObject(
				       Object obj,
				       boolean canCreate,
				       boolean willSucceed) {
	String uid, uid2;
	uid = ItemUID.getIDOfObject(obj, canCreate);
	uid2 = ItemUID.getIDOfObject(obj, false);
	assertTrue(
		   "Assign ID to object "
		   + obj.getClass().getName()
		   + " "
		   + (willSucceed ? "failed" : "succeeded")
		   + " unexpectedly",
		   (uid != null) == willSucceed);
	assertTrue(
		   "Assign ID to object "
		   + obj.getClass().getName()
		   + " yielded different results",
		   (uid == null && uid2 == null)
		   || (uid != null && uid2 != null && uid.equals(uid2)));
    }
}
