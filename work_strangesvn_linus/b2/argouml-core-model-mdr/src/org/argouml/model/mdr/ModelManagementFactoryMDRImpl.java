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
    private ModelImplementation modelImpl;

    /**
     * Constructor.
     *
     * @param mi
     *            The MDRModelImplementation.
     */
    public ModelManagementFactoryMDRImpl(MDRModelImplementation mi) {
        modelManagementPackage = mi.getUmlPackage().getModelManagement();
        modelImpl = mi;
    }


    public Model createModel() {
        Model myModel = modelManagementPackage.getModel().createModel();
        super.initialize(myModel);
        return myModel;
    }


    public void setRootModel(Object rootModel) {
        if (rootModel != null && !(rootModel instanceof Model)) {
            throw new IllegalArgumentException(
                    "The rootModel supplied must be a Model. Got a "
                            + rootModel.getClass().getName());
        }
        theRootModel = rootModel;
    }


    public Object getRootModel() {
        return theRootModel;
    }


    public ElementImport createElementImport() {
        ElementImport myElementImport =
            modelManagementPackage.getElementImport().createElementImport();
        super.initialize(myElementImport);
        return myElementImport;
    }


    public ElementImport buildElementImport(Object pack, Object me) {
        if (pack instanceof UmlPackage && me instanceof ModelElement) {
            ElementImport ei = createElementImport();
            ei.setImportedElement((ModelElement) me);
            ei.setUmlPackage((UmlPackage) pack);
            return ei;
        }
        throw new IllegalArgumentException(
                "To build an ElementImport we need a "
                + "Package and a ModelElement.");
    }


    public UmlPackage createPackage() {
        UmlPackage myUmlPackage =
            modelManagementPackage.getUmlPackage().createUmlPackage();
        super.initialize(myUmlPackage);
        return myUmlPackage;
    }

    @Deprecated
    @SuppressWarnings("deprecation")
    public Object buildPackage(String name, String uuid) {
        UmlPackage pkg = createPackage();
        pkg.setName(name);
        // We could throw an exception if uuid is not null, but since the
        // Javadoc technically says that it is only used if the element doesn't
        // already have one set, we're going to assume we can ignore it.
        return pkg;
    }

    public Object buildPackage(String name) {
        UmlPackage pkg = createPackage();
        pkg.setName(name);
        return pkg;
    }
    

    public Object createSubsystem() {
        Subsystem mySubsystem =
            modelManagementPackage.getSubsystem().createSubsystem();
        super.initialize(mySubsystem);
        return mySubsystem;
    }


    public Object copyPackage(Object source, Object ns) {
        if (!(source instanceof UmlPackage)) {
            throw new IllegalArgumentException("source");
        }
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException("namespace");
        }

        UmlPackage p = createPackage();
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
        ((CoreFactoryMDRImpl) modelImpl.getCoreFactory())
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
