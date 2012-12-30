/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2008 The Regents of the University of California. All
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

package org.argouml.uml.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class with methods to manage files in the temporary directory.
 */
public class TempFileUtils {

    private static final Logger LOG =
        Logger.getLogger(TempFileUtils.class.getName());

    /**
     * Create a temporary directory.
     *
     * @return a newly created, empty, temporary directory
     */
    public static File createTempDir() {
        File tmpdir = null;
        try  {
            tmpdir = File.createTempFile("argouml", null);
            tmpdir.delete();
            if (!tmpdir.mkdir()) {
                return null;
            }
            return tmpdir;
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, "Error while creating a temporary directory", ioe);
            return null;
        }
    }

    private interface FileAction {
        /**
         * Execute some action on the specified file.
         *
         * @param file the file on which to perform the action
         * @throws IOException
         */
        void act(File file) throws IOException;
    }

    /**
     * Visit directory in post-order fashion.
     */
    private static void traverseDir(File dir, FileAction action)
        throws IOException {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    traverseDir(files[i], action);
                } else {
                    action.act(files[i]);
                }
            }
            action.act(dir);
        }
    }

    /**
     * Reads all files in a directory in memory.
     * @param dir directory to read files from
     * @return A collection of SourceUnit objects.
     */
    public static Collection<SourceUnit> readAllFiles(File dir) {
        try {
            final List<SourceUnit> ret = new ArrayList<SourceUnit>();
            final int prefix = dir.getPath().length() + 1;
            traverseDir(dir, new FileAction() {

                public void act(File f) throws IOException {
                    // skip backup files. This is actually a workaround for the
                    // cpp generator, which always creates backup files (it's a
                    // bug).
                    if (!f.isDirectory() && !f.getName().endsWith(".bak")) {
                        // TODO: This is using the default platform character
                        // encoding.  Specifying an encoding will produce more
                        // predictable results
                        FileReader fr = new FileReader(f);
                        BufferedReader bfr = new BufferedReader(fr);
                        try {
                            StringBuffer result =
                                new StringBuffer((int) f.length());
                            String line = bfr.readLine();
                            do {
                                result.append(line);
                                line = bfr.readLine();
                                if (line != null) {
                                    result.append('\n');
                                }
                            } while (line != null);
                            ret.add(new SourceUnit(f.toString().substring(
                                    prefix), result.toString()));
                        } finally {
                            bfr.close();
                            fr.close();
                        }
                    }
                }

            });
            return ret;
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, "Exception reading files", ioe);
        }
        return null;
    }

    /**
     * Deletes a directory and all of its contents.
     * @param dir The directory to delete.
     */
    public static void deleteDir(File dir) {
        try {
            traverseDir(dir, new FileAction() {
                public void act(File f) {
                    f.delete();
                }
            });
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, "Exception deleting directory", ioe);
        }
    }

    /**
     * Reads all the files within a directory tree.
     * @param dir The base directory.
     * @return The collection of files.
     */
    public static Collection<String> readFileNames(File dir) {
        final List<String> ret = new ArrayList<String>();
        final int prefix = dir.getPath().length() + 1;
        try {
            traverseDir(dir, new FileAction() {
                public void act(File f) {
                    if (!f.isDirectory()) {
                        ret.add(f.toString().substring(prefix));
                    }
                }
            });
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, "Exception reading file names", ioe);
        }
        return ret;
    }

}
