// $Id$
// Copyright (c) 2004-2005 The Regents of the University of California. All
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.rmi.server.UID;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import org.argouml.application.ArgoVersion;

import org.argouml.application.api.Argo;
import org.argouml.application.api.Notation;
import org.argouml.application.api.PluggableNotation;

import org.argouml.language.php.PHPDocumentor;

import org.argouml.model.ModelFacade;

import org.argouml.uml.generator.FileGenerator;
import org.argouml.uml.generator.Generator2;
import org.argouml.uml.UUIDHelper;

/**
 * Generator class for PHP 4.x source code
 *
 * @author Kai Schr&ouml;der
 * @since  ArgoUML 0.15.5
 */
public class GeneratorPHP4
    extends Generator2
    implements PluggableNotation, FileGenerator {

    /**
     * Sets the indentation level to four spaces
     */
    protected static final String INDENT = "    ";

    /**
     * The name of the language this module generates source code for
     */
    protected static final String LANGUAGE_NAME = "PHP";

    /**
     * Icon to represent this notation in the GUI
     */
    protected static final Icon ICON = Argo.lookupIconResource("PHPNotation");

    /**
     * Info block already written to log file?
     */
    protected static final TreeMap TM_INFO_BLOCK_LOGGED = new TreeMap();

    /**
     * The major version of the language this module generates source code for
     */
    private int iLanguageMajorVersion = LANGUAGE_MAJOR_VERSION;

    /**
     * source section handler
     */
    private static Section objSection = null;

    /**
     * The log4j logger to log messages to
     */
    private static final Logger LOG = Logger.getLogger(GeneratorPHP4.class);

    /**
     * The major version of the language this module generates source code for
     */
    private static final int LANGUAGE_MAJOR_VERSION = 4;

    // ----- class constructors ------------------------------------------------

    /**
     * Zero-argument class constructor
     */
    private GeneratorPHP4() {
        super(Notation.makeNotation(LANGUAGE_NAME, LANGUAGE_MAJOR_VERSION
                + ".x", ICON));

        logModuleInfo();
    }

    /**
     * Class constructor
     *
     * @param iLangMajorVersion The major version of the language this module
     *                          generates source code for.
     */
    protected GeneratorPHP4(int iLangMajorVersion) {
        super(Notation.makeNotation(LANGUAGE_NAME, iLangMajorVersion
                + ".x", ICON));

        iLanguageMajorVersion = iLangMajorVersion;

        logModuleInfo();
    }

    // ----- org.argouml.application.api.NotationProvider ----------------------

    /**
     * Generates extension point
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateExtensionPoint(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateExtensionPoint(MExtensionPoint modelElement)");

        if (!ModelFacade.isAExtensionPoint(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, ExtensionPoint required");
        }

        return "generateExtensionPoint(MExtensionPoint modelElement)";
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
     * Generates operation
     *
     * @param modelElement Model element to generate notation for.
     * @param bAddDocs     Add documentation in front of notation?
     *
     * @return Generated notation for model element.
     */
    public String generateOperation(Object modelElement, boolean bAddDocs) {
        if (!ModelFacade.isAOperation(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Operation required");
        }

        String sOperation = "";

        if (bAddDocs) {
            PHPDocumentor objPHPDoc = null;
            try {
                objPHPDoc = new PHPDocumentor(modelElement);
            } catch (Exception exp) {
                LOG.error("Generating operation DocBlock FAILED: "
                    + exp.getMessage());
            } finally {
                if (objPHPDoc != null) {
                    sOperation += objPHPDoc.toString();
                }
            }
        }

        String sVisibility = generate(ModelFacade.getVisibility(modelElement));
        if (sVisibility != null && sVisibility != "") {
            sOperation += sVisibility + " ";
        }

        if (iLanguageMajorVersion > 4) {
            /* scope */
            Object ownerScope = ModelFacade.getOwnerScope(modelElement);
            if (ModelFacade.CLASSIFIER_SCOPEKIND.equals(ownerScope)) {
                sOperation += "static ";
            }

            /* changeability */
            if (ModelFacade.isLeaf(modelElement)) {
                sOperation += "final ";
            }

            /* abstractness */
            if (ModelFacade.isAbstract(modelElement)) {
                sOperation += "abstract ";
            }
        }

        boolean bReturnByReference = false;

        Iterator itTaggedValues = ModelFacade.getTaggedValues(modelElement);
        if (itTaggedValues != null && itTaggedValues instanceof Iterator) {
            while (itTaggedValues.hasNext()) {
                Object objTaggedValue = itTaggedValues.next();
                if (ModelFacade.getTagOfTag(objTaggedValue).equals("&")) {
                    if (ModelFacade.getValueOfTag(objTaggedValue)
                            .equals("true")) {
                        bReturnByReference = true;

                        break;
                    }
                }
            }
        }

        String sOperationName = NameGenerator.generate(modelElement,
                iLanguageMajorVersion);

        if (bReturnByReference) {
            sOperationName = "&" + sOperationName;
        }

        sOperation += "function " + sOperationName + "(";

        Collection colParameters = ModelFacade.getParameters(modelElement);
        if (colParameters != null) {
            Iterator itParameters = colParameters.iterator();
            boolean bFirst = true;
            while (itParameters.hasNext()) {
                Object objParameter = itParameters.next();
                if (!ModelFacade.isReturn(objParameter)) {
                    if (!bFirst) {
                        sOperation += ", ";
                    } else {
                        bFirst = false;
                    }

                    sOperation += generate(objParameter);
                }
            }
        }

        sOperation += ")";

        return sOperation;
    }

    /**
     * Generates attribute
     *
     * @param modelElement Model element to generate notation for.
     * @param bAddDocs     Add documentation in front of notation?
     *
     * @return Generated notation for model element.
     */
    public String generateAttribute(Object modelElement, boolean bAddDocs) {
        if (!ModelFacade.isAAttribute(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Attribute required");
        }

        String sAttribute = "";

        if (bAddDocs) {
            PHPDocumentor objPHPDoc = null;
            try {
                objPHPDoc = new PHPDocumentor(modelElement);
            } catch (Exception exp) {
                LOG.error("Generating attribute DocBlock FAILED: "
                    + exp.getMessage());
            } finally {
                if (objPHPDoc != null) {
                    sAttribute += objPHPDoc.toString(INDENT);
                }
            }
        }

        String sVisibility = generate(ModelFacade.getVisibility(modelElement));
        if (sVisibility != null && sVisibility != "") {
            sAttribute += sVisibility + " ";
        }

        if (iLanguageMajorVersion > 4) {
            /* scope */
            Object ownerScope = ModelFacade.getOwnerScope(modelElement);
            if (ModelFacade.CLASSIFIER_SCOPEKIND.equals(ownerScope)) {
                sAttribute += "static ";
            }
        } else {
            sAttribute += "var ";
        }

        sAttribute += "$" + NameGenerator.generate(modelElement,
                iLanguageMajorVersion);

        String sInitialValue = null;
        Object exprInitialValue = ModelFacade.getInitialValue(modelElement);
        if (exprInitialValue != null) {
            sInitialValue = generateDefaultValue(
                    ModelFacade.getType(modelElement),
                    generate(exprInitialValue).trim(), false);
        } else {
            sInitialValue = generateDefaultValue(
                    ModelFacade.getType(modelElement), null, false);
        }

        if (sInitialValue != null && sInitialValue.length() > 0) {
            sAttribute += " = " + sInitialValue;
        } else {
            sAttribute += "[ ";
            sAttribute += (exprInitialValue != null) ? "!= null" : "null";
            sAttribute += " | ";
            sAttribute += generateDefaultValue(
                ModelFacade.getType(modelElement),
                    generate(exprInitialValue).trim(), false);
            sAttribute += " | ";
            sAttribute += generateDefaultValue(
                ModelFacade.getType(modelElement),
                    generate(exprInitialValue).trim(), true);
            sAttribute += " ]";
        }

        sAttribute += ";";

        return sAttribute;
    }

    /**
     * Generates parameter
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateParameter(Object modelElement) {
        if (!ModelFacade.isAParameter(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Parameter required");
        }

        String sParameter = "";

        if (ModelFacade.isReturn(modelElement)) {
            Object objType = ModelFacade.getType(modelElement);
            if (ModelFacade.getName(objType).equals("void")) {
                return "";
            } else {
                String sType = convertType(objType);
                if (sType != null && sType.trim() != "") {
                    return "return (" + sType + ") $returnValue;";
                } else {
                    return "return $returnValue;";
                }
            }
        } else {
            if (iLanguageMajorVersion < 5) {
                // TODO: Do we really need this for PHP5?
                // TODO: Implent this in ModelFacade
                /* if OUT or INOUT, then pass by reference */
                if (ModelFacade.getKind(modelElement).equals(
                        ModelFacade.INOUT_PARAMETERDIRECTIONKIND)
                        || ModelFacade.getKind(modelElement).equals(
                            ModelFacade.OUT_PARAMETERDIRECTIONKIND)) {
                    sParameter += "&";
                }
            }

            sParameter += "$" + ModelFacade.getName(modelElement);

            String sDefaultValue =
                generate(ModelFacade.getDefaultValue(modelElement));
            if (sDefaultValue != null && sDefaultValue.length() > 0) {
                sParameter += " = " + sDefaultValue;
            } else {
                boolean bAddDefaultValue = false;

                Collection colParameters =
                        ModelFacade.getParameters(ModelFacade
                                .getBehavioralFeature(modelElement));
                if (colParameters != null) {
                    Iterator itParameters = colParameters.iterator();
                    while (itParameters.hasNext()) {
                        Object objParameter = itParameters.next();
                        if (!ModelFacade.isReturn(objParameter)) {
                            if (!modelElement.equals(objParameter)) {
                                if (ModelFacade.getDefaultValue(objParameter)
                                        != null) {
                                    bAddDefaultValue = true;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (bAddDefaultValue) {
                    sParameter += " = " + generateDefaultValue(
                        ModelFacade.getType(modelElement), null, false);
                }
            }
        }

        return sParameter;
    }

    /**
     * Generates package
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     *
     * TODO: fix org.argouml.model.ModelFacade#getType
     */
    public String generatePackage(Object modelElement) {
        String sPackage = "";

        if (!ModelFacade.isAPackage(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Package required");
        }

        String sPackageName =
                NameGenerator.generate(modelElement, iLanguageMajorVersion);

        PHPDocumentor objPHPDoc = null;
        try {
            objPHPDoc = new PHPDocumentor(modelElement);
        } catch (Exception exp) {
            LOG.error("Generating package DocBlock FAILED: "
                    + exp.getMessage());
        } finally {
            if (objPHPDoc != null) {
                sPackage += objPHPDoc.toString() + "\n";
            }
        }

        Collection colElements = ModelFacade.getOwnedElements(modelElement);
        if (colElements != null) {
            Iterator itElements = colElements.iterator();
            if (itElements != null) {
                while (itElements.hasNext()) {
                    Object objElement = itElements.next();
                    if (ModelFacade.isAPackage(objElement)) {
                        sPackage += generate(objElement) + "\n";
                    } else if (ModelFacade.isAClassifier(objElement)) {
                        sPackage += generate(objElement) + "\n";
                    } else {
                        sPackage += "/*\n";
                        sPackage += "feature not supported by PHP:\n";
                        sPackage += "-----------------------------\n";
                        sPackage += generate(objElement) + "\n";
                        sPackage += "*/\n";
                    }
                    if (itElements.hasNext()) {
                        sPackage += "\n";
                    }
                }
            }
        } else {
            sPackage += "// this package contains no elements\n";
        }

        sPackage += "\n/* end of package " + sPackageName + " */";

        return sPackage;
    }

    /**
     * Generates class or interface
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateClassifier(Object modelElement) {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        String sClassifier = "";

        String sClassType = "";
        if (iLanguageMajorVersion > 4) {
            if (ModelFacade.isAClass(modelElement)) {
                if (ModelFacade.isLeaf(modelElement)) {
                    sClassType = "final class";
                } else {
                    sClassType = "class";
                }
                if (ModelFacade.isAbstract(modelElement)) {
                    sClassType = "abstract " + sClassType;
                }
            } else if (ModelFacade.isAInterface(modelElement)) {
                sClassType = "interface";
                if (ModelFacade.isLeaf(modelElement)) {
                    sClassType = "final " + sClassType;
                }
            } else {
                return null;
            }
        } else {
            if (ModelFacade.isAClass(modelElement)
                    || ModelFacade.isAInterface(modelElement)) {
                sClassType = "class";
            } else {
                return null;
            }
        }

        PHPDocumentor objPHPDoc = null;
        try {
            objPHPDoc = new PHPDocumentor(modelElement);
        } catch (Exception exp) {
            LOG.error("Generating classifier DocBlock FAILED: "
                    + exp.getMessage());
        } finally {
            if (objPHPDoc != null) {
                sClassifier += objPHPDoc.toString();
            }
        }

        String sClassName =
                NameGenerator.generate(modelElement, iLanguageMajorVersion);

        sClassifier += sClassType + " " + sClassName + "\n";

        sClassifier += generateClassifierGeneralisations(modelElement);
        sClassifier += generateClassifierSpecifications(modelElement);

        sClassifier += "{\n";

        sClassifier += generateClassifierAttributes(modelElement);
        sClassifier += generateClassifierOperations(modelElement);

        sClassifier += "\n} /* end of " + sClassType + " " + sClassName + " */";

        return sClassifier;
    }

    /**
     * Generates tagged value
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateTaggedValue(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateTaggedValue(MTaggedValue modelElement)");

        if (!ModelFacade.isATaggedValue(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, TaggedValue required");
        }

        return "generateTaggedValue(MTaggedValue modelElement)";
    }

    /**
     * Generates association
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateAssociation(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateExtensionPoint(MExtensionPoint modelElement)");

        if (!ModelFacade.isAAssociation(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Association required");
        }

        return "generateAssociation(MAssociation modelElement)";
    }

    /**
     * Generates association end
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateAssociationEnd(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateAssociationEnd(MAssociationEnd modelElement)");

        if (!ModelFacade.isAAssociationEnd(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, AssociationEnd required");
        }

        return "generateAssociationEnd(MAssociationEnd modelElement)";
    }

    /**
     * Genarates multiplicity
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateMultiplicity(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateMultiplicity(MMultiplicity modelElement)");

        if (!ModelFacade.isAMultiplicity(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Multiplicity required");
        }

        return "generateMultiplicity(MMultiplicity modelElement)";
    }

    /**
     * Generates state
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateState(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateState(MState modelElement)");

        if (!ModelFacade.isAState(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, State required");
        }

        return "generateState(MState modelElement)";
    }

    /**
     * Generates transition
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateTransition(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateTransition(MTransition modelElement)");

        if (!ModelFacade.isATransition(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Transition required");
        }

        return "generateTransition(MTransition modelElement)";
    }

    /**
     * Generates action
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateAction(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateAction(Object modelElement)");

        if (!ModelFacade.isAAction(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Action required");
        }

        return "generateAction(Object modelElement)";
    }

    /**
     * Generates guard
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateGuard(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateGuard(MGuard modelElement)");

        if (!ModelFacade.isAGuard(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Guard required");
        }

        return "generateGuard(MGuard modelElement)";
    }

    /**
     * Generates message
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateMessage(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateMessage(MMessage modelElement)");

        if (!ModelFacade.isAMessage(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Message required");
        }

        return "generateMessage(MMessage modelElement)";
    }

    /**
     * Generates visibility
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateVisibility(Object modelElement) {
        if (!ModelFacade.isAVisibilityKind(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, VisibilityKind required");
        }

        if (iLanguageMajorVersion > 4) {
            if (ModelFacade.PUBLIC_VISIBILITYKIND.equals(modelElement)) {
                return "public";
            } else if (ModelFacade.PROTECTED_VISIBILITYKIND
                    .equals(modelElement)) {
                return "protected";
            } else if (ModelFacade.PRIVATE_VISIBILITYKIND
                    .equals(modelElement)) {
                return "private";
            }
        }

        return "";
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

    // --- org.argouml.uml.generator.FileGenerator -----------------------------

    /**
     * Generates file
     *
     * @param modelElement Model element to generate notation for.
     * @param sPath        output base directory
     *
     * @deprecated style break, see issue 2570
     *
     * @return name of generated file on success;
     *         <code>null</code> otherwise.
     *
     * @see org.argouml.uml.generator.FileGenerator#generateFile2(
     * java.lang.Object, java.lang.String)
     */
    public String generateFile2(Object modelElement, String sPath) {
        return generateFile(modelElement, sPath);
    }

    /**
     * Generates file
     *
     * @param modelElement Model element to generate notation for.
     * @param sPath        output base directory
     *
     * @return name of generated file on success;
     *         <code>null</code> otherwise.
     */
    public String generateFile(Object modelElement, String sPath) {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        String sFilename = NameGenerator.generateFilename(modelElement, sPath,
                iLanguageMajorVersion);

        if (sFilename == null || sFilename.length() == 0) {
            LOG.error("Can't generate a nameless class");

            return null;
        }

        File f = new File(sFilename);
        if (f.exists()) {
            LOG.info(getModuleName() + " updates " + f.getPath());
            try {
                updateFile(modelElement, f);
            } catch (Exception exp) {
                LOG.error("Update " + f.getPath() + " failed: "
                        + exp.getMessage());

                return null;
            }

            LOG.debug("Update " + f.getPath() + " successfull");

            return sFilename;
        }

        LOG.info(getModuleName() + " creates " + f.getPath());

        File fPath = new File(sPath);
        if (!fPath.isDirectory()) {
            if (!fPath.mkdirs()) {
                LOG.error(getModuleName() + " could not make directory "
                        + sPath);
                return null;
            }
        }

        if (createFile(modelElement, f)) {
            LOG.debug("Creating " + f.getPath() + " successfull");

            return sFilename;
        } else {
            LOG.error("Creating " + f.getPath() + " failed");

            return null;
        }
    }

    // ----- org.argouml.application.api.ArgoModule ----------------------------

    /**
     * Gets name of this module
     *
     * @return name of this module
     */
    public final String getModuleName() {
        return "Generator" + LANGUAGE_NAME.toUpperCase()
                + iLanguageMajorVersion;
    }

    /**
     * Gets description of this module
     *
     * @return description of this module
     */
    public final String getModuleDescription() {
        return "notation and source code generator for "
                + LANGUAGE_NAME.toUpperCase() + iLanguageMajorVersion;
    }

    /**
     * Gets version of this module
     *
     * @return current version of this module
     */
    public String getModuleVersion() {
        return "0.0.1";
    }

    /**
     * Gets author(s) of this module
     *
     * @return name of author(s) of this module
     */
    public final String getModuleAuthor() {
        return "Kai Schr\u00F6der";
    }

    /**
     * Gets key of this module
     *
     * @return key string to identify this module
     */
    public final String getModuleKey() {
        return "org.argouml.language." + LANGUAGE_NAME.toLowerCase()
                + iLanguageMajorVersion + ".generator";
    }

    // ----- org.argouml.application.api.NotationProvider ----------------------

    /**
     * Generates state body
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateStateBody(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateStateBody(MState modelElement)");

        if (!ModelFacade.isAState(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, State required");
        }

        return "generateStateBody(MState modelElement)";
    }

    /**
     * Generates association role
     *
     * @param modelElement Model element to generate notation for.
     *
     * @return Generated notation for model element.
     */
    public String generateAssociationRole(Object modelElement) {
        // TODO: Auto-generated method stub
        LOG.debug("generateAssociationRole(MAssociationRole modelElement)");

        if (!ModelFacade.isAAssociationRole(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, AssociationRole required");
        }

        return "generateAssociationRole(MAssociationRole modelElement)";
    }

    /**
     * Can this module parse given object?
     *
     * @param obj object to parse
     *
     * @return <code>false</code>
     */
    public boolean canParse(Object obj) {
        // TODO: Auto-generated method stub
        return false;
    }

    /**
     * Can this module parse in general?
     *
     * @return <code>false</code>
     */
    public boolean canParse() {
        // TODO: Auto-generated method stub
        return false;
    }

    // -------------------------------------------------------------------------

    /**
     * Logs module info block to logger
     */
    protected final void logModuleInfo() {
        logModuleInfo(0);
    }

    /**
     * Logs module info block to logger
     *
     * @param iMinWidth Minimum block width.
     */
    protected final void logModuleInfo(int iMinWidth) {
        if (!TM_INFO_BLOCK_LOGGED.containsKey(this.getClass().toString())) {
            StringBuffer sbHeadLine = new StringBuffer("| Module ");
            sbHeadLine.append(getModuleName() + " "
                    + getModuleVersion() + " |");

            StringBuffer sbCopyLine =
                    new StringBuffer("| Copyright (c) 2004, ");
            sbCopyLine.append(getModuleAuthor() + " |");

            StringBuffer sbDescLine = new StringBuffer("| ");
            sbDescLine.append(getModuleDescription() + " |");

            if (sbHeadLine.length() > iMinWidth) {
                iMinWidth = sbHeadLine.length();
            }
            if (sbCopyLine.length() > iMinWidth) {
                iMinWidth = sbCopyLine.length();
            }
            if (sbDescLine.length() > iMinWidth) {
                iMinWidth = sbDescLine.length();
            }

            String sRulerLine = "+";
            for (int i = 1; i <= iMinWidth - 2; i++) sRulerLine += "-";
            sRulerLine += "+";

            String sEmptyLine = "";
            for (int i = 1; i <= iMinWidth; i++) sEmptyLine += " ";

            while (sbHeadLine.length() < iMinWidth) {
                sbHeadLine.insert(sbHeadLine.length() - 2, " ");
                if (sbHeadLine.length() < iMinWidth) {
                    sbHeadLine.insert(2, " ");
                }
            }
            while (sbCopyLine.length() < iMinWidth) {
                sbCopyLine.insert(sbCopyLine.length() - 2, " ");
            }
            while (sbDescLine.length() < iMinWidth) {
                sbDescLine.insert(sbDescLine.length() - 2, " ");
            }

            LOG.info(sEmptyLine);
            LOG.info(sRulerLine);
            LOG.info(sbHeadLine.toString());
            LOG.info(sRulerLine);
            LOG.info(sbCopyLine.toString());
            LOG.info("| " + sEmptyLine.substring(4) + " |");
            LOG.info(sbDescLine.toString());
            LOG.info(sRulerLine);
            LOG.info(sEmptyLine);

            TM_INFO_BLOCK_LOGGED.put(this.getClass().toString(), "true");
        }
    }

    /**
     * Converts a type model element to a PHP type
     *
     * @param modelElement The model element to convert to a PHP type.
     *
     * @return The PHP type converted from the model element.
     */
    protected final String convertType(Object modelElement) {
        String sName = ModelFacade.getName(modelElement).trim();

        if (sName.equals("void"))    return null;

        if (sName.equals("char"))    return "string";

        if (sName.equals("boolean")) return "bool";
        if (sName.equals("bool"))    return "bool";

        if (sName.equals("int"))     return "int";
        if (sName.equals("byte"))    return "int";
        if (sName.equals("short"))   return "int";
        if (sName.equals("long"))    return "int";

        if (sName.equals("float"))   return "float";
        if (sName.equals("double"))  return "float";

        /* user defined type string, not (java.lang.)String */
        if (sName.equals("string"))  return "string";

        /* user defined type array */
        if (sName.equals("array"))   return "array";

        return null;
    }

    /**
     * Generates the default value for a type
     *
     * @param modelElement classifier representing a type
     * @param sDefault     default value
     * @param bCast        prefix value with cast statement
     *
     * @return default value with explicite cast (if needed)
     */
    protected final String generateDefaultValue(Object modelElement,
            String sDefault, boolean bCast) {
        if (modelElement == null) return null;

        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        String sType = convertType(modelElement);
        if (sType == null) {
            return "null";
        } else if (sType.equals("string")) {
            String sReturn = bCast ? "(string) " : "";
            if (sDefault != null) {
                int iFirstApos = sDefault.indexOf("'");
                while (iFirstApos != -1) {
                    sDefault = sDefault.substring(0, iFirstApos)
                            + "\\" + sDefault.substring(iFirstApos);
                    iFirstApos = sDefault.indexOf("'", iFirstApos + 2);
                }
                return sReturn + "'" + sDefault + "'";
            } else {
                return sReturn + "''";
            }
        } else if (sType.equals("bool")) {
            String sReturn = bCast ? "(bool) " : "";
            if (sDefault != null) {
            	sDefault = sDefault.trim();
                if (sDefault.length() > 0) {
		    if (sDefault == "0") {
			return sReturn + "false";
		    } else if (sDefault == "false") {
			return sReturn + "false";
		    } else {
			return sReturn + "true";
		    }
                } else {
                	return sReturn + "false";
                }
            } else {
                return sReturn + "false";
            }
        } else if (sType.equals("int")) {
            String sReturn = bCast ? "(int) " : "";
            if (sDefault != null && sDefault.trim().length() > 0) {
                return sReturn + sDefault.trim();
            } else {
                return sReturn + String.valueOf(0);
            }
        } else if (sType.equals("float")) {
            String sReturn = bCast ? "(float) " : "";
            if (sDefault != null && sDefault.trim().length() > 0) {
                return sReturn + sDefault.trim();
            } else {
                return sReturn + "0.0";
            }
        } else if (sType.equals("array")) {
            if (sDefault != null && sDefault.trim() != "") {
                return "array(" + sDefault + ")";
            } else {
                return "array()";
            }
        }

        return "null";
    }

    /**
     * Generates section for an operation element
     *
     * @param modelElement The model element to generate the section for.
     *
     * @return Generated section code for the model element.
     */
    private final String generateSection(Object modelElement) {
        return generateSection(modelElement, INDENT, null);
    }

    /**
     * Generates section
     *
     * @param modelElement The model element to generate the section for.
     * @param sIndent      String to indent every section block line with.
     * @param sSuffix      Section identifier suffix
     *
     * @return Generated section code for the model element.
     */
    private final String generateSection(Object modelElement, String sIndent,
                                   String sSuffix) {
        String uuid = UUIDHelper.getInstance().getUUID(modelElement);
        if (uuid == null) {
            uuid = (new UID().toString());
            ModelFacade.setUUID(modelElement, uuid);
        }

        if (sSuffix != null && sSuffix.trim() != "") {
            return Section.generate(uuid + "-" + sSuffix.trim(), sIndent);
        } else {
            return Section.generate(uuid, sIndent);
        }
    }

    /**
     * Creates new source file.
     *
     * @param modelElement The class or interface.
     * @param file         The file object to write to.
     *
     * @return <code>true</code> on success,
     *         <code>false</code> otherwise;
     */
    private final boolean createFile(Object modelElement, File file) {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        LOG.info("    Generating new " + file.getPath());

        String sOutput = "<?php\n\n";

        sOutput += "error_reporting(E_ALL);\n\n";

        PHPDocumentor objPHPDoc = null;
        try {
            objPHPDoc = new PHPDocumentor(modelElement,
                    PHPDocumentor.BLOCK_TYPE_FILE);
        } catch (Exception exp) {
            LOG.error("Generating file DocBlock FAILED: "
                    + exp.getMessage());
        } finally {
            if (objPHPDoc != null) {
                try {
                    objPHPDoc.setFilename(NameGenerator
                        .generateFilename(modelElement, iLanguageMajorVersion));
                } catch (Exception exp) {
                    LOG.error("Setting filename for DocBlock FAILED: "
                            + exp.getMessage());
                } finally {
                    sOutput += objPHPDoc.toString() + "\n";
                }
            }
        }

        sOutput += "if (0 > version_compare(PHP_VERSION, '"
                + iLanguageMajorVersion + "')) {\n";
        sOutput += INDENT + "die('This file was generated for PHP "
                + iLanguageMajorVersion + "');\n" + "}\n\n";

        sOutput += generateRequired(modelElement);

        sOutput += "/* user defined includes */\n";
        sOutput += generateSection(modelElement, "", "includes") + "\n";

        sOutput += "/* user defined constants */\n";
        sOutput += generateSection(modelElement, "", "constants") + "\n";

        sOutput += generate(modelElement);
        sOutput += "\n\n?>";

        boolean bReturn = true;
        BufferedWriter bwOutput = null;
        try {
            File parentDir = new File(file.getParent());
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            bwOutput = new BufferedWriter(new FileWriter(file));
            bwOutput.write(sOutput);
        } catch (IOException exp) {
            LOG.error("    Catched IOException: " + exp
                    + ", for file " + file.getPath());
            bReturn = false;
        } finally {
            try {
                if (bwOutput != null) {
                    bwOutput.close();
                }
            } catch (IOException exp) {
                LOG.error("    Catched IOException: " + exp
                        + ", for file " + file.getPath());

                bReturn = false;
            }
        }

        return bReturn;
    }

    /**
     * Updates the output file for a model element.
     *
     * @param modelElement The model element to update file for.
     * @param fileOrig     The original (previous) output file.
     *
     * @throws Exception
     */
    private final void updateFile(Object modelElement, File fileOrig)
        throws Exception {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                + " has wrong object type, Classifier required");
        }

        objSection = new Section();

        File fileNew    = new File(fileOrig.getAbsolutePath() + ".out");
        File fileBackup = new File(fileOrig.getAbsolutePath() + ".bak");

        LOG.debug("    Parsing sections from " + fileOrig.getPath());
        objSection.read(fileOrig.getAbsolutePath());

        if (fileBackup.exists()) {
            LOG.debug("    Delete (old) backup " + fileBackup.getPath());
            fileBackup.delete();
        }

        LOG.debug("    Backup " + fileOrig.getPath() + " to "
                + fileBackup.getPath());
        fileOrig.renameTo(fileBackup);

        if (this.createFile(modelElement, fileOrig)) {
            LOG.debug("    Merging sections into " + fileNew.getPath());
            objSection.write(fileOrig.getAbsolutePath(), INDENT, true);

            LOG.debug("    Renaming " + fileNew.getPath()
                    + " to " + fileOrig.getPath());
            fileOrig.delete();
            fileNew.renameTo(fileOrig);
        } else {
            if (fileBackup.exists()) {
                LOG.debug("    Renaming (restore) " + fileBackup.getPath()
                        + " to " + fileOrig.getPath());
                fileBackup.renameTo(fileOrig);
            }

            LOG.error("    Updating " + fileOrig.getPath() + " failed");
        }
    }

    /**
     * Generates classifier attributes
     *
     * @param modelElement classifier
     *
     * @return source code for class attributes
     */
    private String generateClassifierAttributes(Object modelElement) {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        String sClsAttr = "";

        if (ModelFacade.isAClass(modelElement)) {
            sClsAttr += INDENT + "// --- ATTRIBUTES ---\n";

            Collection colAttributes =
                ModelFacade.getAttributes(modelElement);

            if (colAttributes != null) {
                Iterator itAttributes = colAttributes.iterator();
                while (itAttributes.hasNext()) {
                    Object attr = itAttributes.next();

                    sClsAttr += "\n";

                    PHPDocumentor objPHPDoc = null;
                    try {
                        objPHPDoc = new PHPDocumentor(attr);
                    } catch (Exception exp) {
                        LOG.error("Generating attribute DocBlock FAILED: "
                                + exp.getMessage());
                    } finally {
                        if (objPHPDoc != null) {
                            sClsAttr += objPHPDoc.toString(INDENT);
                        }
                    }

                    sClsAttr += INDENT + generate(attr) + "\n";
                }
            }

            sClsAttr += "\n";
        }

        return sClsAttr;
    }

    /**
     * Generates classifier generalisations
     *
     * @param modelElement classifier
     *
     * @return source code for extends part of class declaration
     */
    private String generateClassifierGeneralisations(Object modelElement) {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        String sClsGen = "";

        Collection colGeneralizations =
            ModelFacade.getGeneralizations(modelElement);
        if (colGeneralizations != null) {
            Iterator itGen = colGeneralizations.iterator();
            if (itGen.hasNext()) {
                if (colGeneralizations.size() == 1) {
                    sClsGen += INDENT + "extends ";
                } else {
                    sClsGen += INDENT + "/* multiple generalisations not"
                            + " supported by PHP: */\n";
                    sClsGen += INDENT + "/* extends ";
                }

                while (itGen.hasNext()) {
                    Object elmGen = ModelFacade.getParent(itGen.next());
                    if (elmGen != null) {
                        sClsGen += NameGenerator.generate(elmGen,
                                iLanguageMajorVersion);
                        if (itGen.hasNext()) {
                            sClsGen += ",\n" + INDENT + "        ";
                        }
                    }
                }

                if (colGeneralizations.size() > 1) {
                    sClsGen += " */";
                }

                sClsGen += "\n";
            }
        }

        return sClsGen;
    }

    /**
     * Generates classifier operations
     *
     * @param modelElement classifier
     *
     * @return source code for all class methods
     */
    private String generateClassifierOperations(Object modelElement) {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        String sClsOp = "";

        sClsOp += INDENT + "// --- OPERATIONS ---\n";

        /* generate constructor */
        Object objTaggedValue =
            ModelFacade.getTaggedValue(modelElement, "constructor");
        if (objTaggedValue != null) {
            String sTaggedValueConstructor =
                ModelFacade.getValueOfTag(objTaggedValue);

            if (sTaggedValueConstructor != null
                    && sTaggedValueConstructor.equals("true")) {
                if (findConstructor(modelElement) == null) {
                    String sConstructor = null;

                    if (iLanguageMajorVersion < 5) {
                        sConstructor = "function " + NameGenerator.generate(
                                modelElement, iLanguageMajorVersion);
                    } else {
                        sConstructor = "public function __construct";
                    }

                    sClsOp += "\n";
                    sClsOp += INDENT + "/**\n";
                    sClsOp += INDENT + " * Class constructor\n";
                    sClsOp += INDENT + " *\n";
                    sClsOp += INDENT + " * @access public\n";
                    sClsOp += INDENT + " *\n";
                    sClsOp += INDENT + " * @return void\n";
                    sClsOp += INDENT + " *\n";
                    sClsOp += INDENT + " * @author ArgoUML "
                            + ArgoVersion.getVersion() + "\n";
                    sClsOp += INDENT + " */\n";

                    sClsOp += INDENT + sConstructor + "()\n";
                    sClsOp += INDENT + "{\n";
                    sClsOp += generateSection(modelElement, INDENT,
                        sConstructor.substring(sConstructor.lastIndexOf(" ")));
                    sClsOp += INDENT + "}\n";
                }
            }
        }

        if (ModelFacade.isAClass(modelElement)) {
            Collection colSpec = ModelFacade.getSpecifications(modelElement);
            if (colSpec != null) {
                Iterator itSpec = colSpec.iterator();
                while (itSpec.hasNext()) {
                    Collection colIfOps =
                        ModelFacade.getOperations(itSpec.next());
                    if (colIfOps != null) {
                        Iterator itIfOps = colIfOps.iterator();
                        while (itIfOps.hasNext()) {
                            Object op = itIfOps.next();
                            sClsOp += "\n";

                            PHPDocumentor objPHPDoc = null;
                            try {
                                objPHPDoc = new PHPDocumentor(op);
                            } catch (Exception exp) {
                                LOG.error("Generating operation DocBlock "
                                        + "FAILED: " + exp.getMessage());
                            } finally {
                                if (objPHPDoc != null) {
                                    sClsOp += objPHPDoc.toString(INDENT);
                                }
                            }

                            sClsOp += INDENT + generate(op);
                            sClsOp += generateMethodBody(op, true);
                        }
                    }
                }
            }
        }

        Collection colOperations = ModelFacade.getOperations(modelElement);
        if (colOperations != null) {
            Iterator itOperations = colOperations.iterator();
            while (itOperations.hasNext()) {
                Object op = itOperations.next();

                sClsOp += "\n";
                PHPDocumentor objPHPDoc = null;
                try {
                    objPHPDoc = new PHPDocumentor(op);
                } catch (Exception exp) {
                    LOG.error("Generating operation DocBlock FAILED: "
                            + exp.getMessage());
                } finally {
                    if (objPHPDoc != null) {
                        sClsOp += objPHPDoc.toString(INDENT);
                    }
                }

                sClsOp += INDENT + generate(op);

                if (ModelFacade.isAClass(modelElement)) {
                    sClsOp += generateMethodBody(op, false);
                } else {
                    if (iLanguageMajorVersion < 5) {
                        sClsOp += "\n" + INDENT + "{\n";
                        sClsOp += INDENT + INDENT
                                + "die('abstract method called');\n";
                        sClsOp += INDENT + "}\n";
                    } else {
                        sClsOp += ";\n";
                    }
                }
            }
        }

        return sClsOp;
    }

    /**
     * Generates classifier specifications
     *
     * @param modelElement The model element to generate specifications for.
     *
     * @return source code for implements part of class declaration
     */
    private String generateClassifierSpecifications(Object modelElement) {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        String sClsSpec = "";

        if (ModelFacade.isAClass(modelElement)) {
            Collection colSpecifications =
                    ModelFacade.getSpecifications(modelElement);
            if (colSpecifications != null) {
                Iterator itSpecifications = colSpecifications.iterator();
                if (itSpecifications.hasNext()) {
                    if (iLanguageMajorVersion < 5) {
                        sClsSpec += INDENT + INDENT + "/* specifications are "
                                + "not supported by PHP versions before 5.0 "
                                + "*/\n";
                    }
                    sClsSpec += INDENT + INDENT;
                    if (iLanguageMajorVersion < 5) {
                        sClsSpec += "/* ";
                    }
                    sClsSpec += "implements ";

                    while (itSpecifications.hasNext()) {
                        Object ifSpecification = itSpecifications.next();
                        sClsSpec += NameGenerator.generate(ifSpecification,
                                iLanguageMajorVersion);

                        if (itSpecifications.hasNext()) {
                            sClsSpec += ",\n" + INDENT + INDENT + "           ";
                        }
                    }

                    if (iLanguageMajorVersion < 5) {
                        sClsSpec += " */";
                    }

                    sClsSpec += "\n";
                }
            }
        }

        return sClsSpec;
    }

    /**
     * Generates single require_once statement for class or interface
     *
     * @param modelElement The required class or interface.
     * @param bAddDocs     Add DocBlock before the require_once statement.
     *
     * @return single require_once statement
     */
    private String generateRequireOnceStatement(Object modelElement,
                                                boolean bAddDocs) {
        String sRequired = "";

        if (bAddDocs) {
            PHPDocumentor objPHPDoc = null;
            try {
                objPHPDoc = new PHPDocumentor(modelElement,
                        PHPDocumentor.BLOCK_TYPE_INCLUDE);
            } catch (Exception exp) {
                LOG.error("Generating include DocBlock FAILED: "
                        + exp.getMessage());
            } finally {
                if (objPHPDoc != null) {
                    sRequired += objPHPDoc.toString();
                }
            }
        }

        String sFilename = NameGenerator.generateFilename(modelElement,
                iLanguageMajorVersion);

        if (FILE_SEPARATOR != "/") {
            int iFirstFS = sFilename.indexOf(FILE_SEPARATOR);
            while (iFirstFS != -1) {
                sFilename = sFilename.substring(0, iFirstFS) + "/"
                        + sFilename.substring(iFirstFS + 1);
                iFirstFS = sFilename.indexOf(FILE_SEPARATOR, iFirstFS + 1);
            }
        }

        sRequired += "require_once('" + sFilename + "');\n";

        return sRequired;
    }

    /**
     * Generates method body for an operation element
     *
     * @param modelElement    Model element to generate body notation for.
     * @param bIgnoreAbstract Ignore abstract to generate implementations of
     *                        abstract methods.
     *
     * @return Generated body notation for model element.
     *
     * TODO: add documentation
     */
    private String generateMethodBody(Object modelElement,
                                      boolean bIgnoreAbstract) {
        if (!ModelFacade.isAOperation(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Operation required");
        }

        String sMethodBody = "";

        if (!ModelFacade.isAbstract(modelElement) || bIgnoreAbstract) {
            sMethodBody += "\n" + INDENT + "{\n";

            Collection colParameters = ModelFacade.getParameters(modelElement);
            if (colParameters != null) {
                Iterator itParameters = colParameters.iterator();
                while (itParameters.hasNext()) {
                    Object objParameter = itParameters.next();
                    if (ModelFacade.isReturn(objParameter)) {
                        String sReturnInit = generateDefaultValue(
                                ModelFacade.getType(objParameter), null, true);
                        String sReturnValue = generate(objParameter);

                        if (sReturnInit != null && sReturnValue.trim() != "") {
                            sMethodBody += INDENT + INDENT + "$returnValue = "
                                    + sReturnInit + ";\n\n";
                        }

                        sMethodBody += generateSection(modelElement);

                        if (sReturnValue != null && sReturnValue != "") {
                            sMethodBody += "\n" + INDENT + INDENT
                                    + sReturnValue + "\n";
                        }

                        sMethodBody += INDENT + "}\n";

                        break;
                    }
                }
            }
        } else {
            if (iLanguageMajorVersion < 5) {
                sMethodBody += "\n" + INDENT + "{\n";
                sMethodBody += INDENT + INDENT
                    + "die('abstract method called');\n";
                sMethodBody += INDENT + "}\n";
            } else {
                sMethodBody += ";\n";
            }
        }

        return sMethodBody;
    }

    /**
     * Generates all required_once statements for the class header
     *
     * @param modelElement the class
     *
     * @return source code for all required_once statements
     *
     * TODO: fix the comparator code
     */
    private String generateRequired(Object modelElement) {
        String sRequired = "";

        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        TreeSet tsRequired = new TreeSet(
            new Comparator() {
                public int compare(Object obj1, Object obj2) {
                    if (obj1 != null) {
                        if (!ModelFacade.isAClassifier(obj1)) {
                            throw new ClassCastException(obj1.getClass()
                                    + " is not comparable as classifier");
                        }
                        if (!ModelFacade.isAClassifier(obj2)) {
                            throw new ClassCastException(obj2.getClass()
                                    + " is not comparable as classifier");
                        }

                        String sFilename1 = NameGenerator.generateFilename(obj1,
                                iLanguageMajorVersion);
                        if (sFilename1 != null) {
                            return sFilename1.compareTo(NameGenerator
                                .generateFilename(obj2, iLanguageMajorVersion));
                        } else {
                            return (NameGenerator.generateFilename(obj2,
                                    iLanguageMajorVersion) != null) ? -1 : 0;
                        }
                    } else {
                        if (obj2 != null) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            );

        /* generalisations */
        Collection colGens = ModelFacade.getGeneralizations(modelElement);
        if (colGens != null) {
            Iterator itGens = colGens.iterator();
            while (itGens.hasNext()) {
                Object objGen = itGens.next();
                tsRequired.add(ModelFacade.getParent(objGen));
            }
        }

        /* association ends */
        Collection colAssocEnds = ModelFacade.getAssociationEnds(modelElement);
        if (colAssocEnds != null) {
            Iterator itAssocEnds = colAssocEnds.iterator();
            while (itAssocEnds.hasNext()) {
                Object assocEnd = itAssocEnds.next();
                Object oppositeEnd = ModelFacade.getOppositeEnd(assocEnd);
                if (ModelFacade.isNavigable(oppositeEnd)) {
                    tsRequired.add(ModelFacade.getType(oppositeEnd));
                }
            }
        }

        /* client dependencies */
        Collection colClientDeps =
                ModelFacade.getClientDependencies(modelElement);
        if (colClientDeps != null) {
            Iterator itClientDeps = colClientDeps.iterator();
            while (itClientDeps.hasNext()) {
                Object dep = itClientDeps.next();
                Collection colDepSuppliers = ModelFacade.getSuppliers(dep);
                Iterator itDepSuppliers = colDepSuppliers.iterator();
                while (itDepSuppliers.hasNext()) {
                    tsRequired.add(itDepSuppliers.next());
                }
            }
        }

        Iterator itRequired = tsRequired.iterator();
        if (itRequired != null) {
            while (itRequired.hasNext()) {
                Object objRequired = itRequired.next();
                if (!objRequired.equals(modelElement)) {
                    sRequired += generateRequireOnceStatement(objRequired,
                            true) + "\n";
                }
            }
        }

        return sRequired;
    }

    /**
     * Finds the model element that represents the class constructor
     *
     * @param modelElement The model element to find constructor for
     *
     * @return The constructor operation element;
     *         <code>null</code> otherwise;
     *
     * TODO: implement the lookup
     */
    private final Object findConstructor(Object modelElement) {
        if (!ModelFacade.isAClass(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Class required");
        }

        return null;
    }

    /**
     * @see org.argouml.application.api.NotationProvider2#generateActionState(java.lang.Object)
     */
    public String generateActionState(Object actionState) {
        return generateState(actionState);
    }
}
