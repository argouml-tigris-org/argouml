// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

// File: UMLExtendListModel.java
// Classes: UMLExtendListModel

package org.argouml.uml.ui.behavior.use_cases;

import java.util.*;

import org.argouml.uml.ui.UMLBinaryRelationListModel;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.tigris.gef.graph.MutableGraphModel;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesFactory;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;


/**
 * A list model for the extend relationship on use case and extension
 * point property panels.<p>
 *
 * Supports the full menu (Open, Add, Delete, Move Up, Move
 * Down). Provides its own formatElement routine, to use the name of
 * the base use case (where the container is a use case) or the
 * extension use case (where the container is an extension point,
 * rather than name of the extend relationship itself.<p>
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 * TODO: What is this replaced by?
 * this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 * that used reflection a lot.
 */
public class UMLExtendListModel extends UMLBinaryRelationListModel  {
    /**
     * @deprecated by Linus Tolke as of 0.15.4. Use your own logger in your
     * class. This will be removed.
     */
    protected static Logger cat = Logger.getLogger(UMLExtendListModel.class);

    /**
     * Create a new list model.<p>
     *
     * Implementation is just an invocation of the parent constructor.<p>
     *
     * @param container The container for this list - the use case
     * property panel.
     *
     * @param property The name associated with the NSUML {@link
     * ru.novosoft.uml.MElementEvent} that we are tracking or
     * <code>null</code> if we track them all. We probably want to
     * just track the "extend" event.
     *
     * @param showNone   True if an empty list is represented by the keyword
     *                   "none"
     */
    public UMLExtendListModel(UMLUserInterfaceContainer container,
                              String property, boolean showNone) {

        super(container, property, showNone);
    }


    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#build(Object,Object)
     */
    protected void build(Object/*MModelElement*/ from,
			 Object/*MModelElement*/ to) {
        if (ModelFacade.isAUseCase(from) && ModelFacade.isAUseCase(to)) {
            UseCasesFactory.getFactory().buildExtend(/*(MUseCase)*/ to,
						     /*(MUseCase)*/ from);
        } else
            throw new IllegalArgumentException("In build of UMLExtendListModel:"
					       + " either the arguments are "
					       + "null or "
					       + "not instanceof MUseCase");
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#connect(
     *          MutableGraphModel, Object, Object)
     */
    protected void connect(
			   MutableGraphModel gm,
			   Object/*MModelElement*/ from,
			   Object/*MModelElement*/ to) {
	gm.connect(from, to, (Class) ModelFacade.EXTEND);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getAddDialogTitle()
     */
    protected String getAddDialogTitle() {
        return Translator.localize("UMLMenu",
				   "dialog.title.add-extended-usecases");
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getChoices()
     */
    protected Collection getChoices() {
        return ModelManagementHelper.getHelper()
	    .getAllModelElementsOfKind(getTarget().getClass());
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getRelation(
     *          Object, Object)
     */
    protected Object/*MModelElement*/ getRelation(Object/*MModelElement*/ from,
						  Object/*MModelElement*/ to) {
        return UseCasesHelper.getHelper().getExtends(/*(MUseCase)*/ to,
						     /*(MUseCase)*/ from);
    }

    /**
     * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getSelected()
     */
    protected Collection getSelected() {
        if (ModelFacade.isAUseCase(getTarget())) {
            return UseCasesHelper.getHelper().getExtendedUseCases(getTarget());
        } else {
            throw new IllegalStateException("target is not "
					    + "an instanceof MUseCase");
	}
    }

} /* End of class UMLExtendListModel */
