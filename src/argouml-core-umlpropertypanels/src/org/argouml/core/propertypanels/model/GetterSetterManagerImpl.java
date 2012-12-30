/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling - Original implementation
 *    Thomas Neustupny
 *    Michiel van der Wulp
 *******************************************************************************
 */

package org.argouml.core.propertypanels.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Command;
import org.argouml.kernel.NonUndoableCommand;
import org.argouml.kernel.ProfileConfiguration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.ModelManagementHelper;
import org.argouml.model.PseudostateKind;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLAddDialog;
import org.argouml.util.ArgoFrame;

/**
 * Property getters and setters for UML1.4.
 * @author Bob Tarling
 */
class GetterSetterManagerImpl extends GetterSetterManager {

    private static final Logger LOG =
        Logger.getLogger(GetterSetterManagerImpl.class.getName());

    /**
     * The constructor
     */
    public GetterSetterManagerImpl(Class<?> type) {
        build(type);
    }

    /**
     * Create all the getter/setters for this implementation
     */
    private void build(Class<?> type) {
        addGetterSetter("action", new ActionGetterSetter());
        addGetterSetter("actualArgument", new ArgumentGetterSetter());
        addGetterSetter("aggregation", new AggregationGetterSetter());
        addGetterSetter("baseClass", new BaseClassGetterSetter());
        addGetterSetter("base", new BaseGetterSetter());
        addGetterSetter("body", new MethodExpressionGetterSetter());
        addGetterSetter("changeability", new ChangeabilityGetterSetter());
        addGetterSetter("classifier", new ClassifierGetterSetter());
        addGetterSetter("concurrency", new ConcurrencyGetterSetter());
        addGetterSetter("deferrableEvent", new DeferrableEventGetterSetter());
        addGetterSetter("definedTag", new TagDefinitionGetterSetter());
        addGetterSetter("derived", new DerivedGetterSetter());
        addGetterSetter("doActivity", new DoActivityActionGetterSetter());
        addGetterSetter("entry", new EntryActionGetterSetter());
        addGetterSetter("exit", new ExitActionGetterSetter());
        addGetterSetter("extensionPoint", new ExtensionPointGetterSetter());
        addGetterSetter("effect", new EffectGetterSetter());
        addGetterSetter("elementImport", new ElementImportGetterSetter());
        addGetterSetter("feature", new FeatureGetterSetter());
        addGetterSetter("guard", new GuardGetterSetter());
        addGetterSetter("internalTransition", new InternalTransitionGetterSetter());
        addGetterSetter("isAbstract", new AbstractGetterSetter());
        addGetterSetter("isActive", new ActiveGetterSetter());
        addGetterSetter("isLeaf", new LeafGetterSetter());
        addGetterSetter("isNavigable", new NavigableGetterSetter());
        addGetterSetter("isQuery", new QueryGetterSetter());
        addGetterSetter("isRoot", new RootGetterSetter());
        addGetterSetter("isSynch", new SynchGetterSetter());
        addGetterSetter("isAsynchronous", new AsynchronousGetterSetter());
        addGetterSetter("kind", new ParameterDirectionGetterSetter());
        addGetterSetter("literal", new LiteralGetterSetter());
        addGetterSetter("message", new MessageGetterSetter());
        addGetterSetter("method", new MethodGetterSetter());
        addGetterSetter("navigable", new NavigableGetterSetter());
        addGetterSetter("ordering", new OrderingGetterSetter());
        addGetterSetter("ownedElement", new OwnedElementGetterSetter());
        addGetterSetter("ownerScope", new OwnerScopeGetterSetter());
        addGetterSetter("parameter", new ParameterGetterSetter());
        addGetterSetter("qualifier", new QualifierGetterSetter());
        addGetterSetter("raisedException", new RaisedExceptionGetterSetter());
        addGetterSetter("raisedSignal", new RaisedExceptionGetterSetter());
        addGetterSetter("receiver", new ReceiverGetterSetter());
        addGetterSetter("reception", new ReceptionGetterSetter());
        addGetterSetter("region", new RegionGetterSetter());
        addGetterSetter("residentElement", new ResidentElementGetterSetter());
        addGetterSetter("sender", new SenderGetterSetter());
        addGetterSetter("subvertex", new SubvertexGetterSetter());
        addGetterSetter("targetScope", new TargetScopeGetterSetter());
        addGetterSetter("templateParameter", new TemplateParameterGetterSetter());
        addGetterSetter("trigger", new TriggerGetterSetter());
        addGetterSetter("visibility", new VisibilityGetterSetter());

        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            // UML1.4 only
            addGetterSetter("association", new AssociationEndGetterSetter());
        } else {
            // UML2 only
            addGetterSetter("ownedOperation", new FeatureGetterSetter());
            addGetterSetter("association", new AssociationGetterSetter());
        }
    }

    /**
     * Helper method for adding a new getter/setter
     * @param propertyName
     * @param bgs
     */
    private void addGetterSetter(String propertyName, GetterSetter bgs) {
        getterSetterByPropertyName.put(propertyName, bgs);
    }

    /**
     * Set a UML property by property name
     * @param handle the element to which a property must be set
     * @param value the new property value
     * @param propertyName the property name
     */
    public void set(Object handle, Object value, String propertyName) {
        GetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs != null) {
            bgs.set(handle, value);
        }
    }

    public Object get(Object handle, String propertyName, Class<?> type) {
        GetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs != null) {
            return bgs.get(handle, type);
        }

        return null;
    }


    public Collection getOptions(
            final Object umlElement,
            final String propertyName,
            final Collection<Class<?>> types) {
        GetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs instanceof OptionGetterSetter) {

            LOG.log(Level.FINE, "OptionGetterSetter found for {0} of {1}", new Object[]{propertyName, bgs});

            final OptionGetterSetter ogs = (OptionGetterSetter) bgs;
            return ogs.getOptions(umlElement, types);
        }

        return null;
    }

    public boolean isFullBuildOnly(
            final String propertyName) {
        GetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs instanceof ListGetterSetter) {
            return ((ListGetterSetter) bgs).isFullBuildOnly();
        }

        return false;
    }


    public Object create(String propertyName, String language, String body) {
        GetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs instanceof ExpressionGetterSetter) {
            return ((ExpressionGetterSetter) bgs).create(language, body);
        }

        return null;
    }

    public boolean isValidElement(
            final String propertyName,
            final Collection<Class<?>> types,
            final Object element) {
        GetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs instanceof ListGetterSetter) {
            return ((ListGetterSetter) bgs).isValidElement(element, types);
        }

        return false;
    }

    public Object getMetaType(String propertyName) {
        GetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs instanceof ListGetterSetter) {
            return ((ListGetterSetter) bgs).getMetaType();
        }

        return null;
    }

    @Override
    public Command getAddCommand(String propertyName, Object umlElement) {
        LOG.log(Level.INFO, "Finding getter/setter for {0}", propertyName);

        GetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs instanceof Addable) {
            LOG.log(Level.INFO, "Returning add command");
            return ((Addable) bgs).getAddCommand(umlElement);
        }
        return null;
    }

    @Override
    public List<Command> getAdditionalCommands(
	    final String propertyName, final Object umlElement) {
        GetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs instanceof ListGetterSetter) {
            return ((ListGetterSetter) bgs).getAdditionalCommands(umlElement);
        }
	return null;
    }

    @Override
    public Command getRemoveCommand(
	    final String propertyName, Object umlElement,
	    final Object objectToRemove) {
	GetterSetter bgs = getterSetterByPropertyName.get(propertyName);
	if (bgs instanceof Removeable) {
            return ((Removeable) bgs).getRemoveCommand(
        	    umlElement, objectToRemove);
        }
	return null;
    }

    private interface Addable {
        Command getAddCommand(Object umlElement);
    }

    private interface Removeable {
	Command getRemoveCommand(Object umlElement, Object objectToRemove);
    }


    /**
     * The getter/setter for the Absrtact property
     * @author Bob Tarling
     */
    private class AbstractGetterSetter extends GetterSetter {
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().isAbstract(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setAbstract(modelElement, (Boolean) value);
        }
    }

    private class LeafGetterSetter extends GetterSetter {
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().isLeaf(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setLeaf(modelElement, (Boolean) value);
        }
    }

    private class RootGetterSetter extends GetterSetter {
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().isRoot(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setRoot(modelElement, (Boolean) value);
        }
    }

    private class ActiveGetterSetter extends GetterSetter {
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().isActive(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setActive(modelElement, (Boolean) value);
        }
    }

    private class OwnerScopeGetterSetter extends GetterSetter {
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().isStatic(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setStatic(modelElement, (Boolean) value);
        }
    }

    private class TagDefinitionGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return new LinkedList<String>(
                    Model.getFacade().getTagDefinitions(modelElement));
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
                return Model.getMetaTypes().getTagDefinition();
            } else {
                return Model.getMetaTypes().getProperty();
            }
        }
    }

    private class TargetScopeGetterSetter extends GetterSetter {
        // Have we handled UML2 here?
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().isStatic(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setStatic(modelElement, (Boolean) value);
        }
    }

    private class QueryGetterSetter extends GetterSetter {
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().isQuery(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setQuery(modelElement, (Boolean) value);
        }
    }

    private class NavigableGetterSetter extends GetterSetter {
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().isNavigable(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setNavigable(modelElement, (Boolean) value);
        }
    }

    private class AsynchronousGetterSetter extends GetterSetter {
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().isAsynchronous(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCommonBehaviorHelper().setAsynchronous(modelElement, (Boolean) value);
        }
    }

    private class SynchGetterSetter extends GetterSetter {
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().isSynch(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getActivityGraphsHelper().setSynch(modelElement, (Boolean) value);
        }
    }

    private class OrderingGetterSetter extends GetterSetter {
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().getOrdering(modelElement) ==
                Model.getOrderingKind().getOrdered();
        }
        public void set(Object modelElement, Object value) {
            if ((Boolean) value) {
                Model.getCoreHelper().setOrdering(modelElement,
                        Model.getOrderingKind().getOrdered());
            } else {
                Model.getCoreHelper().setOrdering(modelElement,
                        Model.getOrderingKind().getUnordered());
            }
        }
    }

    private class DerivedGetterSetter extends GetterSetter {

        /**
         * Derived is not a true UML property but is in fact a pseudo property
         * stored in a tag named "derived"
         */
        private static final String TagName = "derived";

        public Object get(Object modelElement, Class<?> type) {
            Object tv = Model.getFacade().getTaggedValue(modelElement, TagName);
            if (tv != null) {
                String tag = Model.getFacade().getValueOfTag(tv);
                return ("true".equals(tag));
            }
            return false;
        }
        public void set(Object modelElement, Object value) {
            Object taggedValue = Model.getFacade().getTaggedValue(modelElement, TagName);
            if (taggedValue == null) {
                taggedValue =
                        Model.getExtensionMechanismsFactory().buildTaggedValue(
                                TagName, "");
                Model.getExtensionMechanismsHelper().addTaggedValue(
                        modelElement, taggedValue);
            }
            if ((Boolean) value) {
                Model.getCommonBehaviorHelper().setValue(taggedValue, "true");
            } else {
                Model.getCommonBehaviorHelper().setValue(taggedValue, "false");
            }
        }
    }


    public class VisibilityGetterSetter extends OptionGetterSetter {

        /**
         * Identifier for public visibility.
         */
        public static final String PUBLIC = "public";

        /**
         * Identifier for protected visibility.
         */
        public static final String PROTECTED = "protected";

        /**
         * Identifier for private visibility.
         */
        public static final String PRIVATE = "private";

        /**
         * Identifier for package visibility.
         */
        public static final String PACKAGE = "package";

        public VisibilityGetterSetter() {
            setOptions(Arrays.asList((new String[] {PUBLIC, PACKAGE, PROTECTED, PRIVATE})));
        }

        public Object get(Object modelElement, Class<?> type) {
            Object kind = Model.getFacade().getVisibility(modelElement);
            if (kind == null) {
                return null;
            } else if (kind.equals(Model.getVisibilityKind().getPublic())) {
                return PUBLIC;
            } else if (kind.equals(Model.getVisibilityKind().getPackage())) {
                return PACKAGE;
            } else if (kind.equals(Model.getVisibilityKind().getProtected())) {
                return PROTECTED;
            } else if (kind.equals(Model.getVisibilityKind().getPrivate())) {
                return PRIVATE;
            } else {
                return PUBLIC;
            }
        }

        public void set(Object modelElement, Object value) {
            Object kind = null;
            if (value.equals(PUBLIC)) {
                kind = Model.getVisibilityKind().getPublic();
            } else if (value.equals(PROTECTED)) {
                kind = Model.getVisibilityKind().getProtected();
            } else if (value.equals(PACKAGE)) {
                kind = Model.getVisibilityKind().getPackage();
            } else {
                kind = Model.getVisibilityKind().getPrivate();
            }
            Model.getCoreHelper().setVisibility(modelElement, kind);
        }
    }


    private class AggregationGetterSetter extends OptionGetterSetter {

        /**
         * Identifier for aggregate aggregation kind.
         */
        public static final String AGGREGATE = "aggregate";

        /**
         * Identifier for composite aggregation kind.
         */
        public static final String COMPOSITE = "composite";

        /**
         * Identifier for no aggregation kind.
         */
        public static final String NONE = "none";

        public AggregationGetterSetter() {
            setOptions(Arrays.asList(new String[] {AGGREGATE, COMPOSITE, NONE}));
        }

        public Object get(Object modelElement, Class<?> type) {
            Object kind = Model.getFacade().getAggregation(modelElement);
            if (kind == null) {
                return null;
            } else if (kind.equals(Model.getAggregationKind().getNone())) {
                return NONE;
            } else if (kind.equals(Model.getAggregationKind().getAggregate())) {
                return AGGREGATE;
            } else if (kind.equals(Model.getAggregationKind().getComposite())) {
                return COMPOSITE;
            } else {
                return NONE;
            }
        }

        public void set(Object modelElement, Object value) {
            Object kind = null;

            if (value.equals(AGGREGATE)) {
                kind = Model.getAggregationKind().getAggregate();
            } else if (value.equals(COMPOSITE)) {
                kind = Model.getAggregationKind().getComposite();
            } else {
                kind = Model.getAggregationKind().getNone();
            }
            Model.getCoreHelper().setAggregation(modelElement, kind);

        }
    }

    private class ParameterDirectionGetterSetter extends OptionGetterSetter {

        /**
         * Identifier for an "in" parameter.
         */
        public static final String IN = "in";

        /**
         * Identifier for an "out" parameter.
         */
        public static final String OUT = "out";

        /**
         * Identifier for an "in/out" parameter.
         */
        public static final String INOUT = "inout";

        /**
         * Identifier for a "return" parameter.
         */
        public static final String RETURN = "return";

        public ParameterDirectionGetterSetter() {
            setOptions(Arrays.asList(new String[] {
                    IN,
                    OUT,
                    INOUT,
                    RETURN}));
        }

        public Object get(Object modelElement, Class<?> type) {
            Object kind = Model.getFacade().getKind(modelElement);
            if (kind == null) {
                return null;
            } else if (kind.equals(Model.getDirectionKind().getInParameter())) {
                return IN;
            } else if (kind.equals(Model.getDirectionKind().getInOutParameter())) {
                return INOUT;
            } else if (kind.equals(Model.getDirectionKind().getOutParameter())) {
                return OUT;
            } else {
                return RETURN;
            }
        }

        public void set(Object modelElement, Object value) {
            Object kind = null;
            if (value == null) {
                kind = null;
            } else if (value.equals(IN)) {
                kind = Model.getDirectionKind().getInParameter();
            } else if (value.equals(OUT)) {
                kind = Model.getDirectionKind().getOutParameter();
            } else if (value.equals(INOUT)) {
                kind = Model.getDirectionKind().getInOutParameter();
            } else if (value.equals(RETURN)) {
                kind = Model.getDirectionKind().getReturnParameter();
            }
            Model.getCoreHelper().setKind(modelElement, kind);

        }
    }


    private class ConcurrencyGetterSetter extends OptionGetterSetter {

        /**
         * Identifier for sequential concurrency.
         */
        public static final String SEQUENTIAL= "sequential";

        /**
         * Identifier for guarded concurrency.
         */
        public static final String GUARDED = "guarded";

        /**
         * Identifier for concurrent concurrency.
         */
        public static final String CONCURRENT = "concurrent";

        /**
         * Identifier for no defined concurrency.
         */
        public static final String UNDEFINED = "undefined";

        public ConcurrencyGetterSetter() {
            setOptions(Arrays.asList(new String[] {
                    SEQUENTIAL,
                    GUARDED,
                    CONCURRENT,
                    UNDEFINED}));
        }

        public Object get(Object modelElement, Class<?> type) {
            Object kind = Model.getFacade().getConcurrency(modelElement);
            if (kind == null) {
                return UNDEFINED;
            } else if (kind.equals(Model.getConcurrencyKind().getSequential())) {
                return SEQUENTIAL;
            } else if (kind.equals(Model.getConcurrencyKind().getGuarded())) {
                return GUARDED;
            } else if (kind.equals(Model.getConcurrencyKind().getConcurrent())) {
                return CONCURRENT;
            } else {
                return UNDEFINED;
            }
        }

        public void set(Object modelElement, Object value) {
            Object kind = null;
            if (value.equals(SEQUENTIAL)) {
                kind = Model.getConcurrencyKind().getSequential();
            } else if (value.equals(GUARDED)) {
                kind = Model.getConcurrencyKind().getGuarded();
            } else if (value.equals(CONCURRENT)) {
                kind = Model.getConcurrencyKind().getConcurrent();
            }
            Model.getCoreHelper().setConcurrency(modelElement, kind);
        }
    }


    private class ChangeabilityGetterSetter extends OptionGetterSetter {

        /**
         * Identifier for addonly changeability.
         * TODO: Note this should not be in UML2 version
         */
        public static final String ADDONLY = "addonly";

        /**
         * CHANGEABLE_COMMAND determines a changeability kind.
         */
        public static final String CHANGEABLE = "changeable";

        /**
         * FROZEN_COMMAND determines a changeability kind.
         */
        public static final String FROZEN = "frozen";

        public ChangeabilityGetterSetter() {
            setOptions(Arrays.asList(new String[] {ADDONLY, CHANGEABLE, FROZEN}));
        }

        public Object get(Object modelElement, Class<?> type) {
            Object kind = Model.getFacade().getChangeability(modelElement);
            if (kind == null) {
                return null;
            } else if (kind.equals(Model.getChangeableKind().getAddOnly())) {
                return ADDONLY;
            } else if (kind.equals(Model.getChangeableKind().getChangeable())) {
                return CHANGEABLE;
            } else if (kind.equals(Model.getChangeableKind().getFrozen())) {
                return FROZEN;
            } else {
                return CHANGEABLE;
            }
        }

        public void set(Object modelElement, Object value) {
            if (value.equals(CHANGEABLE)) {
                Model.getCoreHelper().setReadOnly(modelElement, false);
            } else if (value.equals(ADDONLY)) {
                Model.getCoreHelper().setChangeability(
                        modelElement, Model.getChangeableKind().getAddOnly());
            } else {
                Model.getCoreHelper().setReadOnly(modelElement, true);
            }
        }
    }

    private class FeatureGetterSetter extends ListGetterSetter {

        /**
         * Get all the features for the model
         * @param modelElement
         * @param types
         * @return
         * @see org.argouml.core.propertypanels.model.GetterSetterManager.OptionGetterSetter#getOptions(java.lang.Object, Collection)
         */
        public Collection getOptions(
                final Object modelElement,
                final Collection<Class<?>> types) {

            if (types.contains(Model.getMetaTypes().getOperation()) && types.contains(Model.getMetaTypes().getReception())) {
                return Model.getFacade().getOperationsAndReceptions(modelElement);
            } else if (types.contains(Model.getMetaTypes().getAttribute())) {
                return Model.getFacade().getAttributes(modelElement);
            } else {
                return Collections.EMPTY_LIST;
            }
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(
                final Object element,
                final Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getOperation();
        }
    }



    private class OwnedElementGetterSetter extends ListGetterSetter {

        /**
         * Get all the owned elements for the namespace
         * @param modelElement
         * @param types
         * @return
         * @see org.argouml.core.propertypanels.model.GetterSetterManager.OptionGetterSetter#getOptions(java.lang.Object, Collection)
         */
        public Collection getOptions(
                final Object modelElement,
                final Collection<Class<?>> types) {

            return Model.getFacade().getOwnedElements(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(
                final Object element,
                final Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getOperation();
        }
    }


    private class RaisedExceptionGetterSetter extends ListGetterSetter {

        /**
         * Get all the owned elements for the namespace
         * @param modelElement
         * @param types
         * @return
         * @see org.argouml.core.propertypanels.model.GetterSetterManager.OptionGetterSetter#getOptions(java.lang.Object, Collection)
         */
        public Collection getOptions(
                final Object modelElement,
                final Collection<Class<?>> types) {
            return Model.getFacade().getRaisedExceptions(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(
                final Object element,
                final Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getSignal();
        }
    }

    private class MethodGetterSetter extends ListGetterSetter {

        /**
         * Get all the method for the operation
         * @param modelElement
         * @param types
         * @return
         * @see org.argouml.core.propertypanels.model.GetterSetterManager.OptionGetterSetter#getOptions(java.lang.Object, Collection)
         */
        public Collection getOptions(
                final Object modelElement,
                final Collection<Class<?>> types) {
            return Model.getFacade().getMethods(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(
                final Object element,
                final Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getMethod();
        }
    }


    private class MessageGetterSetter extends ListGetterSetter {

        /**
         * Get all the method for the operation
         * @param modelElement
         * @param types
         * @return
         * @see org.argouml.core.propertypanels.model.GetterSetterManager.OptionGetterSetter#getOptions(java.lang.Object, Collection)
         */
        public Collection getOptions(
                final Object modelElement,
                final Collection<Class<?>> types) {
            return Model.getFacade().getMessages(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(
                final Object element,
                final Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getMessage();
        }
    }


    private class ArgumentGetterSetter extends ListGetterSetter {

        /**
         * Get all the method for the operation
         * @param modelElement
         * @param types
         * @return
         * @see org.argouml.core.propertypanels.model.GetterSetterManager.OptionGetterSetter#getOptions(java.lang.Object, Collection)
         */
        public Collection getOptions(
                final Object modelElement,
                final Collection<Class<?>> types) {
            return Model.getFacade().getArguments(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(
                final Object element,
                final Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getArgument();
        }
    }


    private class ExtensionPointGetterSetter extends ListGetterSetter {

        /**
         * Get all the extension points
         * @param modelElement
         * @param types
         * @return
         * @see org.argouml.core.propertypanels.model.GetterSetterManager.OptionGetterSetter#getOptions(java.lang.Object, Collection)
         */
        public Collection getOptions(
                final Object modelElement,
                final Collection<Class<?>> types) {
            return Model.getFacade().getExtensionPoints(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(
                final Object element,
                final Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getExtensionPoint();
        }
    }

    private class GuardGetterSetter extends ListGetterSetter {

        /**
         * Get all the guards
         * @param modelElement
         * @param types
         * @return
         * @see org.argouml.core.propertypanels.model.GetterSetterManager.OptionGetterSetter#getOptions(java.lang.Object, Collection)
         */
        public Collection getOptions(
                final Object modelElement,
                final Collection<Class<?>> types) {
            final ArrayList l = new ArrayList(1);
            l.add(Model.getFacade().getGuard(modelElement));
            return l;
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(
                final Object element,
                final Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getGuard();
        }
    }

    private class EffectGetterSetter extends ListGetterSetter {

        /**
         * Get all the effects
         * @param modelElement
         * @param types
         * @return
         * @see org.argouml.core.propertypanels.model.GetterSetterManager.OptionGetterSetter#getOptions(java.lang.Object, Collection)
         */
        public Collection getOptions(
                final Object modelElement,
                final Collection<Class<?>> types) {
            final ArrayList l = new ArrayList(1);
            l.add(Model.getFacade().getEffect(modelElement));
            return l;
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(
                final Object element,
                final Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getAction();
        }
    }

    private class TriggerGetterSetter extends ListGetterSetter {

        /**
         * Get all the effects
         * @param modelElement
         * @param types
         * @return
         * @see org.argouml.core.propertypanels.model.GetterSetterManager.OptionGetterSetter#getOptions(java.lang.Object, Collection)
         */
        public Collection getOptions(
                final Object modelElement,
                final Collection<Class<?>> types) {
            final ArrayList l = new ArrayList(1);
            l.add(Model.getFacade().getTrigger(modelElement));
            return l;
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(
                final Object element,
                final Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getEvent();
        }
    }

    private class ParameterGetterSetter extends ListGetterSetter {

        public Collection getOptions(
        	final Object modelElement,
        	final Collection<Class<?>> types) {
            return Model.getFacade().getParameters(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getParameter();
        }
    }

    private class EntryActionGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            final ArrayList list = new ArrayList(1);
            list.add(Model.getFacade().getEntry(modelElement));
            return list;
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getAction();
        }
    }

    private class ExitActionGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            final ArrayList list = new ArrayList(1);
            list.add(Model.getFacade().getExit(modelElement));
            return list;
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getAction();
        }
    }

    private class DoActivityActionGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            final ArrayList list = new ArrayList(1);
            list.add(Model.getFacade().getDoActivity(modelElement));
            return list;
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getAction();
        }
    }

    private class ActionGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getActions(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getAction();
        }
    }

    private class SubvertexGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getSubvertices(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getState();
        }

        /**
         * Returns additional commands that cannot be deduced from the panel
         * xml or other means. This is currently only used by
         * SubvertexGetterSetter and should be removed as soon as we have some
         * configurable way to replace.
         */
        public List<Command> getAdditionalCommands(Object modelElement) {
            final List<Command> commands = new ArrayList<Command>(6);
            commands.add(new NewPseudoStateCommand(
        	    modelElement, Model.getPseudostateKind().getFork()));
            commands.add(new NewPseudoStateCommand(
        	    modelElement, Model.getPseudostateKind().getJoin()));
            commands.add(new NewPseudoStateCommand(
        	    modelElement, Model.getPseudostateKind().getChoice()));
            commands.add(new NewPseudoStateCommand(
        	    modelElement,
        	    Model.getPseudostateKind().getDeepHistory()));
            commands.add(new NewPseudoStateCommand(
        	    modelElement,
        	    Model.getPseudostateKind().getShallowHistory()));
            commands.add(new NewPseudoStateCommand(
        	    modelElement, Model.getPseudostateKind().getInitial()));
            commands.add(new NewPseudoStateCommand(
        	    modelElement, Model.getPseudostateKind().getJunction()));
            return commands;
        }

        private class NewPseudoStateCommand extends NonUndoableCommand
        	implements IconIdentifiable, Named {

            private final String label;
            private final Object kind;
            private final Icon icon;
            private final Object target;

            /**
             * A Command to create a new pseudostate inside some target
             * model element.
             * @param target the target model element the pseudo state
             * should belong to
             * @param kind the required kind of pseudostate
    s         */
            NewPseudoStateCommand(Object target, Object kind) {
                this.target = target;
                this.kind = kind;
                final String key;
                PseudostateKind kinds = Model.getPseudostateKind();
                if (kind == kinds.getFork()) {
                    key = "label.pseudostate.fork";
                } else if (kind == kinds.getJoin()) {
                    key = "label.pseudostate.join";
                } else if (kind == kinds.getChoice()) {
                    key = "label.pseudostate.choice";
                } else if (kind == kinds.getDeepHistory()) {
                    key = "label.pseudostate.deephistory";
                } else if (kind == kinds.getShallowHistory()) {
                    key = "label.pseudostate.shallowhistory";
                } else if (kind == kinds.getInitial()) {
                    key = "label.pseudostate.initial";
                } else if (kind == kinds.getJunction()) {
                    key = "label.pseudostate.junction";
                } else {
                    throw new IllegalArgumentException(
                	kind + " is not a known PseudostateKind");
                }
                label = Translator.localize(key);
                final String name =
                    Model.getFacade().getName(kind).substring(0,1).toUpperCase()
                    + Model.getFacade().getName(kind).substring(1);
                icon = ResourceLoaderWrapper.lookupIcon(name);
            }

			@Override
			public Object execute() {
                final Object ps =
                    Model.getStateMachinesFactory().buildPseudoState(target);
                if (kind != null) {
                    Model.getCoreHelper().setKind(ps, kind);
                }
                TargetManager.getInstance().setTarget(ps);
				return null;
			}

			public Icon getIcon() {
				return icon;
			}

			public String getName() {
				return label;
			}
        }
    }

    private class TemplateParameterGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            LOG.log(Level.FINE, "Getting template parameters for {0}", modelElement);

            return Model.getFacade().getTemplateParameters(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getTemplateParameter();
        }
    }

    private class ElementImportGetterSetter extends ListGetterSetter implements Addable, Removeable {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getImportedElements(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public boolean isFullBuildOnly() {
        	return true;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getModelElement();
        }

        public Command getAddCommand(Object modelElement) {
        	return new AddElementImportCommand(modelElement);
        }

        public Command getRemoveCommand(Object modelElement, Object objectToRemove) {
        	return new RemoveElementImportCommand(modelElement, objectToRemove);
        }

        private class AddElementImportCommand extends AddModelElementCommand {

            /**
             * Constructor for ActionAddPackageImport.
             */
            public AddElementImportCommand(Object target) {
                super(target);
            }


            protected List getChoices() {
                List list = new ArrayList();
                /* TODO: correctly implement next function
                 * in the model subsystem for
                 * issue 1942: */
                list.addAll(Model.getModelManagementHelper()
                        .getAllPossibleImports(getTarget()));
                return list;
            }


            protected List getSelected() {
                List list = new ArrayList();
                list.addAll(Model.getFacade().getImportedElements(getTarget()));
                return list;
            }


            protected String getDialogTitle() {
                return Translator.localize("dialog.title.add-imported-elements");
            }


            @Override
            protected void doIt(Collection selected) {
            	LOG.log(Level.INFO, "Setting {0} imported elements", selected.size());

                Model.getModelManagementHelper().setImportedElements(getTarget(), selected);
            }
        }

        private class RemoveElementImportCommand
    	    extends NonUndoableCommand {

        	private final Object target;
        	private final Object objectToRemove;

    	    /**
    	     * Constructor for ActionRemovePackageImport.
    	     */
    	    public RemoveElementImportCommand(final Object target, final Object objectToRemove) {
    	        this.target = target;
    	        this.objectToRemove = objectToRemove;
    	    }

    	    /*
    	     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    	     */
    	    public Object execute() {
    	        Model.getModelManagementHelper()
    	            .removeImportedElement(target, objectToRemove);
    	        return null;
    	    }
    	}
    }

    private class DeferrableEventGetterSetter extends ListGetterSetter implements Addable, Removeable {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getDeferrableEvents(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public boolean isFullBuildOnly() {
        	return false;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getEvent();
        }

        public Command getAddCommand(Object modelElement) {
        	return new AddCommand(modelElement);
        }

        public Command getRemoveCommand(Object modelElement, Object objectToRemove) {
        	return new RemoveCommand(modelElement, objectToRemove);
        }

        private class AddCommand extends AddModelElementCommand {

            /**
             * Constructor for ActionAddPackageImport.
             */
            public AddCommand(Object target) {
                super(target);
            }


            protected List getChoices() {
                List list = new ArrayList();

                Object parent =
                    Model.getStateMachinesHelper()
                        .findNamespaceForEvent(getTarget(), null);
                list.addAll(
                    Model.getModelManagementHelper().getAllModelElementsOfKind(
                        parent,
                        Model.getMetaTypes().getEvent()));

                return list;
            }


            protected List getSelected() {
                List list = new ArrayList();
                list.addAll(Model.getFacade().getDeferrableEvents(getTarget()));
                return list;
            }


            protected String getDialogTitle() {
                return Translator.localize("dialog.title.add-events");
            }


            @Override
            protected void doIt(Collection selected) {
                Object state = getTarget();
                assert (Model.getFacade().isAState(state));

                Collection oldOnes = new ArrayList(Model.getFacade()
                        .getDeferrableEvents(state));
                Collection toBeRemoved = new ArrayList(oldOnes);
                for (Object o : selected) {
                    if (oldOnes.contains(o)) {
                        toBeRemoved.remove(o);
                    } else {
                        Model.getStateMachinesHelper().addDeferrableEvent(state, o);
                    }
                }
                for (Object o : toBeRemoved) {
                    Model.getStateMachinesHelper().removeDeferrableEvent(state, o);
                }
            }
        }

        private class RemoveCommand extends NonUndoableCommand {

        	private final Object target;
        	private final Object objectToRemove;

    	    public RemoveCommand(final Object target, final Object objectToRemove) {
    	        this.target = target;
    	        this.objectToRemove = objectToRemove;
    	    }

    	    /*
    	     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    	     */
    	    public Object execute() {
    	        Model.getStateMachinesHelper()
    	            .removeDeferrableEvent(target, objectToRemove);
    	        return null;
    	    }
    	}
    }



    private class ReceptionGetterSetter extends ListGetterSetter implements Addable, Removeable {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getReceptions(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getReception();
        }

        public Command getAddCommand(Object modelElement) {
        	return new AddCommand(modelElement);
        }

        public Command getRemoveCommand(Object modelElement, Object objectToRemove) {
        	return new RemoveCommand(modelElement, objectToRemove);
        }

        private class AddCommand extends AddModelElementCommand {

            /**
             * Constructor for ActionAddPackageImport.
             */
            public AddCommand(final Object target) {
                super(target);
            }


            protected List getChoices() {
                List list = new ArrayList();
                /* TODO: correctly implement next function
                 * in the model subsystem for
                 * issue 1942: */
                Object model =
                    ProjectManager.getManager().getCurrentProject().getModel();
                list.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKind(model,
                            Model.getMetaTypes().getReception()));
                return list;
            }


            protected List getSelected() {
                List list = new ArrayList();
                list.addAll(Model.getFacade().getReceptions(getTarget()));
                return list;
            }


            protected String getDialogTitle() {
                return Translator.localize("dialog.title.add-imported-elements");
            }


            @Override
            protected void doIt(Collection selected) {
                Model.getCommonBehaviorHelper().setReception(getTarget(), selected);
            }
        }

        private class RemoveCommand
    	    extends NonUndoableCommand {

        	private final Object target;
        	private final Object objectToRemove;

    	    /**
    	     * Constructor for ActionRemovePackageImport.
    	     */
    	    public RemoveCommand(final Object target, final Object objectToRemove) {
    	        this.target = target;
    	        this.objectToRemove = objectToRemove;
    	    }

    	    /*
    	     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    	     */
    	    public Object execute() {
    	        Model.getCommonBehaviorHelper()
    	            .removeReception(target, objectToRemove);
    	        return null;
    	    }
    	}
    }

    private class RegionGetterSetter extends ListGetterSetter {

        public Collection getOptions(
        	final Object modelElement,
        	final Collection<Class<?>> types) {
            return Model.getStateMachinesHelper().getRegions(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getRegion();
        }
    }

    private class ResidentElementGetterSetter extends ListGetterSetter implements Addable, Removeable {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            Iterator it = Model.getFacade().getResidentElements(modelElement).iterator();
            ArrayList list = new ArrayList();
            while (it.hasNext()) {
        	list.add(Model.getFacade().getResident(it.next()));
            }
            return list;
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isFullBuildOnly() {
            return true;
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getElementResidence();
        }

        public Command getAddCommand(Object modelElement) {
        	return new AddCommand(modelElement);
        }

        public Command getRemoveCommand(Object modelElement, Object objectToRemove) {
        	return new RemoveCommand(modelElement, objectToRemove);
        }

        private class AddCommand extends AddModelElementCommand {

            /**
             * Constructor for ActionAddPackageImport.
             */
            public AddCommand(final Object target) {
                super(target);
            }


            protected List getChoices() {
                List list = new ArrayList();
                /* TODO: correctly implement next function
                 * in the model subsystem for
                 * issue 1942: */
                Object model =
                    ProjectManager.getManager().getCurrentProject().getModel();
                list.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKind(model,
                            Model.getMetaTypes().getUMLClass()));
                list.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKind(model,
                            Model.getMetaTypes().getInterface()));
                list.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKind(model,
                            Model.getMetaTypes().getDataType()));
                list.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKind(model,
                            Model.getMetaTypes().getDataValue()));
                list.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKind(model,
                            Model.getMetaTypes().getAssociation()));
                list.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKind(model,
                            Model.getMetaTypes().getDependency()));
                list.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKind(model,
                            Model.getMetaTypes().getConstraint()));
                list.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKind(model,
                            Model.getMetaTypes().getSignal()));
                return list;
            }


            protected List getSelected() {
                Iterator it = Model.getFacade().getResidentElements(getTarget()).iterator();
                ArrayList list = new ArrayList();
                while (it.hasNext()) {
            	list.add(Model.getFacade().getResident(it.next()));
                }
                return list;
            }


            protected String getDialogTitle() {
                return Translator.localize("dialog.title.add-residentelements");
            }


            @Override
            protected void doIt(Collection selected) {
                Collection current = Model.getFacade().getResidentElements(getTarget());

                ArrayList toRemove = new ArrayList(current);

                toRemove.removeAll(selected);
                selected.removeAll(current);

                for (Iterator it = toRemove.iterator(); it.hasNext(); ) {
                    Object elementResidenceToDelete = it.next();
                    Model.getCoreHelper().removeElementResidence(getTarget(), elementResidenceToDelete);
                    Model.getUmlFactory().delete(elementResidenceToDelete);
                }
                for (Iterator it = selected.iterator(); it.hasNext(); ) {
                    final Object element = it.next();
                    if (!Model.getFacade().isAElementResidence(element)) {
                        Model.getCoreFactory().buildElementResidence(
                                element, getTarget());
                    }
                }
            }
        }

        private class RemoveCommand
    	    extends NonUndoableCommand {

        	private final Object target;
        	private final Object objectToRemove;

    	    /**
    	     * Constructor for ActionRemovePackageImport.
    	     */
    	    public RemoveCommand(final Object target, final Object objectToRemove) {
    	        this.target = target;
    	        this.objectToRemove = objectToRemove;
    	    }

    	    /*
    	     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    	     */
    	    public Object execute() {
                Collection current = Model.getFacade().getResidentElements(target);
                for (Iterator it = current.iterator(); it.hasNext(); ) {
                    Object elementResidenceToDelete = it.next();
                    if (Model.getFacade().getResident(elementResidenceToDelete) == objectToRemove) {
                        Model.getCoreHelper().removeElementResidence(target, elementResidenceToDelete);
                        Model.getUmlFactory().delete(elementResidenceToDelete);
                        return null;
                    }
                }
    	        return null;
    	    }
    	}
    }



    private abstract class AddModelElementCommand extends NonUndoableCommand {

        private Object target;
        private boolean multiSelect = true;
        private boolean exclusive = true;

        /**
         * Construct a command to add a model element to some list.
         */
        protected AddModelElementCommand(final Object target) {
            if (target == null) {
            	throw new IllegalArgumentException("target expected");
            }
        	this.target = target;
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public Object execute() {
            UMLAddDialog dialog =
                new UMLAddDialog(getChoices(), getSelected(), getDialogTitle(),
                                 isMultiSelect(),
                                 isExclusive());
            int result = dialog.showDialog(ArgoFrame.getFrame());
            if (result == JOptionPane.OK_OPTION) {
                doIt(dialog.getSelected());
            }
            return null;
        }

        /**
         * Returns the choices the user has in the UMLAddDialog. The choices are
         * depicted on the left side of the UMLAddDialog (sorry Arabic users) and
         * can be moved via the buttons on the dialog to the right side. On the
         * right side are the selected modelelements.
         * @return List of choices
         */
        protected abstract List getChoices();


        /**
         * The modelelements already selected BEFORE the dialog is shown.
         * @return List of model elements
         */
        protected abstract List getSelected();

        /**
         * The action that has to be done by ArgoUml after the user clicks ok in the
         * UMLAddDialog.
         * @param selected The choices the user has selected in the UMLAddDialog
         */
        protected abstract void doIt(Collection selected);

        /**
         * Returns the UML model target.
         * @return UML ModelElement
         */
        protected Object getTarget() {
            return target;
        }

        /**
         * Sets the UML model target.
         * @param theTarget The target to set
         */
        public void setTarget(Object theTarget) {
            target = theTarget;
        }

        /**
         * Returns the title of the dialog.
         * @return String
         */
        protected abstract String getDialogTitle();

        /**
         * Returns the exclusive.
         * @return boolean
         */
        public boolean isExclusive() {
            return exclusive;
        }

        /**
         * Returns the multiSelect.
         * @return boolean
         */
        public boolean isMultiSelect() {
            return multiSelect;
        }

        /**
         * Sets the exclusive.
         * @param theExclusive The exclusive to set
         */
        public void setExclusive(boolean theExclusive) {
            exclusive = theExclusive;
        }

        /**
         * Sets the multiSelect.
         * @param theMultiSelect The multiSelect to set
         */
        public void setMultiSelect(boolean theMultiSelect) {
            multiSelect = theMultiSelect;
        }

    }

    private class MethodExpressionGetterSetter extends ExpressionGetterSetter {

        @Override
        public Object get(Object modelElement, Class<?> type) {
            return Model.getFacade().getBody(modelElement);
        }

        @Override
        public void set(Object modelElement, Object value) {
            LOG.log(Level.INFO, "About to call setBody {0} / {1}", new Object[]{modelElement, value});
            Model.getCoreHelper().setBody(modelElement, value);
        }

        @Override
        public Object create(final String language, final String body) {
            return Model.getDataTypesFactory().createProcedureExpression(language, body);
        }
    }

    private class SenderGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getSentStimuli(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {

            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getAttribute();
        }
    }

    private class LiteralGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getEnumerationLiterals(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {

            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getEnumerationLiteral();
        }
    }

    private class ReceiverGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getReceivedStimuli(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {

            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getAttribute();
        }
    }
    private class InternalTransitionGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getInternalTransitions(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {

            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getTransition();
        }
    }


    private class ClassifierGetterSetter extends ListGetterSetter implements Addable, Removeable {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getClassifiers(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getClassifier();
        }

        public Command getAddCommand(Object modelElement) {
            return new AddCommand(modelElement);
        }

        public Command getRemoveCommand(Object modelElement, Object objectToRemove) {
    	    return new RemoveCommand(modelElement, objectToRemove);
        }

        private class AddCommand extends AddModelElementCommand {

            /**
             * Constructor for ActionAddPackageImport.
             */
            public AddCommand(Object target) {
                super(target);
            }

            protected List getChoices() {
                List list = new ArrayList();

                // Get all classifiers in our model
                // TODO: We need the property panels to have some reference to
                // the project they belong to instead of using deprecated
                // functionality
                Project p = ProjectManager.getManager().getCurrentProject();
                Object model = p.getRoot();
                list.addAll(Model.getModelManagementHelper()
                        .getAllModelElementsOfKindWithModel(
                        	model, Model.getMetaTypes().getClassifier()));
                final ProfileConfiguration profileConfiguration =
                    p.getProfileConfiguration();
		final ModelManagementHelper mmh =
		    Model.getModelManagementHelper();
		final Object classifierMetaType =
		    Model.getMetaTypes().getClassifier();
                // Get all classifiers in all top level packages of all profiles
                for (Profile profile : profileConfiguration.getProfiles()) {
                    try {
        		for (Object topPackage : profile.getProfilePackages()) {
        		    if (Model.getFacade().isAModel(topPackage)) {
        	                list.addAll(mmh.getAllModelElementsOfKindWithModel(
            	        	    topPackage, classifierMetaType));
        		    }
                        }
                    } catch (ProfileException e) {
                        // TODO: We need to rethrow this as some other exception
                        // type but that is too much change for the moment.
                        LOG.log(Level.SEVERE, "Exception", e);
                    }
                }
                return list;
            }

            protected List getSelected() {
                List list = new ArrayList();
                list.addAll(Model.getFacade().getClassifiers(getTarget()));
                return list;
            }


            protected String getDialogTitle() {
                return Translator.localize("dialog.title.add-specifications");
            }


            @Override
            protected void doIt(Collection selected) {
                Model.getCommonBehaviorHelper().setClassifiers(getTarget(), selected);
            }
        }

        private class RemoveCommand
	    extends NonUndoableCommand {

            private final Object target;
            private final Object objectToRemove;

	    /**
	     * Constructor for ActionRemovePackageImport.
	     */
	    public RemoveCommand(final Object target, final Object objectToRemove) {
	        this.target = target;
	        this.objectToRemove = objectToRemove;
	    }

	    /*
	     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	     */
	    public Object execute() {
	        Model.getCommonBehaviorHelper()
	            .removeClassifier(target, objectToRemove);
	        return null;
	    }
	}
    }

    private class BaseClassGetterSetter extends ListGetterSetter implements Addable, Removeable {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            LinkedList<String> list = new LinkedList<String>(
                    Model.getFacade().getBaseClasses(modelElement));
            Collections.sort(list);
            return list;
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Class.class;
        }

        public Command getAddCommand(Object modelElement) {
            return new AddCommand(modelElement);
        }

        public Command getRemoveCommand(Object modelElement, Object objectToRemove) {
    	    return new RemoveCommand(modelElement, objectToRemove);
        }

        private class AddCommand extends AddModelElementCommand {

            final private List<String> metaClasses;

            /**
             * Constructor for ActionAddPackageImport.
             */
            public AddCommand(Object target) {
                super(target);
                metaClasses = initMetaClasses();
            }

            /**
             * Initialize the meta-classes list. <p>
             *
             * All this code is necessary to be independent of
             * model repository implementation,
             * i.e. to ensure that we have a
             * sorted list of strings.
             */
            private List<String> initMetaClasses() {
                Collection<String> tmpMetaClasses =
                    Model.getCoreHelper().getAllMetatypeNames();
                List<String> metaClasses;
                if (tmpMetaClasses instanceof List) {
                    metaClasses = (List<String>) tmpMetaClasses;
                } else {
                    metaClasses = new LinkedList<String>(tmpMetaClasses);
                }
                try {
                    Collections.sort(metaClasses);
                } catch (UnsupportedOperationException e) {
                    // We got passed an unmodifiable List.  Copy it and sort the result
                    metaClasses = new LinkedList<String>(tmpMetaClasses);
                    Collections.sort(metaClasses);
                }

                return metaClasses;
            }

            protected List getChoices() {
        	return Collections.unmodifiableList(metaClasses);
            }

            protected List getSelected() {
                LinkedList<String> list = new LinkedList<String>(
                        Model.getFacade().getBaseClasses(getTarget()));
                Collections.sort(list);
                return list;
            }


            protected String getDialogTitle() {
                return Translator.localize("dialog.title.add-baseclasses");
            }


            @Override
            protected void doIt(Collection selected) {
                Object stereo = getTarget();
                Set<Object> oldSet = new HashSet<Object>(getSelected());
                Set toBeRemoved = new HashSet<Object>(oldSet);

                for (Object o : selected) {
                    if (oldSet.contains(o)) {
                        toBeRemoved.remove(o);
                    } else {
                        Model.getExtensionMechanismsHelper()
                                .addBaseClass(stereo, o);
                    }
                }
                for (Object o : toBeRemoved) {
                    Model.getExtensionMechanismsHelper().removeBaseClass(stereo, o);
                }
            }
        }

        private class RemoveCommand
	    extends NonUndoableCommand {

            private final Object target;
            private final Object objectToRemove;

	    /**
	     * Constructor for RemoveCommand.
	     */
	    public RemoveCommand(final Object target, final Object objectToRemove) {
	        this.target = target;
	        this.objectToRemove = objectToRemove;
	    }

	    /*
	     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	     */
	    public Object execute() {
                Model.getExtensionMechanismsHelper().removeBaseClass(target, objectToRemove);
	        return null;
	    }
	}
    }

    private class BaseGetterSetter extends ListGetterSetter implements Addable, Removeable {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getBases(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getClassifier();
        }

        public Command getAddCommand(Object modelElement) {
            return new AddCommand(modelElement);
        }

        public Command getRemoveCommand(Object modelElement, Object objectToRemove) {
    	    return new RemoveCommand(modelElement, objectToRemove);
        }

        private class AddCommand extends AddModelElementCommand {

            /**
             * Constructor for ActionAddPackageImport.
             */
            public AddCommand(Object target) {
                super(target);
            }

            protected List getChoices() {
                List vec = new ArrayList();
                vec.addAll(Model.getCollaborationsHelper()
                        .getAllPossibleBases(getTarget()));
                return vec;
            }

            protected List getSelected() {
                List list = new ArrayList();
                list.addAll(Model.getFacade().getBases(getTarget()));
                return list;
            }


            protected String getDialogTitle() {
                return Translator.localize("dialog.title.add-bases");
            }


            @Override
            protected void doIt(Collection selected) {
                Object role = getTarget();
                Model.getCollaborationsHelper().setBases(role, selected);
            }
        }

        private class RemoveCommand
	    extends NonUndoableCommand {

            private final Object target;
            private final Object objectToRemove;

	    /**
	     * Constructor for RemoveCommand.
	     */
	    public RemoveCommand(final Object target, final Object objectToRemove) {
	        this.target = target;
	        this.objectToRemove = objectToRemove;
	    }

	    /*
	     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	     */
	    public Object execute() {
		Model.getCollaborationsHelper().removeBase(target, objectToRemove);
	        return null;
	    }
	}
    }

    private class QualifierGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getQualifiers(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getAttribute();
        }
    }

    private class AssociationEndGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getAssociationEnds(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getAssociationEnd();
        }
    }

    private class AssociationGetterSetter extends ListGetterSetter {

        public Collection getOptions(Object modelElement, Collection<Class<?>> types) {
            return Model.getFacade().getAssociations(modelElement);
        }

        public Object get(Object modelElement, Class<?> type) {
            // not needed
            return null;
        }

        public void set(Object element, Object x) {
            // not needed
        }

        public boolean isValidElement(Object element, Collection<Class<?>> types) {
            return getOptions(element, types).contains(element);
        }

        public Object getMetaType() {
            return Model.getMetaTypes().getAssociationEnd();
        }
    }
}
