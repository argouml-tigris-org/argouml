// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.model;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
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
    private List controls;

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
        controls = new ArrayList();
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
        Iterator iter = controls.iterator();
        while (iter.hasNext()) {
            MockControl control = (MockControl) iter.next();
            control.reset();
        }
    }

    /**
     * Replay all mock objects.
     */
    public void replay() {
        Iterator iter = controls.iterator();
        while (iter.hasNext()) {
            MockControl control = (MockControl) iter.next();
            control.replay();
        }
    }
    /**
     * Verify all mock objects.
     */
    public void verify() {
        Iterator iter = controls.iterator();
        while (iter.hasNext()) {
            MockControl control = (MockControl) iter.next();
            control.verify();
        }
    }

    /**
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

    /**
     * @see org.argouml.model.ModelImplementation#getDiagramInterchangeModel()
     */
    public DiagramInterchangeModel getDiagramInterchangeModel() {
        return (DiagramInterchangeModel) controlDIM.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getModelEventPump()
     */
    public ModelEventPump getModelEventPump() {
        return (ModelEventPump) controlMEP.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getActivityGraphsFactory()
     */
    public ActivityGraphsFactory getActivityGraphsFactory() {
        return (ActivityGraphsFactory) controlAGF.getMock();
    }

    /**
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

    /**
     * @see org.argouml.model.ModelImplementation#getCollaborationsFactory()
     */
    public CollaborationsFactory getCollaborationsFactory() {
        return (CollaborationsFactory) controlCF.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCollaborationsHelper()
     */
    public CollaborationsHelper getCollaborationsHelper() {
        return (CollaborationsHelper) controlCH.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCommonBehaviorFactory()
     */
    public CommonBehaviorFactory getCommonBehaviorFactory() {
        return (CommonBehaviorFactory) controlCBF.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCommonBehaviorHelper()
     */
    public CommonBehaviorHelper getCommonBehaviorHelper() {
        return (CommonBehaviorHelper) controlCBH.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCoreFactory()
     */
    public CoreFactory getCoreFactory() {
        return (CoreFactory) controlCoreFactory.getMock();
    }

    /**
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

    /**
     * @see org.argouml.model.ModelImplementation#getDataTypesFactory()
     */
    public DataTypesFactory getDataTypesFactory() {
        return (DataTypesFactory) controlDTF.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getDataTypesHelper()
     */
    public DataTypesHelper getDataTypesHelper() {
        return (DataTypesHelper) controlDTH.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getExtensionMechanismsFactory()
     */
    public ExtensionMechanismsFactory getExtensionMechanismsFactory() {
        return (ExtensionMechanismsFactory) controlEMF.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getExtensionMechanismsHelper()
     */
    public ExtensionMechanismsHelper getExtensionMechanismsHelper() {
        return (ExtensionMechanismsHelper) controlEMH.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getEventAdapter()
     */
    public EventAdapter getEventAdapter() {
        throw new NotImplementedException();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getModelManagementFactory()
     */
    public ModelManagementFactory getModelManagementFactory() {
        return (ModelManagementFactory) controlMMF.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getModelManagementHelper()
     */
    public ModelManagementHelper getModelManagementHelper() {
        return (ModelManagementHelper) controlMMH.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getStateMachinesFactory()
     */
    public StateMachinesFactory getStateMachinesFactory() {
        return (StateMachinesFactory) controlSMF.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getStateMachinesHelper()
     */
    public StateMachinesHelper getStateMachinesHelper() {
        return (StateMachinesHelper) controlSMH.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getUmlFactory()
     */
    public UmlFactory getUmlFactory() {
        return (UmlFactory) controlUmlFactory.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getUmlHelper()
     */
    public UmlHelper getUmlHelper() {
        return (UmlHelper) controlUmlHelper.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getUseCasesFactory()
     */
    public UseCasesFactory getUseCasesFactory() {
        return (UseCasesFactory) controlUCF.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getUseCasesHelper()
     */
    public UseCasesHelper getUseCasesHelper() {
        return (UseCasesHelper) controlUCH.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getMetaTypes()
     */
    public MetaTypes getMetaTypes() {
        return (MetaTypes) controlMT.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getChangeableKind()
     */
    public ChangeableKind getChangeableKind() {
        return (ChangeableKind) controlCK.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getAggregationKind()
     */
    public AggregationKind getAggregationKind() {
        return (AggregationKind) controlAK.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getPseudostateKind()
     */
    public PseudostateKind getPseudostateKind() {
        return (PseudostateKind) controlPK.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getScopeKind()
     */
    public ScopeKind getScopeKind() {
        return (ScopeKind) controlSK.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getConcurrencyKind()
     */
    public ConcurrencyKind getConcurrencyKind() {
        return (ConcurrencyKind) controlConK.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getDirectionKind()
     */
    public DirectionKind getDirectionKind() {
        return (DirectionKind) controlDK.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getOrderingKind()
     */
    public OrderingKind getOrderingKind() {
        return (OrderingKind) controlOK.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getVisibilityKind()
     */
    public VisibilityKind getVisibilityKind() {
        return (VisibilityKind) controlVK.getMock();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getXmiReader()
     */
    public XmiReader getXmiReader() throws UmlException {
        throw new NotImplementedException();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getXmiWriter(java.lang.Object, java.io.Writer)
     */
    public XmiWriter getXmiWriter(Object model, Writer writer)
        throws UmlException {
        throw new NotImplementedException();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getCopyHelper()
     */
    public CopyHelper getCopyHelper() {
        return (CopyHelper) controlCopyHelper.getMock();
    }


    /**
     * The mementoCreationObserver.
     */
    private MementoCreationObserver mementoCreationObserver;

    /**
     * @see org.argouml.model.ModelImplementation#setMementoCreationObserver(
     *      org.argouml.model.MementoCreationObserver)
     * @deprecated by Linus Tolke in 0.21.3. This is taken care of
     *         in the {@link org.argouml.model.Model} and the
     *         implementation need not bother.
     */
    public void setMementoCreationObserver(MementoCreationObserver observer) {
        mementoCreationObserver = observer;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getMementoCreationObserver()
     * @deprecated by Linus Tolke in 0.21.3. This is taken care of
     *         in the {@link org.argouml.model.Model} and the
     *         implementation need not bother.
     */
    public MementoCreationObserver getMementoCreationObserver() {
        return mementoCreationObserver;
    }


}
