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
import java.io.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Model_Management.*;
import uci.uml.ui.DocumentationManager;

/** Generator subclass to generate text for display in diagrams in in
 * text fields in the Argo/UML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for outher languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced. */

// needs-more-work: always check for null!!!

public class GeneratorJava extends Generator {

  public static GeneratorJava SINGLETON = new GeneratorJava();

  /** Two spaces used for indenting code in classes. */
  public static String INDENT = "  ";

  public static String Generate(Object o) {
    return SINGLETON.generate(o);
  }

  public static void GenerateFile(Classifier cls, String path) {
    String name = cls.getName().getBody();
    if (name == null || name.length() == 0) return;
    String filename = name + ".java";
    if (!path.endsWith("/")) path += "/";
    File f = new File(path);
    if (!f.isDirectory()) {
      if (!f.mkdir()) {
	System.out.println(" could not make directory");
	return;
      }
    }

    String packagePath = cls.getNamespace().getName().getBody();
    Namespace parent = cls.getNamespace().getNamespace();
    while (parent != null) {
      packagePath = parent.getName().getBody() + "." + packagePath;
      parent = parent.getNamespace();
    }

    int dotIndex = packagePath.indexOf(".");
    while (dotIndex != -1) {
      path += packagePath.substring(0, dotIndex) + "/";
      packagePath = packagePath.substring(dotIndex + 1);
      dotIndex = packagePath.indexOf(".");
      f = new File(path);
      if (!f.isDirectory()) {
	if (!f.mkdir()) {
	  System.out.println(" could not make directory");
	  return;
	}
      }
    }
    path += packagePath.substring(0) + "/";
    f = new File(path);
    if (!f.isDirectory()) {
      if (!f.mkdir()) {
	System.out.println(" could not make directory:" + f);
	return;
      }
    }
    String pathname = path + filename;
    //String pathname = path + filename;
    // needs-more-work: package, project basepath, tagged values to configure
    String header = SINGLETON.generateHeader(cls, pathname);
    String src = SINGLETON.generate(cls);
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(pathname);
      fos.write(header.getBytes());
      fos.write(src.getBytes());
    }
    catch (IOException exp) { }
    finally {
      try { if (fos != null) fos.close(); }
      catch (IOException exp) { }
    }
  }

  public String generateHeader(Classifier cls, String pathname) {
    String s = "";
    //needs-more-work: add user-defined copyright
    s += "// FILE: " + pathname +"\n\n";
    Namespace ns = cls.getNamespace();
    String pack = ns.getName().getBody();
    if (pack.length() > 0) s += "package " + pack + ";\n";
    s += "import java.util.*;\n";

    s += "\n";
    return s;
  }

  public String generateOperation(Operation op) {
    String s = "";
    String nameStr = generateName(op.getName());
    String clsName = generateName(op.getOwner().getName());

    s += DocumentationManager.getDocs(op) + "\n" + INDENT;
    s += generateVisibility(op);
    s += generateScope(op);

    // pick out return type
    Classifier returnType = op.getReturnType();
    if (returnType == null && !nameStr.equals(clsName)) s += "void?? ";
    else if (returnType != null) s += generateClassifierRef(returnType) + " ";


    // name and params
    s += nameStr + "(";
    Vector params = op.getParameter();
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
    s += DocumentationManager.getDocs(attr) + "\n" + INDENT;
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
    if (init != null) {
      String initStr = generateExpression(init).trim();
      if (initStr.length() > 0)
	s += " = " + initStr;
    }

    s += ";\n";
    s += generateConstraints(attr);

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


  public String generatePackage(MMPackage p) {
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
    s += DocumentationManager.getDocs(cls) + "\n";
    s += generateVisibility(cls.getElementOwnership());
    if (cls.getIsAbstract() && !(cls instanceof Interface)) s += "abstract ";
    if (cls.getIsLeaf()) s += "final ";
    s += classifierKeyword + " " + generatedName + " ";
    String baseClass = generateGeneralzation(cls.getGeneralization());
    if (!baseClass.equals("")) s += "extends " + baseClass + " ";
    String interfaces = generateSpecification(cls.getSpecification());
    if (!interfaces.equals("")) s += "implements " + interfaces + " ";
    s += "{\n";

    s += INDENT + generateTaggedValues(cls);
    s += generateConstraints(cls);

    Vector strs = cls.getStructuralFeature();
    if (strs != null) {
      s += "\n";

      if (cls instanceof MMClass) s += INDENT + "// Attributes\n";
      java.util.Enumeration strEnum = strs.elements();
      while (strEnum.hasMoreElements()) {
	StructuralFeature sf = (StructuralFeature) strEnum.nextElement();
	s += INDENT + generate(sf);
	String tv = generateTaggedValues(sf);
	if (tv.length() > 0) s += INDENT + tv;
      }
    }

    Vector ends = cls.getAssociationEnd();
    if (ends != null) {
      s += "\n";
      if (cls instanceof MMClass) s += INDENT + "// Associations\n";
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	AssociationEnd ae = (AssociationEnd) endEnum.nextElement();
	IAssociation a = ae.getAssociation();
	s += INDENT + generateAssociationFrom(a, ae);
	String tv = generateTaggedValues(a);
	if (tv.length() > 0) s += INDENT + tv;
	s += generateConstraints(a);
      }
    }

    // needs-more-work: constructors

    Vector behs = cls.getBehavioralFeature();
    if (behs != null) {
      s += "\n";
      s += INDENT + "// Operations\n";
      java.util.Enumeration behEnum = behs.elements();
      String terminator1 = " {\n";
      String terminator2 = INDENT + "}";
      if (cls instanceof Interface) { terminator1 = ";\n"; terminator2 = ""; }
      while (behEnum.hasMoreElements()) {
	BehavioralFeature bf = (BehavioralFeature) behEnum.nextElement();
	s += INDENT + generate(bf) + terminator1;
	String tv = generateTaggedValues(bf);
	if (tv.length() > 0) s += INDENT + tv;
	s += generateConstraints(bf);
	if (!(cls instanceof Interface))
	  s += generateDefaultReturnStatement(bf.getReturnType());
	s += terminator2;
      }
    }
    s += "\n";
    s += "} /* end " + classifierKeyword + " " + generatedName + " */\n";
    return s;
  }

  public String generateDefaultReturnStatement(Classifier cls) {
    if (cls == null) return "";

    String clsName = cls.getName().getBody();
    if (clsName.equals("void")) return "";
    if (clsName.equals("char")) return INDENT + "return 'x';\n";
    if (clsName.equals("int")) return INDENT + "return 0;\n";
    if (clsName.equals("boolean")) return INDENT + "return false;\n";
    if (clsName.equals("byte")) return INDENT + "return 0;\n";
    if (clsName.equals("long")) return INDENT + "return 0;\n";
    if (clsName.equals("float")) return INDENT + "return 0.0;\n";
    if (clsName.equals("double")) return INDENT + "return 0.0;\n";
    return INDENT + "return null;\n";
  }

  public String generateStereotype(Stereotype s) {
    return "<<" + generateName(s.getName()) + ">>";
  }

  public String generateTaggedValues(Element e) {
    Vector tvs = e.getTaggedValue();
    if (tvs == null || tvs.size() == 0) return "";
    String s = "// {";
    int size = tvs.size();
    for (int i = 0; i < size; i++) {
      TaggedValue tv = (TaggedValue) tvs.elementAt(i);
      s += generateTaggedValue(tv);
      if (i < size-1) s += ", ";
    }
    s += "}\n";
    return s;
  }

  public String generateTaggedValue(TaggedValue tv) {
    if (tv == null) return "";
    return generateName(tv.getTag()) + "=" +
      generateUninterpreted(tv.getValue());
  }

  public String generateConstraints(ModelElement me) {
    Vector cs = me.getConstraint();
    if (cs == null || cs.size() == 0) return "";
    String s = INDENT + "// constraints\n";
    int size = cs.size();
    for (int i = 0; i < size; i++) {
      Constraint c = (Constraint) cs.elementAt(i);
      String constrStr = generateConstraint(c);
      StringTokenizer st = new StringTokenizer(constrStr, "\n\r");
      while (st.hasMoreElements()) {
	String constrLine = st.nextToken();
	s += INDENT + "// " + constrLine + "\n";
      }
    }
    s += "\n";
    return s;
  }

  public String generateConstraint(Constraint c) {
    if (c == null) return "";
    String s = "";
    if (c.getName() != null && c.getName().getBody().length() != 0)
      s += generateName(c.getName()) + ": ";
    s += generateExpression(c.getBody());
    return s;
  }


  public String generateAssociationFrom(IAssociation a, AssociationEnd ae) {
    // needs-more-work: does not handle n-ary associations
    String s = "";
    s += DocumentationManager.getDocs(a) + "\n" + INDENT;
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
    String s = INDENT + "protected ";
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

//   public String generateConstraints(ModelElement me) {
//     Vector constr = me.getConstraint();
//     if (constr == null || constr.size() == 0) return "";
//     String s = "{";
//     java.util.Enumeration conEnum = constr.elements();
//     while (conEnum.hasMoreElements()) {
//       s += generateConstraint((Constraint)conEnum.nextElement());
//       if (conEnum.hasMoreElements()) s += "; ";
//     }
//     s += "}";
//     return s;
//   }


//   public String generateConstraint(Constraint c) {
//     return generateExpression(c.getBody());
//   }

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

  public String generateSpecification(Vector realizations) {
    if (realizations == null) return "";
    String s = "";
    java.util.Enumeration clsEnum = realizations.elements();
    while (clsEnum.hasMoreElements()) {
      Realization r = (Realization)clsEnum.nextElement();
      s += generateClassifierRef(r.getSupertype());
      System.out.println("sup=" + r.getSupertype());
      if (clsEnum.hasMoreElements()) s += ", ";
    }
    return s;
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

  public String generateState(State m) {
    return m.getName().getBody();
  }

  public String generateStateBody(State m) {
    String s = "";
    ActionSequence entry = m.getEntry();
    ActionSequence exit = m.getExit();
    if (entry != null) {
      String entryStr = Generate(entry);
      if (entryStr.length() > 0) s += "entry / " + entryStr;
    }
    if (exit != null) {
      String exitStr = Generate(exit);
      if (s.length() > 0) s += "\n";
      if (exitStr.length() > 0) s += "exit / " + exitStr;
    }
    Vector trans = m.getInternalTransition();
    if (trans != null) {
      int size = trans.size();
      for (int i = 0; i < size; i++) {
	if (s.length() > 0) s += "\n";
	s += Generate(trans.elementAt(i));
      }
    }
    return s;
  }

  public String generateTransition(Transition m) {
    String s = m.getName().getBody();
    String t = generate(m.getTrigger());
    String g = generate(m.getGuard());
    String e = generate(m.getEffect());
    if (s.length() > 0 && (t.length() > 0 || g.length() > 0 || e.length() > 0))
      s += ": ";
    s += t;
    if (g.length() > 0) s += " [" + g + "]";
    if (e.length() > 0) s += " / " + e;
    return s;
  }

  public String generateAction(MMAction m) {
    return m.getName().getBody();
  }

  public String generateGuard(Guard m) {
    return generateExpression(m.getExpression());
  }

} /* end class GeneratorJava */
