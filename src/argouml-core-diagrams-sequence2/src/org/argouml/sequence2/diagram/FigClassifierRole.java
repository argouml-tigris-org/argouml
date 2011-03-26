/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2009 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.sequence2.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigEmptyRect;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Geometry;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigRect;

/**
 * The Fig that represents a Classifier Role
 * @author penyaskito
 */
public class FigClassifierRole extends FigNodeModelElement {

    /**
     * This is an empty rectangle placed above HeadFig. It creates a space
     * between where the classifierRole would normally start and where it
     * starts as the result of a create message.
     */
    private FigEmptyRect emptyFig;
    /**
     * This is the box head of the classifierRole containing the notation
     * and stereotypes.
     */
    private FigHead headFig;
    /**
     * This is the dashed lifeline under FigHead that contain the activation
     * blocks.
     */
    private FigLifeLine lifeLineFig;
    
    // TODO: Do we need this? Is this the same as emptyFig.getHeight()?
    private int offset = 0;
    
    /**
     * The minimum height of the classifier role.
     */
    private int minimumHeight;

    /**
     * Construct a use case figure with the given owner, bounds, and rendering 
     * settings.  This constructor is used by the PGML parser.
     * 
     * @param owner owning model element
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigClassifierRole(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings);
        initialize();
        if (bounds != null) {
            setLocation(bounds.x, bounds.y);
        }
    }
    
    @Override
    protected Fig createBigPortFig() {
        return new FigClassifierRolePort();
    }

    /**
     * Initialization which is common to multiple constructors.
     */
    private void initialize() {
        emptyFig = new FigEmptyRect(getX(), getY(), getWidth(), offset);
        emptyFig.setLineWidth(0);
        
        headFig = new FigHead(getOwner(), getSettings(), getStereotypeFig(),
                getNameFig());
        headFig.setBounds(getX(), getY() + offset,
                getWidth(), headFig.getHeight());
        
        lifeLineFig = new FigLifeLine(getOwner(), new Rectangle(headFig.getX(),
                getY() + offset + headFig.getHeight() - getLineWidth(), 0, 0),
                getSettings());
        
        addFig(getBigPort());        
        getBigPort().setVisible(false);

        // TODO: Move magic number 10 to descriptive constant
        minimumHeight = headFig.getMinimumHeight() + 10;
        
        addFig(emptyFig);        
        addFig(lifeLineFig);
        addFig(headFig);
        
        createActivations();
    }

    /**
     * The NotationProvider for the ClassifierRole. <p>
     * 
     * The syntax is for UML is:
     * <pre>
     * baselist := [base] [, base]*
     * classifierRole := [name] [/ role] [: baselist]
     * </pre></p>
     * 
     * The <code>name</code> is the Instance name, not used currently.
     * See ClassifierRoleNotationUml for details.<p>
     *
     * This syntax is compatible with the UML 1.4 specification.
     * @return TYPE_CLASSIFIERROLE
     */
    @Override
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_CLASSIFIERROLE;
    }

    @Override
    protected void setBoundsImpl(
            final int x, final int y,
            final int w, final int h) {
        
        final Rectangle oldBounds = getBounds();
        final int ww = Math.max(w, headFig.getMinimumSize().width);
        
        emptyFig.setBounds(x, y, ww, offset);
        headFig.setBounds(x, y + offset, ww, headFig.getMinimumHeight());
        lifeLineFig.setBounds(x, 
                y + offset + headFig.getHeight() - lifeLineFig.getLineWidth(), 
                ww, h - offset - headFig.getHeight());
        getBigPort().setBounds(x, y, ww, h);

        _x = x;
        _y = y;
        _w = w;
        _h = h;
        
        //TODO: I suspect this isn't needed call isn't needed but don't remove
        // till out of alpha/beta stage
        updateEdges();
        //
        
        firePropChange("bounds", oldBounds, getBounds());
    }
    
    /*
     * This method is overridden in order to ignore change of the y coordinate
     * during dragging.
     *
     * @see org.tigris.gef.presentation.FigNode#superTranslate(int, int)
     */
    public void superTranslate(int dx, int dy) {
        super.superTranslate(dx, 0);
    }
     
    /**
     * Updates the head offset, looking for the create messages. 
     */
    private void updateHeadOffset() {
        FigMessage createMessage = getFirstCreateFigMessage();
        if (createMessage != null) {
            int y = createMessage.getFirstPoint().y;
            if (y > 0) {
                offset = y - (getY() + headFig.getHeight() / 2);
            }
        } else {
            offset = 0;
        }       
    }

    /**
     * Gets the first create message received by the classifier role
     * @return a figmessage.
     */
    private FigMessage getFirstCreateFigMessage() {
        List<FigMessage> messages = getFigMessages();
        FigMessage createMessage = null;
        for (FigMessage message : messages) {
            if (message.getDestFigNode().equals(this)
                    && message.isCreateMessage()) {
                
                createMessage = message;
                break;
            }
        }        
        return createMessage;
    }
    
    /**
     * Gets the minimum size of the Fig.<p>
     * 
     * The width is restricted by the notation making sure that the full
     * classifier role description is displayed.<p>
     * 
     * The minimum height is restricted so that the all attached message will
     * remain in the same position relative to the Fig. If no messages are
     * attached then the minimum height will ensure box is shown plus at least
     * 10 pixels of the lifeline.
     * 
     * @return dimensions of the minimum size
     */
    public Dimension getMinimumSize() {       
         /**
          * TODO: minimum height should not be calculated every time, but only 
          * when an FigMessage has been added or removed.
          * Currently doing that doesn't work because of an unknown problem. 
          * How to test: create only two CRs and a create message between them. 
          * Then move the create message to the bottom!
          * Until that is fixed the workaround is to call updateMinimumHeight()
          * every time the minimum size is needed
          */
        updateMinimumHeight();
        
        return new Dimension(headFig.getMinimumWidth(), minimumHeight);
    }
    
    /**
     * Updates the minimum height of the classifier role when a FigMessage
     * is added or removed.
     */
    private void updateMinimumHeight() {
        int yMax = getY();
        List<FigEdge> figsEdges = getFigEdges();
        FigMessage createMessage = getFirstCreateFigMessage();
        
        // TODO: Is this next line safe? What happens if there is just one
        // comment edge or a comment edge and a single message?
        if (figsEdges.size() == 1 && createMessage != null) {
            // TODO: Move magic number 10 to descriptive constant
            minimumHeight = headFig.getMinimumSize().height + offset + 10;
        } else {
            for (Fig fig : figsEdges) {
                if ( fig instanceof FigMessage
                        // we need the edge to be complete
                        && ((FigMessage) fig).getDestFigNode() != null
                        && ((FigMessage) fig).getSourceFigNode() != null
                        && ((FigMessage) fig).getY() > yMax) {
                    yMax = ((FigMessage) fig).getY();
                }
            }
            // TODO: Move magic number 10 to descriptive constant
            minimumHeight = yMax - getY() + 10;
        }
    }
    
    /**
     * Override ancestor behaviour by always calling setBounds even if the
     * size hasn't changed. Without this override the Package bounds draw
     * incorrectly. This is not the best fix but is a workaround until the
     * true cause is known. See issue 6135.
     * 
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateBounds()
     */
    protected void updateBounds() {
        if (!isCheckSize()) {
            return;
        }
        Rectangle bbox = getBounds();
        Dimension minSize = getMinimumSize();
        bbox.width = Math.max(bbox.width, minSize.width);
        bbox.height = Math.max(bbox.height, minSize.height);
        setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    }
    
    @Override
    public void removeFigEdge(FigEdge edge) {
        super.removeFigEdge(edge);

        if (edge instanceof FigMessage) {
            final FigMessage figMessage = (FigMessage) edge;
            positionHead(figMessage);
            createActivations();
        }
    }
    
    @Override
    public void addFigEdge(FigEdge edge) {
        super.addFigEdge(edge);
        
        if (edge instanceof FigMessage) {
            FigMessage mess = (FigMessage) edge;
            if (mess.isSelfMessage()) {
                mess.convertToArc();
            }
            positionHead(mess);
        }        
    }
    
    /**
     * Position the head at the top of the lifeline so that it is at the top
     * of the FigClassifierRole or where the create message enters the
     * FigClassifierRole
     */
    void positionHead(final FigMessage message) {
        // if the added edge is a Create Message it will affect the position
        // of the ClassifierRole so it should be repositioned
        if (message.isCreateMessage()
                && equals(message.getDestFigNode())
                && !equals(message.getSourceFigNode())) {
            updateHeadOffset();
            setBounds(getX(), getY(), getWidth(), getHeight());
        }
    }
    
    /**
     * Return an ordered list of message edges that are complete (ie the user
     * has finished drawing). Messages are ordered from top to bottom.
     * @return A list with all the messages that are complete
     */
    public List<FigMessage> getFigMessages() {
        final List<FigMessage> completeMessages = new ArrayList<FigMessage>(10);
        for (Object o : getFigEdges()) {
            if (o instanceof FigMessage) {
                final FigMessage fm = (FigMessage) o;
                if (fm.getPoints().length > 1
                        && fm.getDestFigNode() != null
                        && fm.getSourceFigNode() != null) {
                    completeMessages.add(fm);
                }
            }
        }
        Collections.sort(completeMessages, new FigMessageComparator());
        return completeMessages;
    }
    
    void createActivations() {
        lifeLineFig.createActivations(getFigMessages());
        forceRepaintShadow();
    }
    
    @Override
    public void setFillColor(Color color) {
        headFig.setFillColor(color);
        lifeLineFig.setFillColor(color);
    }
    
    @Override
    public Color getFillColor() {
        return headFig.getFillColor();
    }
    
    @Override
    public void setFilled(boolean filled) {
        headFig.setFilled(filled);
    }
    
    @Override
    public void setLineWidth(int w) {
        headFig.setLineWidth(w);
        getBigPort().setLineWidth(0);
    }
    
    /**
     * This class represents the port of the FigClassifierRole.
     * It has the logic for locating the messages. 
     *
     * @author penyaskito
     */
    class FigClassifierRolePort extends FigRect {
        
        /**
         * 
         */
        FigClassifierRolePort() {
            super(0, 0, 0, 0, null, null);
            setLineWidth(0);
        }
        
        @Override
        public Point getClosestPoint(Point anotherPt) {
            int width = FigActivation.DEFAULT_WIDTH;
            int y = anotherPt.y;
             
            // the initial x is the left side of the activation
            int x = getX() + getWidth() / 2 - width / 2; 
            // put the x at the right side of the activation if needed
            if (anotherPt.x > x + width) {
                x = x + width;
            } 
            // if the y coordinate is before the classifier role y,
            // we place the start in the corner of the fighead.
            if (y < getY()) {
                y = getY();
                x = Geometry.ptClosestTo(headFig.getBounds(), anotherPt).x;
            }
            // if the y coordinate is inside the head,
            // the x must be in the border of the headFig.
            else if (y < lifeLineFig.getY()) {
                x = Geometry.ptClosestTo(headFig.getBounds(), anotherPt).x; 
            }
            // else if the y coordinate is outside of the classifier role,
            // we fix the y in the max y of the classifier role.
            // FIXME: We should increase the height of the classifier role, 
            // don't???
            else if (y > getY() + getHeight()) {
                y = headFig.getY() + FigClassifierRole.this.getHeight();
            }
            return new Point(x, y);
        }
    }
    
    public Selection makeSelection() {
        return new SelectionClassifierRole(this);
    }
}
