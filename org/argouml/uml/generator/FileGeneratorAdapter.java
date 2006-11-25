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

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * Adapter that implements CodeGeneration using a FileGeneration implementation.
 * When requested to return file names or file contents, it generates all files
 * in a temporary directory and reads them.
 * TODO: Remove this class when all code generators implements the new
 * CodeGenerator interface directly.
 * 
 * @deprecated in 0.23.3 by thn.  Not used in core ArgoUML any more.
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

    /*
     * @see org.argouml.uml.generator.CodeGenerator#generate(
     *      java.util.Collection, boolean)
     */
    public Collection generate(Collection elements, boolean deps) {
        LOG.debug("generate() called");
        File tmpdir = null;
        try {
            tmpdir = TempFileUtils.createTempDir();
            if (tmpdir != null) {
                generateFiles(elements, tmpdir.getPath(), deps);
                return TempFileUtils.readAllFiles(tmpdir);
            }
            return new Vector();
        } finally {
            if (tmpdir != null) {
                TempFileUtils.deleteDir(tmpdir);
            }
            LOG.debug("generate() terminated");
        }
    }

    /*
     * @see org.argouml.uml.generator.CodeGenerator#generateFiles(
     *      java.util.Collection, java.lang.String, boolean)
     */
    public Collection generateFiles(Collection elements, String path,
            boolean deps) {
        LOG.debug("generateFiles() called");
        // TODO: 'deps' is ignored here
        for (Iterator it = elements.iterator(); it.hasNext();) {
            fileGen.generateFile2(it.next(), path);
        }
        return TempFileUtils.readFileNames(new File(path));
    }

    /*
     * @see org.argouml.uml.generator.CodeGenerator#generateFileList(
     *      java.util.Collection, boolean)
     */
    public Collection generateFileList(Collection elements, boolean deps) {
        LOG.debug("generateFileList() called");
        // TODO: 'deps' is ignored here
        File tmpdir = null;
        try {
            tmpdir = TempFileUtils.createTempDir();
            for (Iterator it = elements.iterator(); it.hasNext();) {
                fileGen.generateFile2(it.next(), tmpdir.getName());
            }
            return TempFileUtils.readFileNames(tmpdir);
        } finally {
            if (tmpdir != null) {
                TempFileUtils.deleteDir(tmpdir);
            }
        }
    }




}
