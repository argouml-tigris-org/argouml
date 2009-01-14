// $Id$
// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.collaboration.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.ArgoFigGroup;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigMessage;
import org.argouml.uml.diagram.ui.PathItemPlacement;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;


/**
 * This class represents the Fig of an AssociationRole
 * for a collaboration diagram.
 *
 */
public class FigAssociationRole extends FigAssociation {

    /**
     * Serial version - Eclipse generated for rev. 1.30
     */
    private static final long serialVersionUID = -6543020797101620194L;
    
    private FigMessageGroup messages;

    /**
     * Main Constructor
     * 
     * @deprecated for 0.27.3 by tfmorris. Use
     *            {@link #FigAssociationRole(Object, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigAssociationRole() {
        super();
        messages = new FigMessageGroup();
        addPathItem(messages, new PathItemPlacement(this, messages, 50, 10));
    }

    /**
     * Constructor for FigAssociationRole.
     * 
     * @param edge the owning UML element
     * @param lay the layer
     * @deprecated for 0.27.3 by tfmorris. Use
     *            {@link #FigAssociationRole(Object, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigAssociationRole(Object edge, Layer lay) {
	this();
	setLayer(lay);
    	setOwner(edge);
    }
    
    /**
     * Construct an AssociationRole fig with the given onwer and render
     * settings.
     * 
     * @param owner owning UML element
     * @param settings render settings
     */
    public FigAssociationRole(Object owner, DiagramSettings settings) {
        super(owner, settings);
        messages = new FigMessageGroup(owner, settings);
        addPathItem(messages, new PathItemPlacement(this, messages, 50, 10));
    }
    
    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getNotationProviderType()
     */
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_ASSOCIATION_ROLE;
    }

    /**
     * @param message the message to be added
     */
    public void addMessage(FigMessage message) {
    	messages.addFig(message);
    	updatePathItemLocations();
    	messages.damage();
    }

@Override
public void computeRouteImpl() {
    super.computeRouteImpl();
    messages.updateArrows();
}
    
} /* end class FigAssociationRole */

/**
 * A Fig for the group of Messages shown above the Association Role.
 */
class FigMessageGroup extends ArgoFigGroup {

    /**
     * Constructor for FigMessageGroup.
     * @deprecated for 0.27.3 by tfmorris. Use
     *            {@link #FigMessageGroup(Object, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigMessageGroup() {
	super();
    }

    /**
     * Constructor for FigMessageGroup.
     * 
     * @param figs
     * @deprecated for 0.27.3 by tfmorris. Use
     *            {@link #FigMessageGroup(Object, DiagramSettings)}.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public FigMessageGroup(List<ArgoFig> figs) {
        super(figs);
    }
    
    public FigMessageGroup(Object owner, DiagramSettings settings) {
        super(owner, settings);
    }

    private void updateFigPositions() {
    	Collection figs = getFigs(); // the figs that make up this group
        Iterator it = figs.iterator();
    	if (!figs.isEmpty()) {
            FigMessage previousFig = null;
            for (int i = 0; it.hasNext(); i++) {
                FigMessage figMessage = (FigMessage) it.next();
                int y;
                if (i != 0) {
                    y = previousFig.getY() + previousFig.getHeight() + 5;
                } else {
                    y = getY();
                }
                figMessage.setLocation(getX(), y);
                figMessage.endTrans();
                previousFig = figMessage;
            }
    	}
    }

    /*
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    public void calcBounds() {
	super.calcBounds();
	Collection figs = getFigs();
	if (!figs.isEmpty()) {
	    Fig last = null;
	    Fig first = null;
	    // _x = first.getX();
	    // _y = first.getY();
	    _w = 0;
            Iterator it = figs.iterator();
            int size = figs.size();
	    for (int i = 0; i < size; i++) {
                Fig fig = (Fig) it.next();

                if (i == 0) {
                    first = fig;
                }
                if (i == size - 1) {
                    last = fig;
                }

		if (fig.getWidth() > _w) {
		    _w = fig.getWidth();
		}
	    }
            _h = last.getY() + last.getHeight() - first.getY();
	} else {
	    _w = 0;
	    _h = 0;
	}
    }

    /*
     * @see org.tigris.gef.presentation.FigGroup#addFig(Fig)
     */
    @Override
    public void addFig(Fig f) {
	super.addFig(f);
	updateFigPositions();
	updateArrows();
	calcBounds();
    }
    
    void updateArrows() {
        for (FigMessage fm : (List<FigMessage>) getFigs()) {
            fm.updateArrow();
        }
    }

}

