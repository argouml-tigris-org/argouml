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

package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.argouml.model.Model;
import org.argouml.model.ModelEventPump;
import org.argouml.uml.diagram.sequence.MessageNode;
import org.argouml.uml.diagram.ui.FigNodeModelElement;

import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Selection;
import org.tigris.gef.persistence.pgml.Container;
import org.tigris.gef.persistence.pgml.FigGroupHandler;
import org.tigris.gef.persistence.pgml.HandlerFactory;
import org.tigris.gef.persistence.pgml.HandlerStack;
import org.tigris.gef.persistence.pgml.PGMLStackParser;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

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
     * The list where the nodes to which links can be attached are stored
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
     * The set of activation figs
     */
    private HashSet activationFigs;

    /**
     * Default constructor. Constructs the object rectangle, the lifeline,
     * the name box and the stereotype box.
     */
    public FigClassifierRole() {
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
        linkPositions.add(new MessageNode( this));
        for (int i = 0;
            i <= lifeLine.getHeight() / SequenceDiagramLayout.LINK_DISTANCE;
            i++) {
            linkPositions.add(new MessageNode( this));
        }
        addFig(lifeLine);
        addFig(backgroundBox);
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(outerBox);
        activationFigs=new HashSet();
    }

    /**
     * @param node the owner
     */
    public FigClassifierRole(Object node) {
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
            (classifierRoleName + ":" + baseNames)
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

    int getNodeCount()
    {
        return linkPositions.size();
    }

    /**
     * Change a node to point to an actual FigMessagePort
     */
    FigMessagePort createFigMessagePort( Object owner, Fig fig)
    {
        TempFig tempFig=(TempFig)fig;
        MessageNode node=(MessageNode)fig.getOwner();
        FigMessagePort fmp=new FigMessagePort( tempFig.getX1(), tempFig.getY1(),
                                               tempFig.getX2());
        node.setFigMessagePort( fmp);
        fmp.setNode( node);
        fmp.setOwner( owner);
        addFig(fmp);
        updateNodeStates();
        return fmp;
    }

    /**
     * Connect a FigMessagePort with a MessageNode by position
     */
    void setMatchingNode( FigMessagePort fmp)
    {
        while ( getYCoordinate( getNodeCount()-1)<fmp.getY1())
            growToSize( getNodeCount()+10);
        int i=0;
        for ( Iterator it=linkPositions.iterator(); it.hasNext(); ++i)
        {
            MessageNode node=(MessageNode)it.next();
            if ( getYCoordinate( i)==fmp.getY1())
            {
                node.setFigMessagePort( fmp);
                fmp.setNode( node);
                updateNodeStates();
                break;
            }
        }
    }

    /**
     * Set the node's fig to a FigMessagePort if one is available
     */
    private void setMatchingFig( MessageNode node)
    {
        if ( node.getFigMessagePort()==null)
        {
            int y = getYCoordinate(node);
            for (Iterator it = getFigs().iterator(); it.hasNext();)
            {
                Fig fig=(Fig)it.next();
                if ( fig instanceof FigMessagePort){
                    FigMessagePort fmp=(FigMessagePort)fig;
                    if (fmp.getY1() == y) {
                        node.setFigMessagePort( fmp);
                        fmp.setNode( node);
                        updateNodeStates();
                    }
                }
            }
        }
    }

    private void center(FigText figText) {
        int newX = this.getX() + this.getWidth()/2 - figText.getWidth()/2;
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
		      + getNameFig().getHeight()/2));
        getStereotypeFig().setCenter(
            new Point(
                x + w / 2,
                (getStereotypeFig().getY() - oldBounds.y
		 + y + getStereotypeFig().getHeight()/2)));
        reSize(outerBox, x, y, w, h);
        reSize(backgroundBox, x, y, w, h);
        lifeLine.setBounds(
            outerBox.getX() + outerBox.getWidth()/2,
            outerBox.getY() + outerBox.getHeight(),
            0,
            h - outerBox.getHeight());
        for ( Iterator figIt=getFigs().iterator(); figIt.hasNext();)
        {
            Fig fig=(Fig)figIt.next();
            if ( activationFigs.contains( fig) || fig instanceof FigMessagePort)
            {
                fig.setBounds(
                    outerBox.getX() + outerBox.getWidth()/2 - fig.getWidth()/2,
                    y - oldBounds.y + fig.getY(),
                    fig.getWidth(),
                    fig.getHeight());
            }
        }
        calcBounds(); //_x = x; _height = y; _w = w; _h = h;
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * This method is overridden in order to ignore change of y co-ordinate
     * during drag.
     */
    public void superTranslate( int dx, int dy) {
        setBounds(getX()+dx, getY(), getWidth(), getHeight());
    }

    /**
     * Scales the given fig that must be part of this FigClassifierRole.<p>
     *
     * @param f the fig to scale
     * @param x the new x coordinate for the FigClassifierRole
     * @param y the new y coordinate for the FigClassifierRole
     * @param w the new w coordinate for the FigClassifierRole
     * @param h the new h coordinate for the FigClassifierRole
     */
    private void reSize(Fig f, int x, int y, int w, int h) {
        if (f.isVisible()) {
            int newX =
                (_w == 0)
                    ? x
                    : x + (int) ((f.getX() - _x) * ((float) w / (float) _w));
            int newY = f.getY()+y-_y;
            int newW =
                (_w == 0)
                    ? 0
                    : (int) (((float) f.getWidth()) * ((float) w / (float) _w));

            int newH = f.getHeight();

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
        String name = Model.getFacade().getName(o);
        if (name == null || name.equals("")) {
            name = "(anon " + Model.getFacade().getUMLClassName(o) + ")";
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

    static public boolean isCallMessage( Object message)
    {
        return Model.getFacade().isACallAction( Model.getFacade().getAction( message));
    }

    static public boolean isReturnMessage( Object message)
    {
        return Model.getFacade().isAReturnAction( Model.getFacade().getAction( message));
    }

    static public boolean isCreateMessage( Object message)
    {
        return Model.getFacade().isACreateAction( Model.getFacade().getAction( message));
    }

    static public boolean isDestroyMessage( Object message)
    {
        return Model.getFacade().isADestroyAction( Model.getFacade().getAction( message));
    }

    private void setPreviousState( int start, int newState)
    {
        for ( int i=start-1; i>=0; --i)
        {
            MessageNode node=(MessageNode)linkPositions.get( i);
            if ( node.getFigMessagePort()!=null)
            {
                break;
            }
            else
                node.setState( newState);
        }
    }

    private int setFromActionNode( int lastState, int offset)
    {
        if ( lastState==MessageNode.INITIAL)
        {
            setPreviousState( offset, lastState=MessageNode.DONE_SOMETHING_NO_CALL);
        }
        else if ( lastState==MessageNode.IMPLICIT_RETURNED)
        {
            setPreviousState( offset, lastState=MessageNode.CALLED);
        }
        else if ( lastState==MessageNode.IMPLICIT_CREATED)
        {
            setPreviousState( offset, lastState=MessageNode.CREATED);
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
     * <p>
     * If the state is INITIAL, the state for that and prior
     * nodes becomes DONE_SOMETHING_NO_CALL<p>
     * else if the state is IMPLICIT_RETURNED, the state for that
     * and prior nodes becomes CALLED<p>
     * Otherwise, the state doesn't change
     * <p>
     * If the classifier is called:<p>
     * <p>
     * If the caller list is empty, the state becomes CALLED
     * <p>
     * The caller is added to the caller list
     * <p>
     * If the classifier returns, the caller being returned to and any callers
     * added to the list since that call are removed from the caller list.
     * If the caller list is empty the state becomes RETURNED.
     * <p>
     * If nothing happens on the node:<p>
     * If the previous state was CALLED, the state becomes IMPLICIT_RETURNED<p>
     * Otherwise, the state is the same as the previous node's state<p>
     * Start or stop a rectangle when the state changes.
     *
     */
    private void updateNodeStates()
    {
        int lastState=MessageNode.INITIAL;
        ArrayList callers=null;
        int nodeCount=linkPositions.size();
        Rectangle fmpBounds=new Rectangle();

        for ( int i=0; i<nodeCount; ++i)
        {
            MessageNode node=(MessageNode)linkPositions.get(i);
            FigMessagePort fmp=node.getFigMessagePort();
            // If the node has a FigMessagePort
            if ( fmp!=null)
            {
                fmpBounds=fmp.getBounds( fmpBounds);
                int fmpY=getYCoordinate( i);
                if ( fmpBounds.y!=fmpY)
                {
                    fmpBounds.y=fmpY;
                    fmp.setBounds( fmpBounds);
                }
                Object message=fmp.getOwner();
                boolean selfMessage=( Model.getFacade().isAMessage( message) &&
                Model.getFacade().getSender( message)==
                Model.getFacade().getReceiver( message));
                boolean selfReceiving=false;
                if ( selfMessage)
                {
                    for ( int j=i-1; j>=0; --j)
                    {
                        MessageNode prev=(MessageNode)linkPositions.get(j);
                        FigMessagePort prevmp=prev.getFigMessagePort();
                        if ( prevmp!=null && prevmp.getOwner()==message)
                            selfReceiving=true;
                    }
                }
                if ( isCallMessage( message))
                {
                    if ( Model.getFacade().getSender( message)==getOwner()
                         && ! selfReceiving)
                    {
                        lastState=setFromActionNode( lastState, i);
                        node.setState( lastState);
                        node.setCallers( callers);
                    }
                    else
                    {
                        if ( lastState==MessageNode.INITIAL || lastState==MessageNode.CREATED ||
                             lastState==MessageNode.IMPLICIT_CREATED ||
                             lastState==MessageNode.IMPLICIT_RETURNED ||
                             lastState==MessageNode.RETURNED)
                            lastState=MessageNode.CALLED;
                        if ( callers==null)
                            callers=new ArrayList();
                        else
                            callers=new ArrayList(callers);
                        callers.add( Model.getFacade().getSender( message));
                        node.setState( lastState);
                        node.setCallers( callers);
                    }
                }
                else if ( isReturnMessage( message))
                {
                    if (lastState == MessageNode.IMPLICIT_RETURNED)
                    {
                        setPreviousState(i, MessageNode.CALLED);
                        lastState=MessageNode.CALLED;
                    }
                    if ( Model.getFacade().getSender( message)==getOwner() &&
                         ! selfReceiving)
                    {
                        Object caller=Model.getFacade().getReceiver( message);
                        int callerIndex=callers.lastIndexOf( caller);
                        if ( callerIndex!= -1)
                        {
                            for ( int backNodeIndex=i-1;
                                  backNodeIndex>0 &&
                                  ((MessageNode)linkPositions.get( backNodeIndex)).matchingCallerList( caller, callerIndex);
                                  --backNodeIndex);
                            if (callerIndex == 0) {
                                callers = null;
                                if (lastState == MessageNode.CALLED)
                                    lastState = MessageNode.RETURNED;
                            }
                            else {
                                callers = new ArrayList(callers.subList(0,
                                    callerIndex));
                            }
                        }
                    }
                    node.setState( lastState);
                    node.setCallers( callers);
                }
                else if ( isCreateMessage( message))
                {
                    if ( Model.getFacade().getSender( message)==getOwner())
                    {
                        lastState=setFromActionNode( lastState, i);
                        node.setState( lastState);
                        node.setCallers( callers);
                    }
                    else
                    {
                        lastState=MessageNode.CREATED;
                        setPreviousState( i, MessageNode.PRECREATED);
                        node.setState( lastState);
                        node.setCallers( callers);
                    }
                }
                else if ( isDestroyMessage( message))
                {
                    if ( Model.getFacade().getSender( message)==getOwner()
                         && ! selfReceiving)
                    {
                        lastState=setFromActionNode( lastState, i);
                        node.setState( lastState);
                        node.setCallers( callers);
                    }
                    else
                    {
                        lastState=MessageNode.DESTROYED;
                        callers=null;
                        node.setState( lastState);
                        node.setCallers( callers);
                    }
                }
            }
            else
            {
                if ( lastState==MessageNode.CALLED)
                    lastState=MessageNode.IMPLICIT_RETURNED;
                if ( lastState==MessageNode.CREATED)
                    lastState=MessageNode.IMPLICIT_CREATED;
                node.setState( lastState);
                node.setCallers( callers);
            }
        }
    }

    private void removeActivations() {
        ArrayList activations=new ArrayList( activationFigs);
        activationFigs.clear();
        for ( Iterator it=activations.iterator();
              it.hasNext();
              )
            removeFig( (Fig)it.next());
        calcBounds();
    }

    private void addActivationFig( Fig f)
    {
        addFig( f);
        activationFigs.add( f);
    }

    private void addActivations() {
        MessageNode startActivationNode = null;
        MessageNode endActivationNode = null;
        int lastState=MessageNode.INITIAL;
        boolean startFull=false;
        boolean endFull=false;
        int nodeCount=linkPositions.size();
        int x = lifeLine.getX() - WIDTH / 2;
        for ( int i=0; i<nodeCount; ++i)
        {
            MessageNode node = (MessageNode) linkPositions.get(i);
            int nextState = node.getState();
            if ( lastState!=nextState && nextState==MessageNode.CREATED)
            {
                FigRect birthFig=new FigRect( lifeLine.getX()-WIDTH/2,
                                              getYCoordinate( i)-
                                              SequenceDiagramLayout.LINK_DISTANCE/4,
                                              WIDTH,
                                              SequenceDiagramLayout.LINK_DISTANCE/4);
                birthFig.setFilled( true);
                birthFig.setFillColor( Color.BLACK);
                addActivationFig( birthFig);
            }
            if ( lastState!=nextState && nextState==MessageNode.DESTROYED)
            {
                int y=getYCoordinate( i)-SequenceDiagramLayout.LINK_DISTANCE/2;
                addActivationFig( new FigLine( x, y+SequenceDiagramLayout.LINK_DISTANCE/2, x+WIDTH,
                                               y+SequenceDiagramLayout.LINK_DISTANCE));
                addActivationFig( new FigLine( x, y+SequenceDiagramLayout.LINK_DISTANCE,
                                               x+WIDTH, y+SequenceDiagramLayout.LINK_DISTANCE/2));
            }
            if (startActivationNode == null) {
                switch (nextState) {
                    case MessageNode.DONE_SOMETHING_NO_CALL:
                        startActivationNode = node;
                        startFull=true;
                        break;
                    case MessageNode.CALLED:
                    case MessageNode.CREATED:
                        startActivationNode = node;
                        startFull=false;
                }
            }
            else
            {
                switch (nextState) {
                    case MessageNode.DESTROYED :
                    case MessageNode.RETURNED :
                        endActivationNode=node;
                        endFull=false;
                        break;
                    case MessageNode.IMPLICIT_RETURNED :
                    case MessageNode.IMPLICIT_CREATED :
                        endActivationNode=(MessageNode)linkPositions.get( i-1);
                        endFull=true;
                        break;
                    case MessageNode.CALLED :
                        if ( lastState==MessageNode.CREATED)
                        {
                            endActivationNode=(MessageNode)linkPositions.get( i-1);
                            endFull=false;
                            --i;
                            nextState=lastState;
                        }
                        break;
                }
            }
            lastState=nextState;
            if ( startActivationNode!=null && endActivationNode!=null)
            {
                if ( startActivationNode!=endActivationNode ||
                     startFull || endFull)
                {
                    int y1 = getYCoordinate(startActivationNode);
                    if (startFull) {
                        y1 -= SequenceDiagramLayout.LINK_DISTANCE / 2;
                    }
                    int y2 = getYCoordinate(endActivationNode);
                    if (endFull) {
                        y2 += SequenceDiagramLayout.LINK_DISTANCE / 2;
                    }
                    addActivationFig(new FigRect(x, y1, WIDTH, y2 - y1));
                }
                startActivationNode=endActivationNode=null;
                startFull=endFull=false;
            }
        }
        if ( startActivationNode!=null)
        {
            endActivationNode=(MessageNode)linkPositions.get( nodeCount-1);
            endFull=true;
            int y1 = getYCoordinate(startActivationNode);
            if (startFull) {
                y1 -= SequenceDiagramLayout.LINK_DISTANCE / 2;
            }
            int y2 = getYCoordinate(endActivationNode);
            if (endFull) {
                y2 += SequenceDiagramLayout.LINK_DISTANCE / 2;
            }
            addActivationFig(new FigRect(x, y1, WIDTH, y2 - y1));
            startActivationNode=endActivationNode=null;
            startFull=endFull=false;
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

    //////////////////////////////////////////////////////////////////////////
    // HandlerFactory implementation
    public DefaultHandler getHandler( HandlerStack stack,
                               Object container,
                               String uri,
                               String localname,
                               String qname,
                               Attributes attributes)
    throws SAXException
    {
        PGMLStackParser parser=(PGMLStackParser)stack;
        StringTokenizer st = new StringTokenizer(attributes.getValue( "description"), ",;[] ");
        if ( st.hasMoreElements()) st.nextToken();
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
        if(xStr != null && !xStr.equals("")) {
            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            int w = Integer.parseInt(wStr);
            int h = Integer.parseInt(hStr);
            setBounds(x, y, w, h);
        }
        PGMLStackParser.setCommonAttrs( this, attributes);
        String ownerRef=attributes.getValue( "href");
        if ( ownerRef!=null)
        {
            Object owner=parser.findOwner( ownerRef);
            if ( owner!=null)
                setOwner( owner);
        }
        parser.registerFig( this, attributes.getValue( "name"));
        ((Container)container).addObject( this);
        return new FigClassifierRoleHandler( parser, this);
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
        if (Model.getFacade().isAClassifierRole(newOwner)) {
            Object oldOwner = getOwner();
            ModelEventPump pump = Model.getPump();
            pump.removeModelEventListener(this, oldOwner);
            pump.addModelEventListener(this,
				       newOwner,
				       new String[] {
					   "name",
					   "stereotype",
                       "base"
				       });
            if ( oldOwner != null)
            {
                Iterator it = Model.getFacade().getBases(oldOwner).iterator();
                while (it.hasNext()) {
                    pump.removeModelEventListener(this, it.next());
                }
            }
			Iterator it = Model.getFacade().getBases( newOwner).iterator();
			String[] names=new String[] { "name" };
			while (it.hasNext()) {
				Object base=it.next();
				pump.removeModelEventListener( this, base);
				pump.addModelEventListener( this, base, names);
			}
        }
    }

    void growToSize( int nodeCount)
    {
        grow( linkPositions.size(), nodeCount-linkPositions.size());
    }

    /**
     * Add count link spaces before nodePosition
     */
    void grow( int nodePosition, int count)
    {
        for ( int i=0; i<count; ++i)
        {
            linkPositions.add( nodePosition, new MessageNode( this));
        }
        if ( count>0)
        {
            updateNodeStates();
            Rectangle r=getBounds();
            r.height+=count*SequenceDiagramLayout.LINK_DISTANCE;
            setBounds( r);
            updateEdges();
        }
    }


    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        boolean nameChanged = false;
        if ( mee.getPropertyName().equals("name")) {
            if ( mee.getSource()==getOwner())
            	updateClassifierRoleName();
            else
            	updateBaseNames();
            nameChanged = true;
        } else if ( mee.getPropertyName().equals("stereotype")) {
            updateStereotypeText();
        } else if ( mee.getPropertyName().equals( "base")) {
            updateBaseNames();
            updateListeners( getOwner());
            nameChanged = true;
        }
        if (nameChanged) {
            updateNameText();
        }
    }

    /**
     * Remove a FigMessagePort that's associated with a removed FigMessage
     */
    void removeFigMessagePort( FigMessagePort fmp)
    {
        fmp.getNode().setFigMessagePort( null);
        fmp.setNode(null);
        removeFig( fmp);
        updateNodeStates();
    }

    /**
     * Update an array of booleans to set node indexes that have associated
     * FigMessagePort to false.
     * @param start Index of first node in array
     * @param emptyNodes True where there is no FigMessagePort at the node
     * with the index in the array + start (at creation the entire array
     * is set to true)
     */
    void updateEmptyNodeArray( int start, boolean[] emptyNodes)
    {
        for ( int i=0; i<emptyNodes.length; ++i)
        {
            if ( ((MessageNode)linkPositions.get(i+start)).getFigMessagePort()!=null)
                emptyNodes[i]=false;
        }
    }

    /**
     * Remove nodes according to the emptyNodes array; contract total height
     * of fig
     * @param start Index of first node in array
     * @param emptyNodes True where there is no FigMessagePort at the node
     * with the index in the array + start
     */
    void contractNodes( int start, boolean[] emptyNodes)
    {
        int contracted=0;
        for ( int i=0; i<emptyNodes.length; ++i)
        {
            if ( emptyNodes[i])
            {
                if ( ((MessageNode)linkPositions.get(i+start-contracted)).getFigMessagePort()!=null)
                    throw new IllegalArgumentException( "Trying to contract non-empty MessageNode");
                linkPositions.remove( i+start-contracted);
                ++contracted;
            }
        }
        if ( contracted>0)
        {
            updateNodeStates();
            Rectangle r=getBounds();
            r.height-=contracted*SequenceDiagramLayout.LINK_DISTANCE;
            updateEdges();
            setBounds( r);
        }
    }

    private void updateBaseNames() {
		StringBuffer b=new StringBuffer();
		Iterator it = Model.getFacade().getBases( getOwner()).iterator();
		while (it.hasNext()) {
			b.append( getBeautifiedName(it.next()));
			if (it.hasNext()) {
				b.append(',');
			}
		}
        baseNames = b.toString();
    }

    private void updateClassifierRoleName() {
        classifierRoleName = getBeautifiedName(getOwner());
    }

    /**
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
     * @see org.tigris.gef.presentation.FigNode#deepHitPort(int, int)
     */
     public Object deepHitPort(int x, int y) {
        Rectangle rect = new Rectangle(getX(), y - 16, getWidth(), 32);
        MessageNode foundNode = null;
        if (lifeLine.intersects(rect)) {
            for (int i = 0;
                i < linkPositions.size();
                i++) {
                MessageNode node = (MessageNode) linkPositions.get(i);
                int position = getYCoordinate( i);
                if (i < linkPositions.size() - 1) {
                    int nextPosition =
                        getYCoordinate( i + 1);
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
                        (MessageNode) linkPositions.get(linkPositions.size() - 1);
                    MessageNode nextNode;
                    linkPositions.add( nextNode=new MessageNode( this));
                    int nextPosition=getYCoordinate( i+1);
                    if ( ( y-position)>=(nextPosition-y))
                        foundNode=nextNode;
                    break;
                }
            }

        } else if (outerBox.intersects(rect)) {
            foundNode=getClassifierRoleNode();
        } else {
            return null;
        }
        setMatchingFig( (MessageNode)foundNode);
        return foundNode;
    }

    private int getYCoordinate(MessageNode node) {
        return getYCoordinate( linkPositions.indexOf( node));
    }

    private int getYCoordinate( int nodeIndex) {
        return
            nodeIndex * SequenceDiagramLayout.LINK_DISTANCE
                + getY()
                + outerBox.getHeight()
                + SequenceDiagramLayout.LINK_DISTANCE/2;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object own) {
        super.setOwner(own);
        bindPort(own, outerBox);
    }

    /**
     * Gets the {@link FigMessage} that is attached to the given
     * {@link FigMessagePort}.
     *
     * @param portFig the given port
     * @return the {@link FigMessage} that is attached.
     */
    public FigMessage getFigMessage(FigMessagePort portFig) {
        Iterator it = getFigEdges(null).iterator();
        while (it.hasNext()) {
            FigEdge figEdge = (FigEdge) it.next();
            if (figEdge instanceof FigMessage
                && (figEdge.getSourcePortFig() == portFig
                    || figEdge.getDestPortFig() == portFig)) {
                return (FigMessage) figEdge;
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
        if (getIndexOf(node) < linkPositions.size())
            return (MessageNode) linkPositions.get(getIndexOf(node) + 1);
        else
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
        } else
            return null;
    }

    /**
     * @see org.tigris.gef.presentation.FigNode#getPortFig(java.lang.Object)
     */
    public Fig getPortFig(Object np) {
        if ( np==null)
            return null;
        if (np instanceof MessageNode) {
            setMatchingFig( (MessageNode)np);
        }
        if ( ( np instanceof MessageNode) && ((MessageNode)np).getFigMessagePort()!= null)
        {
            return ((MessageNode) np).getFigMessagePort();
        }
        else if ( Model.getFacade().isAMessage( np))
        {
            for ( Iterator it=getFigs().iterator(); it.hasNext();)
            {
                Fig fig=(Fig)it.next();
                if ( fig.getOwner()==np)
                    return fig;
            }
        }
        else if ( np instanceof MessageNode)
        {
            return new TempFig(np, lifeLine.getX() - WIDTH / 2,
                               getYCoordinate( (MessageNode) np),
                               lifeLine.getX() + WIDTH / 2);
        }
        return null;
    }

    /**
     * Returns the ClassifierRoleNode. This is the port that represents the
     * object Figrect.
     *
     * @return the ClassifierRoleNode.
     */
    public MessageNode getClassifierRoleNode() {
        return (MessageNode)linkPositions.get(0);
    }

    /**
     * Removes the fig from both the figs list as from the
     * activationFigs set.  This insures
     * that removal will indeed remove all 'pointers' to the object.<p>
     *
     * @see org.tigris.gef.presentation.FigGroup#removeFig(Fig)
     */
    public void removeFig(Fig f) {
        super.removeFig(f);
        activationFigs.remove(f);
    }

    /**
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
                FigMessagePort figMessagePort = ((MessageNode) o).getFigMessagePort();
                if (figMessagePort != null) {
                    figMessagePort.setY(
                        figMessagePort.getY()
                            + SequenceDiagramLayout.LINK_DISTANCE);
                }
            }
        }
        calcBounds();
    }
    
    
    
    /**
     * Override to return a custom SelectionResize class that will not allow
     * handles on the north edge to be dragged.
     */
    public Selection makeSelection() {
        return new SelectionClassifierRole(this);
    }

    static class TempFig extends FigLine
    {
        TempFig( Object owner, int x, int y, int x2)
        {
            super( x, y, x2, y);
            setVisible( false);
            setOwner( owner);
        }
    }

    static class FigClassifierRoleHandler extends FigGroupHandler
    {
        FigClassifierRoleHandler( PGMLStackParser parser, FigClassifierRole
                                  classifierRole)
        {
            super( parser, classifierRole);
        }

        protected DefaultHandler getElementHandler(HandlerStack stack,
            Object container,
            String uri,
            String localname,
            String qname,
            Attributes attributes)
        throws SAXException {
            DefaultHandler result = null;
            if (qname.equals("group")) {
                FigMessagePort fmp = new FigMessagePort();
                ((FigGroupHandler)container).getFigGroup().addFig(fmp);
                result = new FigGroupHandler( (PGMLStackParser) stack, fmp);
                PGMLStackParser parser=(PGMLStackParser)stack;
                PGMLStackParser.setCommonAttrs( fmp, attributes);
                String ownerRef=attributes.getValue( "href");
                if ( ownerRef!=null)
                {
                    Object owner=parser.findOwner( ownerRef);
                    if ( owner!=null)
                        fmp.setOwner( owner);
                }
                parser.registerFig( fmp, attributes.getValue( "name"));
            }
            else {
                result = ( (PGMLStackParser) stack).getHandler(
                    stack,
                    container,
                    uri,
                    localname,
                    qname,
                    attributes);
            }
            return result;
        }
    }
}
