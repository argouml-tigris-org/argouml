// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;

/**
 * To persist to and from zargo (zipped file) storage.
 *
 * @author Bob Tarling
 */
public abstract class AbstractFilePersister extends FileFilter
        implements ProjectFilePersister {

    private static final Logger LOG =
        Logger.getLogger(AbstractFilePersister.class);

    /**
     * The PERSISTENCE_VERSION is increased every time the persistence format
     * changes.
     * This controls conversion of old persistence version files to be
     * converted to the current one, keeping ArgoUML backwards compatible.
     */
    protected static final int PERSISTENCE_VERSION = 3;
    
    /**
     * Supply the encoding to be used throughout the persistence
     * mechanism.
     * @return the encoding.
     */
    protected String getEncoding() {
        return "UTF-8";
    }

    /**
     * Create a temporary copy of the existing file.
     * @param file the file to copy.
     * @return the temp file or null if none copied.
     * @throws FileNotFoundException if file not found
     * @throws IOException if error reading or writing
     */
    protected File createTempFile(File file)
        throws FileNotFoundException, IOException {
        File tempFile = new File(file.getAbsolutePath() + "#");
        
        if (tempFile.exists()) {
            tempFile.delete();
        }
        
        if (file.exists()) {
            copyFile(file, tempFile);
        }
        
        return tempFile;
    }
    
    /**
     * Copies one file src to another, raising file exceptions
     * if there are some problems.
     *
     * @param dest The destination file.
     * @param src The source file.
     * @return The destination file after successful copying.
     * @throws IOException if there is some problems with the files.
     * @throws FileNotFoundException if any of the files cannot be found.
     */
    private File copyFile(File src, File dest)
        throws FileNotFoundException, IOException {

        FileInputStream fis  = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] buf = new byte[1024];
        int i = 0;
        while ((i = fis.read(buf)) != -1) {
            fos.write(buf, 0, i);
        }
        fis.close();
        fos.close();

        dest.setLastModified(src.lastModified());

        return dest;
    }



    ////////////////////////////////////////////////////////////////
    // FileFilter API

    /**
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
        if (f == null) return false;
        if (f.isDirectory()) return true;
        String s = getExtension(f);
        if (s != null) // this check for files without extension...
            if (s.equalsIgnoreCase(getExtension())) return true;
        return false;
    }

    /**
     * The extension valid for this type of file.
     * (Just the chars, not the dot: e.g. "zargo".)
     *
     * @return the extension valid for this type of file
     */
    public abstract String getExtension();

    /**
     * (Just the description, not the extension between "()".)
     *
     * @return the description valid for this type of file
     */
    protected abstract String getDesc();

    private static String getExtension(File f) {
        if (f == null) return null;
        return getExtension(f.getName());
    }

    private static String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1) {
            return filename.substring(i + 1).toLowerCase();
        }
        return null;
    }

    /**
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
        return getDesc() + " (*." + getExtension() + ")";
    }

    /**
     * Save a project to file.<p>
     * This first archives the existing file, then calls
     * doSave(...) to do the actual saving.<p>
     * Should doSave(...) throw an exception then it is
     * caught here and any rollback handled before rethrowing
     * the exception.
     *
     * @param project The project being saved.
     * @param file The file to which the save is taking place.
     * @throws SaveException when anything goes wrong
     *
     * @see org.argouml.persistence.ProjectFilePersister#save(
     * org.argouml.kernel.Project, java.io.File)
     */
    public final void save(Project project, File file) throws SaveException {
        preSave(project, file);
        doSave(project, file);
        postSave(project, file);
    }

    /**
     * Handle archiving of previous file or any other common
     * requirements before saving a model to a file.
     *
     * @param project The project being saved.
     * @param file The file to which the save is taking place.
     * @throws SaveException when anything goes wrong
     */
    private void preSave(Project project, File file) throws SaveException {

    }

    /**
     * Handle archiving on completion of a save such as renaming
     * the temporary save file to the real filename.
     *
     * @param project The project being saved.
     * @param file The file to which the save is taking place.
     * @throws SaveException when anything goes wrong
     */
    private void postSave(Project project, File file) throws SaveException {

    }

    /**
     * Handle any common requirements on detection of a save error.
     * Such as restoring the archive. This method is called should
     * the concrete implementation of doSave(...) throw a
     * SaveException.
     *
     * @param project The project being saved.
     * @param file The file to which the save is taking place.
     * @param e The original exception that triggered a call to
     *          this method.
     * @throws SaveException if anything goes wrong in trying to
     *                       restore the previous archive.
     */
    private void postSaveFailure(Project project, File file, SaveException e)
        throws SaveException {

    }

    /**
     * Implement in your concrete class to save a project to a
     * file.<p>
     * There is no need to worry about archiving or restoring
     * archive on failure, that is handled by the rest of the
     * framework.<p>
     *
     * @param project the project to save
     * @param file The file to write.
     * @throws SaveException when anything goes wrong
     *
     * @see org.argouml.persistence.AbstractFilePersister#save(
     * org.argouml.kernel.Project, java.io.File)
     */
    protected abstract void doSave(Project project, File file)
        throws SaveException;

    /**
     * @see org.argouml.persistence.ProjectFilePersister#loadProject(File, JProgressBar)
     */
    public Project doLoad(File file, JProgressBar progressBar) throws OpenException {
        try {
            return doLoad(file.toURL(), progressBar);
        } catch (MalformedURLException e) {
            LOG.error("MalformedURLException", e);
            throw new OpenException(e);
        }
    }
}
