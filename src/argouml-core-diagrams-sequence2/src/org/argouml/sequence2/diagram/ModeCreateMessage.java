/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.sequence2.diagram;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.Model;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigPoly;

/**
 * Mode to create a link between two FigClassifierRoles.
 * TODO: Provide a ModeFactory and then this class can become package scope.
 * @author penyaskito
 */
public class ModeCreateMessage extends ModeCreatePolyEdge {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ModeCreateMessage.class.getName());

    private static final int DEFAULT_ACTIVATION_HEIGHT = 50;
    private static final int DEFAULT_MESSAGE_GAP = 20;

    /**
     * The constructor.
     *
     * @param par the editor
     */
    public ModeCreateMessage(Editor par) {
        super(par);
        LOG.log(Level.FINE, "ModeCreateMessage created with editor:{0}", editor);
    }

    /**
     * The constructor.
     */
    public ModeCreateMessage() {
        super();
        LOG.log(Level.FINE, "ModeCreateMessage created without editor.");
    }

    @Override
    public void endAttached(FigEdge fe) {
        super.endAttached(fe);
        final SequenceDiagramGraphModel gm =
            (SequenceDiagramGraphModel) getEditor().getGraphModel();

        final FigMessage figMessage = (FigMessage) fe;

        Object message = fe.getOwner();
        FigClassifierRole dcr = (FigClassifierRole) fe.getDestFigNode();
        FigClassifierRole scr = (FigClassifierRole) fe.getSourceFigNode();

        ensureSpace(figMessage);

        if (figMessage.isSynchCallMessage()) {
            // Auto-create a return message for a call message

            // TODO: Maybe a return message already exists. Check first and
            // and if the first found has no activator then set this call
            // message as the activator and skip the code below.

            // get the source of the return message
            final Object returnMessageSource =
                Model.getFacade().getReceiver(message);
            // get the dest of the return message
            final Object returnMessageDest =
                Model.getFacade().getSender(message);

            // create the return message modelelement with the interaction
            // and the collaboration
            final Object returnMessage = gm.connectMessage(
                    returnMessageSource,
                    returnMessageDest,
                    Model.getMessageSort().getReply());

            // Correct the activator value
            Model.getCollaborationsHelper().setActivator(
                    returnMessage, message);

            final LayerPerspective layer =
                (LayerPerspective) editor.getLayerManager().getActiveLayer();

            FigMessage returnEdge = null;

            List<Fig> figs = layer.getContents();
            for (Fig fig : figs) {
                if (fig.getOwner() == returnMessage) {
                    returnEdge = (FigMessage) fig;
                    break;
                }
            }

            returnEdge.setSourcePortFig(fe.getDestPortFig());
            returnEdge.setSourceFigNode(dcr);
            returnEdge.setDestPortFig(fe.getSourcePortFig());
            returnEdge.setDestFigNode(scr);

            final Point[] points = returnEdge.getPoints();
            for (int i = 0; i < points.length; ++i) {
                // TODO: this shouldn't be hardcoded
                // 20 is the height of the spline
                // 50 is the default activation height
                points[i].y = fe.getFirstPoint().y + DEFAULT_ACTIVATION_HEIGHT;
            }
            returnEdge.setPoints(points);

            if (returnEdge.isSelfMessage()) {
                returnEdge.convertToArc();
            }

            // Mark the contain FigPoly as complete.
            // TODO: I think more work is needed in GEF to either do this
            // automatically when both ends are set or at the very least
            // Give a setComplete method on FigPolyEdge that calls its
            // contained poly
            FigPoly poly = (FigPoly) returnEdge.getFig();
            poly.setComplete(true);
        } else if (figMessage.isReplyMessage()) {
            figMessage.determineActivator();
        }
        FigPoly poly = (FigPoly) fe.getFig();
        poly.setComplete(true);

        dcr.createActivations();
        dcr.renderingChanged();
        if (dcr != scr) {
            scr.createActivations();
            scr.renderingChanged();
        }
    }

    /**
     * Called for a call message. Make sure there is enough space to fit the
     * return message that will be created below.
     * @param figMessage
     */
    private void ensureSpace(final FigMessage figMessage) {
        // Make sure there is the minimum gap above the message being drawn
        final FigMessage firstMessageAbove = getNearestMessage(
                (FigClassifierRole) getSourceFigNode(),
                figMessage,
                false);

        if (firstMessageAbove != null) {
            final int figMessageY = figMessage.getFirstPoint().y;
            final int firstMessageY = firstMessageAbove.getFirstPoint().y;
            if ((figMessageY - firstMessageY) < DEFAULT_MESSAGE_GAP) {
                figMessage.translateEdge(
                        0,
                        firstMessageY + DEFAULT_MESSAGE_GAP - figMessageY);
            }
        }

        // Make sure there is the minimum gap below the message being drawn
        LOG.log(Level.INFO, "Looking for minimum space below");
        final FigMessage firstMessageBelow = getNearestMessage(
                (FigClassifierRole) getSourceFigNode(),
                figMessage,
                true);

        final int heightPlusGap;
        if (figMessage.isSynchCallMessage()) {
            heightPlusGap =
                DEFAULT_ACTIVATION_HEIGHT + DEFAULT_MESSAGE_GAP;
        } else {
            heightPlusGap =
                DEFAULT_MESSAGE_GAP;
        }

        if (firstMessageBelow != null
                && firstMessageBelow.getFirstPoint().y
                < figMessage.getFirstPoint().y + heightPlusGap) {

            final int dy =
                (figMessage.getFirstPoint().y
                + heightPlusGap)
                - firstMessageBelow.getFirstPoint().y;

            for (FigMessage fig : getMessagesBelow(figMessage)) {
                fig.translateEdge(0, dy);
                if (fig.isCreateMessage()) {
                    FigClassifierRole fcr =
                        (FigClassifierRole) fig.getDestFigNode();
                    fcr.positionHead(fig);
                }
            }
        }
    }

    /**
     * Get a list of FigMessages below (higher Y position) than the FigMessage
     * provided.
     * @param figMessage
     * @return a list of FigMessage
     */
    private List<FigMessage> getMessagesBelow(FigMessage figMessage) {
        final List<FigMessage> messagesBelow = new ArrayList<FigMessage>();
        for (Fig f : getEditor().getLayerManager().getContents()) {
            if (f instanceof FigMessage
                    && f != figMessage) {
                FigMessage fm = (FigMessage) f;
                if (fm.getFirstPoint().y >= figMessage.getFirstPoint().y) {
                    messagesBelow.add((FigMessage) f);
                }
            }
        }
        return messagesBelow;
    }

    /**
     * Get the first FigMessage below (higher Y position) the given
     * FigMessage.
     * @param figMessage
     * @return the FigMessage below or null
     */
    private FigMessage getNearestMessage(
            final FigClassifierRole figClassifierRole,
            final FigMessage figMessage,
            final boolean below) {

        FigMessage nearestMessage = null;

        for (FigEdge fe : figClassifierRole.getFigEdges()) {
            if (fe instanceof FigMessage && fe != figMessage) {
                final FigMessage fm = (FigMessage) fe;
                final int y = fm.getFirstPoint().y;
                if (below) {
                    if (isBetween(y, figMessage, nearestMessage)) {
                        nearestMessage = fm;
                    }
                } else {
                    if (isBetween(y, nearestMessage, figMessage)) {
                        nearestMessage = fm;
                    }
                }
            }
        }

        return nearestMessage;
    }

    /**
     * Return true if a given y co-ordinate is between the two given messages
     * @param y the value to test
     * @param message1 the lowest value to check
     * @param message2 the upper value to check
     * @return
     */
    private boolean isBetween(
            final int val,
            final FigMessage message1,
            final FigMessage message2) {
        if ((message1 == null || val >= message1.getFirstPoint().y)
                && (message2 == null || val <= message2.getFirstPoint().y)) {
            return true;
        }
        return false;
    }
}
