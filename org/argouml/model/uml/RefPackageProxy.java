// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;

import javax.jmi.reflect.RefAssociation;
import javax.jmi.reflect.RefBaseObject;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefEnum;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import javax.jmi.reflect.RefStruct;

/**
 * @author Thierry
 */
public class RefPackageProxy extends RefBaseObjectProxy
implements InvocationHandler, RefPackage {

    /**
	 * @param obj
	 */
	public RefPackageProxy(Object obj) {
		super(obj);
	}

	/** Creates a new instance of the proxied object.
     * 
     * @param obj to proxy
     * @return a proxy object if obj does not already implement the interface.
     */
    public static Object newInstance(Object obj)
    {
        Class[] newInterfaces = null;
        if (obj instanceof RefBaseObject) {
            // We don't need to add the interface
            return obj;
        }
        // Get the set of interfaces of the object
        Class[] oldInterfaces = obj.getClass().getInterfaces();
        /*
         * Create an array to contain the old set of interfaces
         * plus the one we are adding
         */
        newInterfaces = new Class[oldInterfaces.length + 2];
        // Copy the old interfaces into the new array
        System.arraycopy(oldInterfaces, 0,
                         newInterfaces, 0, oldInterfaces.length);
        // Add our new interface
		newInterfaces[oldInterfaces.length-1] = RefBaseObject.class;
		newInterfaces[oldInterfaces.length] = RefPackage.class;
        // Now return the proxy
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                                      newInterfaces,
                                      new RefPackageProxy(obj));
    }

	/**
	  * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	  */
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {
		Object result = null;

		System.out.println("method: " + method.getName());

		if (method.getName().equals("refMetaObject")) {
			result = refMetaObject();
		}
		else if (method.getName().equals("refMofId")) {
			result = refMofId();
		}
		else if (method.getName().equals("refImmediatePackage")) {
			result = refImmediatePackage();
		}
		else if (method.getName().equals("refOutermostPackage")) {
			result = refOutermostPackage();
		}
		else {
			super.invoke(proxy, method, args);
		}
		return result;
	}



	/** @see javax.jmi.reflect.RefPackage#refClass(javax.jmi.reflect.RefObject)
	 */
	public RefClass refClass(RefObject arg0) {
		throw new RuntimeException("Not yet implemented");
	}

	/** @see javax.jmi.reflect.RefPackage#refClass(java.lang.String)
	 */
	public RefClass refClass(String arg0) {
		throw new RuntimeException("Not yet implemented");
	}

	/** @see javax.jmi.reflect.RefPackage#refPackage(javax.jmi.reflect.RefObject)
	 */
	public RefPackage refPackage(RefObject arg0) {
		throw new RuntimeException("Not yet implemented");
	}

	/** @see javax.jmi.reflect.RefPackage#refPackage(java.lang.String)
	 */
	public RefPackage refPackage(String arg0) {
		throw new RuntimeException("Not yet implemented");
	}

	/** @see javax.jmi.reflect.RefPackage#refAssociation(javax.jmi.reflect.RefObject)
	 */
	public RefAssociation refAssociation(RefObject arg0) {
		throw new RuntimeException("Not yet implemented");
	}

	/** @see javax.jmi.reflect.RefPackage#refAssociation(java.lang.String)
	 */
	public RefAssociation refAssociation(String arg0) {
		throw new RuntimeException("Not yet implemented");
	}

	/** @see javax.jmi.reflect.RefPackage#refAllPackages()
	 */
	public Collection refAllPackages() {
		throw new RuntimeException("Not yet implemented");
	}

	/** @see javax.jmi.reflect.RefPackage#refAllClasses()
	 */
	public Collection refAllClasses() {
		throw new RuntimeException("Not yet implemented");
	}

	/** @see javax.jmi.reflect.RefPackage#refAllAssociations()
	 */
	public Collection refAllAssociations() {
		throw new RuntimeException("Not yet implemented");
	}

	/** @see javax.jmi.reflect.RefPackage#refCreateStruct(javax.jmi.reflect.RefObject, java.util.List)
	 */
	public RefStruct refCreateStruct(RefObject arg0, List arg1) {
		throw new RuntimeException("Not yet implemented");
	}

	/** @see javax.jmi.reflect.RefPackage#refCreateStruct(java.lang.String, java.util.List)
	 */
	public RefStruct refCreateStruct(String arg0, List arg1) {
		throw new RuntimeException("Not yet implemented");
	}

	/* (non-Javadoc)
	 * @see javax.jmi.reflect.RefPackage#refGetEnum(javax.jmi.reflect.RefObject, java.lang.String)
	 */
	public RefEnum refGetEnum(RefObject arg0, String arg1) {
		throw new RuntimeException("Not yet implemented");
	}

	/* (non-Javadoc)
	 * @see javax.jmi.reflect.RefPackage#refGetEnum(java.lang.String, java.lang.String)
	 */
	public RefEnum refGetEnum(String arg0, String arg1) {
		throw new RuntimeException("Not yet implemented");
	}

	/* (non-Javadoc)
	 * @see javax.jmi.reflect.RefPackage#refDelete()
	 */
	public void refDelete() {
		throw new RuntimeException("Not yet implemented");
	}

}