// $Id$
//Copyright (c) 2003 The Regents of the University of California. All
//Rights Reserved. Permission to use, copy, modify, and distribute this
//software and its documentation without fee, and without a written
//agreement is hereby granted, provided that the above copyright notice
//and this paragraph appear in all copies.  This software program and
//documentation are copyrighted by The Regents of the University of
//California. The software program and documentation are supplied "AS
//IS", without any accompanying services from The Regents. The Regents
//does not warrant that the operation of the program will be
//uninterrupted or error-free. The end-user understands that the program
//was developed for research purposes and is advised not to rely
//exclusively on the program for any reason.  IN NO EVENT SHALL THE
//UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
//SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
//ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
//THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
//WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
//MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
//PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
//CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
//UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.model.uml;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains identifiers for all of the different
 * UML elements identified by ArgoUML.
 * 
 * @author Thierry Lach
 */
public final class Uml {

    private static Map umlClassList;

	/** Get a map containing entries for all the name/identifier pairs
     * identified by Argo
     */
    public static Map getUmlClassList() {
        return umlClassList;
    }

	public static final UmlEntity ABSTRACTION;
	public static final UmlEntity ACTION;
	public static final UmlEntity ACTION_EXPRESSION;
	public static final UmlEntity ACTION_SEQUENCE;
	public static final UmlEntity ACTION_STATE;
	public static final UmlEntity ACTIVITY_GRAPH;
	public static final UmlEntity ACTOR;
	public static final UmlEntity ARG_LISTS_EXPRESSION;
	public static final UmlEntity ARGUMENT;
	public static final UmlEntity ASSOCIATION;
	public static final UmlEntity ASSOCIATION_CLASS;
	public static final UmlEntity ASSOCIATION_END;
	public static final UmlEntity ASSOCIATION_END_ROLE;
	public static final UmlEntity ASSOCIATION_ROLE;
	public static final UmlEntity ATTRIBUTE;
	public static final UmlEntity ATTRIBUTE_LINK;
	public static final UmlEntity BEHAVIORAL_FEATURE;
	public static final UmlEntity BINDING;
	public static final UmlEntity BOOLEAN_EXPRESSION;
	public static final UmlEntity CALL_ACTION;
	public static final UmlEntity CALL_EVENT;
	public static final UmlEntity CALL_STATE;
	public static final UmlEntity CHANGE_EVENT;
	public static final UmlEntity CLASS;
	public static final UmlEntity CLASSIFIER;
	public static final UmlEntity CLASSIFIER_IN_STATE;
	public static final UmlEntity CLASSIFIER_ROLE;
	public static final UmlEntity COLLABORATION;
	public static final UmlEntity COMMENT;
	public static final UmlEntity COMPONENT;
	public static final UmlEntity COMPONENT_INSTANCE;
	public static final UmlEntity COMPOSITE_STATE;
	public static final UmlEntity CONSTRAINT;
	public static final UmlEntity CREATE_ACTION;
	public static final UmlEntity DATA_VALUE;
	public static final UmlEntity DATATYPE;
	public static final UmlEntity DEPENDENCY;
	public static final UmlEntity DESTROY_ACTION;
	public static final UmlEntity ELEMENT;
	public static final UmlEntity ELEMENT_IMPORT;
	public static final UmlEntity ELEMENT_RESIDENCE;
	public static final UmlEntity EVENT;
	public static final UmlEntity EXCEPTION;
	public static final UmlEntity EXPRESSION;
	public static final UmlEntity EXTEND;
	public static final UmlEntity EXTENSION_POINT;
	public static final UmlEntity FEATURE;
	public static final UmlEntity FINAL_STATE;
	public static final UmlEntity FLOW;
	public static final UmlEntity GENERALIZABLE_ELEMENT   ;
	public static final UmlEntity GENERALIZATION;
	public static final UmlEntity GUARD;
	public static final UmlEntity INCLUDE;
	public static final UmlEntity INSTANCE;
	public static final UmlEntity INTERACTION;
	public static final UmlEntity INTERFACE;
	public static final UmlEntity ITERATION_EXPRESSION;
	public static final UmlEntity LINK;
	public static final UmlEntity LINK_END;
	public static final UmlEntity LINK_OBJECT;
	public static final UmlEntity MAPPING_EXPRESSION;
	public static final UmlEntity MESSAGE;
	public static final UmlEntity METHOD;
	public static final UmlEntity MODEL;
	public static final UmlEntity MODEL_ELEMENT;
	public static final UmlEntity MULTIPLICITY;
	public static final UmlEntity MULTIPLICITY_RANGE;
	public static final UmlEntity NAMESPACE;
	public static final UmlEntity NODE;
	public static final UmlEntity NODE_INSTANCE;
	public static final UmlEntity OBJECT;
	public static final UmlEntity OBJECT_FLOW_STATE;
	public static final UmlEntity OBJECT_SET_EXPRESSION   ;
	public static final UmlEntity OPERATION;
	public static final UmlEntity PACKAGE;
	public static final UmlEntity PARAMETER;
	public static final UmlEntity PARTITION;
	public static final UmlEntity PERMISSION;
	public static final UmlEntity PRESENTATION_ELEMENT;
	public static final UmlEntity PROCEDURE_EXPRESSION;
	public static final UmlEntity PSEUDOSTATE;
	public static final UmlEntity RECEPTION;
	public static final UmlEntity RELATIONSHIP;
	public static final UmlEntity RETURN_ACTION;
	public static final UmlEntity SEND_ACTION;
	public static final UmlEntity SIGNAL;
	public static final UmlEntity SIGNAL_EVENT;
	public static final UmlEntity SIMPLE_STATE;
	public static final UmlEntity STATE;
	public static final UmlEntity STATE_MACHINE;
	public static final UmlEntity STATE_VERTEX;
	public static final UmlEntity STEREOTYPE;
	public static final UmlEntity STIMULUS;
	public static final UmlEntity STRUCTURAL_FEATURE;
	public static final UmlEntity STUB_STATE;
	public static final UmlEntity SUBACTIVITY_STATE;
	public static final UmlEntity SUBMACHINE_STATE;
	public static final UmlEntity SUBSYSTEM;
	public static final UmlEntity SYNCH_STATE;
	public static final UmlEntity TAGGED_VALUE;
	public static final UmlEntity TEMPLATE_PARAMETER;
	public static final UmlEntity TERMINATE_ACTION;
	public static final UmlEntity TIME_EVENT;
	public static final UmlEntity TIME_EXPRESSION;
	public static final UmlEntity TRANSITION;
	public static final UmlEntity TYPE_EXPRESSION;
	public static final UmlEntity UNINTERPRETED_ACTION;
	public static final UmlEntity USAGE;
	public static final UmlEntity USE_CASE;
	public static final UmlEntity USE_CASE_INSTANCE;

	/** Initialize the marker classes and the list of valid Uml classes */

	static {

		Uml uml = new Uml();
		// Initialize all of the marker classes.

		ABSTRACTION             = uml.new TypeAbstraction();
		ACTION                  = uml.new TypeAction();
		ACTION_EXPRESSION       = uml.new TypeActionExpression();
		ACTION_SEQUENCE         = uml.new TypeActionSequence();
		ACTION_STATE            = uml.new TypeActionState();
		ACTIVITY_GRAPH          = uml.new TypeActivityGraph();
		ACTOR                   = uml.new TypeActor();
		ARG_LISTS_EXPRESSION    = uml.new TypeArgListsExpression();
		ARGUMENT                = uml.new TypeArgument();
		ASSOCIATION             = uml.new TypeAssociation();
		ASSOCIATION_CLASS       = uml.new TypeAssociationClass();
		ASSOCIATION_END         = uml.new TypeAssociationEnd();
		ASSOCIATION_END_ROLE    = uml.new TypeAssociationEndRole();
		ASSOCIATION_ROLE        = uml.new TypeAssociationRole();
		ATTRIBUTE               = uml.new TypeAttribute();
		ATTRIBUTE_LINK          = uml.new TypeAttributeLink();
		BEHAVIORAL_FEATURE      = uml.new TypeBehavioralFeature();
		BINDING                 = uml.new TypeBinding();
		BOOLEAN_EXPRESSION      = uml.new TypeBooleanExpression();
		CALL_ACTION             = uml.new TypeCallAction();
		CALL_EVENT              = uml.new TypeCallEvent();
		CALL_STATE              = uml.new TypeCallState();
		CHANGE_EVENT            = uml.new TypeChangeEvent();
		CLASS                   = uml.new TypeClass();
		CLASSIFIER              = uml.new TypeClassifier();
		CLASSIFIER_IN_STATE     = uml.new TypeClassifierInState();
		CLASSIFIER_ROLE         = uml.new TypeClassifierRole();
		COLLABORATION           = uml.new TypeCollaboration();
		COMMENT                 = uml.new TypeComment();
		COMPONENT               = uml.new TypeComponent();
		COMPONENT_INSTANCE      = uml.new TypeComponentInstance();
		COMPOSITE_STATE         = uml.new TypeCompositeState();
		CONSTRAINT              = uml.new TypeConstraint();
		CREATE_ACTION           = uml.new TypeCreateAction();
		DATA_VALUE              = uml.new TypeDataValue();
		DATATYPE                = uml.new TypeDataType();
		DEPENDENCY              = uml.new TypeDependency();
		DESTROY_ACTION          = uml.new TypeDestroyAction();
		ELEMENT                 = uml.new TypeElement();
		ELEMENT_IMPORT          = uml.new TypeElementImport();
		ELEMENT_RESIDENCE       = uml.new TypeElementResidence();
		EVENT                   = uml.new TypeEvent();
		EXCEPTION               = uml.new TypeException();
		EXPRESSION              = uml.new TypeExpression();
		EXTEND                  = uml.new TypeExtend();
		EXTENSION_POINT         = uml.new TypeExtensionPoint();
		FEATURE                 = uml.new TypeFeature();
		FINAL_STATE             = uml.new TypeFinalState();
		FLOW                    = uml.new TypeFlow();
		GENERALIZABLE_ELEMENT   = uml.new TypeGeneralizableElement();
		GENERALIZATION          = uml.new TypeGeneralization();
		GUARD                   = uml.new TypeGuard();
		INCLUDE                 = uml.new TypeInclude();
		INSTANCE                = uml.new TypeInstance();
		INTERACTION             = uml.new TypeInteraction();
		INTERFACE               = uml.new TypeInterface();
		ITERATION_EXPRESSION    = uml.new TypeIterationExpression();
		LINK                    = uml.new TypeLink();
		LINK_END                = uml.new TypeLinkEnd();
		LINK_OBJECT             = uml.new TypeLinkObject();
		MAPPING_EXPRESSION      = uml.new TypeMappingExpression();
		MESSAGE                 = uml.new TypeMessage();
		METHOD                  = uml.new TypeMethod();
		MODEL                   = uml.new TypeModel();
		MODEL_ELEMENT           = uml.new TypeModelElement();
		MULTIPLICITY            = uml.new TypeMultiplicity();
		MULTIPLICITY_RANGE      = uml.new TypeMultiplicityRange();
		NAMESPACE               = uml.new TypeNamespace();
		NODE                    = uml.new TypeNode();
		NODE_INSTANCE           = uml.new TypeNodeInstance();
		OBJECT                  = uml.new TypeObject();
		OBJECT_FLOW_STATE       = uml.new TypeObjectFlowState();
		OBJECT_SET_EXPRESSION   = uml.new TypeObjectSetExpression();
		OPERATION               = uml.new TypeOperation();
		PACKAGE                 = uml.new TypePackage();
		PARAMETER               = uml.new TypeParameter();
		PARTITION               = uml.new TypePartition();
		PERMISSION              = uml.new TypePermission();
		PRESENTATION_ELEMENT    = uml.new TypePresentationElement();
		PROCEDURE_EXPRESSION    = uml.new TypeProcedureExpression();
		PSEUDOSTATE             = uml.new TypePseudostate();
		RECEPTION               = uml.new TypeReception();
		RELATIONSHIP            = uml.new TypeRelationship();
		RETURN_ACTION           = uml.new TypeReturnAction();
		SEND_ACTION             = uml.new TypeSendAction();
		SIGNAL                  = uml.new TypeSignal();
		SIGNAL_EVENT            = uml.new TypeSignalEvent();
		SIMPLE_STATE            = uml.new TypeSimpleState();
		STATE                   = uml.new TypeState();
		STATE_MACHINE           = uml.new TypeStateMachine();
		STATE_VERTEX            = uml.new TypeStateVertex();
		STEREOTYPE              = uml.new TypeStereotype();
		STIMULUS                = uml.new TypeStimulus();
		STRUCTURAL_FEATURE      = uml.new TypeStructuralFeature();
		STUB_STATE              = uml.new TypeStubState();
		SUBACTIVITY_STATE       = uml.new TypeSubactivityState();
		SUBMACHINE_STATE        = uml.new TypeSubmachineState();
		SUBSYSTEM               = uml.new TypeSubsystem();
		SYNCH_STATE             = uml.new TypeSynchState();
		TAGGED_VALUE            = uml.new TypeTaggedValue();
		TEMPLATE_PARAMETER      = uml.new TypeTemplateParameter();
		TERMINATE_ACTION        = uml.new TypeTerminateAction();
		TIME_EVENT              = uml.new TypeTimeEvent();
		TIME_EXPRESSION         = uml.new TypeTimeExpression();
		TRANSITION              = uml.new TypeTransition();
		TYPE_EXPRESSION         = uml.new TypeTypeExpression();
		UNINTERPRETED_ACTION    = uml.new TypeUninterpretedAction();
		USAGE                   = uml.new TypeUsage();
		USE_CASE                = uml.new TypeUseCase();
		USE_CASE_INSTANCE       = uml.new TypeUseCaseInstance();

		// Add the marker classes to the list
		umlClassList = new HashMap(110);

		umlClassList.put("Abstraction", Uml.ABSTRACTION);
		umlClassList.put("Action", Uml.ACTION);
		umlClassList.put("ActionExpression", Uml.ACTION_EXPRESSION);
		umlClassList.put("ActionSequence", Uml.ACTION_SEQUENCE);
		umlClassList.put("ActionState", Uml.ACTION_STATE);
		umlClassList.put("ActivityGraph", Uml.ACTIVITY_GRAPH);
		umlClassList.put("Actor", Uml.ACTOR);
		umlClassList.put("Actor", Uml.ACTOR);
		umlClassList.put("ArgListsExpression", Uml.ARG_LISTS_EXPRESSION);
		umlClassList.put("Argument", Uml.ARGUMENT);
		umlClassList.put("Association", Uml.ASSOCIATION);
		umlClassList.put("AssociationClass", Uml.ASSOCIATION_CLASS);
		umlClassList.put("AssociationEnd", Uml.ASSOCIATION_END);
		umlClassList.put("AssociationEndRole", Uml.ASSOCIATION_END_ROLE);
		umlClassList.put("AssociationRole", Uml.ASSOCIATION_ROLE);
		umlClassList.put("Attribute", Uml.ATTRIBUTE);
		umlClassList.put("AttributeLink", Uml.ATTRIBUTE_LINK);
		umlClassList.put("BehavioralFeature", Uml.BEHAVIORAL_FEATURE);
		umlClassList.put("Binding", Uml.BINDING);
		umlClassList.put("BooleanExpression", Uml.BOOLEAN_EXPRESSION);
		umlClassList.put("CallAction", Uml.CALL_ACTION);
		umlClassList.put("CallEvent", Uml.CALL_EVENT);
		umlClassList.put("CallState", Uml.CALL_STATE);
		umlClassList.put("ChangeEvent", Uml.CHANGE_EVENT);
		umlClassList.put("Class", Uml.CLASS);
		umlClassList.put("Classifier", Uml.CLASSIFIER);
		umlClassList.put("ClassifierInState", Uml.CLASSIFIER_IN_STATE);
		umlClassList.put("ClassifierRole", Uml.CLASSIFIER_ROLE);
		umlClassList.put("Collaboration", Uml.COLLABORATION);
		umlClassList.put("Comment", Uml.COMMENT);
		umlClassList.put("Component", Uml.COMPONENT);
		umlClassList.put("ComponentInstance", Uml.COMPONENT_INSTANCE);
		umlClassList.put("CompositeState", Uml.COMPOSITE_STATE);
		umlClassList.put("Constraint", Uml.CONSTRAINT);
		umlClassList.put("CreateAction", Uml.CREATE_ACTION);
		umlClassList.put("DataType", Uml.DATATYPE);
		umlClassList.put("DataValue", Uml.DATA_VALUE);
		umlClassList.put("Dependency", Uml.DEPENDENCY);
		umlClassList.put("DestroyAction", Uml.DESTROY_ACTION);
		umlClassList.put("Element", Uml.ELEMENT);
		umlClassList.put("ElementImport", Uml.ELEMENT_IMPORT);
		umlClassList.put("ElementResidence", Uml.ELEMENT_RESIDENCE);
		umlClassList.put("Event", Uml.EVENT);
		umlClassList.put("Exception", Uml.EXCEPTION);
		umlClassList.put("Expression", Uml.EXPRESSION);
		umlClassList.put("Extend", Uml.EXTEND);
		umlClassList.put("ExtensionPoint", Uml.EXTENSION_POINT);
		umlClassList.put("Feature", Uml.FEATURE);
		umlClassList.put("FinalState", Uml.FINAL_STATE);
		umlClassList.put("Flow", Uml.FLOW);
		umlClassList.put("GeneralizableElement", Uml.GENERALIZABLE_ELEMENT);
		umlClassList.put("Generalization", Uml.GENERALIZATION);
		umlClassList.put("Guard", Uml.GUARD);
		umlClassList.put("Include", Uml.INCLUDE);
		umlClassList.put("Instance", Uml.INSTANCE);
		umlClassList.put("Interaction", Uml.INTERACTION);
		umlClassList.put("Interface", Uml.INTERFACE);
		umlClassList.put("IterationExpression", Uml.ITERATION_EXPRESSION);
		umlClassList.put("Link", Uml.LINK);
		umlClassList.put("LinkEnd", Uml.LINK_END);
		umlClassList.put("LinkObject", Uml.LINK_OBJECT);
		umlClassList.put("MappingExpression", Uml.MAPPING_EXPRESSION);
		umlClassList.put("Message", Uml.MESSAGE);
		umlClassList.put("Method", Uml.METHOD);
		umlClassList.put("Model", Uml.MODEL);
		umlClassList.put("ModelElement", Uml.MODEL_ELEMENT);
		umlClassList.put("Multiplicity", Uml.MULTIPLICITY);
		umlClassList.put("MultiplicityRange", Uml.MULTIPLICITY_RANGE);
		umlClassList.put("Namespace", Uml.NAMESPACE);
		umlClassList.put("Node", Uml.NODE);
		umlClassList.put("NodeInstance", Uml.NODE_INSTANCE);
		umlClassList.put("Object", Uml.OBJECT);
		umlClassList.put("ObjectFlowState", Uml.OBJECT_FLOW_STATE);
		umlClassList.put("ObjectSetExpression", Uml.OBJECT_SET_EXPRESSION);
		umlClassList.put("Operation", Uml.OPERATION);
		umlClassList.put("Package", Uml.PACKAGE);
		umlClassList.put("Parameter", Uml.PARAMETER);
		umlClassList.put("Partition", Uml.PARTITION);
		umlClassList.put("Permission", Uml.PERMISSION);
		umlClassList.put("PresentationElement", Uml.PRESENTATION_ELEMENT);
		umlClassList.put("ProcedureExpression", Uml.PROCEDURE_EXPRESSION);
		umlClassList.put("Pseudostate", Uml.PSEUDOSTATE);
		umlClassList.put("Reception", Uml.RECEPTION);
		umlClassList.put("Relationship", Uml.RELATIONSHIP);
		umlClassList.put("ReturnAction", Uml.RETURN_ACTION);
		umlClassList.put("SendAction", Uml.SEND_ACTION);
		umlClassList.put("Signal", Uml.SIGNAL);
		umlClassList.put("SignalEvent", Uml.SIGNAL_EVENT);
		umlClassList.put("SimpleState", Uml.SIMPLE_STATE);
		umlClassList.put("State", Uml.STATE);
		umlClassList.put("StateMachine", Uml.STATE_MACHINE);
		umlClassList.put("StateVertex", Uml.STATE_VERTEX);
		umlClassList.put("Stereotype", Uml.STEREOTYPE);
		umlClassList.put("Stimulus", Uml.STIMULUS);
		umlClassList.put("StructuralFeature", Uml.STRUCTURAL_FEATURE);
		umlClassList.put("StubState", Uml.STUB_STATE);
		umlClassList.put("SubactivityState", Uml.SUBACTIVITY_STATE);
		umlClassList.put("SubmachineState", Uml.SUBMACHINE_STATE);
		umlClassList.put("Subsystem", Uml.SUBSYSTEM);
		umlClassList.put("SynchState", Uml.SYNCH_STATE);
		umlClassList.put("TaggedValue", Uml.TAGGED_VALUE);
		umlClassList.put("TemplateParameter", Uml.TEMPLATE_PARAMETER);
		umlClassList.put("TerminateAction", Uml.TERMINATE_ACTION);
		umlClassList.put("TimeEvent", Uml.TIME_EVENT);
		umlClassList.put("TimeExpression", Uml.TIME_EXPRESSION);
		umlClassList.put("Transition", Uml.TRANSITION);
		umlClassList.put("TypeExpression", Uml.TYPE_EXPRESSION);
		umlClassList.put("UninterpretedAction", Uml.UNINTERPRETED_ACTION);
		umlClassList.put("Usage", Uml.USAGE);
		umlClassList.put("UseCase", Uml.USE_CASE);
		umlClassList.put("UseCaseInstance", Uml.USE_CASE_INSTANCE);
	}

	/** An abstract which all ArgoUML-recognized model types
	 * must extend.
	 * 
	 * Private so that no classes outside of Uml can instantiate it.
	 * 
	 * @author Thierry Lach
	 */
	private abstract class AbstractUmlEntity implements UmlEntity {
		AbstractUmlEntity() {
		}		
	}

	/** An interface which all ArgoUML-recognized model types
	 * must implement.
	 * 
	 * @author Thierry Lach
	 */
	public interface UmlEntity {

	}

	/** An interface which all ArgoUML-recognized model types
	 * must implement.
	 * 
	 * @author Thierry Lach
	 * @deprecated without replacement after the facade handles everything.
	 */
	public interface UmlFacadeEntity {

	}

	private class TypeAbstraction extends AbstractUmlEntity { }
	private class TypeAction extends AbstractUmlEntity { }
	private class TypeActionExpression extends AbstractUmlEntity { }
	private class TypeActionSequence extends AbstractUmlEntity { }
	private class TypeActionState extends AbstractUmlEntity { }
	private class TypeActivityGraph extends AbstractUmlEntity { }
	private class TypeActor extends AbstractUmlEntity { }
	private class TypeArgListsExpression extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeArgument extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeAssociation extends AbstractUmlEntity { }
	private class TypeAssociationClass extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeAssociationEnd extends AbstractUmlEntity { }
	private class TypeAssociationEndRole extends AbstractUmlEntity { }
	private class TypeAssociationRole extends AbstractUmlEntity { }
	private class TypeAttribute extends AbstractUmlEntity { }
	private class TypeAttributeLink extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeBehavioralFeature extends AbstractUmlEntity { }
	private class TypeBinding extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeBooleanExpression extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeCallAction extends AbstractUmlEntity { }
	private class TypeCallEvent extends AbstractUmlEntity  { }
	private class TypeCallState extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeChangeEvent extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeClass extends AbstractUmlEntity { }
	private class TypeClassifier extends AbstractUmlEntity { }
	private class TypeClassifierInState extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeClassifierRole extends AbstractUmlEntity { }
	private class TypeCollaboration extends AbstractUmlEntity { }
	private class TypeComment extends AbstractUmlEntity { }
	private class TypeComponent extends AbstractUmlEntity { }
	private class TypeComponentInstance extends AbstractUmlEntity { }
	private class TypeCompositeState extends AbstractUmlEntity { }
	private class TypeConstraint extends AbstractUmlEntity { }
	private class TypeCreateAction extends AbstractUmlEntity { }
	private class TypeDataType extends AbstractUmlEntity { }
	private class TypeDataValue extends AbstractUmlEntity { }
	private class TypeDependency extends AbstractUmlEntity { }
	private class TypeDestroyAction extends AbstractUmlEntity { }
	private class TypeElement extends AbstractUmlEntity { }
	private class TypeElementImport extends AbstractUmlEntity { }
	private class TypeElementResidence extends AbstractUmlEntity { }
	private class TypeEvent extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeException extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeExpression extends AbstractUmlEntity { }
	private class TypeExtend extends AbstractUmlEntity { }
	private class TypeExtensionPoint extends AbstractUmlEntity { }
	private class TypeFeature extends AbstractUmlEntity { }
	private class TypeFinalState extends AbstractUmlEntity { }
	private class TypeFlow extends AbstractUmlEntity { }
	private class TypeGeneralizableElement extends AbstractUmlEntity { }
	private class TypeGeneralization extends AbstractUmlEntity { }
	private class TypeGuard extends AbstractUmlEntity { }
	private class TypeInclude extends AbstractUmlEntity { }
	private class TypeInstance extends AbstractUmlEntity { }
	private class TypeInteraction extends AbstractUmlEntity { }
	private class TypeInterface extends AbstractUmlEntity { }
	private class TypeIterationExpression extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeLink extends AbstractUmlEntity{ }
	private class TypeLinkEnd extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeLinkObject extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeMappingExpression extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeMessage extends AbstractUmlEntity { }
	private class TypeMethod extends AbstractUmlEntity { }
	private class TypeModel extends AbstractUmlEntity  { }
	private class TypeModelElement extends AbstractUmlEntity { }
	private class TypeMultiplicity extends AbstractUmlEntity { }
	private class TypeMultiplicityRange extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeNamespace extends AbstractUmlEntity { }
	private class TypeNode extends AbstractUmlEntity { }
	private class TypeNodeInstance extends AbstractUmlEntity { }
	private class TypeObject extends AbstractUmlEntity { }
	private class TypeObjectFlowState extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeObjectSetExpression extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeOperation extends AbstractUmlEntity { }
	private class TypePackage extends AbstractUmlEntity { }
	private class TypeParameter extends AbstractUmlEntity { }
	private class TypePartition extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypePermission extends AbstractUmlEntity { }
	private class TypePresentationElement extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeProcedureExpression extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypePseudostate extends AbstractUmlEntity { }
	private class TypeReception extends AbstractUmlEntity { }
	private class TypeRelationship extends AbstractUmlEntity { }
	private class TypeReturnAction extends AbstractUmlEntity { }
	private class TypeSendAction extends AbstractUmlEntity { }
	private class TypeSignal extends AbstractUmlEntity { }
	private class TypeSignalAction extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeSignalEvent extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeSimpleState extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeState extends AbstractUmlEntity { }
	private class TypeStateMachine extends AbstractUmlEntity { }
	private class TypeStateVertex extends AbstractUmlEntity { }
	private class TypeStereotype extends AbstractUmlEntity { }
	private class TypeStimulus extends AbstractUmlEntity { }
	private class TypeStructuralFeature extends AbstractUmlEntity { }
	private class TypeStubState extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeSubactivityState extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeSubmachineState extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeSubsystem extends AbstractUmlEntity { }
	private class TypeSynchState extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeTaggedValue extends AbstractUmlEntity { }
	private class TypeTemplateParameter extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeTerminateAction extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeTimeEvent extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeTimeExpression extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeTransition extends AbstractUmlEntity { }
	private class TypeTypeExpression extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeUninterpretedAction extends AbstractUmlEntity implements UmlFacadeEntity  { }
	private class TypeUsage extends AbstractUmlEntity { }
	private class TypeUseCase extends AbstractUmlEntity { }
	private class TypeUseCaseInstance extends AbstractUmlEntity implements UmlFacadeEntity  { }


}
