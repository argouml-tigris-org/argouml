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

package org.argouml.language.php.generator;

import org.apache.log4j.Logger;

import org.argouml.model.ModelFacade;

/**
 * Helper class to generate names for model elements in PHP notation
 *
 * @author Kai Schr&ouml;der
 * @since  ArgoUML 0.15.5
 */
public final class NameGenerator {
    /**
     * The log4j logger to log messages to
     */
    private static final Logger LOG = Logger.getLogger(NameGenerator.class);

    /**
     * The fileseperation for this operating system.
     */
    private static final String FILE_SEPARATOR =
            System.getProperty("file.separator");

    // -------------------------------------------------------------------------

    /**
     * Generates the name for a model element
     *
     * @param modelElement  The model element to generates the name for.
     * @param iMajorVersion PHP major version to generate name for.
     *
     * @return The generated name for the given model element.
     *
     * TODO: fix org.argouml.model.ModelFacade#getName
     */
    protected static final String generate(Object modelElement,
        int iMajorVersion) {
        String sModelElementName = "";

        if (ModelFacade.isAPackage(modelElement)) {
            return generatePackageName(modelElement);
        } else if (ModelFacade.isAClassifier(modelElement)) {
            return generateClassifierName(modelElement);
        } else if (ModelFacade.isAAttribute(modelElement)) {
            return generateAttributeName(modelElement, iMajorVersion);
        } else if (ModelFacade.isAOperation(modelElement)) {
            return generateOperationName(modelElement, iMajorVersion);
        } else {
            try {
                sModelElementName = ModelFacade.getName(modelElement);
            } catch (IllegalArgumentException exp) {
                LOG.error("org.argouml.model.ModelFacade#getName"
                        + " needs already a fix");
            }
        }

        return sModelElementName;
    }

    /**
     * Generates the PHP package name for a given namespace, ignoring the root
     * namespace (which is the model).
     *
     * @param modelElement The model element to generate the package name for.
     *
     * @return The PHP package name for the given model element
     */
    public static final String generatePackageName(Object modelElement) {
        if (modelElement == null
                || !ModelFacade.isANamespace(modelElement)
                || ModelFacade.getNamespace(modelElement) == null) {
            return null;
        }

        if (!ModelFacade.isAPackage(modelElement)) {
            modelElement = ModelFacade.getNamespace(modelElement);
            if (ModelFacade.getNamespace(modelElement) == null) {
                return null;
            }
        }

        String sPackagePath = ModelFacade.getName(modelElement);
        while ((modelElement = ModelFacade.getNamespace(modelElement))
                != null) {
            if (ModelFacade.getNamespace(modelElement) != null) {
                sPackagePath = ModelFacade.getName(modelElement) + '_'
                        + sPackagePath;
            }
        }

        return sPackagePath;
    }

    /**
     * Generates the PHP class name for a given classifier, ignoring the root
     * namespace (which is the model).
     *
     * @param modelElement The model element to generate the class name for.
     *
     * @return The PHP class name for the given model element
     */
    public static final String generateClassifierName(Object modelElement) {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        String sName = ModelFacade.getName(modelElement);

        String sPackageName = generatePackageName(modelElement);
        if (sPackageName != null && sPackageName.length() > 0) {
            sName = sPackageName + "_" + sName;
        }

        return sName;
    }

    /**
     * Generates the PHP name for a given method.
     *
     * @param modelElement  The model element to generate the method name for.
     * @param iMajorVersion PHP major version to generate name for.
     *
     * @return The PHP method name for the given model element
     */
    protected static final String generateAttributeName(Object modelElement,
        int iMajorVersion) {
        if (!ModelFacade.isAAttribute(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Attribute required");
        }

        String sAttributeName = ModelFacade.getName(modelElement);

        if (iMajorVersion < 5 && !(ModelFacade.isPublic(modelElement))) {
            return "_" + sAttributeName;
        } else {
            return sAttributeName;
        }

    }

    /**
     * Generates the PHP name for a given method.
     *
     * @param modelElement  The model element to generate the method name for.
     * @param iMajorVersion PHP major version to generate name for.
     *
     * @return The PHP method name for the given model element
     */
    protected static final String generateOperationName(Object modelElement,
        int iMajorVersion) {
        if (!ModelFacade.isAOperation(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Operation required");
        }

        String sOperationName = ModelFacade.getName(modelElement);

        if (iMajorVersion < 5 && !(ModelFacade.isPublic(modelElement))) {
            return "_" + sOperationName;
        } else {
            return sOperationName;
        }
    }

    /**
     * Generates filename for a classifier
     *
     * @param modelElement  The model element to generate the filename for.
     * @param iMajorVersion PHP major version to generate name for.
     *
     * @return The generated filename.
     */
    protected static final String generateFilename(Object modelElement,
        int iMajorVersion) {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        return generateFilename(modelElement, null, iMajorVersion);
    }

    /**
     * Generates filename for a classifier
     *
     * @param modelElement  The model element to generate the filename for.
     * @param sPath         The path to prefix the filename with.
     * @param iMajorVersion PHP major version to generate name for.
     *
     * @return The generated filename.
     */
    protected static final String generateFilename(Object modelElement,
        String sPath, int iMajorVersion) {
        if (!ModelFacade.isAClassifier(modelElement)) {
            throw new ClassCastException(modelElement.getClass()
                    + " has wrong object type, Classifier required");
        }

        String sClassName = NameGenerator.generate(modelElement, iMajorVersion);

        if (sPath != null && sPath.trim().length() > 0) {
            if (!sPath.endsWith(FILE_SEPARATOR)) {
                sPath += FILE_SEPARATOR;
            }
        } else {
            sPath = "";
        }

        String sPackagePath = null;
        int iLastUnderscore = sClassName.lastIndexOf("_");
        if (iLastUnderscore > -1) {
            sPackagePath = sClassName.substring(0, iLastUnderscore);

            int iFirstUnderscore = sPackagePath.indexOf("_");
            while (iFirstUnderscore != -1) {
                sPackagePath = sPackagePath.substring(0, iFirstUnderscore)
                        + FILE_SEPARATOR
                        + sPackagePath.substring(iFirstUnderscore + 1);
                iFirstUnderscore =
                        sPackagePath.indexOf("_", iFirstUnderscore + 1);
            }
        }

        sPath += sPackagePath != null ? sPackagePath + FILE_SEPARATOR : "";

        String sFilename = sPath;
        if (iMajorVersion > 4) {
            if (ModelFacade.isAInterface(modelElement)) {
                sFilename += "interface.";
            } else if (ModelFacade.isAClass(modelElement)) {
                sFilename += "class.";
            } else {
                sFilename += "unknown.";
            }
        }
        if (iLastUnderscore > -1) {
            sFilename += sClassName.substring(iLastUnderscore + 1);
        } else {
            sFilename += sClassName;
        }
        sFilename += ".php";

        return sFilename;
    }
}
