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

// Import classes fully qualified to ensure that
// no one can short-circuit our intended inheritance.
import java.lang.System;
import java.lang.Class;
import java.lang.ClassLoader;
import java.lang.String;
import java.lang.Exception;
import java.lang.ClassNotFoundException;
import java.lang.SecurityManager;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.io.DataInputStream;


/** Argo custom classloader.
 *
 *  Much of the code that this is based upon is taken from the
 *  O'reilly book <cite>needs-more-work</cite>.
 *
 *  @author Thierry Lach
 *  @since 0.9.4
 */
public final class ArgoClassLoader extends ClassLoader {

    /**
     */
    private JarFile jf = null;

    /**
     */
    public ArgoClassLoader (JarFile jarfile) {
        jf = jarfile;
    }

    /** The worker for this classloader.
     */
    public Class loadClass(String classname) throws ClassNotFoundException {

	try {
            // The standard classloader caches classes.  Look there first.
            Class c = findLoadedClass(classname);

	    // And other standard places.  If a class is loaded with
	    // a classloader, the same classloader will be called
	    // to load all of the superclasses.  These may not be in
	    // the same place as the class is loaded from.
	    if (c == null) {
	        try {
	            c = findSystemClass(classname);
	        }
	        catch (Exception e) { }
	    }
	    // If we still haven't found it, then it must be up to us.
	    if (c == null) {
	        // See if the class is in the jarfile
		JarEntry je = jf.getJarEntry(classname + ".class");
		int entrylength = (int)je.getSize();
		DataInputStream di = new DataInputStream(jf.getInputStream(je));
		byte[] classbytes = new byte[entrylength];
		di.readFully(classbytes);
		di.close();
		c = defineClass(classname, classbytes, 0, entrylength); 
	    }
	    return c;
	}
	catch (Exception e) {
	    throw new ClassNotFoundException(e.toString());
	} 
    }
}

