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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.ModelFacade;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;

import ru.novosoft.uml.MElementEvent;

/**
 * @author ics 125b course, spring 1998
 */
public class FigDependency extends FigEdgeModelElement {

    private ArrowHeadGreater endArrow;
    
    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor
     */
    public FigDependency() {
        addPathItem(getStereotypeFig(), new PathConvPercent(this, 50, 10));
        endArrow = new ArrowHeadGreater();
        endArrow.setFillColor(Color.red);
        setDestArrowHead(endArrow);
        setBetweenNearestPoints(true);
        setLayer(ProjectManager.getManager()
		 .getCurrentProject().getActiveDiagram().getLayer());
        getFig().setDashed(true);
    }

    /**
     * Constructor that sets the UML element
     * @param edge the UML element
     */
    public FigDependency(Object edge) {
        this();
        setOwner(edge);
    }

    /**
     * @param edge theUML element
     * @param lay the layer
     */
    public FigDependency(Object edge, Layer lay) {
        this();
        setOwner(edge);
        setLayer(lay);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object own) {
        Object oldOwner = getOwner();
        super.setOwner(own);

        if (org.argouml.model.ModelFacade.isADependency(own)) {
            Object newDep = own; //MDependency
            UmlModelEventPump pump = UmlModelEventPump.getPump();
            Iterator it = ModelFacade.getSuppliers(newDep).iterator();
            while (it.hasNext()) {
                Object o = it.next();
                pump.removeModelEventListener(this, o);
                pump.addModelEventListener(this, o);
            }
            it = ModelFacade.getClients(newDep).iterator();
            while (it.hasNext()) {
                Object o = it.next();
                pump.removeModelEventListener(this, o);
                pump.addModelEventListener(this, o);
            }
            pump.removeModelEventListener(this, newDep);
            pump.addModelEventListener(this, newDep);
            Object supplier =	// MModelElement
                (ModelFacade.getSuppliers(newDep).toArray())[0];
            Object client =	// MModelElement
                (ModelFacade.getClients(newDep).toArray())[0];

            FigNode supFN = (FigNode) getLayer().presentationFor(supplier);
            FigNode cliFN = (FigNode) getLayer().presentationFor(client);

            if (cliFN != null) {
                setSourcePortFig(cliFN);
                setSourceFigNode(cliFN);
            }
            if (supFN != null) {
                setDestPortFig(supFN);
                setDestFigNode(supFN);
            }
        }
    }
    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    public void setFig(Fig f) {
        super.setFig(f);
        _fig.setDashed(true);
        // computeRoute(); 
        // this recomputes the route if you reload the diagram.
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#canEdit(org.tigris.gef.presentation.Fig)
     */
    protected boolean canEdit(Fig f) {
        return false;
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /** 
     * This is called aftern any part of the UML MModelElement has
     * changed. This method automatically updates the name FigText.
     * Subclasses should override and update other parts.
     * 
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(ru.novosoft.uml.MElementEvent)
     */
    protected void modelChanged(MElementEvent e) {
        // do not set _name
        updateStereotypeText();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        endArrow.setLineColor(getLineColor());
        super.paint(g);
    }

} /* end class FigDependency */
