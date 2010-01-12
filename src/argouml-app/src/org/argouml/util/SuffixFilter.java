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

    private final String[] suffixes;
    private final String desc;

    /**
     * Construct a file filter files with the given suffix and description.
     *
     * @param suffix the suffix string
     * @param d the file type description
     */
    public SuffixFilter(String suffix, String d) {
        suffixes = new String[] {suffix};
	desc = d;
    }

    /**
     * Construct a filter for an array of suffixes
     *
     * @param s the suffixes string
     * @param d the file type description
     */
    public SuffixFilter(String[] s, String d) {
        suffixes = new String[s.length];
        System.arraycopy(s, 0, suffixes, 0, s.length);
        desc = d;
    }
    
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
        for (String suffix : suffixes) {
            if (suffix.equalsIgnoreCase(extension)) {
                return true;
            }
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
        StringBuffer result = new StringBuffer(desc);
        result.append(" (");
        for (int i = 0; i < suffixes.length; i++) {
            result.append('.');
            result.append(suffixes[i]);
            if (i < suffixes.length - 1) {
                result.append(", ");
            }
        }
        result.append(')');
        return result.toString();
    }

    /**
     * @return Returns the default or preferred suffix for this type of file.
     */
    public String getSuffix() {
        return suffixes[0];
    }

    /**
     * @return Returns the list of all acceptable suffixes.
     */
    public String[] getSuffixes() {
        return suffixes;
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

}
