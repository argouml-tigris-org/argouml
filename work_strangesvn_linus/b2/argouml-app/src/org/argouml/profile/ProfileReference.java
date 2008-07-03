// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.profile;

import java.io.File;
import java.net.URL;

/**
 * A reference to a Profile which internally implies having 
 * both a public profile reference and the path to the profile file. 
 * 
 * @author Luis Sergio Oliveira (euluis)
 */
public class ProfileReference {

    private String path;
    private URL url;

    /**
     * Constructor. Note that this checks if the file name in path and in 
     * publicReference are the same.
     * 
     * @param thePath the system path to the profile file.
     * @param publicReference see {@link #getPublicReference()}.
     */
    public ProfileReference(String thePath, URL publicReference) {
        File file = new File(thePath);
        File fileFromPublicReference = new File(publicReference.getPath());
        assert file.getName().equals(fileFromPublicReference.getName()) 
            : "File name in path and in publicReference are different.";
        path = thePath;
        url = publicReference;
    }

    /**
     * @return the path to the profile, being in principle this path the 
     * system path to the profile file.
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the public reference by which the profile will 
     * be known in models that depend on it. I.e., this reference will prefix 
     * the IDs of the profile model elements referred in the XMI of models 
     * that depend on the profile for which the constructed ProfileReference 
     * is used.
     */
    public URL getPublicReference() {
        return url;
    }

}
