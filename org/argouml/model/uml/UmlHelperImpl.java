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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import org.argouml.model.ActivityGraphsHelper;
import org.argouml.model.CollaborationsHelper;
import org.argouml.model.CommonBehaviorHelper;
import org.argouml.model.CoreHelper;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.ExtensionMechanismsHelper;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.model.ModelManagementHelper;
import org.argouml.model.StateMachinesHelper;
import org.argouml.model.UmlHelper;
import org.argouml.model.UseCasesHelper;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;

import ru.novosoft.uml.MBase;


/**
 * Helper class for UML metamodel.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class UmlHelperImpl implements UmlHelper {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(UmlHelperImpl.class);

    /**
     * TODO: Don't allow instantiation.
     */
    public UmlHelperImpl() {
    }

    /**
     *  Ensures that all of the elements in a model are registered
     *  to the UmlModelListener.  This is useful when the MModel is
     *  not created by the UmlFactory.
     *
     * @param model the UML model
     */
    public void addListenersToModel(Object model) {
        if (!ModelFacade.isAModel(model)) {
            throw new IllegalArgumentException();
        }

	addListenersToMBase(model);
    }

    /**
     *  Internal recursive worker to add UmlModelListener.
     *
     * @param mbase the element to add listeners to
     */
    protected void addListenersToMBase(Object mbase) {
        if (!ModelFacade.isABase(mbase)) {
            throw new IllegalArgumentException();
        }

        // TODO: Decide how to handle this. The casting is not so beautiful.
	((UmlFactoryImpl) Model.getUmlFactory())
		.addListenersToModelElement(mbase);
	Collection elements = ((MBase) mbase).getModelElementContents();
	if (elements != null) {
	    Iterator iterator = elements.iterator();
	    while (iterator.hasNext()) {
	        Object o = iterator.next();
	        if (o instanceof MBase) {
	            addListenersToMBase(o);
	        }
	    }
	}
    }

    /**
     * Returns the package helper for the UML
     * package Foundation::ExtensionMechanisms.
     *
     * @return the ExtensionMechanisms helper instance.
     */
    public ExtensionMechanismsHelper getExtensionMechanisms() {
        return Model.getExtensionMechanismsHelper();
    }

    /**
     * Returns the package helper for the UML
     * package Foundation::DataTypes.
     *
     * @return the DataTypes helper instance.
     */
    public DataTypesHelper getDataTypes() {
        return Model.getDataTypesHelper();
    }

    /**
     * Returns the package helper for the UML
     * package Foundation::Core.
     *
     * @return the Core helper instance.
     */
    public CoreHelper getCore() {
        return Model.getCoreHelper();
    }

    /**
     * Returns the package helper for the UML
     * package BehavioralElements::CommonBehavior.
     *
     * @return the CommonBehavior helper instance.
     */
    public CommonBehaviorHelper getCommonBehavior() {
        return Model.getCommonBehaviorHelper();
    }

    /**
     * Returns the package helper for the UML
     * package BehavioralElements::UseCases.
     *
     * @return the UseCases helper instance.
     */
    public UseCasesHelper getUseCases() {
        return Model.getUseCasesHelper();
    }

    /**
     * Returns the package helper for the UML
     * package BehavioralElements::StateMachines.
     *
     * @return the StateMachines helper instance.
     */
    public StateMachinesHelper getStateMachines() {
        return Model.getStateMachinesHelper();
    }

    /**
     * Returns the package helper for the UML
     * package BehavioralElements::Collaborations.
     *
     * @return the Collaborations helper instance.
     */
    public CollaborationsHelper getCollaborations() {
        return Model.getCollaborationsHelper();
    }

    /**
     * Returns the package helper for the UML
     * package BehavioralElements::ActivityGraphs.
     *
     * @return the ActivityGraphs helper instance.
     */
    public ActivityGraphsHelper getActivityGraphs() {
        return Model.getActivityGraphsHelper();
    }

    /**
     * Returns the package helper for the UML
     * package ModelManagement.
     *
     * @return the ModelManagement helper instance.
     */
    public ModelManagementHelper getModelManagement() {
        return Model.getModelManagementHelper();
    }

    /**
     * Returns the correct helper on basis of the package of base.
     *
     * @param base the modelelement
     * @return Object the helper
     */
    public Object getHelper(Object base) {
    	if (base instanceof MBase) {
	    String name = base.getClass().getName();
	    name = name.substring(0, name.lastIndexOf('.'));
	    name = name.substring(name.lastIndexOf('.') + 1, name.length());
	    Method[] methods = this.getClass().getMethods();
	    for (int i = 0; i < methods.length; i++) {
		String methodname = methods[i].getName();
		if (methodname.toLowerCase().indexOf(name) >= 0) {
		    try {
			return methods[i].invoke(this, new Object[] {});
		    } catch (IllegalAccessException e) {
                        LOG.warn(e);
		    } catch (InvocationTargetException e) {
                        LOG.warn(e);
		    }
		}
	    }
    	}
    	return null;
    }

    /**
     * Returns the owner of some modelelement object. In most cases this will be
     * the owning namespace but in some cases it will be null (the root model)
     * or for instance the owning class with an attribute.
     * @param handle the modelelement
     * @return Object the owner
     */
    public Object getOwner(Object handle) {
        if (handle instanceof MBase) {
            return ((MBase) handle).getModelElementContainer();
        }
        return null;
    }

    /**
     * Utility method to quickly delete a collection of modelelements. This
     * method should only be called from within the model component. The only
     * reason it is public is that the other helpers/factories are in other
     * packages and therefore cannot see this method if it is not public.
     * @param col a collection of modelelements
     */
    public void deleteCollection(Collection col) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
            Model.getUmlFactory().delete(it.next());
        }
    }

    /**
     * Returns the source of some relationship.
     * This is the element in binary relations from which a relation 'departs'.
     * @param relationShip the relationship to be tested
     * @return the source of the relationship
     */
    public Object getSource(Object relationShip) {
        if (relationShip == null) {
            throw new IllegalArgumentException("Argument relationship is null");
        }
        if (!(ModelFacade.isAModelElement(relationShip)
                || relationShip instanceof CommentEdge)) {
           throw new IllegalArgumentException("Argument relationship of class "
                   + relationShip.getClass().toString()
                   + " is not a valid relationship");
        }
        if (relationShip instanceof CommentEdge) {
            return ((CommentEdge) relationShip).getSource();
        }
        if (ModelFacade.isARelationship(relationShip)) {
            // handles all children of relationship including extend and
            // include which are not members of core
            return Model.getCoreHelper().getSource(relationShip);
        }
        if (ModelFacade.isATransition(relationShip)) {
            return Model.getStateMachinesHelper().getSource(relationShip);
        }
        if (ModelFacade.isAAssociationEnd(relationShip)) {
            return Model.getCoreHelper().getSource(relationShip);
        }
        return null;
    }

    /**
     * Returns the destination of some relationship.
     * This is the element in binary relations at which a relation 'arrives'.
     * @param relationShip  the relationship to be tested
     * @return the destination of the relationship
     */
    public Object getDestination(Object relationShip) {
        if (relationShip == null) {
            throw new IllegalArgumentException("Argument relationship is null");
        }
        if (!(ModelFacade.isAModelElement(relationShip)
                || relationShip instanceof CommentEdge)) {
           throw new IllegalArgumentException("Argument relationship of class "
                   + relationShip.getClass().toString()
                   + " is not a valid relationship");
        }
        if (relationShip instanceof CommentEdge) {
            return ((CommentEdge) relationShip).getDestination();
        }
        if (ModelFacade.isARelationship(relationShip)) {
            // handles all children of relationship including extend and
            // include which are not members of core
            return Model.getCoreHelper().getDestination(relationShip);
        }
        if (ModelFacade.isATransition(relationShip)) {
            return Model.getStateMachinesHelper().getDestination(relationShip);
        }
        if (ModelFacade.isAAssociationEnd(relationShip)) {
            return Model.getCoreHelper().getDestination(relationShip);
        }
        return null;
    }

}
