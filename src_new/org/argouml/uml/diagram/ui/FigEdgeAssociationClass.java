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

package org.argouml.uml.diagram.ui;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;

import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;


/**
 * Class to display a connection linking the class and the
 * association in a Association Class
 * It must be used only from a FigAssociationClass
 *
 * @author pepargouml
 */
public class FigEdgeAssociationClass
        extends FigEdgeModelElement
        implements VetoableChangeListener,
        DelayedVChangeListener,
        MouseListener,
        KeyListener,
        PropertyChangeListener {

    /**
     * The FigAssociationClass that is associated to.
     */
    private FigAssociationClass mainFig;
    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public FigEdgeAssociationClass() {
        setBetweenNearestPoints(true);
        ((FigPoly) getFig()).setRectilinear(false);
        setDashed(true);
    }

    /**
     * The constructor for the AssociationClass fig.
     *
     * @param fromFig the fig where we started
     * @param toFig the fig where we ended
     * @param ownerFig the owner fig
     */
    public FigEdgeAssociationClass(Fig fromFig, Fig toFig,
                                   FigAssociationClass ownerFig) {
        this();
        if (toFig == null || fromFig == null) {
            throw new IllegalStateException("No destfig or sourcefig while "
                    + "creating FigEdgeAssociationClass");
        }
        mainFig = ownerFig;
        setDestFigNode((FigNode) toFig);
        setDestPortFig(toFig);
        setSourcePortFig(fromFig);
        setSourceFigNode((FigNode) fromFig);
        computeRoute();
    }
    
    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    public void setFig(Fig f) {
        super.setFig(f);
        getFig().setDashed(true);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#canEdit(org.tigris.gef.presentation.Fig)
     */
    protected boolean canEdit(Fig f) {
        return false;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent e) {
    }

    /**
     * @return the main fig
     */
    public FigAssociationClass getMainFig() {
        return mainFig;
    }

    /**
     * @param f the main fig
     */
    public void setMainFig(FigAssociationClass f) {
        mainFig = f;
    }

    /**
     * It not only damages itself but also its
     * associated FigAssociationClass
     */
    public void damage() {
        if (mainFig != null) {
            mainFig.figDamaged();
        }
        super.damage();
    }

    /**
     * It not only removes itself but also its
     * associated FigAssociationClass
     */
    public void removeFromDiagram() {
        super.removeFromDiagram();
        if (mainFig != null)
            mainFig.removeFromDiagram();
    }

    /**
     * It is used to remove itself without removing its
     * associated FigAssociationClass.
     */
    public void removeThisFromDiagram() {
        super.removeFromDiagram();
        TargetManager.getInstance().removeHistoryElement(this);
    }

} /* end class FigEdgeAssociationClass */
