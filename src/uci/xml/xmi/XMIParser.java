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
// import org.w3c.dom.*;
import org.xml.sax.*;


public class XMIParser extends XMIParserBase {

  ////////////////////////////////////////////////////////////////
  // constants

  public static int MAX_MODEL_NEST = 32;

  ////////////////////////////////////////////////////////////////
  // static variables

  public static XMIParser SINGLETON = new XMIParser();

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Project _proj = null;
  protected boolean _firstPass = true;

  protected ModelElementImpl _curME = null;
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
//  JH - this was a *DOM* node - doesn't seem to be the right usage; nothing in the model ?
//protected Node _curNode = null;
  protected MMException _curException = null;
  protected LocalInvocation _curLocalInvocation = null;
  protected UninterpretedAction _curUninterpretedAction = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  protected XMIParser() { super(); }

  ////////////////////////////////////////////////////////////////
  // accessors

  public void setIDs(Hashtable h)   { _seen = h; }

  public void setProject(Project p) { _proj = p; }

  public uci.uml.Foundation.Core.Namespace getCurModel() { return _curModel; }


  ////////////////////////////////////////////////////////////////
  // main parsing methods

  public synchronized void readModels(Project p, URL url) {
    _proj = p;
    long p1Time, p2Time;
    pass1(url);
    p1Time = getParseTime();
    pass2(url);
    p2Time = getParseTime();
    if (getStats()) System.out.println("Total Elapsed Time: " + (p1Time + p2Time) + " ms");
    int ec = ElementImpl.getElementCount();
    ElementImpl.setElementCount(Math.max(ec, getElementIdMax() + 1));
  }

  protected void pass1(URL url) {
    _firstPass = true;
    try {
      System.out.println("=======================================");
      System.out.println("== READING MODEL " + url);
      System.out.println("== PASS ONE");
      parse(url);
    }
    catch (Exception ex) {
      System.out.println("Exception in pass1================");
      ex.printStackTrace();
    }
  }

  protected void pass2(URL url) {
    _firstPass = false;
    try {
      System.out.println("== PASS TWO");
      parse(url);
    }
    catch (Exception ex) {
      System.out.println("Exception in pass2================");
      ex.printStackTrace();
    }
  }


  ////////////////////////////////////////////////////////////////
  // XML element handlers

  protected void handle_name(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String nStr = e.getText().trim();
    Name n = new Name(nStr);
    if (_curME != null) _curME.setName(n);
  }

  protected void handle_visibility(XMLElement e) throws PropertyVetoException {
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

  protected void handle_namespace(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e);
    // needs-more-work: done with XMI.reference
  }

  protected void handle_stereotype(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curME != null && _lastReference instanceof Stereotype)
      _curME.addStereotype((Stereotype)_lastReference);
  }

  protected void handle_constraint(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curME != null && _lastReference instanceof Constraint)
      _curME.addConstraint((Constraint)_lastReference);
    // needs-more-work: done with XMI.reference
  }

  protected void handle_provision(XMLElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
  }

  protected void handle_requirement(XMLElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
  }

  protected void handle_template(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curME != null && _lastReference instanceof ModelElement)
      _curME.setTemplate((ModelElement)_lastReference);
  }

  protected void handle_implementation(XMLElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
  }

  protected void handle_ownerScope(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String s = e.getAttribute("XMI.value");
    ScopeKind k = ScopeKind.UNSPEC;
    if (s == null || s.equals("instance")) k = ScopeKind.INSTANCE;
    else if (s.equals("classifer")) k = ScopeKind.CLASSIFIER;
    else System.out.println("unknown ScopeKind: " + s + ".");

    if (_curME instanceof Feature) ((Feature)_curME).setOwnerScope(k);
    else System.out.println("can't set OwnerScopeKind");
  }

  protected void handle_owner(XMLElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
    if (_verbose) ignoreElement(e);
  }

  protected void handle_isQuery(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof BehavioralFeature) ((BehavioralFeature)_curME).setIsQuery(b);
    else System.out.println("can't set isQuery");
  }

  protected void handle_raisedException(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (inTag("Operation")) {
      if (_curOperation != null && _lastReference instanceof MMException)
          _curOperation.addRaisedException((MMException)_lastReference);
    }
  }

  protected void handle_isAbstract(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof GeneralizableElementImpl)
      ((GeneralizableElementImpl)_curME).setIsAbstract(b);
    else System.out.println("can't set isAbstract");
  }

  protected void handle_isLeaf(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof GeneralizableElementImpl)
      ((GeneralizableElementImpl)_curME).setIsLeaf(b);
    else System.out.println("can't set isLeaf");
  }

  protected void handle_isRoot(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof GeneralizableElementImpl)
      ((GeneralizableElementImpl)_curME).setIsRoot(b);
    else System.out.println("can't set isRoot");
  }

  protected void handle_generalization(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e);
    // needs-more-work: done with XMI.reference
  }

  protected void handle_specialization(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e);
    // needs-more-work: done with XMI.reference
  }

  protected void handle_participant(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e);
    // needs-more-work: done with XMI.reference
  }

  protected void handle_realization(XMLElement e) throws PropertyVetoException {
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

  protected void handle_specification(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e);
    // needs-more-work: done with XMI.reference
  }

  protected void handle_changeable(XMLElement e) throws PropertyVetoException {
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

  protected void handle_multiplicity(XMLElement e) throws PropertyVetoException {
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


  protected void handle_targetScope(XMLElement e) throws PropertyVetoException {
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

  protected void handle_type(XMLElement e) throws PropertyVetoException {
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

  protected void handleAssociation(XMLElement e) throws PropertyVetoException {
    _curAssociation = (Association) findOrCreate(e, Association.class);
    _curME = _curAssociation;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handle_isActive(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curClass != null) _curClass.setIsActive(b);
    else System.out.println("can't set isActive");
  }

  protected void handleClass(XMLElement e) throws PropertyVetoException {
    _curClass  = (MMClass) findOrCreate(e, MMClass.class);
    _curME = _curClass;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleClassEnd(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curModel == null) _proj.defineType(_curClass);
  }

  protected void handleAssociationClass(XMLElement e) throws PropertyVetoException {
    _curAssociationClass = (AssociationClass)
    findOrCreate(e, AssociationClass.class);
    _curME = _curAssociationClass;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handle_isNavigable(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (inTag("AssociationEnd") && _curAssociationEnd != null)
      _curAssociationEnd.setIsNavigable(b);
    else if (inTag("AssociationEndRole") && _curAssociationEndRole != null)
      _curAssociationEndRole.setIsNavigable(b);
    else
      System.out.println("can't set isNavigable");
  }

  protected void handle_isOrdered(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (inTag("AssociationEnd") && _curAssociationEnd != null)
      _curAssociationEnd.setIsOrdered(b);
    else if (inTag("AssociationEndRole") && _curAssociationEndRole != null)
      _curAssociationEndRole.setIsOrdered(b);
    else
      System.out.println("can't set isOrdered");
  }

  protected void handle_aggregation(XMLElement e) throws PropertyVetoException {
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

  protected void handle_qualifier(XMLElement e) throws PropertyVetoException {
    // needs-more-work
  }

  protected void handleAssociationEnd(XMLElement e) throws PropertyVetoException {
    _curAssociationEnd = (AssociationEnd) findOrCreate(e, AssociationEnd.class);
    _curME = _curAssociationEnd;
    if (_firstPass) return;
    if (_curAssociation != null) _curAssociation.addConnection(_curAssociationEnd);
  }

  protected void handle_initialValue(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && !body.equals(""))
      _curAttribute.setInitialValue(new Expression(body));
      //System.out.println("set init val to:" + body + ".");
  }

  protected void handleAttribute(XMLElement e) throws PropertyVetoException {
    _curAttribute = (Attribute) findOrCreate(e, Attribute.class);
    _curME = _curAttribute;
    if (_firstPass) return;
    if (_curClass != null) _curClass.addStructuralFeature(_curAttribute);
  }

  protected void handle_body(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body == null) return;
    if (inTag("UninterpretedAction") && _curUninterpretedAction != null)
      _curUninterpretedAction.setBody(body);
  }

  protected void handleConstraint(XMLElement e) throws PropertyVetoException {
    _curConstraint = (Constraint) findOrCreate(e, Constraint.class);
    _curME = _curConstraint;
    if (_firstPass) return;
  }

  protected void handleDataType(XMLElement e) throws PropertyVetoException {
    _curDataType = (DataType) findOrCreate(e, DataType.class);
    _curME = _curDataType;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleDataTypeEnd(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curModel == null) _proj.defineType(_curDataType);
  }

  protected void handleDescription(XMLElement e) throws PropertyVetoException {
    //     Description o = (Description) findOrCreate(e, Description.class);
    if (_firstPass) return;
    if (_curDependency != null) {
      String desc = e.getText().trim();
      _curDependency.setDescription(desc);
    }
  }

  protected void handle_client(XMLElement e) throws PropertyVetoException {
    if (_curME instanceof Dependency) {
      if (_lastReference instanceof ModelElement) {
          ((Dependency)_curME).addClient((ModelElement)_lastReference);
      }
    }
  }

  protected void handle_supplier(XMLElement e) throws PropertyVetoException {
    if (_curME instanceof Dependency) {
      if (_lastReference instanceof ModelElement) {
          ((Dependency)_curME).addSupplier((ModelElement)_lastReference);
      }
    }
  }

  protected void handle_subDependencies(XMLElement e) throws PropertyVetoException {
    //needs-more-work
  }

  protected void handleDependency(XMLElement e) throws PropertyVetoException {
    _curDependency = (Dependency) findOrCreate(e, Dependency.class);
    _curME = _curDependency;
    if (_firstPass) return;
    // needs-more-work: if inTag("subDependencies") addSubDependency();
    addToModel(_curME);
  }

  protected void handle_discriminator(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && !body.equals(""))
      _curGeneralization.setDiscriminator(new Name(body));
    //System.out.println("set discriminator to:" + body + ".");
  }

  protected void handle_subtype(XMLElement e) throws PropertyVetoException {
    if (_curME instanceof Generalization) {
      if (_lastReference instanceof GeneralizableElement) {
          ((Generalization)_curME).setSubtype((GeneralizableElement)_lastReference);
      }
    }
  }

  protected void handle_supertype(XMLElement e) throws PropertyVetoException {
    if (_curME instanceof Generalization) {
      if (_lastReference instanceof GeneralizableElement) {
          ((Generalization)_curME).setSupertype((GeneralizableElement)_lastReference);
      }
    }
  }

  protected void handleGeneralization(XMLElement e) throws PropertyVetoException {
    _curGeneralization = (Generalization) findOrCreate(e, Generalization.class);
    _curME = _curGeneralization;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleInterface(XMLElement e) throws PropertyVetoException {
    _curInterface = (Interface) findOrCreate(e, Interface.class);
    _curME = _curInterface;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleMethod(XMLElement e) throws PropertyVetoException {
    _curMethod = (Method) findOrCreate(e, Method.class);
    _curME = _curMethod;
    if (_firstPass) return;
    // needs-more-work
  }

  protected void handle_concurrency(XMLElement e) throws PropertyVetoException {
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

  protected void handle_isPolymorphic(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curOperation != null) _curOperation.setIsPolymorphic(b);
    else System.out.println("can't set isPolymorphic");
  }

  protected void handle_operationSpecification(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && !body.equals("")) {
      if (inTag("Operation") && _curOperation != null)
          _curOperation.setSpecification(new Uninterpreted(body));
      else System.out.println("unknown context for Uninterpreted");
    }
    //System.out.println("set operation spec to:" + body + ".");
  }

  protected void handleOperation(XMLElement e) throws PropertyVetoException {
    _curOperation = (Operation) findOrCreate(e, Operation.class);
    _curME = _curOperation;
    if (_firstPass) return;
    if (inTag("Class") && _curClass != null)
      _curClass.addBehavioralFeature(_curOperation);
    else if (inTag("Interface") && _curInterface != null)
      _curInterface.addBehavioralFeature(_curOperation);
    else System.out.println("unknown context for operation: " + e.getText());
  }

  protected void handle_defaultValue(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && !body.equals(""))
      _curParameter.setDefaultValue(new Expression(body));
    //System.out.println("set default value to:" + body + ".");
  }

  protected void handle_kind(XMLElement e) throws PropertyVetoException {
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

  protected void handleParameter(XMLElement e) throws PropertyVetoException {
    _curParameter = (Parameter) findOrCreate(e, Parameter.class);
    _curME = _curParameter;
    if (_firstPass) return;
    if (inTag("Operation")) {
      if (_curOperation != null) _curOperation.addParameter(_curParameter);
    }
  }

  protected void handleComment(XMLElement e) throws PropertyVetoException {
    // needs-more-work
    //? _curComment = (Comment) findOrCreate(e, Comment.class);
    //? if (_firstPass) return;
    if (_dbg) notImplemented(e);
  }

  protected void handle_deployment(XMLElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
  }

  protected void handle_modelElement(XMLElement e) throws PropertyVetoException { }
  protected void handleComponent(XMLElement e) throws PropertyVetoException {
    if (_dbg) notImplemented(e);
    addToModel(_curME);
  }

  protected void handle_component(XMLElement e) throws PropertyVetoException {
    // needs-more-work: done with XMI.reference
  }

// JH - commented this out, it created a *DOM* node, don't think this ever
// worked correctly, but I could be wrong ;-)
//  protected void handleNode(XMLElement e) throws PropertyVetoException {
//    _curNode = (Node) findOrCreate(e, Node.class);
//    if (_firstPass) return;
//    addToModel(_curME);
//  }

  protected void handle_geometry(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e);
  }

  protected void handle_style(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e);
  }

  protected void handle_model(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e);
  }

  protected void handlePresentation(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e);
  }

  protected void handle_baseClass(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String nStr = e.getText().trim();
    Name n = new Name(nStr);
    if (_curStereotype != null) _curStereotype.setBaseClass(n);
  }

  protected void handle_icon(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e);
  }

  protected void handle_stereotypeConstraint(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curStereotype != null && _lastReference instanceof Constraint)
      _curStereotype.addStereotypeConstraint((Constraint)_lastReference);
  }

  protected void handle_extendedElement(XMLElement e) throws PropertyVetoException {
    if (_verbose) ignoreElement(e); //needs-more-work?
  }

  protected void handleStereotype(XMLElement e) throws PropertyVetoException {
    _curStereotype = (Stereotype) findOrCreate(e, Stereotype.class);
    _curME = _curStereotype;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleTaggedValue(XMLElement e) throws PropertyVetoException {
    //_curTaggedValue = (TaggedValue) findOrCreate(e, TaggedValue.class);
    if (_firstPass) return;
    _curTaggedValue = new TaggedValue();
    if (inTag("requiredTag") && _curStereotype != null)
      _curStereotype.addRequiredTag(_curTaggedValue);
    else if (_curME != null)
      _curME.addTaggedValue(_curTaggedValue);
  }

  protected void handle_tag(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && _curTaggedValue != null)
      _curTaggedValue.setTag(new Name(body));
    //System.out.println("set tag to:" + body + ".");
  }

  protected void handle_value(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null  && _curTaggedValue != null)
      _curTaggedValue.setValue(new Uninterpreted(body));
    //System.out.println("set tag VALUE to:" + body + ".");
  }

  protected void handleEnumerationLiteral(XMLElement e) throws PropertyVetoException {
    _curEnumerationLiteral = (EnumerationLiteral)
      findOrCreate(e, EnumerationLiteral.class);
    if (_firstPass) return;
    if (_curEnumeration != null) _curEnumeration.addLiteral(_curEnumerationLiteral);
  }

  protected void handleEnumeration(XMLElement e) throws PropertyVetoException {
    _curEnumeration = (uci.uml.Foundation.Data_Types.Enumeration)
      findOrCreate(e, uci.uml.Foundation.Data_Types.Enumeration.class);
    _curME = _curEnumeration;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleRequest(XMLElement e) throws PropertyVetoException {
    _curRequest = (Request) findOrCreate(e, Request.class);
    if (_firstPass) return;
  }

  protected void handleActionSequence(XMLElement e) throws PropertyVetoException {
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

  protected void handleCreateAction(XMLElement e) throws PropertyVetoException {
    _curCreateAction = (CreateAction) findOrCreate(e, CreateAction.class);
    _curME = _curCreateAction;
    if (_firstPass) return;
    //? addToModel(_curME);
  }

  protected void handleDestroyAction(XMLElement e) throws PropertyVetoException {
    _curDestroyAction = (DestroyAction) findOrCreate(e, DestroyAction.class);
    _curME = _curDestroyAction;
    if (_firstPass) return;
    //? addToModel(_curME);
  }

  protected void handleSignal(XMLElement e) throws PropertyVetoException {
    _curSignal = (Signal) findOrCreate(e, Signal.class);
    _curME = _curSignal;
    if (_firstPass) return;
    //? addToModel(_curME);
  }

  protected void handleException(XMLElement e) throws PropertyVetoException {
    _curException = (MMException) findOrCreate(e, Exception.class);
    _curME = _curException;
    if (_firstPass) return;
    //? addToModel(_curME);
  }

  protected void handleLink(XMLElement e) throws PropertyVetoException {
    _curLink = (Link) findOrCreate(e, Link.class);
    _curME = _curLink;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleLinkEnd(XMLElement e) throws PropertyVetoException {
    _curLinkEnd = (LinkEnd) findOrCreate(e, LinkEnd.class);
    _curME = _curLinkEnd;
    if (_firstPass) return;
    if (_curLink != null) _curLink.addLinkRole(_curLinkEnd);
  }

  protected void handleObject(XMLElement e) throws PropertyVetoException {
    if (_dbg) notImplemented(e);
//     _curObject = (Object) findOrCreate(e, Object.class);
//     if (_firstPass) return;
  }

  protected void handleLinkObject(XMLElement e) throws PropertyVetoException {
    _curLink = (Link) findOrCreate(e, Link.class);
    _curME = _curLink;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleLocalInvocation(XMLElement e) throws PropertyVetoException {
    _curLocalInvocation = (LocalInvocation)
      findOrCreate(e, LocalInvocation.class);
    _curME = _curLocalInvocation;
    if (_firstPass) return;
    //? addToModel(_curME);
  }

  protected void handleMessageInstance(XMLElement e) throws
  PropertyVetoException {
    if (_dbg) notImplemented(e);
//     _curMessage = (MessageInstance) findOrCreate(e, MessageInstance.class);
//     _curME = _curMessage;
//     if (_firstPass) return;
//     addToModel(_curME);
  }

  protected void handleReception(XMLElement e) throws PropertyVetoException {
    _curReception = (Reception) findOrCreate(e, Reception.class);
    _curME = _curReception;
    if (_firstPass) return;
    //? addToModel(_curME);
  }

  protected void handleReturnAction(XMLElement e) throws PropertyVetoException {
    _curReturnAction = (ReturnAction) findOrCreate(e, ReturnAction.class);
    _curME = _curReturnAction;
    if (_firstPass) return;
    //? addToModel(_curME);
  }

  protected void handleSendAction(XMLElement e) throws PropertyVetoException {
    _curSendAction = (SendAction) findOrCreate(e, SendAction.class);
    _curME = _curSendAction;
    if (_firstPass) return;
    //? addToModel(_curME);
  }

  protected void handleTerminateAction(XMLElement e) throws PropertyVetoException {
    _curTerminateAction = (TerminateAction)
      findOrCreate(e, TerminateAction.class);
    _curME = _curTerminateAction;
    if (_firstPass) return;
    //? addToModel(_curME);
  }

  protected void handleUninterpretedAction(XMLElement e) throws PropertyVetoException {
    _curUninterpretedAction = (UninterpretedAction)
      findOrCreate(e, UninterpretedAction.class);
    _curME = _curUninterpretedAction;
    if (_firstPass) return;
    if (inTag("Message") && _curMessage != null)
      _curMessage.setAction(_curUninterpretedAction);
    //? addToModel(_curME);
  }

  protected void handle_base(XMLElement e) throws PropertyVetoException {
    //@@@ set base name
    String baseStr = e.getText().trim();
//     if (inTag("AssociationRole") && _curAssociationEnd != null)
//       _curAssociationEnd.setBaseStr(baseStr);
    if (inTag("ClassifierRole") && _curClassifierRole != null)
      _curClassifierRole.setBaseString(baseStr);
  }

  protected void handleAssociationEndRole(XMLElement e) throws PropertyVetoException {
    _curAssociationEndRole = (AssociationEndRole)
      findOrCreate(e, AssociationEndRole.class);
    _curME = _curAssociationEndRole;
    if (_firstPass) return;
    if (_curAssociationRole != null)
      _curAssociationEndRole.setAssociationRole(_curAssociationRole);
  }

  protected void handleAssociationRole(XMLElement e) throws PropertyVetoException {
    _curAssociationRole = (AssociationRole)
      findOrCreate(e, AssociationRole.class);
    _curME = _curAssociationRole;
    if (_firstPass) return;
    if (_curCollaboration != null) _curCollaboration.addPublicOwnedElement(_curME);
  }

  protected void handleClassifierRole(XMLElement e) throws PropertyVetoException {
    _curClassifierRole = (ClassifierRole)
      findOrCreate(e, ClassifierRole.class);
    _curME = _curClassifierRole;
    if (_firstPass) return;
    if (_curCollaboration != null) _curCollaboration.addPublicOwnedElement(_curME);
  }

  protected void handleCollaboration(XMLElement e) throws PropertyVetoException {
    _curCollaboration = (Collaboration) findOrCreate(e, Collaboration.class);
    _curME = _curCollaboration;
    if (_firstPass) return;
    addTopModel(_curCollaboration);
  }

  protected void handle_messageRef(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curAssociationRole != null && _lastReference instanceof Message)
      _curAssociationRole.addMessage((Message)_lastReference);
  }

  protected void handleInteraction(XMLElement e) throws PropertyVetoException {
    _curInteraction = (Interaction) findOrCreate(e, Interaction.class);
    _curME = _curInteraction;
    if (_firstPass) return;
    if (_curCollaboration != null)
      _curCollaboration.setInteraction(_curInteraction);
  }

  protected void handleMessage(XMLElement e) throws PropertyVetoException {
    _curMessage = (Message) findOrCreate(e, Message.class);
    _curME = _curMessage;
    if (_firstPass) return;
    if (_curCollaboration != null) _curCollaboration.addPublicOwnedElement(_curME);
    if (_curInteraction != null) _curInteraction.addMessage(_curMessage);
  }

  protected void handleActor(XMLElement e) throws PropertyVetoException {
    _curActor = (Actor) findOrCreate(e, Actor.class);
    _curME = _curActor;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleUseCase(XMLElement e) throws PropertyVetoException {
    _curUseCase = (UseCase) findOrCreate(e, UseCase.class);
    _curME = _curUseCase;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleUseCaseInstance(XMLElement e) throws PropertyVetoException {
    _curUseCaseInstance = (UseCaseInstance)
      findOrCreate(e, UseCaseInstance.class);
    _curME = _curUseCaseInstance;
    if (_firstPass) return;
    addToModel(_curME);
  }

  protected void handleCallEvent(XMLElement e) throws PropertyVetoException {
    _curCallEvent = (CallEvent) findOrCreate(e, CallEvent.class);
    _curME = _curCallEvent;
    if (_firstPass) return;
    if (_curTransition != null) _curTransition.setTrigger(_curCallEvent);
  }

  protected void handleChangeEvent(XMLElement e) throws PropertyVetoException {
    _curChangeEvent  = (ChangeEvent) findOrCreate(e, ChangeEvent.class);
    _curME = _curChangeEvent;
    if (_firstPass) return;
    if (_curTransition != null) _curTransition.setTrigger(_curChangeEvent);
  }

  protected void handleState(XMLElement e) throws PropertyVetoException {
    //System.out.println("<State> id=" + e.getAttribute("XMI.id"));
    //System.out.println("<state> found: " + find);
    _curState = (State) findOrCreate(e, State.class);
    _curME = _curState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }

  protected void handle_isConcurrent(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof CompositeState)
      ((CompositeState)_curME).setIsConcurrent(b);
    else System.out.println("can't set isConcurrent");
  }

  protected void handleCompositeState(XMLElement e) throws PropertyVetoException {
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

  protected void handleExpression(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    if (body != null && !body.equals(""))
      _curGuard.setExpression(new BooleanExpression(body));
    //System.out.println("set guard expression to:" + body + ".");
  }

  protected void handleGuard(XMLElement e) throws PropertyVetoException {
    _curGuard= (Guard) findOrCreate(e, Guard.class);
    _curME = _curGuard;
    if (_firstPass) return;
    if (_curTransition != null) _curTransition.setGuard(_curGuard);
  }

  protected void handle_stateKind(XMLElement e) throws PropertyVetoException {
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

  protected void handlePseudostate(XMLElement e) throws PropertyVetoException {
    _curPseudostate = (Pseudostate) findOrCreate(e, Pseudostate.class);
    _curME = _curPseudostate;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }

  protected void handleSignalEvent(XMLElement e) throws PropertyVetoException {
    _curSignalEvent = (SignalEvent) findOrCreate(e, SignalEvent.class);
    _curME = _curSignalEvent;
    if (_firstPass) return;
    if (_curTransition != null) _curTransition.setTrigger(_curSignalEvent);
  }

  protected void handleSimpleState(XMLElement e) throws PropertyVetoException {
    _curSimpleState = (SimpleState) findOrCreate(e, SimpleState.class);
    _curME = _curSimpleState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }

  protected void handleStateMachine(XMLElement e) throws PropertyVetoException {
    _curStateMachine = (StateMachine) findOrCreate(e, StateMachine.class);
    _curME = _curStateMachine;
    if (_firstPass) return;
    if (inTag("behavior") && _curClass != null)
      _curClass.addBehavior(_curStateMachine);
    //? addToModel(_curClass);
  }

  protected void handleSubmachineState(XMLElement e) throws PropertyVetoException {
    _curSubmachineState = (SubmachineState)
      findOrCreate(e, SubmachineState.class);
    _curME = _curSubmachineState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }

  protected void handle_duration(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    String body = e.getText().trim();
    _curExpression = new TimeExpression(body);
    if (body != null && !body.equals(""))
      _curTimeEvent.setDuration((TimeExpression)_curExpression);
    //System.out.println("set guard expression to:" + body + ".");
  }

  protected void handleTimeEvent(XMLElement e) throws PropertyVetoException {
    _curTimeEvent = (TimeEvent) findOrCreate(e, TimeEvent.class);
    _curME = _curTimeEvent;
    if (_firstPass) return;
    if (_curTransition != null) _curTransition.setTrigger(_curTimeEvent);
  }

  protected void handle_sourceState(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curTransition != null && _lastReference instanceof StateVertex)
      _curTransition.setSource((StateVertex)_lastReference);
  }

  protected void handle_targetState(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curTransition != null && _lastReference instanceof StateVertex)
      _curTransition.setTarget((StateVertex)_lastReference);
  }

  protected void handleTransition(XMLElement e) throws PropertyVetoException {
    _curTransition = (Transition) findOrCreate(e, Transition.class);
    _curME = _curTransition;
    if (_firstPass) return;
    //? addToModel(_curME);
    if (inTag("StateMachine") && _curStateMachine != null)
      _curStateMachine.addTransition(_curTransition);
    else if (inTag("ActivityModel") && _curActivityModel != null)
      _curActivityModel.addTransition(_curTransition);
  }

  protected void handlePartition(XMLElement e) throws PropertyVetoException {
    _curPartition = (Partition) findOrCreate(e, Partition.class);
    _curME = _curPartition;
    if (_firstPass) return;
  }

  protected void handleActivityModel(XMLElement e) throws PropertyVetoException {
    _curActivityModel = (ActivityModel) findOrCreate(e, ActivityModel.class);
    _curME = _curActivityModel;
    if (_firstPass) return;
    if (inTag("behavior") && _curUseCase != null)
      _curUseCase.addBehavior(_curActivityModel);
    // needs-more-work: should have a stack
  }

  protected void handleActionState(XMLElement e) throws PropertyVetoException {
    _curActionState = (ActionState) findOrCreate(e, ActionState.class);
    _curME = _curActionState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }

  protected void handleActivityState(XMLElement e) throws PropertyVetoException {
    _curActivityState = (ActivityState) findOrCreate(e, ActivityState.class);
    _curME = _curActivityState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
    //? addToModel(_curME);
  }

  protected void handle_inState(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curClassifierInState != null && _lastReference instanceof State)
      _curClassifierInState.setInState((State)_lastReference);
  }

  protected void handleClassifierInState(XMLElement e) throws PropertyVetoException {
    _curClassifierInState = (ClassifierInState) findOrCreate(e, ClassifierInState.class);
    _curME = _curClassifierInState;
    if (_firstPass) return;
    //? addToModel(_curME);
  }

  protected void handle_typeState(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_curObjectFlowState != null && _lastReference instanceof ClassifierInState)
      _curObjectFlowState.setTypeState((ClassifierInState)_lastReference);
  }

  protected void handleObjectFlowState(XMLElement e) throws PropertyVetoException {
    _curObjectFlowState = (ObjectFlowState) findOrCreate(e, ObjectFlowState.class);
    _curME = _curObjectFlowState;
    if (_firstPass) return;
    if (inTag("CompositeState") && _curCompositeState != null)
      _curCompositeState.addSubstate((StateVertex)_curME);
  }

  protected void handle_contents(XMLElement e) throws PropertyVetoException {
    if (_dbg) notImplemented(e);
    // needs-more-work: done with XMI.reference
  }

  protected void handleElementReference(XMLElement e) throws PropertyVetoException {
    _curElementReference = (ElementReference) findOrCreate(e, ElementReference.class);
    if (_firstPass) return;
  }

  protected void handlePackage(XMLElement e) throws PropertyVetoException {
    _curPackage = (MMPackage) findOrCreate(e, MMPackage.class);
    if (_firstPass) return;
  }

  protected void handleModel(XMLElement e) throws PropertyVetoException {
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

  protected void handleModelEnd(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    if (_numNestedModels > 0)
      _curModel = _ModelStack[-- _numNestedModels];
  }

  protected void handle_isInstantiable(XMLElement e) throws PropertyVetoException {
    if (_firstPass) return;
    boolean b = "true".equals(e.getAttribute("XMI.value"));
    if (_curME instanceof Subsystem)
      ((Subsystem)_curME).setIsInstantiable(b);
    else System.out.println("can't set isInstantiable");
  }

  protected void handleSubsystem(XMLElement e) throws PropertyVetoException {
    _curSubsystem = (Subsystem) findOrCreate(e, Subsystem.class);
    _curME = _curSubsystem;
    if (_firstPass) return;
    addToModel(_curME);
  }


  ////////////////////////////////////////////////////////////////
  // internal model-building methods

  protected Object findOrCreate(XMLElement e, java.lang.Class cls) {
      Object obj = super.findOrCreate(e,cls);
      if ((obj != null) && (obj instanceof ElementImpl)) {
          ((ElementImpl)obj).setId(_lastId);
      }
      return obj;
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
    if (_dbg) System.out.println("Adding top-level model");
    _proj.addModel((uci.uml.Foundation.Core.Namespace)me);
  }

} /* end class XMIParser */

