// $Id:SuffixFilter.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * This class handles file extensions.
 *
 */
public class SuffixFilter extends FileFilter {

    ////////////////////////////////////////////////////////////////
    // instance varaibles

    private final String suffix;
    private final String desc;

    ////////////////////////////////////////////////////////////////
    // constructor

    /**
     * The constructor.
     *
     * @param s the suffix string
     * @param d the file type description
     */
    public SuffixFilter(String s, String d) {
	suffix = s;
	desc = d;
    }

    ////////////////////////////////////////////////////////////////
    // FileFilter API

    /*
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
	if (f == null) {
            return false;
        }
	if (f.isDirectory()) {
            return true;
        }
	String extension = getExtension(f);
	if (suffix.equalsIgnoreCase(extension)) {
            return true;
        }
	return false;
    }

    /**
     * @param f the file to get the extension from
     * @return the extension string (without the dot)
     */
    public static String getExtension(File f) {
	if (f == null) {
            return null;
        }
	return getExtension(f.getName());
    }

    /**
     * @param filename the name of the file to get the extension from
     * @return the extension string (without the dot)
     */
    public static String getExtension(String filename) {
	int i = filename.lastIndexOf('.');
	if (i > 0 && i < filename.length() - 1) {
	    return filename.substring(i + 1).toLowerCase();
	}
	return null;
    }

    /*
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
	return desc + " (*." + suffix + ")";
    }

    /**
     * @return Returns the _suffix.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Adding this function enables easy selection of suffixfilters
     * e.g. in a combobox.
     *
     * {@inheritDoc}
     */
    public String toString() {
        return getDescription();
    }

} /* end class SuffixFilter */
