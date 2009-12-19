// $Id: PopupMenuNewEvent.java 15918 2008-10-14 06:40:41Z mvw $

package org.argouml.core.propertypanels.ui;

import org.argouml.model.Model;

/**
 * Property getters and setters
 * @author Bob Tarling
 */
class GetterSetterImpl extends GetterSetter {
    
    /**
     * Set a UML property by property name
     * @param handle the element to which a property must be set
     * @param value the new property value
     * @param propertyName the property name
     */
    public void set(Object handle, Object value, String propertyName) {
        if ("isAbstract".equals(propertyName)) {
            Model.getCoreHelper().setAbstract(handle, (Boolean) value);
        } else if ("isLeaf".equals(propertyName)) {
            Model.getCoreHelper().setLeaf(handle, (Boolean) value);
        } else if ("isRoot".equals(propertyName)) {
            Model.getCoreHelper().setRoot(handle, (Boolean) value);
        } else if ("isActive".equals(propertyName)) {
            Model.getCoreHelper().setActive(handle, (Boolean) value);
        } else if ("ownerScope".equals(propertyName)) {
            Model.getCoreHelper().setStatic(handle, (Boolean) value);
        } else if ("targetScope".equals(propertyName)) {
            // Have we handled UML2 here?
            Model.getCoreHelper().setStatic(handle, (Boolean) value);
        } else if ("isQuery".equals(propertyName)) {
            Model.getCoreHelper().setQuery(handle, (Boolean) value);
        } else if ("isNavigable".equals(propertyName)) {
            Model.getCoreHelper().setNavigable(handle, (Boolean) value);
        } else if ("ordering".equals(propertyName)) {
            if ((Boolean) value) {
                Model.getCoreHelper().setOrdering(handle,
                        Model.getOrderingKind().getOrdered());
            } else {
                Model.getCoreHelper().setOrdering(handle,
                        Model.getOrderingKind().getUnordered());
            }
        } else if ("isAsynchronous".equals(propertyName)) {
            Model.getCommonBehaviorHelper().setAsynchronous(
                    handle,
                    (Boolean) value);
        } else if ("isSynch".equals(propertyName)) {
            Model.getActivityGraphsHelper().setSynch(
                    handle,
                    (Boolean) value);
        } else if ("derived".equals(propertyName)) {
            Object taggedValue = Model.getFacade().getTaggedValue(handle, (String) propertyName);
            if (taggedValue == null) {
                taggedValue =
                        Model.getExtensionMechanismsFactory().buildTaggedValue(
                                (String) propertyName, "");
                Model.getExtensionMechanismsHelper().addTaggedValue(
                        handle, taggedValue);
            }
            if ((Boolean) value) {
                Model.getCommonBehaviorHelper().setValue(taggedValue, "true");
            } else {
                Model.getCommonBehaviorHelper().setValue(taggedValue, "false");
            }
        }
    }
    
    /**
     * Get a UML property by property name
     * @param handle the element from which a property must be return
     * @param value the new property value
     * @param propertyName the property name
     */
    public Object get(Object handle, String propertyName) {
        if ("isAbstract".equals(propertyName)) {
            return Model.getFacade().isAbstract(handle);
        } else if ("isLeaf".equals(propertyName)) {
            return Model.getFacade().isLeaf(handle);
        } else if ("isRoot".equals(propertyName)) {
            return Model.getFacade().isRoot(handle);
        } else if ("isActive".equals(propertyName)) {
            return Model.getFacade().isActive(handle);
        } else if ("ownerScope".equals(propertyName)) {
            return Model.getFacade().isStatic(handle);
        } else if ("targetScope".equals(propertyName)) {
            // Have we handled UML2 here?
            return Model.getFacade().isStatic(handle);
        } else if ("isQuery".equals(propertyName)) {
            return Model.getFacade().isQuery(handle);
        } else if ("isNavigable".equals(propertyName)) {
            return Model.getFacade().isNavigable(handle);
        } else if ("ordering".equals(propertyName)) {
            return Model.getFacade().getOrdering(handle) == Model.getOrderingKind().getOrdered();
        } else if ("isAsynchronous".equals(propertyName)) {
            return Model.getFacade().isAsynchronous(handle);
        } else if ("isSynch".equals(propertyName)) {
            return Model.getFacade().isSynch(handle);
        } else if ("derived".equals(propertyName)) {
            Object tv = Model.getFacade().getTaggedValue(handle, propertyName);
            if (tv != null) {
                String tag = Model.getFacade().getValueOfTag(tv);
                return ("true".equals(tag));
            }
            return false;
        }
        return null;
    }
}
