// Copyright (c) 1996-2003 The Regents of the University of California. All
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

// $Id$
package org.argouml.ui;

import org.argouml.application.security.ArgoSecurityManager;

import junit.framework.TestCase;

/**
 * <p>
 * ActionGoToDetails must only be active if the pane that must be activated by
 * this action supports the target of the targetmanager (or better
 * the first target of the targetmanager).
 * </p>
 * <p>
 * The following panes support the following types of targets:<br/>
 * <table>
 * <tr>
 * <td> 
 * Todo pane
 * </td>
 * <td>
 * All targets (allways enabled)
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Property panel
 * </td>
 * <td>
 * Modelelement targets (the target is a modelelement or a fig that shows a modelelement)
 * </td>
 * </tr>
 * <tr>
 * <td>
 * Tagged values panel
 * </td>
 * <td>
 * Modelelement targets (the target is a modelelement or a fig that shows a modelelement)
 * </td>
 * </tr> 
 * <tr>
 * <td>
 * Documentation panel
 * </td>
 * <td>
 * All targets except null (so disabled if no target is selected)
 * </td>
 * </tr> 
 * <tr>
 * <td>
 * Style panel
 * </td>
 * <td>
 * Only if the target is a fig OR the current shown diagram contains a fig that shows the currently 
 * selected model target.
 * </td>
 * </tr> 
 * <tr>
 * <td>
 * Source panel
 * </td>
 * <td>
 * Only if the target is a model element
 * </td>
 * </tr> 
 * <tr>
 * <td>
 * Constraints panel
 * </td>
 * <td>
 * Only if the target is a class or a feature or a fig that shows a class or a feature.
 * </td>
 * </tr> 
 * </table>  
 * @author jaap.branderhorst@xs4all.nl
 * Jul 20, 2003
 */
public class GUITestActionGoToDetails extends TestCase {
    
    ActionGoToDetails _action;

    

    /**
     * @param arg0
     */
    public GUITestActionGoToDetails(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {       
        super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
        _action = new ActionGoToDetails("");
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }
    
    public void testToDoTab() {    
    }

}
