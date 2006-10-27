// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.reveng;

/**
 * Interface for the generic import settings that a pluggable importer module
 * can query the value of.
 */

public interface ImportSettings {

    /**
     * Import only classifiers
     */
    public static final int DETAIL_CLASSIFIER = 0;

    /**
     * Import classifiers and their features
     */
    public static final int DETAIL_CLASSIFIER_FEATURE = 1;

    /**
     * Import full detail
     */
    public static final int DETAIL_FULL = 2;

    /**
     * @return the level of import detail requested by the user. One of
     *         DETAIL_CLASSIFIER, DETAIL_CLASSIFIER_FEATURE, or DETAIL_FULL.
     */
    public int getImportLevel();

    /**
     * @return string representing the character encoding of the input source
     *         files.
     */
    public String getInputSourceEncoding();

    // TODO: Change attribute and datatype to return a literal/enum
    // instead? - tfm
    public static final int ASSOCIATION_AS_ATTRIBUTE = 1;

    public static final int ASSOCIATION_AS_ASSOCIATION = 0;

    public static final int ARRAY_AS_DATATYPE = 1;

    public static final int ARRAY_AS_ARRAY = 0;

    /**
     * @return true if associations should be modeled as attributes
     */
    public boolean isAttributeSelected();

    /**
     * @return true if arrays should be modeled as UML Datatypes
     */
    public boolean isDatatypeSelected();

    /**
     * @return true if the directory tree should be descended recursively
     *         importing all parseable files.
     */
    public boolean isDescendSelected();

    /**
     * @return true if user as requested that only sources files which have been
     *         changed since the last import should be imported this time. If
     *         false, all files should be imported, regardless of their
     *         modification date.
     */
    public boolean isChangedOnlySelected();

    // boolean isCreateDiagramsSelected();
    // boolean isMinimizeFigsSelected();
    // boolean isLayoutDiagramsSelected();

    /**
     * @deprecated by tfmorris for 0.23.3 - only for use by old code
     * @return the interface to the diagram subsystem
     */
    DiagramInterface getDiagramInterface();

    /**
     * @deprecated only to be used by Java and IDL until they are upgraded.
     * @return the current import session
     */
    Import getImportSession();

}
