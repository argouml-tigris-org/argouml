/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.uml.ui.AbstractActionAddModelElement2;


/**
 * Test the ActionAddClientDependency and ActionAddSupplierDependency
 *
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class TestActionAddDependency extends TestCase {

    private Object root;
    private Object target;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Translator.init("en");
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        Project p = ProjectManager.getManager().makeEmptyProject();

        root = Model.getModelManagementFactory().createModel();
        Collection roots = new ArrayList();
        roots.add(root);
        p.setRoots(roots);
        Model.getCoreHelper().setName(root, "root package");
        target = Model.getCoreFactory().buildClass("A", root);
        Model.getCoreFactory().buildClass("b", root);
        Model.getCoreFactory().buildClass("c", root);
    }

    protected void tearDown() {
        Model.getUmlFactory().delete(root);
    }
    
    /**
     * Test the add actions
     */
    public void testAddActions() {
        AbstractActionAddModelElement2 actionClient = 
            new ActionAddClientDependencyAction();
//        assertNotNull(actionClient.getValue(Action.SMALL_ICON));
        actionClient.setTarget(target);
        // The following will bring up a dialog box, so can't be run headless
//        actionClient.actionPerformed(new ActionEvent(this, 0, "foo"));
        
        AbstractActionAddModelElement2 actionSupplier = 
            new ActionAddSupplierDependencyAction();
//        assertNotNull(actionSupplier.getValue(Action.SMALL_ICON));
        actionSupplier.setTarget(target);
        // The following will bring up a dialog box, so can't be run headless
//        actionSupplier.actionPerformed(new ActionEvent(this, 0, "foo"));

    }
}
