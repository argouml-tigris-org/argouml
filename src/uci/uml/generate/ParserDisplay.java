// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.

package uci.uml.generate;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Model_Management.*;

public class ParserDisplay extends Parser {

  public static ParserDisplay SINGLETON = new ParserDisplay();

  ////////////////////////////////////////////////////////////////
  // parsing methods

  /** Parse a line of the form:
   *  [visibility] [keywords] returntype name(params)[;] */
  public Operation parseOperation(String s) {
    s = s.trim();
    if (s.endsWith(";")) s = s.substring(0, s.length()-2);
    Operation res = new Operation();
    s = parseOutVisibility(res, s);
    s = parseOutKeywords(res, s);
    s = parseOutReturnType(res, s);
    s = parseOutName(res, s);
    s = parseOutParams(res, s);
    return res;
  }

  public Attribute parseAttribute(String s) {
    return null;
  }


  public String parseOutVisibility(Feature f, String s) {
    s = s.trim();
    int firstSpace = s.indexOf(" ");
    if (firstSpace == -1) return s;
    String visStr = s.substring(0, firstSpace-1);
    if (visStr.equals("public") || visStr.equals("+"))
      f.setVisibility(VisibilityKind.PUBLIC);
    else if (visStr.equals("private") || visStr.equals("-"))
      f.setVisibility(VisibilityKind.PRIVATE);
    else if (visStr.equals("protected") || visStr.equals("#"))
      f.setVisibility(VisibilityKind.PROTECTED);
    else if (visStr.equals("package") || visStr.equals("~"))
      f.setVisibility(VisibilityKind.UNSPEC);
    else {
      System.out.println("unknown visibility, using default");
      return s;
    }
    return s.substring(firstSpace+1);
  }

  public String parseOutKeywords(Feature s, String s) {
    s = s.trim();
    int firstSpace = s.indexOf(" ");
    if (firstSpace == -1) return s;
    String visStr = s.substring(0, firstSpace-1);
    if (visStr.equals("static"))
      f.setOwnerScope(ScopeKind.CLASSIFIER);
    else if (visStr.equals("transient"))
      System.out.println("'transient' keyword is currently ignored");
    else if (visStr.equals("final"))
      System.out.println("'final' keyword is currently ignored");
    else if (visStr.equals("abstract"))
      System.out.println("'abstract' keyword is currently ignored");
    else {
      return s;
    }
    return parseOutKeywords(op, s.substring(firstSpace+1));
  }

  public String parseOutReturnType(Operation op, String s) {
    return s;
  }

  public String parseOutParams(Operation op, String s) {
    return s;
  }

  public Parameter parseParameter(String s) {
    return null;
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

    System.out.println("name=|" + name +"|");
    System.out.println("trigger=|" + trigger +"|");
    System.out.println("guard=|" + guard +"|");
    System.out.println("actions=|" + actions +"|");

    Transition t = new Transition(parseName(name));
    try {
      t.setTrigger(parseEvent(trigger));
      t.setGuard(parseGuard(guard));
      t.setEffect(parseActions(actions));
    }
    catch (PropertyVetoException pve) { }

    return t;
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
    return new uci.uml.Behavioral_Elements.State_Machines.Event(s);
  }


} /* end class ParserDisplay */
