// Copyright (c) 1996-2001 The Regents of the University of California. All
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


// File: UMLCognitiveResourceBundle.java
// Classes: UMLCognitiveResourceBundle
// Original Author: Curt Arnold
// $Id$

// 4 Feb 2002: Jeremy Bennett (mail@jeremybennett.com). Fixed description of
// CrConstructorNeeded, CrConsiderSingleton and CrSingletonViolated.

// 15 Feb 2002: Jeremy Bennett (mail@jeremybennett.com). Fixed headline and
// description of CrNavFromInterface.

// 6 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Fixed description for
// CrOperNameConflict.

// 8 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Further fix to
// CrOperNameConflict to make clear return parameters are ignored.

// 12 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Description of
// CrCrossNamespaceAssoc, CrDupRoleName, CrMultipleAgg and CrNWayAgg given in
// more detail.

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Description of
// CrConsiderSingleton and CrSingletonViolated updated now that <<singleton>>
// is built in to ArgoUML. Corrected MState Machines to State Machines.


package org.argouml.i18n;
import java.util.*;

/**
 *   This class is the default member of a resource bundle that
 *   provides strings for UML related critiques and check lists
 *
 *   @author Curt Arnold
 *   @since 0.9
 *   @see java.util.ResourceBundle
 *   @see org.argouml.uml.cognitive.UMLCognitiveResourceBundle_de
 *   @see org.argouml.util.CheckResourceBundle
 *   @see org.argouml.uml.cognitive.critics.CrUML
 */
public class UMLCognitiveResourceBundle extends ListResourceBundle {

    private static final Object[][] _contents = {
	// General stuff
        {"decision.behavior", "Behavior"},
        {"decision.class-selection", "Class Selection"},
        {"decision.code-generation", "Code Generation"},
        {"decision.containment", "Containment"},
        {"decision.design-patterns", "Design Patterns"},
        {"decision.expected-usage", "Expected Usage"},
        {"decision.inheritance", "Inheritance"},
        {"decision.instantiation", "Instantiation"},
        {"decision.methods", "Methods"},
        {"decision.modularity", "Modularity"},
        {"decision.mstate-machines", "State Machines"},
        {"decision.naming", "Naming"},
        {"decision.planned-extensions", "Planned Extensions"},
        {"decision.relationships", "Relationships"},
        {"decision.stereotypes", "Stereotypes"},
        {"decision.storage", "Storage"},
        {"decision.uncategorized", "Uncategorized"},

        {"goal.unspecified", "Unspecified"},

        {"knowledge.completeness", "Completeness"},
        {"knowledge.consistency", "Consistency"},
        {"knowledge.correctness", "Correctness"},
        {"knowledge.designers", "Designer's"},
        {"knowledge.experiential", "Experiential"},
        {"knowledge.optimization", "Optimization"},
        {"knowledge.organizational", "Organizational"},
        {"knowledge.presentation", "Presentation"},
        {"knowledge.semantics", "Semantics"},
        {"knowledge.syntax", "Syntax"},
        {"knowledge.tool", "Tool"},

        {"todopane.label.no-items", " No Items "},
        {"todopane.label.item", " {0} Item "},
        {"todopane.label.items", " {0} Items "},

        {"docpane.label.documentation", "Documentation"},
        {"docpane.label.author", "Author"},
        {"docpane.label.version", "Version"},
        {"docpane.label.since", "Since"},
        {"docpane.label.deprecated", "Deprecated"},
        {"docpane.label.see", "See"},

        {"stylepane.label.bounds", "Bounds"},
        {"stylepane.label.fill", "Fill"},
        {"stylepane.label.no-fill", "No Fill"},
        {"stylepane.label.line", "Line"},
        {"stylepane.label.no-line", "No Line"},
        {"stylepane.label.shadow", "Shadow"},
        {"stylepane.label.no-shadow", "No Shadow"},
        {"stylepane.label.custom", "Custom"},

        {"taggedvaluespane.label.tag", "Tag"},
        {"taggedvaluespane.label.value", "Value"},

        {"button.open", "Open"},
        {"button.back", "Back"},
        {"button.next", "Next"},
        {"button.finish", "Finish"},
        {"button.help", "Help"},

        {"mnemonic.button.back", "B"},
        {"mnemonic.button.next", "N"},
        {"mnemonic.button.finish", "F"},
        {"mnemonic.button.help", "H"},

        {"level.low", "Low"},
        {"level.medium", "Medium"},
        {"level.high", "High"},

        {"message.no-item-selected", "No ToDoItem selected"},
        {"message.branch-critic", "This branch contains \"to do\" items generated by the critic:\n{0}."},
        {"message.branch-decision", "This branch contains \"to do\" items related to the decision:\n{0}."},
        {"message.branch-goal", "This branch contains \"to do\" items related to the goal:\n{0}."},
        {"message.branch-knowledge", "This branch contains \"to do\" items that provide {0} related knowledge."},
        {"message.branch-model", "This branch contains \"to do\" items related to the model element:\n{0}."},
        {"message.branch-priority", "This branch contains {0} priority \"to do\" items."},

	// Critics text
        { "CrAssocNameConflict_head" ,
                "Resolve Association Name Conflict" },
        { "CrAssocNameConflict_desc" ,
                "Every element of a namespace must have a unique name. \n\nClear and unambiguous naming is key to code generation and the understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select the elements and use the Properties tab to change their names." },
        { "CrAttrNameConflict_head" ,
                "Revise Attribute Names to Avoid Conflict" },
        { "CrAttrNameConflict_desc" ,
                "Attributes must have distinct names.  This may be because of an inherited attribute. \n\nClear and unambiguous names are key to code generation and producing an understandable and maintainable design.\n\nTo fix this, use the \"Next>\" button, or manually select the one of the conflicting attributes of this class and change its name." },

        // Updated following bug fix

        { "CrOperNameConflict_head",
          "Change Operation Names or Signatures in <ocl>self</ocl>" },

        { "CrOperNameConflict_desc",
          "Two operations in <ocl>self</ocl> have the exact same " +
          "signature.  Operations must have distinct signatures.  A " +
          "signature is the combination of the operation's name, and " +
          "parameter types (excluding return parameters).\n" +
          "\n" +
          "Where there are conflicting signatures, correct code cannot be " +
          "generated for mainstream OO languages. It also leads to very " +
          "unclear semantics of the design.\n" +
          "\n" +
          "To fix this, select the one of the conflicting operations of " +
          "this class and change its name or parameters." },

        { "CrCircularAssocClass_head" ,
                "Circular Association" },
        { "CrCircularAssocClass_desc" ,
                "AssociationClasses cannot include roles that refer directly back to the AssociationClass." },
        { "CrCircularInheritance_head" ,
                "Remove <ocl>self</ocl>'s Circular Inheritance" },
        { "CrCircularInheritance_desc" ,
                "Inheritances relationships cannot have cycles. \n\nA legal class inheritance hierarchy is needed for code generation and the correctness of the design. \n\nTo fix this, use the \"Next>\" button, or manually select one of the  generalization arrows in the cycle and remove it." },
        { "CrCircularComposition_head" ,
                "Remove Circular Composition" },
        { "CrCircularComposition_desc" ,
                "Composition relationships (black diamonds) cannot have cycles. \n\nA legal aggregation hierarchy is needed for code generation and the correctness of the design. \n\nTo fix this, use the \"Next>\" button, or manually select one of the  associations in the cycle and remove it or change its aggregation to something other than composite." },

        // Updated following bug fix

        { "CrCrossNamespaceAssoc_head" ,
          "Classifier <ocl>self</ocl> not in Namespace of its Association" },

        { "CrCrossNamespaceAssoc_desc" ,
          "Each Class, Interface or other Classifier (<ocl>self</ocl>) " + 
          "involved in an " +
          "Association should be in the Namespace of the Association.\n" +
          "\n" +
          "If this is not the case, then there will be no way for the " +
          "Classes, Interfaces or other Classifiers to name the reference " +
          "to each other using the Association.\n" +
          "\n" +
          "Note that this critic currently does not interpret hierarchical " +
          "namespaces. It will trigger if the final components of the " +
          "namespaces are different, even if they share a common root, and " +
          "this critic should be interpreted in the light of this.\n" +
          "\n" +
          "To fix this, delete the association, and recreate it in a " +
          "diagram, whose namespace includes the classes, interfaces and " +
          "classifiers involved." },

        { "CrDupParamName_head" ,
                "Duplicate Parameter Name" },
        { "CrDupParamName_desc" ,
                "Each parameter of an operation must have a unique name. \n\nClean and unambigous naming is needed for code generation and to achieve clear and maintainable designs.\n\nTo fix this, use the \"Next>\" button, or manually rename one of the parameters to this operation." },

        // Updated following bug fix

        { "CrDupRoleNames_head" ,
          "Duplicate end (role) names for <ocl>self</ocl>" },

        { "CrDupRoleNames_desc" ,
          "Association <ocl>self</ocl> has two (or more) roles with the " +
          "same name.\n" +
          "\n" +
          "Clear and unambiguous naming is key to code generation and the " +
          "understandability and maintainability of the design.\n" +
          "\n" +
          "To fix this manually select <ocl>self</ocl> and use the " +
          "Properties tab to change one or more of the conflicting role " +
          "names." },

        { "CrFinalSubclassed_head" ,
                "Remove leaf keyword or remove subclasses from <ocl>self</ocl>" },
        { "CrFinalSubclassed_desc" ,
                "The keyword 'leaf' indicates that a class is not intended to have subclasses.  This class or interface is marked as 'leaf' and has subclasses.\n\nA well thought-out class inheritance hierarchy that conveys and supports intended extensions is an important part of achieving an understandable and maintainable design.\n\nTo fix this, use the \"Next>\" button, or manually select the class and change its base class, or select the base class and use the properties tab to remove the 'leaf' keyword." },
        { "CrIllegalGeneralization_head" ,
                "Illegal Generalization " },
        { "CrIllegalGeneralization_desc" ,
                "Model elements can only be inherit from others of the same type. \n\nA legal inheritance hierarchy is needed for code generation and the correctness of the design. \n\nTo fix this, use the \"Next>\" button, or manually select the  generalization arrow and remove it." },
        { "CrAlreadyRealizes_head" ,
                "Remove Unneeded Realizes from <ocl>self</ocl>" },
        { "CrAlreadyRealizes_desc" ,
                "The selected class already indirectly realizes Interface {item.extra}.  There is no need to directly realize it again.\n\nSimplifying the design is always a good idea.  You might dismiss this \"to do\" item if you want to make it very explicit that the selected Class realizes this Interface.\n\nTo fix this, select the Realization (dashed line with white triangular arrowhead) and press the \"Delete\" key." },
        { "CrInterfaceAllPublic_head" ,
                "Operations in Interfaces must be public" },
        { "CrInterfaceAllPublic_desc" ,
                "Interfaces are intended to specify the set of operations that other classes must implement.  They must be public. \n\nA well-designed set of interfaces is a good way to define the possible extensions of a class framework. \n\nTo fix this, use the \"Next>\" button, or manually select the operations of the interface and use the Properties tab to make them public." },
        { "CrInterfaceOperOnly_head" ,
                "Interfaces may only have operations" },
        { "CrInterfaceOperOnly_desc" ,
                "Interfaces are intended to specify the set of operations that \nother classes must implement.  They do not implement these \noperations themselves, and cannot have attribues. \n\nA well-designed set of interfaces is a good way to define the \npossible extensions of a class framework. \n\nTo fix this, use the \"Next>\" button, or manually select the  \ninterface and use the Properties tab to remove all attributes." },

        // Fixed to go with updated critic

        { "CrMultipleAgg_head" ,
          "Two aggregate ends (roles) in binary Association" },

        { "CrMultipleAgg_desc" ,
          "Only one end (role) of a binary Association can be aggregate or " +
          "composite.\n" +
          "\n" +
          "Aggregation and composition are used to indicate whole-part " +
          "relationships and by defintion, the \"part\" end cannot be " +
          "aggregate.\n" +
          "\n" +
          "To fix this, select \"Next>\" to use the wizard, or manually " +
          "select the Association and set some of its role aggregations to " +
          "None." },

        // Fixed to go with updated critic

        { "CrNWayAgg_head" ,
          "Aggregate end (role) in 3-way (or more) Association" },

        { "CrNWayAgg_desc" ,
          "Three-way (or more) Associations can not have aggregate ends " +
          "(roles).\n" +
          "\n" +
          "Aggregation and composition are used to indicated whole-part " +
          "relationships, and by definition can only apply to binary " +
          "associations between artifacts.\n" +
          "\n" +
          "To fix this, manually select the Association and set the " +
          "aggregation of each of its ends (roles) to None." },

        // Updated to ensure reference is to the Associaton between Class and
        // Interface.

        { "CrNavFromInterface_head" ,
          "Remove Navigation from Interface via Association <ocl>self</ocl>" },

        { "CrNavFromInterface_desc" ,
          "Associations involving an interface can be not be navigable in " +
          "the direction from the interface.  This is because interfaces " +
          "contain only operation declarations and cannot hold pointers to " +
          "other objects.\n" +
          "\n" +
          "This part of the design should be changed before you can " +
          "generate code from this design.  If you do generate code before " +
          "fixing this problem, the code will not match the design.\n" +
          "\n" +
          "To fix this, select the association and use the \"Properties\" " +
          "tab to select in turn each association end that is NOT " +
          "connected to the interface. Uncheck \"Navigable\" for each of " +
          "these ends.\n" +
          "\n" +
          "The association should then appear with a stick arrowhead " +
          "pointed towards the interface\n" +
          "\n" +
          "NOTE. In an ideal world, ArgoUML would not permit associations " +
          "to be created that were navigable from interfaces.\n" +
          "\n" +
          "WARNING. The current version of ArgoUML has a known defect " +
          "where it creates an association between a class and interface " +
          "that is either navigable in both directions, or navigable only " +
          "from the interface to the class. The fix is to remove the " +
          "navigation from the class end of the association and if " +
          "necessary add navigation to the interface end (navigation " +
          "refers to navigability TOWARDS the end) if this critic is " +
          "triggered." },

        { "CrUnnavigableAssoc_head" ,
                "Make <ocl>self</ocl> Navigable" },
        { "CrUnnavigableAssoc_desc" ,
                "The Association <ocl>self</ocl> is not navigable in any direction. All associations should be navigable at least one way.\n\nSetting the navigablility of associations allows your code to access data by following pointers. \n\nTo fix this, select association \"<ocl>self</ocl>\" in the diagram or navigator panel and click the properties tab.  Then use the checkboxes at the bottom of the properties panel to turn on navigablility." },
        { "CrNameConflictAC_head" ,
                "Role name conflicts with member" },
        { "CrNameConflictAC_desc" ,
                "Association role names of an AssociationClass must not conflict \nwith the names of structural features (e.g., instance variables) \nof the class." },
        { "CrMissingClassName_head" ,
                "Choose a Name" },
        { "CrMissingClassName_desc" ,
                "Every class and interface within a package must have a name. \n\nClear and unambiguous naming is key to code generation and the understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select the class and use the Properties tab to give it a name." },
        { "CrMissingAttrName_head" ,
                "Choose a name" },
        { "CrMissingAttrName_desc" ,
                "Every attribute must have a name. \n\nClear and unambiguous naming is key to code generation and \nthe understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select the \nattribute and use the Properties tab to give it a name." },
        { "CrMissingOperName_head" ,
                "Choose a name" },
        { "CrMissingOperName_desc" ,
                "Every operation must have a name. \n\nClear and unambiguous naming is key to code generation and \nthe understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select the \noperation and use the Properties tab to give it a name." },
        { "CrMissingStateName_head" ,
                "Choose a Name" },
        { "CrMissingStateName_desc" ,
                "Every state within a state machine should have a name. \n\nClear and unambiguous naming is key to code generation and the understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select the state and use the Properties tab to give it a name, or select the state and type a name." },
        { "CrNoInstanceVariables_head" ,
                "Add Instance Variables to <ocl>self</ocl>" },
        { "CrNoInstanceVariables_desc" ,
                "You have not yet specified instance variables for <ocl>self</ocl>. Normally classes have instance variables that store state information for each instance. Classes that provide only static attributes and methods should be stereotyped <<utility>>.\n\nDefining instance variables is needed to complete the information representation part of your design. \n\nTo fix this, press the \"Next>\" button, or add instance variables by double clicking on <ocl>self</ocl> in the navigator pane and using the Create menu to make a new attribute." },
        { "CrNoAssociations_head" ,
                "Add Associations to <ocl>self</ocl>" },
        { "CrNoAssociations_desc" ,
                "You have not yet specified any Associations for <ocl>self</ocl>. Normally classes, actors and use cases are associated with others. \n\nDefining the associations between objects an important part of your design. \n\nTo fix this, press the \"Next>\" button, or add associations manually by clicking on the association tool in the tool bar and dragging from <ocl>self</ocl> to another node." },
        { "CrNonAggDataType_head" ,
                "Wrap DataType" },
        { "CrNonAggDataType_desc" ,
                "DataTypes are not full classes and cannot be associated with classes, unless the DataType is part of a composite (black diamond) aggregation. \n\nGood OO design depends on careful choices about which entities to represent as full objects and which to represent as attributes of objects.\n\nTo fix this, use the \"Next>\" button, or manually replace the DataType with a full class or change the association aggregation to containment by a full class." },
        { "CrOppEndConflict_head" ,
                "Rename Association Roles" },
        { "CrOppEndConflict_desc" ,
                "Two roles of <ocl>self</ocl> have the same name. Roles must have distinct names. \n\nClear and unambiguous names are key to code generation and producing an understandable and maintainable design.\n\nTo fix this, use the \"Next>\" button, or manually select the one of the conflicting roles at the far end of an association from this class and change its name." },
        { "CrParamTypeNotImported_head" ,
                "Import Parameter Type into Class" },
        { "CrParamTypeNotImported_desc" ,
                "The type of each operation parameter must be visible and imported into the class that owns the operation.\n\nImporting classes is needed for code generation. Good modularization of classes into packages is key to an understandable design.\n\nTo fix this, use the \"Next>\" button, or manually add an import to the class that owns this operation." },
        { "CrUselessAbstract_head" ,
                "Define Concrete (Sub)Class" },
        { "CrUselessAbstract_desc" ,
                "<ocl>self</ocl> can never influence the running system because it can never have any instances, and none of its subclasses can have instances either. \n\nTo fix this problem: (1) define concrete subclasses that implement the interface of this class; or (2) make <ocl>self</ocl> or one of its existing subclasses concrete." },
        { "CrUselessInterface_head" ,
                "Define Class to Implement <ocl>self</ocl>" },
        { "CrUselessInterface_desc" ,
                "<ocl>self</ocl> can never be used because no classes implement it.\n\nTo fix this problem, press the \"Next>\" button or manually use the toolbar \"Class\" button to define classes and the \"Realizes\" button to make a relationship from the class to the highlighted interface." },
        { "CrDisambigClassName_head" ,
                "Choose a Unique Name for <ocl>self</ocl>" },
        { "CrDisambigClassName_desc" ,
                "Every class and interface within a package must have a unique name. There are at least two elements in this package named \"<ocl>self</ocl>\".\n\nClear and unambiguous naming is key to code generation and the understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select one of the conflicting classes and use the Properties tab to change their names." },
        { "CrDisambigStateName_head" ,
                "Choose a Unique Name for <ocl>self</ocl>" },
        { "CrDisambigStateName_desc" ,
                "Every state within a state machine must have a unique name. There are at least two states in this machine named \"<ocl>self</ocl>\".\n\nClear and unambiguous naming is key to code generation and the understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select one of the conflicting states and use the \"Properties\" tab to change their names." },
        { "CrConflictingComposites_head" ,
                "Remove Conflicting Composite Associations" },
        { "CrConflictingComposites_desc" ,
                "A composite (black diamond) role of an association indicates that instances of that class contain instances of the associated classes. Since each instance can only be contained in one other object, each object can be the 'part' in at most one is-part-of relationship.\n\nGood OO design depends on building good is-part-of relationships.\n\nTo fix this, use the \"Next>\" button, or manually change one association to have multiplicity to 0..1 or 1..1, or another kind of aggregation (e.g., a white diamond is less strict), or remove one of the associations" },
        { "CrTooManyAssoc_head" ,
                "Reduce Associations on <ocl>self</ocl>" },
        { "CrTooManyAssoc_desc" ,
                "There are too many Associations on class <ocl>self</ocl>.  Whenever one class becomes too central to the design it may become a maintenance bottleneck that must be updated frequently. \n\nDefining the associations between objects is an important part of your design. \n\nTo fix this, press the \"Next>\" button, or remove associations manually by clicking on an association in the navigator pane or diagram and pressing the \"Del\" key." },
        { "CrTooManyAttr_head" ,
                "Reduce Attributes on <ocl>self</ocl>" },
        { "CrTooManyAttr_desc" ,
                "There are too many Attributes on class <ocl>self</ocl>.  Whenever one class becomes too central to the design it may become a maintenance bottleneck that must be updated frequently. \n\nDefining the attributes of objects is an important part of your design. \n\nTo fix this, press the \"Next>\" button, or remove attributes manually by double-clicking on the attribute compartment of the  highlighted class in the diagram and removing the line of text for an attribute." },
        { "CrTooManyOper_head" ,
                "Reduce Operations on <ocl>self</ocl>" },
        { "CrTooManyOper_desc" ,
                "There are too many Operations on class <ocl>self</ocl>.  Whenever one class becomes too central to the design it may become a maintenance bottleneck that must be updated frequently. \n\nDefining the operations of objects is an important part of your design. \n\nTo fix this, press the \"Next>\" button, or remove attributes manually by double-clicking on the operation compartment of the  highlighted class in the diagram and removing the line of text for an operation." },
        { "CrTooManyStates_head" ,
                "Reduce States in machine <ocl>self</ocl>" },
        { "CrTooManyStates_desc" ,
                "There are too many States in <ocl>self</ocl>.  If one state machine has too many states it may become very difficult for humans to understand. \n\nDefining an understandable set of states is an important part of your design. \n\nTo fix this, press the \"Next>\" button, or remove states manually by clicking on a states in the navigator pane or diagram and pressing the \"Del\" key.  Or you can nest states..." },
        { "CrTooManyTransitions_head" ,
                "Reduce Transitions on <ocl>self</ocl>" },
        { "CrTooManyTransitions_desc" ,
                "There are too many Transitions on state <ocl>self</ocl>.  Whenever one state becomes too central to the machine it may become a maintenance bottleneck that must be updated frequently. \n\nDefining the transitions between states is an important part of your design. \n\nTo fix this, press the \"Next>\" button, or remove transitions manually by clicking on a transition in the navigator pane or diagram and pressing the \"Del\" key." },
        { "CrTooManyClasses_head" ,
                "Reduce Classes in diagram <ocl>self</ocl>" },
        { "CrTooManyClasses_desc" ,
                "There are too many classes in <ocl>self</ocl>.  If one class diagram has too many classes it may become very difficult for humans to understand. \n\nDefining an understandable set of class diagrams is an important part of your design. \n\nTo fix this, press the \"Next>\" button, or remove classes manually by clicking on a class in the navigator pane or diagram and pressing the \"Del\" key.  Or you can make a new diagram..." },
        { "CrNoTransitions_head" ,
                "Add Transitions to <ocl>self</ocl>" },
        { "CrNoTransitions_desc" ,
                "State <ocl>self</ocl> has no Incoming or Outgoing transitions. Normally states have both incoming and outgoing transitions. \n\nDefining complete state transitions is needed to complete the behavioral specification part of your design.  \n\nTo fix this, press the \"Next>\" button, or add transitions manually by clicking on transition tool in the tool bar and dragging from another state to <ocl>self</ocl> or from <ocl>self</ocl> to another state." },
        { "CrNoIncomingTransitions_head" ,
                "Add Incoming Transitions to <ocl>self</ocl>" },
        { "CrNoIncomingTransitions_desc" ,
                "State <ocl>self</ocl> has no incoming transitions. Normally states have both incoming and outgoing transitions. \n\nDefining complete state transitions is needed to complete the behavioral specification part of your design. Without incoming transitions, this state can never be reached.\n\nTo fix this, press the \"Next>\" button, or add transitions manually by clicking on transition tool in the tool bar and dragging from another state to <ocl>self</ocl>." },
        { "CrNoOutgoingTransitions_head" ,
                "Add Outgoing Transitions from <ocl>self</ocl>" },
        { "CrNoOutgoingTransitions_desc" ,
                "State <ocl>self</ocl> has no Outgoing transitions. Normally states have both incoming and outgoing transitions. \n\nDefining complete state transitions is needed to complete the behavioral specification part of your design.  Without outgoing transitions, this state is a \"dead\" state that can naver be exited.\n\nTo fix this, press the \"Next>\" button, or add transitions manually by clicking on transition tool in the tool bar and dragging from <ocl>self</ocl> to another state." },
        { "CrMultipleInitialStates_head" ,
                "Remove Extra Initial States" },
        { "CrMultipleInitialStates_desc" ,
                "There are multiple, ambiguous initial states in this machine or composite state. Normally each state machine or composite state has one initial state. \n\nDefining unambiguous states is needed to complete the behavioral specification part of your design.\n\nTo fix this, press the \"Next>\" button, or manually select one of the extra initial states and remove it." },
        { "CrNoInitialState_head" ,
                "Place an Initial State" },
        { "CrNoInitialState_desc" ,
                "There is no initial state in this machine or composite state. Normally each state machine or composite state has one initial state. \n\nDefining unambiguous states is needed to complete the behavioral specification part of your design.\n\nTo fix this, press the \"Next>\" button, or manually select initial state from the tool bar and place it in the diagram." },
        { "CrNoTriggerOrGuard_head" ,
                "Add Trigger or Guard to Transistion" },
        { "CrNoTriggerOrGuard_desc" ,
                "The highlighted Transition is incomplete because it has no trigger or guard condition.  Triggers are events that cause a transition to be taken.  Guard conditions must be true for the transition to be taken.  If only a guard is used, the transition is taken when the condition becomes true.\n\nThis problem must be resolved to complete the state machine.\n\nTo fix this, select the Transition and use the \"Properties\" tab, or select the Transition and type some text of the form:\nTRIGGER [GUARD] / ACTION\nWhere TRIGGER is an event name, GUARD is a boolean expression, and ACTION is an action to be performed when the Transition is taken.  All three parts are optional." },
        { "CrNoGuard_head" ,
                "Add Guard to Transistion" },
        { "CrNoGuard_desc" ,
                "The highlighted Transisition is incomplete because it has no guard condition.  Guard conditions must be true for the transition to be taken.  If only a guard is used, the transition is taken when the condition becomes true.\n\nThis problem must be resolved to complete the state machine.\n\nTo fix this, select the Transition and use the \"Properties\" tab, or select the Transition and type some text of the form:\n[GUARD]\nWhere GUARD is a boolean expression." },
        { "CrInvalidFork_head" ,
                "Change Fork Transitions" },
        { "CrInvalidFork_desc" ,
                "This fork state has an invalid number of transitions. Normally fork states have one incoming and two or more outgoing transitions. \n\nDefining correct state transitions is needed to complete the  behavioral specification part of your design.  \n\nTo fix this, press the \"Next>\" button, or remove transitions  manually by clicking on transition in the diagram and pressing the Delete key." },
        { "CrInvalidJoin_head" ,
                "Change Join Transitions" },
        { "CrInvalidJoin_desc" ,
                "This join state has an invalid number of transitions. Normally join states have two or more incoming and one outgoing transitions. \n\nDefining correct state transitions is needed to complete the  behavioral specification part of your design.  \n\nTo fix this, press the \"Next>\" button, or remove transitions  manually by clicking on transition in the diagram and pressing the Delete key." },
        { "CrInvalidBranch_head" ,
                "Change Branch Transitions" },
        { "CrInvalidBranch_desc" ,
                "This branch state has an invalid number of transitions. Normally branch states have one incoming and two or more outgoing transitions. \n\nDefining correct state transitions is needed to complete the  behavioral specification part of your design.  \n\nTo fix this, press the \"Next>\" button, or remove transitions  manually by clicking on transition in the diagram and pressing the Delete key, or add transitions using the transition tool." },
        { "CrEmptyPackage_head" ,
                "Add Elements to Package <ocl>self</ocl>" },
        { "CrEmptyPackage_desc" ,
                "You have not yet put anything in package <ocl>self</ocl>. Normally packages contain groups of related classes.\n\nDefining and using packages is key to making a maintainable design. \n\nTo fix this, select package <ocl>self</ocl> in the navigator panel and add  diagrams or model elements such as classes or use cases." },
        { "CrNoOperations_head" ,
                "Add Operations to <ocl>self</ocl>" },
        { "CrNoOperations_desc" ,
                "You have not yet specified operations for <ocl>self</ocl>. Normally classes provide operations that define their behavior. \n\nDefining operations is needed to complete the behavioral specification part of your design. \n\nTo fix this, press the \"Next>\" button, or add operations manually by clicking on <ocl>self</ocl> in the navigator pane and using the Create menu to make a new operations." },

        // Updated to reflect use of <<create>> stereotype and lack of a wizard
        // at this stage.

        { "CrConstructorNeeded_head" ,
          "Add Constructor to <ocl>self</ocl>" },

        { "CrConstructorNeeded_desc" ,
          "You have not yet defined a constructor for class " +
          "<ocl>self</ocl>. Constructors initialize new instances such that " +
          "their attributes have valid values.  This class probably needs a " +
          "constructor because not all of its attributes have initial" +
          "values. \n" +
          "\n" +
          "Defining good constructors is key to establishing class " +
          "invariants, and class invariants are a powerful aid in writing " +
          "solid code. \n" +
          "\n" +
          "To fix this, add a constructor manually by clicking on " +
          "<ocl>self</ocl> in the navigator pane and adding an operation " +
          "using the context sensitive pop-up menu in the property tab, or" +
          "select <ocl>self</ocl> where it appears on a class diagram and " +
          "use the \"Add Operation\" tool.\n" +
          "\n" +
          "A constructor is an operation with the stereotype <<create>>.\n" +
          "\n" +
          "By convention (Java, C++) a constructor has the same name as " +
          "the class, is not static, and returns no value (which means you " +
          "must remove the return parameter that ArgoUML adds by default). " +
          "ArgoUML does still require you to stereotype the operation " +
          "<<create>>, even if it has those characteristics."},

        { "CrNameConfusion_head" ,
                "Revise Name to Avoid Confusion" },
        { "CrNameConfusion_desc" ,
                "Names should be clearly distinct from each other. These two names are so close to each other that readers might be confused.\n\nClear and unambiguous naming is key to code generation and the understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select the elements and use the Properties tab to change their names.  Avoid names that differ from other names only in capitalization, or use of underscore characters, or by only one character." },
        { "CrMergeClasses_head" ,
                "Consider Combining Classes (<ocl>self</ocl>)" },
        { "CrMergeClasses_desc" ,
                "The highlighted class, <ocl>self</ocl>, only participates in one association and that association is one-to-one with another class.  Since instances of these two classes must always be created together and destroyed together, combining these classes might simplify your design without loss of any representation power. However, you may find the combined class too large and complex, in which case separating them is usually better.\n\nOrganizing classes to manage complexity of the design is always important, especially when the design is already complex. \n\nTo fix this, click on the \"Next>\" button, or manually add the attribues and operations of the highlighted class to the other class, then remove the highlighted class from the project." },
        { "CrSubclassReference_head" ,
                "Remove Reference to Specific Subclass" },
        { "CrSubclassReference_desc" ,
                "Class <ocl>self</ocl> has a reference to one of it's subclasses. Normally all subclasses should be treated \"equally\" by the superclass.  This allows for addition of new subclasses without modification to the superclass. \n\nDefining the associations between objects is an important part of your design.  Some patterns of associations are easier to maintain than others, depending on the nature of future changes. \n\nTo fix this, press the \"Next>\" button, or remove the association  manually by clicking on it in the diagram and pressing Delete." },
        { "CrComponentWithoutNode_head" ,
                "Components normally are inside nodes" },
        { "CrComponentWithoutNode_desc" ,
                "There are nodes in the diagram. So you have got a real\n deployment-diagram, and in deployment-diagrams components\n normally resides on nodes." },
        { "CrCompInstanceWithoutNode_head" ,
                "ComponentInstances normally are inside nodes" },
        { "CrCompInstanceWithoutNode_desc" ,
                "There are node-instances in the Diagram. So you have got a real\n deployment-diagram, and in deployment-diagrams Component-instances\n normally resides on node-instances." },
        { "CrClassWithoutComponent_head" ,
                "Classes normally are inside components" },
        { "CrClassWithoutComponent_desc" ,
                "In Deployment-diagrams classes are normally inside components" },
        { "CrInterfaceWithoutComponent_head" ,
                "Interfaces normally are inside components" },
        { "CrInterfaceWithoutComponent_desc" ,
                "In Deployment-diagrams interfaces are normally inside components" },
        { "CrObjectWithoutComponent_head" ,
                "Objects normally are inside components" },
        { "CrObjectWithoutComponent_desc" ,
                "In Deployment-diagrams objects are normally inside components or component-instances" },
        { "CrNodeInsideElement_head" ,
                "Nodes normally have no enclosers" },
        { "CrNodeInsideElement_desc" ,
                "Nodes normally are not inside other Elements. They represent\n run-time physical objects with a processing resource, generally having\n at least a memory and often processing capability as well." },
        { "CrNodeInstanceInsideElement_head" ,
                "NodeInstances normally have no enclosers" },
        { "CrNodeInstanceInsideElement_desc" ,
                "NodeInstances normally are not inside other Elements. They represent\n run-time physical objects with a processing resource, generally having\n at least a memory and often processing capability as well." },
        { "CrWrongLinkEnds_head" ,
                "LinkEnds have not the same locations" },
        { "CrWrongLinkEnds_desc" ,
                "In deployment-diagrams objects can reside either on components\n or on component-instances. So it is not possible to have two objects\n connected with a Link, while one object resides on an component and\n an the other object on a component-instance.\n\n\n To fix this remove one object of the two connected objects from its location to an element that has the\n same type as the location of the other object" },
        { "CrInstanceWithoutClassifier_head" ,
                "Set classifier" },
        { "CrInstanceWithoutClassifier_desc" ,
                " Instances have a classifier" },
        { "CrCallWithoutReturn_head" ,
                "Missing return-actions" },
        { "CrCallWithoutReturn_desc" ,
                "Every call- or send-action requires a return-action,\n but this Link has no return-action." },
        { "CrReturnWithoutCall_head" ,
                "Missing call(send)-action" },
        { "CrReturnWithoutCall_desc" ,
                "Every return-action requires a call- or send-action,\n but this Link has no corresponding call- or send-action." },
        { "CrLinkWithoutStimulus_head" ,
                "No Stimuli on these links" },
        { "CrLinkWithoutStimulus_desc" ,
                "In sequence-diagrams a sender-object sends stimuli\nto a receiving object over a link. The link is only the communication-\nconnection, so a stimulus is needed." },
        { "CrSeqInstanceWithoutClassifier_head" ,
                "Set classifier" },
        { "CrSeqInstanceWithoutClassifier_desc" ,
                " Instances have a classifier" },
        { "CrStimulusWithWrongPosition_head" ,
                "Wrong position of these stimuli" },
        { "CrStimulusWithWrongPosition_desc" ,
                "In sequence-diagrams the sender-side of the communication-connections of these\nstimuli are connected at the beginning of an activation. To be a sender an object must\nhave a focus-of-control first." },
        { "CrUnconventionalOperName_head" ,
                "Choose a Better Operation Name" },
        { "CrUnconventionalOperName_desc" ,
                "Normally operation names begin with a lowercase letter. The name '<ocl>self</ocl>' is unconventional because it does not.\n\nFollowing good naming conventions help to improve the understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select <ocl>self</ocl> and use the Properties tab to give it a new name." },
        { "CrUnconventionalAttrName_head" ,
                "Choose a Better Attribute Name" },
        { "CrUnconventionalAttrName_desc" ,
                "Normally attributes begin with a lowercase letter. The name '<ocl>self</ocl>' is unconventional because it does not.\n\nFollowing good naming conventions help to improve the understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select <ocl>self</ocl> and use the Properties tab to give it a different name." },
        { "CrUnconventionalClassName_head" ,
                "Capitalize Class Name <ocl>self</ocl>" },
        { "CrUnconventionalClassName_desc" ,
                "Normally classes begin with a capital letter. The name '<ocl>self</ocl>' is unconventional because it does not begin with a capital.\n\nFollowing good naming conventions help to improve the understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select <ocl>self</ocl> and use the Properties tab to give it a different name." },
        { "CrUnconventionalPackName_head" ,
                "Revise Package Name <ocl>self</ocl>" },
        { "CrUnconventionalPackName_desc" ,
                "Normally package names are written in all lower case with periods used to indicate \"nested\" packages.  The name '<ocl>self</ocl>' is unconventional because it is not all lower case with periods.\n\nFollowing good naming conventions help to improve the understandability and maintainability of the design. \n\nTo fix this, use the \"Next>\" button, or manually select <ocl>self</ocl> and use the Properties tab to give it a different name." },
        { "CrClassMustBeAbstract_head" ,
                "Class <ocl>self</ocl> must be abstract" },
        { "CrClassMustBeAbstract_desc" ,
                "Classes that include or inherit abstract methods from base classes or interfaces must be marked abstract.\n\nDeciding which classes are abstract or concrete is a key part of class hierarchy design.\n\nTo fix this, use the \"Next>\" button, or manually select the class and use the properties tab to add the Abstract keyword, or manually implement each abstract operation that is inherited from a base class or interface." },
        { "CrReservedName_head" ,
                "Change <ocl>self</ocl> to a Non-Reserved Word" },
        { "CrReservedName_desc" ,
                "\"<ocl>self</ocl>\" is a reserved word or very close to one.  The names of model elements must not conflict with reserved words of programming languages or UML.\n\nUsing legal names is needed to generate compilable code. \n\nTo fix this, use the \"Next>\" button, or manually select the highlighted element and use the Properties tab to give it a different name." },
        { "CrMultipleInheritance_head" ,
                "Change Multiple Inheritance to Interfaces" },
        { "CrMultipleInheritance_desc" ,
                "<ocl>self</ocl> has multiple base classes, but Java does not support multiple inheritance.  You must use interfaces instead. \n\nThis change is required before you can generate Java code.\n\nTo fix this, use the \"Next>\" button, or manually (1) remove one of the base classes and then (2) optionally define a new interface with the same method declarations and (3) add it as an interface of <ocl>self</ocl>, and (4) move the method bodies from the old base class down into <ocl>self</ocl>." },
        { "CrMultipleRealization_head" ,
                "Change Multiple Realization in <ocl>self</ocl> to Generalizations" },
        { "CrMultipleRealization_desc" ,
                "<ocl>self</ocl> implements multiple interfaces. Though this is legal in UML this code will not compile in java. Replace the multiple realizations with generalizations if you want to create compilable and error free code." }, 
        { "CrIllegalName_head" ,
                "Choose a Legal Name for <ocl>self</ocl>" },
        { "CrIllegalName_desc" ,
                "The names of model elements must be sequences of letters, numbers, and underscores.  They cannot contain punctuation.\n\nCode generation requires legal names for the resulting code to compile correctly. \n\nTo fix this, use the \"Next>\" button, or manually select the highlighted element and use the Properties tab to give it a different name." },
	
	{ "CrUtilityViolated_head",
	  "Utility Stereotype Violated, instances can be created." },
	{ "CrUtilityViolated_desc",
	  "<ocl>self</ocl> is marked with the <<utility>>; stereotype, " +
	  "but it does not satisfy the constraints imposed on Utilities.\n" + 
	  "It does have instance attributes or variables.\n" +
	  "\n" +
	  "If you no longer wants this class to be a Utility, " +
	  "remove the <<utility>> stereotype by clicking on the class " +
	  "and selecting the blank selection on the stereotype " +
	  "drop-down within the properties tab.\n" },


        // Updated to bring in line with CrSingletonViolated

        { "CrConsiderSingleton_head" ,
          "Consider using Singleton Pattern for <ocl>self</ocl>" },

        { "CrConsiderSingleton_desc" ,
          "<ocl>self</ocl> has no non-static attributes nor any " +
          "associations that are navigable away from instances of this " +
          "class.  This means that every instance of this class will be " +
          "identical to every other instance, since there will be nothing " +
          "about the instances that can differentiate them.\n" +
          "\n" +
          "Under these circumstances you should consider making explicit " +
          "that you have exactly one instance of this class, by using the " +
          "Singleton Pattern. Using the Singleton Pattern can save time and " +
          "memory space. Within ArgoUML; this can be done by using the " +
          "<<singleton>> stereotype on this class.\n" +
          "\n" +
          "If it is not your intent to have a single instance, you should " +
          "define instance variables (i.e. non-static attributes) and/or " +
          "outgoing associations that will represent differences bewteen " +
          "instances.\n" +
          "\n" +
          "Having specified <ocl>self</ocl> as a Singleton, you need to " +
          "define the class so there can only be a single instance. This " +
          "will complete the information representation part of your " +
          "design. To achieve this you need to do the following.\n" +
          "\n" +
          "1. Define a static attribute (a class variable) holding the " +
          "instance. This must therefore have <ocl>self</ocl> as its type.\n" +
          "\n" +
          "2. Provide only private constructors for <ocl>self</ocl> so that " +
          "new instances cannot be made by other code. The creation of the " +
          "single instance could be through a suitable helper operation, " +
          "which invokes this private constructor just once.\n" +
          "\n" +
          "3. Provide at least one constructor to override the default " +
          "constructor, so that the default constructor is not used to " +
          "create multiple instances.\n" +
          "\n" +
          "In the UML 1.3 standard, a constructor is an operation with the " +
          "stereotype <<create>>. Although not strictly standard, ArgoUML " +
          "will also accept <<Create>> as a stereotype for constructors.\n" +
          "\n" +
          "By convention in Java and C++ a constructor has the same name as " +
          "the class, is not static, and returns no value. Note that in " +
          "ArgoUML this means you must remove the return value created by " +
          "default for an operation. ArgoUML; will also accept any " +
          "operation that follows these conventions as a constructor even if" +
          "it is not stereotyped <<create>> or <<Create>>."},

        // Updated to reflect use of <<create>> stereotype for constructors and
        // lack of a wizard at this stage

        { "CrSingletonViolatedMissingStaticAttr_head",
          "Singleton Stereotype Violated, missing static attribute " +
	  "in <ocl>self</ocl>" },

        { "CrSingletonViolatedMissingStaticAttr_desc",
          "<ocl>self</ocl> is marked with the <<singleton>>; stereotype, " +
          "but it does not satisfy the constraints imposed on Singletons." +
          "\n" +
          "It does not have a static attribute (a class variable) to hold " +
          "the instance.\n" +
          "\n" +
          "Whenever you mark a class with a stereotype, the class should " +
          "satisfy all constraints of the stereotype.  This is an important " +
          "part of making a self-consistent and understandable design. " +
          "Using the Singleton Pattern can save time and memory space.\n" +
          "\n" +
          "If you no longer want this class to be a Singleton, remove the " +
          "<<singleton>> stereotype by clicking on the " +
          "class and selecting the blank selection on the stereotype " +
          "drop-down within the properties tab.\n" },

        { "CrSingletonViolatedOnlyPrivateConstructors_head",
          "Singleton Stereotype Violated, has non-private constructor, "
	  + "in <ocl>self</ocl>" },

        { "CrSingletonViolatedOnlyPrivateConstructors_desc",
          "<ocl>self</ocl> is marked with the <<singleton>>; stereotype, " +
          "but it does not satisfy the constraints imposed on Singletons." +
          "\n" +
          "It must have only private constructors so that new instances " +
          "cannot be made by other code.\n" +
          "Whenever you mark a class with a stereotype, the class should " +
          "satisfy all constraints of the stereotype.  This is an important " +
          "part of making a self-consistent and understandable design. " +
          "Using the Singleton Pattern can save time and memory space.\n" +
          "\n" +
          "If you no longer want this class to be a Singleton, remove the " +
          "<<singleton>> stereotype by clicking on the " +
          "class and selecting the blank selection on the stereotype " +
          "drop-down within the properties tab.\n" },

        { "CrNodesOverlap_head" ,
                "Clean Up Diagram <ocl>self</ocl>" },
        { "CrNodesOverlap_desc" ,
                "Some of the objects in this diagram overlap and obscure each other. This may hide important information and make it difficult for humans to understand. A neat appearance may also make your diagrams more influencial on other designers, implementors, and decision makers.\n\nConstructing an understandable set of class diagrams is an important part of your design. \n\nTo fix this, move the highlighted nodes in the diagram." },
        { "CrZeroLengthEdge_head" ,
                "Make Edge More Visible" },
        { "CrZeroLengthEdge_desc" ,
                "This edge is too small to see easily. This may hide important information and make it difficult for humans to understand. A neat appearance may also make your diagrams more influencial on other designers, implementors, and decision makers.\n\nConstructing an understandable set of diagrams is an important part of your design. \n\nTo fix this, move one or more nodes so that the highlighted edges will be longer, or click in the center of the edge and drag to make a new vertex." },
        //
        //   these phrases should be localized here
        //      not in the following check list section
        { "Naming", "Naming" },
        { "Encoding", "Encoding" },
        { "Value", "Value" },
        { "Location", "Location" },
        { "Updates", "Updates" },
        { "General", "General" },
        { "Actions" , "Actions" },
        { "Transitions", "Transitions" },
        { "Structure", "Structure" },
        { "Trigger", "Trigger" },
        { "MGuard", "MGuard" },
        //
        //   The following blocks define the UML related
        //      Checklists.  The key is the name of
        //      the non-deprecated implementing class,
        //      the value is an array of categories which
        //      are each an array of Strings.  The first
        //      string in each category is the name of the
        //      category and should not be localized here
        //      but should be in the immediate preceeding
        //      section
        //
        { "ChClass",
            new String[][] {
                new String[] { "Naming",
                  "Does the name '<ocl>self</ocl>' clearly describe the class?",
                  "Is '<ocl>self</ocl>' a noun or noun phrase?",
                  "Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?"
                },
                new String[] { "Encoding",
                  "Should <ocl>self</ocl> be its own class or a simple attribute of another class?",
                  "Does <ocl>self</ocl> do exactly one thing and do it well?",
                  "Could <ocl>self</ocl> be broken down into two or more classes?"
                },
                new String[] { "Value",
                  "Do all attributes of <ocl>self</ocl> start with meaningful values?",
                  "Could you write an invariant for this class?",
                  "Do all constructors establish the class invariant?",
                  "Do all operations maintain the class invariant?"
                },
                new String[] { "Location",
                  "Could <ocl>self</ocl> be defined in a different location in the class hierarchy?",
                  "Have you planned to have subclasses of <ocl>self</ocl>?",
                  "Could <ocl>self</ocl> be eliminated from the model?",
                  "Is there another class in the model that should be revised or eliminated because it serves the same purpose as <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "For what reasons will an instance of <ocl>self</ocl> be updated?",
                  "Is there some other object that must be updated whenever <ocl>self</ocl> is updated?"
                }
            }
        },
        { "ChAttribute",
            new String[][] {
                new String[] { "Naming",
                  "Does the name '<ocl>self</ocl>' clearly describe the attribute?",
                  "Is '<ocl>self</ocl>' a noun or noun phrase?",
                  "Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?"
                },
                new String[] { "Encoding",
                  "Is the type <ocl>self.type</ocl> too restrictive to represent all possible values of <ocl>self</ocl>?",
                  "Does the type <ocl>self.type</ocl> allow values for <ocl>self</ocl> that could never be correct?",
                  "Could <ocl>self</ocl> be combined with some other attribute of <ocl>self.owner</ocl> (e.g., {owner.structuralFeature})?",
                  "Could <ocl>self</ocl> be broken down into two or more parts (e.g., a phonenumber can be broken down into area code, prefix, and number)?",
                  "Could <ocl>self</ocl> be computed from other attributes instead of stored?"
                },
                new String[] { "Value",
                  "Should <ocl>self</ocl> have an initial (or default) value?",
                  "Is the initial value <ocl>self.initialValue</ocl> correct?",
                  "Could you write an expression to check if <ocl>self</ocl> is correct? Plausible?"
                },
                new String[] { "Location",
                  "Could <ocl>self</ocl> be defined in a different class that is associated with <ocl>self.owner</ocl>?",
                  "Could <ocl>self</ocl> be moved up the inheritance hierarchy to apply to <ocl>owner.name</ocl> and to other classes?",
                  "Does <ocl>self</ocl> apply to all instances of class <ocl>self.owner</ocl> including instances of subclasses?",
                  "Could <ocl>self</ocl> be eliminated from the model?",
                  "Is there another attribute in the model that should be revised or eliminated because it serves the same purpose as <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "For what reasons will <ocl>self</ocl> be updated?",
                  "Is there some other attribute that must be updated whenever <ocl>self</ocl> is updated?",
                  "Is there a method that should be called when <ocl>self</ocl> is updated?",
                  "Is there a method that should be called when <ocl>self</ocl> is given a certain kind of value?"
                }
            }
        },
        { "ChOperation",
            new String[][] {
                new String[] { "Naming",
                  "Does the name '<ocl>self</ocl>' clearly describe the operation?",
                  "Is '<ocl>self</ocl>' a verb or verb phrase?",
                  "Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?",
                  "Does <ocl>self</ocl> do one thing and do it well?"
                },
                new String[] { "Encoding",
                  "Is the return type '<ocl>self.returnType</ocl>' too restrictive to represent all possible values returned by <ocl>self</ocl>?",
                  "Does '<ocl>self.returnType</ocl>' allow return values that could never be correct?",
                  "Could <ocl>self</ocl> be combined with some other operation of <ocl>self.owner</ocl> (e.g., <ocl sep=', '>self.owner.behavioralFeature</ocl>)?",
                  "Could <ocl>self</ocl> be broken down into two or more parts (e.g., pre-process, main processing, and post-processing)?",
                  "Could <ocl>self</ocl> be replaced by a series of client calls to simpler operations?",
                  "Could <ocl>self</ocl> be combined with other operations to reduce the number of calls clients must make?"
                },
                new String[] { "Value",
                  "Can <ocl>self</ocl> handle all possible inputs?",
                  "Are there special case inputs that must be handled separately?",
                  "Could you write an expression to check if the arguments to <ocl>self</ocl> are correct? Plausible?",
                  "Can you express the preconditions of <ocl>self</ocl>?",
                  "Can you express the postconditions of <ocl>self</ocl>?",
                  "How will <ocl>self</ocl> behave if preconditions are violated?",
                  "How will <ocl>self</ocl> behave if postconditions cannot be achieved?"
                },
                new String[] { "Location",
                  "Could <ocl>self</ocl> be defined in a different class that is associated with <ocl>self.owner</ocl>?",
                  "Could <ocl>self</ocl> be moved up the inheritance hierarchy to apply to <ocl>self.owner</ocl> and to other classes?",
                  "Does <ocl>self</ocl> apply to all instances of class <ocl>self.owner</ocl> including instances of subclasses?",
                  "Could <ocl>self</ocl> be eliminated from the model?",
                  "Is there another operation in the model that should be revised or eliminated because it serves the same purpose as <ocl>self</ocl>?"
                }
            }
        },
        { "ChAssociation",
            new String[][] {
                new String[] { "Naming",
                  "Does the name '<ocl>self</ocl>' clearly describe the class?",
                  "Is '<ocl>self</ocl>' a noun or noun phrase?",
                  "Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?"
                },
                new String[] { "Encoding",
                  "Should <ocl>self</ocl> be its own class or a simple attribute of another class?",
                  "Does <ocl>self</ocl> do exactly one thing and do it well?",
                  "Could <ocl>self</ocl> be broken down into two or more classes?"
                },
                new String[] { "Value",
                  "Do all attributes of <ocl>self</ocl> start with meaningful values?",
                  "Could you write an invariant for this class?",
                  "Do all constructors establish the class invariant?",
                  "Do all operations maintain the class invariant?"
                },
                new String[] { "Location",
                  "Could <ocl>self</ocl> be defined in a different location in the class hierarchy?",
                  "Have you planned to have subclasses of <ocl>self</ocl>?",
                  "Could <ocl>self</ocl> be eliminated from the model?",
                  "Is there another class in the model that should be revised or eliminated because it serves the same purpose as <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "For what reasons will an instance of <ocl>self</ocl> be updated?",
                  "Is there some other object that must be updated whenever <ocl>self</ocl> is updated?"
                }
            }
        },
        { "ChInterface",
            new String[][] {
                new String[] { "Naming",
                  "Does the name '<ocl>self</ocl>' clearly describe the class?",
                  "Is '<ocl>self</ocl>' a noun or noun phrase?",
                  "Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?"
                },
                new String[] { "Encoding",
                  "Should <ocl>self</ocl> be its own class or a simple attribute of another class?",
                  "Does <ocl>self</ocl> do exactly one thing and do it well?",
                  "Could <ocl>self</ocl> be broken down into two or more classes?"
                },
                new String[] { "Value",
                  "Do all attributes of <ocl>self</ocl> start with meaningful values?",
                  "Could you write an invariant for this class?",
                  "Do all constructors establish the class invariant?",
                  "Do all operations maintain the class invariant?"
                },
                new String[] { "Location",
                  "Could <ocl>self</ocl> be defined in a different location in the class hierarchy?",
                  "Have you planned to have subclasses of <ocl>self</ocl>?",
                  "Could <ocl>self</ocl> be eliminated from the model?",
                  "Is there another class in the model that should be revised or eliminated because it serves the same purpose as <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "For what reasons will an instance of <ocl>self</ocl> be updated?",
                  "Is there some other object that must be updated whenever <ocl>self</ocl> is updated?"
                }
            }
        },
        { "ChInstance",
            new String[][] {
                new String[] { "General",
                  "Does this instance <ocl>self</ocl> clearly describe the instance?"
                },
                new String[] { "Naming",
                  "Does the name '<ocl>self</ocl>' clearly describe the instance?",
                  "Does '<ocl>self</ocl>' denote a state rather than an activity?",
                  "Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?"
                },
                new String[] { "Structure",
                  "Should <ocl>self</ocl> be its own state or could it be merged with another state?",
                  "Does <ocl>self</ocl> do exactly one thing and do it well?",
                  "Could <ocl>self</ocl> be broken down into two or more states?",
                  "Could you write a characteristic equation for <ocl>self</ocl>?",
                  "Does <ocl>self</ocl> belong in this state machine or another?",
                  "Should <ocl>self</ocl> be be an initial state?",
                  "Is some state in another machine exclusive with <ocl>self</ocl>?"
                },
                new String[] { "Actions",
                  "What action should be performed on entry into <ocl>self</ocl>?",
                  "Should some attribute be updated on entry into <ocl>self</ocl>?",
                  "What action should be performed on exit from <ocl>self</ocl>?",
                  "Should some attribute be updated on exit from <ocl>self</ocl>?",
                  "What action should be performed while in <ocl>self</ocl>?",
                  "Do state-actions maintain <ocl>self</ocl> as the current state?"
                },
                new String[] { "Transitions",
                  "Should there be another transition into <ocl>self</ocl>?",
                  "Can all the transitions into <ocl>self</ocl> be used?",
                  "Could some incoming transitions be combined?",
                  "Should there be another transition out of <ocl>self</ocl>?",
                  "Can all the transitions out of <ocl>self</ocl> be used?",
                  "Is each outgoing transition exclusive?",
                  "Could some outgoing transitions be combined?"
                }
            }
        },
        { "ChLink",
            new String[][] {
                new String[] { "Naming",
                  "Does the name '<ocl>self</ocl>' clearly describe the class?",
                  "Is '<ocl>self</ocl>' a noun or noun phrase?",
                  "Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?"
                },
                new String[] { "Encoding",
                  "Should <ocl>self</ocl> be its own class or a simple attribute of another class?",
                  "Does <ocl>self</ocl> do exactly one thing and do it well?",
                  "Could <ocl>self</ocl> be broken down into two or more classes?"
                },
                new String[] { "Value",
                  "Do all attributes of <ocl>self</ocl> start with meaningful values?",
                  "Could you write an invariant for this class?",
                  "Do all constructors establish the class invariant?",
                  "Do all operations maintain the class invariant?"
                },
                new String[] { "Location",
                  "Could <ocl>self</ocl> be defined in a different location in the class hierarchy?",
                  "Have you planned to have subclasses of <ocl>self</ocl>?",
                  "Could <ocl>self</ocl> be eliminated from the model?",
                  "Is there another class in the model that should be revised or eliminated because it serves the same purpose as <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "For what reasons will an instance of <ocl>self</ocl> be updated?",
                  "Is there some other object that must be updated whenever <ocl>self</ocl> is updated?"
                }
            }
        },
        { "ChState",
            new String[][] {
                new String[] { "Naming",
                  "Does the name '<ocl>self</ocl>' clearly describe the state?",
                  "Does '<ocl>self</ocl>' denote a state rather than an activity?",
                  "Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?"
                },
                new String[] { "Structure",
                  "Should <ocl>self</ocl> be its own state or could it be merged with another state?",
                  "Does <ocl>self</ocl> do exactly one thing and do it well?",
                  "Could <ocl>self</ocl> be broken down into two or more states?",
                  "Could you write a characteristic equation for <ocl>self</ocl>?",
                  "Does <ocl>self</ocl> belong in this state machine or another?",
                  "Should <ocl>self</ocl> be be an initial state?",
                  "Is some state in another machine exclusive with <ocl>self</ocl>?"
                },
                new String[] { "Actions",
                  "What action should be performed on entry into <ocl>self</ocl>?",
                  "Should some attribute be updated on entry into <ocl>self</ocl>?",
                  "What action should be performed on exit from <ocl>self</ocl>?",
                  "Should some attribute be updated on exit from <ocl>self</ocl>?",
                  "What action should be performed while in <ocl>self</ocl>?",
                  "Do state-actions maintain <ocl>self</ocl> as the current state?"
                },
                new String[] { "Transitions",
                  "Should there be another transition into <ocl>self</ocl>?",
                  "Can all the transitions into <ocl>self</ocl> be used?",
                  "Could some incoming transitions be combined?",
                  "Should there be another transition out of <ocl>self</ocl>?",
                  "Can all the transitions out of <ocl>self</ocl> be used?",
                  "Is each outgoing transition exclusive?",
                  "Could some outgoing transitions be combined?"
                }
            }
        },
        { "ChTransition",
            new String[][] {
                new String[] { "Structure",
                  "Should this transition start at a different source?",
                  "Should this transition end at a different destination?",
                  "Should there be another transition \"like\" this one?",
                  "Is another transition unneeded because of this one?"
                },
                new String[] { "Trigger",
                  "Does this transition need a trigger?",
                  "Does the trigger happen too often?",
                  "Does the trigger happen too rarely?"
                },
                new String[] { "MGuard",
                  "Could this transition be taken too often?",
                  "Is this transition's condition too restrictive?",
                  "Could it be broken down into two or more transitions?"
                },
                new String[] { "Actions",
                  "Should this transition have an action?",
                  "Should this transition's action be an exit action?",
                  "Should this transition's action be an entry action?",
                  "Is the precondition of the action always met?",
                  "Is the action's postcondition consistent with the destination?"
                }
            }
        },
        { "ChUseCase",
            new String[][] {
                new String[] { "Naming",
                  "Does the name '<ocl>self</ocl>' clearly describe the class?",
                  "Is '<ocl>self</ocl>' a noun or noun phrase?",
                  "Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?"
                },
                new String[] { "Encoding",
                  "Should <ocl>self</ocl> be its own class or a simple attribute of another class?",
                  "Does <ocl>self</ocl> do exactly one thing and do it well?",
                  "Could <ocl>self</ocl> be broken down into two or more classes?"
                },
                new String[] { "Value",
                  "Do all attributes of <ocl>self</ocl> start with meaningful values?",
                  "Could you write an invariant for this class?",
                  "Do all constructors establish the class invariant?",
                  "Do all operations maintain the class invariant?"
                },
                new String[] { "Location",
                  "Could <ocl>self</ocl> be defined in a different location in the class hierarchy?",
                  "Have you planned to have subclasses of <ocl>self</ocl>?",
                  "Could <ocl>self</ocl> be eliminated from the model?",
                  "Is there another class in the model that should be revised or eliminated because it serves the same purpose as <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "For what reasons will an instance of <ocl>self</ocl> be updated?",
                  "Is there some other object that must be updated whenever <ocl>self</ocl> is updated?"
                }
            }
        },
        { "ChActor",
            new String[][] {
                new String[] { "Naming",
                  "Does the name '<ocl>self</ocl>' clearly describe the class?",
                  "Is '<ocl>self</ocl>' a noun or noun phrase?",
                  "Could the name '<ocl>self</ocl>' be misinterpreted to mean something else?"
                },
                new String[] { "Encoding",
                  "Should <ocl>self</ocl> be its own class or a simple attribute of another class?",
                  "Does <ocl>self</ocl> do exactly one thing and do it well?",
                  "Could <ocl>self</ocl> be broken down into two or more classes?"
                },
                new String[] { "Value",
                  "Do all attributes of <ocl>self</ocl> start with meaningful values?",
                  "Could you write an invariant for this class?",
                  "Do all constructors establish the class invariant?",
                  "Do all operations maintain the class invariant?"
                },
                new String[] { "Location",
                  "Could <ocl>self</ocl> be defined in a different location in the class hierarchy?",
                  "Have you planned to have subclasses of <ocl>self</ocl>?",
                  "Could <ocl>self</ocl> be eliminated from the model?",
                  "Is there another class in the model that should be revised or eliminated because it serves the same purpose as <ocl>self</ocl>?"
                },
                new String[] { "Updates",
                  "For what reasons will an instance of <ocl>self</ocl> be updated?",
                  "Is there some other object that must be updated whenever <ocl>self</ocl> is updated?"
                }
            }
        }
    };

    public Object[][] getContents() {
        return _contents;
    }
}
