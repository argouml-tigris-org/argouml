/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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

/**
 * Information about a source unit and its content, whether
 * it exists only in memory or it's stored in a file.
 * 
 * TODO: Making this an interface instead of a class would allow 
 * more flexibility in dealing with non-file-based resources. - tfm
 * 
 * @author aslo
 */
public class SourceUnit {
    /**
     * The file seperator for this operating system.
     */
    public static final String FILE_SEPARATOR =
        System.getProperty("file.separator");

    private Language language;
    private String name;
    private String basePath;
    private String content;

    /**
     * @param theName Name of the unit.
     * @param path The path relative to the project source path.
     * @param theContent The source code of the unit.
     */
    public SourceUnit(String theName, String path, String theContent) {
        setName(theName);
        setBasePath(path);
        this.content = theContent;
    }

    /**
     * @param fullName Name with path relative to the project source path.
     * @param theContent The source code of the unit.
     */
    public SourceUnit(String fullName, String theContent) {
        setFullName(fullName);
        content = theContent;
    }

    /**
     * @return Returns the source code of the unit.
     */
    public String getContent() {
        return content;
    }

    /**
     * @param theContent The source code for this unit.
     */
    public void setContent(String theContent) {
        this.content = theContent;
    }

    /**
     * @return Returns the file name of this unit, without path.
     */
    public String getName() {
        return name;
    }

    /**
     * @param filename The file name of this unit, without path.
     */
    public void setName(String filename) {
        int sep = filename.lastIndexOf(FILE_SEPARATOR);
        if (sep >= 0) {
            name = filename.substring(sep + FILE_SEPARATOR.length());
        } else {
            name = filename;
        }
    }

    /**
     * @return Returns The base path of the unit (relative to the
     * project source path).
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * @param path The base path of the unit (relative to the
     * project source path).
     */
    public void setBasePath(String path) {
        if (path.endsWith(FILE_SEPARATOR)) {
            basePath =
                path.substring(0, path.length() - FILE_SEPARATOR.length());
        } else {
            basePath = path;
        }
    }

    /**
     * @return Returns The name with path of the unit (relative to the
     * project source path).
     */
    public String getFullName() {
        return basePath + System.getProperty("file.separator") + name;
    }

    /**
     * @param path The full name (with path) of the unit, relative to the
     * project source path.
     */
    public void setFullName(String path) {
        int sep = path.lastIndexOf(FILE_SEPARATOR);
        if (sep >= 0) {
            basePath = path.substring(0, sep);
            name = path.substring(sep + FILE_SEPARATOR.length());
        } else {
            basePath = "";
            name = path;
        }
    }

    /**
     * @return Returns the language.
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * @param lang The language to set.
     */
    public void setLanguage(Language lang) {
        this.language = lang;
    }

}
