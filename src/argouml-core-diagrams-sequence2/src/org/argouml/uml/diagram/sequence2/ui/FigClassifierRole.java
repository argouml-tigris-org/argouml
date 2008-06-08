// $Id$
// Copyright (c) 2007-2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence2.ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.ui.FigEmptyRect;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Geometry;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigRect;

/**
 * The Fig that represents a Classifier Role
 * @author penyaskito
 */
public class FigClassifierRole extends FigNodeModelElement {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(FigClassifierRole.class);

    /**
     * This is an empty rectangle needed to keep the size of the CR.
     */
    private FigEmptyRect emptyFig;
    private FigHead headFig;
    private FigLifeLine lifeLineFig;
    
    private int offset = 0;
    
    /**
     * Constructor 
     */
    public FigClassifierRole() {
        super();
        setBigPort(new FigClassifierRolePort());
        
        emptyFig = new FigEmptyRect(getX(), getY(), getWidth(), offset);
        
        headFig = new FigHead(getStereotypeFig(), getNameFig());
        headFig.setBounds(getX(), getY() + offset,
                getWidth(), headFig.getHeight());
        lifeLineFig = new FigLifeLine(headFig.getX(), 
                getY() + offset + headFig.getHeight());
        
        addFig(getBigPort());        
        getBigPort().setVisible(false);
        
        emptyFig.setLineWidth(0);
        
        addFig(emptyFig);        
        addFig(lifeLineFig);
        addFig(headFig);
        
        createActivations();
    }

    /**
     * Constructor.
     * @param node The model element
     */
    public FigClassifierRole(Object node) {
        this();
        setOwner(node);
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
     * 
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
        
        updateHeadOffset();
        emptyFig.setBounds(x, y, ww, offset);
        headFig.setBounds(x, y + offset, ww, headFig.getMinimumHeight());
        lifeLineFig.setBounds(x, y + offset + headFig.getHeight(),
                ww, h - offset - headFig.getHeight());
        getBigPort().setBounds(x, y, ww, h);

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }
    
    /*
     * This method is overridden in order to ignore change of the y coordinate
     * during draging.
     *
     * @see org.tigris.gef.presentation.FigNode#superTranslate(int, int)
     */
    public void superTranslate(int dx, int dy) {
 	setBounds(getX() + dx, getY(), getWidth(), getHeight());
    }
     
    /**
     * Updates the head offset, looking for the create messages. 
     */
    private void updateHeadOffset() {
        FigMessage createMessage = getFirstCreateFigMessage();
        if (createMessage != null) {
            int y = createMessage.getFirstPoint().y;
            if (y > 0)
                offset = y - headFig.getHeight();
        } else {
            offset = 0;
        }       
    }

    /**
     * Gets the first create message received by the classifier role
     * @return a figmessage.
     */
    private FigMessage getFirstCreateFigMessage() {
        List<FigMessage> messages = getCompleteMessages();
        FigMessage createMessage = null;
        for (FigMessage message : messages) {
            if (message.getDestFigNode().equals(this)
                    && Model.getFacade().isACreateAction(message.getAction())) {
                
                createMessage = message;
                break;
            }
        }        
        return createMessage;
    }
    
    @Override
    public void addFigEdge(FigEdge edge) {
        super.addFigEdge(edge);
        if (edge instanceof FigMessage) {
            FigMessage mess = (FigMessage) edge;
            if (mess.isSelfMessage()) {
                mess.convertToArc();
            }
            if (equals(mess.getDestFigNode())
                  && !equals(mess.getSourceFigNode())  
                  && Model.getFacade().isACreateAction(mess.getAction())) {
                
                LOG.info("Added a create message");
                relocate();
            }
        }        
    }
    /**
     * Return all message edges that are complete (ie the user has finished
     * drawing).
     * @return A list with all the messages that are complete
     */
    private List<FigMessage> getCompleteMessages() {
        List<FigMessage> completeMessages = new ArrayList<FigMessage>(10);
        for (Object o : getFigEdges()) {
            if (o instanceof FigMessage) {
                FigMessage fm = (FigMessage) o;
                if (fm.getPoints().length > 1
                        && fm.getDestFigNode() != null
                        && fm.getSourceFigNode() != null) {
                    completeMessages.add(fm);
                }
            }
        }
        return completeMessages;
    }
    /**
     * Updates the position of the classifier role.
     * Called when a create message is added or moved.
     */
    void relocate() {
        updateHeadOffset();
        emptyFig.setBounds(getX(), getY(), getWidth(), offset);
        headFig.setBounds(getX(), getY() + offset,
                getWidth(), headFig.getHeight());
        lifeLineFig.setBounds(getX(), getY() + headFig.getHeight() + offset,
                getWidth(), getHeight() - headFig.getHeight() - offset);

        updateEdges();
        forceRepaintShadow();
    }
    
    void createActivations() {
        lifeLineFig.createActivations(getCompleteMessages());
        forceRepaintShadow();
    }
    
    @Override
    public void setFillColor(Color color) {
        headFig.setFillColor(color);
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
