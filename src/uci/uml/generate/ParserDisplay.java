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

//import com.sun.java.util.collections.*;
import java.beans.*;

import com.sun.java.util.collections.*;
import java.util.StringTokenizer;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;
import uci.uml.ui.ProjectBrowser;
import uci.uml.ui.Project;
import uci.uml.visual.*;
import uci.gef.*;
import uci.graph.*;
import uci.uml.util.MMUtil;

public class ParserDisplay extends Parser {

  public static ParserDisplay SINGLETON = new ParserDisplay();

  ////////////////////////////////////////////////////////////////
  // parsing methods

  public void parseOperationCompartment(MClassifier cls, String s) {
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, "\n\r");
    List newOps = new ArrayList();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      MOperation op = parseOperation(token);
      newOps.add(op);
    }
    // System.out.println("parsed " + newOps.size() + " operations");
	Vector features = new Vector(cls.getFeatures());
	features.removeAll(MMUtil.SINGLETON.getOperations(cls));
	features.addAll(newOps);
	cls.setFeatures(features);

  }

  public void parseAttributeCompartment(MClassifier cls, String s) {
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, "\n\r");
    List newAttrs = new ArrayList();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      MAttribute attr = parseAttribute(token);
      newAttrs.add(attr);
    }
    // System.out.println("parsed " + newAttrs.size() + " attributes");
	Vector features = new Vector(cls.getFeatures());
	features.removeAll(MMUtil.SINGLETON.getAttributes(cls));
	features.addAll(newAttrs);
	cls.setFeatures(features);
	
  }
	
  /** Parse a line of the form:
   *  [visibility] [keywords] returntype name(params)[;] */
  public MOperation parseOperation(String s) {
    s = s.trim();
    if (s.endsWith(";")) s = s.substring(0, s.length()-1);
    MOperation res = new MOperationImpl();
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
  public MAttribute parseAttribute(String s) {
    s = s.trim();
    if (s.endsWith(";")) s = s.substring(0, s.length()-1);
    MAttribute newAttribute = new MAttributeImpl();
    s = parseOutVisibility(newAttribute, s);
    s = parseOutKeywords(newAttribute, s);
    s = parseOutType(newAttribute, s);
    s = parseOutName(newAttribute, s);
//     if (newAttribute.getName() == null && newAttribute.getType() != null) {
// 		newAttribute.setName(newAttribute.getType().getName());
// 		Project p = ProjectBrowser.TheInstance.getProject();
// 		newAttribute.setType(p.findType("int"));
//    }
    s = parseOutInitValue(newAttribute, s);
    if (s.length() > 2)
      System.out.println("leftover in parseAttribute=|" + s + "|");
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
    int firstSpace = s.indexOf(" ");
    if (firstSpace == -1) return s;
    String rtStr = s.substring(0, firstSpace);
    if (rtStr.indexOf("(") > 0) {
		// must be CONSTRUCTOR, must be included in nsuml later on!
       op.setStereotype(new MStereotypeImpl());
      return s;
    }
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    MClassifier rt = p.findType(rtStr);
    
    //System.out.println("setting return type: " + rtStr);
    MParameter param = new MParameterImpl();
    param.setType(rt);
    MMUtil.SINGLETON.setReturnParameter(op,param);
    return s.substring(firstSpace+1);
  }

	public String parseOutParams(MOperation op, String s) {
		s = s.trim();
		String leftOver = s;
		java.util.StringTokenizer st = new java.util.StringTokenizer(s, "(),");
		List params = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			MParameter p = parseParameter(token);
			if (p != null) params.add(p);
			if (!st.hasMoreTokens())
				leftOver = s.substring(s.indexOf(token) + token.length());
		}
		op.setParameters(params);
		
		return leftOver;
	}
	
  public String parseOutName(MModelElement me, String s) {
    s = s.trim();
    if (s.equals("") || s.charAt(0) == '=') return s;
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, " \t()[]=;");
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
		s = s.trim();
		int firstSpace = s.indexOf(" ");

		int firstEq = s.indexOf("=");
		if (firstEq != -1 && firstEq < firstSpace) firstSpace = firstEq;

		Project p = ProjectBrowser.TheInstance.getProject();
		MClassifier type=null; // = p.findType("int");

		if (firstSpace != -1) {
			String typeStr = s.substring(0, firstSpace);
			// System.out.println("Trying to find "+typeStr+" in project...");
			type = p.findType(typeStr);
		}

		// System.out.println("setting attribute type: " + type.getName());
		attr.setType(type);
		return s.substring(firstSpace+1);
	}

  public String parseOutInitValue(MAttribute attr, String s) {
    s = s.trim();
    int equalsIndex = s.indexOf("=");
    if (equalsIndex != 0) return s;
    String initStr = s.substring(1).trim(); //move past "="
    if (initStr.length() == 0) return "";
    MExpression initExpr = new MExpression("Java", initStr);

      //System.out.println("setting return type: " + rtStr);
      attr.setInitialValue(initExpr);
    return "";
  }

  public MParameter parseParameter(String s) {
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, " \t");
    String typeStr = "int", paramNameStr = "parameterName?";
    if (st.hasMoreTokens()) typeStr = st.nextToken();
    if (st.hasMoreTokens()) paramNameStr = st.nextToken();
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
	  int low = 0;
	  int high = 0;
	  s = s.trim();
	  if (s.length() == 0) return MMultiplicity.M1_1;
	  java.util.StringTokenizer st1 = new java.util.StringTokenizer(s, ",");
	  while (st1.hasMoreElements()) {
		  String range = st1.nextToken().trim();
		  String lowStr = "", highStr = "";
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
			  if (lowStr.indexOf("*") == -1) low = Integer.valueOf(lowStr).intValue();
			  if (highStr.indexOf("*") == -1) high = Integer.valueOf(highStr).intValue();
		  }
		  catch (NumberFormatException nfe) { }
	  }
	  //	MMultiplicity res =new MMultiplicityImpl();
	  //res.setUpper(high);
	  //res.setLower(low);
	  return new MMultiplicity(low, high);
  }
	

  public MState parseState(String s) {
    return null;
  }

  public void parseStateBody(MState st, String s) {
    Collection trans = new ArrayList();
    java.util.StringTokenizer lines = new java.util.StringTokenizer(s, "\n\r");
    while (lines.hasMoreTokens()) {
      String line = lines.nextToken().trim();
      if (line.startsWith("entry")) parseStateEntyAction(st, line);
      else if (line.startsWith("exit")) parseStateExitAction(st, line);
      else {
		MTransition t = parseTransition(line);
		if (t == null) continue;
		//System.out.println("just parsed:" + GeneratorDisplay.Generate(t));
		trans.add(t);
      }
    }
    st.setInternalTransitions(trans);
  }

  public void parseStateEntyAction(MState st, String s) {
    if (s.startsWith("entry") && s.indexOf("/") > -1)
      s = s.substring(s.indexOf("/")+1).trim();
    st.setEntry(parseActions(s));
  }

  public void parseStateExitAction(MState st, String s) {
    if (s.startsWith("exit") && s.indexOf("/") > -1)
      s = s.substring(s.indexOf("/")+1).trim();
    st.setExit(parseActions(s));
  }

  /** Parse a line of the form: "name: trigger [guard] / actions" */
  public MTransition parseTransition(String s) {
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

    MTransition t = new MTransitionImpl();
    t.setName(parseName(name));

    t.setTrigger(parseEvent(trigger));
    t.setGuard(parseGuard(guard));
    t.setEffect(parseActions(actions));


    return t;
  }

  /** Parse a line of the form: "name: base" */
  public void parseClassifierRole(MClassifierRole cls, String s) {
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

	Project p = ProjectBrowser.TheInstance.getProject();
	MClassifier type = p.findType(base);
	if (type != null) {
		cls.setBases(new Vector());
		cls.addBase(type);
	}
	else {
		// generate critic here? (Toby)
		cls.setBases(new Vector());
	}

    cls.setName(name);

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

     MUninterpretedAction ua = (MUninterpretedAction) mes.getAction();
     ua.setName(action);
     mes.setName(name);

  }

  public MAction parseAction(String s) {
    return null;
  }

  public MActionSequence parseActions(String s) {
    MActionSequence as = new MActionSequenceImpl();
    as.setName(s);
    return as;
  }

  public MGuard parseGuard(String s) {
	MGuard g = new MGuardImpl();
	g.setName(s);
    return g;
  }

  public MEvent parseEvent(String s) {
	MSignalEvent se = new MSignalEventImpl();
	se.setName(s);
    return se;
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
    String base = "";
    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      basefirst = s.substring(s.indexOf(":") + 1).trim();
      if (basefirst.indexOf(":", 0) > 1) {
        base = basefirst.substring(0, basefirst.indexOf(":")).trim();
      }
      else base = basefirst;
    }
    else {
      name = s;
    }

    Collection col = obj.getClassifiers();
    if ((col != null) && (col.size()>0)) { 
      Iterator itcol = col.iterator(); 
      while (itcol.hasNext()) { 
        MClassifier cls = (MClassifier) itcol.next(); 
        obj.removeClassifier(cls); 
      } 
    } 

    Vector diagrams = ProjectBrowser.TheInstance.getProject().getDiagrams();
    Vector v = new Vector();
    MClass classifier = new MClassImpl();
    GraphModel model = null;
    int size = diagrams.size();
    for (int i=0; i<size; i++) {
      Object o = diagrams.elementAt(i);
      if (!(o instanceof Diagram)) continue;
      if (o instanceof MModel) continue;
      Diagram d = (Diagram) o;
      model = d.getGraphModel(); 
      if (!(model instanceof ClassDiagramGraphModel || model instanceof DeploymentDiagramGraphModel)) continue;
       
      Vector nodes = model.getNodes();
      int si = nodes.size();
      for (int j=0; j<si; j++) {
        MModelElement node = (MModelElement) nodes.elementAt(j);
        if (node != null && (node instanceof MClassImpl)) {
          MClass mclass = (MClass) node;
          if (mclass.getNamespace() != obj.getNamespace()) continue;
          String class_name = mclass.getName();
          if (class_name != null && (class_name.equals(base))) {
            v.addElement(mclass);
            obj.setClassifiers(v);
            obj.setName(new String(name)); 
            return; 
          }      
        }
      }
    }
    classifier.setName(base);
    
    v.addElement(classifier);
    obj.setClassifiers(v);
    obj.setName(new String(name));

  }

  /** Parse a line of the form: "name : base-node" */ 
  public void parseNodeInstance(MNodeInstance noi, String s) { 
    // strip any trailing semi-colons 
    s = s.trim(); 
    if (s.length() == 0) return; 
    if (s.charAt(s.length()-1) == ';') 
      s = s.substring(0, s.length() - 2); 
 

    String name = ""; 
    String basefirst = "";
    String base = ""; 
    if (s.indexOf(":", 0) > -1) { 
      name = s.substring(0, s.indexOf(":")).trim(); 
      basefirst = s.substring(s.indexOf(":") + 1).trim();
      if (basefirst.indexOf(":", 0) > 1) {
        base = basefirst.substring(0, basefirst.indexOf(":")).trim();
      }
      else base = basefirst;
    } 
    else { 
      name = s; 
    } 
 
    Collection col = noi.getClassifiers(); 
    if ((col != null) && (col.size()>0)) {  
      Iterator itcol = col.iterator();  
      while (itcol.hasNext()) {  
        MClassifier cls = (MClassifier) itcol.next();  
        noi.removeClassifier(cls);  
      }  
    }  
 
    Vector diagrams = ProjectBrowser.TheInstance.getProject().getDiagrams(); 
    Vector v = new Vector(); 
    MNode no = new MNodeImpl(); 
    GraphModel model = null; 
    int size = diagrams.size(); 
    for (int i=0; i<size; i++) { 
      Object o = diagrams.elementAt(i); 
      if (!(o instanceof Diagram)) continue; 
      if (o instanceof MModel) continue; 
      Diagram d = (Diagram) o; 
      model = d.getGraphModel();  
      if (!(model instanceof DeploymentDiagramGraphModel)) continue; 
        
      Vector nodes = model.getNodes(); 
      int si = nodes.size(); 
      for (int j=0; j<si; j++) { 
        MModelElement node = (MModelElement) nodes.elementAt(j); 
        if (node != null && (node instanceof MNodeImpl)) { 
          MNode mnode = (MNode) node; 
          if (mnode.getNamespace() != noi.getNamespace()) continue;
          String node_name = mnode.getName(); 
          if (node_name != null && (node_name.equals(base))) { 
            v.addElement(mnode); 
            noi.setClassifiers(v); 
            return;  
          }       
        } 
      } 
    } 
    no.setName(base);     
    v.addElement(no); 
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
    String basefirst = "";
    String base = ""; 
    if (s.indexOf(":", 0) > -1) { 
      name = s.substring(0, s.indexOf(":")).trim(); 
      basefirst = s.substring(s.indexOf(":") + 1).trim();
      if (basefirst.indexOf(":", 0) > 1) {
        base = basefirst.substring(0, basefirst.indexOf(":")).trim();
      }
      else base = basefirst;
    } 
    else { 
      name = s; 
    } 
 
    Collection col = coi.getClassifiers(); 
    if ((col != null) && (col.size()>0)) {  
      Iterator itcol = col.iterator();  
      while (itcol.hasNext()) {  
        MClassifier cls = (MClassifier) itcol.next();  
        coi.removeClassifier(cls);  
      }  
    }  
 
    Vector diagrams = ProjectBrowser.TheInstance.getProject().getDiagrams(); 
    Vector v = new Vector(); 
    MComponent co = new MComponentImpl(); 
    GraphModel model = null; 
    int size = diagrams.size(); 
    for (int i=0; i<size; i++) { 
      Object o = diagrams.elementAt(i); 
      if (!(o instanceof Diagram)) continue; 
      if (o instanceof MModel) continue; 
      Diagram d = (Diagram) o; 
      model = d.getGraphModel();  
      if (!(model instanceof DeploymentDiagramGraphModel)) continue; 
        
      Vector nodes = model.getNodes(); 
      int si = nodes.size(); 
      for (int j=0; j<si; j++) { 
        MModelElement node = (MModelElement) nodes.elementAt(j); 
        if (node != null && (node instanceof MComponentImpl)) { 
          MComponent mcomp = (MComponent) node; 
          if (mcomp.getNamespace() != coi.getNamespace()) continue;
          String comp_name = mcomp.getName(); 
          if (comp_name != null && (comp_name.equals(base))) { 
            v.addElement(mcomp); 
            coi.setClassifiers(v); 
            return;  
          }       
        } 
      } 
    } 
    co.setName(base);     
    v.addElement(co); 
    coi.setClassifiers(v);
    coi.setName(new String(name)); 
 
  } 

} /* end class ParserDisplay */
