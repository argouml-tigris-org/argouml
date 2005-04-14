// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import org.apache.log4j.Logger;
import org.argouml.model.ModelManagementFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.model_management.MElementImport;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MSubsystem;

/**
 * Factory to create UML classes for the UML
 * ModelManagement package.<p>
 *
 * TODO: Change visibility to package after reflection problem solved.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class ModelManagementFactoryImpl
	extends AbstractUmlModelFactory
	implements ModelManagementFactory {

    /**
     * Logger.
     */
    private static final Logger LOG = 
            Logger.getLogger(ModelManagementFactoryImpl.class);

    /**
     * The root model.
     */
    private MModel rootModel;
    
    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    ModelManagementFactoryImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Create an empty but initialized instance of a UML Model.
     *
     * @return an initialized UML Model instance.
     */
    public Object createModel() {
        MModel modelElement = MFactory.getDefaultFactory().createModel();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Sets the root model of the project
     * @param theRootModel the new root model.
     *
     * @see org.argouml.model.ModelManagementFactory#setRootModel(java.lang.Object)
     */
    public void setRootModel(Object theRootModel) {
        LOG.info("Setting the root model to " + theRootModel);
        this.rootModel = (MModel) theRootModel;
    }
    
    /**
     * Gets the root model of the project
     * @return the current root model.
     */
    public Object getRootModel() {
        return rootModel;
    }
    
    
    /**
     * Create an empty but initialized instance of a UML ElementImport.
     *
     * @return an initialized UML ElementImport instance.
     */
    public Object createElementImport() {
        MElementImport modelElement =
	    MFactory.getDefaultFactory().createElementImport();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Package.
     *
     * @return an initialized UML Package instance.
     */
    public Object createPackage() {
        MPackage modelElement = MFactory.getDefaultFactory().createPackage();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * Build an empty but initialized instance of a UML Package
     * with a given name, and set it's UUID, if it doesn't exist.
     *
     * @param name is the given name
     * @param uuid is the UUID.
     * @return an initialized UML Package instance.
     */
    public Object buildPackage(String name, String uuid) {
        MPackage modelElement = MFactory.getDefaultFactory().createPackage();
	super.initialize(modelElement);
	modelElement.setName(name);
	if (modelElement.getUUID() == null) {
	    modelElement.setUUID(uuid);
	}
	return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Subsystem.
     *
     * @return an initialized UML Subsystem instance.
     */
    public Object createSubsystem() {
        MSubsystem modelElement =
	    MFactory.getDefaultFactory().createSubsystem();
	super.initialize(modelElement);
	return modelElement;
    }

    /**
     * @param elem to be deleted
     */
    void deleteElementImport(Object elem) {
        if (!(elem instanceof MElementImport)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem to be deleted
     */
    void deleteModel(Object elem) {
        if (!(elem instanceof MModel)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem to be deleted
     */
    void deletePackage(Object elem) {
        if (!(elem instanceof MPackage)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem to be deleted
     */
    void deleteSubsystem(Object elem) {
        if (!(elem instanceof MSubsystem)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Copies a package, but not any elements within it. This does however
     * not mean the package will be empty, since eg it or it's parents may
     * reference a stereotype within it causing that to be copied into it.
     *
     * @param source is the package to copy.
     * @param ns is the namespace to put the copy in.
     * @return the newly created package.
     */
    public Object copyPackage(Object source, Object ns) {
        if (!(source instanceof MPackage)) {
            throw new IllegalArgumentException("source");
        }
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException("namespace");
        }

        MPackage p = (MPackage) createPackage();
	((MNamespace) ns).addOwnedElement(p);
	doCopyPackage((MPackage) source, p);
	return p;
    }

    /**
     * Used by the copy functions. Do not call this function directly.
     *
     * @param source The source package.
     * @param target The target package.
     */
    private void doCopyPackage(MPackage source, MPackage target) {
	nsmodel.getCoreFactory().doCopyNamespace(source, target);
    }
}

