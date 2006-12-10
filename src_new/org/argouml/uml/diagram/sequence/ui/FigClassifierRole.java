// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.diagram.sequence.MessageNode;
import org.argouml.uml.diagram.sequence.ui.FigLifeLine.FigLifeLineHandler;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigStereotypesCompartment;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Selection;
import org.tigris.gef.persistence.pgml.Container;
import org.tigris.gef.persistence.pgml.FigGroupHandler;
import org.tigris.gef.persistence.pgml.HandlerFactory;
import org.tigris.gef.persistence.pgml.HandlerStack;
import org.tigris.gef.persistence.pgml.PGMLStackParser;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Fig to show an object on a sequence diagram. The fig consists of an
 * upper box that shows the name of the object (the owner) and a
 * lifeline. The lifeline consists of lifeline elements. An element
 * can be a dashed line (no link attached) or a rectangle (link
 * attached).
 * @author jaap.branderhorst@xs4all.nl
 * Aug 11, 2003
 */
public class FigClassifierRole extends FigNodeModelElement
    implements MouseListener, HandlerFactory {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(FigClassifierRole.class);

    /**
     * The width of an activation box.
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
    public static final int MIN_HEAD_HEIGHT =
        (3 * ROWHEIGHT + 3 * ROWDISTANCE + STEREOHEIGHT);

    /**
     * The defaultwidth of the object rectangle.
     */
    public static final int MIN_HEAD_WIDTH = 3 * MIN_HEAD_HEIGHT / 2;

    /**
     * The filled box for the object box (object fig without lifeline).
     */
    FigHead headFig;

    /**
     * The lifeline (dashed line under the object box to which activations are
     * attached).
     */
    private FigLifeLine lifeLineFig;

    /**
     * The list where the nodes to which links can be attached are stored.
     */
    private List linkPositions = new ArrayList();

    /**
     * The comma seperated list of base names of the classifierRole(s)
     * that this object represents.
     */
    private String baseNames = "";

    /**
     * The name of the classifier role (the owner of this fig).
     */
    private String classifierRoleName = "";

    /**
     * Default constructor. Constructs the object rectangle, the lifeline,
     * the name box and the stereotype box.
     */
    private FigClassifierRole() {
        super();
        headFig = new FigHead(getStereotypeFig(), getNameFig());
        getStereotypeFig().setBounds(MIN_HEAD_WIDTH / 2,
				     ROWHEIGHT + ROWDISTANCE,
				     0,
				     0);
        getStereotypeFig().setFilled(false);
        getStereotypeFig().setLineWidth(0);
        getNameFig().setEditable(false);
        getNameFig().setFilled(false);
        getNameFig().setLineWidth(0);
        lifeLineFig =
            new FigLifeLine(MIN_HEAD_WIDTH / 2 - WIDTH / 2, MIN_HEAD_HEIGHT);
        linkPositions.add(new MessageNode(this));
        for (int i = 0;
                i <= lifeLineFig.getHeight() 
                            / SequenceDiagramLayer.LINK_DISTANCE;
                i++) {
            linkPositions.add(new MessageNode(this));
        }
        
        // TODO: Why does this give loading problems?
//        addFig(getBigPort());
        addFig(lifeLineFig);
        addFig(headFig);
        // TODO: Why does this give loading problems?
//        addFig(getStereotypeFig());
//        addFig(getNameFig());
    }

    public FigClassifierRole(Object node, int x, int y, int w, int h) {
        this();
        setBounds(x, y, w, h);
        setOwner(node);
    }

    public FigClassifierRole(Object node) {
        this();
        setOwner(node);
    }

    
    /*
     * When the mouse button is released, this fig will be moved into position.
     *
     * @see MouseListener#mouseReleased(MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        super.mouseReleased(me);
        Layer lay = Globals.curEditor().getLayerManager().getActiveLayer();
        if (lay instanceof SequenceDiagramLayer) {
            ((SequenceDiagramLayer) lay).putInPosition(this);
        }
    }

    /**
     * Constructs the contents of the name text box and upates the name text box
     * accordingly. The contents of the name text box itself are NOT updated.
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        String nameText =
            (classifierRoleName + ":" + baseNames).trim();
        getNameFig().setText(nameText);
        calcBounds();
        damage();
    }

    public int getNodeCount() {
        return linkPositions.size();
    }

    /**
     * Change a node to point to an actual FigMessagePort.
     */
    Fig createFigMessagePort(Object message, TempFig tempFig) {
        Fig fmp = lifeLineFig.createFigMessagePort(message, tempFig);
        updateNodeStates();
        return fmp;
    }

    void addFigMessagePort(FigMessagePort messagePortFig) {
        lifeLineFig.addFig(messagePortFig);
    }

    /**
     * Connect a FigMessagePort with a MessageNode by position.
     */
    void setMatchingNode(FigMessagePort fmp) {
        while (lifeLineFig.getYCoordinate(getNodeCount() - 1) < fmp.getY1()) {
            growToSize(getNodeCount() + 10);
        }
        int i = 0;
        for (Iterator it = linkPositions.iterator(); it.hasNext(); ++i) {
            MessageNode node = (MessageNode) it.next();
            if (lifeLineFig.getYCoordinate(i) == fmp.getY1()) {
                node.setFigMessagePort(fmp);
                fmp.setNode(node);
                updateNodeStates();
                break;
            }
        }
    }

    /**
     * Set the node's fig to a FigMessagePort if one is available.
     */
    private void setMatchingFig(MessageNode messageNode) {
        if (messageNode.getFigMessagePort() == null) {
            int y = getYCoordinate(messageNode);
            for (Iterator it = lifeLineFig.getFigs().iterator();
                                it.hasNext();) {
                Fig fig = (Fig) it.next();
                if (fig instanceof FigMessagePort) {
                    FigMessagePort messagePortFig = (FigMessagePort) fig;
                    if (messagePortFig.getY1() == y) {
                        messageNode.setFigMessagePort(messagePortFig);
                        messagePortFig.setNode(messageNode);
                        updateNodeStates();
                    }
                }
            }
        }
    }

    /*
     * Sets the bounds and coordinates of this Fig. The outerbox (the
     * black box around the upper box) and the background box (the
     * white box at the background) are scaled to the given size. The
     * name text box and the stereo text box are moved to a correct
     * position.
     *
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    public void setBoundsImpl(int x, int y, int w, int h) {
        y = 50;
        Rectangle oldBounds = getBounds();
        w = headFig.getMinimumSize().width;

        headFig.setBounds(x, y, w, headFig.getMinimumSize().height);

        lifeLineFig.setBounds(
                (x + w / 2) - WIDTH / 2,
                y + headFig.getHeight(),
                WIDTH,
                h - headFig.getHeight());

        this.updateEdges(); //???
        growToSize(
                lifeLineFig.getHeight() / SequenceDiagramLayer.LINK_DISTANCE
                + 2);
        calcBounds(); //_x = x; _height = y; _w = w; _h = h;
        firePropChange("bounds", oldBounds, getBounds());
    }

    /*
     * This method is overridden in order to ignore change of y co-ordinate
     * during drag.
     *
     * @see org.tigris.gef.presentation.FigNode#superTranslate(int, int)
     */
    public void superTranslate(int dx, int dy) {
        setBounds(getX() + dx, getY(), getWidth(), getHeight());
    }

    /**
     * Returns a beautified name to show in the name text box.<p>
     *
     * @param o is the object
     * @return a name
     */
    private String getBeautifiedName(Object o) {
        String name = Model.getFacade().getName(o);
        if (name == null || name.equals("")) {
            name = "(anon " + Model.getFacade().getUMLClassName(o) + ")";
        }
        return name;
    }

    public static boolean isCallMessage(Object message) {
        return Model.getFacade()
	    .isACallAction(Model.getFacade().getAction(message));
    }

    public static boolean isReturnMessage(Object message) {
        return Model.getFacade()
	    .isAReturnAction(Model.getFacade().getAction(message));
    }

    public static boolean isCreateMessage(Object message) {
        return Model.getFacade()
	    .isACreateAction(Model.getFacade().getAction(message));
    }

    public static boolean isDestroyMessage(Object message) {
        return Model.getFacade()
	    .isADestroyAction(Model.getFacade().getAction(message));
    }

    private void setPreviousState(int start, int newState) {
        for (int i = start - 1; i >= 0; --i) {
            MessageNode node = (MessageNode) linkPositions.get(i);
            if (node.getFigMessagePort() != null) {
                break;
            }
            node.setState(newState);
        }
    }

    private int setFromActionNode(int lastState, int offset) {
        if (lastState == MessageNode.INITIAL) {
	    lastState = MessageNode.DONE_SOMETHING_NO_CALL;
            setPreviousState(offset, lastState);
        } else if (lastState == MessageNode.IMPLICIT_RETURNED) {
	    lastState = MessageNode.CALLED;
            setPreviousState(offset, lastState);
        } else if (lastState == MessageNode.IMPLICIT_CREATED) {
	    lastState = MessageNode.CREATED;
            setPreviousState(offset, lastState);
        }
        return lastState;
    }

    /**
     * Every classifier role has a state and a caller list.
     * The state is PRECREATED before a node is created
     * and DESTROYED after classifier role is
     * destroyed.
     * The state is INITIAL before anything happens
     * After a classifier is created, the state is CREATED and
     * the state for prior nodes is set to PRECREATED
     * If the classifier does something:<p>
     *
     * If the state is INITIAL, the state for that and prior
     * nodes becomes DONE_SOMETHING_NO_CALL<p>
     *
     * else if the state is IMPLICIT_RETURNED, the state for that
     * and prior nodes becomes CALLED<p>
     *
     * Otherwise, the state doesn't change<p>
     *
     * If the classifier is called:<p>
     *
     * If the caller list is empty, the state becomes CALLED<p>
     *
     * The caller is added to the caller list<p>
     *
     * If the classifier returns, the caller being returned to and any callers
     * added to the list since that call are removed from the caller list.
     * If the caller list is empty the state becomes RETURNED.<p>
     *
     * If nothing happens on the node:<p>
     *
     * If the previous state was CALLED, the state becomes IMPLICIT_RETURNED<p>
     *
     * Otherwise, the state is the same as the previous node's state<p>
     *
     * Start or stop a rectangle when the state changes.
     *
     */
    void updateNodeStates() {
        int lastState = MessageNode.INITIAL;
        ArrayList callers = null;
        int nodeCount = linkPositions.size();

        for (int i = 0; i < nodeCount; ++i) {
            MessageNode node = (MessageNode) linkPositions.get(i);
            FigMessagePort figMessagePort = node.getFigMessagePort();
            // If the node has a FigMessagePort
            if (figMessagePort != null) {
                int fmpY = lifeLineFig.getYCoordinate(i);
                if (figMessagePort.getY() != fmpY) {
                    figMessagePort.setBounds(lifeLineFig.getX(), 
                            fmpY, WIDTH, 1);
                }
                Object message = figMessagePort.getOwner();
                boolean selfMessage =
                    (Model.getFacade().isAMessage(message)
                     && (Model.getFacade().getSender(message)
                     == Model.getFacade().getReceiver(message)));
                boolean selfReceiving = false;
                if (selfMessage) {
                    for (int j = i - 1; j >= 0; --j) {
                        MessageNode prev = (MessageNode) linkPositions.get(j);
                        FigMessagePort prevmp = prev.getFigMessagePort();
                        if (prevmp != null && prevmp.getOwner() == message) {
                            selfReceiving = true;
                        }
                    }
                }
                if (isCallMessage(message)) {
                    if (Model.getFacade().getSender(message) == getOwner()
                         && !selfReceiving) {
                        lastState = setFromActionNode(lastState, i);
                        node.setState(lastState);
                        node.setCallers(callers);
                    } else {
                        if (lastState == MessageNode.INITIAL
                            || lastState == MessageNode.CREATED
                            || lastState == MessageNode.IMPLICIT_CREATED
                            || lastState == MessageNode.IMPLICIT_RETURNED
                            || lastState == MessageNode.RETURNED) {

                            lastState = MessageNode.CALLED;

                        }

                        if (callers == null) {
                            callers = new ArrayList();
                        } else {
                            callers = new ArrayList(callers);
                        }
                        callers.add(Model.getFacade().getSender(message));
                        node.setState(lastState);
                        node.setCallers(callers);
                    }
                } else if (isReturnMessage(message)) {
                    if (lastState == MessageNode.IMPLICIT_RETURNED) {
                        setPreviousState(i, MessageNode.CALLED);
                        lastState = MessageNode.CALLED;
                    }
                    if (Model.getFacade().getSender(message) == getOwner()
                            && !selfReceiving) {
                        if (callers == null) {
                            callers = new ArrayList();
                        }
                        Object caller = Model.getFacade().getReceiver(message);
                        int callerIndex = callers.lastIndexOf(caller);
                        if (callerIndex != -1) {
                            for (int backNodeIndex = i - 1;
                                  backNodeIndex > 0
                                    && ((MessageNode) linkPositions
                                            .get(backNodeIndex))
                                            .matchingCallerList(caller,
                                                    callerIndex);
                                  --backNodeIndex) {
                                ;
                            }
                            if (callerIndex == 0) {
                                callers = null;
                                if (lastState == MessageNode.CALLED) {
                                    lastState = MessageNode.RETURNED;
                                }
                            } else {
                                callers =
                                    new ArrayList(callers.subList(0,
								  callerIndex));
                            }
                        }
                    }
                    node.setState(lastState);
                    node.setCallers(callers);
                } else if (isCreateMessage(message)) {
                    if (Model.getFacade().getSender(message) == getOwner()) {
                        lastState = setFromActionNode(lastState, i);
                        node.setState(lastState);
                        node.setCallers(callers);
                    } else {
                        lastState = MessageNode.CREATED;
                        setPreviousState(i, MessageNode.PRECREATED);
                        node.setState(lastState);
                        node.setCallers(callers);
                    }
                } else if (isDestroyMessage(message)) {
                    if (Model.getFacade().getSender(message) == getOwner()
                         && !selfReceiving) {
                        lastState = setFromActionNode(lastState, i);
                        node.setState(lastState);
                        node.setCallers(callers);
                    } else {
                        lastState = MessageNode.DESTROYED;
                        callers = null;
                        node.setState(lastState);
                        node.setCallers(callers);
                    }
                }
            } else {
                if (lastState == MessageNode.CALLED) {
                    lastState = MessageNode.IMPLICIT_RETURNED;
		}
                if (lastState == MessageNode.CREATED) {
                    lastState = MessageNode.IMPLICIT_CREATED;
		}
                node.setState(lastState);
                node.setCallers(callers);
            }
        }
    }

    private void addActivations() {
        MessageNode startActivationNode = null;
        MessageNode endActivationNode = null;
        int lastState = MessageNode.INITIAL;
        boolean startFull = false;
        boolean endFull = false;
        int nodeCount = linkPositions.size();
        int x = lifeLineFig.getX();
        for (int i = 0; i < nodeCount; ++i) {
            MessageNode node = (MessageNode) linkPositions.get(i);
            int nextState = node.getState();
            if (lastState != nextState && nextState == MessageNode.CREATED) {
                lifeLineFig.addActivationFig(
                        new FigBirthActivation(
                                lifeLineFig.getX(),
                                lifeLineFig.getYCoordinate(i)
                                - SequenceDiagramLayer.LINK_DISTANCE / 4));
            } 
            if (lastState != nextState 
                    && nextState == MessageNode.DESTROYED) {
                int y =
                    lifeLineFig.getYCoordinate(i)
                    - SequenceDiagramLayer.LINK_DISTANCE / 2;
                lifeLineFig.addActivationFig(
                    new FigLine(x,
                    	    y + SequenceDiagramLayer.LINK_DISTANCE / 2,
                    	    x + WIDTH,
                    	    y + SequenceDiagramLayer.LINK_DISTANCE));
                lifeLineFig.addActivationFig(
                    new FigLine(x,
                    	    y + SequenceDiagramLayer.LINK_DISTANCE,
                    	    x + WIDTH,
                    	    y
                    	    + SequenceDiagramLayer.LINK_DISTANCE / 2));
            }
            if (startActivationNode == null) {
                switch (nextState) {
                case MessageNode.DONE_SOMETHING_NO_CALL:
                    startActivationNode = node;
                    startFull = true;
                    break;
                case MessageNode.CALLED:
                case MessageNode.CREATED:
                    startActivationNode = node;
                    startFull = false;
                    break;
                default:
                }
            } else {
                switch (nextState) {
                case MessageNode.DESTROYED :
                case MessageNode.RETURNED :
                    endActivationNode = node;
                    endFull = false;
                    break;
                case MessageNode.IMPLICIT_RETURNED :
                case MessageNode.IMPLICIT_CREATED :
                    endActivationNode = (MessageNode) linkPositions.get(i - 1);
                    endFull = true;
                    break;
                case MessageNode.CALLED :
                    if (lastState == MessageNode.CREATED) {
                	endActivationNode =
                	    (MessageNode) linkPositions.get(i - 1);
                	endFull = false;
                	--i;
                	nextState = lastState;
                    }
                    break;
                default:
                }
            }
            lastState = nextState;
            if (startActivationNode != null && endActivationNode != null) {
                if (startActivationNode != endActivationNode 
                        || startFull || endFull) {
                    int y1 = getYCoordinate(startActivationNode);
                    if (startFull) {
                        y1 -= SequenceDiagramLayer.LINK_DISTANCE / 2;
                    }
                    int y2 = getYCoordinate(endActivationNode);
                    if (endFull) {
                        y2 += SequenceDiagramLayer.LINK_DISTANCE / 2;
                    }
                    lifeLineFig.addActivationFig(
                            new FigActivation(x, y1, WIDTH, y2 - y1));
                }
                startActivationNode = null;
                endActivationNode = null;
                startFull = false;
                endFull = false;
            }
        }
        if (startActivationNode != null) {
            endActivationNode = (MessageNode) linkPositions.get(nodeCount - 1);
            endFull = true;
            int y1 = getYCoordinate(startActivationNode);
            if (startFull) {
                y1 -= SequenceDiagramLayer.LINK_DISTANCE / 2;
            }
            int y2 = getYCoordinate(endActivationNode);
            if (endFull) {
                y2 += SequenceDiagramLayer.LINK_DISTANCE / 2;
            }
            lifeLineFig.addActivationFig(
                    new FigActivation(x, y1, WIDTH, y2 - y1));
            startActivationNode = null;
            endActivationNode = null;
            startFull = false;
            endFull = false;
        }
    }

    /**
     * First removes all current activation boxes, then add new ones
     * to the figobject depending on the state of the nodes.
     */
    public void updateActivations() {
        LOG.info("Updating activations");
        lifeLineFig.removeActivations();
        addActivations();
    }

    //////////////////////////////////////////////////////////////////////////
    // HandlerFactory implementation
    /*
     * @see org.tigris.gef.persistence.pgml.HandlerFactory#getHandler(
     *      org.tigris.gef.persistence.pgml.HandlerStack, java.lang.Object,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      org.xml.sax.Attributes)
     */
    public DefaultHandler getHandler(HandlerStack stack,
                               Object container,
                               String uri,
                               String localname,
                               String qname,
                               Attributes attributes)
	throws SAXException {

        PGMLStackParser parser = (PGMLStackParser) stack;
        StringTokenizer st =
	    new StringTokenizer(attributes.getValue("description"), ",;[] ");
        if (st.hasMoreElements()) {
	    st.nextToken();
	}
        String xStr = null;
        String yStr = null;
        String wStr = null;
        String hStr = null;
        if (st.hasMoreElements()) {
            xStr = st.nextToken();
            yStr = st.nextToken();
            wStr = st.nextToken();
            hStr = st.nextToken();
        }
        if (xStr != null && !xStr.equals("")) {
            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            int w = Integer.parseInt(wStr);
            int h = Integer.parseInt(hStr);
            setBounds(x, y, w, h);
        }
        PGMLStackParser.setCommonAttrs(this, attributes);
        parser.registerFig(this, attributes.getValue("name"));
        ((Container) container).addObject(this);
        return new FigClassifierRoleHandler(parser, this);
    }

    /**
     * The width of the FigClassifierRole should be equal to the width of the
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
     * Set the line width.  Only change the line width of the outerbox
     * and the lifeline are changed. Values of zero are ignored.
     * @param w width.  Must be greater than zero.
     */
    public void setLineWidth(int w) {
        if (headFig.getLineWidth() != w && w != 0) {
            headFig.setLineWidth(w);
            lifeLineFig.setLineWidth(w);
            damage();
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        if (col != null && col != headFig.getFillColor()) {
            headFig.setFillColor(col);
            damage();
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean filled) {
        if (headFig.getFilled() != filled) {
            headFig.setFilled(filled);
            damage();
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return headFig.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return headFig.getFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return headFig.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return headFig.getLineWidth();
    }

    private FigLifeLine getLifeLineFig() {
        return lifeLineFig;
    }

    /*
     * @see FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        removeAllElementListeners();
        super.updateListeners(oldOwner, newOwner);
        if (newOwner != null) {
            addElementListener(newOwner);
            Iterator it = Model.getFacade().getBases(newOwner).iterator();
            while (it.hasNext()) {
                Object base = it.next();
                addElementListener(base, "name");
            }
            it = Model.getFacade().getStereotypes(newOwner).iterator();
            while (it.hasNext()) {
                Object stereo = it.next();
                addElementListener(stereo, "name");
            }
        }
    }

    void growToSize(int nodeCount) {
        grow(linkPositions.size(), nodeCount - linkPositions.size());
    }

    /**
     * Add count link spaces before nodePosition.
     */
    void grow(int nodePosition, int count) {
        for (int i = 0; i < count; ++i) {
            linkPositions.add(nodePosition, new MessageNode(this));
        }
        if (count > 0) {
            updateNodeStates();
            Rectangle r = getBounds();
            r.height += count * SequenceDiagramLayer.LINK_DISTANCE;
            setBounds(r);
            updateEdges();
        }
    }


    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        if (mee.getPropertyName().equals("name")) {
            if (mee.getSource() == getOwner()) {
            	updateClassifierRoleName();
            } else if (Model.getFacade().isAStereotype(mee.getSource())) {
                updateStereotypeText();
            } else {
            	updateBaseNames();
            }
            renderingChanged();
        } else if (mee.getPropertyName().equals("stereotype")) {
            updateStereotypeText();
            updateListeners(getOwner(), getOwner());
            renderingChanged();
        } else if (mee.getPropertyName().equals("base")) {
            updateBaseNames();
            updateListeners(getOwner(), getOwner());
            renderingChanged();
        }
    }

    /**
     * Remove a FigMessagePort that's associated with a removed FigMessage.
     *
     * @param fmp The FigMessagePort.
     */
    void removeFigMessagePort(FigMessagePort fmp) {
        fmp.getNode().setFigMessagePort(null);
        fmp.setNode(null);
        lifeLineFig.removeFig(fmp);
    }
    
    /**
     * Update an array of booleans to set node indexes that have associated
     * FigMessagePort to false.
     * @param start Index of first node in array
     * @param emptyNodes True where there is no FigMessagePort at the node
     * with the index in the array + start (at creation the entire array
     * is set to true)
     */
    void updateEmptyNodeArray(int start, boolean[] emptyNodes) {
        for (int i = 0; i < emptyNodes.length; ++i) {
            if (((MessageNode) linkPositions.get(i + start)).getFigMessagePort()
                    != null) {
                emptyNodes[i] = false;
            }
        }
    }

    /**
     * Remove nodes according to the emptyNodes array; contract total height
     * of fig.
     *
     * @param start Index of first node in array
     * @param emptyNodes True where there is no FigMessagePort at the node
     * with the index in the array + start
     */
    void contractNodes(int start, boolean[] emptyNodes) {
        int contracted = 0;
        for (int i = 0; i < emptyNodes.length; ++i) {
            if (emptyNodes[i]) {
                if (((MessageNode) linkPositions.get(i + start - contracted))
		        .getFigMessagePort()
		    != null) {
                    throw new IllegalArgumentException(
		    	"Trying to contract non-empty MessageNode");
		}
                linkPositions.remove(i + start - contracted);
                ++contracted;
            }
        }
        if (contracted > 0) {
            updateNodeStates();
            Rectangle r = getBounds();
            r.height -= contracted * SequenceDiagramLayer.LINK_DISTANCE;
            updateEdges();
            setBounds(r);
        }
    }

    private void updateBaseNames() {
	StringBuffer b = new StringBuffer();
	Iterator it = Model.getFacade().getBases(getOwner()).iterator();
	while (it.hasNext()) {
	    b.append(getBeautifiedName(it.next()));
	    if (it.hasNext()) {
		b.append(',');
	    }
	}
        baseNames = b.toString();
    }

    private void updateClassifierRoleName() {
        classifierRoleName = getBeautifiedName(getOwner());
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        updateBaseNames();
        updateClassifierRoleName();
        super.renderingChanged();
    }

    /**
     * Returns the port for a given coordinate pair. Normally
     * deepHitPort returns the owner of the fig in the FigGroup that
     * is present at the given coordinate pair (returning figs that
     * are added later first). In this case it returns a
     * MessagePort.<p>
     *
     * {@inheritDoc}
     */
    public Object deepHitPort(int x, int y) {
        Rectangle rect = new Rectangle(getX(), y - 16, getWidth(), 32);
        MessageNode foundNode = null;
        if (lifeLineFig.intersects(rect)) {
            for (int i = 0; i < linkPositions.size(); i++) {
                MessageNode node = (MessageNode) linkPositions.get(i);
                int position = lifeLineFig.getYCoordinate(i);
                if (i < linkPositions.size() - 1) {
                    int nextPosition =
                        lifeLineFig.getYCoordinate(i + 1);
                    if (nextPosition >= y && position <= y) {
                        if ((y - position) <= (nextPosition - y)) {
                            foundNode = node;
                        } else {
                            foundNode = (MessageNode) linkPositions.get(i + 1);
                        }
                        break;
                    }
                } else {
                    foundNode =
                        (MessageNode)
                            linkPositions.get(linkPositions.size() - 1);
                    MessageNode nextNode;
                    nextNode = new MessageNode(this);
                    linkPositions.add(nextNode);
                    int nextPosition = lifeLineFig.getYCoordinate(i + 1);
                    if ((y - position) >= (nextPosition - y)) {
                        foundNode = nextNode;
                    }
                    break;
                }
            }
        } else if (headFig.intersects(rect)) {
            foundNode = getClassifierRoleNode();
        } else {
            return null;
        }
        setMatchingFig(foundNode);
        return foundNode;
    }

    int getYCoordinate(MessageNode node) {
        return lifeLineFig.getYCoordinate(linkPositions.indexOf(node));
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object own) {
        super.setOwner(own);
        bindPort(own, headFig);
    }

    /**
     * Returns the index of a given node.
     *
     * @param node is the given node
     * @return the index
     */
    public int getIndexOf(MessageNode node) {
        return linkPositions.indexOf(node);
    }

    /**
     * Returns the node that's next to the given node.
     *
     * @param node is the given node
     * @return the Node
     */
    public MessageNode nextNode(MessageNode node) {
        if (getIndexOf(node) < linkPositions.size()) {
            return (MessageNode) linkPositions.get(getIndexOf(node) + 1);
        }
	return null;
    }

    /**
     * Returns the node that's before the given node in the nodes list.
     *
     * @param node is the given node
     * @return the node
     */
    public MessageNode previousNode(MessageNode node) {
        if (getIndexOf(node) > 0) {
            return (MessageNode) linkPositions.get(getIndexOf(node) - 1);
        }
	return null;
    }
    
    /*
     * @see org.tigris.gef.presentation.FigNode#getPortFig(java.lang.Object)
     */
    public Fig getPortFig(Object messageNode) {
        if (Model.getFacade().isAClassifierRole(messageNode)) {
            LOG.warn("Got a ClassifierRole - only legal on load");
            return null;
        }

        if (!(messageNode instanceof MessageNode)) {
            throw new IllegalArgumentException(
                    "Expecting a MessageNode but got a "
                    + messageNode.getClass().getName());
        }

        setMatchingFig((MessageNode) messageNode);

        if (((MessageNode) messageNode).getFigMessagePort() != null) {
            return ((MessageNode) messageNode).getFigMessagePort();
        }
        return new TempFig(
                messageNode, lifeLineFig.getX(),
                getYCoordinate((MessageNode) messageNode),
                lifeLineFig.getX() + WIDTH);
    }

    /**
     * Returns the ClassifierRoleNode. This is the port that represents the
     * object Figrect.
     *
     * @return the ClassifierRoleNode.
     */
    private MessageNode getClassifierRoleNode() {
        return (MessageNode) linkPositions.get(0);
    }

    /**
     * Adds a node at the given position.
     *
     * @param position the position in which the node will be added
     * @param node the node to be added
     */
    public void addNode(int position, MessageNode node) {
        linkPositions.add(position, node);
        Iterator it =
            linkPositions
                .subList(position + 1, linkPositions.size())
                .iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MessageNode) {
                FigMessagePort figMessagePort =
		    ((MessageNode) o).getFigMessagePort();
                if (figMessagePort != null) {
                    figMessagePort.setY(
                        figMessagePort.getY()
                            + SequenceDiagramLayer.LINK_DISTANCE);
                }
            }
        }
        calcBounds();
    }

    /**
     * Gets a node that has the given position (creates new nodes if needed).
     *
     * @param position the position of the resulting node
     *
     * @return the node with the given position
     */
    public MessageNode getNode(int position) {
        if (position < linkPositions.size()) {
            return (MessageNode) linkPositions.get(position);
        }
        MessageNode node = null;
        for (int cnt = position - linkPositions.size(); cnt >= 0; cnt--) {
            node = new MessageNode(this);
            linkPositions.add(node);
        }
        calcBounds();
        return node;
    }

    /*
     * Override to return a custom SelectionResize class that will not allow
     * handles on the north edge to be dragged.
     *
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionClassifierRole(this);
    }

    static class TempFig extends FigLine {
        /**
         * Constructor.
         *
         * @param owner
         * @param x
         * @param y
         * @param x2
         */
        TempFig(Object owner, int x, int y, int x2) {
            super(x, y, x2, y);
            setVisible(false);
            setOwner(owner);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = 1478952234873792638L;
    }

    static class FigClassifierRoleHandler extends FigGroupHandler {
        /**
         * Constructor.
         *
         * @param parser
         * @param classifierRole
         */
        FigClassifierRoleHandler(PGMLStackParser parser,
                FigClassifierRole classifierRole) {
            super(parser, classifierRole);
        }

        /*
         * @see org.tigris.gef.persistence.pgml.BaseHandler#getElementHandler(
         *      org.tigris.gef.persistence.pgml.HandlerStack, java.lang.Object,
         *      java.lang.String, java.lang.String, java.lang.String,
         *      org.xml.sax.Attributes)
         */
        protected DefaultHandler getElementHandler(
                HandlerStack stack,
                Object container,
                String uri,
                String localname,
                String qname,
                Attributes attributes) throws SAXException {

            DefaultHandler result = null;
            String description = attributes.getValue("description");
            if (qname.equals("group")
                    && description != null
                    && description.startsWith(FigLifeLine.class.getName())) {
                FigClassifierRole fcr = (FigClassifierRole)
                    ((FigGroupHandler) container).getFigGroup();
                result = new FigLifeLineHandler(
                            (PGMLStackParser) stack, fcr.getLifeLineFig());
            } else if (qname.equals("group")
                        && description != null
                        && description.startsWith(
                                FigMessagePort.class.getName())) {
                // TODO: This if-else-block exists in order
                // to load sequence diagrams
                // from 0.20. It must exist until -
                // http://argouml.tigris.org/issues/show_bug.cgi?id=4039
                PGMLStackParser parser = (PGMLStackParser) stack;
                String ownerRef = attributes.getValue("href");
                Object owner = parser.findOwner(ownerRef);
                FigMessagePort fmp = new FigMessagePort(owner);
                FigClassifierRole fcr =
                    (FigClassifierRole)
                        ((FigGroupHandler) container).getFigGroup();
                fcr.getLifeLineFig().addFig(fmp);
                result = new FigGroupHandler((PGMLStackParser) stack, fmp);
                PGMLStackParser.setCommonAttrs(fmp, attributes);
                parser.registerFig(fmp, attributes.getValue("name"));
            } else {
                result =
                    ((PGMLStackParser) stack).getHandler(stack,
							 container,
							 uri,
							 localname,
							 qname,
							 attributes);
            }
            return result;
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {

        Rectangle rect = headFig.getBounds();

        getStereotypeFig().setOwner(getOwner());
        ((FigStereotypesCompartment) getStereotypeFig()).populate();

        int minWidth = headFig.getMinimumSize().width;
        if (minWidth > rect.width) {
            rect.width = minWidth;
        }
        
        int headHeight = headFig.getMinimumSize().height;

        headFig.setBounds(
                rect.x,
                rect.y,
                rect.width,
                headHeight);

        if (getLayer() == null) {
            return;
        }

        int h = MIN_HEAD_HEIGHT;
        List figs = getLayer().getContents();
        for (Iterator i = figs.iterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof FigClassifierRole) {
                FigClassifierRole other = (FigClassifierRole) o;
                int otherHeight = other.headFig.getMinimumHeight();
                if (otherHeight > h) {
                    h = otherHeight;
                }
            }
        }

        int height = headFig.getHeight() + lifeLineFig.getHeight();
        
        setBounds(
                headFig.getX(),
                headFig.getY(),
                headFig.getWidth(),
                height);
        calcBounds();
        
        // Set all other CLassifierRoles to be the same height as this one
        // now is
        Layer layer = getLayer();
        List layerFigs = layer.getContents();
        for (Iterator i = layerFigs.iterator(); i.hasNext();) {
            Object o = i.next();
            if (o instanceof FigClassifierRole && o != this) {
                FigClassifierRole other = (FigClassifierRole) o;
                other.setHeight(height);
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 7763573563940441408L;
}
