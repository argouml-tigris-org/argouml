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

package org.argouml.api;

import org.argouml.api.model.diagram.DiagramFacade;
import org.argouml.api.model.uml.UmlModelFacade;

/**
 * There is a single FacadeManager that is used to
 * return the different facade object used by ArgoUML.
 * <p>
 * The global UmlModelFacade object can be retrieved using 
 * FacadeManager.getUmlFacade().
 * <p>
 * The global DiagramFacade object can be retrieved using 
 * FacadeManager.getDiagramFacade().
 * <p>
 * The facade objects are created during class initialization
 * and cannot subsequently be changed.
 * <p>
 * At startup the UmlModelFacade class is located using the 
 * org.argouml.model.uml.UmlModelFacade system property.
 * <p>
 * At startup the DiagramFacade class is located using the 
 * org.argouml.model.diagram.DiagramFacade system property.
 */
public final class FacadeManager {

	/** The global UmlModelFacade object */
	private static UmlModelFacade modelFacade = null;
	
	/** The global DiagramFacade object */
	private static DiagramFacade diagramFacade = null;
	
    /** Initialize the global objects.
     * 
     */
	static {
		String cname = null;
		try {
			cname = System.getProperty("org.argouml.api.model.uml.facade");
			if (cname != null && cname.length() > 0) {
				Class clz = ClassLoader.getSystemClassLoader().loadClass(cname);
				modelFacade = (UmlModelFacade) clz.newInstance();
			}
		} catch (Exception ex) {
			System.err.println("Could not load UmlModelFacade '" + cname + "'");
			ex.printStackTrace();
		}
		if (modelFacade == null) {
			try {
				cname = "org.argouml.model.uml.NsumlModelFacade";
				Class clz = ClassLoader.getSystemClassLoader().loadClass(cname);
				modelFacade = (UmlModelFacade) clz.newInstance();
			} catch (Exception ex) {
				System.err.println("Could not load NsumlModelFacade");
				ex.printStackTrace();
			}
		}
		
		try {
			cname = System.getProperty("org.argouml.api.model.diagram.facade");
			if (cname != null && cname.length() > 0) {
				Class clz = ClassLoader.getSystemClassLoader().loadClass(cname);
				diagramFacade = (DiagramFacade) clz.newInstance();
			}
		} catch (Exception ex) {
			System.err.println("Could not load DiagramFacade '" + cname + "'");
			ex.printStackTrace();
		}
		if (diagramFacade == null) {
			try {
				cname = "org.argouml.model.diagram.GefDiagramFacade";
				Class clz = ClassLoader.getSystemClassLoader().loadClass(cname);
				diagramFacade = (DiagramFacade) clz.newInstance();
			} catch (Exception ex) {
				System.err.println("Could not load GefModelFacade");
				ex.printStackTrace();
			}
		}

	}

	/**
	 * Return the global UmlModelFacade object.
	 */
	public static UmlModelFacade getUmlFacade() {
		return modelFacade;
	}

	/**
	 * Return the global DiagramFacade object.
	 */
	public static DiagramFacade getDiagramFacade() {
		return diagramFacade;
	}

    /** Do not allow instantiation */
    private FacadeManager() {
		// Add a shutdown hook to close the global handlers.
		Runtime.getRuntime().addShutdownHook(new ModelFacadeShutdownHook());
    }

	/** 
	 * This is a shutdown hook for the facades.
	 */
	private class ModelFacadeShutdownHook extends Thread {
		public void run() {
			// If the global handlers haven't been initialized yet, we
			// don't want to initialize them just so we can close them!
			synchronized (FacadeManager.this) {
				// Perform anything requiring synchronization here.
			}

			// Do anything else here.
		}
	}


}
