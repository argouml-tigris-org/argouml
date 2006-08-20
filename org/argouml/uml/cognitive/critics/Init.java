// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import org.argouml.cognitive.critics.Agency;
import org.argouml.cognitive.critics.CompoundCritic;
import org.argouml.cognitive.critics.Critic;
import org.argouml.language.java.cognitive.critics.CrMultipleInheritance;
import org.argouml.language.java.cognitive.critics.CrMultipleRealization;
import org.argouml.model.Model;
import org.argouml.pattern.cognitive.critics.CrConsiderSingleton;
import org.argouml.pattern.cognitive.critics.CrSingletonViolatedMissingStaticAttr;
import org.argouml.pattern.cognitive.critics.CrSingletonViolatedOnlyPrivateConstructors;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.UMLDiagram;

/**
 * Registers critics for use in ArgoUML.  This class is called at
 * system startup time. If you add a new critic, you need to add a
 * line here.
 *
 * @author jrobbins
 * @see org.argouml.cognitive.critics.Agency
 */
public class Init {

    // UML specific

    private static Critic crAttrNameConflict = new CrAttrNameConflict();

    private static Critic crOperNameConflict = new CrOperNameConflict();

    private static Critic crCircularAssocClass = new CrCircularAssocClass();

    private static Critic crCircularInheritance = new CrCircularInheritance();

    private static Critic crCircularComposition = new CrCircularComposition();

    private static Critic crCrossNamespaceAssoc = new CrCrossNamespaceAssoc();

    private static Critic crDupParamName = new CrDupParamName();

    private static Critic crDupRoleNames = new CrDupRoleNames();

    private static Critic crFinalSubclassed = new CrFinalSubclassed();

    private static Critic crIllegalGeneralization =
	new CrIllegalGeneralization();

    private static Critic crAlreadyRealizes = new CrAlreadyRealizes();

    private static Critic crInterfaceAllPublic = new CrInterfaceAllPublic();

    private static Critic crInterfaceOperOnly = new CrInterfaceOperOnly();

    private static Critic crMultipleAgg = new CrMultipleAgg();

    private static Critic crNWayAgg = new CrNWayAgg();

    private static Critic crNavFromInterface = new CrNavFromInterface();

    private static Critic crUnnavigableAssoc = new CrUnnavigableAssoc();

    private static Critic crNameConflictAC = new CrNameConflictAC();

    private static Critic crMissingClassName = new CrMissingClassName();

    private static Critic crMissingAttrName = new CrMissingAttrName();

    private static Critic crMissingOperName = new CrMissingOperName();

    private static Critic crMissingStateName = new CrMissingStateName();

    private static Critic crNoInstanceVariables = new CrNoInstanceVariables();

    private static Critic crNoAssociations = new CrNoAssociations();

    private static Critic crNonAggDataType = new CrNonAggDataType();

    private static Critic crOppEndConflict = new CrOppEndConflict();

    private static Critic crUselessAbstract = new CrUselessAbstract();

    private static Critic crUselessInterface = new CrUselessInterface();

    private static Critic crDisambigClassName = new CrDisambigClassName();

    private static Critic crDisambigStateName = new CrDisambigStateName();

    private static Critic crConflictingComposites =
	new CrConflictingComposites();

    private static Critic crTooManyAssoc = new CrTooManyAssoc();

    private static Critic crTooManyAttr = new CrTooManyAttr();

    private static Critic crTooManyOper = new CrTooManyOper();

    private static Critic crTooManyStates = new CrTooManyStates();

    private static Critic crTooManyTransitions = new CrTooManyTransitions();

    private static Critic crTooManyClasses = new CrTooManyClasses();

    private static Critic crNoTransitions = new CrNoTransitions();

    private static Critic crNoIncomingTransitions =
	new CrNoIncomingTransitions();

    private static Critic crNoOutgoingTransitions =
	new CrNoOutgoingTransitions();

    private static Critic crMultipleInitialStates =
	new CrMultipleInitialStates();

    private static Critic crNoInitialState = new CrNoInitialState();

    private static Critic crNoTriggerOrGuard = new CrNoTriggerOrGuard();

    private static Critic crNoGuard = new CrNoGuard();

    private static Critic crInvalidFork = new CrInvalidFork();

    private static Critic crInvalidJoin = new CrInvalidJoin();

    private static Critic crInvalidBranch = new CrInvalidBranch();

    private static Critic crEmptyPackage = new CrEmptyPackage();

    private static Critic crNoOperations = new CrNoOperations();

    private static Critic crConstructorNeeded = new CrConstructorNeeded();

    private static Critic crNameConfusion = new CrNameConfusion();

    private static Critic crMergeClasses = new CrMergeClasses();

    private static Critic crSubclassReference = new CrSubclassReference();

    private static Critic crComponentWithoutNode = new CrComponentWithoutNode();

    private static Critic crCompInstanceWithoutNode =
	new CrCompInstanceWithoutNode();

    private static Critic crClassWithoutComponent =
	new CrClassWithoutComponent();

    private static Critic crInterfaceWithoutComponent =
	new CrInterfaceWithoutComponent();

    private static Critic crObjectWithoutComponent =
	new CrObjectWithoutComponent();

    private static Critic crNodeInsideElement = new CrNodeInsideElement();

    private static Critic crNodeInstanceInsideElement =
	new CrNodeInstanceInsideElement();

    private static Critic crWrongLinkEnds = new CrWrongLinkEnds();

    private static Critic crInstanceWithoutClassifier =
	new CrInstanceWithoutClassifier();

    private static Critic crInvalidHistory =
        new CrInvalidHistory();

    private static Critic crInvalidSynch =
        new CrInvalidSynch();

    private static Critic crInvalidJoinTriggerOrGuard =
        new CrInvalidJoinTriggerOrGuard();

    private static Critic crInvalidForkTriggerOrGuard =
        new CrInvalidForkTriggerOrGuard();

    private static Critic crInvalidPseudoStateTrigger =
        new CrInvalidPseudoStateTrigger();

    private static Critic crMultipleDeepHistoryStates =
        new CrMultipleDeepHistoryStates();

    private static Critic crMultipleShallowHistoryStates =
        new CrMultipleShallowHistoryStates();

    private static Critic crInvalidInitial =
        new CrInvalidInitial();

    private static Critic crForkOutgoingTransition =
        new CrForkOutgoingTransition();

    private static Critic crJoinIncomingTransition =
        new CrJoinIncomingTransition();

    // private static Critic crCallWithoutReturn = new CrCallWithoutReturn();
    // private static Critic crReturnWithoutCall = new CrReturnWithoutCall();
    // private static Critic crSeqInstanceWithoutClassifier =
	// new CrSeqInstanceWithoutClassifier();
    // private static Critic crStimulusWithWrongPosition =
	// new CrStimulusWithWrongPosition();

    // from UML 1.1 Semantics spec

    // common coding conventions
    private static Critic crUnconventionalOperName = 
        new CrUnconventionalOperName();

    private static Critic crUnconventionalAttrName = 
        new CrUnconventionalAttrName();

    private static Critic crUnconventionalClassName = 
        new CrUnconventionalClassName();

    private static Critic crUnconventionalPackName = 
        new CrUnconventionalPackName();

    // Java specific
    private static Critic crClassMustBeAbstract = new CrClassMustBeAbstract();

    private static Critic crReservedName = new CrReservedName();

    private static Critic crMultiInherit = new CrMultipleInheritance();

    private static Critic crMultiRealization = new CrMultipleRealization();

    // code generation
    private static Critic crIllegalName = new CrIllegalName();

    // Pattern specific
    private static Critic crConsiderSingleton = new CrConsiderSingleton();

    private static Critic crSingletonViolatedMSA =
	new CrSingletonViolatedMissingStaticAttr();

    private static Critic crSingletonViolatedOPC =
	new CrSingletonViolatedOnlyPrivateConstructors();

    // Presentation critics
    private static Critic crNodesOverlap = new CrNodesOverlap();

    private static Critic crZeroLengthEdge = new CrZeroLengthEdge();


    // Compound critics
    private static CompoundCritic clsNaming =
	new CompoundCritic(crMissingClassName, crDisambigClassName);

    private static CompoundCritic noTrans1 =
	new CompoundCritic(crNoTransitions, crNoIncomingTransitions);

    private static CompoundCritic noTrans2 =
	new CompoundCritic(crNoTransitions, crNoOutgoingTransitions);

    // only classes with name need a constructor
    private static CompoundCritic crCompoundConstructorNeeded =
        new CompoundCritic(crMissingClassName, crConstructorNeeded);

    /**
     * static initializer, register all appropriate critics.
     */
    public static void init() {
        Object modelCls = Model.getMetaTypes().getModel();
        Object packageCls = Model.getMetaTypes().getPackage();
        Object classCls = Model.getMetaTypes().getUMLClass();
        Object classifierCls = Model.getMetaTypes().getClassifier();
        Object interfaceCls = Model.getMetaTypes().getInterface();
        Object attrCls = Model.getMetaTypes().getAttribute();
        Object paramCls = Model.getMetaTypes().getParameter();
        Object operCls = Model.getMetaTypes().getOperation();
        Object assocCls = Model.getMetaTypes().getAssociation();
        Object assocEndCls = Model.getMetaTypes().getAssociationEnd();
        Object assocClassCls = Model.getMetaTypes().getAssociationClass();
        Object namespaceCls = Model.getMetaTypes().getNamespace();
        Object genElementCls = Model.getMetaTypes().getGeneralizableElement();
        Object genCls = Model.getMetaTypes().getGeneralization();
        Object datatypeCls = Model.getMetaTypes().getDataType();
        Object useCaseCls = Model.getMetaTypes().getUseCase();
        Object actorCls = Model.getMetaTypes().getActor();
        Object stateVertexCls = Model.getMetaTypes().getStateVertex();
        Object stateCls = Model.getMetaTypes().getState();
        Object compositieStateCls = Model.getMetaTypes().getCompositeState();
        Object synchStateCls = Model.getMetaTypes().getSynchState();

	// Class stateDiagramCls   = UMLStateDiagram.class;
	// Class useCaseDiagramCls = UMLUseCaseDiagram.class;

	// TODO: Agency should allow registration by interface
	// useful for MAssociation.
	Agency.register(crAttrNameConflict, classifierCls);
	Agency.register(crOperNameConflict, classifierCls);
	Agency.register(crCircularAssocClass, assocClassCls);
	Agency.register(crCircularInheritance, genElementCls);
	Agency.register(crCircularComposition, classCls);
	Agency.register(crClassMustBeAbstract, classCls);
	Agency.register(crCrossNamespaceAssoc, assocCls);
	Agency.register(crDupParamName, operCls);
	Agency.register(crDupRoleNames, assocCls);
	Agency.register(crFinalSubclassed, classCls);
	Agency.register(crFinalSubclassed, interfaceCls);
	Agency.register(crIllegalGeneralization, genCls);
	Agency.register(crAlreadyRealizes, classCls);
	Agency.register(crInterfaceAllPublic, interfaceCls);
	Agency.register(crInterfaceOperOnly, interfaceCls);
	Agency.register(crMultipleAgg, assocCls);
	Agency.register(crUnnavigableAssoc, assocCls);
	Agency.register(crNWayAgg, assocCls);
	Agency.register(crNavFromInterface, assocCls);
	Agency.register(crNameConflictAC, assocClassCls);
	Agency.register(clsNaming, classCls);
	Agency.register(clsNaming, actorCls);
	Agency.register(clsNaming, useCaseCls);

	// TODO: should be just CrMissingName with a
	// customized description
	Agency.register(crMissingClassName, modelCls);
	Agency.register(crMissingAttrName, attrCls);
	Agency.register(crMissingOperName, operCls);
	Agency.register(crMissingStateName, stateVertexCls);
	Agency.register(crNoInstanceVariables, classCls);
	Agency.register(crNoAssociations, classCls);
	Agency.register(crNoAssociations, actorCls);
	Agency.register(crNoAssociations, useCaseCls);
	Agency.register(crNoOperations, classCls);
        Agency.register(crNoOperations, interfaceCls);
	Agency.register(crCompoundConstructorNeeded, classCls);
	Agency.register(crEmptyPackage, packageCls);
	Agency.register(crNonAggDataType, datatypeCls);
	Agency.register(crUselessAbstract, classCls);
	Agency.register(crUselessInterface, interfaceCls);
	Agency.register(crDisambigStateName, stateCls);
	Agency.register(crNameConfusion, classifierCls);
	Agency.register(crNameConfusion, stateCls);
	Agency.register(crMergeClasses, classCls);
	Agency.register(crSubclassReference, classCls);
	Agency.register(crIllegalName, classCls);
	Agency.register(crIllegalName, interfaceCls);
	Agency.register(crIllegalName, assocCls);
	Agency.register(crIllegalName, operCls);
	Agency.register(crIllegalName, attrCls);
	Agency.register(crIllegalName, paramCls);
	Agency.register(crIllegalName, stateCls);
	Agency.register(crReservedName, classifierCls);
	Agency.register(crReservedName, operCls);
	Agency.register(crReservedName, attrCls);
	Agency.register(crReservedName, stateCls);
	Agency.register(crReservedName, assocCls);
	Agency.register(crMultiInherit, classCls);
	Agency.register(crMultiRealization, interfaceCls);
	Agency.register(crTooManyAssoc, classCls);
	Agency.register(crTooManyAttr, classCls);
	Agency.register(crTooManyOper, classCls);
	Agency.register(crTooManyTransitions, stateVertexCls);
	Agency.register(crTooManyStates, compositieStateCls);
	Class classDiagramCls   = UMLClassDiagram.class;
	Agency.register(crTooManyClasses, classDiagramCls);
	Object pseudostateCls = Model.getMetaTypes().getPseudostate();
	Object transitionCls = Model.getMetaTypes().getTransition();
	Agency.register(noTrans1, stateVertexCls);
	Agency.register(noTrans2, stateVertexCls);
	Agency.register(crMultipleInitialStates, pseudostateCls);
	Agency.register(crNoInitialState, compositieStateCls);
	Agency.register(crNoTriggerOrGuard, transitionCls);
	Agency.register(crInvalidJoin, pseudostateCls);
	Agency.register(crInvalidFork, pseudostateCls);
	Agency.register(crInvalidBranch, pseudostateCls);
	Agency.register(crNoGuard, transitionCls);
	Agency.register(crUnconventionalOperName, operCls);
	Agency.register(crUnconventionalAttrName, attrCls);
	Agency.register(crUnconventionalClassName, classCls);
	Agency.register(crUnconventionalPackName, packageCls);
	Agency.register(crConsiderSingleton, classCls);
	Agency.register(crSingletonViolatedMSA, classCls);
	Agency.register(crSingletonViolatedOPC, classCls);
	Class deploymentDiagramCls = UMLDeploymentDiagram.class;
	Agency.register(crNodeInsideElement, deploymentDiagramCls);
	Agency.register(crNodeInstanceInsideElement, deploymentDiagramCls);
	Agency.register(crComponentWithoutNode, deploymentDiagramCls);
	Agency.register(crCompInstanceWithoutNode, deploymentDiagramCls);
	Agency.register(crClassWithoutComponent, deploymentDiagramCls);
	Agency.register(crInterfaceWithoutComponent, deploymentDiagramCls);
	Agency.register(crObjectWithoutComponent, deploymentDiagramCls);
	Agency.register(crWrongLinkEnds, deploymentDiagramCls);
	Agency.register(crInstanceWithoutClassifier, deploymentDiagramCls);

	Agency.register(crMultipleDeepHistoryStates, pseudostateCls);
	Agency.register(crMultipleShallowHistoryStates, pseudostateCls);
	Agency.register(crInvalidHistory, pseudostateCls);
	Agency.register(crInvalidSynch, synchStateCls);
	Agency.register(crInvalidJoinTriggerOrGuard, transitionCls);
	Agency.register(crInvalidForkTriggerOrGuard, transitionCls);
	Agency.register(crInvalidPseudoStateTrigger, transitionCls);
	Agency.register(crInvalidInitial, pseudostateCls);
	Agency.register(crForkOutgoingTransition, transitionCls);
	Agency.register(crJoinIncomingTransition, transitionCls);

	// Class sequenceDiagramCls = UMLSequenceDiagram.class;
	// Agency.register(crCallWithoutReturn, sequenceDiagramCls);
	// Agency.register(crReturnWithoutCall, sequenceDiagramCls);
	// Agency.register(crLinkWithoutStimulus, sequenceDiagramCls);
	// Agency.register(crSeqInstanceWithoutClassifier, sequenceDiagramCls);
	// Agency.register(crStimulusWithWrongPosition, sequenceDiagramCls);

	// Class nodeCls           = FigNodeModelElement.class;
	Class edgeCls           = FigEdgeModelElement.class;

	Class diagramCls        = UMLDiagram.class;
	Agency.register(crNodesOverlap, diagramCls);
	Agency.register(crZeroLengthEdge, edgeCls);
	Agency.register(crOppEndConflict, assocEndCls);
	Agency.register(new CrMultiComposite(), assocEndCls);
	Agency.register(new CrNameConflict(), namespaceCls);
	Agency.register(crAlreadyRealizes, classCls);
	Agency.register(new CrUtilityViolated(), classifierCls);
	Agency.register(new CrOppEndVsAttr(), classifierCls);
    }

} /* end class Init */
