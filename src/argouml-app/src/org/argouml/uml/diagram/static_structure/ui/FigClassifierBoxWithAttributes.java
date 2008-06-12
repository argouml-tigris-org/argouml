// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2008 The Regents of the University of California. All
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Action;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.argouml.uml.diagram.ui.FigAttributesCompartment;
import org.argouml.uml.ui.foundation.core.ActionAddAttribute;
import org.tigris.gef.presentation.Fig;

/**
 * A Fig for a ClassifierBox that adds an attributes compartment.
 *
 * @author Michiel
 */
public class FigClassifierBoxWithAttributes extends FigClassifierBox 
    implements AttributesCompartmentContainer {

    private FigAttributesCompartment attributesFigCompartment;

    /**
     * The constructor.
     */
    public FigClassifierBoxWithAttributes() {
        super();
        attributesFigCompartment =
            new FigAttributesCompartment(10, 30, 60, ROWHEIGHT + 2);
        
    }

    /**
     * @return The vector of graphics for operations (if any).
     * First one is the rectangle for the entire operations box.
     */
    protected FigAttributesCompartment getAttributesFig() {
        return attributesFigCompartment;
    }
    
    public Rectangle getAttributesBounds() {
        return attributesFigCompartment.getBounds();
    }

    public boolean isAttributesVisible() {
        return attributesFigCompartment.isVisible();
    }
    
    /*
     * Insert an Add Attribute entry before the default menu.
     * 
     * @see org.argouml.uml.diagram.static_structure.ui.FigClassifierBox#buildAddMenu()
     */
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
    public String classNameAndBounds() {
        return super.classNameAndBounds()
            + "attributesVisible=" + isAttributesVisible() + ";";
    }

    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        if (newOwner != null) {
            // add the listeners to the newOwner
            addElementListener(newOwner);
            // and its stereotypes
            // TODO: Aren't stereotypes handled elsewhere?
            Collection c = new ArrayList(
                    Model.getFacade().getStereotypes(newOwner));
            // and its features
            for (Object feat : Model.getFacade().getFeatures(newOwner)) {
                c.add(feat);
                // and the stereotypes of its features
                c.addAll(new ArrayList(Model.getFacade().getStereotypes(feat)));
                // and the parameter of its operations
                if (Model.getFacade().isAOperation(feat)) {
                    c.addAll(Model.getFacade().getParameters(feat));
                }
            }
            // And now add listeners to them all:
            for (Object obj : c) {
                addElementListener(obj);
            }
        }
    }
    
    public void renderingChanged() {
        if (getOwner() != null) {
            updateAttributes();
        }
        super.renderingChanged();
    }
    
    /*
     * TODO: Based on my comments below, with that work done,
     * this method can be removed - Bob.
     */
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

    protected void updateStereotypeText() {

        Rectangle rect = getBounds();

        int stereotypeHeight = 0;
        if (getStereotypeFig().isVisible()) {
            stereotypeHeight = getStereotypeFig().getHeight();
        }
        int heightWithoutStereo = getHeight() - stereotypeHeight;

        getStereotypeFig().setOwner(getOwner());

        stereotypeHeight = 0;
        if (getStereotypeFig().isVisible()) {
            stereotypeHeight = getStereotypeFig().getHeight();
        }

        int minWidth = this.getMinimumSize().width;
        if (minWidth > rect.width) {
            rect.width = minWidth;
        }

        setBounds(
                rect.x,
                rect.y,
                rect.width,
                heightWithoutStereo + stereotypeHeight);
        calcBounds();
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

        Rectangle rect = getBounds();
        // ouch ugly but that's for a next refactoring
        // TODO: make setBounds, calcBounds and updateBounds consistent
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }
    
    /**
     * @param isVisible true if the attribute compartment is visible
     *
     * @see org.argouml.uml.diagram.AttributesCompartmentContainer#setAttributesVisible(boolean)
     */
    public void setAttributesVisible(boolean isVisible) {
        Rectangle rect = getBounds();
        if (getAttributesFig().isVisible()) {
            if (!isVisible) {  // hide compartment
                damage();
                Iterator it = getAttributesFig().getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(false);
                }
                getAttributesFig().setVisible(false);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
                          (int) aSize.getWidth(), (int) aSize.getHeight());
            }
        } else {
            if (isVisible) { // show compartment
                Iterator it = getAttributesFig().getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(true);
                }
                getAttributesFig().setVisible(true);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
                          (int) aSize.getWidth(), (int) aSize.getHeight());
                damage();
            }
        }
    }

    public Dimension getMinimumSize() {
        // Use "aSize" to build up the minimum size. Start with the size of the
        // name compartment and build up.

        Dimension aSize = getNameFig().getMinimumSize();
        aSize.height += 4; // +2 padding above and below name
        aSize.height = Math.max(21, aSize.height);

        aSize = addChildDimensions(aSize, getStereotypeFig());
        aSize = addChildDimensions(aSize, getAttributesFig());
        aSize = addChildDimensions(aSize, getOperationsFig());

        aSize.width = Math.max(60, aSize.width);

        return aSize;
    }

    /**
     * Add size of a child component to overall size.  Width is maximized
     * with child's width and child's height is added to the overall height.
     * If the child figure is not visible, it's size is not added.
     * 
     * @param size current dimensions
     * @param child child figure
     * @return new Dimension with child size added
     */
    protected Dimension addChildDimensions(Dimension size, Fig child) {
        if (child.isVisible()) {
            Dimension childSize = child.getMinimumSize();
            size.width = Math.max(size.width, childSize.width);
            size.height += childSize.height;
        }
        return size;
    }

    /**
     * Sets the bounds, but the size will be at least the one returned by
     * {@link #getMinimumSize()}, unless checking of size is disabled.<p>
     *
     * If the required height is bigger, then the additional height is
     * equally distributed among all figs (i.e. compartments), such that the
     * cumulated height of all visible figs equals the demanded height<p>.
     *
     * Some of this has "magic numbers" hardcoded in. In particular there is
     * a knowledge that the minimum height of a name compartment is 21
     * pixels.<p>
     *
     * @param x  Desired X coordinate of upper left corner
     *
     * @param y  Desired Y coordinate of upper left corner
     *
     * @param w  Desired width of the FigClass
     *
     * @param h  Desired height of the FigClass
     * 
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setStandardBounds(final int x, final int y, final int w,
            final int h) {

        // Save our old boundaries so it can be used in property message later
        Rectangle oldBounds = getBounds();

        // set bounds of big box
        getBigPort().setBounds(x, y, w, h);
        borderFig.setBounds(x, y, w, h);
        
        // Save our old boundaries (needed later), and get minimum size
        // info. "whitespace" will be used to maintain a running calculation
        // of our size at various points.
        final int whitespace = h - getMinimumSize().height;

        getNameFig().setLineWidth(0);
        getNameFig().setLineColor(Color.red);
        int currentHeight = 0;

        if (getStereotypeFig().isVisible()) {
            int stereotypeHeight = getStereotypeFig().getMinimumSize().height;
            getStereotypeFig().setBounds(
                    x,
                    y,
                    w,
                    stereotypeHeight);
            currentHeight += stereotypeHeight;
        }

        int nameHeight = getNameFig().getMinimumSize().height;
        getNameFig().setBounds(x, y + currentHeight, w, nameHeight);
        currentHeight += nameHeight;

        if (attributesFigCompartment.isVisible()){
            int attributesHeight = attributesFigCompartment.getMinimumSize().height;
            if (isOperationsVisible()) {
                attributesHeight += whitespace / 2;
            }
            attributesFigCompartment.setBounds(
                    x,
                    y + currentHeight,
                    w,
                    attributesHeight);
            currentHeight += attributesHeight;
        }

        if (getOperationsFig().isVisible()) {
            int operationsY = y + currentHeight;
            int operationsHeight = (h + y) - operationsY - 1;
            if (operationsHeight < getOperationsFig().getMinimumSize().height) {
                operationsHeight = getOperationsFig().getMinimumSize().height;
            }
            getOperationsFig().setBounds(
                    x,
                    operationsY,
                    w,
                    operationsHeight);
        }

        // Now force calculation of the bounds of the figure, update the edges
        // and trigger anyone who's listening to see if the "bounds" property
        // has changed.

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }


}
