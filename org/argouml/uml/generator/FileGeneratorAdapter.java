// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Adapter that implements CodeGeneration using a FileGeneration implementation.
 * When requested to return file names or file contents, it generates all files
 * in a temporary directory and reads them.
 * TODO: Remove this class when all code generators implements the new
 * CodeGenerator interface directly.
 *
 * @author Daniele Tamino
 */
public class FileGeneratorAdapter implements CodeGenerator {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(FileGeneratorAdapter.class);

    private FileGenerator fileGen;

    /**
     * @param fg The FileGenerator to wrap.
     */
    public FileGeneratorAdapter(FileGenerator fg) {
        fileGen = fg;
        LOG.debug("Wrapping " + fg + " info FileGeneratorAdapter");
    }

    /**
     * @see org.argouml.uml.generator.CodeGenerator#generate(java.util.Collection, boolean)
     */
    public Collection generate(Collection elements, boolean deps) {
        LOG.debug("generate() called");
        File tmpdir = null;
        try {
            tmpdir = createTempDir();
            if (tmpdir != null) {
                generateFiles(elements, tmpdir.getPath(), deps);
                return readAllFiles(tmpdir);
            }
            return new Vector();
        } finally {
            if (tmpdir != null) {
                deleteDir(tmpdir);
            }
            LOG.debug("generate() terminated");
        }
    }

    /**
     * @see org.argouml.uml.generator.CodeGenerator#generateFiles(java.util.Collection,
     *      java.lang.String, boolean)
     */
    public Collection generateFiles(Collection elements, String path,
            boolean deps) {
        LOG.debug("generateFiles() called");
        // TODO: 'deps' is ignored here
        for (Iterator it = elements.iterator(); it.hasNext();) {
            fileGen.generateFile2(it.next(), path);
        }
        return readFileNames(new File(path));
    }

    /**
     * @see org.argouml.uml.generator.CodeGenerator#generateFileList(java.util.Collection, boolean)
     */
    public Collection generateFileList(Collection elements, boolean deps) {
        LOG.debug("generateFileList() called");
        // TODO: 'deps' is ignored here
        File tmpdir = null;
        try {
            tmpdir = createTempDir();
            for (Iterator it = elements.iterator(); it.hasNext();) {
                fileGen.generateFile2(it.next(), tmpdir.getName());
            }
            return readFileNames(tmpdir);
        } finally {
            if (tmpdir != null) {
                deleteDir(tmpdir);
            }
        }
    }

    // methods to manage files in the temporary directory

    private File createTempDir() {
        File tmpdir = null;
        try  {
            tmpdir = File.createTempFile("argouml", null);
            tmpdir.delete();
            if (!tmpdir.mkdir()) {
                return null;
            }
            return tmpdir;
        } catch (IOException ioe) {
            LOG.error("Error while creating a temporary directory", ioe);
            return null;
        }
    }

    private interface FileAction {
        /**
         * Execute some action on the specified file.
         */
        void act(File f) throws IOException;
    }

    /**
     * Visit directory in post-order fashion.
     */
    private void traverseDir(File dir, FileAction action) throws IOException {
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
     * @param dir
     * @return A collection of SourceUnit objects.
     */
    private Collection readAllFiles(File dir) {
        try {
            final Vector ret = new Vector();
            final int prefix = dir.getPath().length() + 1;
            traverseDir(dir, new FileAction() {

                public void act(File f) throws IOException {
                    // skip backup files. This is actually a workaround for the
                    // cpp generator, which always creates backup files (it's a
                    // bug).
                    if (!f.isDirectory() && !f.getName().endsWith(".bak")) {
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
            LOG.error("Exception reading files", ioe);
        }
        return null;
    }

    /**
     * Deletes a directory and all of its contents.
     * @param dir The directory to delete.
     */
    private void deleteDir(File dir) {
        try {
            traverseDir(dir, new FileAction() {
                public void act(File f) {
                    f.delete();
                }
            });
        } catch (IOException ioe) {
            // never happens, just to keep the compiler happy
        }
    }

    /**
     * Reads all the files within a directory tree.
     * @param dir The base directory.
     * @return The collection of files.
     */
    private Collection readFileNames(File dir) {
        final List ret = new Vector();
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
            // never happens, just to keep the compiler happy
        }
        return ret;
    }

}
