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

import java.awt.*;
import java.util.*;
import java.util.Enumeration;
import java.beans.*;
import java.awt.event.*;
import javax.swing.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.base.SelectionManager;

import org.tigris.gef.base.ModeSelect;

import org.argouml.application.api.*;
import org.argouml.kernel.*;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsFactory;
import org.argouml.ui.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.sequence.ui.*;
import org.argouml.uml.diagram.sequence.specification_level.PortModel;
import org.argouml.uml.diagram.sequence.specification_level.event.*;

/** Class to display graphics for a UML sequence in a diagram. */

public class FigSeqClRoleLifeline extends FigGroup implements FigSeqConstants, PortModelChangeListener {

    ////////////////////////////////////////////////////////////////
    // constants

    ////////////////////////////////////////////////////////////////
    // instance variables
    private FigSeqClRole _figBase;
    private PortModel _model;
    private Vector _ports;
    private Vector _lines;
    private Vector _activations;
    // add other Figs here as needed

    // actual mouse position , needed for dynamically placed  rapid buttons
    int _yPos;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigSeqClRoleLifeline(FigSeqClRole figBase) {
        _figBase = figBase;
        _model   = new PortModel(this);
        setLocation(20, 25);
        rebuild();
    }

    public boolean isResizable() {
        return false;
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

    public Object clone() {
        // TODO: clone is incomplete; does not copy elements from vectors
        FigSeqClRoleLifeline figClone = (FigSeqClRoleLifeline)super.clone();
//        Vector v = figClone.getFigs();
        figClone._activations = _activations;
        figClone._model = _model;
        return figClone;
    }

    /**
     * The width and height should'nt be set using setBounds, as this is a composite object
     * @param x The new x coordinate.
     * @param y The new y coordinate.
     * @param w Ignored.
     * @param h Ignored.
     */
    public void setBounds(int x, int y, int w, int h) {
        setLocation(x, y);
    }

    /**
     * This method is used to calculate ports use:
     * - The first port on a lifeline has index 0. If this port
     *   is connected to another port, it becomes port 1 etc.
     * - Between two connected ports there is at least one
     *   unconnected port.
     * - CreateActions connect to the object figure; therefore,
     *   the destination port gets a negative port number.
     */
    int getObjectCreationIndex() {
        Rectangle b = _figBase.getFigObject().getBounds();
        int hs = LIFELINE_PORT_DIST + LIFELINE_PORT_SIZE;
        int hf = (b.y + b.height - ORIGIN_Y - 1);
        int h0 = (hs - 1) - (hf) % hs;              // The filler length from object to lifeline;
                                                    // this will enable horizontal lines by aligning
                                                    // ports of different lifelines (non-horizontal
                                                    // lines are used for delayed messages; this is
                                                    // currently not supported)
        int r = (hf + h0) / hs;
        return - (r + 1) / 2;
    }

    private FigLine createLineSegment(int x1, int y1, int x2, int y2) {
        System.out.println("Creating line segment: (" + x1 + "," + y1 + ") - (" + x2 + "," + y2 + ")");
        FigLine f = new FigLine(x1, y1, x2, y2);
        f.setLineWidth(1);
        f.setLineColor(Color.black);
        f.setDashed(true);
        return f;
    }

    /**
     * Creates a message connection to the lifeline
     */
    public FigSeqClRolePort getPortFig(int idxConn, int idxThread, int idxLevel) {
        return _model.getPort(idxConn * 2 + 1, idxThread, idxLevel);
    }

    /**
     * Creates a message connection to the lifeline
     */
    public FigSeqClRolePort getNewPortFig(int idxConn, int idxThread, int idxLevel) {
        _model.insertConnectionPoint(idxConn);
        return _model.getPort(idxConn * 2 + 1, idxThread, idxLevel);
    }

    public FigSeqClRole getBaseFig() {
        return _figBase;
    }

    void setPortsDisplayed(boolean d) {
        _model.setPortsDisplayed(d);
    }

    public void portModelChanged(PortModelChangeEvent pmce) {
        rebuild();
    }

    public void relocate() {
        Rectangle b = _figBase.getFigObject().getBounds();
        int x = b.x + b.width / 2;
        int y = b.y + b.height;
        if (_activations.isEmpty()) {
            if (!_ports.isEmpty()) {
                x -= LIFELINE_PORT_SIZE / 2;
            }
        } else {
            x -= LIFELINE_ACTIVATION_WIDTH / 2;
        }
        setLocation(x, y);
    }

    void rebuild() {
        _ports = _model.getPortFigs();
        _lines = _model.getLineSegments();
        _activations = _model.getActivations();
        FigSeqClRoleActivation acti = null;
        FigSeqClRoleActivation act0 = null;
        this.removeAll();
        for(int i = 0; i < _lines.size(); i++) {
            addFig((FigPoly)_lines.get(i));
        }
        for(int i = 0; i < _activations.size(); i++) {
            acti = (FigSeqClRoleActivation)_activations.get(i);
            if (acti.getThreadIndex() == 0 && acti.getLevelIndex() == 0) act0 = acti;
            addFig(acti);
        }
        for(int i = 0; i < _ports.size(); i++) {
            addFig((FigSeqClRolePort)_ports.get(i));
        }
        if (_model.isTerminated()) {
            int   n = _model.getLineCount() - 1;
            Point p = act0.getLastPort().center();
            p.y = this.getY() + this.getHeight();
            FigLine term = new FigLine(p.x - TERMINATION_WIDTH / 2,
                                       p.y - TERMINATION_WIDTH / 2,
                                       p.x + TERMINATION_WIDTH / 2,
                                       p.y + TERMINATION_WIDTH / 2);
            addFig(term);
            term = new FigLine(p.x - TERMINATION_WIDTH / 2,
                               p.y + TERMINATION_WIDTH / 2,
                               p.x + TERMINATION_WIDTH / 2,
                               p.y - TERMINATION_WIDTH / 2);
            addFig(term);
        }
        relocate();
    }

    public PortModel getPortModel() {
        return _model;
    }

    public Fig hitPort(Rectangle r) {
        return _model.hitPort(r);
    }

    public FigSeqClRolePort getFirstConnectedPort() {
        return _model.getFirstConnectedPort();
    }
    public FigSeqClRolePort getConnectedPortBelow(FigSeqClRolePort port) {
        return _model.getConnectedPortBelow(port);
    }
    public FigSeqClRolePort getConnectedPortAbove(FigSeqClRolePort port) {
        return _model.getConnectedPortAbove(port);
    }
    public int getLineCount() {
        return _model.getLineCount();
    }
} /* end class FigSeqClRoleLifeline */