/* $Id$
 *******************************************************************************
 * Copyright (c) 2010-2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Michiel van der Wulp
 *******************************************************************************
 *
 * Some portions of this file were previously release using the BSD License:
 */
// $Id$
// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.ActionCreateContainedModelElement;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.ActionEdgesDisplay;
import org.argouml.uml.diagram.ui.FigCompartment;
import org.argouml.uml.diagram.ui.FigCompartmentBox;
import org.argouml.uml.diagram.ui.FigOperationsCompartment;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;

/**
 * Class to display graphics for any UML Classifier in a diagram.<p>
 * 
 * This abstract Fig adds an Operations compartment.
 */
public abstract class FigClassifierBox extends FigCompartmentBox {

    /**
     * The Fig for the operations compartment (if any).
     */
    private FigOperationsCompartment operationsFigCompartment;
    
    private Rectangle getDefaultBounds() {
        // this rectangle marks the operation section; all operations
        // are inside it
        Rectangle bounds = new Rectangle(DEFAULT_COMPARTMENT_BOUNDS);
        // 2nd compartment, so adjust Y appropriately
        bounds.y = DEFAULT_COMPARTMENT_BOUNDS.y + ROWHEIGHT + 1;
        return bounds;
    }

    /**
     * Construct a Fig with owner, bounds, and settings.
     * 
     * @param owner the model element that owns this fig
     * @param bounds the rectangle defining the bounds
     * @param settings the rendering settings
     */
    public FigClassifierBox(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        operationsFigCompartment = new FigOperationsCompartment(
                owner, 
                getDefaultBounds(),
                getSettings());
    }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigClassifierBox figClone = (FigClassifierBox) super.clone();
        Iterator thisIter = this.getFigs().iterator();
        while (thisIter.hasNext()) {
            Fig thisFig = (Fig) thisIter.next();
            if (thisFig == operationsFigCompartment) {
                figClone.operationsFigCompartment = (FigOperationsCompartment) thisFig;
                return figClone;
            }
        }
        return figClone;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        super.renderingChanged();
        // TODO: We should be able to just call renderingChanged on the child
        // figs here instead of doing an updateOperations...
        updateCompartment(Model.getMetaTypes().getOperation());
        
        // TODO: Taken from FigClassifierBoxWithAttribute to handle events
        // on an attribute. All this event handling should eventually be moved
        // to the compartment Fig for attributes
        if (isCompartmentVisible(Model.getMetaTypes().getAttribute())) {
            // TODO: We shouldn't actually have to do all this work
            updateCompartment(Model.getMetaTypes().getAttribute());
        }
    }
    
    /**
     * We are getting events we don't want. Filter them out.
     * TODO: Can we instruct the model event pump not to send these in the
     * first place? See defect 5095.
     * @param event the event
     */
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("generalization")
                && Model.getFacade().isAGeneralization(event.getOldValue())) {
            return;
        } else if (event.getPropertyName().equals("association")
                && Model.getFacade().isAAssociationEnd(event.getOldValue())) {
            return;
        } else if (event.getPropertyName().equals("supplierDependency")
                && Model.getFacade().isAUsage(event.getOldValue())) {
            return;
        } else if (event.getPropertyName().equals("clientDependency")
                && Model.getFacade().isAAbstraction(event.getOldValue())) {
            return;
        }
        
        super.propertyChange(event);
    }

    protected void updateLayout(UmlChangeEvent event) {
        super.updateLayout(event);
        if (event instanceof AssociationChangeEvent 
                && getOwner().equals(event.getSource())) {
            Object o = null;
            if (event instanceof AddAssociationEvent) {
                o = event.getNewValue();
            } else if (event instanceof RemoveAssociationEvent) {
                o = event.getOldValue();
            }
            if ((Model.getFacade().isAOperation(o) 
                    || Model.getFacade().isAReception(o))
                    && isCompartmentVisible(
                            Model.getMetaTypes().getOperation())) {
                operationsFigCompartment.populate();
                setBounds(getBounds());
                damage();
            }
        }
        
        // TODO: Taken from FigClassifierBoxWithAttribute to handle events
        // on an attribute. All this event handling should eventually be moved
        // to the compartment Fig for attributes
        if (Model.getFacade().isAAttribute(getOwner())) {
            if (event instanceof AttributeChangeEvent) {
                Object source = event.getSource();
                if (Model.getFacade().isAAttribute(source)) {
                    // TODO: We just need to get someone to re-render a single
                    // line of text which represents the element here, but I'm
                    // not sure how to do that. - tfm
                    // TODO: Bob replies - we shouldn't be interested in this
                    // event here. The FigFeature (or its notation) should be
                    // listen for change and the FigFeature should be update
                    // from that.
                    updateCompartment(Model.getMetaTypes().getAttribute());
                }
            } else if (event instanceof AssociationChangeEvent 
                    && getOwner().equals(event.getSource())) {
                Object o = null;
                if (event instanceof AddAssociationEvent) {
                    o = event.getNewValue();
                } else if (event instanceof RemoveAssociationEvent) {
                    o = event.getOldValue();
                }
                if (Model.getFacade().isAAttribute(o)) {
                    // TODO: Bob says - we should not be listening here for
                    // addition and removal of attributes. This should be done in
                    // FigAttributesCompartment.
                    updateCompartment(Model.getMetaTypes().getAttribute());
                }
            }
        }
    }
    
    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
       
        if (isCompartmentVisible(Model.getMetaTypes().getAttribute())) {
            Set<Object[]> listeners = new HashSet<Object[]>();

            // Collect the set of model elements that we want to listen to
            if (newOwner != null) {
                // TODO: Because we get called on each and every change event, when
                // the model is in a state of flux, we'll often get an
                // InvalidElementException before we finish this collection. The
                // only saving grace is that we're called SO many times that on the
                // last time, things should be stable again and we'll get a good set
                // of elements for the final update.  We need a better mechanism.
                
                // add the listeners to the newOwner
                listeners.add(new Object[] {newOwner, null});
                
                // and its stereotypes
                // TODO: Aren't stereotypes handled elsewhere?
                for (Object stereotype 
                        : Model.getFacade().getStereotypes(newOwner)) {
                    listeners.add(new Object[] {stereotype, null});
                }

                // and its features
                for (Object feat : Model.getFacade().getFeatures(newOwner)) {
                    listeners.add(new Object[] {feat, null});
                    // and the stereotypes of its features
                    for (Object stereotype 
                            : Model.getFacade().getStereotypes(feat)) {
                        listeners.add(new Object[] {stereotype, null});
                    }
                    // and the parameter of its operations
                    if (Model.getFacade().isAOperation(feat)) {
                        for (Object param : Model.getFacade().getParameters(feat)) {
                            listeners.add(new Object[] {param, null});
                        }
                    }
                }
            }
            
            // Update the listeners to match the desired set using the minimal
            // update facility
            updateElementListeners(listeners);
        } else {
            super.updateListeners(oldOwner, newOwner);
        }
    }
    
    /**
     * Updates a compartment box. Called from updateLayout if there is
     * a model event effecting the attributes/operations and from
     * renderingChanged in all cases.
     * TODO: The above statement means that the entire contents of the
     * compartments are being rebuilt whenever an add/remove
     * of an attribute, operation or a reception is detected. It would be
     * better to have compartments listen for add and remove events
     * and make minimum change rather than entirely rebuild. 
     * Remark MVW: This is a bit exaggerated, since the populate() 
     * method is already heavily optimized.
     */
    protected void updateCompartment(Object metaType) {
        FigCompartment fc = getCompartment(metaType);
        if (!fc.isVisible()) {
            return;
        }
        fc.populate();
    
        // TODO: make setBounds, calcBounds and updateBounds consistent
        setBounds(getBounds());
    }

    /*
     * @see org.tigris.gef.presentation.Fig#translate(int, int)
     */
    public void translate(int dx, int dy) {
        super.translate(dx, dy);
        Editor ce = Globals.curEditor();
        if (ce != null) {
            Selection sel = ce.getSelectionManager().findSelectionFor(this);
            // TODO" What is the purpose of this? Why do we hide buttons here?
            // Presumably if so we should not assume SelectionClass
            if (sel instanceof SelectionClass) {
                ((SelectionClass) sel).hideButtons();
            }
        }
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on an Interface.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
    
        // Add ...
        ArgoJMenu addMenu = buildAddMenu();
        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                addMenu);

        // Modifier ...
        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                buildModifierPopUp());
    
        // Visibility ...
        popUpActions.add(
                popUpActions.size() - getPopupAddOffset(),
                buildVisibilityPopUp());
    
        return popUpActions;
    }

    protected ArgoJMenu buildShowPopUp() {
        ArgoJMenu showMenu = super.buildShowPopUp();

        Iterator i = ActionCompartmentDisplay.getActions().iterator();
        while (i.hasNext()) {
            showMenu.add((Action) i.next());
        }
        return showMenu;
    }

    protected ArgoJMenu buildAddMenu() {
        ArgoJMenu addMenu = new ArgoJMenu("menu.popup.add");
        
        List<FigCompartment> comps = getCompartments();
        for (FigCompartment comp : comps) {
            final Object metaType = comp.getCompartmentType();
            Action addAction = new ActionCreateContainedModelElement(metaType, getOwner());
            addAction.setEnabled(isSingleTarget());
            addMenu.insert(addAction, 0);
        }
        addMenu.add(new ActionAddNote());
        addMenu.add(ActionEdgesDisplay.getShowEdges());
        addMenu.add(ActionEdgesDisplay.getHideEdges());
        return addMenu;
    }

    /**
     * USED BY PGML.tee.
     * TODO We should loop round the compartments to build this string. That
     * way we have no attribute/operation knowledge at this level.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    public String classNameAndBounds() {
        String classNameAndBounds =  super.classNameAndBounds()
            + "operationsVisible="
            + isCompartmentVisible(Model.getMetaTypes().getOperation()) + ";";
        FigCompartment fc = getCompartment(Model.getMetaTypes().getAttribute());
        if (fc != null) {
            classNameAndBounds += 
                "attributesVisible=" + fc.isVisible() + ";";
        }
        return classNameAndBounds;
    }

    
    protected Object buildModifierPopUp() {
        return buildModifierPopUp(ABSTRACT | LEAF | ROOT);
    }
}
