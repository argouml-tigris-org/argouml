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

package uci.xml.xmi;

import java.util.*;
import java.io.*;
import java.net.URL;

import uci.xml.SAXParserBase;
import uci.xml.XMLElement;

public class XMIParserBase extends SAXParserBase {

  ////////////////////////////////////////////////////////////////
  // static variables

  protected static String        _lastId        = null;
  protected static Object        _lastReference = null;
  protected static Hashtable     _seen          = new Hashtable();

  private   static XMITokenTable _tokens        = new XMITokenTable();
  private   static Vector        _openTags      = new Vector();
  private   static int           _elementIdMax  = 0;

  ////////////////////////////////////////////////////////////////
  // constructors

  protected XMIParserBase() { super(); }

  ////////////////////////////////////////////////////////////////
  // accessors

  public String  getLastId()             { return _lastId; }
  public int     getElementIdMax()       { return _elementIdMax; }

  ////////////////////////////////////////////////////////////////
  // XML element handlers

  public void handleStartElement(XMLElement e) {
    if (_dbg) System.out.println("NOTE: XMIParserBase handleStartTag:" +
				 e.getName());
    try {
      switch (_tokens.toXMIToken(e.getName(), true)) {
      case _tokens.TOKEN_name: handle_name(e); break;
      case _tokens.TOKEN_XMI : handleXMI(e);  break;
      case _tokens.TOKEN_XMI_header : handleXMIHeader(e);  break;
      case _tokens.TOKEN_XMI_documentation : handleXMIDocumentation(e);  break;
      case _tokens.TOKEN_XMI_owner : handleXMIOwner(e);  break;
      case _tokens.TOKEN_XMI_contact : handleXMIContact(e);  break;
      case _tokens.TOKEN_XMI_longDescription :
	handleXMILongDescription(e);  break;
      case _tokens.TOKEN_XMI_shortDescription :
	handleXMIShortDescription(e);  break;
      case _tokens.TOKEN_XMI_exporter : handleXMIExporter(e);  break;
      case _tokens.TOKEN_XMI_exporterVersion :
	handleXMIExporterVersion(e);  break;
      case _tokens.TOKEN_XMI_exporterID : handleXMIExporterID(e);  break;
      case _tokens.TOKEN_XMI_notice : handleXMINotice(e);  break;
      case _tokens.TOKEN_XMI_metamodel : handleXMIMetamodel(e);  break;
      case _tokens.TOKEN_XMI_content : handleXMIContent(e);  break;
      case _tokens.TOKEN_XMI_extensions : handleXMIExtensions(e);  break;
      case _tokens.TOKEN_XMI_reference : handleXMIReference(e);  break;
      case _tokens.TOKEN_XMI_field : handleXMIField(e);  break;
      case _tokens.TOKEN_XMI_struct : handleXMIStruct(e);  break;
      case _tokens.TOKEN_XMI_seqItem : handleXMISeqItem(e);  break;
      case _tokens.TOKEN_XMI_sequence : handleXMISequence(e);  break;
      case _tokens.TOKEN_XMI_arrayLen : handleXMIArrayLen(e);  break;
      case _tokens.TOKEN_XMI_arrayItem : handleXMIArrayItem(e);  break;
      case _tokens.TOKEN_XMI_array : handleXMIArray(e);  break;
      case _tokens.TOKEN_XMI_enum : handleXMIEnum(e);  break;
      case _tokens.TOKEN_XMI_discrim : handleXMIDiscrim(e);  break;
      case _tokens.TOKEN_XMI_union : handleXMIUnion(e);  break;
      case _tokens.TOKEN_XMI_any : handleXMIAny(e);  break;
      case _tokens.TOKEN_XMI_extension : handleXMIExtension(e);  break;
      case _tokens.TOKEN_XMI_remoteContent : handleXMIRemoteContent(e);  break;
      case _tokens.TOKEN_visibility : handle_visibility(e);  break;
      case _tokens.TOKEN_taggedValue : handle_taggedValue(e);  break;
      case _tokens.TOKEN_behavior : handle_behavior(e);  break;
      case _tokens.TOKEN_ownerScope : handle_ownerScope(e);  break;
      case _tokens.TOKEN_isQuery : handle_isQuery(e);  break;
      case _tokens.TOKEN_parameter : handle_parameter(e);  break;
      case _tokens.TOKEN_ownedElement : handle_ownedElement(e);  break;
      case _tokens.TOKEN_isAbstract : handle_isAbstract(e);  break;
      case _tokens.TOKEN_isLeaf : handle_isLeaf(e);  break;
      case _tokens.TOKEN_isRoot : handle_isRoot(e);  break;
      case _tokens.TOKEN_feature : handle_feature(e);  break;
      case _tokens.TOKEN_changeable : handle_changeable(e);  break;
      case _tokens.TOKEN_targetScope : handle_targetScope(e);  break;
      case _tokens.TOKEN_connection : handle_connection(e);  break;
      case _tokens.TOKEN_Association : handleAssociation(e);  break;
      case _tokens.TOKEN_isActive : handle_isActive(e);  break;
      case _tokens.TOKEN_Class : handleClass(e);  break;
      case _tokens.TOKEN_AssociationClass : handleAssociationClass(e);  break;
      case _tokens.TOKEN_isNavigable : handle_isNavigable(e);  break;
      case _tokens.TOKEN_isOrdered : handle_isOrdered(e);  break;
      case _tokens.TOKEN_aggregation : handle_aggregation(e);  break;
      case _tokens.TOKEN_qualifier : handle_qualifier(e);  break;
      case _tokens.TOKEN_AssociationEnd : handleAssociationEnd(e);  break;
      case _tokens.TOKEN_Attribute : handleAttribute(e);  break;
      case _tokens.TOKEN_Constraint : handleConstraint(e);  break;
      case _tokens.TOKEN_DataType : handleDataType(e);  break;
      case _tokens.TOKEN_description : handleDescription(e);  break;
      case _tokens.TOKEN_subDependencies : handle_subDependencies(e);  break;
      case _tokens.TOKEN_Dependency : handleDependency(e);  break;
      case _tokens.TOKEN_discriminator : handle_discriminator(e);  break;
      case _tokens.TOKEN_Generalization : handleGeneralization(e);  break;
      case _tokens.TOKEN_Interface : handleInterface(e);  break;
      case _tokens.TOKEN_Method : handleMethod(e);  break;
      case _tokens.TOKEN_concurrency : handle_concurrency(e);  break;
      case _tokens.TOKEN_isPolymorphic : handle_isPolymorphic(e);  break;
      case _tokens.TOKEN_Operation : handleOperation(e);  break;
      case _tokens.TOKEN_kind : handle_kind(e);  break;
      case _tokens.TOKEN_Parameter : handleParameter(e);  break;
      case _tokens.TOKEN_Binding : handleBinding(e);  break;
      case _tokens.TOKEN_Comment : handleComment(e);  break;
      case _tokens.TOKEN_Component : handleComponent(e);  break;
      case _tokens.TOKEN_Node : handleNode(e);  break;
      case _tokens.TOKEN_geometry : handle_geometry(e);  break;
      case _tokens.TOKEN_style : handle_style(e);  break;
      case _tokens.TOKEN_Presentation : handlePresentation(e);  break;
      case _tokens.TOKEN_mapping : handle_mapping(e);  break;
      case _tokens.TOKEN_Refinement : handleRefinement(e);  break;
      case _tokens.TOKEN_Trace : handleTrace(e);  break;
      case _tokens.TOKEN_Usage : handleUsage(e);  break;
      case _tokens.TOKEN_icon : handle_icon(e);  break;
      case _tokens.TOKEN_requiredTag : handle_requiredTag(e);  break;
      case _tokens.TOKEN_Stereotype : handleStereotype(e);  break;
      case _tokens.TOKEN_TaggedValue : handleTaggedValue(e);  break;
      case _tokens.TOKEN_Primitive : handlePrimitive(e);  break;
      case _tokens.TOKEN_EnumerationLiteral :
	handleEnumerationLiteral(e);  break;
      case _tokens.TOKEN_literal : handle_literal(e);  break;
      case _tokens.TOKEN_Enumeration : handleEnumeration(e);  break;
      case _tokens.TOKEN_Structure : handleStructure(e);  break;
      case _tokens.TOKEN_recurrence : handle_recurrence(e);  break;
      case _tokens.TOKEN_target : handle_target(e);  break;
      case _tokens.TOKEN_isAsynchronous : handle_isAsynchronous(e);  break;
      case _tokens.TOKEN_script : handle_script(e);  break;
      case _tokens.TOKEN_actualArgument : handle_actualArgument(e);  break;
      case _tokens.TOKEN_slot : handle_slot(e);  break;
      case _tokens.TOKEN_Request : handleRequest(e);  break;
      case _tokens.TOKEN_action : handle_action(e);  break;
      case _tokens.TOKEN_ActionSequence : handleActionSequence(e);  break;
      case _tokens.TOKEN_argumentValue : handle_argumentValue(e);  break;
      case _tokens.TOKEN_Argument : handleArgument(e);  break;
      case _tokens.TOKEN_attributeValue : handle_attributeValue(e);  break;
      case _tokens.TOKEN_AttributeLink : handleAttributeLink(e);  break;
      case _tokens.TOKEN_mode : handle_mode(e);  break;
      case _tokens.TOKEN_CallAction : handleCallAction(e);  break;
      case _tokens.TOKEN_CreateAction : handleCreateAction(e);  break;
      case _tokens.TOKEN_DestroyAction : handleDestroyAction(e);  break;
      case _tokens.TOKEN_DataValue : handleDataValue(e);  break;
      case _tokens.TOKEN_Signal : handleSignal(e);  break;
      case _tokens.TOKEN_Exception : handleException(e);  break;
      case _tokens.TOKEN_linkRole : handle_linkRole(e);  break;
      case _tokens.TOKEN_Link : handleLink(e);  break;
      case _tokens.TOKEN_LinkEnd : handleLinkEnd(e);  break;
      case _tokens.TOKEN_Object : handleObject(e);  break;
      case _tokens.TOKEN_LinkObject : handleLinkObject(e);  break;
      case _tokens.TOKEN_LocalInvocation : handleLocalInvocation(e);  break;
      case _tokens.TOKEN_MessageInstance : handleMessageInstance(e);  break;
      case _tokens.TOKEN_Reception : handleReception(e);  break;
      case _tokens.TOKEN_ReturnAction : handleReturnAction(e);  break;
      case _tokens.TOKEN_SendAction : handleSendAction(e);  break;
      case _tokens.TOKEN_TerminateAction : handleTerminateAction(e);  break;
      case _tokens.TOKEN_UninterpretedAction :
	handleUninterpretedAction(e);  break;
      case _tokens.TOKEN_AssociationEndRole :
	handleAssociationEndRole(e);  break;
      case _tokens.TOKEN_AssociationRole : handleAssociationRole(e);  break;
      case _tokens.TOKEN_availableFeature : handle_availableFeature(e);  break;
      case _tokens.TOKEN_ClassifierRole : handleClassifierRole(e);  break;
      case _tokens.TOKEN_interaction : handle_interaction(e);  break;
      case _tokens.TOKEN_Collaboration : handleCollaboration(e);  break;
      case _tokens.TOKEN_Interaction : handleInteraction(e);  break;
      case _tokens.TOKEN_Message : handleMessage(e);  break;
      case _tokens.TOKEN_Actor : handleActor(e);  break;
      case _tokens.TOKEN_extensionPoint : handle_extensionPoint(e);  break;
      case _tokens.TOKEN_UseCase : handleUseCase(e);  break;
      case _tokens.TOKEN_UseCaseInstance : handleUseCaseInstance(e);  break;
      case _tokens.TOKEN_CallEvent : handleCallEvent(e);  break;
      case _tokens.TOKEN_changeExpression : handle_changeExpression(e);  break;
      case _tokens.TOKEN_ChangeEvent : handleChangeEvent(e);  break;
      case _tokens.TOKEN_entry : handle_entry(e);  break;
      case _tokens.TOKEN_exit : handle_exit(e);  break;
      case _tokens.TOKEN_internalTransition :
	handle_internalTransition(e);  break;
      case _tokens.TOKEN_State : handleState(e);  break;
      case _tokens.TOKEN_isConcurrent : handle_isConcurrent(e);  break;
      case _tokens.TOKEN_substate : handle_substate(e);  break;
      case _tokens.TOKEN_CompositeState : handleCompositeState(e);  break;
      case _tokens.TOKEN_Guard : handleGuard(e);  break;
      case _tokens.TOKEN_stateKind : handle_stateKind(e);  break;
      case _tokens.TOKEN_PseudoState : handlePseudostate(e);  break;
      case _tokens.TOKEN_SignalEvent : handleSignalEvent(e);  break;
      case _tokens.TOKEN_SimpleState : handleSimpleState(e);  break;
      case _tokens.TOKEN_top : handle_top(e);  break;
      case _tokens.TOKEN_transitions : handle_transitions(e);  break;
      case _tokens.TOKEN_StateMachine : handleStateMachine(e);  break;
      case _tokens.TOKEN_SubmachineState : handleSubmachineState(e);  break;
      case _tokens.TOKEN_TimeEvent : handleTimeEvent(e);  break;
      case _tokens.TOKEN_trigger : handle_trigger(e);  break;
      case _tokens.TOKEN_guard : handle_guard(e);  break;
      case _tokens.TOKEN_effect : handle_effect(e);  break;
      case _tokens.TOKEN_Transition : handleTransition(e);  break;
      case _tokens.TOKEN_partition : handle_partition(e);  break;
      case _tokens.TOKEN_ActivityModel : handleActivityModel(e);  break;
      case _tokens.TOKEN_ActionState : handleActionState(e);  break;
      case _tokens.TOKEN_ActivityState : handleActivityState(e);  break;
      case _tokens.TOKEN_ClassifierInState :
	handleClassifierInState(e);  break;
      case _tokens.TOKEN_ObjectFlowState : handleObjectFlowState(e);  break;
      case _tokens.TOKEN_Partition : handlePartition(e);  break;
      case _tokens.TOKEN_alias : handle_alias(e);  break;
      case _tokens.TOKEN_ElementReference : handleElementReference(e);  break;
      case _tokens.TOKEN_elementReference : handle_elementReference(e);  break;
      case _tokens.TOKEN_Package : handlePackage(e);  break;
      case _tokens.TOKEN_Model : handleModel(e);  break;
      case _tokens.TOKEN_isInstantiable : handle_isInstantiable(e);  break;
      case _tokens.TOKEN_Subsystem : handleSubsystem(e);  break;
      default: if (_dbg)
	System.out.println("WARNING: unknown tag:" + e.getName());  break;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    _openTags.addElement(e.getName());
  }

  public void handleEndElement(XMLElement e) {
    if (_dbg) System.out.println("NOTE: XMIParserBase handleEndTag:" +
				 e.getName()+".");
    try {
      switch (_tokens.toXMIToken(e.getName(), false)) {
      case _tokens.TOKEN_name: handle_name(e); break;
      case _tokens.TOKEN_tag : handle_tag(e); break;
      case _tokens.TOKEN_value : handle_value(e); break;
      case _tokens.TOKEN_baseClass : handle_baseClass(e); break;
      case _tokens.TOKEN_initialValue : handle_initialValue(e); break;
      case _tokens.TOKEN_operationSpecification :
	handle_operationSpecification(e); break;
      case _tokens.TOKEN_defaultValue : handle_defaultValue(e); break;
      case _tokens.TOKEN_expression : handleExpression(e); break;
      case _tokens.TOKEN_duration : handle_duration(e); break;
      case _tokens.TOKEN_Model : handleModelEnd(e); break;
      case _tokens.TOKEN_Collaboration : handleCollaborationEnd(e); break;
      case _tokens.TOKEN_subtype : handle_subtype(e); break;
      case _tokens.TOKEN_supertype : handle_supertype(e); break;
      case _tokens.TOKEN_type : handle_type(e); break;
      case _tokens.TOKEN_realization : handle_realization(e); break;
      case _tokens.TOKEN_specification : handle_specification(e); break;
      case _tokens.TOKEN_client : handle_client(e); break;
      case _tokens.TOKEN_supplier : handle_supplier(e); break;
      case _tokens.TOKEN_namespace : handle_namespace(e); break;
      case _tokens.TOKEN_stereotype : handle_stereotype(e); break;
      case _tokens.TOKEN_constraint : handle_constraint(e); break;
      case _tokens.TOKEN_provision : handle_provision(e); break;
      case _tokens.TOKEN_requirement : handle_requirement(e); break;
      case _tokens.TOKEN_templateParameter :
	handle_templateParameter(e); break;
      case _tokens.TOKEN_template : handle_template(e); break;
      case _tokens.TOKEN_implementation : handle_implementation(e); break;
      case _tokens.TOKEN_owner : handle_owner(e); break;
      case _tokens.TOKEN_raisedException : handle_raisedException(e); break;
      case _tokens.TOKEN_generalization : handle_generalization(e); break;
      case _tokens.TOKEN_specialization : handle_specialization(e); break;
      case _tokens.TOKEN_participant : handle_participant(e); break;
      case _tokens.TOKEN_associationEnd : handle_associationEnd(e); break;
      case _tokens.TOKEN_constrainedElement :
	handle_constrainedElement(e); break;
      case _tokens.TOKEN_constrainedStereotype :
	handle_constrainedStereotype(e); break;
      case _tokens.TOKEN_owningDependency : handle_owningDependency(e); break;
      case _tokens.TOKEN_occurrence : handle_occurrence(e); break;
      case _tokens.TOKEN_argument : handle_argument(e); break;
      case _tokens.TOKEN_deployment : handle_deployment(e); break;
      case _tokens.TOKEN_modelElement : handle_modelElement(e); break;
      case _tokens.TOKEN_model : handle_model(e); break;
      case _tokens.TOKEN_stereotypeConstraint :
	handle_stereotypeConstraint(e); break;
      case _tokens.TOKEN_extendedElement : handle_extendedElement(e); break;
      case _tokens.TOKEN_request : handle_request(e); break;
      case _tokens.TOKEN_linkEnd : handle_linkEnd(e); break;
      case _tokens.TOKEN_classifier : handle_classifier(e); break;
      case _tokens.TOKEN_attribute : handle_attribute(e); break;
      case _tokens.TOKEN_instantiation : handle_instantiation(e); break;
      case _tokens.TOKEN_reception : handle_reception(e); break;
      case _tokens.TOKEN_context : handle_context(e); break;
      case _tokens.TOKEN_association : handle_association(e); break;
      case _tokens.TOKEN_instance : handle_instance(e); break;
      case _tokens.TOKEN_receiver : handle_receiver(e); break;
      case _tokens.TOKEN_sender : handle_sender(e); break;
      case _tokens.TOKEN_signal : handle_signal(e); break;
      case _tokens.TOKEN_base : handle_base(e); break;
      case _tokens.TOKEN_constrainingElement :
	handle_constrainingElement(e); break;
      case _tokens.TOKEN_representedClassifier :
	handle_representedClassifier(e); break;
      case _tokens.TOKEN_representedOperation :
	handle_representedOperation(e);
	break;
      case _tokens.TOKEN_message : handle_message(e); break;
      case _tokens.TOKEN_messageRef : handle_messageRef(e); break;
      case _tokens.TOKEN_activator : handle_activator(e); break;
      case _tokens.TOKEN_messageAction : handle_messageAction(e); break;
      case _tokens.TOKEN_predecessor : handle_predecessor(e); break;
      case _tokens.TOKEN_parent : handle_parent(e); break;
      case _tokens.TOKEN_outgoing : handle_outgoing(e); break;
      case _tokens.TOKEN_incoming : handle_incoming(e); break;
      case _tokens.TOKEN_operation : handle_operation(e); break;
      case _tokens.TOKEN_deferredEvent : handle_deferredEvent(e); break;
      case _tokens.TOKEN_submachine : handle_submachine(e); break;
      case _tokens.TOKEN_sourceState : handle_sourceState(e); break;
      case _tokens.TOKEN_targetState : handle_targetState(e); break;
      case _tokens.TOKEN_inState : handle_inState(e); break;
      case _tokens.TOKEN_typeState : handle_typeState(e); break;
      case _tokens.TOKEN_contents : handle_contents(e); break;
      case _tokens.TOKEN_referencedElement : handle_referencedElement(e); break;
      case _tokens.TOKEN_body : handle_body(e); break;
      case _tokens.TOKEN_multiplicity : handle_multiplicity(e); break;
      case _tokens.TOKEN_Class : handleClassEnd(e); break;
      case _tokens.TOKEN_DataType : handleDataTypeEnd(e); break;
      default : if (_dbg)
	System.out.println("WARNING: unknown end tag:" + e.getName()); break;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    _openTags.removeElementAt(_openTags.size() - 1);
  }

  protected void handleXMI(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIHeader(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIDocumentation(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIOwner(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIContact(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMILongDescription(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIShortDescription(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIExporter(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIExporterVersion(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIExporterID(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMINotice(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIMetamodel(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIContent(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIExtensions(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIReference(XMLElement e) throws Exception {
    String ref = e.getAttribute("target");
    _lastReference = lookUpReference(ref);
  }
  protected void handleXMIField(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIStruct(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMISeqItem(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMISequence(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIArrayLen(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIArrayItem(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIArray(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIEnum(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIDiscrim(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIUnion(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIAny(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIExtension(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleXMIRemoteContent(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_name(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_visibility(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_namespace(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_stereotype(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_constraint(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_provision(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_requirement(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_templateParameter(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_template(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_implementation(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_taggedValue(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_behavior(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_ownerScope(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_owner(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isQuery(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_raisedException(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_parameter(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_ownedElement(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isAbstract(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isLeaf(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isRoot(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_generalization(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_specialization(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_participant(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_realization(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_specification(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_associationEnd(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_feature(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_changeable(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_multiplicity(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_targetScope(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_type(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_connection(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleAssociation(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isActive(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleClass(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleClassEnd(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleAssociationClass(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isNavigable(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isOrdered(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_aggregation(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_qualifier(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleAssociationEnd(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_initialValue(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleAttribute(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_body(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_constrainedElement(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_constrainedStereotype(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleConstraint(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleDataType(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleDataTypeEnd(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleDescription(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_owningDependency(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_client(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_supplier(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_subDependencies(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleDependency(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_discriminator(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_subtype(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_supertype(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleGeneralization(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleInterface(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleMethod(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_concurrency(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isPolymorphic(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_operationSpecification(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_occurrence(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleOperation(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_defaultValue(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_kind(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleParameter(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_argument(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleBinding(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleComment(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_deployment(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_modelElement(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleComponent(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_component(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleNode(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_geometry(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_style(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_model(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handlePresentation(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_mapping(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleRefinement(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleTrace(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleUsage(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_baseClass(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_icon(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_stereotypeConstraint(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_extendedElement(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_requiredTag(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleStereotype(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleTaggedValue(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_tag(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_value(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handlePrimitive(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleEnumerationLiteral(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_literal(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleEnumeration(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleStructure(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_recurrence(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_target(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isAsynchronous(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_script(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_request(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_actualArgument(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_linkEnd(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_classifier(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_slot(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleRequest(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_action(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleActionSequence(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_argumentValue(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleArgument(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_attributeValue(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_attribute(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleAttributeLink(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_mode(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleCallAction(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_instantiation(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleCreateAction(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleDestroyAction(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleDataValue(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_reception(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleSignal(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_context(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleException(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_association(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_linkRole(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleLink(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_instance(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleLinkEnd(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleObject(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleLinkObject(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleLocalInvocation(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_receiver(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_sender(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleMessageInstance(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_signal(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleReception(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleReturnAction(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleSendAction(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleTerminateAction(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleUninterpretedAction(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_base(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleAssociationEndRole(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleAssociationRole(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_availableFeature(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleClassifierRole(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_constrainingElement(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_representedClassifier(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_representedOperation(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_interaction(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleCollaboration(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleCollaborationEnd(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_message(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_messageRef(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleInteraction(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_activator(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_messageAction(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_predecessor(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleMessage(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleActor(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_extensionPoint(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleUseCase(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleUseCaseInstance(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_parent(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_outgoing(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_incoming(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_operation(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleCallEvent(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_changeExpression(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleChangeEvent(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_deferredEvent(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_entry(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_exit(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_internalTransition(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isConcurrent(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_substate(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleCompositeState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleExpression(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleGuard(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_stateKind(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handlePseudostate(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleSignalEvent(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleSimpleState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_top(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_transitions(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleStateMachine(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_submachine(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleSubmachineState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_duration(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleTimeEvent(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_sourceState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_targetState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_trigger(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_guard(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_effect(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleTransition(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handlePartition(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleActivityModel(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleActionState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleActivityState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_inState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleClassifierInState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_typeState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleObjectFlowState(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_contents(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_partition(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_alias(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_referencedElement(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleElementReference(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_elementReference(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handlePackage(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleModel(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleModelEnd(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handle_isInstantiable(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }
  protected void handleSubsystem(XMLElement e) throws Exception
  { if (_dbg) notImplemented(e); }


  ////////////////////////////////////////////////////////////////
  // internal model-building methods

  protected boolean inTag(String tagName) {
    return _openTags.contains(tagName);
  }

  protected Object findOrCreate(XMLElement e, java.lang.Class cls) {
    _lastId = e.getAttribute("XMI.id");
    if (_lastId == null && _verbose) {
      System.out.println("WARNING: null XMI.id: "+ cls);
    }
    Object obj = null;
    if (_lastId != null && !_lastId.equals("")) obj = _seen.get(_lastId);
    if (obj != null) {
      if (_dbg) System.out.println("NOTE: found " + cls + " id=" + _lastId);
      if (obj.getClass() != cls && _verbose)
	System.out.println("WARNING: class mismatch on id=" + _lastId);
      return obj;
    }
    try {
      obj = cls.newInstance();
      int dotPos = _lastId.indexOf(".1");
      if (dotPos > -1) {
	try {
	  int idNum = Integer.parseInt(_lastId.substring(dotPos + 2));
	  _elementIdMax = Math.max(_elementIdMax, idNum);
	}
	catch (NumberFormatException ex) { }
      }
      if (_lastId != null && !_lastId.equals("")) _seen.put(_lastId, obj);
      if (_dbg) System.out.println("made " + cls + " id=" + _lastId);
      return obj;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  protected Object lookUpReference(String id) {
    if (id == null || id.equals("")) return null;
    return _seen.get(id);
  }


} /* end class XMIParserBase */
