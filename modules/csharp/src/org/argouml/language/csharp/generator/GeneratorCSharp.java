// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Notation;
import org.argouml.application.api.PluggableNotation;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.uml.DocumentationManager;
import org.argouml.uml.UUIDHelper;
import org.argouml.uml.generator.FileGenerator;
import org.argouml.uml.generator.Generator2;

import tudresden.ocl.parser.node.AConstraintBody;

/**
 * Generator2 subclass to generate text for display in diagrams in in
 * text fields in the ArgoUML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for outher languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced.
 */
public class GeneratorCSharp extends Generator2
    implements PluggableNotation, FileGenerator {
    private static final boolean VERBOSE = false;
    private static final String LINE_SEPARATOR =
	System.getProperty("line.separator");

    private static Section sect;

    private static final Logger LOG = Logger.getLogger(GeneratorCSharp.class);

    /**
     * The singleton.
     */
    private static final GeneratorCSharp INSTANCE = new GeneratorCSharp();

    /**
     * Get this object.
     *
     * @return The one and only instance.
     */
    public static GeneratorCSharp getInstance() {
        return INSTANCE;
    }

    /**
     * Constructor.
     */
    public GeneratorCSharp() {
	super(Notation.makeNotation("CSharp", null,
				    Argo.lookupIconResource("CSharpNotation")));
    }

    /**
     * Generate the CSharp representation for an object.
     *
     * @param o The object.
     * @return The String representation.
     * @deprecated by Linus Tolke as of 0.17.1. Use {link #generate(Object)}.
     */
    public static String cSharpGenerate(Object o) {
	return INSTANCE.generate(o);
    }

    /**
     * Generates a file for the classifier.
     * This method could have been static if it where not for the need to
     * call it through the Generatorinterface.
     * Returns the full path name of the the generated file.
     *
     * @see org.argouml.uml.generator.FileGenerator#generateFile2(
     * java.lang.Object, java.lang.String)
     */
    public String generateFile2(Object cls, String path) {
	sect = new Section();

	String name = ModelFacade.getName(cls);
	if (name == null || name.length() == 0)  {
	    return null;
	}
	String filename = name + ".cs";
	if (!path.endsWith(FILE_SEPARATOR)) {
	    path += FILE_SEPARATOR;
	}

        String packagePath = "";
        Object parent = ModelFacade.getNamespace(ModelFacade.getNamespace(cls));
        if (parent != null) {
            packagePath = ModelFacade.getName(ModelFacade.getNamespace(cls));
        }
	while (parent != null) {
	    // ommit root package name; it's the model's root
	    if (ModelFacade.getNamespace(parent) != null) {
	        packagePath = ModelFacade.getName(parent) + "." + packagePath;
	    }
	    parent = ModelFacade.getNamespace(parent);
	}

	int lastIndex = -1;
	do {
	    File f = new File(path);
	    if (!f.isDirectory()) {
		if (!f.mkdir()) {
		    LOG.debug(" could not make directory " + path);
		    return null;
		}
	    }
	    if (lastIndex == packagePath.length()) {
	        break;
	    }
	    int index = packagePath.indexOf(".", lastIndex + 1);
	    if (index == -1) {
	        index = packagePath.length();
	    }
	    path +=
	        packagePath.substring(lastIndex + 1, index) + FILE_SEPARATOR;
	    lastIndex = index;
	} while (true);
	String pathname = path + filename;
	LOG.debug("-----" + pathname + "-----");

	//String pathname = path + filename;
	// TODO: package, project basepath, tagged values to configure
	File f = new File(pathname);
	if (f.exists()) {
	    LOG.debug("Generating (updated) " + f.getPath());
	    sect.read(pathname);
	} else {
	    LOG.debug("Generating (new) " + f.getPath());
	}
	String header = INSTANCE.generateHeader(cls, pathname, packagePath);
	String src = INSTANCE.generate(cls);
	if (packagePath.length() > 0) {
	    src += "\n}";
	}
	BufferedWriter fos = null;
	try {
	    fos = new BufferedWriter(new FileWriter(f));
	    fos.write(header);
	    fos.write(src);
	} catch (IOException exp) {
	    // IO Problem.
	} finally {
	    try {
		if (fos != null) {
		    fos.close();
		}
	    } catch (IOException exp) {
		LOG.debug("FAILED: " + f.getPath());
	    }
	}

	sect.write(pathname, INDENT);
	LOG.debug("written: " + pathname);


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

	LOG.debug("----- end updating -----");
	return pathname;
    }



    private String generateHeader(
            Object cls,
            String pathname,
            String packagePath) {
	String s = "";
	// TODO: add user-defined copyright
	s += "// FILE: " + pathname.replace('\\', '/') + "\n\n";

        s += generateImports(cls, packagePath);

	if (packagePath.length() > 0) {
	    s += "namespace " + packagePath + " {\n";
	}

	s += "\n";

	// check if the class has a base class
	// String baseClass = generateGeneralzation(cls.getGeneralizations());

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
	//       LOG.debug("col: " + col);
	//       if (col != null){
	//           Iterator itr = col.iterator();
	//           while (itr.hasNext()) {
	//               MDependency dep = (MDependency) itr.next();
	//               Collection clients_col = dep.getSuppliers();
	//               Iterator itr2 = clients_col.iterator();
	//               while (itr2.hasNext()){
	//                   String name =
	//                       ((MClassifier) itr2.next()).getName();
	//                   s += "require_once \"" + name + ".php\";\n";
	//               }
	//           }
	//       }
	//   }

	return s;
    }

    /**
     * Generate code for an extension point.<p>
     *
     * Provided to comply with the interface, but returns null
     * since no code will be generated. This should prevent a source tab
     * being shown.<p>
     *
     * @param ep  The extension point to generate for
     *
     * @return    The generated code string. Always empty in this
     *            implementation.
     */
    public String generateExtensionPoint(Object ep) {
	return null;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateObjectFlowState(java.lang.Object)
     */
    public String generateObjectFlowState(Object m) {
        Object c = ModelFacade.getType(m);
        if (c == null) return "";
        return ModelFacade.getName(c);
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateOperation(
     *         java.lang.Object, boolean)
     */
    public String generateOperation(Object op, boolean documented) {

	String s = "";

	Object cls = ModelFacade.getOwner(op);

	String nameStr = generateName(ModelFacade.getName(op));
	String clsName =
	    generateName(ModelFacade.getName(ModelFacade.getOwner(op)));

	/*
	 * Replaced 2001-09-26 STEFFEN ZSCHALER
	 *
	 * Was
	 *
	 s += DocumentationManager.getDocs(op) + "\n" + INDENT;
	*/

	if (documented) {
	    s += generateConstraintEnrichedDocComment(op) + "\n" + INDENT;
	}

	//    s += "function ";

	if (!(ModelFacade.isAInterface(cls))) {
	    s += generateAbstractness (op);
	    s += generateScope (op);
	    s += generateChangeability (op);
	    s += generateVisibility (op);
	}
	// pick out return type
	Object rp = Model.getUmlHelper().getCore().getReturnParameter(op);
	if (rp != null) {
	    Object returnType = ModelFacade.getType(rp);

	    if (returnType == null && !nameStr.equals(clsName)) {
		s += " void ";
	    } else if (returnType != null) {
		s += " " + generateClassifierRef (returnType) + " ";
	    }
	}

	// name and params
	Vector params = new Vector(ModelFacade.getParameters(op));
	params.remove (rp);

	s += nameStr + "(";

	if (params != null) {
	    boolean first = true;

	    for (int i = 0; i < params.size(); i++) {
		Object p = params.elementAt (i);

		if (!first) {
		    s += ", ";
		}

		s += generateParameter(p);
		first = false;
	    }
	}

	s += ")";

	return s;
    }


    /**
     * @see org.argouml.application.api.NotationProvider2#generateAttribute(
     *         java.lang.Object, boolean)
     */
    public String generateAttribute(Object attr, boolean documented) {
	String s = "";

	String makeGet = ModelFacade.getTaggedValueValue(attr, "get");
	String makeSet = ModelFacade.getTaggedValueValue(attr, "set");

	boolean genAccessor = false;

	genAccessor = (((makeGet != null) && (makeGet.equals("true")))
	        || ((makeSet != null) && (makeSet.equals("true"))));

	/*
	 * Replaced 2001-09-26 STEFFEN ZSCHALER
	 *
	 * Was:
	 *
	 s += DocumentationManager.getDocs(attr) + "\n" + INDENT;
	*/

	if (documented) {
	    s += generateConstraintEnrichedDocComment (attr) + "\n" + INDENT;
	}
	if (genAccessor) {
	    s += " private ";
	} else {
	    s += generateVisibility(attr);
	}

	s += generateScope(attr);
	s += generateChangability(attr);
	if (!ModelFacade.M1_1_MULTIPLICITY.equals(
	        ModelFacade.getMultiplicity(attr))) {
	    String temp =
	        generateMultiplicity(ModelFacade.getMultiplicity(attr));
	    if (temp.length() > 0) {
		s += temp + " ";
	    }
	}

	Object type = ModelFacade.getType(attr);
	if (type != null) {
	    s += generateClassifierRef(type) + " ";
	}

	String slash = "";
	//    if (attr.containsStereotype(MStereotype.DERIVED)) slash = "/";
	String attrName = ModelFacade.getName(attr);
	Object vis = ModelFacade.getVisibility(attr);

	// if (ModelFacade.PUBLIC_VISIBILITYKIND.equals(vis)) {
	    // use original attribute name, no change
	// }
	if (ModelFacade.PRIVATE_VISIBILITYKIND.equals(vis)) {
	    attrName =
	        ModelFacade.getName(ModelFacade.getOwner(attr))
	        + "_" + attrName;
	}
	// if (ModelFacade.PROTECTED_VISIBILITYKIND.equals(vis)) {
	    // use orignial name for the moment
	// }

	if (genAccessor) {
	    attrName = "m_" + attrName;
	}

	s += slash + generateName(attrName);
	Object init = ModelFacade.getInitialValue(attr);
	if (init != null) {
	    String initStr = generateExpression(init).trim();
	    if (initStr.length() > 0) {
	        s += " = " + initStr;
	    }
	}

	s += ";\n";
	// s += generateConstraints(attr);  Removed 2001-09-26 STEFFEN ZSCHALER

	// look if get and set methods are needed (Marian Heddesheimer)

	//  if get and set are set, visibility is set to same as orig attribute
	//  and a variable with m_ prefix is created.


	if (genAccessor) {

	    s += "\n";

	    s += INDENT + generateVisibility(attr) + " ";
	    s += generateClassifierRef(type) + " " + ModelFacade.getName(attr);
	    s += " {\n";

	    if ((makeSet != null) && (makeSet.equals("true"))) {
	        s += INDENT + INDENT + "set { m_" + ModelFacade.getName(attr);
	        s += " = value; } \n";
	    }

	    if ((makeGet != null) && (makeGet.equals("true"))) {
	        s += INDENT + INDENT;
	        s += "get { return m_" + ModelFacade.getName(attr) + "; } \n";
	    }

	    s += INDENT + "}\n";
	}

	return s;
    }


    /**
     * @see org.argouml.application.api.NotationProvider2#generateParameter(java.lang.Object)
     */
    public String generateParameter(Object param) {
	String s = "";
        String temp = "";
	// TODO: qualifiers (e.g., const)
	// TODO: stereotypes...
	s +=  generateClassifierRef(ModelFacade.getType(param)) + " ";
	if ((ModelFacade.getKind(param).equals(
	        ModelFacade.INOUT_PARAMETERDIRECTIONKIND))
	    || (ModelFacade.getKind(param).equals(
	            ModelFacade.OUT_PARAMETERDIRECTIONKIND))) {
	    // if OUT or INOUT, then pass by Reference
	    temp = "ref " + s;
            s = temp;
	}
	s += generateName(ModelFacade.getName(param));

	// TODO: initial value

	//    MExpression default_val = param.getDefaultValue();
	//    if ( (default_val != null) && (default_val.getBody() != null) ) {
	//        s += " = " + default_val.getBody();
	//    }

	return s;
    }


    /**
     * @see org.argouml.application.api.NotationProvider2#generatePackage(java.lang.Object)
     */
    public String generatePackage(Object p) {
	String s = "";
	String packName = generateName(ModelFacade.getName(p));
	s += "namespace " + packName + " {\n";
	Collection ownedElements = ModelFacade.getOwnedElements(p);
	if (ownedElements != null) {
	    Iterator ownedEnum = ownedElements.iterator();
	    while (ownedEnum.hasNext()) {
		Object me = ownedEnum.next();
		s += generate(me);
		s += "\n\n";
	    }
	} else {
	    s += "(no elements)";
	}
	s += "\n}\n";
	return s;
    }


    /**
     * @see org.argouml.application.api.NotationProvider2#generateClassifier(java.lang.Object)
     */
    public String generateClassifier(Object cls) {
	String generatedName = generateName(ModelFacade.getName(cls));
	String classifierKeyword;
	if (ModelFacade.isAClass(cls)) {
	    classifierKeyword = "class";
	} else if (ModelFacade.isAInterface(cls)) {
	    classifierKeyword = "interface";
	} else {
	    return ""; // actors and use cases
	}

	StringBuffer sb = new StringBuffer();

	// Add the comments for this classifier first.
	sb.append(DocumentationManager.getComments(cls));

	sb.append(generateConstraintEnrichedDocComment (cls)).append ("\n");

	sb.append(generateVisibility(ModelFacade.getVisibility(cls)));
	if (ModelFacade.isAbstract(cls) && !(ModelFacade.isAInterface(cls))) {
	    if (VERBOSE) {
	        sb.append("/* abstract */ ");
	    }
	}
	if (ModelFacade.isLeaf(cls)) {
	    if (VERBOSE) {
	        sb.append("/* final */ ");
	    }
	}
	sb.append(classifierKeyword).append(" ").append(generatedName);
	String baseClass =
	    generateGeneralization(ModelFacade.getGeneralizations(cls));
	String tv = null;
	if (!baseClass.equals("")) {
	    sb.append(' ').append(": ").append(baseClass);
	}

	// nsuml: realizations!
	if (ModelFacade.isAClass(cls)) {
	    String interfaces = generateSpecification(cls);
	    if (!interfaces.equals("")) {
		if (baseClass.equals("")) {
		    sb.append(": ");
		} else {
		    sb.append(", ");
		}
		sb.append(interfaces);
	    }
	}
	sb.append("\n{");

	tv = generateTaggedValues(cls);
	if (tv != null && tv.length() > 0) {
	    sb.append(INDENT).append(tv);
	}

	// Removed 2001-09-26 STEFFEN ZSCHALER:
	// sb.append(generateConstraints(cls));


	// generate constructor (Marian Heddesheimer)
	String makeConstructor =
	    ModelFacade.getTaggedValueValue(cls, "constructor");
	if ((makeConstructor != null) && (makeConstructor.equals("true"))) {
	    sb.append(INDENT).append("function ");
	    sb.append(ModelFacade.getName(cls)).append("() {\n");
	    sb.append(generateSection(cls));
	    sb.append(INDENT).append("}\n");
	}

	Collection strs = ModelFacade.getAttributes(cls);
	if (strs != null) {
	    sb.append('\n');

	    if (ModelFacade.isAClass(cls)) {
	        sb.append(INDENT).append("// Attributes\n");
	    }
	    Iterator strEnum = strs.iterator();
	    while (strEnum.hasNext()) {
		Object sf = strEnum.next();
		sb.append('\n').append(INDENT).append(generate(sf));
		tv = generateTaggedValues(sf);
		if (tv != null && tv.length() > 0) {
		    sb.append(INDENT).append(tv).append('\n');
		}
	    }
	}

	Collection ends = ModelFacade.getAssociationEnds(cls);
	if (ends != null) {
	    sb.append('\n');
	    if (ModelFacade.isAClass(cls)) {
	        sb.append(INDENT).append("// Associations\n");
	    }
	    Iterator endEnum = ends.iterator();
	    while (endEnum.hasNext()) {
		Object ae = endEnum.next();
		Object a = ModelFacade.getAssociation(ae);
		sb.append('\n');
		sb.append(INDENT).append(generateAssociationFrom(a, ae));
		tv = generateTaggedValues(a);
		if (tv != null && tv.length() > 0) {
		    sb.append(INDENT).append(tv);
		}

		// TODO: Why was this not in generateAssociationFrom ?
		// sb.append(generateConstraints(a));
	    }
	}

	// TODO: constructors
	Collection behs = Model.getUmlHelper().getCore().getOperations(cls);
	if (behs != null) {
	    sb.append ('\n');
	    sb.append (INDENT).append ("// Operations\n");

	    Iterator behEnum = behs.iterator();

	    while (behEnum.hasNext()) {
		Object bf = behEnum.next();

		sb.append('\n').append(INDENT).append(generate (bf));

		tv = generateTaggedValues(bf);

		if ((ModelFacade.isAClass(cls))
		        && (ModelFacade.isAOperation(bf))
		        && (!(ModelFacade.isAbstract(bf)))) {
		    sb.append('\n').append(INDENT).append("{\n");

		    if (tv.length() > 0) {
		        sb.append (INDENT).append (tv);
		    }

		    sb.append(generateMethodBody(bf));
		    sb.append('\n');
		    sb.append(INDENT).append ("}\n");
		} else {
		    sb.append(";\n");
		    if (tv.length() > 0) {
		        sb.append(INDENT).append(tv).append('\n');
		    }
		}
	    }
	}
	sb.append("} /* end ").append(classifierKeyword).append(' ');
	sb.append(generatedName).append(" */\n");

	return sb.toString();
    }

    /**
     * Generate the body of a method associated with the given operation. This
     * assumes there's at most one method associated!
     *
     * If no method is associated with the operation, a default method body will
     * be generated.
     */
    private String generateMethodBody(Object op) {
	if (op != null) {
	    // Collection methods = op.getMethods();
	    // Iterator i = methods.iterator();
	    // MMethod m = null;

	    // System.out.print(", op!=null, size="+methods.size());
	    return generateSection(op);
	    // return INDENT + INDENT + "/* method body for "
	    //     + op.getName() + " */";
	    /*
	      while (i != null && i.hasNext()) {
	      //System.out.print(", i!= null");
	      m = (MMethod) i.next();

	      if (m != null) {
	      //LOG.debug(", BODY of "+m.getName());
	      //LOG.debug("|"+m.getBody().getBody()+"|");
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

    private String generateDefaultReturnStatement(Object cls) {
	if (cls == null) {
	    return "";
	}

	String clsName = ModelFacade.getName(cls);
	if (clsName.equals("void")) {
	    return "";
	} else if (clsName.equals("char")) {
	    return INDENT + "return 'x';\n";
	} else if (clsName.equals("int")) {
	    return INDENT + "return 0;\n";
	} else if (clsName.equals("boolean")) {
	    return INDENT + "return false;\n";
	} else if (clsName.equals("byte")) {
	    return INDENT + "return 0;\n";
	} else if (clsName.equals("long")) {
	    return INDENT + "return 0;\n";
	} else if (clsName.equals("float")) {
	    return INDENT + "return 0.0;\n";
	} else if (clsName.equals("double")) {
	    return INDENT + "return 0.0;\n";
	} else {
	    return INDENT + "return null;\n";
	}
    }

    private String generateTaggedValues(Object e) {
	Iterator iter = ModelFacade.getTaggedValues(e);
	if (!iter.hasNext()) {
	    return "";
	}
	boolean first = true;
	StringBuffer buf = new StringBuffer();
	String s = null;
	while (iter.hasNext()) {
	    s = generateTaggedValue(iter.next());
	    if (s != null && s.length() > 0) {
		if (first) {
		    /*
		     * Corrected 2001-09-26 STEFFEN ZSCHALER
		     *
		     * Was:
		     buf.append("// {");
		     *
		     * which caused problems with new lines characters
		     * in tagged values (e.g. comments...).  The new
		     * version still has some problems with tagged values
		     * containing "*"+"/" as this closes the comment
		     * prematurely, but comments should be taken out of
		     * the tagged values list anyway...
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
	if (!first) {
	    buf.append ("}*/\n");
	}

	return buf.toString();
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateTaggedValue(java.lang.Object)
     */
    public String generateTaggedValue(Object tv) {
	if (tv == null) {
	    return "";
	}
	String s = generateUninterpreted(ModelFacade.getValueOfTag(tv));
	if (s == null || s.length() == 0 || s.equals("/** */")) {
	    return "";
	}
	return generateName(ModelFacade.getTagOfTag(tv)) + "=" + s;
    }

    /**
     * Enhance/Create the doccomment for the given model element, including
     * tags for any OCL constraints connected to the model element. The tags
     * generated are suitable for use with the ocl injector which is part of
     * the Dresden OCL Toolkit and are in detail:
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
    private String generateConstraintEnrichedDocComment(Object me,
						       Object ae) {
	String sDocComment = generateConstraintEnrichedDocComment(me);

	Object m = ModelFacade.getMultiplicity(ae);
	if (!(ModelFacade.M1_1_MULTIPLICITY.equals(m)
	        || ModelFacade.M0_1_MULTIPLICITY.equals (m))) {
	    // Multiplicity greater 1, that means we will generate some sort of
	    // collection, so we need to specify the element type tag

	    // Prepare doccomment
	    if (sDocComment != null) {
		// Just remove closing */
		sDocComment =
		    sDocComment.substring(0, sDocComment.indexOf("*/") + 1);
	    } else {
		if (VERBOSE) {
		    sDocComment = INDENT + "/**\n"
		    	+ INDENT + " * \n"
		    	+ INDENT + " *";
		} else {
		    sDocComment = "";
		}
	    }

	    // Build doccomment
	    sDocComment += " @element-type ";
	    sDocComment += ModelFacade.getName(ModelFacade.getType(ae));

	    sDocComment += "\n" + INDENT + " */";

	    return sDocComment;
	} else {
	    if (sDocComment != null) {
	        return sDocComment;
	    } else {
	        return "";
	    }
	}
    }

    /**
     * Enhance/Create the doccomment for the given model element, including
     * tags for any OCL constraints connected to the model element. The tags
     * generated are suitable for use with the ocl injector which is part
     * of the Dresden OCL Toolkit and are in detail:
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
    private String generateConstraintEnrichedDocComment(Object me) {
	// Retrieve any existing doccomment
	String sDocComment =
	    DocumentationManager.getDocs(me, GeneratorCSharp.INDENT);

	if (sDocComment != null) {
	    // Fix Bug in documentation manager.defaultFor -->
	    // look for current INDENT and use it
	    for (int i = sDocComment.indexOf ('\n');
		 i >= 0 && i < sDocComment.length();
		 i = sDocComment.indexOf ('\n', i + 1)) {
		sDocComment = sDocComment.substring(0, i + 1)
			+ INDENT + sDocComment.substring (i + 1);
	    }
	}

	// Extract constraints
	Collection cConstraints = ModelFacade.getConstraints(me);

	if (cConstraints.size() == 0) {
	    if (sDocComment != null) {
	        return sDocComment;
	    } else {
	        return "";
	    }
	}

	// Prepare doccomment
	if (sDocComment != null) {
	    // Just remove closing */
	    sDocComment =
	        sDocComment.substring(0, sDocComment.indexOf("*/") + 1);
	} else {
	    if (VERBOSE) {
		sDocComment = INDENT + "/**\n"
			+ INDENT + " * \n"
			+ INDENT + " *";
	    } else {
		sDocComment = "";
	    }
	}

	// Add each constraint

	class TagExtractor
		extends tudresden.ocl.parser.analysis.DepthFirstAdapter {
	    private LinkedList llsTags = new LinkedList();
	    private String constraintName;
	    private int constraintID = 0;

	    /**
	     * Constructor.
	     *
	     * @param sConstraintName
	     */
	    public TagExtractor(String sConstraintName) {
		super();

		constraintName = sConstraintName;
	    }

	    public Iterator getTags() {
		return llsTags.iterator();
	    }

	    public void caseAConstraintBody(AConstraintBody node) {
		// We don't care for anything below this node,
	        // so we do not use apply anymore.
		String sKind = null;
		if (node.getStereotype() != null) {
		    sKind = node.getStereotype().toString();
		}

		String sExpression = null;
		if (node.getExpression() != null) {
		    sExpression = node.getExpression().toString();
		}

		String sName;
		if (node.getName() != null) {
		    sName = node.getName().getText();
		} else {
		    sName = constraintName + "_" + (constraintID++);
		}

		if ((sKind == null)
		        || (sExpression == null)) {
		    return;
		}

		String sTag;
		if (sKind.equals ("inv ")) {
		    sTag = "@invariant ";
		} else if (sKind.equals ("post ")) {
		    sTag = "@post-condition ";
		} else if (sKind.equals ("pre ")) {
		    sTag = "@pre-condition ";
		} else {
		    return;
		}

		sTag += sName + ": " + sExpression;
		llsTags.addLast (sTag);
	    }
	}

	tudresden.ocl.check.types.ModelFacade mf =
	    new org.argouml.ocl.ArgoFacade (me);
	for (Iterator i = cConstraints.iterator(); i.hasNext();) {
	    Object constraint = i.next();

	    try {
		tudresden.ocl.OclTree otParsed =
		    tudresden.ocl.OclTree.createTree(
		            (String) ModelFacade.getBody(
		                    ModelFacade.getBody(constraint)),
						     mf);

		TagExtractor te =
		    new TagExtractor(ModelFacade.getName(constraint));
		otParsed.apply (te);

		for (Iterator j = te.getTags(); j.hasNext();) {
		    sDocComment += " " + j.next() + "\n" + INDENT + " *";
		}
	    } catch (IOException ioe) {
		// Nothing to be done, should not happen anyway ;-)
	    }
	}

	sDocComment += "/";

	return sDocComment;
    }

    private String generateAssociationFrom(Object association,
            				  Object associationEnd) {
	// TODO: does not handle n-ary associations
	String s = "";

	/*
	 * Moved into while loop 2001-09-26 STEFFEN ZSCHALER
	 *
	 * Was:
	 *
	 s += DocumentationManager.getDocs(a) + "\n" + INDENT;
	*/

	Collection connections = ModelFacade.getConnections(association);
	Iterator connEnum = connections.iterator();
	while (connEnum.hasNext()) {
	    Object associationEnd2 = connEnum.next();
	    if (associationEnd2 != associationEnd) {
		/**
		 * Added generation of doccomment 2001-09-26 STEFFEN ZSCHALER
		 *
		 */
		s += generateConstraintEnrichedDocComment(
		        association,
		        associationEnd2);
		s += "\n";

		s += generateAssociationEnd(associationEnd2);
	    }
	}

	return s;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAssociation(java.lang.Object)
     */
    public String generateAssociation(Object a) {
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

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAssociationRole(java.lang.Object)
     */
    public String generateAssociationRole(Object m) {
	return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAssociationEnd(java.lang.Object)
     */
    public String generateAssociationEnd(Object associationEnd) {
	if (!ModelFacade.isNavigable(associationEnd)) {
	    return "";
	}
	String s = INDENT + "protected ";
	//String s = INDENT;
	String tempS = "";
	if (VERBOSE) {
	    tempS += "/* public */ ";
	}
	// must be public or generate public navigation method!

	if (ModelFacade.CLASSIFIER_SCOPEKIND.equals(
	        ModelFacade.getTargetScope(associationEnd))) {
	    if (VERBOSE) {
	        tempS += "static ";
	    }
	}
	//     String n = ae.getName();
	//     if (n != null && !String.UNSPEC.equals(n)) {
	//         s += generateName(n) + " ";
	//     }
	//     if (ae.isNavigable()) s += "navigable ";
	//     if (ae.getIsOrdered()) s += "ordered ";
	// Object m = ModelFacade.getMultiplicity(associationEnd);
	// if (ModelFacade.M1_1_MULTIPLICITY.equals(m)
	//         || ModelFacade.M0_1_MULTIPLICITY.equals(m)) {
	// }
	if (VERBOSE) {
	    tempS += "/*"
	        + generateClassifierRef(ModelFacade.getType(associationEnd))
	        + "*/";
	} else {
	    if (VERBOSE) {
	        tempS += "/* Vector */ "; //generateMultiplicity(m) + " ";
	    }
	}

	if (tempS.length() > 0) {
	    s += tempS + " ";
	}

	String name = ModelFacade.getName(associationEnd);
	Object association = ModelFacade.getAssociation(associationEnd);
        Object multi = ModelFacade.getMultiplicity(associationEnd);
        if ((multi.equals(ModelFacade.M1_1_MULTIPLICITY))
                || multi.equals(ModelFacade.M0_1_MULTIPLICITY)) {
            s += generateClassifierRef(ModelFacade.getType(associationEnd))
                + " ";
        } else if ((multi.equals(ModelFacade.M1_N_MULTIPLICITY))
                || multi.equals(ModelFacade.M0_N_MULTIPLICITY)) {
            s += "ArrayList ";
        }
	String associationName = ModelFacade.getName(association);
	if (name != null
	        && name != null && name.length() > 0) {
	    s += "var $" + generateName(name);
	} else if (associationName != null
	        && associationName != null && associationName.length() > 0) {
	    s += "var $" + generateName(associationName);
	} else {
	    s += "var $my";
	    s += generateClassifierRef(ModelFacade.getType(associationEnd));
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


    private String generateGeneralization(Collection generalizations) {
	if (generalizations == null) {
	    return "";
	}
	Collection classes = new ArrayList();
	Iterator iter = generalizations.iterator();
	while (iter.hasNext()) {
	    Object generalization = iter.next();
	    Object generalizableElement = ModelFacade.getParent(generalization);
	    // assert ge != null
	    if (generalizableElement != null) {
	        classes.add(generalizableElement);
	    }
	}
	return generateClassList(classes);
    }

    /**
     * TODO: Once the {@link
     * org.argouml.model.uml.CoreHelperImpl#getRealizedInterfaces(
     * ru.novosoft.uml.foundation.core.MClassifier)} can be called without
     * using NSUML interfaces, this class can be NSUML-free.
     *
     * @param cls The classifier that we generate the specification for.
     * @return The specification, as a String.
     */
    private String generateSpecification(Object cls) {
	String s = "";
	//s += cls.getName();

	Collection realizations =
	    Model.getUmlHelper().getCore().getRealizedInterfaces(cls);
	if (realizations == null) {
	    return "";
	}
	Iterator clsEnum = realizations.iterator();
	while (clsEnum.hasNext()) {
	    Object i = clsEnum.next();
	    s += generateClassifierRef(i);
	    if (clsEnum.hasNext()) {
	        s += ", ";
	    }
	}

	return s;
    }

    private String generateClassList(Collection classifiers) {
	String s = "";
	if (classifiers == null) {
	    return "";
	}
	Iterator clsEnum = classifiers.iterator();
	while (clsEnum.hasNext()) {
	    s += generateClassifierRef(clsEnum.next());
	    if (clsEnum.hasNext()) {
	        s += ", ";
	    }
	}
	return s;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateVisibility(java.lang.Object)
     *
     * This can be called with either a feature or a visibility.
     */
    public String generateVisibility(Object handle) {
        Object visibility;
	if (ModelFacade.isAFeature(handle)) {
	    visibility = ModelFacade.getVisibility(handle);
	} else {
	    visibility = handle;
	}

	//if (vis == null) return "";
	if (ModelFacade.PUBLIC_VISIBILITYKIND.equals(visibility)) {
	    return "public ";
	} else if (ModelFacade.PRIVATE_VISIBILITYKIND.equals(visibility)) {
	    return  "private ";
	} else if (ModelFacade.PROTECTED_VISIBILITYKIND.equals(visibility)) {
	    return  "protected ";
	} else {
	    return "";
	}
    }

    private String generateScope(Object feature) {
	Object scope = ModelFacade.getOwnerScope(feature);
	//if (scope == null) return "";
	if (ModelFacade.CLASSIFIER_SCOPEKIND.equals(scope)) {
	    if (VERBOSE) {
	        return "/* static */ ";
	    } else {
	        return "";
	    }
	}
	return "";
    }

    /**
     * Generate "abstract" keyword for abstract operations.
     *
     * @param op The candidate.
     * @return Return the abstractness.
     */
    private String generateAbstractness(Object op) {
	if (ModelFacade.isAbstract(op)) {
	    return "abstract ";
	}
	return "";
    }

    /**
     * Generate "final" keyword for final operations.
     *
     * @param op The candidate.
     * @return The generated changeability.
     */
    private String generateChangeability(Object op) {
	if (ModelFacade.isLeaf(op)) {
	    return " sealed ";
	} else {
	    return "";
	}
    }

    private String generateChangability(Object sf) {
	Object ck = ModelFacade.getChangeability(sf);
	//if (ck == null) return "";
	if (ModelFacade.FROZEN_CHANGEABLEKIND.equals(ck)) {
	    return " sealed ";
	}
	//if (MChangeableKind.ADDONLY.equals(ck)) return "final ";
	return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateMultiplicity(java.lang.Object)
     */
    public String generateMultiplicity(Object multiplicity) {
	if (multiplicity == null) {
	    return "";
	}
	if (ModelFacade.M0_N_MULTIPLICITY.equals(multiplicity)) {
	    return ANY_RANGE;
	}
	String s = "";
	Iterator iter = ModelFacade.getRanges(multiplicity);
	while (iter.hasNext()) {
	    Object mr = iter.next();
	    s += generateMultiplicityRange(mr);
	    if (iter.hasNext()) {
	        s += ",";
	    }
	}
	return s;
    }


    private static final String ANY_RANGE = "0..*";
    //public static final String ANY_RANGE = "*";
    // TODO: user preference between "*" and "0..*"

    private String generateMultiplicityRange(Object multiplicityRange) {
	Integer lower = new Integer(ModelFacade.getLower(multiplicityRange));
	Integer upper = new Integer(ModelFacade.getUpper(multiplicityRange));
	if (lower == null && upper == null) {
	    return ANY_RANGE;
	}
	if (lower == null) {
	    return "*.." + upper.toString();
	}
	if (upper == null) {
	    return lower.toString() + "..*";
	}
	if (lower.intValue() == upper.intValue()) {
	    return lower.toString();
	}
	return lower.toString() + ".." + upper.toString();

    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateState(java.lang.Object)
     */
    public String generateState(Object m) {
	return ModelFacade.getName(m);
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateStateBody(java.lang.Object)
     */
    public String generateStateBody(Object state) {
	LOG.debug("GeneratorCSharp: generating state body");
	String s = "";
	Object entry = ModelFacade.getEntry(state);
	Object exit = ModelFacade.getExit(state);
	if (entry != null) {
	    String entryStr = generate(entry);
	    if (entryStr.length() > 0) {
	        s += "entry / " + entryStr;
	    }
	}
	if (exit != null) {
	    String exitStr = generate(exit);
	    if (s.length() > 0) {
	        s += "\n";
	    }
	    if (exitStr.length() > 0) {
	        s += "exit / " + exitStr;
	    }
	}
	Collection trans = ModelFacade.getInternalTransitions(state);
	if (trans != null) {
	    Iterator iter = trans.iterator();
	    while (iter.hasNext()) {
		if (s.length() > 0) {
		    s += "\n";
		}
		s += generateTransition(iter.next());
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

    /**
     * @see org.argouml.application.api.NotationProvider2#generateTransition(java.lang.Object)
     */
    public String generateTransition(Object state) {
	String s = generate(ModelFacade.getName(state));
	String t = generate(ModelFacade.getTrigger(state));
	String g = generate(ModelFacade.getGuard(state));
	String e = generate(ModelFacade.getEffect(state));
	if (s.length() > 0) {
	    s += ": ";
	}
	s += t;
	if (g.length() > 0) {
	    s += " [" + g + "]";
	}
	if (e.length() > 0) {
	    s += " / " + e;
	}
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

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAction(java.lang.Object)
     */
    public String generateAction(Object m) {
	// return m.getName();
	Object script = ModelFacade.getScript(m);
	if ((script != null) && (ModelFacade.getBody(script) != null)) {
	    return ModelFacade.getBody(script).toString();
	}
	return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateGuard(java.lang.Object)
     */
    public String generateGuard(Object guard) {
	//return generateExpression(m.getExpression());
	if (ModelFacade.getExpression(guard) != null) {
	    return generateExpression(ModelFacade.getExpression(guard));
	}
	return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateMessage(java.lang.Object)
     */
    public String generateMessage(Object message) {
	if (message == null) {
	    return "";
	}
	return generateName(ModelFacade.getName(message)) + "::"
		+ generateAction(ModelFacade.getAction(message));
    }

    /**
     * Generates the String representation for an Event.
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateEvent(Object modelElement) {
        if (!ModelFacade.isAEvent(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Event required");
        }

        return "";
    }

    private String generateSection(Object cls) {
        String id = UUIDHelper.getInstance().getUUID(cls);
	if (id == null) {
	    id = (new UID().toString());
	    // id = cls.getName() + "__" + static_count;
	    ModelFacade.setUUID(cls, id);
        }
	// String s = "";
	// s += INDENT + "// section " + id + " begin\n";
	// s += INDENT + "// section " + id + " end\n";
	return Section.generate(id, INDENT);
    }


    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { return "GeneratorCSharp"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
	return "CSharp Notation and Code Generator";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { return "Mike Lipkie"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { return "0.1.0"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.language.csharp.generator"; }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateActionState(java.lang.Object)
     */
    public String generateActionState(Object actionState) {
        return generateState(actionState);
    }

    private String generateImports(Object cls, String packagePath) {
        // TODO: check also generalizations
        StringBuffer sb = new StringBuffer(80);
        java.util.HashSet importSet = new java.util.HashSet();
        String ftype;
        Iterator j;
        Collection c = ModelFacade.getFeatures(cls);
        if (c != null) {
            // now check packages of all feature types
            for (j = c.iterator(); j.hasNext();) {
                Object mFeature = /*(MFeature)*/ j.next();
                if (ModelFacade.isAAttribute(mFeature)) {
                    if ((ftype =
                            generateImportType(ModelFacade.getType(mFeature),
                                               packagePath))
                            != null) {
                        importSet.add(ftype);
                    }
                } else if (ModelFacade.isAOperation(mFeature)) {
                    // check the parameter types
                    Iterator it =
			ModelFacade.getParameters(mFeature).iterator();
                    while (it.hasNext()) {
                        Object parameter = it.next();
			ftype =
			    generateImportType(ModelFacade.getType(parameter),
					       packagePath);
			if (ftype != null) {
                            importSet.add(ftype);
                        }
                    }

                    // check the return parameter types
                    it =
                        Model.getUmlHelper()
			    .getCore()
			        .getReturnParameters(/*(MOperation)*/mFeature)
			            .iterator();
                    while (it.hasNext()) {
                        Object parameter = it.next();
			ftype =
			    generateImportType(ModelFacade.getType(parameter),
					       packagePath);
                        if (ftype != null) {
                            importSet.add(ftype);
                        }
                    }

		    // check raised signals
		    it = ModelFacade.getRaisedSignals(mFeature).iterator();
		    while (it.hasNext()) {
			Object signal = it.next();
			if (!ModelFacade.isAException(signal)) {
			    continue;
			}

			ftype =
			    generateImportType(ModelFacade.getType(signal),
					       packagePath);
			if (ftype != null) {
			    importSet.add(ftype);
			}
		    }
                }
            }
        }

	c = ModelFacade.getGeneralizations(cls);
	if (c != null) {
	    // now check packages of all generalized types
	    for (j = c.iterator(); j.hasNext();) {
		Object gen = /*(MGeneralization)*/ j.next();
		Object parent = ModelFacade.getParent(gen);
		if (parent == cls) {
		    continue;
		}

		ftype = generateImportType(parent,
					   packagePath);
		if (ftype != null) {
		    importSet.add(ftype);
		}
	    }
	}

	c = ModelFacade.getSpecifications(cls);
	if (c != null) {
	    // now check packages of the interfaces
	    for (j = c.iterator(); j.hasNext();) {
		Object iface = /*(MInterface)*/ j.next();

		ftype = generateImportType(iface,
					   packagePath);
		if (ftype != null) {
		    importSet.add(ftype);
		}
	    }
	}

        c = ModelFacade.getAssociationEnds(cls);
        if (!c.isEmpty()) {
            // check association end types
            for (j = c.iterator(); j.hasNext();) {
                Object associationEnd = /*(MAssociationEnd)*/ j.next();
                Object association = ModelFacade.getAssociation(associationEnd);
                Iterator connEnum =
		    ModelFacade.getConnections(association).iterator();
                while (connEnum.hasNext()) {
                    Object associationEnd2 =
			/*(MAssociationEnd)*/ connEnum.next();
                    if (associationEnd2 != associationEnd
                            && ModelFacade.isNavigable(associationEnd2)
                            && !ModelFacade.isAbstract(
                                    ModelFacade.getAssociation(
                                            associationEnd2))) {
                        // association end found
                        Object multiplicity =
			    ModelFacade.getMultiplicity(associationEnd2);
                        if (!ModelFacade.M1_1_MULTIPLICITY.equals(multiplicity)
                                && !ModelFacade.M0_1_MULTIPLICITY.equals(
                                        multiplicity)) {
                            importSet.add("System.Collections");
                        } else {
			    ftype =
				generateImportType(ModelFacade.getType(
				        associationEnd2),
						   packagePath);
			    if (ftype != null) {
				importSet.add(ftype);
			    }
                        }
                    }
                }
            }
        }
        // finally generate the import statements
        for (j = importSet.iterator(); j.hasNext();) {
            ftype = (String) j.next();
            sb.append("using ").append(ftype).append(";");
	    sb.append(LINE_SEPARATOR);
        }
        if (!importSet.isEmpty()) {
            sb.append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    private String generateImportType(Object type, String exclude) {
        String ret = null;
        if (type != null && ModelFacade.getNamespace(type) != null) {
            String p = getPackageName(ModelFacade.getNamespace(type));
            if (!p.equals(exclude)) {
                ret = p;
		if (p.length() > 0) {
		    ret = p;
		} else {
		    ret = null;
		}
	    }
        }
        return ret;
    }
    /**
       Gets the .NET package name for a given namespace,
       ignoring the root namespace (which is the model).

       @param namespace the namespace
       @return the Java package name
    */
    public String getPackageName(Object namespace) {
        if (namespace == null
	    || !ModelFacade.isANamespace(namespace)
	    || ModelFacade.getNamespace(namespace) == null)
            return "";
        String packagePath = ModelFacade.getName(namespace);
        while ((namespace = ModelFacade.getNamespace(namespace)) != null) {
            // ommit root package name; it's the model's root
            if (ModelFacade.getNamespace(namespace) != null)
                packagePath =
		    ModelFacade.getName(namespace) + '.' + packagePath;
        }
        return packagePath;
    }
} /* end class GeneratorCSharp */
