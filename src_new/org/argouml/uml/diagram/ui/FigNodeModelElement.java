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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.application.api.ArgoEventListener;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.application.api.NotationContext;
import org.argouml.application.api.NotationName;
import org.argouml.application.events.ArgoEvent;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ItemUID;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.ui.ActionGoToCritique;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.Clarifier;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.UUIDManager;
import org.argouml.uml.generator.ParserDisplay;
import org.argouml.util.Trash;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * Abstract class to display diagram icons for UML ModelElements that
 * look like nodes and that have editiable names and can be
 * resized.
 *
 * @author abonner
 */
public abstract class FigNodeModelElement
    extends FigNode
    implements
        VetoableChangeListener,
        DelayedVChangeListener,
        MouseListener,
        KeyListener,
        PropertyChangeListener,
        MElementListener, // TODO: NSUML interface, how do we rid
			  // ourselves of this?
        NotationContext,
        ArgoNotationEventListener {            

    private static final Logger LOG =
        Logger.getLogger(FigNodeModelElement.class);

    ////////////////////////////////////////////////////////////////
    // constants

    private NotationName currentNotationName;
    private static final Font LABEL_FONT;
    private static final Font ITALIC_LABEL_FONT;
    private static final int MARGIN = 2;

    /**
     * min. 17, used to calculate y pos of FigText items in a compartment
     */
    protected static final int ROWHEIGHT = 17;

    /**
     * min. 18, used to calculate y pos of stereotype FigText items 
     * in a compartment
     */
    protected static final int STEREOHEIGHT = 18;
    
    /**
     * Needed for loading. Warning: if false, a too small size might look bad!
     */
    private boolean checkSize = true;
    
    /**
     * Offset from the end of the set of popup actions at which new items
     * should be inserted by concrete figures.
     */
    protected static final int POPUP_ADD_OFFSET = 3;
    
    // Fields used in paint() for painting shadows
    private BufferedImage           shadowImage = null;
    private int                     cachedWidth = -1;
    private int                     cachedHeight = -1;
    private static final LookupOp   SHADOW_LOOKUP_OP;
    private static final ConvolveOp SHADOW_CONVOLVE_OP;

    /**
     * The intensity value of the shadow color (0-255).
    **/
    protected static final int SHADOW_COLOR_VALUE = 32;
    
    /**
     * The transparency value of the shadow color (0-255).
    **/    
    protected static final int SHADOW_COLOR_ALPHA = 128;
    
    /**
     * Used for creating right-click pop-up menus 
     */
    protected static final String BUNDLE = "UMLMenu";

    static {
        LABEL_FONT =
            new javax.swing.plaf.metal.DefaultMetalTheme().getSubTextFont();
        ITALIC_LABEL_FONT =
            new Font(LABEL_FONT.getFamily(), Font.ITALIC, LABEL_FONT.getSize());

        // Setup image ops used in rendering shadows            
        byte[][] data = new byte[4][256];
        for (int i = 1; i < 256; ++i) {
            data[0][i] = (byte) SHADOW_COLOR_VALUE;
            data[1][i] = (byte) SHADOW_COLOR_VALUE;
            data[2][i] = (byte) SHADOW_COLOR_VALUE;
            data[3][i] = (byte) SHADOW_COLOR_ALPHA;
        }        
        float[] blur = new float[9];
        for (int i = 0; i < blur.length; ++i) {
            blur[i] = 1 / 12f;
        }
        SHADOW_LOOKUP_OP = new LookupOp(new ByteLookupTable(0, data), null);
        SHADOW_CONVOLVE_OP = new ConvolveOp(new Kernel(3, 3, blur));            
    }

    ////////////////////////////////////////////////////////////////
    // instance variables

    private Fig bigPort;

    /**
     * use getNameFig() and setNameFig() to access the Figs.
     * Use getName() and setName() to just change the text.
     */
    private FigText name; 

    /**
     * use getter/setter
     * getStereotypeFig() and setStereoTypeFig() to access the Figs.
     * Use getStereotype() and setStereotype() to change stereotype
     * text.
     *  
     * MVW: Why are the getter/setter not returning a FigText, but a Fig?
     * I created a new (real) getter: getStereotypeFigText()
     */
    private FigText stereo; 

    /**
     * enclosedFigs are the Figs that are enclosed by this figure. Say that
     * it is a Package then these are the Classes, Interfaces, Packages etc
     * that are on this figure. This is not the same as the figures in the
     * FigGroup that this FigNodeModelElement "is", since these are the
     * figures that make up this high-level primitive figure.
     */
    private Vector enclosedFigs = new Vector();

    /** The figure enclosing this figure */
    private Fig encloser = null;

    private boolean readyToEdit = true;
    private boolean suppressCalcBounds = false;
    
    private int shadowSize =
        Configuration.getInteger(Notation.KEY_DEFAULT_SHADOW_WIDTH, 1);

    private ItemUID itemUid;

    /**
     * The main constructor.
     * 
     */
    public FigNodeModelElement() {
        // this rectangle marks the whole interface figure; everything
        // is inside it:
        bigPort = new FigRect(10, 10, 0, 0, Color.cyan, Color.cyan);

        name = new FigText(10, 10, 90, 21, true);
        name.setFont(getLabelFont());
        name.setTextColor(Color.black);
        // _name.setFilled(false);
        name.setMultiLine(false);
        name.setAllowsTab(false);
        name.setText(placeString());

        stereo = new FigText(10, 10, 90, 15, true);
        stereo.setFont(getLabelFont());
        stereo.setTextColor(Color.black);
        stereo.setFilled(false);
        stereo.setLineWidth(0);
        //_stereo.setLineColor(Color.black);
        stereo.setEditable(false);

        readyToEdit = false;
        ArgoEventPump.addListener(ArgoEvent.ANY_NOTATION_EVENT, this);
    }

    /** Partially construct a new FigNode.  This method creates the
     *  name element that holds the name of the model element and adds
     *  itself as a listener. 
     * 
     * @param gm ignored
     * @param node the owning UML element
     */
    public FigNodeModelElement(GraphModel gm, Object node) {
        this();
        setOwner(node);
        name.setText(placeString());
        readyToEdit = false;
        //ArgoEventPump.addListener(ArgoEvent.ANY_NOTATION_EVENT, this);
    }

    /**
     * @see java.lang.Object#finalize()
     */
    public void finalize() {
        ArgoEventPump.removeListener(ArgoEvent.ANY_NOTATION_EVENT, this);
    }

    /**
     * After the base clone method has been called determine which child
     * figs of the clone represent the name, stereotype and port.
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigNodeModelElement clone = (FigNodeModelElement) super.clone();
        Iterator thisIter = this.getFigs(null).iterator();
        Iterator cloneIter = clone.getFigs(null).iterator();
        while (thisIter.hasNext()) {
            Fig thisFig = (Fig) thisIter.next();
            Fig cloneFig = (Fig) cloneIter.next();
            if (thisFig == getBigPort()) clone.setBigPort(cloneFig);
            if (thisFig == name) clone.name = (FigText) cloneFig;
            if (thisFig == stereo) clone.stereo = (FigText) cloneFig;
        }
        return clone;
    }
// _enclosedFigs, _encloser and _eventSenders may also need to be cloned
// must check usage
// MVW: What are these Clone functions used for? Who would want to clone a fig? 


    /** Default Reply text to be shown while placing node in diagram. 
     * Overrule this when the text is not "new [UMLClassName]".  
     * @return the text to be shown while placing node in diagram
     */
    public String placeString() {
        if (org.argouml.model.ModelFacade.isAModelElement(getOwner())) {
            String placeString = ModelFacade.getName(getOwner());
            if (placeString == null) {
                placeString = "new " + ModelFacade.getUMLClassName(getOwner());
            }
            return placeString;
        }
        return "";
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @param id UID
     */
    public void setItemUID(ItemUID id) {
        itemUid = id;
    }

    /**
     * @return UID
     */
    public ItemUID getItemUID() {
        return itemUid;
    }

    /**
     * Get the Fig that displays the model element name
     * @return the name Fig
     */
    public FigText getNameFig() {
        return name;
    }

    /**
     * Set the Fig that displays the model element name
     * @param fig the name Fig
     */
    protected void setNameFig(FigText fig) {
        name = fig;
    }

    /**
     * Get the name of the model element this Fig represents
     * @return the name of the model element
     */
    public String getName() {
        return name.getText();
    }

    /**
     * Change the name of the model element this Fig represents
     * @param n the name of the model element
     */
    public void setName(String n) {
        name.setText(n);
    }

    /**
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        ToDoList list = Designer.theDesigner().getToDoList();
        Vector items = (Vector) list.elementsForOffender(getOwner()).clone();
        if (items != null && items.size() > 0) {
            ArgoJMenu critiques = new ArgoJMenu(BUNDLE, "menu.popup.critiques");
            ToDoItem itemUnderMouse = hitClarifier(me.getX(), me.getY());
            if (itemUnderMouse != null) {
                critiques.add(new ActionGoToCritique(itemUnderMouse));
                critiques.addSeparator();
            }
            int size = items.size();
            for (int i = 0; i < size; i++) {
                ToDoItem item = (ToDoItem) items.elementAt(i);
                if (item != itemUnderMouse) {
                    critiques.add(new ActionGoToCritique(item));
                }
            }
            popUpActions.insertElementAt(new JSeparator(), 0);
            popUpActions.insertElementAt(critiques, 0);
        }
        // POPUP_ADD_OFFSET should be equal to the number of items added here:
        popUpActions.addElement(new JSeparator());
        popUpActions.addElement(ActionProperties.getSingleton());
        popUpActions.addElement(ActionDeleteFromDiagram.getSingleton());
        return popUpActions;
    }

    ////////////////////////////////////////////////////////////////
    // Fig API

    /**
     * @see org.tigris.gef.presentation.Fig#getEnclosingFig()
     */
    public Fig getEnclosingFig() {
        return encloser;
    }

    /**
     * Updates the modelelement container if the fig is moved in or
     * out another fig. If this fig doesn't have an enclosing fig
     * anymore, the namespace of the diagram will be the owning
     * modelelement. If this fig is moved inside another
     * FigNodeModelElement the owner of that fignodemodelelement will
     * be the owning modelelement.
     * @see Fig#setEnclosingFig(Fig)
     */
    public void setEnclosingFig(Fig newEncloser) {
	super.setEnclosingFig(newEncloser);
	Fig oldEncloser = encloser;
	if (newEncloser != oldEncloser) {
	    Object owningModelelement = null;
	    if (newEncloser == null && isVisible()) {
	        // If we are not visible most likely we're being deleted.

		// moved outside another fig onto the diagram canvas
		Project currentProject =
		    ProjectManager.getManager().getCurrentProject();
                ArgoDiagram diagram = currentProject.getActiveDiagram();
                if (diagram instanceof UMLDiagram
			&& ((UMLDiagram) diagram).getNamespace() != null) {
                    owningModelelement = ((UMLDiagram) diagram).getNamespace();
                } else {
                    owningModelelement = currentProject.getRoot();
                }
	    } else {
		// moved into a fig
                if (ModelFacade.isABase(newEncloser.getOwner())) {
                    owningModelelement = newEncloser.getOwner();
                }
            }
            if (owningModelelement != null
		&& getOwner() != null
		&& (!ModelManagementHelper.getHelper()
		    .isCyclicOwnership(owningModelelement, getOwner()))
		&& ((CoreHelper.getHelper()
			.isValidNamespace(getOwner(),
					  owningModelelement)))) {
                ModelFacade.setModelElementContainer(getOwner(), 
						     owningModelelement);
                // TODO: move the associations to the correct owner (namespace)
            }
        }
	if (newEncloser != encloser) {
	    if (encloser instanceof FigNodeModelElement) {
		((FigNodeModelElement) encloser).removeEnclosedFig(this);
            }
	    if (newEncloser instanceof FigNodeModelElement) {
		((FigNodeModelElement) newEncloser).addEnclosedFig(this);
            }
	}
        encloser = newEncloser;
    }

    /**
     * @param fig The fig to be added 
     */
    public void addEnclosedFig(Fig fig) {
        enclosedFigs.add(fig);
    }

    /**
     * @param fig The Fig to be removed
     */
    public void removeEnclosedFig(Fig fig) {
        enclosedFigs.remove(fig);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getEnclosedFigs()
     */
    public Vector getEnclosedFigs() {
        return enclosedFigs;
    }

    /** Update the order of this fig and the order of the
     *    figs that are inside of this fig 
     * @param figures in the new order 
     */
    public void elementOrdering(Vector figures) {
        int size = figures.size();
        getLayer().bringToFront(this);
        if (figures != null && (size > 0)) {
            for (int i = 0; i < size; i++) {
                Object o = figures.elementAt(i);
                if (o instanceof FigNodeModelElement
                    && o != getEnclosingFig()) {
                    FigNodeModelElement fignode = (FigNodeModelElement) o;
                    Vector enclosed = fignode.getEnclosedFigs();
                    fignode.elementOrdering(enclosed);
                }
            }
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionNodeClarifiers(this);
    }

    /**
     * Overridden to paint shadows. This method supports painting shadows
     * for any FigNodeModelElement. Any Figs that are nested within the
     * FigNodeModelElement will be shadowed.<p>
     *
     * TODO: If g is not a Graphics2D shadows cannot be painted. This is
     * a problem when saving the diagram as SVG.
     *
     * @param g is a Graphics that we paint this object on.
     */
    public void paint(Graphics g) {
        if (shadowSize > 0
	        && g instanceof Graphics2D) {
            int width = getWidth();
            int height = getHeight();
            int x = getX();
            int y = getY();

            // Only create new shadow image if figure size has changed.
            if (width != cachedWidth 
                    || height != cachedHeight) {
                cachedWidth = width;
                cachedHeight = height;

                BufferedImage img = new BufferedImage(
                    width + 100,
                    height + 100,
                    BufferedImage.TYPE_INT_ARGB);

                // Paint figure onto offscreen image
                Graphics ig = img.getGraphics();
                ig.translate(50 - x, 50 - y);
                super.paint(ig);

                // Apply two filters to the image:
                // 1. Apply LookupOp which converts all pixel data in the
                //    figure to the same shadow color.
                // 2. Apply ConvolveOp which creates blurred effect around
                //    the edges of the shadow.
                shadowImage = SHADOW_CONVOLVE_OP.filter(
                    SHADOW_LOOKUP_OP.filter(img, null), null);
            }

            // Paint shadow image onto canvas
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(
                shadowImage,
                null,
                x + shadowSize - 50,
                y + shadowSize - 50);
        }
            
        // Paint figure on top of shadow
        super.paint(g);
    }
    
    /**
     * Displays visual indications of pending ToDoItems.
     * Please note that the list of advices (ToDoList) is not the same
     * as the list of element known by the FigNode (_figs). Therefore,
     * it is necessary to check if the graphic item exists before drawing
     * on it. See ClAttributeCompartment for an example.
     * @see org.argouml.uml.cognitive.critics.ClAttributeCompartment
     */
    public void paintClarifiers(Graphics g) {
        int iconX = getX();
        int iconY = getY() - 10;
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
                icon.paintIcon(null, g, iconX, iconY);
                iconX += icon.getIconWidth();
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
                icon.paintIcon(null, g, iconX, iconY);
                iconX += icon.getIconWidth();
            }
        }
    }

    /**
     * @param x the x of the hit
     * @param y the y of the hit
     * @return the todo item of which the clarifier has been hit
     */
    public ToDoItem hitClarifier(int x, int y) {
        int iconX = getX();
        ToDoList list = Designer.theDesigner().getToDoList();
        Vector items = list.elementsForOffender(getOwner());
        int size = items.size();
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem) items.elementAt(i);
            Icon icon = item.getClarifier();
            int width = icon.getIconWidth();
            if (y >= getY() - 15
                    && y <= getY() + 10
                    && x >= iconX
                    && x <= iconX + width) {
                return item;
            }
            iconX += width;
        }
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem) items.elementAt(i);
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier) icon).setFig(this);
                ((Clarifier) icon).setToDoItem(item);
                if (((Clarifier) icon).hit(x, y)) {
                    return item;
                }
            }
        }
        items = list.elementsForOffender(this);
        size = items.size();
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem) items.elementAt(i);
            Icon icon = item.getClarifier();
            int width = icon.getIconWidth();
            if (y >= getY() - 15
                    && y <= getY() + 10
                    && x >= iconX
                    && x <= iconX + width) {
                return item;
            }
            iconX += width;
        }
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem) items.elementAt(i);
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier) icon).setFig(this);
                ((Clarifier) icon).setToDoItem(item);
                if (((Clarifier) icon).hit(x, y)) {
                    return item;
                }
            }
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
            tip = item.getHeadline() + " ";
        } else if (getOwner() != null) {
            tip = getOwner().toString();
        } else {
            tip = toString();
        }
        if (tip != null && tip.length() > 0 && !tip.endsWith(" ")) {
            tip += " ";
        }
        return tip;
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /**
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange(PropertyChangeEvent pce) {
        LOG.debug("in vetoableChange");
        Object src = pce.getSource();
        if (src == getOwner()) {
            DelayedChangeNotify delayedNotify =
                new DelayedChangeNotify(this, pce);
            SwingUtilities.invokeLater(delayedNotify);
        } else {
            LOG.debug("FigNodeModelElement got vetoableChange"
		      + " from non-owner:"
		      + src);
        }
    }

    /**
     * @see org.argouml.kernel.DelayedVChangeListener#delayedVetoableChange(java.beans.PropertyChangeEvent)
     */
    public void delayedVetoableChange(PropertyChangeEvent pce) {
        LOG.debug("in delayedVetoableChange");
        // update any text, colors, fonts, etc.
        renderingChanged();
        endTrans();
    }

    /**
     * set some new bounds
     */
    protected void updateBounds() {
        if (!checkSize) {
            return;
        }
        Rectangle bbox = getBounds();
        Dimension minSize = getMinimumSize();
        bbox.width = Math.max(bbox.width, minSize.width);
        bbox.height = Math.max(bbox.height, minSize.height);
        setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pve) {
        Object src = pve.getSource();
        String pName = pve.getPropertyName();
        if (pName.equals("editing")
                && Boolean.FALSE.equals(pve.getNewValue())) {
	    LOG.debug("finished editing");
            try {
                //parse the text that was edited
                textEdited((FigText) src);
                // resize the FigNode to accomodate the new text
                Rectangle bbox = getBounds();
                Dimension minSize = getMinimumSize();
                bbox.width = Math.max(bbox.width, minSize.width);
                bbox.height = Math.max(bbox.height, minSize.height);
                setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
                endTrans();
            } catch (PropertyVetoException ex) {
                LOG.error("could not parse the text entered. "
			  + "PropertyVetoException",
			  ex);
            }
        } else {
            super.propertyChange(pve);
        }
    }

    /**
     * This method is called after the user finishes editing a text
     * field that is in the FigNodeModelElement.  Determine which
     * field and update the model.  This class handles the name,
     * subclasses should override to handle other text elements.
     * 
     * @param ft the FigText that has been edited and contains the new text
     * @throws PropertyVetoException thrown when new text represents 
     * an unacceptable value
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        if (ft == name) {
            if (getOwner() == null) {
                return;
            }
            try {
                ParserDisplay.SINGLETON.parseModelElement(getOwner(),
							  ft.getText().trim());
                ProjectBrowser.getInstance().getStatusBar().showStatus("");
                updateNameText();
            } catch (ParseException pe) {
                ProjectBrowser.getInstance().getStatusBar()
		    .showStatus("Error: " + pe + " at " + pe.getErrorOffset());
                // if there was a problem parsing,
                // then reset the text in the fig - because the model was not
                // updated.
                if (ModelFacade.getName(getOwner()) != null) {
                    ft.setText(ModelFacade.getName(getOwner()));
                } else {
                    ft.setText("");
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////
    // event handlers - MouseListener implementation

    /**
     * If the user double clicks on any part of this FigNode, pass it
     * down to one of the internal Figs. This allows the user to
     * initiate direct text editing.
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        if (!readyToEdit) {
            if (ModelFacade.isAModelElement(getOwner())) {
                ModelFacade.setName(getOwner(), "");
                readyToEdit = true;
            } else {
                LOG.debug("not ready to edit name");
                return;
            }
        }
        if (me.isConsumed()) {
            return;
        }
        if (me.getClickCount() >= 2
                && !(me.isPopupTrigger()
		|| me.getModifiers() == InputEvent.BUTTON3_MASK)) {
            if (getOwner() == null) {
                return;
            }
            Rectangle r = new Rectangle(me.getX() - 2, me.getY() - 2, 4, 4);
            Fig f = hitFig(r);
            if (f instanceof MouseListener && f.isVisible()) {
		((MouseListener) f).mouseClicked(me);
            } else if (f instanceof FigGroup && f.isVisible()) {
                //this enables direct text editing for sub figs of a
                //FigGroup object:
                Fig f2 = ((FigGroup) f).hitFig(r);
                if (f2 instanceof MouseListener) {
		    ((MouseListener) f2).mouseClicked(me);
                } else {
                    createFeatureIn((FigGroup) f, me);
                }
            }
        }
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) {
        if (!readyToEdit) {
            if (ModelFacade.isAModelElement(getOwner())) {
                ModelFacade.setName(getOwner(), "");
                readyToEdit = true;
            } else {
                LOG.debug("not ready to edit name");
                return;
            }
        }
        if (ke.isConsumed() || getOwner() == null) {
            return;
        }
        name.keyPressed(ke);
    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     *
     * not used, do nothing.
     */
    public void keyReleased(KeyEvent ke) {
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     *
     * not used, do nothing.
     */
    public void keyTyped(KeyEvent ke) {
    }

    ////////////////////////////////////////////////////////////////
    // internal methods

    /** This is called after any part of the UML MModelElement has
     *  changed. This method automatically updates the name FigText.
     *  Subclasses should override and update other parts.
     *
     * @param mee the ModelElementEvent that caused the change
     */
    protected void modelChanged(MElementEvent mee) {
        if (mee == null) {
            throw new IllegalArgumentException("event may never be null "
					       + "with modelchanged");
        }
        if (getOwner() == null) {
            return;
        }
        if ("name".equals(mee.getName()) && mee.getSource() == getOwner()) {
            updateNameText();
            damage();
        }
        if ((mee.getSource() == getOwner()
	     && mee.getName().equals("stereotype"))) {
            if (mee.getOldValue() != null) {
                UmlModelEventPump.getPump()
                    .removeModelEventListener(this, mee.getRemovedValue(), 
                            		      "name");
            }
            if (mee.getNewValue() != null) {
                UmlModelEventPump.getPump()
                    .addModelEventListener(this, mee.getNewValue(), "name");
            }
            updateStereotypeText();
            damage();
        }
    }

    /**
     * Create a new "feature" in the owner fig.
     * 
     * must be overridden to make sense
     * (I didn't want to make it abstract because it might not be required)
     * 
     * @param fg The fig group to which this applies
     * @param me The input event that triggered us. In the current
     *            implementation a mouse double click.
     */
    protected void createFeatureIn(FigGroup fg, InputEvent me) {
        
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent mee) {
        //if (_group != null) _group.propertySet(mee);        
        modelChanged(mee);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent mee) {
        //if (_group != null) _group.listRoleItemSet(mee);
        modelChanged(mee);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent mee) {
        //if (_group != null) _group.recovered(mee);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent mee) {
        LOG.debug("deleting: " + this + mee);
        Object o = mee.getSource();
        if (o == getOwner()) {
            removeFromDiagram();
        } else if (isPartlyOwner(o)) {
            updateBounds();
            damage();
            return;
        }

    }

    /**
     * @param o the given object
     * @return true if one of my figs has the given object as owner
     */
    protected boolean isPartlyOwner(Object o) {
        if (o == null || o == getOwner()) {
            return true;
        }
        Iterator it = getFigs(null).iterator();
        while (it.hasNext()) {
            Fig fig = (Fig) it.next();
            if (isPartlyOwner(fig, o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param fig the given fig (may be a group)
     * @param o the given object
     * @return true if one of the given figs has the given object as owner
     */
    protected boolean isPartlyOwner(Fig fig, Object o) {
        if (o == null) {
            return false;
        }
        if (o == fig.getOwner()) {
            return true;
        }
        if (fig instanceof FigGroup) {
            Iterator it = ((FigGroup) fig).getFigs(null).iterator();
            while (it.hasNext()) {
                Fig fig2 = (Fig) it.next();
                if (isPartlyOwner(fig2, o)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent mee) {
        //if (_group != null) _group.roleAdded(mee);
        modelChanged(mee);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent mee) {
        //if (_group != null) _group.roleRemoved(mee);
        modelChanged(mee);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#deleteFromModel()
     */
    public void deleteFromModel() {
        Object own = getOwner();
        if (own != null) {
            Trash.SINGLETON.addItemFrom(own, null);
            if (ModelFacade.isAModelElement(own)) {
                UmlFactory.getFactory().delete(own);
            }
        }
        Iterator it = getFigs(null).iterator();
        while (it.hasNext()) {
            ((Fig) it.next()).deleteFromModel();
        }
        super.dispose();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object own) {
        updateListeners(own);
        super.setOwner(own);
        if (ModelFacade.isAModelElement(own)
                && ModelFacade.getUUID(own) == null) {
            ModelFacade.setUUID(own, UUIDManager.getInstance().getNewUUID());
        }
        readyToEdit = true;
        if (own != null) {
            renderingChanged();
        }
        updateBounds();
        bindPort(own, bigPort);
    }

    /**
     * Updates the text of the sterotype FigText. Override in subclasses to get
     * wanted behaviour.
     *
     * TODO: remove all 'misuses' of the stereotype figtexts (like in
     * FigInterface)
     */
    protected void updateStereotypeText() {
        Object stereotype = null;
        if (getOwner() == null) {
            LOG.warn("Owner of [" + this.toString() + "/"
		     + this.getClass() + "] is null.");
            LOG.warn("I return...");
	    return;
        }
        if (ModelFacade.getStereotypes(getOwner()).size() > 0) {
            stereotype =
		ModelFacade.getStereotypes(getOwner()).iterator().next();
        }
        stereo.setText(Notation.generate(this, stereotype));
    }

    /**
     * Updates the text of the name FigText.
     */
    protected void updateNameText() {
        if (readyToEdit) {
            if (getOwner() == null)
                return;
            String nameStr =
                Notation.generate(this, ModelFacade.getName(getOwner()));
            name.setText(nameStr);
            updateBounds();
        }
    }

    /**
     * Implementations of this method should register/unregister the fig for all
     * (model)events. For FigNodeModelElement only the fig itself is registred
     * as listening to events fired by the owner itself. But for, for example,
     * FigClass the fig must also register for events fired by the operations
     * and attributes of the owner.
     * @param newOwner the new owner for the listeners
     */
    protected void updateListeners(Object newOwner) {
        Object oldOwner = getOwner();
        if (oldOwner != null) {
            UmlModelEventPump.getPump().removeModelEventListener(this,
								 oldOwner);
        }
        if (newOwner != null) {
            UmlModelEventPump.getPump().addModelEventListener(this, newOwner);
        }

    }

    /**
     * Returns the notation name for this fig. First start to
     * implement notations on a per fig basis.
     * @see org.argouml.application.api.NotationContext#getContextNotation()
     */
    public NotationName getContextNotation() {
        return currentNotationName;
    }

    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationChanged(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationChanged(ArgoNotationEvent event) {
        PropertyChangeEvent changeEvent =
	    (PropertyChangeEvent) event.getSource();
        currentNotationName =
	    Notation.findNotation((String) changeEvent.getNewValue());
        renderingChanged();
    }

    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationAdded(ArgoNotationEvent event) {
    }

    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationRemoved(ArgoNotationEvent event) {
    }

    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderAdded(ArgoNotationEvent event) {
    }

    /**
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderRemoved(ArgoNotationEvent event) {
    }

    /**
     * Rerenders the fig if needed. This functionality was originally
     * the functionality of modelChanged but modelChanged takes the
     * event now into account.
     */
    public void renderingChanged() {
        updateNameText();
        updateStereotypeText();
        updateBounds();
        damage();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    public void calcBounds() {
        if (suppressCalcBounds) {
            return;
        }
        super.calcBounds();
    }

    /** The setter for checkSize.
     * @param flag the new value
     */
    public void enableSizeChecking(boolean flag) {
        checkSize = flag;
    }

    /**
     * Returns the new size of the FigGroup (either attributes or
     * operations) after calculation new bounds for all sub-figs,
     * considering their minimal sizes; FigGroup need not be
     * displayed; no update event is fired.<p>
     *
     * This method has side effects that are sometimes used.
     * 
     * @param fg the FigGroup to be updated
     * @param x x
     * @param y y
     * @param w w 
     * @param h h
     * @return the new dimension
     */
    protected Dimension updateFigGroupSize(
				       FigGroup fg,
				       int x,
				       int y,
				       int w,
				       int h) {
        int newW = w;
        int n = fg.getFigs(null).size() - 1;
        int newH = checkSize ? Math.max(h, ROWHEIGHT * Math.max(1, n) + 2) : h;
        int step = (n > 0) ? (newH - 1) / n : 0;
        // width step between FigText objects int maxA =
        //Toolkit.getDefaultToolkit().getFontMetrics(LABEL_FONT).getMaxAscent();

        //set new bounds for all included figs
        Enumeration figs = fg.elements();
        Fig myBigPort = (Fig) figs.nextElement();
        Fig fi;
        int fw, yy = y;
        while (figs.hasMoreElements()) {
            fi = (Fig) figs.nextElement();
            fw = fi.getMinimumSize().width;
            if (!checkSize && fw > newW - 2)
                fw = newW - 2;
            fi.setBounds(x + 1, yy + 1, fw, Math.min(ROWHEIGHT, step) - 2);
            if (checkSize && newW < fw + 2)
                newW = fw + 2;
            yy += step;
        }
        myBigPort.setBounds(x, y, newW, newH);
        // rectangle containing all following FigText objects
        fg.calcBounds();
        return new Dimension(newW, newH);
    }

    /**
     * @param size the new shadow size
     */
    public void setShadowSize(int size) {
        shadowSize = size;
    }

    /**
     * @return the current shadow size
     */
    public int getShadowSize() {
        return shadowSize;
    }

    /**
     * Necessary since GEF contains some errors regarding the hit subject.
     * @see org.tigris.gef.presentation.Fig#hit(Rectangle)
     */
    public boolean hit(Rectangle r) {
        int cornersHit = countCornersContained(r.x, r.y, r.width, r.height);
        if (_filled)
            return cornersHit > 0 || intersects(r);
        else
            return (cornersHit > 0 && cornersHit < 4) || intersects(r);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    public void removeFromDiagram() {
        if (this instanceof ArgoEventListener) {
            ArgoEventPump.removeListener(this);
        }

        Object own = getOwner();
        if (org.argouml.model.ModelFacade.isAClassifier(own)) {
            Iterator it = ModelFacade.getFeatures(own).iterator();
            while (it.hasNext()) {
                Object feature = it.next();
                if (ModelFacade.isAOperation(feature)) {
                    Iterator it2 =
			ModelFacade.getParameters(feature).iterator();
                    while (it2.hasNext()) {
                        UmlModelEventPump.getPump().removeModelEventListener(
                                                        	    this,
                                                                    it2.next());
                    }
                }
                UmlModelEventPump.getPump().removeModelEventListener(
								     this,
								     feature);
            }
        }
        if (ModelFacade.isABase(own)) {
            UmlModelEventPump.getPump().removeModelEventListener(this, own);
        }
        super.removeFromDiagram();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#damage()
     */
    public void damage() {
        updateEdges();
        super.damage();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#postLoad()
     */
    public void postLoad() {       
        super.postLoad();
        if (this instanceof ArgoEventListener) {
            ArgoEventPump.removeListener(this);
            ArgoEventPump.addListener(this);
        }
        Iterator it = getFigs(null).iterator();
        while (it.hasNext()) {
            Fig fig = (Fig) it.next();
            if (fig instanceof ArgoEventListener) {
                // cannot do the adding of listeners recursive since
                // some are not children of FigNodeModelELement or
                // FigEdgeModelElement
                ArgoEventPump.removeListener((ArgoEventListener) fig);
                ArgoEventPump.addListener((ArgoEventListener) fig);
            }
        }
    }

    /**
     * @see org.tigris.gef.presentation.FigNode#superTranslate(int, int)
     *
     * Overridden to notify project that save is needed when figure is moved.
     */
    public void superTranslate(int dx, int dy) {
        super.superTranslate(dx, dy);
        Project p = ProjectManager.getManager().getCurrentProject();
        if (p != null) {      
            p.setNeedsSave(true);
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setHandleBox(int, int, int, int)
     *
     * Overridden to notify project that save is needed when figure is resized.
     */
    public void setHandleBox(int x, int y, int w, int h) {
        super.setHandleBox(x, y, w, h);
        Project p = ProjectManager.getManager().getCurrentProject();
        if (p != null) {      
            p.setNeedsSave(true);
        }
    }
    
    

    /**
     * Adds a fig to this FigNodeModelElement and removes it from the
     * group it belonged to if any.  Correction to the GEF
     * implementation that does not handle the double association
     * correctly.<p>
     *
     * @see FigGroup#addFig(Fig)
     * TODO: remove this once GEF0.10 is in place and tested
     */
    public void addFig(Fig f) {
        Fig group = f.getGroup();
        if (group != null) {
            ((FigGroup) group).removeFig(f);
        }
        super.addFig(f);
    }

    /**
     * Set the Fig containing the stereotype
     * @param fig the stereotype Fig
     */
    protected void setStereotypeFig(Fig fig) {
        stereo = (FigText) fig;
    }

    /**
     * Get the Fig containing the stereotype
     * @return the stereotype Fig
     */
    protected Fig getStereotypeFig() {
        return stereo;
    }

    /**
     * Get the FigText containing the stereotype
     * @return the stereotype FigText
     */
    protected FigText getStereotypeFigText() {
        return stereo;
    }

    /**
     * Set the text describing the stereotype
     * @param stereotype the stereotype text
     */
    public void setStereotype(String stereotype) {
        stereo.setText(stereotype);
    }

    /**
     * Get the text describing the stereotype
     * @return the stereotype text
     */
    public String getStereotype() {
        return stereo.getText();
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
     * @param bp The _bigPort to set.
     */
    protected void setBigPort(Fig bp) {
        this.bigPort = bp;
    }

    /**
     * @return Returns the _bigPort.
     */
    protected Fig getBigPort() {
        return bigPort;
    }

    /**
     * @return Returns the checkSize.
     */
    protected boolean isCheckSize() {
        return checkSize;
    }

    /**
     * @param e The _encloser to set.
     */
    protected void setEncloser(Fig e) {
        this.encloser = e;
    }

    /**
     * @return Returns the _encloser.
     */
    protected Fig getEncloser() {
        return encloser;
    }
    /**
     * @return Returns the ReadyToEdit.
     */
    protected boolean isReadyToEdit() {
        return readyToEdit;
    }

    /**
     * @param v if ready to edit
     */
    protected void setReadyToEdit(boolean v) {
        readyToEdit = v;
    }
    
    /**
     * @param scb The suppressCalcBounds to set.
     */
    protected void setSuppressCalcBounds(boolean scb) {
        this.suppressCalcBounds = scb;
    }
} /* end class FigNodeModelElement */

