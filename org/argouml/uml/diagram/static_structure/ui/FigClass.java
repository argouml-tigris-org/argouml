// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.ActionEdgesDisplay;
import org.argouml.uml.diagram.ui.AttributesCompartmentContainer;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.FigAttributesCompartment;
import org.argouml.uml.diagram.ui.FigStereotypesCompartment;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Class in a diagram.<p>
 */
public class FigClass extends FigClassifierBox
        implements AttributesCompartmentContainer {

    /**
     * Logger.
     */
    //private static final Logger LOG = Logger.getLogger(FigClass.class);

    private FigAttributesCompartment attributesFigCompartment;

    /**
     * Manages residency of a class within a component on a deployment
     * diagram.
     * TODO: This belongs in ModelElement, not Class - tfm
     */
    private Object resident =
            Model.getCoreFactory().createElementResidence();

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor for a {@link FigClass} during file load.<p>
     *
     * Parent {@link org.argouml.uml.diagram.ui.FigNodeModelElement}
     * will have created the main box {@link #getBigPort()} and its
     * name {@link #getNameFig()} and stereotype
     * (@link #getStereotypeFig()}. This constructor
     * creates a box for the attributes and operations.<p>
     *
     * The properties of all these graphic elements are adjusted
     * appropriately. The main boxes are all filled and have
     * outlines.<p>
     *
     * <em>Warning</em>. Much of the graphics positioning is hard
     * coded. The overall figure is placed at location (10,10). The
     * name compartment (in the parent
     * {@link org.argouml.uml.diagram.ui.FigNodeModelElement} is
     * 21 pixels high. The stereotype compartment is created 15 pixels
     * high in the parent, but we change it to 19 pixels, 1 more than
     * ({@link #STEREOHEIGHT} here. The attribute and operations boxes
     * are created at 19 pixels, 2 more than {@link #ROWHEIGHT}.<p>
     */
    public FigClass(Object modelElement, int x, int y, int w, int h) {
        this(null, modelElement);
        setBounds(x, y, w, h);
    }

    /**
     * Constructor for a {@link FigClass} by diagram interaction.<p>
     *
     * Parent {@link org.argouml.uml.diagram.ui.FigNodeModelElement}
     * will have created the main box {@link #getBigPort()} and its
     * name {@link #getNameFig()} and stereotype
     * (@link #getStereotypeFig()}. This constructor
     * creates a box for the attributes and operations.<p>
     *
     * The properties of all these graphic elements are adjusted
     * appropriately. The main boxes are all filled and have
     * outlines.<p>
     *
     * <em>Warning</em>. Much of the graphics positioning is hard
     * coded. The overall figure is placed at location (10,10). The
     * name compartment (in the parent
     * {@link org.argouml.uml.diagram.ui.FigNodeModelElement} is
     * 21 pixels high. The stereotype compartment is created 15 pixels
     * high in the parent, but we change it to 19 pixels, 1 more than
     * ({@link #STEREOHEIGHT} here. The attribute and operations boxes
     * are created at 19 pixels, 2 more than {@link #ROWHEIGHT}.<p>
     *
     * @param gm   Not actually used in the current implementation
     *
     * @param node The UML object being placed.
     */
    public FigClass(GraphModel gm, Object node) {
        super();
        attributesFigCompartment =
            new FigAttributesCompartment(10, 30, 60, ROWHEIGHT + 2);
        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(operationsFig);
        addFig(attributesFigCompartment);
        addFig(borderFig);
        setOwner(node);
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigClass figClone = (FigClass) super.clone();
        Iterator thisIter = this.getFigs().iterator();
        Iterator cloneIter = figClone.getFigs().iterator();
        while (thisIter.hasNext()) {
            Fig thisFig = (Fig) thisIter.next();
            Fig cloneFig = (Fig) cloneIter.next();
            if (thisFig == borderFig) {
                figClone.borderFig = (FigRect) thisFig;
            }
        }
        return figClone;
    }


    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionClass(this);
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on a Class.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     *
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        // Add...
        ArgoJMenu addMenu = new ArgoJMenu("menu.popup.add");
        addMenu.add(TargetManager.getInstance().getAddAttributeAction());
        addMenu.add(TargetManager.getInstance().getAddOperationAction());
        addMenu.add(new ActionAddNote());
        addMenu.add(ActionEdgesDisplay.getShowEdges());
        addMenu.add(ActionEdgesDisplay.getHideEdges());
        popUpActions.insertElementAt(addMenu,
            popUpActions.size() - getPopupAddOffset());

        // Show ...
        ArgoJMenu showMenu = new ArgoJMenu("menu.popup.show");
        Iterator i = ActionCompartmentDisplay.getActions().iterator();
        while (i.hasNext()) {
            showMenu.add((Action) i.next());
        }
        popUpActions.insertElementAt(showMenu,
            popUpActions.size() - getPopupAddOffset());

        // Modifiers ...
        popUpActions.insertElementAt(
                buildModifierPopUp(ABSTRACT | LEAF | ROOT | ACTIVE),
                popUpActions.size() - getPopupAddOffset());

        // Visibility ...
        popUpActions.insertElementAt(buildVisibilityPopUp(),
                popUpActions.size() - getPopupAddOffset());

        return popUpActions;
    }

    /**
     * @return The bounds of the attributes compartment.
     */
    public Rectangle getAttributesBounds() {
        return attributesFigCompartment.getBounds();
    }

    /**
     * @return The vector of graphics for operations (if any).
     * First one is the rectangle for the entire operations box.
     */
    private FigAttributesCompartment getAttributesFig() {
        return attributesFigCompartment;
    }

    /**
     * Returns the status of the attribute field.
     * @return true if the attributes are visible, false otherwise
     *
     * @see org.argouml.uml.diagram.ui.AttributesCompartmentContainer#isAttributesVisible()
     */
    public boolean isAttributesVisible() {
        return getAttributesFig().isVisible();
    }

    /**
     * @param isVisible true if the attribute compartment is visible
     *
     * @see org.argouml.uml.diagram.ui.AttributesCompartmentContainer#setAttributesVisible(boolean)
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
            if (isVisible) { // show compartement
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

    /**
     * @param isVisible true if the operation compartment is visible
     *
     * @see org.argouml.uml.diagram.ui.OperationsCompartmentContainer#setOperationsVisible(boolean)
     */
    public void setOperationsVisible(boolean isVisible) {
        Rectangle rect = getBounds();
        if (isOperationsVisible()) { // if displayed
            if (!isVisible) {
                damage();
                Iterator it = getOperationsFig().getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(false);
                }
                getOperationsFig().setVisible(false);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
			  (int) aSize.getWidth(), (int) aSize.getHeight());
            }
        } else {
            if (isVisible) {
                Iterator it = getOperationsFig().getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(true);
                }
                getOperationsFig().setVisible(true);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
                    (int) aSize.getWidth(), (int) aSize.getHeight());
                damage();
            }
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        borderFig.setLineWidth(w);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return borderFig.getLineWidth();
    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    public String classNameAndBounds() {
        return super.classNameAndBounds()
            + "operationsVisible=" + isOperationsVisible() + ";"
            + "attributesVisible=" + isAttributesVisible();
    }

    /**
     * Gets the minimum size permitted for a class on the diagram.<p>
     *
     * Parts of this are hardcoded, notably the fact that the name
     * compartment has a minimum height of 21 pixels.<p>
     *
     * @return  the size of the minimum bounding box.
     */
    public Dimension getMinimumSize() {
        // Use "aSize" to build up the minimum size. Start with the size of the
        // name compartment and build up.

        Dimension aSize = getNameFig().getMinimumSize();

        // If we have a stereotype displayed, then allow some space for that
        // (width and height)

        if (getStereotypeFig().isVisible()) {
            Dimension stereoMin = getStereotypeFig().getMinimumSize();
            aSize.width = Math.max(aSize.width, stereoMin.width);
            aSize.height += stereoMin.height;
        }

        // Allow space for each of the attributes we have

        if (getAttributesFig().isVisible()) {
            Dimension attrMin = getAttributesFig().getMinimumSize();
            aSize.width = Math.max(aSize.width, attrMin.width);
            aSize.height += attrMin.height;
        }

        // Allow space for each of the operations we have

        if (isOperationsVisible()) {
            Dimension operMin = getOperationsFig().getMinimumSize();
            aSize.width = Math.max(aSize.width, operMin.width);
            aSize.height += operMin.height;
        }

        // we want to maintain a minimum width for the class
        aSize.width = Math.max(60, aSize.width);

        // And now aSize has the answer

        return aSize;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#translate(int, int)
     */
    public void translate(int dx, int dy) {
        super.translate(dx, dy);
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionClass) {
            ((SelectionClass) sel).hideButtons();
        }
    }

    ////////////////////////////////////////////////////////////////
    // internal methods

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        Object classifier = getOwner();
        if (classifier == null) {
            return;
        }
        int i = new Vector(getAttributesFig().getFigs()).indexOf(ft);
        if (i != -1) {
            highlightedFigText = (CompartmentFigText) ft;
            highlightedFigText.setHighlighted(true);
            
            ft.setText(highlightedFigText.getNotationProvider()
                    .parse(ft.getText()));
            return;
        }
        i = new Vector(getOperationsFig().getFigs()).indexOf(ft);
        if (i != -1) {
            highlightedFigText = (CompartmentFigText) ft;
            highlightedFigText.setHighlighted(true);
            
            ft.setText(highlightedFigText.getNotationProvider()
                    .parse(ft.getText()));
            return;
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        super.textEditStarted(ft);
        if (getAttributesFig().getFigs().contains(ft)) {
            showHelp(((CompartmentFigText) ft)
                    .getNotationProvider().getParsingHelp());
        }
        if (getOperationsFig().getFigs().contains(ft)) {
            showHelp(((CompartmentFigText) ft)
                    .getNotationProvider().getParsingHelp());
        }
    }

    /**
     * @param fgVec the FigGroup
     * @param ft    the Figtext
     * @param i     get the fig before fig i
     * @return the FigText
     */
    protected FigText getPreviousVisibleFeature(FigGroup fgVec,
						FigText ft, int i) {
        if (fgVec == null || i < 1) {
            return null;
        }
        FigText ft2 = null;
        // TODO: come GEF V 0.12 use getFigs returning an array
        Vector v = new Vector(fgVec.getFigs());
        if (i >= v.size() || !((FigText) v.elementAt(i)).isVisible()) {
            return null;
        }
        do {
            i--;
            while (i < 1) {
                if (fgVec == getAttributesFig()) {
                    fgVec = getOperationsFig();
                } else {
                    fgVec = getAttributesFig();
                }
                v = new Vector(fgVec.getFigs());
                i = v.size() - 1;
            }
            ft2 = (FigText) v.elementAt(i);
            if (!ft2.isVisible()) {
                ft2 = null;
            }
        } while (ft2 == null);
        return ft2;
    }

    /**
     * @param fgVec the FigGroup
     * @param ft    the Figtext
     * @param i     get the fig after fig i
     * @return the FigText
     */
    protected FigText getNextVisibleFeature(FigGroup fgVec, FigText ft, int i) {
        if (fgVec == null || i < 1) {
            return null;
        }
        FigText ft2 = null;
        // TODO: come GEF V 0.12 use getFigs returning an array
        Vector v = new Vector(fgVec.getFigs());
        if (i >= v.size() || !((FigText) v.elementAt(i)).isVisible()) {
            return null;
        }
        do {
            i++;
            while (i >= v.size()) {
                if (fgVec == getAttributesFig()) {
                    fgVec = getOperationsFig();
                } else {
                    fgVec = getAttributesFig();
                }
                v = new Vector(fgVec.getFigs());
                i = 1;
            }
            ft2 = (FigText) v.elementAt(i);
            if (!ft2.isVisible()) {
                ft2 = null;
            }
        } while (ft2 == null);
        return ft2;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        if (getOwner() != null) {
            updateAttributes();
            updateOperations();
            updateAbstract();
        }
        super.renderingChanged();
    }
    
    /**
     * Handles changes to the model. Takes into account the event that
     * occurred. If you need to update the whole fig, consider using
     * renderingChanged.
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        // Let our superclass sort itself out first
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     * TODO: Refactor into FigClassifierBox
     */
    protected void updateStereotypeText() {

        Rectangle rect = getBounds();

        int stereotypeHeight = 0;
        if (getStereotypeFig().isVisible()) {
            stereotypeHeight = getStereotypeFig().getHeight();
        }
        int heightWithoutStereo = getHeight() - stereotypeHeight;

        getStereotypeFig().setOwner(getOwner());
        ((FigStereotypesCompartment) getStereotypeFig()).populate();

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
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig encloser) {
        if (encloser == getEncloser()) {
            return;
        }
        if (encloser == null
                || (encloser != null
                && !Model.getFacade().isAInstance(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }
        if (!(Model.getFacade().isAModelElement(getOwner()))) {
            return;
        }
        if (encloser != null
                && (Model.getFacade().isAComponent(encloser.getOwner()))) {
            Object component = encloser.getOwner();
            Object anInterface = getOwner();
            Model.getCoreHelper().setContainer(resident, component);
            Model.getCoreHelper().setResident(resident, anInterface);
        } else {
            Model.getCoreHelper().setContainer(resident, null);
            Model.getCoreHelper().setResident(resident, null);
        }

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
    protected void setBoundsImpl(final int x, final int y,
            final int w, final int h) {
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
            currentHeight = stereotypeHeight;
        }

        int nameHeight = getNameFig().getMinimumSize().height;
        getNameFig().setBounds(x, y + currentHeight, w, nameHeight);
        currentHeight += nameHeight;

        if (isAttributesVisible()) {
            int attributesHeight = getAttributesFig().getMinimumSize().height;
            if (isOperationsVisible()) {
                attributesHeight += whitespace / 2;
            }
            getAttributesFig().setBounds(
                    x,
                    y + currentHeight,
                    w,
                    attributesHeight);
            currentHeight += attributesHeight;
        }

        if (isOperationsVisible()) {
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

    /**
     * Updates the attributes in the fig. Called from modelchanged if there is
     * a modelevent effecting the attributes and from renderingChanged in all
     * cases.
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
     * Updates the operations box. Called from modelchanged if there is
     * a modelevent effecting the attributes and from renderingChanged in all
     * cases.
     */
    protected void updateOperations() {
        if (!isOperationsVisible()) {
            return;
        }
        operationsFig.populate();

        Rectangle rect = getBounds();
        // ouch ugly but that's for a next refactoring
        // TODO: make setBounds, calcBounds and updateBounds consistent
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }



    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        super.updateNameText();
        calcBounds();
        setBounds(getBounds());
    }

    /**
     * Updates the name if modelchanged receives an "isAbstract" event.
     * TODO: Move up to ClassifierBox (should be GeneralizableElement)
     */
    protected void updateAbstract() {
        Rectangle rect = getBounds();
        if (getOwner() == null) {
            return;
        }
        Object cls = /*(MClass)*/ getOwner();
        if (Model.getFacade().isAbstract(cls)) {
            getNameFig().setFont(getItalicLabelFont());
	} else {
            getNameFig().setFont(getLabelFont());
	}
        super.updateNameText();
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        if (newOwner != null) {
            // add the listeners to the newOwner
            addElementListener(newOwner);
            // and its stereotypes
            Collection c = new ArrayList(
                    Model.getFacade().getStereotypes(newOwner));
            // and its features
            Iterator it = Model.getFacade().getFeatures(newOwner).iterator();
            while (it.hasNext()) {
                Object feat = it.next();
                c.add(feat);
                // and the stereotypes of its features
                c.addAll(new ArrayList(Model.getFacade().getStereotypes(feat)));
                // and the parameter of its operations
                if (Model.getFacade().isAOperation(feat)) {
                    c.addAll(Model.getFacade().getParameters(feat));
                }
            }
            Iterator it2 = c.iterator();
            while (it2.hasNext()) {
                addElementListener(it2.next());
            }
            if (isPathVisible()) {
                c = Model.getModelManagementHelper()
                    .getAllSurroundingNamespaces(newOwner);
                Iterator itpv = c.iterator();
                while (itpv.hasNext()) {
                    addElementListener(itpv.next(), 
                            new String[] {"name", "namespace", "ownedElement"});
                }
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3415118710864626882L;

} /* end class FigClass */
