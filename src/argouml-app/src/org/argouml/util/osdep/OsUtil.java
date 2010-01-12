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

// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.util.osdep;


/** Utility class providing hooks to
 *  operating-system-specific functionality.
 *
 *  @author Thierry Lach
 *  @since ARGO0.9.8
 */

public class OsUtil {

    /** Do not allow this class to be instantiated.
     */
    private OsUtil() {
    }

    /**
     * Check whether we deal with a Windows Operating System.
     *
     * @return true if this is Windows
     */
    public static boolean isWin32() {
        return (System.getProperty("os.name").indexOf("Windows") != -1);
    }

    /**
     * Check whether we deal with a Macintosh.
     *
     * @return true if this is a Mac
     */
    public static boolean isMac() {
        return (System.getProperty("mrj.version") != null);
    }

    /**
     * Check whether this is a Mac running OS X.
     *
     * @return true if this is a Mac running OS X
     */
    public static boolean isMacOSX() {
        return (System.getProperty("os.name").toLowerCase()
                .startsWith("mac os x"));
    }
    
    /**
     * Check whether we deal with a Sun Java.
     *
     * @return true if this is a Sun Java
     */
    public static boolean isSunJdk() {
        return (System.getProperty("java.vendor")
                .equals("Sun Microsystems Inc."));
    }
}
