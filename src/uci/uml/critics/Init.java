// Copyright (c) 1996-99 The Regents of the University of California. All
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

package uci.uml.critics;

import uci.argo.kernel.*;
import uci.uml.critics.java.*;
import uci.uml.critics.patterns.*;
import uci.uml.critics.presentation.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.visual.*;

/** Registers critics for use in Argo/UML.  This class is called at
 *  system startup time. If you add a new critic, you need to add a
 *  line here.
 *
 * @see uci.argo.kernel.Agency */
  
public class Init {
  // domain independent
  //public static Critic crTooManyDisabled = new CrTooManyDisabled();
  //public static Critic crTooMuchFeedback = new CrTooMuchFeedback();

  // UML specific
  public static Critic crAssocNameConflict = new CrAssocNameConflict();
  public static Critic crAttrNameConflict = new CrAttrNameConflict();
  public static Critic crOperNameConflict = new CrOperNameConflict();
  public static Critic crCircularAssocClass = new CrCircularAssocClass();
  public static Critic crCircularInheritance = new CrCircularInheritance();
  public static Critic crCircularComposition = new CrCircularComposition();
  public static Critic crCrossNamespaceAssoc = new CrCrossNamespaceAssoc();
  public static Critic crDupParamName = new CrDupParamName();
  public static Critic crDupRoleNames = new CrDupRoleNames();
  public static Critic crFinalSubclassed = new CrFinalSubclassed();
  public static Critic crIllegalGeneralization = new CrIllegalGeneralization();
  public static Critic crAlreadyRealizes = new CrAlreadyRealizes();
  public static Critic crInterfaceAllPublic = new CrInterfaceAllPublic();
  public static Critic crInterfaceOperOnly = new CrInterfaceOperOnly();
  //public static Critic crMultiComposite = new CrMultiComposite();
  public static Critic crMultipleAgg = new CrMultipleAgg();
  public static Critic crNWayAgg = new CrNWayAgg();
  public static Critic crNavFromInterface = new CrNavFromInterface();
  public static Critic crUnnavigableAssoc = new CrUnnavigableAssoc();
  //public static Critic crNameConflict = new CrNameConflict();
  public static Critic crNameConflictAC = new CrNameConflictAC();
  public static Critic crMissingClassName = new CrMissingClassName();
  public static Critic crMissingAttrName = new CrMissingAttrName();
  public static Critic crMissingOperName = new CrMissingOperName();
  public static Critic crMissingStateName = new CrMissingStateName();
  public static Critic crNoInstanceVariables = new CrNoInstanceVariables();
  public static Critic crNoAssociations = new CrNoAssociations();
  public static Critic crNonAggDataType = new CrNonAggDataType();
  public static Critic crOppEndConflict = new CrOppEndConflict();
  public static Critic crParamTypeNotImported = new CrParamTypeNotImported();
  public static Critic crSignatureConflict = new CrSignatureConflict();
  public static Critic crUselessAbstract = new CrUselessAbstract();
  public static Critic crUselessInterface = new CrUselessInterface();
  public static Critic crDisambigClassName = new CrDisambigClassName();
  public static Critic crDisambigStateName = new CrDisambigStateName();
  public static Critic crConflictingComposites = new CrConflictingComposites();

  public static Critic crTooManyAssoc = new CrTooManyAssoc();
  public static Critic crTooManyAttr = new CrTooManyAttr();
  public static Critic crTooManyOper = new CrTooManyOper();
  public static Critic crTooManyStates = new CrTooManyStates();
  public static Critic crTooManyTransitions = new CrTooManyTransitions();
  public static Critic crTooManyClasses = new CrTooManyClasses();

  public static Critic crNoTransitions = new CrNoTransitions();
  public static Critic crNoIncomingTransitions = new CrNoIncomingTransitions();
  public static Critic crNoOutgoingTransitions = new CrNoOutgoingTransitions();
  public static Critic crMultipleInitialStates = new CrMultipleInitialStates();
  public static Critic crNoInitialState = new CrNoInitialState();
  public static Critic crNoTriggerOrGuard = new CrNoTriggerOrGuard();
  public static Critic crNoGuard = new CrNoGuard();
  public static Critic crInvalidFork = new CrInvalidFork();
  public static Critic crInvalidJoin = new CrInvalidJoin();
  public static Critic crInvalidBranch = new CrInvalidBranch();

  public static Critic crEmptyPackage = new CrEmptyPackage();
  public static Critic crNoOperations = new CrNoOperations();
  public static Critic crConstructorNeeded = new CrConstructorNeeded();

  public static Critic crNameConfusion = new CrNameConfusion();
  public static Critic crMergeClasses = new CrMergeClasses();
  public static Critic crSubclassReference = new CrSubclassReference();

  // from UML 1.1 Semantics spec

  // common coding conventions
  public static Critic
  crUnconventionalOperName = new CrUnconventionalOperName();

  public static Critic
  crUnconventionalAttrName = new CrUnconventionalAttrName(); 

  public static Critic
  crUnconventionalClassName = new CrUnconventionalClassName(); 

  public static Critic
  crUnconventionalPackName = new CrUnconventionalPackName(); 

  // Java specific
  public static Critic crClassMustBeAbstract = new CrClassMustBeAbstract();
  public static Critic crReservedName = new CrReservedName();
  public static Critic crMultiInherit = new CrMultipleInheritance();

  // code generation
  public static Critic crIllegalName = new CrIllegalName();

  // Pattern specific
  public static Critic crConsiderSingleton = new CrConsiderSingleton();
  public static Critic crSingletonViolated = new CrSingletonViolated();

  // Presentation critics
  public static Critic crNodesOverlap = new CrNodesOverlap();
  public static Critic crZeroLengthEdge = new CrZeroLengthEdge();


  // Compound critics
  public static CompoundCritic clsNaming = new CompoundCritic(crMissingClassName,
						crDisambigClassName);
  public static CompoundCritic noTrans1 = new CompoundCritic(crNoTransitions,
					       crNoIncomingTransitions);
  public static CompoundCritic noTrans2 = new CompoundCritic(crNoTransitions,
					       crNoOutgoingTransitions);

  /** static initializer, register all appropriate critics */
  public static void init() {
//     try {
      java.lang.Class modelCls = Model.class;
      java.lang.Class modelElementCls = ModelElementImpl.class;
      java.lang.Class classCls = MMClass.class;
      java.lang.Class classifierCls = Classifier.class;
      java.lang.Class interfaceCls = Interface.class;
      java.lang.Class attrCls = Attribute.class;
      java.lang.Class operCls = Operation.class;
      java.lang.Class iassocCls = IAssociation.class;
      java.lang.Class assocCls = Association.class;
      java.lang.Class assocEndCls = AssociationEnd.class;
      java.lang.Class assocClassCls = AssociationClass.class;
      java.lang.Class namespaceCls = NamespaceImpl.class;
      java.lang.Class genElementCls = GeneralizableElementImpl.class;
      java.lang.Class genCls = Generalization.class;
      java.lang.Class datatypeCls = DataType.class;

      java.lang.Class useCaseCls = UseCase.class;
      java.lang.Class actorCls = Actor.class;

      java.lang.Class stateVertexCls = StateVertex.class;
      java.lang.Class stateCls = State.class;
      java.lang.Class compositieStateCls = CompositeState.class;
      java.lang.Class pseudostateCls = Pseudostate.class;
      java.lang.Class transitionCls = Transition.class;
      //java.lang.Class stateMachineCls = StateMachine.class;

      java.lang.Class diagramCls        = UMLDiagram.class;
      java.lang.Class classDiagramCls   = UMLClassDiagram.class;
      java.lang.Class stateDiagramCls   = UMLStateDiagram.class;
      java.lang.Class useCaseDiagramCls = UMLUseCaseDiagram.class;
      java.lang.Class nodeCls           = FigNodeModelElement.class;
      java.lang.Class edgeCls           = FigEdgeModelElement.class;


      // needs-more-work: Agency should allow registration by interface
      // useful for IAssociation.

      Agency.register(crAssocNameConflict, namespaceCls);
      Agency.register(crAttrNameConflict, classifierCls);
      Agency.register(crOperNameConflict, classifierCls);
      Agency.register(crCircularAssocClass, assocClassCls);
      Agency.register(crCircularInheritance, genElementCls);
      Agency.register(crCircularComposition, classCls);
      Agency.register(crClassMustBeAbstract, classCls); //needs-more-work
      Agency.register(crCrossNamespaceAssoc, assocClassCls);
      Agency.register(crDupParamName, operCls);
      Agency.register(crDupRoleNames, assocCls);
      Agency.register(crFinalSubclassed, classCls);
      Agency.register(crIllegalGeneralization, genCls);
      Agency.register(crAlreadyRealizes, classCls);
      Agency.register(crInterfaceAllPublic, interfaceCls);
      Agency.register(crInterfaceOperOnly, interfaceCls);
      //Agency.register(crMultiComposite, assocEndCls);
      Agency.register(crMultipleAgg, assocCls);
      Agency.register(crUnnavigableAssoc, assocCls);
      Agency.register(crNWayAgg, assocCls);
      Agency.register(crNavFromInterface, assocCls);
      //Agency.register(crNameConflict, namespaceCls);
      Agency.register(crNameConflictAC, assocClassCls);

      // Agency.register(crMissingClassName, classCls);
      // Agency.register(crMissingClassName, actorCls);
      // Agency.register(crMissingClassName, useCaseCls);
      Agency.register(clsNaming, classCls);
      Agency.register(clsNaming, actorCls);
      Agency.register(clsNaming, useCaseCls);

      // needs-more-work: should be just CrMissingName with a
      // customized description
      Agency.register(crMissingClassName, modelCls);
      Agency.register(crMissingAttrName, attrCls);
      Agency.register(crMissingOperName, operCls);
      Agency.register(crMissingStateName, stateCls);

      Agency.register(crNoInstanceVariables, classCls);
      Agency.register(crNoAssociations, classCls);
      Agency.register(crNoAssociations, actorCls);
      Agency.register(crNoAssociations, useCaseCls);
      Agency.register(crNoOperations, classCls);
      Agency.register(crConstructorNeeded, classCls);
      Agency.register(crEmptyPackage, modelCls);
      Agency.register(crNonAggDataType, datatypeCls);
      Agency.register(crOppEndConflict, classifierCls);
      Agency.register(crParamTypeNotImported, operCls);
      Agency.register(crSignatureConflict, classifierCls);
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
      Agency.register(crIllegalName, stateCls);
      Agency.register(crReservedName, classifierCls);
      Agency.register(crReservedName, operCls);
      Agency.register(crReservedName, attrCls);
      Agency.register(crReservedName, stateCls);
      Agency.register(crReservedName, assocCls);
      Agency.register(crMultiInherit, classCls);
      Agency.register(crConflictingComposites, classifierCls);
      Agency.register(crTooManyAssoc, classCls);
      Agency.register(crTooManyAttr, classCls);
      Agency.register(crTooManyOper, classCls);
      Agency.register(crTooManyTransitions, stateVertexCls);
      Agency.register(crTooManyStates, compositieStateCls);
      Agency.register(crTooManyClasses, classDiagramCls);

      // Agency.register(crNoTransitions, stateVertexCls);
      // Agency.register(crNoIncomingTransitions, stateVertexCls);
      // Agency.register(crNoOutgoingTransitions, stateVertexCls);
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
      Agency.register(crUnconventionalPackName, modelCls);

      Agency.register(crConsiderSingleton, classCls);
      Agency.register(crSingletonViolated, classCls);


      Agency.register(crNodesOverlap, diagramCls);
      Agency.register(crZeroLengthEdge, edgeCls);


      
      
      //Agency.register(crTooMuchFeedback, project);
      //Agency.register(crTooManyDisabled, project);
      //Agency.dumpRegistry();
      
//     }
//     catch (java.lang.ClassNotFoundException e) {
// 	System.out.println("!!!! Error while registering " + e.toString());
//     }
  }

      
} /* end class Init */
