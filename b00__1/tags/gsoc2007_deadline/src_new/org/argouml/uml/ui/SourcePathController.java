// $Id:SourcePathController.java 10734 2006-06-11 15:43:58Z mvw $
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.io.File;
import java.util.Collection;

/**
 * @since 0.17.1
 * @author euluis
 */
public interface SourcePathController {
    /**
     * From a model element retrieve its source path or null if it
     * does not have a direct one (if that happens, you must use
     * recursively its parent or the parent's parent).
     *
     * @param modelElement model element
     * @return The file reference that represents the source file.
     */
    File getSourcePath(final Object modelElement);

    /**
     * Provide the complete list of existing source path settings for a model
     * on request.
     * @return The source path settings table - @see SourcePathTableModel.
     */
    SourcePathTableModel getSourcePathSettings();

    /**
     * Set the source path for a specific model element (package or
     * classifier) based on a given File object.
     *
     * @param modelElement the model element
     * @param sourcePath its source path
     */
    void setSourcePath(Object modelElement, File sourcePath);

    /**
     * Set the source path of the model from the specified settings.
     * @param srcPaths the source path settings to be set in the model. Note,
     * these are used in a resetting way, use the object retrieved with.
     * @see #getSourcePathSettings()
     */
    void setSourcePath(SourcePathTableModel srcPaths);

    /**
     * Delete the source path settings of the model element.
     * @param modelElement the model element for which the source path settings
     * are going to be removed
     */
    void deleteSourcePath(Object modelElement);

    /**
     * Retrieve a collection of all model elements that have source path
     * settings.
     *
     * @return the collection
     */
    Collection getAllModelElementsWithSourcePath();
}
