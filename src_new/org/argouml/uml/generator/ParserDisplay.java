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

package org.argouml.uml.generator;

import java.beans.*;
import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.graph.*;

import org.argouml.kernel.Project;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.MMUtil;
import org.argouml.uml.diagram.static_structure.*;
import org.argouml.uml.diagram.deployment.*;
import org.argouml.application.api.*;

public class ParserDisplay extends Parser {

  public static ParserDisplay SINGLETON = new ParserDisplay();

  ////////////////////////////////////////////////////////////////
  // parsing methods

  public void parseOperationCompartment(MClassifier cls, String s) {
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, "\n\r");
    Vector newOps = new Vector();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      MOperation op = parseOperation(token);
      newOps.add(op);
    }
    // System.out.println("parsed " + newOps.size() + " operations");
	Vector features = new Vector(cls.getFeatures());
	Vector oldOps = new Vector(MMUtil.SINGLETON.getOperations(cls));
	features.removeAll(oldOps);

	// don't forget to remove old Operations!
	for (int i = 0; i < oldOps.size(); i++)
		cls.removeFeature((MOperation)oldOps.elementAt(i));

	// now re-set the attributes
	cls.setFeatures(features);

	//features.addAll(newOps);
	//add features with add-Operation, so a role-added-event is generated
	for (int i=0; i<newOps.size(); i++){
	    MOperation oper=(MOperation)newOps.elementAt(i);
	    cls.addFeature(oper);
	}
  }

  public void parseOperationFig(MClassifier cls, MOperation op, String s) {
	if (cls == null || op == null)
	  return;
	int i = cls.getFeatures().indexOf(op);
	if (s != null && s.length() > 0) {
	  MOperation newOp = parseOperation(s);
	  if (newOp != null) {
		newOp.setAbstract(op.isAbstract());
		newOp.setOwnerScope(op.getOwnerScope());
		if (i != -1) {
		  cls.removeFeature(i);
		  cls.addFeature(i,newOp);
	    } else {
		  cls.addFeature(newOp);
	    }
	  }
    } else {
	  cls.removeFeature(i);
	}
  }

  public void parseAttributeCompartment(MClassifier cls, String s) {
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, "\n\r");
    Vector newAttrs = new Vector();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      MAttribute attr = parseAttribute(token);
      newAttrs.add(attr);
    }
    // System.out.println("parsed " + newAttrs.size() + " attributes");
	Vector features = new Vector(cls.getFeatures());
	Vector oldAttrs = new Vector(MMUtil.SINGLETON.getAttributes(cls));
	features.removeAll(oldAttrs);

	// don't forget to remove old Attrbutes!
        oldAttrs.clear();

	// now re-set the operations
	cls.setFeatures(features);

	//features.addAll(newAttrs);
	//add features with add-Operation, so a role-added-event is generated
	for (int i=0; i<newAttrs.size(); i++){
	    MAttribute attr=(MAttribute)newAttrs.elementAt(i);
	    cls.addFeature(attr);
	}

  }

  public void parseAttributeFig(MClassifier cls, MAttribute at, String s) {
	if (cls == null || at == null)
	  return;
	int i = cls.getFeatures().indexOf(at);
	if (s != null && s.length() > 0) {
            MAttribute newAt = parseAttribute(s);
            if (newAt != null) {
		newAt.setOwnerScope(at.getOwnerScope());
		if (i != -1) {
                    cls.removeFeature(i);
                    cls.addFeature(i,newAt);
                } 
            else {
		  cls.addFeature(newAt);
            }
            }
        }   
        else {
            cls.removeFeature(i);
	}
  }

  /** Parse a line of the form:
   *  [visibility] [keywords] name(params) ": " returntype[;] */
   /* (formerly: [visibility] [keywords] returntype name(params)[;] ) */
  public MOperation parseOperation(String s) {
    s = s.trim();
    if (s.endsWith(";")) s = s.substring(0, s.length()-1);
    MOperation res = new MOperationImpl();
    s = parseOutVisibility(res, s);
    s = parseOutKeywords(res, s);
    s = parseOutName(res, s);
    s = parseOutParams(res, s);
    s = parseOutColon(s);
    s = parseOutReturnType(res, s);
    s = s.trim();
    if (s.length() > 2)
      System.out.println("leftover in parseOperation=|" + s + "|");
    return res;
  }


   /** Parse a line of the form:
    *  [visibility] [keywords] name ": " type [= init] [;] */
   /* (formerly: [visibility] [keywords] type name [= init] [;] ) */
  public MAttribute parseAttribute(String s) {
      s = s.trim();

      if (s.endsWith(";")) {
          s = s.substring(0, s.length()-1);
      }
      MAttribute newAttribute = new MAttributeImpl();
      s = parseOutVisibility(newAttribute, s);
      s = parseOutKeywords(newAttribute, s);
      s = parseOutName(newAttribute, s);
      s = parseOutColon(s);
      s = parseOutType(newAttribute, s);
      s = parseOutInitValue(newAttribute, s);
      if (s.length() > 2){
          System.out.println("Value left over in parseAttribute=|" + s + "|");
      }
      return newAttribute;
  }


  public String parseOutVisibility(MFeature f, String s) {
    s = s.trim();
    int firstSpace = s.indexOf(" ");
    if (firstSpace == -1) firstSpace = s.length();
    String visStr = s.substring(0, firstSpace);
    MVisibilityKind vk = MVisibilityKind.PUBLIC;
    if (visStr.equals("public") || s.startsWith("+"))
      vk = MVisibilityKind.PUBLIC;
    else if (visStr.equals("private") || s.startsWith("-"))
      vk = MVisibilityKind.PRIVATE;
    else if (visStr.equals("protected") || s.startsWith("#"))
      vk = MVisibilityKind.PROTECTED;
	//    else if (visStr.equals("package") || s.startsWith("~"))
	// vk = MVisibilityKind.PACKAGE;
    else {
      System.out.println("unknown visibility \"" + visStr +
			 "\", using default");
      return s;
    }
     f.setVisibility(vk);

    if (s.startsWith("+") || s.startsWith("-") ||
	s.startsWith("#") || s.startsWith("~"))
      s = s.substring(1);
    else
      s = s.substring(firstSpace+1);
    return s;
  }

  public String parseOutKeywords(MFeature f, String s) {
    s = s.trim();
    int firstSpace = s.indexOf(" ");
    if (firstSpace == -1) return s;
    String visStr = s.substring(0, firstSpace);

      if (visStr.equals("static"))
	f.setOwnerScope(MScopeKind.CLASSIFIER);
      else if (visStr.equals("synchronized") && (f instanceof MOperation))
	((MOperation)f).setConcurrency(MCallConcurrencyKind.GUARDED);
      else if (visStr.equals("transient"))
	System.out.println("'transient' keyword is currently ignored");
      else if (visStr.equals("final"))
	System.out.println("'final' keyword is currently ignored");
      else if (visStr.equals("abstract"))
	System.out.println("'abstract' keyword is currently ignored");
      else {
	return s;
      }

    return parseOutKeywords(f, s.substring(firstSpace+1));
  }

  public String parseOutReturnType(MOperation op, String s) {
    s = s.trim();
    //int firstSpace = s.indexOf(" ");
    //if (firstSpace == -1) return s;
    //String rtStr = s.substring(0, firstSpace);
    String rtStr = (s.length()>0) ? s : "void";
    //if (rtStr.indexOf("(") > 0) {
	//	// must be CONSTRUCTOR, must be included in nsuml later on!
    //   op.setStereotype(new MStereotypeImpl());
    //  return s;
    //}
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    MClassifier rt = p.findType(rtStr);

    //System.out.println("setting return type: " + rtStr +" "+rt);
    MParameter param = new MParameterImpl();
    param.setType(rt);
    MMUtil.SINGLETON.setReturnParameter(op,param);
    //return s.substring(firstSpace+1);
    return "";
  }

	public String parseOutParams(MOperation op, String s) {
		s = s.trim();
		String leftOver = s;
		int end = s.lastIndexOf(")");
		if (end != -1) {
			java.util.StringTokenizer st = new java.util.StringTokenizer(s.substring(1,end), ",");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				MParameter p = parseParameter(token);
				if (p != null) op.addParameter(p);
			}
			leftOver = s.substring(end+1);
		}
		return leftOver;
	}

  public String parseOutName(MModelElement me, String s) {
    s = s.trim();
    if (s.equals("") || s.charAt(0) == '=') return s;
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, ": \t()[]=;");
    if (!st.hasMoreTokens()) {
      System.out.println("name not parsed");
      return s;
    }
    String nameStr = st.nextToken();

    // needs-more-work: wasteful
     me.setName(nameStr);

    int namePos = s.indexOf(nameStr);
    return s.substring(namePos + nameStr.length());
  }


	public String parseOutType(MAttribute attr, String s) {
            int equalIndex = s.indexOf("=");
            String typeStr = "";
            String retStr  = "";

            int i, len = s.length();
            for (i = 0; ((i < len) && (i != equalIndex)); i++) {
		typeStr = (i > 0) ? s.substring(0,i) : "";
		retStr = (i < len) ? s.substring(i, len) : "";
            }
            Project p = ProjectBrowser.TheInstance.getProject();
            MClassifier type = null; // = p.findType("int");
            /*
            int firstSpace = s.indexOf(" ");
            int firstEq = s.indexOf("=");
            if (firstEq != -1 && firstEq < firstSpace) firstSpace = firstEq;
            if (firstSpace != -1) {
                    String typeStr = s.substring(0, firstSpace);
                    // System.out.println("Trying to find "+typeStr+" in project...");
                    type = p.findType(typeStr);
            }
            */
            // System.out.println("Trying to find "+typeStr+" in project...");
            type = p.findType(typeStr);
            // System.out.println("setting attribute type: " + type.getName());
            attr.setType(type);

            return retStr;
	}


    public String parseOutInitValue(MAttribute attr, String s) {
        s = s.trim();
        int equalsIndex = s.indexOf("=");

//        if (equalsIndex != 0) return s;
        String initStr = s.substring(1, s.length()); //move past "=" to end of "initvalue"--pjs--
//        if (initStr.trim().length() == 0) return ""; // trim it here...pjs
        MExpression initExpr = new MExpression("Java", initStr);

          attr.setInitialValue(initExpr);
          return "";
  }

  private String parseOutColon(String s) {
	s = s.trim();
	if (s.length()>0 && s.charAt(0) == ':')
		return s.substring(1);
	return "";
  }

  public MParameter parseParameter(String s) {
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, ": = \t");
    String typeStr = "int"; 
    String paramNameStr = "parameterName?";

    if (st.hasMoreTokens()) paramNameStr = st.nextToken();
    if (st.hasMoreTokens()) typeStr = st.nextToken();
    Project p = ProjectBrowser.TheInstance.getProject();
    MClassifier cls = p.findType(typeStr);
    MParameter param = new MParameterImpl();
    param.setType(cls);
    param.setKind(MParameterDirectionKind.IN);
    param.setName(paramNameStr);

    return param;
  }


  //   public abstract Package parsePackage(String s);
//   public abstract MClassImpl parseClassifier(String s);

  public MStereotype parseStereotype(String s) {
    return null;
  }

  public MTaggedValue parseTaggedValue(String s) {
    return null;
  }

//   public abstract MAssociation parseAssociation(String s);
//   public abstract MAssociationEnd parseAssociationEnd(String s);

  /** Parse a string of the form: "range, ...", where range is of the
   *  form "lower..upper", or "integer" */
  public MMultiplicity parseMultiplicity(String s) {
	  return new MMultiplicity(s);
  }


  public MState parseState(String s) {
    return null;
  }

  public void parseStateBody(MState st, String s) {
      //remove all old transitions; needs-more-work: this should be done better!!
      st.setEntry(null);
      st.setExit(null);

      Collection trans = new ArrayList();
      java.util.StringTokenizer lines = new java.util.StringTokenizer(s, "\n\r");
      while (lines.hasMoreTokens()) {
	  String line = lines.nextToken().trim();
	  if (line.startsWith("entry")) parseStateEntyAction(st, line);
	  else if (line.startsWith("exit")) parseStateExitAction(st, line);
	  else {
	      MTransition t = parseTransition(new MTransitionImpl(), line);

	      if (t == null) continue;
	      //System.out.println("just parsed:" + GeneratorDisplay.Generate(t));
	      t.setStateMachine(st.getStateMachine());
	      t.setTarget(st);
	      t.setSource(st);
	      trans.add(t);
	  }
      }

      Vector internals = new Vector(st.getInternalTransitions());
      Vector oldinternals = new Vector(st.getInternalTransitions());
      internals.removeAll(oldinternals); //now the vector is empty

      // don't forget to remove old internals!
      for (int i = 0; i < oldinternals.size(); i++)
	  ((MTransition)oldinternals.elementAt(i)).remove();
      internals.addAll(trans);

      st.setInternalTransitions(trans);
  }

    public void parseStateEntyAction(MState st, String s) {
    if (s.startsWith("entry") && s.indexOf("/") > -1)
	s = s.substring(s.indexOf("/")+1).trim();
    MCallAction entryAction=(MCallAction)parseAction(s);
    entryAction.setName("anon");
    st.setEntry(entryAction);
  }

  public void parseStateExitAction(MState st, String s) {
    if (s.startsWith("exit") && s.indexOf("/") > -1)
      s = s.substring(s.indexOf("/")+1).trim();
    MCallAction exitAction=(MCallAction)parseAction(s);
    exitAction.setName("anon");
    st.setExit(exitAction);
  }

  /** Parse a line of the form: "name: trigger [guard] / actions" */
  public MTransition parseTransition(MTransition trans, String s) {
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

    /*     System.out.println("name=|" + name +"|");
     System.out.println("trigger=|" + trigger +"|");
     System.out.println("guard=|" + guard +"|");
     System.out.println("actions=|" + actions +"|");
    */
    trans.setName(parseName(name));

    if (trigger.length()>0) {
	MEvent evt=parseEvent(trigger);
	if (evt!=null){
	    trans.setTrigger((MCallEvent)evt);
	}
    }
    else
	trans.setTrigger(null);

    if (guard.length()>0){
	MGuard g=parseGuard(guard);
	if (g!=null){
	    g.setName("anon");
	    g.setTransition(trans);
	    trans.setGuard(g);
	}
    }
    else
	trans.setGuard(null);

    if (actions.length()>0){
	MCallAction effect=(MCallAction)parseAction(actions);
	effect.setName("anon");
	trans.setEffect(effect);
    }
    else
	trans.setEffect(null);

    return trans;
  }

  /** Parse a line of the form: "name: base" */
  public void parseClassifierRole(MClassifierRole cls, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String basefirst = "";
    String bases = "";
    StringTokenizer baseTokens = null;

    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      bases = s.substring(s.indexOf(":") + 1).trim();
      baseTokens = new StringTokenizer(bases,",");
    }
    else {
      name = s;
    }

    cls.setName(name);

    Collection col = cls.getBases();
    if ((col != null) && (col.size()>0)) {
      Iterator itcol = col.iterator();
      while (itcol.hasNext()) {
        MClassifier bse = (MClassifier) itcol.next();
	if (bse!=null)
	    cls.removeBase(bse);
      }
    }

    if (baseTokens!=null){
	while(baseTokens.hasMoreElements()){
	    String typeString = baseTokens.nextToken();
	    MClassifier type = ProjectBrowser.TheInstance.getProject().findType(typeString);
	    if (type!=null)
		cls.addBase(type);
	}
    }

   }

  /** Parse a line of the form: "name: action" */
  public void parseMessage(MMessage mes, String s) {
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

     MAction ua = (MAction) mes.getAction();
     ua.setName(action);
     mes.setName(name);

  }

  /** Parse a line of the form: "name: action" */
  public void parseStimulus(MStimulus sti, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    //cut trailing string "new Action"
    s = s.trim();
    if (s.length() == 0) return;
    if (s.endsWith("new Action"))
      s = s.substring(0, s.length() - 10);

    String name = "";
    String action = "";
    String actionfirst = "";
    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      actionfirst = s.substring(s.indexOf(":") + 1).trim();
      if (actionfirst.indexOf(":", 0) > 1) {
        action = actionfirst.substring(0, actionfirst.indexOf(":")).trim();
      }
      else action = actionfirst;
    }
    else name = s;

     MAction act = (MAction) sti.getDispatchAction();
     act.setName(action);
     sti.setName(name);
  }

  public MAction parseAction(String s) {
	  MCallAction a = new MCallActionImpl();
	  a.setScript(new MActionExpression("Java",s));
	  return a;
  }

    /*  public MActionSequence parseActions(String s) {
    MActionSequence as = new MActionSequenceImpl();
    as.setName(s);
    return as;
    }*/

  public MGuard parseGuard(String s) {
	MGuard g = new MGuardImpl();
	g.setExpression(new MBooleanExpression("Java",s));
        return g;
  }

  public MEvent parseEvent(String s) {
	MCallEvent ce = new MCallEventImpl();
	ce.setName(s);
	ce.setNamespace(ProjectBrowser.TheInstance.getProject().getModel());
        return ce;
  }

  /** Parse a line of the form: "name: base-class" */
  public void parseObject(MObject obj, String s) {
    // strip any trailing semi-colons
    s = s.trim();

    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String basefirst = "";
    String bases = "";
    StringTokenizer baseTokens = null;

    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":",0)).trim();
      bases = s.substring(s.indexOf(":",0) + 1).trim();
      baseTokens = new StringTokenizer(bases,",");
    }
    else {
      name = s;
    }

    obj.setName(name);

    obj.setClassifiers(new Vector());
    if (baseTokens != null) {
      while(baseTokens.hasMoreElements()){
  	String typeString = baseTokens.nextToken();
	MClassifier type = ProjectBrowser.TheInstance.getProject().findType(typeString);
	obj.addClassifier(type);
      }
    }
  }

  /** Parse a line of the form: "name : base-node" */
  public void parseNodeInstance(MNodeInstance noi, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);



    String name = "";
    String bases = "";
    StringTokenizer tokenizer = null;

    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      bases = s.substring(s.indexOf(":") + 1).trim();
    }
    else {
      name = s;
    }

    tokenizer = new StringTokenizer(bases,",");

    Vector v = new Vector();
    MNamespace ns = noi.getNamespace();
    if (ns !=null) {
	while (tokenizer.hasMoreElements()) {
	    String newBase = tokenizer.nextToken();
	    MClassifier cls = (MClassifier)ns.lookup(newBase.trim());
	    if (cls != null)
		v.add(cls);
	}
    }

    noi.setClassifiers(v);
    noi.setName(new String(name));

  }

  /** Parse a line of the form: "name : base-component" */
  public void parseComponentInstance(MComponentInstance coi, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String bases = "";
    StringTokenizer tokenizer = null;

    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      bases = s.substring(s.indexOf(":") + 1).trim();
    }
    else {
      name = s;
    }

    tokenizer = new StringTokenizer(bases,",");

    Vector v = new Vector();
    MNamespace ns = coi.getNamespace();
    if (ns !=null) {
	while (tokenizer.hasMoreElements()) {
	    String newBase = tokenizer.nextToken();
	    MClassifier cls = (MClassifier)ns.lookup(newBase.trim());
	    if (cls != null)
		v.add(cls);
	}
    }

    coi.setClassifiers(v);
    coi.setName(new String(name));

  }

} /* end class ParserDisplay */
