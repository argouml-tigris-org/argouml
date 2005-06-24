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

import java.awt.Container;
import java.io.Writer;

import org.argouml.model.ActivityGraphsFactory;
import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.AggregationKind;
import org.argouml.model.ChangeableKind;
import org.argouml.model.CollaborationsFactory;
import org.argouml.model.CollaborationsHelper;
import org.argouml.model.CommonBehaviorFactory;
import org.argouml.model.CommonBehaviorHelper;
import org.argouml.model.ConcurrencyKind;
import org.argouml.model.ContainerDispatcher;
import org.argouml.model.CoreFactory;
import org.argouml.model.CoreHelper;
import org.argouml.model.DataTypesFactory;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.DirectionKind;
import org.argouml.model.EventAdapter;
import org.argouml.model.ExtensionMechanismsFactory;
import org.argouml.model.ExtensionMechanismsHelper;
import org.argouml.model.Facade;
import org.argouml.model.MementoCreationObserver;
import org.argouml.model.MetaTypes;
import org.argouml.model.ModelEventPump;
import org.argouml.model.ModelImplementation;
import org.argouml.model.ModelManagementFactory;
import org.argouml.model.ModelManagementHelper;
import org.argouml.model.Multiplicities;
import org.argouml.model.OrderingKind;
import org.argouml.model.PseudostateKind;
import org.argouml.model.ScopeKind;
import org.argouml.model.StateMachinesFactory;
import org.argouml.model.StateMachinesHelper;
import org.argouml.model.UmlException;
import org.argouml.model.UmlFactory;
import org.argouml.model.UmlHelper;
import org.argouml.model.UseCasesFactory;
import org.argouml.model.UseCasesHelper;
import org.argouml.model.VisibilityKind;
import org.argouml.model.XmiReader;
import org.argouml.model.XmiWriter;

/**
 * The handle to find all helper and factories.
 */
public class NSUMLModelImplementation implements ModelImplementation {
    private Facade theFacade = new NSUMLModelFacade(this);

    private ActivityGraphsFactory theActivityGraphsFactory =
        new ActivityGraphsFactoryImpl(this);
    private ActivityGraphsHelper theActivityGraphsHelper =
        new ActivityGraphsHelperImpl(this);
    private CollaborationsFactory theCollaborationsFactory =
        new CollaborationsFactoryImpl(this);
    private CollaborationsHelper theCollaborationsHelper =
        new CollaborationsHelperImpl(this);
    private CommonBehaviorFactory theCommonBehaviorFactory =
        new CommonBehaviorFactoryImpl(this);
    private CommonBehaviorHelper theCommonBehaviorHelper =
        new CommonBehaviorHelperImpl(this);
    private CopyHelper theCopyHelper = new CopyHelper(this);
    private CoreFactory theCoreFactory = new CoreFactoryImpl(this);
    private CoreHelper theCoreHelper = new CoreHelperImpl(this);
    private DataTypesFactory theDataTypesFactory =
        new DataTypesFactoryImpl(this);
    private DataTypesHelper theDataTypesHelper = new DataTypesHelperImpl(this);
    private ExtensionMechanismsFactory theExtensionMechanismsFactory =
        new ExtensionMechanismsFactoryImpl(this);
    private ExtensionMechanismsHelper theExtensionMechanismsHelper =
        new ExtensionMechanismsHelperImpl(this);
    private ModelManagementFactory theModelManagementFactory =
        new ModelManagementFactoryImpl(this);
    private ModelManagementHelper theModelManagementHelper =
        new ModelManagementHelperImpl(this);
    private StateMachinesFactory theStateMachinesFactory =
        new StateMachinesFactoryImpl(this);
    private StateMachinesHelper theStateMachinesHelper =
        new StateMachinesHelperImpl(this);
    private UmlFactory theUmlFactory;
    private UmlHelper theUmlHelper = new UmlHelperImpl(this);
    private UseCasesFactory theUseCasesFactory = new UseCasesFactoryImpl(this);
    private UseCasesHelper theUseCasesHelper = new UseCasesHelperImpl(this);
    private ModelEventPump theModelEventPump = new NSUMLModelEventPump(this);
    private MetaTypesImpl theMetaTypesObject = new MetaTypesImpl();
    private EventAdapter theEventAdapter = new ExplorerNSUMLEventAdaptor();

    private KindsImpl theKindsObject = new KindsImpl();

    private MementoCreationObserver mementoCreationObserver;

    /**
     * @see org.argouml.model.ModelImplementation#getFacade()
     */
    public Facade getFacade() {
        return theFacade;
    }

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
     * @return The Copy helper.
     */
    CopyHelper getCopyHelper() {
        return theCopyHelper;
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
     * @see org.argouml.model.ModelImplementation#getEventAdapter()
     */
    public EventAdapter getEventAdapter() {
        return theEventAdapter;
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
            theUmlFactory = new UmlFactoryImpl(this);
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

    /**
     * Getter for the MetaTypes object.
     *
     * @return The MetaTypes object.
     */
    public MetaTypes getMetaTypes() {
        return theMetaTypesObject;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getChangeableKind()
     */
    public ChangeableKind getChangeableKind() {
        return theKindsObject;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getAggregationKind()
     */
    public AggregationKind getAggregationKind() {
        return theKindsObject;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getPseudostateKind()
     */
    public PseudostateKind getPseudostateKind() {
        return theKindsObject;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getScopeKind()
     */
    public ScopeKind getScopeKind() {
        return theKindsObject;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getConcurrencyKind()
     */
    public ConcurrencyKind getConcurrencyKind() {
        return theKindsObject;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getDirectionKind()
     */
    public DirectionKind getDirectionKind() {
        return theKindsObject;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getMultiplicities()
     */
    public Multiplicities getMultiplicities() {
        return theKindsObject;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getOrderingKind()
     */
    public OrderingKind getOrderingKind() {
        return theKindsObject;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getVisibilityKind()
     */
    public VisibilityKind getVisibilityKind() {
        return theKindsObject;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getXmiReader()
     */
    public XmiReader getXmiReader() throws UmlException {
        return new XmiReaderImpl();
    }

    /**
     * @see org.argouml.model.ModelImplementation#getXmiWriter(
     *         java.lang.Object, java.io.Writer)
     */
    public XmiWriter getXmiWriter(Object model, Writer writer)
        throws UmlException {
        return new XmiWriterImpl(model, writer);
    }

    /**
     * @see org.argouml.model.ModelImplementation#createContainerDispatcher(
     *         java.awt.Container)
     */
    public ContainerDispatcher createContainerDispatcher(Container container) {
        return new ContainerDispatcherImpl(container);
    }

    /**
     * @see org.argouml.model.ModelImplementation#setMementoCreationObserver(
     *         org.argouml.model.MementoCreationObserver)
     */
    public void setMementoCreationObserver(MementoCreationObserver observer) {
        mementoCreationObserver = observer;
    }

    /**
     * @see org.argouml.model.ModelImplementation#getMementoCreationObserver(
     *         org.argouml.model.MementoCreationObserver)
     */
    public MementoCreationObserver getMementoCreationObserver() {
        return mementoCreationObserver;
    }
}
