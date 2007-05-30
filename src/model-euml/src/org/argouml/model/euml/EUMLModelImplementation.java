// $Id$
// Copyright (c) 2007, The ArgoUML Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the ArgoUML Project nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE ArgoUML PROJECT ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE ArgoUML PROJECT BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import java.io.Writer;

import org.apache.log4j.Logger;
import org.argouml.model.ActivityGraphsFactory;
import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.AggregationKind;
import org.argouml.model.ChangeableKind;
import org.argouml.model.CollaborationsFactory;
import org.argouml.model.CollaborationsHelper;
import org.argouml.model.CommonBehaviorFactory;
import org.argouml.model.CommonBehaviorHelper;
import org.argouml.model.ConcurrencyKind;
import org.argouml.model.CopyHelper;
import org.argouml.model.CoreFactory;
import org.argouml.model.CoreHelper;
import org.argouml.model.DataTypesFactory;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.DiagramInterchangeModel;
import org.argouml.model.DirectionKind;
import org.argouml.model.ExtensionMechanismsFactory;
import org.argouml.model.ExtensionMechanismsHelper;
import org.argouml.model.Facade;
import org.argouml.model.MetaTypes;
import org.argouml.model.ModelEventPump;
import org.argouml.model.ModelImplementation;
import org.argouml.model.ModelManagementFactory;
import org.argouml.model.ModelManagementHelper;
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
import org.argouml.model.euml.ActivityGraphsFactoryEUMLlImpl;
import org.argouml.model.euml.ActivityGraphsHelperEUMLImpl;
import org.argouml.model.euml.CommonBehaviorFactoryEUMLImpl;
import org.argouml.model.euml.CommonBehaviorHelperEUMLImpl;
import org.argouml.model.euml.CoreFactoryEUMLImpl;
import org.argouml.model.euml.CoreHelperEUMLImpl;
import org.argouml.model.euml.DataTypesFactoryEUMLImpl;
import org.argouml.model.euml.DataTypesHelperEUMLImpl;
import org.argouml.model.euml.ExtensionMechanismsFactoryEUMLImpl;
import org.argouml.model.euml.ExtensionMechanismsHelperEUMLImpl;
import org.argouml.model.euml.ModelEventPumpEUMLImpl;
import org.argouml.model.euml.ModelManagementFactoryEUMLImpl;
import org.argouml.model.euml.ModelManagementHelperEUMLImpl;
import org.argouml.model.euml.StateMachinesFactoryEUMLImpl;
import org.argouml.model.euml.StateMachinesHelperEUMLImpl;
import org.argouml.model.euml.UseCasesFactoryEUMLImpl;
import org.argouml.model.euml.UseCasesHelperEUMLImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * Eclipse UML2 implementation of the ArgoUML Model subsystem. Although built on
 * the Eclipse UML2 plugin which is, in turn, built on eCore and the Eclipse
 * Modeling Framework (EMF), the only things required from Eclipse are the five
 * Jars which implement UML2, eCore, and the subset of EMF needed for to support
 * them.
 * <p>
 * The implementation of this subsystem was generously sponsored by Google as
 * part of the Google Summer of Code 2007. The bulk of the implementation was
 * built by the sponsored student, Bogdan Ciprian Pistol, who was mentored by
 * Tom Morris.
 * 
 * @author Bogdan Ciprian Pistol
 * @author Tom Morris <tfmorris@gmail.com>
 * @since ArgoUML 0.25.4, May 2007
 */
public class EUMLModelImplementation implements ModelImplementation {
    
    private static final Logger LOG =
            Logger.getLogger(EUMLModelImplementation.class);
    
    private ActivityGraphsFactory theActivityGraphsFactory;

    private ActivityGraphsHelper theActivityGraphsHelper;

    private AggregationKind theAggregationKind;

    @SuppressWarnings("deprecation")
    private ChangeableKind theChangeableKind;

    private CollaborationsFactory theCollaborationsFactory;

    private CollaborationsHelper theCollaborationsHelper;

    private CommonBehaviorFactory theCommonBehaviorFactory;

    private CommonBehaviorHelper theCommonBehaviorHelper;

    private ConcurrencyKind theConcurrencyKind;

    private CopyHelper theCopyHelper;

    private CoreFactory theCoreFactory;

    private CoreHelper theCoreHelper;

    private DataTypesFactory theDataTypesFactory;

    private DataTypesHelper theDataTypesHelper;

    private DirectionKind theDirectionKind;

    private ExtensionMechanismsFactory theExtensionMechanismsFactory;

    private ExtensionMechanismsHelper theExtensionMechanismsHelper;

    private Facade theFacade;

    private MetaTypes theMetaTypes;

    private ModelEventPump theModelEventPump;

    private ModelManagementFactory theModelManagementFactory;

    private ModelManagementHelper theModelManagementHelper;

    private OrderingKind theOrderingKind;

    private PseudostateKind thePseudostateKind;

    @SuppressWarnings("deprecation")
    private ScopeKind theScopeKind;

    private StateMachinesFactory theStateMachinesFactory;
    
    private StateMachinesHelper theStateMachinesHelper;
    
    private UmlFactory theUmlFactory;

    private UmlHelper theUmlHelper;

    private UseCasesFactory theUseCasesFactory;

    private UseCasesHelper theUseCasesHelper;

    private VisibilityKind theVisibilityKind;

    /**
     * Constructor.
     */
    public EUMLModelImplementation() {

        registerResourceFactories();
        
//        model = UMLFactory.eINSTANCE.createModel();
//        model.setName("uml2_simple_model");
        
//        theCoreFactory = new CoreFactoryEUMLImpl(this);
//        
//        
//        
//        // Create and start event pump first so it's available for all others
//        theModelEventPump = new ModelEventPumpEUMLImpl(this);
//        theModelEventPump.startPumpingEvents();
//        LOG.debug("EUML Init - event pump started");
//
//        // DataTypes is next so it's available for Kinds, ModelManagement,
//        // & Extensions
//        theDataTypesFactory = new DataTypesFactoryEUMLImpl(this);
//        theDataTypesHelper = new DataTypesHelperEUMLImpl(this);
//
//        theAggregationKind = new AggregationKindEUMLImpl(this);
//        theChangeableKind = new ChangeableKindEUMLImpl(this);
//        theConcurrencyKind = new ConcurrencyKindEUMLImpl(this);
//        theDirectionKind = new DirectionKindEUMLImpl(this);
//        theOrderingKind = new OrderingKindEUMLImpl(this);
//        thePseudostateKind = new PseudostateKindEUMLImpl(this);
//        theScopeKind = new ScopeKindEUMLImpl(this);
//        theVisibilityKind = new VisibilityKindEUMLImpl(this);
//
//        theModelManagementFactory = new ModelManagementFactoryEUMLImpl(this);
//        theExtensionMechanismsHelper =
//            new ExtensionMechanismsHelperEUMLImpl(this);
//        theExtensionMechanismsFactory =
//            new ExtensionMechanismsFactoryEUMLImpl(this);
//        LOG.debug("EUML Init - initialized package Extension mechanism");
//
//        // Initialize remaining factories and helpers
//        // (but defer heavyweight ones until needed)
//        theCopyHelper = new CopyHelperEUMLImpl(this);
//        theActivityGraphsHelper = new ActivityGraphsHelperEUMLImpl(this);
//        theCoreHelper = new CoreHelperEUMLImpl(this);
//        LOG.debug("EUML Init - initialized package Core helper");
//        theModelManagementHelper = new ModelManagementHelperEUMLImpl(this);
//        theStateMachinesHelper = new StateMachinesHelperEUMLImpl(this);
//        LOG.debug("EUML Init - initialized package StateMachines");
//        theUseCasesFactory = new UseCasesFactoryEUMLImpl(this);
//        theUseCasesHelper = new UseCasesHelperEUMLImpl(this);
//        LOG.debug("EUML Init - initialized package Use Cases");
//        theActivityGraphsFactory = new ActivityGraphsFactoryEUMLlImpl(this);
//        LOG.debug("EUML Init - initialized package Collaborations");
//        theCommonBehaviorFactory = new CommonBehaviorFactoryEUMLImpl(this);
//        theCommonBehaviorHelper = new CommonBehaviorHelperEUMLImpl(this);
//        LOG.debug("EUML Init - initialized package CommonBehavior");
//        theStateMachinesFactory = new StateMachinesFactoryEUMLImpl(this);
//        theCoreFactory = new CoreFactoryEUMLImpl(this);
//        LOG.debug("EUML Init - all packages initialized");
    }

    private static void registerResourceFactories() {
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
                UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
                "xmi", UMLResource.Factory.INSTANCE);
    }
    
    public ActivityGraphsFactory getActivityGraphsFactory() {
        if (theActivityGraphsFactory == null) {
            theActivityGraphsFactory = new ActivityGraphsFactoryEUMLlImpl(this);
        }
        return theActivityGraphsFactory;
    }

    public ActivityGraphsHelper getActivityGraphsHelper() {
        if (theActivityGraphsHelper == null) {
            theActivityGraphsHelper = new ActivityGraphsHelperEUMLImpl(this);
        }
        return theActivityGraphsHelper;
    }

    public AggregationKind getAggregationKind() {
        if (theAggregationKind == null) {
            theAggregationKind = new AggregationKindEUMLImpl();
        }
        return theAggregationKind;
    }

    @SuppressWarnings("deprecation")
    public ChangeableKind getChangeableKind() {
        if (theChangeableKind == null) {
            theChangeableKind = new ChangeableKindEUMLImpl();
        }

        return theChangeableKind;
    }

    public CollaborationsFactory getCollaborationsFactory() {
        if (theCollaborationsFactory == null) {
            theCollaborationsFactory = new CollaborationsFactoryEUMLImpl(this);
        }
        return theCollaborationsFactory;
    }

    public CollaborationsHelper getCollaborationsHelper() {
        if (theCollaborationsHelper == null) {
            theCollaborationsHelper = new CollaborationsHelperEUMLImpl(this);
        }
        return theCollaborationsHelper;
    }

    public CommonBehaviorFactory getCommonBehaviorFactory() {
        if (theCommonBehaviorFactory == null) {
            theCommonBehaviorFactory = new CommonBehaviorFactoryEUMLImpl(this);
        }
        return theCommonBehaviorFactory;
    }

    public CommonBehaviorHelper getCommonBehaviorHelper() {
        if (theCommonBehaviorHelper == null) {
            theCommonBehaviorHelper = new CommonBehaviorHelperEUMLImpl(this);
        }
        return theCommonBehaviorHelper;
    }

    public ConcurrencyKind getConcurrencyKind() {
        if (theConcurrencyKind == null) {
            theConcurrencyKind = new ConcurrencyKindEUMLImpl();
        }
        return theConcurrencyKind;
    }

    public CopyHelper getCopyHelper() {
        if (theCopyHelper == null) {
            theCopyHelper = new CopyHelperEUMLImpl(this);
        }
        return theCopyHelper;
    }

    public CoreFactory getCoreFactory() {
        if (theCoreFactory == null) {
            theCoreFactory = new CoreFactoryEUMLImpl(this);
        }
        return theCoreFactory;
    }

    public CoreHelper getCoreHelper() {
        if (theCoreHelper == null) {
            theCoreHelper = new CoreHelperEUMLImpl(this);
        }
        return theCoreHelper;
    }

    public DataTypesFactory getDataTypesFactory() {
        if (theDataTypesFactory == null) {
            theDataTypesFactory = new DataTypesFactoryEUMLImpl(this);
        }
        return theDataTypesFactory;
    }

    public DataTypesHelper getDataTypesHelper() {
        if (theDataTypesHelper == null) {
            theDataTypesHelper = new DataTypesHelperEUMLImpl(this);
        }
        return theDataTypesHelper;
    }

    public DirectionKind getDirectionKind() {
        if (theDirectionKind == null) {
            theDirectionKind = new DirectionKindEUMLImpl();
        }
        return theDirectionKind;
    }

    public ExtensionMechanismsFactory getExtensionMechanismsFactory() {
        if (theExtensionMechanismsFactory == null) {
            theExtensionMechanismsFactory =
                    new ExtensionMechanismsFactoryEUMLImpl(this);
        }
        return theExtensionMechanismsFactory;
    }

    public ExtensionMechanismsHelper getExtensionMechanismsHelper() {
        if (theExtensionMechanismsHelper == null) {
            theExtensionMechanismsHelper =
                    new ExtensionMechanismsHelperEUMLImpl(this);
        }

        return theExtensionMechanismsHelper;
    }

    public Facade getFacade() {
        if (theFacade == null) {
            theFacade = new FacadeEUMLImpl(this);
        }

        return theFacade;
    }

    public MetaTypes getMetaTypes() {
        if (theMetaTypes == null) {
            theMetaTypes = new MetaTypesEUMLImpl(this);
        }
        return theMetaTypes;
    }

    public ModelEventPump getModelEventPump() {
        if (theModelEventPump == null) {
            theModelEventPump = new ModelEventPumpEUMLImpl(this);
        }
        return theModelEventPump;
    }

    public ModelManagementFactory getModelManagementFactory() {
        if (theModelManagementFactory == null) {
            theModelManagementFactory =
                    new ModelManagementFactoryEUMLImpl(this);
        }
        return theModelManagementFactory;
    }

    public ModelManagementHelper getModelManagementHelper() {
        if (theModelManagementHelper == null) {
            theModelManagementHelper = new ModelManagementHelperEUMLImpl(this);
        }
        return theModelManagementHelper;
    }

    public OrderingKind getOrderingKind() {
        if (theOrderingKind == null) {
            theOrderingKind = new OrderingKindEUMLImpl();
        }

        return theOrderingKind;
    }

    public PseudostateKind getPseudostateKind() {
        if (thePseudostateKind == null) {
            thePseudostateKind = new PseudostateKindEUMLImpl();
        }
        return thePseudostateKind;
    }

    @SuppressWarnings("deprecation")
    public ScopeKind getScopeKind() {
        if (theScopeKind == null) {
            theScopeKind = new ScopeKindEUMLImpl();
        }
        return theScopeKind;
    }


    public StateMachinesFactory getStateMachinesFactory() {
        if (theStateMachinesFactory == null) {
            theStateMachinesFactory = new StateMachinesFactoryEUMLImpl(this);
        }
        return theStateMachinesFactory;
    }

    public StateMachinesHelper getStateMachinesHelper() {
        if (theStateMachinesHelper == null) {
            theStateMachinesHelper = new StateMachinesHelperEUMLImpl(this);
        }
        return theStateMachinesHelper;
    }
    
    public UmlFactory getUmlFactory() {
        if (theUmlFactory == null) {
            theUmlFactory = new UmlFactoryEUMLImpl(this);
        }
        return theUmlFactory;
    }

    public UmlHelper getUmlHelper() {
        if (theUmlHelper == null) {
            theUmlHelper = new UmlHelperEUMLImpl(this);
        }
        return theUmlHelper;
    }

    public UseCasesFactory getUseCasesFactory() {
        if (theUseCasesFactory == null) {
            theUseCasesFactory = new UseCasesFactoryEUMLImpl(this);
        }
        return theUseCasesFactory;
    }

    public UseCasesHelper getUseCasesHelper() {
        if (theUseCasesHelper == null) {
            theUseCasesHelper = new UseCasesHelperEUMLImpl(this);
        }
        return theUseCasesHelper;
    }

    public VisibilityKind getVisibilityKind() {
        if (theVisibilityKind == null) {
            theVisibilityKind = new VisibilityKindEUMLImpl();
        }

        return theVisibilityKind;
    }

    public XmiReader getXmiReader() throws UmlException {
        return new XmiReaderEUMLImpl(this);
    }

    public XmiWriter getXmiWriter(Object model, Writer writer, String version)
        throws UmlException {
        return new XmiWriterEUMLImpl(this, model, writer, version);
    }

    public DiagramInterchangeModel getDiagramInterchangeModel() {
        // TODO Auto-generated method stub
        return null;
    }

}
