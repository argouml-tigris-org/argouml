// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: SelectionUseCase.java
// Classes: SelectionUseCase
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.use_case.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Icon;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;
import org.tigris.gef.util.*;

import org.apache.commons.logging.Log;
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.diagram.ui.*;

public class SelectionUseCase extends SelectionWButtons {
    protected static Log logger = 
        org.apache.commons.logging.LogFactory.getLog(SelectionUseCase.class);
  ////////////////////////////////////////////////////////////////
  // constants
  public static Icon inherit = ResourceLoader.lookupIconResource("Generalization");
  public static Icon assoc = ResourceLoader.lookupIconResource("Association");


  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionUseCase for the given Fig */
  public SelectionUseCase(Fig f) { super(f); }

  /** Return a handle ID for the handle under the mouse, or -1 if
   *  none. Needs-More-Work: in the future, return a Handle instance or
   *  null. <p>
   *  <pre>
   *   0-------1-------2
   *   |               |
   *   3               4
   *   |               |
   *   5-------6-------7
   * </pre>
   */
  public void hitHandle(Rectangle r, Handle h) {
    super.hitHandle(r, h);
    if (h.index != -1) return;
    if (!_paintButtons) return;
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    if (sm.size() != 1) return;
    ModeManager mm = ce.getModeManager();
    if (mm.includes(ModeModify.class) && _pressedButton == -1) return;
    int cx = _content.getX();
    int cy = _content.getY();
    int cw = _content.getWidth();
    int ch = _content.getHeight();
    int iw = inherit.getIconWidth();
    int ih = inherit.getIconHeight();
    int aw = assoc.getIconWidth();
    int ah = assoc.getIconHeight();
    if (hitAbove(cx + cw/2, cy, iw, ih, r)) {
      h.index = 10;
      h.instructions = "Add a more general use case";
    }
    else if (hitBelow(cx + cw/2, cy + ch, iw, ih, r)) {
      h.index = 11;
      h.instructions = "Add a specialized use case";
    }
    else if (hitLeft(cx + cw, cy + ch/2, aw, ah, r)) {
      h.index = 12;
      h.instructions = "Add an associated actor";
    }
    else if (hitRight(cx, cy + ch/2, aw, ah, r)) {
      h.index = 13;
      h.instructions = "Add an associated actor";
    }
    else {
      h.index = -1;
      h.instructions = "Move object(s)";
    }
  }


  /** Paint the handles at the four corners and midway along each edge
   * of the bounding box.  */
  public void paintButtons(Graphics g) {
    int cx = _content.getX();
    int cy = _content.getY();
    int cw = _content.getWidth();
    int ch = _content.getHeight();
    paintButtonAbove(inherit, g, cx + cw/2, cy, 10);
    paintButtonBelow(inherit, g, cx + cw/2, cy + ch, 11);
    paintButtonLeft(assoc, g, cx + cw, cy + ch/2, 12);
    paintButtonRight(assoc, g, cx, cy + ch/2, 13);
  }


  public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {
    if (hand.index < 10) {
      _paintButtons = false;
      super.dragHandle(mX, mY, anX, anY, hand);
      return;
    }
    int cx = _content.getX(), cy = _content.getY();
    int cw = _content.getWidth(), ch = _content.getHeight();
    int newX = cx, newY = cy, newW = cw, newH = ch;
    Dimension minSize = _content.getMinimumSize();
    int minWidth = minSize.width, minHeight = minSize.height;
    Class edgeClass = null;
    Class nodeClass = null;
    if (hand.index == 10 || hand.index == 11)
      nodeClass = MUseCaseImpl.class;
    else
      nodeClass = MActorImpl.class;

    int bx = mX, by = mY;
    boolean reverse = false;
    switch (hand.index) {
    case 10: //add superclass
      edgeClass = MGeneralization.class;
      by = cy;
      bx = cx + cw/2;
      break;
    case 11: //add subclass
      edgeClass = MGeneralization.class;
      reverse = true;
      by = cy + ch;
      bx = cx + cw/2;
      break;
    case 12: //add assoc
      edgeClass = MAssociation.class;
      by = cy + ch/2;
      bx = cx + cw;
      break;
    case 13: // add assoc
      edgeClass = MAssociation.class;
      reverse = true;
      by = cy + ch/2;
      bx = cx;
      break;
    default:
      logger.warn("invalid handle number");
      break;
    }
    if (edgeClass != null && nodeClass != null) {
      Editor ce = Globals.curEditor();
      ModeCreateEdgeAndNode m = new
	ModeCreateEdgeAndNode(ce, edgeClass, nodeClass, false);
      m.setup((FigNode)_content, _content.getOwner(), bx, by, reverse);
      ce.mode(m);
    }

  }


  public void buttonClicked(int buttonCode) {
    super.buttonClicked(buttonCode);
    MClassifier newNode = null;
    if (buttonCode == 10 || buttonCode == 11)
      newNode = UmlFactory.getFactory().getUseCases().createUseCase();
    else
      newNode = UmlFactory.getFactory().getUseCases().createActor();

    FigUseCase fc = (FigUseCase) _content;
    MUseCase cls = (MUseCase) fc.getOwner();

    Editor ce = Globals.curEditor();
    GraphModel gm = ce.getGraphModel();
    if (!(gm instanceof MutableGraphModel)) return;
    MutableGraphModel mgm = (MutableGraphModel) gm;

    if (!mgm.canAddNode(newNode)) return;
    GraphNodeRenderer renderer = ce.getGraphNodeRenderer();
    LayerPerspective lay = (LayerPerspective)
      ce.getLayerManager().getActiveLayer();
    Fig newFC = renderer.getFigNodeFor(gm, lay, newNode);

    Rectangle outputRect = new Rectangle(Math.max(0, fc.getX() - 200),
					 Math.max(0, fc.getY() - 200),
					 fc.getWidth() + 400,
					 fc.getHeight() + 400);
    if (buttonCode == 10) {
      newFC.setLocation(fc.getX(), Math.max(0, fc.getY() - 200));
      outputRect.height = 200;
      lay.bumpOffOtherNodesIn(newFC, outputRect, false, false);
    }
    else if (buttonCode == 11) {
      newFC.setLocation(fc.getX(), fc.getY() + fc.getHeight() + 100);
      outputRect.y = fc.getY() + fc.getHeight() + 100;
      outputRect.height = 200;
      lay.bumpOffOtherNodesIn(newFC, outputRect, false, false);
    }
    else if (buttonCode == 12) {
      newFC.setLocation(fc.getX() + fc.getWidth() + 100, fc.getY());
      outputRect.x = fc.getX()+ fc.getWidth() + 100 ;
      outputRect.width = 200;
      lay.bumpOffOtherNodesIn(newFC, outputRect, false, true);
    }
    else if (buttonCode == 13) {
      newFC.setLocation(Math.max(0, fc.getX() - 200), fc.getY());
      outputRect.x = fc.getX() - 200;
      outputRect.width = 200;
      lay.bumpOffOtherNodesIn(newFC, outputRect, false, true);
    }
    ce.add(newFC);
    mgm.addNode(newNode);

    FigPoly edgeShape = new FigPoly();
    Point fcCenter = fc.center();
    edgeShape.addPoint(fcCenter.x, fcCenter.y);
    Point newFCCenter = newFC.center();
    edgeShape.addPoint(newFCCenter.x, newFCCenter.y);
    Object newEdge = null;
    if (buttonCode == 10) newEdge = addSuperClass(mgm, cls, newNode);
    else if (buttonCode == 11) newEdge = addSubClass(mgm, cls, newNode);
    else if (buttonCode == 12) newEdge = addAssocClassRight(mgm, cls, newNode);
    else if (buttonCode == 13) newEdge = addAssocClassLeft(mgm, cls, newNode);

    FigEdge fe = (FigEdge) lay.presentationFor(newEdge);
    edgeShape.setLineColor(Color.black);
    edgeShape.setFilled(false);
    edgeShape._isComplete = true;
    fe.setFig(edgeShape);
    ce.getSelectionManager().select(fc);
  }

  public Object addSuperClass(MutableGraphModel mgm, MUseCase cls,
			    MClassifier newCls) {
    return mgm.connect(cls, newCls, MGeneralization.class);
  }

  public Object addSubClass(MutableGraphModel mgm, MUseCase cls,
			    MClassifier newCls) {
    return mgm.connect(newCls, cls, MGeneralization.class);
  }

  public Object addAssocClassLeft(MutableGraphModel mgm, MUseCase cls,
			    MClassifier newCls) {
    return mgm.connect(cls, newCls, MAssociation.class);
  }

  public Object addAssocClassRight(MutableGraphModel mgm, MUseCase cls,
			    MClassifier newCls) {
    return mgm.connect(cls, newCls, MAssociation.class);
  }


} /* end class SelectionUseCase */

