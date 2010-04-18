/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    maurelio1234
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.profile.init;

import org.argouml.profile.ProfileFacade;
import org.argouml.profile.internal.ui.ProfilePropPanelFactory;
import org.argouml.uml.ui.PropPanelFactory;
import org.argouml.uml.ui.PropPanelFactoryManager;

/**
 * Initializer class for the Profile subsystem.
 * 
 * @author Luis Sergio Oliveira (euluis)
 */
public class InitProfileSubsystem {

    /**
     * Initialize the Profiles subsystem by binding the 2 packages together.
     */
    public void init() {
        // TODO: There are tests which depend on being able to reinitialize
        // the Profile subsystem multiple times.
//        if (!ProfileFacade.isInitiated()) {
//            ProfileFacade.setManager(
//                    new org.argouml.profile.internal.ProfileManagerImpl());
//        }
        ProfileFacade.reset();
        ProfileFacade.setManager(
                new org.argouml.profile.internal.ProfileManagerImpl());

        /* Set up the property panels for critics: */
        PropPanelFactory factory = new ProfilePropPanelFactory();
        PropPanelFactoryManager.addPropPanelFactory(factory);    
        
        /* init profiles defined in jar files */
        new ProfileLoader().doLoad();
    }

}
