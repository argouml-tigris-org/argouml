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




// File: SelectionSeqObject.java
// Classes: SelectionSeqObject
// Original Author: 5kanzler@informatik.uni-hamburg.de

/**
 * This class provides dynamically placed rapid buttons for objects in a sequence diagram.
 * By clicking the mouse button on the object's lifeline, the rapid buttons appear 
 * at this position.
 * If you click on the rapid button , a new object is created right beside the selected object.
 */

package org.argouml.uml.diagram.sequence.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Icon;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;
import org.tigris.gef.util.Util;

import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.sequence.*;

public class SelectionSeqObject extends SelectionWButtons  {

  
  ////////////////////////////////////////////////////////////////
  // constants
  public static Icon stimCall = Util.loadIconResource("StimulusCall");
  public static Icon stimRet = Util.loadIconResource("StimulusReturn");

  ////////////////////////////////////////////////////////////////
  // instance variables
  private int yPos=0;
  
  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new SelectionClass for the given Fig */
  public SelectionSeqObject(Fig f) { super(f); }

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
    int sCw = stimCall.getIconWidth();
    int sCh = stimCall.getIconHeight();
    int sRw = stimRet.getIconWidth();
    int sRh = stimRet.getIconHeight();

    if (hitLeft(cx + cw, yPos, sCw, sCh, r)) {
      h.index = 10;
      h.instructions = "Add a called object";
    }
    else if (hitRight(cx, yPos, sRw, sRh, r)) {
      h.index = 11;
      h.instructions = "Add a calling object";
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
   
    // get the position at which the user clicked the mouse button
    yPos = ((FigSeqObject) _content)._yPos;
     
    paintButtonLeft(stimCall, g, cx + cw, yPos , 10);  
    paintButtonRight(stimRet, g, cx, yPos, 11);

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
    Class nodeClass = ru.novosoft.uml.behavior.common_behavior.MObjectImpl.class;
    Class actionClass = null;
    int bx = mX, by = mY;
    boolean reverse = false;
    switch (hand.index) {
    case 10: //add a called object
      edgeClass = ru.novosoft.uml.behavior.common_behavior.MLinkImpl.class;
      actionClass =  ru.novosoft.uml.behavior.common_behavior.MCallActionImpl.class;
      by = yPos;
      bx = cx + cw;
      break;
    case 11: // add a callin object
      edgeClass = ru.novosoft.uml.behavior.common_behavior.MLinkImpl.class;
      actionClass = ru.novosoft.uml.behavior.common_behavior.MReturnActionImpl.class;
      //reverse = true;
      by = yPos;
      bx = cx;
      break;
    default:
      System.out.println("invalid handle number");
      break;
    }
    if (edgeClass != null && nodeClass != null && actionClass != null) {
      Editor ce = Globals.curEditor();
      
      // last parameter in  constructor interface:
      // if true, the method ModeCreateEdgeAndNode.postProcessEdge() will be called
      ModeCreateEdgeAndNode m = new
          ModeCreateEdgeAndNode(ce, edgeClass, nodeClass, false);
      m.setup((FigNode)_content, _content.getOwner(), bx, by, reverse);
      ce.mode(m);
      m.setArg("action", actionClass);
    }
  
  }


  public void buttonClicked(int buttonCode) {
   
    super.buttonClicked(buttonCode);
    MObject newNode = new MObjectImpl();
    FigSeqObject fc = (FigSeqObject) _content;
    MObject cls = (MObject) fc.getOwner();

    Editor ce = Globals.curEditor();
    GraphModel gm = ce.getGraphModel();
    if (!(gm instanceof MutableGraphModel)) return;
    MutableGraphModel mgm = (MutableGraphModel) gm;

    if (!mgm.canAddNode(newNode)) return;
    GraphNodeRenderer renderer = ce.getGraphNodeRenderer();
    LayerPerspective lay = (LayerPerspective)
      ce.getLayerManager().getActiveLayer();
    Fig newFC = renderer.getFigNodeFor(gm, lay, newNode);
    
    if (buttonCode == 10) {
      int index = lay.indexOf( fc );
      lay.insertAt(newFC, index+1);      
    }
    else if (buttonCode == 11) {
      int index = lay.indexOf( fc );
      lay.insertAt(newFC, index);

    }

    mgm.addNode(newNode);

    FigPoly edgeShape = new FigPoly();
    Point fcCenter = fc.center();
    //edgeShape.addPoint(fcCenter.x, fcCenter.y);
    edgeShape.addPoint(fcCenter.x, yPos);

    Point newFCCenter = newFC.center();
    edgeShape.addPoint(newFCCenter.x, yPos);
    Object newEdge = null;
   
    if (buttonCode == 10) newEdge = addLinkStimulusCall(mgm, cls, newNode);
    else if (buttonCode == 11) newEdge = addLinkStimulusReturn(mgm, cls, newNode);

    MLink link = (MLink) newEdge;
    FigSeqLink figSeqLink = (FigSeqLink) lay.presentationFor(newEdge);


    edgeShape.setLineColor(Color.black);
    edgeShape.setFilled(false);
    edgeShape._isComplete = true;
    figSeqLink.setFig(edgeShape);
    ce.getSelectionManager().select(fc);


    Collection liEnds = link.getConnections();
    if (liEnds.size() != 2 ) return;
    Iterator iter = liEnds.iterator();
    MLinkEnd le1 = (MLinkEnd)iter.next();
    MLinkEnd le2 = (MLinkEnd)iter.next();
    MObject objSrc = (MObject)le1.getInstance();
    MObject objDst = (MObject)le2.getInstance();

    FigSeqObject figObjSrc = (FigSeqObject)lay.presentationFor(objSrc);
    FigSeqObject figObjDst = (FigSeqObject)lay.presentationFor(objDst);

    if ( figObjSrc != null && figObjDst != null ) {
              
      figSeqLink.setSourcePortFig( figObjSrc._lifeline );
      figSeqLink.setDestPortFig( figObjDst._lifeline);

    }
   
     figSeqLink.addFigSeqStimulusWithAction();
  }

  public Object addLinkStimulusCall(MutableGraphModel mgm, MObject cls,
          MObject newCls) {
    Editor ce = Globals.curEditor();
    ModeManager modeManager = ce.getModeManager();
    Mode mode = (Mode)modeManager.top();
    mode.setArg("action", ru.novosoft.uml.behavior.common_behavior.MCallActionImpl.class);

    return mgm.connect(cls, newCls, MLinkImpl.class);
  }

  public Object addLinkStimulusReturn(MutableGraphModel mgm, MObject cls,
          MObject newCls) {
    Editor ce = Globals.curEditor();
    ModeManager modeManager = ce.getModeManager();
    Mode mode = (Mode)modeManager.top();
    mode.setArg("action", ru.novosoft.uml.behavior.common_behavior.MReturnActionImpl.class);
    return mgm.connect(cls, newCls, MLinkImpl.class);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

   

  public void mouseReleased(MouseEvent me) {
    if (_pressedButton < 10) return;
    Handle h = new Handle(-1);
    hitHandle(me.getX(), me.getY(), 0, 0, h);
    if (_pressedButton == h.index) {
            buttonClicked(_pressedButton);
    }
    _pressedButton = -1;
    Editor ce = Globals.curEditor();
    ce.damaged(this);
  }



} /* end class SelectionClass */
