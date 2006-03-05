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

package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.Notation;
import org.argouml.ui.ActionAutoResize;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.cmd.CmdSetPreferredSize;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * Abstract class to display diagram lines (edges) for UML ModelElements that
 * look like lines and that have editable names.
 */
public abstract class FigEdgeModelElement extends FigGraphEdge {
    
    private static final Logger LOG =
        Logger.getLogger(FigEdgeModelElement.class);

    /** Partially construct a new FigNode.  This method creates the
     *  _name element that holds the name of the model element and adds
     *  itself as a listener. */
    public FigEdgeModelElement() {
        super();
    }

    /**
     * The constructor that hooks the Fig into the UML model element.
     *
     * @param edge the UML element
     */
    public FigEdgeModelElement(Object edge) {
        super(edge);
    }
    
    /**
     * generate the notation for the modelelement and stuff it into the text Fig
     */
    protected void updateNameText() {

        if ((getOwner() == null) || (getOwner() instanceof CommentEdge))
            return;
        String nameStr =
        Notation.generate(this, Model.getFacade().getName(getOwner()));
        getNameFig().setText(nameStr);
        calcBounds();
        setBounds(getBounds());
    }

    /**
     * generate the notation for the stereotype and stuff it into the text Fig
     */
    protected void updateStereotypeText() {
        if ((getOwner() == null) || (getOwner() instanceof CommentEdge)) {
            return;
        }
        Object modelElement = getOwner();
        getStereotypeFig().setOwner(modelElement);
        ((FigStereotypesCompartment) getStereotypeFig()).populate();
    }

    /**
     * This is called after any part of the UML ModelElement has
     * changed. This method automatically updates the name FigText.
     * Subclasses should override and update other parts.<p>
     * 
     * NOTE: If you override this method you probably also want to 
     * override the modelChanged() method
     *
     * @param e the event
     */
    // TODO: Merge so there's only a single place to deal with
    protected void modelChanged(PropertyChangeEvent e) {
        if (e instanceof DeleteInstanceEvent) {
            // No need to update if model element went away
            return;
        }
        if (e.getSource() == getOwner()
                && "name".equals(e.getPropertyName())) {
            updateNameText();
        }

        updateStereotypeText();

        if (ActionAutoResize.isAutoResizable()) {
            CmdSetPreferredSize cmdSPS =
                new CmdSetPreferredSize(CmdSetPreferredSize.MINIMUM_SIZE);
            cmdSPS.setFigToResize(this);
            cmdSPS.doIt();
        }

        // Update attached node figures
        updateClassifiers();
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pve) {
        Object src = pve.getSource();
        String pName = pve.getPropertyName();
        // We handle and consume editing events
        if (pName.equals("editing")
            && Boolean.FALSE.equals(pve.getNewValue())) {
            LOG.debug("finished editing");
            textEdited((FigText) src);
            calcBounds();
            endTrans();
        } else if (pName.equals("editing")
                && Boolean.TRUE.equals(pve.getNewValue())) {
            textEditStarted((FigText) src);
        } else {
            // Add/remove name change listeners for applied stereotypes
            if (src == getOwner()
                    && "stereotype".equals(pName)) {
                if (pve instanceof RemoveAssociationEvent) {
                    removeElementListener(pve.getOldValue());
                } else if (pve instanceof AddAssociationEvent) {
                    addElementListener(pve.getNewValue(), "name");
                }
            }
            // Pass everything except editing events to superclass
            super.propertyChange(pve);
        }

        if (Model.getFacade().isAModelElement(src)) {
            /* If the source of the event is an UML object,
             * then the UML model has been changed.*/
            modelChanged(pve);
        }
        damage();  // TODO: (MVW) Is this required?
        // After all these events? I doubt it...
    }
    
    /**
     * Rerenders the fig if needed. This functionality was originally
     * the functionality of modelChanged but modelChanged takes the
     * event now into account.<p>
     * 
     * NOTE: If you override this method you probably also want to 
     * override the modelChanged() method
     */
    public void renderingChanged() {
        // updateAnnotationPositions();
        updateClassifiers();
        updateNameText();
        updateStereotypeText();
        damage();
    }
    
    /**
     * @see org.argouml.kernel.DelayedVChangeListener#delayedVetoableChange(java.beans.PropertyChangeEvent)
     */
    public void delayedVetoableChange(PropertyChangeEvent pce) {
        // update any text, colors, fonts, etc.
        renderingChanged();
        // update the relative sizes and positions of internel Figs
        Rectangle bbox = getBounds();
        setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
        endTrans();
    }
    
    /**
     * In ArgoUML, for every Fig, this setOwner() function
     * may only be called twice: Once after the fig is created, 
     * with a non-null argument, and once at end-of-life of the Fig,
     * with a null argument. It is not allowed in ArgoUML to change 
     * the owner of a fig in any other way. <p>
     * 
     * Hence, during the lifetime of this Fig object, 
     * the owner shall go from null to some UML object, and to null again.
     * 
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object newOwner) {
        updateListeners(newOwner);
        super.setOwner(newOwner);
        initNotationProviders(newOwner);
        if (newOwner != null) {
            renderingChanged();
        }
    }
    
    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationChanged(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationChanged(ArgoNotationEvent event) {
        PropertyChangeEvent changeEvent =
            (PropertyChangeEvent) event.getSource();
        if (changeEvent.getPropertyName().equals("argo.notation.only.uml")) {
            if (changeEvent.getNewValue().equals("true")) {
                setContextNotation(Notation.getConfigueredNotation());
            }
        } else {
            setContextNotation(
                Notation.findNotation((String) changeEvent.getNewValue()));
        }
        renderingChanged();
        damage();
    }
    
    /**
     * Returns the source of the edge. The source is the owner of the
     * node the edge travels from in a binary relationship. For
     * instance: for a classifierrole, this is the sender.
     * @return The model element
     */
    protected Object getSource() {
        Object owner = getOwner();
        if (owner != null) {
            if (owner instanceof CommentEdge) {
                return ((CommentEdge) owner).getSource();
            }
            return Model.getCoreHelper().getSource(owner);
        }
        return null;
    }
    
    /**
     * Returns the destination of the edge. The destination is the
     * owner of the node the edge travels to in a binary
     * relationship. For instance: for a classifierrole, this is the
     * receiver.
     * @return Object
     */
    protected Object getDestination() {
        Object owner = getOwner();
        if (owner != null) {
            if (owner instanceof CommentEdge) {
                return ((CommentEdge) owner).getDestination();
            }
            return Model.getCoreHelper().getDestination(owner);
        }
        return null;
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#getTipString(java.awt.event.MouseEvent)
     */
    public String getTipString(MouseEvent me) {
        ToDoItem item = hitClarifier(me.getX(), me.getY());
        String tip = "";
        if (item != null
            && Globals.curEditor().getSelectionManager().containsFig(this)) {
            tip = item.getHeadline();
        } else if (getOwner() != null 
                && Model.getFacade().isAModelElement(getOwner())) {
            tip = Model.getFacade().getTipString(getOwner());
        } else {
            tip = toString();
        }

        if (tip != null && tip.length() > 0 && !tip.endsWith(" ")) {
            tip += " ";
        }
        return tip;
    }

    /**
     * Implementations of this method should register/unregister the fig for all
     * (model)events. For FigEdgeModelElement only the fig itself is registered
     * as listening to events fired by the owner itself. But for, for example,
     * FigAssociation the fig must also register for events fired by the
     * stereotypes of the owner. <p>
     * 
     * This function is used in UMLDiagram, which removes all listeners 
     * to all Figs when a diagram is not displayed, and restore them
     * when it becomes visible again. <p>
     * 
     * In this case, it is not imperative that indeed ALL listeners are 
     * updated, as long as the ones removed get added again and vice versa. <p>
     * 
     * Additionally, this function may be used by the modelChanged() function.
     * <p>
     * 
     * In this case, it IS imperative that all listeners get removed / added.
     * 
     * @param newOwner the new owner for the listeners
     */
    protected void updateListeners(Object newOwner) {
        Object oldOwner = getOwner();
        if (oldOwner != null && Model.getFacade().isAModelElement(oldOwner)) {
            removeElementListener(oldOwner);
        }
        if (newOwner != null && Model.getFacade().isAModelElement(newOwner)) {
            addElementListener(newOwner);
        }
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    public void removeFromDiagram() {
        Object o = getOwner();
        if (Model.getFacade().isAModelElement(o)) {
            removeElementListener(o);
        }
        ArgoEventPump.removeListener(this);

        Iterator it = getPathItemFigs().iterator();
        while (it.hasNext()) {
            Fig fig = (Fig) it.next();
            fig.removeFromDiagram();
        }

        /* TODO: MVW: Why the next action?
         * Removing a fig from 1 diagram should not influence others!
         * TODO: Bobs says: I agree this looks odd and I think we should delete.
         * However I don't want to change this during current refactoring proess.
         */
        // GEF does not take into account the multiple diagrams we have
        // therefore we loop through our diagrams and delete each and every
        // occurence on our own
        it =
        ProjectManager.getManager().getCurrentProject()
            .getDiagrams().iterator();
        while (it.hasNext()) {
            ArgoDiagram diagram = (ArgoDiagram) it.next();
            diagram.damage();
        }

        super.removeFromDiagram();

    }

}
