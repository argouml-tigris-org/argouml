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

import org.apache.log4j.Logger;
import org.argouml.model.ModelImplementation;
import org.argouml.model.ModelManagementFactory;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.modelmanagement.ElementImport;
import org.omg.uml.modelmanagement.Model;
import org.omg.uml.modelmanagement.ModelManagementPackage;
import org.omg.uml.modelmanagement.Subsystem;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * The ModelManagementFactory.<p>
 *
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * @author Tom Morris
 * derived from NSUML implementation by:
 * @author Linus Tolke
 */
final class ModelManagementFactoryMDRImpl extends
        AbstractUmlModelFactoryMDR implements ModelManagementFactory {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ModelManagementFactoryMDRImpl.class);

    /**
     * The model.
     */
    private Object theRootModel;

    /**
     * The ModelManagement package.
     */
    private ModelManagementPackage modelManagementPackage;

    /**
     * The model implementation.
     */
    private ModelImplementation nsmodel;

    /**
     * Constructor.
     *
     * @param mi
     *            The MDRModelImplementation.
     */
    public ModelManagementFactoryMDRImpl(MDRModelImplementation mi) {
        modelManagementPackage = mi.getUmlPackage().getModelManagement();
        nsmodel = mi;
    }

    /*
     * @see org.argouml.model.ModelManagementFactory#createModel()
     */
    public Object createModel() {
        Model myModel = modelManagementPackage.getModel().createModel();
        super.initialize(myModel);
        return myModel;
    }

    /*
     * @see org.argouml.model.ModelManagementFactory#setRootModel(java.lang.Object)
     */
    public void setRootModel(Object rootModel) {
        if (rootModel != null && !(rootModel instanceof Model)) {
            throw new IllegalArgumentException(
                    "The rootModel supplied must be a Model. Got a "
                            + rootModel.getClass().getName());
        }
        theRootModel = rootModel;
    }

    /*
     * @see org.argouml.model.ModelManagementFactory#getRootModel()
     */
    public Object getRootModel() {
        return theRootModel;
    }

    /*
     * @see org.argouml.model.ModelManagementFactory#createElementImport()
     */
    public Object createElementImport() {
        ElementImport myElementImport =
            modelManagementPackage.getElementImport().createElementImport();
        super.initialize(myElementImport);
        return myElementImport;
    }

    /*
     * @see org.argouml.model.ModelManagementFactory#buildElementImport(
     * java.lang.Object, java.lang.Object)
     */
    public Object buildElementImport(Object pack, Object me) {
        if (pack instanceof UmlPackage && me instanceof ModelElement) {
            ElementImport ei = (ElementImport) createElementImport();
            ei.setImportedElement((ModelElement) me);
            ei.setUmlPackage((UmlPackage) pack);
            return ei;
        }
        throw new IllegalArgumentException(
                "To build an ElementImport we need a "
                + "Package and a ModelElement.");
    }

    /*
     * @see org.argouml.model.ModelManagementFactory#createPackage()
     */
    public Object createPackage() {
        UmlPackage myUmlPackage =
            modelManagementPackage.getUmlPackage().createUmlPackage();
        super.initialize(myUmlPackage);
        return myUmlPackage;
    }

    /*
     * @see org.argouml.model.ModelManagementFactory#buildPackage(
     *      java.lang.String, java.lang.String)
     */
    public Object buildPackage(String name, String uuid) {
        UmlPackage pkg = (UmlPackage) createPackage();
        pkg.setName(name);
        // TODO: not sure who added below message.  Needs resolution. - tfm
        LOG.warn("UUID [" + uuid + "] ignored - what to do with it?");
        return pkg;
    }

    /*
     * @see org.argouml.model.ModelManagementFactory#createSubsystem()
     */
    public Object createSubsystem() {
        Subsystem mySubsystem =
            modelManagementPackage.getSubsystem().createSubsystem();
        super.initialize(mySubsystem);
        return mySubsystem;
    }

    /*
     * @see org.argouml.model.ModelManagementFactory#copyPackage(
     *      java.lang.Object, java.lang.Object)
     */
    public Object copyPackage(Object source, Object ns) {
        if (!(source instanceof UmlPackage)) {
            throw new IllegalArgumentException("source");
        }
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException("namespace");
        }

        UmlPackage p = (UmlPackage) createPackage();
        ((Namespace) ns).getOwnedElement().add(p);
        doCopyPackage((UmlPackage) source, p);
        return p;
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     *
     * @param source
     *            The source package.
     * @param target
     *            The target package.
     */
    private void doCopyPackage(UmlPackage source, UmlPackage target) {
        ((CoreFactoryMDRImpl) nsmodel.getCoreFactory())
            .doCopyNamespace(source, target);
    }

    /**
     * @param elem
     *            to be deleted
     */
    void deleteElementImport(Object elem) {
        if (!(elem instanceof ElementImport)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem
     *            to be deleted
     */
    void deleteModel(Object elem) {
        if (!(elem instanceof Model)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem
     *            to be deleted
     */
    void deletePackage(Object elem) {
        if (!(elem instanceof UmlPackage)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem
     *            to be deleted
     */
    void deleteSubsystem(Object elem) {
        if (!(elem instanceof Subsystem)) {
            throw new IllegalArgumentException();
        }

    }

}
