// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

package org.argouml.language.php;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import org.argouml.application.ArgoVersion;

import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;

import org.argouml.language.php.generator.NameGenerator;

import org.argouml.model.ModelFacade;

/**
 * This class generates DocBlocks in PHPDocumentor style
 *
 * @author Kai Schr&ouml;der
 * @since  ArgoUML 0.15.5
 */
public final class PHPDocumentor {
    /**
     * Class level DocBlock.
     */
    private static final int BLOCK_TYPE_CLASSIFIER = -4;

    /**
     * DocBlock for an attribute.
     */
    private static final int BLOCK_TYPE_ATTRIBUTE = -3;

    /**
     * DocBlock for a method.
     */
    private static final int BLOCK_TYPE_OPERATION = -2;

    /**
     * Package level DocBlock.
     */
    private static final int BLOCK_TYPE_PACKAGE = -1;

    /**
     * Unknown DocBlock type.
     */
    public static final int BLOCK_TYPE_UNKNOWN = 0;

    /**
     * File level DocBlock.
     */
    public static final int BLOCK_TYPE_FILE = 1;

    /**
     * DocBlock for include/require/include_once/require_once.
     */
    public static final int BLOCK_TYPE_INCLUDE = 2;

    // -------------------------------------------------------------------------

    /**
     * The log4j logger to log messages to
     */
    private static final Logger LOG = Logger.getLogger(PHPDocumentor.class);

    /**
     * full name of ArgoUML user
     */
    private static String sArgoUserFullname =
            Configuration.getString(Argo.KEY_USER_FULLNAME);

    /**
     * email address of ArgoUML user
     */
    private static String sArgoUserEmail =
            Configuration.getString(Argo.KEY_USER_EMAIL);

    // -------------------------------------------------------------------------

    /**
     * The DocBlock
     */
    private DocBlock objDocBlock = null;

    /**
     * The model element behind the DocBlock
     */
    private Object objModelElement = null;

    /**
     * The type of the DocBlock
     */
    private int iDocBlockType = BLOCK_TYPE_UNKNOWN;

    /**
     * The filename for the file level DocBlock
     */
    private String sFilename = null;

    // -------------------------------------------------------------------------

    /**
     * Zero-argument class constructor
     *
     * @throws Exception
     */
    public PHPDocumentor() throws Exception {
        super();

        create(null);
    }

    /**
     * Class constructor
     *
     * @param modelElement The model element to document.
     *
     * @throws Exception
     */
    public PHPDocumentor(Object modelElement) throws Exception {
        super();

        create(modelElement);
    }

    /**
     * Class constructor
     *
     * @param modelElement The model element to document.
     * @param iType        The type of DocBlock to generate.
     *
     * @throws Exception
     */
    public PHPDocumentor(Object modelElement, int iType) throws Exception {
        super();

        create(modelElement, iType);
    }

    /**
     * Creates a new DocBlock for the given model element
     *
     * @param modelElement The model element to document.
     *
     * @return <code>true</code> on success;
     *         <code>false</code> otherwise.
     *
     * @throws Exception
     */
    private final void create(Object modelElement) throws Exception {
        create(modelElement, BLOCK_TYPE_UNKNOWN);
    }

    /**
     * Creates a new DocBlock for the given model element of the given type
     *
     * @param modelElement The model element to document.
     * @param iBlockType   The type of DocBlock to generate.
     *
     * @throws Exception
     */
    private final void create(Object modelElement, int iBlockType)
        throws Exception {
        switch (iBlockType) {
        // TODO: move block 4 spaces right if checkstyle is fixed
        case BLOCK_TYPE_UNKNOWN:
            createUnknown(modelElement);
            break;
        case BLOCK_TYPE_FILE:
            createFile(modelElement);
            break;
        case BLOCK_TYPE_INCLUDE:
            createInclude(modelElement);
            break;
        case BLOCK_TYPE_CLASSIFIER:
            createClassifier(modelElement);
            break;
        case BLOCK_TYPE_ATTRIBUTE:
            createAttribute(modelElement);
            break;
        case BLOCK_TYPE_OPERATION:
            createOperation(modelElement);
            break;
        case BLOCK_TYPE_PACKAGE:
            createPackage(modelElement);
            break;
        default:
            throw new IllegalArgumentException(iBlockType
                    + " is not a valid DocBlock type.");
        }
    }

    /**
     * Creates a new DocBlock for the given model element of unknown type
     *
     * @param modelElement The model element to document.
     *
     * @throws Exception
     */
    private final void createUnknown(Object modelElement) throws Exception {
        if (modelElement != null) {
            if (ModelFacade.isAClassifier(modelElement)) {
                createClassifier(modelElement);
            } else if (ModelFacade.isAAttribute(modelElement)) {
                createAttribute(modelElement);
            } else if (ModelFacade.isAOperation(modelElement)) {
                createOperation(modelElement);
            } else if (ModelFacade.isAPackage(modelElement)) {
                createPackage(modelElement);
            } else {
                throw new IllegalArgumentException(modelElement.getClass()
                        + " is not a Classifier, Attribute or Operation.");
            }
        } else {
            objDocBlock = new DocBlock();
            iDocBlockType = BLOCK_TYPE_UNKNOWN;

            objDocBlock.setDescription("UNKNOWN");
        }
    }

    /**
     * Creates a new file level DocBlock for the given model element
     *
     * @param modelElement The model element to document.
     *
     * @throws Exception
     */
    private final void createFile(Object modelElement) throws Exception {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                + " has wrong object type, Classifier required");
        }

        objDocBlock = new DocBlock();
        objModelElement = modelElement;
        iDocBlockType = BLOCK_TYPE_FILE;

        objDocBlock.enableTag(DocBlock.TAG_TYPE_PACKAGE);
        objDocBlock.enableTag(DocBlock.TAG_TYPE_SUBPACKAGE);

        objDocBlock.disableTag(DocBlock.TAG_TYPE_ACCESS);
        objDocBlock.disableTag(DocBlock.TAG_TYPE_STATIC);

        String sPackageName = NameGenerator.generatePackageName(modelElement);

        if (sPackageName != null && sPackageName != "") {
            int iFirstUnderscore = sPackageName.indexOf("_");
            if (iFirstUnderscore == -1) {
                objDocBlock.setTag(DocBlock.TAG_TYPE_PACKAGE, sPackageName);
            } else {
                objDocBlock.setTag(DocBlock.TAG_TYPE_PACKAGE,
                        sPackageName.substring(0, iFirstUnderscore));
                objDocBlock.setTag(DocBlock.TAG_TYPE_SUBPACKAGE,
                        sPackageName.substring(iFirstUnderscore + 1));
            }
        }
    }

    /**
     * Updates a file level DocBlock for the given model element
     *
     * @param modelElement The model element to document.
     *
     * @throws Exception
     */
    private final void updateFile(Object modelElement) throws Exception {
        // TODO: add license
        // TODO: add copyright

        GregorianCalendar calNow = new GregorianCalendar();
        int iDay  = calNow.get(Calendar.DATE);
        int iMon  = (calNow.get(Calendar.MONTH) + 1);
        int iYear = calNow.get(Calendar.YEAR);
        int iHour = calNow.get(Calendar.HOUR_OF_DAY);
        int iMin  = calNow.get(Calendar.MINUTE);
        int iSec  = calNow.get(Calendar.SECOND);

        String sProjectName = null;
        Object objModel = ModelFacade.getModel(modelElement);
        if (objModel != null) {
            sProjectName = ModelFacade.getName(objModel);
        }

        String sDateTime = "";
        sDateTime += iDay  < 10 ? "0" + iDay  + "." : iDay + ".";
        sDateTime += iMon  < 10 ? "0" + iMon  + "." : iMon + ".";
        sDateTime += iYear + ", ";
        sDateTime += iHour < 10 ? "0" + iHour + ":" : iHour + ":";
        sDateTime += iMin  < 10 ? "0" + iMin  + ":" : iMin + ":";
        sDateTime += iSec  < 10 ? "0" + iSec        : iSec + "";

        String sDescription = "";

        // TODO: replace "\" in sFilename with "/"
        if (sFilename != null && sProjectName != null) {
            sDescription += sProjectName + " - " + sFilename + "\n\n";
        }

        sDescription += "$" + "Id" + "$" + "\n\n";

        if (sProjectName != null) {
            sDescription += "This file is part of " + sProjectName + ".\n\n";
        }

        sDescription += "Automatic generated with ArgoUML "
                + ArgoVersion.getVersion() + " on " + sDateTime;

        objDocBlock.setDescription(sDescription);

        objDocBlock.setTags(ModelFacade.getTaggedValues(modelElement));
    }

    /**
     * Creates a new DocBlock for the given model element to document an include
     *
     * @param modelElement The model element to document.
     *
     * @throws Exception
     */
    private final void createInclude(Object modelElement) throws Exception {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                + " has wrong object type, Classifier required");
        }

        objDocBlock = new DocBlock();
        objModelElement = modelElement;
        iDocBlockType = BLOCK_TYPE_INCLUDE;

        objDocBlock.disableTag(DocBlock.TAG_TYPE_ACCESS);
        objDocBlock.disableTag(DocBlock.TAG_TYPE_STATIC);

        objDocBlock.setDescription("include "
                + NameGenerator.generateClassifierName(modelElement));
        objDocBlock.setTags(ModelFacade.getTaggedValues(modelElement));
    }

    /**
     * Creates a new class level DocBlock for the given model element
     *
     * @param modelElement The model element to document.
     *
     * @throws Exception
     */
    private final void createClassifier(Object modelElement) throws Exception {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                + " has wrong object type, Classifier required");
        }

        objDocBlock = new DocBlock();
        objModelElement = modelElement;
        iDocBlockType = BLOCK_TYPE_CLASSIFIER;

        objDocBlock.enableTag(DocBlock.TAG_TYPE_PACKAGE);
        objDocBlock.enableTag(DocBlock.TAG_TYPE_SUBPACKAGE);
        objDocBlock.enableTag(DocBlock.TAG_TYPE_STATIC);
        objDocBlock.enableTag(DocBlock.TAG_TYPE_ABSTRACT);

        objDocBlock.setDefaultDescription("Short description of class "
                + NameGenerator.generateClassifierName(modelElement));

        objDocBlock.setTags(ModelFacade.getTaggedValues(modelElement));

        if (ModelFacade.isAbstract(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ABSTRACT, "true");
        }

        if (ModelFacade.isPublic(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ACCESS, "public");
        } else if (ModelFacade.isProtected(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ACCESS, "protected");
        } else if (ModelFacade.isPrivate(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ACCESS, "private");
        }

        String sPackageName = NameGenerator.generatePackageName(modelElement);
        if (sPackageName != null && sPackageName != "") {
            int iFirstUnderscore = sPackageName.indexOf("_");
            if (iFirstUnderscore == -1) {
                objDocBlock.setTag(DocBlock.TAG_TYPE_PACKAGE, sPackageName);
            } else {
                objDocBlock.setTag(DocBlock.TAG_TYPE_PACKAGE,
                        sPackageName.substring(0, iFirstUnderscore));
                objDocBlock.setTag(DocBlock.TAG_TYPE_SUBPACKAGE,
                        sPackageName.substring(iFirstUnderscore + 1));
            }
        }
    }

    /**
     * Creates a new DocBlock for the given model element to document
     * an attribute
     *
     * @param modelElement The model element to document.
     *
     * @throws Exception
     */
    private final void createAttribute(Object modelElement) throws Exception {
        if (!ModelFacade.isAAttribute(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                + " has wrong object type, Attribute required");
        }

        objDocBlock = new DocBlock();
        objModelElement = modelElement;
        iDocBlockType = BLOCK_TYPE_ATTRIBUTE;

        objDocBlock.enableTag(DocBlock.TAG_TYPE_STATIC);
        objDocBlock.enableTag(DocBlock.TAG_TYPE_VAR);

        objDocBlock.disableTag(DocBlock.TAG_TYPE_AUTHOR);
        objDocBlock.disableTag(DocBlock.TAG_TYPE_VERSION);

        objDocBlock.setDefaultDescription("Short description of attribute "
                + ModelFacade.getName(modelElement));

        objDocBlock.setTags(ModelFacade.getTaggedValues(modelElement));

        Object objVarType = ModelFacade.getType(modelElement);
        if (objVarType != null) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_VAR,
                    ModelFacade.getName(objVarType));
        }

        if (ModelFacade.isPublic(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ACCESS, "public");
        } else if (ModelFacade.isProtected(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ACCESS, "protected");
        } else if (ModelFacade.isPrivate(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ACCESS, "private");
        }
    }

    /**
     * Creates a new DocBlock for the given model element to document a method
     *
     * @param modelElement The model element to document.
     *
     * @throws Exception
     */
    private final void createOperation(Object modelElement) throws Exception {
        if (!ModelFacade.isAOperation(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                + " has wrong object type, Operation required");
        }

        objDocBlock = new DocBlock();
        objModelElement = modelElement;
        iDocBlockType = BLOCK_TYPE_OPERATION;

        objDocBlock.enableTag(DocBlock.TAG_TYPE_PARAM);
        objDocBlock.enableTag(DocBlock.TAG_TYPE_RETURN);
        objDocBlock.enableTag(DocBlock.TAG_TYPE_STATIC);
        objDocBlock.enableTag(DocBlock.TAG_TYPE_ABSTRACT);

        objDocBlock.setDefaultDescription("Short description of method "
                + ModelFacade.getName(modelElement));

        if (ModelFacade.isPublic(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ACCESS, "public");
        } else if (ModelFacade.isProtected(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ACCESS, "protected");
        } else if (ModelFacade.isPrivate(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ACCESS, "private");
        }

        objDocBlock.setTags(ModelFacade.getTaggedValues(modelElement));

        if (ModelFacade.isAbstract(modelElement)) {
            objDocBlock.setTag(DocBlock.TAG_TYPE_ABSTRACT, "true");
        }

        Collection colParameter = ModelFacade.getParameters(modelElement);
        if (colParameter != null) {
            Iterator itParameter = colParameter.iterator();
            while (itParameter.hasNext()) {
                Object objParameter = itParameter.next();
                if (ModelFacade.isReturn(objParameter)) {
                    Object objReturnType = ModelFacade.getType(objParameter);
                    if (objReturnType != null) {
                        String sPackageName = NameGenerator
                            .generatePackageName(objReturnType);
                        if (sPackageName != null && sPackageName != "") {
                            objDocBlock.setTag(DocBlock.TAG_TYPE_RETURN,
                                    sPackageName + "_"
                                    + ModelFacade.getName(objReturnType));
                        } else {
                            objDocBlock.setTag(DocBlock.TAG_TYPE_RETURN,
                                    ModelFacade.getName(objReturnType));
                        }
                    } else {
                        objDocBlock.setTag(DocBlock.TAG_TYPE_RETURN, "mixed");
                    }
                } else {
                    Object objParamType = ModelFacade.getType(objParameter);
                    if (objParamType != null) {
                        objDocBlock.addTag(DocBlock.TAG_TYPE_PARAM,
                                ModelFacade.getName(objParamType));
                    }
                }
            }
        }
    }

    /**
     * Creates a new package level DocBlock for the given model element
     *
     * @param modelElement The model element to document.
     *
     * @throws Exception
     */
    private final void createPackage(Object modelElement) throws Exception {
        if (!ModelFacade.isAPackage(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                + " has wrong object type, Classifier required");
        }

        objDocBlock = new DocBlock();
        objModelElement = modelElement;
        iDocBlockType = BLOCK_TYPE_PACKAGE;

        objDocBlock.enableTag(DocBlock.TAG_TYPE_PACKAGE);
        objDocBlock.enableTag(DocBlock.TAG_TYPE_SUBPACKAGE);

        objDocBlock.disableTag(DocBlock.TAG_TYPE_ACCESS);
        objDocBlock.disableTag(DocBlock.TAG_TYPE_STATIC);

        objDocBlock.setDefaultDescription("Short description of package "
                + NameGenerator.generatePackageName(modelElement));

        objDocBlock.setTags(ModelFacade.getTaggedValues(modelElement));

        String sPackageName = NameGenerator.generatePackageName(modelElement);
        if (sPackageName != null && sPackageName != "") {
            int iFirstUnderscore = sPackageName.indexOf("_");
            if (iFirstUnderscore == -1) {
                objDocBlock.setTag(DocBlock.TAG_TYPE_PACKAGE, sPackageName);
            } else {
                objDocBlock.setTag(DocBlock.TAG_TYPE_PACKAGE,
                        sPackageName.substring(0, iFirstUnderscore));
                objDocBlock.setTag(DocBlock.TAG_TYPE_SUBPACKAGE,
                        sPackageName.substring(iFirstUnderscore + 1));
            }
        }
    }

    /**
     * Sets filename for file level DocBlock.
     *
     * @param sBlockFilename The filename to set for the DocBlock.
     *
     * @return <code>true</code> on success;
     *         <code>false</code> otherwise.
     */
    public boolean setFilename(String sBlockFilename) {
        if (objDocBlock != null) {
            if (iDocBlockType == BLOCK_TYPE_FILE) {
                if (sBlockFilename != null
                        && sBlockFilename.trim().length() > 0) {
                    sFilename = sBlockFilename.trim();

                    LOG.info("Set filename " + sFilename + " successfull.");

                    try {
                        updateFile(objModelElement);
                    } catch (Exception exp) {
                        LOG.warn("Update file level DocBlock FAILED: "
                            + exp.getMessage());
                    }

                    return true;
                } else {
                    throw new IllegalArgumentException("can not be empty");
                }
            } else {
                LOG.error("method setFilename is only available for file "
                        + "level DocBlock, not for type " + iDocBlockType);

                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Gets string representation of the DocBlock without indention.
     *
     * @return The unindented string representation of the DocBlock;
     *         <code>null</code> otherwise.
     */
    public final String toString() {
        return toString("");
    }

    /**
     * Gets string representation of the DocBlock.
     *
     * @param sIndent The indention string.
     *
     * @return The indented string representation of the DocBlock;
     *         <code>null</code> otherwise.
     */
    public final String toString(String sIndent) {
        if (objDocBlock != null) {
            return objDocBlock.toString(sIndent);
        } else {
            return null;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the base class for PHPDocumentor DocBlock's.
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class DocBlock {
        public static final int TAG_TYPE_ACCESS = 1;
        public static final int TAG_TYPE_AUTHOR = 2;
        public static final int TAG_TYPE_DEPRECATED = 3;
        public static final int TAG_TYPE_PACKAGE = 4;
        public static final int TAG_TYPE_PARAM = 5;
        public static final int TAG_TYPE_RETURN = 6;
        public static final int TAG_TYPE_SEE = 7;
        public static final int TAG_TYPE_SINCE = 8;
        public static final int TAG_TYPE_STATIC = 9;
        public static final int TAG_TYPE_SUBPACKAGE = 10;
        public static final int TAG_TYPE_VAR = 11;
        public static final int TAG_TYPE_VERSION = 12;
        public static final int TAG_TYPE_ABSTRACT = 13;

        /**
         * Default description of DocBlock
         */
        private String sDefaultDescription = null;

        /**
         * User defined description of DocBlock
         */
        private String sDescription = null;

        /**
         * Map of all tags in DocBlock
         */
        private TreeMap tmTags = new TreeMap();

        /**
         * Class constructor
         */
        public DocBlock() {
            enableTag(TAG_TYPE_ACCESS);
            enableTag(TAG_TYPE_AUTHOR);
            enableTag(TAG_TYPE_DEPRECATED);
            enableTag(TAG_TYPE_SEE);
            enableTag(TAG_TYPE_SINCE);
            enableTag(TAG_TYPE_VERSION);
        }

        /**
         * Formats the DocBlock without indention.
         *
         * @return The formatted DocBlock.
         */
        public final String toString() {
            return toString("");
        }

        /**
         * Formats the DocBlock
         *
         * @param sIndent line indention string
         *
         * @return The formatted DocBlock.
         */
        public final String toString(String sIndent) {
            String s = "";

            if (sDescription == null || sDescription == "") {
                setDescription(sDefaultDescription);
            }

            s += sIndent + "/**\n";

            s += wrapDescription(sIndent, 80);

            Iterator itTags = tmTags.entrySet().iterator();
            while (itTags.hasNext()) {
                Map.Entry entryMap = (Map.Entry) itTags.next();
                Object objEntry = entryMap.getValue();
                if (objEntry instanceof ArrayList) {
                    ArrayList objArrayList = (ArrayList) objEntry;
                    for (int i = 0; i < objArrayList.size(); ++i) {
                        Tag objTag = (Tag) objArrayList.get(i);
                        if (objTag.getTag() != null) {
                            s += sIndent + " * " + objTag.getTag() + "\n";
                        }
                    }
                } else {
                    Tag objTag = (Tag) objEntry;
                    if (objTag.getContent() != null) {
                        if (objTag.getTag() != null) {
                            s += sIndent + " * " + objTag.getTag() + "\n";
                        }
                    }
                }
            }

            s += sIndent + " */\n";

            return s;
        }

        /**
         * Wraps long description lines
         *
         * @param sIndent   The indention string.
         * @param iMaxWidth Maximal line length
         *
         * @return The formated description part of DocBlock
         */
        private final String wrapDescription(String sIndent, int iMaxWidth) {
            String sWrapped = "";

            if (sDescription != null && sDescription.trim() != "") {
                StringTokenizer stDescription =
                        new StringTokenizer(sDescription, "\n\r", true);
                while (stDescription.hasMoreElements()) {
                    String sToken = stDescription.nextToken();
                    if (!sToken.equals("\n")
                            && stDescription.hasMoreElements()) {
                        stDescription.nextToken();
                    } else if (sToken.equals("\n")) {
                        sToken = "";
                    }

                    if (sToken.length() > (iMaxWidth - sIndent.length() - 3)) {
                        String sLine = "";

                        StringTokenizer stLine =
                                new StringTokenizer(sToken, " ");
                        while (stLine.hasMoreElements()) {
                            String sLineToken = stLine.nextToken();

                            int iMaxLen = iMaxWidth - 4 - sIndent.length();
                            if ((sLine.length() + sLineToken.length())
                                    <= iMaxLen) {
                                sLine += sLine.trim() != "" ? " " : "";
                                sLine += sLineToken;
                            } else {
                                if (sLine.trim() != "") {
                                    sWrapped += sIndent + " * " + sLine + "\n";
                                }
                                sLine = "";
                            }
                        }

                        if (sLine.trim() != "") {
                            sWrapped += sIndent + " * " + sLine + "\n";
                        }
                    } else {
                        if (sToken.trim() != "") {
                            sWrapped += sIndent + " * " + sToken + "\n";
                        } else if (stDescription.hasMoreElements()) {
                            sWrapped += sIndent + " *\n";
                        }
                    }
                }

                sWrapped += sIndent + " *\n";

                return sWrapped;
            } else {
                return "";
            }
        }

        /**
         * Sets description lines for DocBlock
         *
         * @param sBlockDesc descrition lines, separated with \n
         *
         * @return <code>true</code> on success,
         *         <code>false</code> otherwise;
         */
        public final boolean setDescription(String sBlockDesc) {
            if (sBlockDesc != null && sBlockDesc.trim().length() > 0) {
                sDescription = sBlockDesc.trim();

                return true;
            } else {
                return false;
            }
        }

        /**
         * Sets default description lines for DocBlock
         *
         * @param sBlockDesc default descrition lines, separated with \n
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         */
        public final boolean setDefaultDescription(String sBlockDesc) {
            if (sBlockDesc != null && sBlockDesc.trim().length() > 0) {
                sDefaultDescription = sBlockDesc.trim();

                return true;
            } else {
                return false;
            }
        }

        /**
         * Enables a tag of given type.
         *
         * @param iTagType The type of the tag to enable.
         *
         * @throws IllegalArgumentException
         */
        protected final void enableTag(int iTagType)
            throws IllegalArgumentException {
            switch (iTagType) {
            // TODO: move block 4 spaces right if checkstyle is fixed
            case TAG_TYPE_ACCESS:
                tmTags.put("access", new AccessTag());
                break;
            case TAG_TYPE_ABSTRACT:
                tmTags.put("abstract", new AbstractTag());
                break;
            case TAG_TYPE_AUTHOR:
                tmTags.put("author", new AuthorTag());
                break;
            case TAG_TYPE_DEPRECATED:
                tmTags.put("deprecated", new DeprecatedTag());
                break;
            case TAG_TYPE_PACKAGE:
                tmTags.put("package", new PackageTag());
                break;
            case TAG_TYPE_PARAM:
                tmTags.put("param", new ArrayList());
                break;
            case TAG_TYPE_RETURN:
                tmTags.put("return", new ReturnTag());
                break;
            case TAG_TYPE_SEE:
                tmTags.put("see", new SeeTag());
                break;
            case TAG_TYPE_SINCE:
                tmTags.put("since", new SinceTag());
                break;
            case TAG_TYPE_STATIC:
                tmTags.put("static", new StaticTag());
                break;
            case TAG_TYPE_SUBPACKAGE:
                tmTags.put("subpackage", new SubpackageTag());
                break;
            case TAG_TYPE_VAR:
                tmTags.put("var", new VarTag());
                break;
            case TAG_TYPE_VERSION:
                tmTags.put("version", new VersionTag());
                break;
            default:
                throw new IllegalArgumentException(iTagType
                        + " is not a valid tag type for add.");
            }
        }

        /**
         * Disables a tag of given type.
         *
         * @param iTagType The type of the tag to disable.
         *
         * @throws IllegalArgumentException
         */
        protected final void disableTag(int iTagType)
            throws IllegalArgumentException {
            switch (iTagType) {
            // TODO: move block 4 spaces right if checkstyle is fixed
            case TAG_TYPE_ACCESS:
                tmTags.remove("access");
                break;
            case TAG_TYPE_AUTHOR:
                tmTags.remove("author");
                break;
            case TAG_TYPE_STATIC:
                tmTags.remove("static");
                break;
            case TAG_TYPE_VERSION:
                tmTags.remove("version");
                break;
            default:
                throw new IllegalArgumentException(iTagType
                        + " is not a valid tag type for remove.");
            }
        }

        /**
         * Sets tags from tagged values
         *
         * @param itTaggedValues Tagged values iterator
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         *
         * @see ModelFacade#getTaggedValues
         */
        public final boolean setTags(Iterator itTaggedValues) {
            if (itTaggedValues == null) {
                return false;
            }

            if (!(itTaggedValues instanceof Iterator)) {
                return false;
            }

            if (!itTaggedValues.hasNext()) {
                return true;
            } else {
                boolean bSuccess = true;

                while (itTaggedValues.hasNext()) {
                    if (!setTag(itTaggedValues.next())) {
                        bSuccess = false;
                    }
                }

                return bSuccess;
            }
        }

        /**
         * Sets tag value from TaggedValue element.
         *
         * @param objTaggedValue A TaggedValue object.
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         */
        private final boolean setTag(Object objTaggedValue) {
            if (!(ModelFacade.isATaggedValue(objTaggedValue))) {
                return false;
            }

            boolean bSuccess = false;

            try {
                bSuccess = setTag(ModelFacade.getTagOfTag(objTaggedValue),
                        ModelFacade.getValueOfTag(objTaggedValue));
            } catch (IllegalArgumentException exp) {
                LOG.warn("setting tag FAILED: " + exp.getMessage());
            }

            return bSuccess;
        }

        /**
         * Add a new tag of given type and sets his value
         *
         * @param iTagType  The tag type.
         * @param sTagValue The new tag value.
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         *
         * @throws IllegalArgumentException
         */
        public final boolean addTag(int iTagType, String sTagValue)
            throws IllegalArgumentException {
            switch (iTagType) {
            // TODO: move block 4 spaces right if checkstyle is fixed
            case TAG_TYPE_PARAM:
                ArrayList alParams = (ArrayList) tmTags.get("param");

                if (alParams != null) {
                    ParamTag tagParam = new ParamTag();
                    if (tagParam.setContent(sTagValue)) {
                        return alParams.add(tagParam);
                    } else {
                        return false;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Can not add a tag of "
                        + "type "  + iTagType + " with value '"
                        +  sTagValue + "' to the DocBlock");
            }

            return false;
        }

        /**
         * Sets value of a tag.
         *
         * @param iTagType  The tag type.
         * @param sTagValue The new tag value.
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         *
         * @throws IllegalArgumentException
         */
        public final boolean setTag(int iTagType, String sTagValue)
            throws IllegalArgumentException {
            Tag objTag = null;

            switch (iTagType) {
            // TODO: move block 4 spaces right if checkstyle is fixed
            case TAG_TYPE_ACCESS:
                objTag = (AccessTag) tmTags.get("access");
                break;
            case TAG_TYPE_ABSTRACT:
                objTag = (AbstractTag) tmTags.get("abstract");
                break;
            case TAG_TYPE_AUTHOR:
                objTag = (AuthorTag) tmTags.get("author");
                break;
            case TAG_TYPE_DEPRECATED:
                objTag = (DeprecatedTag) tmTags.get("deprecated");
                break;
            case TAG_TYPE_PACKAGE:
                objTag = (PackageTag) tmTags.get("package");
                break;
            case TAG_TYPE_RETURN:
                objTag = (ReturnTag) tmTags.get("return");
                break;
            case TAG_TYPE_SEE:
                objTag = (SeeTag) tmTags.get("see");
                break;
            case TAG_TYPE_SINCE:
                objTag = (SinceTag) tmTags.get("since");
                break;
            case TAG_TYPE_STATIC:
                objTag = (StaticTag) tmTags.get("static");
                break;
            case TAG_TYPE_SUBPACKAGE:
                objTag = (SubpackageTag) tmTags.get("subpackage");
                break;
            case TAG_TYPE_VAR:
                objTag = (VarTag) tmTags.get("var");
                break;
            case TAG_TYPE_VERSION:
                objTag = (VersionTag) tmTags.get("version");
                break;
            default:
                throw new IllegalArgumentException("Can not set value '"
                        + sTagValue + "' for tag type " + iTagType);
            }

            if (objTag != null) {
                return objTag.setContent(sTagValue);
            } else {
                return false;
            }
        }

        /**
         * Sets tag value for a named tag.
         *
         * @param sTagName  The tag name (or better the tagged value name).
         * @param sTagValue The new tag value.
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         *
         * @throws IllegalArgumentException
         */
        private final boolean setTag(String sTagName, String sTagValue)
            throws IllegalArgumentException {
            if (sTagName.equals("documentation")) {
                return setDescription(sTagValue);
            } else if (sTagName.equals("access")) {
                return setTag(TAG_TYPE_ACCESS, sTagValue);
            } else if (sTagName.equals("abstract")) {
                return setTag(TAG_TYPE_ABSTRACT, sTagValue);
            } else if (sTagName.equals("author")) {
                return setTag(TAG_TYPE_AUTHOR, sTagValue);
            } else if (sTagName.equals("deprecated")) {
                return setTag(TAG_TYPE_DEPRECATED, sTagValue);
            } else if (sTagName.equals("package")) {
                return setTag(TAG_TYPE_PACKAGE, sTagValue);
            } else if (sTagName.equals("return")) {
                return setTag(TAG_TYPE_RETURN, sTagValue);
            } else if (sTagName.equals("see")) {
                return setTag(TAG_TYPE_SEE, sTagValue);
            } else if (sTagName.equals("since")) {
                return setTag(TAG_TYPE_SINCE, sTagValue);
            } else if (sTagName.equals("static")) {
                return setTag(TAG_TYPE_STATIC, sTagValue);
            } else if (sTagName.equals("subpackage")) {
                return setTag(TAG_TYPE_SUBPACKAGE, sTagValue);
            } else if (sTagName.equals("var")) {
                return setTag(TAG_TYPE_VAR, sTagValue);
            } else if (sTagName.equals("version")) {
                return setTag(TAG_TYPE_VERSION, sTagValue);
            } else {
                /* tags we ignore for PHP */
                if (!sTagName.equals("src_lang") &&
                    !sTagName.equals("transient") &&
                    !sTagName.equals("volatile")) {
                    throw new IllegalArgumentException("Can not set value '"
                            + sTagValue + "' for tag '" + sTagName + "'");
                } else {
                    return true;
                }
            }
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the abstract base class for all PHPDocumentor tags.
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private abstract class Tag {
        /**
         * name of tag
         */
        private String sName = null;

        /**
         * Class constructor
         *
         * @param sTagName The name of the tag to instanciate.
         */
        protected Tag(String sTagName) {
            sName = sTagName;
        }

        /**
         * Gets tag content without tag name
         *
         * @return tag content without tag name
         */
        protected abstract String getContent();

        /**
         * Sets tag content without tag name
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         */
        protected abstract boolean setContent(String sTagContent);

        /**
         * Gets tag name with prefixed @
         *
         * @return tag name with prefixed @
         */
        private final String getName() {
            return "@" + this.sName;
        }

        /**
         * Gets the complete tag.
         *
         * @return The complete tag;
         *         <code>null</code> otherwise.
         */
        public final String getTag() {
            if (getContent() != null) {
                if (getContent().trim().length() > 0) {
                    return getName() + " " + getContent();
                } else {
                    return getName();
                }
            } else {
                return null;
            }
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the base class for simple PHPDocumentor tags.
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private abstract class SimpleTag extends Tag {
        /**
         * The content of this tag
         */
        private String sContent = null;

        /**
         * Class constructor
         *
         * @param sTagName name of tag
         */
        public SimpleTag(String sTagName) {
            super(sTagName);
        }

        /**
         * Gets the content of this tag.
         */
        public final String getContent() {
            return sContent;
        }

        /**
         * Sets the content of this tag.
         *
         * @param sTagContent The content of this tag.
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         */
        public boolean setContent(String sTagContent) {
            if (sTagContent != null && sTagContent.trim().length() > 0) {
                sContent = sTagContent.trim();

                return true;
            } else {
                return false;
            }
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the base class for boolean PHPDocumentor tags.
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private abstract class BooleanTag extends Tag {
        /**
         * Is this tag <code>true</code> or <code>false</code>?
         */
        private boolean bContent = false;

        /**
         * The reason because this tag is true.
         */
        private String sReason = null;

        /**
         * Class constructor
         *
         * @param sTagName name of tag
         */
        public BooleanTag(String sTagName) {
            super(sTagName);
        }

        /**
         * Gets the content of this tag.
         *
         * @return empty string if element content is true;
         *         reason if given;
         *         <code>null</code> otherwise.
         */
        public final String getContent() {
            if (bContent) {
                if (sReason != null && sReason.length() > 0) {
                    return sReason;
                } else {
                    return "";
                }
            } else {
                return null;
            }
        }

        /**
         * Sets the content of this tag.
         *
         * @param sTagContent The content of this tag.
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         */
        public final boolean setContent(String sTagContent) {
            if (sTagContent != null && sTagContent.trim().length() > 0) {
                if (sTagContent.trim().equals("true")) {
                    return setContent(true);
                } else if (sTagContent.trim().equals("false")) {
                    return setContent(false);
                } else {
                    sReason = sTagContent;
                    return setContent(true);
                }
            } else {
                return false;
            }
        }

        /**
         * Sets the content of this tag.
         *
         * @param bTagContent <code>true</code> or <code>false</code>.
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         */
        public final boolean setContent(boolean bTagContent) {
            bContent = bTagContent;

            return true;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation of
     * PHPDocumentor's @access tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class AccessTag extends Tag {
        /**
         * Element is public visible
         */
        private boolean bPublic = true;

        /**
         * Element is protected visible
         */
        private boolean bProtected = false;

        /**
         * Element is private visible
         */
        private boolean bPrivate = false;

        /**
         * Class constructor
         */
        public AccessTag() {
            super("access");
        }

        /**
         * Sets visibility to public
         *
         * @return <code>true</code>
         */
        public boolean setPublic() {
            this.bPublic    = true;
            this.bProtected = false;
            this.bPrivate   = false;

            return true;
        }

        /**
         * Sets visibility to protected
         *
         * @return <code>true</code>
         */
        public boolean setProtected() {
            this.bPublic    = false;
            this.bProtected = true;
            this.bPrivate   = false;

            return true;
        }

        /**
         * Sets visibility to private
         *
         * @return <code>true</code>
         */
        public boolean setPrivate() {
            this.bPublic    = false;
            this.bProtected = false;
            this.bPrivate   = true;

            return true;
        }

        /**
         * Gets tag content without tag name
         *
         * @return public|protected|private,
         *         <code>null</code> otherwise;
         */
        public String getContent() {
            if (bPublic) {
                return "public";
            } else if (bProtected) {
                return "protected";
            } else if (bPrivate) {
                return "private";
            }

            return null;
        }

        public boolean setContent(String sTagContent) {
            if (sTagContent != null && sTagContent.trim().length() > 0) {
                if (sTagContent.trim().equals("public")) {
                    return setPublic();
                } else if (sTagContent.trim().equals("protected")) {
                    return setProtected();
                } else if (sTagContent.trim().equals("private")) {
                    return setPrivate();
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @author tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class AuthorTag extends Tag {
        /**
         * name of author
         */
        private String sName = null;

        /**
         * (optional) email address of author
         */
        private String sEmail = null;

        /**
         * Class constructor
         */
        public AuthorTag() {
            super("author");

            if (this.setName(PHPDocumentor.sArgoUserFullname)) {
                this.setEmail(PHPDocumentor.sArgoUserEmail);
            } else {
                this.setName("firstname and lastname of author");
                this.setEmail("author@example.org");
            }
        }

        /**
         * Sets name of author
         *
         * Resets authors email
         *
         * @param sAuthorName name of author
         *
         * @return <code>true</code> on success,
         *         <code>false</code> otherwise;
         */
        public boolean setName(String sAuthorName) {
            if (sAuthorName != null && sAuthorName.trim().length() > 0) {
                this.sName  = sAuthorName.trim();
                this.sEmail = null;

                return true;
            }

            return false;
        }

        /**
         * Sets email address of author
         *
         * @param sAuthorEmail email address of author
         *
         * @return <code>true</code> on success,
         *         <code>false</code> otherwise;
         */
        public boolean setEmail(String sAuthorEmail) {
            if (sAuthorEmail != null && sAuthorEmail.trim().length() > 0) {
                this.sEmail = sAuthorEmail.trim();

                return true;
            }

            return false;
        }

        /**
         * Gets tag content without tag name
         *
         * @return name|name, <email>,
         *         <code>null</code> otherwise;
         */
        public String getContent() {
            if (this.sName != null) {
                if (this.sEmail != null) {
                    return this.sName + ", <" + this.sEmail + ">";
                } else {
                    return this.sName;
                }
            }

            return null;
        }

        /**
         * Sets tag content without tag name
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         */
        public boolean setContent(String sTagContent) {
            return setName(sTagContent);
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @abstract tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.7
     */
    private final class AbstractTag extends BooleanTag {
        /**
         * Class constructor
         */
        public AbstractTag() {
            super("abstract");
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @deprecated tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class DeprecatedTag extends BooleanTag {
        /**
         * Class constructor
         */
        public DeprecatedTag() {
            super("deprecated");
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @package tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class PackageTag extends SimpleTag {
        /**
         * Class constructor
         */
        public PackageTag() {
            super("package");
        }
    }

    // -------------------------------------------------------------------------

    private final class ParamTag extends Tag {
        /**
         * Type of parameter
         */
        private String sType = "mixed";

        /**
         * Description of parameter
         */
        private String sDescription = null;

        /**
         * Class constructor
         */
        public ParamTag() {
            super("param");
        }

        /**
         * Sets type of parameter
         *
         * @param sParamType type of parameter
         *
         * @return <code>true</code> on success,
         *         <code>false</code> otherwise;
         */
        public boolean setType(String sParamType) {
            if (sParamType != null && sParamType.trim() != "") {
                this.sType = sParamType.trim();

                return true;
            }

            return false;
        }

        /**
         * Sets decription of parameter
         *
         * @param sParamDescription description of parameter
         *
         * @return <code>true</code> on success,
         *         <code>false</code> otherwise;
         */
        public boolean setDescription(String sParamDescription) {
            if (sParamDescription != null && sParamDescription.trim() != "") {
                this.sDescription = sParamDescription.trim();

                return true;
            }

            return false;
        }

        /**
         * Gets tag content without tag name
         *
         * @return type [description],
         *         <code>null</code> otherwise;
         */
        public String getContent() {
            if (this.sDescription != null) {
                return this.sType + " " + this.sDescription;
            } else {
                return this.sType;
            }
        }

        /**
         * Sets tag content without tag name
         *
         * @return <code>true</code> on success;
         *         <code>false</code> otherwise.
         */
        public boolean setContent(String sTagContent) {
            if (sTagContent.indexOf(" ") != -1) {
                return setType(sTagContent.substring(0,
                        sTagContent.indexOf(" ")))
                        && setDescription(sTagContent.substring(
                            sTagContent.indexOf(" ")));
            } else {
                return setType(sTagContent);
            }
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @return tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class ReturnTag extends SimpleTag {
        /**
         * Class constructor
         */
        public ReturnTag() {
            super("return");
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @see tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class SeeTag extends SimpleTag {
        /**
         * Class constructor
         */
        public SeeTag() {
            super("see");
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @since tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class SinceTag extends SimpleTag {
        /**
         * Class constructor
         */
        public SinceTag() {
            super("since");
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @static tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class StaticTag extends BooleanTag {
        /**
         * Class constructor
         */
        public StaticTag() {
            super("static");
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @package tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class SubpackageTag extends SimpleTag {
        /**
         * Class constructor
         */
        public SubpackageTag() {
            super("subpackage");
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @var tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class VarTag extends SimpleTag {
        /**
         * Class constructor
         */
        public VarTag() {
            super("var");
        }
    }

    // -------------------------------------------------------------------------

    /**
     * This class is the final implementation to generate
     * PHPDocumentor's @version tag
     *
     * @author  Kai Schr&ouml;der, k.schroeder@php.net
     * @since   ArgoUML 0.15.5
     */
    private final class VersionTag extends SimpleTag {
        /**
         * Class constructor
         */
        public VersionTag() {
            super("version");
        }
    }
}
