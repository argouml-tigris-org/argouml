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

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.application.api.ArgoEventListener;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.application.api.NotationContext;
import org.argouml.application.api.NotationName;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ItemUID;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.kernel.NsumlEnabler;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.DiElement;
import org.argouml.model.Model;
import org.argouml.ui.ActionGoToCritique;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.Clarifier;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.UUIDHelper;
import org.argouml.uml.generator.ParserDisplay;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.util.CollectionUtil;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Abstract class to display diagram icons for UML ModelElements that
 * look like nodes and that have editable names and can be
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
        NotationContext,
        PathContainer,
        ArgoNotationEventListener {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(FigNodeModelElement.class);

    private DiElement diElement = null;
    
    ////////////////////////////////////////////////////////////////
    // constants

    private NotationName currentNotationName;
    private static final Font LABEL_FONT;
    private static final Font ITALIC_LABEL_FONT;

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
     * See #getPopUpActions()
     */
    private static int popupAddOffset;

    // Fields used in paint() for painting shadows
    private BufferedImage           shadowImage = null;
    private int                     cachedWidth = -1;
    private int                     cachedHeight = -1;
    private static final LookupOp   SHADOW_LOOKUP_OP;
    private static final ConvolveOp SHADOW_CONVOLVE_OP;

    /**
     * The intensity value of the shadow color (0-255).
     */
    protected static final int SHADOW_COLOR_VALUE = 32;

    /**
     * The transparency value of the shadow color (0-255).
     */
    protected static final int SHADOW_COLOR_ALPHA = 128;

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

    /**
     * Used for #buildModifierPopUp().
     */
    protected static final int ROOT = 1;

    /**
     * Used for #buildModifierPopUp().
     */
    protected static final int ABSTRACT = 2;

    /**
     * Used for #buildModifierPopUp().
     */
    protected static final int LEAF = 4;

    /**
     * Used for #buildModifierPopUp().
     */
    protected static final int ACTIVE = 8;

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
     */
    private Fig stereotypeFig;

    /**
     * EnclosedFigs are the Figs that are enclosed by this figure. Say that
     * it is a Package then these are the Classes, Interfaces, Packages etc
     * that are on this figure. This is not the same as the figures in the
     * FigGroup that this FigNodeModelElement "is", since these are the
     * figures that make up this high-level primitive figure.
     */
    private Vector enclosedFigs = new Vector();

    /**
     * The figure enclosing this figure.
     */
    private Fig encloser = null;

    private boolean readyToEdit = true;
    private boolean suppressCalcBounds = false;

    private int shadowSize =
        Configuration.getInteger(Notation.KEY_DEFAULT_SHADOW_WIDTH, 1);

    private ItemUID itemUid;

    /**
     * Set the removeFromDiagram to false if this node may not
     * be removed from the diagram.
     */
    private boolean removeFromDiagram = true;
    
    /**
     * Set this to force a repaint of the shadow. 
     * Normally repainting only happens 
     * when the outside boundaries change 
     * (for performance reasons (?)). 
     * In some cases this does not
     * suffice, and you can set this attribute to force the update.
     */
    private boolean forceRepaint;

    /**
     * Flag that indicates if the full namespace path should be shown 
     * in front of the name.
     */
    private boolean pathVisible = false;
    
    /**
     * The main constructor.
     *
     */
    public FigNodeModelElement() {
        // this rectangle marks the whole interface figure; everything
        // is inside it:
        bigPort = new FigRect(10, 10, 0, 0, Color.cyan, Color.cyan);

        name = new FigSingleLineText(10, 10, 90, 21, true);
        name.setLineWidth(1);
        name.setFilled(true);
        name.setText(placeString());

        if (NsumlEnabler.isNsuml()) {
            stereotypeFig = new FigSingleLineText(10, 10, 90, 15, true);
        } else {
            stereotypeFig = new FigStereotypesCompartment(10, 10, 90, 15);
        }

        readyToEdit = false;
        ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
        currentNotationName = Notation.getConfigueredNotation();
    }

    /**
     * Partially construct a new FigNode.  This method creates the
     * name element that holds the name of the model element and adds
     * itself as a listener.
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
    protected void finalize() {
        ArgoEventPump.removeListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
    }

    /**
     * After the base clone method has been called determine which child
     * figs of the clone represent the name, stereotype and port. <p>
     *
     * The clone function is used by Copy/Paste operations.
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigNodeModelElement clone = (FigNodeModelElement) super.clone();
        Iterator thisIter = this.getFigs().iterator();
        Iterator cloneIter = clone.getFigs().iterator();
        while (thisIter.hasNext()) {
            Fig thisFig = (Fig) thisIter.next();
            Fig cloneFig = (Fig) cloneIter.next();
            if (thisFig == getBigPort()) {
                clone.setBigPort(cloneFig);
            }
            if (thisFig == name) {
                clone.name = (FigText) cloneFig;
            }
            if (thisFig == stereotypeFig) {
                clone.stereotypeFig = (FigStereotype) cloneFig;
            }
        }
        return clone;
    }
// TODO: _enclosedFigs, _encloser and _eventSenders may also need to be cloned



    /**
     * Default Reply text to be shown while placing node in diagram.
     * Overrule this when the text is not "new [UMLClassName]".
     *
     * @return the text to be shown while placing node in diagram
     */
    public String placeString() {
        if (Model.getFacade().isAModelElement(getOwner())) {
            String placeString = Model.getFacade().getName(getOwner());
            if (placeString == null) {
                placeString =
                    "new " + Model.getFacade().getUMLClassName(getOwner());
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
     * Get the Fig that displays the model element name.
     *
     * @return the name Fig
     */
    public FigText getNameFig() {
        return name;
    }

    /**
     * Set the Fig that displays the model element name.
     *
     * @param fig the name Fig
     */
    protected void setNameFig(FigText fig) {
        name = fig;
    }

    /**
     * Get the name of the model element this Fig represents.
     *
     * @return the name of the model element
     */
    public String getName() {
        return name.getText();
    }

    /**
     * Change the name of the model element this Fig represents.
     *
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
        if (TargetManager.getInstance().getTargets().size() == 1) {
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
                    if (item != itemUnderMouse) {
                        critiques.add(new ActionGoToCritique(item));
                    }
                }
                popUpActions.insertElementAt(new JSeparator(), 0);
                popUpActions.insertElementAt(critiques, 0);
            }

            // Add stereotypes submenu
            if (!NsumlEnabler.isNsuml()) {
                Collection models =
                    ProjectManager.getManager().getCurrentProject().getModels();
                ArrayList availableStereotypes =
                    new ArrayList(Model.getExtensionMechanismsHelper().
                    getAllPossibleStereotypes(models, getOwner()));
                
                availableStereotypes.removeAll(Model.getFacade().getStereotypes(getOwner()));
                
                if (!availableStereotypes.isEmpty()) {
                    ArgoJMenu stereotypes = new ArgoJMenu("menu.popup.addstereotypes");
                    Iterator it = availableStereotypes.iterator();
                    while (it.hasNext()) {
                        stereotypes.add(new ActionAddStereotype(getOwner(), it.next()));
                    }
                    popUpActions.insertElementAt(new JSeparator(), 0);
                    popUpActions.insertElementAt(stereotypes, 0);
                }
            }
        }

        return popUpActions;
    }

    /**
     * @return the pop-up menu item for Visibility
     */
    protected Object buildVisibilityPopUp() {
        ArgoJMenu visibilityMenu = new ArgoJMenu("menu.popup.visibility");

        visibilityMenu.addRadioItem(new ActionVisibilityPublic(getOwner()));
        visibilityMenu.addRadioItem(new ActionVisibilityPrivate(getOwner()));
        visibilityMenu.addRadioItem(new ActionVisibilityProtected(getOwner()));

        return visibilityMenu;
    }

    /**
     * Build a pop-up menu item for the various modifiers.<p>
     *
     * This function is designed to be easily extendable with new items.
     *
     * @param items bitwise OR of the items: ROOT, ABSTRACT, LEAF, ACTIVE.
     * @return the menu item
     */
    protected Object buildModifierPopUp(int items) {
        ArgoJMenu modifierMenu = new ArgoJMenu("menu.popup.modifiers");

        if ((items & ABSTRACT) > 0) {
            modifierMenu.addCheckItem(new ActionModifierAbstract(getOwner()));
	}
        if ((items & LEAF) > 0) {
            modifierMenu.addCheckItem(new ActionModifierLeaf(getOwner()));
	}
        if ((items & ROOT) > 0) {
            modifierMenu.addCheckItem(new ActionModifierRoot(getOwner()));
	}
        if ((items & ACTIVE) > 0) {
            modifierMenu.addCheckItem(new ActionModifierActive(getOwner()));
	}

        return modifierMenu;
    }

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
	Fig oldEncloser = encloser;
	super.setEnclosingFig(newEncloser);
	if (newEncloser != oldEncloser) {
	    Object owningModelelement = null;
	    if (newEncloser == null && isVisible()) {
	        // If we are not visible most likely we're being deleted.

		// moved outside another fig onto the diagram canvas
		Project currentProject =
		    ProjectManager.getManager().getCurrentProject();
                ArgoDiagram diagram = currentProject.getActiveDiagram();
                // TODO: Who said this was about the active diagram?
                if (diagram instanceof UMLDiagram
			&& ((UMLDiagram) diagram).getNamespace() != null) {
                    owningModelelement = ((UMLDiagram) diagram).getNamespace();
                } else {
                    owningModelelement = currentProject.getRoot();
                }
	    } else if (newEncloser != null
                    && Model.getFacade().isABase(newEncloser.getOwner())) {
                owningModelelement = newEncloser.getOwner();
            }
            if (owningModelelement != null
		&& getOwner() != null
		&& (!Model.getModelManagementHelper()
		    .isCyclicOwnership(owningModelelement, getOwner()))
		&& ((Model.getCoreHelper()
			.isValidNamespace(getOwner(),
					  owningModelelement)))) {
                Model.getCoreHelper().setModelElementContainer(getOwner(),
						     owningModelelement);
                /* TODO: move the associations to the correct owner (namespace)
                 * i.e. issue 2151*/
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

    /**
     * Update the order of this fig and the order of the
     * figs that are inside of this fig.
     *
     * @param figures in the new order
     */
    public void elementOrdering(Vector figures) {
        int size = figures.size();
        getLayer().bringToFront(this);
        if (size > 0) {
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

            /* Only create a new shadow image if figure size has changed.
             * Which does not catch all cases: 
             * consider show/hide toggle of a stereotype on a package: 
             * in this case the total size remains, but the notch 
             * at the corner increases/decreases. 
             * Hence also check the "forceRepaint" attribute. 
             */
            if (width != cachedWidth
                    || height != cachedHeight
                    || forceRepaint) {
                forceRepaint = false;
                
                cachedWidth = width;
                cachedHeight = height;

                BufferedImage img =
		    new BufferedImage(width + 100,
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
                shadowImage =
		    SHADOW_CONVOLVE_OP.filter(
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
     * @param g the graphics device
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
            tip = Model.getFacade().getTipString(getOwner());
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
     * set some new bounds.
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
        if (pve instanceof DeleteInstanceEvent
                && pve.getSource() == getOwner()) {
            ProjectManager.getManager().getCurrentProject()
                .moveToTrash(getOwner());
        } else if (pName.equals("editing")
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
        } else if (pName.equals("editing")
                        && Boolean.TRUE.equals(pve.getNewValue())) {
            textEditStarted((FigText) src);
        } else {
            super.propertyChange(pve);
        }
        if (Model.getFacade().isABase(src)) {
            /* If the source of the event is an UML object,
             * e.g. the owner of this Fig (but not always only the owner
             * is shown, e.g. for a class, also its attributes are shown),
             * then the UML model has been changed.
             */
            modelChanged(pve);
        }
    }

    /**
     * This method is called when the user doubleclicked on the text field, 
     * and starts editing. Subclasses should overrule this field to e.g.
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
            showHelp("parsing.help.fig-nodemodelelement");
        }
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
     * field that is in the FigNodeModelElement.  Determine which
     * field and update the model.  This class handles the name,
     * and the stereotype,
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
                String msg = "statusmsg.bar.error.parsing.node-modelelement";
                Object[] args = {pe.getLocalizedMessage(), 
                                 new Integer(pe.getErrorOffset())};
                ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
                // if there was a problem parsing,
                // then reset the text in the fig - because the model was not
                // updated.
                if (Model.getFacade().getName(getOwner()) != null) {
                    ft.setText(Model.getFacade().getName(getOwner()));
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
            if (Model.getFacade().isAModelElement(getOwner())) {
                Model.getCoreHelper().setName(getOwner(), "");
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
            if (Model.getFacade().isAModelElement(getOwner())) {
                Model.getCoreHelper().setName(getOwner(), "");
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

    /**
     * This is called after any part of the UML MModelElement has
     * changed. This method automatically updates the name FigText.
     * Subclasses should override and update other parts.
     *
     * @param mee the ModelElementEvent that caused the change
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        if (mee == null) {
            throw new IllegalArgumentException("event may never be null "
                           + "with modelchanged");
        }
        if (getOwner() == null) {
            return;
        }
        if ("name".equals(mee.getPropertyName())
                && mee.getSource() == getOwner()) {
            updateNameText();
            damage();
        }
        if ((mee.getSource() == getOwner()
                && mee.getPropertyName().equals("stereotype"))) {
            if (mee.getOldValue() != null) {
                /* TODO: MVW: No idea what to replace getRemovedValue() with...
                 * I try getOldValue() for now. To be checked!
                 */
                Model.getPump().removeModelEventListener(this,
                        mee.getOldValue(), "name");
            }
            if (mee.getNewValue() != null) {
                Model.getPump().addModelEventListener(this,
                        mee.getNewValue(), "name");
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
     * @param o the given object
     * @return true if one of my figs has the given object as owner
     */
    protected boolean isPartlyOwner(Object o) {
        if (o == null || o == getOwner()) {
            return true;
        }
        Iterator it = getFigs().iterator();
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
            Iterator it = ((FigGroup) fig).getFigs().iterator();
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
     * @see org.tigris.gef.presentation.Fig#deleteFromModel()
     */
    public void deleteFromModel() {
        Object own = getOwner();
        if (own != null) {
            ProjectManager.getManager().getCurrentProject().moveToTrash(own);
        }
        Iterator it = getFigs().iterator();
        while (it.hasNext()) {
            ((Fig) it.next()).deleteFromModel();
        }
        super.deleteFromModel();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object own) {
        updateListeners(own);
        super.setOwner(own);
        if (Model.getFacade().isAModelElement(own)
                && UUIDHelper.getUUID(own) == null) {
            Model.getCoreHelper().setUUID(own, UUIDHelper.getNewUUID());
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
        if (getOwner() == null) {
            LOG.warn("Owner of [" + this.toString() + "/" + this.getClass()
                    + "] is null.");
            LOG.warn("I return...");
            return;
        }
        
        stereotypeFig.setOwner(getOwner());
    }

    /**
     * Updates the text of the name FigText.
     */
    protected void updateNameText() {
        if (readyToEdit) {
            if (getOwner() == null) {
                return;
            }
            String packName =
                Notation.generate(Notation.getConfigueredNotation(), 
                                Model.getFacade().getName(getOwner()));
            packName = generatePath() + packName;
            name.setText(packName);
            updateBounds();
        }
    }
    
    /**
     * TODO: this should move in some generic notation generation class, 
     * supporting other languages.
     * 
     * @return a string which represents the path
     */
    protected String generatePath() {
        String s = "";
        if (pathVisible) {
            Object p = getOwner();
            Stack stack = new Stack();
            Object ns = Model.getFacade().getNamespace(p);
            while (ns != null && !Model.getFacade().isAModel(ns)) {
                stack.push(Model.getFacade().getName(ns));
                ns = Model.getFacade().getNamespace(ns);
            }
            while (!stack.isEmpty()) {
                s += (String) stack.pop() + "::";
            }
            
            if (s.length() > 0 && !s.endsWith(":")) {
                s += "::";
            }
        }
        return s;
    }
    

    /**
     * @see org.argouml.uml.diagram.ui.PathContainer#isPathVisible()
     */
    public boolean isPathVisible() {
        return pathVisible;
    }
    
    /**
     * @see org.argouml.uml.diagram.ui.PathContainer#setPathVisible(boolean)
     */
    public void setPathVisible(boolean visible) {
        pathVisible = visible;
        if (readyToEdit) {
            renderingChanged();
            damage();
        }
    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    public String classNameAndBounds() {
        return super.classNameAndBounds()
                + "pathVisible=" + isPathVisible()
                + ";";
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
            Model.getPump().removeModelEventListener(this, oldOwner);
        }
        if (newOwner != null) {
            Model.getPump().addModelEventListener(this, newOwner);
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
     * @see org.argouml.application.api.NotationContext#setContextNotation(org.argouml.application.api.NotationName)
     */
    public void setContextNotation(NotationName nn) {
        currentNotationName = nn;
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

    /**
     * The setter for checkSize.
     *
     * @param flag the new value
     */
    public void enableSizeChecking(boolean flag) {
        checkSize = flag;
    }

//    /**
//     * Returns the new size of the FigGroup (either attributes or
//     * operations) after calculation new bounds for all sub-figs,
//     * considering their minimal sizes; FigGroup need not be
//     * displayed; no update event is fired.<p>
//     *
//     * This method has side effects that are sometimes used.
//     *
//     * @param fg the FigGroup to be updated
//     * @param x x
//     * @param y y
//     * @param w w
//     * @param h h
//     * @return the new dimension
//     */
//    protected Dimension updateFigGroupSize(
//				       FigGroup fg,
//				       int x,
//				       int y,
//				       int w,
//				       int h) {
//        int newW = w;
//        int n = fg.getFigs().size() - 1;
//        int newH = checkSize ? Math.max(h, ROWHEIGHT * Math.max(1, n) + 2) : h;
//        int step = (n > 0) ? (newH - 1) / n : 0;
//        // width step between FigText objects int maxA =
//        //Toolkit.getDefaultToolkit().getFontMetrics(LABEL_FONT).getMaxAscent();
//
//        //set new bounds for all included figs
//        Iterator figs = fg.iterator();
//        Fig myBigPort = (Fig) figs.next();
//        Fig fi;
//        int fw, yy = y;
//        while (figs.hasNext()) {
//            fi = (Fig) figs.next();
//            fw = fi.getMinimumSize().width;
//            if (!checkSize && fw > newW - 2) {
//                fw = newW - 2;
//            }
//            fi.setBounds(x + 1, yy + 1, fw, Math.min(ROWHEIGHT, step) - 2);
//            if (checkSize && newW < fw + 2) {
//                newW = fw + 2;
//            }
//            yy += step;
//        }
//        myBigPort.setBounds(x, y, newW, newH);
//        // rectangle containing all following FigText objects
//        fg.calcBounds();
//        return new Dimension(newW, newH);
//    }

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
        if (_filled) {
            return cornersHit > 0 || intersects(r);
        }
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
        if (Model.getFacade().isAClassifier(own)) {
            Iterator it = Model.getFacade().getFeatures(own).iterator();
            while (it.hasNext()) {
                Object feature = it.next();
                if (Model.getFacade().isAOperation(feature)) {
                    Iterator it2 =
			Model.getFacade().getParameters(feature).iterator();
                    while (it2.hasNext()) {
                        Model.getPump().removeModelEventListener(this,
                                it2.next());
                    }
                }
                Model.getPump().removeModelEventListener(this, feature);
            }
        }
        if (Model.getFacade().isABase(own)) {
            Model.getPump().removeModelEventListener(this, own);
        }
        shadowSize = 0;

        // This partly solves issue 3042.
//        Layer l = this.getLayer();
//        if (l != null) l.remove(this);

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
        Iterator it = getFigs().iterator();
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

//    /**
//     * Set the Fig containing the stereotype.
//     *
//     * @param fig the stereotype Fig
//     */
//    protected void setStereotypeFig(Fig fig) {
//        stereo = (FigStereotype)fig;
//    }
//
    /**
     * Get the Fig containing the stereotype.
     *
     * @return the stereotype Fig
     */
    protected Fig getStereotypeFig() {
        return stereotypeFig;
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
    
    public boolean isDragConnectable() {
        return false;
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

    /**
     * @see org.tigris.gef.presentation.Fig#setLayer(org.tigris.gef.base.Layer)
     */
    public void setLayer(Layer lay) {
        LOG.info("Setting " + this + " to layer " + lay);
        super.setLayer(lay);
    }

    /**
     * To redraw each element correctly when changing its location
     * with X and U additions.
     *
     * @param xInc the increment in the x direction
     * @param yInc the increment in the y direction
     */
    public void displace (int xInc, int yInc) {
        Vector figsVector;
        Rectangle rFig = getBounds();
        setLocation(rFig.x + xInc, rFig.y + yInc);
        figsVector = ((Vector) getEnclosedFigs().clone());
        if (!figsVector.isEmpty()) {
            for (int i = 0; i < figsVector.size(); i++) {
                ((FigNodeModelElement) figsVector.elementAt(i))
                            .displace(xInc, yInc);
            }
        }
    }


    /**
     * @param allowed true if the function RemoveFromDiagram is allowed
     */
    protected void allowRemoveFromDiagram(boolean allowed) {
        this.removeFromDiagram = allowed;
    }

    /**
     * Force painting the shadow.
     */
    public void forceRepaintShadow() {
        forceRepaint = true;
    }
    
    public void setDiElement(DiElement diElement) {
        this.diElement = diElement;
    }
    
    public DiElement getDiElement() {
        return diElement;
    }

    /**
     * @return Returns the popupAddOffset.
     */
    protected static int getPopupAddOffset() {
        return popupAddOffset;
    }
    
} /* end class FigNodeModelElement */


/**
 * Action to add a stero type to the model element represented by this Fig.
 * @author Bob Tarling
 */
class ActionAddStereotype extends AbstractAction {
    
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionAddStereotype.class);
    
    private Object modelElement;
    private Object stereotype;
    
    public ActionAddStereotype(Object modelElement, Object stereotype) {
        super("<<" + Model.getFacade().getName(stereotype) + ">>");
        this.modelElement = modelElement;
        this.stereotype = stereotype;
    }
    
    public void actionPerformed(ActionEvent ae) {
        Model.getCoreHelper().addStereotype(modelElement, stereotype);
    }
}