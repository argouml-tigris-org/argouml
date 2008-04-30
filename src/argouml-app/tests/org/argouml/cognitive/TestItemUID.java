// $Id$
// Copyright (c) 2002-2007 The Regents of the University of California. All
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
import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.uml.CommentEdge;
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
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
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
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.static_structure.ui.FigLink;
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
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
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
        CommentEdge commentedge = new CommentEdge();
        UMLActivityDiagram actdiag = new UMLActivityDiagram();
        UMLClassDiagram classdiag = new UMLClassDiagram();
        UMLCollaborationDiagram colldiag = new UMLCollaborationDiagram();
        UMLDeploymentDiagram depdiag = new UMLDeploymentDiagram();
        UMLSequenceDiagram seqdiag = new UMLSequenceDiagram();
        UMLStateDiagram statediag = new UMLStateDiagram();
        UMLUseCaseDiagram ucdiag = new UMLUseCaseDiagram();

        FigActionState figactionstate = new FigActionState();

        // FigAssociationRole figassociationrole = new FigAssociationRole();
        FigClassifierRole figclassifierrole = new FigClassifierRole();

        FigComponent figcomponent = new FigComponent();
        FigComponentInstance figcomponentinstance = new FigComponentInstance();
        FigMNode figmnode = new FigMNode();
        FigNodeInstance figmnodeinstance = new FigNodeInstance();
        FigObject figobject = new FigObject();

        FigBranchState figbranchstate = new FigBranchState();
        FigCompositeState figcompositestate = new FigCompositeState();
        FigDeepHistoryState figdeephistorystate = new FigDeepHistoryState();
        FigFinalState figfinalstate = new FigFinalState();
        FigForkState figforkstate = new FigForkState();
        //FigHistoryState fighistorystate = new FigHistoryState();
        FigInitialState figinitialstate = new FigInitialState();
        FigJoinState figjoinstate = new FigJoinState();
        FigShallowHistoryState figshallowhistorystate =
            new FigShallowHistoryState();
        //FigState figstate = new FigState();
        FigTransition figtransition = new FigTransition();

//        FigClass figclass = new FigClass();
        FigEdgeNote figedgenote = new FigEdgeNote();
        FigInterface figinterface = new FigInterface();
        FigLink figlink = new FigLink();
//        FigPackage figpackage = new FigPackage();

        FigActor figactor = new FigActor();
        FigExtend figextend = new FigExtend();
        FigInclude figinclude = new FigInclude();
        FigUseCase figusecase = new FigUseCase();

        checkAssignIDToObject(testmc, true, true);
        checkAssignIDToObject(commentedge, false, true);
        checkAssignIDToObject(actdiag, true, true);
        checkAssignIDToObject(classdiag, true, true);
        checkAssignIDToObject(colldiag, true, true);
        checkAssignIDToObject(depdiag, true, true);
        checkAssignIDToObject(seqdiag, true, true);
        checkAssignIDToObject(statediag, true, true);
        checkAssignIDToObject(ucdiag, true, true);

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

//        checkAssignIDToObject(figclass, true, true);
        checkAssignIDToObject(figedgenote, true, true);
        checkAssignIDToObject(figinterface, true, true);
        checkAssignIDToObject(figlink, true, true);
        //checkAssignIDToObject(figpackage, true, true);

        checkAssignIDToObject(figactor, true, true);
        checkAssignIDToObject(figextend, true, true);
        checkAssignIDToObject(figinclude, true, true);
        checkAssignIDToObject(figusecase, true, true);
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
