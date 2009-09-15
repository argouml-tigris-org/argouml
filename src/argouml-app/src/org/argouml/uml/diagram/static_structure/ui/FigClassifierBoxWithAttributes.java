// $Id$
// Copyright (c) 2008-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigAttributesCompartment;
import org.argouml.uml.ui.foundation.core.ActionAddAttribute;

/**
 * A Fig for a ClassifierBox that adds an attributes compartment.
 *
 * @author Michiel
 */
public class FigClassifierBoxWithAttributes extends FigClassifierBox 
    implements AttributesCompartmentContainer {

    private static final Logger LOG = 
        Logger.getLogger(FigClassifierBoxWithAttributes.class);
    
    private FigAttributesCompartment attributesFigCompartment;
    
    /**
     * Construct a Fig with owner, bounds, and settings.
     * 
     * @param owner the model element that owns this fig
     * @param bounds the rectangle defining the bounds
     * @param settings the rendering settings
     */
    public FigClassifierBoxWithAttributes(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        getAttributesCompartment(); // this creates the compartment fig
    }

    /**
     * @return The graphics for the UML attributes (if any).
     * @deprecated use getAttributesCompartment
     */
    protected FigAttributesCompartment getAttributesFig() {
        return getAttributesCompartment();
    }
    
    public Rectangle getAttributesBounds() {
        return getAttributesCompartment().getBounds();
    }

    public boolean isAttributesVisible() {
        return attributesFigCompartment != null 
            && attributesFigCompartment.isVisible();
    }
    
    /*
     * Insert an Add Attribute entry before the default menu.
     * 
     * @see org.argouml.uml.diagram.static_structure.ui.FigClassifierBox#buildAddMenu()
     */
    @Override
    protected ArgoJMenu buildAddMenu() {
        ArgoJMenu addMenu = super.buildAddMenu();
        Action addAttribute = new ActionAddAttribute();
        addAttribute.setEnabled(isSingleTarget());
        addMenu.insert(addAttribute, 0);
        return addMenu;
    }
    
    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    @Override
    public String classNameAndBounds() {
        return super.classNameAndBounds()
            + "attributesVisible=" + isAttributesVisible() + ";";
    }

    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
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
    }
    
    @Override
    public void renderingChanged() {
        super.renderingChanged();
        if (getOwner() != null) {
            // TODO: We shouldn't actually have to do all this work
            updateAttributes();
        }
    }
    
    /*
     * TODO: Based on my comments below, with that work done,
     * this method can be removed - Bob.
     */
    @Override
    protected void updateLayout(UmlChangeEvent event) {
        super.updateLayout(event);

        if (event instanceof AttributeChangeEvent) {
            Object source = event.getSource();
            if (Model.getFacade().isAAttribute(source)) {
                // TODO: We just need to get someone to rerender a single line
                // of text which represents the element here, but I'm not sure
                // how to do that. - tfm
                // TODO: Bob replies - we shouldn't be interested in this event
                // here. The FigFeature (or its notation) should be listen for
                // change and the FigFeature should be update from that.
                updateAttributes();
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
                updateAttributes();
            }
        }
    }

    /**
     * Updates the attributes in the fig. Called from modelchanged if there is
     * a modelevent effecting the attributes and from renderingChanged in all
     * cases.
     * TODO: Looks like this should be private - Bob.
     */
    protected void updateAttributes() {
        if (!isAttributesVisible()) {
            return;
        }
        attributesFigCompartment.populate();

        // TODO: make setBounds, calcBounds and updateBounds consistent
        setBounds(getBounds());
    }
    
    /**
     * @param isVisible true if the attribute compartment is visible
     *
     * @see org.argouml.uml.diagram.AttributesCompartmentContainer#setAttributesVisible(boolean)
     */
    public void setAttributesVisible(boolean isVisible) {
        setCompartmentVisible(attributesFigCompartment, isVisible);
    }
    @Override
    public Dimension getMinimumSize() {
        // Use "aSize" to build up the minimum size. Start with the size of the
        // name compartment and build up.
        Dimension aSize = getNameFig().getMinimumSize();

        /* Only take into account the stereotype width, not the height, 
         * since the height is included in the name fig: */
        aSize = addChildWidth(aSize, getStereotypeFig());
        aSize = addChildDimensions(aSize, getAttributesCompartment());
        aSize = addChildDimensions(aSize, getOperationsFig());

        /* We want to maintain a minimum width for the 
         * fig. Also, add the border dimensions 
         * to the minimum space required for its contents: */
        aSize.width = Math.max(WIDTH, aSize.width);
        aSize.width += 2 * getLineWidth();
        aSize.height += 2 * getLineWidth();
    
        return aSize;
    }

    /**
     * Sets the bounds, but the size will be at least the one returned by
     * {@link #getMinimumSize()}, unless checking of size is disabled.<p>
     *
     * If the required height is bigger, then the additional height is
     * equally distributed among all compartments, such that the
     * accumulated height of all visible figs equals the demanded height.
     *
     * @param x  Desired X coordinate of upper left corner
     *
     * @param y  Desired Y coordinate of upper left corner
     *
     * @param width  Desired width of the Fig
     *
     * @param height  Desired height of the Fig
     * 
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
    protected void setStandardBounds(final int x, final int y, final int w,
            final int h) {

        // Save our old boundaries so it can be used in property message later
        Rectangle oldBounds = getBounds();

        // Make sure we don't try to set things smaller than the minimum
        Dimension minimumSize = getMinimumSize();
        int newW = Math.max(w, minimumSize.width);
        int newH = Math.max(h, minimumSize.height);
        
        int currentHeight = 0;

        if (getStereotypeFig().isVisible()) {
            int stereotypeHeight = getStereotypeFig().getMinimumSize().height;
            getNameFig().setTopMargin(stereotypeHeight);
            getStereotypeFig().setBounds(
                    x + getLineWidth(),
                    y + getLineWidth(),
                    newW - 2 * getLineWidth(),
                    stereotypeHeight);
        } else {
            getNameFig().setTopMargin(0);
        }

        /* Now the new nameFig height will include the stereotype height: */
        Dimension nameMin = getNameFig().getMinimumSize();
        int minNameHeight = Math.max(nameMin.height, NAME_FIG_HEIGHT);
        
        getNameFig().setBounds(
                x + getLineWidth(), 
                y + getLineWidth(), 
                newW - 2 * getLineWidth(), 
                minNameHeight);
        
        /* The new height can not be less than the name height: */
        /* TODO: Is this needed/correct? 
         * For when all compartments are hidden? */
        newH = Math.max(minNameHeight + 2 * getLineWidth(), newH);
        
        currentHeight += minNameHeight;

        int attributesHeight = 0;
        int operationsHeight = 0;
        int visibleCompartments = 0;

        if (getAttributesCompartment().isVisible()) {
            visibleCompartments++;
            attributesHeight = 
                getAttributesCompartment().getMinimumSize().height;
        }
        if (getOperationsFig().isVisible()) {
            visibleCompartments++;
            operationsHeight = getOperationsFig().getMinimumSize().height;
        }
        
        int requestedHeight = newH - currentHeight - 2 * getLineWidth();
        int neededHeight = attributesHeight + operationsHeight;
        
        if (requestedHeight > neededHeight) {
            /* Distribute the extra height over the visible compartments: */
            if (getAttributesCompartment().isVisible()) {
                attributesHeight += (requestedHeight - neededHeight) / visibleCompartments;
            }
            if (getOperationsFig().isVisible()) {
                operationsHeight += (requestedHeight - neededHeight) / visibleCompartments;
            }
        } else if (requestedHeight < neededHeight) {
            /* Increase the height of the fig: */
            newH += neededHeight - requestedHeight;
        }

        if (getAttributesCompartment().isVisible()) {
            getAttributesCompartment().setBounds(
                    x + getLineWidth(),
                    y + currentHeight + getLineWidth(),
                    newW - 2 * getLineWidth(),
                    attributesHeight);
            currentHeight += attributesHeight;
        }
        
        if (getOperationsFig().isVisible()) {
            getOperationsFig().setBounds(
                    x + getLineWidth(),
                    y + currentHeight + getLineWidth(),
                    newW - 2 * getLineWidth(),
                    operationsHeight);
        }
        
        // set bounds of big box
        getBigPort().setBounds(x, y, newW, newH);
        getBorderFig().setBounds(x, y, newW, newH);

        // Now force calculation of the bounds of the figure, update the edges
        // and trigger anyone who's listening to see if the "bounds" property
        // has changed.

        calcBounds();
        updateEdges();
        LOG.debug("Bounds change : old - " + oldBounds + ", new - " 
                + getBounds());
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * @return the Fig for the EnumerationLiterals compartment
     */
    public FigAttributesCompartment getAttributesCompartment() {
        // Set bounds will be called from our superclass constructor before
        // our constructor has run, so make sure this gets set up if needed.
        if (attributesFigCompartment == null) {
            attributesFigCompartment = new FigAttributesCompartment(
                    getOwner(),
                    DEFAULT_COMPARTMENT_BOUNDS, 
                    getSettings());
        }
        return attributesFigCompartment;
    }
    
}
