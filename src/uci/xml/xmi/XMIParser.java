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
import java.beans.*;
import java.net.URL;

import uci.uml.ui.Project;
import uci.uml.ui.ProjectBrowser;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Collaborations.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Model_Management.*;
import uci.xml.*;

import com.ibm.xml.parser.*;
import org.w3c.dom.*;
//import org.xml.sax.SAXDriver;

public class XMIParser implements ElementHandler, TagHandler {

  ////////////////////////////////////////////////////////////////
  // constants

  public static int MAX_MODEL_NEST = 32;

  ////////////////////////////////////////////////////////////////
  // static variables

  public static XMIParser SINGLETON = new XMIParser();
  protected static Hashtable _seen = null;

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Project _proj = null;
  protected boolean _firstPass = true;
  protected boolean _dbg = true;
  protected Vector  _openTags = new Vector();
  protected int     _elementIdMax = 0;

  protected ModelElementImpl _curME = null;
  protected Object _lastReference = null;
  protected uci.uml.Foundation.Core.Namespace _curModel = null;
  protected uci.uml.Foundation.Core.Namespace _ModelStack[] =
  new uci.uml.Foundation.Core.Namespace[MAX_MODEL_NEST];
  protected int _numNestedModels = 0;
  protected MMPackage _curPackage = null;
  protected MMClass _curClass = null;
  protected Interface _curInterface = null;
  protected Association _curAssociation = null;
  protected AssociationClass _curAssociationClass = null;
  protected AssociationEnd _curAssociationEnd = null;
  protected AssociationRole _curAssociationRole = null;
  protected AssociationEndRole _curAssociationEndRole = null;
  protected Instance _curInstance = null;
  //protected Object _curObject = null;
  protected Link _curLink = null;
  protected LinkEnd _curLinkEnd = null;
  protected Generalization _curGeneralization = null;
  protected Realization _curRealization = null;
  protected Operation _curOperation = null;
  protected Attribute _curAttribute = null;
  protected StateMachine _curStateMachine = null;
  protected CompositeState _curCompositeState = null;
  protected UseCase _curUseCase = null;
  protected UseCaseInstance _curUseCaseInstance = null;
  protected Actor _curActor = null;
  protected Transition _curTransition = null;
  protected EnumerationLiteral _curEnumerationLiteral = null;
  protected uci.uml.Foundation.Data_Types.Enumeration _curEnumeration = null;
  protected MMAction _curAction = null;
  protected CreateAction _curCreateAction = null;
  protected DestroyAction _curDestroyAction = null;
  protected SendAction _curSendAction = null;
  protected Parameter _curParameter = null;
  protected TerminateAction _curTerminateAction = null;
  protected ReturnAction _curReturnAction = null;
  protected Reception _curReception = null;
  protected ActionSequence _curActionSequence = null;
  protected Request _curRequest = null;
  protected Stereotype _curStereotype = null;
  protected TaggedValue _curTaggedValue = null;
  protected Pseudostate _curPseudostate = null;
  protected Signal _curSignal = null;
  protected SignalEvent _curSignalEvent = null;
  protected CallEvent _curCallEvent = null;
  protected ChangeEvent _curChangeEvent = null;
  protected State _curState = null;
  protected SimpleState _curSimpleState = null;
  protected ObjectFlowState _curObjectFlowState = null;
  protected Guard _curGuard = null;
  protected Expression _curExpression = null;
  protected SubmachineState _curSubmachineState = null;
  protected TimeEvent _curTimeEvent = null;
  protected Partition _curPartition = null;
  protected ActivityModel _curActivityModel = null;
  protected ActionState _curActionState = null;
  protected ActivityState _curActivityState = null;
  protected ClassifierRole _curClassifierRole = null;
  protected ClassifierInState _curClassifierInState = null;
  protected ElementReference _curElementReference = null;
  protected Subsystem _curSubsystem = null;
  protected Message _curMessage = null;
  protected Interaction _curInteraction = null;
  protected Collaboration _curCollaboration = null;
  protected Constraint _curConstraint = null;
  protected DataType _curDataType = null;
  protected Dependency _curDependency = null;
  protected Method _curMethod = null;
  //? protected Comment _curComment = null;
  protected Node _curNode = null;
  protected MMException _curException = null;
  protected LocalInvocation _curLocalInvocation = null;
  protected UninterpretedAction _curUninterpretedAction = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  protected XMIParser() { }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setIDs(Hashtable h) { _seen = h; }

  public void setProject(Project p) { _proj = p; }

  public uci.uml.Foundation.Core.Namespace getCurModel() { return _curModel; }

  ////////////////////////////////////////////////////////////////
  // main parsing methods

  public synchronized void readModels(Project p, URL url) {
    _proj = p;
    pass1(url);
    pass2(url);
    int ec = ElementImpl.getElementCount();
    ElementImpl.setElementCount(Math.max(ec, _elementIdMax + 1));
  }

  protected void pass1(URL url) {
    _firstPass = true;
    try {
      System.out.println("=======================================");
      System.out.println("== READING MODEL " + url);
      System.out.println("== PASS ONE");
      InputStream is = url.openStream();

      Parser pc = new Parser(url.getFile());
      pc.setEndBy1stError(false);
      pc.addElementHandler(this);
      pc.setTagHandler(this);
      pc.getEntityHandler().setEntityResolver(DTDEntityResolver.SINGLETON);
      //pc.setProcessExternalDTD(false);
//       if (_proj == null) {
// 	System.out.println("XMIParser made new project");
// 	_proj = new Project();
//       }
      pc.readStream(is);
      is.close();
    }
    catch (Exception ex) {
      System.out.println("Exception in pass1================");
      ex.printStackTrace();
    }
//     catch (FileNotFoundException fnfe) {
//       System.out.println("File not found: " + pathname + filename);
//       fnfe.printStackTrace();
//     }
  }

  protected void pass2(URL url) {
    _firstPass = false;
    try {
      System.out.println("== PASS TWO");
      InputStream is = url.openStream();

      Parser pc = new Parser(url.getFile());
      pc.setEndBy1stError(false);
      pc.addElementHandler(this);
      pc.setTagHandler(this);
      pc.getEntityHandler().setEntityResolver(DTDEntityResolver.SINGLETON);
      //pc.setProcessExternalDTD(false);
      pc.readStream(is);
      is.close();
    }
    catch (Exception ex) {
      System.out.println("Exception in pass2================");
      ex.printStackTrace();
    }
  }


  ////////////////////////////////////////////////////////////////
  // XML element handlers


  public TXElement handleElement(TXElement e) {
    //System.out.println("XMIParser handleElement:" + e.getName());
    String n = e.getName();
    try {
      if (n.equals("name")) handle_name(e);
    }
    catch (Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
    return e; // needs-more-work: too much memory? should return null.    
  }

  public void handleStartTag(TXElement e, boolean empty) {
    String n = e.getName();
    //System.out.println("XMIParser handleStartTag:" + e.getName());

    try {
      if (n.equals("XMI")) handleXMI(e);
      else if (n.equals("XMI.header")) handleXMIHeader(e);
      else if (n.equals("XMI.documentation")) handleXMIDocumentation(e);
      else if (n.equals("XMI.owner")) handleXMIOwner(e);
      else if (n.equals("XMI.contact")) handleXMIContact(e);
      else if (n.equals("XMI.longDescription")) handleXMILongDescription(e);
      else if (n.equals("XMI.shortDescription")) handleXMIShortDescription(e);
      else if (n.equals("XMI.exporter")) handleXMIExporter(e);
      else if (n.equals("XMI.exporterVersion")) handleXMIExporterVersion(e);
      else if (n.equals("XMI.exporterID")) handleXMIExporterID(e);
      else if (n.equals("XMI.notice")) handleXMINotice(e);
      else if (n.equals("XMI.metamodel")) handleXMIMetamodel(e);
      else if (n.equals("XMI.content")) handleXMIContent(e);
      else if (n.equals("XMI.extensions")) handleXMIExtensions(e);
      else if (n.equals("XMI.reference")) handleXMIReference(e);
      else if (n.equals("XMI.field")) handleXMIField(e);
      else if (n.equals("XMI.struct")) handleXMIStruct(e);
      else if (n.equals("XMI.seqItem")) handleXMISeqItem(e);
      else if (n.equals("XMI.sequence")) handleXMISequence(e);
      else if (n.equals("XMI.arrayLen")) handleXMIArrayLen(e);
      else if (n.equals("XMI.arrayItem")) handleXMIArrayItem(e);
      else if (n.equals("XMI.array")) handleXMIArray(e);
      else if (n.equals("XMI.enum")) handleXMIEnum(e);
      else if (n.equals("XMI.discrim")) handleXMIDiscrim(e);
      else if (n.equals("XMI.union")) handleXMIUnion(e);
      else if (n.equals("XMI.any")) handleXMIAny(e);
      else if (n.equals("XMI.extension")) handleXMIExtension(e);
      else if (n.equals("XMI.remoteContent")) handleXMIRemoteContent(e);
      else if (n.equals("visibility")) handle_visibility(e);
      else if (n.equals("taggedValue")) handle_taggedValue(e);
      else if (n.equals("behavior")) handle_behavior(e);
      else if (n.equals("ownerScope")) handle_ownerScope(e);
      else if (n.equals("isQuery")) handle_isQuery(e);
      else if (n.equals("parameter")) handle_parameter(e);
      else if (n.equals("ownedElement")) handle_ownedElement(e);
      else if (n.equals("isAbstract")) handle_isAbstract(e);
      else if (n.equals("isLeaf")) handle_isLeaf(e);
      else if (n.equals("isRoot")) handle_isRoot(e);
      else if (n.equals("feature")) handle_feature(e);
      else if (n.equals("changeable")) handle_changeable(e);
      else if (n.equals("targetScope")) handle_targetScope(e);
      else if (n.equals("connection")) handle_connection(e);
      else if (n.equals("Association")) handleAssociation(e);
      else if (n.equals("isActive")) handle_isActive(e);
      else if (n.equals("Class")) handleClass(e);
      else if (n.equals("AssociationClass")) handleAssociationClass(e);
      else if (n.equals("isNavigable")) handle_isNavigable(e);
      else if (n.equals("isOrdered")) handle_isOrdered(e);
      else if (n.equals("aggregation")) handle_aggregation(e);
      else if (n.equals("qualifier")) handle_qualifier(e);
      else if (n.equals("AssociationEnd")) handleAssociationEnd(e);
      else if (n.equals("Attribute")) handleAttribute(e);
      else if (n.equals("Constraint")) handleConstraint(e);
      else if (n.equals("DataType")) handleDataType(e);
      else if (n.equals("description")) handleDescription(e);
      else if (n.equals("subDependencies")) handle_subDependencies(e);
      else if (n.equals("Dependency")) handleDependency(e);
      else if (n.equals("discriminator")) handle_discriminator(e);
      else if (n.equals("Generalization")) handleGeneralization(e);
      else if (n.equals("Interface")) handleInterface(e);
      else if (n.equals("Method")) handleMethod(e);
      else if (n.equals("concurrency")) handle_concurrency(e);
      else if (n.equals("isPolymorphic")) handle_isPolymorphic(e);
      else if (n.equals("Operation")) handleOperation(e);
      else if (n.equals("kind")) handle_kind(e);
      else if (n.equals("Parameter")) handleParameter(e);
      else if (n.equals("Binding")) handleBinding(e);
      else if (n.equals("Comment")) handleComment(e);
      else if (n.equals("Component")) handleComponent(e);
      else if (n.equals("Node")) handleNode(e);
      else if (n.equals("geometry")) handle_geometry(e);
      else if (n.equals("style")) handle_style(e);
      else if (n.equals("Presentation")) handlePresentation(e);
      else if (n.equals("mapping")) handle_mapping(e);
      else if (n.equals("Refinement")) handleRefinement(e);
      else if (n.equals("Trace")) handleTrace(e);
      else if (n.equals("Usage")) handleUsage(e);
      else if (n.equals("icon")) handle_icon(e);
      else if (n.equals("requiredTag")) handle_requiredTag(e);
      else if (n.equals("Stereotype")) handleStereotype(e);
      else if (n.equals("TaggedValue")) handleTaggedValue(e);
      else if (n.equals("Primitive")) handlePrimitive(e);
      else if (n.equals("EnumerationLiteral")) handleEnumerationLiteral(e);
      else if (n.equals("literal")) handle_literal(e);
      else if (n.equals("Enumeration")) handleEnumeration(e);
      else if (n.equals("Structure")) handleStructure(e);
      else if (n.equals("recurrence")) handle_recurrence(e);
      else if (n.equals("target")) handle_target(e);
      else if (n.equals("isAsynchronous")) handle_isAsynchronous(e);
      else if (n.equals("script")) handle_script(e);
      else if (n.equals("actualArgument")) handle_actualArgument(e);
      else if (n.equals("slot")) handle_slot(e);
      else if (n.equals("Request")) handleRequest(e);
      else if (n.equals("action")) handle_action(e);
      else if (n.equals("ActionSequence")) handleActionSequence(e);
      else if (n.equals("argumentValue")) handle_argumentValue(e);
      else if (n.equals("Argument")) handleArgument(e);
      else if (n.equals("attributeValue")) handle_attributeValue(e);
      else if (n.equals("AttributeLink")) handleAttributeLink(e);
      else if (n.equals("mode")) handle_mode(e);
      else if (n.equals("CallAction")) handleCallAction(e);
      else if (n.equals("CreateAction")) handleCreateAction(e);
      else if (n.equals("DestroyAction")) handleDestroyAction(e);
      else if (n.equals("DataValue")) handleDataValue(e);
      else if (n.equals("Signal")) handleSignal(e);
      else if (n.equals("Exception")) handleException(e);
      else if (n.equals("linkRole")) handle_linkRole(e);
      else if (n.equals("Link")) handleLink(e);
      else if (n.equals("LinkEnd")) handleLinkEnd(e);
      else if (n.equals("Object")) handleObject(e);
      else if (n.equals("LinkObject")) handleLinkObject(e);
      else if (n.equals("LocalInvocation")) handleLocalInvocation(e);
      else if (n.equals("MessageInstance")) handleMessageInstance(e);
      else if (n.equals("Reception")) handleReception(e);
      else if (n.equals("ReturnAction")) handleReturnAction(e);
      else if (n.equals("SendAction")) handleSendAction(e);
      else if (n.equals("TerminateAction")) handleTerminateAction(e);
      else if (n.equals("UninterpretedAction")) handleUninterpretedAction(e);
      else if (n.equals("AssociationEndRole")) handleAssociationEndRole(e);
      else if (n.equals("AssociationRole")) handleAssociationRole(e);
      else if (n.equals("availableFeature")) handle_availableFeature(e);
      else if (n.equals("ClassifierRole")) handleClassifierRole(e);
      else if (n.equals("interaction")) handle_interaction(e);
      else if (n.equals("Collaboration")) handleCollaboration(e);
      else if (n.equals("Interaction")) handleInteraction(e);
      else if (n.equals("Message")) handleMessage(e);
      else if (n.equals("Actor")) handleActor(e);
      else if (n.equals("extensionPoint")) handle_extensionPoint(e);
      else if (n.equals("UseCase")) handleUseCase(e);
      else if (n.equals("UseCaseInstance")) handleUseCaseInstance(e);
      else if (n.equals("CallEvent")) handleCallEvent(e);
      else if (n.equals("changeExpression")) handle_changeExpression(e);
      else if (n.equals("ChangeEvent")) handleChangeEvent(e);
      else if (n.equals("entry")) handle_entry(e);
      else if (n.equals("exit")) handle_exit(e);
      else if (n.equals("internalTransition")) handle_internalTransition(e);
      else if (n.equals("State")) handleState(e);
      else if (n.equals("isConcurrent")) handle_isConcurrent(e);
      else if (n.equals("substate")) handle_substate(e);
      else if (n.equals("CompositeState")) handleCompositeState(e);
      else if (n.equals("Guard")) handleGuard(e);
      else if (n.equals("stateKind")) handle_stateKind(e);
      else if (n.equals("PseudoState")) handlePseudostate(e);
      else if (n.equals("SignalEvent")) handleSignalEvent(e);
      else if (n.equals("SimpleState")) handleSimpleState(e);
      else if (n.equals("top")) handle_top(e);
      else if (n.equals("transitions")) handle_transitions(e);
      else if (n.equals("StateMachine")) handleStateMachine(e);
      else if (n.equals("SubmachineState")) handleSubmachineState(e);
      else if (n.equals("TimeEvent")) handleTimeEvent(e);
      else if (n.equals("trigger")) handle_trigger(e);
      else if (n.equals("guard")) handle_guard(e);
      else if (n.equals("effect")) handle_effect(e);
      else if (n.equals("Transition")) handleTransition(e);
      else if (n.equals("partition")) handle_partition(e);
      else if (n.equals("ActivityModel")) handleActivityModel(e);
      else if (n.equals("ActionState")) handleActionState(e);
      else if (n.equals("ActivityState")) handleActivityState(e);
      else if (n.equals("ClassifierInState")) handleClassifierInState(e);
      else if (n.equals("ObjectFlowState")) handleObjectFlowState(e);
      else if (n.equals("Partition")) handlePartition(e);
      else if (n.equals("alias")) handle_alias(e);
      else if (n.equals("ElementReference")) handleElementReference(e);
      else if (n.equals("elementReference")) handle_elementReference(e);
      else if (n.equals("Package")) handlePackage(e);
      else if (n.equals("Model")) handleModel(e);
      else if (n.equals("isInstantiable")) handle_isInstantiable(e);
      else if (n.equals("Subsystem")) handleSubsystem(e);
      //else System.out.println("unknown tag:" + n);
    }
    catch (Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
    _openTags.addElement(n);
  }


  public void handleEndTag(TXElement e, boolean empty) {
    String n = e.getName();
    //System.out.println("XMIParser handleEndTag:" + e.getName()+".");
    try {
      if (n.equals("tag")) handle_tag(e);
      else if (n.equals("value")) handle_value(e);
      else if (n.equals("baseClass")) handle_baseClass(e);
      else if (n.equals("initialValue")) handle_initialValue(e);
      else if (n.equals("operationSpecification")) handle_operationSpecification(e);
      else if (n.equals("defaultValue")) handle_defaultValue(e);
      else if (n.equals("expression")) handleExpression(e);
      else if (n.equals("duration")) handle_duration(e);
      else if (n.equals("Model")) handleModelEnd(e);
      else if (n.equals("Collaboration")) handleCollaborationEnd(e);
      else if (n.equals("subtype")) handle_subtype(e);
      else if (n.equals("supertype")) handle_supertype(e);
      else if (n.equals("type")) handle_type(e);
      else if (n.equals("realization")) handle_realization(e);
      else if (n.equals("specification")) handle_specification(e);
      else if (n.equals("client")) handle_client(e);
      else if (n.equals("supplier")) handle_supplier(e);
      else if (n.equals("namespace")) handle_namespace(e);
      else if (n.equals("stereotype")) handle_stereotype(e);
      else if (n.equals("constraint")) handle_constraint(e);
      else if (n.equals("provision")) handle_provision(e);
      else if (n.equals("requirement")) handle_requirement(e);
      else if (n.equals("templateParameter")) handle_templateParameter(e);
      else if (n.equals("template")) handle_template(e);
      else if (n.equals("implementation")) handle_implementation(e);
      else if (n.equals("owner")) handle_owner(e);
      else if (n.equals("raisedException")) handle_raisedException(e);
      else if (n.equals("generalization")) handle_generalization(e);
      else if (n.equals("specialization")) handle_specialization(e);
      else if (n.equals("participant")) handle_participant(e);
      else if (n.equals("associationEnd")) handle_associationEnd(e);
      else if (n.equals("constrainedElement")) handle_constrainedElement(e);
      else if (n.equals("constrainedStereotype")) handle_constrainedStereotype(e);
      else if (n.equals("owningDependency")) handle_owningDependency(e);
      else if (n.equals("occurrence")) handle_occurrence(e);
      else if (n.equals("argument")) handle_argument(e);
      else if (n.equals("deployment")) handle_deployment(e);
      else if (n.equals("modelElement")) handle_modelElement(e);
      else if (n.equals("model")) handle_model(e);
      else if (n.equals("stereotypeConstraint")) handle_stereotypeConstraint(e);
      else if (n.equals("extendedElement")) handle_extendedElement(e);
      else if (n.equals("request")) handle_request(e);
      else if (n.equals("linkEnd")) handle_linkEnd(e);
      else if (n.equals("classifier")) handle_classifier(e);
      else if (n.equals("attribute")) handle_attribute(e);
      else if (n.equals("instantiation")) handle_instantiation(e);
      else if (n.equals("reception")) handle_reception(e);
      else if (n.equals("context")) handle_context(e);
      else if (n.equals("association")) handle_association(e);
      else if (n.equals("instance")) handle_instance(e);
      else if (n.equals("receiver")) handle_receiver(e);
      else if (n.equals("sender")) handle_sender(e);
      else if (n.equals("signal")) handle_signal(e);
      else if (n.equals("base")) handle_base(e);
      else if (n.equals("constrainingElement")) handle_constrainingElement(e);
      else if (n.equals("representedClassifier")) handle_representedClassifier(e);
      else if (n.equals("representedOperation")) handle_representedOperation(e);
      else if (n.equals("message")) handle_message(e);
      else if (n.equals("messageRef")) handle_messageRef(e);
      else if (n.equals("activator")) handle_activator(e);
      else if (n.equals("messageAction")) handle_messageAction(e);
      else if (n.equals("predecessor")) handle_predecessor(e);
      else if (n.equals("parent")) handle_parent(e);
      else if (n.equals("outgoing")) handle_outgoing(e);
      else if (n.equals("incoming")) handle_incoming(e);
      else if (n.equals("operation")) handle_operation(e);
      else if (n.equals("deferredEvent")) handle_deferredEvent(e);
      else if (n.equals("submachine")) handle_submachine(e);
      else if (n.equals("sourceState")) handle_sourceState(e);
      else if (n.equals("targetState")) handle_targetState(e);
      else if (n.equals("inState")) handle_inState(e);
      else if (n.equals("typeState")) handle_typeState(e);
      else if (n.equals("contents")) handle_contents(e);
      else if (n.equals("referencedElement")) handle_referencedElement(e);
      else if (n.equals("body")) handle_body(e);
      else if (n.equals("multiplicity")) handle_multiplicity(e);
      else if (n.equals("Class")) handleClassEnd(e);
      else if (n.equals("DataType")) handleDataTypeEnd(e);
      //else System.out.println("unknown end tag:" + n);
    }
    catch (Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
    _openTags.removeElementAt(_openTags.size() - 1);
  }

  protected void handleXMI(TXElement e) { }
  protected void handleXMIHeader(TXElement e) { }
  protected void handleXMIDocumentation(TXElement e) { }
  protected void handleXMIOwner(TXElement e) { }
  protected void handleXMIContact(TXElement e) { }
  protected void handleXMILongDescription(TXElement e) { }
  protected void handleXMIShortDescription(TXElement e) { }
  protected void handleXMIExporter(TXElement e) { }
  protected void handleXMIExporterVersion(TXElement e) { }
  protected void handleXMIExporterID(TXElement e) { }
  protected void handleXMINotice(TXElement e) { }
  protected void handleXMIMetamodel(TXElement e) { }
  protected void handleXMIContent(TXElement e) { }
  protected void handleXMIExtensions(TXElement e) { }
  protected void handleXMIReference(TXElement e) {
    String ref = e.getAttribute("target");
    _lastReference = lookUpReference(ref);
  }
  protected void handleXMIField(TXElement e) { }
  protected void handleXMIStruct(TXElement e) { }
  protected void handleXMISeqItem(TXElement e) { }
  protected void handleXMISequence(TXElement e) { }
  protected void handleXMIArrayLen(TXElement e) { }
  protected void handleXMIArrayItem(TXElement e) { }
  protected void handleXMIArray(TXElement e) { }
  protected void handleXMIEnum(TXElement e) { }
  protected void handleXMIDiscrim(TXElement e) { }
  protected void handleXMIUnion(TXElement e) { }
  protected void handleXMIAny(TXElement e) { }
  protected void handleXMIExtension(TXElement e) { }
  protected void handleXMIRemoteContent(TXElement e) { }

  protected void handle_name(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String nStr = e.getText().trim();
    Name n = new Name(nStr);
    if (_curME != null) _curME.setName(n);
  }

  protected void handle_visibility(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String s = e.getAttribute("XMI.value");
    VisibilityKind k = VisibilityKind.PUBLIC;
    if (s == null || s.equals("public")) k = VisibilityKind.PUBLIC;
    else if (s.equals("private")) k = VisibilityKind.PRIVATE;
    else if (s.equals("protected")) k = VisibilityKind.PROTECTED;
    else if (s.equals("package")) k = VisibilityKind.PACKAGE;
    //else if (s.equals("unspec")) k = VisibilityKind.UNSPEC;
    else System.out.println("unknown VisibilityKind: " + s + ".");

    if (_curME != null) _curME.setVisibility(k);
    else System.out.println("can't set Visibility");
  }

  protected void handle_namespace(TXElement e) throws PropertyVetoException {
    ignoreElement(e);
    // needs-more-work: done with XMI.reference
  }
  protected void handle_stereotype(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curME != null && _lastReference instanceof Stereotype)
      _curME.addStereotype((Stereotype)_lastReference);
  }
  protected void handle_constraint(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curME != null && _lastReference instanceof Constraint)
      _curME.addConstraint((Constraint)_lastReference);
    // needs-more-work: done with XMI.reference
  }
  protected void handle_provision(TXElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
  }
  protected void handle_requirement(TXElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
  }
  protected void handle_templateParameter(TXElement e) throws PropertyVetoException { }
  protected void handle_template(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curME != null && _lastReference instanceof ModelElement)
      _curME.setTemplate((ModelElement)_lastReference);
  }
  protected void handle_implementation(TXElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
  }
  protected void handle_taggedValue(TXElement e) throws PropertyVetoException { }
  protected void handle_behavior(TXElement e) throws PropertyVetoException { }

  protected void handle_ownerScope(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String s = e.getAttribute("XMI.value");
    ScopeKind k = ScopeKind.UNSPEC;
    if (s == null || s.equals("instance")) k = ScopeKind.INSTANCE;
    else if (s.equals("classifer")) k = ScopeKind.CLASSIFIER;
    else System.out.println("unknown ScopeKind: " + s + ".");

    if (_curME instanceof Feature) ((Feature)_curME).setOwnerScope(k);
    else System.out.println("can't set OwnerScopeKind");
  }

  protected void handle_owner(TXElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
    ignoreElement(e);
  }

  protected void handle_isQuery(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof BehavioralFeature) ((BehavioralFeature)_curME).setIsQuery(b);
    else System.out.println("can't set isQuery");
  }

  protected void handle_raisedException(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (inTag("Operation")) {
      if (_curOperation != null && _lastReference instanceof MMException)
	_curOperation.addRaisedException((MMException)_lastReference);
    }
  }
  protected void handle_parameter(TXElement e) throws PropertyVetoException { }
  protected void handle_ownedElement(TXElement e) throws PropertyVetoException { }

  protected void handle_isAbstract(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof GeneralizableElementImpl)
      ((GeneralizableElementImpl)_curME).setIsAbstract(b);
    else System.out.println("can't set isAbstract");
  }

  protected void handle_isLeaf(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof GeneralizableElementImpl)
      ((GeneralizableElementImpl)_curME).setIsLeaf(b);
    else System.out.println("can't set isLeaf");
  }
  protected void handle_isRoot(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof GeneralizableElementImpl)
      ((GeneralizableElementImpl)_curME).setIsRoot(b);
    else System.out.println("can't set isRoot");
  }
  protected void handle_generalization(TXElement e) throws PropertyVetoException {
    ignoreElement(e);
    // needs-more-work: done with XMI.reference
  }
  protected void handle_specialization(TXElement e) throws PropertyVetoException {
    ignoreElement(e);
    // needs-more-work: done with XMI.reference
  }
  protected void handle_participant(TXElement e) throws PropertyVetoException {
    ignoreElement(e);
    // needs-more-work: done with XMI.reference
  }
  protected void handle_realization(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (inTag("Operation")) {
      if (_curOperation != null && _lastReference instanceof Operation) {
	//? _curOperation.addRaisedException((MMException)_lastReference);
      }
    }
    else if (_curME instanceof Classifier) {
      if (_curME != null && _lastReference instanceof Classifier) {
	_curRealization = new Realization((Classifier) _lastReference,
					  (Classifier) _curME);
	addToModel(_curRealization);
      }
    }
  }
  protected void handle_specification(TXElement e) throws PropertyVetoException {
    ignoreElement(e);
    // needs-more-work: done with XMI.reference
  }
  protected void handle_associationEnd(TXElement e) throws PropertyVetoException {
  }
  protected void handle_feature(TXElement e) throws PropertyVetoException { }
  protected void handle_changeable(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String s = e.getAttribute("XMI.value");
    ChangeableKind k = ChangeableKind.UNSPEC;
    if (s == null || s.equals("none")) k = ChangeableKind.NONE;
    else if (s.equals("frozen")) k = ChangeableKind.FROZEN;
    else if (s.equals("addonly")) k = ChangeableKind.ADDONLY;
    else System.out.println("unknown ChangeableKind: " + s + ".");

    if (inTag("Attribute") && _curAttribute != null)
      _curAttribute.setChangeable(k);
    else if (inTag("AssociationEnd") && _curAssociationEnd != null)
      _curAssociationEnd.setChangeable(k);
    else if (inTag("AssociationEndRole") && _curAssociationEndRole != null)
      _curAssociationEndRole.setChangeable(k);
    else System.out.println("can't set ChangeableKind");
  }
  protected void handle_multiplicity(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String multStr = e.getText().trim();
    if (multStr == null || multStr.equals("")) return;
    Multiplicity m = parseMultiplicity(multStr);
    if (inTag("AssociationEnd") && _curAssociationEnd != null)
      _curAssociationEnd.setMultiplicity(m);
    else if (inTag("AssociationEndRole") && _curAssociationEndRole != null)
      _curAssociationEndRole.setMultiplicity(m);
    else if (inTag("Attribute") && _curAttribute != null)
      _curAttribute.setMultiplicity(m);
    else if (inTag("AssociationRole") && _curAssociationRole != null)
      _curAssociationRole.setMultiplicity(m);
    else if (inTag("ClassifierRole") && _curClassifierRole != null)
      _curClassifierRole.setMultiplicity(m);
    else System.out.println("unknown context for multiplicity: " + multStr);
  }
  /** Parse a string of the form: "range, ...", where range is of the
   *  form "lower..upper", or "integer" */
  public Multiplicity parseMultiplicity(String s) {
    s = s.trim();
    if (s.length() == 0) return Multiplicity.ONE;
    Multiplicity m = new Multiplicity();
    StringTokenizer st1 = new StringTokenizer(s, ",");
    while (st1.hasMoreElements()) {
      String range = st1.nextToken().trim();
      String lowStr = "", highStr = "";
      Integer low = null, high = null;
      int dotdot = range.indexOf("..");
      if (dotdot == -1) {
	lowStr = range;
	highStr = range;
      }
      else {
	lowStr = range.substring(0, dotdot);
	highStr = range.substring(dotdot + 2);
      }
      try {
	if (lowStr.indexOf("*") == -1) low = Integer.valueOf(lowStr);
	if (highStr.indexOf("*") == -1) high = Integer.valueOf(highStr);
	MultiplicityRange mr = new MultiplicityRange(low, high);
	m.addRange(mr);
      }
      catch (NumberFormatException nfe) { }
    }
    return m;
  }


  protected void handle_targetScope(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String s = e.getAttribute("XMI.value");
    ScopeKind k = ScopeKind.UNSPEC;
    if (s == null || s.equals("instance")) k = ScopeKind.INSTANCE;
    else if (s.equals("classifer")) k = ScopeKind.CLASSIFIER;
    else System.out.println("unknown ScopeKind: " + s + ".");

    if (_curME instanceof StructuralFeature)
      ((StructuralFeature)_curME).setTargetScope(k);
    else if (_curME instanceof AssociationEnd)
      ((AssociationEnd)_curME).setTargetScope(k);
    else System.out.println("can't set TargetScopeKind: " + _curME);
  }
  protected void handle_type(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (inTag("AssociationEnd")) {
      if (_lastReference instanceof Classifier) {
	//((Classifier)_lastReference).addAssociationEnd(_curAssociationEnd);
	_curAssociationEnd.setType((Classifier)_lastReference);
	// needs-more-work: this is redundant with handle_associationEnd
      }
    }
    else if (inTag("Attribute")) {
      if (_lastReference instanceof Classifier) {
	_curAttribute.setType((Classifier)_lastReference);
	// needs-more-work: this is redundant with handle_associationEnd
      }
    }
    else if (inTag("Parameter")) {
      if (_lastReference instanceof Classifier) {
	_curParameter.setType((Classifier)_lastReference);
	// needs-more-work: this is redundant with handle_associationEnd
      }
    }
    else System.out.println("unknown context for <type>:" + e.getText());
  }
  protected void handle_connection(TXElement e) throws PropertyVetoException { }
  protected void handleAssociation(TXElement e) throws PropertyVetoException {
    _curAssociation = (Association) findOrCreate(e, Association.class);
    _curME = _curAssociation;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handle_isActive(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curClass != null) _curClass.setIsActive(b);
    else System.out.println("can't set isActive");
  }
  protected void handleClass(TXElement e) throws PropertyVetoException {
    _curClass  = (MMClass) findOrCreate(e, MMClass.class);
    _curME = _curClass;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handleClassEnd(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curModel == null) _proj.defineType(_curClass);
  }
  protected void handleAssociationClass(TXElement e) throws PropertyVetoException {
    _curAssociationClass = (AssociationClass)
      findOrCreate(e, AssociationClass.class);
    _curME = _curAssociationClass;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handle_isNavigable(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (inTag("AssociationEnd") && _curAssociationEnd != null)
      _curAssociationEnd.setIsNavigable(b);
    else if (inTag("AssociationEndRole") && _curAssociationEndRole != null)
      _curAssociationEndRole.setIsNavigable(b);
    else
      System.out.println("can't set isNavigable");
  }
  protected void handle_isOrdered(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (inTag("AssociationEnd") && _curAssociationEnd != null)
      _curAssociationEnd.setIsOrdered(b);
    else if (inTag("AssociationEndRole") && _curAssociationEndRole != null)
      _curAssociationEndRole.setIsOrdered(b);
    else
      System.out.println("can't set isOrdered");
  }
  protected void handle_aggregation(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String s = e.getAttribute("XMI.value");
    AggregationKind k = AggregationKind.NONE;
    if (s == null || s.equals("none")) k = AggregationKind.NONE;
    else if (s.equals("shared")) k = AggregationKind.NONE; // needs-more-work
    else if (s.equals("aggregate")) k = AggregationKind.AGG;
    else if (s.equals("composite")) k = AggregationKind.COMPOSITE;
    else System.out.println("unknown AggregationKind: " + s + ".");

    if (inTag("AssociationEnd") && _curAssociationEnd != null)
      _curAssociationEnd.setAggregation(k);
    else if (inTag("AssociationEndRole") && _curAssociationEndRole != null)
      _curAssociationEndRole.setAggregation(k);
    else
      System.out.println("can't set Aggregation");
  }
  protected void handle_qualifier(TXElement e) throws PropertyVetoException {
    // needs-more-work
  }
  protected void handleAssociationEnd(TXElement e) throws PropertyVetoException {
    _curAssociationEnd = (AssociationEnd) findOrCreate(e, AssociationEnd.class);
    _curME = _curAssociationEnd;
    if (_firstPass) return;
    if (_curAssociation != null) _curAssociation.addConnection(_curAssociationEnd);
  }
  protected void handle_initialValue(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && !body.equals(""))
      _curAttribute.setInitialValue(new Expression(body));
    //System.out.println("set init val to:" + body + ".");
  }
  protected void handleAttribute(TXElement e) throws PropertyVetoException {
    _curAttribute = (Attribute) findOrCreate(e, Attribute.class);
    _curME = _curAttribute;
    if (_firstPass) return;
    if (_curClass != null) _curClass.addStructuralFeature(_curAttribute);
  }
  protected void handle_body(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body == null) return;
    if (inTag("UninterpretedAction") && _curUninterpretedAction != null) 
      _curUninterpretedAction.setBody(body);
  }
  protected void handle_constrainedElement(TXElement e) throws PropertyVetoException { }
  protected void handle_constrainedStereotype(TXElement e) throws PropertyVetoException { }
  protected void handleConstraint(TXElement e) throws PropertyVetoException {
    _curConstraint = (Constraint) findOrCreate(e, Constraint.class);
    _curME = _curConstraint;
    if (_firstPass) return;
  }
  protected void handleDataType(TXElement e) throws PropertyVetoException {
    _curDataType = (DataType) findOrCreate(e, DataType.class);
    _curME = _curDataType;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handleDataTypeEnd(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curModel == null) _proj.defineType(_curDataType);
  }

  protected void handleDescription(TXElement e) throws PropertyVetoException {
    //     Description o = (Description) findOrCreate(e, Description.class);
    if (_firstPass) return;
    if (_curDependency != null) {
      String desc = e.getText().trim();
      _curDependency.setDescription(desc);
    }
  }
  protected void handle_owningDependency(TXElement e) throws PropertyVetoException { }
  protected void handle_client(TXElement e) throws PropertyVetoException {
    if (_curME instanceof Dependency) {
      if (_lastReference instanceof ModelElement) {
	((Dependency)_curME).addClient((ModelElement)_lastReference);
      }
    }
  }
  protected void handle_supplier(TXElement e) throws PropertyVetoException {
    if (_curME instanceof Dependency) {
      if (_lastReference instanceof ModelElement) {
	((Dependency)_curME).addSupplier((ModelElement)_lastReference);
      }
    }
  }
  protected void handle_subDependencies(TXElement e) throws PropertyVetoException {
    //needs-more-work
  }
  protected void handleDependency(TXElement e) throws PropertyVetoException {
    _curDependency = (Dependency) findOrCreate(e, Dependency.class);
    _curME = _curDependency;
    if (_firstPass) return;
    // needs-more-work: if inTag("subDependencies") addSubDependency();
    addToModel(_curME);
  }
  protected void handle_discriminator(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && !body.equals(""))
      _curGeneralization.setDiscriminator(new Name(body));
    //System.out.println("set discriminator to:" + body + ".");
  }
  protected void handle_subtype(TXElement e) throws PropertyVetoException {
    if (_curME instanceof Generalization) {
      if (_lastReference instanceof GeneralizableElement) {
	((Generalization)_curME).setSubtype((GeneralizableElement)_lastReference);
      }
    }
  }
  protected void handle_supertype(TXElement e) throws PropertyVetoException {
    if (_curME instanceof Generalization) {
      if (_lastReference instanceof GeneralizableElement) {
	((Generalization)_curME).setSupertype((GeneralizableElement)_lastReference);
      }
    }
  }
  protected void handleGeneralization(TXElement e) throws PropertyVetoException {
    _curGeneralization = (Generalization) findOrCreate(e, Generalization.class);
    _curME = _curGeneralization;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handleInterface(TXElement e) throws PropertyVetoException {
    _curInterface = (Interface) findOrCreate(e, Interface.class);
    _curME = _curInterface;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handleMethod(TXElement e) throws PropertyVetoException {
    _curMethod = (Method) findOrCreate(e, Method.class);
    _curME = _curMethod;
    if (_firstPass) return;
    // needs-more-work
  }
  protected void handle_concurrency(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String s = e.getAttribute("XMI.value");
    CallConcurrencyKind k = CallConcurrencyKind.SEQUENTIAL;
    if (s == null || s.equals("sequential")) k = CallConcurrencyKind.SEQUENTIAL;
    else if (s.equals("guarded")) k = CallConcurrencyKind.GUARDED;
    else if (s.equals("concurrent")) k = CallConcurrencyKind.CONCURRENT;
    else System.out.println("unknown CallConcurrencyKind: " + s + ".");

    if (_curOperation != null) _curOperation.setConcurrency(k);
    else System.out.println("can't set CallConcurrency");
  }
  protected void handle_isPolymorphic(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curOperation != null) _curOperation.setIsPolymorphic(b);
    else System.out.println("can't set isPolymorphic");
  }
  protected void handle_operationSpecification(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && !body.equals("")) {
      if (inTag("Operation") && _curOperation != null) 
	_curOperation.setSpecification(new Uninterpreted(body));
      else System.out.println("unknown context for Uninterpreted");
    }
    //System.out.println("set operation spec to:" + body + ".");
  }
  protected void handle_occurrence(TXElement e) throws PropertyVetoException { }
  protected void handleOperation(TXElement e) throws PropertyVetoException {
    _curOperation = (Operation) findOrCreate(e, Operation.class);
    _curME = _curOperation;
    if (_firstPass) return;
    if (inTag("Class") && _curClass != null)
      _curClass.addBehavioralFeature(_curOperation);
    else if (inTag("Interface") && _curInterface != null)
      _curInterface.addBehavioralFeature(_curOperation);
    else System.out.println("unknown context for operation: " + e.getText());
  }
  protected void handle_defaultValue(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && !body.equals(""))
      _curParameter.setDefaultValue(new Expression(body));
    //System.out.println("set default value to:" + body + ".");
  }
  protected void handle_kind(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String s = e.getAttribute("XMI.value");
    ParameterDirectionKind k = ParameterDirectionKind.IN;
    if (s == null || s.equals("in")) k = ParameterDirectionKind.IN;
    else if (s.equals("inout")) k = ParameterDirectionKind.INOUT;
    else if (s.equals("out")) k = ParameterDirectionKind.OUT;
    else if (s.equals("return")) k = ParameterDirectionKind.RETURN;
    else System.out.println("unknown ParameterDirectionKind: " + s + ".");

    if (_curParameter != null) _curParameter.setKind(k);
    else System.out.println("can't set ParameterDirectionKind");
  }
  protected void handleParameter(TXElement e) throws PropertyVetoException {
    _curParameter = (Parameter) findOrCreate(e, Parameter.class);
    _curME = _curParameter;
    if (_firstPass) return;
    if (inTag("Operation")) {
      if (_curOperation != null) _curOperation.addParameter(_curParameter);
    }
  }
  protected void handle_argument(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handleBinding(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handleComment(TXElement e) throws PropertyVetoException {
    // needs-more-work
    //? _curComment = (Comment) findOrCreate(e, Comment.class);
    //? if (_firstPass) return;
    notImplementedYet(e);
  }
  protected void handle_deployment(TXElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
  }
  protected void handle_modelElement(TXElement e) throws PropertyVetoException { }
  protected void handleComponent(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
    addToModel(_curME);
  }
  protected void handle_component(TXElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
  }
  protected void handleNode(TXElement e) throws PropertyVetoException {
    _curNode = (Node) findOrCreate(e, Node.class);
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handle_geometry(TXElement e) throws PropertyVetoException {
    ignoreElement(e);
  }
  protected void handle_style(TXElement e) throws PropertyVetoException {
    ignoreElement(e);
  }
  protected void handle_model(TXElement e) throws PropertyVetoException {
    ignoreElement(e);
  }
  protected void handlePresentation(TXElement e) throws PropertyVetoException {
    ignoreElement(e);
  }
  protected void handle_mapping(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handleRefinement(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handleTrace(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handleUsage(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handle_baseClass(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String nStr = e.getText().trim();
    Name n = new Name(nStr);
    if (_curStereotype != null) _curStereotype.setBaseClass(n);
  }

  protected void handle_icon(TXElement e) throws PropertyVetoException {
    ignoreElement(e);
  }
  protected void handle_stereotypeConstraint(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curStereotype != null && _lastReference instanceof Constraint)
      _curStereotype.addStereotypeConstraint((Constraint)_lastReference);
  }
  protected void handle_extendedElement(TXElement e) throws PropertyVetoException {
    ignoreElement(e); //needs-more-work?
  }
  protected void handle_requiredTag(TXElement e) throws PropertyVetoException { }
  protected void handleStereotype(TXElement e) throws PropertyVetoException {
    _curStereotype = (Stereotype) findOrCreate(e, Stereotype.class);
    _curME = _curStereotype;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handleTaggedValue(TXElement e) throws PropertyVetoException {
    //_curTaggedValue = (TaggedValue) findOrCreate(e, TaggedValue.class);
    if (_firstPass) return;
    _curTaggedValue = new TaggedValue();
    if (inTag("requiredTag") && _curStereotype != null)
      _curStereotype.addRequiredTag(_curTaggedValue);
    else if (_curME != null)
      _curME.addTaggedValue(_curTaggedValue);
  }
  protected void handle_tag(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && _curTaggedValue != null)
      _curTaggedValue.setTag(new Name(body));
    //System.out.println("set tag to:" + body + ".");
  }
  protected void handle_value(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null  && _curTaggedValue != null)
      _curTaggedValue.setValue(new Uninterpreted(body));
    //System.out.println("set tag VALUE to:" + body + ".");
  }
  protected void handlePrimitive(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handleEnumerationLiteral(TXElement e) throws PropertyVetoException {
    _curEnumerationLiteral = (EnumerationLiteral)
      findOrCreate(e, EnumerationLiteral.class);
    if (_firstPass) return;
    if (_curEnumeration != null) _curEnumeration.addLiteral(_curEnumerationLiteral);
  }
  protected void handle_literal(TXElement e) throws PropertyVetoException {  }
  protected void handleEnumeration(TXElement e) throws PropertyVetoException {
    _curEnumeration = (uci.uml.Foundation.Data_Types.Enumeration)
      findOrCreate(e, uci.uml.Foundation.Data_Types.Enumeration.class);
    _curME = _curEnumeration;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handleStructure(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handle_recurrence(TXElement e) throws PropertyVetoException { }
  protected void handle_target(TXElement e) throws PropertyVetoException { }
  protected void handle_isAsynchronous(TXElement e) throws PropertyVetoException { }
  protected void handle_script(TXElement e) throws PropertyVetoException { }
  protected void handle_request(TXElement e) throws PropertyVetoException { }
  protected void handle_actualArgument(TXElement e) throws PropertyVetoException { }
  protected void handle_linkEnd(TXElement e) throws PropertyVetoException { }
  protected void handle_classifier(TXElement e) throws PropertyVetoException { }
  protected void handle_slot(TXElement e) throws PropertyVetoException { }
  protected void handleRequest(TXElement e) throws PropertyVetoException {
    _curRequest = (Request) findOrCreate(e, Request.class);
    if (_firstPass) return;
  }
  protected void handle_action(TXElement e) throws PropertyVetoException {
  }
  protected void handleActionSequence(TXElement e) throws PropertyVetoException {
    _curActionSequence = (ActionSequence)
      findOrCreate(e, ActionSequence.class);
    _curME = _curActionSequence;
    if (_firstPass) return;
    if (inTag("effect") && _curTransition != null)
      _curTransition.setEffect(_curActionSequence);
    else if (inTag("entry") && _curME instanceof State)
      ((State)_curME).setEntry(_curActionSequence);
    else if (inTag("exit") && _curME instanceof State)
      ((State)_curME).setEntry(_curActionSequence);
  }
  protected void handle_argumentValue(TXElement e) throws PropertyVetoException { }
  protected void handleArgument(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handle_attributeValue(TXElement e) throws PropertyVetoException { }
  protected void handle_attribute(TXElement e) throws PropertyVetoException { }
  protected void handleAttributeLink(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handle_mode(TXElement e) throws PropertyVetoException { }
  protected void handleCallAction(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handle_instantiation(TXElement e) throws PropertyVetoException { }
  protected void handleCreateAction(TXElement e) throws PropertyVetoException {
    _curCreateAction = (CreateAction) findOrCreate(e, CreateAction.class);
    _curME = _curCreateAction;
    if (_firstPass) return;
    //? addToModel(_curME);
  }
  protected void handleDestroyAction(TXElement e) throws PropertyVetoException {
    _curDestroyAction = (DestroyAction) findOrCreate(e, DestroyAction.class);
    _curME = _curDestroyAction;
    if (_firstPass) return;
    //? addToModel(_curME);
  }
  protected void handleDataValue(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handle_reception(TXElement e) throws PropertyVetoException { }
  protected void handleSignal(TXElement e) throws PropertyVetoException {
    _curSignal = (Signal) findOrCreate(e, Signal.class);
    _curME = _curSignal;
    if (_firstPass) return;
    //? addToModel(_curME);
  }
  protected void handle_context(TXElement e) throws PropertyVetoException { }
  protected void handleException(TXElement e) throws PropertyVetoException {
    _curException = (MMException) findOrCreate(e, Exception.class);
    _curME = _curException;
    if (_firstPass) return;
    //? addToModel(_curME);
  }
  protected void handle_association(TXElement e) throws PropertyVetoException { }
  protected void handle_linkRole(TXElement e) throws PropertyVetoException { }
  protected void handleLink(TXElement e) throws PropertyVetoException {
    _curLink = (Link) findOrCreate(e, Link.class);
    _curME = _curLink;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handle_instance(TXElement e) throws PropertyVetoException { }
  protected void handleLinkEnd(TXElement e) throws PropertyVetoException {
    _curLinkEnd = (LinkEnd) findOrCreate(e, LinkEnd.class);
    _curME = _curLinkEnd;
    if (_firstPass) return;
    if (_curLink != null) _curLink.addLinkRole(_curLinkEnd);
  }
  protected void handleObject(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
//     _curObject = (Object) findOrCreate(e, Object.class);
//     if (_firstPass) return;
  }
  protected void handleLinkObject(TXElement e) throws PropertyVetoException {
    _curLink = (Link) findOrCreate(e, Link.class);
    _curME = _curLink;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handleLocalInvocation(TXElement e) throws PropertyVetoException {
    _curLocalInvocation = (LocalInvocation)
      findOrCreate(e, LocalInvocation.class);
    _curME = _curLocalInvocation;
    if (_firstPass) return;
    //? addToModel(_curME);
  }
  protected void handle_receiver(TXElement e) throws PropertyVetoException { }
  protected void handle_sender(TXElement e) throws PropertyVetoException { }
  protected void handleMessageInstance(TXElement e) throws
  PropertyVetoException {
    notImplementedYet(e);
//     _curMessage = (MessageInstance) findOrCreate(e, MessageInstance.class);
//     _curME = _curMessage;
//     if (_firstPass) return;
//     addToModel(_curME);
  }
  protected void handle_signal(TXElement e) throws PropertyVetoException { }
  protected void handleReception(TXElement e) throws PropertyVetoException {
    _curReception = (Reception) findOrCreate(e, Reception.class);
    _curME = _curReception;
    if (_firstPass) return;
    //? addToModel(_curME);
  }
  protected void handleReturnAction(TXElement e) throws PropertyVetoException {
    _curReturnAction = (ReturnAction) findOrCreate(e, ReturnAction.class);
    _curME = _curReturnAction;
    if (_firstPass) return;
    //? addToModel(_curME);
  }
  protected void handleSendAction(TXElement e) throws PropertyVetoException {
    _curSendAction = (SendAction) findOrCreate(e, SendAction.class);
    _curME = _curSendAction;
    if (_firstPass) return;
    //? addToModel(_curME);
  }
  protected void handleTerminateAction(TXElement e) throws PropertyVetoException {
    _curTerminateAction = (TerminateAction)
      findOrCreate(e, TerminateAction.class);
    _curME = _curTerminateAction;
    if (_firstPass) return;
    //? addToModel(_curME);
  }
  protected void handleUninterpretedAction(TXElement e) throws PropertyVetoException {
    _curUninterpretedAction = (UninterpretedAction)
      findOrCreate(e, UninterpretedAction.class);
    _curME = _curUninterpretedAction;
    if (_firstPass) return;
    if (inTag("Message") && _curMessage != null)
      _curMessage.setAction(_curUninterpretedAction);
    //? addToModel(_curME);
  }
  protected void handle_base(TXElement e) throws PropertyVetoException {
    //@@@ set base name
    String baseStr = e.getText().trim();
//     if (inTag("AssociationRole") && _curAssociationEnd != null)
//       _curAssociationEnd.setBaseStr(baseStr);
    if (inTag("ClassifierRole") && _curClassifierRole != null)
      _curClassifierRole.setBaseString(baseStr);
  }
  protected void handleAssociationEndRole(TXElement e) throws PropertyVetoException {
    _curAssociationEndRole = (AssociationEndRole)
      findOrCreate(e, AssociationEndRole.class);
    _curME = _curAssociationEndRole;
    if (_firstPass) return;
    if (_curAssociationRole != null)
      _curAssociationEndRole.setAssociationRole(_curAssociationRole);
  }
  protected void handleAssociationRole(TXElement e) throws PropertyVetoException {
    _curAssociationRole = (AssociationRole)
      findOrCreate(e, AssociationRole.class);
    _curME = _curAssociationRole;
    if (_firstPass) return;
    if (_curCollaboration != null) _curCollaboration.addPublicOwnedElement(_curME);
  }

  protected void handle_availableFeature(TXElement e) throws PropertyVetoException { }
  protected void handleClassifierRole(TXElement e) throws PropertyVetoException {
    _curClassifierRole = (ClassifierRole)
      findOrCreate(e, ClassifierRole.class);
    _curME = _curClassifierRole;
    if (_firstPass) return;
    if (_curCollaboration != null) _curCollaboration.addPublicOwnedElement(_curME);
  }
  protected void handle_constrainingElement(TXElement e) throws PropertyVetoException { }
  protected void handle_representedClassifier(TXElement e) throws PropertyVetoException { }
  protected void handle_representedOperation(TXElement e) throws PropertyVetoException { }
  protected void handle_interaction(TXElement e) throws PropertyVetoException { }
  protected void handleCollaboration(TXElement e) throws PropertyVetoException {
    _curCollaboration = (Collaboration) findOrCreate(e, Collaboration.class);
    _curME = _curCollaboration;
    if (_firstPass) return;
    addTopModel(_curCollaboration);
  }

  protected void handleCollaborationEnd(TXElement e) throws PropertyVetoException {
  }


  protected void handle_message(TXElement e) throws PropertyVetoException { }
  protected void handle_messageRef(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curAssociationRole != null && _lastReference instanceof Message)
      _curAssociationRole.addMessage((Message)_lastReference);
  }

  protected void handleInteraction(TXElement e) throws PropertyVetoException {
    _curInteraction = (Interaction) findOrCreate(e, Interaction.class);
    _curME = _curInteraction;
    if (_firstPass) return;
    if (_curCollaboration != null)
      _curCollaboration.setInteraction(_curInteraction);
  }
  protected void handle_activator(TXElement e) throws PropertyVetoException { }
  protected void handle_messageAction(TXElement e) throws PropertyVetoException { }
  protected void handle_predecessor(TXElement e) throws PropertyVetoException { }
  protected void handleMessage(TXElement e) throws PropertyVetoException {
    _curMessage = (Message) findOrCreate(e, Message.class);
    _curME = _curMessage;
    if (_firstPass) return;
    if (_curCollaboration != null) _curCollaboration.addPublicOwnedElement(_curME);
    if (_curInteraction != null) _curInteraction.addMessage(_curMessage);
  }
  protected void handleActor(TXElement e) throws PropertyVetoException {
    _curActor = (Actor) findOrCreate(e, Actor.class);
    _curME = _curActor;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handle_extensionPoint(TXElement e) throws PropertyVetoException { }
  protected void handleUseCase(TXElement e) throws PropertyVetoException {
    _curUseCase = (UseCase) findOrCreate(e, UseCase.class);
    _curME = _curUseCase;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handleUseCaseInstance(TXElement e) throws PropertyVetoException {
    _curUseCaseInstance = (UseCaseInstance)
      findOrCreate(e, UseCaseInstance.class);
    _curME = _curUseCaseInstance;
    if (_firstPass) return;
    addToModel(_curME);
  }
  protected void handle_parent(TXElement e) throws PropertyVetoException { }
  protected void handle_outgoing(TXElement e) throws PropertyVetoException { }
  protected void handle_incoming(TXElement e) throws PropertyVetoException { }
  protected void handle_operation(TXElement e) throws PropertyVetoException { }
  protected void handleCallEvent(TXElement e) throws PropertyVetoException {
    _curCallEvent = (CallEvent) findOrCreate(e, CallEvent.class);
    _curME = _curCallEvent;
    if (_firstPass) return;
    if (_curTransition != null) _curTransition.setTrigger(_curCallEvent);
  }
  protected void handle_changeExpression(TXElement e) throws PropertyVetoException { }
  protected void handleChangeEvent(TXElement e) throws PropertyVetoException {
    _curChangeEvent  = (ChangeEvent) findOrCreate(e, ChangeEvent.class);
    _curME = _curChangeEvent;
    if (_firstPass) return;
    if (_curTransition != null) _curTransition.setTrigger(_curChangeEvent);
  }
  protected void handle_deferredEvent(TXElement e) throws PropertyVetoException { }
  protected void handle_entry(TXElement e) throws PropertyVetoException { }
  protected void handle_exit(TXElement e) throws PropertyVetoException { }
  protected void handle_internalTransition(TXElement e) throws PropertyVetoException { }
  protected void handleState(TXElement e) throws PropertyVetoException {
    //System.out.println("<State> id=" + e.getAttribute("XMI.id"));
    //System.out.println("<state> found: " + find);
    _curState = (State) findOrCreate(e, State.class);
    _curME = _curState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }
  protected void handle_isConcurrent(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof CompositeState)
      ((CompositeState)_curME).setIsConcurrent(b);
    else System.out.println("can't set isConcurrent");
  }
  protected void handle_substate(TXElement e) throws PropertyVetoException { }
  protected void handleCompositeState(TXElement e) throws PropertyVetoException {
    _curME = (ModelElementImpl) findOrCreate(e, CompositeState.class);
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    else if (inTag("StateMachine") && inTag("top") && _curStateMachine != null)
      _curStateMachine.setTop((State)_curME);
    else if (inTag("ActivityModel") && inTag("top") && _curActivityModel != null)
      _curActivityModel.setTop((State)_curME);
    _curCompositeState = (CompositeState) _curME;
  }
  protected void handleExpression(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && !body.equals(""))
      _curGuard.setExpression(new BooleanExpression(body));
    //System.out.println("set guard expression to:" + body + ".");
  }
  protected void handleGuard(TXElement e) throws PropertyVetoException {
    _curGuard= (Guard) findOrCreate(e, Guard.class);
    _curME = _curGuard;
    if (_firstPass) return;
    if (_curTransition != null) _curTransition.setGuard(_curGuard);
  }
  protected void handle_stateKind(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String s = e.getAttribute("XMI.value");
    PseudostateKind k = PseudostateKind.INITIAL;
    if (s == null ||s.equals("initial")) k = PseudostateKind.INITIAL;
    else if (s.equals("deepHistory")) k = PseudostateKind.DEEP_HISTORY;
    else if (s.equals("shallowHistory")) k = PseudostateKind.SHALLOW_HISTORY;
    else if (s.equals("join")) k = PseudostateKind.JOIN;
    else if (s.equals("fork")) k = PseudostateKind.FORK;
    else if (s.equals("branch")) k = PseudostateKind.BRANCH;
    else if (s.equals("final")) k = PseudostateKind.FINAL;
    else System.out.println("unknown PseudostateKind: " + s + ".");

    if (_curPseudostate != null) _curPseudostate.setKind(k);
    else System.out.println("can't set PseudoState Kind");
  }
  protected void handlePseudostate(TXElement e) throws PropertyVetoException {
    _curPseudostate = (Pseudostate) findOrCreate(e, Pseudostate.class);
    _curME = _curPseudostate;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }
  protected void handleSignalEvent(TXElement e) throws PropertyVetoException {
    _curSignalEvent = (SignalEvent) findOrCreate(e, SignalEvent.class);
    _curME = _curSignalEvent;
    if (_firstPass) return;
    if (_curTransition != null) _curTransition.setTrigger(_curSignalEvent);
  }
  protected void handleSimpleState(TXElement e) throws PropertyVetoException {
    _curSimpleState = (SimpleState) findOrCreate(e, SimpleState.class);
    _curME = _curSimpleState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }
  protected void handle_top(TXElement e) throws PropertyVetoException { }
  protected void handle_transitions(TXElement e) throws PropertyVetoException { }
  protected void handleStateMachine(TXElement e) throws PropertyVetoException {
    _curStateMachine = (StateMachine) findOrCreate(e, StateMachine.class);
    _curME = _curStateMachine;
    if (_firstPass) return;
    if (inTag("behavior") && _curClass != null)
      _curClass.addBehavior(_curStateMachine);
    //? addToModel(_curClass);
  }
  protected void handle_submachine(TXElement e) throws PropertyVetoException { }
  protected void handleSubmachineState(TXElement e) throws PropertyVetoException {
    _curSubmachineState = (SubmachineState)
      findOrCreate(e, SubmachineState.class);
    _curME = _curSubmachineState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }
  protected void handle_duration(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    _curExpression = new TimeExpression(body);
    if (body != null && !body.equals(""))
      _curTimeEvent.setDuration((TimeExpression)_curExpression);
    //System.out.println("set guard expression to:" + body + ".");
  }
  protected void handleTimeEvent(TXElement e) throws PropertyVetoException {
    _curTimeEvent = (TimeEvent) findOrCreate(e, TimeEvent.class);
    _curME = _curTimeEvent;
    if (_firstPass) return;
    if (_curTransition != null) _curTransition.setTrigger(_curTimeEvent);
  }
  protected void handle_sourceState(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curTransition != null && _lastReference instanceof StateVertex)
      _curTransition.setSource((StateVertex)_lastReference);
  }
  protected void handle_targetState(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curTransition != null && _lastReference instanceof StateVertex)
      _curTransition.setTarget((StateVertex)_lastReference);
  }
  protected void handle_trigger(TXElement e) throws PropertyVetoException { }
  protected void handle_guard(TXElement e) throws PropertyVetoException { }
  protected void handle_effect(TXElement e) throws PropertyVetoException { }
  protected void handleTransition(TXElement e) throws PropertyVetoException {
    _curTransition = (Transition) findOrCreate(e, Transition.class);
    _curME = _curTransition;
    if (_firstPass) return;
    //? addToModel(_curME);
    if (inTag("StateMachine") && _curStateMachine != null)
      _curStateMachine.addTransition(_curTransition);
    else if (inTag("ActivityModel") && _curActivityModel != null)
      _curActivityModel.addTransition(_curTransition);
  }
  protected void handlePartition(TXElement e) throws PropertyVetoException {
    _curPartition = (Partition) findOrCreate(e, Partition.class);
    _curME = _curPartition;
    if (_firstPass) return;
  }
  protected void handleActivityModel(TXElement e) throws PropertyVetoException {
    _curActivityModel = (ActivityModel) findOrCreate(e, ActivityModel.class);
    _curME = _curActivityModel;
    if (_firstPass) return;
    if (inTag("behavior") && _curUseCase != null)
      _curUseCase.addBehavior(_curActivityModel);
    // needs-more-work: should have a stack
  }
  protected void handleActionState(TXElement e) throws PropertyVetoException {
    _curActionState = (ActionState) findOrCreate(e, ActionState.class);
    _curME = _curActionState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }
  protected void handleActivityState(TXElement e) throws PropertyVetoException {
    _curActivityState = (ActivityState) findOrCreate(e, ActivityState.class);
    _curME = _curActivityState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }
  protected void handle_inState(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curClassifierInState != null && _lastReference instanceof State)
      _curClassifierInState.setInState((State)_lastReference);
  }
  protected void handleClassifierInState(TXElement e) throws PropertyVetoException {
    _curClassifierInState = (ClassifierInState) findOrCreate(e, ClassifierInState.class);
    _curME = _curClassifierInState;
    if (_firstPass) return;
    //? addToModel(_curME);
  }
  protected void handle_typeState(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curObjectFlowState != null && _lastReference instanceof ClassifierInState)
      _curObjectFlowState.setTypeState((ClassifierInState)_lastReference);
  }
  protected void handleObjectFlowState(TXElement e) throws PropertyVetoException {
    _curObjectFlowState = (ObjectFlowState) findOrCreate(e, ObjectFlowState.class);
    _curME = _curObjectFlowState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
  }
  protected void handle_contents(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
    // needs-more-work: done with XMI.reference
  }
  protected void handle_partition(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handle_alias(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handle_referencedElement(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handleElementReference(TXElement e) throws PropertyVetoException {
    _curElementReference = (ElementReference) findOrCreate(e, ElementReference.class);
    if (_firstPass) return;
  }
  protected void handle_elementReference(TXElement e) throws PropertyVetoException {
    notImplementedYet(e);
  }
  protected void handlePackage(TXElement e) throws PropertyVetoException {
    _curPackage = (MMPackage) findOrCreate(e, MMPackage.class);
    if (_firstPass) return;
  }
  protected void handleModel(TXElement e) throws PropertyVetoException {
    _curME = (Model) findOrCreate(e, Model.class);
    if (_firstPass) return;
    addToModel(_curME);
    //if (_curModel != null)
    _ModelStack[_numNestedModels++] = _curModel;
    if (_numNestedModels > MAX_MODEL_NEST) _numNestedModels = MAX_MODEL_NEST;
    // If you nest more than 32 models deep, it will flatten the ones
    // that are at level 33 and greater.
    _curModel = (Model) _curME;
  }
  protected void handleModelEnd(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_numNestedModels > 0)
      _curModel = _ModelStack[-- _numNestedModels];
  }

  protected void handle_isInstantiable(TXElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof Subsystem)
      ((Subsystem)_curME).setIsInstantiable(b);
    else System.out.println("can't set isInstantiable");
  }
  protected void handleSubsystem(TXElement e) throws PropertyVetoException {
    _curSubsystem = (Subsystem) findOrCreate(e, Subsystem.class);
    _curME = _curSubsystem;
    if (_firstPass) return;
    addToModel(_curME);
  }


  ////////////////////////////////////////////////////////////////
  // internal model-building methods

  protected boolean inTag(String tagName) {
    return _openTags.contains(tagName);
  }

  protected Object findOrCreate(TXElement e, java.lang.Class cls) {
    String id = e.getAttribute("XMI.id");
    if (id == null) {
      System.out.println("null XMI.id: "+ cls);
    }
    Object obj = null;
    if (id != null && !id.equals("")) obj = _seen.get(id);
    if (obj != null) {
      //if (_dbg) System.out.println("found " + cls + " id=" + id);
      if (obj.getClass() != cls)
	System.out.println("class mismatch on id=" + id);
      return obj;
    }
    try {
      obj = cls.newInstance();
      int dotPos = id.indexOf(".1");
      if (dotPos > -1) {
	try {
	  int idNum = Integer.parseInt(id.substring(dotPos + 2));
	  _elementIdMax = Math.max(_elementIdMax, idNum);
	}
	catch (NumberFormatException ex) { }
      }
      if (obj instanceof ElementImpl)
	((ElementImpl)obj).setId(id);
      if (id != null && !id.equals("")) _seen.put(id, obj);
      //System.out.println("made " + cls + " id=" + id);
      return obj;
    }
    catch (Exception ex) {
      System.out.println("Exception in createAndHold:" + cls);
      ex.printStackTrace();
    }
    return null;
  }

  protected Object lookUpReference(String id) {
    if (id == null || id.equals("")) return null;
    return _seen.get(id);
  }

  protected void addToModel(ModelElement me) throws PropertyVetoException {
    if (_curModel != null) _curModel.addPublicOwnedElement(me);
    else if (_proj != null && me instanceof Model) addTopModel(me);
    else if (me instanceof Classifier) { /* will be added to definedTypes */ }
    else {
      System.out.println("trying to add to null model:" + me);
      System.out.println("id:" + me.getId());
    }
  }

  protected void addTopModel(ModelElement me) throws PropertyVetoException {
    //System.out.println("Adding top-level model");
    _proj.addModel((uci.uml.Foundation.Core.Namespace)me);
  }

  ////////////////////////////////////////////////////////////////
  // internal parsing methods

  public void ignoreElement(TXElement e) {
    //if (_dbg) System.out.println("ignoring tag:" + e.getName());
  }

  public void notImplementedYet(TXElement e) {
    if (_dbg)
      System.out.println("no metamodel class for " + e.getName());
  }


} /* end class XMIParser */

