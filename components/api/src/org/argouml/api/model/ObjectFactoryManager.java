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

package org.argouml.api.model;

import org.argouml.api.model.uml.UmlObjectFactory;

/**
 * There is a single ObjectFactoryManager that is used to
 * return the different model factory objects used by ArgoUML.
 * <p>
 * The global UmlObjectFactory object can be retrieved using 
 * ObjectFactoryManager.getUmlFactory()().
 * <p>
 * The object factory objects are created during class initialization
 * and cannot subsequently be changed.
 * <p>
 * At startup the UmlObjectFactory class is located using the 
 * org.argouml.model.uml.UmlObjectFactory system property.
 */
public final class ObjectFactoryManager {

	/** The global UmlObjectFactory object */
	private static UmlObjectFactory umlObjectFactory = null;
	
    /** Initialize the global objects.
     * 
     */
	static {
		String cname = null;
		try {
			cname = System.getProperty("org.argouml.api.model.uml.UmlObjectFactory");
			if (cname != null && cname.length() > 0) {
				Class clz = ClassLoader.getSystemClassLoader().loadClass(cname);
				umlObjectFactory = (UmlObjectFactory) clz.newInstance();
			}
		} catch (Exception ex) {
			System.err.println("Could not load UmlObjectFactory '" + cname + "'");
			ex.printStackTrace();
		}
		if (umlObjectFactory == null) {
			try {
				cname = "org.argouml.model.uml.NsumlObjectFactory";
				Class clz = ClassLoader.getSystemClassLoader().loadClass(cname);
				umlObjectFactory = (UmlObjectFactory) clz.newInstance();
			} catch (Exception ex) {
				System.err.println("Could not load NsumlObjectFactory");
				ex.printStackTrace();
			}
		}
		
	}

	/**
	 * Return the global UmlObjectFactory object.
	 */
	public static UmlObjectFactory getUmlFactory() {
		return umlObjectFactory;
	}

    /** Do not allow instantiation */
    private ObjectFactoryManager() {
		// Add a shutdown hook to close the global handlers.
		Runtime.getRuntime().addShutdownHook(new ObjectFactoryShutdownHook());
    }

	/** 
	 * This is a shutdown hook for the factories.
	 */
	private class ObjectFactoryShutdownHook extends Thread {
		public void run() {
			// If the global handlers haven't been initialized yet, we
			// don't want to initialize them just so we can close them!
			synchronized (ObjectFactoryManager.this) {
				// Perform anything requiring synchronization here.
			}

			// Do anything else here.
		}
	}


}
