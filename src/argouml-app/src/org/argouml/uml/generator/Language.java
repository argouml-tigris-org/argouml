/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    penyaskito
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

import javax.swing.Icon;

/**
 * Encapsulates a programming language name and other properties.
 * 
 * @author Daniele Tamino
 */
public class Language implements Comparable<Language> {

    private String name;
    private String title;
    private Icon icon;

    /**
     * Construct a language instance with all its properties.
     * 
     * @param theName The name of the language.
     * @param theTitle A string representing the language for display.
     * @param theIcon An icon for the language.
     */
    public Language(String theName, String theTitle, Icon theIcon) {
        this.name = theName;
        if (theTitle == null) {
            this.title = theName;
        } else {
            this.title = theTitle;
        }
        this.icon = theIcon;
    }

    /**
     * Creates a language with no icon.
     * 
     * @param theName The name of the language.
     * @param theTitle A string representing the language for display.
     */
    public Language(String theName, String theTitle) {
        this(theName, theTitle, null);
    }

    /**
     * Creates a language with title equal to the name.
     * 
     * @param theName The name of the language.
     * @param theIcon An icon for the language.
     */
    public Language(String theName, Icon theIcon) {
        this(theName, theName, theIcon);
    }

    /**
     * Creates a language with title equal to the name and no icon.
     * 
     * @param theName The name of the language.
     */
    public Language(String theName) {
        this(theName, theName, null);
    }
    
    /**
     * Compares the current object with another object.
     * 
     * @param o The object one compares to
     * @return int
     */
    public int compareTo(Language o) {
        return (this.getName().compareToIgnoreCase(o.getName()));
    }

    /**
     * @return Returns the icon.
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @param theIcon The icon to set.
     */
    public void setIcon(Icon theIcon) {
        this.icon = theIcon;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param theName The name to set.
     */
    public void setName(String theName) {
        this.name = theName;
    }

    /**
     * @return Returns the title, which should be a string representing the
     *         language, in a form suitable for display.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param theTitle A string representing the language, in a form suitable
     *                for display.
     */
    public void setTitle(String theTitle) {
        this.title = theTitle;
    }

    public String toString() {
        String tit = getTitle();
        return tit == null ? "(no name)" : tit;
    }

}
