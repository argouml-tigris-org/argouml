/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Owned;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.ArgoFigUtil;
import org.argouml.util.IItemUID;
import org.argouml.util.ItemUID;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdgePoly;
import org.tigris.gef.presentation.FigNode;

/**
 * Class to display a UML note connection to a annotated model element.
 * <p>
 *
 * The owner of this fig is always a CommentEdge. Because it is different from
 * most every other FigEdge in ArgoUML, it doesn't subclass FigEdgeModelElement.
 *
 * @author Andreas Rueckert a_rueckert@gmx.net
 * @author jaap.branderhorst@xs4all.nl
 */
public class FigEdgeNote extends FigEdgePoly
        implements ArgoFig, DiagramElement, Owned, IItemUID, PropertyChangeListener {

    private static final Logger LOG =
        Logger.getLogger(FigEdgeNote.class.getName());

    private Object comment;
    private Object annotatedElement;

    private DiagramSettings settings;

    private ItemUID itemUid;

    /**
     * @param element owning CommentEdge object. This is a special case since it
     *            is not a UML element.
     * @param theSettings render settings
     */
    public FigEdgeNote(Object element, DiagramSettings theSettings) {
        // element will normally be null when called from PGML parser
        // It will get it's source & destination set later in attachEdges
        super();
        settings = theSettings;

        if (element != null) {
            super.setOwner(element);
        } else {
            super.setOwner(new CommentEdge());
        }

        setBetweenNearestPoints(true);
        getFig().setLineWidth(LINE_WIDTH);
        getFig().setDashed(true);

        // Unfortunately the Fig and it's associated CommentEdge will not be
        // fully initialized yet here if we're being loaded from a PGML file.
        // The remainder of the initialization will happen when
        // set{Dest|Source}FigNode are called from PGMLStackParser.attachEdges()
    }

    /*
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void setFig(Fig f) {
        LOG.log(Level.INFO, "Setting the internal fig to {0}", f);

        super.setFig(f);
        getFig().setDashed(true);
    }


    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Translator.localize("misc.comment-edge");
    }

    /*
     * Listen for a RemoveAssociationEvent between the comment
     * and the annotated element. When recieved delete the CommentEdge
     * and this FigEdgeNote.
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent e) {
        if (e instanceof RemoveAssociationEvent
                && e.getOldValue() == annotatedElement) {
            removeFromDiagram();
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getTipString(java.awt.event.MouseEvent)
     */
    @Override
    public String getTipString(MouseEvent me) {
        return "Comment Edge"; // TODO: get tip string from comment
    }


    /*
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent pve) {
        modelChanged(pve);
    }



    /*
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    @Override
    public final void removeFromDiagram() {
        if (comment != null) {
            removeElementListener(comment);
        }

        super.removeFromDiagram();
        damage();
    }


    /**
     * Returns the source of the edge. The source is the owner of the
     * node the edge travels from in a binary relationship. For
     * instance: for a classifierrole, this is the sender.
     * @return MModelElement
     */
    protected Object getSource() {
        Object theOwner = getOwner();
        if (theOwner != null) {
            return ((CommentEdge) theOwner).getSource();
        }
        return null;
    }
    /**
     * Returns the destination of the edge. The destination is the
     * owner of the node the edge travels to in a binary
     * relationship. For instance: for a classifierrole, this is the
     * receiver.
     * @return Object
     */
    protected Object getDestination() {
        Object theOwner = getOwner();
        if (theOwner != null) {
            return ((CommentEdge) theOwner).getDestination();
        }
        return null;
    }

    /*
     * @see org.tigris.gef.presentation.FigEdge#setDestFigNode(org.tigris.gef.presentation.FigNode)
     */
    @Override
    public void setDestFigNode(FigNode fn) {
        // When this is called from PGMLStackParser.attachEdges, we finished
        // the initialization of owning pseudo element (CommentEdge)
        if (fn != null && Model.getFacade().isAComment(fn.getOwner())) {
            Object oldComment = comment;
            if (oldComment != null) {
                removeElementListener(oldComment);
            }
            comment = fn.getOwner();
            if (comment != null) {
                addElementListener(comment);
            }

            ((CommentEdge) getOwner()).setComment(comment);
        } else if (fn != null
                && !Model.getFacade().isAComment(fn.getOwner())) {
            annotatedElement = fn.getOwner();
            ((CommentEdge) getOwner()).setAnnotatedElement(annotatedElement);
        }

        super.setDestFigNode(fn);
    }

    /*
     * @see org.tigris.gef.presentation.FigEdge#setSourceFigNode(org.tigris.gef.presentation.FigNode)
     */
    @Override
    public void setSourceFigNode(FigNode fn) {
        // When this is called from PGMLStackParser.attachEdges, we finished
        // the initialization of owning pseudo element (CommentEdge)
        if (fn != null && Model.getFacade().isAComment(fn.getOwner())) {
            Object oldComment = comment;
            if (oldComment != null) {
                removeElementListener(oldComment);
            }
            comment = fn.getOwner();
            if (comment != null) {
                addElementListener(comment);
            }
            ((CommentEdge) getOwner()).setComment(comment);
        } else if (fn != null
                && !Model.getFacade().isAComment(fn.getOwner())) {
            annotatedElement = fn.getOwner();
            ((CommentEdge) getOwner()).setAnnotatedElement(annotatedElement);
        }
        super.setSourceFigNode(fn);
    }

    private void addElementListener(Object element) {
        Model.getPump().addModelEventListener(this, element);
    }

    private void removeElementListener(Object element) {
        Model.getPump().removeModelEventListener(this, element);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public Project getProject() {
        return ArgoFigUtil.getProject(this);
    }

    public DiagramSettings getSettings() {
        return settings;
    }

    public void renderingChanged() {

    }

    @SuppressWarnings("deprecation")
    @Deprecated
    public void setProject(Project project) {
        // unimplemented
    }

    public void setSettings(DiagramSettings theSettings) {
        settings = theSettings;
    }

    /**
     * Setter for the UID
     * @param newId the new UID
     */
    public void setItemUID(ItemUID newId) {
        itemUid = newId;
    }

    /**
     * Getter for the UID
     * @return the UID
     */
    public ItemUID getItemUID() {
        return itemUid;
    }


    /**
     * Setting the owner of the Fig must be done in the constructor and not
     * changed afterwards for all ArgoUML figs.
     *
     * @param owner owning UML element
     * @deprecated for 0.27.3 by tfmorris. Set owner in constructor. This method
     *             is implemented in GEF, so we'll leave this implementation
     *             here to block any attempts to use it within ArgoUML.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public void setOwner(Object owner) {
        if (owner != getOwner()) {
            throw new UnsupportedOperationException(
                    "Owner must be set in constructor and left unchanged");
        }
    }

}
