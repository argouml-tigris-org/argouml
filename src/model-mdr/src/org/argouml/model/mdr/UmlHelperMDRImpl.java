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

package org.argouml.model.mdr;

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.CollaborationsHelper;
import org.argouml.model.CommonBehaviorHelper;
import org.argouml.model.CoreHelper;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.ExtensionMechanismsHelper;
import org.argouml.model.ModelManagementHelper;
import org.argouml.model.StateMachinesHelper;
import org.argouml.model.UmlHelper;
import org.argouml.model.UseCasesHelper;
import org.omg.uml.behavioralelements.statemachines.Transition;
import org.omg.uml.foundation.core.AssociationEnd;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Relationship;

/**
 * Helper class for UML metamodel.
 * 
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class UmlHelperMDRImpl implements UmlHelper {

    /**
     * The model implementation.
     */
    private MDRModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     * 
     * @param implementation
     *            To get other helpers and factories.
     */
    UmlHelperMDRImpl(MDRModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Ensures that all of the elements in a model are registered to the
     * UmlModelListener. 
     * 
     * This does nothing for the MDR implementation since we get events
     * for all model elements by default.
     * 
     * @param model
     *            the UML model
     */
    public void addListenersToModel(Object model) {
    }

    /**
     * Returns the package helper for the UML package
     * Foundation::ExtensionMechanisms.
     * 
     * @return the ExtensionMechanisms helper instance.
     */
    public ExtensionMechanismsHelper getExtensionMechanisms() {
        return nsmodel.getExtensionMechanismsHelper();
    }

    /**
     * Returns the package helper for the UML package Foundation::DataTypes.
     * 
     * @return the DataTypes helper instance.
     */
    public DataTypesHelper getDataTypes() {
        return nsmodel.getDataTypesHelper();
    }

    /**
     * Returns the package helper for the UML package Foundation::Core.
     * 
     * @return the Core helper instance.
     */
    public CoreHelper getCore() {
        return nsmodel.getCoreHelper();
    }

    /**
     * Returns the package helper for the UML package
     * BehavioralElements::CommonBehavior.
     * 
     * @return the CommonBehavior helper instance.
     */
    public CommonBehaviorHelper getCommonBehavior() {
        return nsmodel.getCommonBehaviorHelper();
    }

    /**
     * Returns the package helper for the UML package
     * BehavioralElements::UseCases.
     * 
     * @return the UseCases helper instance.
     */
    public UseCasesHelper getUseCases() {
        return nsmodel.getUseCasesHelper();
    }

    /**
     * Returns the package helper for the UML package
     * BehavioralElements::StateMachines.
     * 
     * @return the StateMachines helper instance.
     */
    public StateMachinesHelper getStateMachines() {
        return nsmodel.getStateMachinesHelper();
    }

    /**
     * Returns the package helper for the UML package
     * BehavioralElements::Collaborations.
     * 
     * @return the Collaborations helper instance.
     */
    public CollaborationsHelper getCollaborations() {
        return nsmodel.getCollaborationsHelper();
    }

    /**
     * Returns the package helper for the UML package
     * BehavioralElements::ActivityGraphs.
     * 
     * @return the ActivityGraphs helper instance.
     */
    public ActivityGraphsHelper getActivityGraphs() {
        return nsmodel.getActivityGraphsHelper();
    }

    /**
     * Returns the package helper for the UML package ModelManagement.
     * 
     * @return the ModelManagement helper instance.
     */
    public ModelManagementHelper getModelManagement() {
        return nsmodel.getModelManagementHelper();
    }

    /**
     * Returns the owner of some modelelement object. In most cases this will be
     * the owning namespace but in some cases it will be null (the root model)
     * or for instance the owning class with an attribute.
     * 
     * @param handle
     *            the modelelement
     * @return Object the owner
     */
    // TODO: Delete?  Redundant with getModelElementContainer
    public Object getOwner(Object handle) {
        // TODO: Not yet implemented
        throw new RuntimeException("Not yet implemented");
//        if (handle instanceof Attribute) {
//            return ((Attribute) handle).getOwner();
//        }
//        if (handle instanceof ModelElement) {
//            return ((ModelElement) handle).getNamespace();
//        }
//        if (handle instanceof Attribute) {
//            return ((Attribute) handle).getOwner();
//        }
//        return null;
    }

    /**
     * Utility method to quickly delete a collection of modelelements. This
     * method should only be called from within the model component. The only
     * reason it is public is that the other helpers/factories are in other
     * packages and therefore cannot see this method if it is not public.
     * 
     * @param col
     *            a collection of modelelements
     */
    public void deleteCollection(Collection col) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
            nsmodel.getUmlFactory().delete(it.next());
        }
    }

    /**
     * Returns the source of some relationship. This is the element in binary
     * relations from which a relation 'departs'.
     * 
     * @param relationShip
     *            the relationship to be tested
     * @return the source of the relationship
     */
    public Object getSource(Object relationShip) {
        if (relationShip == null) {
            throw new IllegalArgumentException("Argument relationship is null");
        }
        if (!(relationShip instanceof ModelElement)) {
            throw new IllegalArgumentException(
                    "Argument relationship of class "
                            + relationShip.getClass().toString()
                            + " is not a valid relationship");
        }
        if (relationShip instanceof Relationship) {
            // handles all children of relationship including extend and
            // include which are not members of core
            return nsmodel.getCoreHelper().getSource(relationShip);
        }
        if (relationShip instanceof Transition) {
            return nsmodel.getStateMachinesHelper().getSource(relationShip);
        }
        if (relationShip instanceof AssociationEnd) {
            return nsmodel.getCoreHelper().getSource(relationShip);
        }
        return null;
    }

    /**
     * Returns the destination of some relationship. This is the element in
     * binary relations at which a relation 'arrives'.
     * 
     * @param relationShip
     *            the relationship to be tested
     * @return the destination of the relationship
     */
    public Object getDestination(Object relationShip) {
        if (relationShip == null) {
            throw new IllegalArgumentException("Argument relationship is null");
        }

        if (!(relationShip instanceof ModelElement)) {
            throw new IllegalArgumentException(
                    "Argument relationship of class "
                            + relationShip.getClass().toString()
                            + " is not a valid relationship");
        }

        if (relationShip instanceof Relationship) {
            // handles all children of relationship including extend and
            // include which are not members of core
            return nsmodel.getCoreHelper().getDestination(relationShip);
        }

        if (relationShip instanceof Transition) {
            return nsmodel.getStateMachinesHelper().
                    getDestination(relationShip);
        }

        if (relationShip instanceof AssociationEnd) {
            return nsmodel.getCoreHelper().getDestination(relationShip);
        }
        return null;
    }

}
