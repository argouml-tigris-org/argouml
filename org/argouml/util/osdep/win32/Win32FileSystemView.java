// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.util.osdep.win32;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.filechooser.FileSystemView;

/**
 * This class is necessary due to an annoying bug on Windows NT where
 * instantiating a JFileChooser with the default FileSystemView will
 * cause a "drive A: not ready" error every time. I grabbed the
 * Windows FileSystemView impl from the 1.3 SDK and modified it so
 * as to not use java.io.File.listRoots() to get fileSystem roots.
 * java.io.File.listRoots() does a SecurityManager.checkRead() which
 * causes the OS to try to access drive A: even when there is no disk,
 * causing an annoying "abort, retry, ignore" popup message every time
 * we instantiate a JFileChooser!
 *
 * Instead of calling listRoots() we use a straightforward alternate
 * method of getting file system roots.
 *
 * @author http://forum.java.sun.com/thread.jsp?forum=38&amp;thread=71610
 * @since ARGO0.9.8
 */
//
// The following code is taken and only slightly modified
// from Sun's java discussion forums.
//
// http://forum.java.sun.com/thread.jsp?forum=38&thread=71610
//
public class Win32FileSystemView extends FileSystemView {

    /**
     * The constructor.
     *
     */
    public Win32FileSystemView() {
        super();
    }

    /**
    * Returns true if the given file is a root.
    *
    * @see javax.swing.filechooser.FileSystemView#isRoot(java.io.File)
    */
    public boolean isRoot(File f) {
        if (!f.isAbsolute()) {
            return false;
        }

        String parentPath = f.getParent();
        if (parentPath == null) {
            return true;
        } else {
            File parent = new File(parentPath);
            return parent.equals(f);
        }
    }

    /**
    * Creates a new folder with a default folder name.
    *
    * @see javax.swing.filechooser.FileSystemView#createNewFolder(java.io.File)
    */
    public File createNewFolder(File containingDir) throws IOException {
        if (containingDir == null) {
            throw new IOException("Containing directory is null:");
        }
        File newFolder = null;
        // Using NT's default folder name
        newFolder = createFileObject(containingDir, "New Folder");
        int i = 2;
        while (newFolder.exists() && (i < 100)) {
            newFolder =
                createFileObject(containingDir,
                                 "New Folder (" + i + ")");
            i++;
        }

        if (newFolder.exists()) {
            throw new IOException("Directory already exists:"
	                          + newFolder.getAbsolutePath());
        } else {
            newFolder.mkdirs();
        }

        return newFolder;
    }

    /**
     * Returns whether a file is hidden or not. On Windows
     * there is currently no way to get this information from
     * io.File, therefore always return false.
     *
     * @see javax.swing.filechooser.FileSystemView#isHiddenFile(java.io.File)
     */
    public boolean isHiddenFile(File f) {
        return false;
    }

    /**
    * Returns all root partitians on this system. On Windows, this
    * will be the A: through Z: drives.
    *
    * Note - This appears to bypass the B drive!  Should
    * we treat the B drive the same as the A drive, or should
    * we continue to bypass it?
    *
    * @see javax.swing.filechooser.FileSystemView#getRoots()
    */
    public File[] getRoots() {

        Vector rootsVector = new Vector();

        // Create the A: drive whether it is mounted or not
        FileSystemRoot floppy = new FileSystemRoot("A" + ":" + File.separator);
        rootsVector.addElement(floppy);

        // Run through all possible mount points and check
        // for their existance.
        for (char c = 'C'; c <= 'Z'; c++) {
            char[] device = {c, ':', File.separatorChar};
            String deviceName = new String(device);
            File deviceFile = new FileSystemRoot(deviceName);
            if (deviceFile != null && deviceFile.exists()) {
                rootsVector.addElement(deviceFile);
            }
        }
        File[] roots = new File[rootsVector.size()];
        rootsVector.copyInto(roots);
        return roots;
    }

    static class FileSystemRoot extends File {
        /**
         * Constructor.
         *
         * @param s The String to create the file from.
         */
        public FileSystemRoot(String s) {
            super(s);
        }

        /*
         * @see java.io.File#isDirectory()
         */
        public boolean isDirectory() {
            return true;
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -4639018030180783658L;
    }
}
