// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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


package org.argouml.language.csharp.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.argouml.application.api.Argo;
import org.argouml.application.api.Notation;
import org.argouml.application.api.PluggableNotation;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlHelper;
import org.argouml.uml.DocumentationManager;
import org.argouml.uml.generator.FileGenerator;
import org.argouml.uml.generator.Generator;

import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MMethod;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MMultiplicityRange;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MPackage;

/** Generator subclass to generate text for display in diagrams in in
 * text fields in the Argo/UML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for outher languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced. */

// TODO: always check for null!!!

public class GeneratorCSharp extends Generator
    implements PluggableNotation, FileGenerator {
    public static boolean VERBOSE = false;
    public static boolean hasNamespace = false;

    private static Section sect;

    private static GeneratorCSharp SINGLETON = new GeneratorCSharp();

    public static GeneratorCSharp getInstance() { return SINGLETON; }

    public GeneratorCSharp() {
	super(Notation.makeNotation("CSharp", null,
				    Argo.lookupIconResource("CSharpNotation")));
    }

    public static String Generate(Object o) {
	return SINGLETON.generate(o);
    }

    /** Generates a file for the classifier.
     * This method could have been static if it where not for the need to
     * call it through the Generatorinterface.
     * @return the full path name of the the generated file.
     */
    public String GenerateFile(Object o, String path) {
	MClassifier cls = (MClassifier) o;
	sect = new Section();

	String name = cls.getName();
	if (name == null || name.length() == 0) return null;
	String filename = name + ".cs";
	if (!path.endsWith(FILE_SEPARATOR)) path += FILE_SEPARATOR;

	String packagePath = cls.getNamespace().getName();
	MNamespace parent = cls.getNamespace().getNamespace();
	while (parent != null) {
	    // ommit root package name; it's the model's root
	    if (parent.getNamespace() != null)
		packagePath = parent.getName() + "." + packagePath;
	    parent = parent.getNamespace();
	}

	int lastIndex = -1;
	do {
	    File f = new File(path);
	    if (!f.isDirectory()) {
		if (!f.mkdir()) {
		    System.out.println(" could not make directory " + path);
		    return null;
		}
	    }
	    if (lastIndex == packagePath.length())
		break;
	    int index = packagePath.indexOf(".", lastIndex + 1);
	    if (index == -1)
		index = packagePath.length();
	    path += packagePath.substring(lastIndex + 1, index) + FILE_SEPARATOR;
	    lastIndex = index;
	} while (true);
	String pathname = path + filename;
	System.out.println("-----" + pathname + "-----");

	//String pathname = path + filename;
	// TODO: package, project basepath, tagged values to configure
	File f = new File(pathname);
	if (f.exists()) {
	    System.out.println("Generating (updated) " + f.getPath());
	    sect.read(pathname);
	} else {
	    System.out.println("Generating (new) " + f.getPath());
	}
	String header = SINGLETON.generateHeader(cls, pathname, packagePath);
	String src = SINGLETON.generate(cls);
	if (packagePath.length() > 0) src += "\n}";
	BufferedWriter fos = null;
	try {
	    fos = new BufferedWriter(new FileWriter(f));
	    fos.write(header);
	    fos.write(src);
	}
	catch (IOException exp) { }
	finally {
	    try { 
		if (fos != null) fos.close(); 
	    }
	    catch (IOException exp) {
		System.out.println("FAILED: " + f.getPath());
	    }
	}

	sect.write(pathname, INDENT);
	System.out.println("written: " + pathname);


	File f1 = new File(pathname + ".bak");
	if (f1.exists()) {
	    f1.delete();
	}

	File f2 = new File(pathname);
	if (f2.exists()) {
	    f2.renameTo(new File(pathname + ".bak"));
	}

	File f3 = new File(pathname + ".out");
	if (f3.exists()) {
	    f3.renameTo(new File(pathname));
	}

	System.out.println("----- end updating -----");
	return pathname;
    }



    public String generateHeader(MClassifier cls, String pathname, String packagePath) {
	String s = "";
	// TODO: add user-defined copyright
	s += "// FILE: " + pathname.replace('\\', '/') + "\n\n";

	hasNamespace = false;
	if (packagePath.length() > 0) {
	    s += "namespace " + packagePath + " {\n";
	    hasNamespace = true;
	}

	s += "\n";

	// check if the class has a base class
	String baseClass = generateGeneralzation(cls.getGeneralizations());

	// check if the class has dependencies
	//   {
	//       Collection col = cls.getAssociationEnds();
	//       if (col != null){
	//           Iterator itr = col.iterator();
	//           while (itr.hasNext()) {
	//               MAssociationEnd ae = (MAssociationEnd) itr.next();
	//               MAssociation a = ae.getAssociation();
	//               ae = ae.getOppositeEnd();
	//               if (ae.isNavigable()) {
	//                   MClassifier cls2 = ae.getType();
	//                   String name = cls2.getName();
	//                   String name2 = cls.getName();
	//                   if (name != name2){
	//                     s += "require_once \"" + name + ".php\";\n";
	//                   }
	//               }
	//           }
	//       }
	//   }

	//   {
	//       Collection col = cls.getClientDependencies();
	//       System.out.println("col: " + col);
	//       if (col != null){
	//           Iterator itr = col.iterator();
	//           while (itr.hasNext()) {
	//               MDependency dep = (MDependency) itr.next();
	//               Collection clients_col = dep.getSuppliers();
	//               Iterator itr2 = clients_col.iterator();
	//               while (itr2.hasNext()){
	//                   String name = ((MClassifier) itr2.next()).getName();
	//                   s += "require_once \"" + name + ".php\";\n";
	//               }
	//           }
	//       }
	//   }

	return s;
    }

    /**
     * <p>Generate code for an extension point.</p>
     *
     * <p>Provided to comply with the interface, but returns null
     *   since no code will be generated. This should prevent a source tab
     *   being shown.</p>
     *
     * @param ep  The extension point to generate for
     *
     * @return    The generated code string. Always empty in this
     *            implementation.
     */
    public String generateExtensionPoint (MExtensionPoint ep) {
	return null;
    }

    public String generateOperation(MOperation op, boolean documented) {

	String s = "";

	Object cls = op.getOwner();

	String nameStr = generateName(op.getName());
	String clsName = generateName(op.getOwner().getName());

	/*
	 * Replaced 2001-09-26 STEFFEN ZSCHALER
	 *
	 * Was
	 *
	 s += DocumentationManager.getDocs(op) + "\n" + INDENT;
	*/

	if (documented)
	    s += generateConstraintEnrichedDocComment (op) + "\n" + INDENT;

	//    s += "function ";

	if (!(cls instanceof MInterface)) {
	    s += generateAbstractness (op);
	    s += generateScope (op);
	    s += generateChangeability (op);
	    s += generateVisibility (op);
	}
	// pick out return type
	MParameter rp = UmlHelper.getHelper().getCore().getReturnParameter(op);
	if ( rp != null) {
	    MClassifier returnType = rp.getType();

	    if (returnType == null && !nameStr.equals(clsName)) {
		s += " void ";
	    }
	    else if (returnType != null) {
		s += " " + generateClassifierRef (returnType) + " ";
	    }
	}
	else {
	    //          removed since it was throwing exceptions and didn't seem to do
	    //                 much,  Curt Arnold 15 Jan 2001
	    //
	    //		if (nameStr.equals(clsName)) s += " "; // this is a constructor!
	}

	// name and params
	Vector params = new Vector (op.getParameters());
	params.remove (rp);

	s += nameStr + "(";

	if (params != null) {
	    boolean first = true;

	    for (int i = 0; i < params.size(); i++) {
		MParameter p = (MParameter) params.elementAt (i);

		if (!first) s += ", ";

		s += generateParameter (p);
		first = false;
	    }
	}

	s += ")";

	return s;

    }


    public String generateAttribute(MAttribute attr, boolean documented) {
	String s = "";

	String make_get = attr.getTaggedValue("get");
	String make_set = attr.getTaggedValue("set");

	boolean gen_accessor = false;

	gen_accessor = (((make_get != null) && (make_get.equals("true"))) ||
			((make_set != null) && (make_set.equals("true"))));

	/*
	 * Replaced 2001-09-26 STEFFEN ZSCHALER
	 *
	 * Was:
	 *
	 s += DocumentationManager.getDocs(attr) + "\n" + INDENT;
	*/

	if (documented)
	    s += generateConstraintEnrichedDocComment (attr) + "\n" + INDENT;
	if (gen_accessor)
	    s += " private ";
	else
	    s += generateVisibility(attr);

	s += generateScope(attr);
	s += generateChangability(attr);
	if (!MMultiplicity.M1_1.equals(attr.getMultiplicity())) {
	    String temp = generateMultiplicity(attr.getMultiplicity());
	    if (temp.length() > 0) {
		s += temp + " ";
	    }
	}

	MClassifier type = attr.getType();
	if (type != null) {
	    s += generateClassifierRef(type) + " ";
	}

	String slash = "";
	//    if (attr.containsStereotype(MStereotype.DERIVED)) slash = "/";
	String attr_name = attr.getName();
	MVisibilityKind vis = attr.getVisibility();

	if (MVisibilityKind.PUBLIC.equals(vis)) {
	    // use original attribute name, no change
	}
	if (MVisibilityKind.PRIVATE.equals(vis)) {
	    attr_name = attr.getOwner().getName() + "_" + attr_name;

	}
	if (MVisibilityKind.PROTECTED.equals(vis)) {
	    // use orignial name for the moment
	}

	if (gen_accessor) attr_name = "m_" + attr_name;

	s += slash + generateName(attr_name);
	MExpression init = attr.getInitialValue();
	if (init != null) {
	    String initStr = generateExpression(init).trim();
	    if (initStr.length() > 0)
		s += " = " + initStr;
	}

	s += ";\n";
	// s += generateConstraints(attr);  Removed 2001-09-26 STEFFEN ZSCHALER

	// look if get and set methods are needed (Marian Heddesheimer)

	//  if get and set are set, visibility is set to same as orig attribute
	//  and a variable with m_ prefix is created.


	if ( gen_accessor ) {

	    s += "\n";

	    s += INDENT + generateVisibility(attr) + " " + generateClassifierRef(type) + " " + attr.getName() + " {\n";

	    if ( (make_set != null) && (make_set.equals("true")) )
		s += INDENT + INDENT + "set { m_" + attr.getName() + " = value; } \n";

	    if ( (make_get != null) && (make_get.equals("true")) )
		s += INDENT + INDENT + "get { return m_" + attr.getName() + "; } \n";

	    s += INDENT + "}\n";
	}

	return s;
    }


    public String generateParameter(MParameter param) {
	String s = "";
	// TODO: qualifiers (e.g., const)
	// TODO: stereotypes...
	s +=  generateClassifierRef(param.getType()) + " ";
	if (
	    (param.getKind().getValue() == ru.novosoft.uml.foundation.data_types.MParameterDirectionKind._INOUT) ||
	    (param.getKind().getValue() == ru.novosoft.uml.foundation.data_types.MParameterDirectionKind._OUT)
	    ) {
	    // if OUT or INOUT, then pass by Reference
	    s += "ByRef ";
	}
	s += generateName(param.getName());

	// TODO: initial value

	//    MExpression default_val = param.getDefaultValue();
	//    if ( (default_val != null) && (default_val.getBody() != null) ) {
	//        s += " = " + default_val.getBody();
	//    }

	return s;
    }


    public String generatePackage(MPackage p) {
	String s = "";
	String packName = generateName(p.getName());
	s += "namespace " + packName + " {\n";
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
	if (cls instanceof MClass) classifierKeyword = "class";
	else if (cls instanceof MInterface) classifierKeyword = "interface";
	else return ""; // actors and use cases

	StringBuffer sb = new StringBuffer(80);
	sb.append(DocumentationManager.getComments(cls));  // Add the comments for this classifier first.

	/*
	 * Replaced 2001-09-26 STEFFEN ZSCHALER
	 *
	 * Was:
	 *
	 sb.append(DocumentationManager.getDocs(cls)).append("\n");
	*/
	sb.append (generateConstraintEnrichedDocComment (cls)).append ("\n");

	sb.append(generateVisibility(cls.getVisibility()));
	if (cls.isAbstract() && !(cls instanceof MInterface)) {
	    if (VERBOSE) sb.append("/* abstract */ ");
	}
	if (cls.isLeaf()) {
	    if (VERBOSE) sb.append("/* final */ ");
	}
	sb.append(classifierKeyword).append(" ").append(generatedName);
	String baseClass = generateGeneralzation(cls.getGeneralizations());
	String tv = null;
	if (!baseClass.equals("")) sb.append(' ').append(": ").append(baseClass);

	// nsuml: realizations!
	if (cls instanceof MClass) {
	    String interfaces = generateSpecification((MClass) cls);
	    if (!interfaces.equals("")) {
		if (baseClass.equals(""))
		    sb.append(": ");
		else
		    sb.append(", ");
		sb.append(interfaces);
	    }
	}
	sb.append("\n{");

	tv = generateTaggedValues(cls);
	if (tv != null && tv.length() > 0) sb.append(INDENT).append(tv);

	// sb.append(generateConstraints(cls)); Removed 2001-09-26 STEFFEN ZSCHALER


	// generate constructor (Marian Heddesheimer)
	String make_constructor = cls.getTaggedValue("constructor");
	if ( (make_constructor != null) && (make_constructor.equals("true")) ) {
	    sb.append(INDENT).append("function ").append(cls.getName()).append("() {\n");
	    sb.append(generateSection(cls));
	    sb.append(INDENT).append("}\n");
	}


	Collection strs = UmlHelper.getHelper().getCore().getAttributes(cls);
	if (strs != null) {
	    sb.append('\n');

	    if (cls instanceof MClass) sb.append(INDENT).append("// Attributes\n");
	    Iterator strEnum = strs.iterator();
	    while (strEnum.hasNext()) {
		MStructuralFeature sf = (MStructuralFeature) strEnum.next();
		sb.append('\n').append(INDENT).append(generate(sf));
		tv = generateTaggedValues(sf);
		if (tv != null && tv.length() > 0) sb.append(INDENT).append(tv).append('\n');
	    }
	}

	Collection ends = cls.getAssociationEnds();
	if (ends != null) {
	    sb.append('\n');
	    if (cls instanceof MClass) sb.append(INDENT).append("// Associations\n");
	    Iterator endEnum = ends.iterator();
	    while (endEnum.hasNext()) {
		MAssociationEnd ae = (MAssociationEnd) endEnum.next();
		MAssociation a = ae.getAssociation();
		sb.append('\n').append(INDENT).append(generateAssociationFrom(a, ae));
		tv = generateTaggedValues(a);
		if (tv != null && tv.length() > 0) sb.append(INDENT).append(tv);

		// sb.append(generateConstraints(a));  Removed 2001-09-26 STEFFEN ZSCHALER Why was this not in generateAssociationFrom ?
	    }
	}

	// TODO: constructors
	Collection behs = UmlHelper.getHelper().getCore().getOperations(cls);
	if (behs != null) {
	    sb.append ('\n');
	    sb.append (INDENT).append ("// Operations\n");

	    Iterator behEnum = behs.iterator();

	    while (behEnum.hasNext()) {
		MBehavioralFeature bf = (MBehavioralFeature) behEnum.next();

		sb.append ('\n').append (INDENT).append (generate (bf));

		tv = generateTaggedValues((MModelElement) bf);

		if ((cls instanceof MClass) &&
		    (bf instanceof MOperation) &&
		    (!((MOperation) bf).isAbstract())) {
		    sb.append ('\n').append (INDENT).append ("{\n");

		    if (tv.length() > 0) sb.append (INDENT).append (tv);

		    sb.append (generateMethodBody ((MOperation) bf)).append ('\n')
			.append (INDENT).append ("}\n");
		}
		else {
		    sb.append (";\n");
		    if (tv.length() > 0) sb.append (INDENT).append (tv).append ('\n');
		}
	    }
	}
	sb.append("} /* end ").append(classifierKeyword).append(' ').append(generatedName).append(" */\n");

	return sb.toString();
    }

    /**
     * Generate the body of a method associated with the given operation. This
     * assumes there's at most one method associated!
     *
     * If no method is associated with the operation, a default method body will
     * be generated.
     */
    public String generateMethodBody (MOperation op) {
	if (op != null) {
	    Collection methods = op.getMethods();
	    Iterator i = methods.iterator();
	    MMethod m = null;

	    // System.out.print(", op!=null, size="+methods.size());
	    return generateSection(op);
	    // return INDENT + INDENT + "/* method body for " + op.getName() + " */";
	    /*
	      while (i != null && i.hasNext()) {
	      //System.out.print(", i!= null");
	      m = (MMethod) i.next();

	      if (m != null) {
	      //System.out.println(", BODY of "+m.getName());
	      //System.out.println("|"+m.getBody().getBody()+"|");
	      if (m.getBody() != null)
	      return m.getBody().getBody();
	      else
	      return "";
	      }
	      }

	      // pick out return type
	      MParameter rp = MMUtil.SINGLETON.getReturnParameter (op);
	      if (rp != null) {
	      MClassifier returnType = rp.getType();
	      return generateDefaultReturnStatement (returnType);
	      }
	    */
	}

	return generateDefaultReturnStatement (null);
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

    public String generateTaggedValues(MModelElement e) {
	Collection tvs = e.getTaggedValues();
	if (tvs == null || tvs.size() == 0) return "";
	boolean first = true;
	StringBuffer buf = new StringBuffer();
	Iterator iter = tvs.iterator();
	String s = null;
	while (iter.hasNext()) {
	    s = generateTaggedValue((MTaggedValue) iter.next());
	    if (s != null && s.length() > 0) {
		if (first) {
		    /*
		     * Corrected 2001-09-26 STEFFEN ZSCHALER
		     *
		     * Was:
		     buf.append("// {");
		     *
		     * which caused problems with new lines characters in tagged values
		     * (e.g. comments...). The new version still has some problems with
		     * tagged values containing "*"+"/" as this closes the comment
		     * prematurely, but comments should be taken out of the tagged values
		     * list anyway...
		     */
		    buf.append("/* {");
		    first = false;
		} else {
		    buf.append(", ");
		}
		buf.append(s);
	    }
	}
	/*
	 * Corrected 2001-09-26 STEFFEN ZSCHALER
	 *
	 * Was:
	 if (!first) buf.append("}\n");
	 *
	 * which caused problems with new-lines in tagged values.
	 */
	if (!first) buf.append ("}*/\n");

	return buf.toString();
    }

    public String generateTaggedValue(MTaggedValue tv) {
	if (tv == null) return "";
	String s = generateUninterpreted(tv.getValue());
	if (s == null || s.length() == 0 || s.equals("/** */")) return "";
	return generateName(tv.getTag()) + "=" + s;
    }

    /**
     * Enhance/Create the doccomment for the given model element, including tags
     * for any OCL constraints connected to the model element. The tags generated
     * are suitable for use with the ocl injector which is part of the Dresden OCL
     * Toolkit and are in detail:
     *
     * &nbsp;@invariant for each invariant specified
     * &nbsp;@precondition for each precondition specified
     * &nbsp;@postcondition for each postcondition specified
     * &nbsp;@key-type specifying the class of the keys of a mapped association
     * &nbsp; Currently mapped associations are not supported yet...
     * &nbsp;@element-type specifying the class referenced in an association
     *
     * @since 2001-09-26 ArgoUML 0.9.3
     * @author Steffen Zschaler
     *
     * @param me the model element for which the documentation comment is needed
     * @param ae the association end which is represented by the model element
     * @return the documentation comment for the specified model element, either
     * enhanced or completely generated
     */
    public String generateConstraintEnrichedDocComment (MModelElement me,
							MAssociationEnd ae) {
	String sDocComment = generateConstraintEnrichedDocComment (me);

	MMultiplicity m = ae.getMultiplicity();
	if (!(MMultiplicity.M1_1.equals(m) || MMultiplicity.M0_1.equals (m))) {
	    // Multiplicity greater 1, that means we will generate some sort of
	    // collection, so we need to specify the element type tag

	    // Prepare doccomment
	    if (sDocComment != null) {
		// Just remove closing */
		sDocComment = sDocComment.substring (0, sDocComment.indexOf ("*/") + 1);
	    }
	    else {
		if (VERBOSE) {
		    sDocComment = INDENT + "/**\n" +
			INDENT + " * \n" +
			INDENT + " *";
		} else {
		    sDocComment = "";
		}
	    }

	    // Build doccomment
	    sDocComment += " @element-type " + ae.getType().getName();

	    sDocComment += "\n" +
		INDENT + " */";

	    return sDocComment;
	}
	else {
	    return ((sDocComment != null) ? (sDocComment) : (""));
	}
    }

    /**
     * Enhance/Create the doccomment for the given model element, including tags
     * for any OCL constraints connected to the model element. The tags generated
     * are suitable for use with the ocl injector which is part of the Dresden OCL
     * Toolkit and are in detail:
     *
     * &nbsp;@invariant for each invariant specified
     * &nbsp;@precondition for each precondition specified
     * &nbsp;@postcondition for each postcondition specified
     *
     * @since 2001-09-26 ArgoUML 0.9.3
     * @author Steffen Zschaler
     *
     * @param me the model element for which the documentation comment is needed
     * @return the documentation comment for the specified model element, either
     * enhanced or completely generated
     */
    public String generateConstraintEnrichedDocComment (MModelElement me) {
	// Retrieve any existing doccomment
	String sDocComment = DocumentationManager.getDocs (me, GeneratorCSharp.INDENT);

	if (sDocComment != null) {
	    // Fix Bug in documentation manager.defaultFor --> look for current INDENT
	    // and use it
	    for (int i = sDocComment.indexOf ('\n');
		 i >= 0 && i < sDocComment.length();
		 i = sDocComment.indexOf ('\n', i + 1)) {
		sDocComment = sDocComment.substring (0, i + 1) +
		    INDENT + sDocComment.substring (i + 1);
	    }
	}

	// Extract constraints
	Collection cConstraints = me.getConstraints();

	if (cConstraints.size() == 0) {
	    return (sDocComment != null) ? (sDocComment) : ("");
	}

	// Prepare doccomment
	if (sDocComment != null) {
	    // Just remove closing */
	    sDocComment = sDocComment.substring (0, sDocComment.indexOf ("*/") + 1);
	}
	else {
	    if (VERBOSE) {
		sDocComment = INDENT + "/**\n" +
		    INDENT + " * \n" +
		    INDENT + " *";
	    } else {
		sDocComment = "";
	    }
	}

	// Add each constraint

	class TagExtractor extends tudresden.ocl.parser.analysis.DepthFirstAdapter {
	    private LinkedList m_llsTags = new LinkedList();
	    private String m_sConstraintName;
	    private int m_nConstraintID = 0;

	    public TagExtractor (String sConstraintName) {
		super();

		m_sConstraintName = sConstraintName;
	    }

	    public Iterator getTags() {
		return m_llsTags.iterator();
	    }

	    public void caseAConstraintBody (tudresden.ocl.parser.node.AConstraintBody node) {
		// We don't care for anything below this node, so we do not use apply anymore.
		String sKind = (node.getStereotype() != null) ?
		    (node.getStereotype().toString()) :
		    (null);
		String sExpression = (node.getExpression() != null) ?
		    (node.getExpression().toString()) :
		    (null);
		String sName = (node.getName() != null) ?
		    (node.getName().getText()) :
		    (m_sConstraintName + "_" + (m_nConstraintID++));

		if ((sKind == null) ||
		    (sExpression == null)) {
		    return;
		}

		String sTag;
		if (sKind.equals ("inv ")) {
		    sTag = "@invariant ";
		}
		else if (sKind.equals ("post ")) {
		    sTag = "@post-condition ";
		}
		else if (sKind.equals ("pre ")) {
		    sTag = "@pre-condition ";
		}
		else {
		    return;
		}

		sTag += sName + ": " + sExpression;
		m_llsTags.addLast (sTag);
	    }
	}

	tudresden.ocl.check.types.ModelFacade mf = new org.argouml.ocl.ArgoFacade (me);
	for (Iterator i = cConstraints.iterator(); i.hasNext();) {
	    MConstraint mc = (MConstraint) i.next();

	    try {
		tudresden.ocl.OclTree otParsed =
		    tudresden.ocl.OclTree.createTree(mc.getBody().getBody(), 
						     mf);

		TagExtractor te = new TagExtractor (mc.getName());
		otParsed.apply (te);

		for (Iterator j = te.getTags(); j.hasNext();) {
		    sDocComment += " " + j.next() + "\n" + INDENT + " *";
		}
	    }
	    catch (java.io.IOException ioe) {
		// Nothing to be done, should not happen anyway ;-)
	    }
	}

	sDocComment += "/";

	return sDocComment;
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
	// TODO: does not handle n-ary associations
	String s = "";

	/*
	 * Moved into while loop 2001-09-26 STEFFEN ZSCHALER
	 *
	 * Was:
	 *
	 s += DocumentationManager.getDocs(a) + "\n" + INDENT;
	*/

	Collection connections = a.getConnections();
	Iterator connEnum = connections.iterator();
	while (connEnum.hasNext()) {
	    MAssociationEnd ae2 = (MAssociationEnd) connEnum.next();
	    if (ae2 != ae) {
		/**
		 * Added generation of doccomment 2001-09-26 STEFFEN ZSCHALER
		 *
		 */
		s += generateConstraintEnrichedDocComment (a, ae2) + "\n";

		s += generateAssociationEnd(ae2);
	    }
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

    public String generateAssociationRole(MAssociationRole m) {
	return "";
    }

    public String generateAssociationEnd(MAssociationEnd ae) {
	if (!ae.isNavigable()) return "";
	//String s = INDENT + "protected ";
	String s = INDENT;
	String temp_s = "";
	if (VERBOSE) temp_s += "/* public */ ";
	// must be public or generate public navigation method!

	if (MScopeKind.CLASSIFIER.equals(ae.getTargetScope())) {
	    if (VERBOSE) temp_s += "static ";
	}
	//     String n = ae.getName();
	//     if (n != null && !String.UNSPEC.equals(n)) s += generateName(n) + " ";
	//     if (ae.isNavigable()) s += "navigable ";
	//     if (ae.getIsOrdered()) s += "ordered ";
	MMultiplicity m = ae.getMultiplicity();
	if (MMultiplicity.M1_1.equals(m) || MMultiplicity.M0_1.equals(m)) {
	}
	if (VERBOSE) temp_s += "/*" + generateClassifierRef(ae.getType()) + "*/";
	else {
	    if (VERBOSE) temp_s += "/* Vector */ "; //generateMultiplicity(m) + " ";
	}

	if (temp_s.length() > 0) {
	    s += temp_s + " ";
	}

	String n = ae.getName();
	MAssociation asc = ae.getAssociation();
	String ascName = asc.getName();
	if (n != null  &&
	    n != null && n.length() > 0) {
	    s += "var $" + generateName(n);
	}
	else if (ascName != null  &&
		 ascName != null && ascName.length() > 0) {
	    s += "var $" + generateName(ascName);
	}
	else {
	    s += "var $my" + generateClassifierRef(ae.getType());
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
	String s = "";
	//s += cls.getName();
	Collection realizations = UmlHelper.getHelper().getCore().getRealizedInterfaces(cls);
	if (realizations == null) return "";
	Iterator clsEnum = realizations.iterator();
	while (clsEnum.hasNext()) {
	    MInterface i = (MInterface) clsEnum.next();
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
	    s += generateClassifierRef((MClassifier) clsEnum.next());
	    if (clsEnum.hasNext()) s += ", ";
	}
	return s;
    }

    public String generateVisibility(MVisibilityKind vis) {
	//if (vis == null) return "";
	if (MVisibilityKind.PUBLIC.equals(vis)) return "public ";
	if (MVisibilityKind.PRIVATE.equals(vis)) return  "private ";
	if (MVisibilityKind.PROTECTED.equals(vis)) return  "protected ";
	return "";
    }

    public String generateVisibility(MFeature f) {
	return generateVisibility(f.getVisibility());
    }

    public String generateScope(MFeature f) {
	MScopeKind scope = f.getOwnerScope();
	//if (scope == null) return "";
	if (MScopeKind.CLASSIFIER.equals(scope)) 
	    if (VERBOSE) 
		return "/* static */ ";
	    else return "";
	return "";
    }

    /**
     * Generate "abstract" keyword for abstract operations.
     */
    public String generateAbstractness (MOperation op) {
	if (op.isAbstract()) return "abstract ";
	return "";
    }

    /**
     * Generate "final" keyword for final operations.
     */
    public String generateChangeability (MOperation op) {
	if (op.isLeaf()) {
	    return " sealed ";
	}
	else {
	    return "";
	}
    }

    public String generateChangability(MStructuralFeature sf) {
	MChangeableKind ck = sf.getChangeability();
	//if (ck == null) return "";
	if (MChangeableKind.FROZEN.equals(ck)) return " sealed ";
	//if (MChangeableKind.ADDONLY.equals(ck)) return "final ";
	return "";
    }

    public String generateMultiplicity(MMultiplicity m) {
	if (m == null) { return ""; }
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
    // TODO: user preference between "*" and "0..*"

    public String generateMultiplicityRange(MMultiplicityRange mr) {
	Integer lower = new Integer(mr.getLower());
	Integer upper = new Integer(mr.getUpper());
	if (lower == null && upper == null) return ANY_RANGE;
	if (lower == null) return "*.." + upper.toString();
	if (upper == null) return lower.toString() + "..*";
	if (lower.intValue() == upper.intValue()) return lower.toString();
	return lower.toString() + ".." + upper.toString();

    }

    public String generateState(MState m) {
	return m.getName();
    }

    public String generateStateBody(MState m) {
	System.out.println("GeneratorCSharp: generating state body");
	String s = "";
	Object entry = ModelFacade.getEntry(m);
	Object exit = ModelFacade.getExit(m);
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
	    while (iter.hasNext())
	    {
		if (s.length() > 0) 
		    s += "\n";
		s += generateTransition((MTransition) iter.next());
	    }
	}

	/*   if (trans != null) {
	     int size = trans.size();
	     MTransition[] transarray = (MTransition[])trans.toArray();
	     for (int i = 0; i < size; i++) {
	     if (s.length() > 0) s += "\n";
	     s += Generate(transarray[i]);
	     }
	     }*/
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

	/*  String s = m.getName();
	    String t = generate(m.getTrigger());
	    String g = generate(m.getGuard());
	    String e = generate(m.getEffect());
	    if(s == null) s = "";
	    if(t == null) t = "";
	    if (s.length() > 0 &&
	    (t.length() > 0 ||
	    (g != null && g.length() > 0) ||
	    (e != null && e.length() > 0)))
	    s += ": ";
	    s += t;
	    if (g != null && g.length() > 0) s += " [" + g + "]";
	    if (e != null && e.length() > 0) s += " / " + e;
	    return s;*/
    }

    public String generateAction(Object m) {
	// return m.getName();
	Object script = ModelFacade.getScript(m);
	if ((script != null) && (ModelFacade.getBody(script) != null))
	    return ModelFacade.getBody(script).toString();
	return "";
    }

    public String generateGuard(MGuard m) {
	//return generateExpression(m.getExpression());
	if (m.getExpression() != null)
	    return generateExpression(m.getExpression());
	return "";
    }

    public String generateMessage(MMessage m) {
	if (m == null) return "";
	return generateName(m.getName()) + "::" +
	    generateAction(m.getAction());
    }


    /**
       Update a source code file.

       @param mClassifier The classifier to update from.
       @param file The file to update.
    */
    private static void update(MClassifier mClassifier,
			       File file)
	throws Exception
    {
	System.out.println("Parsing " + file.getPath());

	// read the existing file and store preserved sections

	/*
	  JavaLexer lexer =
	  new JavaLexer(new BufferedReader(new FileReader(file)));
	  JavaRecognizer parser = new JavaRecognizer(lexer);
	  CodePieceCollector cpc = new CodePieceCollector();
	  parser.compilationUnit(cpc);
	*/


	File origFile = new File(file.getAbsolutePath());
	File newFile = new File(file.getAbsolutePath() + ".updated");
	System.out.println("Generating " + newFile.getPath());

	boolean eof = false;
	BufferedReader origFileReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
	FileWriter newFileWriter = new FileWriter(file.getAbsolutePath() + ".updated");
	while (!eof) {
	    String line = origFileReader.readLine();
	    if (line == null) {
		eof = true;
	    } else {
		newFileWriter.write(line + "\n");
	    }
	}
	newFileWriter.close();
	origFileReader.close();

	// cpc.filter(file, newFile, mClassifier.getNamespace());
	System.out.println("Backing up " + file.getPath());
	file.renameTo(new File(file.getAbsolutePath() + ".backup"));
	System.out.println("Updating " + file.getPath());
	newFile.renameTo(origFile);
    }

    public String generateSection(MOperation op) {
	String id = op.getUUID();
	if (id == null) {
	    id = (new UID().toString());
	    // id =  op.getName() + "__" + static_count;
	    op.setUUID(id);
        }
	// String s = "";
	// s += INDENT + "// section " + id + " begin\n";
	// s += INDENT + "// section " + id + " end\n";
	return Section.generate(id, INDENT);
    }

    public String generateSection(MClassifier cls) {
	String id = cls.getUUID();
	if (id == null) {
	    id = (new UID().toString());
	    // id = cls.getName() + "__" + static_count;
	    cls.setUUID(id);
        }
	// String s = "";
	// s += INDENT + "// section " + id + " begin\n";
	// s += INDENT + "// section " + id + " end\n";
	return Section.generate(id, INDENT);
    }
    public boolean canParse() {
	return true;
    }

    public boolean canParse(Object o) {
	return true;
    }


    public String getModuleName() { return "GeneratorCSharp"; }
    public String getModuleDescription() {
	return "CSharp Notation and Code Generator";
    }
    public String getModuleAuthor() { return "Mike Lipkie"; }
    public String getModuleVersion() { return "0.1.0"; }
    public String getModuleKey() { return "module.language.csharp.generator"; }


} /* end class GeneratorCSharp */

