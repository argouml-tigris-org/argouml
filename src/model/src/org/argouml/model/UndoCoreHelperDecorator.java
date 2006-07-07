// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.List;


/**
 * This Decorator is responsible for generating mementos for any
 * mutable methods.
 *
 * @author Bob Tarling
 * @author Linus Tolke
 */
class UndoCoreHelperDecorator extends AbstractCoreHelperDecorator {

    /**
     * Constructor.
     *
     * @param component The component we are decorating.
     */
    UndoCoreHelperDecorator(CoreHelper component) {
        super(component);
    }



    // Helper interfaces and methods.
    /**
     * Interface to set a boolean value.
     */
    protected interface BooleanSetter {
        /**
         * Do the actual setting.
         *
         * @param value The new value.
         */
        void set(boolean value);
    }

    /**
     * Interface to set a Object value.
     */
    protected interface ObjectSetter {
        /**
         * Do the actual setting.
         *
         * @param value The new value.
         */
        void set(Object value);
    }

    /**
     * Interface to set a String value.
     */
    protected interface StringSetter {
        /**
         * Do the actual setting.
         *
         * @param value The new value.
         */
        void set(String value);
    }


    /**
     * Create a memento for a setter of a boolean value.
     *
     * @param accesser The accesser.
     * @param newValue The new value.
     * @param oldValue The old value.
     */
    private void createMemento(
            final BooleanSetter accesser,
            final boolean newValue, final boolean oldValue) {
        if (newValue == oldValue) {
            return;
        }
        ModelMemento memento = new ModelMemento() {
            public void undo() {
                accesser.set(oldValue);
            }
            public void redo() {
                accesser.set(newValue);
            }
        };
        Model.notifyMementoCreationObserver(memento);
        memento.redo();
    }

    /**
     * Create a memento for a setter of a Object value.
     *
     * @param accesser The accesser.
     * @param newValue The new value.
     * @param oldValue The old value.
     */
    private void createMemento(
            final ObjectSetter accesser,
            final Object newValue, final Object oldValue) {
        if (newValue == oldValue) {
            return;
        }
        if (newValue != null
                && newValue.equals(oldValue)) {
            return;
        }
        ModelMemento memento = new ModelMemento() {
            public void undo() {
                accesser.set(oldValue);
            }
            public void redo() {
                accesser.set(newValue);
            }
        };
        Model.notifyMementoCreationObserver(memento);
        memento.redo();
    }

    /**
     * Create a memento for a setter of a String value.
     *
     * @param accesser The accesser.
     * @param newValue The new value.
     * @param oldValue The old value.
     */
    private void createMemento(
            final StringSetter accesser,
            final String newValue, final String oldValue) {
        if (newValue == oldValue) {
            return;
        }
        if (newValue != null
                && newValue.equals(oldValue)) {
            return;
        }
        ModelMemento memento = new ModelMemento() {
            public void undo() {
                accesser.set(oldValue);
            }
            public void redo() {
                accesser.set(newValue);
            }
        };
        Model.notifyMementoCreationObserver(memento);
        memento.redo();
    }

    /**
     * @see org.argouml.model.CoreHelper#setAbstract(java.lang.Object, boolean)
     */
    public void setAbstract(final Object handle, boolean flag) {
        createMemento(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setAbstract(handle, value);
            }
        }, flag, Model.getFacade().isAbstract(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setActive(java.lang.Object, boolean)
     */
    public void setActive(final Object handle, boolean active) {
        createMemento(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setActive(handle, value);
            }
        }, active, Model.getFacade().isActive(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setAggregation(
     *         java.lang.Object, java.lang.Object)
     */
    public void setAggregation(final Object handle, Object aggregationKind) {
        createMemento(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setAggregation(handle, value);
            }
        }, aggregationKind, Model.getFacade().getAggregation(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setLeaf(java.lang.Object, boolean)
     */
    public void setLeaf(final Object handle, boolean flag) {
        createMemento(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setLeaf(handle, value);
            }
        }, flag, Model.getFacade().isLeaf(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setChangeability(
     *         java.lang.Object, java.lang.Object)
     */
    public void setChangeability(final Object handle, Object ck) {
        createMemento(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setChangeability(handle, value);
            }
        }, ck, Model.getFacade().getChangeability(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setChangeable(
     *         java.lang.Object, boolean)
     */
    public void setChangeable(final Object handle, boolean flag) {
        createMemento(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setChangeable(handle, value);
            }
        }, flag, Model.getFacade().isChangeable(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setConcurrency(
     *         java.lang.Object, java.lang.Object)
     */
    public void setConcurrency(final Object handle, Object concurrencyKind) {
        createMemento(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setConcurrency(handle, value);
            }
        }, concurrencyKind, Model.getFacade().getConcurrency(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setKind(
     *         java.lang.Object, java.lang.Object)
     */
    public void setKind(final Object handle, Object kind) {
        createMemento(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setKind(handle, value);
            }
        }, kind, Model.getFacade().getKind(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToIn(
     * java.lang.Object)
     */
    public void setKindToIn(Object handle) {
        setKind(handle, Model.getDirectionKind().getInParameter());
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToInOut(
     * java.lang.Object)
     */
    public void setKindToInOut(Object handle) {
        setKind(handle, Model.getDirectionKind().getInOutParameter());
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToOut(
     * java.lang.Object)
     */
    public void setKindToOut(Object handle) {
        setKind(handle, Model.getDirectionKind().getOutParameter());
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToReturn(
     * java.lang.Object)
     */
    public void setKindToReturn(Object handle) {
        setKind(handle, Model.getDirectionKind().getReturnParameter());
    }

    /**
     * @see org.argouml.model.CoreHelper#setMultiplicity(
     *         java.lang.Object, java.lang.Object)
     */
    public void setMultiplicity(final Object handle, Object arg) {
        createMemento(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setMultiplicity(handle, value);
            }
        }, arg, Model.getFacade().getMultiplicity(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setName(
     *         java.lang.Object, java.lang.String)
     */
    public void setName(final Object handle, String name) {
        createMemento(new StringSetter() {
            public void set(String value) {
                getComponent().setName(handle, value);
            }
        }, name, Model.getFacade().getName(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setBody(
     *         java.lang.Object, java.lang.String)
     */
    public void setBody(final Object handle, String body) {
        createMemento(new StringSetter() {
            public void set(String value) {
                getComponent().setBody(handle, value);
            }
        }, body, Model.getCoreHelper().getBody(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setNavigable(
     *         java.lang.Object, boolean)
     */
    public void setNavigable(final Object handle, boolean flag) {
        createMemento(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setNavigable(handle, value);
            }
        }, flag, Model.getFacade().isNavigable(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setOrdering(
     *         java.lang.Object, java.lang.Object)
     */
    public void setOrdering(final Object handle, Object ok) {
        createMemento(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setOrdering(handle, value);
            }
        }, ok, Model.getFacade().getOrdering(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setPowertype(
     *         java.lang.Object, java.lang.Object)
     */
    public void setPowertype(final Object handle, Object pt) {
        createMemento(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setPowertype(handle, value);
            }
        }, pt, Model.getFacade().getPowertype(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setQuery(java.lang.Object, boolean)
     */
    public void setQuery(final Object handle, boolean flag) {
        createMemento(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setQuery(handle, value);
            }
        }, flag, Model.getFacade().isQuery(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setRoot(java.lang.Object, boolean)
     */
    public void setRoot(final Object handle, boolean flag) {
        createMemento(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setRoot(handle, value);
            }
        }, flag, Model.getFacade().isRoot(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setSpecification(
     *         java.lang.Object, boolean)
     */
    public void setSpecification(final Object handle, boolean specification) {
        createMemento(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setSpecification(handle, value);
            }
        }, specification, Model.getFacade().isSpecification(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setSpecification(java.lang.Object, java.lang.String)
     */
    public void setSpecification(final Object handle, String specification) {
        createMemento(new StringSetter() {
            public void set(String value) {
                getComponent().setSpecification(handle, value);
            }
        }, specification, Model.getFacade().getSpecification(handle));
    }
    /**
     * @see org.argouml.model.CoreHelper#setSpecification(java.lang.Object, java.lang.Object)
     */
    public void setSpecification(final Object handle, Object specification) {
        createMemento(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setSpecification(handle, value);
            }
        }, specification, Model.getCoreHelper().getSpecification(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setTargetScope(
     *         java.lang.Object, java.lang.Object)
     */
    public void setTargetScope(final Object handle, Object scopeKind) {
        createMemento(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setTargetScope(handle, value);
            }
        }, scopeKind, Model.getFacade().getTargetScope(handle));
    }

    /**
     * @see org.argouml.model.CoreHelper#setVisibility(
     *         java.lang.Object, java.lang.Object)
     */
    public void setVisibility(final Object handle, Object visibility) {
        createMemento(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setVisibility(handle, value);
            }
        }, visibility, Model.getFacade().getVisibility(handle));
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addAllStereotypes(java.lang.Object, java.util.Collection)
     */
    public void addAllStereotypes(Object modelElement, Collection stereotypes) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addAnnotatedElement(
     * java.lang.Object, java.lang.Object)
     */
    public void addAnnotatedElement(Object comment, Object annotatedElement) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addClient(
     * java.lang.Object, java.lang.Object)
     */
    public void addClient(Object handle, Object element) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addClientDependency(
     * java.lang.Object, java.lang.Object)
     */
    public void addClientDependency(Object handle, Object dependency) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addComment(
     * java.lang.Object, java.lang.Object)
     */
    public void addComment(Object element, Object comment) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addConnection(
     * java.lang.Object, java.lang.Object)
     */
    public void addConnection(Object handle, Object connection) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addConstraint(
     * java.lang.Object, java.lang.Object)
     */
    public void addConstraint(Object handle, Object mc) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addDeploymentLocation(
     * java.lang.Object, java.lang.Object)
     */
    public void addDeploymentLocation(Object handle, Object node) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addElementResidence(
     * java.lang.Object, java.lang.Object)
     */
    public void addElementResidence(Object handle, Object residence) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addFeature(
     * java.lang.Object, int, java.lang.Object)
     */
    public void addFeature(Object handle, int index, Object f) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addFeature(
     * java.lang.Object, java.lang.Object)
     */
    public void addFeature(Object handle, Object f) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addLink(
     * java.lang.Object, java.lang.Object)
     */
    public void addLink(Object handle, Object link) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addMethod(
     * java.lang.Object, java.lang.Object)
     */
    public void addMethod(Object handle, Object m) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addOwnedElement(
     * java.lang.Object, java.lang.Object)
     */
    public void addOwnedElement(Object handle, Object me) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addParameter(
     * java.lang.Object, int, java.lang.Object)
     */
    public void addParameter(Object handle, int index, Object parameter) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addParameter(
     * java.lang.Object, java.lang.Object)
     */
    public void addParameter(Object handle, Object parameter) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addRaisedSignal(
     * java.lang.Object, java.lang.Object)
     */
    public void addRaisedSignal(Object handle, Object sig) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addSourceFlow(
     * java.lang.Object, java.lang.Object)
     */
    public void addSourceFlow(Object handle, Object flow) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addStereotype(
     * java.lang.Object, java.lang.Object)
     */
    public void addStereotype(Object modelElement, Object stereotype) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addSupplier(
     * java.lang.Object, java.lang.Object)
     */
    public void addSupplier(Object handle, Object element) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addSupplierDependency(
     * java.lang.Object, java.lang.Object)
     */
    public void addSupplierDependency(Object supplier, Object dependency) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addTaggedValue(
     * java.lang.Object, java.lang.Object)
     */
    public void addTaggedValue(Object handle, Object taggedValue) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#addTargetFlow(
     * java.lang.Object, java.lang.Object)
     */
    public void addTargetFlow(Object handle, Object flow) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#clearStereotypes(
     * java.lang.Object)
     */
    public void clearStereotypes(Object modelElement) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeAnnotatedElement(
     * java.lang.Object, java.lang.Object)
     */
    public void removeAnnotatedElement(Object handle, Object me) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeClientDependency(
     * java.lang.Object, java.lang.Object)
     */
    public void removeClientDependency(Object handle, Object dep) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeConnection(
     * java.lang.Object, java.lang.Object)
     */
    public void removeConnection(Object handle, Object connection) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeConstraint(
     * java.lang.Object, java.lang.Object)
     */
    public void removeConstraint(Object handle, Object cons) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeDeploymentLocation(
     * java.lang.Object, java.lang.Object)
     */
    public void removeDeploymentLocation(Object handle, Object node) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeElementResidence(
     * java.lang.Object, java.lang.Object)
     */
    public void removeElementResidence(Object handle, Object residence) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeFeature(
     * java.lang.Object, java.lang.Object)
     */
    public void removeFeature(Object cls, Object feature) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeOwnedElement(
     * java.lang.Object, java.lang.Object)
     */
    public void removeOwnedElement(Object handle, Object value) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeParameter(
     * java.lang.Object, java.lang.Object)
     */
    public void removeParameter(Object handle, Object parameter) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeSourceFlow(
     * java.lang.Object, java.lang.Object)
     */
    public void removeSourceFlow(Object handle, Object flow) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeStereotype(
     * java.lang.Object, java.lang.Object)
     */
    public void removeStereotype(Object modelElement, Object stereotype) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeSupplierDependency(
     * java.lang.Object, java.lang.Object)
     */
    public void removeSupplierDependency(Object supplier, Object dependency) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeTaggedValue(
     * java.lang.Object, java.lang.String)
     */
    public void removeTaggedValue(Object handle, String name) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeTargetFlow(
     * java.lang.Object, java.lang.Object)
     */
    public void removeTargetFlow(Object handle, Object flow) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setAnnotatedElements(
     * java.lang.Object, java.util.Collection)
     */
    public void setAnnotatedElements(Object handle, Collection elems) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setAssociation(
     * java.lang.Object, java.lang.Object)
     */
    public void setAssociation(Object handle, Object association) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setAttributes(
     * java.lang.Object, java.util.Collection)
     */
    public void setAttributes(Object classifier, Collection attributes) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setBody(
     * java.lang.Object, java.lang.Object)
     */
    public void setBody(Object handle, Object expr) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setChild(
     * java.lang.Object, java.lang.Object)
     */
    public void setChild(Object handle, Object child) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setConnections(
     * java.lang.Object, java.util.Collection)
     */
    public void setConnections(Object handle, Collection elems) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setContainer(
     * java.lang.Object, java.lang.Object)
     */
    public void setContainer(Object handle, Object component) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setDefaultValue(
     * java.lang.Object, java.lang.Object)
     */
    public void setDefaultValue(Object handle, Object expr) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setDiscriminator(
     * java.lang.Object, java.lang.String)
     */
    public void setDiscriminator(Object handle, String discriminator) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see 
     * org.argouml.model.AbstractCoreHelperDecorator#setEnumerationLiterals(
     * java.lang.Object, java.util.List)
     */
    public void setEnumerationLiterals(Object enumeration, List literals) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setFeature(
     * java.lang.Object, int, java.lang.Object)
     */
    public void setFeature(Object elem, int i, Object feature) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setFeatures(
     * java.lang.Object, java.util.Collection)
     */
    public void setFeatures(Object handle, Collection features) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see 
     * org.argouml.model.AbstractCoreHelperDecorator#setImplementationLocation(
     * java.lang.Object, java.lang.Object)
     */
    public void setImplementationLocation(Object handle, Object component) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setInitialValue(
     * java.lang.Object, java.lang.Object)
     */
    public void setInitialValue(Object at, Object expr) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see
     * org.argouml.model.AbstractCoreHelperDecorator#setModelElementContainer(
     * java.lang.Object, java.lang.Object)
     */
    public void setModelElementContainer(Object handle, Object container) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setNamespace(
     * java.lang.Object, java.lang.Object)
     */
    public void setNamespace(Object handle, Object ns) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setOperations(
     * java.lang.Object, java.util.Collection)
     */
    public void setOperations(Object classifier, Collection operations) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setOwner(
     * java.lang.Object, java.lang.Object)
     */
    public void setOwner(Object handle, Object owner) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setOwnerScope(
     * java.lang.Object, java.lang.Object)
     */
    public void setOwnerScope(Object handle, Object os) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setParameters(
     * java.lang.Object, java.util.Collection)
     */
    public void setParameters(Object handle, Collection parameters) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setParent(
     * java.lang.Object, java.lang.Object)
     */
    public void setParent(Object handle, Object parent) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setQualifiers(
     * java.lang.Object, java.util.Collection)
     */
    public void setQualifiers(Object handle, Collection elems) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setRaisedSignals(
     * java.lang.Object, java.util.Collection)
     */
    public void setRaisedSignals(Object handle, Collection raisedSignals) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setResident(
     * java.lang.Object, java.lang.Object)
     */
    public void setResident(Object handle, Object resident) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setResidents(
     * java.lang.Object, java.util.Collection)
     */
    public void setResidents(Object handle, Collection residents) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setSources(
     * java.lang.Object, java.util.Collection)
     */
    public void setSources(Object handle, Collection specifications) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setSpecifications(
     * java.lang.Object, java.util.Collection)
     */
    public void setSpecifications(Object handle, Collection specifications) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setTaggedValue(
     * java.lang.Object, java.lang.String, java.lang.String)
     */
    public void setTaggedValue(Object handle, String tag, String value) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setTaggedValues(
     * java.lang.Object, java.util.Collection)
     */
    public void setTaggedValues(Object handle, Collection taggedValues) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setType(
     * java.lang.Object, java.lang.Object)
     */
    public void setType(Object handle, Object type) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#setUUID(
     * java.lang.Object, java.lang.String)
     */
    public void setUUID(Object handle, String uuid) {
        Model.notifyMementoCreationObserver(new DummyModelMemento());
    }
}
