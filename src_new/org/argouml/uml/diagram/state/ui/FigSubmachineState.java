// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.diagram.state.ui;

import org.apache.log4j.Logger;
import org.argouml.application.api.Notation;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.text.ParseException;
import java.util.Iterator;

/**
 * Class to display graphics for a UML MSubmachineState in a diagram.
 *
 * @author pepargouml@yahoo.es
 */

public class FigSubmachineState extends FigState {
    protected static Logger cat =
            Logger.getLogger(FigSubmachineState.class);

    ////////////////////////////////////////////////////////////////
    // constants

    public final int MARGIN = 2;

    ////////////////////////////////////////////////////////////////
    // instance variables

    FigRect _cover;
    FigLine _divider;
    FigLine _divider2;
    FigRect _circle1;
    FigRect _circle2;
    FigLine _circle1tocircle2;
    public FigText _include;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigSubmachineState() {
        super();

        setBigPort(new FigRRect(getInitialX() + 1, getInitialY() + 1,
                getInitialWidth() - 2, getInitialHeight() - 2,
                Color.cyan, Color.cyan));
        _cover = new FigRRect(getInitialX(), getInitialY(),
                getInitialWidth(), getInitialHeight(),
                Color.black, Color.white);

        getBigPort().setLineWidth(0);
        getNameFig().setLineWidth(0);
        getNameFig().setBounds(getInitialX() + 2, getInitialY() + 2,
                getInitialWidth() - 4, getNameFig().getBounds().height);
        getNameFig().setFilled(false);

        _divider =
                new FigLine(getInitialX(),
                        getInitialY() + 2 + getNameFig().getBounds().height + 1,
                        getInitialWidth() - 1,
                        getInitialY() + 2 + getNameFig().getBounds().height + 1,
                        Color.black);

        _include = new FigText(10, 10, 90, 21, true);
        _include.setFont(getLabelFont());
        _include.setTextColor(Color.black);
        _include.setMultiLine(false);
        _include.setAllowsTab(false);
        _include.setText(placeString());
        _include.setLineWidth(0);
        _include.setBounds(getInitialX() + 2, getInitialY() + 2,
                getInitialWidth() - 4, _include.getBounds().height);
        _include.setFilled(false);
        _include.setEditable(false);

        _divider2 =
                new FigLine(getInitialX(),
                        getInitialY() + 2 + getNameFig().getBounds().height + 1,
                        getInitialWidth() - 1,
                        getInitialY() + 2 + getNameFig().getBounds().height + 1,
                        Color.black);

        _circle1 = new FigRRect(getInitialX() + getInitialWidth() - 55,
                getInitialY() + getInitialHeight() - 15,
                20, 10,
                Color.black, Color.white);
        _circle2 = new FigRRect(getInitialX() + getInitialWidth() - 25,
                getInitialY() + getInitialHeight() - 15,
                20, 10,
                Color.black, Color.white);

        _circle1tocircle2 =
                new FigLine(getInitialX() + getInitialWidth() - 35,
                        getInitialY() + getInitialHeight() - 10,
                        getInitialX() + getInitialWidth() - 25,
                        getInitialY() + getInitialHeight() - 10,
                        Color.black);

        addFig(getBigPort());
        addFig(_cover);
        addFig(getNameFig());
        addFig(_divider);
        addFig(_include);
        addFig(_divider2);
        addFig(getInternal());
        addFig(_circle1);
        addFig(_circle2);
        addFig(_circle1tocircle2);

        //setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }

    public FigSubmachineState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    public void setOwner(Object node) {
        super.setOwner(node);
        updateInclude();
    }

    public Object clone() {
        FigSubmachineState figClone = (FigSubmachineState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone._cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        figClone._divider = (FigLine) it.next();
        figClone._include = (FigText) it.next();
        figClone._divider2 = (FigLine) it.next();
        figClone.setInternal((FigText) it.next());
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public Dimension getMinimumSize() {
        Dimension nameDim = getNameFig().getMinimumSize();
        Dimension internalDim = getInternal().getMinimumSize();
        Dimension includeDim = _include.getMinimumSize();

        int h = nameDim.height + 4 + internalDim.height + includeDim.height;
        int waux = Math.max(nameDim.width + 4, internalDim.width + 4);
        int w = Math.max(waux, includeDim.width + 50);
        return new Dimension(w, h);
    }

    public boolean getUseTrapRect() {
        return true;
    }

    /* Override setBounds to keep shapes looking right */
    public void setBounds(int x, int y, int w, int h) {
        if (getNameFig() == null)
            return;

        Rectangle oldBounds = getBounds();
        Dimension nameDim = getNameFig().getMinimumSize();
        Dimension includeDim = _include.getMinimumSize();

        getNameFig().setBounds(x + 2, y + 2, w - 4, nameDim.height);
        _divider.setShape(x, y + nameDim.height + 1,
                x + w - 1, y + nameDim.height + 1);

        _include.setBounds(x + 2, y + 3 + nameDim.height,
                w - 4, includeDim.height);
        _divider2.setShape(x,
                y + nameDim.height + 2 + includeDim.height + 1,
                x + w - 1,
                y + nameDim.height + 2 + includeDim.height + 1);

        getInternal().setBounds(x + 2,
                y + nameDim.height + includeDim.height + 4,
                w - 4, h - nameDim.height - includeDim.height - 4);

        _circle1.setBounds(x + w - 55,
                y + h - 15,
                20, 10);
        _circle2.setBounds(x + w - 25,
                y + h - 15,
                20, 10);
        _circle1tocircle2.setShape(x + w - 35,
                y + h - 10,
                x + w - 25,
                y + h - 10);

        getBigPort().setBounds(x, y, w, h);
        _cover.setBounds(x, y, w, h);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    ////////////////////////////////////////////////////////////////
    // fig accessors

    public void setLineColor(Color col) {
        _cover.setLineColor(col);
        _divider.setLineColor(col);
        _divider2.setLineColor(col);
        _circle1.setLineColor(col);
        _circle2.setLineColor(col);
        _circle1tocircle2.setLineColor(col);
    }

    public Color getLineColor() {
        return _cover.getLineColor();
    }

    public void setFillColor(Color col) {
        _cover.setFillColor(col);
    }

    public Color getFillColor() {
        return _cover.getFillColor();
    }

    public void setFilled(boolean f) {
        _cover.setFilled(f);
    }

    public boolean getFilled() {
        return _cover.getFilled();
    }

    public void setLineWidth(int w) {
        _cover.setLineWidth(w);
        _divider.setLineWidth(w);
        _divider2.setLineWidth(w);
    }

    public int getLineWidth() {
        return _cover.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////
    // event processing

    /**
     * Update the text labels and listeners
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (getOwner() == null)
            return;
        if ((mee.getSource().equals(getOwner()))) {
            if ((mee.getPropertyName()).equals("submachine")) {
                updateInclude();
                if (mee.getOldValue() != null) {
                    updateListeners(getOwner(), mee.getOldValue());
                }
            }
        } else {
            if (mee.getSource() ==
                    Model.getFacade().getSubmachine(getOwner())) {
                // The Machine State has got a new name
                if (mee.getPropertyName().equals("name")) {
                    updateInclude();
                }
                // The Machine State has been deleted from model
                if (mee.getPropertyName().equals("top")) {
                    updateListeners(null);
                }
            }
        }
    }

    protected void updateListeners(Object newOwner) {
        super.updateListeners(newOwner);
        if (newOwner != null) {
            Object newSm = Model.getFacade().getSubmachine(newOwner);
            if (newSm != null)
                Model.getPump().addModelEventListener(this, newSm);
        } else {
            Object oldOwner = getOwner();
            if (oldOwner != null) {
                Object oldSm = Model.getFacade().getSubmachine(oldOwner);
                if (oldSm != null)
                    Model.getPump().removeModelEventListener(this, oldSm);
            }
        }
    }

    protected void updateListeners(Object newOwner, Object oldV) {
        this.updateListeners(newOwner);
        if (oldV != null)
            Model.getPump().removeModelEventListener(this, oldV);
    }

    protected void updateInclude() {
        _include.setText(Notation.generate(this, getOwner()));
        calcBounds();
        setBounds(getBounds());
        damage();
    }

    public void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        if (ft == getInternal()) {
            Object st = getOwner();
            if (st == null)
                return;
            String s = ft.getText();
            try {
                ParserDisplay.SINGLETON.parseStateBody(st, s);
            } catch (ParseException pe) {
                ProjectBrowser.getInstance()
                        .getStatusBar()
                        .showStatus(
                                "Error: " + pe + " at " + pe.getErrorOffset());
                // TODO: i18n
            }
        }
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialHeight()
     */
    protected int getInitialHeight() {
        return 150;
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialWidth()
     */
    protected int getInitialWidth() {
        return 180;
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialX()
     */
    protected int getInitialX() {
        return 0;
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigState#getInitialY()
     */
    protected int getInitialY() {
        return 0;
    }

} /* end class FigSubmachineState */