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

package org.argouml.model.uml;

import org.argouml.model.uml.foundation.core.*;
import org.argouml.model.uml.foundation.datatypes.*;
import org.argouml.model.uml.foundation.extensionmechanisms.*;
import org.argouml.model.uml.behavioralelements.commonbehavior.*;
import org.argouml.model.uml.behavioralelements.usecases.*;
import org.argouml.model.uml.behavioralelements.statemachines.*;
import org.argouml.model.uml.behavioralelements.collaborations.*;
import org.argouml.model.uml.behavioralelements.activitygraphs.*;
import org.argouml.model.uml.modelmanagement.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.parsers.ParserConfigurationException;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.model_management.MModel;

import org.apache.log4j.Category;

import org.xml.sax.SAXException;

/**
 * Root factory for UML model element instance creation.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class UmlFactory extends AbstractUmlModelFactory {

    /** Log4j logging category.
     */
    protected Category logger = null;

    /** Singleton instance.
     */
    private static UmlFactory SINGLETON =
                   new UmlFactory();

    /** Singleton instance access method.
     */
    public static UmlFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation.
     *  Create a logger.
     */
    private UmlFactory() {
        logger =Category.getInstance("org.argouml.model.uml.factory");
    }

    /** Returns the package factory for the UML
     *  package Foundation::ExtensionMechanisms.
     *  
     *  @return the ExtensionMechanisms factory instance.
     */
    public ExtensionMechanismsFactory getExtensionMechanisms() {
        return ExtensionMechanismsFactory.getFactory();
    }

    /** Returns the package factory for the UML
     *  package Foundation::DataTypes.
     *  
     *  @return the DataTypes factory instance.
     */
    public DataTypesFactory getDataTypes() {
        return DataTypesFactory.getFactory();
    }

    /** Returns the package factory for the UML
     *  package Foundation::Core.
     *  
     *  @return the Core factory instance.
     */
    public CoreFactory getCore() {
        return CoreFactory.getFactory();
    }

    /** Returns the package factory for the UML
     *  package BehavioralElements::CommonBehavior.
     *  
     *  @return the CommonBehavior factory instance.
     */
    public CommonBehaviorFactory getCommonBehavior() {
        return CommonBehaviorFactory.getFactory();
    }

    /** Returns the package factory for the UML
     *  package BehavioralElements::UseCases.
     *  
     *  @return the UseCases factory instance.
     */
    public UseCasesFactory getUseCases() {
        return UseCasesFactory.getFactory();
    }

    /** Returns the package factory for the UML
     *  package BehavioralElements::StateMachines.
     *  
     *  @return the StateMachines factory instance.
     */
    public StateMachinesFactory getStateMachines() {
        return StateMachinesFactory.getFactory();
    }

    /** Returns the package factory for the UML
     *  package BehavioralElements::Collaborations.
     *  
     *  @return the Collaborations factory instance.
     */
    public CollaborationsFactory getCollaborations() {
        return CollaborationsFactory.getFactory();
    }

    /** Returns the package factory for the UML
     *  package BehavioralElements::ActivityGraphs.
     *  
     *  @return the ActivityGraphs factory instance.
     */
    public ActivityGraphsFactory getActivityGraphs() {
        return ActivityGraphsFactory.getFactory();
    }

    /** Returns the package factory for the UML
     *  package ModelManagement.
     *  
     *  @return the ModelManagement factory instance.
     */
    public ModelManagementFactory getModelManagement() {
        return ModelManagementFactory.getFactory();
    }
}
