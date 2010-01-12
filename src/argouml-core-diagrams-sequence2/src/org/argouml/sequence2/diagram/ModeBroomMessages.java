/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.tigris.gef.base.FigModifyingModeImpl;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.FigNode;

/**
 * Brooms all the message which y coordinate is upper than the position
 * of the broom.
 * TODO: Provide a ModeFactory and then this class can become package scope.
 * @author penyaskito
 */
public class ModeBroomMessages extends FigModifyingModeImpl  {
   
    private static final int DIRECTION_UNDEFINED = 0;
    private static final int DIRECTION_UPWARD = 1;
    private static final int DIRECTION_DOWNWARD = 2;
    
    private int currentDirection;

    private boolean shouldDraw = true;

    private int x1, x2, y;
    private int lastX1, lastX2, lastY;
    
    /**
     * 
     */
    public ModeBroomMessages() {
        currentDirection = DIRECTION_UNDEFINED;
    }

    /** 
     * Handle mouse down events by preparing for a drag.  
     * @param me The mouse event.
     */
    public void mousePressed(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }
        
        // we initialize the coordinates.
        x1 = me.getX();
        x2 = me.getX();
        y = me.getY();
        lastX1 = x1 - 100;
        lastY = y;
        lastX2 = x1 + 100;
                
        editor.damageAll();
        me.consume();
        start();
    }
    
    @Override
    public void mouseDragged(MouseEvent me) {
        me.consume();
        editor.getSelectionManager().deselectAll();
        
        int crY = 0;
        List nodes = editor.getGraphModel().getNodes();
        if (nodes.size() > 0) {
            crY = (editor.getLayerManager().
                    getActiveLayer().presentationFor(nodes.get(0))).getY();
        }
        
        Point snapPt = me.getPoint();
        snapPt.x = Math.max(0, snapPt.x);
        snapPt.y = Math.max(crY, snapPt.y);
        editor.snap(snapPt);
        
        int dy = snapPt.y - lastY;

        if (dy == 0) {
            // we do nothing
            x1 = me.getX();
            x2 = me.getX();
            editor.damageAll();
            return;
        }
        
        // manage if cursor x is less than lastX1 
        lastX1 = Math.min(snapPt.x, lastX1);
        // manage if cursor x is more than lastX2 
        lastX2 = Math.max(snapPt.x, lastX2);     
        
        // manage changes of direction...
        if (currentDirection == DIRECTION_UPWARD 
                && dy > 0) {
            currentDirection = DIRECTION_DOWNWARD;
        }
        else if (currentDirection == DIRECTION_DOWNWARD 
                && dy > 0) {
            currentDirection = DIRECTION_UPWARD;            
        }
        // I have no direction yet
        if (currentDirection == DIRECTION_UNDEFINED) {
            if (dy < 0) {
                currentDirection = DIRECTION_UPWARD;
            } else if (dy > 0) {
                currentDirection = DIRECTION_DOWNWARD;
            }
        }
        
        x1 = lastX1;
        x2 = lastX2; 
                
        // we move down or up the messages that are downward the broom
        if (currentDirection == DIRECTION_DOWNWARD
                || currentDirection == DIRECTION_UPWARD) {
            List<FigMessage> messages = getAllFigMessagesDownward(lastY);
            for (FigMessage message : messages) {                
                message.translate(0, dy);
                // we recalculate all the activations
                FigNode source = message.getSourceFigNode();
                if (source != null && source instanceof FigClassifierRole) {
                    ((FigClassifierRole) source).createActivations();
                }
                FigNode dest = message.getDestFigNode();
                if (dest != null && !message.isSelfMessage() 
                	&& dest instanceof FigClassifierRole) {
                    ((FigClassifierRole) dest).createActivations();
                }
            }
        }
        lastY = snapPt.y;
        editor.damageAll();        
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {
        // in this event we do nothing,
        // only stop drawing the broom.
        shouldDraw = false;
        me.consume();
        editor.damageAll();
        done();
    }
    
    /**
     * Gets all the messages that are downward to a y position
     * @param position the coordinate
     * @return the list with all the messages.
     */
    private List<FigMessage> getAllFigMessagesDownward(int position) {
        final List<FigMessage> messages = getAllFigMessages();
        List<FigMessage> dMessages = new LinkedList<FigMessage>();
        for (FigMessage message : messages) {
            if (message.getY() > position) {
                dMessages.add(message);
            }
        }
        return dMessages;        
    }
    
    /**
     * Gets all the figmessages of the diagram.
     * @return the list of FigMessages
     */
    private List<FigMessage> getAllFigMessages() {
        List edges = editor.getGraphModel().getEdges();
        List<FigMessage> messages = new ArrayList<FigMessage>(edges.size());
        for (Object message : edges) {
            Object figM = 
                (editor.getLayerManager().
                    getActiveLayer().presentationFor(message));
            messages.add((FigMessage) figM);
        }        
        return messages;
    }
    
    @Override
    public void paint(Graphics g) {
        if (!shouldDraw) {
            return;
        }
        
        Color selectRectColor = Globals.getPrefs().getRubberbandColor();
        g.setColor(selectRectColor);
        
        
        switch(currentDirection) {
        
        case DIRECTION_UNDEFINED:
            // we draw a cross
            g.fillRect(x1 - 10, y - 2, 20, 4);
            g.fillRect((x1 + x2) / 2 - 2, y - 10, 4, 20);
            break;
            
        case DIRECTION_UPWARD:
            g.fillRect(lastX1, lastY - 2, lastX2 - lastX1, 4);
            g.fillRect((lastX1 + lastX2) / 2 - 2, lastY, 4, 10);
            break;
            
        case DIRECTION_DOWNWARD:
            g.fillRect(lastX1, lastY - 2, lastX2 - lastX1, 4);
            g.fillRect((lastX1 + lastX2) / 2 - 2, lastY - 10, 4, 10);
            break;
        }
    }
}
