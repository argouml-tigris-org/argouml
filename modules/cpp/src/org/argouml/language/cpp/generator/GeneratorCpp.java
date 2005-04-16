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
import org.argouml.model.Model;
import org.argouml.uml.DocumentationManager;
import org.argouml.uml.UUIDHelper;
import org.argouml.uml.generator.FileGenerator;
import org.argouml.uml.generator.Generator2;

/**
 * Generator2 subclass to generate C++ source code that is used in ArgoUML GUI
 * and text fields when the cpp notation is selected by the user. It also
 * implements FileGenerator and makes it possible for the user to generate
 * the source code files for model elements of his choice.
 */
public class GeneratorCpp extends Generator2
    implements PluggableNotation, FileGenerator {

    /**
     * The logger.
     */
    private static final Logger LOG = Logger.getLogger(GeneratorCpp.class);

    private boolean verboseDocs = false;
    private boolean lfBeforeCurly = false;
    /**
     * TODO: make it configurable.
     */
    private static final boolean VERBOSE_DOCS = false;

    private static final String ANY_RANGE = "0..*";

    private static Section sect;

    /**
     * Store actual namespace, to avoid unneeded curley braces.
     *
     * @author Achim Spangler
     * @since 2002-12-07
     */
    private Object actualNamespace;

    /**
     * Store extra include dependencies which are generated during
     * generation of multiplicity to get needed container type.
     *
     * @author Achim Spangler
     * @since 2002-12-12
     */
    private String extraIncludes = "";

    /**
     * System newline separator.
     */
    private static final String LINE_SEPARATOR =
        System.getProperty("line.separator");


    /**
     * C++ doesn't place visibility information for each class member
     * --> sort items during generation and store visibility state
     * of lastly generated member in central class variable, so that
     * the appropriate lines: "public:\n", "protected:\n", "private:\n"
     * can be created.
     *
     * @author Achim Spangler
     * @since 2002-11-28
     */
    private static final int PUBLIC_PART = 0;
    private static final int PROTECTED_PART = 1;
    private static final int PRIVATE_PART = 2;

    private static final int[] ALL_PARTS = {
        PUBLIC_PART,
        PROTECTED_PART,
        PRIVATE_PART,
    };

    private static final String[] PART_NAME = {
        "public", "protected", "private"
    };

    /**
     * C++ uses two files for each class: header (.h) with class definition
     * and source (.cpp) with methods implementation
     * --> two generation passes are needed.
     *
     * @author Achim Spangler
     * @since 2002-11-28
     */
    private static final int NONE_PASS = 1;
    private static final int HEADER_PASS = 2;
    private static final int SOURCE_PASS = 3;
    private static int generatorPass = NONE_PASS;

    /**
     * use Tag generation for generation of: doccomment, simple tags of
     * tags which are not used for doccomment or simple tags for all.
     *
     * @author Achim Spangler
     * @since 2002-12-05
     */
    private static final int DOC_COMMENT_TAGS = 1;
    private static final int ALL_BUT_DOC_TAGS = 2;
    private static final int ALL_TAGS = 3;

    /**
     * C++ developers need to specify for parameters whether they are
     * pointers or references (especially for class-types)
     * -> a general check function must get the searched tag.
     *
     * @author Achim Spangler
     * @since 2002-12-06
     */
    private static final int NORMAL_MOD = 0;
    private static final int REFERENCE_MOD = 1;
    private static final int POINTER_MOD = 2;

    private static final GeneratorCpp SINGLETON = new GeneratorCpp();

    /**
     * Prefix for names in the std namespace. Defaults to "std::",
     * but could be "" if a "using namsepace std;" directive is used.  
     */
    private String stdPrefix = "std::";
    
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
        super (Notation.makeNotation("Cpp", null,
                                     Argo.lookupIconResource ("CppNotation")));
    }

    /**
     * @param o the object to be generated
     * @return the generated string
     */
    public static String cppGenerate(Object o) {
        return SINGLETON.generate(o);
    }

    /**
     * Generate the header code for the given object
     * @param o the object to be generated
     * @return the generated header as a string
     */
    public String generateH(Object o) {
        generatorPass = HEADER_PASS;
        String ret = generate(o);
        generatorPass = NONE_PASS;
        return ret;
    }

    /** 2002-11-28 Achim Spangler
     * @return file extension for actual generation pass
     */
    private String getFileExtension() {
        if (generatorPass == HEADER_PASS) return ".h";
        return ".cpp";
    }

    /**
     * Generate the relative path for the specified object.
     *
     * @return path relative to the project root (without name)
     */
    private String generateRelativeDirPath(Object cls) {
        String packagePath = "";
        // avoid model being used as a package name
        Object parent =
            Model.getFacade().getNamespace(Model.getFacade().getNamespace(cls));
        if (parent != null) {
            packagePath =
                Model.getFacade().getName(Model.getFacade().getNamespace(cls));
        }
        while (parent != null) {
            // ommit root package name; it's the model's root
            if (Model.getFacade().getNamespace(parent) != null) {
                packagePath =
                    Model.getFacade().getName(parent) + "." + packagePath;
            }
            parent = Model.getFacade().getNamespace(parent);
        }

       return packagePath;
    }

    /**
     * create the needed directories for the derived appropriate pathname
     * @return full pathname
     */
    private String generateDirectoriesPathname(Object cls, String path) {
        String name = Model.getFacade().getName(cls);
        if (name == null || name.length() == 0) {
            return null;
        }

        if (!path.endsWith (FILE_SEPARATOR)) {
            path += FILE_SEPARATOR;
        }

        String packagePath = generateRelativeDirPath(cls);
        String filename = name + getFileExtension();

        int lastIndex = -1;
        do {
            File f = new File (path);
            if (!f.isDirectory()) {
                if (!f.mkdir()) {
                    LOG.error(" could not make directory " + path);
                    return null;
                }
            }

            if (lastIndex == packagePath.length()) {
                break;
            }

            int index = packagePath.indexOf (".", lastIndex + 1);
            if (index == -1) {
                index = packagePath.length();
            }

            path += packagePath.substring(lastIndex + 1, index)
                + FILE_SEPARATOR;
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
     * It works only for classes and interfaces, everything else is ignored.
     *
     * Param node is the node.
     * Param path is the base path for the source.
     * Returns the full path name of the the generated file, or null if 'o'
     * is not a class or a interface.
     *
     * @see org.argouml.uml.generator.FileGenerator#generateFile2(
     * java.lang.Object, java.lang.String)
     */
    public String generateFile2(Object o, String path) {
        if (!Model.getFacade().isAClass(o)
               && !Model.getFacade().isAInterface(o)) {
            // TODO: is returning null a correct behaviour here?
            return null;
        }
        String packagePath =
            Model.getFacade().getName(Model.getFacade().getNamespace(o));
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
            if (generatorPass == SOURCE_PASS 
                    && Model.getFacade().isAInterface(o))
                break; // don't generate the .cpp, it's useless.

            String headerTop = generateHeaderTop(pathname);
            String header = generateHeader(o, packagePath);
            String src = generate(o);
            BufferedWriter fos = null;
            try {
                fos = new BufferedWriter (new FileWriter (f));
                if (generatorPass == HEADER_PASS) {
                    String name = Model.getFacade().getName(o);
                    String guard = generateRelativeDirPath(o) + '_' 
                        + name + getFileExtension();
                    guard = guard.replace('/', '_').replace('.', '_');
                    fos.write("#ifndef " + guard + LINE_SEPARATOR 
                            + "#define " + guard 
                            + LINE_SEPARATOR + LINE_SEPARATOR);
                }
                writeTemplate(o, path, fos);
                fos.write(headerTop);
                fos.write(extraIncludes);
                fos.write(header);
                fos.write(src);
                if (generatorPass == HEADER_PASS) {
                    fos.write("#endif");
                    fos.write(LINE_SEPARATOR);
                }
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
            if (generatorPass == HEADER_PASS)   {
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
        // reset generator pass to NONE for the notation to be correct
        generatorPass = NONE_PASS;
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
    private void writeTemplate(Object cls, String path, BufferedWriter fos) {
        String templatePathName = path + "/templates/";
        String fileName = Model.getFacade().getName(cls);
        String tagTemplatePathName =
            Model.getFacade().getTaggedValueValue(cls, "TemplatePath");
        String authorTag = Model.getFacade().getTaggedValueValue(cls, "author");
        String emailTag = Model.getFacade().getTaggedValueValue(cls, "email");
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
        
        File templateFile = new File(templatePathName);
        if (templateFile.exists()) {
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

                        fos.write(line + LINE_SEPARATOR);
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
    private void replaceToken(StringBuffer line, String tokenName,
            String tokenValue) {
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
    private String generateHeaderTop(String pathname) {
        StringBuffer sb = new StringBuffer(80);
        //TODO: add user-defined copyright
        if (VERBOSE_DOCS) {
            sb.append("// FILE: ").append(pathname.replace('\\', '/'));
            sb.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
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

    /**
     * As each language has its own syntax to incorporate other elements
     * the command for this inclusion is created in a seperate function.
     *
     * @author Achim Spangler
     * @since 2002-11-28
     */
    private String generateHeaderImportLine4Item(Object clsDepend) {
        StringBuffer sb = new StringBuffer(80);
        String packagePath = Model.getFacade().getName(clsDepend);
        Object parent = Model.getFacade().getNamespace(Model.getFacade()
                .getNamespace(clsDepend));
        if (parent != null) {
            packagePath =
                Model.getFacade().getName(
                        Model.getFacade().getNamespace(clsDepend))
                + "/" + packagePath;
            while (parent != null) {
                // ommit root package name; it's the model's root
                if (Model.getFacade().getNamespace(parent) != null) {
                    packagePath =
                        Model.getFacade().getName(parent) + "/" + packagePath;
                }
                parent = Model.getFacade().getNamespace(parent);
            }
        }

        // if class depends on a list of other classes, all inheritance
        // elements are seperated by Tokens ", " -> use StringTokenizer
        // to access the single elements
        StringTokenizer st = new StringTokenizer(packagePath, ", ");
        while (st.hasMoreTokens()) {
            sb.append("#include <").append(st.nextToken()).append(".h>")
                .append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    private boolean checkInclude4UsageIndirection(boolean isIndirect,
            String usageTag) {
        boolean result = false;

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

        Iterator iter = Model.getFacade().getTaggedValues(cls);
        while (iter.hasNext()) {
            Object tv = iter.next();
            String tag = Model.getFacade().getTagOfTag(tv);
            if (tag.equals("usage")) {
                usageTag = Model.getFacade().getValueOfTag(tv);
            }

            if (tag.indexOf("ref") != -1 || tag.equals("&")
                    || tag.indexOf("pointer") != -1 || tag.equals("*")) {
                predeclareCandidate = true;
            }
        }
        return checkInclude4UsageIndirection(predeclareCandidate, usageTag);
    }


    private boolean checkIncludeNeeded4ElementAttribute(Object cls) {
        if (!(Model.getFacade().isAClass(Model.getFacade().getType(cls)))) {
            return false;
        }
        return checkIncludeNeeded4Element(cls);
    }

    private String generateHeaderDependencies(Object cls) {
        StringBuffer sb = new StringBuffer(160);
        StringBuffer predeclare = new StringBuffer(60);

        if (generatorPass == SOURCE_PASS) { // include header in .cpp
            sb.append(generateHeaderImportLine4Item(cls));

            Iterator iter = Model.getFacade().getTaggedValues(cls);
            while (iter.hasNext()) {
                Object tv = iter.next();
                String tag = Model.getFacade().getTagOfTag(tv);
                if (tag.equals("source_incl")
                        || tag.equals("source_include")) {
                    sb.append("#include ");
                    sb.append(Model.getFacade().getValueOfTag(tv));
                    sb.append(LINE_SEPARATOR);
                }
            }
        }
        else {
            Collection baseClassList = getGeneralizationClassList(
                    Model.getFacade().getGeneralizations(cls));
            sb.append(generateHeaderImportLine4ItemList(baseClassList));

            Iterator iter = Model.getFacade().getTaggedValues(cls);
            while (iter.hasNext()) {
                Object tv = iter.next();
                String tag = Model.getFacade().getTagOfTag(tv);
                if (tag.equals("header_incl")
                        || tag.equals("header_include")) {
                    sb.append("#include ");
                    sb.append(Model.getFacade().getValueOfTag(tv));
                    sb.append(LINE_SEPARATOR);
                }
            }
        }

        // check if the class has dependencies
        {
            Collection col = Model.getFacade().getAssociationEnds(cls);
            if (col != null) {
                Iterator itr = col.iterator();
                while (itr.hasNext()) {
                    Object ae = itr.next();
                    if (Model.getFacade().isNavigable(ae)) {
                        Object cls2 = Model.getFacade().getType(ae);
                        String name = Model.getFacade().getName(cls2);
                        String name2 = Model.getFacade().getName(cls);
                        if (name != name2) {
                            if (checkIncludeNeeded4Element(
                                    Model.getFacade().getAssociation(ae))) {
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
                                predeclare.append(";").append(LINE_SEPARATOR);
                            }
                        }
                    }
                }
            }
        }

        {
            Collection col = Model.getFacade().getAttributes(cls);
            if (col != null) {
                Iterator itr = col.iterator();
                while (itr.hasNext()) {
                    Object attr = itr.next();
                    // " mit Typ: " + attr.getType());
                    if (Model.getFacade().isAClass(Model.getFacade()
                            .getType(attr))) {
                        String name = Model.getFacade().getName(attr);
                        if (checkIncludeNeeded4ElementAttribute(attr)) {
                            sb.append(generateHeaderImportLine4Item(
                                Model.getFacade().getType(attr)));
                        }
                        else if (generatorPass == HEADER_PASS) {
                            // predeclare classes which are not
                            // directly used in header usefull for
                            // classes which are only used indirectly
                            // as pointer where no knowledge about
                            // internals of class are needed
                            predeclare
                                .append(generateHeaderPackageStart(
                                             Model.getFacade().getType(attr)))
                                .append("class ").append(name).append(";")
                                    .append(LINE_SEPARATOR);
                        }
                    }
                }
            }
        }

        {
            Collection col = Model.getFacade().getClientDependencies(cls);
            if (col != null) {
                Iterator itr = col.iterator();
                while (itr.hasNext()) {
                    Object dependency = itr.next();
                    if (dependency == null) {
                        break;
                    }
                    Collection clientsCol =
                        Model.getFacade().getSuppliers(dependency);
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
                            predeclare.append(Model.getFacade().getName(temp));
                            predeclare.append(";").append(LINE_SEPARATOR);
                        }
                    }
                }
            }
        }

        if (predeclare.toString().length() > 0) {
            sb.append(LINE_SEPARATOR).append(LINE_SEPARATOR)
                .append(predeclare.toString());
        }
        return sb.toString();
    }

    private String generateHeaderPackageStartSingle(Object pkg) {
        StringBuffer sb = new StringBuffer(30);
        String packageName = Model.getFacade().getName(pkg);
        StringTokenizer st = new StringTokenizer(packageName, ".");
        String token = "";

        sb.append(generateTaggedValues(pkg, DOC_COMMENT_TAGS));
        while (st.hasMoreTokens()) {
            token = st.nextToken();
            // create line: namespace FOO {"
            sb.append("namespace ").append(token).append(" {")
                .append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    private String generateHeaderPackageEndSingle(Object pkg) {
        StringBuffer sb = new StringBuffer(30);
        String packageName = Model.getFacade().getName(pkg);
        StringTokenizer st = new StringTokenizer(packageName, ".");
        String token = "";
        while (st.hasMoreTokens()) {
            token = st.nextToken();
            StringBuffer tempBuf = new StringBuffer(20);
            String absoluteName = generatePackageAbsoluteName(pkg);
            if (absoluteName.indexOf(token) != -1) {
                absoluteName =
                    absoluteName.substring(0,
                               (absoluteName.indexOf(token)
                                + token.length()));
            }

            // create line: namespace FOO {"
            tempBuf.append("} /* End of namespace ").append(absoluteName);
            tempBuf.append(" */").append(LINE_SEPARATOR);
            sb.insert(0, tempBuf.toString());
        }
        return sb.toString();
    }

    private String generatePackageAbsoluteName(Object pkg) {
        StringBuffer sb = new StringBuffer(30);
        String token = "";
        for (Object actual = pkg;
             actual != null;
             actual = getNamespaceWithoutModel(actual)) {
            StringTokenizer st =
                new StringTokenizer(Model.getFacade().getName(actual), ".");
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

    private String generateNameWithPkgSelection(Object item, Object localPkg) {
        if (item == null) {
            return "void ";
        }
        Object pkg = null;
        if (Model.getFacade().isADataType(item)) {
            return generateName(Model.getFacade().getName(item));
        } else if (Model.getFacade().isAParameter(item)
                   || Model.getFacade().isAAttribute(item)
                   || Model.getFacade().isAAssociationEnd(item)
                   || Model.getFacade().isAClassifier(item)) {
            pkg = getNamespaceWithoutModel(item);
        }

        if (pkg == null) {
            return generateName(Model.getFacade().getName(item));
        }
        if (localPkg == null) {
            LOG.info("LOCAL NAMESPACE IS NULL");
        }

        String localPkgName = generatePackageAbsoluteName(localPkg);
        String targetPkgName = generatePackageAbsoluteName(pkg);

        int localPkgNameLen = localPkgName.length();
        int targetPkgNameLen = targetPkgName.length();
        if (localPkgName.equals(targetPkgName)) {
            return generateName(Model.getFacade().getName(item));
        }
        if (targetPkgName.indexOf(localPkgName) != -1) {
            if (targetPkgName.substring(localPkgNameLen, localPkgNameLen + 2)
                .equals("::")) {
                // target is in Sub-Package of local class
                return (targetPkgName.substring(localPkgNameLen + 2,
                                targetPkgNameLen)
                    + "::"
                    + generateName(Model.getFacade().getName(item)));
            }
        }
        return (targetPkgName + "::" 
                + generateName(Model.getFacade().getName(item)));
    }

    private String generateNameWithPkgSelection(Object item) {
        return generateNameWithPkgSelection(item, actualNamespace);
    }

    private String generateHeaderPackageStart(Object cls) {
        StringBuffer sb = new StringBuffer(80);

        if (actualNamespace != null) {
            for (Object fromSearch = actualNamespace;
                    fromSearch != null;
                    fromSearch = getNamespaceWithoutModel(fromSearch)) {
                StringBuffer contPath = new StringBuffer(80);
                Object toSearch = getNamespaceWithoutModel(cls);
                for (; (toSearch != null) && (toSearch != fromSearch);
                        toSearch = getNamespaceWithoutModel(toSearch)) {
                    contPath.insert(0,
                            generateHeaderPackageStartSingle(toSearch));
                }
                if (toSearch == fromSearch) {
                    sb.append(contPath.toString());
                    break;
                }
                // close one namespace
                sb.append(generateHeaderPackageEndSingle(fromSearch));
            }
        }
        else { // initial start
            for (Object toSearch = getNamespaceWithoutModel(cls);
                    toSearch != null;
                    toSearch = getNamespaceWithoutModel(toSearch)) {
                sb.insert(0, generateHeaderPackageStartSingle(toSearch));
            }
        }
        if (sb.length() > 0) {
            sb.insert(0, LINE_SEPARATOR).append(LINE_SEPARATOR);
        }
        actualNamespace = getNamespaceWithoutModel(cls);
        return sb.toString();
    }

    /**
     * Retrieve the namespace of the given model element, excluding the model,
     * which shouldn't be considered a namespace.
     * @param me the model element for which to get the namespace
     * @return the namespace if it exists or null if the model is the
     * containing namespace for <code>me</code>
     */
    private Object getNamespaceWithoutModel(Object me) {
        Object parent = Model.getFacade().getNamespace(me);
        if (parent != null && Model.getFacade().getNamespace(parent) != null) 
            return parent;
        return null;
    }

    private String generateHeaderPackageEnd() {
        StringBuffer sb = new StringBuffer(20);

        for (Object closeIt = actualNamespace;
                closeIt != null;
                closeIt = getNamespaceWithoutModel(closeIt)) {
            sb.append(generateHeaderPackageEndSingle(closeIt));
        }
        actualNamespace = null;
        if (sb.length() > 0) {
            sb.insert(0, LINE_SEPARATOR).append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    private String generateHeader(Object cls, String packagePath) {
        StringBuffer sb = new StringBuffer(240);

        // check if the class has a base class
        sb.append(generateHeaderDependencies(cls));

        sb.append(LINE_SEPARATOR);

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
     * @see org.argouml.application.api.NotationProvider2#generateSubmachine(java.lang.Object)
     */
    public String generateSubmachine(Object m) {
        Object c = Model.getFacade().getSubmachine(m);
        if (c == null) {
            return "include / ";
        }
        if (Model.getFacade().getName(c) == null) {
            return "include / ";
        }
        if (Model.getFacade().getName(c).length() == 0) {
            return "include / ";
        }
        return ("include / " + generateName(Model.getFacade().getName(c)));
    }

    
    /**
     * @see org.argouml.application.api.NotationProvider2#generateObjectFlowState(java.lang.Object)
     */
    public String generateObjectFlowState(Object m) {
        Object c = Model.getFacade().getType(m);
        if (c == null) return "";
        return Model.getFacade().getName(c);
    }

    /** 2002-11-28 Achim Spangler
     * seperate generation of Operation Prefix from generateOperation
     * so that generateOperation is language independent
     */
    private String generateOperationPrefix(Object op) {
        StringBuffer sb = new StringBuffer(80);
        // c++ doesn't have any builtin construct for concurrency
        //sb.append(generateConcurrency(op));
        if (generatorPass != SOURCE_PASS) {
            // make all operations to virtual - as long as they are not "leaf"
            Object scope = Model.getFacade().getOwnerScope(op);
            if (Model.getFacade().isLeaf(op)
		    && !Model.getFacade().isRoot(op)) {
                // there's no way to make a leaf method that it's not root in
                // c++, so warn the user and ignore the 'root' attribute
                // (or it may be better to ignore the 'leaf' attribute?)
                LOG.warn(op + " is leaf but not root: "
			 + "C++ can't handle this properly");
		LOG.warn("    Ignoring the 'root' attribute");
            }
            // generate a function as virtual, if it can be overriden
            // or override another function AND if this function is
            // not marked as static, which disallows "virtual"
            // alternatively every abstract function is defined as
            // virtual
            if ((!Model.getFacade().isLeaf(op)
                    && !Model.getFacade().isConstructor(op)
                    && (!(Model.getScopeKind().getClassifier().equals(scope))))
                    || (Model.getFacade().isAbstract(op))) {
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
            StringBuffer sb) {
        if (generatorPass == SOURCE_PASS) {
            sb.append(Model.getFacade().getName(
                          Model.getFacade().getOwner(op))).append("::");
        }
        boolean constructor = false;
        String name;
        if (Model.getFacade().isConstructor(op)) {
            // constructor
            name = Model.getFacade().getName(Model.getFacade().getOwner(op));
            constructor = true;
        } else {
            name = Model.getFacade().getName(op);
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
        if ((generatorPass == SOURCE_PASS) 
                && (Model.getFacade().isAbstract(op))) {
            return "";
        }
        StringBuffer sb = new StringBuffer(80);
        StringBuffer nameBuffer = new StringBuffer(20);
        String operationIndent = (generatorPass == HEADER_PASS) ? INDENT : "";
        boolean constructor =
            generateOperationNameAndTestForConstructor(op, nameBuffer);

        // if generating a file always document
        if (documented || generatorPass != NONE_PASS) {
            // generate DocComment from tagged values
            String tv = generateTaggedValues (op, DOC_COMMENT_TAGS);
            if (tv != null && tv.length() > 0) {
                sb.append (LINE_SEPARATOR).append(operationIndent).append (tv);
            }
        }

        sb.append(operationIndent)
            .append(generateOperationPrefix(op));

        // pick out return type
        Object rp = Model.getCoreHelper().getReturnParameter(op);
        if (!constructor) {
            if (rp != null) {
                Object returnType = Model.getFacade().getType(rp);
                if (returnType == null) {
                    sb.append("void ");
                }
                else if (returnType != null) {
                    sb.append(generateNameWithPkgSelection(returnType))
                        .append(' ');
                    /* fixing 2862 - apply modifiers, 
                     * i.e. pointer or reference TV */
                    sb.append(generateAttributeParameterModifier(rp));
                }
            }
        }

        // name and params
        Vector params = new Vector(Model.getFacade().getParameters(op));
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

        String suffix = generateOperationSuffix(op);
        if (suffix.equals(""))
            sb.append(")");
        else
            sb.append(") ").append(suffix);

        return sb.toString();
    }

    /** 2002-12-06 Achim Spangler
     * check if a parameter is tagged as pointer or reference (not
     * part of UML - as far as author knows - but important for C++
     * developers)
     * @param elem element to check
     * @return one of NORMAL_MOD, REFERENCE_MOD, POINTER_MOD, or -1 if
     *         no specific tag is found
     */
    private int getAttributeModifierType(Object elem) {
        // first check whether the parameter shall be a pointer of reference
        Iterator iter = Model.getFacade().getTaggedValues(elem);
        while (iter.hasNext()) {
            Object tv = iter.next();
            String tag = Model.getFacade().getTagOfTag(tv);
            String val = Model.getFacade().getValueOfTag(tv);
            if (tag.indexOf("ref") != -1 || tag.equals("&")) {
                return val.equals("false") ? NORMAL_MOD
                                           : REFERENCE_MOD;
            } else if (tag.indexOf("pointer") != -1 || tag.equals("*")) {
                return val.equals("false") ? NORMAL_MOD
                                           : POINTER_MOD;
            }
        }
        return -1; /* no tag found */
    }


    private String generateAttributeParameterModifier(Object attr,
                                                      String def) {
        int modType = getAttributeModifierType(attr);

	// if attr has an abstract type it must be pointer or reference
	if (modType == NORMAL_MOD || modType == -1) {
	    Object type = Model.getFacade().getType(attr);
	    if (type == null) {
		// model corrupt (this really happened -- aslo)
		LOG.error(attr + " has no type!");
		return "";
	    }
	    if (Model.getFacade().isAbstract(type)
		|| Model.getFacade().isAInterface(type)) {
		if (modType == NORMAL_MOD) {
		    // user explicitly requested no modifier, print a warning
 		    LOG.warn("Requested no reference or pointer modifier, but");
 		    LOG.warn("\t" + type 
 		            + " cannot be instantiated, using reference");
		}
		modType = REFERENCE_MOD;
	    }
	}

        if (modType == NORMAL_MOD) {
            return "";
        } else if (modType == REFERENCE_MOD) {
            return "&";
        } else if (modType == POINTER_MOD) {
            return "*";
        } else if (def.length() == 0) {
            if (Model.getFacade().isAParameter(attr)
                    && (Model.getFacade().getKind(attr).equals(
                        Model.getDirectionKind().getOutParameter())
                    || Model.getFacade().getKind(attr).equals(
                            Model.getDirectionKind().getInOutParameter()))) {
                // out or inout parameters are defaulted to reference if
                // not specified else
                return "&";
            }
        }
        return def;
    }

    private String generateAttributeParameterModifier(Object attr) {
        return generateAttributeParameterModifier(attr, "");
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAttribute(
     *         java.lang.Object, boolean)
     */
    public String generateAttribute(Object attr, boolean documented) {
        StringBuffer sb = new StringBuffer(80);

        // list tagged values for documentation
        if (documented || generatorPass != NONE_PASS) {
            String tv = generateTaggedValues (attr, DOC_COMMENT_TAGS);
            if (tv != null && tv.length() > 0) {
                sb.append (LINE_SEPARATOR).append (INDENT).append (tv);
            }
        }
        // cat.info("generate Visibility for Attribute");
        sb.append(generateVisibility(attr));
        sb.append(generateOwnerScope(attr));
        sb.append(generateStructuralFeatureChangeability(attr));
        sb.append(
                generateMultiplicity(
                        attr,
                        generateName(Model.getFacade().getName(attr)),
                        Model.getFacade().getMultiplicity(attr),
                        generateAttributeParameterModifier(attr)));
        sb.append(";");
        if (generatorPass != NONE_PASS)
            sb.append(LINE_SEPARATOR);

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
        sb.append(generateNameWithPkgSelection(
                Model.getFacade().getType(param)));
        sb.append(' ');
        sb.append(generateAttributeParameterModifier(param));
        sb.append(generateName(Model.getFacade().getName(param)));

        // insert default value, if we are generating the header or notation
        if (generatorPass != SOURCE_PASS) {
            Object defvalObj = Model.getFacade().getDefaultValue(param);
            if (defvalObj != null) {
                String defval =
                    Model.getFacade().getBody(defvalObj).toString().trim();
                if (defval.length() > 0) {
                    sb.append(" = ").append(defval);
                }
            }
        }

        return sb.toString();
    }


    /**
     * @see org.argouml.application.api.NotationProvider2#generatePackage(java.lang.Object)
     */
    public String generatePackage(Object p) {
        StringBuffer sb = new StringBuffer();
        String packName = 
                generateName(Model.getDataTypesHelper().getLanguage(p));
        sb.append("// package ").append(packName).append(" {")
            .append(LINE_SEPARATOR);
        Collection ownedElements = Model.getFacade().getOwnedElements(p);
        if (ownedElements != null) {
            Iterator ownedEnum = ownedElements.iterator();
            while (ownedEnum.hasNext()) {
                Object me = ownedEnum.next();
                sb.append(generate(me));
                sb.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
            }
        }
        else {
            sb.append("(no elements)");
        }
        sb.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
        return sb.toString();
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
        if (generatorPass == SOURCE_PASS) return sb;

        String sClassifierKeyword;
        if (Model.getFacade().isAClass(cls) 
                || Model.getFacade().isAInterface(cls)) {
            sClassifierKeyword = "class";
        } else {
            return null; // actors, use cases etc.
        }
        boolean hasBaseClass = false;

        // Add the comments for this classifier first.
        sb.append(LINE_SEPARATOR)
            .append (DocumentationManager.getComments(cls));

        // list tagged values for documentation
        String tv = generateTaggedValues (cls, DOC_COMMENT_TAGS);
        if (tv != null && tv.length() > 0) {
            sb.append (LINE_SEPARATOR).append (INDENT).append (tv);
        }

        // add classifier keyword and classifier name
        sb.append(sClassifierKeyword).append(" ");
        sb.append(generateName(Model.getFacade().getName(cls)));

        // add base class/interface
        String baseClass =
            generateGeneralization(Model.getFacade().getGeneralizations(cls));
        if (!baseClass.equals ("")) {
            sb.append (" : ")
                .append (baseClass);
            hasBaseClass = true;
        }

        // add implemented interfaces, if needed
        // nsuml: realizations!
        if (Model.getFacade().isAClass(cls)) {
            String interfaces = generateSpecification(cls);
            if (!interfaces.equals ("")) {
                if (!hasBaseClass) sb.append (" : ");
                else sb.append (", ");
                sb.append (interfaces);
            }
        }

        // add opening brace
        if (lfBeforeCurly)
            sb.append(LINE_SEPARATOR).append('{');
        else
            sb.append(" {");

        // list tagged values for documentation
        tv = generateTaggedValues (cls, ALL_BUT_DOC_TAGS);
        if (tv != null && tv.length() > 0) {
            sb.append(LINE_SEPARATOR).append (INDENT).append (tv);
        }

        return sb;
    }

    private StringBuffer generateClassifierEnd(Object cls) {
        StringBuffer sb = new StringBuffer();
        if (Model.getFacade().isAClass(cls) 
                || Model.getFacade().isAInterface(cls)) {
            if ((verboseDocs) && (generatorPass != SOURCE_PASS)) {
                String classifierkeyword = null;
                if (Model.getFacade().isAClass(cls)) {
                    classifierkeyword = "class";
                } else {
                    classifierkeyword = "class";
                }
                sb.append(LINE_SEPARATOR)
                    .append("//end of ")
                        .append(classifierkeyword)
                            .append(" ").append(Model.getFacade().getName(cls))
                                .append(LINE_SEPARATOR);
            }
            if (generatorPass != SOURCE_PASS)
                sb.append("};").append(LINE_SEPARATOR);
            sb.append(generateHeaderPackageEnd());
        }
        return sb;
    }

    /**
     * Generate three parts under public, protected and private visibility,
     * adding the visibility keywords on top of each part. 
     * @param parts the parts to output
     * @return the composed parts
     */
    private String generateAllParts(StringBuffer[] parts) {
        StringBuffer sb = new StringBuffer();
        // generate all parts in order: public, protected, private
        for (int i = 0; i < ALL_PARTS.length; i++) {
            if (parts[i].toString().trim().length() > 0) {
                if (generatorPass != SOURCE_PASS) {
                    sb.append(LINE_SEPARATOR);
                    sb.append(' ').append(PART_NAME[i]).append(':');
                    sb.append(LINE_SEPARATOR);
                }
                sb.append(parts[i]);
            }
        }
        return sb.toString();
    }
    
    private int getVisibilityPart(Object o) {
        if (Model.getFacade().isPublic(o)) {
            return PUBLIC_PART;
        } else if (Model.getFacade().isProtected(o)) {
            return PROTECTED_PART;
        } else if (Model.getFacade().isPrivate(o)) {
            return PRIVATE_PART;
        } else {
            LOG.warn(Model.getFacade().getName(o)
                    + " is not public, nor protected, "
                    + "nor private!!! (ignored)");
            return -1;
        }
    }
    
    /**
     * @see org.argouml.application.api.NotationProvider2#generateClassifier(java.lang.Object)
     *
     * Generates code for a classifier. In case of Java code is
     * generated for classes and interfaces only at the moment.
     */
    public String generateClassifier(Object cls) {
        if (generatorPass == NONE_PASS
                && (Model.getFacade().isAClass(cls)
                    || Model.getFacade().isAInterface(cls))) {
            // we're probably in the notation pane, so do a special trick
            // to show both header and source
            StringBuffer sb = new StringBuffer();
            String name = Model.getFacade().getName(cls);
            sb.append("// ").append(name).append(".h");
            sb.append(LINE_SEPARATOR);
            generatorPass = HEADER_PASS;
            sb.append(generateClassifier(cls));
            if (Model.getFacade().isAClass(cls)) {
                sb.append(LINE_SEPARATOR);
                sb.append("// ").append(name).append(".cpp");
                sb.append(LINE_SEPARATOR);
                generatorPass = SOURCE_PASS;
                sb.append(generateClassifier(cls));
            }
            generatorPass = NONE_PASS;
            return sb.toString();
        }
        
        StringBuffer returnValue = new StringBuffer();
        StringBuffer start = generateClassifierStart(cls);
        if (((start != null) && (start.length() > 0))
            || (generatorPass == SOURCE_PASS)) {
            StringBuffer typedefs = generateGlobalTypedefs(cls);
            StringBuffer body = generateClassifierBody(cls);
            StringBuffer end = generateClassifierEnd(cls);
            returnValue.append((typedefs != null) ? typedefs.toString() : "");
            returnValue.append(start);
            if ((body != null) && (body.length() > 0)) {
                returnValue.append(LINE_SEPARATOR);
                returnValue.append(body);
                if (lfBeforeCurly) {
                    returnValue.append(LINE_SEPARATOR);
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
        if (Model.getFacade().isAClass(cls) 
                || Model.getFacade().isAInstance(cls)) {
            // add typedefs
            if (generatorPass == HEADER_PASS) {
                sb.append("// global type definitions for header defined "
                      + "by Tag entries in ArgoUML").append(LINE_SEPARATOR);
                sb.append("// Result: typedef <typedef_global_header> "
                      + "<tag_value>;").append(LINE_SEPARATOR);
                Collection globalTypedefStatements =
                    findTagValues(cls, "typedef_global_header");
                if (!globalTypedefStatements.isEmpty()) {
                    Iterator typedefEnum =
                        globalTypedefStatements.iterator();
                    while (typedefEnum.hasNext()) {
                        sb.append("typedef ").append(typedefEnum.next());
                        sb.append(";").append(LINE_SEPARATOR);
                    }
                }
            }
            else {
                sb.append("// global type definitions for class implementation "
                      + "in source file defined "
                      + "by Tag entries in ArgoUML").append(LINE_SEPARATOR);
                sb.append("// Result: typedef <typedef_global_source> "
                      + "<tag_value>;").append(LINE_SEPARATOR);
                Collection globalTypedefStatements =
                    findTagValues(cls, "typedef_global_source");
                if (!globalTypedefStatements.isEmpty()) {
                    Iterator typedefEnum = globalTypedefStatements.iterator();
                    while (typedefEnum.hasNext()) {
                        sb.append("typedef ").append(typedefEnum.next());
                        sb.append(";").append(LINE_SEPARATOR);
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
        Collection strs = Model.getFacade().getAttributes(cls);
        if (strs.isEmpty() || (generatorPass != HEADER_PASS)) {
            return;
        }
        String tv = null; // helper for tagged values
        sb.append(LINE_SEPARATOR);
        if (verboseDocs && Model.getFacade().isAClass(cls)) {
            sb.append(INDENT).append("// Attributes").append(LINE_SEPARATOR);
        }

        // generate attributes in order public, protected, private
        for (int i = 0; i < ALL_PARTS.length; i++) {
            int publicProtectedPrivate = ALL_PARTS[i];

            Iterator strEnum = strs.iterator();
            boolean isVisibilityLinePrinted = false;
            while (strEnum.hasNext()) {
                Object sf = strEnum.next();
                if (((publicProtectedPrivate == PUBLIC_PART)
                         && Model.getFacade().isPublic(sf))
                        || ((publicProtectedPrivate == PROTECTED_PART)
                        && Model.getFacade().isProtected(sf))
                        || ((publicProtectedPrivate == PRIVATE_PART)
                        && Model.getFacade().isPrivate(sf))) {
                    if (!isVisibilityLinePrinted) {
                        isVisibilityLinePrinted = true;
                        sb.append(LINE_SEPARATOR);
                        if (publicProtectedPrivate == PUBLIC_PART) {
                            sb.append(" public:");
                        } else if (publicProtectedPrivate == PROTECTED_PART) {
                            sb.append(" protected:");
                        } else if (publicProtectedPrivate == PRIVATE_PART) {
                            sb.append(" private:");
                        }
                        sb.append(LINE_SEPARATOR);
                    }
                    sb.append(INDENT).append(generate(sf));

                    tv = generateTaggedValues(sf, ALL_BUT_DOC_TAGS);
                    if (tv != null && tv.length() > 0) {
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

        if (generatorPass == SOURCE_PASS)
            return;

        Collection ends = Model.getFacade().getAssociationEnds(cls);
        if (!ends.isEmpty()) {

            sb.append(LINE_SEPARATOR);
            if (verboseDocs && Model.getFacade().isAClass(cls)) {
                sb.append(INDENT).append("// Associations").append(
                        LINE_SEPARATOR);
            }

            StringBuffer part[] = new StringBuffer[3];
            for (int i = 0; i < ALL_PARTS.length; i++)
                part[i] = new StringBuffer(80);
            
            Iterator endEnum = ends.iterator();
            while (endEnum.hasNext()) {
                Object ae = endEnum.next();
                Object a = Model.getFacade().getAssociation(ae);
                generateAssociationFrom(a, ae, part);
            }
            sb.append(generateAllParts(part));
        }
        // if this is an association class, generate attributes for
        // all the AssociationEnds
        if (Model.getFacade().isAAssociationClass(cls)) {
            if (verboseDocs) {
                sb.append(LINE_SEPARATOR).append(INDENT);
                sb.append("// AssociationClass associated classes");
            }
            // make all ends public... does it make sense?
            // should we declare friend all the associated and make
            // these protected? (private is too restrictive anyway, IMHO)
            // TODO: make it configurable, with a tag 
            sb.append(LINE_SEPARATOR).append(" public:").append(LINE_SEPARATOR);
            ends = Model.getFacade().getConnections(cls);
            Iterator iter = ends.iterator();
            while (iter.hasNext()) {
                Object ae = iter.next();
                sb.append(LINE_SEPARATOR);
                String comment = generateConstraintEnrichedDocComment(cls, ae);
                if (comment.length() > 0)
                    sb.append(comment).append(INDENT);

                String n = Model.getFacade().getName(ae);
                String name;
                Object type = Model.getFacade().getType(ae);

                if (n != null && n.length() > 0) {
                    name = generateName(n);
                } else {
                    name = "my" + generateClassifierRef(type);
                }

                sb.append(generateNameWithPkgSelection(type));
                sb.append(generateAttributeParameterModifier(ae));
                sb.append(" ").append(name);
                sb.append(";").append(LINE_SEPARATOR);

                String tv = generateTaggedValues(ae, ALL_BUT_DOC_TAGS);
                if (tv != null && tv.length() > 0) {
                    sb.append(INDENT).append(tv);
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
    private boolean checkGenerateOperationBody(Object cls) {
        boolean result = !((generatorPass == HEADER_PASS)
            || (Model.getFacade().isAbstract(cls))
            || Model.getFacade().isAInterface(Model.getFacade().getOwner(cls)));

        // if this operation has Tag "inline" the method shall be
        // generated in header
        if (Model.getFacade().getTaggedValue(cls, "inline") != null) {
            result = generatorPass == HEADER_PASS;
        }
        return result;
    }

    /** 2002-12-13 Achim Spangler
     * generate a single set function for a given attribute and StringBuffer
     */
    private void generateSingleAttributeSet(Object attr, StringBuffer sb) {
        if (Model.getFacade().getType(attr) == null) {
            return;
        }
        // generate for attributes with class-type:
        // "INDENT void set_<name>( const <type> &value ) { <name> = value; };"
        // generate for other (small) data types:
        // "INDENT void set_<name>( <type> value ) { <name> = value; };"
        // generate: "INDENT void set_<name>( "
        sb.append(LINE_SEPARATOR).append(INDENT);
        sb.append("/** simple access function to set the attribute ");
        sb.append(Model.getFacade().getName(attr));
        sb.append(" by function").append(LINE_SEPARATOR).append(INDENT);
        sb.append("  * @param value value to set for the attribute ");
        sb.append(Model.getFacade().getName(attr)).append(LINE_SEPARATOR);
        sb.append(INDENT).append("  */").append(LINE_SEPARATOR);
        sb.append(INDENT);
        sb.append("void set_").append(Model.getFacade().getName(attr))
            .append("( ");
        String modifier = generateAttributeParameterModifier(attr);
        if (modifier != null && modifier.length() > 0) {
            // generate: "const <type> <modifier>value"
            if (modifier.equals("&")) sb.append("const ");
            sb.append(generateClassifierRef(Model.getFacade().getType(attr)))
                .append(' ').append(modifier).append("value");
        } else if (Model.getFacade().isAClass(
                Model.getFacade().getType(attr))) {
            // generate: "const <type> &value"
            sb.append("const ");
            sb.append(generateClassifierRef(Model.getFacade().getType(attr)));
            sb.append(" &value");
        } else {
            // generate: "<type> value"
            sb.append(generateClassifierRef(Model.getFacade().getType(attr)))
                .append(" value");
        }
        // generate: " ) { <name> = value; };"
        sb.append(" ) { ").append(Model.getFacade().getName(attr));
        sb.append(" = value; };").append(LINE_SEPARATOR);
    }

    /** 2002-12-13 Achim Spangler
     * generate a single get function for a given attribute and StringBuffer
     */
    private void generateSingleAttributeGet(Object attr, StringBuffer sb) {
        if (Model.getFacade().getType(attr) == null) return;
        // generate for attributes with class-type:
        // "const <type>& get_<name>( void ) { return <name>; };"
        // generate for other (small) data types
        // "<type> get_<name>( void ) { return <name>; };"
        // generate: "INDENT"
        sb.append(LINE_SEPARATOR).append(INDENT);
        sb.append("/** simple access function to get the attribute ");
        sb.append(Model.getFacade().getName(attr));
        sb.append(" by function */").append(LINE_SEPARATOR).append(INDENT);
        String modifier = generateAttributeParameterModifier(attr);
        if (modifier != null && modifier.length() > 0) {
            // generate: "const <type><modifier>"
            sb.append("const ");
            sb.append(generateClassifierRef(Model.getFacade().getType(attr)));
            sb.append(modifier);
        } else if (Model.getFacade().isAClass(
                Model.getFacade().getType(attr))) {
            // generate: "const <type>&"
            sb.append("const ");
            sb.append(generateClassifierRef(Model.getFacade().getType(attr)));
            sb.append("&");
        } else {
            // generate: "<type>"
            sb.append(generateClassifierRef(Model.getFacade().getType(attr)));
        }
        // generate: " get_<name>( void ) const { return <name>; };"
        sb.append(" get_").append(Model.getFacade().getName(attr));
        sb.append("( void ) const { return ")
            .append(Model.getFacade().getName(attr));
        sb.append("; };").append(LINE_SEPARATOR);
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
        Collection strs = Model.getFacade().getAttributes(cls);
        if (strs.isEmpty() || (generatorPass != HEADER_PASS)) {
            return;
        }
        String accessTag = null;

        Iterator strEnum = strs.iterator();
        while (strEnum.hasNext()) {
            Object attr = strEnum.next();
            accessTag = Model.getFacade().getTaggedValueValue(attr, "set");
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

            accessTag = Model.getFacade().getTaggedValueValue(attr, "get");
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
        Collection behs = Model.getCoreHelper().getOperations(cls);
        if (behs.isEmpty()) return;
        sb.append(LINE_SEPARATOR);
        if (verboseDocs) {
            sb.append(INDENT).append("// Operations").append(LINE_SEPARATOR);
        }

        // generate tag controlled access functions for attributes
        StringBuffer[] funcs = new StringBuffer[3]; 
        funcs[0] = new StringBuffer(80);
        funcs[1] = new StringBuffer(80);
        funcs[2] = new StringBuffer(80);
        generateClassifierBodyTaggedAccess4Attributes(cls, funcs[PRIVATE_PART],
                                                      funcs[PROTECTED_PART],
                                                      funcs[PUBLIC_PART]);

        Iterator behEnum = behs.iterator();
        while (behEnum.hasNext()) {
            Object bf = behEnum.next();
            StringBuffer tb = null;

            int p = getVisibilityPart(bf);
            if (p < 0) continue;
            tb = funcs[p];
            tb.append(LINE_SEPARATOR).append(INDENT);

            boolean mustGenBody = checkGenerateOperationBody(bf);
            if (tb != null 
                && ((generatorPass == HEADER_PASS) || mustGenBody))
            {
                tb.append(generate(bf));
                
                // helper for tagged values
                String tv = generateTaggedValues(bf, ALL_BUT_DOC_TAGS);
                
                if (mustGenBody && (Model.getFacade().isAClass(cls))
                        && (Model.getFacade().isAOperation(bf))
                        && (!Model.getFacade().isAbstract(bf))) {
                    // there is no ReturnType in behavioral feature (nsuml)
                    tb.append(LINE_SEPARATOR).append(generateMethodBody(bf));
                } else {
                    tb.append(";").append(LINE_SEPARATOR);
                    if (tv.length() > 0) {
                        tb.append(INDENT).append(tv).append(LINE_SEPARATOR);
                    }
                }
            }
        } // end loop through all operations

        sb.append(generateAllParts(funcs));
    }

    /**
     * Generates the association ends of the body of a class or interface.
     * @param cls
     * @param sb Where to put the result.
     */
    private void generateClassifierBodyTypedefs(Object cls, StringBuffer sb) {
        if (generatorPass == HEADER_PASS) {
            Collection publicTypedefStatements =
                findTagValues(cls, "typedef_public");
            Collection protectedTypedefStatements =
                findTagValues(cls, "typedef_protected");
            Collection privateTypedefStatements =
                findTagValues(cls, "typedef_private");
            if (!publicTypedefStatements.isEmpty()) {
                sb.append(LINE_SEPARATOR).append(" public:")
                    .append(LINE_SEPARATOR).append(INDENT);
                sb.append("// public type definitions for header defined "
                      + "by Tag entries in ArgoUML").append(LINE_SEPARATOR);
                sb.append(INDENT);
                sb.append("// Result: typedef <typedef_public> "
                      + "<tag_value>;").append(LINE_SEPARATOR);
                Iterator typedefEnum = publicTypedefStatements.iterator();

                while (typedefEnum.hasNext()) {
                    sb.append(INDENT).append("typedef ");
                    sb.append(typedefEnum.next())
                        .append(";").append(LINE_SEPARATOR);
                }
            }
            if (!protectedTypedefStatements.isEmpty()) {
                sb.append(LINE_SEPARATOR).append(" protected:")
                    .append(LINE_SEPARATOR).append(INDENT);
                sb.append("// protected type definitions for header defined "
                      + "by Tag entries in ArgoUML").append(LINE_SEPARATOR);
                sb.append(INDENT);
                sb.append("// Result: typedef <typedef_protected> "
                      + "<tag_value>;").append(LINE_SEPARATOR);
                Iterator typedefEnum = protectedTypedefStatements.iterator();

                while (typedefEnum.hasNext()) {
                    sb.append(INDENT).append("typedef ");
                    sb.append(typedefEnum.next())
                        .append(";").append(LINE_SEPARATOR);
                }
            }
            if (!privateTypedefStatements.isEmpty()) {
                sb.append(LINE_SEPARATOR).append(" private:")
                    .append(LINE_SEPARATOR).append(INDENT);
                sb.append("// private type definitions for header defined "
                      + "by Tag entries in ArgoUML").append(LINE_SEPARATOR);
                sb.append(INDENT);
                sb.append("// Result: typedef <typedef_private> "
                      + "<tag_value>;").append(LINE_SEPARATOR);
                Iterator typedefEnum = privateTypedefStatements.iterator();

                while (typedefEnum.hasNext()) {
                    sb.append(INDENT).append("typedef ");
                    sb.append(typedefEnum.next()).append(";")
                        .append(LINE_SEPARATOR);
                }
            }
        }
    }

    /**
     * Generates a virtual destructor when the classifier is an interface.
     * @param cls the classifier object
     * @param sb the buffer to where the generate code goes
     */
    private void generateClassifierDestructor(Object cls, StringBuffer sb) {
        if (Model.getFacade().isAInterface(cls) 
                && generatorPass == HEADER_PASS) {
            sb.append(LINE_SEPARATOR).append("public:").append(LINE_SEPARATOR);
            sb.append(INDENT).append("// virtual destructor for interface ")
                .append(LINE_SEPARATOR);
            sb.append(INDENT).append("virtual ").append('~').append(
                Model.getFacade().getName(cls)).append("() { }")
                    .append(LINE_SEPARATOR);
        }
    }

    /**
     * Generates the body of a class or interface.
     * @param cls
     * @return a StringBuffer with the result.
     */
    private StringBuffer generateClassifierBody(Object cls) {
        StringBuffer sb = new StringBuffer();
        if (Model.getFacade().isAClass(cls) 
                || Model.getFacade().isAInterface(cls))
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
            Collection methods = Model.getFacade().getMethods(op);
            Iterator i = methods.iterator();
            Object method = null;
            boolean methodFound = false;
            String tv = generateTaggedValues(op, ALL_BUT_DOC_TAGS);
            String operationIndent =
                (generatorPass == HEADER_PASS) ? INDENT : "";

            // append tags which are not Doc-Comments
            if (tv.length() > 0) {
                sb.append(operationIndent).append(tv).append(LINE_SEPARATOR);
            }

            // place the curley braces within the protected area, to
            // allow placement of preserved contructor initialisers in
            // this area otherwise all possible constructor-attribute
            // initialisers would have to be autogenerated with an
            // army of special tags
            sb.append(generateSectionTop(op, operationIndent))
                .append(operationIndent).append("{").append(LINE_SEPARATOR);

            while (i != null && i.hasNext()) {
                method = i.next();

                if (method != null) {
                    if ((Model.getFacade().getBody(method) != null)
                            && (!methodFound)) {
                        Object body = Model.getFacade().getBody(method);
                        sb.append(Model.getFacade().getBody(body));
                        methodFound = true;
                        break;
                    }
                }
            }

            if (!methodFound) {
                // pick out return type as default method body
                Object rp =
                    Model.getCoreHelper().getReturnParameter(op);
                if (rp != null) {
                    Object returnType = Model.getFacade().getType(rp);
                    sb.append(generateDefaultReturnStatement(returnType));
                }
            }
            sb.append(operationIndent).append("}").append(LINE_SEPARATOR)
                .append(generateSectionBottom(op, operationIndent));
            return sb.toString();
        }
        return generateDefaultReturnStatement (null);
    }


    private String generateSectionTop(Object op, String localIndent) {
        String id = UUIDHelper.getInstance().getUUID(op);
        if (id == null) {
            id = (new UID().toString());
            Model.getCoreHelper().setUUID(op, id);
        }
        return Section.generateTop(id, localIndent);
    }

    private String generateSectionBottom(Object op, String localIndent) {
        String id = UUIDHelper.getInstance().getUUID(op);
        if (id == null) {
            id = (new UID().toString());
            Model.getCoreHelper().setUUID(op, id);
        }
        return Section.generateBottom(id, localIndent);
    }

    private String generateDefaultReturnStatement(Object cls) {
        if (cls == null) return "";

        String clsName = Model.getFacade().getName(cls);
        String res = null;
        if (clsName.equals("void")) res = "";
        else if (clsName.equals("char")) res = "return 'x';";
        else if (clsName.equals("int")) res = "return 0;";
        else if (clsName.equals("boolean")) res = "return false;";
        else if (clsName.equals("byte")) res = "return 0;";
        else if (clsName.equals("long")) res = "return 0;";
        else if (clsName.equals("float")) res = "return 0.0;";
        else if (clsName.equals("double")) res = "return 0.0;";

        if (res == null) {
            return INDENT + "return 0;" + LINE_SEPARATOR;
        }
        return INDENT + res + LINE_SEPARATOR;
    }

    private String generateTaggedValues(Object e, int tagSelection) {
        Iterator iter = Model.getFacade().getTaggedValues(e);
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
                        else {
                            buf.append("/** ");
                        }
                    }
                    else {
                        buf.append("/* {");
                    }
                    first = false;
                } // end first
                else {
                    if (tagSelection == DOC_COMMENT_TAGS) {
                        buf.append(LINE_SEPARATOR)
                            .append(INDENT).append(" *  ");
                    }
                    else {
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
                buf.append(LINE_SEPARATOR).append(INDENT).append(" */")
                    .append(LINE_SEPARATOR);
            } else {
                buf.append ("}*/").append(LINE_SEPARATOR);
            }
        }
        else if (tagSelection == DOC_COMMENT_TAGS) {
            // create at least main documentation field, if no other tag found
            String doc = (DocumentationManager.hasDocs(e))
                ? DocumentationManager.getDocs(e, INDENT)
                : null;
            if (doc != null && doc.trim().length() > 0) {
                buf.append(doc).append(LINE_SEPARATOR);
            }
        }

        return buf.toString();
    }

    private String generateTaggedValue(Object tv, int tagSelection) {
        if (tv == null) return "";
        String s = generateUninterpreted(Model.getFacade().getValueOfTag(tv));

        String tagName = Model.getFacade().getTagOfTag(tv);
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

        Iterator iter = Model.getFacade().getTaggedValues(item);
        String s = null;
        while (iter.hasNext()) {
            Object tag = iter.next();
            if (Model.getFacade().getTagOfTag(tag).equals(searchedName)) {
                s = Model.getFacade().getValueOfTag(tag);
                if (s != null && s.length() != 0) result.add(s);
            }
        }
        return result;
    }

    private boolean isDocCommentTag(String tagName) {
        boolean result = false;
        if (tagName.equals ("inv")) {
            result = true;
        }
        else if (tagName.equals ("post")) {
            result = true;
        }
        else if (tagName.equals ("pre")) {
            result = true;
        }
        else if (tagName.equals ("author")) {
            result = true;
        }
        else if (tagName.equals ("version")) {
            result = true;
        }
        else if (tagName.equals ("see")) {
            result = true;
        }
        else if (tagName.equals ("param")) {
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
        String s = generateUninterpreted(Model.getFacade().getValueOfTag(tv));
        if (s == null || s.length() == 0 || s.equals("/** */")) return "";
        String t = Model.getFacade().getTagOfTag(tv);
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
    public String generateConstraintEnrichedDocComment(Object me, Object ae) {
        // list tagged values for documentation
        String s = generateTaggedValues (me, DOC_COMMENT_TAGS);

        Object multiplicity = Model.getFacade().getMultiplicity(ae);
        if (!(Model.getMultiplicities().get11().equals(multiplicity)
                || Model.getMultiplicities().get01().equals (multiplicity))) {
            // Multiplicity greater 1, that means we will generate some sort of
            // collection, so we need to specify the element type tag
            StringBuffer sDocComment = new StringBuffer(80);

            // Prepare doccomment
            if (!(s == null || "".equals(s))) {
                // Just remove closing "*/"
                sDocComment.append(INDENT)
                    .append(s.substring(0, s.indexOf("*/") + 1));
            }
            else {
                sDocComment.append(INDENT).append("/**").append(LINE_SEPARATOR);
                sDocComment.append(INDENT).append(" *");
            }

            // Build doccomment
            Object type = Model.getFacade().getType(ae);
            if (type != null) {
                sDocComment.append(" @element-type ");
                sDocComment.append(Model.getFacade().getName(type));
            }
            sDocComment.append(LINE_SEPARATOR).append(INDENT)
                .append(" */").append(LINE_SEPARATOR);
            return sDocComment.toString();
        }
        return (s != null) ? s : "";
    }

    /**
     * 
     * @param a association object
     * @param ae association end attached to the classifier
     *        for which the code is to be generated
     * @param parts the buffers associated with the public, protected
     *        and private parts, where the code is to be written.
     */
    private void generateAssociationFrom(Object a, Object ae,
            StringBuffer[] parts) {
        // TODO: does not handle n-ary associations

        /*
         * Moved into while loop 2001-09-26 STEFFEN ZSCHALER
         *
         * Was:
         *
         s += DocumentationManager.getDocs(a) + "\n" + INDENT;
        */

        Collection connections = Model.getFacade().getConnections(a);
        Iterator connEnum = connections.iterator();
        while (connEnum.hasNext()) {
            Object ae2 = connEnum.next();
            if (ae2 != ae) {
                int p = getVisibilityPart(ae2);
                if (p >= 0) {
                    StringBuffer sb = parts[p];
                    /**
                     * Added generation of doccomment 2001-09-26 STEFFEN
                     * ZSCHALER
                     */
                    sb.append(LINE_SEPARATOR);
                    String assend = generateAssociationEnd(ae2);
                    if (assend.length() > 0) {
                        String comment =
                            generateConstraintEnrichedDocComment(a, ae2);
                        if (comment.length() > 0)
                            sb.append(comment);
                        // both comment and assend ends with simple newline
                        sb.append(INDENT).append(assend);
                    }

                    String tv = generateTaggedValues(a, ALL_BUT_DOC_TAGS);
                    if (tv != null && tv.length() > 0) {
                        sb.append(INDENT).append(tv);
                    }
                }
            }
        }
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAssociation(java.lang.Object)
     */
    public String generateAssociation(Object handle) {
        return ""; // Maybe Model.getFacade().getName(handle) ???
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAssociationEnd(java.lang.Object)
     */
    public String generateAssociationEnd(Object ae) {
        if (!Model.getFacade().isNavigable(ae)) {
            return "";
        }
        if (Model.getFacade().isAbstract(
                Model.getFacade().getAssociation(ae))) {
            return "";
        }
        StringBuffer sb = new StringBuffer(80);

        sb.append(generateAssociationEndScope(ae));

        String n = Model.getFacade().getName(ae);
        Object asc = Model.getFacade().getAssociation(ae);
        String ascName = Model.getFacade().getName(asc);
        String name = null;

        if (n != null && n.length() > 0) {
            name = generateName(n);
        } else if (ascName != null && ascName.length() > 0) {
            name = generateName(ascName);
        } else {
            name = "my" + generateClassifierRef(Model.getFacade().getType(ae));
        }

        String modifier;
        if (Model.getFacade().isAAssociationClass(asc)) {
            // With an association class, we actually make an association
            // between us and the association class itself.
            // Usually, this is a pointer or a reference, so default
            // to a pointer.
            modifier = generateAttributeParameterModifier(asc, "*");
        } else {
            modifier = generateAttributeParameterModifier(ae);
        }
        
        sb.append(generateMultiplicity(ae, name,
                             Model.getFacade().getMultiplicity(ae),
                             modifier));

        return (sb.append(";").append(LINE_SEPARATOR)).toString();
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
            Object ge = Model.getFacade().getParent(generalization);
            if (ge != null) {
                String visTag =
                    Model.getFacade().getTaggedValueValue(generalization,
                                            "visibility").trim();
                if (visTag != null && !visTag.equals("")) {
                    sb.append(visTag).append(" ");
                } else {
                    sb.append("public ");
                }
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
            Object ge = Model.getFacade().getParent(generalization);
            if (ge != null) {
                classes.add(ge);
            }
        }
        return classes;
    }

    //  public String generateSpecification(Collection realizations) {
    private String generateSpecification(Object cls) {
        Collection deps = Model.getFacade().getClientDependencies(cls);
        Iterator depIterator = deps.iterator();
        StringBuffer sb = new StringBuffer(80);

        while (depIterator.hasNext()) {
            Object dependency = depIterator.next();
            if (Model.getFacade().isAAbstraction(dependency)
                    && Model.getFacade().isRealize(dependency)) {
                Object iFace = Model.getFacade().getSuppliers(dependency)
                    .iterator().next();
                String visTag =
                    Model.getFacade().getTaggedValueValue(dependency,
                                            "visibility").trim();
                if (sb.length() > 0) sb.append(", ");
                if (visTag != null && !visTag.equals("")) {
                    sb.append(visTag).append(" ");
                } else {
                    sb.append("virtual public ");
                }
                sb.append(generateNameWithPkgSelection(iFace));
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
        if (!Model.getFacade().isAEvent(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Event required");
        }

        return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateVisibility(java.lang.Object)
     */
    public String generateVisibility(Object o) {
        if (Model.getFacade().isAAttribute(o)) {
            return "";
        }
        // cut'n'pasted from GeneratorJava.java
        if (Model.getFacade().isAFeature(o)) {
            Object tv = Model.getFacade().getTaggedValue(o, "src_visibility");
            if (tv != null) {
                String tagged = (String) Model.getFacade().getValue(tv);
                if (tagged != null) {
                    if (tagged.trim().equals("")
                            || tagged.trim().toLowerCase().equals("default")) {
                        return "";
                    } 
                    return tagged + ": ";
                }
            }
        }
        if (Model.getFacade().isAModelElement(o)) {
            if (Model.getFacade().isPublic(o))
                return "public: ";
            if (Model.getFacade().isPrivate(o))
                return "private: ";
            if (Model.getFacade().isProtected(o))
                return "protected: ";
        }
        if (Model.getFacade().isAVisibilityKind(o)) {
            if (Model.getVisibilityKind().getPublic().equals(o))
                return "public: ";
            if (Model.getVisibilityKind().getPrivate().equals(o))
                return "private: ";
            if (Model.getVisibilityKind().getProtected().equals(o))
                return "protected: ";
        }
        return "";
    }

    private String generateAssociationEndScope(Object ae) {
        return generateScope(Model.getFacade().getTargetScope(ae));
    }

    private String generateOwnerScope(Object f) {
        return generateScope(Model.getFacade().getOwnerScope(f));
    }

    /**
     * @param scope The scope to compare.
     * @return The generated text representing the scope.
     */
    private String generateScope(Object scope) {
        if (Model.getScopeKind().getClassifier().equals(scope)) {
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
        // use Model subsystem Facade to check if the operation is 
        // owned by an interface
        Object opOwner = Model.getFacade().getOwner(op);
        if (Model.getFacade().isAbstract(op) 
                || Model.getFacade().isAInterface(opOwner)) {
            return " = 0";
        }
        return "";
    }

    /**
     * Generate "const" keyword for query operations.
     */
    private String generateOperationChangeability(Object op) {
        if (Model.getFacade().isQuery(op)) {
            return "const ";
        }
        return "";
    }

    /**
     * Generate "const" keyword for const pointer/reference parameters.
     */
    private String generateParameterChangeability(Object par) {
        int parType = getAttributeModifierType(par);
        if (parType != -1 && parType != NORMAL_MOD
                && (Model.getFacade().getKind(par)).equals(
                        Model.getDirectionKind().getInParameter())) {
            return "const ";
        }
        return "";
    }

    private String generateStructuralFeatureChangeability(Object sf) {
        Object changeableKind = Model.getFacade().getChangeability(sf);
        if (Model.getChangeableKind().getFrozen().equals(changeableKind)) {
            return "const ";
        }
        return "";
    }

    /**
     * Generates "synchronized" keyword for guarded operations.
     * FIXME: not needed for c++, it doesn't handle changeability
     * directly. Maybe, implement this in the methods body using 
     * some kind of API (posix, win32, ... as user wishes ...)?
     * @param op The operation
     * @return The synchronized keyword if the operation is guarded, else ""
     */
    private String generateConcurrency(Object op) {
        return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateMultiplicity(java.lang.Object)
     */
    public String generateMultiplicity(Object multiplicity) {
        if (multiplicity == null) {
            return "";
        }
        if (Model.getMultiplicities().get0N().equals(multiplicity)) {
            return ANY_RANGE;
        }
        String s = "";

        Iterator rangeEnum = Model.getFacade().getRanges(multiplicity);
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
        Object typeCls = null;
        if (Model.getFacade().isAAssociationEnd(item)) {
            // take into account association classes
            Object assoc = Model.getFacade().getAssociation(item);
            if (Model.getFacade().isAAssociationClass(assoc)) {
                typeCls = assoc;
                name += "Assoc";
            }
            else
                typeCls = Model.getFacade().getType(item);
        } else if (Model.getFacade().isAAttribute(item)) {
            typeCls = Model.getFacade().getType(item);
        } else if (Model.getFacade().isAClassifier(item)) {
            type = Model.getFacade().getName(item);
        } else {
            type = "";
        }
        if (typeCls != null) {
            type = generateNameWithPkgSelection(typeCls);
        }
        if (m == null) {
            return (type + " " + modifier + name);
        }
        StringBuffer sb = new StringBuffer(80);
        int countUpper = Model.getFacade().getUpper(m);
        int countLower = Model.getFacade().getLower(m);

        if (countUpper == 1 && countLower == 1) {
            // simple generate identifier for default 1:1 multiplicity
            sb.append(type).append(' ').append(modifier).append(name);
        } else if (countUpper == 1 && countLower == 0) {
            // use a simple pointer for 0:1 multiplicity
            // TODO: use an auto_ptr in case of attributes or compositions
            sb.append(type).append(' ').append(modifier)
                .append("* ").append(name);
        } else if (countUpper == countLower) {
            // fixed array -> <type> <name>[<count>];
            sb.append(type).append(' ').append(modifier).append(name)
                .append("[ " + countUpper + "]");
        } else {
            // variable association -> if no tag found use vector
            // else search for tag:
            // <MultipliciyType> : vector|list|slist|map|stack|stringmap
            String multType =
                Model.getFacade().getTaggedValueValue(item, "MultiplicityType");
            if (multType != null && multType.length() > 0) {
                if (multType.equals("vector")) {
                    containerType = "vector";
                } else if (multType.equals("list")) {
                    containerType = "list";
                } else if (multType.equals("slist")) {
                    containerType = "slist";
                } else if (multType.equals("map")) {
                    // FIXME: map does not work this way, needs a index type
                    containerType = "map";
                } else if (multType.equals("stack")) {
                    containerType = "stack";
                } else if (multType.equals("stringmap")) {
                    if (extraIncludes.indexOf("#include <map>") == -1) {
                        extraIncludes += "#include <map>" + LINE_SEPARATOR;
                    }
                    if (extraIncludes.indexOf("#include <string>") == -1) {
                        extraIncludes += "#include <string>" + LINE_SEPARATOR;
                    }
                    sb.append(stdPrefix + "map<" + stdPrefix + "string, ");
                    if (modifier.indexOf('&') != -1) {
                        LOG.warn("cannot generate STL container "
                                + "with references, using pointers");
                        modifier = "*";
                    }
                    sb.append(type).append(modifier);
                    sb.append(" > ").append(name);
                } else {
                    LOG.warn("unknown MultiplicityType \"" + multType
                            + "\", using default");
                    containerType = "vector";
                }
            } else {
                containerType = "vector";
            }

            if (containerType != null) {
                // these container are declared the same except the name
                String includeLine = "#include <" + containerType + ">";
                if (extraIncludes.indexOf(includeLine) == -1) {
                    extraIncludes += includeLine + LINE_SEPARATOR;
                }
                sb.append(stdPrefix).append(containerType).append("< ");
                if (modifier.indexOf('&') != -1) {
                    LOG.warn("cannot generate STL container "
                            + "with references, using pointers");
                    modifier = "*";
                }
                sb.append(type).append(modifier);
                sb.append(" > ").append(name);
            }
        }
        return sb.toString();
    }

    private String generateMultiplicityRange(Object mr) {
        Integer lower = new Integer(Model.getFacade().getLower(mr));
        Integer upper = new Integer(Model.getFacade().getUpper(mr));
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
        return Model.getFacade().getName(handle);
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateStateBody(java.lang.Object)
     */
    public String generateStateBody(Object state) {
        String s = "";
        Object entry = Model.getFacade().getEntry(state);
        Object exit = Model.getFacade().getExit(state);
        if (entry != null) {
            String entryStr = cppGenerate(entry);
            if (entryStr.length() > 0) s += "entry / " + entryStr;
        }
        if (exit != null) {
            String exitStr = cppGenerate(exit);
            if (s.length() > 0) s += LINE_SEPARATOR;
            if (exitStr.length() > 0) s += "exit / " + exitStr;
        }
        Collection trans = Model.getFacade().getInternalTransitions(state);
        if (trans != null) {
            Iterator iter = trans.iterator();
            while (iter.hasNext()) {
                if (s.length() > 0) s += LINE_SEPARATOR;
                s += generateTransition(iter.next());
            }
        }
        return s;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateTransition(java.lang.Object)
     */
    public String generateTransition(Object transition) {
        String s = generate(Model.getFacade().getName(transition));
        String t = generate(Model.getFacade().getTrigger(transition));
        String g = generate(Model.getFacade().getGuard(transition));
        String e = generate(Model.getFacade().getEffect(transition));
        if (s.length() > 0) s += ": ";
        s += t;
        if (g.length() > 0) s += " [" + g + "]";
        if (e.length() > 0) s += " / " + e;
        return s;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateAction(java.lang.Object)
     */
    public String generateAction(Object m) {
        Object script = Model.getFacade().getScript(m);
        if ((script != null) && (Model.getFacade().getBody(script) != null))
            return Model.getFacade().getBody(script).toString();
        return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateGuard(java.lang.Object)
     */
    public String generateGuard(Object guard) {
        if (Model.getFacade().getExpression(guard) != null)
            return generateExpression(Model.getFacade().getExpression(guard));
        return "";
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateMessage(java.lang.Object)
     */
    public String generateMessage(Object message) {
        if (message == null) {
            return "";
        }
        return generateName(Model.getFacade().getName(message)) + "::"
            + generateAction(Model.getFacade().getAction(message));
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
    public String getModuleVersion() { return "0.17.2"; }
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.language.cpp.generator"; }
    /**
     * Returns the _lfBeforeCurly.
     * @return boolean
     */
    public boolean isLfBeforeCurly() {
        return lfBeforeCurly;
    }

    /**
     * Returns the _verboseDocs.
     * @return boolean
     */
    public boolean isVerboseDocs() {
        return verboseDocs;
    }

    /**
     * Sets the _lfBeforeCurly.
     * @param beforeCurly The _lfBeforeCurly to set
     */
    public void setLfBeforeCurly(boolean beforeCurly) {
        this.lfBeforeCurly = beforeCurly;
    }

    /**
     * Sets the _verboseDocs.
     * @param verbose The _verboseDocs to set
     */
    public void setVerboseDocs(boolean verbose) {
        this.verboseDocs = verbose;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateActionState(java.lang.Object)
     */
    public String generateActionState(Object actionState) {
        String ret = "";
        Object action = Model.getFacade().getEntry(actionState);
        if (action != null) {
            Object expression = Model.getFacade().getScript(action);
            if (expression != null) {
                ret = generateExpression(expression);
            }
        }
        return ret;
    }
} /* end class GeneratorCpp */
