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

// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.profile;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class provides the base referencing and URL for ArgoUML core profiles.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class CoreProfileReference extends ProfileReference {
    
    static String PROFILES_RESOURCE_PATH = 
        "/org/argouml/profile/profiles/uml14/";
    
    static String PROFILES_BASE_URL = 
        "http://argouml.org/profiles/uml14/";

    /**
     * Constructor, which builds a ProfileReference for ArgoUML core profiles 
     * by: 
     * <li>prefixing the fileName with {@link #PROFILES_RESOURCE_PATH} and 
     * using this as the path;</li>
     * <li>and prefixing the fileName with {@link #PROFILES_BASE_URL} and 
     * using this as the publicReference.</li>
     * 
     * @param fileName the profile file name.
     * @throws MalformedURLException if the built URL is incorrect.
     */
    public CoreProfileReference(String fileName) throws MalformedURLException {
        super(PROFILES_RESOURCE_PATH + fileName, 
            new URL(PROFILES_BASE_URL + fileName));
        assert fileName != null 
            : "null isn't acceptable as the profile file name.";
        assert !"".equals(fileName)
        : "the empty string isn't acceptable as the profile file name.";
    }
    
    /**
     * Sets the directory name where the profiles can be found. Initially,
     * 'uml14' is used. TODO: Intermediate solution for UML 2.x support,
     * please implement a better solution.
     * 
     * @param dir Name of the directory for the profiles
     */
    public static void setProfileDirectory(String dir) {
        PROFILES_RESOURCE_PATH = "/org/argouml/profile/profiles/" + dir + '/';
        PROFILES_BASE_URL = "http://argouml.org/profiles/" + dir + '/';
    }
}
