// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

// Import the following classes fully qualified to ensure that
// no one can short-circuit our intended inheritance.
import java.util.PropertyPermission;
import java.security.Permission;

import org.apache.log4j.Logger;

/**
 * The Argo custom security manager.
 *
 *  Since Argo is an open-source product, the concept of a
 *  security manager may seem odd.  This class is not intended
 *  to provide security in the standard way that Java programmers
 *  think of, in the context of Applets, for example.
 *
 *  Rather, it is intended to protect Argo from accidental modifications
 *  to its own environment by external modules.
 *
 *  One of the areas this is necessary is to protect from the
 *  {@link java.lang.System#exit(int)} or
 *  {@link java.lang.Runtime#exit(int)} calls.
 *
 *  Another is to prevent modules from replacing the awt exception
 *  trapping hook so that we are able to properly catch any
 *  ArgoSecurityExceptions and prevent the stack trace when
 *  we desire.
 *
 *  @author Thierry Lach
 *  @since 0.9.4
 *  @stereotype singleton
 */
public final class ExitSecurityManager extends SecurityManager
{
    private static final Logger LOG =
	Logger.getLogger(ExitSecurityManager.class);

    /**
     * true if we are allowed to exit.<p>
     *
     * This is set to false from main() just before we start the ball rolling
     * for the GUI.<p>
     *
     * This means that all applications that doesn't start from main i.e.
     * when running JUnit test cases and batch commands, are allowed to
     * exit immediatly.
     */
    private boolean allowExit = true;

    /** The only allowed instance. */
    private static final ExitSecurityManager SINGLETON =
	new ExitSecurityManager();

    /**
     * Accessor for the instance.
     *
     * @return the signleton
     */
    public static final ExitSecurityManager getInstance() {
        return SINGLETON;
    }

    /** Don't allow it to be instantiated from the outside. */
    private ExitSecurityManager() {
    }

    /**
     * @see java.lang.SecurityManager#checkPermission(java.security.Permission)
     */
    public void checkPermission(Permission perm) {
        // TODO:  
	// Don't allow write access to <code>sun.awt.exception.handler</code>
	if (perm.getClass().equals(PropertyPermission.class)) {
	    if ("sun.awt.exception.handler".equals(perm.getName())) {
	        PropertyPermission pp = (PropertyPermission) perm;
		if ("write".equals(pp.getActions())
		    && (!OsUtil.isMac())) {
		    // Don't allow this one to be trapped
		    // by using ArgoSecurityException.
		    LOG.debug("Violating Permission Name: " + pp.getName());
		    throw new SecurityException();
		}
	    }
	}
	// Don't allow anything to exit that we don't know about.
	else if (perm.getClass().equals(java.lang.RuntimePermission.class)) {
	    RuntimePermission rp = (RuntimePermission) perm;
            // Uncomment for more information about what happens...
	    LOG.debug("RuntimePermission: " + rp.getName() 
		      + " - '" + rp.getActions() + "'");
	    if ("exitVM".equals(rp.getName())) {
		if (!getInstance().getAllowExit()) {
		    throw new ExitSecurityException(true);
		}
	    }
	}
    }

    /**
     * @return true if we may exit
     */
    public boolean getAllowExit() {
        return allowExit;
    }

    /**
     * @param myAllowExit true if we may exit
     */
    public void setAllowExit(boolean myAllowExit) {
        allowExit = myAllowExit;
    }
}
