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

import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigOperationsCompartment;
import org.argouml.uml.diagram.ui.OperationsCompartmentContainer;
import org.tigris.gef.presentation.Fig;

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

} /* end class FigClass */
