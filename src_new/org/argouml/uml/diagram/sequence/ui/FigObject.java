// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.uml.diagram.sequence.ActivationNode;
import org.argouml.uml.diagram.sequence.EmptyNode;
import org.argouml.uml.diagram.sequence.LifeLinePort;
import org.argouml.uml.diagram.sequence.LinkNode;
import org.argouml.uml.diagram.sequence.LinkPort;
import org.argouml.uml.diagram.sequence.Node;
import org.argouml.uml.diagram.sequence.ObjectNode;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;

/**
 * Fig to show an object on a sequence diagram. The fig consists of an
 * upper box that shows the name of the object (the owner) and a
 * lifeline. The lifeline consists of lifeline elements. An element
 * can be a dashed line (no link attached) or a rectangle (link
 * attached).
 * @author jaap.branderhorst@xs4all.nl
 * Aug 11, 2003
 */
public class FigObject extends FigNodeModelElement implements MouseListener {
    /**
    * The width of an activation box
    */
    public static final int WIDTH = 20;

    /**
     * The margin between the outer box and the name and stereotype text box.
     */
    public static final int MARGIN = 10;

    /**
     * The distance between two rows in the object rectangle.
     */
    public static final int ROWDISTANCE = 2;

    /**
     * The defaultheight of the object rectangle. That's 3 times the rowheight +
     * 3 times a distance of 2 between the rows + the stereoheight.
     */
    public static final int DEFAULT_HEIGHT =
        (3 * ROWHEIGHT + 3 * ROWDISTANCE + STEREOHEIGHT);

    /**
    * The defaultwidth of the object rectangle
    */
    public static final int DEFAULT_WIDTH = 3 * DEFAULT_HEIGHT / 2;

    /**
     * The outer black rectangle of the object box (object fig without
     * lifeline).
     */
    private FigRect outerBox;

    /**
     * The filled box for the object box (object fig without lifeline).
     */
    private FigRect backgroundBox;

    /**
     * The lifeline (dashed line under the object box to which activations are
     * attached)
     */
    private FigLine lifeLine;

    /**
     * The owner of the lifeline.
     */
    private LifeLinePort lifeLinePort;

    /**
     * The list where the nodes to which links can be attached are stored
     */
    private List linkPositions = new ArrayList();

    /**
     * The list where the figrects are stored that are the activation
     * boxes. The list is only here for performance issues. When
     * removing a figrect, it should also be removed from this list,
     * not only from the figs list.
     */
    private List figActivations = new ArrayList();

    /**
     * The list with the figLinkPorts. The list is only here for
     * performance issues. When deleting a figLinkPort, it should also
     * be removed from this list, not only from the figs list.
     */
    private List figFigLinkPorts = new ArrayList();

    /**
     * The comma seperated list of base names of the classifierRole(s)
     * that this object represents.
     */
    private String baseNames = "";

    /**
     * The comma seperated list of names of the classifierRole(s) that
     * this object represents.
     */
    private String classifierRoleNames = "";

    /**
     * The name of the object (the owner of this fig).
     */
    private String objectName = "";

    /**
     * Default constructor. Constructs the object rectangle, the lifeline,
     * the name box and the stereotype box.
     */
    public FigObject() {
        super();
        backgroundBox =
            new FigRect(
                0,
                0,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                Color.white,
                Color.white);
        backgroundBox.setFilled(true);
        outerBox = new FigRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        outerBox.setFilled(false);
        setStereotypeFig(new FigText(DEFAULT_WIDTH / 2,
				     ROWHEIGHT + ROWDISTANCE,
				     0,
				     0,
				     Color.black,
				     "Dialog",
				     12,
				     false));
        getStereotypeFigText().setAllowsTab(false);
        getStereotypeFigText().setEditable(false);
        getStereotypeFig().setFilled(false);
        getStereotypeFig().setLineWidth(0);
        setNameFig(new FigText(DEFAULT_WIDTH / 2,
			       2 * ROWDISTANCE + STEREOHEIGHT + ROWHEIGHT,
			       0,
			       0,
			       Color.black,
			       "Dialog",
			       12,
			       false));
        getNameFig().setEditable(false);
        getNameFig().setAllowsTab(false);
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        lifeLine =
            new FigLine(
                DEFAULT_WIDTH / 2,
                DEFAULT_HEIGHT,
                DEFAULT_WIDTH / 2,
                1000,
                Color.black);
        lifeLine.setDashed(true);
        linkPositions.add(new ObjectNode(this));
        for (int i = 0;
            i <= lifeLine.getHeight() / SequenceDiagramLayout.LINK_DISTANCE;
            i++) {
            linkPositions.add(new EmptyNode());
        }
        addFig(lifeLine);
        addFig(backgroundBox);
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(outerBox);
    }

    /**
     * @param node the owner
     */
    public FigObject(Object node) {
        this();
        setOwner(node);
    }

    /**
     * When the mouse button is released, this fig will be moved into position
     *
     * @see MouseListener#mouseReleased(MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        super.mouseReleased(me);
        Layer lay = Globals.curEditor().getLayerManager().getActiveLayer();
        if (lay instanceof SequenceDiagramLayout) {
            ((SequenceDiagramLayout) lay).putInPosition(this);
        }
    }

    /**
     * Constructs the contents of the name text box and upates the name text box
     * accordingly. The contents of the name text box itself are NOT updated.
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        String nameText =
            (objectName + "/" + classifierRoleNames + ":" + baseNames)
                .trim();
        getNameFig().setText(nameText);
        center(getNameFig());
        damage();
    }

    /**
     * @see FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        super.updateStereotypeText();
        center(getStereotypeFigText());
    }

    private void center(FigText figText) {
        int newX = this.getX() + this.getHalfWidth() - figText.getHalfWidth();
        if (figText.getX() != newX) {
            figText.setX(newX);
            updateBounds();
        }
    }

    /**
     * Sets the bounds and coordinates of this Fig. The outerbox (the
     * black box around the upper box) and the background box (the
     * white box at the background) are scaled to the given size. The
     * name text box and the stereo text box are moved to a correct
     * position.
     *
     * @see Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int w, int h) {
        Rectangle oldBounds = getBounds();
        if (getNameFig().getWidth() > w) {
            w = getNameFig().getWidth() + 2 * MARGIN;
        }
        if (getStereotypeFig().getWidth() > w) {
            w = getStereotypeFig().getWidth() + 2 * MARGIN;
        }
        getNameFig().setCenter(
            new Point(x + w / 2,
		      getNameFig().getY() - oldBounds.y + y
		      + getNameFig().getHalfHeight()));
        getStereotypeFig().setCenter(
            new Point(
                x + w / 2,
                (getStereotypeFig().getY() - oldBounds.y
		 + y + getStereotypeFig().getHalfHeight())));
        reSize(outerBox, x, y, w, h);
        reSize(backgroundBox, x, y, w, h);
        lifeLine.setBounds(
            outerBox.getX() + outerBox.getHalfWidth(),
            outerBox.getY() + outerBox.getHeight(),
            0,
            h - outerBox.getHeight());
        int factor = h / oldBounds.height;
        for (int i = 0; i < figActivations.size(); i++) {
            FigRect fig = (FigRect) figActivations.get(i);
            fig.setBounds(
                x - oldBounds.x + fig.getX(),
                y - oldBounds.y + fig.getY(),
                fig.getWidth(),
                fig.getHeight());
        }
        for (int i = 0; i < figFigLinkPorts.size(); i++) {
            FigLinkPort fig = (FigLinkPort) figFigLinkPorts.get(i);
            fig.setBounds(
                x - oldBounds.x + fig.getX(),
                y - oldBounds.y + fig.getY(),
                fig.getWidth(),
                fig.getHeight());
        }
        calcBounds(); //_x = x; _height = y; _w = w; _h = h;
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * Scales the given fig that must be part of this FigObject.<p>
     *
     * @param f the fig to scale
     * @param x the new x coordinate for the FigObject
     * @param y the new y coordinate for the FigObject
     * @param w the new w coordinate for the FigObject
     * @param h the new h coordinate for the FigObject
     */
    private void reSize(Fig f, int x, int y, int w, int h) {
        if (f.isVisible()) {
            int newX =
                (_w == 0)
                    ? x
                    : x + (int) ((f.getX() - _x) * ((float) w / (float) _w));
            int newY =
                (_h == 0)
                    ? y
                    : y + (int) ((f.getY() - _y) * ((float) h / (float) _h));
            int newW =
                (_w == 0)
                    ? 0
                    : (int) (((float) f.getWidth()) * ((float) w / (float) _w));

            int newH =
                (_h == 0)
                    ? 0
                    : (int) (((float) f.getHeight())
			     * ((float) h / (float) _h));

            f.setBounds(newX, newY, newW, newH);
        }
    }

    /**
     * Returns a beautified name to show in the name text box.<p>
     *
     * @param o is the object
     * @return a name
     */
    private String getBeautifiedName(Object o) {
        String name = ModelFacade.getName(o);
        if (name == null || name.equals("")) {
            name = "(anon " + ModelFacade.getUMLClassName(o) + ")";
        }
        return name;
    }

    /**
     * @see Fig#calcBounds()
     * @see FigNodeModelElement#calcBounds()
     */
    public void calcBounds() {
        Rectangle bounds = outerBox.getBounds();
        bounds.add(lifeLine.getBounds());
        _x = bounds.x;
        _y = bounds.y;
        _h = bounds.height;
        _w = bounds.width;
    }

    private void removeActivations() {
        for (int i = 0; i < figActivations.size(); i++) {
            getFigs(null).remove(figActivations.get(i));
        }
        figActivations = new ArrayList();
        calcBounds();
    }

    private void addActivations() {
        boolean startActivation = false;
        ActivationNode startActivationNode = null;
        ActivationNode endActivationNode = null;
        int x = lifeLine.getX() - WIDTH / 2;
        for (int i = 0; i < linkPositions.size(); i++) {
            Node node = (Node) linkPositions.get(i);
            if (node instanceof ActivationNode
                && ((ActivationNode) node).isStart()) {
                startActivationNode = (ActivationNode) node;
                endActivationNode = null;
            }
            if (node instanceof ActivationNode
                && ((ActivationNode) node).isEnd()) {

                if (startActivationNode == null) {
                    throw new IllegalStateException(
			    "End activation node without "
			    + "start activation node.");
                }

                endActivationNode = (ActivationNode) node;
                int startPos =
                    getYCoordinateForActivationBox(startActivationNode, true);
                int endPos = 0;
                if (!endActivationNode.isStart()) {
                    endPos =
                        getYCoordinateForActivationBox(
                            endActivationNode,
                            false);
                } else {
                    endPos = getYCoordinate(endActivationNode);
                    if (!endActivationNode.isCutOffBottom()) {
                        endPos += SequenceDiagramLayout.LINK_DISTANCE / 2;
                    }
                }
                FigRect activation =
                    new FigRect(
                        x,
                        startPos,
                        WIDTH,
                        endPos - startPos,
                        outerBox.getLineColor(),
                        backgroundBox.getFillColor());
                figActivations.add(activation);
                addFig(activation);
            }
        }
    }

    /**
     * First removes all current activation boxes, then add new ones
     * to the figobject depending on the state of the nodes.
     */
    public void updateActivations() {
        removeActivations();
        addActivations();
    }

    /**
     * The width of the FigObject should be equal to the width of the
     * name or stereo text box.<p>
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateBounds()
     */
    protected void updateBounds() {
        Rectangle bounds = getBounds();
        bounds.width =
            Math.max(
                getNameFig().getWidth() + 2 * MARGIN,
                getStereotypeFig().getWidth() + 2 * MARGIN);
        setBounds(bounds);
    }

    /**
     * Changing the line width should only change the line width of the outerbox
     * and the lifeline. 0 Values are ignored.
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        if (outerBox.getLineWidth() != w && w != 0) {
            outerBox.setLineWidth(w);
            lifeLine.setLineWidth(w);
            damage();
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        if (col != null && col != backgroundBox.getFillColor()) {
            backgroundBox.setFillColor(col);
            damage();
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean filled) {
        if (backgroundBox.getFilled() != filled) {
            backgroundBox.setFilled(filled);
            damage();
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return backgroundBox.getFillColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return backgroundBox.getFilled();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return outerBox.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return outerBox.getLineWidth();
    }

    /**
     * @see FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object newOwner) {
        if (ModelFacade.isAInstance(newOwner)) {
            Object oldOwner = getOwner();
            UmlModelEventPump pump = UmlModelEventPump.getPump();
            pump.removeModelEventListener(this, oldOwner);
            pump.addModelEventListener(this,
				       newOwner,
				       new String[] {
					   "name",
					   "stereotype",
				       });
            Iterator it = ModelFacade.getClassifiers(newOwner).iterator();
            while (it.hasNext()) {
                Object classifierRole = it.next();
                pump.removeModelEventListener(this, classifierRole);
                pump.addModelEventListener(
					   this,
					   classifierRole,
					   new String[] {
					       "name",
					       "base",
					   });
                Iterator it2 = ModelFacade.getBases(classifierRole).iterator();
                while (it2.hasNext()) {
                    Object base = it2.next();
                    pump.addModelEventListener(this, base, "name");
                }
            }
        }
    }

    /**
     * @see FigNodeModelElement#modelChanged(MElementEvent)
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
        classifierRoleNames = roleNames;
    }

    private void updateBaseNames() {
        String b = "";
        Iterator it = ModelFacade.getClassifiers(getOwner()).iterator();
        while (it.hasNext()) {
            Iterator it2 = ModelFacade.getBases(it.next()).iterator();
            while (it2.hasNext()) {
                b += getBeautifiedName(it2.next());
                if (it2.hasNext()) {
                    b += ",";
                }
            }
            if (it.hasNext()) {
                b += ",";
            }
        }
        baseNames = b;
    }

    private void updateObjectName() {
        objectName = getBeautifiedName(getOwner());
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

    /**
     * Returns the port for a given coordinate pair. Normally
     * deepHitPort returns the owner of the fig in the FigGroup that
     * is present at the given coordinate pair (returning figs that
     * are added later first). In this case it returns a
     * LinkPort.<p>
     *
     * This method has a side effect of creating a
     * FigLinkPort if ???<p>
     *
     * @see org.tigris.gef.presentation.FigNode#deepHitPort(int, int)
     */
    public Object deepHitPort(int x, int y) {
        Object port = null;
        Rectangle rect = new Rectangle(getX(), y - 16, getWidth(), 32);
        Node foundNode = null;
        if (lifeLine.intersects(rect)) {
            for (int i = linkPositions.indexOf(getObjectNode());
                i < linkPositions.size();
                i++) {
                Node node = (Node) linkPositions.get(i);
                int position = getYCoordinate(node);
                if (i < linkPositions.size() - 1) {
                    int nextPosition =
                        getYCoordinate((Node) linkPositions.get(i + 1));
                    if (nextPosition >= y && position < y) {
                        if ((y - position) <= (nextPosition - y)) {
                            foundNode = node;
                        } else {
                            foundNode = (Node) linkPositions.get(i + 1);
                        }
                        break;
                    }
                } else {
                    foundNode =
                        (Node) linkPositions.get(linkPositions.size() - 1);
                    break;
                }
            }
            int yCoordinate = 0;
            SequenceDiagramLayout layout = (SequenceDiagramLayout) getLayer();
            if (!layout.getFigLinks(getYCoordinate(foundNode)).isEmpty()) {
                FigLinkPort figLinkPort =
                    new FigLinkPort(
                        lifeLine.getX() - WIDTH / 2,
                        getYCoordinate(foundNode)
                            + SequenceDiagramLayout.LINK_DISTANCE,
                        lifeLine.getX() + WIDTH / 2);
                addFig(figLinkPort);
                figFigLinkPorts.add(figLinkPort);
                LinkNode linkNode = new LinkNode(getOwner(), figLinkPort);
                layout.addNode(getIndexOf(foundNode) + 1, linkNode);
                foundNode = linkNode;
            } else {
                if (!(foundNode instanceof LinkPort)
                    && !(foundNode instanceof ObjectNode)) {
                    FigLinkPort figLinkPort =
                        new FigLinkPort(
                            lifeLine.getX() - WIDTH / 2,
                            getYCoordinate(foundNode),
                            lifeLine.getX() + WIDTH / 2);
                    addFig(figLinkPort);
                    figFigLinkPorts.add(figLinkPort);
                    LinkNode linkNode = new LinkNode(getOwner(), figLinkPort);
                    linkPositions.set(
                        linkPositions.indexOf(foundNode),
                        linkNode);
                    foundNode = linkNode;
                }

            }
            return foundNode;
        } else if (outerBox.intersects(rect)) {
            ObjectNode objectNode = (ObjectNode) getObjectNode();
            if (objectNode.getFigLinkPort() != null
                && getFigLink(objectNode.getFigLinkPort()) != null) {
                return null; // cannot connect here
            }
            if (objectNode.getFigLinkPort() == null) {
                FigLinkPort figLinkPort =
                    new FigLinkPort(
                        lifeLine.getX() - WIDTH / 2,
                        getYCoordinate(foundNode),
                        lifeLine.getX() + WIDTH / 2);
                objectNode.setFigLinkPort(figLinkPort);
            }
            objectNode.setObject(getOwner());
            return objectNode;
        } else {
            return null;
        }
    }

    private int getYCoordinate(Node node) {
        int index = linkPositions.indexOf(node);
        int y = 0;
        if (index == 0) {
            y = getY() + outerBox.getHalfHeight();
        } else {
            y =
                (index - 1) * SequenceDiagramLayout.LINK_DISTANCE
                    + getY()
                    + outerBox.getHeight();
        }
        return y;
    }

    private int getYCoordinateForActivationBox(Node node, boolean start) {
        int y = getYCoordinate(node);
        if (node instanceof ActivationNode) {
            if (node instanceof ObjectNode) {
                y = y + outerBox.getHalfHeight();
            } else {
                ActivationNode activationNode = (ActivationNode) node;
                if (start
                    && activationNode.isStart()
                    && !activationNode.isCutOffTop()) {
                    y -= SequenceDiagramLayout.LINK_DISTANCE / 2;
                } else if (
                    !start
                        && activationNode.isEnd()
                        && !activationNode.isCutOffBottom()) {
                    y += SequenceDiagramLayout.LINK_DISTANCE / 2;
                }
            }
        }
        return y;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object own) {
        super.setOwner(own);
        bindPort(own, outerBox);
    }

    /**
     * Gets the {@link FigLink} that is attached to the given
     * {@link FigLinkPort}.
     *
     * @param portFig the given port
     * @return the {@link FigLink} that is attached.
     */
    public FigLink getFigLink(FigLinkPort portFig) {
        Iterator it = getFigEdges(null).iterator();
        while (it.hasNext()) {
            FigEdge figEdge = (FigEdge) it.next();
            if (figEdge instanceof FigLink
                && (figEdge.getSourcePortFig() == portFig
                    || figEdge.getDestPortFig() == portFig)) {
                return (FigLink) figEdge;
            }
        }
        return null;
    }

    /**
     * @return the lifeline Fig
     */
    public FigLine getLifeLine() {
        return lifeLine;
    }

    /**
     * Returns a list with all nodes that belong to the same
     * activation as the given node.<p>
     *
     * TODO: Does not work for EmptyNode atm.
     *
     * @param node is the given node
     * @return a List
     */
    public List getActivationNodes(Node node) {
        int start = -1;
        int end = -1;
        if (node instanceof ActivationNode) {
            ActivationNode activationNode = (ActivationNode) node;
            if (activationNode.isStart()) {
                start = linkPositions.indexOf(activationNode);
                ListIterator it =
                    linkPositions
                        .subList(
                            linkPositions.indexOf(activationNode),
                            linkPositions.size())
                        .listIterator();
                while (it.hasNext()) {
                    Node node2 = (Node) it.next();
                    if (node2 instanceof ActivationNode
                        && ((ActivationNode) node2).isEnd()) {
                        end = linkPositions.indexOf(node2);
                        break;
                    }
                }
            } else if (activationNode.isEnd()) {
                end = linkPositions.indexOf(activationNode);
                for (int i = end - 1; i >= 1; i--) {
                    Node node2 = (Node) linkPositions.get(i);
                    if (node2 instanceof ActivationNode
                        && ((ActivationNode) node2).isStart()) {
                        start = linkPositions.indexOf(node2);
                        break;
                    }
                }
            } else {
                // node is in between activations or does not belong
                // to activation (yet)
                if (!(nextNode(activationNode) instanceof ActivationNode)
                    || !(previousNode(activationNode)
                        instanceof ActivationNode)) {
                    return new ArrayList();
                }
                for (int i = getIndexOf(activationNode) + 1;
                    i < linkPositions.size();
                    i++) {
                    Node node2 = (Node) linkPositions.get(i);
                    if (node2 instanceof ActivationNode
                        && ((ActivationNode) node2).isEnd()) {
                        end = getIndexOf(node2);
                        break;
                    }
                }
                for (int i = getIndexOf(activationNode) - 1; i >= 0; i--) {
                    Node node2 = (Node) linkPositions.get(i);
                    if (node2 instanceof ActivationNode
                        && ((ActivationNode) node2).isStart()) {
                        start = getIndexOf(node2);
                        break;
                    }
                }

            }
            if (end == -1 || start == -1) {
                return new ArrayList();
            }
            return linkPositions.subList(start, end);

        }
        return new ArrayList();
    }

    /**
     * Returns the index of a given node.
     *
     * @param node is the given node
     * @return the index
     */
    public int getIndexOf(Node node) {
        return linkPositions.indexOf(node);
    }

    /**
     * See makeActivation(int startIndex, int endIndex)
     * @param startNode Node
     * @param endNode Node
     */
    public void makeActivation(Node startNode, Node endNode) {
        int startIndex = linkPositions.indexOf(startNode);
        int endIndex = linkPositions.indexOf(endNode);
        makeActivation(startIndex, endIndex);
    }

    /**
     * Makes an activation from the node with index startindex to the
     * node with index endindex. An activation means in this context
     * that the starting and ending node will be an activation node
     * after a call to this method.  The starting node is designated
     * via the start property that's a starting node. The same is true
     * for the ending node via the end property.<p>
     *
     * @param startIndex int
     * @param endIndex int
     */
    public void makeActivation(int startIndex, int endIndex) {
        for (int i = startIndex; i <= endIndex; i++) {
            Object o = linkPositions.get(i);
            if (!(o instanceof ActivationNode)) {
                linkPositions.set(i, new ActivationNode());
            }
        }
        ((ActivationNode) linkPositions.get(startIndex)).setStart(true);
        ((ActivationNode) linkPositions.get(endIndex)).setEnd(true);
    }

    /**
     * Returns the node that's next to the given node.
     *
     * @param node is the given node
     * @return the Node
     */
    public Node nextNode(Node node) {
        if (getIndexOf(node) < linkPositions.size())
            return (Node) linkPositions.get(getIndexOf(node) + 1);
        else
            return null;
    }

    /**
     * Returns the node that's before the given node in the nodes list.
     *
     * @param node is the given node
     * @return the node
     */
    public Node previousNode(Node node) {
        if (getIndexOf(node) > 0) {
            return (Node) linkPositions.get(getIndexOf(node) - 1);
        } else
            return null;
    }

    /**
     * Returns true if this {@link FigObject} has activations. An
     * activation is in this case a start ActivationNode and an end
     * ActivationNode in the Nodes list (_linkPositions).<p>
     *
     * @return true if this {@link FigObject} has activations.
     */
    public boolean hasActivations() {
        for (int i = 0; i < linkPositions.size(); i++) {
            Node node = (Node) linkPositions.get(i);
            if (node instanceof ActivationNode) {
                if (getActivationNodes(node).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @see org.tigris.gef.presentation.FigNode#getPortFig(java.lang.Object)
     */
    public Fig getPortFig(Object np) {
        if (np instanceof LinkPort) {
            return ((LinkPort) np).getFigLinkPort();
        }
        return null;
    }

    /**
     * Sets the given node on the given index in linkPositions.<p>
     *
     * @param index the given index
     * @param node the given node
     */
    public void setNode(int index, Node node) {
        linkPositions.set(index, node);
    }

    /**
     * Returns the ObjectNode. This is the port that represents the
     * object Figrect.
     *
     * @return the ObjectNode.
     */
    public ObjectNode getObjectNode() {
        for (int i = 0; i < linkPositions.size(); i++) {
            if (linkPositions.get(i) instanceof ObjectNode) {
                return (ObjectNode) linkPositions.get(i);
            }
        }
        return null;
    }

    /**
     * Removes the fig from both the figs list as from the
     * _figActivations list and the _figLinkPorts. This assures us
     * that removal will indeed remove all 'pointers' to the object.<p>
     *
     * @see org.tigris.gef.presentation.FigGroup#removeFig(Fig)
     */
    public void removeFig(Fig f) {
        super.removeFig(f);
        figActivations.remove(f);
        figFigLinkPorts.remove(f);
    }

    /**
     * Returns a list with the start, end and all nodes in between
     * them of the activation that is prior to the given node. If the
     * node is part of an activation, that activation is returned. If
     * the node is null an empty list is returned.
     *
     * @param node is the given node
     * @return a List
     */
    public List getPreviousActivation(Node node) {
        List retList = getActivationNodes(node);
        if (retList.isEmpty()) {
            retList = getPreviousActivation(previousNode(node));
        }
        return retList;
    }

    /**
     * @param position the position in which the node will be added
     * @param node the node to be added
     */
    public void addNode(int position, Node node) {
        linkPositions.add(position, node);
        Iterator it =
            linkPositions
                .subList(position + 1, linkPositions.size())
                .iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof LinkPort) {
                FigLinkPort figLinkPort = ((LinkPort) o).getFigLinkPort();
                if (figLinkPort != null) {
                    figLinkPort.setY(
                        figLinkPort.getY()
                            + SequenceDiagramLayout.LINK_DISTANCE);
                }
            }
        }
        calcBounds();
    }

}
