// Traverse UML model for XMI purposes

package uci.uml.xmi;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.Collaborations.*;
import uci.uml.ui.Project;
import uci.uml.Model_Management.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.*;

public class XMITraverse {
  static PrintStream XMIfile = System.out;

  public static void traverse (Object o) {
    if (o == null) return;

    //System.out.println("Traversing - " + o.toString());

    if (o instanceof Vector) {
      Vector v = (Vector)o;

      for (int i = 0; i < v.size(); i++) {
        traverse(v.elementAt(i));
      }
    }
    else if (o instanceof Project) traverseProject((Project) o);
    else if (o instanceof Model) traverseModel((Model) o);
    else if (o instanceof Classifier) traverseClass((Classifier) o);
    else if (o instanceof ElementOwnership) traverseElementOwnership((ElementOwnership) o);
    else if (o instanceof Attribute) traverseAttribute((Attribute) o);
    else if (o instanceof Operation) traverseOperation((Operation) o);
    else if (o instanceof Link) traverseLink((Link) o);
    else if (o instanceof Attribute) traverseAttribute((Attribute) o);
    else if (o instanceof UninterpretedAction) traverseUninterpretedAction((UninterpretedAction) o);
    else if (o instanceof SubmachineState) traverseSubmachineState((SubmachineState) o);
    else if (o instanceof Constraint) traverseConstraint((Constraint) o);
    else if (o instanceof CallEvent) traverseCallEvent((CallEvent) o);
    else if (o instanceof ElementReference) traverseElementReference((ElementReference) o);
    else if (o instanceof Interaction) traverseInteraction((Interaction) o);
    else if (o instanceof AssociationClass) traverseAssociationClass((AssociationClass) o);
    else if (o instanceof ObjectFlowState) traverseObjectFlowState((ObjectFlowState) o);
    else if (o instanceof StateMachine) traverseStateMachine((StateMachine) o);
    else if (o instanceof Message) traverseMessage((Message) o);
    else if (o instanceof Enumeration) traverseEnumeration((Enumeration) o);
    else if (o instanceof Transition) traverseTransition((Transition) o);
    else if (o instanceof CreateAction) traverseCreateAction((CreateAction) o);
    //else if (o instanceof Usecase) traverseUsecase((Usecase) o);
    //else if (o instanceof LinkObject) traverseLinkObject((LinkObject) o);
    else if (o instanceof Actor) traverseActor((Actor) o);
    else if (o instanceof DestroyAction) traverseDestroyAction((DestroyAction) o);
    else if (o instanceof Object) traverseObject((Object) o);
    else if (o instanceof State) traverseState((State) o);
    //else if (o instanceof Usage) traverseUsage((Usage) o);
    else if (o instanceof DataType) traverseDataType((DataType) o);
    else if (o instanceof AssociationEnd) traverseAssociationEnd((AssociationEnd) o);
    //else if (o instanceof CallAction) traverseCallAction((CallAction) o);
    else if (o instanceof ClassifierInState) traverseClassifierInState((ClassifierInState) o);
    else if (o instanceof ReturnAction) traverseReturnAction((ReturnAction) o);
    else if (o instanceof ActionState) traverseActionState((ActionState) o);
    else if (o instanceof TimeEvent) traverseTimeEvent((TimeEvent) o);
    else if (o instanceof Interface) traverseInterface((Interface) o);
    else if (o instanceof Request) traverseRequest((Request) o);
    else if (o instanceof SimpleState) traverseSimpleState((SimpleState) o);
    else if (o instanceof SignalEvent) traverseSignalEvent((SignalEvent) o);
    else if (o instanceof Package) traversePackage((Package) o);
    else if (o instanceof ActivityState) traverseActivityState((ActivityState) o);
    else if (o instanceof Partition) traversePartition((Partition) o);
    else if (o instanceof Parameter) traverseParameter((Parameter) o);
    //else if (o instanceof Refinement) traverseRefinement((Refinement) o);
    else if (o instanceof SendAction) traverseSendAction((SendAction) o);
    else if (o instanceof LocalInvocation) traverseLocalInvocation((LocalInvocation) o);
    //else if (o instanceof DataValue) traverseDataValue((DataValue) o);
    else if (o instanceof Association) traverseAssociation((Association) o);
    else if (o instanceof Reception) traverseReception((Reception) o);
    //else if (o instanceof Binding) traverseBinding((Binding) o);
    else if (o instanceof Subsystem) traverseSubsystem((Subsystem) o);
    //else if (o instanceof PseudoState) traversePseudoState((PseudoState) o);
    //else if (o instanceof Comment) traverseComment((Comment) o);
    else if (o instanceof Collaboration) traverseCollaboration((Collaboration) o);
    else if (o instanceof AssociationRole) traverseAssociationRole((AssociationRole) o);
    else if (o instanceof Method) traverseMethod((Method) o);
    //else if (o instanceof UsecaseInstance) traverseUsecaseInstance((UsecaseInstance) o);
    else if (o instanceof AssociationEndRole) traverseAssociationEndRole((AssociationEndRole) o);
    else if (o instanceof Model) traverseModel((Model) o);
    else if (o instanceof EnumerationLiteral) traverseEnumerationLiteral((EnumerationLiteral) o);
    else if (o instanceof Stereotype) traverseStereotype((Stereotype) o);
    else if (o instanceof Signal) traverseSignal((Signal) o);
    else if (o instanceof Generalization) traverseGeneralization((Generalization) o);
    //else if (o instanceof Component) traverseComponent((Component) o);
    else if (o instanceof Dependency) traverseDependency((Dependency) o);
    else if (o instanceof Exception) traverseException((Exception) o);
    else if (o instanceof LinkEnd) traverseLinkEnd((LinkEnd) o);
    //else if (o instanceof Node) traverseNode((Node) o);
    else if (o instanceof Guard) traverseGuard((Guard) o);
    else if (o instanceof ClassifierRole) traverseClassifierRole((ClassifierRole) o);
    else if (o instanceof Operation) traverseOperation((Operation) o);
    else if (o instanceof ActionSequence) traverseActionSequence((ActionSequence) o);
    else if (o instanceof CompositeState) traverseCompositeState((CompositeState) o);
    else if (o instanceof TaggedValue) traverseTaggedValue((TaggedValue) o);
    //else if (o instanceof Trace) traverseTrace((Trace) o);
    else if (o instanceof ActivityModel) traverseActivityModel((ActivityModel) o);
    //else if (o instanceof Argument) traverseArgument((Argument) o);
    else if (o instanceof TerminateAction) traverseTerminateAction((TerminateAction) o);
    //else if (o instanceof AttributeLink) traverseAttributeLink((AttributeLink) o);
    else if (o instanceof ChangeEvent) traverseChangeEvent((ChangeEvent) o);
    //else if (o instanceof MessageInstance) traverseMessageInstance((MessageInstance) o);
    //else if (o instanceof ViewElement) traverseViewElement((ViewElement) o);
    else { System.out.println("! Got an object that cannot be traversed");
           System.out.println("! it is: " + o.getClass()); }
  }

  public static void simpleOCLRun (Object runOn, Reader inTempl) {
    BufferedReader template = new BufferedReader(inTempl);
    String currentLine = "";

    // This loop handles line-size chunks
    while (currentLine != null)
    {
      try {
        currentLine = template.readLine();
      } catch (IOException ie) {
        System.err.println("Caught an IOEc");
      }

      if (currentLine == null) break;

      //System.err.println("# " + currentLine);

      ParseString ps = new ParseString(currentLine);

      // This loop handles OCL'ish statements to be executed and replaced
      while (true) {
        String currToken = ps.nextToken();

        if (currToken == null) break;

        //System.out.println("Token: " + currToken);

        String outputString = OCLCommandProcess(runOn, currToken);

        ps.replaceCurrentToken(outputString);
      } 

      if (!(ps.equals("") || ps.equals("\n"))) System.out.println(ps);
    }
  }

  static String OCLCommandProcess (Object runOn, String command) {
    StringTokenizer st = new StringTokenizer(command, ".");

    String commandPart = st.nextToken();
    String currentToken = null;

    if (commandPart.equals("self") | commandPart.equals("traverse") | commandPart.equals("reference")) {
      Vector props = null;
      Object propsOne = runOn;

      // This loop handles getting values and calling functions with those
      while (st.hasMoreTokens()) {
          currentToken = st.nextToken();
          if (propsOne instanceof ElementImpl) {
            // If it can return a property, use that capability
            props = ((ElementImpl)propsOne).getNamedProperty(currentToken);
          } else {
            // In this case we grovel for a "get" method of this name
            props = figureGetMethod(currentToken, propsOne);
          }

          if (props.size() > 1) {
            traverse(props);

            return "";

          } else if (props.size() < 1) { return ""; } 
          else propsOne = props.firstElement();
      }   

      if (propsOne == null) return "";
      else if (commandPart.equals("reference")) { return "<" + currentToken + ">\n  <XMI.reference href='" + OCLCommandProcess(propsOne, "$self.id$\n") + "' expected-Type='" + OCLCommandProcess(propsOne, "$self.name.body$") + "'>\n</" + currentToken + ">\n"; }
      else if (commandPart.equals("traverse")) { traverse(propsOne); return ""; }
      else return propsOne.toString();

    } else if (commandPart.equals("gen")) {
      // Implement later
    }

    return "--b command--";
  }


  public static Vector figureGetMethod(String propName, Object classToMeddle) {
    Class voidArray[] = {};
    Object objArray[] = {};
    java.lang.reflect.Method methodToCall = null;
    Vector returnVector = new Vector();
    String realName = null;

    try {
      realName = "get" + propName.substring(0,1).toUpperCase() + propName.substring(1, propName.length());
      methodToCall = classToMeddle.getClass().getMethod(realName, voidArray); 
    } catch (NoSuchMethodException ne) {
      //System.err.println("NO method (" + realName + ") matched in figureGetMethod!");
    }

    try {
    returnVector.addElement(methodToCall.invoke(classToMeddle, objArray));

    if (returnVector.firstElement() instanceof Vector) returnVector = (Vector)(returnVector.firstElement());

    } catch (Exception e) {
      //System.err.println("Not happy with invoke!");
    }
    
    return returnVector;
  } 

  public static void traverseProject (Project p) {
    String Ss = "<XMI xmi-version='1.0'>\n" +
     "<XMI.header>\n" +
     "<XMI.metamodel name='uml' version='1.1'>\n\n" +
     "</XMI.header>\n\n" +
     "<XMI.content>\n\n" +
     "$traverse.currentModel$\n\n" +
     "</XMI.content>\n\n" +
     "</XMI>\n"; 

    simpleOCLRun(p, new StringReader(Ss));
  } 

  public static void traverseElementOwnership (ElementOwnership c) {
    String cT = "<ownedElement>\n\n" +
    "$traverse.modelElement$\n" +
    "</ownedElement>\n";

    //System.out.println("Traversing ElementOwnership/ownedElement");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseActionSequence (ActionSequence c) {
    String cT = "<ActionSequence XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.action$\n" +
    "</ActionSequence>\n";

    //System.out.println("Traversing ActionSequence");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseActionState (ActionState c) {
    String cT = "<ActionState XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.parent$\n" +
    "$traverse.outgoing$\n" +
    "$traverse.incoming$\n" +
    "$traverse.deferredEvent$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.entry$\n" +
    "$traverse.exit$\n" +
    "$traverse.internalTransition$\n" +
    "</ActionState>\n";

    //System.out.println("Traversing ActionState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseActivityModel (ActivityModel c) {
    String cT = "<ActivityModel XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.context$\n" +
    "</ActivityModel>\n";

    //System.out.println("Traversing ActivityModel");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseActivityState (ActivityState c) {
    String cT = "<ActivityState XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.parent$\n" +
    "$traverse.outgoing$\n" +
    "$traverse.incoming$\n" +
    "$traverse.deferredEvent$\n" +
    "$traverse.submachine$\n" +
    "</ActivityState>\n";

    //System.out.println("Traversing ActivityState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseActor (Actor c) {
    String cT = "<Actor XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.feature$\n" +
    "</Actor>\n";

    //System.out.println("Traversing Actor");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseArgument (Argument c) {
    String cT = "<Argument XMI.id='$self.id$'>\n\n" +
    "$traverse.argumentValue$\n" +
    "</Argument>\n";

    //System.out.println("Traversing Argument");
    simpleOCLRun(c, new StringReader(cT));
  } */
  public static void traverseAssociation (Association c) {
    String cT = "<Association XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.source$\n" +
    "</Association>\n";

    //System.out.println("Traversing Association");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseAssociationClass (AssociationClass c) {
    String cT = "<AssociationClass XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    "<isActive XMI.value='$self.isActive$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.feature$\n" +
    "$traverse.source$\n" +
    "</AssociationClass>\n";

    //System.out.println("Traversing AssociationClass");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseAssociationEnd (AssociationEnd c) {
    String cT = "<AssociationEnd XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isNavigable XMI.value='$self.isNavigable$'/>\n" +
    "<isOrdered XMI.value='$self.isOrdered$'/>\n" +
    "<aggregation XMI.value='$self.aggregation$'/>\n" +
    "$traverse.multiplicity$\n" +
    "<changeable XMI.value='$self.changeable$'/>\n" +
    "<targetScope XMI.value='$self.targetScope$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.type$\n" +
    "</AssociationEnd>\n";

    //System.out.println("Traversing AssociationEnd");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseAssociationEndRole (AssociationEndRole c) {
    String cT = "<AssociationEndRole XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isNavigable XMI.value='$self.isNavigable$'/>\n" +
    "<isOrdered XMI.value='$self.isOrdered$'/>\n" +
    "<aggregation XMI.value='$self.aggregation$'/>\n" +
    "$traverse.multiplicity$\n" +
    "<changeable XMI.value='$self.changeable$'/>\n" +
    "<targetScope XMI.value='$self.targetScope$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.type$\n" +
    "</AssociationEndRole>\n";

    //System.out.println("Traversing AssociationEndRole");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseAssociationRole (AssociationRole c) {
    String cT = "<AssociationRole XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    "$traverse.multiplicity$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.base$\n" +
    "</AssociationRole>\n";

    //System.out.println("Traversing AssociationRole");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseAttribute (Attribute c) {
    String cT = "<Attribute XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<ownerScope XMI.value='$self.ownerScope$'/>\n" +
    "<changeable XMI.value='$self.changeable$'/>\n" +
    "$traverse.multiplicity$\n" +
    "<initialValue>$self.initialValue$</initialValue>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$reference.owner$\n" +
    "</Attribute>\n";

    //System.out.println("Traversing Attribute");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseAttributeLink (AttributeLink c) {
    String cT = "<AttributeLink XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.attribute$\n" +
    "</AttributeLink>\n";

    //System.out.println("Traversing AttributeLink");
    simpleOCLRun(c, new StringReader(cT));
  } */
  /*
  public static void traverseBinding (Binding c) {
    String cT = "<Binding XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.description$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.owningDependency$\n" +
    "$traverse.client$\n" +
    "$traverse.supplier$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.subDependencies$\n" +
    "$traverse.argument$\n" +
    "</Binding>\n";

    //System.out.println("Traversing Binding");
    simpleOCLRun(c, new StringReader(cT));
  } */
  /*
  public static void traverseCallAction (CallAction c) {
    String cT = "<CallAction XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.recurrence$\n" +
    "$traverse.actionTarget$\n" +
    "<isAsynchronous XMI.value='$self.isAsynchronous$'/>\n" +
    "$traverse.script$\n" +
    "<mode XMI.value='$self.mode$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.request$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.actualArgument$\n" +
    "</CallAction>\n";

    //System.out.println("Traversing CallAction");
    simpleOCLRun(c, new StringReader(cT));
  } */
  public static void traverseCallEvent (CallEvent c) {
    String cT = "<CallEvent XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.operation$\n" +
    "</CallEvent>\n";

    //System.out.println("Traversing CallEvent");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseChangeEvent (ChangeEvent c) {
    String cT = "<ChangeEvent XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.changeExpression$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "</ChangeEvent>\n";

    //System.out.println("Traversing ChangeEvent");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseClass (Classifier c) {
    String cT = "<Class XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    "<isActive XMI.value='$self.isActive$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.behavioralFeature$\n" +
    "$traverse.structuralFeature$\n" +
    "</Class>\n";

    //System.out.println("Traversing Class");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseClassifierInState (ClassifierInState c) {
    String cT = "<ClassifierInState XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.type$\n" +
    "</ClassifierInState>\n";

    //System.out.println("Traversing ClassifierInState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseClassifierRole (ClassifierRole c) {
    String cT = "<ClassifierRole XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.multiplicity$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.base$\n" +
    "</ClassifierRole>\n";

    //System.out.println("Traversing ClassifierRole");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseCollaboration (Collaboration c) {
    String cT = "<Collaboration XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.constrainingElement$\n" +
    "$traverse.representedClassifier$\n" +
    "$traverse.representedOperation$\n" +
    "</Collaboration>\n";

    //System.out.println("Traversing Collaboration");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseComment (Comment c) {
    String cT = "<Comment XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.element$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "</Comment>\n";

    //System.out.println("Traversing Comment");
    simpleOCLRun(c, new StringReader(cT));
  } */
  /*
  public static void traverseComponent (Component c) {
    String cT = "<Component XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    "<isActive XMI.value='$self.isActive$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.deployment$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.feature$\n" +
    "</Component>\n";

    //System.out.println("Traversing Component");
    simpleOCLRun(c, new StringReader(cT));
  } */
  public static void traverseCompositeState (CompositeState c) {
    String cT = "<CompositeState XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isConcurrent XMI.value='$self.isConcurrent$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.parent$\n" +
    "$traverse.outgoing$\n" +
    "$traverse.incoming$\n" +
    "$traverse.deferredEvent$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.entry$\n" +
    "$traverse.exit$\n" +
    "$traverse.internalTransition$\n" +
    "$traverse.substate$\n" +
    "</CompositeState>\n";

    //System.out.println("Traversing CompositeState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseConstraint (Constraint c) {
    String cT = "<Constraint XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.body$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.constrainedElement$\n" +
    "$traverse.constrainedStereotype$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "</Constraint>\n";

    //System.out.println("Traversing Constraint");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseCreateAction (CreateAction c) {
    String cT = "<CreateAction XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.recurrence$\n" +
    "$traverse.actionTarget$\n" +
    "<isAsynchronous XMI.value='$self.isAsynchronous$'/>\n" +
    "$traverse.script$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.request$\n" +
    "$traverse.instantiation$\n" +
    "</CreateAction>\n";

    //System.out.println("Traversing CreateAction");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseDataType (DataType c) {
    String cT = "<DataType XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.feature$\n" +
    "</DataType>\n";

    //System.out.println("Traversing DataType");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseDataValue (DataValue c) {
    String cT = "<DataValue XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.classifier$\n" +
    "</DataValue>\n";

    //System.out.println("Traversing DataValue");
    simpleOCLRun(c, new StringReader(cT));
  } */
  public static void traverseDependency (Dependency c) {
    String cT = "<Dependency XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.description$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.owningDependency$\n" +
    "$traverse.client$\n" +
    "$traverse.supplier$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.subDependencies$\n" +
    "</Dependency>\n";

    //System.out.println("Traversing Dependency");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseDestroyAction (DestroyAction c) {
    String cT = "<DestroyAction XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.recurrence$\n" +
    "$traverse.actionTarget$\n" +
    "<isAsynchronous XMI.value='$self.isAsynchronous$'/>\n" +
    "$traverse.script$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.request$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.actualArgument$\n" +
    "</DestroyAction>\n";

    //System.out.println("Traversing DestroyAction");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseElementReference (ElementReference c) {
    String cT = "<ElementReference XMI.id='$self.id$'>\n\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.alias$\n" +
    "</ElementReference>\n";

    //System.out.println("Traversing ElementReference");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseEnumeration (Enumeration c) {
    String cT = "<Enumeration XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.feature$\n" +
    "$traverse.literal$\n" +
    "</Enumeration>\n";

    //System.out.println("Traversing Enumeration");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseEnumerationLiteral (EnumerationLiteral c) {
    String cT = "<EnumerationLiteral XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    "</EnumerationLiteral>\n";

    //System.out.println("Traversing EnumerationLiteral");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseException (Exception c) {
    String cT = "<Exception XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    "$traverse.body$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.reception$\n" +
    "$traverse.occurrence$\n" +
    "$traverse.context$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.parameter$\n" +
    "</Exception>\n";

    //System.out.println("Traversing Exception");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseGeneralization (Generalization c) {
    String cT = "<Generalization XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.discriminator$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.subtype$\n" +
    "</Generalization>\n";

    //System.out.println("Traversing Generalization");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseGuard (Guard c) {
    String cT = "<Guard XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.expression$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "</Guard>\n";

    //System.out.println("Traversing Guard");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseInteraction (Interaction c) {
    String cT = "<Interaction XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.context$\n" +
    "</Interaction>\n";

    //System.out.println("Traversing Interaction");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseInterface (Interface c) {
    String cT = "<Interface XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.feature$\n" +
    "</Interface>\n";

    //System.out.println("Traversing Interface");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseLink (Link c) {
    String cT = "<Link XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.association$\n" +
    "</Link>\n";

    //System.out.println("Traversing Link");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseLinkEnd (LinkEnd c) {
    String cT = "<LinkEnd XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.instance$\n" +
    "</LinkEnd>\n";

    //System.out.println("Traversing LinkEnd");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseLinkObject (LinkObject c) {
    String cT = "<LinkObject XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.classifier$\n" +
    "</LinkObject>\n";

    //System.out.println("Traversing LinkObject");
    simpleOCLRun(c, new StringReader(cT));
  } */
  public static void traverseLocalInvocation (LocalInvocation c) {
    String cT = "<LocalInvocation XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.recurrence$\n" +
    "$traverse.actionTarget$\n" +
    "<isAsynchronous XMI.value='$self.isAsynchronous$'/>\n" +
    "$traverse.script$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.request$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.actualArgument$\n" +
    "</LocalInvocation>\n";

    //System.out.println("Traversing LocalInvocation");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseMessage (Message c) {
    String cT = "<Message XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.activator$\n" +
    "</Message>\n";

    //System.out.println("Traversing Message");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseMessageInstance (MessageInstance c) {
    String cT = "<MessageInstance XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.specification$\n" +
    "</MessageInstance>\n";

    //System.out.println("Traversing MessageInstance");
    simpleOCLRun(c, new StringReader(cT));
  } */
  public static void traverseMethod (Method c) {
    String cT = "<Method XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<ownerScope XMI.value='$self.ownerScope$'/>\n" +
    "<isQuery XMI.value='$self.isQuery$'/>\n" +
    "$traverse.body$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$reference.owner$\n" +
    "</Method>\n";

    //System.out.println("Traversing Method");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseModel (Model c) {
    String cT = "<Model XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "</Model>\n";

    //System.out.println("Traversing Model");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseNode (Node c) {
    String cT = "<Node XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    "<isActive XMI.value='$self.isActive$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.component$\n" +
    "</Node>\n";

    //System.out.println("Traversing Node");
    simpleOCLRun(c, new StringReader(cT));
  } */
  public static void traverseObject (Object c) {
    String cT = "<Object XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.classifier$\n" +
    "</Object>\n";

    return;

    //System.out.println("Traversing Object");
    //simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseObjectFlowState (ObjectFlowState c) {
    String cT = "<ObjectFlowState XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.parent$\n" +
    "$traverse.outgoing$\n" +
    "$traverse.incoming$\n" +
    "$traverse.deferredEvent$\n" +
    "$traverse.typeState$\n" +
    "</ObjectFlowState>\n";

    //System.out.println("Traversing ObjectFlowState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseOperation (Operation c) {
    String cT = "<Operation XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<ownerScope XMI.value='$self.ownerScope$'/>\n" +
    "<isQuery XMI.value='$self.isQuery$'/>\n" +
    "<concurrency XMI.value='$self.concurrency$'/>\n" +
    "<isPolymorphic XMI.value='$self.isPolymorphic$'/>\n" +
    "$traverse.operationSpecification$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$reference.owner$\n" +
    "</Operation>\n";

    //System.out.println("Traversing Operation");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversePackage (Package c) {
    String cT = "<Package XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "</Package>\n";

    //System.out.println("Traversing Package");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseParameter (Parameter c) {
    String cT = "<Parameter XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.defaultValue$\n" +
    "<kind XMI.value='$self.kind$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.type$\n" +
    "</Parameter>\n";

    //System.out.println("Traversing Parameter");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversePartition (Partition c) {
    String cT = "<Partition XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.contents$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "</Partition>\n";

    //System.out.println("Traversing Partition");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversePseudostate (Pseudostate c) {
    String cT = "<Pseudostate XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<stateKind XMI.value='$self.stateKind$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.parent$\n" +
    "$traverse.outgoing$\n" +
    "$traverse.incoming$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "</Pseudostate>\n";

    //System.out.println("Traversing Pseudostate");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseReception (Reception c) {
    String cT = "<Reception XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<ownerScope XMI.value='$self.ownerScope$'/>\n" +
    "<isQuery XMI.value='$self.isQuery$'/>\n" +
    "<isPolymorphic XMI.value='$self.isPolymorphic$'/>\n" +
    "$traverse.operationSpecification$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$reference.owner$\n" +
    "</Reception>\n";

    //System.out.println("Traversing Reception");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseRefinement (Refinement c) {
    String cT = "<Refinement XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.description$\n" +
    "$traverse.mapping$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.owningDependency$\n" +
    "$traverse.client$\n" +
    "$traverse.supplier$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.subDependencies$\n" +
    "</Refinement>\n";

    //System.out.println("Traversing Refinement");
    simpleOCLRun(c, new StringReader(cT));
  } */
  public static void traverseRequest (Request c) {
    String cT = "<Request XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "</Request>\n";

    //System.out.println("Traversing Request");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseReturnAction (ReturnAction c) {
    String cT = "<ReturnAction XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.recurrence$\n" +
    "$traverse.actionTarget$\n" +
    "<isAsynchronous XMI.value='$self.isAsynchronous$'/>\n" +
    "$traverse.script$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.request$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.actualArgument$\n" +
    "</ReturnAction>\n";

    //System.out.println("Traversing ReturnAction");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseSendAction (SendAction c) {
    String cT = "<SendAction XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.recurrence$\n" +
    "$traverse.actionTarget$\n" +
    "<isAsynchronous XMI.value='$self.isAsynchronous$'/>\n" +
    "$traverse.script$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.request$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.actualArgument$\n" +
    "</SendAction>\n";

    //System.out.println("Traversing SendAction");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseSignal (Signal c) {
    String cT = "<Signal XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.reception$\n" +
    "$traverse.occurrence$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.parameter$\n" +
    "</Signal>\n";

    //System.out.println("Traversing Signal");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseSignalEvent (SignalEvent c) {
    String cT = "<SignalEvent XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.signal$\n" +
    "</SignalEvent>\n";

    //System.out.println("Traversing SignalEvent");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseSimpleState (SimpleState c) {
    String cT = "<SimpleState XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.parent$\n" +
    "$traverse.outgoing$\n" +
    "$traverse.incoming$\n" +
    "$traverse.deferredEvent$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.entry$\n" +
    "$traverse.exit$\n" +
    "$traverse.internalTransition$\n" +
    "</SimpleState>\n";

    //System.out.println("Traversing SimpleState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseState (State c) {
    String cT = "<State XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.parent$\n" +
    "$traverse.outgoing$\n" +
    "$traverse.incoming$\n" +
    "$traverse.deferredEvent$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.entry$\n" +
    "$traverse.exit$\n" +
    "$traverse.internalTransition$\n" +
    "</State>\n";

    //System.out.println("Traversing State");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseStateMachine (StateMachine c) {
    String cT = "<StateMachine XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.context$\n" +
    "</StateMachine>\n";

    //System.out.println("Traversing StateMachine");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseStereotype (Stereotype c) {
    String cT = "<Stereotype XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    "$traverse.baseClass$\n" +
    "$traverse.icon$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.stereotypeConstraint$\n" +
    "$traverse.extendedElement$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.requiredTag$\n" +
    "</Stereotype>\n";

    //System.out.println("Traversing Stereotype");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseSubmachineState (SubmachineState c) {
    String cT = "<SubmachineState XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.parent$\n" +
    "$traverse.outgoing$\n" +
    "$traverse.incoming$\n" +
    "$traverse.deferredEvent$\n" +
    "$traverse.submachine$\n" +
    "</SubmachineState>\n";

    //System.out.println("Traversing SubmachineState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseSubsystem (Subsystem c) {
    String cT = "<Subsystem XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    "<isInstantiable XMI.value='$self.isInstantiable$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.feature$\n" +
    "</Subsystem>\n";

    //System.out.println("Traversing Subsystem");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseTaggedValue (TaggedValue c) {
    String cT = "<TaggedValue XMI.id='$self.id$'>\n\n" +
    "$traverse.tag$\n" +
    "$traverse.value$\n" +
    "$traverse.constrainedStereotype$\n" +
    "</TaggedValue>\n";

    //System.out.println("Traversing TaggedValue");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseTerminateAction (TerminateAction c) {
    String cT = "<TerminateAction XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.recurrence$\n" +
    "$traverse.actionTarget$\n" +
    "<isAsynchronous XMI.value='$self.isAsynchronous$'/>\n" +
    "$traverse.script$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.request$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.actualArgument$\n" +
    "</TerminateAction>\n";

    //System.out.println("Traversing TerminateAction");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseTimeEvent (TimeEvent c) {
    String cT = "<TimeEvent XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.duration$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "</TimeEvent>\n";

    //System.out.println("Traversing TimeEvent");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseTrace (Trace c) {
    String cT = "<Trace XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.description$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.owningDependency$\n" +
    "$traverse.client$\n" +
    "$traverse.supplier$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.subDependencies$\n" +
    "</Trace>\n";

    //System.out.println("Traversing Trace");
    simpleOCLRun(c, new StringReader(cT));
  } */
  public static void traverseTransition (Transition c) {
    String cT = "<Transition XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.sourceState$\n" +
    "</Transition>\n";

    //System.out.println("Traversing Transition");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseUninterpretedAction (UninterpretedAction c) {
    String cT = "<UninterpretedAction XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.recurrence$\n" +
    "$traverse.actionTarget$\n" +
    "<isAsynchronous XMI.value='$self.isAsynchronous$'/>\n" +
    "$traverse.script$\n" +
    "$traverse.body$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.request$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.actualArgument$\n" +
    "</UninterpretedAction>\n";

    //System.out.println("Traversing UninterpretedAction");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseUsage (Usage c) {
    String cT = "<Usage XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.description$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.owningDependency$\n" +
    "$traverse.client$\n" +
    "$traverse.supplier$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.subDependencies$\n" +
    "</Usage>\n";

    //System.out.println("Traversing Usage");
    simpleOCLRun(c, new StringReader(cT));
  } */
  public static void traverseUseCase (UseCase c) {
    String cT = "<UseCase XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isAbstract XMI.value='$self.isAbstract$'/>\n" +
    "<isLeaf XMI.value='$self.isLeaf$'/>\n" +
    "<isRoot XMI.value='$self.isRoot$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.generalization$\n" +
    "$traverse.specialization$\n" +
    "$traverse.participant$\n" +
    "$traverse.realization$\n" +
    "$traverse.specification$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.ownedElement$\n" +
    "$traverse.feature$\n" +
    "$traverse.ViewElement$\n" +
    "$traverse.extensionPoint$\n" +
    "</UseCase>\n";

    //System.out.println("Traversing UseCase");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseUseCaseInstance (UseCaseInstance c) {
    String cT = "<UseCaseInstance XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.classifier$\n" +
    "</UseCaseInstance>\n";

    //System.out.println("Traversing UseCaseInstance");
    simpleOCLRun(c, new StringReader(cT));
  }
  /*
  public static void traverseViewElement (ViewElement c) {
    String cT = "<ViewElement XMI.id='$self.id$'>\n\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.TaggedValue$\n" +
    "$traverse.modelElement$\n" +
    "$traverse.ViewElement$\n" +
    //"$traverse.viewElement$\n" +
    "</ViewElement>\n";

    //System.out.println("Traversing ViewElement");
    simpleOCLRun(c, new StringReader(cT));
  } */
  /*
  public static void traverseaction (action c) {
    String cT = "<action XMI.id='$self.id$'>\n\n" +
    "$traverse.CreateAction$\n" +
    "$traverse.CallAction$\n" +
    "$traverse.LocalInvocation$\n" +
    "$traverse.ReturnAction$\n" +
    "$traverse.SendAction$\n" +
    "$traverse.TerminateAction$\n" +
    "$traverse.DestroyAction$\n" +
    "$traverse.UninterpretedAction$\n" +
    "</action>\n";

    //System.out.println("Traversing action");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseactionTarget (actionTarget c) {
    String cT = "<actionTarget XMI.id='$self.id$'>\n\n" +
    "</actionTarget>\n";

    //System.out.println("Traversing actionTarget");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseactivator (activator c) {
    String cT = "<activator XMI.id='$self.id$'>\n\n" +
    "</activator>\n";

    //System.out.println("Traversing activator");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseactualArgument (actualArgument c) {
    String cT = "<actualArgument XMI.id='$self.id$'>\n\n" +
    "$traverse.Argument$\n" +
    "$traverse.argumentValue$\n" +
    "</actualArgument>\n";

    //System.out.println("Traversing actualArgument");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseaggregation (aggregation c) {
    String cT = "<aggregation XMI.id='$self.id$'>\n\n" +
    "</aggregation>\n";

    //System.out.println("Traversing aggregation");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversealias (alias c) {
    String cT = "<alias XMI.id='$self.id$'>\n\n" +
    "</alias>\n";

    //System.out.println("Traversing alias");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseargument (argument c) {
    String cT = "<argument XMI.id='$self.id$'>\n\n" +
    "$traverse.Model$\n" +
    "$traverse.Subsystem$\n" +
    "$traverse.Package$\n" +
    "$traverse.Class$\n" +
    "$traverse.Generalization$\n" +
    "$traverse.Association$\n" +
    "$traverse.Interface$\n" +
    "$traverse.ViewElement$\n" +
    "$traverse.UseCase$\n" +
    "$traverse.AssociationClass$\n" +
    "$traverse.Node$\n" +
    "$traverse.Component$\n" +
    "$traverse.Comment$\n" +
    "$traverse.Signal$\n" +
    "$traverse.Exception$\n" +
    "$traverse.Object$\n" +
    "$traverse.Link$\n" +
    "$traverse.LinkObject$\n" +
    "$traverse.UseCaseInstance$\n" +
    "$traverse.ElementReference$\n" +
    "$traverse.Actor$\n" +
    "$traverse.Dependency$\n" +
    "$traverse.Refinement$\n" +
    "$traverse.Usage$\n" +
    "$traverse.Trace$\n" +
    "$traverse.Binding$\n" +
    "$traverse.Constraint$\n" +
    "$traverse.Stereotype$\n" +
    "$traverse.StateMachine$\n" +
    "$traverse.ActivityModel$\n" +
    "$traverse.Collaboration$\n" +
    "$traverse.Request$\n" +
    "$traverse.ClassifierRole$\n" +
    "$traverse.AssociationRole$\n" +
    "</argument>\n";

    //System.out.println("Traversing argument");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseargumentValue (argumentValue c) {
    String cT = "<argumentValue XMI.id='$self.id$'>\n\n" +
    "</argumentValue>\n";

    //System.out.println("Traversing argumentValue");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseassociation (association c) {
    String cT = "<association XMI.id='$self.id$'>\n\n" +
    "</association>\n";

    //System.out.println("Traversing association");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseassociationEnd (associationEnd c) {
    String cT = "<associationEnd XMI.id='$self.id$'>\n\n" +
    "</associationEnd>\n";

    //System.out.println("Traversing associationEnd");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseattribute (attribute c) {
    String cT = "<attribute XMI.id='$self.id$'>\n\n" +
    "</attribute>\n";

    //System.out.println("Traversing attribute");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseattributeValue (attributeValue c) {
    String cT = "<attributeValue XMI.id='$self.id$'>\n\n" +
    "$traverse.DataValue$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.classifier$\n" +
    "</attributeValue>\n";

    //System.out.println("Traversing attributeValue");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseavailableFeature (availableFeature c) {
    String cT = "<availableFeature XMI.id='$self.id$'>\n\n" +
    "$traverse.Attribute$\n" +
    "$traverse.Operation$\n" +
    "$traverse.Method$\n" +
    "$traverse.Reception$\n" +
    "</availableFeature>\n";

    //System.out.println("Traversing availableFeature");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversebase (base c) {
    String cT = "<base XMI.id='$self.id$'>\n\n" +
    "</base>\n";

    //System.out.println("Traversing base");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversebaseClass (baseClass c) {
    String cT = "<baseClass XMI.id='$self.id$'>\n\n" +
    "</baseClass>\n";

    //System.out.println("Traversing baseClass");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversebehavior (behavior c) {
    String cT = "<behavior XMI.id='$self.id$'>\n\n" +
    "$traverse.StateMachine$\n" +
    "$traverse.ActivityModel$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.context$\n" +
    "</behavior>\n";

    //System.out.println("Traversing behavior");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversebody (body c) {
    String cT = "<body XMI.id='$self.id$'>\n\n" +
    "</body>\n";

    //System.out.println("Traversing body");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversechangeExpression (changeExpression c) {
    String cT = "<changeExpression XMI.id='$self.id$'>\n\n" +
    "</changeExpression>\n";

    //System.out.println("Traversing changeExpression");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversechangeable (changeable c) {
    String cT = "<changeable XMI.id='$self.id$'>\n\n" +
    "</changeable>\n";

    //System.out.println("Traversing changeable");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseclassifier (classifier c) {
    String cT = "<classifier XMI.id='$self.id$'>\n\n" +
    "</classifier>\n";

    //System.out.println("Traversing classifier");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseclient (client c) {
    String cT = "<client XMI.id='$self.id$'>\n\n" +
    "</client>\n";

    //System.out.println("Traversing client");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversecomponent (component c) {
    String cT = "<component XMI.id='$self.id$'>\n\n" +
    "</component>\n";

    //System.out.println("Traversing component");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseconcurrency (concurrency c) {
    String cT = "<concurrency XMI.id='$self.id$'>\n\n" +
    "</concurrency>\n";

    //System.out.println("Traversing concurrency");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseconnection (connection c) {
    String cT = "<connection XMI.id='$self.id$'>\n\n" +
    "$traverse.AssociationEnd$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isNavigable XMI.value='$self.isNavigable$'/>\n" +
    "<isOrdered XMI.value='$self.isOrdered$'/>\n" +
    "<aggregation XMI.value='$self.aggregation$'/>\n" +
    "$traverse.multiplicity$\n" +
    "<changeable XMI.value='$self.changeable$'/>\n" +
    "<targetScope XMI.value='$self.targetScope$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.type$\n" +
    "</connection>\n";

    //System.out.println("Traversing connection");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseconnectionEndRole (connectionEndRole c) {
    String cT = "<connectionEndRole XMI.id='$self.id$'>\n\n" +
    "</connectionEndRole>\n";

    //System.out.println("Traversing connectionEndRole");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseconstrainedElement (constrainedElement c) {
    String cT = "<constrainedElement XMI.id='$self.id$'>\n\n" +
    "</constrainedElement>\n";

    //System.out.println("Traversing constrainedElement");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseconstrainedStereotype (constrainedStereotype c) {
    String cT = "<constrainedStereotype XMI.id='$self.id$'>\n\n" +
    "</constrainedStereotype>\n";

    //System.out.println("Traversing constrainedStereotype");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseconstrainingElement (constrainingElement c) {
    String cT = "<constrainingElement XMI.id='$self.id$'>\n\n" +
    "</constrainingElement>\n";

    //System.out.println("Traversing constrainingElement");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseconstraint (constraint c) {
    String cT = "<constraint XMI.id='$self.id$'>\n\n" +
    "</constraint>\n";

    //System.out.println("Traversing constraint");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversecontents (contents c) {
    String cT = "<contents XMI.id='$self.id$'>\n\n" +
    "</contents>\n";

    //System.out.println("Traversing contents");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversecontext (context c) {
    String cT = "<context XMI.id='$self.id$'>\n\n" +
    "</context>\n";

    //System.out.println("Traversing context");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversedefaultValue (defaultValue c) {
    String cT = "<defaultValue XMI.id='$self.id$'>\n\n" +
    "</defaultValue>\n";

    //System.out.println("Traversing defaultValue");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversedeferredEvent (deferredEvent c) {
    String cT = "<deferredEvent XMI.id='$self.id$'>\n\n" +
    "</deferredEvent>\n";

    //System.out.println("Traversing deferredEvent");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversedeployment (deployment c) {
    String cT = "<deployment XMI.id='$self.id$'>\n\n" +
    "</deployment>\n";

    //System.out.println("Traversing deployment");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversedescription (description c) {
    String cT = "<description XMI.id='$self.id$'>\n\n" +
    "</description>\n";

    //System.out.println("Traversing description");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversediscriminator (discriminator c) {
    String cT = "<discriminator XMI.id='$self.id$'>\n\n" +
    "</discriminator>\n";

    //System.out.println("Traversing discriminator");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseduration (duration c) {
    String cT = "<duration XMI.id='$self.id$'>\n\n" +
    "</duration>\n";

    //System.out.println("Traversing duration");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseeffect (effect c) {
    String cT = "<effect XMI.id='$self.id$'>\n\n" +
    "$traverse.ActionSequence$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.action$\n" +
    "</effect>\n";

    //System.out.println("Traversing effect");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseelement (element c) {
    String cT = "<element XMI.id='$self.id$'>\n\n" +
    "</element>\n";

    //System.out.println("Traversing element");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseentry (entry c) {
    String cT = "<entry XMI.id='$self.id$'>\n\n" +
    "$traverse.ActionSequence$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.action$\n" +
    "</entry>\n";

    //System.out.println("Traversing entry");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseexit (exit c) {
    String cT = "<exit XMI.id='$self.id$'>\n\n" +
    "$traverse.ActionSequence$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.action$\n" +
    "</exit>\n";

    //System.out.println("Traversing exit");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseexpression (expression c) {
    String cT = "<expression XMI.id='$self.id$'>\n\n" +
    "</expression>\n";

    //System.out.println("Traversing expression");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseextendedElement (extendedElement c) {
    String cT = "<extendedElement XMI.id='$self.id$'>\n\n" +
    "</extendedElement>\n";

    //System.out.println("Traversing extendedElement");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseextensionPoint (extensionPoint c) {
    String cT = "<extensionPoint XMI.id='$self.id$'>\n\n" +
    "</extensionPoint>\n";

    //System.out.println("Traversing extensionPoint");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversefeature (feature c) {
    String cT = "<feature XMI.id='$self.id$'>\n\n" +
    "$traverse.Attribute$\n" +
    "$traverse.Operation$\n" +
    "$traverse.Method$\n" +
    "$traverse.Reception$\n" +
    "</feature>\n";

    //System.out.println("Traversing feature");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversegeneralization (generalization c) {
    String cT = "<generalization XMI.id='$self.id$'>\n\n" +
    "</generalization>\n";

    //System.out.println("Traversing generalization");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseguard (guard c) {
    String cT = "<guard XMI.id='$self.id$'>\n\n" +
    "$traverse.Guard$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.expression$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "</guard>\n";

    //System.out.println("Traversing guard");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseicon (icon c) {
    String cT = "<icon XMI.id='$self.id$'>\n\n" +
    "</icon>\n";

    //System.out.println("Traversing icon");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseinState (inState c) {
    String cT = "<inState XMI.id='$self.id$'>\n\n" +
    "</inState>\n";

    //System.out.println("Traversing inState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseincoming (incoming c) {
    String cT = "<incoming XMI.id='$self.id$'>\n\n" +
    "</incoming>\n";

    //System.out.println("Traversing incoming");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseinitialValue (initialValue c) {
    String cT = "<initialValue XMI.id='$self.id$'>\n\n" +
    "</initialValue>\n";

    //System.out.println("Traversing initialValue");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseinstance (instance c) {
    String cT = "<instance XMI.id='$self.id$'>\n\n" +
    "</instance>\n";

    //System.out.println("Traversing instance");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseinstantiation (instantiation c) {
    String cT = "<instantiation XMI.id='$self.id$'>\n\n" +
    "</instantiation>\n";

    //System.out.println("Traversing instantiation");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseinteraction (interaction c) {
    String cT = "<interaction XMI.id='$self.id$'>\n\n" +
    "$traverse.Interaction$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.context$\n" +
    "</interaction>\n";

    //System.out.println("Traversing interaction");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseinternalTransition (internalTransition c) {
    String cT = "<internalTransition XMI.id='$self.id$'>\n\n" +
    "$traverse.Transition$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.sourceState$\n" +
    "</internalTransition>\n";

    //System.out.println("Traversing internalTransition");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisAbstract (isAbstract c) {
    String cT = "<isAbstract XMI.id='$self.id$'>\n\n" +
    "</isAbstract>\n";

    //System.out.println("Traversing isAbstract");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisActive (isActive c) {
    String cT = "<isActive XMI.id='$self.id$'>\n\n" +
    "</isActive>\n";

    //System.out.println("Traversing isActive");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisAsynchronous (isAsynchronous c) {
    String cT = "<isAsynchronous XMI.id='$self.id$'>\n\n" +
    "</isAsynchronous>\n";

    //System.out.println("Traversing isAsynchronous");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisConcurrent (isConcurrent c) {
    String cT = "<isConcurrent XMI.id='$self.id$'>\n\n" +
    "</isConcurrent>\n";

    //System.out.println("Traversing isConcurrent");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisInstantiable (isInstantiable c) {
    String cT = "<isInstantiable XMI.id='$self.id$'>\n\n" +
    "</isInstantiable>\n";

    //System.out.println("Traversing isInstantiable");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisLeaf (isLeaf c) {
    String cT = "<isLeaf XMI.id='$self.id$'>\n\n" +
    "</isLeaf>\n";

    //System.out.println("Traversing isLeaf");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisNavigable (isNavigable c) {
    String cT = "<isNavigable XMI.id='$self.id$'>\n\n" +
    "</isNavigable>\n";

    //System.out.println("Traversing isNavigable");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisOrdered (isOrdered c) {
    String cT = "<isOrdered XMI.id='$self.id$'>\n\n" +
    "</isOrdered>\n";

    //System.out.println("Traversing isOrdered");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisPolymorphic (isPolymorphic c) {
    String cT = "<isPolymorphic XMI.id='$self.id$'>\n\n" +
    "</isPolymorphic>\n";

    //System.out.println("Traversing isPolymorphic");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisQuery (isQuery c) {
    String cT = "<isQuery XMI.id='$self.id$'>\n\n" +
    "</isQuery>\n";

    //System.out.println("Traversing isQuery");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseisRoot (isRoot c) {
    String cT = "<isRoot XMI.id='$self.id$'>\n\n" +
    "</isRoot>\n";

    //System.out.println("Traversing isRoot");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversekind (kind c) {
    String cT = "<kind XMI.id='$self.id$'>\n\n" +
    "</kind>\n";

    //System.out.println("Traversing kind");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverselinkEnd (linkEnd c) {
    String cT = "<linkEnd XMI.id='$self.id$'>\n\n" +
    "</linkEnd>\n";

    //System.out.println("Traversing linkEnd");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverselinkRole (linkRole c) {
    String cT = "<linkRole XMI.id='$self.id$'>\n\n" +
    "$traverse.linkEnd$\n" +
    "</linkRole>\n";

    //System.out.println("Traversing linkRole");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverselinkSource (linkSource c) {
    String cT = "<linkSource XMI.id='$self.id$'>\n\n" +
    "$traverse.linkEnd$\n" +
    "</linkSource>\n";

    //System.out.println("Traversing linkSource");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverselinkTarget (linkTarget c) {
    String cT = "<linkTarget XMI.id='$self.id$'>\n\n" +
    "$traverse.linkEnd$\n" +
    "</linkTarget>\n";

    //System.out.println("Traversing linkTarget");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseliteral (literal c) {
    String cT = "<literal XMI.id='$self.id$'>\n\n" +
    "$traverse.EnumerationLiteral$\n" +
    "<name>$self.name.body$</name>\n" +
    "</literal>\n";

    //System.out.println("Traversing literal");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversemapping (mapping c) {
    String cT = "<mapping XMI.id='$self.id$'>\n\n" +
    "</mapping>\n";

    //System.out.println("Traversing mapping");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversemessage (message c) {
    String cT = "<message XMI.id='$self.id$'>\n\n" +
    "</message>\n";

    //System.out.println("Traversing message");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversemessageAction (messageAction c) {
    String cT = "<messageAction XMI.id='$self.id$'>\n\n" +
    "</messageAction>\n";

    //System.out.println("Traversing messageAction");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversemode (mode c) {
    String cT = "<mode XMI.id='$self.id$'>\n\n" +
    "</mode>\n";

    //System.out.println("Traversing mode");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversemodelElement (modelElement c) {
    String cT = "<modelElement XMI.id='$self.id$'>\n\n" +
    "</modelElement>\n";

    //System.out.println("Traversing modelElement");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversemultiplicity (multiplicity c) {
    String cT = "<multiplicity XMI.id='$self.id$'>\n\n" +
    "</multiplicity>\n";

    //System.out.println("Traversing multiplicity");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversename (name c) {
    String cT = "<name XMI.id='$self.id$'>\n\n" +
    "</name>\n";

    //System.out.println("Traversing name");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversenamespace (namespace c) {
    String cT = "<namespace XMI.id='$self.id$'>\n\n" +
    "</namespace>\n";

    //System.out.println("Traversing namespace");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseoccurrence (occurrence c) {
    String cT = "<occurrence XMI.id='$self.id$'>\n\n" +
    "</occurrence>\n";

    //System.out.println("Traversing occurrence");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseoperation (operation c) {
    String cT = "<operation XMI.id='$self.id$'>\n\n" +
    "</operation>\n";

    //System.out.println("Traversing operation");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseoperationSpecification (operationSpecification c) {
    String cT = "<operationSpecification XMI.id='$self.id$'>\n\n" +
    "</operationSpecification>\n";

    //System.out.println("Traversing operationSpecification");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseoutgoing (outgoing c) {
    String cT = "<outgoing XMI.id='$self.id$'>\n\n" +
    "</outgoing>\n";

    //System.out.println("Traversing outgoing");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseownedElement (ownedElement c) {
    String cT = "<ownedElement XMI.id='$self.id$'>\n\n" +
    "$traverse.Model$\n" +
    "$traverse.Subsystem$\n" +
    "$traverse.Package$\n" +
    "$traverse.Class$\n" +
    "$traverse.Generalization$\n" +
    "$traverse.Association$\n" +
    "$traverse.Interface$\n" +
    "$traverse.ViewElement$\n" +
    "$traverse.UseCase$\n" +
    "$traverse.AssociationClass$\n" +
    "$traverse.Node$\n" +
    "$traverse.Component$\n" +
    "$traverse.Comment$\n" +
    "$traverse.Signal$\n" +
    "$traverse.Exception$\n" +
    "$traverse.Object$\n" +
    "$traverse.Link$\n" +
    "$traverse.LinkObject$\n" +
    "$traverse.UseCaseInstance$\n" +
    "$traverse.ElementReference$\n" +
    "$traverse.Actor$\n" +
    "$traverse.Dependency$\n" +
    "$traverse.Refinement$\n" +
    "$traverse.Usage$\n" +
    "$traverse.Trace$\n" +
    "$traverse.Binding$\n" +
    "$traverse.Constraint$\n" +
    "$traverse.Stereotype$\n" +
    "$traverse.StateMachine$\n" +
    "$traverse.ActivityModel$\n" +
    "$traverse.Collaboration$\n" +
    "$traverse.Request$\n" +
    "$traverse.ClassifierRole$\n" +
    "$traverse.AssociationRole$\n" +
    "</ownedElement>\n";

    //System.out.println("Traversing ownedElement");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseowner (owner c) {
    String cT = "<owner XMI.id='$self.id$'>\n\n" +
    "</owner>\n";

    //System.out.println("Traversing owner");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseownerScope (ownerScope c) {
    String cT = "<ownerScope XMI.id='$self.id$'>\n\n" +
    "</ownerScope>\n";

    //System.out.println("Traversing ownerScope");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseowningDependency (owningDependency c) {
    String cT = "<owningDependency XMI.id='$self.id$'>\n\n" +
    "</owningDependency>\n";

    //System.out.println("Traversing owningDependency");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseparameter (parameter c) {
    String cT = "<parameter XMI.id='$self.id$'>\n\n" +
    "$traverse.Parameter$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "$traverse.defaultValue$\n" +
    "<kind XMI.value='$self.kind$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.type$\n" +
    "</parameter>\n";

    //System.out.println("Traversing parameter");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseparent (parent c) {
    String cT = "<parent XMI.id='$self.id$'>\n\n" +
    "</parent>\n";

    //System.out.println("Traversing parent");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseparticipant (participant c) {
    String cT = "<participant XMI.id='$self.id$'>\n\n" +
    "</participant>\n";

    //System.out.println("Traversing participant");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversepartition (partition c) {
    String cT = "<partition XMI.id='$self.id$'>\n\n" +
    "$traverse.Partition$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.contents$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "</partition>\n";

    //System.out.println("Traversing partition");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversepredecessor (predecessor c) {
    String cT = "<predecessor XMI.id='$self.id$'>\n\n" +
    "</predecessor>\n";

    //System.out.println("Traversing predecessor");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseprovision (provision c) {
    String cT = "<provision XMI.id='$self.id$'>\n\n" +
    "</provision>\n";

    //System.out.println("Traversing provision");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversequalifier (qualifier c) {
    String cT = "<qualifier XMI.id='$self.id$'>\n\n" +
    "$traverse.Attribute$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<ownerScope XMI.value='$self.ownerScope$'/>\n" +
    "<changeable XMI.value='$self.changeable$'/>\n" +
    "$traverse.multiplicity$\n" +
    "$traverse.initialValue$\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$reference.owner$\n" +
    "</qualifier>\n";

    //System.out.println("Traversing qualifier");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseraisedException (raisedException c) {
    String cT = "<raisedException XMI.id='$self.id$'>\n\n" +
    "</raisedException>\n";

    //System.out.println("Traversing raisedException");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverserealization (realization c) {
    String cT = "<realization XMI.id='$self.id$'>\n\n" +
    "</realization>\n";

    //System.out.println("Traversing realization");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversereceiver (receiver c) {
    String cT = "<receiver XMI.id='$self.id$'>\n\n" +
    "</receiver>\n";

    //System.out.println("Traversing receiver");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversereception (reception c) {
    String cT = "<reception XMI.id='$self.id$'>\n\n" +
    "</reception>\n";

    //System.out.println("Traversing reception");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverserecurrence (recurrence c) {
    String cT = "<recurrence XMI.id='$self.id$'>\n\n" +
    "</recurrence>\n";

    //System.out.println("Traversing recurrence");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverserepresentedClassifier (representedClassifier c) {
    String cT = "<representedClassifier XMI.id='$self.id$'>\n\n" +
    "</representedClassifier>\n";

    //System.out.println("Traversing representedClassifier");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverserepresentedOperation (representedOperation c) {
    String cT = "<representedOperation XMI.id='$self.id$'>\n\n" +
    "</representedOperation>\n";

    //System.out.println("Traversing representedOperation");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverserequest (request c) {
    String cT = "<request XMI.id='$self.id$'>\n\n" +
    "</request>\n";

    //System.out.println("Traversing request");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverserequiredTag (requiredTag c) {
    String cT = "<requiredTag XMI.id='$self.id$'>\n\n" +
    "$traverse.TaggedValue$\n" +
    "$traverse.tag$\n" +
    "$traverse.value$\n" +
    "$traverse.constrainedStereotype$\n" +
    "</requiredTag>\n";

    //System.out.println("Traversing requiredTag");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverserequirement (requirement c) {
    String cT = "<requirement XMI.id='$self.id$'>\n\n" +
    "</requirement>\n";

    //System.out.println("Traversing requirement");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversescript (script c) {
    String cT = "<script XMI.id='$self.id$'>\n\n" +
    "</script>\n";

    //System.out.println("Traversing script");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesender (sender c) {
    String cT = "<sender XMI.id='$self.id$'>\n\n" +
    "</sender>\n";

    //System.out.println("Traversing sender");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesignal (signal c) {
    String cT = "<signal XMI.id='$self.id$'>\n\n" +
    "</signal>\n";

    //System.out.println("Traversing signal");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseslot (slot c) {
    String cT = "<slot XMI.id='$self.id$'>\n\n" +
    "$traverse.AttributeLink$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.attribute$\n" +
    "</slot>\n";

    //System.out.println("Traversing slot");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesource (source c) {
    String cT = "<source XMI.id='$self.id$'>\n\n" +
    "$traverse.AssociationEnd$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isNavigable XMI.value='$self.isNavigable$'/>\n" +
    "<isOrdered XMI.value='$self.isOrdered$'/>\n" +
    "<aggregation XMI.value='$self.aggregation$'/>\n" +
    "$traverse.multiplicity$\n" +
    "<changeable XMI.value='$self.changeable$'/>\n" +
    "<targetScope XMI.value='$self.targetScope$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.type$\n" +
    "</source>\n";

    //System.out.println("Traversing source");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesourceEndRole (sourceEndRole c) {
    String cT = "<sourceEndRole XMI.id='$self.id$'>\n\n" +
    "</sourceEndRole>\n";

    //System.out.println("Traversing sourceEndRole");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesourceState (sourceState c) {
    String cT = "<sourceState XMI.id='$self.id$'>\n\n" +
    "</sourceState>\n";

    //System.out.println("Traversing sourceState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversespecialization (specialization c) {
    String cT = "<specialization XMI.id='$self.id$'>\n\n" +
    "</specialization>\n";

    //System.out.println("Traversing specialization");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversespecification (specification c) {
    String cT = "<specification XMI.id='$self.id$'>\n\n" +
    "</specification>\n";

    //System.out.println("Traversing specification");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversestateKind (stateKind c) {
    String cT = "<stateKind XMI.id='$self.id$'>\n\n" +
    "</stateKind>\n";

    //System.out.println("Traversing stateKind");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversestereotype (stereotype c) {
    String cT = "<stereotype XMI.id='$self.id$'>\n\n" +
    "</stereotype>\n";

    //System.out.println("Traversing stereotype");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversestereotypeConstraint (stereotypeConstraint c) {
    String cT = "<stereotypeConstraint XMI.id='$self.id$'>\n\n" +
    "</stereotypeConstraint>\n";

    //System.out.println("Traversing stereotypeConstraint");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesubDependencies (subDependencies c) {
    String cT = "<subDependencies XMI.id='$self.id$'>\n\n" +
    "$traverse.Dependency$\n" +
    "$traverse.Refinement$\n" +
    "$traverse.Usage$\n" +
    "$traverse.Trace$\n" +
    "$traverse.Binding$\n" +
    "</subDependencies>\n";

    //System.out.println("Traversing subDependencies");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesubmachine (submachine c) {
    String cT = "<submachine XMI.id='$self.id$'>\n\n" +
    "</submachine>\n";

    //System.out.println("Traversing submachine");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesubstate (substate c) {
    String cT = "<substate XMI.id='$self.id$'>\n\n" +
    "$traverse.Pseudostate$\n" +
    "$traverse.SimpleState$\n" +
    "$traverse.SubmachineState$\n" +
    "$traverse.ActivityState$\n" +
    "$traverse.ActionState$\n" +
    "$traverse.ObjectFlowState$\n" +
    "</substate>\n";

    //System.out.println("Traversing substate");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesubtype (subtype c) {
    String cT = "<subtype XMI.id='$self.id$'>\n\n" +
    "</subtype>\n";

    //System.out.println("Traversing subtype");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesupertype (supertype c) {
    String cT = "<supertype XMI.id='$self.id$'>\n\n" +
    "</supertype>\n";

    //System.out.println("Traversing supertype");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversesupplier (supplier c) {
    String cT = "<supplier XMI.id='$self.id$'>\n\n" +
    "</supplier>\n";

    //System.out.println("Traversing supplier");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetag (tag c) {
    String cT = "<tag XMI.id='$self.id$'>\n\n" +
    "</tag>\n";

    //System.out.println("Traversing tag");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetaggedValue (taggedValue c) {
    String cT = "<taggedValue XMI.id='$self.id$'>\n\n" +
    "$traverse.TaggedValue$\n" +
    "$traverse.tag$\n" +
    "$traverse.value$\n" +
    "$traverse.constrainedStereotype$\n" +
    "</taggedValue>\n";

    //System.out.println("Traversing taggedValue");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetarget (target c) {
    String cT = "<target XMI.id='$self.id$'>\n\n" +
    "$traverse.AssociationEnd$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isNavigable XMI.value='$self.isNavigable$'/>\n" +
    "<isOrdered XMI.value='$self.isOrdered$'/>\n" +
    "<aggregation XMI.value='$self.aggregation$'/>\n" +
    "$traverse.multiplicity$\n" +
    "<changeable XMI.value='$self.changeable$'/>\n" +
    "<targetScope XMI.value='$self.targetScope$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.type$\n" +
    "</target>\n";

    //System.out.println("Traversing target");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetargetEndRole (targetEndRole c) {
    String cT = "<targetEndRole XMI.id='$self.id$'>\n\n" +
    "</targetEndRole>\n";

    //System.out.println("Traversing targetEndRole");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetargetScope (targetScope c) {
    String cT = "<targetScope XMI.id='$self.id$'>\n\n" +
    "</targetScope>\n";

    //System.out.println("Traversing targetScope");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetargetState (targetState c) {
    String cT = "<targetState XMI.id='$self.id$'>\n\n" +
    "</targetState>\n";

    //System.out.println("Traversing targetState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetemplate (template c) {
    String cT = "<template XMI.id='$self.id$'>\n\n" +
    "</template>\n";

    //System.out.println("Traversing template");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetemplateParameter (templateParameter c) {
    String cT = "<templateParameter XMI.id='$self.id$'>\n\n" +
    "</templateParameter>\n";

    //System.out.println("Traversing templateParameter");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetext (text c) {
    String cT = "<text XMI.id='$self.id$'>\n\n" +
    "</text>\n";

    //System.out.println("Traversing text");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetop (top c) {
    String cT = "<top XMI.id='$self.id$'>\n\n" +
    "$traverse.CompositeState$\n" +
    "<name>$self.name.body$</name>\n" +
    //"<visibility XMI.value='$self.visibility$'/>\n" +
    "<isConcurrent XMI.value='$self.isConcurrent$'/>\n" +
    //"$traverse.namespace$\n" +
    "$traverse.template$\n" +
    "$traverse.stereotype$\n" +
    "$traverse.constraint$\n" +
    //"$traverse.view$\n" +
    "$traverse.provision$\n" +
    "$traverse.requirement$\n" +
    "$traverse.templateParameter$\n" +
    "$traverse.parent$\n" +
    "$traverse.outgoing$\n" +
    "$traverse.incoming$\n" +
    "$traverse.deferredEvent$\n" +
    "$traverse.taggedValue$\n" +
    "$traverse.behavior$\n" +
    "$traverse.entry$\n" +
    "$traverse.exit$\n" +
    "$traverse.internalTransition$\n" +
    "$traverse.substate$\n" +
    "</top>\n";

    //System.out.println("Traversing top");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetransitions (transitions c) {
    String cT = "<transitions XMI.id='$self.id$'>\n\n" +
    "$traverse.Transition$\n" +
    "</transitions>\n";

    //System.out.println("Traversing transitions");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetrigger (trigger c) {
    String cT = "<trigger XMI.id='$self.id$'>\n\n" +
    "$traverse.SignalEvent$\n" +
    "$traverse.CallEvent$\n" +
    "$traverse.TimeEvent$\n" +
    "$traverse.ChangeEvent$\n" +
    "</trigger>\n";

    //System.out.println("Traversing trigger");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetype (type c) {
    String cT = "<type XMI.id='$self.id$'>\n\n" +
    "</type>\n";

    //System.out.println("Traversing type");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversetypeState (typeState c) {
    String cT = "<typeState XMI.id='$self.id$'>\n\n" +
    "</typeState>\n";

    //System.out.println("Traversing typeState");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversevalue (value c) {
    String cT = "<value XMI.id='$self.id$'>\n\n" +
    "</value>\n";

    //System.out.println("Traversing value");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseview (view c) {
    String cT = "<view XMI.id='$self.id$'>\n\n" +
    "</view>\n";

    //System.out.println("Traversing view");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traverseviewElement (viewElement c) {
    String cT = "<viewElement XMI.id='$self.id$'>\n\n" +
    "</viewElement>\n";

    //System.out.println("Traversing viewElement");
    simpleOCLRun(c, new StringReader(cT));
  }
  public static void traversevisibility (visibility c) {
    String cT = //"<visibility XMI.id='$self.id$'>\n\n" +
    "</visibility>\n";

    //System.out.println("Traversing visibility");
    simpleOCLRun(c, new StringReader(cT));
  } */
}
