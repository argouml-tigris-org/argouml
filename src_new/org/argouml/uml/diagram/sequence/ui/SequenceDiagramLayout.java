/** New Class to layout the sequence diagram
 *  needs more work: yet not all methods are restructured
 */

// file: ActionAddLink.java 
// author: 5kanzler@informatik.uni-hamburg.de

package org.argouml.uml.diagram.sequence.ui;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Collection;
import java.util.Enumeration;
import java.awt.Rectangle;
import java.awt.Color;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.FigDynPort;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphEvent;

import ru.novosoft.uml.behavior.common_behavior.MStimulus;
import ru.novosoft.uml.behavior.common_behavior.MLink;

public class SequenceDiagramLayout extends LayerPerspective {

  static final int MIN_GAP = 80;
  static final int OBJ_WIDTH = 0;
  static final int X1 = 10;
  static final int Y1 = 10;


  public SequenceDiagramLayout(String name, GraphModel gm) {
    super(name,gm);

  }


  public void nodeAdded(GraphEvent ge) {

    Object node = ge.getArg();
    Fig oldDE = presentationFor(node);
    // assumes each node can only appear once in a given layer
    if (null == oldDE) {
      if (!shouldShow(node)) { System.out.println("node rejected"); return; }
      FigNode newFigNode = _nodeRenderer.getFigNodeFor(_gm, this, node);
      if (newFigNode != null) {
	putInPosition(newFigNode);
	add(newFigNode);
        placeAllFigures();
      }
    }

  }

  public void edgeAdded(GraphEvent ge) {

    Object edge = ge.getArg();
    Fig oldFig = presentationFor(edge);
    if (null == oldFig) {
      if (!shouldShow(edge)) { System.out.println("edge rejected"); return; }
      FigEdge newFigEdge = _edgeRenderer.getFigEdgeFor(_gm, this, edge);
      if (newFigEdge != null) {
	add(newFigEdge);
	newFigEdge.computeRoute();
	newFigEdge.endTrans();
       
      }
    }
  }


  public Vector getFigSeqObjects() {
    Vector figSeqObjects=new Vector();
    for (int i=0; i<_contents.size(); i++) {
       if (_contents.elementAt(i) instanceof FigSeqObject) figSeqObjects.add( _contents.elementAt(i) );
    }
    return figSeqObjects;
  }


  /** return all instances of FigSeqLink in the active layer */
  public Vector getFigSeqLinks() {
    Vector figSeqLinks=new Vector();
    for (int i=0; i<_contents.size(); i++) {
      if (_contents.elementAt(i) instanceof FigSeqLink) figSeqLinks.add( _contents.elementAt(i) );        }
    return figSeqLinks;
  }

  public Vector getFigSeqStimuli() {

    Vector figSeqStimuli=new Vector();
    for (int i=0; i<_contents.size(); i++) {
       if (_contents.elementAt(i) instanceof FigSeqStimulus) figSeqStimuli.add( _contents.elementAt(i) );
    }
    return figSeqStimuli;

  }

 
  /** returns the maximal width overall stimuli between two objects  */

  public int getMaxStimulusWidth(FigSeqObject o1,FigSeqObject o2) {
    Vector figSeqStimuli = getFigSeqStimuli();
    int maxWidth=0;
    for  (int i=0; i< figSeqStimuli.size(); i++) {
      FigSeqStimulus fig = (FigSeqStimulus) figSeqStimuli.elementAt(i);
      MStimulus st= (MStimulus) fig.getOwner();
      if ( (st.getReceiver() == o1.getOwner() && st.getSender() == o2.getOwner() ) ||
           (st.getReceiver() == o2.getOwner() && st.getSender() == o1.getOwner() ) ) {
         if (fig.getBounds().width > maxWidth) maxWidth = fig.getBounds().width;
         // needs more work: include the width of end roles
      }
    }
    return maxWidth;
  }



   /** put the FigSeqObjects in place*/
  public void placeAllFigures() {
 
    Hashtable figDimensions=new Hashtable();
    int i,j,k,width,height,x1,x2;
    Rectangle rect;

    int x = X1;
    int y = Y1;
    FigSeqObject obj,objLeft,objRight;

    Vector figSeqObjects = getFigSeqObjects();
    Vector figSeqLinks = getFigSeqLinks();
    Vector figSeqStimuli = getFigSeqStimuli();

    int objSize = figSeqObjects.size();
    for  (i=0; i<objSize; i++) {
      obj= (FigSeqObject) figSeqObjects.elementAt(i);
      width = obj.getNameFig().getMinimumSize().width;
      height = obj.getNameFig().getMinimumSize().height;
      rect = new Rectangle(x,y,width,height);

      figDimensions.put(obj, rect);
      x = x + MIN_GAP + width;

    }

    for (i=0; i<objSize-1; i++) {
      objLeft= (FigSeqObject) figSeqObjects.elementAt(i);
      for  (j=i+1; j<objSize; j++) {
        // calculate distance between two objects
        objRight = (FigSeqObject) figSeqObjects.elementAt(j);
        // search for stimulus (+link) between o1 and o2 with the max. width
        int maxStimWidth= getMaxStimulusWidth(objLeft,objRight);

        // actual gap between the two objects
        x1 = ((Rectangle) figDimensions.get(objLeft)).x ;
        x2 = ((Rectangle) figDimensions.get(objRight)).x ;
        int objWidth =  ((Rectangle) figDimensions.get(objLeft)).width ;

        if ( maxStimWidth > (x2-(x1+objWidth)) ) {
          // name of stimulus is too long -> the gap between every object objLeft,...,objRight
          // has to be increased
          int gap = 25+(maxStimWidth-(x2-(x1+objWidth)))/(j-i);
          for  (k=i+1; k<objSize; k++) {
             obj = (FigSeqObject) figSeqObjects.elementAt(k);
             rect = (Rectangle) figDimensions.get(obj) ;
             x = rect.x+gap;
             y = rect.y;
             rect.setLocation(x,y);
          }
        }
      }
    }
    int linkSize = figSeqLinks.size();
    for (i=0; i<objSize; i++) {
      obj= (FigSeqObject) figSeqObjects.elementAt(i);
      rect = (Rectangle) figDimensions.get(obj) ;
      obj.startTrans();
      obj.setBounds(rect.x, rect.y, rect.width,rect.height, linkSize);
      obj.endTrans();
    }

    for (i=0; i<linkSize; i++) {
      FigSeqLink link = (FigSeqLink) figSeqLinks.elementAt(i);
      int portNumber = link.getPortNumber(_contents);
      FigSeqObject sourcePort = (FigSeqObject) link.getSourceFigNode();
      FigSeqObject destPort = (FigSeqObject) link.getDestFigNode();

      if (link.getSourcePortFig() == sourcePort._lifeline) {
        // new link
        for (j=0;j<figSeqObjects.size(); j++) {
          FigSeqObject fso = (FigSeqObject) figSeqObjects.elementAt(j);
          Enumeration e = fso._ports.elements();
          while (e.hasMoreElements()) {
            FigDynPort fsp = (FigDynPort) e.nextElement();
            int pos = fsp.getPosition();
            if (pos >= portNumber) {
              fsp.setPosition(pos+1);
              int dynPos = fsp.getDynVectorPos();
              fso._dynVector.removeElementAt(dynPos);
              String newDynStr = "b|"+fsp.getPosition();
              fso._dynVector.insertElementAt(newDynStr, dynPos);
              fso._dynObjects = fso._dynVector.toString();
            }
          }
          if (fso._terminated && fso._terminateHeight >= portNumber) fso._terminateHeight++;
          if (fso._created && fso._createHeight >= portNumber) {
            fso._createHeight++;
           
          }
          link.setActivations(fso, sourcePort, destPort, portNumber);
        }

        // add a new port to source object and connect the link to it
        FigDynPort _port1 = new FigDynPort(10, 10, 15, 5, Color.black, Color.white, portNumber);
        sourcePort._ports.addElement(_port1);
        sourcePort.addFig(_port1);
        sourcePort.bindPort(sourcePort.getOwner(), _port1);
        //System.out.println("SeqDiagramLay.playceAll..: setSourcePortFig->DynPort");
        link.setSourcePortFig(_port1);

        // the dynamic Vector has to be updated
        String dynStr = "b|"+_port1.getPosition();
        sourcePort._dynVector.addElement(dynStr);
        _port1.setDynVectorPos(sourcePort._dynVector.indexOf(dynStr));
        sourcePort._dynObjects = sourcePort._dynVector.toString();

        // Rectangle sPr = sourcePort.getBounds();
        //sourcePort.setBounds(sPr.x, sPr.y, sPr.width, sPr.height, linkSize);
      }
      if (link.getDestPortFig() == destPort._lifeline) {
        // new link -> add new port to destination object and connect the link to it

        FigDynPort _port2 = new FigDynPort(10, 10, 15, 5, Color.black, Color.white, portNumber);
        destPort._ports.addElement(_port2);
        destPort.addFig(_port2);
        destPort.bindPort(destPort.getOwner(), _port2);
        link.setDestPortFig(_port2);

        // update the dynVector
        String dynStr = "b|"+_port2.getPosition();
        destPort._dynVector.addElement(dynStr);
        _port2.setDynVectorPos(destPort._dynVector.indexOf(dynStr));
        destPort._dynObjects = destPort._dynVector.toString();

        //Rectangle dPr = destPort.getBounds();
        //destPort.setBounds(dPr.x, dPr.y, dPr.width, dPr.height, linkSize);
      }

      // after all objects are in place the stimuli has to be set in front of the layer
      // to keep it all selectable

      Collection col = ((MLink)link.getOwner()).getStimuli();
      MStimulus stimulus = null;
      Iterator it = col.iterator();

      while (it.hasNext()) {
        stimulus = (MStimulus) it.next();
      }
      if (stimulus != null && this.presentationFor(stimulus) != null) {
        this.bringToFront(this.presentationFor(stimulus) ); }
      }

      for (i=0; i<objSize; i++) {
        obj= (FigSeqObject) figSeqObjects.elementAt(i);
        rect = (Rectangle) figDimensions.get(obj) ;
        obj.startTrans();
        obj.setBounds(rect.x, rect.y, rect.width,rect.height, linkSize);
        obj.endTrans();
      }
      figDimensions=null;
    }

  }














