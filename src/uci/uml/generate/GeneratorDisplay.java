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
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

/** Generator subclass to generate text for display in diagrams in in
 * text fields in the Argo/UML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for outher languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced. */

// needs-more-work: always check for null!!!

public class GeneratorDisplay extends Generator {

  public static GeneratorDisplay SINGLETON = new GeneratorDisplay();

  public static String Generate(Object o) {
    return SINGLETON.generate(o);
  }

  public String generateOperation(Operation op) {
    String s = "";
    s += generateVisibility(op);
    s += generateScope(op);

    // pick out return type
    Classifier returnType = null;
    Vector params = op.getParameter();
    if (params != null) {
      java.util.Enumeration enum = params.elements();
      while (enum.hasMoreElements()) {
	Parameter p = (Parameter) enum.nextElement();
	if (Parameter.RETURN_NAME.equals(p.getName())) {
	  returnType = p.getType();
	  break;
	}
      }
    }
    if (returnType == null) s += "void?? ";
    else s += generateClassifierRef(returnType) + " ";


    // name and params
    s += generateName(op.getName()) + "(";
    params = op.getParameter();
    if (params != null) {
      java.util.Enumeration enum = params.elements();
      boolean first = true;
      while (enum.hasMoreElements()) {
	Parameter p = (Parameter) enum.nextElement();
	if (Parameter.RETURN_NAME.equals(p.getName())) continue;
	if (!first) s += ", ";
	s += generateParameter(p);
	first = false;
      }
    }
    s += ")";
    return s;
  }

  public String generateAttribute(Attribute attr) {
    String s = "";
    s += generateVisibility(attr);
    s += generateScope(attr);
    s += generateChangability(attr);
    if (!Multiplicity.ONE.equals(attr.getMultiplicity()))
      s += generateMultiplicity(attr.getMultiplicity()) + " ";

    Classifier type = attr.getType();
    if (type != null) s += generateClassifierRef(type) + " ";

    String slash = "";
    if (attr.containsStereotype(Stereotype.DERIVED)) slash = "/";

    s += slash + generateName(attr.getName());
    Expression init = attr.getInitialValue();
    if (init != null)
      s += " = " + generateExpression(init);

    String constraintStr = generateConstraints(attr);
    if (constraintStr.length() > 0)
      s += " " + constraintStr;

    return s;
  }


  public String generateParameter(Parameter param) {
    String s = "";
    //needs-more-work: qualifiers (e.g., const)
    //needs-more-work: stereotypes...
    s += generateClassifierRef(param.getType()) + " ";
    s += generateName(param.getName());
    //needs-more-work: initial value
    return s;
  }


  public String generatePackage(Package p) {
    String s = "";
    String packName = generateName(p.getName());
    s += "package " + packName + " {\n";
    Vector ownedElements = p.getOwnedElement();
    if (ownedElements != null) {
      java.util.Enumeration ownedEnum = ownedElements.elements();
      while (ownedEnum.hasMoreElements()) {
	ElementOwnership eo = (ElementOwnership) ownedEnum.nextElement();
	s += generate(eo.getModelElement());
	s += "\n\n";
      }
    }
    else {
      s += "(no elements)";
    }
    s += "\n}\n";
    return s;
  }


  public String generateClassifier(Classifier cls) {
    String generatedName = generateName(cls.getName());
    String classifierKeyword;
    if (cls instanceof MMClass) classifierKeyword = "class";
    else if (cls instanceof Interface) classifierKeyword = "interface";
    else return ""; // actors and use cases
    String s = "";
    s += generateVisibility(cls.getElementOwnership());
    if (cls.getIsAbstract()) s += "abstract ";
    if (cls.getIsLeaf()) s += "final ";
    s += classifierKeyword + " " + generatedName + " ";
    String baseClass = generateGeneralzation(cls.getGeneralization());
    if (!baseClass.equals("")) s += "extends " + baseClass + " ";
    String interfaces = generateRealization(cls.getRealization());
    if (!interfaces.equals("")) s += "implements " + interfaces + " ";
    s += "{\n";

    Vector strs = cls.getStructuralFeature();
    if (strs != null) {
      s += "\n";
      //s += "////////////////////////////////////////////////////////////////\n";
      s += "// Attributes\n";
      java.util.Enumeration strEnum = strs.elements();
      while (strEnum.hasMoreElements())
	s += generate(strEnum.nextElement()) + ";\n";
    }

    Vector ends = cls.getAssociationEnd();
    if (ends != null) {
      s += "\n";
      //s += "////////////////////////////////////////////////////////////////\n";
      s += "// Associations\n";
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	AssociationEnd ae = (AssociationEnd) endEnum.nextElement();
	IAssociation a = ae.getAssociation();
	s += generateAssociationFrom(a, ae);
      }
    }

    // needs-more-work: constructors

    Vector behs = cls.getBehavioralFeature();
    if (behs != null) {
      s += "\n";
      //s += "////////////////////////////////////////////////////////////////\n";
      s += "// Operations\n";
      java.util.Enumeration behEnum = behs.elements();
      while (behEnum.hasMoreElements())
	s += generate(behEnum.nextElement()) + ";\n";
    }

    s += "} /* end " + classifierKeyword + " " + generatedName + " */\n";
    return s;
  }

  public String generateStereotype(Stereotype s) {
    return "<<" + generateName(s.getName()) + ">>";
  }

  public String generateTaggedValue(TaggedValue tv) {
    if (tv == null) return "";
    return generateName(tv.getTag()) + "=" +
      generateUninterpreted(tv.getValue());
  }


  public String generateAssociationFrom(IAssociation a, AssociationEnd ae) {
    // needs-more-work: does not handle n-ary associations
    String s = "";
    Vector connections = a.getConnection();
    java.util.Enumeration connEnum = connections.elements();
    while (connEnum.hasMoreElements()) {
      AssociationEnd ae2 = (AssociationEnd) connEnum.nextElement();
      if (ae2 != ae) s += generateAssociationEnd(ae2);
    }
    return s;
  }

  public String generateAssociation(IAssociation a) {
    String s = "";
//     String generatedName = generateName(a.getName());
//     s += "Association " + generatedName + " {\n";

//     java.util.Enumeration endEnum = a.getConnection().elements();
//     while (endEnum.hasMoreElements()) {
//       AssociationEnd ae = (AssociationEnd)endEnum.nextElement();
//       s += generateAssociationEnd(ae);
//       s += ";\n";
//     }
//     s += "}\n";
    return s;
  }

  public String generateAssociationEnd(AssociationEnd ae) {
    if (!ae.getIsNavigable()) return "";
    String s = "protected ";
    if (ScopeKind.CLASSIFIER.equals(ae.getTargetScope()))
	s += "static ";
//     Name n = ae.getName();
//     if (n != null && !Name.UNSPEC.equals(n)) s += generateName(n) + " ";
//     if (ae.getIsNavigable()) s += "navigable ";
//     if (ae.getIsOrdered()) s += "ordered ";
    Multiplicity m = ae.getMultiplicity();
    if (Multiplicity.ONE.equals(m) || Multiplicity.ONE_OR_ZERO.equals(m))
      s += generateClassifierRef(ae.getType());
    else
      s += "Vector "; //generateMultiplicity(m) + " ";

    s += " ";
    
    Name n = ae.getName();
    IAssociation asc = ae.getAssociation();
    Name ascName = asc.getName();
    if (n != null && !Name.UNSPEC.equals(n) &&
	n.getBody() != null && n.getBody().length() > 0) {
      s += generateName(n);
    }
    else if (ascName != null && !Name.UNSPEC.equals(ascName) &&
	ascName.getBody() != null && ascName.getBody().length() > 0) {
      s += generateName(ascName);
    }
    else {
      s += "my" + generateClassifierRef(ae.getType());
    }

    return s + ";\n";
  }

  public String generateConstraints(ModelElement me) {
    Vector constr = me.getConstraint();
    if (constr == null || constr.size() == 0) return "";
    String s = "{";
    java.util.Enumeration conEnum = constr.elements();
    while (conEnum.hasMoreElements()) {
      s += generateConstraint((Constraint)conEnum.nextElement());
      if (conEnum.hasMoreElements()) s += "; ";
    }
    s += "}";
    return s;
  }


  public String generateConstraint(Constraint c) {
    return generateExpression(c.getBody());
  }

  ////////////////////////////////////////////////////////////////
  // internal methods?


  public String generateGeneralzation(Vector generalizations) {
    if (generalizations == null) return "";
    Vector classes = new Vector(generalizations.size());
    java.util.Enumeration enum = generalizations.elements();
    while (enum.hasMoreElements()) {
      Generalization g = (Generalization) enum.nextElement();
      GeneralizableElement ge = g.getSupertype();
      // assert ge != null
      if (ge != null) classes.addElement(ge);
    }
    return generateClassList(classes);
  }
  public String generateRealization(Vector realizations) {
    // Realization is much simplier than Generalization.
    // There is no Realization class in UML metamodel
    return generateRealizationList(realizations);
  }

  public String generateClassList(Vector classifiers) {
    String s = "";
    if (classifiers == null) return "";
    java.util.Enumeration clsEnum = classifiers.elements();
    while (clsEnum.hasMoreElements()) {
      s += generateClassifierRef((Classifier)clsEnum.nextElement());
      if (clsEnum.hasMoreElements()) s += ", ";
    }
    return s;
  }

  public String generateRealizationList(Vector realizations) {
    String s = "";
    if (realizations == null) return "";
    java.util.Enumeration clsEnum = realizations.elements();
    while (clsEnum.hasMoreElements()) {
      Realization r = (Realization)clsEnum.nextElement();
      s += generateClassifierRef(r.getSupertype());
      if (clsEnum.hasMoreElements()) s += ", ";
    }
    return s;
  }

  public String generateVisibility(ElementOwnership eo) {
    if (eo == null) return "";
    VisibilityKind vis = eo.getVisibility();
    //if (vis == null) return "";
    if (VisibilityKind.PUBLIC.equals(vis)) return "public ";
    if (VisibilityKind.PRIVATE.equals(vis)) return "private ";
    if (VisibilityKind.PROTECTED.equals(vis)) return "protected ";
    return "";
  }

  public String generateVisibility(Feature f) {
    VisibilityKind vis = f.getVisibility();
    //if (vis == null) return "";
    if (VisibilityKind.PUBLIC.equals(vis)) return "public ";
    if (VisibilityKind.PRIVATE.equals(vis)) return "private ";
    if (VisibilityKind.PROTECTED.equals(vis)) return "protected ";
    return "";
  }

  public String generateScope(Feature f) {
    ScopeKind scope = f.getOwnerScope();
    //if (scope == null) return "";
    if (ScopeKind.CLASSIFIER.equals(scope)) return "static ";
    return "";
  }

  public String generateChangability(StructuralFeature sf) {
    ChangeableKind ck = sf.getChangeable();
    //if (ck == null) return "";
    if (ChangeableKind.FROZEN.equals(ck)) return "final ";
    //if (ChangeableKind.ADDONLY.equals(ck)) return "final ";
    return "";
  }

  public String generateMultiplicity(Multiplicity m) {
    if (m == null) { System.out.println("null Multiplicity"); return ""; }
    if (Multiplicity.ZERO_OR_MORE.equals(m)) return ANY_RANGE;
    String s = "";
    Vector v = m.getRange();
    if (v == null) return s;
    java.util.Enumeration rangeEnum = v.elements();
    while (rangeEnum.hasMoreElements()) {
      MultiplicityRange mr = (MultiplicityRange) rangeEnum.nextElement();
      s += generateMultiplicityRange(mr);
      if (rangeEnum.hasMoreElements()) s += ",";
    }
    return s;
  }


  public static final String ANY_RANGE = "0..*";
  //public static final String ANY_RANGE = "*";
  // needs-more-work: user preference between "*" and "0..*"

  public String generateMultiplicityRange(MultiplicityRange mr) {

    Integer lower = mr.getLower();
    Integer upper = mr.getUpper();
    if (lower == null && upper == null) return ANY_RANGE;
    if (lower == null) return "*.."+ upper.toString();
    if (upper == null) return lower.toString() + "..*";
    if (lower.intValue() == upper.intValue()) return lower.toString();
    return mr.getLower().toString() + ".." + mr.getUpper().toString();
  }

} /* end class GeneratorDisplay */
