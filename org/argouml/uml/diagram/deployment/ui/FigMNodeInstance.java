

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

// File: FigMNodeInstance.java
// Classes: FigMNodeInstance
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

import org.argouml.application.api.Notation;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigCube;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/** Class to display graphics for a UML NodeInstance in a diagram. */

public class FigMNodeInstance extends FigNodeModelElement {

    ////////////////////////////////////////////////////////////////
    // instance variables

    protected FigCube _cover;
    protected FigRect _test;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigMNodeInstance() {
        _bigPort = new FigRect(10, 10, 200, 180);
        _cover = new FigCube(10, 10, 200, 180, Color.black, Color.white);
        _test = new FigRect(10, 10, 1, 1, Color.black, Color.white);

        _name.setLineWidth(0);
        _name.setFilled(false);
        _name.setJustification(0);
        _name.setUnderline(true);

        addFig(_bigPort);
        addFig(_cover);
        addFig(_stereo);
        addFig(_name);
        addFig(_test);

    }

    public FigMNodeInstance(GraphModel gm, Object node) {
        this();
        setOwner(node);
        if (org.argouml.model.ModelFacade.isAClassifier(node)
            && (org.argouml.model.ModelFacade.getName(node) != null))
            _name.setText(org.argouml.model.ModelFacade.getName(node));
    }

    public String placeString() {
        return "new NodeInstance";
    }

    public Object clone() {
        FigMNodeInstance figClone = (FigMNodeInstance) super.clone();
        Vector v = figClone.getFigs();
        figClone._bigPort = (FigRect) v.elementAt(0);
        figClone._cover = (FigCube) v.elementAt(1);
        figClone._stereo = (FigText) v.elementAt(2);
        figClone._name = (FigText) v.elementAt(3);
        figClone._test = (FigRect) v.elementAt(4);
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
        _test.setLineColor(c);
    }

    public Selection makeSelection() {
        return new SelectionNodeInstance(this);
    }

    public Dimension getMinimumSize() {
        Dimension stereoDim = _stereo.getMinimumSize();
        Dimension nameDim = _name.getMinimumSize();
        int w = Math.max(stereoDim.width, nameDim.width + 1) + 20;
        int h = stereoDim.height + nameDim.height + 20;
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
        _name.setBounds(x + 1, y + stereoDim.height + 1, w - 1, nameDim.height);
        _stereo.setBounds(x + 1, y + 1, w - 2, stereoDim.height);
        _x = x;
        _y = y;
        _w = w;
        _h = h;
        firePropChange("bounds", oldBounds, getBounds());
        updateEdges();
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

    ////////////////////////////////////////////////////////////////
    // user interaction methods

    public void mouseClicked(MouseEvent me) {
        super.mouseClicked(me);
        setLineColor(Color.black);
    }

    public void setEnclosingFig(Fig encloser) {
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
    }

    protected void textEdited(FigText ft) throws PropertyVetoException {
        // super.textEdited(ft);
        MNodeInstance noi = (MNodeInstance) getOwner();
        if (ft == _name) {
            String s = ft.getText().trim();
            // why ever...
            //       if (s.length()>0) {
            //         s = s.substring(0, (s.length() - 1));
            //      }
            ParserDisplay.SINGLETON.parseNodeInstance(noi, s);
        }
    }

    public boolean getUseTrapRect() {
        return true;
    }

    static final long serialVersionUID = 8822005566372687713L;

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        MNodeInstance noi = (MNodeInstance) getOwner();
        if (noi == null)
            return;
        String nameStr = "";
        if (noi.getName() != null) {
            nameStr = noi.getName().trim();
        }
        // construct bases string (comma separated)
        String baseStr = "";
        Collection col = noi.getClassifiers();
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

} /* end class FigMNodeInstance */