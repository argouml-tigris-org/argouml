// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.language.cpp.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.UID;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Notation;
import org.argouml.application.api.PluggableNotation;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlHelper;
import org.argouml.uml.DocumentationManager;
import org.argouml.uml.generator.FileGenerator;
import org.argouml.uml.generator.Generator2;

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
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDependency;
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
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MMultiplicityRange;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MPackage;

/**
 * Generator2 subclass to generate text for display in diagrams in in
 * text fields in the Argo/UML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for outher languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced.
 */

// TODO: always check for null!!!

public class GeneratorCpp extends Generator2
    implements PluggableNotation, FileGenerator
{

    /** logger */
    private static final Logger LOG = Logger.getLogger(GeneratorCpp.class);

    protected boolean _verboseDocs = false;
    protected boolean _lfBeforeCurly = false;
    /**
     * TODO: make it configurable
     */
    private static final boolean VERBOSE_DOCS = false;

    public static final boolean VERBOSE = false;

    private static Section sect;

    /**
     * 2002-11-21
     * aim: convert package nesting in C++ namespaces
     * store names of namespaces during packagePath parsing, so that
     * the names of namespaces can be put after closing curly braces
     * for each namespace
     */
    protected Vector _NamespaceNames = new Vector();

    /** 2002-12-07 Achim Spangler
     * store actual namespace, to avoid unneeded curley braces
     */
    private MNamespace actualNamespace;

    /** 2002-12-12 Achim Spangler
     * store extra include dependencies which are generated during
     * generation of multiplicity to get needed container type
     */
    private String extraIncludes = "";


    /**
     * 2002-11-28 Achim Spangler
     * C++ doesn't place visibility information for each class member
     * --> sort items during generation and store visibility state
     * of lastly generated member in central class variable, so that
     * the appropriate lines: "public:\n", "protected:\n", "private:\n"
     * can be created
     */
    private static final int PUBLIC_PART = 1;
    private static final int PROTECTED_PART = 2;
    private static final int PRIVATE_PART = 3;

    private static final int[] ALL_PARTS = {
	PUBLIC_PART,
	PROTECTED_PART,
	PRIVATE_PART,
    };

    /**
     * @deprecated by Linus Tolke as of 0.15.5. Not used.
     */
    public static final int undefined_part = 0;

    /**
     * @deprecated by Linus Tolke as of 0.15.5. Use {@link #PUBLIC_PART}.
     */
    public static final int public_part = 1;

    /**
     * @deprecated by Linus Tolke as of 0.15.5. Use {@link #PROTECTED_PART}.
     */
    public static final int protected_part = 2;

    /**
     * @deprecated by Linus Tolke as of 0.15.5. Use {@link #PRIVATE_PART}.
     */
    public static final int private_part = 3;

    /**
     * 2002-11-28 Achim Spangler
     * C++ uses two files for each class: header (.h) with class definition
     * and source (.cpp) with methods implementation
     * --> two generation passes are needed
     */
    public static final int none_pass = 1;
    public static final int header_pass = 2;
    public static final int source_pass = 3;
    private static int generatorPass = none_pass;

    /**
     * 2002-12-05 Achim Spangler
     * use Tag generation for generation of: doccomment, simple tags of
     * tags which are not used for doccomment or simple tags for all
     */
    public static final int DocCommentTags = 1;
    public static final int AllButDocTags = 2;
    public static final int AllTags = 3;

    /**
     * 2002-12-06 Achim Spangler
     * C++ developers need to specify for parameters whether they are
     * pointers or references (especially for class-types)
     * -> a general check function must get the searched tag
     */
    public static final int SearchReferenceTag = 1;
    public static final int SearchPointerTag = 2;
    public static final int SearchReferencePointerTag = 3;



    private static final GeneratorCpp SINGLETON = new GeneratorCpp();

    /**
     * Get the instance.
     *
     * @return the singleton of the generator.
     */
    public static GeneratorCpp getInstance() { return SINGLETON; }


    protected GeneratorCpp() {
	super (Notation.makeNotation ("Cpp",
				      null,
				      Argo.lookupIconResource ("CppNotation")));
    }

    public static String Generate(Object o) {
	return SINGLETON.generate(o);
    }


    /** 2002-11-28 Achim Spangler
     * @return file extension for actual generation pass
     */
    private String getFileExtension()
    {
	// for Java simply answer ".java" every time
	if (generatorPass == header_pass) return ".h";
	else return ".cpp";
    }

    /**
     * create the needed directories for the derived appropriate pathname
     * @return full pathname
     */
    private String generateDirectoriesPathname(MClassifier cls, String path)
    {
	String name = cls.getName();
	if (name == null || name.length() == 0) return null;
	String filename = name + getFileExtension();
	if (!path.endsWith (FILE_SEPARATOR)) path += FILE_SEPARATOR;

        String packagePath = "";
        // avoid model being used as a package name
        MNamespace parent = cls.getNamespace().getNamespace();
        if (parent != null) packagePath = cls.getNamespace().getName();
	while (parent != null) {
	    // ommit root package name; it's the model's root
	    if (parent.getNamespace() != null)
		packagePath = parent.getName() + "." + packagePath;
	    parent = parent.getNamespace();
	}

	int lastIndex = -1;
	do {
	    File f = new File (path);
	    if (!f.isDirectory()) {
		if (!f.mkdir()) {
		    LOG.error(" could not make directory " + path);
		    return null;
		}
	    }

	    if (lastIndex == packagePath.length())
		break;

	    int index = packagePath.indexOf (".", lastIndex + 1);
	    if (index == -1)
		index = packagePath.length();

	    path +=
		packagePath.substring (lastIndex + 1, index) + FILE_SEPARATOR;
	    lastIndex = index;
	} while (true);

	String pathname = path + filename;
	//cat.info("-----" + pathname + "-----");
	return pathname;
    }

    /**
     * Generates a file for the classifier.
     * This method could have been static if it where not for the need to
     * call it through the Generatorinterface.<p>
     *
     * @return the full path name of the the generated file.
     * @see FileGenerator#GenerateFile(Object, String)
     */
    public String GenerateFile(Object o, String path) {
	MClassifier cls = (MClassifier) o;
	String packagePath = cls.getNamespace().getName();
   	String pathname = null;

	// use unique section for both passes -> allow move of
	// normal function body to inline and vice versa
   	sect = new Section();

	/*
	 * 2002-11-28 Achim Spangler
	 * first read header and source file into global/unique section
	 */
	for (generatorPass = header_pass;
	     generatorPass <= source_pass;
	     generatorPass++) {
	    pathname = generateDirectoriesPathname(cls, path);
	    //String pathname = path + filename;
	    // TODO: package, project basepath, tagged values to configure
	    File f = new File(pathname);
	    if (f.exists()) {
		LOG.info("Generating (updated) " + f.getPath());
		sect.read(pathname);
	    } else {
		LOG.info("Generating (new) " + f.getPath());
	    }
	}

	/**
	 * 2002-11-28 Achim Spangler
	 * run basic generation function two times for header and implementation
	 */
	for (generatorPass = header_pass;
	     generatorPass <= source_pass;
	     generatorPass++) {
	    pathname = generateDirectoriesPathname(cls, path);
	    //String pathname = path + filename;
	    // TODO: package, project basepath, tagged values to configure
	    File f = new File(pathname);
	    String headerTop =
		SINGLETON.generateHeaderTop(cls, pathname, packagePath);
	    String header =
		SINGLETON.generateHeader (cls, pathname, packagePath);
	    String src = SINGLETON.generate (cls);
	    BufferedWriter fos = null;
	    try {
		fos = new BufferedWriter (new FileWriter (f));
		writeTemplate(cls, path, fos);
		fos.write(headerTop);
		fos.write(extraIncludes);
		fos.write (header);
		fos.write (src);
	    }
	    catch (IOException exp) { }
	    finally {
		try {
		    if (fos != null) fos.close();
		}
		catch (IOException exp) {
		    LOG.error("FAILED: " + f.getPath());
		}
	    }

	    // clear extra includes after usage for each pass
	    extraIncludes = "";

	    // output lost sections only in the second path
	    // -> sections which are moved from header(inline) to source
	    // file are prevented to be outputted in header pass
	    if (generatorPass == header_pass)	{
		sect.write(pathname, INDENT, false);
	    }
	    else sect.write(pathname, INDENT, true);

	    LOG.info("written: " + pathname);


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

	    LOG.info("----- end updating -----");
	}
   	return pathname;
    }

    /** 2002-12-07 Achim Spangler
     * @return date
     */
    private String getDate() {
	GregorianCalendar cal = new GregorianCalendar();
	DateFormat df;
	df = DateFormat.getDateInstance(DateFormat.DEFAULT);
	return df.format(cal.getTime());
    }
    /** 2002-12-07 Achim Spangler
     * @return year
     */
    private String getYear() {
	GregorianCalendar cal = new GregorianCalendar();
	return Integer.toString(cal.get(Calendar.YEAR));
    }
    /** 2002-12-07 Achim Spangler
     * @return time
     */
    private String getTime() {
	GregorianCalendar cal = new GregorianCalendar();
	DateFormat df;
	df = DateFormat.getTimeInstance(DateFormat.DEFAULT);
	return df.format(cal.getTime());
    }

    /** 2002-12-07 Achim Spangler
     * write template content on top of file
     */
    private void writeTemplate(MClassifier cls, String path, BufferedWriter fos)
    {
	String templatePathName = path + "/templates/";
	String fileName = cls.getName();
	String tagTemplatePathName = cls.getTaggedValue("TemplatePath");
	String authorTag = cls.getTaggedValue("author");
	String emailTag = cls.getTaggedValue("email");
	if (tagTemplatePathName != null && tagTemplatePathName.length() > 0)
	    templatePathName = tagTemplatePathName;
	if (generatorPass == header_pass) {
	    templatePathName = templatePathName + "header_template";
	    fileName = fileName + ".h";
	}
	else {
	    templatePathName = templatePathName + "cpp_template";
	    fileName = fileName + ".cpp";
	}
	// cat.info("Try to read Template: " + templatePathName);
	File templateFile = new File(templatePathName);
	if (templateFile.exists())
	{
	    boolean eof = false;
	    BufferedReader templateFileReader = null;
	    try {
		templateFileReader =
		    new BufferedReader(new FileReader(templateFile.getAbsolutePath()));
		while (!eof) {
		    String lineStr = templateFileReader.readLine();
		    if (lineStr == null) {
			eof = true;
		    } else {
			StringBuffer line = new StringBuffer(lineStr);
                        replaceToken(line, "|FILENAME|", fileName);
                        replaceToken(line, "|DATE|", getDate());
                        replaceToken(line, "|YEAR|", getYear());
                        replaceToken(line, "|AUTHOR|", authorTag);
                        replaceToken(line, "|EMAIL|", emailTag);

			fos.write(line + "\n");
		    }
		}
		templateFileReader.close();
	    }
	    catch (IOException exp) { }
	    finally {
		try {
		    if (templateFileReader != null) templateFileReader.close();
		}
		catch (IOException exp) {
		    LOG.error("FAILED: " + templateFile.getPath());
		}
	    }
	}
    }

    /**
     * Replace the first occurences of tokenName with tokenValue.
     *
     * @param line is the line where we do the replacing.
     * @param tokenName is the string we search for.
     * @param tokenValue is the value we replace.
     */
    private void replaceToken(StringBuffer line,
			      String tokenName, String tokenValue) {
        int tokenStart;
        tokenStart = line.toString().indexOf(tokenName);
	if ((tokenStart != -1)
	    && (tokenValue != null && tokenValue.length() > 0)) {
	    line.replace(tokenStart,
			 tokenStart + tokenName.length(),
			 tokenValue);
	}
    }

    /** 2002-11-28 Achim Spangler
     * seperate constant Header Top into function
     */
    private String generateHeaderTop(MClassifier cls,
				     String pathname,
				     String packagePath)
    {
	StringBuffer sb = new StringBuffer(80);
	//TODO: add user-defined copyright
	if (VERBOSE_DOCS) {
	    sb.append("// FILE: ").append(pathname.replace('\\', '/'));
	    sb.append("\n\n");
	}
	return sb.toString();
    }

    private String generateHeaderImportLine4ItemList(Collection classifiers) {
	StringBuffer sb = new StringBuffer(80);
	Iterator clsEnum = classifiers.iterator();
	while (clsEnum.hasNext()) {
	    sb.append(generateHeaderImportLine4Item((MModelElement) clsEnum.next()));
	}
	return sb.toString();
    }

    /** 2002-11-28 Achim Spangler
     * as each language has its own syntax to incorporate other elements
     * the command for this inclusion is created in a seperate function
     */
    private String generateHeaderImportLine4Item(MModelElement clsDepend)
    {
	// cat.info("generateHeaderImportLine4Item: fuer Item " +
	// clsDepend.getName() + " in Namespace: " +
	// clsDepend.getNamespace().getName());
	StringBuffer sb = new StringBuffer(80);
	String packagePath = clsDepend.getName();
	MNamespace parent = clsDepend.getNamespace().getNamespace();
	if (parent != null) {
	    packagePath =
		clsDepend.getNamespace().getName() + "/" + packagePath;
	    while (parent != null) {
		// ommit root package name; it's the model's root
		if (parent.getNamespace() != null)
		    packagePath = parent.getName() + "/" + packagePath;
	    	// cat.info("generateHeaderImportLine4Item: Runde mit
	    	// Parent" + parent.getName());
    		parent = parent.getNamespace();
	    }
	}

	// if class depends on a list of other classes, all inheritance
	// elements are seperated by Tokens ", " -> use StringTokenizer
	// to access the single elements
	StringTokenizer st = new StringTokenizer(packagePath, ", ");
	while (st.hasMoreTokens()) {
	    sb.append("#include <").append(st.nextToken()).append(".h>\n");
	}
	return sb.toString();
    }

    private boolean checkInclude4UsageIndirection(boolean isIndirect,
						  String usageTag) {
	boolean result = false;
	/*
	  if (usageTag.length() > 0)
	      cat.info("usage tag " + usageTag + " gefunden");
	  if (isIndirect) cat.info("indirection tag gefunden");
	  if (generatorPass == header_pass) cat.info("Header pass");
	*/

	if ((generatorPass != header_pass)
	    && (usageTag.indexOf("source") != -1)) {
	    // generate include line for source .cpp pass only if
	    // element has usage tag which specifies exclusive use in
	    // source file
	    result = true;
	} else if ((generatorPass == header_pass)
		   && (usageTag.indexOf("source") == -1)) {
	    // generate include line for header, if not specified as
	    // only accessed from .cpp
	    result = true;
	}

	// only predeclare candidates can be ignored in include block of header
	if ((generatorPass == header_pass) && (!isIndirect)) result = true;

	return result;
    }

    private boolean checkIncludeNeeded4Element(MAssociation cls) {
	String usageTag = "";
	boolean result = false;
	boolean predeclareCandidate = false;
	Collection tValues = cls.getTaggedValues();
	if (!tValues.isEmpty()) {
	    Iterator iter = tValues.iterator();
	    while (iter.hasNext()) {
		MTaggedValue tv = (MTaggedValue) iter.next();
		String tag = tv.getTag();
		if (tag.equals("usage")) usageTag = tv.getValue();
		// cat.info("Tag fuer: " + cls.getName() + " mit Tag:
		// " + tag + " mit Wert:" + tv.getValue() + ":");

		if (tag.indexOf("ref") != -1 || tag.equals("&")
		    || tag.indexOf("pointer") != -1 || tag.equals("*"))
		    predeclareCandidate = true;
	    }
	}
	return checkInclude4UsageIndirection(predeclareCandidate, usageTag);
    }

    private boolean checkIncludeNeeded4Element(MDependency cls) {
	String usageTag = "";
	boolean result = false;
	boolean predeclareCandidate = false;
	Collection tValues = cls.getTaggedValues();
	if (!tValues.isEmpty()) {
	    Iterator iter = tValues.iterator();
	    while (iter.hasNext()) {
		MTaggedValue tv = (MTaggedValue) iter.next();
		String tag = tv.getTag();
		if (tag.equals("usage")) usageTag = tv.getValue();
		// cat.info("Tag fuer: " + cls.getName() + " mit Tag:
		// " + tag + " mit Wert:" + tv.getValue() + ":");

		if (tag.indexOf("ref") != -1 || tag.equals("&")
		    || tag.indexOf("pointer") != -1 || tag.equals("*"))
		    predeclareCandidate = true;
	    }
	}
	return checkInclude4UsageIndirection(predeclareCandidate, usageTag);
    }
    private boolean checkIncludeNeeded4Element(MAttribute cls) {
	// cat.info("checkIncludeNeeded4Element: fuer Item" + cls);
	if (!(((MAttribute) cls).getType() instanceof MClass)) return false;

	String usageTag = "";
	boolean result = false;
	boolean predeclareCandidate = false;

	Collection tValues = cls.getTaggedValues();
	if (!tValues.isEmpty()) {
	    Iterator iter = tValues.iterator();
	    while (iter.hasNext()) {
		MTaggedValue tv = (MTaggedValue) iter.next();
		String tag = tv.getTag();
		if (tag.equals("usage")) usageTag = tv.getValue();
		// cat.info("Tag fuer: " + cls.getName() + " mit Tag:
		// " + tag + " mit Wert:" + tv.getValue() + ":");

		if (tag.indexOf("ref") != -1 || tag.equals("&")
		    || tag.indexOf("pointer") != -1 || tag.equals("*"))
		    predeclareCandidate = true;
	    }
	}
	return checkInclude4UsageIndirection(predeclareCandidate, usageTag);
    }

    private String generateHeaderDependencies(MClassifier cls)
    {
	StringBuffer sb = new StringBuffer(160);
	StringBuffer predeclareStatements = new StringBuffer(60);

	if (generatorPass != header_pass)
	{ // include header in .cpp
	    sb.append(SINGLETON.generateHeaderImportLine4Item(cls));

	    Collection tValues = cls.getTaggedValues();
	    if (!tValues.isEmpty()) {
		Iterator iter = tValues.iterator();
		while (iter.hasNext()) {
		    MTaggedValue tv = (MTaggedValue) iter.next();
		    String tag = tv.getTag();
		    if (tag.equals("source_incl")
			|| tag.equals("source_include")) {
			sb.append("#include ").append(tv.getValue());
			sb.append("\n");
		    }
		}
	    }
	}
	else {
	    Collection baseClassList =
		getGeneralizationClassList(cls.getGeneralizations());
	    sb.append(generateHeaderImportLine4ItemList(baseClassList));

	    Collection tValues = cls.getTaggedValues();
	    if (!tValues.isEmpty()) {
		Iterator iter = tValues.iterator();
		while (iter.hasNext()) {
		    MTaggedValue tv = (MTaggedValue) iter.next();
		    String tag = tv.getTag();
		    if (tag.equals("header_incl")
			|| tag.equals("header_include")) {
			sb.append("#include ").append(tv.getValue());
			sb.append("\n");
		    }
		}
	    }
	}

	// check if the class has dependencies
	{
	    Collection col =
		UmlHelper.getHelper().getCore().getAssociateEnds(cls);
	    if (col != null) {
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
		    MAssociationEnd ae = (MAssociationEnd) itr.next();
		    if (ae.isNavigable()) {
			MClassifier cls2 = ae.getType();
			String name = cls2.getName();
			String name2 = cls.getName();
			if (name != name2) {
			    if (checkIncludeNeeded4Element(ae.getAssociation())) {
				sb.append(SINGLETON.generateHeaderImportLine4Item(cls2));
			    }
			    else if (generatorPass == header_pass) {
				// predeclare classes which are not
				// directly used in header usefull for
				// classes which are only used
				// indirectly as pointer where no
				// knowledge about internals of class
				// are needed
				predeclareStatements
				    .append(generateHeaderPackageStart(cls2));
				predeclareStatements
				    .append("class ").append(name);
				predeclareStatements.append(";\n");
			    }
			}
		    }
		}
	    }
	}

	{
	    Collection col = UmlHelper.getHelper().getCore().getAttributes(cls);
	    if (col != null) {
		Iterator itr = col.iterator();
    		// cat.info("Attribut gefunden");
		while (itr.hasNext()) {
		    MAttribute attr = (MAttribute) itr.next();
		    // cat.info("untersuche name " + attr.getName() +
		    // " mit Typ: " + attr.getType());
		    if (attr.getType() instanceof MClass) {
			String name = attr.getName();
			if (checkIncludeNeeded4Element(attr)) {
			    sb.append(SINGLETON.generateHeaderImportLine4Item(
                                          attr.getType()));
			}
			else if (generatorPass == header_pass) {
			    // predeclare classes which are not
			    // directly used in header usefull for
			    // classes which are only used indirectly
			    // as pointer where no knowledge about
			    // internals of class are needed
			    predeclareStatements
				.append(generateHeaderPackageStart(
                                     attr.getType()))
				.append("class ").append(name).append(";\n");
			}
		    }
		}
	    }
	}

	{
	    Collection col = cls.getClientDependencies();
	    // cat.info("col: " + col);
	    if (col != null) {
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
		    MDependency dep = (MDependency) itr.next();
		    if (dep == null) break;
		    Collection clientsCol = dep.getSuppliers();
		    if (clientsCol == null) break;
		    Iterator itr2 = clientsCol.iterator();
		    MClassifier temp;
		    while (itr2.hasNext()) {
			temp = (MClassifier) itr2.next();
			if (checkIncludeNeeded4Element(dep)) {
			    sb.append(SINGLETON.generateHeaderImportLine4Item(temp));
			}
			else if (generatorPass == header_pass) {
			    // predeclare classes which are not
			    // directly used in header usefull for
			    // classes which are only used indirectly
			    // as pointer where no knowledge about
			    // internals of class are needed
			    predeclareStatements
				.append(generateHeaderPackageStart(temp));
			    predeclareStatements.append("class ");
			    predeclareStatements.append(temp.getName());
			    predeclareStatements.append(";\n");
			}
		    }
		}
	    }
	}

	if (predeclareStatements.toString().length() > 0) {
	    sb.append("\n\n").append(predeclareStatements.toString());
	}
	return sb.toString();
    }

    private String generateHeaderPackageStartSingle(MNamespace pkg)
    {
	// cat.info("generateHeaderPackageStartSingle: " + pkg.getName());
	StringBuffer sb = new StringBuffer(30);
	StringTokenizer st = new StringTokenizer(pkg.getName(), ".");
	String token = "";

	sb.append(generateTaggedValues(pkg, DocCommentTags));
	while (st.hasMoreTokens()) {
	    token = st.nextToken();
	    // create line: namespace FOO {"
	    sb.append("namespace ").append(token).append(" {\n");
	}
	return sb.toString();
    }

    private String generateHeaderPackageEndSingle(MNamespace pkg)
    {
	// cat.info("generateHeaderPackageEndSingle: " + pkg.getName());
	StringBuffer sb = new StringBuffer(30);
	StringTokenizer st = new StringTokenizer(pkg.getName(), ".");
	String token = "";
	while (st.hasMoreTokens()) {
	    token = st.nextToken();
	    StringBuffer tempBuf = new StringBuffer(20);
	    String absoluteName = generatePackageAbsoluteName(pkg);
	    if (absoluteName.indexOf(token) != -1)
	    {
		absoluteName =
		    absoluteName.substring(0,
					   (absoluteName.indexOf(token)
					    + token.length()));
	    }

	    // create line: namespace FOO {"
	    tempBuf.append("} /* End of namespace ").append(absoluteName);
	    tempBuf.append(" */\n");
	    sb.insert(0, tempBuf.toString());
	}
	return sb.toString();
    }

    private String generatePackageAbsoluteName(MNamespace pkg)
    {
	// cat.info("generatePackageAbsoluteName: " + pkg.getName());
	StringBuffer sb = new StringBuffer(30);
	String token = "";
	for (MNamespace actual = pkg;
	     actual != null;
	     actual = actual.getNamespace()) {
	    StringTokenizer st = new StringTokenizer(actual.getName(), ".");
	    StringBuffer tempBuf = new StringBuffer(20);
	    while (st.hasMoreTokens()) {
		token = st.nextToken();
		if (tempBuf.length() > 0) tempBuf.append("::");
		tempBuf.append(token);
	    }
	    if ((tempBuf.length() > 0) && (sb.length() > 0)) {
		tempBuf.append("::");
	    }
	    sb.insert(0, tempBuf.toString());
	}
	return sb.toString();
    }

    private String generateNameWithPkgSelection(MModelElement item,
						MNamespace localPkg) {
	if (item == null) {
	    // cat.info("generateNameWithPkgSelection: zu void");
	    return "void ";
	}
	// cat.info("generateNameWithPkgSelection: " + item.getName());
	MNamespace pkg = null;
	if (item instanceof MDataType) {
	    return generateName(item.getName());
	} else if (item instanceof MParameter) {
	    pkg = ((MParameter) item).getNamespace();
	} else if (item instanceof MAttribute) {
	    pkg = ((MAttribute) item).getNamespace();
	} else if (item instanceof MAssociationEnd) {
	    pkg = ((MAssociationEnd) item).getNamespace();
	} else if (item instanceof MClassifier) {
	    pkg = ((MClassifier) item).getNamespace();
	}

	if (pkg == null) {
	    return generateName(item.getName());
	}
	if (localPkg == null) {
	    LOG.info("LOCAL NAMESPACE IS NULL");
	}

	String localPkgName = generatePackageAbsoluteName(localPkg);
	String targetPkgName = generatePackageAbsoluteName(pkg);
	// cat.info("targetNamespace:" + targetPkgName + ":");
	// cat.info("localNamespace:" + localPkgName + ":");
	int localPkgNameLen = localPkgName.length();
	int targetPkgNameLen = targetPkgName.length();
	if (localPkgName.equals(targetPkgName)) {
	    return generateName(item.getName());
	} else {
	    if (targetPkgName.indexOf(localPkgName) != -1) {
		/*
		  cat.info("target is subpackage of local with |" +
		  targetPkgName.substring(localPkgNameLen,
		  localPkgNameLen+2) + "|");
		*/
		if (targetPkgName.substring(localPkgNameLen,
					    localPkgNameLen + 2)
		    .equals("::")) {
		    // target is in Sub-Package of local class
		    return (targetPkgName.substring(localPkgNameLen + 2,
						    targetPkgNameLen)
			    + "::"
			    + generateName(item.getName()));
		}
	    }
	}
	return (targetPkgName + "::" + generateName(item.getName()));
    }

    private String generateNameWithPkgSelection(MModelElement item) {
	return generateNameWithPkgSelection(item, actualNamespace);
    }

    private String generateHeaderPackageStart(MClassifier cls)
    {
	// cat.info("generateHeaderPackageStart: " + cls.getName() + "
	// aus Namespace: " + cls.getNamespace().getName());
	StringBuffer sb = new StringBuffer(80);

	if (actualNamespace != null)
	{
	    for (MNamespace fromSearch = actualNamespace;
		 fromSearch != null;
		 fromSearch = fromSearch.getNamespace())
	    {
		// cat.info("fromSearch: " + fromSearch.getName());
		StringBuffer contPath = new StringBuffer(80);
		MNamespace toSearch = cls.getNamespace();
		for (;
		     (toSearch != null) && (toSearch != fromSearch);
		     toSearch = toSearch.getNamespace())
		{
		    // cat.info("toSearch: " + toSearch.getName());
		    contPath.insert(0,
				    generateHeaderPackageStartSingle(toSearch));
		}
		if (toSearch == fromSearch)
		{
		    sb.append(contPath.toString());
		    break;
		}
		else {
		    // close one namespace
		    sb.append(generateHeaderPackageEndSingle(fromSearch));
		}
	    }
	}
	else { // initial start
	    for (MNamespace toSearch = cls.getNamespace();
		 toSearch != null;
		 toSearch = toSearch.getNamespace())
	    {
		sb.insert(0, generateHeaderPackageStartSingle(toSearch));
	    }
	}
	if (sb.length() > 0) sb.insert(0, "\n").append("\n");
	actualNamespace = cls.getNamespace();
	return sb.toString();
    }

    private String generateHeaderPackageEnd() {
	StringBuffer sb = new StringBuffer(20);

	for (MNamespace closeIt = actualNamespace;
	     closeIt != null;
	     closeIt = closeIt.getNamespace())
	{
	    sb.append(generateHeaderPackageEndSingle(closeIt));
	}
	actualNamespace = null;
	if (sb.length() > 0) sb.insert(0, "\n").append("\n");
	return sb.toString();
    }

    public String generateHeader(MClassifier cls,
				 String pathname,
				 String packagePath) {
	StringBuffer sb = new StringBuffer(240);

	// check if the class has a base class
	sb.append(SINGLETON.generateHeaderDependencies(cls));

	sb.append("\n");

	if (packagePath.length() > 0) {
	    sb.append(SINGLETON.generateHeaderPackageStart(cls));
	}

	return sb.toString();
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
    public String generateExtensionPoint (Object ep) {
	return null;
    }

    public String generateAssociationRole(Object m) {
	return "";
    }

    /** 2002-11-28 Achim Spangler
     * seperate generation of Operation Prefix from generateOperation
     * so that generateOperation is language independent
     */
    private String generateOperationPrefix(MOperation op) {
	StringBuffer sb = new StringBuffer(80);
	sb.append(generateConcurrency(op));
	if (generatorPass == header_pass) {
	    // make all operations to virtual - as long as they are not "leaf"
	    MScopeKind scope = op.getOwnerScope();
	    // generate a function as virtual, if it can be overriden
	    // or override another function AND if this function is
	    // not marked as static, which disallows "virtual"
	    // alternatively every abstract function is defined as
	    // virtual
	    if ((!op.isLeaf()
		 && !op.isRoot()
		 && (!(MScopeKind.CLASSIFIER.equals(scope))))
		|| (op.isAbstract())) {
		sb.append("virtual ");
	    }
	    sb.append(generateScope(op));
	}
	return sb.toString();
    }

    /** 2002-11-28 Achim Spangler
     * seperate generation of Operation Suffix from generateOperation
     * so that generateOperation is language independent
     */
    private String generateOperationSuffix(MOperation op) {
	StringBuffer sb = new StringBuffer(80);
	sb.append(generateChangeability(op));
	sb.append(generateAbstractness(op));
	return sb.toString();
    }

    /** 2002-11-28 Achim Spangler
     * seperate generation of Operation Name from generateOperation
     * so that generateOperation is language independent
     * -> for C++: if we create .cpp we must prepend Owner name
     */
    private boolean generateOperationNameAndTestForConstructor(MOperation op,
							       StringBuffer nameStr)
    {
	// cat.info("generate Operation for File" + generatorPass + "
	// fuer Op: " + op.getName());
	if (generatorPass != header_pass)
	{
	    // cat.info("generate Operation for CPP File");
	    nameStr.append(op.getOwner().getName())
		.append("::");
	}
	boolean constructor = false;
	MStereotype stereo = op.getStereotype();
	if (stereo != null && stereo.getName().equals("create")) {
	    // constructor
	    nameStr.append(generateName (op.getOwner().getName()));
	    constructor = true;
	} else {
	    // cat.info("generate Operation for File" + generatorPass
	    // + " fuer Op: " + op.getName());
	    nameStr.append(generateName (op.getName()));
	}
	return constructor;
    }

    /** 2002-11-28 Achim Spangler
     * modified version from Jaap Branderhorst
     * -> generateOperation is language independent and seperates
     *    different tasks
     */
    public String generateOperation(Object handle, boolean documented) {
	MOperation op = (MOperation)handle;
	// generate nothing for abstract functions, if we generate the
	// source .cpp file at the moment
	if ((generatorPass != header_pass) && (op.isAbstract())) {
	    return "";
	}
	StringBuffer sb = new StringBuffer(80);
	StringBuffer nameBuffer = new StringBuffer(20);
	String operationIndent = (generatorPass == header_pass) ? INDENT : "";
	// cat.info("generate Operation for File" + generatorPass + "
	// fuer Op: " + op.getName());
	boolean constructor =
	    SINGLETON.generateOperationNameAndTestForConstructor(op,
								 nameBuffer);

	sb.append('\n'); // begin with a blank line
	// generate DocComment from tagged values
	String tv = generateTaggedValues (op, DocCommentTags);
	if (tv != null && tv.length() > 0) {
	    sb.append ("\n").append(operationIndent).append (tv);
	}

	// 2002-07-14
	// Jaap Branderhorst
	// missing concurrency generation
	sb.append(operationIndent)
	    .append(SINGLETON.generateOperationPrefix(op));

	// pick out return type
	MParameter rp = UmlHelper.getHelper().getCore().getReturnParameter(op);
	if (rp != null) {
	    MClassifier returnType = rp.getType();
	    if (returnType == null && !constructor) {
		sb.append("void ");
	    }
	    else if (returnType != null) {
		sb.append(generateNameWithPkgSelection(returnType)).append(' ');
	    }
	}

	// name and params
	Vector params = new Vector (op.getParameters());
	params.remove (rp);

	sb.append(nameBuffer.toString()).append('(');

	if (params != null) {
	    boolean first = true;

	    for (int i = 0; i < params.size(); i++) {
		MParameter p = (MParameter) params.elementAt (i);

		if (!first) sb.append(", ");

		sb.append(generateParameter(p));
		first = false;
	    }
	}

	sb.append(") ")
	    .append(SINGLETON.generateOperationSuffix(op));

	return sb.toString();

    }

    /** 2002-12-06 Achim Spangler
     * check if a parameter is tagged as pointer or reference (not
     * part of UML - as far as author knows - but important for C++
     * developers)
     * @param elem element to check
     * @param tag_type tag type to check
     */
    private boolean checkAttributeParameter4Tag(MModelElement elem,
						int tagType) {
	// first check whether the parameter shall be a pointer of reference
  	Collection tValues = elem.getTaggedValues();
  	if (!tValues.isEmpty()) {
	    Iterator iter = tValues.iterator();
	    while (iter.hasNext()) {
		MTaggedValue tv = (MTaggedValue) iter.next();
		String tag = tv.getTag();
		if ((tag.indexOf("ref") != -1 || tag.equals("&"))
		    && (tagType != SearchPointerTag)) {
		    return true;
		} else if ((tag.indexOf("pointer") != -1 || tag.equals("*"))
			   && (tagType != SearchReferenceTag)) {
		    return true;
		}
	    }
	}
	return false;
    }


    private String generateAttributeParameterModifier(MModelElement attr) {
	boolean isReference =
	    checkAttributeParameter4Tag(attr, SearchReferenceTag);
	boolean isPointer = checkAttributeParameter4Tag(attr, SearchPointerTag);
	StringBuffer sb = new StringBuffer(2);

	if (isReference) sb.append("&");
	else if (isPointer) sb.append("*");
	else if (attr instanceof MParameter) {
	    if (((((MParameter) attr).getKind()).equals(MParameterDirectionKind.OUT))
		|| ((((MParameter) attr).getKind()).equals(MParameterDirectionKind.INOUT)))
	    { // out or inout parameters are defaulted to reference if
	      // not specified else
		sb.append("&");
	    }
	}

	return sb.toString();
    }

    public String generateAttribute (Object handle, boolean documented) {
	MAttribute attr = (MAttribute)handle;
	StringBuffer sb = new StringBuffer(80);
	sb.append('\n'); // begin with a blank line

	// list tagged values for documentation
	String tv = generateTaggedValues (attr, DocCommentTags);
	if (tv != null && tv.length() > 0) {
	    sb.append ("\n").append (INDENT).append (tv);
	}


	sb.append(INDENT);
	// cat.info("generate Visibility for Attribute");
	sb.append(generateVisibility(attr));
	sb.append(generateScope(attr));
	sb.append(generateChangability(attr));
	/*
         * 2002-07-14
         * Jaap Branderhorst
         * Generating the multiplicity should not lead to putting the
         * range in the generated code (no 0..1 as modifier)
         * Therefore removed the multiplicity generation
         * START OLD CODE

	 if (!MMultiplicity.M1_1.equals(attr.getMultiplicity()))
	 {
	 String m = generateMultiplicity(attr.getMultiplicity());
	 if (m != null && m.trim().length() > 0)
	 sb.append(m).append(' ');
	 }
        */
        // END OLD CODE
	/*
	  MClassifier type = attr.getType();
	  MMultiplicity multi = attr.getMultiplicity();
	  // handle multiplicity here since we need the type
	  // actually the API of generator is buggy since to generate
	  // multiplicity correctly we need the attribute too
	  if (type != null && multi != null) {
	  if (multi.equals(MMultiplicity.M1_1)) {
	  sb.append(generateClassifierRef(type)).append(' ');
	  } else
	  if (type instanceof MDataType) {
	  sb.append(generateClassifierRef(type)).append("[] ");
	  } else
	  sb.append("java.util.Vector ");
	  }

	  sb.append(generateAttributeParameterModifier(attr));
	  sb.append(generateName(attr.getName()));
	*/
	sb.append(generateMultiplicity(attr,
				       generateName(attr.getName()),
				       attr.getMultiplicity(),
				       generateAttributeParameterModifier(attr)));
	MExpression init = attr.getInitialValue();
	if (init != null) {
	    String initStr = generateExpression(init).trim();
	    if (initStr.length() > 0)
		sb.append(" = ").append(initStr);
	}

	sb.append(";\n");

	return sb.toString();
    }


    public String generateParameter(Object handle) {
	MParameter param = (MParameter)handle;
	StringBuffer sb = new StringBuffer(20);
	//TODO: qualifiers (e.g., const)
	// generate const for references or pointers which are
	// defined as IN - other qualifiers are not important for
	// C++ parameters
	sb.append(generateChangeability(param));
	//TODO: stereotypes...
	sb.append(generateNameWithPkgSelection(param.getType())).append(' ');
	sb.append(generateAttributeParameterModifier(param));
	sb.append(generateName(param.getName()));

	// insert default value, if we are generating the header
	if ((generatorPass == header_pass)
	    && (param.getDefaultValue() != null)) {
	    sb.append(" = ").append(param.getDefaultValue());
	}

	return sb.toString();
    }


    public String generatePackage(Object handle) {
	MPackage p =(MPackage)handle;
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
     * Generate the start sequence for a classifier. The start sequence is
     * everything from the preceding javadoc comment to the opening curly brace.
     * Start sequences are non-empty for classes and interfaces only.
     *
     * This method is intented for package internal usage only.
     *
     * @param cls the classifier for which to generate the start sequence
     *
     * @return the generated start sequence
     */
    StringBuffer generateClassifierStart (MClassifier cls) {
	StringBuffer sb = new StringBuffer (80);

	// don't create class-Start for implementation in .cpp
	if (generatorPass != header_pass) return sb;

	String sClassifierKeyword;
	if (cls instanceof MClass) sClassifierKeyword = "class";
	else if (cls instanceof MInterface) sClassifierKeyword = "class";
	else return null; // actors, use cases etc.
	boolean hasBaseClass = false;

	// Add the comments for this classifier first.
	sb.append ('\n')
	    .append (DocumentationManager.getComments(cls));

	// list tagged values for documentation
	String tv = generateTaggedValues (cls, DocCommentTags);
	if (tv != null && tv.length() > 0) {
	    sb.append ("\n").append (INDENT).append (tv);
	}

	// add classifier keyword and classifier name
	sb.append(sClassifierKeyword).append(" ");
	sb.append(generateName (cls.getName()));

	// add base class/interface
	String baseClass = generateGeneralization (cls.getGeneralizations());
	if (!baseClass.equals ("")) {
	    sb.append (" : ")
		.append (baseClass);
	    hasBaseClass = true;
	}

	// add implemented interfaces, if needed
	// nsuml: realizations!
	if (cls instanceof MClass) {
	    String interfaces = generateSpecification ((MClass) cls);
	    if (!interfaces.equals ("")) {
		if (!hasBaseClass) sb.append (" : ");
		else sb.append (", ");
		sb.append (interfaces);
	    }
	}

	// add opening brace
	sb.append(_lfBeforeCurly ? "\n{" : " {");

	// list tagged values for documentation
	tv = generateTaggedValues (cls, AllButDocTags);
	if (tv != null && tv.length() > 0) {
	    sb.append("\n").append (INDENT).append (tv);
	}

	return sb;
    }

    protected StringBuffer generateClassifierEnd(MClassifier cls) {
	StringBuffer sb = new StringBuffer();
	if (cls instanceof MClass || cls instanceof MInterface)
	{
	    if ((_verboseDocs) && (generatorPass == header_pass))
	    {
		String classifierkeyword = null;
		if (cls instanceof MClass)
		{
		    classifierkeyword = "class";
		}
		else
		{
		    classifierkeyword = "class";
		}
		sb.append(
			  "\n//end of "
			  + classifierkeyword
			  + " "
			  + cls.getName()
			  + "\n");
	    }
	    if (generatorPass == header_pass) sb.append("};\n");
	    sb.append(generateHeaderPackageEnd());
	}
	return sb;
    }

    /**
     * Generates code for a classifier. In case of Java code is
     * generated for classes and interfaces only at the moment.
     *
     * @see org.argouml.application.api.NotationProvider#generateClassifier(
     *          MClassifier)
     */
    public String generateClassifier(Object handle)
    {
  	MClassifier cls = (MClassifier)handle;
  	StringBuffer returnValue = new StringBuffer();
	StringBuffer start = generateClassifierStart(cls);
  	if (((start != null) && (start.length() > 0))
	    || (generatorPass != header_pass)) {
	    StringBuffer typedefs = generateGlobalTypedefs(cls);
	    StringBuffer body = generateClassifierBody(cls);
	    StringBuffer end = generateClassifierEnd(cls);
	    returnValue.append((typedefs != null) ? typedefs.toString() : "");
	    returnValue.append(start);
	    if ((body != null) && (body.length() > 0))
	    {
		returnValue.append("\n");
		returnValue.append(body);
		if (_lfBeforeCurly)
		{
		    returnValue.append("\n");
		}
	    }
	    returnValue.append((end != null) ? end.toString() : "");
	}
  	return returnValue.toString();
    }

    /** 2002-12-12 Achim Spangler
     * generate global typedefs
     */
    private StringBuffer generateGlobalTypedefs(MClassifier cls) {
  	StringBuffer sb = new StringBuffer();
  	if (cls instanceof MClass || cls instanceof MInterface)
	{ // add typedefs
	    if (generatorPass == header_pass) {
		sb.append("// global type definitions for header defined "
			  + "by Tag entries in ArgoUML\n");
		sb.append("// Result: typedef <typedef_global_header> "
			  + "<tag_value>;\n");
		Collection globalTypedefStatements =
		    findTagValues(cls, "typedef_global_header");
		if (!globalTypedefStatements.isEmpty()) {
		    Iterator typedefEnum =
			globalTypedefStatements.iterator();
		    while (typedefEnum.hasNext()) {
			sb.append("typedef ").append(typedefEnum.next());
			sb.append(";\n");
		    }
		}
	    }
	    else {
		sb.append("// global type definitions for class implementation "
			  + "in source file defined "
			  + "by Tag entries in ArgoUML\n");
		sb.append("// Result: typedef <typedef_global_source> "
			  + "<tag_value>;\n");
		Collection globalTypedefStatements =
		    findTagValues(cls, "typedef_global_source");
		if (!globalTypedefStatements.isEmpty()) {
		    Iterator typedefEnum = globalTypedefStatements.iterator();
		    while (typedefEnum.hasNext()) {
			sb.append("typedef ").append(typedefEnum.next());
			sb.append(";\n");
		    }
		}
	    }
	}
	return sb;
    }

    /**
     * Generates the attributes of the body of a class or interface.
     * @param cls
     * @return StringBuffer
     */
    private void generateClassifierBodyAttributes(MClassifier cls,
						  StringBuffer sb)
    {
	Collection strs = UmlHelper.getHelper().getCore().getAttributes(cls);
	if (strs.isEmpty() || (generatorPass != header_pass)) return;
   	String tv = null; // helper for tagged values
	//
	// 2002-06-08
	// Jaap Branderhorst
	// Bugfix: strs is never null. Should check for isEmpty instead
	// old code:
	// if (strs != null)
	// new code:
	//
	sb.append('\n');
	if (_verboseDocs && cls instanceof MClass)
	{
	    sb.append(INDENT).append("// Attributes\n");
	}

	// generate attributes in order public, protected, private
	for (int i = 0; i < ALL_PARTS.length; i++) {
	    int publicProtectedPrivate = ALL_PARTS[i];

	    Iterator strEnum = strs.iterator();
	    boolean isVisibilityLinePrinted = false;
	    while (strEnum.hasNext())
	    {
		MStructuralFeature sf = (MStructuralFeature) strEnum.next();
		MVisibilityKind vis = sf.getVisibility();
		if (((publicProtectedPrivate == PUBLIC_PART)
		     && (MVisibilityKind.PUBLIC.equals(vis)))
		    || ((publicProtectedPrivate == PROTECTED_PART)
			&& (MVisibilityKind.PROTECTED.equals(vis)))
		    || ((publicProtectedPrivate == PRIVATE_PART)
			&& (MVisibilityKind.PRIVATE.equals(vis)))) {
		    if (!isVisibilityLinePrinted) {
			isVisibilityLinePrinted = true;
			if (publicProtectedPrivate == PUBLIC_PART) {
			    sb.append("\n public:");
			} else if (publicProtectedPrivate == PROTECTED_PART) {
			    sb.append("\n protected:");
			} else if (publicProtectedPrivate == PRIVATE_PART) {
			    sb.append("\n private:");
			}
		    }
		    sb.append(generate((MAttribute) sf));

		    tv = generateTaggedValues(sf, AllButDocTags);
		    if (tv != null && tv.length() > 0)
		    {
			sb.append(INDENT).append(tv);
		    }
		}
	    }
	}
    }

    /**
     * Generates the association ends of the body of a class or interface.
     * @param cls
     * @return StringBuffer
     */
    private void generateClassifierBodyAssociations(MClassifier cls,
						    StringBuffer sb)
    {
	Collection ends = cls.getAssociationEnds();
	if (ends.isEmpty() || (generatorPass != header_pass)) return;
   	String tv = null; // helper for tagged values
	// 2002-06-08
	// Jaap Branderhorst
	// Bugfix: ends is never null. Should check for isEmpty instead
	// old code:
	// if (ends != null)
	// new code:
	sb.append('\n');
	if (_verboseDocs && cls instanceof MClass)
        {
	    sb.append(INDENT).append("// Associations\n");
	}

	// generate attributes in order public, protected, private
	for (int i = 0; i < ALL_PARTS.length; i++) {
	    int publicProtectedPrivate = ALL_PARTS[i];
	    Iterator endEnum = ends.iterator();
	    boolean isVisibilityLinePrinted = false;
	    while (endEnum.hasNext())
	    {
		MAssociationEnd ae = (MAssociationEnd) endEnum.next();
		MAssociation a = ae.getAssociation();
		MVisibilityKind vis = ae.getVisibility();
		if (((publicProtectedPrivate == PUBLIC_PART)
		     && (MVisibilityKind.PUBLIC.equals(vis)))
		    || ((publicProtectedPrivate == PROTECTED_PART)
			&& (MVisibilityKind.PROTECTED.equals(vis)))
		    || ((publicProtectedPrivate == PRIVATE_PART)
			&& (MVisibilityKind.PRIVATE.equals(vis)))) {
		    if (!isVisibilityLinePrinted) {
			isVisibilityLinePrinted = true;
			if (publicProtectedPrivate == PUBLIC_PART) {
			    sb.append("\n public:");
			} else if (publicProtectedPrivate == PROTECTED_PART) {
			    sb.append("\n protected:");
			} else if (publicProtectedPrivate == PRIVATE_PART) {
			    sb.append("\n private:");
			}
		    }

		    sb.append(generateAssociationFrom(a, ae));

		    tv = generateTaggedValues(a, AllButDocTags);
		    if (tv != null && tv.length() > 0)
		    {
			sb.append(INDENT).append(tv);
		    }
		}
	    }
	}
    }

    /**
     * Check whether an operation body shall be generated within the actual
     * pass. This is normally done during the imeplementation path.
     * But if the Tag "inline" exists, the method body shall be defined as
     * as inline in header file
     * @return true -> generate body in actual path
     */
    private boolean checkGenerateOperationBody(MOperation cls)
    {
	boolean result =
	    !((generatorPass == header_pass) || (cls.isAbstract()));

	// if this operation has Tag "inline" the method shall be
	// generated in header
	Collection tValues = cls.getTaggedValues();
	if (!tValues.isEmpty()) {
	    Iterator iter = tValues.iterator();
	    while (iter.hasNext()) {
		MTaggedValue tv = (MTaggedValue) iter.next();
		String tag = tv.getTag();
		if (tag.equals("inline")) {
		    result = (generatorPass == header_pass) ? true : false;
		}
	    }
	}
	return result;
    }

    /** 2002-12-13 Achim Spangler
     * generate a single set function for a given attribute and StringBuffer
     */
    private void generateSingleAttributeSet(MAttribute attr, StringBuffer sb) {
	if (attr.getType() == null) return;
	// generate for attributes with class-type:
	// "INDENT void set_<name>( const <type> &value ) { <name> = value; };"
	// generate for other (small) data types:
	// "INDENT void set_<name>( <type> value ) { <name> = value; };"
	// generate: "INDENT void set_<name>( "
	sb.append('\n').append(INDENT);
	sb.append("/** simple access function to set the attribute ");
	sb.append(attr.getName());
	sb.append(" by function\n").append(INDENT);
	sb.append("  * @param value value to set for the attribute ");
	sb.append(attr.getName()).append("\n").append(INDENT).append("  */\n");
	sb.append(INDENT);
	sb.append("void set_").append(attr.getName()).append("( ");
	String modifier = generateAttributeParameterModifier(attr);
	if (modifier != null && modifier.length() > 0) {
	    // generate: "const <type> <modifier>value"
	    if (modifier.equals("&")) sb.append("const ");
	    sb.append(generateClassifierRef(attr.getType()))
		.append(' ').append(modifier).append("value");
	}
	else if (attr.getType() instanceof MClass) {
	    // generate: "const <type> &value"
	    sb.append("const ").append(generateClassifierRef(attr.getType()));
	    sb.append(" &value");
	} else {
	    // generate: "<type> value"
	    sb.append(generateClassifierRef(attr.getType()))
		.append(" value");
	}
	// generate: " ) { <name> = value; };"
	sb.append(" ) { ").append(attr.getName()).append(" = value; };");
    }

    /** 2002-12-13 Achim Spangler
     * generate a single get function for a given attribute and StringBuffer
     */
    private void generateSingleAttributeGet(MAttribute attr, StringBuffer sb) {
	if (attr.getType() == null) return;
	// generate for attributes with class-type:
	// "const <type>& get_<name>( void ) { return <name>; };"
	// generate for other (small) data types
	// "<type> get_<name>( void ) { return <name>; };"
	// generate: "INDENT"
	sb.append('\n').append(INDENT);
	sb.append("/** simple access function to get the attribute ");
	sb.append(attr.getName());
	sb.append(" by function */\n").append(INDENT);
	String modifier = generateAttributeParameterModifier(attr);
	if (modifier != null && modifier.length() > 0)
	{
	    // generate: "const <type><modifier>"
	    sb.append("const ").append(generateClassifierRef(attr.getType()));
	    sb.append(modifier);
	}
	else if (attr.getType() instanceof MClass) {
	    // generate: "const <type>&"
	    sb.append("const ").append(generateClassifierRef(attr.getType()));
	    sb.append("&");
	} else {
	    // generate: "<type>"
	    sb.append(generateClassifierRef(attr.getType()));
	}
	// generate: " get_<name>( void ) const { return <name>; };"
	sb.append(" get_").append(attr.getName());
	sb.append("( void ) const { return ").append(attr.getName());
	sb.append("; };");
    }

    /**
     * Generates the attributes of the body of a class or interface.
     * @param cls
     * @return StringBuffer
     */
    private void generateClassifierBodyTaggedAccess4Attributes(MClassifier cls,
							       StringBuffer funcPrivate,
							       StringBuffer funcProtected,
							       StringBuffer funcPublic)
    {
	Collection strs = UmlHelper.getHelper().getCore().getAttributes(cls);
	if (strs.isEmpty() || (generatorPass != header_pass)) return;
	String accessTag = null;

	Iterator strEnum = strs.iterator();
	while (strEnum.hasNext())
	{
	    MAttribute attr = (MAttribute) strEnum.next();
	    accessTag = attr.getTaggedValue("set");
	    if (accessTag != null && accessTag.length() > 0) {
		if (accessTag.indexOf("public") != -1) {
		    generateSingleAttributeSet(attr, funcPublic);
		}
		if (accessTag.indexOf("protected") != -1) {
		    generateSingleAttributeSet(attr, funcProtected);
		}
		if (accessTag.indexOf("private") != -1) {
		    generateSingleAttributeSet(attr, funcPrivate);
		}
	    }

	    accessTag = attr.getTaggedValue("get");
	    if (accessTag != null && accessTag.length() > 0) {
		if (accessTag.indexOf("public") != -1) {
		    generateSingleAttributeGet(attr, funcPublic);
		}
		if (accessTag.indexOf("protected") != -1) {
		    generateSingleAttributeGet(attr, funcProtected);
		}
		if (accessTag.indexOf("private") != -1) {
		    generateSingleAttributeGet(attr, funcPrivate);
		}
	    }
	}
    }

    /**
     * Generates the association ends of the body of a class or interface.
     * @param cls
     * @return StringBuffer
     */
    private void generateClassifierBodyOperations(MClassifier cls,
						  StringBuffer sb)
    {
	Collection behs = UmlHelper.getHelper().getCore().getOperations(cls);
	if (behs.isEmpty()) return;
   	String tv = null; // helper for tagged values
	//
	// 2002-06-08
	// Jaap Branderhorst
	// Bugfix: behs is never null. Should check for isEmpty instead
	// old code:
	// if (behs != null)
	// new code:
	//
	sb.append('\n');
	if (_verboseDocs)
	{
	    sb.append(INDENT).append("// Operations\n");
	}

	// generate tag controlled access functions for attributes
	StringBuffer funcPrivate = new StringBuffer(80);
	StringBuffer funcProtected = new StringBuffer(80);
	StringBuffer funcPublic = new StringBuffer(80);
	generateClassifierBodyTaggedAccess4Attributes(cls,
						      funcPrivate,
						      funcProtected,
						      funcPublic);

	// generate attributes in order public, protected, private
	for (int i = 0; i < ALL_PARTS.length; i++) {
	    int publicProtectedPrivate = ALL_PARTS[i];
	    Iterator behEnum = behs.iterator();
	    boolean isVisibilityLinePrinted = false;

	    if ((publicProtectedPrivate == PRIVATE_PART)
		&& (funcPrivate.length() > 0)) {
		sb.append("\n private:").append(funcPrivate.toString());
		isVisibilityLinePrinted = true;
	    }
	    if ((publicProtectedPrivate == PROTECTED_PART)
		&& (funcProtected.length() > 0)) {
		sb.append("\n protected:").append(funcProtected.toString());
		isVisibilityLinePrinted = true;
	    }
	    if ((publicProtectedPrivate == PUBLIC_PART)
		&& (funcPublic.length() > 0)) {
		sb.append("\n public:").append(funcPublic.toString());
		isVisibilityLinePrinted = true;
	    }

	    while (behEnum.hasNext())
	    {
		MBehavioralFeature bf = (MBehavioralFeature) behEnum.next();
		MVisibilityKind vis = bf.getVisibility();
		if ((((publicProtectedPrivate == PUBLIC_PART)
		      && (MVisibilityKind.PUBLIC.equals(vis)))
		     || ((publicProtectedPrivate == PROTECTED_PART)
			 && (MVisibilityKind.PROTECTED.equals(vis)))
		     || ((publicProtectedPrivate == PRIVATE_PART)
			 && (MVisibilityKind.PRIVATE.equals(vis))))
		    && ((generatorPass == header_pass)
			|| (checkGenerateOperationBody((MOperation) bf)))) {
		    if ((!isVisibilityLinePrinted)
			&& (generatorPass == header_pass)) {
			isVisibilityLinePrinted = true;
			if (publicProtectedPrivate == PUBLIC_PART) {
			    sb.append("\n public:");
			} else if (publicProtectedPrivate == PROTECTED_PART) {
			    sb.append("\n protected:");
			} else if (publicProtectedPrivate == PRIVATE_PART) {
			    sb.append("\n private:");
			}
		    }

		    sb.append(generate(bf));

		    tv =
			generateTaggedValues((MModelElement) bf,
					     AllButDocTags);

		    if ((cls instanceof MClass)
			&& (bf instanceof MOperation)
			&& (!((MOperation) bf).isAbstract())
			&& (checkGenerateOperationBody((MOperation) bf)))
		    {
			// there is no ReturnType in behavioral feature (nsuml)
			sb.append("\n")
			    .append(generateMethodBody((MOperation) bf));
		    }
		    else
		    {
			sb.append(";\n");
			if (tv.length() > 0)
			{
			    sb.append(INDENT).append(tv).append('\n');
			}
		    }
		}
	    }
	}
    }

    /**
     * Generates the association ends of the body of a class or interface.
     * @param cls
     * @return StringBuffer
     */
    private void generateClassifierBodyTypedefs(MClassifier cls,
						StringBuffer sb)
    {
	if (generatorPass == header_pass) {
	    Collection publicTypedefStatements =
		findTagValues(cls, "typedef_public");
	    Collection protectedTypedefStatements =
		findTagValues(cls, "typedef_protected");
	    Collection privateTypedefStatements =
		findTagValues(cls, "typedef_private");
	    if (!publicTypedefStatements.isEmpty()) {
		sb.append("\n public:\n").append(INDENT);
		sb.append("// public type definitions for header defined "
			  + "by Tag entries in ArgoUML\n");
		sb.append(INDENT);
		sb.append("// Result: typedef <typedef_public> "
			  + "<tag_value>;\n");
    		Iterator typedefEnum = publicTypedefStatements.iterator();

    		while (typedefEnum.hasNext()) {
		    sb.append(INDENT).append("typedef ");
		    sb.append(typedefEnum.next()).append(";\n");
		}
	    }
	    if (!protectedTypedefStatements.isEmpty()) {
		sb.append("\n protected:\n").append(INDENT);
		sb.append("// protected type definitions for header defined "
			  + "by Tag entries in ArgoUML\n");
		sb.append(INDENT);
		sb.append("// Result: typedef <typedef_protected> "
			  + "<tag_value>;\n");
    		Iterator typedefEnum = protectedTypedefStatements.iterator();

    		while (typedefEnum.hasNext()) {
		    sb.append(INDENT).append("typedef ");
		    sb.append(typedefEnum.next()).append(";\n");
		}
	    }
	    if (!privateTypedefStatements.isEmpty()) {
		sb.append("\n private:\n").append(INDENT);
		sb.append("// private type definitions for header defined "
			  + "by Tag entries in ArgoUML\n");
		sb.append(INDENT);
		sb.append("// Result: typedef <typedef_private> "
			  + "<tag_value>;\n");
    		Iterator typedefEnum = privateTypedefStatements.iterator();

    		while (typedefEnum.hasNext()) {
		    sb.append(INDENT).append("typedef ");
		    sb.append(typedefEnum.next()).append(";\n");
		}
	    }
	}
    }

    /**
     * Generates the body of a class or interface.
     * @param cls
     * @return StringBuffer
     */
    protected StringBuffer generateClassifierBody(MClassifier cls)
    {
  	StringBuffer sb = new StringBuffer();
  	if (cls instanceof MClass || cls instanceof MInterface)
	{ // add operations
	    // TODO: constructors
	    generateClassifierBodyOperations(cls, sb);

	    // add attributes
	    generateClassifierBodyAttributes(cls, sb);

	    // add attributes implementing associations
	    generateClassifierBodyAssociations(cls, sb);

	    // add typedefs
	    generateClassifierBodyTypedefs(cls, sb);
	}
  	return sb;
    }

    /**
     * Generate the body of a method associated with the given
     * operation.  This assumes there's at most one method associated!
     *
     * If no method is associated with the operation, a default method
     * body will be generated.
     */
    public String generateMethodBody (MOperation op) {
	if (op != null) {
	    StringBuffer sb = new StringBuffer(80);
	    Collection methods = op.getMethods();
	    Iterator i = methods.iterator();
	    MMethod m = null;
	    boolean methodFound = false;
	    String tv = generateTaggedValues(op, AllButDocTags);
	    String operationIndent =
		(generatorPass == header_pass) ? INDENT : "";

	    // append tags which are not Doc-Comments
	    if (tv.length() > 0)
	    {
		sb.append(operationIndent).append(tv).append('\n');
	    }

	    // place the curley braces within the protected area, to
	    // allow placement of preserved contructor initialisers in
	    // this area otherwise all possible constructor-attribute
	    // initialisers would have to be autogenerated with an
	    // army of special tags
	    sb.append(generateSectionTop(op, operationIndent))
		.append(operationIndent).append("{\n");

	    // System.out.print(", op!=null, size="+methods.size());
	    // return INDENT + INDENT
	    // + "/* method body for " + op.getName() + " */";

	    while (i != null && i.hasNext()) {
		//System.out.print(", i!= null");
		m = (MMethod) i.next();

		if (m != null) {
		    //cat.info(", BODY of "+m.getName());
		    //cat.info("|"+m.getBody().getBody()+"|");
		    if ((m.getBody() != null) && (!methodFound)) {
			sb.append(m.getBody().getBody());
			methodFound = true;
			break;
		    }
		}
	    }

	    if (!methodFound) {
		// pick out return type as default method body
		MParameter rp =
		    UmlHelper.getHelper().getCore().getReturnParameter(op);
		if (rp != null) {
		    MClassifier returnType = rp.getType();
		    sb.append(generateDefaultReturnStatement (returnType));
		}
	    }
	    sb.append(operationIndent).append("}\n")
		.append(generateSectionBottom(op, operationIndent));
	    return sb.toString();
	}
	return generateDefaultReturnStatement (null);
    }


    public String generateSectionTop(MOperation op, String localIndent) {
    	String id = op.getUUID();
    	if (id == null) {
	    id = (new UID().toString());
	    // id =  op.getName() + "__" + static_count;
	    op.setUUID(id);
    	}
    	return Section.generateTop(id, localIndent);
    }

    public String generateSectionBottom(MOperation op, String localIndent) {
    	String id = op.getUUID();
    	if (id == null) {
	    id = (new UID().toString());
	    // id =  op.getName() + "__" + static_count;
	    op.setUUID(id);
    	}
    	return Section.generateBottom(id, localIndent);
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

    public String generateTaggedValues(Object handle, int tagSelection) {
	MModelElement e = (MModelElement)handle;
	// cat.info("generateTaggedValues for element: " + e.getName()
	// + " und selection " + tagSelection);

	Collection tvs = e.getTaggedValues();
	if (tvs == null || tvs.size() == 0) return "";
	boolean first = true;
	StringBuffer buf = new StringBuffer();

	Iterator iter = tvs.iterator();
	String s = null;
	while (iter.hasNext())
	{
	    s = generateTaggedValue((MTaggedValue) iter.next(), tagSelection);
	    if (s != null && s.length() > 0)
	    {
		if (first) {
		    /*
		     * Corrected 2001-09-26 STEFFEN ZSCHALER
		     *
		     * Was:
		     buf.append("// {");
		     *
		     * which caused problems with new lines characters
		     * in tagged values (e.g. comments...). The new
		     * version still has some problems with tagged
		     * values containing "*"+"/" as this closes the
		     * comment prematurely, but comments should be
		     * taken out of the tagged values list anyway...
		     */

		    if (tagSelection == DocCommentTags) {
			// insert main documentation for DocComment at first
			String doc =
			    (DocumentationManager.hasDocs(e))
			    ? DocumentationManager.getDocs(e, INDENT)
			    : null;
			if (doc != null && doc.trim().length() > 0) {
			    buf.append(doc.substring(0, doc.indexOf("*/") + 1));
			    buf.append("  ");
			}
			else
			{
			    buf.append("/** ");
			}
		    }
		    else {
			buf.append("/* {");
		    }
		    first = false;
		} // end first
		else
		{
		    if (tagSelection == DocCommentTags)
		    {
			buf.append("\n").append(INDENT).append(" *  ");
		    }
		    else
		    {
			buf.append(", ");
		    }
		} // end not first tag
		buf.append(s);
	    } // end tag not empty
	} // end while
	/*
	 * Corrected 2001-09-26 STEFFEN ZSCHALER
	 *
	 * Was:
	 if (!first) buf.append("}\n");
	 *
	 * which caused problems with new-lines in tagged values.
	 */
	if (!first)
	{
	    if (tagSelection == DocCommentTags)
	    {
		buf.append("\n").append(INDENT).append(" */\n");
	    }
	    else
	    {
		buf.append ("}*/\n");
	    }
	}
	else if (tagSelection == DocCommentTags) {
	    // create at least main documentation field, if no other tag found
	    String doc =
		(DocumentationManager.hasDocs(e))
		? DocumentationManager.getDocs(e, INDENT)
		: null;
	    if (doc != null && doc.trim().length() > 0) {
		buf.append(doc).append('\n');
	    }
	}

	return buf.toString();
    }

    public String generateTaggedValue(Object handle, int tagSelection) {
	MTaggedValue tv = (MTaggedValue)handle;
	// cat.info("generateTaggedValue: " +
	// generateName(tv.getTag()) + " mit selection: " +
	// tagSelection);
	if (tv == null) return "";
	String s = generateUninterpreted(tv.getValue());
	if (s == null || s.length() == 0 || s.equals("/** */")
	    || (tv.getTag().indexOf("include") != -1)
	    || (tv.getTag().indexOf("_incl") != -1)) {
	    return "";
	}
	if ((tagSelection == DocCommentTags) && (isDocCommentTag(tv.getTag())))
	{
	    return generateDocComment4Tag(generateName(tv.getTag())) + s;
	}
	else if (((tagSelection == AllButDocTags)
		    && (!isDocCommentTag(tv.getTag()))
		    && (!tv.getTag().equals("documentation"))
		    && (!tv.getTag().equals("javadocs"))
		   )
		  || (tagSelection == AllTags))
	{
	    return generateName(tv.getTag()) + "=" + s;
	}
	else
	{
	    return "";
	}
    }

    private Collection findTagValues(MModelElement item, String searchedName) {
	Collection result = new Vector();
	Collection tvs = item.getTaggedValues();
	if (tvs == null || tvs.size() == 0) return result;

	Iterator iter = tvs.iterator();
	MTaggedValue tag;
	String s = null;
	while (iter.hasNext())
	{
	    tag = (MTaggedValue) iter.next();
	    if (tag.getTag().equals(searchedName)) {
		s = tag.getValue();
		if (s != null && s.length() != 0) result.add(s);
	    }
	}
	return result;
    }


    private boolean isDocCommentTag(String tagName) {
	// cat.info("isDocCommentTag:"  + tagName + ":");
  	boolean result = false;
	if (tagName.equals ("inv")) {
	    // cat.info("yes it is doc-comment");
	    result = true;
  	}
  	else if (tagName.equals ("post")) {
	    // cat.info("yes it is doc-comment");
	    result = true;
  	}
  	else if (tagName.equals ("pre")) {
	    // cat.info("yes it is doc-comment");
	    result = true;
  	}
  	else if (tagName.equals ("author")) {
	    // cat.info("yes it is doc-comment");
	    result = true;
  	}
  	else if (tagName.equals ("version")) {
	    // cat.info("yes it is doc-comment");
	    result = true;
  	}
  	else if (tagName.equals ("see")) {
	    // cat.info("yes it is doc-comment");
	    result = true;
	}
  	else if (tagName.equals ("param")) {
	    // cat.info("yes it is doc-comment");
	    result = true;
	}
	return result;
    }
    private String generateDocComment4Tag(String tagName) {
	if (tagName.equals ("inv")) {
	    return "@invariant ";
	}
	else if (tagName.equals ("post")) {
	    return "@postcondition ";
	}
	else if (tagName.equals ("pre")) {
	    return "@precondition ";
	}
  	else if (tagName.equals ("author")) {
	    return "@author ";
  	}
  	else if (tagName.equals ("version")) {
	    return "@version ";
  	}
	else if (tagName.equals ("see")) {
	    return "@see ";
	}
	else if (tagName.equals ("param")) {
	    return "@param ";
	}
	else return "";
    }

    public String generateTaggedValues(Object handle) {
	MModelElement e = (MModelElement)handle;
	Collection tvs = e.getTaggedValues();
	if (tvs == null || tvs.size() == 0) return "";
	boolean first = true;
	StringBuffer buf = new StringBuffer();
	Iterator iter = tvs.iterator();
	String s = null;
	while (iter.hasNext()) {
	    /*
	     * 2002-11-07
	     * Jaap Branderhorst
	     * Was
	     * s = generateTaggedValue((MTaggedValue) iter.next());
	     * which caused problems because the test tags (i.e. tags
	     * with name <NotationName.getName()>+TEST_SUFFIX) were
	     * still generated.
	     * New code:
	     */
	    s = generate((MTaggedValue) iter.next());
	    // end new code
	    if (s != null && s.length() > 0) {
		if (first) {
		    /*
		     * Corrected 2001-09-26 STEFFEN ZSCHALER
		     *
		     * Was:
		     buf.append("// {");
		     *
		     * which caused problems with new lines characters
		     * in tagged values (e.g. comments...). The new
		     * version still has some problems with tagged
		     * values containing "*"+"/" as this closes the
		     * comment prematurely, but comments should be
		     * taken out of the tagged values list anyway...
		     */
		    buf.append ("/* {");

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

    public String generateTaggedValue(Object handle) {
	MTaggedValue tv = (MTaggedValue)handle;
	if (tv == null) return "";
	String s = generateUninterpreted(tv.getValue());
	if (s == null || s.length() == 0 || s.equals("/** */")) return "";
	String t = tv.getTag();
	if (t.equals("documentation")) return "";
	return generateName(t) + "=" + s;
    }

    /**
     * Enhance/Create the doccomment for the given model element,
     * including tags for any OCL constraints connected to the model
     * element. The tags generated are suitable for use with the ocl
     * injector which is part of the Dresden OCL Toolkit and are in
     * detail:
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
	// list tagged values for documentation
	String s = generateTaggedValues (me, DocCommentTags);


	MMultiplicity m = ae.getMultiplicity();
	if (!(MMultiplicity.M1_1.equals(m) || MMultiplicity.M0_1.equals (m))) {
	    // Multiplicity greater 1, that means we will generate some sort of
	    // collection, so we need to specify the element type tag
	    StringBuffer sDocComment = new StringBuffer(80);

	    // Prepare doccomment
	    if (!(s == null || "".equals(s))) {
		// Just remove closing "*/"
		sDocComment.append(s.substring(0, s.indexOf("*/") + 1));
	    }
	    else {
		sDocComment.append(INDENT).append("/**\n");
		sDocComment.append(INDENT).append(" * \n");
		sDocComment.append(INDENT).append(" *");
	    }

	    // Build doccomment
	    MClassifier type = ae.getType();
	    if (type != null) {
		sDocComment.append(" @element-type ").append(type.getName());
	    } else {
		// REMOVED: 2002-03-11 STEFFEN ZSCHALER: element type
		// unknown is not recognized by the OCL injector...
		//sDocComment += " @element-type unknown";
	    }
	    sDocComment.append('\n').append(INDENT).append(" */\n");
	    return sDocComment.toString();
	}
	else {
	    return (s != null) ? s : "";
	}
    }

    public String generateConstraints(Object handle) {
	MModelElement me = (MModelElement)handle;
	// This method just adds comments to the generated java
	// code. This should be code generated by ocl-argo int he
	// future?
	Collection cs = me.getConstraints();
	if (cs == null || cs.size() == 0) return "";
	String s = INDENT + "// constraints\n";
	int size = cs.size();
	// MConstraint[] csarray = (MConstraint[])cs.toArray();
	// cat.info("Got " + csarray.size() + " constraints.");
	for (Iterator i = cs.iterator(); i.hasNext();) {
	    MConstraint c = (MConstraint) i.next();
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

    public String generateConstraint(Object handle) {
	MConstraint c = (MConstraint)handle;
	if (c == null) return "";
	String s = "";
	if (c.getName() != null && c.getName().length() != 0)
	    s += generateName(c.getName()) + ": ";
	s += generateExpression(c);
	return s;
    }


    public String generateAssociationFrom(MAssociation a, MAssociationEnd ae) {
	// TODO: does not handle n-ary associations
	StringBuffer sb = new StringBuffer(80);

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
		sb.append("\n").append(INDENT);
		String comment = generateConstraintEnrichedDocComment(a, ae2);
		// the comment line ends with simple newline -> place INDENT
		// after comment, if not empty
		if (comment.length() > 0)
		    sb.append(comment).append(INDENT);

		sb.append(generateAssociationEnd(ae2));
	    }
	}

	return sb.toString();
    }

    public String generateAssociation(Object handle) {
	//    String s = "";
	//     String generatedName = generateName(a.getName());
	//     s += "MAssociation " + generatedName + " {\n";

	//     Iterator endEnum = a.getConnection().iterator();
	//     while (endEnum.hasNext()) {
	//       MAssociationEnd ae = (MAssociationEnd)endEnum.next();
	//       s += generateAssociationEnd(ae);
	//       s += ";\n";
	//     }
	//     s += "}\n";
	//    return s;
	return "";
    }

    public String generateAssociationEnd(Object handle) {
	MAssociationEnd ae = (MAssociationEnd)handle;
	if (!ae.isNavigable()) return "";
	if (ae.getAssociation().isAbstract()) return "";
	//String s = INDENT + "protected ";
	// must be public or generate public navigation method!
	//String s = INDENT + "public ";
	StringBuffer sb = new StringBuffer(80);
	// cat.info("generate Visibility for Attribute");

	//    sb.append(INDENT).append(generateVisibility(ae.getVisibility()));

	sb.append(generateScope(ae));
	//     String n = ae.getName();
	//     if (n != null && !String.UNSPEC.equals(n))
	//         s += generateName(n) + " ";
	//     if (ae.isNavigable()) s += "navigable ";
	//     if (ae.getIsOrdered()) s += "ordered ";

	String n = ae.getName();
	MAssociation asc = ae.getAssociation();
	String ascName = asc.getName();
	String name = null;

	if (n != null  && n != null && n.length() > 0) {
	    name = generateName(n);
	}
	else if (ascName != null  && ascName != null && ascName.length() > 0) {
	    name = generateName(ascName);
	}
	else {
	    name = "my" + generateClassifierRef(ae.getType());
	}

	sb.append(generateMultiplicity(ae, name, ae.getMultiplicity(),
				       generateAttributeParameterModifier(asc)));

	return (sb.append(";\n")).toString();
    }


    ////////////////////////////////////////////////////////////////
    // internal methods?


    public String generateGeneralization(Collection generalizations) {
	if (generalizations == null) return "";
	StringBuffer sb = new StringBuffer(80);
	Collection classes = new ArrayList();
	Iterator enum = generalizations.iterator();
	while (enum.hasNext()) {
	    MGeneralization g = (MGeneralization) enum.next();
	    MGeneralizableElement ge = g.getParent();
	    // assert ge != null
	    if (ge != null) {
		String visibilityTag = g.getTaggedValue("visibility");
		if (visibilityTag != null && visibilityTag != "")
		    sb.append(visibilityTag).append(" ");
		sb.append(generateNameWithPkgSelection(ge));
		if (enum.hasNext()) sb.append(", ");
	    }
	}
	return sb.toString();
    }

    public Collection getGeneralizationClassList(Collection generalizations) {
	if (generalizations == null) return null;
	Collection classes = new ArrayList();
	Iterator enum = generalizations.iterator();
	while (enum.hasNext()) {
	    MGeneralization g = (MGeneralization) enum.next();
	    MGeneralizableElement ge = g.getParent();
	    // assert ge != null
	    if (ge != null) classes.add(ge);
	}
	return classes;
    }

    //  public String generateSpecification(Collection realizations) {
    public String generateSpecification(MClass cls) {
	Collection deps = cls.getClientDependencies();
	Iterator depIterator = deps.iterator();
	StringBuffer sb = new StringBuffer(80);

	while (depIterator.hasNext()) {
	    MDependency dep = (MDependency) depIterator.next();
	    if (ModelFacade.isAAbstraction(dep)
		&& dep.getStereotype() != null
		&& dep.getStereotype().getName() != null
		&& dep.getStereotype().getName().equals("realize")) {

		MInterface i = (MInterface) dep.getSuppliers().toArray()[0];
		String visibilityTag = dep.getTaggedValue("visibility");
		if (visibilityTag != null && visibilityTag != "")
		    sb.append(visibilityTag).append(" ");
		sb.append(generateNameWithPkgSelection(i));
		if (depIterator.hasNext()) sb.append(", ");

	    }
	}
	return sb.toString();
    }

    public String generateClassList(Collection classifiers) {
	if (classifiers == null) return "";
	StringBuffer sb = new StringBuffer(80);
	Iterator clsEnum = classifiers.iterator();
	while (clsEnum.hasNext()) {
	    MClassifier cls = (MClassifier) clsEnum.next();
	    String visibilityTag = cls.getTaggedValue("visibility");
	    if (visibilityTag != null && visibilityTag != "")
		sb.append(visibilityTag).append(" ");
	    sb.append(generateNameWithPkgSelection(cls));
	    if (clsEnum.hasNext()) sb.append(", ");
	}
	return sb.toString();
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

    public String generateVisibility(Object handle) {
	if (handle instanceof MAttribute) return "";
	if (ModelFacade.isAFeature(handle))
	    handle = ((MFeature)handle).getVisibility();
	MVisibilityKind vis = (MVisibilityKind)handle;
	if (MVisibilityKind.PUBLIC.equals(vis)) return "public ";
	if (MVisibilityKind.PRIVATE.equals(vis)) return "private ";
	if (MVisibilityKind.PROTECTED.equals(vis)) return "protected ";
	return "";
    }

    public String generateScope(MAssociationEnd ae) {
	if (MScopeKind.CLASSIFIER.equals(ae.getTargetScope()))
	    return "static ";
	else
	    return "";
    }

    public String generateScope(MFeature f) {
	MScopeKind scope = f.getOwnerScope();
	//if (scope == null) return "";
	if (MScopeKind.CLASSIFIER.equals(scope)) return "static ";
	return "";
    }

    /**
     * Generate "abstract" keyword for an abstract operation.
     * In C++, since it does not have an explicit "interface" keyword, we must
     * check against this and set the operation to abstract if so.
     */
    public String generateAbstractness (MOperation op) {
        // use ModelFacade to check if the operation is owned by an interface
        Object opOwner = ModelFacade.getOwner(op);
	if (op.isAbstract() || ModelFacade.isAInterface(opOwner)) {
	    return " = 0";
	}
	else {
	    return "";
	}
    }

    /**
     * Generate "final" keyword for final operations.
     */
    public String generateChangeability (MOperation op) {
	if (op.isLeaf() || op.isQuery()) {
	    return "const ";
	}
	else {
	    return "";
	}
    }

    /**
     * Generate "const" keyword for const pointer/reference parameters.
     */
    public String generateChangeability (MParameter par) {
	if ((checkAttributeParameter4Tag(par, SearchReferencePointerTag))
	     && ((par.getKind()).equals(MParameterDirectionKind.IN))) {
	    return "const ";
	}
	else {
	    return "";
	}
    }

    public String generateChangability(MStructuralFeature sf) {
	MChangeableKind ck = sf.getChangeability();
	//if (ck == null) return "";
	if (MChangeableKind.FROZEN.equals(ck)) return "final ";
	//if (MChangeableKind.ADDONLY.equals(ck)) return "final ";
	return "";
    }

    /**
     * Generates "synchronized" keyword for guarded operations.
     * @param op The operation
     * @return The synchronized keyword if the operation is guarded, else ""
     */
    public String generateConcurrency(MOperation op)
    {
	if (op.getConcurrency() != null
	    && op.getConcurrency().getValue() == MCallConcurrencyKind._GUARDED)
	{
	    return "synchronized ";
	}
	return "";
    }

    public String generateMultiplicity(Object handle) {
	MMultiplicity m = (MMultiplicity)handle;
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

    public String generateMultiplicity(MModelElement item, String name,
				       MMultiplicity m, String modifier) {
	String type = null;
	String containerType = null;
	// cat.info("generateMultiplicity mit item" + item.getName() +
	// ", name: " + name + ", modifier: " + modifier);

	MClassifier typeCls = null;
	if (item instanceof MAssociationEnd) {
	    typeCls = ((MAssociationEnd) item).getType();
	} else if (item instanceof MAttribute) {
	    typeCls = ((MAttribute) item).getType();
	} else if (item instanceof MClassifier) {
	    type = ((MClassifier) item).getName();
	} else {
	    type = "";
	}
	if (typeCls != null) type = generateNameWithPkgSelection(typeCls);
	// cat.info("resolved type_name: " + type);
	if (m == null) { return (type + " " + modifier + name); }
	StringBuffer sb = new StringBuffer(80);
	int countUpper = m.getUpper(),
	    countLower = m.getLower();
	// cat.info("resolved int upper/lower bounds");
	Integer lower = new Integer(countLower);
	Integer upper = new Integer(countUpper);
	// cat.info("resolved Integer upper/lower bounds");

	if (countUpper	== 1) {
	    // simple generate identifier for default 0:1, 1:1 association
	    sb.append(type).append(' ').append(modifier).append(name);
	}
	else if (countUpper == countLower) {
	    // fixed array -> <type> <name>[<count>];
	    sb.append(type).append(' ').append(modifier).append(name)
		.append("[ ").append(upper.toString()).append(" ]");
	}
	else {
	    // variable association -> if no tag found use []
	    // else search for tag:
	    // <MultipliciyType> : array|vector|list|slist|map|stack
	    String multType = item.getTaggedValue("MultiplicityType");
	    if (multType == null) {
		// no known container type found
		sb.append(type).append(' ');
		sb.append(modifier).append(name).append("[]");
	    }
	    else if (multType.equals("vector")) {
		if (extraIncludes.indexOf("#include <vector>") == -1)
		    extraIncludes += "#include <vector>\n";
		containerType = "vector";
	    }
	    else if (multType.equals("list")) {
		if (extraIncludes.indexOf("#include <list>") == -1)
		    extraIncludes += "#include <list>\n";
		containerType = "list";
	    }
	    else if (multType.equals("slist")) {
		if (extraIncludes.indexOf("#include <slist>") == -1)
		    extraIncludes += "#include <slist>\n";
		containerType = "slist";
	    }
	    else if (multType.equals("map")) {
		if (extraIncludes.indexOf("#include <map>") == -1)
		    extraIncludes += "#include <map>\n";
		containerType = "map";
	    }
	    else if (multType.equals("stack")) {
		if (extraIncludes.indexOf("#include <stack>") == -1)
		    extraIncludes += "#include <stack>\n";
		containerType = "stack";
	    }

	    if (containerType != null) {
		// known container type
		String includeLine = "#include <" + containerType + ">";
		if (extraIncludes.indexOf(includeLine) == -1)
		    extraIncludes += includeLine + "\n";
		sb.append(containerType).append("< ");
		sb.append(type).append(modifier);
		sb.append(" > ").append(name);
	    }
	}
	return sb.toString();
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

    public String generateState(Object handle) {
	return ModelFacade.getName(handle);
    }

    public String generateStateBody(Object handle) {
	MState m = (MState)handle;
	// cat.info("GeneratorCpp: generating state body");
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
		if (s.length() > 0) s += "\n";
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

    public String generateTransition(Object handle) {
	MTransition m = (MTransition)handle;
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

    public String generateGuard(Object handle) {
	MGuard m = (MGuard)handle;
	//return generateExpression(m.getExpression());
	if (m.getExpression() != null)
	    return generateExpression(m.getExpression());
	return "";
    }

    public String generateMessage(Object handle) {
    	MMessage m = (MMessage)handle;
    	if (m == null) return "";
	return generateName(m.getName()) + "::"
	    + generateAction(m.getAction());
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
	LOG.info("Parsing " + file.getPath());

        // read the existing file and store preserved sections

        /*
	  BufferedReader in = new BufferedReader(new FileReader(file));
	  JavaLexer lexer = new JavaLexer(in);
	  JavaRecognizer parser = new JavaRecognizer(lexer);
	  CodePieceCollector cpc = new CodePieceCollector();
	  parser.compilationUnit(cpc);
	  in.close();
        */


	File origFile = new File(file.getAbsolutePath());
	File newFile = new File(file.getAbsolutePath() + ".updated");
	LOG.info("Generating " + newFile.getPath());

        boolean eof = false;
        BufferedReader origFileReader =
	    new BufferedReader(new FileReader(file.getAbsolutePath()));
        FileWriter newFileWriter =
	    new FileWriter(file.getAbsolutePath() + ".updated");
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
	LOG.info("Backing up " + file.getPath());
	file.renameTo(new File(file.getAbsolutePath() + ".backup"));
	LOG.info("Updating " + file.getPath());
	newFile.renameTo(origFile);
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


    public String getModuleName() { return "GeneratorCpp"; }
    public String getModuleDescription() {
        return "Cpp Notation and Code Generator";
    }
    public String getModuleAuthor() { return "Achim Spangler"; }
    public String getModuleVersion() { return "0.9.8"; }
    public String getModuleKey() { return "module.language.cpp.generator"; }
    /**
     * Returns the _lfBeforeCurly.
     * @return boolean
     */
    public boolean isLfBeforeCurly()
    {
        return _lfBeforeCurly;
    }

    /**
     * Returns the _verboseDocs.
     * @return boolean
     */
    public boolean isVerboseDocs()
    {
        return _verboseDocs;
    }

    /**
     * Sets the _lfBeforeCurly.
     * @param beforeCurly The _lfBeforeCurly to set
     */
    public void setLfBeforeCurly(boolean beforeCurly)
    {
        this._lfBeforeCurly = beforeCurly;
    }

    /**
     * Sets the _verboseDocs.
     * @param verbose The _verboseDocs to set
     */
    public void setVerboseDocs(boolean verbose)
    {
        this._verboseDocs = verbose;
    }


} /* end class GeneratorCpp */

