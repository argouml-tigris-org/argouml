// $Id: FigDataType.java 17045 2009-04-05 16:52:52Z mvw $
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

package org.argouml.class2.diagram;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.FigClassifierBox;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;

/**
 * Class to display graphics for a UML DataType in a diagram.
 * (cloned from FigInterface - perhaps they should both specialize
 * a common supertype).
 * <p>
 * A DataType may show compartments for stereotypes
 * and operations. Attributes are not supported in ArgoUML. <p>
 * 
 * Every DataType shows a keyword, but it is not 
 * always <<datatype>>, e.g. for an Enumeration.
 */
public class FigDataType extends FigClassifierBox {

    private static final Logger LOG = Logger.getLogger(FigDataType.class);
    
    private static final int MIN_WIDTH = 40;

    private void constructFigs() {
        getStereotypeFig().setKeyword(getKeyword());

        setSuppressCalcBounds(true);
        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(getOperationsFig());
        addFig(borderFig);

        setSuppressCalcBounds(false);

        // Set the bounds of the figure to the total of the above 
        enableSizeChecking(true);
        super.setStandardBounds(X0, Y0, WIDTH, NAME_FIG_HEIGHT + ROWHEIGHT);
    }

    /**
     * Primary constructor for a {@link FigDataType}.
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
     * <em>Warning</em>. Much of the graphics positioning is hard coded. The
     * overall figure is placed at location (10,10).
     * The stereotype compartment is created 15 pixels high
     * in the parent, but we change it to 19 pixels, 1 more than
     * ({@link #STEREOHEIGHT} here. The operations box is created at 19 pixels,
     * 2 more than {@link #ROWHEIGHT}.
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     */
    public FigDataType(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
        constructFigs();
    }

    /**
     * This function shall return the keyword to be used by the constructor. <p>
     * 
     * Subclasses of DataType shall 
     * override this method to set their own keyword.
     * 
     * @return the string to be used as the keyword
     */
    protected String getKeyword() {
        return "datatype";
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    @Override
    public Selection makeSelection() {
        return new SelectionDataType(this);
    }

    /**
     * Gets the minimum size permitted for a datatype on the diagram.<p>
     *
     * Parts of this are hardcoded with magic numbers.<p>
     *
     * @return  the size of the minimum bounding box.
     */
    @Override
    public Dimension getMinimumSize() {
        // Use "aSize" to build up the minimum size. Start with the size of the
        // name compartment and build up.

        Dimension aSize = getNameFig().getMinimumSize();

        aSize.height += NAME_V_PADDING * 2;
        aSize.height = Math.max(NAME_FIG_HEIGHT, aSize.height);

        // If we have a stereotype displayed, then allow some space for that
        // (width and height)
        aSize = addChildDimensions(aSize, getStereotypeFig());
        aSize = addChildDimensions(aSize, getOperationsFig());

        // we want to maintain a minimum width for datatypes
        aSize.width = Math.max(MIN_WIDTH, aSize.width);

        return aSize;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    @Override
    public int getLineWidth() {
        return borderFig.getLineWidth();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void setEnclosingFig(Fig encloser) {
        Fig oldEncloser = getEnclosingFig();

        if (encloser == null
                || (encloser != null
                && !Model.getFacade().isAInstance(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }
        if (!(Model.getFacade().isAUMLElement(getOwner()))) {
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

    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    @Override
    public String classNameAndBounds() {
        return super.classNameAndBounds()
                + "operationsVisible=" + isOperationsVisible();
    }


    /**
     * Sets the bounds, but the size will be at least the one returned by
     * {@link #getMinimumSize()}, unless checking of size is disabled.<p>
     *
     * If the required height is bigger, then the additional height is
     * equally distributed among all figs (i.e. compartments), such that the
     * accumulated height of all visible figs equals the demanded height<p>.
     *
     * @param x  Desired X coordinate of upper left corner
     *
     * @param y  Desired Y coordinate of upper left corner
     *
     * @param w  Desired width of the figure
     *
     * @param h  Desired height of the figure
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    @Override
    protected void setStandardBounds(final int x, final int y, final int w,
            final int h) {

        // Save our old boundaries to use in our property message later
        Rectangle oldBounds = getBounds();
        // and get minimum size info.

        // set bounds of big box
        getBigPort().setBounds(x, y, w, h);
        borderFig.setBounds(x, y, w, h);

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
            int operationsHeight = (h + y) - operationsY - LINE_WIDTH;
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
