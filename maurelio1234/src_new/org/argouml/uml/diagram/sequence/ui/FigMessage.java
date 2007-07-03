// $Id: FigMessage.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.uml.diagram.sequence.ui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.uml.diagram.sequence.MessageNode;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigTextGroup;
import org.argouml.uml.ui.ActionRESequenceDiagram;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.Handle;

/**
 * The fig for a link in a sequence diagram.
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class FigMessage
    extends FigEdgeModelElement {

    private FigTextGroup textGroup;

    /**
     * Contructs a new figlink and sets the owner of the figlink.
     *
     * @param owner is the owner.
     */
    public FigMessage(Object owner) {
        super();
        textGroup = new FigTextGroup();
        textGroup.addFig(getNameFig());
        textGroup.addFig(getStereotypeFig());
        addPathItem(textGroup, new PathConvPercent(this, 50, 10));
        setOwner(owner);
    }

    /**
     * Constructor here for saving and loading purposes.
     *
     */
    public FigMessage() {
        this(null);
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on a message.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     *
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        // the menu item for RE a sequence into the diagram is offered always,
        // because a check (e.g. does a operation with a body exist) would be
        // too expensive
        popUpActions.insertElementAt(new ActionRESequenceDiagram(this),
                popUpActions.size() - getPopupAddOffset());

        return popUpActions;
    }

    /**
     * Returns the action attached to this link if any.<p>
     *
     * @return the action attached to this link or null if there isn't any.
     */
    public Object getAction() {
        Object owner = getOwner();
        if (owner != null && Model.getFacade().isAMessage(owner)) {
            return Model.getFacade().getAction(owner);
        }
        return null;
    }

    /*
     * @see org.tigris.gef.presentation.FigEdge#getSourcePortFig()
     */
    public Fig getSourcePortFig() {
        Fig result = super.getSourcePortFig();
        if (result instanceof FigClassifierRole.TempFig
                && getOwner() != null) {
            result =
                getSourceFigClassifierRole().createFigMessagePort(
                    getOwner(), (FigClassifierRole.TempFig) result);
            setSourcePortFig(result);
        }
        return result;
    }

    /*
     * @see org.tigris.gef.presentation.FigEdge#getDestPortFig()
     */
    public Fig getDestPortFig() {
        Fig result = super.getDestPortFig();
        if (result instanceof FigClassifierRole.TempFig
                && getOwner() != null) {
            result =
                getDestFigClassifierRole().createFigMessagePort(
                    getOwner(), (FigClassifierRole.TempFig) result);
            setDestPortFig(result);
        }
        return result;
    }

    /**
     * Computes the route of this {@link FigMessage} and computes the
     * connectionpoints of the figlink to the ports.  This depends on
     * the action attached to the owner of the {@link FigMessage}.  Also
     * adds FigActivations etc or moves the {@link FigClassifierRole}s
     * if necessary.<p>
     *
     * @see org.tigris.gef.presentation.FigEdge#computeRoute()
     */
    public void computeRouteImpl() {
        Fig sourceFig = getSourcePortFig();
        Fig destFig = getDestPortFig();
        if (sourceFig instanceof FigMessagePort
                && destFig instanceof FigMessagePort) {
            FigMessagePort srcMP = (FigMessagePort) sourceFig;
            FigMessagePort destMP = (FigMessagePort) destFig;
            Point startPoint = sourceFig.connectionPoint(destMP.getCenter());
            Point endPoint = destFig.connectionPoint(srcMP.getCenter());
            // If it is a self-message
            if (isSelfMessage()) {
                if (startPoint.x < sourceFig.getCenter().x) {
                    startPoint.x += sourceFig.getWidth();
                }
                endPoint.x = startPoint.x;
                setEndPoints(startPoint, endPoint);
                // If this is the first time it is laid out, will only
                // have 2 points, add the middle point
                if (getNumPoints() <= 2) {
                    insertPoint(0, startPoint.x
                            + SequenceDiagramLayer.OBJECT_DISTANCE / 3,
                                (startPoint.y + endPoint.y) / 2);
                } else {
                    // Otherwise, move the middle point
                    int middleX =
                        startPoint.x
                        + SequenceDiagramLayer.OBJECT_DISTANCE / 3;
                    int middleY = (startPoint.y + endPoint.y) / 2;
                    Point p = getPoint(1);
                    if (p.x != middleX || p.y != middleY) {
                        setPoint(new Handle(1), middleX, middleY);
                    }
                }
            } else {
                setEndPoints(startPoint, endPoint);
            }
            calcBounds();
            layoutEdge();
        }
    }
    
    private boolean isSelfMessage() {
        FigMessagePort srcMP = (FigMessagePort) getSourcePortFig();
        FigMessagePort destMP = (FigMessagePort) getDestPortFig();
        return (srcMP.getNode().getFigClassifierRole()
                == destMP.getNode().getFigClassifierRole());
    }

    /**
     * @return the node for the source port fig
     */
    public MessageNode getSourceMessageNode() {
        return ((FigMessagePort) getSourcePortFig()).getNode();
    }

    /**
     * @return the node for the destination port fig
     */
    public MessageNode getDestMessageNode() {
        return ((FigMessagePort) getDestPortFig()).getNode();
    }

    /**
     * Returns the message belonging to this link if there is one
     * (otherwise null).<p>
     *
     * @return the message.
     */
    public Object getMessage() {
    	return getOwner();
    }

    /*
     * @see org.tigris.gef.presentation.FigEdgePoly#layoutEdge()
     */
    protected void layoutEdge() {
        if (getSourcePortFig() instanceof FigMessagePort
                && getDestPortFig() instanceof FigMessagePort
                && ((FigMessagePort) getSourcePortFig()).getNode() != null
                && ((FigMessagePort) getDestPortFig()).getNode() != null) {
            ((SequenceDiagramLayer) getLayer()).updateActivations();
            Globals.curEditor().damageAll();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getSource()
     */
    protected Object getSource() {
        Object owner = getOwner();
        if (owner == null) {
            return null;
	}
        return Model.getFacade().getSender(owner);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getDestination()
     */
    protected Object getDestination() {
        Object owner = getOwner();
        if (owner == null) {
            return null;
	}

        return Model.getFacade().getReceiver(owner);
    }

    /**
     * Call superclass to update text then recalculate group bounds
     * 
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateNameText()
     */
    protected void updateNameText() {
        super.updateNameText();
        textGroup.calcBounds();
    }
    
    /**
     * Call superclass to update text then recalculate group bounds
     * 
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        super.updateStereotypeText();
        textGroup.calcBounds();
    }

    /**
     * @return the source figobject
     */
    public FigClassifierRole getSourceFigClassifierRole() {
        return (FigClassifierRole) getSourceFigNode();
    }

    /**
     * @return the destination fig object
     */
    public FigClassifierRole getDestFigClassifierRole() {
        return (FigClassifierRole) getDestFigNode();
    }

    /**
     * This won't work, so this implementation does nothing.
     *
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#determineFigNodes()
     */
    protected boolean determineFigNodes() {
        return true;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionMessage(this);
    }
}
