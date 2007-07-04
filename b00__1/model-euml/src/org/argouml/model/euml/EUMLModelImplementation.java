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

/*
 * This implementation uses ideas and code snippets from the 
 * "org.eclipse.uml2.uml.editor.presentation" package
 * 
 * The package "org.eclipse.uml2.uml.editor.presentation" is part of the
 * Eclipse UML2 plugin and it is available under the terms of the Eclipse Public License v1.0
 * 
 * The Eclipse Public License v1.0 is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.argouml.model.euml;

import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

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
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.uml2.common.edit.domain.UML2AdapterFactoryEditingDomain;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.eclipse.uml2.uml.edit.providers.UMLReflectiveItemProviderAdapterFactory;
import org.eclipse.uml2.uml.edit.providers.UMLResourceItemProviderAdapterFactory;

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
     * This keeps track of the editing domain that is used to track all changes to the model.
     */
    private AdapterFactoryEditingDomain editingDomain;
    
    /**
     * This is the one adapter factory used for providing views of the model.
     */
    private ComposedAdapterFactory adapterFactory;
    
    /**
     * Constructor.
     */
    public EUMLModelImplementation() {
//	theModelEventPump = new ModelEventPumpEUMLImpl(this);
//        LOG.debug("EUML Init - event pump started"); //$NON-NLS-1$

	initializeEditingDomain();
	LOG.debug("EUML Init - editing domain initialized"); //$NON-NLS-1$
	
//        theModelEventPump.startPumpingEvents();

        // We may want to initialize some basic packages first,
        // but we should try to use lazy initialization for most/all.
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
    }
    
//    /**
//     * This sets up {@link org.eclipse.emf.ecore.resource.Resource.Factory.Registry Resource.Factory.Registry} and
//     * {@link org.eclipse.emf.ecore.resource.URIConverter URIConverter} for
//     * the ArgoUML standalone (not the Eclipse plugin) application.
//     * <p>
//     * You must define a "eUML.org_eclipse_uml2_uml_resources" property to point to the
//     * location of the "org.eclipse.uml2.uml.resources" jar plugin.
//     * TODO: exemplu cale jar
//     */
//    private void initializeRegisters() {
//	Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
//		UML22UMLResource.FILE_EXTENSION,
//		UML22UMLResource.Factory.INSTANCE);
//	Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
//		XMI2UMLResource.FILE_EXTENSION,
//		XMI2UMLResource.Factory.INSTANCE);
//	Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
//		UMLResource.FILE_EXTENSION,
//		UMLResource.Factory.INSTANCE);
//	
//	URIConverter.URI_MAP.putAll(UML22UMLExtendedMetaData.getURIMap());
//	URIConverter.URI_MAP.putAll(XMI2UMLExtendedMetaData.getURIMap());
//	
//	String uriPath = System.getProperty("eUML.org_eclipse_uml2_uml_resources"); //$NON-NLS-1$
//	if (uriPath == null)
//	    throw(new RuntimeException("'eUML.org_eclipse_uml2_uml_resources' property not defined"));
//	URI uri = URI.createURI(uriPath);
//	
//	URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
//		uri.appendSegment("libraries").appendSegment("")); //$NON-NLS-1$ //$NON-NLS-2$
//	URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
//		uri.appendSegment("metamodels").appendSegment("")); //$NON-NLS-1$ //$NON-NLS-2$
//	URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP),
//		uri.appendSegment("profiles").appendSegment("")); //$NON-NLS-1$ //$NON-NLS-2$
//    }
    
    /**
     * This sets up the editing domain for the model editor.
     */
    private void initializeEditingDomain() {
	List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
	factories.add(new UMLResourceItemProviderAdapterFactory());
	factories.add(new UMLItemProviderAdapterFactory());
	factories.add(new EcoreItemProviderAdapterFactory());
	factories.add(new UMLReflectiveItemProviderAdapterFactory());

	adapterFactory = new ComposedAdapterFactory(factories);

	BasicCommandStack commandStack = new BasicCommandStack() {

	    @Override
	    protected void handleError(Exception exception) {
		super.handleError(exception);
		LOG.error("Command error - " + exception); //$NON-NLS-1$
	    }

	};

	commandStack.addCommandStackListener(new CommandStackListener() {

	    public void commandStackChanged(final EventObject event) {
		LOG.debug("Command stack - " + event); //$NON-NLS-1$
	    }

	});

	editingDomain = new UML2AdapterFactoryEditingDomain(adapterFactory,
		commandStack);
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

    public XmiWriter getXmiWriter(Object model, OutputStream stream,
            String version) throws UmlException {
        return new XmiWriterEUMLImpl(this, model, stream, version);
    }
    
    public DiagramInterchangeModel getDiagramInterchangeModel() {
        // TODO Auto-generated method stub
        return null;
    }



}
