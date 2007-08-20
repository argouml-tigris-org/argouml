// $Id:ModelManagementFactory.java 13118 2007-07-23 19:39:28Z tfmorris $
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

/**
 * The interface for the factory for ModelManagement.
 */
public interface ModelManagementFactory extends Factory {
    /**
     * Create an empty but initialized instance of a UML Model.
     *
     * @return an initialized UML Model instance.
     */
    Object createModel();

    /**
     * Sets the root model of the project.
     *
     * @param rootModel the new root model.
     */
    void setRootModel(Object rootModel);

    /**
     * Gets the root model of the project.
     *
     * @return the current root model.
     */
    Object getRootModel();

    /**
     * Create an empty but initialized instance of a UML ElementImport.
     *
     * @return an initialized UML ElementImport instance.
     */
    Object createElementImport();

    /**
     * Build a ElementImport which imports the given modelelement
     * into the given package.
     * The visibility, alias and isSpecification are not filled in.
     * 
     * @param pack the package to import into
     * @param me the model element to import
     * @return the newly created ElementImport
     */
    Object buildElementImport(Object pack, Object me);

    /**
     * Create an empty but initialized instance of a UML Package.
     *
     * @return an initialized UML Package instance.
     */
    Object createPackage();

    /**
     * Build an empty but initialized instance of a UML Package
     * with a given name, and set it's UUID, if it doesn't exist.
     *
     * @param name is the given name
     * @param uuid is the UUID.
     * @return an initialized UML Package instance.
     */
    Object buildPackage(String name, String uuid);

    /**
     * Create an empty but initialized instance of a UML Subsystem.
     *
     * @return an initialized UML Subsystem instance.
     */
    Object createSubsystem();

    /**
     * Copies a package, but not any elements within it. This does however
     * not mean the package will be empty, since eg it or it's parents may
     * reference a stereotype within it causing that to be copied into it.
     *
     * @param source is the package to copy.
     * @param ns is the namespace to put the copy in.
     * @return the newly created package.
     */
    Object copyPackage(Object source, Object ns);
}
