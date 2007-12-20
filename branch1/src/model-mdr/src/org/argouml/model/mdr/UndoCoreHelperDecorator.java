// $Id$
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.Collection;
import java.util.List;

import org.argouml.model.AbstractCoreHelperDecorator;
import org.argouml.model.CoreHelper;
import org.argouml.model.DummyModelCommand;
import org.argouml.model.Model;
import org.argouml.model.ModelCommand;


/**
 * This Decorator is responsible for generating commands for any
 * mutable methods.
 *
 * @author Bob Tarling
 * @author Linus Tolke
 */
@SuppressWarnings("deprecation")
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
     * Create a command for a setter of a boolean value.
     *
     * @param accesser The accesser.
     * @param newValue The new value.
     * @param oldValue The old value.
     */
    private void createCommand(
            final BooleanSetter accesser,
            final boolean newValue, final boolean oldValue) {
        if (newValue == oldValue) {
            return;
        }
        ModelCommand command = new ModelCommand() {
            public void undo() {
                accesser.set(oldValue);
            }
            public Object execute() {
                accesser.set(newValue);
                return null;
            }
            public boolean isUndoable() {
                return true;
            }
            public boolean isRedoable() {
                return true;
            }
        };
        Model.execute(command);
    }

    /**
     * Create a command for a setter of a Object value.
     *
     * @param accesser The accesser.
     * @param newValue The new value.
     * @param oldValue The old value.
     */
    private void createCommand(
            final ObjectSetter accesser,
            final Object newValue, final Object oldValue) {
        if (newValue == oldValue) {
            return;
        }
        if (newValue != null
                && newValue.equals(oldValue)) {
            return;
        }
        ModelCommand command = new ModelCommand() {
            public void undo() {
                accesser.set(oldValue);
            }
            public Object execute() {
                accesser.set(newValue);
                return null;
            }
            public boolean isUndoable() {
                return true;
            }
            public boolean isRedoable() {
                return true;
            }
        };
        Model.execute(command);
    }

    /**
     * Create a command for a setter of a String value.
     *
     * @param accesser The accesser.
     * @param newValue The new value.
     * @param oldValue The old value.
     */
    private void createCommand(
            final StringSetter accesser,
            final String newValue, final String oldValue) {
        if (newValue == oldValue) {
            return;
        }
        if (newValue != null
                && newValue.equals(oldValue)) {
            return;
        }
        ModelCommand command = new ModelCommand() {
            public void undo() {
                accesser.set(oldValue);
            }
            public Object execute() {
                accesser.set(newValue);
                return null;
            }
            public boolean isUndoable() {
                return true;
            }
            public boolean isRedoable() {
                return true;
            }
        };
        Model.execute(command);
    }


    public void setAbstract(final Object handle, boolean flag) {
        createCommand(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setAbstract(handle, value);
            }
        }, flag, Model.getFacade().isAbstract(handle));
    }


    public void setActive(final Object handle, boolean active) {
        createCommand(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setActive(handle, value);
            }
        }, active, Model.getFacade().isActive(handle));
    }


    public void setAggregation(final Object handle, Object aggregationKind) {
        createCommand(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setAggregation(handle, value);
            }
        }, aggregationKind, Model.getFacade().getAggregation(handle));
    }


    public void setLeaf(final Object handle, boolean flag) {
        createCommand(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setLeaf(handle, value);
            }
        }, flag, Model.getFacade().isLeaf(handle));
    }

    @Override
    public void setChangeability(final Object handle, Object ck) {
        createCommand(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setChangeability(handle, value);
            }
        }, ck, Model.getFacade().getChangeability(handle));
    }

    @Override
    public void setChangeable(final Object handle, boolean flag) {
        createCommand(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setChangeable(handle, value);
            }
        }, flag, Model.getFacade().isChangeable(handle));
    }

    @Override
    public void setReadOnly(final Object handle, boolean flag) {
        createCommand(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setReadOnly(handle, value);
            }
        }, flag, Model.getFacade().isReadOnly(handle));
    }

    public void setConcurrency(final Object handle, Object concurrencyKind) {
        createCommand(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setConcurrency(handle, value);
            }
        }, concurrencyKind, Model.getFacade().getConcurrency(handle));
    }


    public void setKind(final Object handle, Object kind) {
        createCommand(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setKind(handle, value);
            }
        }, kind, Model.getFacade().getKind(handle));
    }


    public void setMultiplicity(final Object handle, Object arg) {
        createCommand(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setMultiplicity(handle, value);
            }
        }, arg, Model.getFacade().getMultiplicity(handle));
    }

    public void setBody(final Object handle, String body) {
        createCommand(new StringSetter() {
            public void set(String value) {
                getComponent().setBody(handle, value);
            }
        }, body, Model.getCoreHelper().getBody(handle));
    }


    public void setNavigable(final Object handle, boolean flag) {
        createCommand(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setNavigable(handle, value);
            }
        }, flag, Model.getFacade().isNavigable(handle));
    }


    public void setOrdering(final Object handle, Object ok) {
        createCommand(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setOrdering(handle, value);
            }
        }, ok, Model.getFacade().getOrdering(handle));
    }


    public void setPowertype(final Object handle, Object pt) {
        createCommand(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setPowertype(handle, value);
            }
        }, pt, Model.getFacade().getPowertype(handle));
    }


    public void setQuery(final Object handle, boolean flag) {
        createCommand(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setQuery(handle, value);
            }
        }, flag, Model.getFacade().isQuery(handle));
    }


    public void setRoot(final Object handle, boolean flag) {
        createCommand(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setRoot(handle, value);
            }
        }, flag, Model.getFacade().isRoot(handle));
    }


    public void setSpecification(final Object handle, boolean specification) {
        createCommand(new BooleanSetter() {
            public void set(boolean value) {
                getComponent().setSpecification(handle, value);
            }
        }, specification, Model.getFacade().isSpecification(handle));
    }

    
    public void setSpecification(final Object handle, String specification) {
        createCommand(new StringSetter() {
            public void set(String value) {
                getComponent().setSpecification(handle, value);
            }
        }, specification, Model.getFacade().getSpecification(handle));
    }

    
    public void setSpecification(final Object handle, Object specification) {
        createCommand(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setSpecification(handle, value);
            }
        }, specification, Model.getCoreHelper().getSpecification(handle));
    }

    
    @Override
    public void setTargetScope(final Object handle, Object scopeKind) {
        createCommand(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setTargetScope(handle, value);
            }
        }, scopeKind, Model.getFacade().getTargetScope(handle));
    }

    
    @Override
    public void setVisibility(final Object handle, Object visibility) {
        createCommand(new ObjectSetter() {
            public void set(Object value) {
                getComponent().setVisibility(handle, value);
            }
        }, visibility, Model.getFacade().getVisibility(handle));
    }

    public void addAllStereotypes(Object modelElement, Collection stereotypes) {
        super.addAllStereotypes(modelElement, stereotypes);
        Model.execute(new DummyModelCommand());
    }

    public void addAnnotatedElement(Object comment, Object annotatedElement) {
        super.addAnnotatedElement(comment, annotatedElement);
        Model.execute(new DummyModelCommand());
    }

    public void addClient(Object handle, Object element) {
        super.addClient(handle, element);
        Model.execute(new DummyModelCommand());
    }

    public void addClientDependency(Object handle, Object dependency) {
        super.addClientDependency(handle, dependency);
        Model.execute(new DummyModelCommand());
    }

    public void addComment(Object element, Object comment) {
        super.addComment(element, comment);
        Model.execute(new DummyModelCommand());
    }

    public void addConnection(Object handle, Object connection) {
        super.addConnection(handle, connection);
        Model.execute(new DummyModelCommand());
    }

    public void addConnection(Object handle, int position, Object connection) {
        super.addConnection(handle, position, connection);
        Model.execute(new DummyModelCommand());
    }

    public void addConstraint(Object handle, Object mc) {
        super.addConstraint(handle, mc);
        Model.execute(new DummyModelCommand());
    }

    public void addDeploymentLocation(Object handle, Object node) {
        super.addDeploymentLocation(handle, node);
        Model.execute(new DummyModelCommand());
    }

    public void addElementResidence(Object handle, Object residence) {
        super.addElementResidence(handle, residence);
        Model.execute(new DummyModelCommand());
    }

    public void addFeature(Object handle, int index, Object f) {
        super.addFeature(handle, index, f);
        Model.execute(new DummyModelCommand());
    }

    public void addLiteral(Object handle, int index, Object literal) {
        super.addLiteral(handle, index, literal);
        Model.execute(new DummyModelCommand());
    }

    public void addFeature(Object handle, Object f) {
        super.addFeature(handle, f);
        Model.execute(new DummyModelCommand());
    }

    public void addLink(Object handle, Object link) {
        super.addLink(handle, link);
        Model.execute(new DummyModelCommand());
    }

    public void addMethod(Object handle, Object m) {
        super.addMethod(handle, m);
        Model.execute(new DummyModelCommand());
    }

    public void addOwnedElement(Object handle, Object me) {
        super.addOwnedElement(handle, me);
        Model.execute(new DummyModelCommand());
    }

    public void addParameter(Object handle, int index, Object parameter) {
        super.addParameter(handle, index, parameter);
        Model.execute(new DummyModelCommand());
    }

    public void addParameter(Object handle, Object parameter) {
        super.addParameter(handle, parameter);
        Model.execute(new DummyModelCommand());
    }

    public void addQualifier(Object handle, int index, Object qualifier) {
        super.addQualifier(handle, index, qualifier);
        Model.execute(new DummyModelCommand());
    }

    public void addRaisedSignal(Object handle, Object sig) {
        super.addRaisedSignal(handle, sig);
        Model.execute(new DummyModelCommand());
    }

    public void addSourceFlow(Object handle, Object flow) {
        super.addSourceFlow(handle, flow);
        Model.execute(new DummyModelCommand());
    }

    public void addStereotype(Object modelElement, Object stereotype) {
        super.addStereotype(modelElement, stereotype);
        Model.execute(new DummyModelCommand());
    }

    public void addSupplier(Object handle, Object element) {
        super.addSupplier(handle, element);
        Model.execute(new DummyModelCommand());
    }

    public void addSupplierDependency(Object supplier, Object dependency) {
        super.addSupplierDependency(supplier, dependency);
        Model.execute(new DummyModelCommand());
    }

    public void addTaggedValue(Object handle, Object taggedValue) {
        super.addTaggedValue(handle, taggedValue);
        Model.execute(new DummyModelCommand());
    }

    public void addTargetFlow(Object handle, Object flow) {
        super.addTargetFlow(handle, flow);
        Model.execute(new DummyModelCommand());
    }

    public void addTemplateParameter(Object handle, int index, 
            Object parameter) {
        super.addTemplateParameter(handle, index, parameter);
        Model.execute(new DummyModelCommand());
    }

    public void addTemplateParameter(Object handle, Object parameter) {
        super.addTemplateParameter(handle, parameter);
        Model.execute(new DummyModelCommand());
    }

    public void clearStereotypes(Object modelElement) {
        super.clearStereotypes(modelElement);
        Model.execute(new DummyModelCommand());
    }

    public void removeAnnotatedElement(Object handle, Object me) {
        super.removeAnnotatedElement(handle, me);
        Model.execute(new DummyModelCommand());
    }

    public void removeClientDependency(Object handle, Object dep) {
        super.removeClientDependency(handle, dep);
        Model.execute(new DummyModelCommand());
    }

    public void removeConnection(Object handle, Object connection) {
        super.removeConnection(handle, connection);
        Model.execute(new DummyModelCommand());
    }

    public void removeConstraint(Object handle, Object cons) {
        super.removeConstraint(handle, cons);
        Model.execute(new DummyModelCommand());
    }

    public void removeDeploymentLocation(Object handle, Object node) {
        super.removeDeploymentLocation(handle, node);
        Model.execute(new DummyModelCommand());
    }

    public void removeElementResidence(Object handle, Object residence) {
        super.removeElementResidence(handle, residence);
        Model.execute(new DummyModelCommand());
    }

    public void removeFeature(Object cls, Object feature) {
        super.removeFeature(cls, feature);
        Model.execute(new DummyModelCommand());
    }

    public void removeLiteral(Object enu, Object literal) {
        super.removeLiteral(enu, literal);
        Model.execute(new DummyModelCommand());
    }

    public void removeOwnedElement(Object handle, Object value) {
        super.removeOwnedElement(handle, value);
        Model.execute(new DummyModelCommand());
    }

    public void removeParameter(Object handle, Object parameter) {
        super.removeParameter(handle, parameter);
        Model.execute(new DummyModelCommand());
    }

    public void removeQualifier(Object handle, Object qualifier) {
        super.removeQualifier(handle, qualifier);
        Model.execute(new DummyModelCommand());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeSourceFlow(
     * java.lang.Object, java.lang.Object)
     */
    public void removeSourceFlow(Object handle, Object flow) {
        super.removeSourceFlow(handle, flow);
        Model.execute(new DummyModelCommand());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeStereotype(
     * java.lang.Object, java.lang.Object)
     */
    public void removeStereotype(Object modelElement, Object stereotype) {
        super.removeStereotype(modelElement, stereotype);
        Model.execute(new DummyModelCommand());
    }

    /**
     * @see org.argouml.model.AbstractCoreHelperDecorator#removeSupplierDependency(
     * java.lang.Object, java.lang.Object)
     */
    public void removeSupplierDependency(Object supplier, Object dependency) {
        super.removeSupplierDependency(supplier, dependency);
        Model.execute(new DummyModelCommand());
    }

    public void removeTargetFlow(Object handle, Object flow) {
        super.removeTargetFlow(handle, flow);
        Model.execute(new DummyModelCommand());
    }


    public void removeTemplateArgument(Object handle, Object argument) {
        super.removeTemplateArgument(handle, argument);
        Model.execute(new DummyModelCommand());
    }
    
    public void removeTemplateParameter(Object handle, Object parameter) {
        super.removeTemplateParameter(handle, parameter);
        Model.execute(new DummyModelCommand());
    }
    
    public void setAnnotatedElements(Object handle, Collection elems) {
        super.setAnnotatedElements(handle, elems);
        Model.execute(new DummyModelCommand());
    }

    public void setAssociation(Object handle, Object association) {
        super.setAssociation(handle, association);
        Model.execute(new DummyModelCommand());
    }

    public void setAttributes(Object classifier, List attributes) {
        super.setAttributes(classifier, attributes);
        Model.execute(new DummyModelCommand());
    }
    
    public void setBody(Object handle, Object expr) {
        super.setBody(handle, expr);
        Model.execute(new DummyModelCommand());
    }

    public void setChild(Object handle, Object child) {
        super.setChild(handle, child);
        Model.execute(new DummyModelCommand());
    }

    public void setConnections(Object handle, Collection elems) {
        super.setConnections(handle, elems);
        Model.execute(new DummyModelCommand());
    }

    public void setContainer(Object handle, Object component) {
        super.setContainer(handle, component);
        Model.execute(new DummyModelCommand());
    }

    public void setDefaultElement(Object handle, Object element) {
        super.setDefaultElement(handle, element);
        Model.execute(new DummyModelCommand());
    }

    public void setDefaultValue(Object handle, Object expr) {
        super.setDefaultValue(handle, expr);
        Model.execute(new DummyModelCommand());
    }

    public void setDiscriminator(Object handle, String discriminator) {
        super.setDiscriminator(handle, discriminator);
        Model.execute(new DummyModelCommand());
    }

    public void setEnumerationLiterals(Object enumeration, List literals) {
        super.setEnumerationLiterals(enumeration, literals);
        Model.execute(new DummyModelCommand());
    }

    public void setFeature(Object elem, int i, Object feature) {
        super.setFeature(elem, i, feature);
        Model.execute(new DummyModelCommand());
    }

    public void setFeatures(Object handle, Collection features) {
        super.setFeatures(handle, features);
        Model.execute(new DummyModelCommand());
    }

    public void setInitialValue(Object at, Object expr) {
        super.setInitialValue(at, expr);
        Model.execute(new DummyModelCommand());
    }

    public void setModelElementContainer(Object handle, Object container) {
        super.setModelElementContainer(handle, container);
        Model.execute(new DummyModelCommand());
    }

    public void setNamespace(Object handle, Object ns) {
        super.setNamespace(handle, ns);
        Model.execute(new DummyModelCommand());
    }

    public void setOperations(Object classifier, List operations) {
        super.setOperations(classifier, operations);
        Model.execute(new DummyModelCommand());
    }

    public void setOwner(Object handle, Object owner) {
        super.setOwner(handle, owner);
        Model.execute(new DummyModelCommand());
    }

    public void setOwnerScope(Object handle, Object os) {
        super.setOwnerScope(handle, os);
        Model.execute(new DummyModelCommand());
    }
    
    public void setStatic(Object handle, boolean isStatic) {
        super.setStatic(handle, isStatic);
        Model.execute(new DummyModelCommand());
    }
    public void setParameters(Object handle, Collection parameters) {
        super.setParameters(handle, parameters);
        Model.execute(new DummyModelCommand());
    }

    public void setParent(Object handle, Object parent) {
        super.setParent(handle, parent);
        Model.execute(new DummyModelCommand());
    }

    public void setQualifiers(Object handle, List elems) {
        super.setQualifiers(handle, elems);
        Model.execute(new DummyModelCommand());
    }

    public void setRaisedSignals(Object handle, Collection raisedSignals) {
        super.setRaisedSignals(handle, raisedSignals);
        Model.execute(new DummyModelCommand());
    }

    public void setResident(Object handle, Object resident) {
        super.setResident(handle, resident);
        Model.execute(new DummyModelCommand());
    }

    public void setResidents(Object handle, Collection residents) {
        super.setResidents(handle, residents);
        Model.execute(new DummyModelCommand());
    }

    public void setSources(Object handle, Collection specifications) {
        super.setSources(handle, specifications);
        Model.execute(new DummyModelCommand());
    }

    public void setSpecifications(Object handle, Collection specifications) {
        super.setSpecifications(handle, specifications);
        Model.execute(new DummyModelCommand());
    }

    public void setTaggedValue(Object handle, String tag, String value) {
        super.setTaggedValue(handle, tag, value);
        Model.execute(new DummyModelCommand());
    }

    public void setTaggedValues(Object handle, Collection taggedValues) {
        super.setTaggedValues(handle, taggedValues);
        Model.execute(new DummyModelCommand());
    }

    public void setType(Object handle, Object type) {
        super.setType(handle, type);
        Model.execute(new DummyModelCommand());
    }

}
