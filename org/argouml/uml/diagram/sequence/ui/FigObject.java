// Copyright (c) 1996-2003 The Regents of the University of California. All
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

// $Id$
package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;

/**
 * Fig to show an object on a sequence diagram
 * @author jaap.branderhorst@xs4all.nl
 * Aug 11, 2003
 */
public class FigObject extends FigNodeModelElement implements MouseListener {

    /**
     * The margin between the outer box and the name and stereotype text box.
     */
    public final static int MARGIN = 10;

    /**
     * The distance between two rows in the object rectangle.
     */
    public final static int ROWDISTANCE = 2;

    /**
     * The defaultheight of the object rectangle. That's 3 times the rowheight + 
     * 3 times a distance of 2 between the rows + the stereoheight. 
     */
    public final static int DEFAULT_HEIGHT =
        (3 * ROWHEIGHT + 3 * ROWDISTANCE + STEREOHEIGHT);

    /**
    * The defaultwidth of the object rectangle
    */
    public final static int DEFAULT_WIDTH = 3 * DEFAULT_HEIGHT / 2;

    /**
     * The outer black rectangle of the object box (object fig without lifeline).
     */
    private FigRect _outerBox;

    /**
     * The filled box for the object box (object fig without lifeline).
     */
    private FigRect _backgroundBox;

    /**
     * The lifeline (dashed line under the object box to which activations are 
     * attached)
     */
    private FigLine _lifeLine;

    /**
     * The comma seperated list of base names of the classifierRole(s) that this object
     * represents.
     */
    private String _baseNames = "";

    /**
     * The comma seperated list of names of the classifierRole(s) that this object
     * represents.
     */
    private String _classifierRoleNames = "";

    /**
     * The name of the object (the owner of this fig).
     */
    private String _objectName = "";

    /**
     * Default constructor. Constructs the object rectangle, the lifeline, 
     * the name box and the stereotype box.
     */
    public FigObject() {
        super();
        _backgroundBox =
            new FigRect(
                0,
                0,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                Color.white,
                Color.white);
        _backgroundBox.setFilled(true);
        _outerBox = new FigRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        _outerBox.setFilled(false);
        _stereo =
            new FigText(
                DEFAULT_WIDTH / 2,
                ROWHEIGHT + ROWDISTANCE,
                0,
                0,
                Color.black,
                "Dialog",
                12,
                false);
        _stereo.setAllowsTab(false);
        _stereo.setEditable(false);
        _stereo.setFilled(false);
        _stereo.setLineWidth(0);
        _name =
            new FigText(
                DEFAULT_WIDTH / 2,
                2 * ROWDISTANCE + STEREOHEIGHT + ROWHEIGHT,
                0,
                0,
                Color.black,
                "Dialog",
                12,
                false);
        _name.setEditable(false);
        _name.setAllowsTab(false);
        _name.setFilled(false);
        _name.setLineWidth(0);
        _lifeLine =
            new FigLine(
                DEFAULT_WIDTH / 2,
                DEFAULT_HEIGHT,
                DEFAULT_WIDTH / 2,
                1000,
                Color.black);
        _lifeLine.setDashed(true);
        addFig(_lifeLine);
        addFig(_backgroundBox);
        addFig(_stereo);
        addFig(_name);
        addFig(_outerBox);

    }

    /**
     * @param gm
     * @param node
     */
    public FigObject(Object node) {
        this();
        setOwner(node);
    }

    /**
     * When the mouse button is released, this fig will be moved into position
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        super.mouseReleased(me);
        Layer lay = Globals.curEditor().getLayerManager().getActiveLayer();
        if (lay instanceof SequenceDiagramLayout) {
            ((SequenceDiagramLayout)lay).putInPosition(this);
        }
    }

    /**
     * Constructs the contents of the name text box and upates the name text box
     * accordingly. The contents of the name text box itself are NOT updated.
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        String nameText =
            (_objectName + "/" + _classifierRoleNames + ":" + _baseNames)
                .trim();
        _name.setText(nameText);
        center(_name);
        damage();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        super.updateStereotypeText();
        center(_stereo);
    }

    private void center(FigText figText) {
        int newX = this.getX() + this.getHalfWidth() - figText.getHalfWidth();
        if (figText.getX() != newX) {
            figText.setX(newX);
            updateBounds();
        }        
    }

    /**
     * Sets the bounds and coordinates of this Fig. The outerbox (the black box around
     * the upper box) and the background box (the white box at the background) are
     * scaled to the given size. The name text box and the stereo text box are
     * moved to a correct position.
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();
        if (_name.getWidth() > w) {
            w = _name.getWidth() + 2 * MARGIN;
        }
        if (_stereo.getWidth() > w) {
            w = _stereo.getWidth() + 2 * MARGIN;
        }
        _name.setCenter(
            new Point(
                x + w / 2,
                _name.getY() - oldBounds.y + y + _name.getHalfHeight()));
        _stereo.setCenter(
            new Point(
                x + w / 2,
                _stereo.getY() - oldBounds.y + y + _stereo.getHalfHeight()));
        reSize(_outerBox, x, y, w, h);
        reSize(_backgroundBox, x, y, w, h);
        _lifeLine.setBounds(
            _outerBox.getX() + _outerBox.getHalfWidth(),
            _outerBox.getY() + _outerBox.getHeight(),
            0,
            h - _outerBox.getHeight());
        calcBounds(); //_x = x; _y = y; _w = w; _h = h;		
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * Scales the given fig that must be part of this FigObject
     * @param f the fig to scale
     * @param x the new x coordinate for the FigObject
     * @param y the new y coordinate for the FigObject 
     * @param w the new w coordinate for the FigObject
     * @param h the new h coordinate for the FigObject
     */
    private void reSize(Fig f, int x, int y, int w, int h) {
        if (f.isDisplayed()) {
            int newX =
                (_w == 0)
                    ? x
                    : x + (int) ((f.getX() - _x) * ((float)w / (float)_w));
            int newY =
                (_h == 0)
                    ? y
                    : y + (int) ((f.getY() - _y) * ((float)h / (float)_h));
            int newW =
                (_w == 0)
                    ? 0
                    : (int) (f.getWidth() * ((float)w / (float)_w));
            int newH =
                (_h == 0)
                    ? 0
                    : (int) (f.getHeight() * ((float)h / (float)_h));
            f.setBounds(newX, newY, newW, newH);
        }
    }

    /**
     * Just factored out this method to save me typing. Returns a beautified name
     * to show in the name text box.
     * @param o
     * @return
     */
    private String getBeautifiedName(Object o) {
        String name = ModelFacade.getName(o);
        if (name == null || name.equals("")) {
            name = "(anon " + ModelFacade.getUMLClassName(o) + ")";
        }
        return name;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    public void calcBounds() {
        Rectangle bounds = _outerBox.getBounds();
        bounds.add(_lifeLine.getBounds());
        _x = bounds.x;
        _y = bounds.y;
        _h = bounds.height;
        _w = bounds.width;
        
    }

    /**
     * The width of the FigObject should be equal to the width of the name or stereo
     * text box.
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateBounds()
     */
    protected void updateBounds() {
        Rectangle bounds = getBounds();
        bounds.width =
            Math.max(
                _name.getWidth() + 2 * MARGIN,
                _stereo.getWidth() + 2 * MARGIN);
        setBounds(bounds);
    }

    /**
     * Changing the line width should only change the line width of the outerbox
     * and the lifeline. 0 Values are ignored.
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        if (_outerBox.getLineWidth() != w && w != 0) {
            _outerBox.setLineWidth(w);
            _lifeLine.setLineWidth(w);
            damage();
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        if (col != null && col != _backgroundBox.getFillColor()) {
            _backgroundBox.setFillColor(col);
            damage();
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean filled) {
        if (_backgroundBox.getFilled() != filled) {
            _backgroundBox.setFilled(filled);
            damage();
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return _backgroundBox.getFillColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return _backgroundBox.getFilled();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return _outerBox.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return _outerBox.getLineWidth();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object newOwner) {
        if (ModelFacade.isAInstance(newOwner)) {
            Object oldOwner = getOwner();
            UmlModelEventPump pump = UmlModelEventPump.getPump();
            pump.removeModelEventListener(this, oldOwner);
            pump.addModelEventListener(
                this,
                newOwner,
                new String[] { "name", "stereotype" });
            Iterator it = ModelFacade.getClassifiers(newOwner).iterator();
            while (it.hasNext()) {
                Object classifierRole = it.next();
                pump.removeModelEventListener(this, classifierRole);
                pump.addModelEventListener(
                    this,
                    classifierRole,
                    new String[] { "name", "base" });
                Iterator it2 = ModelFacade.getBases(classifierRole).iterator();
                while (it2.hasNext()) {
                    Object base = it2.next();
                    pump.addModelEventListener(this, base, "name");
                }
            }
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(ru.novosoft.uml.MElementEvent)
     */
    protected void modelChanged(MElementEvent mee) {
        boolean nameChanged = false;
        if (mee.getSource() == getOwner() && mee.getName().equals("name")) {
            updateObjectName();
            nameChanged = true;
        } else if (
            ModelFacade.isAClassifierRole(mee.getSource())
                && mee.getName().equals("name")) {
            updateClassifierRoleNames();
            nameChanged = true;
        } else if (
            mee.getSource() == getOwner()
                && mee.getName().equals("stereotype")) {
            updateStereotypeText();
        } else if (mee.getName().equals("name")) {
            updateBaseNames();
            nameChanged = true;
        }
        if (nameChanged) {
            updateNameText();
        }
    }

    private void updateClassifierRoleNames() {
        String roleNames = "";
        Iterator it = ModelFacade.getClassifiers(getOwner()).iterator();
        while (it.hasNext()) {
            roleNames += getBeautifiedName(it.next());
            if (it.hasNext()) {
                roleNames += ", ";
            }
        }
        _classifierRoleNames = roleNames;
    }

    private void updateBaseNames() {
        String baseNames = "";
        Iterator it = ModelFacade.getClassifiers(getOwner()).iterator();
        while (it.hasNext()) {
            Iterator it2 = ModelFacade.getBases(it.next()).iterator();
            while (it2.hasNext()) {
                baseNames += getBeautifiedName(it2.next());
                if (it2.hasNext()) {
                    baseNames += ",";
                }
            }
            if (it.hasNext()) {
                baseNames += ",";
            }
        }
        _baseNames = baseNames;
    }

    private void updateObjectName() {
        _objectName = getBeautifiedName(getOwner());
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        updateBaseNames();
        updateClassifierRoleNames();
        updateObjectName();
        super.renderingChanged();
    }

}
