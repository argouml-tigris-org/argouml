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

package uci.uml.generate;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Collaborations.*;
import uci.uml.Model_Management.*;
import uci.uml.ui.ProjectBrowser;
import uci.uml.ui.Project;

public class ParserDisplay extends Parser {

  public static ParserDisplay SINGLETON = new ParserDisplay();

  ////////////////////////////////////////////////////////////////
  // parsing methods

  public void parseOperationCompartment(Classifier cls, String s) {
    StringTokenizer st = new StringTokenizer(s, "\n\r");
    Vector ops = new Vector();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      Operation op = parseOperation(token);
//       if (op != null)
// 	System.out.println("just parsed " + GeneratorDisplay.Generate(op));
      ops.addElement(op);
    }
    System.out.println("parsed " + ops.size() + " operations");
    Vector curOps = cls.getBehavioralFeature();
    if (curOps == null) {
      try { cls.setBehavioralFeature(ops); }
      catch (PropertyVetoException pve) { }
      //System.out.println("set all Behavioral Features");
      return;
    }
    int curOpsSize = curOps.size();
    Hashtable touched = new Hashtable();
    int newOpsSize = ops.size();
    int i;
    for (i = 0; i < newOpsSize; i++) {
      Operation op = (Operation) ops.elementAt(i);
      touched.put(op, op);
      Operation curOp = (Operation) cls.findBehavioralFeature(op.getName());
      if (curOp == null || touched.contains(curOp)) {
	curOps.insertElementAt(op, i);
	try {
	  op.setOwner(cls);
	  //op.setNamespace(cls);
	}
	catch (PropertyVetoException pve) { }
      }
      else {
	try {
	  curOp.setVisibility(op.getVisibility());
	  curOp.setOwnerScope(op.getOwnerScope());
	  curOp.setConcurrency(op.getConcurrency());
	  curOp.setParameter(op.getParameter());
	  touched.put(curOp, curOp);
	}
	catch (PropertyVetoException pve) { }
      }
    }
    Vector removes = new Vector();
    int totalOpSize = curOps.size();
    for (i = 0; i < totalOpSize; i++) {
      Operation op = (Operation) curOps.elementAt(i);
      if (!touched.containsKey(op)) removes.addElement(op);
    }
    int nRemove = removes.size();
    for (i = 0; i < nRemove; i++) {
      Operation oldOper = (Operation) removes.elementAt(i);
      curOps.removeElement(oldOper);
      try { oldOper.setOwner(null); }
      catch (PropertyVetoException pve) { }
    }
  }

  public void parseAttributeCompartment(Classifier cls, String s) {
    StringTokenizer st = new StringTokenizer(s, "\n\r");
    Vector attrs = new Vector();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      Attribute attr = parseAttribute(token);
//       if (attr != null)
// 	System.out.println("just parsed " + GeneratorDisplay.Generate(attr));
      attrs.addElement(attr);
    }
    System.out.println("parsed " + attrs.size() + " attributes");
    Vector curAttrs = cls.getStructuralFeature();
    if (curAttrs == null) {
      try { cls.setStructuralFeature(attrs); }
      catch (PropertyVetoException pve) { }
      //System.out.println("set all Structural Features");
      return;
    }
    int curAttrsSize = curAttrs.size();
    Hashtable touched = new Hashtable();
    int newAttrsSize = attrs.size();
    int i;
    for (i = 0; i < newAttrsSize; i++) {
      Attribute attr = (Attribute) attrs.elementAt(i);
      touched.put(attr, attr);
      Attribute curAttr = (Attribute)
	cls.findStructuralFeature(attr.getName());
      if (curAttr == null || touched.contains(curAttr)) {
	curAttrs.insertElementAt(attr, i);
	try {
	  attr.setOwner(cls);
	  //attr.setNamespace(cls);
	}
	catch (PropertyVetoException pve) { }
      }
      else {
	try {
	  curAttr.setVisibility(attr.getVisibility());
	  curAttr.setOwnerScope(attr.getOwnerScope());
	  curAttr.setType(attr.getType());
	  curAttr.setInitialValue(attr.getInitialValue());
	  touched.put(curAttr, curAttr);
	}
	catch (PropertyVetoException pve) { }
      }
    }
    Vector removes = new Vector();
    int totalAttrSize = curAttrs.size();
    for (i = 0; i < totalAttrSize; i++) {
      Attribute attr = (Attribute) curAttrs.elementAt(i);
      if (!touched.containsKey(attr)) removes.addElement(attr);
    }
    int nRemove = removes.size();
    for (i = 0; i < nRemove; i++) {
      Attribute oldAttr = (Attribute) removes.elementAt(i);
      curAttrs.removeElement(oldAttr);
      try { oldAttr.setOwner(null); }
      catch (PropertyVetoException pve) { }
    }
  }

  /** Parse a line of the form:
   *  [visibility] [keywords] returntype name(params)[;] */
  public Operation parseOperation(String s) {
    s = s.trim();
    if (s.endsWith(";")) s = s.substring(0, s.length()-1);
    Operation res = new Operation();
    s = parseOutVisibility(res, s);
    s = parseOutKeywords(res, s);
    s = parseOutReturnType(res, s);
    s = parseOutName(res, s);
    s = parseOutParams(res, s);
    s = s.trim();
    if (s.length() > 2)
      System.out.println("leftover in parseOperation=|" + s + "|");
    return res;
  }


  /** Parse a line of the form:
   *  [visibility] [keywords] type name [= init] [;] */
  public Attribute parseAttribute(String s) {
    s = s.trim();
    if (s.endsWith(";")) s = s.substring(0, s.length()-1);
    Attribute res = new uci.uml.Foundation.Core.Attribute();
    s = parseOutVisibility(res, s);
    s = parseOutKeywords(res, s);
    s = parseOutType(res, s);
    s = parseOutName(res, s);
    if (res.getName() == Name.UNSPEC && res.getType() != null) {
      try {
	res.setName(new Name(res.getType().getName().getBody()));
	Project p = ProjectBrowser.TheInstance.getProject();
	res.setType(p.findType("int"));
      }
      catch (PropertyVetoException pve) { }
    }
    s = parseOutInitValue(res, s);
    if (s.length() > 2)
      System.out.println("leftover in parseAttribute=|" + s + "|");
    return res;
  }


  public String parseOutVisibility(Feature f, String s) {
    s = s.trim();
    int firstSpace = s.indexOf(" ");
    if (firstSpace == -1) firstSpace = s.length();
    String visStr = s.substring(0, firstSpace);
    VisibilityKind vk = VisibilityKind.PUBLIC;
    if (visStr.equals("public") || s.startsWith("+"))
      vk = VisibilityKind.PUBLIC;
    else if (visStr.equals("private") || s.startsWith("-"))
      vk = VisibilityKind.PRIVATE;
    else if (visStr.equals("protected") || s.startsWith("#"))
      vk = VisibilityKind.PROTECTED;
    else if (visStr.equals("package") || s.startsWith("~"))
      vk = VisibilityKind.PACKAGE;
    else {
      System.out.println("unknown visibility \"" + visStr +
			 "\", using default");
      return s;
    }
    try { f.setVisibility(vk); }
    catch (PropertyVetoException pve) { }

    if (s.startsWith("+") || s.startsWith("-") ||
	s.startsWith("#") || s.startsWith("~"))
      s = s.substring(1);
    else
      s = s.substring(firstSpace+1);
    return s;
  }

  public String parseOutKeywords(Feature f, String s) {
    s = s.trim();
    int firstSpace = s.indexOf(" ");
    if (firstSpace == -1) return s;
    String visStr = s.substring(0, firstSpace);
    try {
      if (visStr.equals("static"))
	f.setOwnerScope(ScopeKind.CLASSIFIER);
      else if (visStr.equals("synchronized") && (f instanceof Operation))
	((Operation)f).setConcurrency(CallConcurrencyKind.GUARDED);
      else if (visStr.equals("transient"))
	System.out.println("'transient' keyword is currently ignored");
      else if (visStr.equals("final"))
	System.out.println("'final' keyword is currently ignored");
      else if (visStr.equals("abstract"))
	System.out.println("'abstract' keyword is currently ignored");
      else {
	return s;
      }
    }
    catch (PropertyVetoException pve) { }
    return parseOutKeywords(f, s.substring(firstSpace+1));
  }

  public String parseOutReturnType(Operation op, String s) {
    s = s.trim();
    int firstSpace = s.indexOf(" ");
    if (firstSpace == -1) return s;
    String rtStr = s.substring(0, firstSpace);
    if (rtStr.indexOf("(") > 0) {
      try { op.addStereotype(Stereotype.CONSTRUCTOR); }
      catch (PropertyVetoException pve) { }
      return s;
    }
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    Classifier rt = p.findType(rtStr);
    try {
      //System.out.println("setting return type: " + rtStr);
      op.setReturnType(rt);
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in parseOutReturnType");
    }
    if (op.getReturnType() != rt) System.out.println("rt not set");
    return s.substring(firstSpace+1);
  }

  public String parseOutParams(Operation op, String s) {
    s = s.trim();
    String leftOver = s;
    StringTokenizer st = new StringTokenizer(s, "(),");
    Vector params = new Vector();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      Parameter p = parseParameter(token);
      if (p != null) params.addElement(p);
      if (!st.hasMoreTokens())
	leftOver = s.substring(s.indexOf(token) + token.length());
    }
    //System.out.println("parsed " + params.size() + " params");
    //System.out.println("leftOver = '" + leftOver  + "'");

    Classifier rt = op.getReturnType(); // must set again because we overwrite
    try {
      op.setParameter(params);
      op.setReturnType(rt);
    }
    catch (PropertyVetoException pve) { }
    return leftOver;
  }

  public String parseOutName(ModelElement me, String s) {
    s = s.trim();
    if (s.equals("") || s.charAt(0) == '=') return s;
    StringTokenizer st = new StringTokenizer(s, " \t()[]=;");
    if (!st.hasMoreTokens()) {
      System.out.println("name not parsed");
      return s;
    }
    String nameStr = st.nextToken();

    // needs-more-work: wasteful
    try { me.setName(new Name(nameStr)); }
    catch (PropertyVetoException pve) { }

    int namePos = s.indexOf(nameStr);
    return s.substring(namePos + nameStr.length());
  }

  public String parseOutType(Attribute attr, String s) {
    s = s.trim();
    int firstSpace = s.indexOf(" ");

    int firstEq = s.indexOf("=");
    if (firstEq != -1 && firstEq < firstSpace) firstSpace = firstEq;

    Project p = ProjectBrowser.TheInstance.getProject();
    Classifier type = p.findType("int");

    if (firstSpace != -1) {
      String typeStr = s.substring(0, firstSpace);
      type = p.findType(typeStr);
    }
    try {
      //System.out.println("setting return type: " + rtStr);
      attr.setType(type);
    }
    catch (PropertyVetoException pve) { }
    return s.substring(firstSpace+1);
  }

  public String parseOutInitValue(Attribute attr, String s) {
    s = s.trim();
    int equalsIndex = s.indexOf("=");
    if (equalsIndex != 0) return s;
    String initStr = s.substring(1).trim(); //move past "="
    if (initStr.length() == 0) return "";
    Expression initExpr = new Expression(initStr);
    try {
      //System.out.println("setting return type: " + rtStr);
      attr.setInitialValue(initExpr);
    }
    catch (PropertyVetoException pve) { }
    return "";
  }

  public Parameter parseParameter(String s) {
    StringTokenizer st = new StringTokenizer(s, " \t");
    String typeStr = "int", paramNameStr = "parameterName?";
    if (st.hasMoreTokens()) typeStr = st.nextToken();
    if (st.hasMoreTokens()) paramNameStr = st.nextToken();
    Project p = ProjectBrowser.TheInstance.getProject();
    Classifier cls = p.findType(typeStr);
    return new Parameter(cls, ParameterDirectionKind.IN, paramNameStr);
  }


  //   public abstract Package parsePackage(String s);
//   public abstract Classifier parseClassifier(String s);

  public Stereotype parseStereotype(String s) {
    return null;
  }

  public TaggedValue parseTaggedValue(String s) {
    return null;
  }

//   public abstract IAssociation parseAssociation(String s);
//   public abstract AssociationEnd parseAssociationEnd(String s);

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


  public State parseState(String s) {
    return null;
  }

  public void parseStateBody(State st, String s) {
    Vector trans = new Vector();
    StringTokenizer lines = new StringTokenizer(s, "\n\r");
    while (lines.hasMoreTokens()) {
      String line = lines.nextToken().trim();
      if (line.startsWith("entry")) parseStateEntyAction(st, line);
      else if (line.startsWith("exit")) parseStateExitAction(st, line);
      else {
	Transition t = parseTransition(line);
	if (t == null) continue;
	//System.out.println("just parsed:" + GeneratorDisplay.Generate(t));
	trans.addElement(t);
      }
    }
    try { st.setInternalTransition(trans); }
    catch (PropertyVetoException pve) { }
  }

  public void parseStateEntyAction(State st, String s) {
    if (s.startsWith("entry") && s.indexOf("/") > -1)
      s = s.substring(s.indexOf("/")+1).trim();
    try { st.setEntry(parseActions(s)); }
    catch (PropertyVetoException pve) { }
  }

  public void parseStateExitAction(State st, String s) {
    if (s.startsWith("exit") && s.indexOf("/") > -1)
      s = s.substring(s.indexOf("/")+1).trim();
    try { st.setExit(parseActions(s)); }
    catch (PropertyVetoException pve) { }
  }

  /** Parse a line of the form: "name: trigger [guard] / actions" */
  public Transition parseTransition(String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return null;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String trigger = "";
    String guard = "";
    String actions = "";
    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      s = s.substring(s.indexOf(":") + 1).trim();
    }

    if (s.indexOf("[", 0) > -1 && s.indexOf("]", 0) > -1) {
      guard = s.substring(s.indexOf("[", 0)+1, s.indexOf("]")).trim();
      s = s.substring(0, s.indexOf("[")) + s.substring(s.indexOf("]")+1);
      s = s.trim();
    }

    if (s.indexOf("/", 0) > -1) {
      actions = s.substring(s.indexOf("/")+1).trim();
      s = s.substring(0, s.indexOf("/")).trim();
    }

    trigger = s;

//     System.out.println("name=|" + name +"|");
//     System.out.println("trigger=|" + trigger +"|");
//     System.out.println("guard=|" + guard +"|");
//     System.out.println("actions=|" + actions +"|");

    Transition t = new Transition(parseName(name));
    try {
      t.setTrigger(parseEvent(trigger));
      t.setGuard(parseGuard(guard));
      t.setEffect(parseActions(actions));
    }
    catch (PropertyVetoException pve) { }

    return t;
  }

  /** Parse a line of the form: "name: base" */
  public void parseClassifierRole(ClassifierRole cls, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String base = "";
    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      base = s.substring(s.indexOf(":") + 1).trim();
    }
    else {
      name = s;
    }
    try { cls.setBaseString(base); }
    catch (PropertyVetoException pve) { }
    //Project p = ProjectBrowser.TheInstance.getProject();
    //Classifier bs = p.findType(base);

    try { cls.setName(new Name(name)); }
    catch (PropertyVetoException pve) { }

  }

  /** Parse a line of the form: "name: action" */
  public void parseMessage(Message mes, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String action = "";
    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      //System.out.println("set message name to: '" + name + "'");
      action = s.substring(s.indexOf(":") + 1).trim();
    }
    else action = s;

    try {
     UninterpretedAction ua = (UninterpretedAction) mes.getAction();
     ua.setBody(action);
    }
    catch (PropertyVetoException pve) { }

    try {
      Name nm = new Name(name);
      mes.setName(nm);
    }
    catch (PropertyVetoException pve) { }
  }

  public MMAction parseAction(String s) {
    return null;
  }

  public ActionSequence parseActions(String s) {
    return new ActionSequence(new Name(s));
  }

  public Guard parseGuard(String s) {
    return new Guard(s);
  }

  public Event parseEvent(String s) {
    return new uci.uml.Behavioral_Elements.State_Machines.SignalEvent(s);
  }


} /* end class ParserDisplay */
