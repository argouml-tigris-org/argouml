// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.model.uml.modelmanagement;

import org.argouml.model.uml.AbstractUmlModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.model_management.*;

/**
 * Factory to create UML classes for the UML
 * ModelManagement package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */

public class ModelManagementFactory extends AbstractUmlModelFactory {

    /** Singleton instance.
     */
    private static ModelManagementFactory SINGLETON =
                   new ModelManagementFactory();

    /** Singleton instance access method.
     */
    public static ModelManagementFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private ModelManagementFactory() {
    }

    /** Create an empty but initialized instance of a UML Model.
     *  
     *  @return an initialized UML Model instance.
     */
    public MModel createModel() {
        MModel modelElement = MFactory.getDefaultFactory().createModel();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML ElementImport.
     *  
     *  @return an initialized UML ElementImport instance.
     */
    public MElementImport createElementImport() {
        MElementImport modelElement = MFactory.getDefaultFactory().createElementImport();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Package.
     *  
     *  @return an initialized UML Package instance.
     */
    public MPackage createPackage() {
        MPackage modelElement = MFactory.getDefaultFactory().createPackage();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Subsystem.
     *  
     *  @return an initialized UML Subsystem instance.
     */
    public MSubsystem createSubsystem() {
        MSubsystem modelElement = MFactory.getDefaultFactory().createSubsystem();
	super.initialize(modelElement);
	return modelElement;
    }
}
