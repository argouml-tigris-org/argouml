/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thn
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

package org.argouml.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Class with helper methods for user defined profiles.
 * 
 * @author Thomas Neustupny
 */
public class UserDefinedProfileHelper {

    /**
     * Creates a JFileChooser which is appropriate for opening multiple files
     * containing user defined profiles.
     * 
     * @return a JFileChooser
     */
    public static JFileChooser createUserDefinedProfileFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileFilter() {

            public boolean accept(File file) {
                String s = file.getName().toLowerCase();
                return file.isDirectory() || (file.isFile() && (
                    s.endsWith(".xmi") || s.endsWith(".xml")
                    || s.endsWith(".uml") // for AndroMDA
                    || s.endsWith(".xmi.zip") || s.endsWith(".xml.zip")));
            }

            public String getDescription() {
                return "*.xmi *.xml *.xmi.zip *.xml.zip";
            }

        });
        return fileChooser;
    }

    /**
     * Get a list of files from a file array, where the directory entries
     * are recursively resolved by all profile files inside the directory.
     * 
     * @param fileArray array of files
     * @return list of files
     */
    public static List<File> getFileList(File[] fileArray) {
        List<File> files = new ArrayList<File>();
        for (int i = 0; i < fileArray.length; i++) {
            File file = fileArray[i];
            files.addAll(getList(file));
        }
        return files;
    }

    private static List<File> getList(File file) {
        List<File> results = new ArrayList<File>();
        List<File> toDoDirectories = new LinkedList<File>();
        Set<File> seenDirectories = new HashSet<File>();
        toDoDirectories.add(file);
        while (!toDoDirectories.isEmpty()) {
            File curDir = toDoDirectories.remove(0);
            if (!curDir.isDirectory()) {
                // For some reason, this alleged directory is a single file
                // This could be that there is some confusion or just
                // the normal, that a single file was selected and is
                // supposed to be imported.
                results.add(curDir);
                continue;
            }
            // Get the contents of the directory
            File[] files = curDir.listFiles();
            if (files != null) {
                for (File curFile : curDir.listFiles()) {
                    // The following test can cause trouble with
                    // links, because links are accepted as
                    // directories, even if they link files. Links
                    // could also result in infinite loops. For this
                    // reason we don't do this traversing recursively.
                    if (curFile.isDirectory()) {
                        // If this file is a directory
                        if (!seenDirectories.contains(curFile)) {
                            toDoDirectories.add(curFile);
                            seenDirectories.add(curFile);
                        }
                    } else {
                        String s = curFile.getName().toLowerCase();
                        if (s.endsWith(".xmi")
                                || s.endsWith(".uml") // for AndroMDA profiles
                                || s.endsWith(".xmi.zip")
                                || s.endsWith(".xml.zip")) {
                            results.add(curFile);
                        }
                    }
                }
            }
        }
        return results;
    }
}
