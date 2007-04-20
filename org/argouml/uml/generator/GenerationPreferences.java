// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.generator;

import org.argouml.application.api.Argo;
import org.argouml.application.configuration.Configuration;

/**
 * Stores generation preference information entered by the user
 * per project. <p>
 *
 * TODO: The header comment is curently not used - this function
 * is not completely implemented yet. How do we store this in the project?
 * Where should the user enter his header comment?
 */
public class GenerationPreferences implements java.io.Serializable {
    ////////////////////////////////////////////////////////////////
    // instance variables
    private String headerComment =
	"Your copyright and other header comments";
    private String outputDir;

    /**
     * Constructor.
     */
    public GenerationPreferences() {
	if (System.getProperty("file.separator").equals("/")) {
	    outputDir = "/tmp";
        } else {
	    //This does not even exist on many systems:
	    //_outputDir = "c:\\temp";
            outputDir = System.getProperty("java.io.tmpdir");
        }
        outputDir = Configuration.getString(
                Argo.KEY_MOST_RECENT_EXPORT_DIRECTORY, outputDir);
    }

    ////////////////////////////////////////////////////////////////
    // accessors
    /**
     * @return the output directory name
     */
    public String getOutputDir() { return outputDir; }

    /**
     * @param od the output directory name
     */
    public void setOutputDir(String od) { 
        outputDir = od;
        Configuration.setString(Argo.KEY_MOST_RECENT_EXPORT_DIRECTORY, od);
    }

    /**
     * @return the header comment string
     */
    public String getHeaderComment() { return headerComment; }

    /**
     * @param c the header comment string
     */
    public void setHeaderComment(String c) { headerComment = c; }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -737447110189956630L;
} /* end class GenerationPreferences */

