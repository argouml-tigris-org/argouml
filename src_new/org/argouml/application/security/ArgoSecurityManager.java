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
