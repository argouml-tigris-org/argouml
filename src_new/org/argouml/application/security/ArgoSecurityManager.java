// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.application.security;

// Import the following classes fully qualified to ensure that
// no one can short-circuit our intended inheritance.
import java.lang.ClassLoader;
import java.lang.SecurityManager;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.security.Permission;
import java.lang.Thread;
import java.lang.Class;
import java.lang.ThreadGroup;
import java.lang.Object;
import java.lang.String;
import java.io.FileDescriptor;
import java.net.InetAddress;
// import java.security.GuardedObject;
// import java.security.Guard;

/** The Argo custom security manager.
 *
 *  @author Thierry Lach
 *  @since 0.9.4
 */
public final class ArgoSecurityManager extends SecurityManager
{

    private boolean _allowExit = false;

    private static ArgoSecurityManager SINGLETON = new ArgoSecurityManager();

    public final static ArgoSecurityManager getInstance() {
        return SINGLETON;
    }

    private ArgoSecurityManager() {
    }

    public void checkPermission(Permission perm) {
	if (perm instanceof java.lang.RuntimePermission) {
	    if ("exitVM".equals(perm.getName())) {
		if (! _allowExit) {
		    throw new SecurityException();
		}
	    }
	}
    }

    public boolean getAllowExit() {
        return _allowExit;
    }

    public void setAllowExit(boolean allowExit) {
        _allowExit = allowExit;
    }
}
