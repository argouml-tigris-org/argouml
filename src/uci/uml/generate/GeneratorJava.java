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

//import java.util.*;
import java.io.*;

import java.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.model_management.*;
import uci.uml.ui.DocumentationManager;

import uci.uml.util.MMUtil;

/** Generator subclass to generate text for display in diagrams in in
 * text fields in the Argo/UML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for outher languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced. */

// needs-more-work: always check for null!!!

public class GeneratorJava extends Generator {

  public static GeneratorJava SINGLETON = null; //new GeneratorJava();

  static {
      // Read Generator class from Property "argo.generator":
      String generatorClass=System.getProperty("argo.generator");
      if (generatorClass!=null) {
	  try {
	      SINGLETON=(GeneratorJava)Class.forName(generatorClass).newInstance();
	      System.out.println("Using Generator "+generatorClass+".");
	  } catch (Exception e) {
	      System.out.println("Generator "+generatorClass+" not found, using GeneratorJava.");
	  }
      }
      if (SINGLETON==null)
	  SINGLETON= new GeneratorJava();
  }

  public final static String fileSep=System.getProperty("file.separator");

  /** Two spaces used for indenting code in classes. */
  public static String INDENT = "  ";

  public static String Generate(Object o) {
    return SINGLETON.generate(o);
  }

  public static String GenerateFile(MClassifier cls, String path) {
    // GenerateFile now returns the full path name of the
    // the generated file. 
    String name = cls.getName();
    if (name == null || name.length() == 0) return null;
    String filename = name + ".java";
    if (!path.endsWith(fileSep)) path += fileSep;

    String packagePath = cls.getNamespace().getName();
    MNamespace parent = cls.getNamespace().getNamespace();
    while (parent != null) {
      packagePath = parent.getName() + "." + packagePath;
      parent = parent.getNamespace();
    }

	int lastIndex=-1;
    do {
      File f = new File(path);
      if (!f.isDirectory()) {
		  if (!f.mkdir()) {
			  System.out.println(" could not make directory "+path);
			  return null;
		  }
      }
	  if (lastIndex == packagePath.length())
		  break;
	  int index = packagePath.indexOf(".", lastIndex+1);
	  if (index == -1)
		  index = packagePath.length();
      path += packagePath.substring(lastIndex+1, index) + fileSep;
	  lastIndex = index;
	} while (true);
    String pathname = path + filename;
    //String pathname = path + filename;
    // needs-more-work: package, project basepath, tagged values to configure
    String header = SINGLETON.generateHeader(cls, pathname, packagePath);
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
    return pathname;
  }

  public String generateHeader(MClassifier cls, String pathname, String packagePath) {
    String s = "";
    //needs-more-work: add user-defined copyright
    s += "// FILE: " + pathname.replace('\\','/') +"\n\n";
    if (packagePath.length() > 0) s += "package " + packagePath + ";\n";
    s += "import java.util.*;\n";

    s += "\n";
    return s;
  }

  public String generateOperation(MOperation op) {
    String s = "";
    String nameStr = generateName(op.getName());
    String clsName = generateName(op.getOwner().getName());

    s += DocumentationManager.getDocs(op) + "\n" + INDENT;
    s += generateVisibility(op);
    s += generateScope(op);

    // pick out return type
    MParameter rp = MMUtil.SINGLETON.getReturnParameter(op);
	if ( rp != null) {
		MClassifier returnType = rp.getType();
		if (returnType == null && !nameStr.equals(clsName)) s += "void ";
		else if (returnType != null) s += generateClassifierRef(returnType) + " ";
	} else {
		if (nameStr.equals(clsName)) s += " "; // this is a constructor!
	}
		

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
    return s;
  }

  public String generateAttribute(MAttribute attr) {
    String s = "";
    s += DocumentationManager.getDocs(attr) + "\n" + INDENT;
    s += generateVisibility(attr);
    s += generateScope(attr);
    s += generateChangability(attr);
    if (!MMultiplicity.M1_1.equals(attr.getMultiplicity()))
      s += generateMultiplicity(attr.getMultiplicity()) + " ";

    MClassifier type = attr.getType();
    if (type != null) s += generateClassifierRef(type) + " ";

    String slash = "";
//    if (attr.containsStereotype(MStereotype.DERIVED)) slash = "/";

    s += slash + generateName(attr.getName());
    MExpression init = attr.getInitialValue();
    if (init != null) {
      String initStr = generateExpression(init).trim();
      if (initStr.length() > 0)
	s += " = " + initStr;
    }

    s += ";\n";
    s += generateConstraints(attr);

    return s;
  }


  public String generateParameter(MParameter param) {
    String s = "";
    //needs-more-work: qualifiers (e.g., const)
    //needs-more-work: stereotypes...
    s += generateClassifierRef(param.getType()) + " ";
    s += generateName(param.getName());
    //needs-more-work: initial value
    return s;
  }


  public String generatePackage(MPackage p) {
    String s = "";
    String packName = generateName(p.getName());
    s += "package " + packName + " {\n";
    Collection ownedElements = p.getOwnedElements();
    if (ownedElements != null) {
      Iterator ownedEnum = ownedElements.iterator();
      while (ownedEnum.hasNext()) {
	MModelElement me = (MModelElement) ownedEnum.next();
	s += generate(me);
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
    s += DocumentationManager.getDocs(cls) + "\n";
    s += generateVisibility(cls.getVisibility());
    if (cls.isAbstract() && !(cls instanceof MInterface)) s += "abstract ";
    if (cls.isLeaf()) s += "final ";
    s += classifierKeyword + " " + generatedName + " ";
    String baseClass = generateGeneralzation(cls.getGeneralizations());
    if (!baseClass.equals("")) s += "extends " + baseClass + " ";

    // nsuml: realizations!
	if (cls instanceof MClass) {
		String interfaces = generateSpecification((MClass)cls);
		if (!interfaces.equals("")) s += "implements " + interfaces + " ";
	}
	s += "{\n";

    s += INDENT + generateTaggedValues(cls);
    s += generateConstraints(cls);

    Collection strs = MMUtil.SINGLETON.getAttributes(cls);
    if (strs != null) {
      s += "\n";

      if (cls instanceof MClassImpl) s += INDENT + "// Attributes\n";
      Iterator strEnum = strs.iterator();
      while (strEnum.hasNext()) {
	MStructuralFeature sf = (MStructuralFeature) strEnum.next();
	s += INDENT + generate(sf);
	String tv = generateTaggedValues(sf);
	if (tv.length() > 0) s += INDENT + tv;
      }
    }

    Collection ends = cls.getAssociationEnds();
    if (ends != null) {
      s += "\n";
      if (cls instanceof MClassImpl) s += INDENT + "// Associations\n";
      Iterator endEnum = ends.iterator();
      while (endEnum.hasNext()) {
	MAssociationEnd ae = (MAssociationEnd) endEnum.next();
	MAssociation a = ae.getAssociation();
	s += INDENT + generateAssociationFrom(a, ae);
	String tv = generateTaggedValues(a);
	if (tv.length() > 0) s += INDENT + tv;
	s += generateConstraints(a);
      }
    }

    // needs-more-work: constructors

    Collection behs = MMUtil.SINGLETON.getOperations(cls);
    if (behs != null) {
      s += "\n";
      s += INDENT + "// Operations\n";
      Iterator behEnum = behs.iterator();
      String terminator1 = " {\n";
      String terminator2 = INDENT + "}";
      if (cls instanceof MInterface) { terminator1 = ";\n"; terminator2 = ""; }
      while (behEnum.hasNext()) {
	MBehavioralFeature bf = (MBehavioralFeature) behEnum.next();
	s += INDENT + generate(bf) + terminator1;
	String tv = generateTaggedValues((MModelElement)bf);
	if (tv.length() > 0) s += INDENT + tv;
	s += generateConstraints((MModelElement)bf);
	
	// there is no ReturnType in behavioral feature (nsuml)
	if (cls instanceof MClassImpl && bf instanceof MOperation) {
	    s += generateMethodBody((MOperation)bf);
	}
	s += terminator2 + "\n";
      }
    }
    s += "\n";
    s += "} /* end " + classifierKeyword + " " + generatedName + " */\n";
    return s;
  }

  public String generateMethodBody(MOperation op) {
    // pick out return type
    MParameter rp = MMUtil.SINGLETON.getReturnParameter(op);
    MClassifier returnType = rp.getType();
      return generateDefaultReturnStatement(returnType);
  }

  public String generateDefaultReturnStatement(MClassifier cls) {
    if (cls == null) return "";

    String clsName = cls.getName();
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

  public String generateStereotype(MStereotype s) {
    return "<<" + generateName(s.getName()) + ">>";
  }

  public String generateTaggedValues(MModelElement e) {
    Collection tvs = e.getTaggedValues();
    if (tvs == null || tvs.size() == 0) return "";
    String s = "// {";
    int size = tvs.size();
    MTaggedValue[] tvsarray = (MTaggedValue[])tvs.toArray();
    for (int i = 0; i < size; i++) {
      MTaggedValue tv = (MTaggedValue) tvsarray[i];
      s += generateTaggedValue(tv);
      if (i < size-1) s += ", ";
    }
    s += "}\n";
    return s;
  }

  public String generateTaggedValue(MTaggedValue tv) {
    if (tv == null) return "";
    return generateName(tv.getTag()) + "=" +
      generateUninterpreted(tv.getValue());
  }

  public String generateConstraints(MModelElement me) {

    // This method just adds comments to the generated java code. This should be code generated by ocl-argo int he future?
    Collection cs = me.getConstraints();
    if (cs == null || cs.size() == 0) return "";
    String s = INDENT + "// constraints\n";
    int size = cs.size();
    // MConstraint[] csarray = (MConstraint[])cs.toArray();
    // System.out.println("Got " + csarray.size() + " constraints.");
    for (Iterator i = cs.iterator(); i.hasNext();) {
      MConstraint c = (MConstraint) i.next();
      String constrStr = generateConstraint(c);
      java.util.StringTokenizer st = new java.util.StringTokenizer(constrStr, "\n\r");
      while (st.hasMoreElements()) {
	String constrLine = st.nextToken();
	s += INDENT + "// " + constrLine + "\n";
      }
    }
    s += "\n";
    return s;
  }

  public String generateConstraint(MConstraint c) {
    if (c == null) return "";
    String s = "";
    if (c.getName() != null && c.getName().length() != 0)
      s += generateName(c.getName()) + ": ";
    s += generateExpression(c);
    return s;
  }


  public String generateAssociationFrom(MAssociation a, MAssociationEnd ae) {
    // needs-more-work: does not handle n-ary associations
    String s = "";
    s += DocumentationManager.getDocs(a) + "\n" + INDENT;
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
    //String s = INDENT + "protected ";
    String s = INDENT + "public ";
    // must be public or generate public navigation method!

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

//   public String generateConstraints(MModelElement me) {
//     Vector constr = me.getConstraint();
//     if (constr == null || constr.size() == 0) return "";
//     String s = "{";
//     Iterator conEnum = constr.iterator();
//     while (conEnum.hasNext()) {
//       s += generateConstraint((MConstraint)conEnum.next());
//       if (conEnum.hasNext()) s += "; ";
//     }
//     s += "}";
//     return s;
//   }


//   public String generateConstraint(MConstraint c) {
//     return generateExpression(c);
//   }

  ////////////////////////////////////////////////////////////////
  // internal methods?


  public String generateGeneralzation(Collection generalizations) {
    if (generalizations == null) return "";
    Collection classes = new ArrayList();
    Iterator enum = generalizations.iterator();
    while (enum.hasNext()) {
      MGeneralization g = (MGeneralization) enum.next();
      MGeneralizableElement ge = g.getParent();
      // assert ge != null
      if (ge != null) classes.add(ge);
    }
    return generateClassList(classes);
  }

    //  public String generateSpecification(Collection realizations) {
	public String generateSpecification(MClass cls) {
		Collection realizations = MMUtil.SINGLETON.getSpecifications(cls);
		if (realizations == null) return "";
		String s = "";
		Iterator clsEnum = realizations.iterator();
		while (clsEnum.hasNext()) {
			MInterface i = (MInterface)clsEnum.next();
			s += generateClassifierRef(i);
			if (clsEnum.hasNext()) s += ", ";
		}
		return s;
	}
	
	public String generateClassList(Collection classifiers) {
		String s = "";
		if (classifiers == null) return "";
		Iterator clsEnum = classifiers.iterator();
		while (clsEnum.hasNext()) {
			s += generateClassifierRef((MClassifier)clsEnum.next());
			if (clsEnum.hasNext()) s += ", ";
		}
		return s;
	}

  public String generateVisibility(MVisibilityKind vis) {
    //if (vis == null) return "";
    if (MVisibilityKind.PUBLIC.equals(vis)) return "public ";
    if (MVisibilityKind.PRIVATE.equals(vis)) return "private ";
    if (MVisibilityKind.PROTECTED.equals(vis)) return "protected ";
    return "";
  }

  public String generateVisibility(MFeature f) {
    MVisibilityKind vis = f.getVisibility();
    //if (vis == null) return "";
    if (MVisibilityKind.PUBLIC.equals(vis)) return "public ";
    if (MVisibilityKind.PRIVATE.equals(vis)) return "private ";
    if (MVisibilityKind.PROTECTED.equals(vis)) return "protected ";
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
    if (m == null) { System.out.println("null Multiplicity"); return ""; }
    if (MMultiplicity.M0_N.equals(m)) return ANY_RANGE;
    String s = "";
    Collection v = m.getRanges();
    if (v == null) return s;
    Iterator rangeEnum = v.iterator();
    while (rangeEnum.hasNext()) {
      MMultiplicityRange mr = (MMultiplicityRange) rangeEnum.next();
      s += generateMultiplicityRange(mr);
      if (rangeEnum.hasNext()) s += ",";
    }
    return s;
  }


  public static final String ANY_RANGE = "0..*";
  //public static final String ANY_RANGE = "*";
  // needs-more-work: user preference between "*" and "0..*"

  public String generateMultiplicityRange(MMultiplicityRange mr) {
    Integer lower = new Integer(mr.getLower());
    Integer upper = new Integer(mr.getUpper());
    if (lower == null && upper == null) return ANY_RANGE;
    if (lower == null) return "*.."+ upper.toString();
    if (upper == null) return lower.toString() + "..*";
    if (lower.intValue() == upper.intValue()) return lower.toString();
    return lower.toString() + ".." + upper.toString();

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
      int size = trans.size();
	  MTransition[] transarray = (MTransition[])trans.toArray();
      for (int i = 0; i < size; i++) {
		if (s.length() > 0) s += "\n";
		s += Generate(transarray[i]);
      }
    }
    return s;
  }

  public String generateTransition(MTransition m) {
    String s = m.getName();
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

  public String generateAction(MAction m) {
    return m.getName();
  }

  public String generateGuard(MGuard m) {
    return generateExpression(m.getExpression());
  }

} /* end class GeneratorJava */
