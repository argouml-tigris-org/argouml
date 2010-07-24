/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *    linus - Linus Tolke
 *******************************************************************************
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

package org.argouml.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.server.UID;

/**
 * @stereotype singleton
 */
public final class UUIDManager {
    ////////////////////////////////////////////////////////////////
    // static variables
    /**
     * The singleton instance.
     */
    private static final UUIDManager INSTANCE = new UUIDManager();

    /**
     * The inet address, used in generating UUIDs.
     */
    private InetAddress address;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor for the UUIDManager. This is private to make sure that
     * we are a proper singleton.
     */
    private UUIDManager() {
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // The application should have checked for availability at
            // startup.
            // For tests we would expect unix developers to be correctly
            // configured.
            // Replace with an assert when we are JRE1.4+
            throw new IllegalStateException(
                    "UnknownHostException caught - set up your /etc/hosts");
        }
    }

    /**
     * Return the UUIDManager.
     *
     * @return an UUIDManager
     */
    public static UUIDManager getInstance() {
	return INSTANCE;
    }

    ////////////////////////////////////////////////////////////////
    // public methods

    /**
     * @return the new uuid
     */
    public synchronized String getNewUUID() {
	UID uid = new UID();
	StringBuffer s = new StringBuffer();
	if (address != null) {
	    byte[] b = address.getAddress();
	    for (int i = 0; i < b.length; i++) {
		s.append((long) b[i]).append("-");
	    }
	}
	s.append(uid.toString());
	return s.toString();
    }
} /* end class UUIDManager */
