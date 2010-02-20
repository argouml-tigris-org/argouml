/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
 *    linus
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import java.io.IOException;

/**
 * Helper for common File related operations used in automated tests.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class FileHelper {
    
    /**
     * Default temporary directory prefix.
     */
    static final String DEFAULT_TEMP_DIR_PREFIX = "prefix";

    /**
     * Setup a directory with the given name prefix for the caller test.
     * 
     * @param dirNamePrefix the prefix of the directory name to be created in
     *        the system temporary directory.
     * @return the created directory.
     * @throws IOException if the directory creation fails.
     */
    public static File setUpDir4Test(String dirNamePrefix) throws IOException {
        return createTempDirectory(dirNamePrefix);
    }
    
    /**
     * @param testClass the {@link junit.framework.TestCase} class for 
     *        which to create a directory.
     * @return the created directory.
     * @throws IOException if the directory creation fails.
     */
    public static File setUpDir4Test(Class<?> testClass) throws IOException {
        String name = testClass.getPackage().getName() + "." 
            + testClass.getSimpleName();
        return setUpDir4Test(name);
    }
    
    /**
     * Delete fileOrDirectory with the bonus of recursively deleting children
     * of fileOrDirectory if it is a directory.
     * 
     * @param fileOrDirectory the file or directory to be deleted.
     */
    public static void delete(File fileOrDirectory) {
        if (fileOrDirectory != null && fileOrDirectory.exists()) {
            if (fileOrDirectory.isDirectory()) {
                File[] children = fileOrDirectory.listFiles();
                for (File child : children) {
                    delete(child);
                }
            }
            fileOrDirectory.delete();
        }
    }

    /**
     * @param prefix the prefix of the directory name.
     * @return a {@link File} associated to a newly created directory which is
     * contained within the system temporary directory.
     * @throws IOException When the creation of the directory throws.
     */
    public static File createTempDirectory(String prefix) throws IOException {
        File tempFile = File.createTempFile(prefix, "");
        String absolutePath = tempFile.getAbsolutePath();
        if (tempFile.exists()) {
            boolean deleted = tempFile.delete();
            assert deleted : "Deletion of " + tempFile + " failed.";
        }
        boolean created = tempFile.mkdir();
        assert created : "Creation of directory " + tempFile + " failed.";
        tempFile.deleteOnExit();
        return new File(absolutePath);
    }

    /**
     * Create a unique temporary directory contained within the system
     * temporary directory.
     * 
     * @return a {@link File} associated to a newly created directory which is
     * contained within the system temporary directory and with prefix
     * {@link FileHelper#DEFAULT_TEMP_DIR_PREFIX}.
     * @throws IOException When the creation of the directory throws.
     */
    public static File createTempDirectory() throws IOException {
        return createTempDirectory(DEFAULT_TEMP_DIR_PREFIX);
    }

    /**
     * @param fileToMove the original file that must be moved.
     * @param filePrefix the prefix of the new file name.
     * @param fileSuffix the suffix (or extension) of the file.
     *        NOTE: should contain "." if it is intended to be an extension.
     * @param directoryPrefix the prefix of the new directory name.
     * @return the new {@link File}.
     * @throws IOException if any of the file operation throws it.
     */
    public static File moveFileToNewTempDirectory(File fileToMove,
            String filePrefix, String fileSuffix, String directoryPrefix)
        throws IOException {
        File directory = createTempDirectory(directoryPrefix);
        File newFile = File.createTempFile(filePrefix, fileSuffix, directory);
        boolean deleted = newFile.delete();
        assert deleted 
            : "Deletion of newly created file " + newFile + "failed.";
        boolean renamed = fileToMove.renameTo(newFile);
        assert renamed 
            : "Renaming of " + fileToMove + " to " + newFile + " failed.";
        newFile.deleteOnExit();
        return newFile;
    }
}
