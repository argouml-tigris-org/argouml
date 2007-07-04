// $Id: FigInterface.java 12926 2007-06-29 20:13:47Z mvw $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.FigStereotypesCompartment;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Interface in a diagram.
 */
public class FigInterface extends FigClassifierBox {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(FigInterface.class);

    /**
     * Main constructor for a {@link FigInterface}.
     *
     * Parent {@link org.argouml.uml.diagram.ui.FigNodeModelElement}
     * will have created the main box {@link #getBigPort()} and
     * its name {@link #getNameFig()} and stereotype
     * (@link #getStereotypeFig()}. This constructor
     * creates a box for the operations.<p>
     *
     * The properties of all these graphic elements are adjusted
     * appropriately. The main boxes are all filled and have outlines.<p>
     *
     * For reasons I don't understand the stereotype is created in a box
     * with lines. So we have to created a blanking rectangle to overlay the
     * bottom line, and avoid three compartments showing.<p>
     *
     * <em>Warning</em>. Much of the graphics positioning is hard coded. The
     * overall figure is placed at location (10,10). The name compartment (in
     * the parent {@link org.argouml.uml.diagram.ui.FigNodeModelElement}
     * is 21 pixels high. The stereotype compartment is created 15 pixels high
     * in the parent, but we change it to 19 pixels, 1 more than
     * ({@link #STEREOHEIGHT} here. The operations box is created at 19 pixels,
     * 2 more than {@link #ROWHEIGHT}.<p>
     *
     * CAUTION: This constructor (with no arguments) is the only one
     * that does enableSizeChecking(false), all others must set it true.
     * This is because this constructor is the only one called when loading
     * a project. In this case, the parsed size must be maintained.<p>
     */
    public FigInterface() {

        FigStereotypesCompartment fsc =
            (FigStereotypesCompartment) getStereotypeFig();
        fsc.setKeyword("interface");

        // Put all the bits together, suppressing bounds calculations until
        // we're all done for efficiency.
        enableSizeChecking(false);
        setSuppressCalcBounds(true);
        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(getOperationsFig());
        addFig(borderFig);

        setSuppressCalcBounds(false);

        // Set the bounds of the figure to the total of the above (hardcoded)
        enableSizeChecking(true);
        setBounds(10, 10, 60, 21 + ROWHEIGHT);
    }

    /**
     * Constructor for use if this figure is created for an
     * existing interface node in the metamodel.
     *
     * @param gm   Not actually used in the current implementation
     *
     * @param node The UML object being placed.
     */
    public FigInterface(GraphModel gm, Object node) {
        this();
        setOwner(node);
        enableSizeChecking(true);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionInterface(this);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigCompartmentBox#setOperationsVisible(boolean)
     * 
     * TODO: This differs only very slightly from the version in the superclass
     *       It probably can be merged, but I don't have time to verify
     *       right now. - tfm - 20070109
     */
    public void setOperationsVisible(boolean isVisible) {
        Rectangle rect = getBounds();
        int h =
                isCheckSize() ? ((ROWHEIGHT
                * Math.max(1, getOperationsFig().getFigs().size() - 1) + 2)
                * rect.height / getMinimumSize().height) : 0;
        if (isOperationsVisible()) {
            if (!isVisible) {
                damage();
                Iterator it = getOperationsFig().getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(false);
                }
                getOperationsFig().setVisible(false);
                setBounds(rect.x, rect.y, rect.width, rect.height - h);
            }
        } else {
            if (isVisible) {
                Iterator it = getOperationsFig().getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(true);
                }
                getOperationsFig().setVisible(true);
                setBounds(rect.x, rect.y, rect.width, rect.height + h);
                damage();
            }
        }
    }

    /**
     * Gets the minimum size permitted for an interface on the diagram.<p>
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

        // +2 padding before and after name

        aSize.height += 4;

        if (aSize.height < 21) {
            aSize.height = 21;
        }

        // If we have a stereotype displayed, then allow some space for that
        // (width and height)

        if (getStereotypeFig().isVisible()) {
            Dimension stereoMin = getStereotypeFig().getMinimumSize();
            aSize.width = Math.max(aSize.width, stereoMin.width);
            aSize.height += stereoMin.height;
        }

        if (isOperationsVisible()) {
            Dimension operMin = getOperationsFig().getMinimumSize();
            aSize.width = Math.max(aSize.width, operMin.width);
            aSize.height += operMin.height;
        }

        // we want to maintain a minimum width for Interfaces
        aSize.width = Math.max(60, aSize.width);

        return aSize;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        borderFig.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return borderFig.getLineWidth();
    }

    /*
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

    /*
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig encloser) {
        Fig oldEncloser = getEnclosingFig();

        if (encloser == null
                || (encloser != null
                && !Model.getFacade().isAInstance(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }
        if (!(Model.getFacade().isAModelElement(getOwner()))) {
            return;
        }
        /* If this fig is not visible, do not adapt the UML model!
         * This is used for deleting. See issue 3042.
         */
        if  (!isVisible()) {
            return;
        }
        Object me = getOwner();
        Object m = null;

        try {
            // If moved into an Package
            if (encloser != null
                    && oldEncloser != encloser
                    && Model.getFacade().isAPackage(encloser.getOwner())) {
                Model.getCoreHelper().setNamespace(me, encloser.getOwner());
            }

            // If default Namespace is not already set
            if (Model.getFacade().getNamespace(me) == null
                    && (TargetManager.getInstance().getTarget()
                    instanceof ArgoDiagram)) {
                m =
                    ((ArgoDiagram) TargetManager.getInstance().getTarget())
                        .getNamespace();
                Model.getCoreHelper().setNamespace(me, m);
            }
        } catch (Exception e) {
            LOG.error("could not set package due to:" + e
                    + "' at " + encloser, e);
        }

        // The next if-clause is important for the Deployment-diagram
        // it detects if the enclosing fig is a component, in this case
        // the container will be set for the owning Interface
        if (encloser != null
                && (Model.getFacade().isAComponent(encloser.getOwner()))) {
            moveIntoComponent(encloser);
            super.setEnclosingFig(encloser);
        }
    }

    ////////////////////////////////////////////////////////////////
    // internal methods

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(
     * org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        Object cls = getOwner();
        if (cls == null) {
            return;
        }
        int i = getOperationsFig().getFigs().indexOf(ft);
        if (i != -1) {
            highlightedFigText = (CompartmentFigText) ft;
            highlightedFigText.setHighlighted(true);

            highlightedFigText.getNotationProvider()
                .parse(highlightedFigText.getOwner(), ft.getText());
            ft.setText(highlightedFigText.getNotationProvider().toString(
                highlightedFigText.getOwner(), null));
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(
     * org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        super.textEditStarted(ft);
        if (getOperationsFig().getFigs().contains(ft)) {
            showHelp(((CompartmentFigText) ft)
                    .getNotationProvider().getParsingHelp());
        }
    }

    /**
     * @param ft the figtext holding the feature
     * @param i the index (?)
     * @return the figtext
     */
    protected FigText getPreviousVisibleFeature(FigText ft, int i) {
        FigText ft2 = null;
        List figs = getOperationsFig().getFigs();
        if (i < 1 || i >= figs.size()
                || !((FigText) figs.get(i)).isVisible()) {
            return null;
        }

        do {
            i--;
            if (i < 1) {
                i = figs.size() - 1;
            }
            ft2 = (FigText) figs.get(i);
            if (!ft2.isVisible()) {
                ft2 = null;
            }
        } while (ft2 == null);

        return ft2;
    }

    /**
     * @param ft the figtext holding the feature
     * @param i the index (?)
     * @return the figtext
     */
    protected FigText getNextVisibleFeature(FigText ft, int i) {
        FigText ft2 = null;
        List operations = getOperationsFig().getFigs();
        if (i < 1 || i >= operations.size()
                || !((FigText) operations.get(i)).isVisible()) {
            return null;
        }

        do {
            i++;
            if (i >= operations.size()) {
                i = 1;
            }
            ft2 = (FigText) operations.get(i);
            if (!ft2.isVisible()) {
                ft2 = null;
            }
        } while (ft2 == null);

        return ft2;
    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    public String classNameAndBounds() {
        return super.classNameAndBounds()
                + "operationsVisible=" + isOperationsVisible();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(
     * java.beans.PropertyChangeEvent)
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

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object, java.lang.Object)
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

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        updateOperations();
        super.renderingChanged();
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
     * @param w  Desired width of the FigInterface
     *
     * @param h  Desired height of the FigInterface
     */
    protected void setStandardBounds(final int x, final int y, final int w,
            final int h) {

        Rectangle oldBounds = getBounds();
        // Save our old boundaries (needed later), and get minimum size
        // info. "aSize will be used to maintain a running calculation of our
        // size at various points.

        // "extra_each" is the extra height per displayed fig if requested
        // height is greater than minimal. "height_correction" is the height
        // correction due to rounded division result, will be added to the name
        // compartment

        // set bounds of big box
        getBigPort().setBounds(x, y, w, h);
        borderFig.setBounds(x, y, w, h);

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

        if (getOperationsFig().isVisible()) {
            int operationsY = y + currentHeight;
            int operationsHeight = (h + y) - operationsY - 1;
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
     * The UID.
     */
    private static final long serialVersionUID = -390783219580351197L;

} /* end class FigInterface */
