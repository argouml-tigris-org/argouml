// Copyright (c) 2001 The Regents of the University of California. All
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

package org.argouml.language.php.generator;

import java.io.*;
import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.model_management.*;
import org.argouml.uml.MMUtil;

import org.argouml.application.api.*;
import org.argouml.uml.DocumentationManager;
import org.argouml.uml.generator.*;

/** 
 * Generator subclass to generate PHP from UML models.
 */

public class GeneratorPHP extends Generator {

    public static GeneratorPHP SINGLETON = new GeneratorPHP();

    public final static String fileSep=System.getProperty("file.separator");

    /** Four spaces used for indenting code in classes. */
    public static String INDENT = "    ";

    /** The default copyright for the fileheaders */
    public static String DEFAULT_FILEHEADER = "(c) <your name>\n"
	                                      + "<your disclaimer>\n";

    public static String Generate(Object o) {
	return SINGLETON.generate(o);
    }

    public GeneratorPHP() {
       super(Notation.makeNotation("PHP"));
    }


    /**
     * Create the file for the classifier to be processed.
     *
     * @param cls The classifier to generate code for.
     * @param path The path, where the file is created.
     * @return The full path name of the created file.
     */
    public static String GenerateFile( MClassifier cls, String path) {
	String name = cls.getName();
	if (name == null || name.length() == 0) return null;
	String filename = name + ".php";
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
	String header = SINGLETON.generateHeader(cls, pathname, packagePath, null);
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

    /**
     * Generate a header for the file to be written.
     *
     * @param cls The classifier to generate code for.
     * @param pathname The full path of the file.
     * @param packagePath The full package of the classifier
     * @param fileHeader The header for the file.
     * @return The entire header as a String.
     */
    public String generateHeader(MClassifier cls, String pathname, String packagePath, String fileHeader) {
	String s = "";
	
	// Add the filename as the 1st entry.
	s += "// FILE: " + pathname.replace('\\','/') +"\n\n";

	// The add the user's header, if one is given. Use the default header otherwise.
	s += fileHeader != null ? fileHeader : DEFAULT_FILEHEADER;

	s += "\n";
	return s;
    }

    /**
     * Generate code for a operation.
     * 
     * @param op The operation to generate code for.
     * @return The code for the operation as a String.
     */
    public String generateOperation(MOperation op, boolean b) {
	String s = "";
	s += DocumentationManager.getDocs(op) + "\n" + INDENT;
	s += "function " + generateName(op.getName()) + "(";

	// params
	// pick out return type
	MParameter rp = MMUtil.SINGLETON.getReturnParameter(op);  

	Vector params = new Vector(op.getParameters());
	params.remove(rp);
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

    /**
     * Generate code for a attribute
     *
     * @param attr The attribute to generate code for.
     * @return The code for the attribute as a String.
     */
    public String generateAttribute(MAttribute attr, boolean b) {
	String s = "";
	s += DocumentationManager.getDocs(attr) + "\n" + INDENT;

	s += "var $" + generateName(attr.getName());

	MExpression init = attr.getInitialValue();
	if (init != null) {
	    String initStr = generateExpression(init).trim();
	    if (initStr.length() > 0)
		s += " = " + initStr;
	}

	s += ";\n";

	return s;
    }

    /**
     * Generate code for a parameter
     *
     * @param param The parameter to generate code for.
     * @return The code for the parameter as a String.
     */
    public String generateParameter(MParameter param) {
	String s = "$" + generateName(param.getName());
	return s;
    }

    /**
     * Generate package info.
     * 
     * @param The package of the currently processed classifier.
     * @return The entire package info as a String.
     */
    public String generatePackage(MPackage p) {
	String s = "";
	String packName = generateName(p.getName());
	s += "// package " + packName + " {\n";
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

    /**
     * Generate code for an entire classifier.
     *
     * @param cls The classifier to generate code for.
     * @return The entire code as a String.
     */
    public String generateClassifier(MClassifier cls) {
	String generatedName = generateName(cls.getName());
	String classifierKeyword;
	if (cls instanceof MClassImpl) classifierKeyword = "class";
	else if (cls instanceof MInterface) classifierKeyword = "class";
	else return ""; // actors and use cases
	StringBuffer sb = new StringBuffer(80);
	sb.append(DocumentationManager.getComments(cls));  // Add the comments for this classifier first.
	sb.append(DocumentationManager.getDocs(cls)).append("\n");

	sb.append(classifierKeyword).append(" ").append(generatedName);
	String baseClass = generateGeneralization(cls.getGeneralizations());
	String tv = null;
	if (!baseClass.equals("")) sb.append(' ').append("extends ").append(baseClass);

	sb.append("{\n");

	tv = generateTaggedValues(cls);
	if (tv != null && tv.length() > 0) sb.append(INDENT).append(tv);
	sb.append(generateConstraints(cls));

	Collection strs = MMUtil.SINGLETON.getAttributes(cls);
	if (strs != null) {
	    sb.append('\n');

	    if (cls instanceof MClassImpl) sb.append(INDENT).append("// Attributes\n");
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
	    if (cls instanceof MClassImpl) sb.append(INDENT).append("// Associations\n");
	    Iterator endEnum = ends.iterator();
	    while (endEnum.hasNext()) {
		MAssociationEnd ae = (MAssociationEnd) endEnum.next();
		MAssociation a = ae.getAssociation();
		sb.append('\n').append(INDENT).append(generateAssociationFrom(a, ae));
		tv = generateTaggedValues(a);
		if (tv != null && tv.length() > 0) sb.append(INDENT).append(tv);
		sb.append(generateConstraints(a));
	    }
	}

	// needs-more-work: constructors

	Collection behs = MMUtil.SINGLETON.getOperations(cls);
	if (behs != null) {
	    sb.append('\n');
	    sb.append(INDENT).append("// Operations\n");
	    Iterator behEnum = behs.iterator();
	    String terminator1 = "\n" + INDENT + "{";
	    String terminator2 = INDENT + "}";
	    if (cls instanceof MInterface) { terminator1 = ";\n"; terminator2 = ""; }
	    while (behEnum.hasNext()) {
		MBehavioralFeature bf = (MBehavioralFeature) behEnum.next();
		sb.append('\n').append(INDENT).append(generate(bf)).append(terminator1);
		tv = generateTaggedValues((MModelElement)bf);
		if (tv.length() > 0) sb.append(INDENT).append(tv);
		sb.append(generateConstraints((MModelElement)bf));

		// there is no ReturnType in behavioral feature (nsuml)
		if (cls instanceof MClassImpl && bf instanceof MOperation) {
		    sb.append(generateMethodBody((MOperation)bf)).append('\n');
		}
		sb.append(terminator2).append('\n');
	    }
	}
	sb.append("} /* end ").append(classifierKeyword).append(' ').append(generatedName).append(" */\n");
	return sb.toString();
    }

    /**
     * Generate code for the body of a method.
     *
     * @param op The method to generate the body for.
     * @return The method body as a String.
     */
    public String generateMethodBody(MOperation op) {
	if (op != null) {
	    Collection methods = op.getMethods();
	    Iterator i = methods.iterator();
	    MMethod m = null;
	    while (i != null && i.hasNext()) {
		m = (MMethod)i.next();
		if (m != null) {
		    return m.getBody().getBody();
		}
	    }
	}
	// pick out return type
	MParameter rp = MMUtil.SINGLETON.getReturnParameter(op);
	if (rp == null)
	    return generateDefaultReturnStatement(null);
	MClassifier returnType = rp.getType();
	return generateDefaultReturnStatement(returnType);
    }

    /**
     * Generate a default return statement for a method.
     *
     * @param cls The classifier to return.
     * @return The return statement as a String.
     */
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

    /**
     * Generate a stereotype info.
     * 
     * @param s The stereotype
     * @return The steretype info as a String.
     */
    public String generateStereotype(MStereotype s) {
	return "<<" + generateName(s.getName()) + ">>";
    }

    /**
     * Generate code for all the tagged values of a model
     * element.
     *
     * @param e The model element with the tagged values.
     * @return The code for the tagged values as a String.
     */
    public String generateTaggedValues(MModelElement e) {
	Collection tvs = e.getTaggedValues();
	if (tvs == null || tvs.size() == 0) return "";
	boolean first=true;
	StringBuffer buf = new StringBuffer();
	Iterator iter = tvs.iterator();
	String s = null;
	while(iter.hasNext()) {
	    s = generateTaggedValue((MTaggedValue)iter.next());
	    if (s != null && s.length() > 0) {
		if (first) {
		    buf.append("// {");
		    first = false;
		} else {
		    buf.append(", ");
		}
		buf.append(s);
	    }
	}
	if (!first) buf.append("}\n");
	return buf.toString();
    }

    /**
     * Generate code for a single tagged value.
     *
     * @param tv The tagged value.
     * @return The code as a String.
     */
    public String generateTaggedValue(MTaggedValue tv) {
	if (tv == null) return "";
	String s=generateUninterpreted(tv.getValue());
	if (s == null || s.length() == 0 || s.equals("/** */")) return "";
	return generateName(tv.getTag()) + "=" + s;
    }

    /**
     * Generate code for all the constraints of a model element.
     *
     * @param e The model element with the constraints.
     * @return The code for the constraints as a String.
     */
    public String generateConstraints(MModelElement me) {

	// This method just adds comments to the generated php code. This should be code generated by ocl-argo in the future?
	Collection cs = me.getConstraints();
	if (cs == null || cs.size() == 0) return "";
	String s = INDENT + "// constraints\n";
	int size = cs.size();
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

    /**
     * Generate code for a single constraint.
     * 
     * @param c The constraint to generate code for.
     * @return The code for the constraint as a String.
     */
    public String generateConstraint(MConstraint c) {
	if (c == null) return "";
	String s = "";
	if (c.getName() != null && c.getName().length() != 0)
	    s += generateName(c.getName()) + ": ";
	s += generateExpression(c);
	return s;
    }

    /**
     * Generate code for a association.
     * 
     * @param a The association to generate code for.
     * @param ae One end of the association.
     * @return The code for the association as a String.
     */
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

    /**
     * Generate code for a association.
     *
     * @param a The association to generate code for.
     * @return The code for the association as a String.
     */
    public String generateAssociation(MAssociation a) {
	String s = "";

	return s;
    }

    /**
     * Generate code for a association end.
     *
     * @param ae The association end to generate code for.
     * @return The code for the association as a String.
     */
    public String generateAssociationEnd(MAssociationEnd ae) {
	String s = "";

	return s;
    }


    ////////////////////////////////////////////////////////////////
    // internal methods?

    /**
     * Generate code for a bunch of generalizations.
     *
     * @param generalizations The generalizations to generate code for.
     * @return The code for the generalizations as a String.
     */
    public String generateGeneralization(Collection generalizations) {
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

    /**
     * Generate code for a specification.
     *
     * @param cls The class that implements the specifications.
     * @return The code for the specifications as a String.
     */
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

    /**
     * Generate code for a visibility.
     *
     * @param vis The visibility to generate code for.
     * @return The code for the visibility as a String.
     */
    public String generateVisibility(MVisibilityKind vis) {
	return "";
    }

    /**
     * Generate code for the visibility of a feature.
     *
     * @param f The feature.
     * @return The code for the visibility of the given feature as a String.
     */
    public String generateVisibility(MFeature f) {
	return "";
    }

    /**
     * Generate code for the scope of a feature.
     *
     * @param f The feature.
     * @return The code for the scope of the feature as a String.
     */
    public String generateScope(MFeature f) {
	return "";
    }

    /**
     * Generate code for the changability of a structural feature.
     *
     * @param sf The structural feature.
     * @return The code for the changability as a String.
     */
    public String generateChangability(MStructuralFeature sf) {
	return "";
    }

    /**
     * Generate code for a multiplicity.
     *
     * @param m The multiplicity to generate code for.
     * @return The code for the multiplicity as a String.
     */
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
    // needs-more-work: user preference between "*" and "0..*"

    /**
     * Generate code for a multiplicity range.
     *
     * @param mr The multiplicity range.
     * @return The code for the multiplicity range as a String.
     */
    public String generateMultiplicityRange(MMultiplicityRange mr) {
	Integer lower = new Integer(mr.getLower());
	Integer upper = new Integer(mr.getUpper());
	if (lower == null && upper == null) return ANY_RANGE;
	if (lower == null) return "*.."+ upper.toString();
	if (upper == null) return lower.toString() + "..*";
	if (lower.intValue() == upper.intValue()) return lower.toString();
	return lower.toString() + ".." + upper.toString();

    }

    /**
     * Generate code a state.
     * 
     * @param m The state to generate code for.
     * @return The code for the state as a String.
     */
    public String generateState(MState m) {
	return m.getName();
    }

    public String generateStateBody(MState m) {
	System.out.println("GeneratorPHP: generating state body");
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

    /**
     * Generate code a transistion.
     * 
     * @param m The transition to generate code for.
     * @return The code for the transition as a String.
     */
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

    /**
     * Generate code for a action.
     *
     * @param m The action to generate code for.
     * @return The code for the action as a String.
     */
    public String generateAction(MAction m) {
	// return m.getName();
	if ((m.getScript() != null) && (m.getScript().getBody() != null))
	    return m.getScript().getBody();
	return "";
    }

    /**
     * Generate code for a guard.
     *
     * @param m The guard to generate code for.
     * @return The code for the guard as a String.
     */
    public String generateGuard(MGuard m) {
	//return generateExpression(m.getExpression());
	if (m.getExpression() != null)
	    return generateExpression(m.getExpression());
	return "";
    }

    /**
     * Generate code for a message.
     *
     * @param m The message to generate code for.
     * @return The code for the message as a String.
     */
    public String generateMessage(MMessage m) {
    	if (m == null) return "";
	return generateName(m.getName()) + "::" +
	    generateAction(m.getAction());
    }

    public boolean canParse(Object o) { return false; }
    public boolean canParse() { return false; }

} /* end class GeneratorPHP */


