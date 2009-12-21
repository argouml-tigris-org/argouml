/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling - Original implementation
 *******************************************************************************
 */

package org.argouml.core.propertypanels.ui;

import org.argouml.model.Model;

/**
 * Property getters and setters for UML1.4
 * @author Bob Tarling
 */
class GetterSetterManagerImpl extends GetterSetterManager {
    
    /**
     * The constructor
     */
    public GetterSetterManagerImpl() {
        build();
    }
    
    /**
     * Create all the getter/setters for this implementation
     */
    private void build() {
        addGetterSetter("isAbstract", new AbstractGetterSetter());
        addGetterSetter("isLeaf", new LeafGetterSetter());
        addGetterSetter("isRoot", new RootGetterSetter());
        addGetterSetter("isActive", new ActiveGetterSetter());
        addGetterSetter("ownerScope", new OwnerScopeGetterSetter());
        addGetterSetter("targetScope", new TargetScopeGetterSetter());
        addGetterSetter("isQuery", new QueryGetterSetter());
        addGetterSetter("isNavigable", new NavigableGetterSetter());
        addGetterSetter("isSynchronous", new AsynchronousGetterSetter());
        addGetterSetter("isSynch", new SynchGetterSetter());
        addGetterSetter("ordering", new OrderingGetterSetter());
        addGetterSetter("navigable", new NavigableGetterSetter());
        addGetterSetter("derived", new DerivedGetterSetter());
        addGetterSetter("visibility", new VisibilityGetterSetter());
        addGetterSetter("aggregation", new AggregationGetterSetter());
        addGetterSetter("kind", new ParameterDirectionGetterSetter());
        addGetterSetter("changeability", new ChangeabilityGetterSetter());
        addGetterSetter("concurrency", new ConcurrencyGetterSetter());
    }
    
    /**
     * Helper method for adding a new getter/setter
     * @param propertyName
     * @param bgs
     */
    private void addGetterSetter(String propertyName, BaseGetterSetter bgs) {
        getterSetterByPropertyName.put(propertyName, bgs);
    }
    
    /**
     * Set a UML property by property name
     * @param handle the element to which a property must be set
     * @param value the new property value
     * @param propertyName the property name
     */
    public void set(Object handle, Object value, String propertyName) {
        BaseGetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs != null) {
            bgs.set(handle, value);
        }
    }
    
    /**
     * Get a UML property by property name
     * @param handle the element from which a property must be return
     * @param value the new property value
     * @param propertyName the property name
     */
    public Object get(Object handle, String propertyName) {
        BaseGetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs != null) {
            return bgs.get(handle);
        }
        
        return null;
    }
    
    public String[] getOptions(String propertyName) {
        BaseGetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs instanceof RadioGetterSetter) {
            return ((RadioGetterSetter) bgs).getOptions();
        }
        
        return null;
    }
    
    /**
     * The getter/setter for the Absrtact property
     * @author Bob Tarling
     */
    private class AbstractGetterSetter extends BaseGetterSetter {
        public Object get(Object modelElement) {
            return Model.getFacade().isAbstract(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setAbstract(modelElement, (Boolean) value);
        }
    }
    
    private class LeafGetterSetter extends BaseGetterSetter {
        public Object get(Object modelElement) {
            return Model.getFacade().isLeaf(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setLeaf(modelElement, (Boolean) value);
        }
    }
    
    private class RootGetterSetter extends BaseGetterSetter {
        public Object get(Object modelElement) {
            return Model.getFacade().isRoot(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setRoot(modelElement, (Boolean) value);
        }
    }
    
    private class ActiveGetterSetter extends BaseGetterSetter {
        public Object get(Object modelElement) {
            return Model.getFacade().isActive(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setActive(modelElement, (Boolean) value);
        }
    }
    
    private class OwnerScopeGetterSetter extends BaseGetterSetter {
        public Object get(Object modelElement) {
            return Model.getFacade().isStatic(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setStatic(modelElement, (Boolean) value);
        }
    }
    
    private class TargetScopeGetterSetter extends BaseGetterSetter {
        // Have we handled UML2 here?
        public Object get(Object modelElement) {
            return Model.getFacade().isStatic(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setStatic(modelElement, (Boolean) value);
        }
    }
    
    private class QueryGetterSetter extends BaseGetterSetter {
        public Object get(Object modelElement) {
            return Model.getFacade().isQuery(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setQuery(modelElement, (Boolean) value);
        }
    }
    
    private class NavigableGetterSetter extends BaseGetterSetter {
        public Object get(Object modelElement) {
            return Model.getFacade().isNavigable(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCoreHelper().setNavigable(modelElement, (Boolean) value);
        }
    }
    
    private class AsynchronousGetterSetter extends BaseGetterSetter {
        public Object get(Object modelElement) {
            return Model.getFacade().isAsynchronous(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getCommonBehaviorHelper().setAsynchronous(modelElement, (Boolean) value);
        }
    }
    
    private class SynchGetterSetter extends BaseGetterSetter {
        public Object get(Object modelElement) {
            return Model.getFacade().isSynch(modelElement);
        }
        public void set(Object modelElement, Object value) {
            Model.getActivityGraphsHelper().setSynch(modelElement, (Boolean) value);
        }
    }
    
    private class OrderingGetterSetter extends BaseGetterSetter {
        public Object get(Object modelElement) {
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
    
    private class DerivedGetterSetter extends BaseGetterSetter {
        
        /**
         * Derived is not a true UML property but is in fact a pseudo property
         * stored in a tag named "derived"
         */
        private static final String TagName = "derived";
        
        public Object get(Object modelElement) {
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
    
    
    public class VisibilityGetterSetter extends RadioGetterSetter {
        
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
            setOptions(new String[] {PUBLIC, PACKAGE, PROTECTED, PRIVATE});
        }
        
        public Object get(Object modelElement) {
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
    
    
    private class AggregationGetterSetter extends RadioGetterSetter {
        
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
            setOptions(new String[] {AGGREGATE, COMPOSITE, NONE});
        }
        
        public Object get(Object modelElement) {
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
    
    private class ParameterDirectionGetterSetter extends RadioGetterSetter {
        
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
            setOptions(new String[] {
                    IN,
                    OUT,
                    INOUT,
                    RETURN});
        }
        
        public Object get(Object modelElement) {
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
    
    
    private class ConcurrencyGetterSetter extends RadioGetterSetter {
        
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

        public ConcurrencyGetterSetter() {
            setOptions(new String[] {
                    SEQUENTIAL,
                    GUARDED,
                    CONCURRENT});
        }
        
        public Object get(Object modelElement) {
            Object kind = Model.getFacade().getConcurrency(modelElement);
            if (kind == null) {
                return null;
            } else if (kind.equals(Model.getConcurrencyKind().getSequential())) {
                return SEQUENTIAL;
            } else if (kind.equals(Model.getConcurrencyKind().getGuarded())) {
                return GUARDED;
            } else if (kind.equals(Model.getConcurrencyKind().getConcurrent())) {
                return CONCURRENT;
            } else {
                return SEQUENTIAL;
            }
        }
        
        public void set(Object modelElement, Object value) {
            Object kind = null;
            if (value.equals(SEQUENTIAL)) {
                kind = Model.getConcurrencyKind().getSequential();
            } else if (value.equals(GUARDED)) {
                kind = Model.getConcurrencyKind().getGuarded();
            } else {
                kind = Model.getConcurrencyKind().getConcurrent();
            }
            Model.getCoreHelper().setConcurrency(modelElement, kind);
        }
    }
    
    
    private class ChangeabilityGetterSetter extends RadioGetterSetter {
        
        /**
         * Identifier for addonly changeability.
         * TODO: Note this should not be ni UML2 version
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
            setOptions(new String[] {ADDONLY, CHANGEABLE, FROZEN});
        }
        
        public Object get(Object modelElement) {
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
}
