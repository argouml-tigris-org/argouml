// $Id$
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

// File: FigComponentInstance.java
// Classes: FigComponentInstance
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.diagram.deployment.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Category;
import org.argouml.application.api.Notation;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/** Class to display graphics for a UML ComponentInstance in a diagram. */

public class FigComponentInstance extends FigNodeModelElement {
    protected static Category cat =
        Category.getInstance(FigComponentInstance.class);
    ////////////////////////////////////////////////////////////////
    // instance variables

    public static int OVERLAP = 4;

    protected FigRect _cover;
    protected FigRect _upperRect;
    protected FigRect _lowerRect;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigComponentInstance() {
        _cover = new FigRect(10, 10, 120, 80, Color.black, Color.white);
        _upperRect = new FigRect(0, 20, 20, 10, Color.black, Color.white);
        _lowerRect = new FigRect(0, 40, 20, 10, Color.black, Color.white);

        _name.setLineWidth(0);
        _name.setFilled(false);
        _name.setUnderline(true);

        addFig(_bigPort);
        addFig(_cover);
        addFig(_stereo);
        addFig(_name);
        addFig(_upperRect);
        addFig(_lowerRect);
    }

    public FigComponentInstance(GraphModel gm, Object node) {
        this();
        setOwner(node);
        if (org.argouml.model.ModelFacade.isAClassifier(node)
            && (org.argouml.model.ModelFacade.getName(node) != null))
            _name.setText(org.argouml.model.ModelFacade.getName(node));
        updateBounds();
    }

    public String placeString() {
        return "new ComponentInstance";
    }

    public Object clone() {
        FigComponentInstance figClone = (FigComponentInstance) super.clone();
        Vector v = figClone.getFigs();
        figClone._bigPort = (FigRect) v.elementAt(0);
        figClone._cover = (FigRect) v.elementAt(1);
        figClone._stereo = (FigText) v.elementAt(2);
        figClone._name = (FigText) v.elementAt(3);
        figClone._upperRect = (FigRect) v.elementAt(4);
        figClone._lowerRect = (FigRect) v.elementAt(5);

        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // acessors

    public void setLineColor(Color c) {
        //     super.setLineColor(c);
        _cover.setLineColor(c);
        _stereo.setFilled(false);
        _stereo.setLineWidth(0);
        _name.setFilled(false);
        _name.setLineWidth(0);
        _upperRect.setLineColor(c);
        _lowerRect.setLineColor(c);
    }

    public Selection makeSelection() {
        return new SelectionComponentInstance(this);
    }

    public Dimension getMinimumSize() {
        Dimension stereoDim = _stereo.getMinimumSize();
        Dimension nameDim = _name.getMinimumSize();

        int h = stereoDim.height + nameDim.height - OVERLAP;
        int w = Math.max(stereoDim.width, nameDim.width);
        return new Dimension(w, h);
    }

    public void setBounds(int x, int y, int w, int h) {
        if (_name == null)
            return;

        Rectangle oldBounds = getBounds();
        _bigPort.setBounds(x, y, w, h);
        _cover.setBounds(x, y, w, h);

        Dimension stereoDim = _stereo.getMinimumSize();
        Dimension nameDim = _name.getMinimumSize();

        if (h < 50) {
            _upperRect.setBounds(x - 10, y + h / 6, 20, 10);
            _lowerRect.setBounds(x - 10, y + 3 * h / 6, 20, 10);
        } else {
            _upperRect.setBounds(x - 10, y + 13, 20, 10);
            _lowerRect.setBounds(x - 10, y + 39, 20, 10);
        }

        _stereo.setBounds(x + 1, y + 1, w - 2, stereoDim.height);
        _name.setBounds(
			x + 1,
			y + stereoDim.height - OVERLAP + 1,
			w - 2,
			nameDim.height);
        _x = x;
        _y = y;
        _w = w;
        _h = h;
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
    }

    ////////////////////////////////////////////////////////////////
    // user interaction methods

    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        setLineColor(Color.black);
    }

    public void mousePressed(MouseEvent me) {
        super.mousePressed(me);
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionComponentInstance)
	    ((SelectionComponentInstance) sel).hideButtons();
    }

    public void setEnclosingFig(Fig encloser) {

        if (encloser != null
            && org.argouml.model.ModelFacade.isANodeInstance(encloser.getOwner())
            && getOwner() != null) {
            MNodeInstance node = (MNodeInstance) encloser.getOwner();
            MComponentInstance comp = (MComponentInstance) getOwner();
            if (comp.getNodeInstance() != node) {
                comp.setNodeInstance(node);
            }
            super.setEnclosingFig(encloser);

            Vector figures = getEnclosedFigs();

            if (getLayer() != null) {
                // elementOrdering(figures);
                Vector contents = getLayer().getContents();
                int contentsSize = contents.size();
                for (int j = 0; j < contentsSize; j++) {
                    Object o = contents.elementAt(j);
                    if (o instanceof FigEdgeModelElement) {
                        FigEdgeModelElement figedge = (FigEdgeModelElement) o;
                        figedge.getLayer().bringToFront(figedge);
                    }
                }
            }
        } else if (encloser == null && getEnclosingFig() != null) {
            if (getEnclosingFig() instanceof FigNodeModelElement)
                ((FigNodeModelElement) getEnclosingFig())
                    .getEnclosedFigs()
                    .removeElement(
				   this);
            _encloser = null;
        }
        /*
	  super.setEnclosingFig(encloser);
        
	  Vector figures = getEnclosedFigs();
        
	  if (getLayer() != null) {
          // elementOrdering(figures);
          Vector contents = getLayer().getContents();
          int contentsSize = contents.size();
          for (int j=0; j<contentsSize; j++) {
	  Object o = contents.elementAt(j);
	  if (o instanceof FigEdgeModelElement) {
	  FigEdgeModelElement figedge = (FigEdgeModelElement) o;
	  figedge.getLayer().bringToFront(figedge);
	  }
          }
	  }
        
	  if (!(getOwner() instanceof MModelElement)) return;
	  if (getOwner() instanceof MComponentInstance) {
          MComponentInstance me = (MComponentInstance) getOwner();
          MNodeInstance mnode = null;
        
          if (encloser != null
	  && (encloser.getOwner() instanceof MNodeInstance)) {
	  mnode = (MNodeInstance) encloser.getOwner();
          }
          if (encloser != null
	  && (encloser.getOwner() instanceof MComponentInstance)) {
	  MComponentInstance comp = (MComponentInstance) encloser.getOwner();
	  mnode = (MNodeInstance) comp.getNodeInstance();
          }
          try {
	  if(mnode != null) {
	  me.setNodeInstance(mnode);
	  }
	  else {
	  if (me.getNodeInstance() != null) {
	  me.setNodeInstance(null);
	  }
	  }
	  setNode(figures);
          }
          catch (Exception e) {
	  cat.error("could not set package", e);
           
          }
	  }
        */
    }

    public void setNode(Vector figures) {
        int size = figures.size();
        if (figures != null && (size > 0)) {
            for (int i = 0; i < size; i++) {
                Object o = figures.elementAt(i);
                if (o instanceof FigComponentInstance) {
                    FigComponentInstance figcomp = (FigComponentInstance) o;
                    figcomp.setEnclosingFig(this);
                }
            }
        }
    }

    public boolean getUseTrapRect() {
        return true;
    }

    ////////////////////////////////////////////////////////////////
    // internal methods

    protected void textEdited(FigText ft) throws PropertyVetoException {
        //super.textEdited(ft);
        MComponentInstance coi = (MComponentInstance) getOwner();
        if (ft == _name) {
            String s = ft.getText().trim();
            //why this???
            //       if (s.length()>0) {
            //         s = s.substring(0, (s.length() - 1));
            //       }
            ParserDisplay.SINGLETON.parseComponentInstance(coi, s);
        }
    }

    protected void updateStereotypeText() {
        MModelElement me = (MModelElement) getOwner();
        if (me == null)
            return;
        MStereotype stereo = me.getStereotype();
        if (stereo == null
            || stereo.getName() == null
            || stereo.getName().length() == 0)
            _stereo.setText("");
        else {
            _stereo.setText(Notation.generateStereotype(this, stereo));
        }
    }

    static final long serialVersionUID = 1647392857462847651L;

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        MComponentInstance coi = (MComponentInstance) getOwner();
        if (coi == null)
            return;
        String nameStr = "";
        if (coi.getName() != null) {
            nameStr = coi.getName().trim();
        }

        // construct bases string (comma separated)
        String baseStr = "";
        Collection col = coi.getClassifiers();
        if (col != null && col.size() > 0) {
            Iterator it = col.iterator();
            baseStr = org.argouml.model.ModelFacade.getName(it.next());
            while (it.hasNext()) {
                baseStr += ", " + org.argouml.model.ModelFacade.getName(it.next());
            }
        }
        if (_readyToEdit) {
            if (nameStr == "" && baseStr == "")
                _name.setText("");
            else
                _name.setText(nameStr.trim() + " : " + baseStr);
        }
        Dimension nameMin = _name.getMinimumSize();
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

} /* end class FigComponentInstance */