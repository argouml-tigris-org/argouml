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
    private ActivityGraphsFactory theActivityGraphsFactory =
        new ActivityGraphsFactoryImpl();
    private ActivityGraphsHelper theActivityGraphsHelper =
        new ActivityGraphsHelperImpl();
    private CollaborationsFactory theCollaborationsFactory =
        new CollaborationsFactoryImpl();
    private CollaborationsHelper theCollaborationsHelper =
        new CollaborationsHelperImpl();
    private CommonBehaviorFactory theCommonBehaviorFactory =
        new CommonBehaviorFactoryImpl();
    private CommonBehaviorHelper theCommonBehaviorHelper =
        new CommonBehaviorHelperImpl();
    private CoreFactory theCoreFactory = new CoreFactoryImpl();
    private CoreHelper theCoreHelper = new CoreHelperImpl();
    private DataTypesFactory theDataTypesFactory = new DataTypesFactoryImpl();
    private DataTypesHelper theDataTypesHelper = new DataTypesHelperImpl();
    private ExtensionMechanismsFactory theExtensionMechanismsFactory =
        new ExtensionMechanismsFactoryImpl();
    private ExtensionMechanismsHelper theExtensionMechanismsHelper =
        new ExtensionMechanismsHelperImpl();
    private ModelManagementFactory theModelManagementFactory =
        new ModelManagementFactoryImpl();
    private ModelManagementHelper theModelManagementHelper =
        new ModelManagementHelperImpl();
    private StateMachinesFactory theStateMachinesFactory =
        new StateMachinesFactoryImpl();
    private StateMachinesHelper theStateMachinesHelper =
        new StateMachinesHelperImpl();
    private UmlFactory theUmlFactory;
    private UmlHelper theUmlHelper = new UmlHelperImpl();
    private UseCasesFactory theUseCasesFactory = new UseCasesFactoryImpl();
    private UseCasesHelper theUseCasesHelper = new UseCasesHelperImpl();
    private ModelEventPump theModelEventPump = new NSUMLModelEventPump();

    /**
     * @see org.argouml.model.ModelImplementation#getModelEventPump()
     */
    public ModelEventPump getModelEventPump() {
        return theModelEventPump;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getActivityGraphsFactory()
     */
    public ActivityGraphsFactory getActivityGraphsFactory() {
        return theActivityGraphsFactory;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getActivityGraphsHelper()
     */
    public ActivityGraphsHelper getActivityGraphsHelper() {
        return theActivityGraphsHelper;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCollaborationsFactory()
     */
    public CollaborationsFactory getCollaborationsFactory() {
        return theCollaborationsFactory;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCollaborationsHelper()
     */
    public CollaborationsHelper getCollaborationsHelper() {
        return theCollaborationsHelper;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCommonBehaviorFactory()
     */
    public CommonBehaviorFactory getCommonBehaviorFactory() {
        return theCommonBehaviorFactory;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCommonBehaviorHelper()
     */
    public CommonBehaviorHelper getCommonBehaviorHelper() {
        return theCommonBehaviorHelper;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCoreFactory()
     */
    public CoreFactory getCoreFactory() {
        return theCoreFactory;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCoreHelper()
     */
    public CoreHelper getCoreHelper() {
        return theCoreHelper;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getDataTypesFactory()
     */
    public DataTypesFactory getDataTypesFactory() {
        return theDataTypesFactory;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getDataTypesHelper()
     */
    public DataTypesHelper getDataTypesHelper() {
        return theDataTypesHelper;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getExtensionMechanismsFactory()
     */
    public ExtensionMechanismsFactory getExtensionMechanismsFactory() {
        return theExtensionMechanismsFactory;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getExtensionMechanismsHelper()
     */
    public ExtensionMechanismsHelper getExtensionMechanismsHelper() {
        return theExtensionMechanismsHelper;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getModelManagementFactory()
     */
    public ModelManagementFactory getModelManagementFactory() {
        return theModelManagementFactory;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getModelManagementHelper()
     */
    public ModelManagementHelper getModelManagementHelper() {
        return theModelManagementHelper;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getStateMachinesFactory()
     */
    public StateMachinesFactory getStateMachinesFactory() {
        return theStateMachinesFactory;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getStateMachinesHelper()
     */
    public StateMachinesHelper getStateMachinesHelper() {
        return theStateMachinesHelper;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getUmlFactory()
     */
    public synchronized UmlFactory getUmlFactory() {
        if (theUmlFactory == null) {
            theUmlFactory = new UmlFactoryImpl();
        }
        return theUmlFactory;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getUmlHelper()
     */
    public UmlHelper getUmlHelper() {
        return theUmlHelper;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getUseCasesFactory()
     */
    public UseCasesFactory getUseCasesFactory() {
        return theUseCasesFactory;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getUseCasesHelper()
     */
    public UseCasesHelper getUseCasesHelper() {
        return theUseCasesHelper;
    }
}

