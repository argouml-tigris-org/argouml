/* $Id$
 *****************************************************************************
 * Copyright (c) 2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    Linus Tolke
 *****************************************************************************
 *
 * This file was copied from the tests of argouml-app.
 */

package org.argouml.model;

import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.easymock.MockControl;

/**
 * A mock ModelImplementation.<p>
 *
 * The Facade, the ModelEventPump, the Factories and Helpers are all
 * created as mock objects using EasyMock. For each of them, there
 * should also be a possibility to access their {@link MockControl}
 * object.
 * TODO: Create the rest of the getters for the control objects.
 *
 * @author Linus Tolke
 */
class MockModelImplementation implements ModelImplementation {

    /**
     * A list of all the control objects.
     */
    private List<MockControl> controls;

    /**
     * The instance that was just created.
     */
    private static MockModelImplementation lastCreatedInstance;

    /**
     * Control object.
     */
    private MockControl controlFacade;

    /**
     * Control object.
     */
    private MockControl controlDIM;

    /**
     * Control object.
     */
    private MockControl controlMEP;

    /**
     * Control object.
     */
    private MockControl controlAGF;

    /**
     * Control object.
     */
    private MockControl controlAGH;

    /**
     * Control object.
     */
    private MockControl controlCF;

    /**
     * Control object.
     */
    private MockControl controlCH;

    /**
     * Control object.
     */
    private MockControl controlCBF;

    /**
     * Control object.
     */
    private MockControl controlCBH;

    /**
     * Control object.
     */
    private MockControl controlCoreFactory;

    /**
     * Control object.
     */
    private MockControl controlCoreHelper;

    /**
     * Control object.
     */
    private MockControl controlDTF;

    /**
     * Control object.
     */
    private MockControl controlDTH;

    /**
     * Control object.
     */
    private MockControl controlEMF;

    /**
     * Control object.
     */
    private MockControl controlEMH;

    /**
     * Control object.
     */
    private MockControl controlMMF;

    /**
     * Control object.
     */
    private MockControl controlMMH;

    /**
     * Control object.
     */
    private MockControl controlSMF;

    /**
     * Control object.
     */
    private MockControl controlSMH;

    /**
     * Control object.
     */
    private MockControl controlUmlFactory;

    /**
     * Control object.
     */
    private MockControl controlUmlHelper;

    /**
     * Control object.
     */
    private MockControl controlUCF;

    /**
     * Control object.
     */
    private MockControl controlUCH;

    /**
     * Control object.
     */
    private MockControl controlMT;
    
    /**
     * Control object.
     */
    private MockControl controlMS;

    /**
     * Control object.
     */
    private MockControl controlCK;

    /**
     * Control object.
     */
    private MockControl controlAK;

    /**
     * Control object.
     */
    private MockControl controlPK;

    /**
     * Control object.
     */
    private MockControl controlSK;

    /**
     * Control object.
     */
    private MockControl controlConK;

    /**
     * Control object.
     */
    private MockControl controlDK;

    /**
     * Control object.
     */
    private MockControl controlOK;

    /**
     * Control object.
     */
    private MockControl controlVK;

    /**
     * Control object.
     */
    private MockControl controlCopyHelper;

    /**
     * Constructor.
     */
    public MockModelImplementation() {
        controls = new ArrayList<MockControl>();
        lastCreatedInstance = this;

        controlFacade = MockControl.createControl(Facade.class);
        controls.add(controlFacade);

        controlDIM = MockControl.createControl(DiagramInterchangeModel.class);
        controls.add(controlDIM);

        controlMEP = MockControl.createControl(ModelEventPump.class);
        controls.add(controlMEP);

        createBehaviorControls();

        createFoundationControls();

        controlMMF = MockControl.createControl(ModelManagementFactory.class);
        controls.add(controlMMF);

        controlMMH = MockControl.createControl(ModelManagementHelper.class);
        controls.add(controlMMH);

        controlUmlFactory = MockControl.createControl(UmlFactory.class);
        controls.add(controlUmlFactory);

        controlUmlHelper = MockControl.createControl(UmlHelper.class);
        controls.add(controlUmlHelper);

        controlCopyHelper = MockControl.createControl(CopyHelper.class);
        controls.add(controlCopyHelper);

	controlMS = MockControl.createControl(MessageSort.class);
	controls.add(controlMS);

        controlMT = MockControl.createControl(MetaTypes.class);
        controls.add(controlMT);

        createKindControls();
    }

    /**
     * Create the controls for the foundation factories and helpers.
     */
    private void createFoundationControls() {
        controlCoreFactory = MockControl.createControl(CoreFactory.class);
        controls.add(controlCoreFactory);

        controlCoreHelper = MockControl.createControl(CoreHelper.class);
        controls.add(controlCoreHelper);

        controlDTF = MockControl.createControl(DataTypesFactory.class);
        controls.add(controlDTF);

        controlDTH = MockControl.createControl(DataTypesHelper.class);
        controls.add(controlDTH);

        controlEMF =
            MockControl.createControl(ExtensionMechanismsFactory.class);
        controls.add(controlEMF);

        controlEMH = MockControl.createControl(ExtensionMechanismsHelper.class);
        controls.add(controlEMH);
    }

    /**
     * Create the controls for all behavior elements' factories and helpers.
     */
    private void createBehaviorControls() {
        controlAGH = MockControl.createControl(ActivityGraphsHelper.class);
        controls.add(controlAGH);

        controlAGF = MockControl.createControl(ActivityGraphsFactory.class);
        controls.add(controlAGF);

        controlCF = MockControl.createControl(CollaborationsFactory.class);
        controls.add(controlCF);

        controlCH = MockControl.createControl(CollaborationsHelper.class);
        controls.add(controlCH);

        controlCBF = MockControl.createControl(CommonBehaviorFactory.class);
        controls.add(controlCBF);

        controlCBH = MockControl.createControl(CommonBehaviorHelper.class);
        controls.add(controlCBH);

        controlSMF = MockControl.createControl(StateMachinesFactory.class);
        controls.add(controlSMF);

        controlSMH = MockControl.createControl(StateMachinesHelper.class);
        controls.add(controlSMH);

        controlUCF = MockControl.createControl(UseCasesFactory.class);
        controls.add(controlUCF);

        controlUCH = MockControl.createControl(UseCasesHelper.class);
        controls.add(controlUCH);
    }

    /**
     * Create the controls for the Kinds.
     */
    private void createKindControls() {
        controlCK = MockControl.createControl(ChangeableKind.class);
        controls.add(controlCK);

        controlAK = MockControl.createControl(AggregationKind.class);
        controls.add(controlAK);

        controlPK = MockControl.createControl(PseudostateKind.class);
        controls.add(controlPK);

        controlSK = MockControl.createControl(ScopeKind.class);
        controls.add(controlSK);

        controlConK = MockControl.createControl(ConcurrencyKind.class);
        controls.add(controlConK);

        controlDK = MockControl.createControl(DirectionKind.class);
        controls.add(controlDK);

        controlOK = MockControl.createControl(OrderingKind.class);
        controls.add(controlOK);

        controlVK = MockControl.createControl(VisibilityKind.class);
        controls.add(controlVK);
    }

    /**
     * @return The last created MockModelImplementation.
     */
    public static MockModelImplementation getLatest() {
        return lastCreatedInstance;
    }

    /**
     * Reset all mock objects.
     */
    public void reset() {
        for (MockControl control : controls) {
            control.reset();
        }
    }

    /**
     * Replay all mock objects.
     */
    public void replay() {
        for (MockControl control : controls) {
            control.replay();
        }
    }
    /**
     * Verify all mock objects.
     */
    public void verify() {
        for (MockControl control : controls) {
            control.verify();
        }
    }

    /*
     * @see org.argouml.model.ModelImplementation#getFacade()
     */
    public Facade getFacade() {
        return (Facade) controlFacade.getMock();
    }

    /**
     * @return The {@link MockControl} object for the Facade.
     */
    public MockControl getFacadeControl() {
        return controlFacade;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getDiagramInterchangeModel()
     */
    public DiagramInterchangeModel getDiagramInterchangeModel() {
        return (DiagramInterchangeModel) controlDIM.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getModelEventPump()
     */
    public ModelEventPump getModelEventPump() {
        return (ModelEventPump) controlMEP.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getActivityGraphsFactory()
     */
    public ActivityGraphsFactory getActivityGraphsFactory() {
        return (ActivityGraphsFactory) controlAGF.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getActivityGraphsHelper()
     */
    public ActivityGraphsHelper getActivityGraphsHelper() {
        return (ActivityGraphsHelper) controlAGH.getMock();
    }

    /**
     * @return The {@link MockControl} object for the ActivityGraphsHelper.
     */
    public MockControl getActivityGraphsHelperControl() {
        return controlAGH;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCollaborationsFactory()
     */
    public CollaborationsFactory getCollaborationsFactory() {
        return (CollaborationsFactory) controlCF.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCollaborationsHelper()
     */
    public CollaborationsHelper getCollaborationsHelper() {
        return (CollaborationsHelper) controlCH.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCommonBehaviorFactory()
     */
    public CommonBehaviorFactory getCommonBehaviorFactory() {
        return (CommonBehaviorFactory) controlCBF.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCommonBehaviorHelper()
     */
    public CommonBehaviorHelper getCommonBehaviorHelper() {
        return (CommonBehaviorHelper) controlCBH.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCoreFactory()
     */
    public CoreFactory getCoreFactory() {
        return (CoreFactory) controlCoreFactory.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCoreHelper()
     */
    public CoreHelper getCoreHelper() {
        return (CoreHelper) controlCoreHelper.getMock();
    }

    /**
     * @return The {@link MockControl} object for the CoreHelper.
     */
    public MockControl getCoreHelperControl() {
        return controlCoreHelper;
    }

    /*
     * @see org.argouml.model.ModelImplementation#getDataTypesFactory()
     */
    public DataTypesFactory getDataTypesFactory() {
        return (DataTypesFactory) controlDTF.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getDataTypesHelper()
     */
    public DataTypesHelper getDataTypesHelper() {
        return (DataTypesHelper) controlDTH.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getExtensionMechanismsFactory()
     */
    public ExtensionMechanismsFactory getExtensionMechanismsFactory() {
        return (ExtensionMechanismsFactory) controlEMF.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getExtensionMechanismsHelper()
     */
    public ExtensionMechanismsHelper getExtensionMechanismsHelper() {
        return (ExtensionMechanismsHelper) controlEMH.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getModelManagementFactory()
     */
    public ModelManagementFactory getModelManagementFactory() {
        return (ModelManagementFactory) controlMMF.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getModelManagementHelper()
     */
    public ModelManagementHelper getModelManagementHelper() {
        return (ModelManagementHelper) controlMMH.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getStateMachinesFactory()
     */
    public StateMachinesFactory getStateMachinesFactory() {
        return (StateMachinesFactory) controlSMF.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getStateMachinesHelper()
     */
    public StateMachinesHelper getStateMachinesHelper() {
        return (StateMachinesHelper) controlSMH.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getUmlFactory()
     */
    public UmlFactory getUmlFactory() {
        return (UmlFactory) controlUmlFactory.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getUmlHelper()
     */
    public UmlHelper getUmlHelper() {
        return (UmlHelper) controlUmlHelper.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getUseCasesFactory()
     */
    public UseCasesFactory getUseCasesFactory() {
        return (UseCasesFactory) controlUCF.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getUseCasesHelper()
     */
    public UseCasesHelper getUseCasesHelper() {
        return (UseCasesHelper) controlUCH.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getMessageSort()
     */
    public MessageSort getMessageSort() {
        return (MessageSort) controlMS.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getMetaTypes()
     */
    public MetaTypes getMetaTypes() {
        return (MetaTypes) controlMT.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getChangeableKind()
     */
    @SuppressWarnings("deprecation")
    public ChangeableKind getChangeableKind() {
        return (ChangeableKind) controlCK.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getAggregationKind()
     */
    public AggregationKind getAggregationKind() {
        return (AggregationKind) controlAK.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getPseudostateKind()
     */
    public PseudostateKind getPseudostateKind() {
        return (PseudostateKind) controlPK.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getScopeKind()
     */
    @SuppressWarnings("deprecation")
    public ScopeKind getScopeKind() {
        return (ScopeKind) controlSK.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getConcurrencyKind()
     */
    public ConcurrencyKind getConcurrencyKind() {
        return (ConcurrencyKind) controlConK.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getDirectionKind()
     */
    public DirectionKind getDirectionKind() {
        return (DirectionKind) controlDK.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getOrderingKind()
     */
    public OrderingKind getOrderingKind() {
        return (OrderingKind) controlOK.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getVisibilityKind()
     */
    public VisibilityKind getVisibilityKind() {
        return (VisibilityKind) controlVK.getMock();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getXmiReader()
     */
    public XmiReader getXmiReader() throws UmlException {
        throw new NotImplementedException();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getXmiWriter(java.lang.Object, java.io.Writer, java.lang.String)
     */
    @SuppressWarnings("deprecation")
    public XmiWriter getXmiWriter(Object model, Writer writer, String version)
        throws UmlException {
        throw new NotImplementedException();
    }

    /*
     * @see org.argouml.model.ModelImplementation#getCopyHelper()
     */
    public CopyHelper getCopyHelper() {
        return (CopyHelper) controlCopyHelper.getMock();
    }

    public XmiWriter getXmiWriter(Object model, OutputStream stream,
            String version) throws UmlException {
        throw new NotImplementedException();
    }

}
