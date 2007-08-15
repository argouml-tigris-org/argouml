// $Id$
// Copyright (c) 2004-2007 The Regents of the University of California. All
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

package org.argouml.model;

import java.io.OutputStream;
import java.io.Writer;


/**
 * This is the root class of the Model subsystem. All other subsystems
 * can retrieve the correct version of the API from this class.<p>
 *
 * Notice that all API's returned from this class are to be independent
 * of and specific UML model implementation.<p>
 *
 * For this to work the Model subsystem needs to be initialized with a
 * {@link ModelImplementation}. This is done by calling the static member
 * {@link #setImplementation(ModelImplementation)}.
 *
 * @stereotype utility
 * @since 0.15.5
 * @author Linus Tolke
 */
public final class Model {
    /**
     * The decorated helper.
     */
    private static ActivityGraphsHelper activityGraphsHelper;

    /**
     * The decorated helper.
     */
    private static CollaborationsHelper collaborationsHelper;

    /**
     * The decorated helper.
     */
    private static CommonBehaviorHelper commonBehaviorHelper;

    /**
     * The decorated helper.
     */
    private static CoreHelper coreHelper;

    /**
     * The decorated helper.
     */
    private static DataTypesHelper dataTypesHelper;

    /**
     * The decorated helper.
     */
    private static ExtensionMechanismsHelper extensionMechanismsHelper;

    /**
     * The decorated helper.
     */
    private static StateMachinesHelper stateMachinesHelper;

    /**
     * The decorated helper.
     */
    private static UmlHelper umlHelper;

    /**
     * The decorated helper.
     */
    private static UseCasesHelper useCasesHelper;

    /**
     * The register for the observer.
     */
    private static MementoCreationObserver mementoCreationObserver;

    /**
     * Constructor to prohibit creation.
     */
    private Model() {
    }

    /**
     * The object used to get objects of the implementation.
     */
    private static ModelImplementation impl;

    /**
     * Selects the implementation.<p>
     *
     * This must be called with a working {@link ModelImplementation}
     * to make the Model subsystem operational.
     *
     * @param newImpl The ModelImplementation object of the selected
     * 		      implementation.
     */
    public static void setImplementation(ModelImplementation newImpl) {
        impl = newImpl;
        activityGraphsHelper = impl.getActivityGraphsHelper();
        collaborationsHelper = impl.getCollaborationsHelper();
        commonBehaviorHelper = impl.getCommonBehaviorHelper();
        coreHelper = impl.getCoreHelper();
        dataTypesHelper = impl.getDataTypesHelper();
        extensionMechanismsHelper = impl.getExtensionMechanismsHelper();
        stateMachinesHelper = impl.getStateMachinesHelper();
        umlHelper = impl.getUmlHelper();
        useCasesHelper = impl.getUseCasesHelper();
    }

    /**
     * Get the facade.<p>
     *
     * The facade is probably the most used interface. It contains recognizers
     * and getters for all method kinds.
     *
     * @return The facade object.
     */
    public static Facade getFacade() {
        return impl.getFacade();
    }

    /**
     * Get the event pump.
     *
     * @return the current ModelEventPump.
     */
    public static ModelEventPump getPump() {
        return impl.getModelEventPump();
    }

    /**
     * Getter for DiagramInterchangeModel.
     *
     * @return the diagram interchange model
     */
    public static DiagramInterchangeModel getDiagramInterchangeModel() {
        return impl.getDiagramInterchangeModel();
    }

    /**
     * Getter for ActivityGraphsFactory.
     *
     * @return the factory
     */
    public static ActivityGraphsFactory getActivityGraphsFactory() {
        return impl.getActivityGraphsFactory();
    }

    /**
     * Getter for the ActivityGraphsHelper.
     *
     * @return the instance of the helper
     */
    public static ActivityGraphsHelper getActivityGraphsHelper() {
        return activityGraphsHelper;
    }

    /**
     * Getter for CollaborationsFactory.
     *
     * @return the factory
     */
    public static CollaborationsFactory getCollaborationsFactory() {
        return impl.getCollaborationsFactory();
    }

    /**
     * Getter for CollaborationsHelper.
     *
     * @return the helper
     */
    public static CollaborationsHelper getCollaborationsHelper() {
        return collaborationsHelper;
    }

    /**
     * Getter for CommonBehaviorFactory.
     *
     * @return the factory
     */
    public static CommonBehaviorFactory getCommonBehaviorFactory() {
        return impl.getCommonBehaviorFactory();
    }

    /**
     * Getter for CommonBehaviorHelper.
     *
     * @return the helper
     */
    public static CommonBehaviorHelper getCommonBehaviorHelper() {
        return commonBehaviorHelper;
    }

    /**
     * Getter for CoreFactory.
     *
     * @return the factory
     */
    public static CoreFactory getCoreFactory() {
        return impl.getCoreFactory();
    }

    /**
     * Getter for CoreHelper.
     *
     * @return The helper.
     */
    public static CoreHelper getCoreHelper() {
        return coreHelper;
    }

    /**
     * Getter for DataTypesFactory.
     *
     * @return the factory
     */
    public static DataTypesFactory getDataTypesFactory() {
        return impl.getDataTypesFactory();
    }

    /**
     * Getter for DataTypesHelper.
     *
     * @return the helper.
     */
    public static DataTypesHelper getDataTypesHelper() {
        return dataTypesHelper;
    }

    /**
     * Getter for ExtensionMechanismsFactory.
     *
     * @return the factory instance.
     */
    public static ExtensionMechanismsFactory getExtensionMechanismsFactory() {
        return impl.getExtensionMechanismsFactory();
    }

    /**
     * Getter for ExtensionMechanismsHelper.
     *
     * @return the helper
     */
    public static ExtensionMechanismsHelper getExtensionMechanismsHelper() {
        return extensionMechanismsHelper;
    }

    /**
     * Getter for ModelManagementFactory.
     *
     * @return the factory
     */
    public static ModelManagementFactory getModelManagementFactory() {
        return impl.getModelManagementFactory();
    }

    /**
     * Getter for ModelManagementHelper.
     *
     * @return The model management helper.
     */
    public static ModelManagementHelper getModelManagementHelper() {
        return impl.getModelManagementHelper();
    }

    /**
     * Getter for StateMachinesFactory.
     *
     * @return the factory
     */
    public static StateMachinesFactory getStateMachinesFactory() {
        return impl.getStateMachinesFactory();
    }

    /**
     * Getter for StateMachinesHelper.
     *
     * @return the helper
     */
    public static StateMachinesHelper getStateMachinesHelper() {
        return stateMachinesHelper;
    }

    /**
     * Getter for UmlFactory.
     *
     * @return the factory
     */
    public static UmlFactory getUmlFactory() {
        return impl.getUmlFactory();
    }

    /**
     * Getter for UmlHelper.
     *
     * @return the helper
     */
    public static UmlHelper getUmlHelper() {
        return umlHelper;
    }

    /**
     * Getter for UseCasesFactory.
     *
     * @return the factory
     */
    public static UseCasesFactory getUseCasesFactory() {
        return impl.getUseCasesFactory();
    }

    /**
     * Getter for UseCasesHelper.
     *
     * @return the helper
     */
    public static UseCasesHelper getUseCasesHelper() {
        return useCasesHelper;
    }

    /**
     * Getter for the MetaTypes object.
     *
     * @return the MetaTypes object.
     */
    public static MetaTypes getMetaTypes() {
        return impl.getMetaTypes();
    }

    // Here follows the interfaces that contain the enums of different
    // kinds in the UML meta-model.
    /**
     * Getter for the ChangeableKind object.
     *
     * @return The object implementing the interface.
     * @deprecated for 0.25.4 by tfmorris.  This enumeration has been
     * removed from UML 2.  Use the getter for the isReadOnly attribute.
     */
    @Deprecated
    public static ChangeableKind getChangeableKind() {
        return impl.getChangeableKind();
    }

    /**
     * Getter for the AggregationKind object.
     *
     * @return The object implementing the interface.
     */
    public static AggregationKind getAggregationKind() {
        return impl.getAggregationKind();
    }

    /**
     * Getter for the PseudostateKind object.
     *
     * @return The object implementing the interface.
     */
    public static PseudostateKind getPseudostateKind() {
        return impl.getPseudostateKind();
    }

    /**
     * Getter for the ScopeKind object.
     *
     * @return The object implementing the interface.
     * @deprecated for 0.25.4 by tfmorris.  This has been removed from 
     * UML 2.  Use the getter for the isStatic attribute instead.
     */
    @Deprecated
    public static ScopeKind getScopeKind() {
        return impl.getScopeKind();
    }

    /**
     * Getter for the ConcurrencyKind object.
     *
     * @return The object implementing the interface.
     */
    public static ConcurrencyKind getConcurrencyKind() {
        return impl.getConcurrencyKind();
    }

    /**
     * Getter for the DirectionKind object.
     *
     * @return The object implementing the interface.
     */
    public static DirectionKind getDirectionKind() {
        return impl.getDirectionKind();
    }

    /**
     * Getter for the OrderingKind object.
     *
     * @return The object implementing the interface.
     */
    public static OrderingKind getOrderingKind() {
        return impl.getOrderingKind();
    }

    /**
     * Getter for the VisibilityKind object.
     *
     * @return The object implementing the interface.
     */
    public static VisibilityKind getVisibilityKind() {
        return impl.getVisibilityKind();
    }

    /**
     * Getter for the XmiReader object.
     *
     * @return the object implementing the XmiReader interface
     * @throws UmlException on any error while reading
     */
    public static XmiReader getXmiReader() throws UmlException {
        return impl.getXmiReader();
    }

    /**
     * Get the XmiWriter object.
     * 
     * @param model
     *            the project member model
     * @param writer
     *            the writer
     * @param version
     *            string to be written into file header as XMI writer version
     * @return the object implementing the XmiWriter interface
     * @throws UmlException
     *             on any error while writing
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #getXmiWriter(Object, OutputStream, String)}.
     */
    @Deprecated
    public static XmiWriter getXmiWriter(Object model, Writer writer,
            String version) throws UmlException {
        return impl.getXmiWriter(model, writer, version);
    }
    
    /**
     * Get the XmiWriter object.
     * 
     * @param model
     *            the project member model
     * @param stream
     *            the stream to write to
     * @param version
     *            string to be written into file header as XMI writer version
     * @return the object implementing the XmiWriter interface
     * @throws UmlException
     *             on any error while writing
     */
    public static XmiWriter getXmiWriter(Object model, OutputStream stream,
            String version) throws UmlException {
        return impl.getXmiWriter(model, stream, version);
    }

    /**
     * Initialise the model repository, based on 
     * the name of a ModelImplementation class. <p>
     * 
     * The name shall include the path, 
     * e.g. "org.argouml.model.mdr.MDRModelImplementation".
     * 
     * @param modelName class name of the model implementation
     * @return null if initialised correctly, the error otherwise
     */
    public static Throwable initialise(String modelName) {
        ModelImplementation newImplementation = null;
        try {
            Class implType = Class.forName(modelName);
            newImplementation = (ModelImplementation) implType.newInstance();
        } catch (ClassNotFoundException e) {
            return e;
        } catch (NoClassDefFoundError e) {
            return e;
        } catch (InstantiationException e) {
            return e;
        } catch (IllegalAccessException e) {
            return e;
        }
        if (newImplementation == null) {
            return new Throwable();
        }
        Model.setImplementation(newImplementation);
        return null;
    }

    /**
     * @return <code>true</code> if the Model subsystem is correctly initiated.
     */
    public static boolean isInitiated() {
        return impl != null;
    }

    /**
     * Allows an external system to register itself to recieve mementos created
     * by the model subsystem.
     *
     * @param observer the interested party
     */
    public static void setMementoCreationObserver(
            MementoCreationObserver observer) {
        mementoCreationObserver = observer;
    }

    /**
     * Gets the external class responsible for handling mementos.
     * @return the MementoCreationObserver
     */
    public static MementoCreationObserver getMementoCreationObserver() {
        return mementoCreationObserver;
    }

    /**
     * Notify any observer that a memento has been created.
     *
     * @param memento The newly created memento.
     */
    public static void notifyMementoCreationObserver(
            ModelMemento memento) {
        MementoCreationObserver mco = getMementoCreationObserver();
        if (mco != null) {
            mco.mementoCreated(memento);
        }
    }

    /**
     * Getter for CopyHelper.
     *
     * @return the helper
     */
    public static CopyHelper getCopyHelper() {
	return impl.getCopyHelper();
    }
}
