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

/** Class to display graphics for a UML sequence in a diagram. */

public class FigSeqClRole extends FigNodeModelElement implements MouseMotionListener, MElementListener, PropertyChangeListener, FigSeqConstants {

    ////////////////////////////////////////////////////////////////
    // constants

    ////////////////////////////////////////////////////////////////
    // instance variables

    FigSeqClRoleObject _figObject;

    private FigSeqClRoleLifeline _figLifeline;
    public boolean _created = false;
    boolean _terminated = false;
    public int _createHeight = 0;
    int _terminateHeight = 0;
    int _terminatePortsSize = 0;
    public Vector _dynVector;
    protected int _create = -1;
    // add other Figs here as needed

    // termination symbol consisting of two lines
    FigLine _terminateLine1;
    FigLine _terminateLine2;

    // actual mouse position , needed for dynamically placed  rapid buttons
    int _yPos = 50;


    ////////////////////////////////////////////////////////////////
    // constructors

    public FigSeqClRole() {
        _name.setLineWidth(0);
        _name.setFilled(false);
        _name.setUnderline(true);
        Dimension nameMin = getMinimumSize();
        _name.setSize(nameMin.width, nameMin.height);
        _name.addPropertyChangeListener(this);

        _figObject   = new FigSeqClRoleObject(this);
        _figObject.setBounds(_name.getBounds());
//        _figObject.addPropertyChangeListener(this);
        _figLifeline = new FigSeqClRoleLifeline(this);
//        _figLifeline.addPropertyChangeListener(this);

        _dynObjects = "0";
        _dynVector = new Vector();

        // add Figs to the FigNode in back-to-front order
        // bigPort,cover,name,lifeline
        _bigPort.setBounds(_figObject.getBounds());
        _name.setBounds(_figObject.getBounds());
        addFig(_bigPort);
        addFig(_figObject);
        addFig(_name);
        addFig(_figLifeline);
//        updateFigBounds();
        _yPos = _figLifeline.getBounds().y + _figLifeline.getBounds().height / 2;
        this.damage();
    }

    public FigSeqClRole(Object node) {
        this();
        setOwner(node);
    }

    public void setOwner(Object node) {
        super.setOwner(node);
        bindPort(node, _figLifeline);
        if (node != null && node instanceof MClassifierRole) {
            MClassifierRole clsfr = (MClassifierRole)node;
            String  text  = clsfr.getName() + " : ";
            boolean first = true;
            Iterator itr  = clsfr.getBases().iterator();
            while (itr.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    text += ", ";
                }
                text += ((MClassifier)itr.next()).getName();
            }
            _name.setText(text);
        }
    }

    public Selection makeSelection() {
        return new SelectionSeqClRole(this);
    }

    public void translate(int dx, int dy) {
System.out.println("FigSeqClRole.translate");
        super.translate(dx, dy);
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionSeqClRole)
                ((SelectionSeqClRole)sel).hideButtons();
    }

    public String ownerName() {
        if (getOwner() != null) { return ( (MClassifierRole)getOwner()).getName(); }
        else return "null";
    }

    /**
    * If the object is terminated, the termination symbol will appear as  a cross.
    * If not, the termination symbol is hidden.
    */
    public void terminateSymbolSetBounds( int x1, int y1, int x2, int y2) {
        System.out.println("Called FigSeqRole.terminateSymbolSetBounds");
    }


    public String placeString() {
        return "new MClassifierRole";
    }

    public Object clone() {
        FigSeqClRole figClone = (FigSeqClRole)super.clone();
        Vector v = figClone.getFigs();
        figClone._name = (FigText) v.elementAt(2);
        figClone._figObject   = (FigSeqClRoleObject)_figObject.clone();
        figClone._figLifeline = (FigSeqClRoleLifeline)_figLifeline.clone();
        // clone termination symbol
        figClone._terminateLine1 = (FigLine) v.elementAt(4);
        figClone._terminateLine2 = (FigLine) v.elementAt(5);
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    public void parseDynObjects(String dynobjs) {
        _dynObjects = dynobjs;
    }

    public FigSeqClRoleObject getFigObject() {
        return _figObject;
    }

    public Dimension getNameSize() {
        return _name.getMinimumSize();
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
     * ???
     */
    public int getObjectCreationIndex() {
        return _figLifeline.getObjectCreationIndex();
    }

    /**
     * Creates a message connection to the lifeline
     */
    public FigSeqClRolePort getPortFig(int idxLine, int idxThread, int idxLevel) {
        System.out.print("FigSeqClRole.getPortFig: ");
        FigSeqClRolePort fp = _figLifeline.getPortFig(idxLine, idxThread, idxLevel);
        System.out.println("Returning FigSeqClRolePort: " + fp);
        return fp;
    }

    /**
     * Creates a message connection to the lifeline
     */
    public FigSeqClRolePort getNewPortFig(int idxConn, int idxThread, int idxLevel) {
        System.out.println("FigSeqClRole.getNewPortFig at connection " + idxConn);
        FigSeqClRolePort fp = _figLifeline.getNewPortFig(idxConn, idxThread, idxLevel);
        System.out.println("Returning FigSeqClRolePort: " + fp);
        return fp;
    }



    /** Sets the port (some object in an underlying model) for Fig f.  f
     *  must already be contained in the FigNode. f will now represent
     *  the given port. Overrides the implementation of FigNode.
     */
    public void bindPort(Object port, Fig f) {
        Fig oldPortFig = getPortFig(port);
        //if (oldPortFig != null) oldPortFig.setOwner(null);
        f.setOwner(port);
    }

    /** Reply the port that "owns" the topmost Fig under the given point, or
     *  null if none. 
     *  Overrides the implementation of FigNode.
     *  If the hit port has an owner, hitPort() returns that owner object. 
     *  The mode manageer tries to create an arc, even in the select mode.
     *  Because the user shall select the FigSeqClRole with its lifeline, 
     *  which is binded to the underlying object,  a special handling is needed 
     *  to make it selectable.
     */
//    public Object hitPort(int x, int y) {
//        Fig f = hitFig(new Rectangle(x, y, 1, 1));
//        if (Globals.mode() instanceof ModeSelect )   {
//            /* if we are in the select mode  and the hit fig is tho object's lifeline, 
//            *  return null, so the object can be selected 
//            */ 
//            return null;
//        }
//        if (f != null) return f.getOwner();
//        else return null;
//    }

    public Fig hitPort(Rectangle r) {
        Fig f = _figObject.getBounds().contains(r) ? _figObject : _figLifeline.hitPort(r);
        return (f == null) ? this : f;
    }

    public Dimension getMinimumSize() {
        Dimension objMin  = new Dimension(OBJECT_DEFAULT_WIDTH, OBJECT_NAME_FILL_Y * 2);
        Dimension nameMin = _name.getMinimumSize();
        int h = Math.max(objMin.height, nameMin.height + OBJECT_NAME_FILL_Y * 2);
        int w = Math.max(objMin.width,  nameMin.width  + OBJECT_NAME_FILL_X * 2);
        return new Dimension(w, h);
    }

    public boolean isResizable() {
        return false;
    }

  ////////////////////////////////////////////////////////////////
  // event handlers

  protected void textEdited(FigText ft) throws PropertyVetoException {
    // super.textEdited(ft);
    MObject obj = (MObject) getOwner();
    if (ft == _name) {
       String s = ft.getText();

      // needs more Work:
      ParserDisplay.SINGLETON.parseObject(obj, s);
    }
  }


    protected void modelChanged(MElementEvent mee) {
        if (!(getOwner() instanceof MClassifierRole)) return;  // HACK
        super.modelChanged(mee);
        MClassifierRole clsfr = (MClassifierRole)getOwner();
        if (clsfr == null) return;
        String nameStr = Notation.generate(this, clsfr.getName()).trim();
        String baseString = "";
        Vector bases = new Vector(clsfr.getBases());
        if (bases.size() == 1)
                baseString = ((MClassifier)bases.elementAt(0)).getName();
        else if (bases.size() > 1)
                baseString = "(multiple)";
        if (_readyToEdit) {
            if( nameStr == "" && baseString == "")
                    _name.setText("");
            else
                    _name.setText(nameStr.trim() + " : " + baseString);
        }
        setEnclosingFig(this);
        if (getLayer() != null && getLayer() instanceof SequenceDiagramLayout) {
            // TODO: must be re-activated after correcting some errors on displaying messages
//            ((SequenceDiagramLayout)getLayer()).placeAllFigures();
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




  /** This method is called, when the FigSeqClRole is
   *    moving around. Changes the position of the
   *    FigSeqClRoles. */
  public void changePosition(Vector contents) {

    int size = contents.size();
    for (int k=0; k<size; k++) {
      if (contents.elementAt(k) instanceof FigSeqClRole) {
        FigSeqClRole figure = (FigSeqClRole) contents.elementAt(k);
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

  /** If the FigSeqAsRole linkFig is connected to a create-action
   *    this FigSeqClRole will be updated with the right values */
  public void setForCreate(FigSeqAsRole linkFig, String connectTo, boolean isCreate) {
    Vector contents = getContents();   
    int portNumber = linkFig.getPortNumber(contents);   
    FigDynPort fsp = (FigDynPort) linkFig.getSourcePortFig();
    if (connectTo == "Dest") {
      fsp = (FigDynPort) linkFig.getDestPortFig();
    }
   
    int firstPos = 10000;
    Vector edges = getFigEdges();
    for (int i=0; i<edges.size(); i++) {
      FigSeqAsRole fsl = (FigSeqAsRole) edges.elementAt(i);
      int fslNumber = fsl.getPortNumber(contents);
      if (fslNumber < firstPos) { firstPos = fslNumber; }
    }
    if (isCreate == true) {
      if (firstPos == portNumber) {
        _created = isCreate;
        _createHeight = firstPos;

      }
      else {
        linkFig.setDefaultAction();
      }
    }
    else if (isCreate == false) {
      if (firstPos == portNumber) {
        _created = isCreate;
        _createHeight = 0;
      }
    }
  }

  /** If the FigSeqAsRole linkFig is connected to a destroy-action
   *    this FigSeqClRole will be updated with the right values */
  public void setForDestroy(FigSeqAsRole linkFig, String connectTo, boolean isTerminate) {
    Vector contents = getContents();
    int portNumber = linkFig.getPortNumber(contents);   
    FigDynPort fsp = (FigDynPort) linkFig.getSourcePortFig();
    if (connectTo == "Dest") fsp = (FigDynPort) linkFig.getDestPortFig();
    int lastPos = 0;
    Vector edges = getFigEdges();

    for (int i=0; i<edges.size(); i++) {
      FigSeqAsRole fsl = (FigSeqAsRole) edges.elementAt(i);
      int fslNumber = fsl.getPortNumber(contents);

      if (fslNumber > lastPos) { lastPos = fslNumber; }
    }
    if (isTerminate == true) {

      if (lastPos == portNumber) {

        _terminated = isTerminate;
        _terminateHeight = lastPos;
      }
      else {
        linkFig.setDefaultAction();
      }

    }
    else if (isTerminate == false) {
      if (lastPos == portNumber) {
        _terminated = isTerminate;
        _terminateHeight = 0;
      }
    }
  }

  /** If the connected action of the FigSeqAsRole figLink is
   *    a return-action, the corresponding FigActivation will
   *    be cut in two FigActivations
   */
  public void breakActivation(FigSeqAsRole figLink, Vector contents) {
  }

  /** This method decides, if two FigActivations can be
   *    replaced with the first of this two FigActivations with
   *    the added lenght of both FigActivations */
  public void concatActivation(FigSeqAsRole figLink, Vector contents) {
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

  /** If the FigSeqAsRole fsl has a return- or destroy-action,
   *    this method decides, if this link can have this action
   *    --> returns true */
  public boolean canDo(boolean side, FigSeqAsRole fsl, int portNumber) {
    return true;
  }

  /** Returns true, if  two FigActivations at the given
   *    port-number can be replaced by one FigActivation */
  public FigActivation canConcat(int portNumber) {
    return null;
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
        System.out.println("~~~FigSeqRole.mousePressed(1): _y = " + _y);
        super.mousePressed(me);
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionSeqClRole) {
            ((SelectionSeqClRole)sel).hideButtons();
        }
        System.out.println("~~~FigSeqRole.mousePressed(2): _y = " + _y);
    }
  
    public void mouseClicked(MouseEvent me) {
        /* by clicking on the object's lifeline or activation bar, the rapid buttons are painted
        *  at this position */
        System.out.println("~~~FigSeqRole.mouseClicked(1): _y = " + _y);
        Fig f = hitFig( new Rectangle(me.getX(),me.getY(),1,1) );
        if ( (f == _figLifeline ||  f instanceof FigSeqClRoleActivation) && (me.getY() != _yPos)) {
            _yPos = me.getY();
            Editor ce = Globals.curEditor();
            SelectionManager selManager = ce.getSelectionManager();
            Selection sel = selManager.findSelectionFor(this);
            if (sel instanceof SelectionSeqClRole) {
                selManager.select(this);
            }
        }
        super.mouseClicked(me);   
        System.out.println("~~~FigSeqRole.mouseClicked(1): _y = " + _y);
    }

    public void mouseReleased(MouseEvent me) {
        System.out.println("~~~FigSeqRole.mouseReleased(1): _y = " + _y);
        super.mouseReleased(me);
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
        System.out.println("~~~FigSeqRole.mouseReleased(9): _y = " + _y);
    }

    ///////////////////////////////////////////////////////////////////////////////
    // MouseMotionListener-implementation

    public void mouseDragged(MouseEvent me) {
        changePosition(getContents());
    }

    public void mouseMoved(MouseEvent me) {
        Rectangle r = getBounds();
        if (me.getX() >= r.x &&
            me.getX() < (r.x + r.width) &&
            me.getY() >= r.y &&
            me.getY() < (r.y + r.height)) {
            // Show ports
            _figLifeline.setPortsDisplayed(true);
        } else {
            // Hide ports
            _figLifeline.setPortsDisplayed(false);
        }
    }

    public void mouseEntered(MouseEvent me) {
        Rectangle r = getBounds();
        if (me.getX() >= r.x &&
            me.getX() < (r.x + r.width) &&
            me.getY() >= r.y &&
            me.getY() < (r.y + r.height)) {
            // Show ports
            _figLifeline.setPortsDisplayed(true);
        } else {
            // Hide ports
            _figLifeline.setPortsDisplayed(false);
        }
        super.mouseEntered(me);
    }

    public void mouseExited(MouseEvent me) {
        // Hide ports
        _figLifeline.setPortsDisplayed(false);
        super.mouseExited(me);
    }

  public String toString() {
      return this.getClass().getName() + "[Text=" + this.getNameFig().getText() + "]";
  }

    public void propertyChange(PropertyChangeEvent pce) {
//        System.out.println("Property of " + pce.getSource() + " changed.");
        Fig oSrc = (Fig)pce.getSource();
        if (oSrc == _name && pce.getPropertyName().equals("editing")) {
            Dimension nameMin = getMinimumSize();
            _name.setSize(nameMin.width, nameMin.height);
            _figObject.setBounds(_name.getBounds());
            _bigPort.setBounds(_figObject.getBounds());
            _figLifeline.relocate();
            calcBounds();
            ((SequenceDiagramLayout)this.getLayer()).placeAllFigures();
        }
    }

    FigSeqClRoleLifeline getLifeline() {
        return _figLifeline;
    }

    /**
     * This method sets the creation line index. The line index is a counting number,
     * starting from the first port on any lifeline of an object, which is existing,
     * not created by any message in the diagram. An index less than 1 means an
     * existing object, any other index means that this object is created by a message
     * in the given line.
     * @param idx The creation line index.
     */
    public void setCreationLineIndex(int idx) {
        setY(_figLifeline.getPortModel().getObjectCreationY(idx));
        _figLifeline.getPortModel().setCreationLineIndex(idx);
    }

    public void relocate() {
        setY(_figLifeline.getPortModel().getObjectCreationY(getCreationLineIndex()));
    }

    /** This method returns the creation line index.
     * @see setCreationLineIndex
     * @return The creation line index.
     */
    public int getCreationLineIndex() {
        return _figLifeline.getPortModel().getCreationLineIndex();
    }


    public void setActiveState(boolean a) {
        _figLifeline.getPortModel().setActiveState(a);
    }

//    public void mouseClicked(MouseEvent me) {
//    }
} /* end class FigSeqClRole */
