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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigOperationsCompartment;
import org.argouml.uml.diagram.ui.OperationsCompartmentContainer;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;

/**
 * Class to display graphics for a UML Class in a diagram.<p>
 */
abstract public class FigClassifierBox extends FigNodeModelElement
        implements OperationsCompartmentContainer {

    /**
     * The Fig for the operations compartment (if any). 
     */
    protected FigOperationsCompartment operationsFig;
    
    /**
     * Text highlighted by mouse actions on the diagram.<p>
     */
    protected CompartmentFigText highlightedFigText = null;

    FigClassifierBox() {

        // Set name box. Note the upper line will be blanked out if there is
        // eventually a stereotype above.
        getNameFig().setLineWidth(1);
        getNameFig().setFilled(true);

        // this rectangle marks the operation section; all operations
        // are inside it
        operationsFig =
            new FigOperationsCompartment(
                    10, 31 + ROWHEIGHT, 60, ROWHEIGHT + 2);
        
        // Set properties of the stereotype box. Make it 1 pixel higher than
        // before, so it overlaps the name box, and the blanking takes out both
        // lines. Initially not set to be displayed, but this will be changed
        // when we try to render it, if we find we have a stereotype.
        getStereotypeFigText().setExpandOnly(true);
        getStereotypeFig().setFilled(true);
        getStereotypeFig().setLineWidth(1);
        getStereotypeFigText().setEditable(false);
        // +1 to have 1 pixel overlap with getNameFig()
        getStereotypeFig().setHeight(STEREOHEIGHT + 1);
        
    }
    
    void addFigsNoCalcBounds() {
        setSuppressCalcBounds(true);
        addFigs();
        setSuppressCalcBounds(false);
    }
    
    void addFigs() {
        addFig(getStereotypeFig());  //0
        addFig(getNameFig());        //1
        addFig(getBigPort());        //2
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
        FigOperationsCompartment operationsCompartment =
            (FigOperationsCompartment)getOperationsFig();
        operationsCompartment.populate();
        Fig operPort = operationsCompartment.getBigPort();

        int xpos = operPort.getX();
        int ypos = operPort.getY();

        Rectangle rect = getBounds();
        updateFigGroupSize(getOperationsFig(), xpos, ypos, 0, 0);
        // ouch ugly but that's for a next refactoring
        // TODO: make setBounds, calcBounds and updateBounds consistent
        setBounds(rect.x, rect.y, rect.width, rect.height);
        damage();
    }
    
    /**
     * @return The vector of graphics for operations (if any).
     * First one is the rectangle for the entire operations box.
     */
    protected FigOperationsCompartment getOperationsFig() {
        return operationsFig;
    }

    /**
     * Get the bounds of the operations compartment
     *
     * @return the bounds of the operations compartment
     */
    public Rectangle getOperationsBounds() {
        return operationsFig.getBounds();
    }
    
    /**
     * Returns the visibility status of the operations compartment
     * @return true if the operations are visible, false otherwise
     *
     * @see org.argouml.uml.diagram.ui.OperationsCompartmentContainer#isOperationsVisible()
     */
    public boolean isOperationsVisible() {
        return operationsFig.isVisible();
    }
    
    /**
     * @param isVisible true will show the operations compartiment
     */
    public void setOperationsVisible(boolean isVisible) {
        Rectangle rect = getBounds();
        int h = isCheckSize()
                ? ((ROWHEIGHT * Math.max(1, operationsFig.getFigs().size() - 1) + 2)
                * rect.height
                / getMinimumSize().height)
                : 0;
        if (operationsFig.isVisible()) {
            if (!isVisible) {
                damage();
                Iterator it = operationsFig.getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(false);
                }
                operationsFig.setVisible(false);
                setBounds(rect.x, rect.y, rect.width, rect.height - h);
            }
        } else {
            if (isVisible) {
                Iterator it = operationsFig.getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(true);
                }
                operationsFig.setVisible(true);
                setBounds(rect.x, rect.y, rect.width, rect.height + h);
                damage();
            }
        }
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

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me) {
        super.mouseExited(me);
        unhighlight();
    }

    /**
     * Remove the hidhlight from the currently highlit FigText
     * @return the FigText that had highlight removed
     */
    protected CompartmentFigText unhighlight() {
        CompartmentFigText ft;
        List figs = operationsFig.getFigs();
        for (int i = 1; i < figs.size(); i++) {
            ft = (CompartmentFigText) figs.get(i);
            if (ft.isHighlighted()) {
                ft.setHighlighted(false);
                highlightedFigText = null;
                return ft;
            }
        }
        return null;
    }

    /**
     * Create a new "feature" in the owner fig.
     *
     * must be overridden to make sense
     * (I didn't want to make it abstract because it might not be required)
     *
     * @param fg The fig group to which this applies
     * @param me The input event that triggered us. In the current
     *            implementation a mouse double click.
     */
    abstract protected void createFeatureIn(FigGroup fg, InputEvent me);


} /* end class FigClass */
