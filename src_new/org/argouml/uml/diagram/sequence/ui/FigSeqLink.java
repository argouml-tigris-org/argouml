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


package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.application.api.Notation;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorHelper;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.base.PathConvPercentPlusConst;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.presentation.*;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;

public class FigSeqLink extends FigEdgeModelElement implements MElementListener{

  // TODO: should be part of some preferences object
  public static boolean SUPPRESS_BIDIRECTIONAL_ARROWS = true;

  protected FigText _srcMult, _srcRole;
  protected FigText _destMult, _destRole;

  public FigSeqLink() {
    addPathItem(_name, new PathConvPercent(this, 50, 10));

    _srcMult = new FigText(10, 30, 90, 20);
    _srcMult.setFont(LABEL_FONT);
    _srcMult.setTextColor(Color.black);
    _srcMult.setTextFilled(false);
    _srcMult.setFilled(false);
    _srcMult.setLineWidth(0);
    addPathItem(_srcMult, new PathConvPercentPlusConst(this, 0, 15, 15));

    _srcRole = new FigText(10, 30, 90, 20);
    _srcRole.setFont(LABEL_FONT);
    _srcRole.setTextColor(Color.black);
    _srcRole.setTextFilled(false);
    _srcRole.setFilled(false);
    _srcRole.setLineWidth(0);
    addPathItem(_srcRole, new PathConvPercentPlusConst(this, 0, 35, -15));


    _destMult = new FigText(10, 30, 90, 20);
    _destMult.setFont(LABEL_FONT);
    _destMult.setTextColor(Color.black);
    _destMult.setTextFilled(false);
    _destMult.setFilled(false);
    _destMult.setLineWidth(0);
    addPathItem(_destMult, new PathConvPercentPlusConst(this, 100, -15, 15));

    _destRole = new FigText(10, 30, 90, 20);
    _destRole.setFont(LABEL_FONT);
    _destRole.setTextColor(Color.black);
    _destRole.setTextFilled(false);
    _destRole.setFilled(false);
    _destRole.setLineWidth(0);
    addPathItem(_destRole, new PathConvPercentPlusConst(this, 100, -35, -15));
    setBetweenNearestPoints(true);
  }

  public FigSeqLink(Object edge) {
    this();
    setOwner(edge);
  }




   public String ownerName() {
    if (getOwner() != null) { return ( (MLink)getOwner()).getName(); }
    else return "null";
  }

  public void setOwner(Object node) {
    super.setOwner(node);
  }

  protected void textEdited(FigText ft) throws PropertyVetoException {
    MLink ml = (MLink) getOwner();
    super.textEdited(ft);

    Collection conn = ml.getConnections();
    if (conn == null || conn.size() == 0) return;

    if (ft == _srcRole) {
      MLinkEnd srcLE = (MLinkEnd) ((Object[])conn.toArray())[0];
      srcLE.setName(_srcRole.getText());
    }
    if (ft == _destRole) {
      MLinkEnd destLE = (MLinkEnd)((Object[])conn.toArray())[1];
      destLE.setName(_destRole.getText());
    }
  }

  protected void modelChanged(MElementEvent e) {

    MLink ml = (MLink) getOwner();
    if (ml == null) return;

    String mlNameStr = Notation.generate(this, ml.getName());

    super.modelChanged(null);

    Vector contents = getContents();

    Collection ends = ml.getConnections();
    MLinkEnd le0 = (MLinkEnd) ((Object[])ends.toArray())[0];
    MLinkEnd le1 = (MLinkEnd) ((Object[])ends.toArray())[1];

    _srcRole.setText(Notation.generate(this, le0.getName()));
    _destRole.setText(Notation.generate(this, le1.getName()));

//    if (srcNav && destNav && SUPPRESS_BIDIRECTIONAL_ARROWS)
//      srcNav = destNav = false;

    Vector conns = new Vector(ml.getConnections());
    if (conns == null || conns.size() != 2) return;
    MLinkEnd endA = (MLinkEnd) conns.elementAt(0);
    MLinkEnd endB = (MLinkEnd) conns.elementAt(1);
    MObject moA = (MObject) endA.getInstance();
    MObject moB = (MObject) endB.getInstance();
   
    setArrowHeads(ml, contents);

    
  }


  /** Takes the action of this link and then
   *    set the aroow-heads on both ends of
   *    this FigSeqLink. */
  public void setArrowHeads(MLink ml, Vector contents) {

  if (ml != null ) {
    Collection col = ml.getStimuli();
    MStimulus stimulus = null;
    Object action = null;
    Iterator it = col.iterator();
    while (it.hasNext()) {
      stimulus = (MStimulus) it.next();    
      action = ModelFacade.getDispatchAction(stimulus);
   }


   if (action != null ) {

    FigSeqObject dest = (FigSeqObject) getDestFigNode();
    FigSeqObject source = (FigSeqObject) getSourceFigNode();
    if (dest != null && source != null) {
    if (action instanceof MCallAction) {
      if (ModelFacade.isAsynchronous(action)) {
        setDestArrowHead(new ArrowHeadHalfTriangle());
        setSourceArrowHead(new ArrowHeadNone());
      }
      else {
        setDestArrowHead(new ArrowHeadTriangle());
        setSourceArrowHead(new ArrowHeadNone());
      }
      setDashed(false);
      dest.setForCreate(this, "Dest", false);
      dest.setForDestroy(this, "Dest", false);
      //source.concatActivation(this, contents);
    }
    if (action instanceof MSendAction) {
      setDestArrowHead(new ArrowHeadHalfTriangle());
      setSourceArrowHead(new ArrowHeadNone());
      setDashed(false);
      dest.setForCreate(this, "Dest", false);
      dest.setForDestroy(this, "Dest", false);
      //source.concatActivation(this, contents);
    }
    if (action instanceof MCreateAction) {
      if (ModelFacade.isAsynchronous(action)) {
        setDestArrowHead(new ArrowHeadHalfTriangle());
        setSourceArrowHead(new ArrowHeadNone());
      }
      else {
        setDestArrowHead(new ArrowHeadTriangle());
        setSourceArrowHead(new ArrowHeadNone());
      }

      //setDestArrowHead(new ArrowHeadTriangle());
      //setSourceArrowHead(new ArrowHeadNone());
      setDashed(false);
      dest.setForCreate(this, "Dest", true);
      dest.setForDestroy(this, "Dest", false);
      //source.concatActivation(this, contents);
    }
    if (action instanceof MReturnAction) {
      setDestArrowHead(new ArrowHeadGreater());
      setSourceArrowHead(new ArrowHeadNone());
      setDashed(true);
      dest.setForCreate(this, "Dest", false);
      dest.setForDestroy(this, "Dest", false);
      //source.breakActivation(this, contents);
    }
    if (action instanceof MDestroyAction) {
      setDestArrowHead(new ArrowHeadGreater());
      //setDestArrowHead(new ArrowHeadX());
      setDashed(false);
      dest.setForCreate(this, "Dest", false);
      dest.setForDestroy(this, "Dest", true);
      //source.concatActivation(this, contents);
    }
    }
   }
  }
  }


/*
  

    public void setDestPortFig(Fig fig) {
        if (fig instanceof FigSeqObject) {
            FigSeqObject o = (FigSeqObject)fig;
            super.setDestPortFig(o._lifeline);
        } else
            super.setDestPortFig(fig);
    }

    public void setSourcePortFig(Fig fig) {
        if (fig instanceof FigSeqObject) {
            FigSeqObject o = (FigSeqObject)fig;
            super.setSourcePortFig(o._lifeline);
        } else
            super.setSourcePortFig(fig);        
    }
*/

  /** After a new link with a stimulus is added to the model and the figure of the link is created
   *  the figure of the stimulus has to be created and added to the layer
   */
  public void  addFigSeqStimulusWithAction() {
  
    LayerPerspective lay = (LayerPerspective)getLayer();
    GraphNodeRenderer renderer = lay.getGraphNodeRenderer();
    GraphModel gm = lay.getGraphModel();
    MLink link = (MLink) getOwner();
    if (link.getStimuli()!=null && link.getStimuli().size() > 0) {
      Collection stimuli = link.getStimuli();
      Iterator it = stimuli.iterator();
      MStimulus stimulus = null;
      while (it.hasNext()) {
        stimulus = (MStimulus) it.next();
      }
      if (lay.presentationFor(stimulus) == null ) {
        Point center = this.center();
        FigNode pers = renderer.getFigNodeFor(gm, lay, stimulus);
        int stiSize = stimuli.size();
        int percent = 35 + stiSize*10;
        if (percent > 100) percent = 100;
        this.addPathItem(pers, new PathConvPercent(this, percent, 10));
        this.updatePathItemLocations();
        lay.add(pers);
        setArrowHeads(link, lay.getContents() );
      }
    }
  }





  /** Sets the activations for every FigSeqObject in this diagram.
   *    Every object must update its activations, when a FigSeqLink
   *    is moved into position. */
  public void setActivations(FigSeqObject fso, FigSeqObject sourcePort, FigSeqObject destPort, int portNumber) {
      if (fso._activations != null && fso._activations.size() > 0) {
      int actSize = fso._activations.size();
      if ( (fso != sourcePort) && (fso != destPort) ) {

        for (int j=0; j<actSize; j++) {
          FigActivation act = (FigActivation) fso._activations.elementAt(j);
          if (act.getFromPosition() >= portNumber) {
            act.setFromPosition(act.getFromPosition()+1);
            act.setToPosition(act.getToPosition()+1);
          }
          else if (act.getFromPosition() < portNumber && act.getToPosition() >= portNumber) {
            act.setToPosition(act.getToPosition()+1);
          }
          else if (act.getToPosition() < portNumber) {
            // do nothing
          }
          int dynPos = act.getDynVectorPos();

          fso._dynVector.removeElementAt(dynPos);
          String newDynStr = "a|"+act.getFromPosition()+"|"+act.getToPosition()+"|"+act.isFromTheBeg()+"|"+act.isEnd();
          fso._dynVector.insertElementAt(newDynStr, dynPos);
          fso._dynObjects = fso._dynVector.toString();

        }
      }
      else if ( (fso == sourcePort) || (fso == destPort) ) {
        int max = 10000;
        int low = 0;
        int high = 0;
        FigActivation lowest = null;
        FigActivation highest = null;
        for (int k=0; k<actSize; k++) {
          FigActivation fa = (FigActivation) fso._activations.elementAt(k);
          int pos = fa.getFromPosition();
          if (pos < max && pos >= portNumber) {
            max = pos;
            low = pos;
            lowest = fa;
          }
          if (pos >= high) {
            high = pos;
            highest = fa;
          }
        }

        for (int j=0; j<actSize; j++) {
          FigActivation act = (FigActivation) fso._activations.elementAt(j);
          if ( (act.getFromPosition() >= portNumber) && (act.getToPosition() >= portNumber) ) {
            boolean hasOtherAct = false;
            for (int k=0; k<actSize; k++) {
              FigActivation fa = (FigActivation) fso._activations.elementAt(k);
              if (fa.getFromPosition() < portNumber && fa.getToPosition() >= portNumber) {
                hasOtherAct = true;
              }
            }
            if (hasOtherAct) {
              act.setFromPosition(act.getFromPosition()+1);
              act.setToPosition(act.getToPosition()+1);
            }
            else {
              if (act == lowest) {
                act.setFromPosition(portNumber);
                act.setToPosition(act.getToPosition()+1);
              }
              else {
                act.setFromPosition(act.getFromPosition()+1);
                act.setToPosition(act.getToPosition()+1);
              }
            }
          }
          else if ( (act.getFromPosition() < portNumber) && (act.getToPosition() >= portNumber) ) {
            act.setToPosition(act.getToPosition()+1);
          }
          else if ( (act.getFromPosition() < portNumber) && (act.getToPosition() < portNumber)) {
            if (act.isEnd()) {
              boolean hasOtherAct = false;
              for (int k=0; k<actSize; k++) {
                FigActivation fa = (FigActivation) fso._activations.elementAt(k);
                if (fa.getFromPosition() < portNumber && fa.getToPosition() >= portNumber) {
                  hasOtherAct = true;
                }
              }
              if (hasOtherAct) {
                // do nothing
              }
              else {
                if (highest == act) {
          	  FigActivation newAct = new FigActivation(0, 0, 21, 40, portNumber, portNumber);
        	  fso._activations.addElement(newAct);
        	  fso.addFig(newAct);
        	  fso.bindPort(fso.getOwner(), newAct);
                  String dynStr = "a|"+newAct.getFromPosition()+"|"+newAct.getToPosition()+"|"+newAct.isFromTheBeg()+"|"+newAct.isEnd();
                  fso._dynVector.addElement(dynStr);
                  newAct.setDynVectorPos(fso._dynVector.indexOf(dynStr));
                  fso._dynObjects = fso._dynVector.toString();

 	}
	else {
	  // do nothing
	}
              }
            }
            else {
              act.setToPosition(portNumber);
            }
          }
          int dynPos = act.getDynVectorPos();
          fso._dynVector.removeElementAt(dynPos);
          String newDynStr = "a|"+act.getFromPosition()+"|"+act.getToPosition()+"|"+act.isFromTheBeg()+"|"+act.isEnd();
          fso._dynVector.insertElementAt(newDynStr, dynPos);
          fso._dynObjects = fso._dynVector.toString();
        }
      }
    }
    else if (fso._activations != null && fso._activations.size() == 0 && (fso == sourcePort || fso == destPort)) {
      FigActivation newAct = new FigActivation(0, 0, 10, 40, portNumber, portNumber);
      fso._activations.addElement(newAct);
      fso.addFig(newAct);
      fso.bindPort(fso.getOwner(), newAct);
      String dynStr = "a|"+newAct.getFromPosition()+"|"+newAct.getToPosition()+"|"+newAct.isFromTheBeg()+"|"+newAct.isEnd();
      fso._dynVector.addElement(dynStr);
      newAct.setDynVectorPos(fso._dynVector.indexOf(dynStr));
      fso._dynObjects = fso._dynVector.toString();

    }
  }

  /** Returns the portNumber of this FigSeqLink.
   *    The first FigSeqLink in the diagram has the
   *    number one. */
  public int getPortNumber(Vector contents) {

    int portNumber = 0;
    FigSeqLink portObj = null;
    int size = contents.size();
    Rectangle rect = new Rectangle(0, 0, 1, 1);
    for (int i=0; i<size; i++) {
      if (contents.elementAt(i) instanceof FigSeqLink) {

        FigSeqLink fsl = (FigSeqLink) contents.elementAt(i);

        if (((fsl.getBounds()).y < getBounds().y) && ((fsl.getBounds()).y > rect.y)) {

          portObj = fsl;
          rect = fsl.getBounds();
        }
      }
    }
    if (portObj != null) {
     
      FigRect port = null;
      //FigSeqObject fso = (FigSeqObject) portObj.getSourceFigNode();
      if  (portObj.getSourcePortFig() != null && 
           portObj.getSourcePortFig() instanceof FigRect) {
        port = (FigRect) portObj.getSourcePortFig();
      }
      if (port != null && port instanceof FigDynPort) {
        FigDynPort fsp = (FigDynPort) port;
        portNumber = fsp.getPosition()+1;

      }
    }
    return portNumber;
  }

  /** Get the Vector of all figures, that are shown in
   *    the diagram, is important because in sequence-
   *    diagrams often you have to update all figures */
  public Vector getContents() {
    if (getLayer() != null ) {
      return getLayer().getContents();
    } else {
      Editor _editor = Globals.curEditor();
      Layer lay = _editor.getLayerManager().getActiveLayer();
      Vector contents = lay.getContents();
      return contents;
    }


  }

  /** Deletes all path-items of this FigSeqLink,
   *    returns all Owners of the deleted FigSeqStimuli. */

  public Vector deletePathItems() {
    Vector delOwners = new Vector();
    Vector figs = getPathItemFigs();
    for (int i=0; i<figs.size(); i++) {
      Fig figure = (Fig) figs.elementAt(i);
      if (figure instanceof FigSeqStimulus) {
        FigSeqStimulus fss = (FigSeqStimulus) figure;
        MStimulus ms = (MStimulus)fss.getOwner();
        delOwners.addElement(ms);
        fss.delete();
      }
    }
    return delOwners;
  }




  /** Removes the port-figs of this FigSeqLink and
   *    updates the Vector _ports of all FigSeqObjects */
  public void updatePorts(FigSeqObject sourceObj, FigSeqObject destObj,
			FigDynPort sourceFig, FigDynPort destFig, Vector contents, int size, int portNumber) {


    if (sourceFig == sourceObj._lifeline || destFig == destObj._lifeline) return;
     
    sourceObj._ports.remove(sourceFig);
    destObj._ports.remove(destFig);

    int sourceDynPos = sourceFig.getDynVectorPos();
    if (sourceObj._ports.size() == 0) {
      sourceObj._dynVector = new Vector();
    }
    else {
      sourceObj._dynVector.removeElementAt(sourceDynPos);
      for (int i=0; i<sourceObj._ports.size(); i++) {
        FigDynPort fsp = (FigDynPort) sourceObj._ports.elementAt(i);
        if (fsp.getDynVectorPos() > sourceDynPos) {
          fsp.setDynVectorPos(fsp.getDynVectorPos()-1);
        }
      }
      for (int i=0; i<sourceObj._activations.size(); i++) {
        FigActivation fa = (FigActivation) sourceObj._activations.elementAt(i);
        if (fa.getDynVectorPos() > sourceDynPos) {
          fa.setDynVectorPos(fa.getDynVectorPos()-1);
        }
      }
    }
    sourceObj._dynObjects = sourceObj._dynVector.toString();

    int destDynPos = destFig.getDynVectorPos();
    if (destObj._ports.size() == 0) {
      destObj._dynVector = new Vector();
    }
    else {
      destObj._dynVector.removeElementAt(destDynPos);
      for (int i=0; i<destObj._ports.size(); i++) {
        FigDynPort fsp = (FigDynPort) destObj._ports.elementAt(i);
        if (fsp.getDynVectorPos() > destDynPos) {
          fsp.setDynVectorPos(fsp.getDynVectorPos()-1);
        }
      }
      for (int i=0; i<destObj._activations.size(); i++) {
        FigActivation fa = (FigActivation) destObj._activations.elementAt(i);
        if (fa.getDynVectorPos() > destDynPos) {
          fa.setDynVectorPos(fa.getDynVectorPos()-1);
        }
      }
    }
    destObj._dynObjects = destObj._dynVector.toString();

    sourceObj.removeFig(sourceFig);
    destObj.removeFig(destFig);
    for (int i=0; i<size; i++) {
      if (contents.elementAt(i) instanceof FigSeqObject) {
        FigSeqObject fso = (FigSeqObject) contents.elementAt(i);
        if (fso != sourceObj && fso != destObj) {
          int portsSize = fso._ports.size();
          FigDynPort fsp = null;

          for (int j=portsSize; j>0; j--) {
            Enumeration e2 = fso._ports.elements();
            int key = 0;
            for (int k=0; k<j; k++) {
              fsp = (FigDynPort) e2.nextElement();
              key = fsp.getPosition();
            }

            if (key < portNumber) {
              // do nothing
            }
            else if (key > portNumber) {
              fsp.setPosition(key-1);
              int dynPos = fsp.getDynVectorPos();
              fso._dynVector.removeElementAt(dynPos);
              String newDynStr = "b|"+fsp.getPosition();
              fso._dynVector.insertElementAt(newDynStr, dynPos);
              fso._dynObjects = fso._dynVector.toString();
            }
          }
        }
        else {
          int portsSize = fso._ports.size();
          FigDynPort fsp = null;

          for (int j=portsSize; j>0; j--) {
            Enumeration e2 = fso._ports.elements();
            int key = 0;
            for (int k=0; k<j; k++) {
              fsp = (FigDynPort) e2.nextElement();
              key = fsp.getPosition();
            }

            if (key < portNumber) {
              // do nothing
            }
            else if (key > portNumber) {
              fsp.setPosition(key-1);
              int dynPos = fsp.getDynVectorPos();
              fso._dynVector.removeElementAt(dynPos);
              String newDynStr = "b|"+fsp.getPosition();
              fso._dynVector.insertElementAt(newDynStr, dynPos);
              fso._dynObjects = fso._dynVector.toString();
            }
          }
        }
      }
    }
  }

  /** Updates the Vector _activations of all FigSeqObjects */
  public void updateActivations(FigSeqObject sourceObj, FigSeqObject destObj,
			FigRect sourceFig, FigRect destFig, Vector contents, int size, int portNumber) {

    if (sourceFig == sourceObj._lifeline || destFig == destObj._lifeline) return;
   
    FigSeqObject fso = null;
    for (int i=0; i<getContents().size(); i++) {
      if (getContents().elementAt(i) instanceof FigSeqObject) {
        fso = (FigSeqObject) getContents().elementAt(i);
        if (fso._terminated && fso._terminateHeight > portNumber) fso._terminateHeight--;
        if (fso._created && fso._createHeight > portNumber) {
          fso._createHeight--;
         
        }


        if (fso != getSourceFigNode() && fso != getDestFigNode()) {
          for (int j=0; j<fso._activations.size(); j++) {
            FigActivation fa = (FigActivation) fso._activations.elementAt(j);
            if (fa.getFromPosition() > portNumber && fa.getToPosition() > portNumber) {
              fa.setFromPosition(fa.getFromPosition()-1);
              fa.setToPosition(fa.getToPosition()-1);
              int dynPos = fa.getDynVectorPos();
              fso._dynVector.removeElementAt(dynPos);
              String newDynStr = "a|"+fa.getFromPosition()+"|"+fa.getToPosition()+"|"+fa.isFromTheBeg()+"|"+fa.isEnd();
              fso._dynVector.insertElementAt(newDynStr, dynPos);
              fso._dynObjects = fso._dynVector.toString();
            }
            else if (fa.getFromPosition() <= portNumber && fa.getToPosition() >= portNumber) {
              fa.setToPosition(fa.getToPosition()-1);
              int dynPos = fa.getDynVectorPos();
              fso._dynVector.removeElementAt(dynPos);
              String newDynStr = "a|"+fa.getFromPosition()+"|"+fa.getToPosition()+"|"+fa.isFromTheBeg()+"|"+fa.isEnd();
              fso._dynVector.insertElementAt(newDynStr, dynPos);
              fso._dynObjects = fso._dynVector.toString();
            }
            else if (fa.getFromPosition() < portNumber && fa.getToPosition() < portNumber) {
              // do nothing
            }
          }
        }
        else if (fso == getSourceFigNode() || fso == getDestFigNode()) {
          Vector edges = fso.getFigEdges();
          Vector newActivations = (Vector) fso._activations.clone();
          for (int j=0; j<fso._activations.size(); j++) {
            FigActivation fa = (FigActivation) fso._activations.elementAt(j);
            int from = fa.getFromPosition();
            int to = fa.getToPosition();
            boolean figActDeleted = false;
            if (from < portNumber && to < portNumber) {
              // do nothing
            }
            else if (from > portNumber && to > portNumber) {
              fa.setFromPosition(from-1);
              fa.setToPosition(to-1);
            }
            else if (from < portNumber && to > portNumber) {
              fa.setToPosition(to-1);
            }
            else if (from == portNumber && to > portNumber) {
              fa.setToPosition(to-1);
              int nextNumber = 10000;
              for (int k=0; k<edges.size(); k++) {
                FigSeqLink fsl = (FigSeqLink) edges.elementAt(k);
                int fslNumber = fsl.getPortNumber(contents);
                if (fslNumber < nextNumber && fslNumber >= portNumber) {
                  nextNumber = fslNumber;
                }
              }
              fa.setFromPosition(nextNumber);
            }
            else if (from < portNumber && to == portNumber) {
              int nextNumber = 0;
              for (int k=0; k<edges.size(); k++) {
                FigSeqLink fsl = (FigSeqLink) edges.elementAt(k);
                int fslNumber = fsl.getPortNumber(contents);
                if (fslNumber > nextNumber && fslNumber < portNumber) {
                  nextNumber = fslNumber;
                }
              }
              fa.setToPosition(nextNumber);
            }
            else if (from == portNumber && to == portNumber) {
              figActDeleted = true;
              newActivations.removeElement(fa);
              fso.removeFig(fa);
            }
            if (!figActDeleted) {
              int dynPos = fa.getDynVectorPos();
              fso._dynVector.removeElementAt(dynPos);
              String newDynStr = "a|"+fa.getFromPosition()+"|"+fa.getToPosition()+"|"+fa.isFromTheBeg()+"|"+fa.isEnd();
              fso._dynVector.insertElementAt(newDynStr, dynPos);
              fso._dynObjects = fso._dynVector.toString();
            }
            else if (figActDeleted) {
              int dynPos = fa.getDynVectorPos();
              for (int l=0; l<fso._activations.size(); l++) {
                FigActivation figAct = (FigActivation) fso._activations.elementAt(l);
                if (figAct.getDynVectorPos() > dynPos) {
                  figAct.setDynVectorPos(figAct.getDynVectorPos()-1);
                }
              }
            }
          }
          fso._activations = newActivations;
          for (int j=0; j<edges.size(); j++) {
            FigSeqLink fsl = (FigSeqLink) edges.elementAt(j);
            fsl.modelChanged(null);
          }
        }
      }
    }
  }


  /** If the action of this Link cannot be set, this
   *   default action will be created and set */
  public void setDefaultAction() {

    MLink ml = (MLink) getOwner();
    Collection col = ml.getStimuli();
    Iterator it = col.iterator();
    while (it.hasNext()) {
      MStimulus ms = (MStimulus) it.next();
      Object ma = ModelFacade.getDispatchAction(ms);
      MNamespace ns = ms.getNamespace();
      Collection elements = ns.getOwnedElements();
      Iterator iterator = elements.iterator();
      while (iterator.hasNext()) {
        MModelElement moe = (MModelElement) iterator.next();
        if (moe instanceof MAction) {
          if (moe == ma) {
              ModelFacade.removeOwnedElement(ns, ma);
          }
        }
      }
      MCallAction mca = UmlFactory.getFactory().getCommonBehavior().createCallAction();
      mca.setAsynchronous(ModelFacade.isAsynchronous(ma));
      mca.setName(ModelFacade.getName(ma));
      ms.setDispatchAction(mca);
      ns.addOwnedElement(mca);
    }
  }
    
    public void dispose() {
        super.dispose();
        FigSeqObject sourceObj = (FigSeqObject) getSourceFigNode();
        FigSeqObject destObj = (FigSeqObject) getDestFigNode();
        FigDynPort sourceFig = (FigDynPort) getSourcePortFig();
        FigDynPort destFig = (FigDynPort) getDestPortFig();
    /*
    Vector contents = getContents();
    int size = contents.size();
    int portNumber = getPortNumber(contents);
    Vector delOwners = deletePathItems();

    MObject mo1 = (MObject)destObj.getOwner();
    MObject mo2 = (MObject)sourceObj.getOwner();
    if (mo1.getStimuli2() != null) {
      Collection stimuli = mo1.getStimuli2();
      Iterator it = stimuli.iterator();
      Vector delStimuli = new Vector();
      while (it.hasNext()) {
        MStimulus ms = (MStimulus) it.next();
        for (int i=0; i<delOwners.size(); i++) {
          MStimulus sti = (MStimulus)delOwners.elementAt(i);
          if (ms == sti) {
            delStimuli.addElement(ms);
            MAction action = ms.getDispatchAction();
            if (action != null ) {
              if (action instanceof MCreateAction) {
                destObj.setForCreate(this, "Dest", false);
              }

              // if stimulus is DestroyAction, the termination Symbol has to
              // be removed at the receiver
              if (action instanceof MDestroyAction) {
                destObj.setForDestroy(this, "Dest", false);
              }

              ((MNamespace)action.getNamespace()).removeOwnedElement(action);
            }
          }
        }
      }
      for (int i=0; i<delStimuli.size(); i++) {
        MStimulus ms = (MStimulus) delStimuli.elementAt(i);
        MNamespace ns = (MNamespace) ms.getNamespace();
        ns.removeOwnedElement(ms);
        mo1.removeStimulus2(ms);
        mo2.removeStimulus3(ms);
      }
    }
    */
    

    Vector contents = getContents();
    int size = contents.size();
    int portNumber = getPortNumber(contents);

    updatePorts(sourceObj, destObj, sourceFig, destFig, contents, size, portNumber);
    updateActivations(sourceObj, destObj, sourceFig, destFig, contents, size, portNumber);
    

  }
  

///////////////////////////////////////////////////////////////////////////////
// EventListener

  public void mouseClicked(MouseEvent me) {

    Vector contents = getContents();
    for (int i=0; i<contents.size(); i++) {
      if (contents.elementAt(i) instanceof FigSeqObject) {
        FigSeqObject fso = (FigSeqObject) contents.elementAt(i);
        fso.setEnclosingFig(fso);
      }
    }

    super.mouseClicked(me);
  }


  public void mouseReleased(MouseEvent me) {
    super.mouseReleased(me);
   
    addFigSeqStimulusWithAction();

    if (getLayer() != null && getLayer() instanceof SequenceDiagramLayout) {
    	((SequenceDiagramLayout)getLayer()).placeAllFigures();
    } else {
    	Diagram diagram = ProjectManager.getManager().getCurrentProject().getActiveDiagram();
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


  public void removed(MElementEvent mee) {

    super.removed(mee);

    Vector contents = getContents();
    int size = contents.size();
    for (int j=0; j<size; j++) {
      if (contents.elementAt(j) instanceof FigSeqObject) {
        FigSeqObject fso = (FigSeqObject) contents.elementAt(j);
        fso.setEnclosingFig(fso);

      }
      if (contents.elementAt(j) instanceof FigSeqLink) {
        FigSeqLink fsl = (FigSeqLink) contents.elementAt(j);
        MLink ml = (MLink) fsl.getOwner();
        if (ml != null) fsl.setArrowHeads(ml, contents);
      }
    }

  }


    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getDestination()
     */
    protected Object getDestination() {
        if (getOwner() != null) {
            return CommonBehaviorHelper.getHelper().getDestination((MLink)getOwner());
        }
        return null;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getSource()
     */
    protected Object getSource() {
        if (getOwner() != null) {
            return CommonBehaviorHelper.getHelper().getSource((MLink)getOwner());
        }
        return null;
    }

    /**
     * Hack to let the sequence diagrams still do their job. The sequence diagrams
     * do not update their model correctly and that's why the updating of the
     * classifiers goes wrong.
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateClassifiers()
     */
    protected boolean updateClassifiers() {
        return true;
    }

} /* end class FigSeqLink */

