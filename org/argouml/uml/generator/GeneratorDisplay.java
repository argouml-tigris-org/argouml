// Copyright (c) 1996-2001 The Regents of the University of California. All
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
import org.argouml.application.api.*;

//import java.util.*;
import java.io.*;

import java.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.data_types.MMultiplicityRange;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.model_management.*;

import org.argouml.uml.MMUtil;

/** Generator subclass to generate text for display in diagrams in in
 * text fields in the Argo/UML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for outher languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced. */

// needs-more-work: always check for null!!!

public class GeneratorDisplay extends Generator
implements PluggableNotation {

  private static GeneratorDisplay SINGLETON = new GeneratorDisplay();

  public static GeneratorDisplay getInstance() { return SINGLETON; }


  public GeneratorDisplay() {
     super(Notation.makeNotation("Uml", "1.3",
                Argo.lookupIconResource("UmlNotation")));
  }


  public static String Generate(Object o) {
    return SINGLETON.generate(o);
  }

  public String generateOperation(MOperation op) {
    boolean isConstructor = false;
    if(op.getStereotype() != null) {
	isConstructor = "create".equals(op.getStereotype().getName());
    }
    String s = "";
    s += generateVisibility(op);
    if (!isConstructor) {
	s += generateScope(op);
    }
    String nameStr = generateName(op.getName());
    String clsName = generateName(op.getOwner().getName());

    // pick out return type
    MParameter rp = MMUtil.SINGLETON.getReturnParameter(op);

    // name and params
    Vector params = new Vector(op.getParameters());
    params.remove(rp);
    s += nameStr + "(";
	if (params != null) {
		boolean first = true;
		for (int i=0; i < params.size(); i++) {
			MParameter p = (MParameter) params.elementAt(i);
			if (!first) s += ", ";
			s += generateParameter(p);
			first = false;
		}
    }
    s += ")";

    // append return type
    if (!isConstructor) {
	if (rp != null && rp.getType() != null) {
	    s += ": " + generateClassifierRef(rp.getType());
	}
	else {
	    s += ": void??";
	}
    }
    return s;
  }

  public String generateAttribute(MAttribute attr) {
    String s = "";
    s += generateVisibility(attr);
    s += generateScope(attr);
    s += generateChangability(attr);
    /*	if (!(attr.getMultiplicity() == null)) {
		if (!MMultiplicity.M1_1.equals(attr.getMultiplicity()))
 			s += generateMultiplicity(attr.getMultiplicity()) + " ";
			}*/

    String slash = "";
    // not in nsuml: if (attr.containsStereotype(MStereotype.DERIVED)) slash = "/";

    s += slash + generateName(attr.getName());
    MExpression init = attr.getInitialValue();
    if (init != null) {
      String initStr = generateExpression(init).trim();
      if (initStr.length() > 0)
		  s += " = " + initStr;
    }
    MClassifier type = attr.getType();
    if (type != null) s += ": " + generateClassifierRef(type);

//     String constraintStr = generateConstraints(attr);
//     if (constraintStr.length() > 0)
//       s += " " + constraintStr;

	// System.out.println("generated attribute string: "+s);
    return s;
  }


  public String generateParameter(MParameter param) {
    String s = "";
    //needs-more-work: qualifiers (e.g., const)
    //needs-more-work: stereotypes...
    s += generateName(param.getName()) + ": ";
    s += generateClassifierRef(param.getType());
    //needs-more-work: initial value
    return s;
  }


  public String generatePackage(MPackage p) {
    String s = "package ";
    String packName = generateName(p.getName());

    java.util.Stack stack = new java.util.Stack();
    MNamespace ns = p.getNamespace();
    while ( ns != null ) {
      stack.push(ns.getName());
      ns = ns.getNamespace();
    }
    while(!stack.isEmpty())
      s += (String) stack.pop() + ".";

    if (s.endsWith(".")) {
      int lastIndex = s.lastIndexOf(".");
      s = s.substring(0, lastIndex);
    }
    s += "." + packName + " {\n";


    Collection ownedElements = p.getOwnedElements();
    if (ownedElements != null) {
      Iterator ownedEnum = ownedElements.iterator();
      while (ownedEnum.hasNext()) {
	s += generate((MModelElement) ownedEnum.next());
	s += "\n\n";
      }
    }
    else {
      s += "(no elements)";
    }
    s += "\n}\n";
    return s;
  }


  public String generateClassifier(MClassifier cls) {
    String generatedName = generateName(cls.getName());
    String classifierKeyword;
    if (cls instanceof MClassImpl) classifierKeyword = "class";
    else if (cls instanceof MInterface) classifierKeyword = "interface";
    else return ""; // actors and use cases
    String s = "";
    s += generateVisibility(cls.getVisibility());
    if (cls.isAbstract()) s += "abstract ";
    if (cls.isLeaf()) s += "final ";
    s += classifierKeyword + " " + generatedName + " ";
    String baseClass = generateGeneralization(cls.getGeneralizations(), false);
    if (!baseClass.equals("")) s += "extends " + baseClass + " ";

    //nsuml: realizations!
//     String interfaces = generateRealization(cls.getRealizations(), true);
//     if (!interfaces.equals("")) s += "implements " + interfaces + " ";
	s += "{\n";

    Collection strs = MMUtil.SINGLETON.getAttributes(cls);
    if (strs != null) {
      s += "\n";
      //s += "////////////////////////////////////////////////////////////////\n";
      s += INDENT + "// Attributes\n";
      Iterator strEnum = strs.iterator();
      while (strEnum.hasNext())
	s += INDENT + generate(strEnum.next()) + ";\n";
    }

    Collection ends = cls.getAssociationEnds();
    if (ends != null) {
      s += "\n";
      //s += "////////////////////////////////////////////////////////////////\n";
      s += INDENT + "// Associations\n";
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	MAssociation a = ae.getAssociation();
	s += INDENT + generateAssociationFrom(a, ae);
      }
    }

    // needs-more-work: constructors

    Collection behs = MMUtil.SINGLETON.getOperations(cls);
    if (behs != null) {
      s += "\n";
      //s += "////////////////////////////////////////////////////////////////\n";
      s += INDENT + "// Operations\n";
      Iterator behEnum = behs.iterator();
      String terminator = " {\n" + INDENT + "}";
      if (cls instanceof MInterface) terminator = ";";
      while (behEnum.hasNext())
	s += INDENT + generate(behEnum.next()) + terminator + "\n";
    }
    s += "\n";
    s += "} /* end " + classifierKeyword + " " + generatedName + " */\n";
    return s;
  }

  public String generateTaggedValue(MTaggedValue tv) {
    if (tv == null) return "";
    return generateName(tv.getTag()) + "=" +
      generateUninterpreted(tv.getValue());
  }


    public String generateMessage(MMessage m) {
	if (m == null) return "";
	return generateName(m.getName()) + "::" +
	    generateAction(m.getAction());
    }

    public String generateAssociationFrom(MAssociation a, MAssociationEnd ae) {
    // needs-more-work: does not handle n-ary associations
    String s = "";
    Collection connections = a.getConnections();
    Iterator connEnum = connections.iterator();
    while (connEnum.hasNext()) {
      MAssociationEnd ae2 = (MAssociationEnd) connEnum.next();
      if (ae2 != ae) s += generateAssociationEnd(ae2);
    }
    return s;
  }

  public String generateAssociation(MAssociation a) {
    String s = "";
//     String generatedName = generateName(a.getName());
//     s += "MAssociation " + generatedName + " {\n";

//     Iterator endEnum = a.getConnection().iterator();
//     while (endEnum.hasNext()) {
//       MAssociationEnd ae = (MAssociationEnd)endEnum.next();
//       s += generateAssociationEnd(ae);
//       s += ";\n";
//     }
//     s += "}\n";
    return s;
  }

  public String generateAssociationEnd(MAssociationEnd ae) {
    if (!ae.isNavigable()) return "";
    String s = "protected ";
    if (MScopeKind.CLASSIFIER.equals(ae.getTargetScope()))
	s += "static ";
//     String n = ae.getName();
//     if (n != null && !String.UNSPEC.equals(n)) s += generateName(n) + " ";
//     if (ae.isNavigable()) s += "navigable ";
//     if (ae.getIsOrdered()) s += "ordered ";
    MMultiplicity m = ae.getMultiplicity();
    if (MMultiplicity.M1_1.equals(m) || MMultiplicity.M0_1.equals(m))
      s += generateClassifierRef(ae.getType());
    else
      s += "Vector "; //generateMultiplicity(m) + " ";

    s += " ";

    String n = ae.getName();
    MAssociation asc = ae.getAssociation();
    String ascName = asc.getName();
    if (n != null  &&
	n != null && n.length() > 0) {
      s += generateName(n);
    }
    else if (ascName != null  &&
	ascName != null && ascName.length() > 0) {
      s += generateName(ascName);
    }
    else {
      s += "my" + generateClassifierRef(ae.getType());
    }

    return s + ";\n";
  }

  public String generateConstraints(MModelElement me) {
    Collection constr = me.getConstraints();
    if (constr == null || constr.size() == 0) return "";
    String s = "{";
    Iterator conEnum = constr.iterator();
    while (conEnum.hasNext()) {
      s += generateConstraint((MConstraint)conEnum.next());
      if (conEnum.hasNext()) s += "; ";
    }
    s += "}";
    return s;
  }


  public String generateConstraint(MConstraint c) {
    return generateExpression(c);
  }

  ////////////////////////////////////////////////////////////////
  // internal methods?


  public String generateGeneralization(Collection generalizations, boolean impl) {
	Collection classes = new ArrayList();
    if (generalizations == null) return "";
    Iterator enum = generalizations.iterator();
    while (enum.hasNext()) {
	  MGeneralization g = (MGeneralization) enum.next();
	  MGeneralizableElement ge = g.getPowertype();
      // assert ge != null
      if (ge != null){
       if (impl)
		 { if (ge instanceof MInterface) classes.add(ge);}
	   else
		 { if (!(ge instanceof MInterface)) classes.add(ge);}
	  }
    }
    return generateClassList(classes);
  }

  public String generateClassList(Collection classifiers) {
    String s = "";
    if (classifiers == null) return "";
    Iterator clsEnum = classifiers.iterator();
    while (clsEnum.hasNext()) {
      s += generateClassifierRef((MClassImpl)clsEnum.next());
      if (clsEnum.hasNext()) s += ", ";
    }
    return s;
  }

  public String generateVisibility(MVisibilityKind vis) {
    if (vis == null) return "";
    //if (vis == null) return "";
    if (MVisibilityKind.PUBLIC.equals(vis)) return "+";
    if (MVisibilityKind.PRIVATE.equals(vis)) return "-";
    if (MVisibilityKind.PROTECTED.equals(vis)) return "#";
    return "";
  }

  public String generateVisibility(MFeature f) {
    MVisibilityKind vis = f.getVisibility();
    //if (vis == null) return "";
    if (MVisibilityKind.PUBLIC.equals(vis)) return "+";
    if (MVisibilityKind.PRIVATE.equals(vis)) return "-";
    if (MVisibilityKind.PROTECTED.equals(vis)) return "#";
    return "";
  }

  public String generateScope(MFeature f) {
    MScopeKind scope = f.getOwnerScope();
    //if (scope == null) return "";
    if (MScopeKind.CLASSIFIER.equals(scope)) return "static ";
    return "";
  }

  public String generateChangability(MStructuralFeature sf) {
    MChangeableKind ck = sf.getChangeability();
    //if (ck == null) return "";
    if (MChangeableKind.FROZEN.equals(ck)) return "final ";
    //if (MChangeableKind.ADDONLY.equals(ck)) return "final ";
    return "";
  }

  public String generateMultiplicity(MMultiplicity m) {
    if (m == null) { return ""; }
    if (MMultiplicity.M0_N.equals(m)) return ANY_RANGE;
    String s = "";
    Collection v = m.getRanges();
    if (v == null) return s;
    Iterator rangeIter = v.iterator();
    while (rangeIter.hasNext()) {
      MMultiplicityRange mr = (MMultiplicityRange) rangeIter.next();
      s += generateMultiplicityRange(mr);
      if (rangeIter.hasNext()) s += ",";
    }
    return s;
  }


  public static final String ANY_RANGE = "0..*";
  //public static final String ANY_RANGE = "*";
  // needs-more-work: user preference between "*" and "0..*"

  public String generateMultiplicityRange(MMultiplicityRange mr) {

      return mr.toString();
//     Integer lower = new Integer(mr.getLower());
//     Integer upper = new Integer(mr.getUpper());
//     if (lower == null && upper == null) return ANY_RANGE;
//     if (lower == null) return "*.."+ upper.toString();
//     if (upper == null) return lower.toString() + "..*";
//     if (lower.intValue() == upper.intValue()) return lower.toString();
//     return lower.toString() + ".." + upper.toString();
  }

  public String generateState(MState m) {
    return m.getName();
  }

  public String generateStateBody(MState m) {
      String s = "";

      MAction entry = m.getEntry();
      MAction exit = m.getExit();
      if (entry != null) {
	  String entryStr = Generate(entry);
	  if (entryStr.length() > 0) s += "entry / " + entryStr;
      }
      if (exit != null) {
	  String exitStr = Generate(exit);
	  if (s.length() > 0) s += "\n";
	  if (exitStr.length() > 0) s += "exit / " + exitStr;
      }
      Collection trans = m.getInternalTransitions();
      if (trans != null) {
	  Iterator iter = trans.iterator();
	  while(iter.hasNext())
	      {
		  if (s.length() > 0) s += "\n";
		  s += generateTransition((MTransition)iter.next());
	      }
      }
      return s;
  }

  public String generateTransition(MTransition m) {
    String s = generate(m.getName());
    String t = generate(m.getTrigger());
    String g = generate(m.getGuard());
    String e = generate(m.getEffect());
    if (s.length() > 0) s += ": ";
    s += t;
    if (g.length() > 0) s += " [" + g + "]";
    if (e.length() > 0) s += " / " + e;
    return s;
  }

  public String generateAction(MAction m) {
      if ((m.getScript() != null) && (m.getScript().getBody() != null))
	  return m.getScript().getBody();
      return "";
  }

  public String generateGuard(MGuard m) {
      if (m.getExpression() != null)
	  return generateExpression(m.getExpression());
      return "";
  }

  public NotationName getNotation() {
      return Notation.NOTATION_DEFAULT;
  }

  public boolean canParse() { return true; }

  public boolean canParse(Object o) { return true; }

    public String getModuleName() { return "GeneratorDisplay"; }
    public String getModuleDescription() {
        return "Uml 1.3 Notation Generator";
    }
    public String getModuleAuthor() { return "ArgoUML Core"; }
    public String getModuleVersion() { return "0.9.4"; }
    public String getModuleKey() { return "module.language.uml.generator"; }

} /* end class GeneratorDisplay */
