// $Id$
// Copyright (c) 2004-2005 The Regents of the University of California. All
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

package org.argouml.ui;

import javax.swing.JFileChooser;

import org.argouml.util.osdep.OsUtil;
import org.argouml.util.osdep.win32.Win32FileSystemView;

/**
 * Factory class to return a JFileChooser configured
 * according to JRE requirements.
 * TODO Remove when JRE1.3 no longer supported.
 *
 * @author Bob Tarling
 */
public class FileChooserFactory {

    /**
     * Return a proper FileChooser. This replaces the normal FileChooser with a
     * system-dependent one, but solely in case of Sun Java 1.3.1 on Windows.
     *
     * @return <code>JFileChooser</code>
     */
    public static JFileChooser getFileChooser() {
        if (OsUtil.isWin32() && OsUtil.isSunJdk() && OsUtil.isJdk131()) {
            return new JFileChooser(new Win32FileSystemView());
        }
        return new JFileChooser();
    }

    /**
     * Return a proper FileChooser. This replaces the normal FileChooser with a
     * system-dependent one, but solely in case of Sun Java 1.3.1 on Windows.
     *
     * @param directory a <code>String</code> giving the path to a file
     * or directory. Passing in a <code>null</code>
     * string causes the file chooser to point to the user's default directory.
     * This default depends on the operating system. It is
     * typically the "My Documents" folder on Windows, and the user's
     * home directory on Unix.
     *
     * @return <code>JFileChooser</code>
     */
    public static JFileChooser getFileChooser(String directory) {
        if (OsUtil.isWin32() && OsUtil.isSunJdk() && OsUtil.isJdk131()) {
            return new JFileChooser(directory, new Win32FileSystemView());
        }
        return new JFileChooser(directory);
    }
}

