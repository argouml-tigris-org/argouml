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


/**
 * Generator2 subclass to generate text for display in diagrams in in
 * text fields in the Argo/UML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for outher languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced.
 */
public class GeneratorCpp extends Generator2
    implements PluggableNotation, FileGenerator {

    /** logger */
    private static final Logger LOG = Logger.getLogger(GeneratorCpp.class);

    private boolean verboseDocs = false;
    private boolean lfBeforeCurly = false;
    /**
     * TODO: make it configurable
     */
    private static final boolean VERBOSE_DOCS = false;

    private static final String ANY_RANGE = "0..*";

    private static Section sect;

    /** 2002-12-07 Achim Spangler
     * store actual namespace, to avoid unneeded curley braces
     */
    private Object actualNamespace;

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
     * 2002-11-28 Achim Spangler
     * C++ uses two files for each class: header (.h) with class definition
     * and source (.cpp) with methods implementation
     * --> two generation passes are needed
     */
    private static final int NONE_PASS = 1;
    private static final int HEADER_PASS = 2;
    private static final int SOURCE_PASS = 3;
    private static int generatorPass = NONE_PASS;

    /**
     * 2002-12-05 Achim Spangler
     * use Tag generation for generation of: doccomment, simple tags of
     * tags which are not used for doccomment or simple tags for all
     */
    private static final int DOC_COMMENT_TAGS = 1;
    private static final int ALL_BUT_DOC_TAGS = 2;
    private static final int ALL_TAGS = 3;

    /**
     * 2002-12-06 Achim Spangler
     * C++ developers need to specify for parameters whether they are
     * pointers or references (especially for class-types)
     * -> a general check function must get the searched tag
     */
    private static final int SEARCH_REFERENCE_TAG = 1;
    private static final int SEARCH_POINTER_TAG = 2;
    private static final int SEARCH_REFERENCE_POINTER_TAG = 3;



    private static final GeneratorCpp SINGLETON = new GeneratorCpp();

    /**
     * Get the instance.
     *
     * @return the singleton of the generator.
     */
    public static GeneratorCpp getInstance() { return SINGLETON; }


    /**
     * Constructor.
     */
    protected GeneratorCpp() {
	super (Notation.makeNotation ("Cpp",
				      null,
				      Argo.lookupIconResource ("CppNotation")));
    }

    /**
     * @param o the object to be generated
     * @return the generated string 
     */
    public static String Generate(Object o) {
	return SINGLETON.generate(o);
    }


    /** 2002-11-28 Achim Spangler
     * @return file extension for actual generation pass
     */
    private String getFileExtension()
    {
	// for Java simply answer ".java" every time
	if (generatorPass == HEADER_PASS) return ".h";
	else return ".cpp";
    }

    /**
     * create the needed directories for the derived appropriate pathname
     * @return full pathname
     */
    private String generateDirectoriesPathname(Object cls, String path) {
	String name = ModelFacade.getName(cls);
	if (name == null || name.length() == 0) {
	    return null;
	}
	String filename = name + getFileExtension();
	if (!path.endsWith (FILE_SEPARATOR)) {
	    path += FILE_SEPARATOR;
	}

        String packagePath = "";
        // avoid model being used as a package name
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
     * @see FileGenerator#GenerateFile(Object, String)
     * 
     * Generates a file for the classifier.
     * This method could have been static if it where not for the need to
     * call it through the Generatorinterface.<p>
     *
     * @return the full path name of the the generated file.
     *
     * @see org.argouml.uml.generator.FileGenerator#GenerateFile(java.lang.Object, java.lang.String)
     */
    public String GenerateFile(Object o, String path) {
	String packagePath = ModelFacade.getName(ModelFacade.getNamespace(o));
   	String pathname = null;

	// use unique section for both passes -> allow move of
	// normal function body to inline and vice versa
   	sect = new Section();

	/*
	 * 2002-11-28 Achim Spangler
	 * first read header and source file into global/unique section
	 */
	for (generatorPass = HEADER_PASS;
	     generatorPass <= SOURCE_PASS;
	     generatorPass++) {
	    pathname = generateDirectoriesPathname(o, path);
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
	for (generatorPass = HEADER_PASS;
	     generatorPass <= SOURCE_PASS;
	     generatorPass++) {
	    pathname = generateDirectoriesPathname(o, path);
	    //String pathname = path + filename;
	    // TODO: package, project basepath, tagged values to configure
	    File f = new File(pathname);
	    String headerTop = generateHeaderTop(pathname);
	    String header = generateHeader(o, packagePath);
	    String src = generate(o);
	    BufferedWriter fos = null;
	    try {
		fos = new BufferedWriter (new FileWriter (f));
		writeTemplate(o, path, fos);
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
	    if (generatorPass == HEADER_PASS)	{
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
     * write template content on top of file
     */
    private void writeTemplate(Object cls, String path, BufferedWriter fos)
    {
	String templatePathName = path + "/templates/";
	String fileName = ModelFacade.getName(cls);
	String tagTemplatePathName = 
	    ModelFacade.getTaggedValueValue(cls, "TemplatePath");
	String authorTag = ModelFacade.getTaggedValueValue(cls, "author");
	String emailTag = ModelFacade.getTaggedValueValue(cls, "email");
	if (tagTemplatePathName != null && tagTemplatePathName.length() > 0)
	    templatePathName = tagTemplatePathName;
	if (generatorPass == HEADER_PASS) {
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
		    new BufferedReader(new FileReader(
		            templateFile.getAbsolutePath()));
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
    private String generateHeaderTop(String pathname)
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
	    sb.append(generateHeaderImportLine4Item(clsEnum.next()));
	}
	return sb.toString();
    }

    /** 2002-11-28 Achim Spangler
     * as each language has its own syntax to incorporate other elements
     * the command for this inclusion is created in a seperate function
     */
    private String generateHeaderImportLine4Item(Object clsDepend) {
	// cat.info("generateHeaderImportLine4Item: fuer Item " +
	// clsDepend.getName() + " in Namespace: " +
	// clsDepend.getNamespace().getName());
	StringBuffer sb = new StringBuffer(80);
	String packagePath = ModelFacade.getName(clsDepend);
	Object parent = 
	    ModelFacade.getNamespace(ModelFacade.getNamespace(clsDepend));
	if (parent != null) {
	    packagePath =
		ModelFacade.getName(ModelFacade.getNamespace(clsDepend)) 
		+ "/" + packagePath;
	    while (parent != null) {
		// ommit root package name; it's the model's root
		if (ModelFacade.getNamespace(parent) != null) {
		    packagePath = 
		        ModelFacade.getName(parent) + "/" + packagePath;
		}
	    	// cat.info("generateHeaderImportLine4Item: Runde mit
	    	// Parent" + parent.getName());
    		parent = ModelFacade.getNamespace(parent);
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

	if ((generatorPass != HEADER_PASS)
	    && (usageTag.indexOf("source") != -1)) {
	    // generate include line for source .cpp pass only if
	    // element has usage tag which specifies exclusive use in
	    // source file
	    result = true;
	} else if ((generatorPass == HEADER_PASS)
		   && (usageTag.indexOf("source") == -1)) {
	    // generate include line for header, if not specified as
	    // only accessed from .cpp
	    result = true;
	}

	// only predeclare candidates can be ignored in include block of header
	if ((generatorPass == HEADER_PASS) && (!isIndirect)) result = true;

	return result;
    }

    private boolean checkIncludeNeeded4Element(Object cls) {
	String usageTag = "";
	boolean predeclareCandidate = false;

	Iterator iter = ModelFacade.getTaggedValues(cls);
	while (iter.hasNext()) {
	    Object tv = iter.next();
	    String tag = ModelFacade.getTagOfTag(tv);
	    if (tag.equals("usage")) {
	        usageTag = ModelFacade.getValueOfTag(tv);
	    }
	    // cat.info("Tag fuer: " + cls.getName() + " mit Tag:
	    // " + tag + " mit Wert:" + tv.getValue() + ":");
	    
	    if (tag.indexOf("ref") != -1 || tag.equals("&")
	            || tag.indexOf("pointer") != -1 || tag.equals("*")) {
	        predeclareCandidate = true;
	    }
	}
	return checkInclude4UsageIndirection(predeclareCandidate, usageTag);
    }


    private boolean checkIncludeNeeded4ElementAttribute(Object cls) {
	// cat.info("checkIncludeNeeded4Element: fuer Item" + cls);
	if (!(ModelFacade.isAClass(ModelFacade.getType(cls)))) {
	    return false;
	}

	return checkIncludeNeeded4Element(cls);
    }

    private String generateHeaderDependencies(Object cls) {
	StringBuffer sb = new StringBuffer(160);
	StringBuffer predeclare = new StringBuffer(60);

	if (generatorPass != HEADER_PASS)
	{ // include header in .cpp
	    sb.append(generateHeaderImportLine4Item(cls));

	    Iterator iter = ModelFacade.getTaggedValues(cls);
	    while (iter.hasNext()) {
	        Object tv = iter.next();
	        String tag = ModelFacade.getTagOfTag(tv);
	        if (tag.equals("source_incl")
	                || tag.equals("source_include")) {
	            sb.append("#include ");
	            sb.append(ModelFacade.getValueOfTag(tv));
	            sb.append("\n");
	        }
	    }
	}
	else {
	    Collection baseClassList =
		getGeneralizationClassList(ModelFacade.getGeneralizations(cls));
	    sb.append(generateHeaderImportLine4ItemList(baseClassList));

	    Iterator iter = ModelFacade.getTaggedValues(cls);
	    while (iter.hasNext()) {
	        Object tv = iter.next();
	        String tag = ModelFacade.getTagOfTag(tv);
	        if (tag.equals("header_incl")
	                || tag.equals("header_include")) {
	            sb.append("#include ");
	            sb.append(ModelFacade.getValueOfTag(tv));
	            sb.append("\n");
	        }
	    }
	}

	// check if the class has dependencies
	{
	    Collection col = ModelFacade.getAssociationEnds(cls);
	    if (col != null) {
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
		    Object ae = itr.next();
		    if (ModelFacade.isNavigable(ae)) {
			Object cls2 = ModelFacade.getType(ae);
			String name = ModelFacade.getName(cls2);
			String name2 = ModelFacade.getName(cls);
			if (name != name2) {
			    if (checkIncludeNeeded4Element(
			            ModelFacade.getAssociation(ae))) {
				sb.append(generateHeaderImportLine4Item(cls2));
			    }
			    else if (generatorPass == HEADER_PASS) {
				// predeclare classes which are not
				// directly used in header usefull for
				// classes which are only used
				// indirectly as pointer where no
				// knowledge about internals of class
				// are needed
				predeclare
				    .append(generateHeaderPackageStart(cls2));
				predeclare
				    .append("class ").append(name);
				predeclare.append(";\n");
			    }
			}
		    }
		}
	    }
	}

	{
	    Collection col = ModelFacade.getAttributes(cls);
	    if (col != null) {
		Iterator itr = col.iterator();
    		// cat.info("Attribut gefunden");
		while (itr.hasNext()) {
		    Object attr = itr.next();
		    // cat.info("untersuche name " + attr.getName() +
		    // " mit Typ: " + attr.getType());
		    if (ModelFacade.isAClass(ModelFacade.getType(attr))) {
			String name = ModelFacade.getName(attr);
			if (checkIncludeNeeded4ElementAttribute(attr)) {
			    sb.append(generateHeaderImportLine4Item(
                                          ModelFacade.getType(attr)));
			}
			else if (generatorPass == HEADER_PASS) {
			    // predeclare classes which are not
			    // directly used in header usefull for
			    // classes which are only used indirectly
			    // as pointer where no knowledge about
			    // internals of class are needed
			    predeclare
				.append(generateHeaderPackageStart(
                                     ModelFacade.getType(attr)))
				.append("class ").append(name).append(";\n");
			}
		    }
		}
	    }
	}

	{
	    Collection col = ModelFacade.getClientDependencies(cls);
	    // cat.info("col: " + col);
	    if (col != null) {
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
		    Object dependency = itr.next();
		    if (dependency == null) {
		        break;
		    }
		    Collection clientsCol = 
		        ModelFacade.getSuppliers(dependency);
		    if (clientsCol == null) {
		        break;
		    }
		    Iterator itr2 = clientsCol.iterator();
		    while (itr2.hasNext()) {
			Object temp = itr2.next();
			if (checkIncludeNeeded4Element(dependency)) {
			    sb.append(generateHeaderImportLine4Item(temp));
			}
			else if (generatorPass == HEADER_PASS) {
			    // predeclare classes which are not
			    // directly used in header usefull for
			    // classes which are only used indirectly
			    // as pointer where no knowledge about
			    // internals of class are needed
			    predeclare
				.append(generateHeaderPackageStart(temp));
			    predeclare.append("class ");
			    predeclare.append(ModelFacade.getName(temp));
			    predeclare.append(";\n");
			}
		    }
		}
	    }
	}

	if (predeclare.toString().length() > 0) {
	    sb.append("\n\n").append(predeclare.toString());
	}
	return sb.toString();
    }

    private String generateHeaderPackageStartSingle(Object pkg)
    {
	// cat.info("generateHeaderPackageStartSingle: " + pkg.getName());
	StringBuffer sb = new StringBuffer(30);
	StringTokenizer st = new StringTokenizer(ModelFacade.getName(pkg), ".");
	String token = "";

	sb.append(generateTaggedValues(pkg, DOC_COMMENT_TAGS));
	while (st.hasMoreTokens()) {
	    token = st.nextToken();
	    // create line: namespace FOO {"
	    sb.append("namespace ").append(token).append(" {\n");
	}
	return sb.toString();
    }

    private String generateHeaderPackageEndSingle(Object pkg)
    {
	// cat.info("generateHeaderPackageEndSingle: " + pkg.getName());
	StringBuffer sb = new StringBuffer(30);
	StringTokenizer st = 
	    new StringTokenizer(ModelFacade.getName(pkg), ".");
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

    private String generatePackageAbsoluteName(Object pkg)
    {
	// cat.info("generatePackageAbsoluteName: " + pkg.getName());
	StringBuffer sb = new StringBuffer(30);
	String token = "";
	for (Object actual = pkg;
	     actual != null;
	     actual = ModelFacade.getNamespace(actual)) {
	    StringTokenizer st = 
	        new StringTokenizer(ModelFacade.getName(actual), ".");
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

    private String generateNameWithPkgSelection(Object item,
						Object localPkg) {
	if (item == null) {
	    // cat.info("generateNameWithPkgSelection: zu void");
	    return "void ";
	}
	// cat.info("generateNameWithPkgSelection: " + item.getName());
	Object pkg = null;
	if (ModelFacade.isADataType(item)) {
	    return generateName(ModelFacade.getName(item));
	} else if (ModelFacade.isAParameter(item)
	           || ModelFacade.isAAttribute(item)
	           || ModelFacade.isAAssociationEnd(item)
	           || ModelFacade.isAClassifier(item)) {
	    pkg = ModelFacade.getNamespace(item);
	}

	if (pkg == null) {
	    return generateName(ModelFacade.getName(item));
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
	    return generateName(ModelFacade.getName(item));
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
			    + generateName(ModelFacade.getName(item)));
		}
	    }
	}
	return (targetPkgName + "::" + generateName(ModelFacade.getName(item)));
    }

    private String generateNameWithPkgSelection(Object item) {
	return generateNameWithPkgSelection(item, actualNamespace);
    }

    private String generateHeaderPackageStart(Object cls)
    {
	// cat.info("generateHeaderPackageStart: " + cls.getName() + "
	// aus Namespace: " + cls.getNamespace().getName());
	StringBuffer sb = new StringBuffer(80);

	if (actualNamespace != null)
	{
	    for (Object fromSearch = actualNamespace;
		 fromSearch != null;
		 fromSearch = ModelFacade.getNamespace(fromSearch))
	    {
		// cat.info("fromSearch: " + fromSearch.getName());
		StringBuffer contPath = new StringBuffer(80);
		Object toSearch = ModelFacade.getNamespace(cls);
		for (;
		     (toSearch != null) && (toSearch != fromSearch);
		     toSearch = ModelFacade.getNamespace(toSearch))
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
	    for (Object toSearch = ModelFacade.getNamespace(cls);
		 toSearch != null;
		 toSearch = ModelFacade.getNamespace(toSearch))
	    {
		sb.insert(0, generateHeaderPackageStartSingle(toSearch));
	    }
	}
	if (sb.length() > 0) {
	    sb.insert(0, "\n").append("\n");
	}
	actualNamespace = ModelFacade.getNamespace(cls);
	return sb.toString();
    }

    private String generateHeaderPackageEnd() {
	StringBuffer sb = new StringBuffer(20);

	for (Object closeIt = actualNamespace;
	     closeIt != null;
	     closeIt = ModelFacade.getNamespace(closeIt))
	{
	    sb.append(generateHeaderPackageEndSingle(closeIt));
	}
	actualNamespace = null;
	if (sb.length() > 0) {
	    sb.insert(0, "\n").append("\n");
	}
	return sb.toString();
    }

    private String generateHeader(Object cls,
				  String packagePath) {
	StringBuffer sb = new StringBuffer(240);

	// check if the class has a base class
	sb.append(generateHeaderDependencies(cls));

	sb.append("\n");

	if (packagePath.length() > 0) {
	    sb.append(generateHeaderPackageStart(cls));
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

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAssociationRole(java.lang.Object)
     */
    public String generateAssociationRole(Object m) {
	return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateObjectFlowState(java.lang.Object)
     */
    public String generateObjectFlowState(Object m) {
        Object c = ModelFacade.getType(m);
        if (c == null) return "";
        return ModelFacade.getName(c);
    }
    
    /** 2002-11-28 Achim Spangler
     * seperate generation of Operation Prefix from generateOperation
     * so that generateOperation is language independent
     */
    private String generateOperationPrefix(Object op) {
	StringBuffer sb = new StringBuffer(80);
	sb.append(generateConcurrency(op));
	if (generatorPass == HEADER_PASS) {
	    // make all operations to virtual - as long as they are not "leaf"
	    Object scope = ModelFacade.getOwnerScope(op);
	    // generate a function as virtual, if it can be overriden
	    // or override another function AND if this function is
	    // not marked as static, which disallows "virtual"
	    // alternatively every abstract function is defined as
	    // virtual
	    if ((!ModelFacade.isLeaf(op)
		 && !ModelFacade.isRoot(op)
		 && (!(ModelFacade.CLASSIFIER_SCOPEKIND.equals(scope))))
		|| (ModelFacade.isAbstract(op))) {
		sb.append("virtual ");
	    }
	    sb.append(generateOwnerScope(op));
	}
	return sb.toString();
    }

    /** 2002-11-28 Achim Spangler
     * seperate generation of Operation Suffix from generateOperation
     * so that generateOperation is language independent
     */
    private String generateOperationSuffix(Object op) {
	StringBuffer sb = new StringBuffer(80);
	sb.append(generateOperationChangeability(op));
	sb.append(generateAbstractness(op));
	return sb.toString();
    }

    /** 2002-11-28 Achim Spangler
     * seperate generation of Operation Name from generateOperation
     * so that generateOperation is language independent
     * -> for C++: if we create .cpp we must prepend Owner name
     * 
     * @param sb Where to put the result.
     */
    private boolean generateOperationNameAndTestForConstructor(Object op,
							       StringBuffer sb)
    {
	// cat.info("generate Operation for File" + generatorPass + "
	// fuer Op: " + op.getName());
	if (generatorPass != HEADER_PASS)
	{
	    // cat.info("generate Operation for CPP File");
	    sb.append(ModelFacade.getName(ModelFacade.getOwner(op)))
		.append("::");
	}
	boolean constructor = false;
	String name;
	if (ModelFacade.isConstructor(op)) {
	    // constructor
	    name = ModelFacade.getName(ModelFacade.getOwner(op));
	    constructor = true;
	} else {
	    // cat.info("generate Operation for File" + generatorPass
	    // + " fuer Op: " + op.getName());
	    name = ModelFacade.getName(op);
	}
	sb.append(generateName(name));
	return constructor;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateOperation(
     *      java.lang.Object, boolean)
     *
     * 2002-11-28 Achim Spangler
     * modified version from Jaap Branderhorst
     * -> generateOperation is language independent and seperates
     *    different tasks
     */
    public String generateOperation(Object op, boolean documented) {
	// generate nothing for abstract functions, if we generate the
	// source .cpp file at the moment
	if ((generatorPass != HEADER_PASS) && (ModelFacade.isAbstract(op))) {
	    return "";
	}
	StringBuffer sb = new StringBuffer(80);
	StringBuffer nameBuffer = new StringBuffer(20);
	String operationIndent = (generatorPass == HEADER_PASS) ? INDENT : "";
	// cat.info("generate Operation for File" + generatorPass + "
	// fuer Op: " + op.getName());
	boolean constructor =
	    generateOperationNameAndTestForConstructor(op, nameBuffer);

	sb.append('\n'); // begin with a blank line
	// generate DocComment from tagged values
	String tv = generateTaggedValues (op, DOC_COMMENT_TAGS);
	if (tv != null && tv.length() > 0) {
	    sb.append ("\n").append(operationIndent).append (tv);
	}

	// 2002-07-14
	// Jaap Branderhorst
	// missing concurrency generation
	sb.append(operationIndent)
	    .append(generateOperationPrefix(op));

	// pick out return type
	Object rp = UmlHelper.getHelper().getCore().getReturnParameter(op);
	if (rp != null) {
	    Object returnType = ModelFacade.getType(rp);
	    if (returnType == null && !constructor) {
		sb.append("void ");
	    }
	    else if (returnType != null) {
		sb.append(generateNameWithPkgSelection(returnType)).append(' ');
	    }
	}

	// name and params
	Vector params = new Vector(ModelFacade.getParameters(op));
	params.remove(rp); // If there are several return parameters, just
	                   // the one found above will be removed.

	sb.append(nameBuffer.toString()).append('(');

	if (params != null) {
	    boolean first = true;

	    for (int i = 0; i < params.size(); i++) {
		Object p = params.elementAt (i);

		if (!first) sb.append(", ");

		sb.append(generateParameter(p));
		first = false;
	    }
	}

	sb.append(") ")
	    .append(generateOperationSuffix(op));

	return sb.toString();

    }

    /** 2002-12-06 Achim Spangler
     * check if a parameter is tagged as pointer or reference (not
     * part of UML - as far as author knows - but important for C++
     * developers)
     * @param elem element to check
     * @param tagType tag type to check
     */
    private boolean checkAttributeParameter4Tag(Object elem,
						int tagType) {
	// first check whether the parameter shall be a pointer of reference
        Iterator iter = ModelFacade.getTaggedValues(elem);
        while (iter.hasNext()) {
            Object tv = iter.next();
            String tag = ModelFacade.getTagOfTag(tv);
            if ((tag.indexOf("ref") != -1 || tag.equals("&"))
                    && (tagType != SEARCH_POINTER_TAG)) {
                return true;
            } else if ((tag.indexOf("pointer") != -1 || tag.equals("*"))
                    && (tagType != SEARCH_REFERENCE_TAG)) {
                return true;
            }
        }
	return false;
    }


    private String generateAttributeParameterModifier(Object attr) {
	boolean isReference =
	    checkAttributeParameter4Tag(attr, SEARCH_REFERENCE_TAG);
	boolean isPointer = 
	    checkAttributeParameter4Tag(attr, SEARCH_POINTER_TAG);
	StringBuffer sb = new StringBuffer(2);

	if (isReference) {
	    sb.append("&");
	} else if (isPointer) {
	    sb.append("*");
	} else if (ModelFacade.isAParameter(attr)) {
	    if (ModelFacade.getKind(attr).equals(
	            ModelFacade.OUT_PARAMETERDIRECTIONKIND)
	        || ModelFacade.getKind(attr).equals(
	            ModelFacade.INOUT_PARAMETERDIRECTIONKIND)) {
	        // out or inout parameters are defaulted to reference if
	        // not specified else
		sb.append("&");
	    }
	}

	return sb.toString();
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAttribute(
     *         java.lang.Object, boolean)
     */
    public String generateAttribute(Object attr, boolean documented) {
	StringBuffer sb = new StringBuffer(80);
	sb.append('\n'); // begin with a blank line

	// list tagged values for documentation
	String tv = generateTaggedValues (attr, DOC_COMMENT_TAGS);
	if (tv != null && tv.length() > 0) {
	    sb.append ("\n").append (INDENT).append (tv);
	}


	sb.append(INDENT);
	// cat.info("generate Visibility for Attribute");
	sb.append(generateVisibility(attr));
	sb.append(generateOwnerScope(attr));
	sb.append(generateStructuralFeatureChangability(attr));
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
	sb.append(
	        generateMultiplicity(
	                attr,
	                generateName(ModelFacade.getName(attr)),
	                ModelFacade.getMultiplicity(attr),
	                generateAttributeParameterModifier(attr)));
	Object initExpression = ModelFacade.getInitialValue(attr);
	if (initExpression != null) {
	    String initStr = generateExpression(initExpression).trim();
	    if (initStr.length() > 0)
		sb.append(" = ").append(initStr);
	}

	sb.append(";\n");

	return sb.toString();
    }


    /**
     * @see org.argouml.application.api.NotationProvider2#generateParameter(java.lang.Object)
     */
    public String generateParameter(Object param) {
	StringBuffer sb = new StringBuffer(20);
	//TODO: qualifiers (e.g., const)
	// generate const for references or pointers which are
	// defined as IN - other qualifiers are not important for
	// C++ parameters
	sb.append(generateParameterChangeability(param));
	//TODO: stereotypes...
	sb.append(generateNameWithPkgSelection(ModelFacade.getType(param)));
	sb.append(' ');
	sb.append(generateAttributeParameterModifier(param));
	sb.append(generateName(ModelFacade.getName(param)));

	// insert default value, if we are generating the header
	if ((generatorPass == HEADER_PASS)
	    && (ModelFacade.getDefaultValue(param) != null)) {
	    sb.append(" = ");
	    sb.append(ModelFacade.getBody(ModelFacade.getDefaultValue(param)));
	}

	return sb.toString();
    }


    /**
     * @see org.argouml.application.api.NotationProvider2#generatePackage(java.lang.Object)
     */
    public String generatePackage(Object p) {
	String s = "";
	String packName = generateName(ModelFacade.getLanguage(p));
	s += "// package " + packName + " {\n";
	Collection ownedElements = ModelFacade.getOwnedElements(p);
	if (ownedElements != null) {
	    Iterator ownedEnum = ownedElements.iterator();
	    while (ownedEnum.hasNext()) {
		Object me = ownedEnum.next();
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
    StringBuffer generateClassifierStart(Object cls) {
	StringBuffer sb = new StringBuffer (80);

	// don't create class-Start for implementation in .cpp
	if (generatorPass != HEADER_PASS) return sb;

	String sClassifierKeyword;
	if (ModelFacade.isAClass(cls)) {
	    sClassifierKeyword = "class";
	} else if (ModelFacade.isAInterface(cls)) {
	    sClassifierKeyword = "class";
	} else {
	    return null; // actors, use cases etc.
	}
	boolean hasBaseClass = false;

	// Add the comments for this classifier first.
	sb.append ('\n')
	    .append (DocumentationManager.getComments(cls));

	// list tagged values for documentation
	String tv = generateTaggedValues (cls, DOC_COMMENT_TAGS);
	if (tv != null && tv.length() > 0) {
	    sb.append ("\n").append (INDENT).append (tv);
	}

	// add classifier keyword and classifier name
	sb.append(sClassifierKeyword).append(" ");
	sb.append(generateName(ModelFacade.getName(cls)));

	// add base class/interface
	String baseClass = 
	    generateGeneralization(ModelFacade.getGeneralizations(cls));
	if (!baseClass.equals ("")) {
	    sb.append (" : ")
		.append (baseClass);
	    hasBaseClass = true;
	}

	// add implemented interfaces, if needed
	// nsuml: realizations!
	if (ModelFacade.isAClass(cls)) {
	    String interfaces = generateSpecification(cls);
	    if (!interfaces.equals ("")) {
		if (!hasBaseClass) sb.append (" : ");
		else sb.append (", ");
		sb.append (interfaces);
	    }
	}

	// add opening brace
	sb.append(lfBeforeCurly ? "\n{" : " {");

	// list tagged values for documentation
	tv = generateTaggedValues (cls, ALL_BUT_DOC_TAGS);
	if (tv != null && tv.length() > 0) {
	    sb.append("\n").append (INDENT).append (tv);
	}

	return sb;
    }

    private StringBuffer generateClassifierEnd(Object cls) {
	StringBuffer sb = new StringBuffer();
	if (ModelFacade.isAClass(cls) || ModelFacade.isAInterface(cls))
	{
	    if ((verboseDocs) && (generatorPass == HEADER_PASS))
	    {
		String classifierkeyword = null;
		if (ModelFacade.isAClass(cls)) {
		    classifierkeyword = "class";
		} else {
		    classifierkeyword = "class";
		}
		sb.append("\n//end of "
			  + classifierkeyword
			  + " "
			  + ModelFacade.getName(cls)
			  + "\n");
	    }
	    if (generatorPass == HEADER_PASS) sb.append("};\n");
	    sb.append(generateHeaderPackageEnd());
	}
	return sb;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateClassifier(java.lang.Object)
     *
     * Generates code for a classifier. In case of Java code is
     * generated for classes and interfaces only at the moment.
     */
    public String generateClassifier(Object cls) {
  	StringBuffer returnValue = new StringBuffer();
	StringBuffer start = generateClassifierStart(cls);
  	if (((start != null) && (start.length() > 0))
	    || (generatorPass != HEADER_PASS)) {
	    StringBuffer typedefs = generateGlobalTypedefs(cls);
	    StringBuffer body = generateClassifierBody(cls);
	    StringBuffer end = generateClassifierEnd(cls);
	    returnValue.append((typedefs != null) ? typedefs.toString() : "");
	    returnValue.append(start);
	    if ((body != null) && (body.length() > 0))
	    {
		returnValue.append("\n");
		returnValue.append(body);
		if (lfBeforeCurly)
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
    private StringBuffer generateGlobalTypedefs(Object cls) {
  	StringBuffer sb = new StringBuffer();
  	if (ModelFacade.isAClass(cls) || ModelFacade.isAInstance(cls))
	{ // add typedefs
	    if (generatorPass == HEADER_PASS) {
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
     * @param sb Where to put the result.
     */
    private void generateClassifierBodyAttributes(Object cls,
						  StringBuffer sb) {
	Collection strs = ModelFacade.getAttributes(cls);
	if (strs.isEmpty() || (generatorPass != HEADER_PASS)) {
	    return;
	}
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
	if (verboseDocs && ModelFacade.isAClass(cls)) {
	    sb.append(INDENT).append("// Attributes\n");
	}

	// generate attributes in order public, protected, private
	for (int i = 0; i < ALL_PARTS.length; i++) {
	    int publicProtectedPrivate = ALL_PARTS[i];

	    Iterator strEnum = strs.iterator();
	    boolean isVisibilityLinePrinted = false;
	    while (strEnum.hasNext()) {
		Object sf = strEnum.next();
		if (((publicProtectedPrivate == PUBLIC_PART)
		     && ModelFacade.isPublic(sf))
		    || ((publicProtectedPrivate == PROTECTED_PART)
			&& ModelFacade.isProtected(sf))
		    || ((publicProtectedPrivate == PRIVATE_PART)
			&& ModelFacade.isPrivate(sf))) {
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
		    sb.append(generate(sf));

		    tv = generateTaggedValues(sf, ALL_BUT_DOC_TAGS);
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
     * @param cls The classifier to generate.
     * @param sb Where to put the result.
     */
    private void generateClassifierBodyAssociations(Object cls,
						    StringBuffer sb) {
	Collection ends = ModelFacade.getAssociationEnds(cls);
	if (ends.isEmpty() || (generatorPass != HEADER_PASS)) {
	    return;
	}
   	String tv = null; // helper for tagged values
	// 2002-06-08
	// Jaap Branderhorst
	// Bugfix: ends is never null. Should check for isEmpty instead
	// old code:
	// if (ends != null)
	// new code:
	sb.append('\n');
	if (verboseDocs && ModelFacade.isAClass(cls)) {
	    sb.append(INDENT).append("// Associations\n");
	}

	// generate attributes in order public, protected, private
	for (int i = 0; i < ALL_PARTS.length; i++) {
	    int publicProtectedPrivate = ALL_PARTS[i];
	    Iterator endEnum = ends.iterator();
	    boolean isVisibilityLinePrinted = false;
	    while (endEnum.hasNext())
	    {
		Object ae = endEnum.next();
		Object a = ModelFacade.getAssociation(ae);
		if (((publicProtectedPrivate == PUBLIC_PART)
		     && ModelFacade.isPublic(ae))
		    || ((publicProtectedPrivate == PROTECTED_PART)
			&& ModelFacade.isProtected(ae))
		    || ((publicProtectedPrivate == PRIVATE_PART)
			&& ModelFacade.isPrivate(ae))) {
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

		    tv = generateTaggedValues(a, ALL_BUT_DOC_TAGS);
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
    private boolean checkGenerateOperationBody(Object cls)
    {
        boolean result =
            !((generatorPass == HEADER_PASS) 
                    || (ModelFacade.isAbstract(cls)) 
                    || ModelFacade.isAInterface(ModelFacade.getOwner(cls)));

	// if this operation has Tag "inline" the method shall be
	// generated in header
        if (ModelFacade.getTaggedValue(cls, "inline") != null) {
            result = generatorPass == HEADER_PASS;
        }
        return result;
    }

    /** 2002-12-13 Achim Spangler
     * generate a single set function for a given attribute and StringBuffer
     */
    private void generateSingleAttributeSet(Object attr, StringBuffer sb) {
	if (ModelFacade.getType(attr) == null) {
	    return;
	}
	// generate for attributes with class-type:
	// "INDENT void set_<name>( const <type> &value ) { <name> = value; };"
	// generate for other (small) data types:
	// "INDENT void set_<name>( <type> value ) { <name> = value; };"
	// generate: "INDENT void set_<name>( "
	sb.append('\n').append(INDENT);
	sb.append("/** simple access function to set the attribute ");
	sb.append(ModelFacade.getName(attr));
	sb.append(" by function\n").append(INDENT);
	sb.append("  * @param value value to set for the attribute ");
	sb.append(ModelFacade.getName(attr)).append("\n");
	sb.append(INDENT).append("  */\n");
	sb.append(INDENT);
	sb.append("void set_").append(ModelFacade.getName(attr)).append("( ");
	String modifier = generateAttributeParameterModifier(attr);
	if (modifier != null && modifier.length() > 0) {
	    // generate: "const <type> <modifier>value"
	    if (modifier.equals("&")) sb.append("const ");
	    sb.append(generateClassifierRef(ModelFacade.getType(attr)))
		.append(' ').append(modifier).append("value");
	}
	else if (ModelFacade.isAClass(ModelFacade.getType(attr))) {
	    // generate: "const <type> &value"
	    sb.append("const ");
	    sb.append(generateClassifierRef(ModelFacade.getType(attr)));
	    sb.append(" &value");
	} else {
	    // generate: "<type> value"
	    sb.append(generateClassifierRef(ModelFacade.getType(attr)))
		.append(" value");
	}
	// generate: " ) { <name> = value; };"
	sb.append(" ) { ").append(ModelFacade.getName(attr));
	sb.append(" = value; };");
    }

    /** 2002-12-13 Achim Spangler
     * generate a single get function for a given attribute and StringBuffer
     */
    private void generateSingleAttributeGet(Object attr, StringBuffer sb) {
	if (ModelFacade.getType(attr) == null) return;
	// generate for attributes with class-type:
	// "const <type>& get_<name>( void ) { return <name>; };"
	// generate for other (small) data types
	// "<type> get_<name>( void ) { return <name>; };"
	// generate: "INDENT"
	sb.append('\n').append(INDENT);
	sb.append("/** simple access function to get the attribute ");
	sb.append(ModelFacade.getName(attr));
	sb.append(" by function */\n").append(INDENT);
	String modifier = generateAttributeParameterModifier(attr);
	if (modifier != null && modifier.length() > 0)
	{
	    // generate: "const <type><modifier>"
	    sb.append("const ");
	    sb.append(generateClassifierRef(ModelFacade.getType(attr)));
	    sb.append(modifier);
	}
	else if (ModelFacade.isAClass(ModelFacade.getType(attr))) {
	    // generate: "const <type>&"
	    sb.append("const ");
	    sb.append(generateClassifierRef(ModelFacade.getType(attr)));
	    sb.append("&");
	} else {
	    // generate: "<type>"
	    sb.append(generateClassifierRef(ModelFacade.getType(attr)));
	}
	// generate: " get_<name>( void ) const { return <name>; };"
	sb.append(" get_").append(ModelFacade.getName(attr));
	sb.append("( void ) const { return ").append(ModelFacade.getName(attr));
	sb.append("; };");
    }

    /**
     * Generates the attributes of the body of a class or interface.
     * @param cls
     */
    private void generateClassifierBodyTaggedAccess4Attributes(
            Object cls,
            StringBuffer funcPrivate,
            StringBuffer funcProtected,
            StringBuffer funcPublic) {
	Collection strs = ModelFacade.getAttributes(cls);
	if (strs.isEmpty() || (generatorPass != HEADER_PASS)) {
	    return;
	}
	String accessTag = null;

	Iterator strEnum = strs.iterator();
	while (strEnum.hasNext())
	{
	    Object attr = strEnum.next();
	    accessTag = ModelFacade.getTaggedValueValue(attr, "set");
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

	    accessTag = ModelFacade.getTaggedValueValue(attr, "get");
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
     * @param sb Where to put the result.
     */
    private void generateClassifierBodyOperations(Object cls,
						  StringBuffer sb) {
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
	if (verboseDocs)
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
		Object bf = behEnum.next();
		if ((((publicProtectedPrivate == PUBLIC_PART)
		      && ModelFacade.isPublic(bf))
		     || ((publicProtectedPrivate == PROTECTED_PART)
			 && ModelFacade.isProtected(bf))
		     || ((publicProtectedPrivate == PRIVATE_PART)
			 && ModelFacade.isPrivate(bf)))
		    && ((generatorPass == HEADER_PASS)
			|| (checkGenerateOperationBody(bf)))) {
		    if ((!isVisibilityLinePrinted)
			&& (generatorPass == HEADER_PASS)) {
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

		    tv = generateTaggedValues(bf, ALL_BUT_DOC_TAGS);

		    if ((ModelFacade.isAClass(cls))
			&& (ModelFacade.isAOperation(bf))
			&& (!ModelFacade.isAbstract(bf))
			&& (checkGenerateOperationBody(bf)))
		    {
			// there is no ReturnType in behavioral feature (nsuml)
			sb.append("\n")
			    .append(generateMethodBody(bf));
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
     * @param sb Where to put the result.
     */
    private void generateClassifierBodyTypedefs(Object cls,
						StringBuffer sb) {
	if (generatorPass == HEADER_PASS) {
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
     * Generates a virtual destructor when the classifier is an interface.
     * @param cls the classifier object
     * @param sb the buffer to where the generate code goes
     */
    private void generateClassifierDestructor(Object cls,
                                              StringBuffer sb)
    {
        if (ModelFacade.isAInterface(cls) && generatorPass == HEADER_PASS) {
            sb.append("\npublic:\n");
            sb.append(INDENT).append("// virtual destructor for interface \n");
            sb.append(INDENT).append("virtual ").append('~').append(
                ModelFacade.getName(cls)).append("() { }\n");
        }
    }

    /**
     * Generates the body of a class or interface.
     * @param cls
     * @return a StringBuffer with the result.
     */
    private StringBuffer generateClassifierBody(Object cls) {
  	StringBuffer sb = new StringBuffer();
  	if (ModelFacade.isAClass(cls) || ModelFacade.isAInterface(cls))
	{ // add operations
	    // TODO: constructors
	    generateClassifierBodyOperations(cls, sb);
            
            // fixing issue #2587 
            generateClassifierDestructor(cls, sb);

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
    private String generateMethodBody(Object op) {
	if (op != null) {
	    StringBuffer sb = new StringBuffer(80);
	    Collection methods = ModelFacade.getMethods(op);
	    Iterator i = methods.iterator();
	    Object method = null;
	    boolean methodFound = false;
	    String tv = generateTaggedValues(op, ALL_BUT_DOC_TAGS);
	    String operationIndent =
		(generatorPass == HEADER_PASS) ? INDENT : "";

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
		method = i.next();

		if (method != null) {
		    //cat.info(", BODY of "+m.getName());
		    //cat.info("|"+m.getBody().getBody()+"|");
		    if ((ModelFacade.getBody(method) != null) 
		            && (!methodFound)) {
			Object body = ModelFacade.getBody(method);
			sb.append(ModelFacade.getBody(body));
			methodFound = true;
			break;
		    }
		}
	    }

	    if (!methodFound) {
		// pick out return type as default method body
		Object rp =
		    UmlHelper.getHelper().getCore().getReturnParameter(op);
		if (rp != null) {
		    Object returnType = ModelFacade.getType(rp);
		    sb.append(generateDefaultReturnStatement(returnType));
		}
	    }
	    sb.append(operationIndent).append("}\n")
		.append(generateSectionBottom(op, operationIndent));
	    return sb.toString();
	}
	return generateDefaultReturnStatement (null);
    }


    private String generateSectionTop(Object op, String localIndent) {
    	String id = ModelFacade.getUUID(op);
    	if (id == null) {
	    id = (new UID().toString());
	    // id =  op.getName() + "__" + static_count;
	    ModelFacade.setUUID(op, id);
    	}
    	return Section.generateTop(id, localIndent);
    }

    private String generateSectionBottom(Object op, String localIndent) {
    	String id = ModelFacade.getUUID(op);
    	if (id == null) {
	    id = (new UID().toString());
	    // id =  op.getName() + "__" + static_count;
	    ModelFacade.setUUID(op, id);
    	}
    	return Section.generateBottom(id, localIndent);
    }

    private String generateDefaultReturnStatement(Object cls) {
	if (cls == null) return "";

	String clsName = ModelFacade.getName(cls);
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

    private String generateTaggedValues(Object e, int tagSelection) {
	// cat.info("generateTaggedValues for element: " + e.getName()
	// + " und selection " + tagSelection);
	Iterator iter = ModelFacade.getTaggedValues(e);
	if (!iter.hasNext()) {
	    return "";
	}
	StringBuffer buf = new StringBuffer();
	boolean first = true;

	String s = null;
	while (iter.hasNext()) {
	    s = generateTaggedValue(iter.next(), tagSelection);
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

		    if (tagSelection == DOC_COMMENT_TAGS) {
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
		    if (tagSelection == DOC_COMMENT_TAGS)
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
	if (!first) {
	    if (tagSelection == DOC_COMMENT_TAGS) {
		buf.append("\n").append(INDENT).append(" */\n");
	    } else {
		buf.append ("}*/\n");
	    }
	}
	else if (tagSelection == DOC_COMMENT_TAGS) {
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

    private String generateTaggedValue(Object tv, int tagSelection) {
	// cat.info("generateTaggedValue: " +
	// generateName(tv.getTag()) + " mit selection: " +
	// tagSelection);
	if (tv == null) return "";
	String s = generateUninterpreted(ModelFacade.getValueOfTag(tv));
	
	String tagName = ModelFacade.getTagOfTag(tv);
	if (s == null || s.length() == 0 || s.equals("/** */")
	    || (tagName.indexOf("include") != -1)
	    || (tagName.indexOf("_incl") != -1)) {
	    return "";
	}
	if ((tagSelection == DOC_COMMENT_TAGS) 
	        && (isDocCommentTag(tagName))) {
	    return generateDocComment4Tag(generateName(tagName)) + s;
	} else if (((tagSelection == ALL_BUT_DOC_TAGS)
		    && (!isDocCommentTag(tagName))
		    && (!tagName.equals("documentation"))
		    && (!tagName.equals("javadocs"))
		   )
		  || (tagSelection == ALL_TAGS)) {
	    return tagName + "=" + s;
	} else {
	    return "";
	}
    }

    private Collection findTagValues(Object item, String searchedName) {
	Collection result = new Vector();

	Iterator iter = ModelFacade.getTaggedValues(item);
	String s = null;
	while (iter.hasNext())
	{
	    Object tag = iter.next();
	    if (ModelFacade.getTagOfTag(tag).equals(searchedName)) {
		s = ModelFacade.getValueOfTag(tag);
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

    /**
     * @see org.argouml.application.api.NotationProvider2#generateTaggedValue(java.lang.Object)
     */
    public String generateTaggedValue(Object tv) {
	if (tv == null) return "";
	String s = generateUninterpreted(ModelFacade.getValueOfTag(tv));
	if (s == null || s.length() == 0 || s.equals("/** */")) return "";
	String t = ModelFacade.getTagOfTag(tv);
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
    public String generateConstraintEnrichedDocComment(Object me,
						       Object ae) {
	// list tagged values for documentation
	String s = generateTaggedValues (me, DOC_COMMENT_TAGS);

	Object multiplicity = ModelFacade.getMultiplicity(ae);
	if (!(ModelFacade.M1_1_MULTIPLICITY.equals(multiplicity) 
	        || ModelFacade.M0_1_MULTIPLICITY.equals (multiplicity))) {
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
	    Object type = ModelFacade.getType(ae);
	    if (type != null) {
		sDocComment.append(" @element-type ");
		sDocComment.append(ModelFacade.getName(type));
	        // } else {
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

    private String generateAssociationFrom(Object a, Object ae) {
	// TODO: does not handle n-ary associations
	StringBuffer sb = new StringBuffer(80);

	/*
	 * Moved into while loop 2001-09-26 STEFFEN ZSCHALER
	 *
	 * Was:
	 *
	 s += DocumentationManager.getDocs(a) + "\n" + INDENT;
	*/

	Collection connections = ModelFacade.getConnections(a);
	Iterator connEnum = connections.iterator();
	while (connEnum.hasNext()) {
	    Object ae2 = connEnum.next();
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

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAssociation(java.lang.Object)
     */
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

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAssociationEnd(java.lang.Object)
     */
    public String generateAssociationEnd(Object ae) {
	if (!ModelFacade.isNavigable(ae)) {
	    return "";
	}
	if (ModelFacade.isAbstract(ModelFacade.getAssociation(ae))) {
	    return "";
	}
	//String s = INDENT + "protected ";
	// must be public or generate public navigation method!
	//String s = INDENT + "public ";
	StringBuffer sb = new StringBuffer(80);
	// cat.info("generate Visibility for Attribute");

	//    sb.append(INDENT).append(generateVisibility(ae.getVisibility()));

	sb.append(generateAssociationEndScope(ae));
	//     String n = ae.getName();
	//     if (n != null && !String.UNSPEC.equals(n))
	//         s += generateName(n) + " ";
	//     if (ae.isNavigable()) s += "navigable ";
	//     if (ae.getIsOrdered()) s += "ordered ";

	String n = ModelFacade.getName(ae);
	Object asc = ModelFacade.getAssociation(ae);
	String ascName = ModelFacade.getName(asc);
	String name = null;

	if (n != null  && n != null && n.length() > 0) {
	    name = generateName(n);
	} else if (ascName != null  
	           && ascName != null 
	           && ascName.length() > 0) {
	    name = generateName(ascName);
	} else {
	    name = "my" + generateClassifierRef(ModelFacade.getType(ae));
	}

	sb.append(
	        generateMultiplicity(ae, name, 
	        		     ModelFacade.getMultiplicity(ae),
	        		     generateAttributeParameterModifier(asc)));

	return (sb.append(";\n")).toString();
    }


    ////////////////////////////////////////////////////////////////
    // internal methods?


    private String generateGeneralization(Collection generalizations) {
	if (generalizations == null) {
	    return "";
	}
	StringBuffer sb = new StringBuffer(80);
	Iterator genEnum = generalizations.iterator();
	while (genEnum.hasNext()) {
	    Object generalization = genEnum.next();
	    Object ge = ModelFacade.getParent(generalization);
	    // assert ge != null
	    if (ge != null) {
		String visibilityTag = 
		    ModelFacade.getTaggedValueValue(generalization, 
		            			    "visibility");
		if (visibilityTag != null && visibilityTag != "")
		    sb.append(visibilityTag).append(" ");
		sb.append(generateNameWithPkgSelection(ge));
		if (genEnum.hasNext()) sb.append(", ");
	    }
	}
	return sb.toString();
    }

    private Collection getGeneralizationClassList(Collection generalizations) {
	if (generalizations == null) {
	    return null;
	}
	Collection classes = new ArrayList();
	Iterator genEnum = generalizations.iterator();
	while (genEnum.hasNext()) {
	    Object generalization = genEnum.next();
	    Object ge = ModelFacade.getParent(generalization);
	    // assert ge != null
	    if (ge != null) {
	        classes.add(ge);
	    }
	}
	return classes;
    }

    //  public String generateSpecification(Collection realizations) {
    private String generateSpecification(Object cls) {
	Collection deps = ModelFacade.getClientDependencies(cls);
	Iterator depIterator = deps.iterator();
	StringBuffer sb = new StringBuffer(80);

	while (depIterator.hasNext()) {
	    Object dependency = depIterator.next();
	    if (ModelFacade.isAAbstraction(dependency)
	            && ModelFacade.isRealize(dependency)) {

		Object iFace = 
		    ModelFacade.getSuppliers(dependency).iterator().next();
		String visibilityTag = 
		    ModelFacade.getTaggedValueValue(dependency, "visibility");
		if (visibilityTag != null && visibilityTag != "")
		    sb.append(visibilityTag).append(" ");
		sb.append(generateNameWithPkgSelection(iFace));
		if (depIterator.hasNext()) sb.append(", ");

	    }
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

    /**
     * @see org.argouml.application.api.NotationProvider2#generateVisibility(java.lang.Object)
     */
    public String generateVisibility(Object handle) {
	if (ModelFacade.isAAttribute(handle)) {
	    return "";
	}
	if (ModelFacade.isAFeature(handle)) {
	    handle = ModelFacade.getVisibility(handle);
	}
	if (ModelFacade.isPublic(handle)) return "public ";
	if (ModelFacade.isPrivate(handle)) return "private ";
	if (ModelFacade.isProtected(handle)) return "protected ";
	return "";
    }

    private String generateAssociationEndScope(Object ae) {
        return generateScope(ModelFacade.getTargetScope(ae));
    }

    private String generateOwnerScope(Object f) {
	return generateScope(ModelFacade.getOwnerScope(f));
    }

    /**
     * @param scope The scope to compare.
     * @return The generated text representing the scope.
     */
    private String generateScope(Object scope) {
        if (ModelFacade.CLASSIFIER_SCOPEKIND.equals(scope)) {
            return "static ";
        }
        return "";
    }


    /**
     * Generate "abstract" keyword for an abstract operation.
     * In C++, since it does not have an explicit "interface" keyword, we must
     * check against this and set the operation to abstract if so.
     */
    private String generateAbstractness(Object op) {
        // use ModelFacade to check if the operation is owned by an interface
        Object opOwner = ModelFacade.getOwner(op);
	if (ModelFacade.isAbstract(op) || ModelFacade.isAInterface(opOwner)) {
	    return " = 0";
	}
	else {
	    return "";
	}
    }

    /**
     * Generate "final" keyword for final operations.
     */
    private String generateOperationChangeability(Object op) {
	if (ModelFacade.isLeaf(op) || ModelFacade.isQuery(op)) {
	    return "const ";
	}
	else {
	    return "";
	}
    }

    /**
     * Generate "const" keyword for const pointer/reference parameters.
     */
    private String generateParameterChangeability(Object par) {
	if (checkAttributeParameter4Tag(par, SEARCH_REFERENCE_POINTER_TAG)
	        && (ModelFacade.getKind(par)).equals(
	                ModelFacade.IN_PARAMETERDIRECTIONKIND)) {
	    return "const ";
	} else {
	    return "";
	}
    }

    private String generateStructuralFeatureChangability(Object sf) {
	Object changeableKind = ModelFacade.getChangeability(sf);
	if (ModelFacade.FROZEN_CHANGEABLEKIND.equals(changeableKind)) {
	    return "final ";
	}
	// if (ModelFacade.ADD_ONLY_CHANGEABLEKIND.equals(changeableKind)) {
	//     return "final ";
	// }
	return "";
    }

    /**
     * Generates "synchronized" keyword for guarded operations.
     * @param op The operation
     * @return The synchronized keyword if the operation is guarded, else ""
     */
    private String generateConcurrency(Object op) {
        Object concurrency = ModelFacade.getConcurrency(op);
	if (concurrency != null
	    && (ModelFacade.getValue(concurrency) 
	            == ModelFacade.GUARDED_CONCURRENCYKIND)) {
	    return "synchronized ";
	}
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

	Iterator rangeEnum = ModelFacade.getRanges(multiplicity);
	while (rangeEnum.hasNext()) {
	    Object multiplicityRange = rangeEnum.next();
	    s += generateMultiplicityRange(multiplicityRange);
	    if (rangeEnum.hasNext()) s += ",";
	}
	return s;
    }

    private String generateMultiplicity(Object item, String name,
				        Object m, String modifier) {
	String type = null;
	String containerType = null;
	// cat.info("generateMultiplicity mit item" + item.getName() +
	// ", name: " + name + ", modifier: " + modifier);

	Object typeCls = null;
	if (ModelFacade.isAAssociationEnd(item)
	        || ModelFacade.isAAttribute(item)) {
	    typeCls = ModelFacade.getType(item);
	} else if (ModelFacade.isAClassifier(item)) {
	    type = ModelFacade.getName(item);
	} else {
	    type = "";
	}
	if (typeCls != null) {
	    type = generateNameWithPkgSelection(typeCls);
	}
	// cat.info("resolved type_name: " + type);
	if (m == null) { 
	    return (type + " " + modifier + name); 
	}
	StringBuffer sb = new StringBuffer(80);
	int countUpper = ModelFacade.getUpper(m);
	int countLower = ModelFacade.getLower(m);
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
	    String multType = 
	        ModelFacade.getTaggedValueValue(item, "MultiplicityType");
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

    private String generateMultiplicityRange(Object mr) {
	Integer lower = new Integer(ModelFacade.getLower(mr));
	Integer upper = new Integer(ModelFacade.getUpper(mr));
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
    public String generateState(Object handle) {
	return ModelFacade.getName(handle);
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateStateBody(java.lang.Object)
     */
    public String generateStateBody(Object state) {
	// cat.info("GeneratorCpp: generating state body");
	String s = "";
	Object entry = ModelFacade.getEntry(state);
	Object exit = ModelFacade.getExit(state);
	if (entry != null) {
	    String entryStr = Generate(entry);
	    if (entryStr.length() > 0) s += "entry / " + entryStr;
	}
	if (exit != null) {
	    String exitStr = Generate(exit);
	    if (s.length() > 0) s += "\n";
	    if (exitStr.length() > 0) s += "exit / " + exitStr;
	}
	Collection trans = ModelFacade.getInternalTransitions(state);
	if (trans != null) {
	    Iterator iter = trans.iterator();
	    while (iter.hasNext())
	    {
		if (s.length() > 0) s += "\n";
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
    public String generateTransition(Object transition) {
	String s = generate(ModelFacade.getName(transition));
	String t = generate(ModelFacade.getTrigger(transition));
	String g = generate(ModelFacade.getGuard(transition));
	String e = generate(ModelFacade.getEffect(transition));
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

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAction(java.lang.Object)
     */
    public String generateAction(Object m) {
	// return m.getName();
	Object script = ModelFacade.getScript(m);
	if ((script != null) && (ModelFacade.getBody(script) != null))
	    return ModelFacade.getBody(script).toString();
	return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateGuard(java.lang.Object)
     */
    public String generateGuard(Object guard) {
	//return generateExpression(m.getExpression());
	if (ModelFacade.getExpression(guard) != null)
	    return generateExpression(ModelFacade.getExpression(guard));
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
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { return "GeneratorCpp"; }
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
        return "Cpp Notation and Code Generator";
    }
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { return "Achim Spangler"; }
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { return "0.9.8"; }
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.language.cpp.generator"; }
    /**
     * Returns the _lfBeforeCurly.
     * @return boolean
     */
    public boolean isLfBeforeCurly()
    {
        return lfBeforeCurly;
    }

    /**
     * Returns the _verboseDocs.
     * @return boolean
     */
    public boolean isVerboseDocs()
    {
        return verboseDocs;
    }

    /**
     * Sets the _lfBeforeCurly.
     * @param beforeCurly The _lfBeforeCurly to set
     */
    public void setLfBeforeCurly(boolean beforeCurly)
    {
        this.lfBeforeCurly = beforeCurly;
    }

    /**
     * Sets the _verboseDocs.
     * @param verbose The _verboseDocs to set
     */
    public void setVerboseDocs(boolean verbose)
    {
        this.verboseDocs = verbose;
    }
    


    /**
     * @see org.argouml.application.api.NotationProvider2#generateActionState(java.lang.Object)
     */
    public String generateActionState(Object actionState) {       
        return generateState(actionState);
    }
} /* end class GeneratorCpp */

