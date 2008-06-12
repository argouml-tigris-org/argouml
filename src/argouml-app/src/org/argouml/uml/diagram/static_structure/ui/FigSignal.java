// $Id$
// Copyright (c) 2007-2008 The Regents of the University of California. All
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

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.uml.diagram.ui.FigStereotypesCompartment;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;

/**
 * Class to display graphics for a UML Signal in a diagram.
 * <p>
 * A Signal may have attributes - the UML standard document 
 * contains an example diagram showing this.
 * <p>
 * A Signal may have operations.
 * 
 * @author Tom Morris
 */
public class FigSignal extends FigClassifierBoxWithAttributes {
    

    /**
     * Default constructor for a {@link FigSignal}.
     */
    public FigSignal() {
        super();
        FigStereotypesCompartment fsc =
            (FigStereotypesCompartment) getStereotypeFig();
        fsc.setKeyword("signal");

        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(getOperationsFig());
        addFig(getAttributesFig());
        addFig(borderFig);

        // by default, do not show operations nor attributes:
        setOperationsVisible(false);
        setAttributesVisible(false);
    }

    /**
     * Constructor for use if this figure is created for an
     * existing interface node in the metamodel.
     *
     * @param gm   Not actually used in the current implementation
     *
     * @param node The UML object being placed.
     */
    public FigSignal(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /*
     * @see org.argouml.uml.diagram.static_structure.ui.FigDataType#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionSignal(this);
    }

    /*
     * @see org.argouml.uml.diagram.static_structure.ui.FigClassifierBox#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        
        // TODO: Do we have anything to add here?

        return popUpActions;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent 
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
        }
    }

} 
