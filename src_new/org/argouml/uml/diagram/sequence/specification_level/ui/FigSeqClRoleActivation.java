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

// File: FigSeqClRoleActivation.java
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

/** Class to display graphics for a UML sequence in a diagram. */

public class FigSeqClRoleActivation extends FigRect implements FigSeqConstants {

    ////////////////////////////////////////////////////////////////
    // constants

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigSeqClRoleLifeline _figLifeline;
    private FigSeqClRolePort[]   _afigPort = new FigSeqClRolePort[2];
    private boolean _active = false;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigSeqClRoleActivation(FigSeqClRoleLifeline figLifeline, FigSeqClRolePort[] ports) {
        super(ORIGIN_X, ORIGIN_Y, LIFELINE_ACTIVATION_WIDTH, LIFELINE_PORT_DIST * 3 + LIFELINE_PORT_SIZE * 2);
        _figLifeline = figLifeline;
        _afigPort[0] = ports[0];
        _afigPort[1] = ports[ports.length-1];
        layout();
    }

    public Object clone() {
        FigSeqClRoleActivation figClone = (FigSeqClRoleActivation)super.clone();
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    public Dimension getMinimumSize() {
        return new Dimension(LIFELINE_ACTIVATION_WIDTH, LIFELINE_ACTIVATION_WIDTH);
    }


    /**
     * Which thread (of the object lifeline) is this activation part of ?
     */
    public int getThreadIndex() {
        return _afigPort[0].getThreadIndex();
    }
    /**
     * Nesting level
     */
    public int getLevelIndex() {
        return _afigPort[0].getLevelIndex();
    }
    /**
     * Get message index of first port
     */
    public int getPortStartIndex() {
        return _afigPort[0].getLineIndex();
    }
    /**
     * Get count of enclosed ports.
     */
    public int getPortCount() {
        return _afigPort[1].getLineIndex() - getPortStartIndex() + 1;
    }
    /**
     * Get first enclosed port.
     */
    public FigSeqClRolePort getFirstPort() {
        return _afigPort[0];
    }
    /**
     * Get last enclosed port.
     */
    public FigSeqClRolePort getLastPort() {
        return _afigPort[1];
    }


    ////////////////////////////////////////////////////////////////
    // event handlers

  /** This method is called, when the FigSeqClRoleActivation is
   *    moving around. Changes the position of the
   *    FigSeqClRoleActivations. */
  public void changePosition(Vector contents) {

    int size = contents.size();
    for (int k=0; k<size; k++) {
      if (contents.elementAt(k) instanceof FigSeqClRoleActivation) {
        FigSeqClRoleActivation figure = (FigSeqClRoleActivation) contents.elementAt(k);
        if (figure != this) {
          Rectangle rect = figure.getBounds();
          if (((this.getBounds()).x + (this.getBounds()).width) > (rect.x + rect.width)) {
            int indexFigure = contents.indexOf(figure);
            int indexThis = contents.indexOf(this);
            if (indexFigure>indexThis) {
              contents.setElementAt(this, indexFigure);
              contents.setElementAt(figure, indexThis);
            }
          }
          if (((this.getBounds()).x) < (rect.x)) {
            int indexFigure = contents.indexOf(figure);
            int indexThis = contents.indexOf(this);
            if (indexFigure<indexThis) {
              contents.setElementAt(this, indexFigure);
              contents.setElementAt(figure, indexThis);
            }
          }
        }
      }
    }
  }


  /** Count the edges that are in this diagram */
  public int edgesCount(Vector contents) {
    int size = contents.size();
    int countEdges = 0;
    if (contents != null && size > 0) {
      for (int i=0; i<size; i++) {
        if (contents.elementAt(i) instanceof FigSeqAsRole) {
          countEdges++;
        }
      }
    }
    return countEdges;
  }


  /** Returns two Integers, one is the port-number of the FigSeqAsRole
   *    which is next to the given portNumber. The second Integer is
   *    the highest port-number */
  public Vector nearestLink(Vector edges, int portNumber, Vector contents) {
    Vector _nearest = new Vector();
    Enumeration e = edges.elements();
    int max = 100000;
    int nearest = 0;
    int high = 0;
    while (e.hasMoreElements()) {
      FigSeqAsRole  fsl = (FigSeqAsRole) e.nextElement();
      int pos = fsl.getPortNumber(contents);
      if (pos < max && pos > portNumber) {
        max = pos;
        nearest = max;
      }
      if (pos > high) high = pos;
    }
    _nearest.addElement(new Integer(nearest));
    _nearest.addElement(new Integer(high));
    return _nearest;
  }
   

  public void mousePressed(MouseEvent me) {
    Editor ce = Globals.curEditor();
    Selection sel = ce.getSelectionManager().findSelectionFor(this);
    if (sel instanceof SelectionSeqClRole)
      ((SelectionSeqClRole)sel).hideButtons();
     
  }
  
  public void mouseReleased(MouseEvent me) {
    if (getLayer() != null && getLayer() instanceof SequenceDiagramLayout) {
    	((SequenceDiagramLayout)getLayer()).placeAllFigures();
    } else {
    	Diagram diagram = ProjectBrowser.TheInstance.getActiveDiagram();
    	Layer lay = null;
    	if (diagram != null) {
    		lay = diagram.getLayer();
    		if (lay instanceof SequenceDiagramLayout) {
    			setLayer(lay);
    		} else {
    			String name = null;
    			GraphModel gm = null;
    			if (lay != null && lay instanceof LayerPerspective) {
    				gm = ((LayerPerspective)lay).getGraphModel();
    				name = ((LayerPerspective)lay).getName();
    			} else {
    				Editor ed = Globals.curEditor();
    				if (ed != null && ed.getGraphModel() != null && ed.getLayerManager() != null && ed.getLayerManager().getActiveLayer() != null) {
    					lay = ed.getLayerManager().getActiveLayer();
    					name = lay.getName();
    					gm = ed.getGraphModel();
    				} else
    					throw new IllegalStateException("No way to get graphmodel. Project corrupted");
    			}
    			setLayer(new SequenceDiagramLayout(name, gm));
    		}
    	}
    	((SequenceDiagramLayout)getLayer()).placeAllFigures();
    }
    
  }

    public void layout() {
        int xa = _afigPort[0].getCenterX() - LIFELINE_ACTIVATION_WIDTH / 2;
        int ya = _afigPort[0].getY() - LIFELINE_PORT_DIST / 2;
        int h0 = _afigPort[1].getY() - _afigPort[0].getY() + LIFELINE_PORT_SIZE + LIFELINE_PORT_DIST;
        int ha = h0;
        if (_afigPort[1].isConnected() && !_afigPort[1].isSender()) {
            if (!_afigPort[1].getMessagesForAction(MDestroyAction.class).isEmpty()) {
                ha -= LIFELINE_PORT_DIST / 2;
            }
        }
        if (_active) {
            Rectangle b = _figLifeline.getBaseFig().getFigObject().getBounds();
            int y0 = b.y + b.height;
            super.setBounds( xa, y0, LIFELINE_ACTIVATION_WIDTH, ha + ya - y0);
        } else {
            super.setBounds( xa, ya, LIFELINE_ACTIVATION_WIDTH, ha );
        }
    }

    public void setActiveState(boolean a) {
        _active = a;
        layout();
    }
} /* end class FigSeqClRoleActivation */
