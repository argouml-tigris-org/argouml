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

// File: FigSeqStimulus.java
// Original Author: agauthie@ics.uci.edu
// $Id$


package org.argouml.uml.diagram.sequence.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Enumeration;
import java.beans.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.GraphEdgeRenderer;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.base.Selection;

import org.argouml.application.api.*;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.ui.FigNodeModelElement;



/** Class to display graphics for a UML collaboration in a diagram. */

public class FigSeqStimulus extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constants
  public int PADDING = 5;
  public static Vector ARROW_DIRECTIONS = new Vector();

  ////////////////////////////////////////////////////////////////
  // instance variables

  //protected Polygon _polygon;

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigSeqStimulus() {
    _name.setLineWidth(0);
    _name.setFilled(false);
    Dimension nameMin = _name.getMinimumSize();
    _name.setBounds(10, 10, 90, nameMin.height);

    // add Figs to the FigNode in back-to-front order
    addFig(_name);

    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  public FigSeqStimulus(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { return "new SequenceStimulus"; }

  public Object clone() {
    FigSeqStimulus figClone = (FigSeqStimulus) super.clone();
    Vector v = figClone.getFigs();
    figClone._name = (FigText) v.elementAt(0);
    return figClone;
  }



  ////////////////////////////////////////////////////////////////
  // Fig accessors

  public void setLineColor(Color col) {
    _name.setLineColor(col);
  }

  public void setFillColor(Color col) {
    _name.setFillColor(col);
  }

  public void setFilled(boolean f) {  }
  public boolean getFilled() { return true; }

  public Dimension getMinimumSize() {
    Dimension nameMin = _name.getMinimumSize();

    int h = nameMin.height;
    int w = nameMin.width;
    return new Dimension(w, h);
  }

  /* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;

    Rectangle oldBounds = getBounds();

    Dimension nameMin = _name.getMinimumSize();

    int ht = 0;

    _name.setBounds(x, y, nameMin.width, nameMin.height);


    firePropChange("bounds", oldBounds, getBounds());
    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    updateEdges();

  }

  protected void textEdited(FigText ft) throws PropertyVetoException {
    super.textEdited(ft);
    MStimulus sti = (MStimulus) getOwner();
    if (ft == _name) {
       String s = ft.getText();
       ParserDisplay.SINGLETON.parseStimulus(sti, s);
    }
  }

  public String ownerName() {
    if (getOwner() != null) { return ( (MStimulus)getOwner()).getName(); }
    else return "null";
  }

  protected void modelChanged(MElementEvent mee) {
   

    super.modelChanged(mee);

    MStimulus sti = (MStimulus) getOwner();
    if (sti == null) return;

    String nameStr = Notation.generate(this, sti.getName()).trim();
	String actionString = "new Action";

	if (sti.getDispatchAction() != null && sti.getDispatchAction().getName() != null)
		actionString = Notation.generate(this, ((MAction)sti.getDispatchAction()).getName()).trim();

    if( nameStr.equals("") && actionString.equals("") )
      _name.setText("");
    else
      _name.setText(nameStr.trim() + " : " + actionString.trim());

    if (sti.getCommunicationLink() != null) {
      MLink link = (MLink) sti.getCommunicationLink();
      if (link.getName() != null) {
        link.setName(link.getName());
      }
      else link.setName("");
    }

    if (sti.getSender() != null) {
      MInstance inst = (MInstance) sti.getSender();
      if (inst.getName() != null) {
        inst.setName(inst.getName());
      }
      else inst.setName("");
    }
    if (sti.getReceiver() != null) {
      MInstance inst = (MInstance) sti.getReceiver();
      if (inst.getName() != null) {
        inst.setName(inst.getName());
      }
      else inst.setName("");
    }

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
   
    /*
  public void delete() {
    // a stimulus can only be removed from the link
    Editor ce = Globals.curEditor();
    Layer lay = ce.getLayerManager().getActiveLayer();
    SelectionManager sm = ce.getSelectionManager();
    Vector figs = sm.selections();
    Selection cf = (Selection) figs.firstElement();
    if (  cf.getContent() instanceof FigSeqLink ) super.delete();

  }
   */

  public void addPathItemToLink(Layer lay) {

    // 15/09/00 AK
    // the new stimulus becomes a pathItem of its link
   
    MLink mlink = ((MStimulus) getOwner()).getCommunicationLink();

    if (mlink != null && lay != null) {
      
      FigSeqLink figSeqLink = (FigSeqLink) lay.presentationFor(mlink);
      if (figSeqLink != null) {
        
        Collection stimuli = mlink.getStimuli();
        int size=0;
        if (stimuli != null) {
          size = stimuli.size();
        }
        int percent = 15 + size*10;
        if (percent > 100) percent = 100;
        figSeqLink.addPathItem(this, new PathConvPercent(figSeqLink, percent, 10));
        figSeqLink.updatePathItemLocations();
        lay.bringToFront(this);
      }
    }
  }

  /** if you move a FigSeqObject around and place it onto  a FigSeqStimulus
     not the FigSeqObject gets the mouseReleased event but the FigSeqStimulus.
     For this case, the diagram has to be replaced, too. */
  public void mouseReleased(MouseEvent me) {
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
  }

    /**
     * @see org.tigris.gef.presentation.Fig#dispose()
     */
    public void dispose() {
        super.dispose();
    }

} /* end class FigSeqStimulus */
