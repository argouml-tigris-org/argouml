package uci.uml.critics;

import uci.argo.kernel.*;
import uci.uml.critics.patterns.*;
import uci.uml.critics.java.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;


public class Init {
  // domain independent
  //public static Critic crTooManyDisabled = new CrTooManyDisabled();
  //public static Critic crTooMuchFeedback = new CrTooMuchFeedback();

  // UML specific
  public static Critic crAssocNameConflict = new CrAssocNameConflict();
  public static Critic crAttrNameConflict = new CrAttrNameConflict();
  public static Critic crCircularAssocClass = new CrCircularAssocClass();
  public static Critic crCircularInheritance = new CrCircularInheritance();
  public static Critic crCircularComposition = new CrCircularComposition();
  public static Critic crCrossNamespaceAssoc = new CrCrossNamespaceAssoc();
  public static Critic crDupParamName = new CrDupParamName();
  public static Critic crDupRoleNames = new CrDupRoleNames();
  public static Critic crFinalSubclassed = new CrFinalSubclassed();
  public static Critic crIllegalGeneralization = new CrIllegalGeneralization();
  public static Critic crInterfaceAllPublic = new CrInterfaceAllPublic();
  public static Critic crInterfaceOperOnly = new CrInterfaceOperOnly();
  public static Critic crMultiComposite = new CrMultiComposite();
  public static Critic crMultipleAgg = new CrMultipleAgg();
  public static Critic crNWayAgg = new CrNWayAgg();
  public static Critic crNameConflict = new CrNameConflict();
  public static Critic crNameConflictAC = new CrNameConflictAC();
  public static Critic crNoInstanceVariables = new CrNoInstanceVariables();
  public static Critic crNonAggDataType = new CrNonAggDataType();
  public static Critic crOppEndConflict = new CrOppEndConflict();
  public static Critic crParamTypeNotImported = new CrParamTypeNotImported();
  public static Critic crSignatureConflict = new CrSignatureConflict();
  public static Critic crUselessAbstract = new CrUselessAbstract();
  public static Critic crDisambigClassName = new CrDisambigClassName();
  public static Critic crConflictingComposites = new CrConflictingComposites();

  public static Critic crEmptyPackage = new CrEmptyPackage();
  public static Critic crNoOperations = new CrNoOperations();
  public static Critic crConstructorNeeded = new CrConstructorNeeded();

  public static Critic crNameConfusion = new CrNameConfusion();
  public static Critic crMergeClasses = new CrMergeClasses();

  // from UML 1.1 Semantics spec

  // common coding conventions
  public static Critic
  crUnconventionalOperName = new CrUnconventionalOperName();

  public static Critic
  crUnconventionalAttrName = new CrUnconventionalAttrName(); 

  public static Critic
  crUnconventionalClassName = new CrUnconventionalClassName(); 

  // Java specific
  public static Critic crClassMustBeAbstract = new CrClassMustBeAbstract();
  public static Critic crReservedName = new CrReservedName();
  public static Critic crMultiInherit = new CrMultipleInheritance();

  // code generation
  public static Critic crIllegalName = new CrIllegalName();

  // Pattern specific
  public static Critic crConsiderSingleton = new CrConsiderSingleton();
  public static Critic crSingletonViolated = new CrSingletonViolated();


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
      java.lang.Class assocClassCls = AssociationClass.class;
      java.lang.Class namespaceCls = NamespaceImpl.class;
      java.lang.Class genElementCls = GeneralizableElementImpl.class;
      java.lang.Class genCls = Generalization.class;
      java.lang.Class datatypeCls = DataType.class;

      // needs-more-work: Agency should allow registration by interface
      // useful for IAssociation.
      
      Agency.register(crAssocNameConflict, namespaceCls);
      Agency.register(crAttrNameConflict, classifierCls);
      Agency.register(crCircularAssocClass, assocClassCls);
      Agency.register(crCircularInheritance, genElementCls);
      Agency.register(crCircularComposition, classCls);
      Agency.register(crClassMustBeAbstract, classCls); //needs-more-work
      Agency.register(crCrossNamespaceAssoc, assocClassCls);
      Agency.register(crDupParamName, operCls);
      Agency.register(crDupRoleNames, assocCls);
      Agency.register(crFinalSubclassed, classCls);
      Agency.register(crIllegalGeneralization, genCls);
      Agency.register(crInterfaceAllPublic, interfaceCls);
      Agency.register(crInterfaceOperOnly, interfaceCls);
      Agency.register(crMultiComposite, assocCls);
      Agency.register(crMultipleAgg, assocCls);
      Agency.register(crNWayAgg, assocCls);
      Agency.register(crNameConflict, namespaceCls);
      Agency.register(crNameConflictAC, assocClassCls);
      Agency.register(crNoInstanceVariables, classCls);
      Agency.register(crNoOperations, classCls);
      Agency.register(crConstructorNeeded, classCls); //needs-more-work
      Agency.register(crEmptyPackage, classCls);
      Agency.register(crNonAggDataType, datatypeCls);
      Agency.register(crOppEndConflict, classifierCls);
      Agency.register(crParamTypeNotImported, operCls);
      Agency.register(crSignatureConflict, classifierCls);
      Agency.register(crUselessAbstract, classCls);
      Agency.register(crDisambigClassName, classifierCls);
      Agency.register(crNameConfusion, classifierCls);
      Agency.register(crMergeClasses, classCls);
      Agency.register(crIllegalName, classifierCls);
      Agency.register(crReservedName, modelElementCls);
      Agency.register(crMultiInherit, classifierCls);
      Agency.register(crConflictingComposites, classifierCls);

      Agency.register(crUnconventionalOperName, operCls);
      Agency.register(crUnconventionalAttrName, attrCls);
      Agency.register(crUnconventionalClassName, classifierCls);

      Agency.register(crConsiderSingleton, classCls);
      Agency.register(crSingletonViolated, classCls);


      //Agency.register(crTooMuchFeedback, project);
      //Agency.register(crTooManyDisabled, project);
      //Agency.dumpRegistry();
      
//     }
//     catch (java.lang.ClassNotFoundException e) {
// 	System.out.println("!!!! Error while registering " + e.toString());
//     }
  }

      
} /* end class Init */
