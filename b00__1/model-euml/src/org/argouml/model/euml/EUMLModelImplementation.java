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
 * "org.eclipse.uml2.uml.editor.presentation" package.
 * 
 * The package "org.eclipse.uml2.uml.editor.presentation" is part of the
 * Eclipse UML2 plugin and it is available under the terms of 
 * the Eclipse Public License v1.0.
 * 
 * The Eclipse Public License v1.0 is available at 
 * http://www.eclipse.org/legal/epl-v10.html.
 */

package org.argouml.model.euml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.argouml.model.CommandStack;
import org.argouml.model.DiagramInterchangeModel;
import org.argouml.model.ModelImplementation;
import org.argouml.model.UmlException;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.uml2.common.edit.domain.UML2AdapterFactoryEditingDomain;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.edit.providers.UMLItemProviderAdapterFactory;
import org.eclipse.uml2.uml.edit.providers.UMLReflectiveItemProviderAdapterFactory;
import org.eclipse.uml2.uml.edit.providers.UMLResourceItemProviderAdapterFactory;
import org.eclipse.uml2.uml.resource.UML22UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.UML22UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resource.XMI2UMLExtendedMetaData;
import org.eclipse.uml2.uml.resource.XMI2UMLResource;

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

    private ActivityGraphsFactoryEUMLlImpl theActivityGraphsFactory;

    private ActivityGraphsHelperEUMLImpl theActivityGraphsHelper;

    private AggregationKindEUMLImpl theAggregationKind;

    @SuppressWarnings("deprecation")
    private ChangeableKindEUMLImpl theChangeableKind;

    private CollaborationsFactoryEUMLImpl theCollaborationsFactory;

    private CollaborationsHelperEUMLImpl theCollaborationsHelper;

    private CommonBehaviorFactoryEUMLImpl theCommonBehaviorFactory;

    private CommonBehaviorHelperEUMLImpl theCommonBehaviorHelper;

    private ConcurrencyKindEUMLImpl theConcurrencyKind;

    private CopyHelperEUMLImpl theCopyHelper;

    private CoreFactoryEUMLImpl theCoreFactory;

    private CoreHelperEUMLImpl theCoreHelper;

    private DataTypesFactoryEUMLImpl theDataTypesFactory;

    private DataTypesHelperEUMLImpl theDataTypesHelper;

    private DirectionKindEUMLImpl theDirectionKind;

    private ExtensionMechanismsFactoryEUMLImpl theExtensionMechanismsFactory;

    private ExtensionMechanismsHelperEUMLImpl theExtensionMechanismsHelper;

    private FacadeEUMLImpl theFacade;

    private MetaTypesEUMLImpl theMetaTypes;

    private ModelEventPumpEUMLImpl theModelEventPump;

    private ModelManagementFactoryEUMLImpl theModelManagementFactory;

    private ModelManagementHelperEUMLImpl theModelManagementHelper;

    private OrderingKindEUMLImpl theOrderingKind;

    private PseudostateKindEUMLImpl thePseudostateKind;

    @SuppressWarnings("deprecation")
    private ScopeKindEUMLImpl theScopeKind;

    private StateMachinesFactoryEUMLImpl theStateMachinesFactory;

    private StateMachinesHelperEUMLImpl theStateMachinesHelper;

    private UmlFactoryEUMLImpl theUmlFactory;

    private UmlHelperEUMLImpl theUmlHelper;

    private UseCasesFactoryEUMLImpl theUseCasesFactory;

    private UseCasesHelperEUMLImpl theUseCasesHelper;

    private VisibilityKindEUMLImpl theVisibilityKind;
    
    private CommandStackImpl theCommandStack;

    /**
     * This keeps track of the editing domain that is used to track all changes
     * to the model.
     */
    private AdapterFactoryEditingDomain editingDomain;

    /**
     * Constructor.
     */
    public EUMLModelImplementation() {
        initializeEditingDomain();
        LOG.debug("EUML Init - editing domain initialized"); //$NON-NLS-1$
    }

    /**
     * This sets up the editing domain for the model editor.
     */
    private void initializeEditingDomain() {
        // If the eUML.resources system property is defined then we are in a
        // stand alone application, else we're in an Eclipse plug in.
        // The eUML.resource should contain the path to the
        // org.eclipse.uml2.uml.resource jar plugin.
        String path = System.getProperty("eUML.resources"); //$NON-NLS-1$

        BasicCommandStack commandStack = new BasicCommandStack() {

            @Override
            protected void handleError(Exception exception) {
                super.handleError(exception);
                throw new RuntimeException(exception);
            }

        };

        List<AdapterFactory> factories = new ArrayList<AdapterFactory>();
        factories.add(new UMLResourceItemProviderAdapterFactory());
        factories.add(new UMLItemProviderAdapterFactory());
        factories.add(new EcoreItemProviderAdapterFactory());
        factories.add(new UMLReflectiveItemProviderAdapterFactory());
        ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
                factories);

        editingDomain = new UML2AdapterFactoryEditingDomain(
                adapterFactory, commandStack);

        ResourceSet resourceSet = editingDomain.getResourceSet();
        Map<String, Object> extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
        Map<URI, URI> uriMap = resourceSet.getURIConverter().getURIMap();

        if (path != null) {
            try {
                FileInputStream in = new FileInputStream(path);
                in.close();
            } catch (IOException e) {
                throw (new RuntimeException(e));
            }

            path = path.replace('\\', '/');
            if (Character.isLetter(path.charAt(0))) {
                path = '/' + path;
            }
            URI uri = URI.createURI("jar:file:" + path + "!/"); //$NON-NLS-1$ //$NON-NLS-2$
            LOG.debug("eUML.resource URI --> " + uri); //$NON-NLS-1$

            resourceSet.getPackageRegistry().put(
                    UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
            resourceSet.getPackageRegistry().put(
                    EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
            extensionToFactoryMap.put(
                    UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
            uriMap.put(
                    URI.createURI(UMLResource.LIBRARIES_PATHMAP),
                    uri.appendSegment("libraries").appendSegment("")); //$NON-NLS-1$ //$NON-NLS-2$
            uriMap.put(
                    URI.createURI(UMLResource.METAMODELS_PATHMAP),
                    uri.appendSegment("metamodels").appendSegment("")); //$NON-NLS-1$//$NON-NLS-2$
            uriMap.put(
                    URI.createURI(UMLResource.PROFILES_PATHMAP),
                    uri.appendSegment("profiles").appendSegment("")); //$NON-NLS-1$//$NON-NLS-2$
        }

        extensionToFactoryMap.put(
                UML22UMLResource.FILE_EXTENSION,
                UML22UMLResource.Factory.INSTANCE);
        extensionToFactoryMap.put(
                XMI2UMLResource.FILE_EXTENSION,
                XMI2UMLResource.Factory.INSTANCE);
        uriMap.putAll(UML22UMLExtendedMetaData.getURIMap());
        uriMap.putAll(XMI2UMLExtendedMetaData.getURIMap());
    }

    /**
     * Getter for {@link #editingDomain the Editing Domain}
     * 
     * @return the editing domain of the current EUMLModelImplementation
     *         instance
     */
    public EditingDomain getEditingDomain() {
        return editingDomain;
    }

    public ActivityGraphsFactoryEUMLlImpl getActivityGraphsFactory() {
        if (theActivityGraphsFactory == null) {
            theActivityGraphsFactory = 
                new ActivityGraphsFactoryEUMLlImpl(this);
        }
        return theActivityGraphsFactory;
    }

    public ActivityGraphsHelperEUMLImpl getActivityGraphsHelper() {
        if (theActivityGraphsHelper == null) {
            theActivityGraphsHelper = new ActivityGraphsHelperEUMLImpl(this);
        }
        return theActivityGraphsHelper;
    }

    public AggregationKindEUMLImpl getAggregationKind() {
        if (theAggregationKind == null) {
            theAggregationKind = new AggregationKindEUMLImpl();
        }
        return theAggregationKind;
    }

    @SuppressWarnings("deprecation")
    public ChangeableKindEUMLImpl getChangeableKind() {
        if (theChangeableKind == null) {
            theChangeableKind = new ChangeableKindEUMLImpl();
        }
        return theChangeableKind;
    }

    public CollaborationsFactoryEUMLImpl getCollaborationsFactory() {
        if (theCollaborationsFactory == null) {
            theCollaborationsFactory = new CollaborationsFactoryEUMLImpl(this);
        }
        return theCollaborationsFactory;
    }

    public CollaborationsHelperEUMLImpl getCollaborationsHelper() {
        if (theCollaborationsHelper == null) {
            theCollaborationsHelper = new CollaborationsHelperEUMLImpl(this);
        }
        return theCollaborationsHelper;
    }

    public CommonBehaviorFactoryEUMLImpl getCommonBehaviorFactory() {
        if (theCommonBehaviorFactory == null) {
            theCommonBehaviorFactory = new CommonBehaviorFactoryEUMLImpl(this);
        }
        return theCommonBehaviorFactory;
    }

    public CommonBehaviorHelperEUMLImpl getCommonBehaviorHelper() {
        if (theCommonBehaviorHelper == null) {
            theCommonBehaviorHelper = new CommonBehaviorHelperEUMLImpl(this);
        }
        return theCommonBehaviorHelper;
    }

    public ConcurrencyKindEUMLImpl getConcurrencyKind() {
        if (theConcurrencyKind == null) {
            theConcurrencyKind = new ConcurrencyKindEUMLImpl();
        }
        return theConcurrencyKind;
    }

    public CopyHelperEUMLImpl getCopyHelper() {
        if (theCopyHelper == null) {
            theCopyHelper = new CopyHelperEUMLImpl(this);
        }
        return theCopyHelper;
    }

    public CoreFactoryEUMLImpl getCoreFactory() {
        if (theCoreFactory == null) {
            theCoreFactory = new CoreFactoryEUMLImpl(this);
        }
        return theCoreFactory;
    }

    public CoreHelperEUMLImpl getCoreHelper() {
        if (theCoreHelper == null) {
            theCoreHelper = new CoreHelperEUMLImpl(this);
        }
        return theCoreHelper;
    }

    public DataTypesFactoryEUMLImpl getDataTypesFactory() {
        if (theDataTypesFactory == null) {
            theDataTypesFactory = new DataTypesFactoryEUMLImpl(this);
        }
        return theDataTypesFactory;
    }

    public DataTypesHelperEUMLImpl getDataTypesHelper() {
        if (theDataTypesHelper == null) {
            theDataTypesHelper = new DataTypesHelperEUMLImpl(this);
        }
        return theDataTypesHelper;
    }

    public DirectionKindEUMLImpl getDirectionKind() {
        if (theDirectionKind == null) {
            theDirectionKind = new DirectionKindEUMLImpl();
        }
        return theDirectionKind;
    }

    public ExtensionMechanismsFactoryEUMLImpl getExtensionMechanismsFactory() {
        if (theExtensionMechanismsFactory == null) {
            theExtensionMechanismsFactory =
                new ExtensionMechanismsFactoryEUMLImpl(this);
        }
        return theExtensionMechanismsFactory;
    }

    public ExtensionMechanismsHelperEUMLImpl getExtensionMechanismsHelper() {
        if (theExtensionMechanismsHelper == null) {
            theExtensionMechanismsHelper =
                new ExtensionMechanismsHelperEUMLImpl(this);
        }
        return theExtensionMechanismsHelper;
    }

    public FacadeEUMLImpl getFacade() {
        if (theFacade == null) {
            theFacade = new FacadeEUMLImpl(this);
        }
        return theFacade;
    }

    public MetaTypesEUMLImpl getMetaTypes() {
        if (theMetaTypes == null) {
            theMetaTypes = new MetaTypesEUMLImpl(this);
        }
        return theMetaTypes;
    }

    public ModelEventPumpEUMLImpl getModelEventPump() {
        if (theModelEventPump == null) {
            theModelEventPump = new ModelEventPumpEUMLImpl(this);
        }
        return theModelEventPump;
    }

    public ModelManagementFactoryEUMLImpl getModelManagementFactory() {
        if (theModelManagementFactory == null) {
            theModelManagementFactory =
                new ModelManagementFactoryEUMLImpl(this);
        }
        return theModelManagementFactory;
    }

    public ModelManagementHelperEUMLImpl getModelManagementHelper() {
        if (theModelManagementHelper == null) {
            theModelManagementHelper = new ModelManagementHelperEUMLImpl(this);
        }
        return theModelManagementHelper;
    }

    public OrderingKindEUMLImpl getOrderingKind() {
        if (theOrderingKind == null) {
            theOrderingKind = new OrderingKindEUMLImpl();
        }
        return theOrderingKind;
    }

    public PseudostateKindEUMLImpl getPseudostateKind() {
        if (thePseudostateKind == null) {
            thePseudostateKind = new PseudostateKindEUMLImpl();
        }
        return thePseudostateKind;
    }

    @SuppressWarnings("deprecation")
    public ScopeKindEUMLImpl getScopeKind() {
        if (theScopeKind == null) {
            theScopeKind = new ScopeKindEUMLImpl();
        }
        return theScopeKind;
    }


    public StateMachinesFactoryEUMLImpl getStateMachinesFactory() {
        if (theStateMachinesFactory == null) {
            theStateMachinesFactory = new StateMachinesFactoryEUMLImpl(this);
        }
        return theStateMachinesFactory;
    }

    public StateMachinesHelperEUMLImpl getStateMachinesHelper() {
        if (theStateMachinesHelper == null) {
            theStateMachinesHelper = new StateMachinesHelperEUMLImpl(this);
        }
        return theStateMachinesHelper;
    }

    public UmlFactoryEUMLImpl getUmlFactory() {
        if (theUmlFactory == null) {
            theUmlFactory = new UmlFactoryEUMLImpl(this);
        }
        return theUmlFactory;
    }

    public UmlHelperEUMLImpl getUmlHelper() {
        if (theUmlHelper == null) {
            theUmlHelper = new UmlHelperEUMLImpl(this);
        }
        return theUmlHelper;
    }

    public UseCasesFactoryEUMLImpl getUseCasesFactory() {
        if (theUseCasesFactory == null) {
            theUseCasesFactory = new UseCasesFactoryEUMLImpl(this);
        }
        return theUseCasesFactory;
    }

    public UseCasesHelperEUMLImpl getUseCasesHelper() {
        if (theUseCasesHelper == null) {
            theUseCasesHelper = new UseCasesHelperEUMLImpl(this);
        }
        return theUseCasesHelper;
    }

    public VisibilityKindEUMLImpl getVisibilityKind() {
        if (theVisibilityKind == null) {
            theVisibilityKind = new VisibilityKindEUMLImpl();
        }
        return theVisibilityKind;
    }

    public XmiReaderEUMLImpl getXmiReader() throws UmlException {
        return new XmiReaderEUMLImpl(this);
    }

    public XmiWriterEUMLImpl getXmiWriter(Object model, Writer writer, String version)
            throws UmlException {
        return new XmiWriterEUMLImpl(this, model, writer, version);
    }

    public XmiWriterEUMLImpl getXmiWriter(Object model, OutputStream stream,
            String version) throws UmlException {
        return new XmiWriterEUMLImpl(this, model, stream, version);
    }

    public DiagramInterchangeModel getDiagramInterchangeModel() {
        return null;
    }

    public CommandStackImpl getCommandStack() {
        if (theCommandStack == null) {
            theCommandStack = new CommandStackImpl(this);
        }
        return theCommandStack;
    }

}
