// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.argouml.kernel.Project;
import org.argouml.swingext.ProgressMonitor;
import org.argouml.util.SuffixFilter;

/**
 * An interface which identifies an ArgoUML plug-in which imports 
 * source language modules and creates UML model elements in our model.
 * 
 * @author Tom Morris
 * @since 0.23.2
 */

public interface ImportInterface {

    /**
     * The name of the TagDefinition which types the TaggedValues used store the
     * source path of a ModelElement. Used for round trip engineering purposes.
     * Set during reverse engineering and used during code generation.
     */
    public static final String SOURCE_PATH_TAG = "src_path";

    /**
     * The name of the TagDefinition which types the TaggedValues used store
     * information about a ModelElement which can't be stored in the model. Used
     * for round trip engineering purposes. Set during reverse engineering and
     * used during code generation.
     */
    public static final String SOURCE_MODIFIERS_TAG = "src_modifiers";

    /**
     * Provides an array of suffix filters for the module.
     * 
     * @return SuffixFilter[] suffixes for processing
     */
    SuffixFilter[] getSuffixFilters();

    /**
     * Tells if the object is parseable or not.  It's is up to the module
     * to decide whether it does something simple like verify that the file
     * has the correct extension, or something more complicated.
     * 
     * @param file
     *            object to be tested.
     * @return true if parseable, false if not.
     */
    boolean isParseable(File file);

    /**
     * Parse a collection of source files. The collection includes the full set
     * of files selected by the user.
     * <p>
     * If the import module needs multiple parsing passes to resolve identifiers
     * or for other reasons it needs to implement that internally. In previous
     * versions of ArgoUML the multipass behavior was implemented both in the
     * calling code and in some import modules. It is now solely the
     * responsibility of the module.
     * 
     * @param p
     *            the current project
     * @param files
     *            Collection of files to be parsed
     * @param settings
     *            Use this object to get common settings.
     * @param monitor
     *            ProgressMonitor which will be updated as files are parsed and
     *            checked for user requests to cancel. It is mandatory for the
     *            module to both update progress and check for cancel requests.
     * @return a collection of model elements parsed from the given files
     * @throws ImportException
     *             if an error occurs, this will contain the nested exception
     *             that was originally thrown
     */
    Collection parseFiles(Project p, final Collection files,
            ImportSettings settings, ProgressMonitor monitor)
        throws ImportException;

    /**
     * Returns a list with objects that represent settings for this import.
     * These objects implement the ImportTypes.* interfaces.
     * <p>
     * The caller must determine what interface an object is implementing
     * iterating the interfaces ImportTypes.*
     * <p>
     * This is done this way to eliminate the need to use GUI elements. The
     * settings can easily be mapped into any GUI elements, this way we are
     * independent from the type of GUI.
     * 
     * @return the list of settings that are required by this particular import
     */
    List getImportSettings();
    
    
    /**
     * Import subsystem exception to wrap any nested exceptions with when
     * thrown.
     */
    public class ImportException extends Exception {

        public ImportException(String message, Throwable cause) {
            super("Import Exception : " + message, cause);
        }
        
        public ImportException(String message) {
            super(message);
        }

        public ImportException(Throwable cause) {
            super("Import Exception", cause);
        }
        
    }

}
