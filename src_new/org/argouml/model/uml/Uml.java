// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.argouml.model.UmlModelEntity;

/**
 * This class contains identifiers for all of the different
 * UML elements identified by ArgoUML.
 * 
 * @author Thierry Lach
 */
public final class Uml {

    private static HashSet umlClassList;

    private static Map xref;

    /** Get a map containing entries for all the name/identifier pairs
     * identified by Argo
     * 
     * @return a collection of all recognized Uml class types
     */
    public static Collection getUmlClassList() {
        return umlClassList;
    }

    /** Abstraction identifier */
    public static final UmlModelEntity ABSTRACTION;
    /** Action identifier */
    public static final UmlModelEntity ACTION;
    /** ActionExpression identifier */
    public static final UmlModelEntity ACTION_EXPRESSION;
    /** ActionSequence identifier */
    public static final UmlModelEntity ACTION_SEQUENCE;
    /** ActionState identifier */
    public static final UmlModelEntity ACTION_STATE;
    /** ActivityGraph identifier */
    public static final UmlModelEntity ACTIVITY_GRAPH;
    /** Actor identifier */
    public static final UmlModelEntity ACTOR;
    /** ArgListsExpression identifier */
    public static final UmlModelEntity ARG_LISTS_EXPRESSION;
    /** Argument identifier */
    public static final UmlModelEntity ARGUMENT;
    /** Association identifier */
    public static final UmlModelEntity ASSOCIATION;
    /** AssocationClass identifier */
    public static final UmlModelEntity ASSOCIATION_CLASS;
    /** AssociationEnd identifier */
    public static final UmlModelEntity ASSOCIATION_END;
    /** AssociationEndRole identifier */
    public static final UmlModelEntity ASSOCIATION_END_ROLE;
    /** AssociationRole identifier */
    public static final UmlModelEntity ASSOCIATION_ROLE;
    /** Attribute identifier */
    public static final UmlModelEntity ATTRIBUTE;
    /** AttributeLink identifier */
    public static final UmlModelEntity ATTRIBUTE_LINK;
    /** BehavioralFeature identifier */
    public static final UmlModelEntity BEHAVIORAL_FEATURE;
    /** Binding identifier */
    public static final UmlModelEntity BINDING;
    /** BooleanExpression identifier */
    public static final UmlModelEntity BOOLEAN_EXPRESSION;
    /** CallAction identifier */
    public static final UmlModelEntity CALL_ACTION;
    /** CallEvent identifier */
    public static final UmlModelEntity CALL_EVENT;
    /** CallState identifier */
    public static final UmlModelEntity CALL_STATE;
    /** ChangeEvent identifier */
    public static final UmlModelEntity CHANGE_EVENT;
    /** Class identifier */
    public static final UmlModelEntity CLASS;
    /** Classifier identifier */
    public static final UmlModelEntity CLASSIFIER;
    /** ClassifierInState identifier */
    public static final UmlModelEntity CLASSIFIER_IN_STATE;
    /** ClassifierRole identifier */
    public static final UmlModelEntity CLASSIFIER_ROLE;
    /** Collaboration identifier */
    public static final UmlModelEntity COLLABORATION;
    /** Comment identifier */
    public static final UmlModelEntity COMMENT;
    /** Component identifier */
    public static final UmlModelEntity COMPONENT;
    /** ComponentInstance identifier */
    public static final UmlModelEntity COMPONENT_INSTANCE;
    /** CompositeState identifier */
    public static final UmlModelEntity COMPOSITE_STATE;
    /** Constraint identifier */
    public static final UmlModelEntity CONSTRAINT;
    /** CreateAction identifier */
    public static final UmlModelEntity CREATE_ACTION;
    /** DataValue identifier */
    public static final UmlModelEntity DATA_VALUE;
    /** Datatype identifier */
    public static final UmlModelEntity DATATYPE;
    /** Dependency identifier */
    public static final UmlModelEntity DEPENDENCY;
    /** DestroyAction identifier */
    public static final UmlModelEntity DESTROY_ACTION;
    /** Element identifier */
    public static final UmlModelEntity ELEMENT;
    /** ElementImport identifier */
    public static final UmlModelEntity ELEMENT_IMPORT;
    /** ElementResidence identifier */
    public static final UmlModelEntity ELEMENT_RESIDENCE;
    /** Event identifier */
    public static final UmlModelEntity EVENT;
    /** Exception identifier */
    public static final UmlModelEntity EXCEPTION;
    /** Expression identifier */
    public static final UmlModelEntity EXPRESSION;
    /** Extend identifier */
    public static final UmlModelEntity EXTEND;
    /** ExtensionPoint identifier */
    public static final UmlModelEntity EXTENSION_POINT;
    /** Feature identifier */
    public static final UmlModelEntity FEATURE;
    /** FinalState identifier */
    public static final UmlModelEntity FINAL_STATE;
    /** Flow identifier */
    public static final UmlModelEntity FLOW;
    /** GeneralizableElement identifier */
    public static final UmlModelEntity GENERALIZABLE_ELEMENT;
    /** Generalization identifier */
    public static final UmlModelEntity GENERALIZATION;
    /** Guard identifier */
    public static final UmlModelEntity GUARD;
    /** Include identifier */
    public static final UmlModelEntity INCLUDE;
    /** Instance identifier */
    public static final UmlModelEntity INSTANCE;
    /** Interaction identifier */
    public static final UmlModelEntity INTERACTION;
    /** Interface identifier */
    public static final UmlModelEntity INTERFACE;
    /** IterationExpression identifier */
    public static final UmlModelEntity ITERATION_EXPRESSION;
    /** Link identifier */
    public static final UmlModelEntity LINK;
    /** LinkEnd identifier */
    public static final UmlModelEntity LINK_END;
    /** LinkObject identifier */
    public static final UmlModelEntity LINK_OBJECT;
    /** MappingExpression identifier */
    public static final UmlModelEntity MAPPING_EXPRESSION;
    /** Message identifier */
    public static final UmlModelEntity MESSAGE;
    /** Method identifier */
    public static final UmlModelEntity METHOD;
    /** Model identifier */
    public static final UmlModelEntity MODEL;
    /** ModelElement identifier */
    public static final UmlModelEntity MODEL_ELEMENT;
    /** Multiplicity identifier */
    public static final UmlModelEntity MULTIPLICITY;
    /** MultiplicityRange identifier */
    public static final UmlModelEntity MULTIPLICITY_RANGE;
    /** Namespace identifier */
    public static final UmlModelEntity NAMESPACE;
    /** Node identifier */
    public static final UmlModelEntity NODE;
    /** NodeInstance identifier */
    public static final UmlModelEntity NODE_INSTANCE;
    /** Object identifier */
    public static final UmlModelEntity OBJECT;
    /** ObjectFlowState identifier */
    public static final UmlModelEntity OBJECT_FLOW_STATE;
    /** ObjectSetExpression identifier */
    public static final UmlModelEntity OBJECT_SET_EXPRESSION;
    /** Operation identifier */
    public static final UmlModelEntity OPERATION;
    /** Package identifier */
    public static final UmlModelEntity PACKAGE;
    /** Parameter identifier */
    public static final UmlModelEntity PARAMETER;
    /** Partition identifier */
    public static final UmlModelEntity PARTITION;
    /** Permission identifier */
    public static final UmlModelEntity PERMISSION;
    /** PresentationElement identifier */
    public static final UmlModelEntity PRESENTATION_ELEMENT;
    /** ProcedureExpression identifier */
    public static final UmlModelEntity PROCEDURE_EXPRESSION;
    /** Pseudostate identifier */
    public static final UmlModelEntity PSEUDOSTATE;
    /** Reception identifier */
    public static final UmlModelEntity RECEPTION;
    /** Relationship identifier */
    public static final UmlModelEntity RELATIONSHIP;
    /** ReturnAction identifier */
    public static final UmlModelEntity RETURN_ACTION;
    /** SendAction identifier */
    public static final UmlModelEntity SEND_ACTION;
    /** Signal identifier */
    public static final UmlModelEntity SIGNAL;
    /** SignalEvent identifier */
    public static final UmlModelEntity SIGNAL_EVENT;
    /** SimpleState identifier */
    public static final UmlModelEntity SIMPLE_STATE;
    /** State identifier */
    public static final UmlModelEntity STATE;
    /** StateMachine identifier */
    public static final UmlModelEntity STATE_MACHINE;
    /** StateVertex identifier */
    public static final UmlModelEntity STATE_VERTEX;
    /** Stereotype identifier */
    public static final UmlModelEntity STEREOTYPE;
    /** Stimulus identifier */
    public static final UmlModelEntity STIMULUS;
    /** StructuralFeature identifier */
    public static final UmlModelEntity STRUCTURAL_FEATURE;
    /** StubState identifier */
    public static final UmlModelEntity STUB_STATE;
    /** SubactivityState identifier */
    public static final UmlModelEntity SUBACTIVITY_STATE;
    /** SubmachineState identifier */
    public static final UmlModelEntity SUBMACHINE_STATE;
    /** Subsystem identifier */
    public static final UmlModelEntity SUBSYSTEM;
    /** SynchState identifier */
    public static final UmlModelEntity SYNCH_STATE;
    /** TaggedValue identifier */
    public static final UmlModelEntity TAGGED_VALUE;
    /** TemplateParameter identifier */
    public static final UmlModelEntity TEMPLATE_PARAMETER;
    /** TerminateAction identifier */
    public static final UmlModelEntity TERMINATE_ACTION;
    /** TimeEvent identifier */
    public static final UmlModelEntity TIME_EVENT;
    /** TimeExpression identifier */
    public static final UmlModelEntity TIME_EXPRESSION;
    /** Transition identifier */
    public static final UmlModelEntity TRANSITION;
    /** TypeExpression identifier */
    public static final UmlModelEntity TYPE_EXPRESSION;
    /** UninterpretedAction identifier */
    public static final UmlModelEntity UNINTERPRETED_ACTION;
    /** Usage identifier */
    public static final UmlModelEntity USAGE;
    /** UseCase identifier */
    public static final UmlModelEntity USE_CASE;
    /** UseCaseInstance identifier */
    public static final UmlModelEntity USE_CASE_INSTANCE;

    /** Initialize the marker classes and the list of valid Uml classes */

    static {

        Uml uml = new Uml();
        // Initialize all of the marker classes.

        ABSTRACTION             = new UmlModelEntity("Abstraction");
        ACTION                  = new UmlModelEntity("Action");
        ACTION_EXPRESSION       = new UmlModelEntity("ActionExpression");
        ACTION_SEQUENCE         = new UmlModelEntity("ActionSequence",
                                                      false, true);
        ACTION_STATE            = new UmlModelEntity("ActionState",
                                                     false, true);
        ACTIVITY_GRAPH          = new UmlModelEntity("ActivityGraph",
                                                     false, true);
        ACTOR                   = new UmlModelEntity("Actor");
        ARG_LISTS_EXPRESSION    = new UmlModelEntity("ArgListsExpression");
        ARGUMENT                = new UmlModelEntity("Argument");
        ASSOCIATION             = new UmlModelEntity("Association");
        ASSOCIATION_CLASS       = new UmlModelEntity("AssociationClass");
        ASSOCIATION_END         = new UmlModelEntity("AssociationEnd");
        ASSOCIATION_END_ROLE    = new UmlModelEntity("AssociationEndRole",
                                                     false, true);
        ASSOCIATION_ROLE        = new UmlModelEntity("AssociationRole");
        ATTRIBUTE               = new UmlModelEntity("Attribute");
        ATTRIBUTE_LINK          = new UmlModelEntity("AttributeLink");
        BEHAVIORAL_FEATURE      = new UmlModelEntity("BehavioralFeature",
                                                     false, true);
        BINDING                 = new UmlModelEntity("Binding");
        BOOLEAN_EXPRESSION      = new UmlModelEntity("BooleanExpression");
        CALL_ACTION             = new UmlModelEntity("CallAction");
        CALL_EVENT              = new UmlModelEntity("CallEvent",
                                                     false, true);
        CALL_STATE              = new UmlModelEntity("CallState");
        CHANGE_EVENT            = new UmlModelEntity("ChangeEvent");
        CLASS                   = new UmlModelEntity("Class");
        CLASSIFIER              = new UmlModelEntity("Classifier");
        CLASSIFIER_IN_STATE     = new UmlModelEntity("ClassifierInState");
        CLASSIFIER_ROLE         = new UmlModelEntity("ClassifierRole");
        COLLABORATION           = new UmlModelEntity("Collaboration",
                                                     false, true);
        COMMENT                 = new UmlModelEntity("Comment",
                                                     false, true);
        COMPONENT               = new UmlModelEntity("Component");
        COMPONENT_INSTANCE      = new UmlModelEntity("ComponentInstance");
        COMPOSITE_STATE         = new UmlModelEntity("CompositeState");
        CONSTRAINT              = new UmlModelEntity("Constraint",
                                                     false, true);
        CREATE_ACTION           = new UmlModelEntity("CreateAction",
                                                     false, true);
        DATA_VALUE              = new UmlModelEntity("DataValue",
                                                     false, true);
        DATATYPE                = new UmlModelEntity("DataType");
        DEPENDENCY              = new UmlModelEntity("Dependency");
        DESTROY_ACTION          = new UmlModelEntity("DestroyAction",
                                                     false, true);
        ELEMENT                 = new UmlModelEntity("Element",
                                                     false, true);
        ELEMENT_IMPORT          = new UmlModelEntity("ElementImport",
                                                     false, true);
        ELEMENT_RESIDENCE       = new UmlModelEntity("ElementResidence",
                                                     false, true);
        EVENT                   = new UmlModelEntity("Event");
        EXCEPTION               = new UmlModelEntity("Exception");
        EXPRESSION              = new UmlModelEntity("Expression",
                                                     false, true);
        EXTEND                  = new UmlModelEntity("Extend");
        EXTENSION_POINT         = new UmlModelEntity("ExtensionPoint",
                                                     false, true);
        FEATURE                 = new UmlModelEntity("Feature",
                                                     false, true);
        FINAL_STATE             = new UmlModelEntity("FinalState",
                                                     false, true);
        FLOW                    = new UmlModelEntity("Flow",
                                                     false, true);
        GENERALIZABLE_ELEMENT   = new UmlModelEntity("GeneralizableElement",
                                                     false, true);
        GENERALIZATION          = new UmlModelEntity("Generalization");
        GUARD                   = new UmlModelEntity("Guard",
                                                     false, true);
        INCLUDE                 = new UmlModelEntity("Include");
        INSTANCE                = new UmlModelEntity("Instance");
        INTERACTION             = new UmlModelEntity("Interaction",
                                                     false, true);
        INTERFACE               = new UmlModelEntity("Interface");
        ITERATION_EXPRESSION    = new UmlModelEntity("IterationExpression");
        LINK                    = new UmlModelEntity("Link");
        LINK_END                = new UmlModelEntity("LinkEnd");
        LINK_OBJECT             = new UmlModelEntity("LinkObject");
        MAPPING_EXPRESSION      = new UmlModelEntity("MappingExpression");
        MESSAGE                 = new UmlModelEntity("Message",
                                                     false, true);
        METHOD                  = new UmlModelEntity("Method",
                                                     false, true);
        MODEL                   = new UmlModelEntity("Model");
        MODEL_ELEMENT           = new UmlModelEntity("ModelElement",
                                                     false, true);
        MULTIPLICITY            = new UmlModelEntity("Multiplicity",
                                                     false, true);
        MULTIPLICITY_RANGE      = new UmlModelEntity("MultiplicityRange");
        NAMESPACE               = new UmlModelEntity("Namespace");
        NODE                    = new UmlModelEntity("Node");
        NODE_INSTANCE           = new UmlModelEntity("NodeInstance");
        OBJECT                  = new UmlModelEntity("Object");
        OBJECT_FLOW_STATE       = new UmlModelEntity("ObjectFlowState");
        OBJECT_SET_EXPRESSION   = new UmlModelEntity("ObjectSetExpression");
        OPERATION               = new UmlModelEntity("Operation");
        PACKAGE                 = new UmlModelEntity("Package");
        PARAMETER               = new UmlModelEntity("Parameter",
                                                     false, true);
        PARTITION               = new UmlModelEntity("Partition");
        PERMISSION              = new UmlModelEntity("Permission");
        PRESENTATION_ELEMENT    = new UmlModelEntity("PresentationElement");
        PROCEDURE_EXPRESSION    = new UmlModelEntity("ProcedureExpression");
        PSEUDOSTATE             = new UmlModelEntity("Pseudostate");
        RECEPTION               = new UmlModelEntity("Reception");
        RELATIONSHIP            = new UmlModelEntity("Relationship",
                                                     false, true);
        RETURN_ACTION           = new UmlModelEntity("ReturnAction",
                                                     false, true);
        SEND_ACTION             = new UmlModelEntity("SendAction",
                                                     false, true);
        SIGNAL                  = new UmlModelEntity("Signal",
                                                     false, true);
        SIGNAL_EVENT            = new UmlModelEntity("SignalEvent");
        SIMPLE_STATE            = new UmlModelEntity("SimpleState");
        STATE                   = new UmlModelEntity("State");
        STATE_MACHINE           = new UmlModelEntity("StateMachine",
                                                     false, true);
        STATE_VERTEX            = new UmlModelEntity("StateVertex",
                                                     false, true);
        STEREOTYPE              = new UmlModelEntity("Stereotype");
        STIMULUS                = new UmlModelEntity("Stimulus",
                                                     false, true);
        STRUCTURAL_FEATURE      = new UmlModelEntity("StructuralFeature",
                                                     false, true);
        STUB_STATE              = new UmlModelEntity("StubState");
        SUBACTIVITY_STATE       = new UmlModelEntity("SubactivityState");
        SUBMACHINE_STATE        = new UmlModelEntity("SubmachineState");
        SUBSYSTEM               = new UmlModelEntity("Subsystem",
                                                     false, true);
        SYNCH_STATE             = new UmlModelEntity("SynchState");
        TAGGED_VALUE            = new UmlModelEntity("TaggedValue",
                                                     false, true);
        TEMPLATE_PARAMETER      = new UmlModelEntity("TemplateParameter");
        TERMINATE_ACTION        = new UmlModelEntity("TerminateAction");
        TIME_EVENT              = new UmlModelEntity("TimeEvent");
        TIME_EXPRESSION         = new UmlModelEntity("TimeExpression");
        TRANSITION              = new UmlModelEntity("Transition");
        TYPE_EXPRESSION         = new UmlModelEntity("TypeExpression");
        UNINTERPRETED_ACTION    = new UmlModelEntity("UninterpretedAction");
        USAGE                   = new UmlModelEntity("Usage");
        USE_CASE                = new UmlModelEntity("UseCase");
        USE_CASE_INSTANCE       = new UmlModelEntity("UseCaseInstance");

        // Add the marker classes to the list
        umlClassList = new HashSet(110);

        umlClassList.add(Uml.ABSTRACTION);
        umlClassList.add(Uml.ACTION);
        umlClassList.add(Uml.ACTION_EXPRESSION);
        umlClassList.add(Uml.ACTION_SEQUENCE);
        umlClassList.add(Uml.ACTION_STATE);
        umlClassList.add(Uml.ACTIVITY_GRAPH);
        umlClassList.add(Uml.ACTOR);
        umlClassList.add(Uml.ACTOR);
        umlClassList.add(Uml.ARG_LISTS_EXPRESSION);
        umlClassList.add(Uml.ARGUMENT);
        umlClassList.add(Uml.ASSOCIATION);
        umlClassList.add(Uml.ASSOCIATION_CLASS);
        umlClassList.add(Uml.ASSOCIATION_END);
        umlClassList.add(Uml.ASSOCIATION_END_ROLE);
        umlClassList.add(Uml.ASSOCIATION_ROLE);
        umlClassList.add(Uml.ATTRIBUTE);
        umlClassList.add(Uml.ATTRIBUTE_LINK);
        umlClassList.add(Uml.BEHAVIORAL_FEATURE);
        umlClassList.add(Uml.BINDING);
        umlClassList.add(Uml.BOOLEAN_EXPRESSION);
        umlClassList.add(Uml.CALL_ACTION);
        umlClassList.add(Uml.CALL_EVENT);
        umlClassList.add(Uml.CALL_STATE);
        umlClassList.add(Uml.CHANGE_EVENT);
        umlClassList.add(Uml.CLASS);
        umlClassList.add(Uml.CLASSIFIER);
        umlClassList.add(Uml.CLASSIFIER_IN_STATE);
        umlClassList.add(Uml.CLASSIFIER_ROLE);
        umlClassList.add(Uml.COLLABORATION);
        umlClassList.add(Uml.COMMENT);
        umlClassList.add(Uml.COMPONENT);
        umlClassList.add(Uml.COMPONENT_INSTANCE);
        umlClassList.add(Uml.COMPOSITE_STATE);
        umlClassList.add(Uml.CONSTRAINT);
        umlClassList.add(Uml.CREATE_ACTION);
        umlClassList.add(Uml.DATATYPE);
        umlClassList.add(Uml.DATA_VALUE);
        umlClassList.add(Uml.DEPENDENCY);
        umlClassList.add(Uml.DESTROY_ACTION);
        umlClassList.add(Uml.ELEMENT);
        umlClassList.add(Uml.ELEMENT_IMPORT);
        umlClassList.add(Uml.ELEMENT_RESIDENCE);
        umlClassList.add(Uml.EVENT);
        umlClassList.add(Uml.EXCEPTION);
        umlClassList.add(Uml.EXPRESSION);
        umlClassList.add(Uml.EXTEND);
        umlClassList.add(Uml.EXTENSION_POINT);
        umlClassList.add(Uml.FEATURE);
        umlClassList.add(Uml.FINAL_STATE);
        umlClassList.add(Uml.FLOW);
        umlClassList.add(Uml.GENERALIZABLE_ELEMENT);
        umlClassList.add(Uml.GENERALIZATION);
        umlClassList.add(Uml.GUARD);
        umlClassList.add(Uml.INCLUDE);
        umlClassList.add(Uml.INSTANCE);
        umlClassList.add(Uml.INTERACTION);
        umlClassList.add(Uml.INTERFACE);
        umlClassList.add(Uml.ITERATION_EXPRESSION);
        umlClassList.add(Uml.LINK);
        umlClassList.add(Uml.LINK_END);
        umlClassList.add(Uml.LINK_OBJECT);
        umlClassList.add(Uml.MAPPING_EXPRESSION);
        umlClassList.add(Uml.MESSAGE);
        umlClassList.add(Uml.METHOD);
        umlClassList.add(Uml.MODEL);
        umlClassList.add(Uml.MODEL_ELEMENT);
        umlClassList.add(Uml.MULTIPLICITY);
        umlClassList.add(Uml.MULTIPLICITY_RANGE);
        umlClassList.add(Uml.NAMESPACE);
        umlClassList.add(Uml.NODE);
        umlClassList.add(Uml.NODE_INSTANCE);
        umlClassList.add(Uml.OBJECT);
        umlClassList.add(Uml.OBJECT_FLOW_STATE);
        umlClassList.add(Uml.OBJECT_SET_EXPRESSION);
        umlClassList.add(Uml.OPERATION);
        umlClassList.add(Uml.PACKAGE);
        umlClassList.add(Uml.PARAMETER);
        umlClassList.add(Uml.PARTITION);
        umlClassList.add(Uml.PERMISSION);
        umlClassList.add(Uml.PRESENTATION_ELEMENT);
        umlClassList.add(Uml.PROCEDURE_EXPRESSION);
        umlClassList.add(Uml.PSEUDOSTATE);
        umlClassList.add(Uml.RECEPTION);
        umlClassList.add(Uml.RELATIONSHIP);
        umlClassList.add(Uml.RETURN_ACTION);
        umlClassList.add(Uml.SEND_ACTION);
        umlClassList.add(Uml.SIGNAL);
        umlClassList.add(Uml.SIGNAL_EVENT);
        umlClassList.add(Uml.SIMPLE_STATE);
        umlClassList.add(Uml.STATE);
        umlClassList.add(Uml.STATE_MACHINE);
        umlClassList.add(Uml.STATE_VERTEX);
        umlClassList.add(Uml.STEREOTYPE);
        umlClassList.add(Uml.STIMULUS);
        umlClassList.add(Uml.STRUCTURAL_FEATURE);
        umlClassList.add(Uml.STUB_STATE);
        umlClassList.add(Uml.SUBACTIVITY_STATE);
        umlClassList.add(Uml.SUBMACHINE_STATE);
        umlClassList.add(Uml.SUBSYSTEM);
        umlClassList.add(Uml.SYNCH_STATE);
        umlClassList.add(Uml.TAGGED_VALUE);
        umlClassList.add(Uml.TEMPLATE_PARAMETER);
        umlClassList.add(Uml.TERMINATE_ACTION);
        umlClassList.add(Uml.TIME_EVENT);
        umlClassList.add(Uml.TIME_EXPRESSION);
        umlClassList.add(Uml.TRANSITION);
        umlClassList.add(Uml.TYPE_EXPRESSION);
        umlClassList.add(Uml.UNINTERPRETED_ACTION);
        umlClassList.add(Uml.USAGE);
        umlClassList.add(Uml.USE_CASE);
        umlClassList.add(Uml.USE_CASE_INSTANCE);

        // Create a name to entity map
        xref = new HashMap(110);
        Iterator i = umlClassList.iterator();
        while (i.hasNext()) {
            UmlModelEntity e = (UmlModelEntity) i.next();
            xref.put(e.getName(), e);
        }
    }

    /**
     * @param name of the object
     * @return the ModelEntity or null
     */
    public static UmlModelEntity getDeclaredType(String name) {
        return (UmlModelEntity) xref.get(name);
    }

}
