// $Id$
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


/*
 * ArgoModule.java
 *
 * Created on June 12, 2001, 6:47 AM
 */

package org.argouml.application.api;
import org.argouml.application.modules.*;

import java.util.Vector;

import org.apache.log4j.*;

/** Interface that defines the characteristics of an external
 *  module usable by Argo.
 *
 * @author  Will Howery
 * @author  Thierry Lach
 * @since 0.9.4
 */
public interface ArgoModule { 

    /** Define a static log4j category variable for ArgoUML configuration.
     */
    public final static Logger cat = 
	Logger.getLogger("org.argouml.application.modules");
    // TODO:  JDK 1.2 seems to not return the package name if
    // not running from a jar.
    //
    // public final static Category cat = 
    // Category.getInstance(ModuleLoader.class.getPackage().getName());


    public static final String MODULEFILENAME = ".argo.modules";
    public static final String MODULEFILENAME_ALTERNATE = "argo.modules";

    public boolean initializeModule(); // called when loading module
    
    public boolean shutdownModule();   // called when the module is shutdown

    public void setModuleEnabled(boolean tf);  // called to enable-disable
    
    public boolean isModuleEnabled(); // determines if enabled-disabled

    /** Display name of the module. */
    public String getModuleName();

    /** Textual description of the module. */
    public String getModuleDescription(); 

    public String getModuleVersion(); 
    
    public String getModuleAuthor(); 
    
    // calls all modules to let them add to a popup menu
    public Vector getModulePopUpActions(Vector popUpActions, Object context);

    public String getModuleKey();
}

