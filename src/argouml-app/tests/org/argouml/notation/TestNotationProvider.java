/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2009 The Regents of the University of California. All
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

package org.argouml.notation;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * @author Michiel
 */
public class TestNotationProvider extends TestCase 
    implements NotationRenderer {

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
    }

    private Object aClass;
    private boolean propChanged = false;

    /**
     * Test the existence of the 
     * toString(Object modelElement, NotationSettings settings) method.
     * TODO: Need to find a more useful test.
     */
    public void testToString() {
        NotationProvider np = new NPImpl();
        NotationSettings settings = new NotationSettings();
        settings.setUseGuillemets(true);
        assertTrue("Test toString()", "atrue".equals(
                np.toString("a", settings)));
    }

    private class NPImpl extends NotationProvider {

        /*
         * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
         */
        public String getParsingHelp() {
            return null;
        }

        public String toString(Object modelElement, NotationSettings settings) {
            return modelElement.toString() + settings.isUseGuillemets();
        }
        
        /*
         * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
         */
        public void parse(Object modelElement, String text) {

        }

    }
    
    /**
     * Test if the listener gets events when model elements change:
     * @throws InterruptedException 
     */
    public void testListener() throws InterruptedException {
        Object model =
            Model.getModelManagementFactory().createModel();
        aClass = Model.getCoreFactory().buildClass(model);
        
        NotationProvider np = new NPImpl();
        np.setRenderer(this);
        np.initialiseListener(aClass);
        
        propChanged = false;
        Model.getCoreHelper().setName(aClass, "ClassA1");
        Model.getPump().flushModelEvents();
        Thread.sleep(2000);
        assertTrue("No event received", propChanged);
        
        np.cleanListener();
        propChanged = false;
        Model.getCoreHelper().setName(aClass, "ClassA2");
        Model.getPump().flushModelEvents();
        assertTrue("Event received, despite not listening", !propChanged);

        np.updateListener(aClass, null);
    }

    public void notationRenderingChanged(NotationProvider np, 
            String rendering) {
        propChanged = true;
    }

    public NotationSettings getNotationSettings(NotationProvider np) {
        return new NotationSettings();
    }

    public Object getOwner(NotationProvider np) {
        return aClass;
    }
}
