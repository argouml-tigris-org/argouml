// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;

import org.argouml.model.uml.UmlFactory;
import org.argouml.model.ModelFacade;
import org.argouml.ui.targetmanager.TargetManager;

/** 
 * Action to add a package to the selected model element in the
 * nav pane. This is a shortcut that helps build model
 * structures quickly.
 *
 * @author alexb@tigris.org
 * @stereotype singleton
 */
public class ActionAddPackage  extends UMLAction {
    
    ////////////////////////////////////////////////////////////////
    // static variables
    
    public static ActionAddPackage SINGLETON = new ActionAddPackage();
    
    ////////////////////////////////////////////////////////////////
    // constructors
    
    /** Creates a new instance of ActionAddPackage */
    public ActionAddPackage() {
        
        super("Add package", NO_ICON);
    }
    
    /**
     * adds a package to the selected object in the nav pane.
     */
    public void actionPerformed(ActionEvent e) {
        Object namespace =
	    TargetManager.getInstance().getTarget();
        ModelFacade.addOwnedElement(namespace,
            UmlFactory.getFactory().getModelManagement().createPackage());
    }
    
}