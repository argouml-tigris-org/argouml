// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.util;

import java.io.File;
import javax.swing.filechooser.*;

public class ClassFileFilter extends FileFilter {

    ////////////////////////////////////////////////////////////////
    // instance varaibles

    private final String [] _suffixes = {"class", "jar"};
    private final String    _desc     = "Java classfiles (*.class, *.jar)";


    ////////////////////////////////////////////////////////////////
    // FileFilter API

    /**
     * Check if  file passes the filter.
     *
     * @param f The file to check.
     *
     * @return true, if the file passes the filter, false otherwise.
     */
    public boolean accept(File f) {
	if (f == null) return false;
	if (f.isDirectory()) return true;
	String extension = getExtension(f.getName());
	for (int i = 0; i < _suffixes.length; i++) {
	    if (_suffixes[i].equalsIgnoreCase(extension)) return true;
	}
	return false;
    }

    /**
     * Get the extension of a filename.
     *
     * @param filename The name of the file.
     *
     * @return The extension of the file, or null.
     */
    private String getExtension(String filename) {
	int i = filename.lastIndexOf('.');
	if (i > 0 && i < filename.length() - 1) {
	    return filename.substring(i + 1).toLowerCase();
	}
	return null;
    }

    /**
     * Get a description for this filefilter.
     *
     * @return The description of this filefilter.
     */
    public String getDescription() {
	return _desc;
    }
} /* end class ClassFileFilter */    








