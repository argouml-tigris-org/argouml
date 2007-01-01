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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Highlightable;
import org.argouml.cognitive.ItemUID;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.DiElement;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.ActionGoToCritique;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.Clarifier;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.notation.NotationProvider;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdgePoly;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigText;

/**
 * Abstract class to display diagram lines (edges) for UML ModelElements that
 * look like lines.
 * This Fig is prepared to show a (possibly editable) name,
 * and/or multiple stereotypes.
 */
public abstract class FigEdgeModelElement
    extends FigEdgePoly
    implements
        VetoableChangeListener,
        DelayedVChangeListener,
        MouseListener,
        KeyListener,
        PropertyChangeListener,
        ArgoNotationEventListener,
        Highlightable {

    private static final Logger LOG =
        Logger.getLogger(FigEdgeModelElement.class);

    private DiElement diElement = null;

    /**
     * Set the removeFromDiagram to false if this edge may not
     * be removed from the diagram.
     */
    private boolean removeFromDiagram = true;

    ////////////////////////////////////////////////////////////////
    // constants

    private static final Font LABEL_FONT;
    private static final Font ITALIC_LABEL_FONT;

    static {
        LABEL_FONT =
        /* TODO: Why is this different from the FigNodeModelElement?
         * Should we not use one of the following? 
         * LookAndFeelMgr.getInstance().getStandardFont();
         * new javax.swing.plaf.metal.DefaultMetalTheme().getSubTextFont();
         * new javax.swing.plaf.metal.DefaultMetalTheme().getUserTextFont(); */
            new Font("Dialog", Font.PLAIN, 10);
        ITALIC_LABEL_FONT =
            new Font(LABEL_FONT.getFamily(), Font.ITALIC, LABEL_FONT.getSize());
    }

    /**
     * Offset from the end of the set of popup actions at which new items
     * should be inserted by concrete figures.
    **/
    private static int popupAddOffset;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private NotationProvider notationProviderName;
    private HashMap npArguments = new HashMap();

    /**
     * The Fig that displays the name of this model element.
     * Use getNameFig(), no setter should be required.
     */
    private FigText nameFig;

    /**
     * Use getStereotypeFig(), no setter should be required.
     */
    private Fig stereotypeFig;

    private FigEdgePort edgePort;

    private ItemUID itemUid;

    /*
     * List of model element listeners we've registered.
     */
    private Collection listeners = new ArrayList();

    ////////////////////////////////////////////////////////////////
    // constructors

    /** 
     * Partially construct a new FigNode.  This method creates the
     * name element that holds the name of the model element and adds
     * itself as a listener. */
    public FigEdgeModelElement() {

        nameFig = new FigSingleLineText(10, 30, 90, 20, false);
        nameFig.setTextFilled(false);

        stereotypeFig = new FigStereotypesCompartment(10, 10, 90, 15);

        setBetweenNearestPoints(true);

        ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
    }

    /**
     * The constructor that hooks the Fig into the UML model element.
     *
     * @param edge the UML element
     */
    public FigEdgeModelElement(Object edge) {
        this();
        setOwner(edge);
    }

    /**
     * Create a FigCommentPort if needed
     */
    public void makeEdgePort() {
        if (edgePort == null) {
            edgePort = new FigEdgePort();
            if (getOwner() != null) {
                edgePort.setOwner(getOwner());
            }
            edgePort.setVisible(false);
            addPathItem(edgePort,
                    new PathConvPercent(this, 50, 0));
            computeRoute();
        }
    }

    /**
     * @return the FigCommentPort
     */
    public FigEdgePort getEdgePort() {
        return edgePort;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * Setter for the UID
     * @param newId the new UID
     */
    public void setItemUID(ItemUID newId) {
        itemUid = newId;
    }

    /**
     * Getter for the UID
     * @return the UID
     */
    public ItemUID getItemUID() {
        return itemUid;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getTipString(java.awt.event.MouseEvent)
     */
    public String getTipString(MouseEvent me) {
        ToDoItem item = hitClarifier(me.getX(), me.getY());
        String tip = "";
        if (item != null
            && Globals.curEditor().getSelectionManager().containsFig(this)) {
            tip = item.getHeadline();
        } else if (getOwner() != null) {
            try {
                tip = Model.getFacade().getTipString(getOwner());
            } catch (InvalidElementException e) {
                // We moused over an object just as it was deleted
                // transient condition - doesn't require I18N
                LOG.warn("A deleted element still exists on the diagram");
                return Translator.localize("misc.name.deleted");
            }
        } else {
            tip = toString();
        }

        if (tip != null && tip.length() > 0 && !tip.endsWith(" ")) {
            tip += " ";
        }
        return tip;
    }

    /*
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        
        // popupAddOffset should be equal to the number of items added here:
        popUpActions.addElement(new JSeparator());
        popupAddOffset = 1;
        if (removeFromDiagram) {
            popUpActions.addElement(
                    ProjectBrowser.getInstance().getRemoveFromDiagramAction());
            popupAddOffset++;
        }
        popUpActions.addElement(new ActionDeleteModelElements());
        popupAddOffset++;

        /* Check if multiple items are selected: */
        boolean ms = TargetManager.getInstance().getTargets().size() > 1;
        if (!ms) {
            ToDoList list = Designer.theDesigner().getToDoList();
            Vector items =
                (Vector) list.elementsForOffender(getOwner()).clone();
            if (items != null && items.size() > 0) {
                ArgoJMenu critiques = new ArgoJMenu("menu.popup.critiques");
                ToDoItem itemUnderMouse = hitClarifier(me.getX(), me.getY());
                if (itemUnderMouse != null) {
                    critiques.add(new ActionGoToCritique(itemUnderMouse));
                    critiques.addSeparator();
                }
                int size = items.size();
                for (int i = 0; i < size; i++) {
                    ToDoItem item = (ToDoItem) items.elementAt(i);
                    if (item == itemUnderMouse)
                        continue;
                    critiques.add(new ActionGoToCritique(item));
                }
                popUpActions.insertElementAt(new JSeparator(), 0);
                popUpActions.insertElementAt(critiques, 0);
            }
        }
        // Add stereotypes submenu
        Action[] stereoActions = getApplyStereotypeActions();
        if (stereoActions != null) {
            popUpActions.insertElementAt(new JSeparator(), 0);
            ArgoJMenu stereotypes = new ArgoJMenu(
                    "menu.popup.apply-stereotypes");
            for (int i = 0; i < stereoActions.length; ++i) {
                stereotypes.addCheckItem(stereoActions[i]);
            }
            popUpActions.insertElementAt(stereotypes, 0);
        }
        return popUpActions;
    }
    
    /**
     * Get the set of Actions which valid for adding/removing
     * Stereotypes on the ModelElement of this Fig's owner.
     *  
     * @return array of Actions 
     */
    protected Action[] getApplyStereotypeActions() {
        return StereotypeUtility.getApplyStereotypeActions(getOwner());
    }

    /**
     * distance formula: (x-h)^2 + (y-k)^2 = distance^2
     *
     * @param p1 point
     * @param p2 point
     * @return the square of the distance
     */
    protected int getSquaredDistance(Point p1, Point p2) {
        int xSquared = p2.x - p1.x;
        xSquared *= xSquared;
        int ySquared = p2.y - p1.y;
        ySquared *= ySquared;
        return xSquared + ySquared;
    }

    /**
     * @param g the <code>Graphics</code> object
     */
    protected void paintClarifiers(Graphics g) {
        int iconPos = 25, gap = 1, xOff = -4, yOff = -4;
        Point p = new Point();
        ToDoList list = Designer.theDesigner().getToDoList();
        Vector items = list.elementsForOffender(getOwner());
        int size = items.size();
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem) items.elementAt(i);
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier) icon).setFig(this);
                ((Clarifier) icon).setToDoItem(item);
            }
            if (icon != null) {
                stuffPointAlongPerimeter(iconPos, p);
                icon.paintIcon(null, g, p.x + xOff, p.y + yOff);
                iconPos += icon.getIconWidth() + gap;
            }
        }
        items = list.elementsForOffender(this);
        size = items.size();
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem) items.elementAt(i);
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier) icon).setFig(this);
                ((Clarifier) icon).setToDoItem(item);
            }
            if (icon != null) {
                stuffPointAlongPerimeter(iconPos, p);
                icon.paintIcon(null, g, p.x + xOff, p.y + yOff);
                iconPos += icon.getIconWidth() + gap;
            }
        }
    }

    /**
     * @param f the fig to indicate the bounds of
     * @param g the graphics
     */
    protected void indicateBounds(FigText f, Graphics g) {
        if (f == null) {
            return;
        }
        String text = f.getText();
        if (text == null || text.length() == 0) {
            Rectangle rect = f.getBounds();
            Color c = g.getColor();
            g.setColor(Globals.getPrefs().handleColorFor(f));
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
            g.setColor(c); // TODO: Is this needed?
        }
    }
    
    /**
     * The user clicked on the clarifier.
     *
     * @param x the x of the point clicked
     * @param y the y of the point clicked
     * @return the todo item clicked
     */
    public ToDoItem hitClarifier(int x, int y) {
        int iconPos = 25, xOff = -4, yOff = -4;
        Point p = new Point();
        ToDoList list = Designer.theDesigner().getToDoList();
        Vector items = list.elementsForOffender(getOwner());
        int size = items.size();
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem) items.elementAt(i);
            Icon icon = item.getClarifier();
            stuffPointAlongPerimeter(iconPos, p);
            int width = icon.getIconWidth();
            int height = icon.getIconHeight();
            if (y >= p.y + yOff
                && y <= p.y + height + yOff
                && x >= p.x + xOff
                && x <= p.x + width + xOff)
                return item;
            iconPos += width;
        }
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem) items.elementAt(i);
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier) icon).setFig(this);
                ((Clarifier) icon).setToDoItem(item);
                if (((Clarifier) icon).hit(x, y))
                    return item;
            }
        }
        items = list.elementsForOffender(this);
        size = items.size();
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem) items.elementAt(i);
            Icon icon = item.getClarifier();
            stuffPointAlongPerimeter(iconPos, p);
            int width = icon.getIconWidth();
            int height = icon.getIconHeight();
            if (y >= p.y + yOff
                && y <= p.y + height + yOff
                && x >= p.x + xOff
                && x <= p.x + width + xOff)
                return item;
            iconPos += width;
        }
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem) items.elementAt(i);
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier) icon).setFig(this);
                ((Clarifier) icon).setToDoItem(item);
                if (((Clarifier) icon).hit(x, y))
                    return item;
            }
        }
        return null;
    }

    /*
     * Returns a {@link SelectionRerouteEdge} object that manages selection
     * and rerouting of the edge.
     *
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionRerouteEdge(this);
    }

    /**
     * Getter for name, the name Fig
     * @return the nameFig
     */
    protected FigText getNameFig() {
        return nameFig;
    }
    
    /**
     * Get the Rectangle in which the model elements name is displayed
     *
     * @return the bounds of the namefig
     */
    public Rectangle getNameBounds() {
        return nameFig.getBounds();
    }
    
    /**
     * @return the text of the namefig
     */
    public String getName() {
        return nameFig.getText();
    }

    /**
     * Getter for stereo, the stereotype Fig
     * @return the stereo Fig
     */
    protected Fig getStereotypeFig() {
        return stereotypeFig;
    }

    /*
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange(PropertyChangeEvent pce) {
        Object src = pce.getSource();
        if (src == getOwner()) {
            DelayedChangeNotify delayedNotify =
                new DelayedChangeNotify(this, pce);
            SwingUtilities.invokeLater(delayedNotify);
        }
    }

    /*
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

    /*
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pve) {
        Object src = pve.getSource();
        String pName = pve.getPropertyName();
        if (pve instanceof DeleteInstanceEvent && src == getOwner()) {
            removeFromDiagram();
            return;
        }
        // We handle and consume editing events
        if (pName.equals("editing")
                && Boolean.FALSE.equals(pve.getNewValue())) {
            LOG.debug("finished editing");
            // parse the text that was edited
            textEdited((FigText) src);
            calcBounds();
            endTrans();
        } else if (pName.equals("editing")
                && Boolean.TRUE.equals(pve.getNewValue())) {
            textEditStarted((FigText) src);
        } else {
            // Pass everything except editing events to superclass
            super.propertyChange(pve);
        }

        if (Model.getFacade().isAModelElement(src) 
                && getOwner() != null
                && !Model.getUmlFactory().isRemoved(getOwner())) {
            /* If the source of the event is an UML object,
             * then the UML model has been changed.*/
            modelChanged(pve);
        }
        /* The following is a possible future improvement 
         * of the modelChanged() function.
         * Michiel: Propose not to do this to keep architecture stable. */
//        if (pve instanceof AttributeChangeEvent) {
//            modelAttributeChanged((AttributeChangeEvent) pve);
//        } else if (pve instanceof AddAssociationEvent) {
//            modelAssociationAdded((AddAssociationEvent) pve);
//        } else if (pve instanceof RemoveAssociationEvent) {
//            modelAssociationRemoved((RemoveAssociationEvent) pve);
//        }
    }
    
    /**
     * Called whenever we receive an AttributeChangeEvent.
     * 
     * @param ace the event
     */
    protected void modelAttributeChanged(AttributeChangeEvent ace) {
        // Default implementation is to do nothing
    }

    /**
     * Called whenever we receive an AddAssociationEvent.
     * 
     * @param aae the event
     */
    protected void modelAssociationAdded(AddAssociationEvent aae) {
        // Default implementation is to do nothing        
    }

    /**
     * Called whenever we receive an RemoveAssociationEvent.
     * 
     * @param rae the event
     */
    protected void modelAssociationRemoved(RemoveAssociationEvent rae) {
        // Default implementation is to do nothing
    }

    /**
     * This method is called when the user doubleclicked on the text field,
     * and starts editing. Subclasses should override this method to e.g.
     * supply help to the user about the used format. <p>
     *
     * It is also possible to alter the text to be edited
     * already here, e.g. by adding the stereotype in front of the name,
     * but that seems not user-friendly.
     *
     * @param ft the FigText that will be edited and contains the start-text
     */
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProviderName.getParsingHelp());
            ft.setText(notationProviderName.toString(getOwner(), npArguments));
        }
    }
    
    /**
     * TODO: Remove this - nobody should need this - Michiel.
     * @return a i18 key that represents a help string
     *         giving an explanation to the user of the syntax
     */
    protected String getParsingHelp() {
	return notationProviderName.getParsingHelp();
    }

    /**
     * Utility function to localize the given string with help text,
     * and show it in the status bar of the ArgoUML window.
     * This function is used in favour of the inline call
     * to enable later improvements; e.g. it would be possible to
     * show a help-balloon. TODO: Work this out.
     * One matter to possibly improve: show multiple lines.
     *
     * @param s the given string to be localized and shown
     */
    protected void showHelp(String s) {
        ProjectBrowser.getInstance().getStatusBar().showStatus(
                Translator.localize(s));
    }

    /**
     * This method is called after the user finishes editing a text
     * field that is in the FigEdgeModelElement.  Determine which field
     * and update the model.  This class handles the name, subclasses
     * should override to handle other text elements.
     *
     * @param ft the text Fig that has been edited
     */
    protected void textEdited(FigText ft) {
        if (ft == nameFig) {
            if (getOwner() == null)
                return;
            notationProviderName.parse(getOwner(), ft.getText());
            ft.setText(notationProviderName.toString(getOwner(), npArguments));
        }
    }

    /**
     * @param f the Fig
     * @return true if editable
     */
    protected boolean canEdit(Fig f) {
        return true;
    }

    ////////////////////////////////////////////////////////////////
    // event handlers - MouseListener implementation

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) {
        // Required for MouseListener interface, but we only care about clicks
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        // Required for MouseListener interface, but we only care about clicks
    }

    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me) {
        // Required for MouseListener interface, but we only care about clicks
    }

    /*
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me) {
        // Required for MouseListener interface, but we only care about clicks
    }

    /*
     * If the user double clicks on any part of this FigNode, pass it
     * down to one of the internal Figs.  This allows the user to
     * initiate direct text editing.
     *
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        if (me.isConsumed())
            return;
        if (me.getClickCount() >= 2) {
            Fig f = hitFig(new Rectangle(me.getX() - 2, me.getY() - 2, 4, 4));
            if (f instanceof MouseListener && canEdit(f))
		((MouseListener) f).mouseClicked(me);
        }
        me.consume();
    }

    /*
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) {
        // Required for KeyListener interface, but not used
    }

    /*
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent ke) {
        // Required for KeyListener interface, but not used
    }

    /*
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent ke) {
        if (ke.isConsumed())
            return;
        if (nameFig != null && canEdit(nameFig))
            nameFig.keyTyped(ke);
    }


    /**
     * Rerenders the attached elements of the fig. <p>
     * 
     * Warning: The purpose of this function is NOT 
     * to redraw the whole Fig everytime
     * something changes. That would be inefficient.<p>
     * 
     * Instead, this function should only be called
     * for major changes that require a complete redraw, 
     * such as change of owner, 
     * and change of notation language. <p>
     * 
     * Overrule this function for subclasses that add extra
     * or remove graphical parts.
     */
    protected void renderingChanged() {
        updateNameText();
        updateStereotypeText();
        damage();
    }
    
    ////////////////////////////////////////////////////////////////
    // internal methods

    /**
     * This is called after any part of the UML ModelElement has
     * changed. This method automatically updates the name FigText.
     * Subclasses should override and update other parts.
     *
     * @param e the event
     */
    protected void modelChanged(PropertyChangeEvent e) {
        if (e instanceof DeleteInstanceEvent) {
            // No need to update if model element went away
            return;
        }
        
        if (e instanceof AssociationChangeEvent 
                || e instanceof AttributeChangeEvent) {
            if (notationProviderName != null) {
                notationProviderName.updateListener(this, getOwner(), e);
                updateNameText();
            }
            updateListeners(getOwner(), getOwner());
        }

        // Update attached node figures
        // TODO: Presumably this should only happen on a add or remove event
        determineFigNodes();
    }
    
    /**
     * generate the notation for the modelelement and stuff it into the text Fig
     */
    protected void updateNameText() {

        if (getOwner() == null) {
            return;
        }
        if (notationProviderName != null) {
            String nameStr = notationProviderName.toString(
                    getOwner(), npArguments);
            nameFig.setText(nameStr);
            calcBounds();
            setBounds(getBounds());
        }
    }

    /**
     * generate the notation for the stereotype and stuff it into the text Fig
     */
    protected void updateStereotypeText() {
        if (getOwner() == null) {
            return;
        }
        Object modelElement = getOwner();
        stereotypeFig.setOwner(modelElement);
        ((FigStereotypesCompartment) stereotypeFig).populate();
    }

    /**
     * This method should only be called once for any one Fig instance that
     * represents a model element (ie not for a FigEdgeNote).
     * It is called either by the constructor that takes an model element as an
     * argument or it is called by PGMLStackParser after it has created the Fig
     * by use of the empty constructor.
     * The assigned model element (owner) must not change during the lifetime
     * of the Fig.
     * TODO: It is planned to refactor so that there is only one Fig
     * constructor. When this is achieved this method can refactored out.
     * 
     * @param owner the model element that this Fig represents.
     * @throws IllegalArgumentException if the owner given is not a model
     * element
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object owner) {
        if (owner == null) {
            throw new IllegalArgumentException("An owner must be supplied");
        }
        if (getOwner() != null) {
            throw new IllegalStateException(
                    "The owner cannot be changed once set");
        }
        if (!Model.getFacade().isAModelElement(owner)) {
            throw new IllegalArgumentException(
                    "The owner must be a model element - got a "
                    + owner.getClass().getName());
        }
        super.setOwner(owner);
        if (edgePort != null) {
            edgePort.setOwner(getOwner());
        }
        initNotationProviders(owner);
        updateListeners(null, owner);
        renderingChanged();
    }

    /**
     * Create the NotationProviders.
     * 
     * @param own the current owner
     */
    protected void initNotationProviders(Object own) {
        if (notationProviderName != null) {
            notationProviderName.cleanListener(this, own);
        }
        if (Model.getFacade().isAModelElement(own)) {
            notationProviderName =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        getNotationProviderType(), own, this);
        }
    }

    /**
     * Overrule this for subclasses 
     * that need a different NotationProvider.
     * 
     * @return the type of the notation provider
     */
    protected int getNotationProviderType() {
        return NotationProviderFactory2.TYPE_NAME;
    }

    /**
     * Implementations of this method should register/unregister the fig for all
     * (model)events that may cause a repaint to be necessary.
     * In the simplest case, the fig should register itself
     * as listening to (all) events fired by (only) the owner. <p>
     *  
     * But for, for example, for a
     * FigLink the fig must also register for events fired by the
     * association of the owner - because the name of 
     * the association is shown, not the name of the Link.<p>
     * 
     * In other cases, there is no need to register for any event, 
     * e.g. when a notationProvider is used. <p>
     * 
     * This function is called in 2 places: at creation (load) time of this Fig,
     * i.e. when the owner changes, 
     * and in some cases by the modelChanged() function, 
     * i.e. when the model changes. <p>
     * 
     * This function shall always register for the "remove" event of the owner!
     * Otherwise the Fig will not be deleted when the owner gets deleted.<p>
     * 
     *  IF this method is called with both the oldOwner and the 
     *  newOwner equal and not null, 
     *  AND we listen only to the owner itself,
     *  THEN we can safely ignore the call, but 
     *  ELSE we need to update the listeners of the related elements, 
     *  since the related elements may have been replaced.
     * 
     * @param newOwner the new owner for the listeners, 
     *          or null if all listeners have to be removed
     * @param oldOwner the previous owner, 
     *          or null if there was none, and all listeners have to be set
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner == newOwner) {
            LOG.warn("Listeners being added and removed from the same owner");
        }
        if (oldOwner != null) {
            removeElementListener(oldOwner);
        }
        if (newOwner != null) {
            addElementListener(newOwner, "remove");
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLayer(org.tigris.gef.base.Layer)
     */
    public void setLayer(Layer lay) {
        super.setLayer(lay);
        getFig().setLayer(lay);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#deleteFromModel()
     */
    public void deleteFromModel() {
        Object own = getOwner();
        if (own != null) {
            ProjectManager.getManager().getCurrentProject().moveToTrash(own);
        }
        Iterator it = getPathItemFigs().iterator();
        while (it.hasNext()) {
            ((Fig) it.next()).deleteFromModel();
        }
        super.deleteFromModel();
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationChanged(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationChanged(ArgoNotationEvent event) {
        if (getOwner() == null) return;
        initNotationProviders(getOwner());
        renderingChanged();
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationAdded(ArgoNotationEvent event) {
        // Default implementation is to do nothing
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationRemoved(ArgoNotationEvent event) {
        // Default implementation is to do nothing
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderAdded(ArgoNotationEvent event) {
        // Default implementation is to do nothing
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderRemoved(ArgoNotationEvent event) {
        // Default implementation is to do nothing
    }

    /*
     * @see org.tigris.gef.presentation.Fig#hit(java.awt.Rectangle)
     */
    public boolean hit(Rectangle r) {
	// Check if labels etc have been hit
	// Apparently GEF does require PathItems to be "annotations"
	// which ours aren't, so until that is resolved...
	Iterator it = getPathItemFigs().iterator();
	while (it.hasNext()) {
	    Fig f = (Fig) it.next();
	    if (f.hit(r))
		return true;
	}
	return super.hit(r);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    public final void removeFromDiagram() {
        Fig delegate = getRemoveDelegate();
        if (delegate instanceof FigNodeModelElement) {
            ((FigNodeModelElement) delegate).removeFromDiagramImpl();
        } else if (delegate instanceof FigEdgeModelElement) {
            ((FigEdgeModelElement) delegate).removeFromDiagramImpl();
        } else if (delegate != null) {
            removeFromDiagramImpl();
        }
    }
    
    /**
     * Subclasses should override this to redirect a remove request from
     * one Fig to another.
     * e.g. FigEdgeAssociationClass uses this to delegate the remove to
     * its attached FigAssociationClass.
     * @return the fig handling the remove
     */
    protected Fig getRemoveDelegate() {
        return this;
    }
    
    protected void removeFromDiagramImpl() {
        Object o = getOwner();
        if (o != null) {
            removeElementListener(o);
        }
        ArgoEventPump.removeListener(this);
        notationProviderName.cleanListener(this, getOwner());

        Iterator it = getPathItemFigs().iterator();
        while (it.hasNext()) {
            Fig fig = (Fig) it.next();
            fig.removeFromDiagram();
        }

        /* TODO: MVW: Why the next action?
         * Deleting a fig from 1 diagram should not influence others!
         * */
        // GEF does not take into account the multiple diagrams we have
        // therefore we loop through our diagrams and delete each and every
        // occurence on our own
        it = ProjectManager.getManager().getCurrentProject().getDiagrams()
                .iterator();
        while (it.hasNext()) {
            ArgoDiagram diagram = (ArgoDiagram) it.next();
            diagram.damage();
        }

        /* TODO: MVW: Should we not call damage()
         * for diagrams AFTER the next step? */
        super.removeFromDiagram();
    }
    
    protected void superRemoveFromDiagram() {
        super.removeFromDiagram();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#damage()
     */
    public void damage() {
        super.damage();
        getFig().damage();
    }

    /**
     * <p>Determines if the FigEdge is currently connected to the correct
     * FigNodes, if not the edges is the correct FigNodes set and the edge
     * rerouted.
     * <p>Typically this is used when a user has amended from the property
     * panel a relationship from one model element to another and the graph
     * needs to react to that change.
     * <p>e.g. if the participant of an association end is changed.
     * <p>Calls a helper method (layoutThisToSelf) to avoid this edge
     * disappearing if the new source and dest are the same node.
     *
     * @return boolean whether or not the update was sucessful
     */
    protected boolean determineFigNodes() {
        Object owner = getOwner();
        if (owner == null || getLayer() == null) {
            LOG.error("The FigEdge has no owner or its layer is null");
            return false;
        }

        Object newSource = getSource();
        Object newDest = getDestination();

        Fig currentSourceFig = getSourceFigNode();
        Fig currentDestFig = getDestFigNode();
        Object currentSource = null;
        Object currentDestination = null;
        if (currentSourceFig != null && currentDestFig != null) {
            currentSource = currentSourceFig.getOwner();
            currentDestination = currentDestFig.getOwner();
        }
        if (newSource != currentSource || newDest != currentDestination) {
            Fig newSourceFig = getNoEdgePresentationFor(newSource);
            Fig newDestFig = getNoEdgePresentationFor(newDest);
            if (newSourceFig != currentSourceFig) {
                setSourceFigNode((FigNode) newSourceFig);
                setSourcePortFig(newSourceFig);

            }
            if (newDestFig != currentDestFig) {
                setDestFigNode((FigNode) newDestFig);
                setDestPortFig(newDestFig);
            }
            ((FigNode) newSourceFig).updateEdges();
            ((FigNode) newDestFig).updateEdges();
            calcBounds();

            // adapted from SelectionWButtons from line 280
            // calls a helper method to avoid this edge disappearing
            // if the new source and dest are the same node.
            if (newSourceFig == newDestFig) {

                layoutThisToSelf();
            }

        }

        return true;
    }

    /**
     * A version of GEF's presentationFor() method which 
     * @param element ModelElement to return presentation for
     * @return the Fig representing the presentation
     */
    private Fig getNoEdgePresentationFor(Object element) {
        if (element == null) {
            throw new IllegalArgumentException("Can't search for a null owner");
        }
        List contents = getLayer().getContentsNoEdges();
        int figCount = contents.size();
        for (int figIndex = 0; figIndex < figCount; ++figIndex) {
            Fig fig = (Fig) contents.get(figIndex);
            if (fig.getOwner() == element) {
                return fig;
            }
        }
        throw new IllegalStateException("Can't find a FigNode representing "
                + Model.getFacade().getName(element));
    }


    /**
     * helper method for updateClassifiers() in order to automatically
     * layout an edge that is now from and to the same node type.
     * <p>adapted from SelectionWButtons from line 280
     */
    private void layoutThisToSelf() {

        FigPoly edgeShape = new FigPoly();
        //newFC = _content;
        Point fcCenter =
            new Point(getSourceFigNode().getX() / 2,
                    getSourceFigNode().getY() / 2);
        Point centerRight =
            new Point(
		      (int) (fcCenter.x
			     + getSourceFigNode().getSize().getWidth() / 2),
		      fcCenter.y);

        int yoffset = (int) ((getSourceFigNode().getSize().getHeight() / 2));
        edgeShape.addPoint(fcCenter.x, fcCenter.y);
        edgeShape.addPoint(centerRight.x, centerRight.y);
        edgeShape.addPoint(centerRight.x + 30, centerRight.y);
        edgeShape.addPoint(centerRight.x + 30, centerRight.y + yoffset);
        edgeShape.addPoint(centerRight.x, centerRight.y + yoffset);

        // place the edge on the layer and update the diagram
        this.setBetweenNearestPoints(true);
        edgeShape.setLineColor(Color.black);
        edgeShape.setFilled(false);
        edgeShape.setComplete(true);
        this.setFig(edgeShape);
    }

    /**
     * Returns the source of the edge. The source is the owner of the
     * node the edge travels from in a binary relationship. For
     * instance: for a classifierrole, this is the sender.
     * @return a model element
     */
    protected Object getSource() {
        Object owner = getOwner();
        if (owner != null) {
            return Model.getCoreHelper().getSource(owner);
        }
        return null;
    }
    /**
     * Returns the destination of the edge. The destination is the
     * owner of the node the edge travels to in a binary
     * relationship. For instance: for a classifierrole, this is the
     * receiver.
     * @return a model element
     */
    protected Object getDestination() {
        Object owner = getOwner();
        if (owner != null) {
            return Model.getCoreHelper().getDestination(owner);
        }
        return null;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#postLoad()
     */
    public void postLoad() {
        ArgoEventPump.removeListener(this);
        ArgoEventPump.addListener(this);
    }

    /**
     * @return Returns the lABEL_FONT.
     */
    public static Font getLabelFont() {
        return LABEL_FONT;
    }

    /**
     * @return Returns the iTALIC_LABEL_FONT.
     */
    public static Font getItalicLabelFont() {
        return ITALIC_LABEL_FONT;
    }

    /**
     * @param allowed true if the function RemoveFromDiagram is allowed
     */
    protected void allowRemoveFromDiagram(boolean allowed) {
        this.removeFromDiagram = allowed;
    }

    /**
     * Set the associated Diagram Interchange element.
     * 
     * @param element the element to be associated with this Fig
     */
    public void setDiElement(DiElement element) {
        this.diElement = element;
    }

    /**
     * @return the Diagram Interchange element associated with this Fig
     */
    public DiElement getDiElement() {
        return diElement;
    }

    /**
     * @return Returns the popupAddOffset.
     */
    protected static int getPopupAddOffset() {
        return popupAddOffset;
    }
    

    /**
     * Add an element listener and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected void addElementListener(Object element) {
        listeners.add(new Object[] {element, null});
        Model.getPump().addModelEventListener(this, element);
    }

    /**
     * Add a listener and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @param property
     *            name of property to listen for changes of
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected void addElementListener(Object element, String property) {
        listeners.add(new Object[] {element, property});
        Model.getPump().addModelEventListener(this, element, property);
    }

    /**
     * Add a listener and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @param property
     *            array of property names (Strings) to listen for changes of
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected void addElementListener(Object element, String[] property) {
        listeners.add(new Object[] {element, property});
        Model.getPump().addModelEventListener(this, element, property);
    }
    
    /**
     * Add an element listener and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener, Object, String)
     */
    protected void removeElementListener(Object element) {
        listeners.remove(new Object[] {element, null});
        Model.getPump().removeModelEventListener(this, element);
    }
   
    /**
     * Unregister all listeners registered through addElementListener
     * @see #addElementListener(Object, String)
     */
    protected void removeAllElementListeners() {
        for (Iterator iter = listeners.iterator(); iter.hasNext();) {
            Object[] l = (Object[]) iter.next();
            Object property = l[1];
            if (property == null) {
                Model.getPump().removeModelEventListener(this, l[0]);
            } else if (property instanceof String[]) {
                Model.getPump().removeModelEventListener(this, l[0],
                        (String[]) property);
            } else if (property instanceof String) {
                Model.getPump().removeModelEventListener(this, l[0],
                        (String) property);
            } else {
                throw new RuntimeException(
                        "Internal error in removeAllElementListeners");
            }
        }
        listeners.clear();
    }

    /**
     * Returns all texts shown in a TextFig that are editable.
     * This is used to meke these texts stand out when the edge is selected.
     * 
     * @return a collection of TextFigs
     */
//    Collection getEditableTextFigs() {
//        Collection c = new ArrayList();
//        c.add(nameFig);
//        return c;
//    }

} /* end class FigEdgeModelElement */
