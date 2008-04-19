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

package org.argouml;

import java.io.File;

/**
 * Helper for common File related operations used in automated tests.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class FileHelper {

    /**
     * System temporary directory property name.
     */
    public static final String SYSPROPNAME_TMPDIR = "java.io.tmpdir";


    public static File getTmpDir() {
        return new File(System.getProperty(SYSPROPNAME_TMPDIR));
    }

    /**
     * Setup a directory with the given name for the caller test.
     * 
     * @param dirName the directory to be created in the system temporary dir
     * @return the created directory
     */
    public static File setUpDir4Test(String dirName) {
        File generationDir = new File(getTmpDir(), dirName);
        generationDir.mkdirs();
        return generationDir;
    }
    
    public static File setUpDir4Test(Class<?> testClass) {
        String name = testClass.getPackage().getName() + "." 
            + testClass.getSimpleName();
        return setUpDir4Test(name);
    }
    
    public static void deleteDir(File dir) {
        if (dir != null && dir.exists()) {
            dir.delete();
        }
    }

}
