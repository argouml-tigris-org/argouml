// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.util.HashMap;
import java.util.Map;

import org.argouml.model.ActivityGraphsFactory;
import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.CollaborationsFactory;
import org.argouml.model.CollaborationsHelper;
import org.argouml.model.CommonBehaviorFactory;
import org.argouml.model.CommonBehaviorHelper;
import org.argouml.model.CoreFactory;
import org.argouml.model.CoreHelper;
import org.argouml.model.DataTypesFactory;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.ExtensionMechanismsFactory;
import org.argouml.model.ExtensionMechanismsHelper;
import org.argouml.model.ModelEventPump;
import org.argouml.model.ModelImplementation;
import org.argouml.model.ModelManagementFactory;
import org.argouml.model.ModelManagementHelper;
import org.argouml.model.StateMachinesFactory;
import org.argouml.model.StateMachinesHelper;
import org.argouml.model.UmlFactory;
import org.argouml.model.UmlHelper;
import org.argouml.model.UseCasesFactory;
import org.argouml.model.UseCasesHelper;

/**
 * The handle to find all helper and factories.
 */
public class NSUMLModelImplementation implements ModelImplementation {
    /**
     * Map with already created factories, helpers and other objects.
     */
    private Map alreadyCreated = new HashMap();

    /**
     * @see org.argouml.model.ModelImplementation#find(java.lang.Class)
     */
    public Object find(Class intf) {
        Object found = alreadyCreated.get(intf);
        if (found == null) {
            found = findCreate(intf);
            alreadyCreated.put(intf, found);
        }
        return found;
    }

    /**
     * Create a new object for a class.
     *
     * @param intf The interface to create the object for.
     * @return The object.
     */
    private Object findCreate(Class intf) {
        if (intf == ActivityGraphsFactory.class) {
            return new ActivityGraphsFactoryImpl();
        }
        if (intf == ActivityGraphsHelper.class) {
            return new ActivityGraphsHelperImpl();
        }

        if (intf == CollaborationsFactory.class) {
            return new CollaborationsFactoryImpl();
        }
        if (intf == CollaborationsHelper.class) {
            return new CollaborationsHelperImpl();
        }

        if (intf == CommonBehaviorFactory.class) {
            return new CommonBehaviorFactoryImpl();
        }
        if (intf == CommonBehaviorHelper.class) {
            return new CommonBehaviorHelperImpl();
        }

        if (intf == CoreFactory.class) {
            return new CoreFactoryImpl();
        }
        if (intf == CoreHelper.class) {
            return new CoreHelperImpl();
        }


        if (intf == DataTypesFactory.class) {
            return new DataTypesFactoryImpl();
        }
        if (intf == DataTypesHelper.class) {
            return new DataTypesHelperImpl();
        }

        if (intf == ExtensionMechanismsFactory.class) {
            return new ExtensionMechanismsFactoryImpl();
        }
        if (intf == ExtensionMechanismsHelper.class) {
            return new ExtensionMechanismsHelperImpl();
        }

        if (intf == ModelManagementFactory.class) {
            return new ModelManagementFactoryImpl();
        }
        if (intf == ModelManagementHelper.class) {
            return new ModelManagementHelperImpl();
        }

        if (intf == StateMachinesFactory.class) {
            return new StateMachinesFactoryImpl();
        }
        if (intf == StateMachinesHelper.class) {
            return new StateMachinesHelperImpl();
        }

        if (intf == UmlFactory.class) {
            return new UmlFactoryImpl();
        }
        if (intf == UmlHelper.class) {
            return new UmlHelperImpl();
        }

        if (intf == UseCasesFactory.class) {
            return new UseCasesFactoryImpl();
        }
        if (intf == UseCasesHelper.class) {
            return new UseCasesHelperImpl();
        }

        if (intf == ModelEventPump.class) {
            return new NSUMLModelEventPump();
        }

        return null;
    }

}

