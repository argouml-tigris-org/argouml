// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: FigSeqClRole.java
// Original Author: agauthie@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.sequence.specification_level.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.base.ModeSelect;
import org.tigris.gef.graph.*;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.presentation.*;

import org.argouml.application.api.*;
import org.argouml.kernel.*;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.ui.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.sequence.ui.*;

/** Class to display graphics for a UML sequence in a diagram. */

public class FigSeqClRolePort extends FigRect implements MouseListener, MouseMotionListener, FigSeqConstants {

    ////////////////////////////////////////////////////////////////
    // constants

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigSeqClRoleLifeline _figLine;
    private Vector  _msg = new Vector();    // Connected messages
    private boolean _snd;                   // true = this ports classifier role
                                            // is the sender of the message;
                                            // false = receiver.
    private int _idxThd;
    private int _idxLvl;
    private int _idxLin;

    // actual mouse position , needed for dynamically placed  rapid buttons
    int _yPos;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigSeqClRolePort(FigSeqClRoleLifeline figLine, int idxThd, int idxLvl, int idxLin) {
        super(0, 0, LIFELINE_PORT_SIZE, LIFELINE_PORT_SIZE);
        _figLine = figLine;
        _idxThd  = idxThd;
        _idxLvl  = idxLvl;
        _idxLin  = idxLin;
        setDisplayed(false);
    }

    public int getCenterX() {
        return _x + _w / 2;
    }
    public int getCenterY() {
        return _y + _h / 2;
    }

    /**
     * Which thread (of the object lifeline) is this port part of ?
     */
    public int getThreadIndex() {
        return _idxThd;
    }
    /**
     * Nesting level
     */
    public int getLevelIndex() {
        return _idxLvl;
    }
    /**
     * Which lifetime index does this port have ?
     */
    public int getLineIndex() {
        return _idxLin;
    }
    public void setLineIndex(int idx) {
        _idxLin = idx;
    }

    public void incrementMessageIndex(int di) {
        _idxLin += di;
        setLocation(_x, _y + (LIFELINE_PORT_SIZE + LIFELINE_PORT_DIST) * di);
    }
    public void decrementMessageIndex(int di) {
        _idxLin -= di;
        setLocation(_x, _y - (LIFELINE_PORT_SIZE + LIFELINE_PORT_DIST) * di);
    }

    public void addMessage(FigSeqMessage msg, boolean sender) {
        if (_msg.isEmpty()) {
            _msg.add(msg);
            _snd = sender;
        } else {
            if (_snd == sender) {
                MAction ma = (MAction)((MMessage)msg.getOwner()).getAction();
                boolean connectable = _snd ^ (ma instanceof MReturnAction);
                if (connectable && _msg.size() == 1) {
                    // If there are more than one message in the vector, the test
                    // is already done for every inserted message. This test is
                    // only needed for thread splitting and joining, so it is not
                    // done at the first insert.
                    ma = (MAction)((MMessage)((FigSeqMessage)_msg.get(0)).getOwner()).getAction();
                    connectable = _snd ^ (ma instanceof MReturnAction);
                }
                if (connectable) {
                    _msg.add(msg);
                } else {
                    throw new RuntimeException("Message cannot be connected to port because its associated action of wrong type for joining/splitting");
                }
            } else {
                throw new RuntimeException("Only messages with same direction may be added to a port");
            }
        }
    }

    public boolean isConnectable(FigSeqMessage msg, boolean sender) {
        return isConnectable(((MAction)((MMessage)msg.getOwner()).getAction()).getClass(), sender);
    }

    public boolean isConnectable(Class actiontype, boolean sender) {
        if (_msg.isEmpty()) {
            return true;
        } else {
            if (_snd == sender) {
                boolean connectable = _snd ^ (actiontype == MReturnAction.class);
                if (connectable && _msg.size() == 1) {
                    // If there are more than one message in the vector, the test
                    // is already done for every inserted message. This test is
                    // only needed for thread splitting and joining, so it is not
                    // done at the first insert.
                    MAction ma = (MAction)((MMessage)((FigSeqMessage)_msg.get(0)).getOwner()).getAction();
                    connectable = _snd ^ (ma instanceof MReturnAction);
                }
                return connectable;
            } else {
                return false;
            }
        }
    }

    public Collection getMessages() {
        return Collections.unmodifiableCollection(_msg);
    }
    public Collection getMessagesForAction(Class actiontype) {
        Vector v = new Vector();
        FigSeqMessage[] aMsgs = getMessagesAsArray();
        for(int i = 0; i < aMsgs.length; i++) {
            if (((MAction)((MMessage)aMsgs[i].getOwner()).getAction()).getClass() == actiontype) {
                v.add(aMsgs[i]);
            }
        }
        return v;
    }

    public FigSeqMessage[] getMessagesAsArray() {
        return (FigSeqMessage[])_msg.toArray(new FigSeqMessage[0]);
    }

    public boolean isSender() {
        return _snd;
    }
    public boolean isReceiver() {
        return ! _snd;
    }
    public boolean isConnected() {
        return ! _msg.isEmpty();
    }

    public String toString() {
        return "FigSeqClRolePort[Classifier.text=" + _figLine.getBaseFig().ownerName()
             + ",idxThread=" + _idxThd
             + ",idxLevel=" + _idxLvl
             + ",idxLine=" + _idxLin;
    }

    public void paint(Graphics g) {
        System.out.println("Painting port, visibility = " + isDisplayed()
                         + ", x = " + this.getX()
                         + ", y = " + this.getY()
                         + ", w = " + this.getWidth()
                         + ", h = " + this.getHeight());
//        this.setFillColor(Color.red);
        super.paint(g);
    }

    public FigSeqClRole getBaseFig() {
        return _figLine.getBaseFig();
    }

    public Vector getGravityPoints() {
        Point  c = this.center();
        Vector r = new Vector(2);
        r.add(new Point(c.x - LIFELINE_ACTIVATION_WIDTH / 2, c.y));
        r.add(new Point(c.x + LIFELINE_ACTIVATION_WIDTH / 2, c.y));
        return r;
    }

    /** Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     */
    public void mouseClicked(MouseEvent e) {
        // Start drawing a line
    }

    /** Invoked when the mouse enters a component.
     *
     */
    public void mouseEntered(MouseEvent e) {
    }

    /** Invoked when the mouse exits a component.
     *
     */
    public void mouseExited(MouseEvent e) {
    }

    /** Invoked when a mouse button has been pressed on a component.
     *
     */
    public void mousePressed(MouseEvent e) {
//        System.out.println("AbsoluteLineIndex = " + getAbsoluteLineIndex());
    }

    /** Invoked when a mouse button has been released on a component.
     *
     */
    public void mouseReleased(MouseEvent e) {
    }

    /** Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&Drop operation.
     *
     */
    public void mouseDragged(MouseEvent e) {
    }

    /** Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     */
    public void mouseMoved(MouseEvent e) {
    }

    public int getAbsoluteLineIndex() {
        int c = _figLine.getPortModel().getCreationLineIndex();
        return _idxLin + ((c < 1) ? 0 : c + 2);
    }
} /* end class FigSeqClRolePort */
