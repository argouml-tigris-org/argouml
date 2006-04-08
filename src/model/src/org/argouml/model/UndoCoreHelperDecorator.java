// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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
     * @see org.argouml.model.CoreHelper#setKindToIn(java.lang.Object)
     */
    public void setKindToIn(Object handle) {
        setKind(handle, Model.getDirectionKind().getInParameter());
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToInOut(java.lang.Object)
     */
    public void setKindToInOut(Object handle) {
        setKind(handle, Model.getDirectionKind().getInOutParameter());
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToOut(java.lang.Object)
     */
    public void setKindToOut(Object handle) {
        setKind(handle, Model.getDirectionKind().getOutParameter());
    }

    /**
     * @see org.argouml.model.CoreHelper#setKindToReturn(java.lang.Object)
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
}

