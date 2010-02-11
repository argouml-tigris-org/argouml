/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class provides the base URL for user defined profiles.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class UserProfileReference extends ProfileReference {

    /**
     * The default user profile base URL, which will be used if no explicit URL
     * is specified.
     */
    public static final String DEFAULT_USER_PROFILE_BASE_URL = 
        "http://argouml.org/user-profiles/";

    /**
     * Constructor that simply delegates to super.
     * 
     * @param thePath see thePath documentation in 
     * {@link ProfileReference#ProfileReference(String, URL)}.
     * @param publicReference see publicReference documentation in 
     * {@link ProfileReference#ProfileReference(String, URL)}.
     */
    public UserProfileReference(String thePath, URL publicReference) {
        super(thePath, publicReference);
    }

    /**
     * Constructor, which builds a ProfileReference for a user defined profile 
     * by prefixing the fileName with {@link #DEFAULT_USER_PROFILE_BASE_URL} 
     * and using this as the publicReference.
     * 
     * @param path the profile absolute file name.
     * @throws MalformedURLException if the built URL is incorrect.
     */
    public UserProfileReference(String path) throws MalformedURLException {
        super(path, 
            new URL(DEFAULT_USER_PROFILE_BASE_URL + new File(path).getName()));
    }
}
