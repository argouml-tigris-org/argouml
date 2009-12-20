// $Id: PopupMenuNewEvent.java 15918 2008-10-14 06:40:41Z mvw $

package org.argouml.core.propertypanels.ui;

import org.argouml.model.Model;

/**
 * Property getters and setters
 * @author Bob Tarling
 */
class GetterSetterImpl extends GetterSetter {
    
    public GetterSetterImpl() {
        addGetterSetter(new AbstractGetterSetter());
        addGetterSetter(new LeafGetterSetter());
        addGetterSetter(new RootGetterSetter());
        addGetterSetter(new ActiveGetterSetter());
        addGetterSetter(new OwnerScopeGetterSetter());
        addGetterSetter(new TargetScopeGetterSetter());
        addGetterSetter(new QueryGetterSetter());
        addGetterSetter(new NavigableGetterSetter());
        addGetterSetter(new AsynchronousGetterSetter());
        addGetterSetter(new SynchGetterSetter());
        addGetterSetter(new OrderingGetterSetter());
    }
    
    private void addGetterSetter(BooleanGetterSetter bgs) {
        getterSetterByPropertyName.put(bgs.getPropertyName(), bgs);
    }
    /**
     * Set a UML property by property name
     * @param handle the element to which a property must be set
     * @param value the new property value
     * @param propertyName the property name
     */
    public void set(Object handle, Object value, String propertyName) {
        BooleanGetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs != null) {
            bgs.set(handle, (Boolean) value);
        }
    }
    
    /**
     * Get a UML property by property name
     * @param handle the element from which a property must be return
     * @param value the new property value
     * @param propertyName the property name
     */
    public Object get(Object handle, String propertyName) {
        BooleanGetterSetter bgs = getterSetterByPropertyName.get(propertyName);
        if (bgs != null) {
            return bgs.get(handle);
        }
        
        return null;
    }
    
    private class AbstractGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "isAbstract";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().isAbstract(modelElement);
        }
        public void set(Object modelElement, Boolean value) {
            Model.getCoreHelper().setAbstract(modelElement, value);
        }
    }
    
    private class LeafGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "isLeaf";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().isLeaf(modelElement);
        }
        public void set(Object modelElement, Boolean value) {
            Model.getCoreHelper().setLeaf(modelElement, value);
        }
    }
    
    private class RootGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "isRoot";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().isRoot(modelElement);
        }
        public void set(Object modelElement, Boolean value) {
            Model.getCoreHelper().setRoot(modelElement, value);
        }
    }
    
    private class ActiveGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "isActive";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().isActive(modelElement);
        }
        public void set(Object modelElement, Boolean value) {
            Model.getCoreHelper().setActive(modelElement, value);
        }
    }
    
    private class OwnerScopeGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "ownerScope";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().isStatic(modelElement);
        }
        public void set(Object modelElement, Boolean value) {
            Model.getCoreHelper().setStatic(modelElement, value);
        }
    }
    
    private class TargetScopeGetterSetter extends BooleanGetterSetter {
        // Have we handled UML2 here?
        public String getPropertyName() {
            return "targetScope";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().isStatic(modelElement);
        }
        public void set(Object modelElement, Boolean value) {
            Model.getCoreHelper().setStatic(modelElement, value);
        }
    }
    
    private class QueryGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "isQuery";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().isQuery(modelElement);
        }
        public void set(Object modelElement, Boolean value) {
            Model.getCoreHelper().setQuery(modelElement, value);
        }
    }
    
    private class NavigableGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "navigable";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().isNavigable(modelElement);
        }
        public void set(Object modelElement, Boolean value) {
            Model.getCoreHelper().setNavigable(modelElement, value);
        }
    }
    
    private class AsynchronousGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "isSynchronous";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().isAsynchronous(modelElement);
        }
        public void set(Object modelElement, Boolean value) {
            Model.getCommonBehaviorHelper().setAsynchronous(modelElement, value);
        }
    }
    
    private class SynchGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "isSynch";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().isSynch(modelElement);
        }
        public void set(Object modelElement, Boolean value) {
            Model.getActivityGraphsHelper().setSynch(modelElement, value);
        }
    }
    
    private class OrderingGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "ordering";
        }
        public Boolean get(Object modelElement) {
            return Model.getFacade().getOrdering(modelElement) ==
                Model.getOrderingKind().getOrdered();
        }
        public void set(Object modelElement, Boolean value) {
            if ((Boolean) value) {
                Model.getCoreHelper().setOrdering(modelElement,
                        Model.getOrderingKind().getOrdered());
            } else {
                Model.getCoreHelper().setOrdering(modelElement,
                        Model.getOrderingKind().getUnordered());
            }
        }
    }
    
    private class DerivedGetterSetter extends BooleanGetterSetter {
        public String getPropertyName() {
            return "derived";
        }
        public Boolean get(Object modelElement) {
            Object tv = Model.getFacade().getTaggedValue(modelElement, getPropertyName());
            if (tv != null) {
                String tag = Model.getFacade().getValueOfTag(tv);
                return ("true".equals(tag));
            }
            return false;
        }
        public void set(Object modelElement, Boolean value) {
            Object taggedValue = Model.getFacade().getTaggedValue(modelElement, (String) getPropertyName());
            if (taggedValue == null) {
                taggedValue =
                        Model.getExtensionMechanismsFactory().buildTaggedValue(
                                (String) getPropertyName(), "");
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
}
