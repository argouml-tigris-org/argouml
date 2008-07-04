// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
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
import org.argouml.application.api.ArgoEventListener;
import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.application.events.ArgoDiagramAppearanceEventListener;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Highlightable;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.ui.ActionGoToCritique;
import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.DiElement;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.Clarifier;
import org.argouml.ui.ProjectActions;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.StereotypeUtility;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramAppearance;
import org.argouml.uml.diagram.PathContainer;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.util.IItemUID;
import org.argouml.util.ItemUID;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.MutableGraphSupport;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigImage;
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
        PathContainer,
        ArgoDiagramAppearanceEventListener,
        ArgoNotationEventListener,
        Highlightable,
        IItemUID,
        Clarifiable,
        ArgoFig {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(FigNodeModelElement.class);

    private DiElement diElement;

    private NotationProvider notationProviderName;
    private HashMap<String, Object> npArguments = new HashMap<String, Object>();
    
    /**
     * True if an instance is allowed to be
     * invisible. This is currently only set true by FigEdgePort.
     * TODO: FigEdgePort should be removed from the FigNodeModelElement
     * hierarchy and so the need for this removed.
     */
    protected boolean invisibleAllowed = false;

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

    private Fig bigPort;

    /**
     * use getNameFig() and setNameFig() to access the Figs.
     * Use getName() and setName() to just change the text.
     */
    private FigText nameFig;

    /**
     * use getter/setter
     * getStereotypeFig() and setStereoTypeFig() to access the Figs.
     * Use getStereotype() and setStereotype() to change stereotype
     * text.
     */
    private Fig stereotypeFig;

    /**
     * The <code>FigProfileIcon</code> being currently displayed
     */
    private FigProfileIcon stereotypeFigProfileIcon;

    /**
     * Contains the figs of the floating stereotypes when viewed in 
     * <code>SmallIcon</code> mode.
     */
    private List<Fig> floatingStereotypes = new ArrayList<Fig>();
    
    /**
     * The current stereotype view, defaults to "textual".
     * 
     * @see DiagramAppearance#STEREOTYPE_VIEW_TEXTUAL
     * @see DiagramAppearance#STEREOTYPE_VIEW_SMALL_ICON
     * @see DiagramAppearance#STEREOTYPE_VIEW_BIG_ICON
     */
    private int stereotypeView = DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL;   
    
    /**
     * The width of the profile icons when viewed at the small icon mode.
     * The icon width is resized to <code>ICON_WIDTH</code> and the height is 
     * set to a value that attempts to keep the original width/height 
     * proportion. 
     */
    private static final int ICON_WIDTH = 16;
    
    /**
     * When stereotypes are shown in <code>BigIcon</code> mode the 
     * <code>nameFig</code> is replaced by the one provided by the 
     * <code>FigProfileIcon</code> 
     * 
     * @see FigProfileIcon
     */
    private FigText originalNameFig;
    
    /**
     * EnclosedFigs are the Figs that are enclosed by this figure. Say that
     * it is a Package then these are the Classes, Interfaces, Packages etc
     * that are on this figure. This is not the same as the figures in the
     * FigGroup that this FigNodeModelElement "is", since these are the
     * figures that make up this high-level primitive figure.
     */
    private Vector<Fig> enclosedFigs = new Vector<Fig>();

    /**
     * The figure enclosing this figure.
     */
    private Fig encloser;

    private boolean readyToEdit = true;
    private boolean suppressCalcBounds;
    private static boolean showBoldName;

    private ItemUID itemUid;

    /**
     * Set the removeFromDiagram to false if this node may not
     * be removed from the diagram.
     */
    private boolean removeFromDiagram = true;

    /**
     * Flag that indicates if the full namespace path should be shown
     * in front of the name.
     */
    private boolean pathVisible;

    /**
     * If the contains text to be edited by the user.
     */
    private boolean editable = true;

    // TODO: A more strongly typed data structure could be used here.
    private Collection<Object[]> listeners = new ArrayList<Object[]>();

    /**
     * If this semaphore is true, then a Runnable to update the layout 
     * is already waiting to be executed. The semaphore is then used to 
     * guarantee that not more than one such (functionally identical) Runnable
     * is created and queued in the SwingUtilities.invokeLater() call.
     */
    private boolean layoutUpdatePending = false;

    /**
     * The main constructor. <p>
     * 
     * The owner nor the Layer (which has a 1..1 relation to the Diagram)
     * are set in this stage of the creation of the Fig.
     */
    protected FigNodeModelElement() {
        // this rectangle marks the whole modelelement figure; everything
        // is inside it:
        bigPort = new FigRect(10, 10, 0, 0, Color.cyan, Color.cyan);

        nameFig = new FigNameWithAbstractAndBold(10, 10, 90, 21, true);
        nameFig.setLineWidth(1);
        nameFig.setFilled(true);
        nameFig.setText(placeString());
        nameFig.setBotMargin(7); // make space for the clarifier
        nameFig.setRightMargin(4); // margin between text and border
        nameFig.setLeftMargin(4);

        stereotypeFig = new FigStereotypesCompartment(10, 10, 90, 15);

        readyToEdit = false;
        ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
        ArgoEventPump.addListener(
                ArgoEventTypes.ANY_DIAGRAM_APPEARANCE_EVENT, this);
        

        Project project = getProject();
        ProjectSettings ps = project.getProjectSettings();

        setShadowSize(ps.getDefaultShadowWidthValue());
        /* TODO: how to handle changes in shadowsize 
         * from the project properties? */
        
        stereotypeView = ps.getDefaultStereotypeViewValue();
    }

    /**
     * Construct a figure at a specific position for a given model element. <p>
     * 
     * The Layer (which has a 1..1 relation to the Diagram)
     * is not yet set in this stage of the creation of the Fig.
     * 
     * @param element ModelElement associated with figure
     * @param x horizontal location
     * @param y vertical location
     */
    protected FigNodeModelElement(Object element, int x, int y) {
        this();
        setOwner(element);
        nameFig.setText(placeString());
        readyToEdit = false;
        setLocation(x, y);
    }

    /**
     * This is the final call at creation time of the Fig, i.e. here
     * it is put on a Diagram.
     * 
     * @param lay the Layer (which has a 1..1 relation to the Diagram)
     * @see org.tigris.gef.presentation.Fig#setLayer(org.tigris.gef.base.Layer)
     */
    @Override
    public void setLayer(Layer lay) {
        super.setLayer(lay);
        determineDefaultPathVisible();
    }

    /*
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        ArgoEventPump.removeListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
        ArgoEventPump.removeListener(
                ArgoEventTypes.ANY_DIAGRAM_APPEARANCE_EVENT, this);
        super.finalize();
    }

    /**
     * Clone this figure. After the base clone method has been called determine
     * which child figs of the clone represent the name, stereotype and port.
     * <p>
     * 
     * @see java.lang.Object#clone()
     * @return the cloned figure
     */
    @Override
    public Object clone() {
        final FigNodeModelElement clone = (FigNodeModelElement) super.clone();
        
        final Iterator cloneIter = clone.getFigs().iterator();
        for (Object thisFig : getFigs()) {
            final Fig cloneFig = (Fig) cloneIter.next();
            if (thisFig == getBigPort()) {
                clone.setBigPort(cloneFig);
            }
            if (thisFig == nameFig) {
                clone.nameFig = (FigSingleLineText) thisFig;
            }
            if (thisFig == stereotypeFig) {
                clone.stereotypeFig = (Fig) thisFig;
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
                    // TODO: I18N
                    "new " + Model.getFacade().getUMLClassName(getOwner());
            }
            return placeString;
        }
        return "";
    }

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
    protected FigText getNameFig() {
        return nameFig;
    }
    
    /**
     * Get the Rectangle in which the model elements name is displayed
     * @return bounding box for name
     */
    public Rectangle getNameBounds() {
        return nameFig.getBounds();
    }
    
    /**
     * Set the Fig that displays the model element name.
     *
     * @param fig the name Fig
     */
    protected void setNameFig(FigText fig) {
        nameFig = fig;
        if (nameFig != null) {
            updateFont();
        }
    }

    /**
     * Get the name of the model element this Fig represents.
     *
     * @return the name of the model element
     */
    public String getName() {
        return nameFig.getText();
    }

    /**
     * Change the name of the model element this Fig represents.
     *
     * @param n the name of the model element
     */
    public void setName(String n) {
        nameFig.setText(n);
    }

    /**
     * This method shall return a Vector of one of these 4 types:
     * AbstractAction, JMenu, JMenuItem, JSeparator.
     */
    @Override
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        // Show ...
        ArgoJMenu show = buildShowPopUp();
        if (show.getMenuComponentCount() > 0) {
            popUpActions.add(show);
        }
        
        // popupAddOffset should be equal to the number of items added here:
        popUpActions.add(new JSeparator());
        popupAddOffset = 1;
        if (removeFromDiagram) {
            popUpActions.add(
                    ProjectActions.getInstance().getRemoveFromDiagramAction());
            popupAddOffset++;
        }
        popUpActions.add(new ActionDeleteModelElements());
        popupAddOffset++;

        /* Check if multiple items are selected: */
        if (TargetManager.getInstance().getTargets().size() == 1) {
            
            // TODO: Having Critics actions here introduces an unnecessary
            // dependency on the Critics subsystem.  Have it register its
            // desired actions using an extension mechanism - tfm
            ToDoList tdList = Designer.theDesigner().getToDoList();
            List<ToDoItem> items = tdList.elementListForOffender(getOwner());
            if (items != null && items.size() > 0) {
                // TODO: This creates a dependency on the Critics subsystem.
                // We need a generic way for modules (including our internal
                // subsystems) to request addition of actions to the popup
                // menu. - tfm 20080430
                ArgoJMenu critiques = new ArgoJMenu("menu.popup.critiques");
                ToDoItem itemUnderMouse = hitClarifier(me.getX(), me.getY());
                if (itemUnderMouse != null) {
                    critiques.add(new ActionGoToCritique(itemUnderMouse));
                    critiques.addSeparator();
                }
                for (ToDoItem item : items) {
                    if (item != itemUnderMouse) {
                        critiques.add(new ActionGoToCritique(item));
                    }
                }
                popUpActions.add(0, new JSeparator());
                popUpActions.add(0, critiques);
            }

            // Add stereotypes submenu
            final Action[] stereoActions =
                StereotypeUtility.getApplyStereotypeActions(getOwner());
            if (stereoActions != null) {
                popUpActions.add(0, new JSeparator());
                final ArgoJMenu stereotypes =
                    new ArgoJMenu("menu.popup.apply-stereotypes");
                for (Action action : stereoActions) {
                    stereotypes.addCheckItem(action);
                }
                popUpActions.add(0, stereotypes);
            }
            
            // add stereotype view submenu
            ArgoJMenu stereotypesView =
                new ArgoJMenu("menu.popup.stereotype-view");
            
            // TODO: There are cyclic dependencies between ActionStereotypeView*
            // and FigNodeModelElement.  Register these actions opaquely since
            // we don't what they are. - tfm
            stereotypesView.addRadioItem(new ActionStereotypeViewTextual(this));
            stereotypesView.addRadioItem(new ActionStereotypeViewBigIcon(this));
            stereotypesView.addRadioItem(new ActionStereotypeViewSmallIcon(this));
            
            popUpActions.add(0, stereotypesView);
        }

        return popUpActions;
    }

    protected ArgoJMenu buildShowPopUp() {
        ArgoJMenu showMenu = new ArgoJMenu("menu.popup.show");

        for (UndoableAction ua : ActionSetPath.getActions()) {
            showMenu.add(ua);
        }
        return showMenu;
    }

    /**
     * @return the pop-up menu item for Visibility
     */
    protected Object buildVisibilityPopUp() {
        ArgoJMenu visibilityMenu = new ArgoJMenu("menu.popup.visibility");

        visibilityMenu.addRadioItem(new ActionVisibilityPublic(getOwner()));
        visibilityMenu.addRadioItem(new ActionVisibilityPrivate(getOwner()));
        visibilityMenu.addRadioItem(new ActionVisibilityProtected(getOwner()));
        visibilityMenu.addRadioItem(new ActionVisibilityPackage(getOwner()));
        
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

    /*
     * @see org.tigris.gef.presentation.Fig#getEnclosingFig()
     */
    @Override
    public Fig getEnclosingFig() {
        return encloser;
    }

    /*
     * Updates the modelelement container if the fig is moved in or
     * out another fig. If this fig doesn't have an enclosing fig
     * anymore, the namespace of the diagram will be the owning
     * modelelement. If this fig is moved inside another
     * FigNodeModelElement the owner of that fignodemodelelement will
     * be the owning modelelement.
     * 
     * @see org.tigris.gef.presentation.FigNode#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    @Override
    public void setEnclosingFig(Fig newEncloser) {
        Fig oldEncloser = encloser;
        
	LayerPerspective layer = (LayerPerspective) getLayer();
	if (layer != null) {
            ArgoDiagram diagram = (ArgoDiagram) layer.getDiagram();
            diagram.encloserChanged(
                    this,
        	    (FigNode) oldEncloser,
        	    (FigNode) newEncloser);
	}
	
	super.setEnclosingFig(newEncloser);
	
	if (layer != null && newEncloser != oldEncloser) {
            Diagram diagram = layer.getDiagram();
            if (diagram instanceof ArgoDiagram) {
        	ArgoDiagram umlDiagram = (ArgoDiagram) diagram;
        	// Set the namespace of the enclosed model element to the
        	// namespace of the encloser.
                Object namespace = null;
                if (newEncloser == null) {
                    // The node's been placed on the diagram
                    umlDiagram.setModelElementNamespace(getOwner(), null);
                } else { 
                    // The node's been placed within some Fig
                    namespace = newEncloser.getOwner();
                    if (Model.getFacade().isANamespace(namespace)) {
                        umlDiagram.setModelElementNamespace(
                                getOwner(), namespace);
                    }
                }
            }

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
     * Handle the case where this fig is moved into a Component.
     * 
     * @param newEncloser the new encloser for this Fig
     */
    protected void moveIntoComponent(Fig newEncloser) {
        final Object component = newEncloser.getOwner();
        final Object owner = getOwner();

        assert Model.getFacade().isAComponent(component);
        assert Model.getFacade().isAUMLElement(owner);

        final Collection er1 = Model.getFacade().getElementResidences(owner);
        final Collection er2 = Model.getFacade().getResidentElements(component);
        boolean found = false;
        // Find all ElementResidences between the class and the component:
        final Collection<Object> common = new ArrayList<Object>(er1);
        common.retainAll(er2);
        for (Object elementResidence : common) {
            if (!found) {
                found = true;
                // There is already a correct ElementResidence
            } else {
                // There were 2 ElementResidences .. strange case.
                Model.getUmlFactory().delete(elementResidence);
            }
        }
        if (!found) {
            // There was no ElementResidence yet, so let's create one:
            Model.getCoreFactory().buildElementResidence(
                    owner, component);
        }
    }

    /**
     * Add a Fig that is enclosed by this figure.
     * 
     * @param fig The fig to be added
     */
    public void addEnclosedFig(Fig fig) {
        enclosedFigs.add(fig);
    }

    /**
     * Removes the given Fig from the list of enclosed Figs.
     * 
     * @param fig The Fig to be removed
     */
    public void removeEnclosedFig(Fig fig) {
        enclosedFigs.remove(fig);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getEnclosedFigs()
     */
    @Override
    public Vector<Fig> getEnclosedFigs() {
        return enclosedFigs;
    }

    /**
     * Update the order of this fig and the order of the
     * figs that are inside of this fig.
     *
     * @param figures in the new order
     * @deprecated in 0.25.5 This method is never used
     */
    @Deprecated
    public void elementOrdering(List<Fig> figures) {
        int size = figures.size();
        getLayer().bringToFront(this);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                Object o = figures.get(i);
                if (o instanceof FigNodeModelElement
                    && o != getEnclosingFig()) {
                    FigNodeModelElement fignode = (FigNodeModelElement) o;
                    List<Fig> enclosed = fignode.getEnclosedFigs();
                    fignode.elementOrdering(enclosed);
                }
            }
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionNodeClarifiers(this);
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
        // TODO: Generalize extension and remove critic specific stuff
        int iconX = getX();
        int iconY = getY() - 10;
        ToDoList tdList = Designer.theDesigner().getToDoList();
        List<ToDoItem> items = tdList.elementListForOffender(getOwner());
        for (ToDoItem item : items) {
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
        items = tdList.elementListForOffender(this);
        for (ToDoItem item : items) {
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
    protected ToDoItem hitClarifier(int x, int y) {
        // TODO: ToDoItem stuff should be made an opaque extension
        int iconX = getX();
        ToDoList tdList = Designer.theDesigner().getToDoList();
        List<ToDoItem> items = tdList.elementListForOffender(getOwner());
        for (ToDoItem item : items) {
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
        for (ToDoItem item : items) {
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier) icon).setFig(this);
                ((Clarifier) icon).setToDoItem(item);
                if (((Clarifier) icon).hit(x, y)) {
                    return item;
                }
            }
        }
        items = tdList.elementListForOffender(this);
        for (ToDoItem item : items) {
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
        for (ToDoItem item : items) {
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

    /*
     * @see org.tigris.gef.presentation.Fig#getTipString(java.awt.event.MouseEvent)
     */
    @Override
    public String getTipString(MouseEvent me) {
        // TODO: Generalize extension and remove critic specific code
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

    /*
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

    /*
     * @see org.argouml.kernel.DelayedVChangeListener#delayedVetoableChange(java.beans.PropertyChangeEvent)
     */
    public void delayedVetoableChange(PropertyChangeEvent pce) {
        LOG.debug("in delayedVetoableChange");
        // update any text, colors, fonts, etc.
        renderingChanged();
        endTrans();
    }

    /**
     * Determine new bounds. <p>
     * 
     * This algorithm makes the box grow 
     * (if the calculated minimum size grows), 
     * but then it can never shrink again 
     * (not even if the calculated minimum size is smaller).
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

    /*
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent pve) {
        final Object src = pve.getSource();
        final String pName = pve.getPropertyName();
        if (pve instanceof DeleteInstanceEvent && src == getOwner()) {
            removeFromDiagram();
            return;
        }
        
        // We are getting events we don't want. Filter them out.
        if (pve.getPropertyName().equals("supplierDependency")
                && Model.getFacade().isADependency(pve.getOldValue())) {
            // TODO: Can we instruct the model event pump not to send these in
            // the first place? See defect 5095.
            return;
        }
        
        // We handle and consume editing events
        if (pName.equals("editing")
                && Boolean.FALSE.equals(pve.getNewValue())) {
            try {
                //parse the text that was edited
                textEdited((FigText) src);
                // resize the FigNode to accomodate the new text
                final Rectangle bbox = getBounds();
                final Dimension minSize = getMinimumSize();
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
        if (Model.getFacade().isAUMLElement(src)) {
            final UmlChangeEvent event = (UmlChangeEvent) pve;
            /* If the source of the event is an UML object,
             * e.g. the owner of this Fig (but not always only the owner
             * is shown, e.g. for a class, also its attributes are shown),
             * then the UML model has been changed.
             */
            final Object owner = getOwner();
            if (owner == null) {
                // TODO: Should this not be an assert?
                return;
            }
            
            try {
                modelChanged(event);
            } catch (InvalidElementException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("event = " + event.getClass().getName());
                    LOG.debug("source = " + event.getSource());
                    LOG.debug("old = " + event.getOldValue());
                    LOG.debug("name = " + event.getPropertyName());
                    LOG.debug("modelChanged method accessed deleted element ",
                            e);
                }
            }
            
            if (event.getSource() == owner 
                    && "stereotype".equals(event.getPropertyName())) {
                stereotypeChanged(event);            
            }

            /*
             * If this semaphore is true, then a Runnable to update the layout 
             * is already waiting to be executed. The semaphore is then used to 
             * guarantee that not more than one such (functionally identical) 
             * Runnable is created and queued in the 
             * SwingUtilities.invokeLater() call.
             */
            if (!layoutUpdatePending) {
                layoutUpdatePending = true;
                Runnable doWorkRunnable = new Runnable() {
                    public void run() {
                        try {
                            layoutUpdatePending = false;
                            updateLayout(event);
                        } catch (InvalidElementException e) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("event = "
                                        + event.getClass().getName());
                                LOG.debug("source = " + event.getSource());
                                LOG.debug("old = " + event.getOldValue());
                                LOG.debug("name = " + event.getPropertyName());
                                LOG.debug("updateLayout method accessed "
                            		+ "deleted element ", e);
                            }
                        }
                    }  
                };
                SwingUtilities.invokeLater(doWorkRunnable);
            }
        }
    }

    /**
     * Called by propertyChanged when it detects that a stereotype
     * has been added or removed. On removal the FigNode removes its
     * listener to that stereotype. When a new stereotype is detected
     * we add a listener.
     * TODO: Bob says: In my opinion we shouldn't be doing this here.
     * FigStereotype should always be listening to change of its
     * owners name.
     * FigStereotypesCompartment should always be listening for add
     * or remove of Stereotypes to its owner.
     * Those classes will need to pass some event to the FigNode on
     * the AWT thread only if a change results in a change of size
     * that requires a redraw.
     * <p>NOTE: Runs at the Model (MDR) Thread </p>
     * @param event the UmlChangeEvent that caused the change
     */
    private void stereotypeChanged(final UmlChangeEvent event) {
        final Object owner = getOwner();
        assert owner != null;
        try {          
            if (event.getOldValue() != null) {
                removeElementListener(event.getOldValue());
            }
            if (event.getNewValue() != null) {
                addElementListener(event.getNewValue(), "name");
            }
        } catch (InvalidElementException e) {
            LOG.debug("stereotypeChanged method accessed deleted element ", e);
        }
    }

    /**
     * This method is called when the user doubleclicked on the text field,
     * and starts editing. Subclasses should overrule this field to e.g.
     * supply help to the user about the used format. <p>
     *
     * It is also possible to alter the text to be edited
     * already here, e.g. by adding the stereotype in front of the name,
     * by adding ["fullyHandleStereotypes", true] in the arguments 
     * HashMap of the NotationProvider.toString() function, 
     * but that seems not user-friendly. See issue 3838.
     *
     * @param ft the FigText that will be edited and contains the start-text
     */
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProviderName.getParsingHelp());
            ft.setText(notationProviderName.toString(getOwner(), npArguments));
        }
        if (ft instanceof CompartmentFigText) {
            final CompartmentFigText figText = (CompartmentFigText) ft;
            showHelp(figText.getNotationProvider().getParsingHelp());
            figText.setText(figText.getNotationProvider().toString(
                    figText.getOwner(), npArguments));
        }
    }

    /**
     * Utility function to localize the given string with help text, and show it
     * in the status bar of the ArgoUML window. This function is used in favour
     * of the inline call to enable later improvements; e.g. it would be
     * possible to show a help-balloon.
     * <p>
     * TODO: Work this out. One matter to possibly improve: show multiple lines.
     * 
     * @param s the given string to be localized and shown
     */
    protected void showHelp(String s) {
        ArgoEventPump.fireEvent(new ArgoHelpEvent(
                ArgoEventTypes.HELP_CHANGED, this,
                Translator.localize(s)));
    }

    /**
     * This method is called after the user finishes editing a text
     * field that is in the FigNodeModelElement.  Determine which
     * field and update the model.  This class handles the name,
     * the stereotype, and any CompartmentFigTexts.
     * Subclasses should override to handle other text elements.
     *
     * @param ft the FigText that has been edited and contains the new text
     * @throws PropertyVetoException thrown when new text represents
     * an unacceptable value
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        if (ft == nameFig) {
            // TODO: Can we delegate this to a specialist FigName class?
            if (getOwner() == null) {
                return;
            }
            notationProviderName.parse(getOwner(), ft.getText());
            ft.setText(notationProviderName.toString(getOwner(), npArguments));
        }
        if (ft instanceof CompartmentFigText) {
            final CompartmentFigText figText = (CompartmentFigText) ft;
            figText.getNotationProvider().parse(ft.getOwner(), ft.getText());
            ft.setText(figText.getNotationProvider().toString(
                    ft.getOwner(), npArguments));
        }
    }

    ////////////////////////////////////////////////////////////////
    // event handlers - MouseListener implementation

    /*
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
                    createContainedModelElement((FigGroup) f, me);
                }
            }
        }
    }

    /*
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) {
        if (ke.isConsumed() || getOwner() == null) {
            return;
        }
        nameFig.keyPressed(ke);
    }

    /*
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent ke) {
        if (ke.isConsumed() || getOwner() == null) {
            return;
        }
        nameFig.keyReleased(ke);
    }

    /*
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent ke) {
        if (!editable) {
            return;
        }
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
        nameFig.keyTyped(ke);
    }

    /**
     * This is a template method called by the ArgoUML framework as the result
     * of a change to a model element. Do not call this method directly
     * yourself.
     * <p>Override this in any subclasses in order to change what model
     * elements the FigNode is listening to as a result of change to the model.</p>
     * <p>This method is guaranteed by the framework to be running on the same
     * thread as the model subsystem.</p>
     * TODO: Lets refactor this at some time to take UmlChangeEvent argument
     *
     * @param event the UmlChangeEvent that caused the change
     */
    protected void modelChanged(PropertyChangeEvent event) {
        if (event instanceof AssociationChangeEvent 
                || event instanceof AttributeChangeEvent) {
            if (notationProviderName != null) {
                notationProviderName.updateListener(this, getOwner(), event);
            }
            updateListeners(getOwner(), getOwner());
        }
    }    
    
    ////////////////////////////////////////////////////////////////
    // internal methods
    
    /**
     * This is a template method called by the ArgoUML framework as the result
     * of a change to a model element. Do not call this method directly
     * yourself.
     * <p>Override this in any subclasses in order to restructure the FigNode
     * due to change of any model element that this FigNode is listening to.</p>
     * <p>This method automatically updates the stereotype rendering.</p>
     * <p>The default behavior is to update the name and stereotype text.</p>
     * <p>For e.g. a Package, if the visibility is changed 
     * via the properties panel, then
     * the display of it on the diagram has to follow the change.
     * This is not handled here, but by the notationProviderName.</p>
     * <p>This method is guaranteed by the framework to be running on the 
     * Swing/AWT thread.</p>
     *
     * @param event the UmlChangeEvent that caused the change
     */
    protected void updateLayout(UmlChangeEvent event) {
        assert event != null;
        final Object owner = getOwner();
        assert owner != null;
        if (owner == null) {
            return;
        }
        boolean needDamage = false;
        if (event instanceof AssociationChangeEvent 
                || event instanceof AttributeChangeEvent) {
            if (notationProviderName != null) {
                updateNameText();
            }
            needDamage = true;
        }
        if (event.getSource() == owner
                && "stereotype".equals(event.getPropertyName())) {
            updateStereotypeText();
            updateStereotypeIcon();
            needDamage = true;
        }
        if (needDamage) {
            damage();
        }
    }

    /**
     * Create a new model element contained in the fig owner.
     * Used by subclasses to, for example, create an attribute
     * within a class.
     *
     * @param fg The fig group to which this applies
     * @param me The input event that triggered us. In the current
     *            implementation a mouse double click.
     */
    protected void createContainedModelElement(FigGroup fg, InputEvent me) {
        // must be overridden to make sense
        // (I didn't want to make it abstract because it might not be required)
    }

    /**
     * @param o the given object
     * @return true if one of my figs has the given object as owner
     */
    protected boolean isPartlyOwner(Object o) {
        if (o == null || o == getOwner()) {
            return true;
        }
        for (Object fig : getFigs()) {
            if (isPartlyOwner((Fig) fig, o)) {
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
            for (Object fig2 : ((FigGroup) fig).getFigs()) {
                if (isPartlyOwner((Fig) fig2, o)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#deleteFromModel()
     */
    @Override
    public void deleteFromModel() {
        Object own = getOwner();
        if (own != null) {
            getProject().moveToTrash(own);
        }
        for (Object fig : getFigs()) {
            ((Fig) fig).deleteFromModel();
        }
        super.deleteFromModel();
    }

    /**
     * This method should only be called once for any one Fig instance that
     * represents a model element (ie not for a FigEdgeNote).
     * It is called either by the constructor that takes an model element as an
     * argument or it is called by PGMLStackParser after it has created the Fig
     * by use of the empty constructor.
     * The assigned model element (owner) must not change during the lifetime
     * of the Fig.
     * <p>
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
        if (!Model.getFacade().isAUMLElement(owner)) {
            throw new IllegalArgumentException(
                    "The owner must be a model element - got a "
                    + owner.getClass().getName());
        }
        super.setOwner(owner);
        initNotationProviders(owner);
        readyToEdit = true;
        renderingChanged();
//        updateBounds(); // included in the previous line.
        /* This next line presumes that the 1st fig with this owner 
         * is the previous port - and consequently nullifies the owner 
         * of this 1st fig. */
        bindPort(owner, bigPort);
        nameFig.setOwner(owner); // for setting abstract
        updateListeners(null, owner);
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
        if (Model.getFacade().isAUMLElement(own)) {
            notationProviderName =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        getNotationProviderType(), own, this);
            npArguments.put("pathVisible", Boolean.valueOf(isPathVisible()));
            Project p = getProject();
            if (p != null) {
                npArguments.put("rightGuillemot", 
                        p.getProjectSettings().getRightGuillemot());
                npArguments.put("leftGuillemot", 
                        p.getProjectSettings().getLeftGuillemot());
            }
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
     * Updates the text of the sterotype FigText. Override in subclasses to get
     * wanted behaviour.
     */
    protected void updateStereotypeText() {
        if (getOwner() == null) {
            LOG.warn("Owner of [" + this.toString() + "/" + this.getClass()
                    + "] is null.");
            return;
        }

        Object modelElement = getOwner();
        stereotypeFig.setOwner(modelElement);
    }

    /**
     * Updates the text of the name FigText.
     */
    protected void updateNameText() {
        if (readyToEdit) {
            if (getOwner() == null) {
                return;
            }
            if (notationProviderName != null) {
                nameFig.setText(notationProviderName.toString(
                        getOwner(), npArguments));
                Project p = getProject();
                if (p != null) {
                    updateFont();
                }
                updateBounds();
            }
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.PathContainer#isPathVisible()
     */
    public boolean isPathVisible() {
        return pathVisible;
    }

    /*
     * @see org.argouml.uml.diagram.ui.PathContainer#setPathVisible(boolean)
     */
    public void setPathVisible(boolean visible) {
        if (pathVisible == visible) {
            return;
        }
        MutableGraphSupport.enableSaveAction();
        // TODO: Use this event mechanism to update 
        // the checkmark on the Presentation Tab:
        firePropChange("pathVisible", !visible, visible);
        pathVisible = visible;
        if (notationProviderName != null) {
            npArguments.put("pathVisible", Boolean.valueOf(visible));
        }
        if (readyToEdit) {
            renderingChanged();
            damage();
        }
    }
    
    /**
     * At creation time of the Fig, we determine 
     * if the path should be visible by default. <p>
     * 
     * The path is a concatenation of the names of all packages by which 
     * this modelelement is contained, 
     * seperated by "::" (for UML at least). <p>
     * 
     * If the default namespace of the diagram corresponds 
     * to the namespace of the modelelement, 
     * then we do NOT show the path. Otherwise, we do. <p>
     * 
     * RRose uses the same heuristic algorithm, 
     * but shows "(from &lt;path&gt;)" below the name, 
     * while we follow the UML syntax.
     */
    protected void determineDefaultPathVisible() {
        Object modelElement = getOwner();
        LayerPerspective layer = (LayerPerspective) getLayer();
        if ((layer != null) 
                && Model.getFacade().isAModelElement(modelElement)) {
            ArgoDiagram diagram = (ArgoDiagram) layer.getDiagram();
            Object elementNs = Model.getFacade().getNamespace(modelElement);
            Object diagramNs = diagram.getNamespace();
            if (elementNs != null) {
                boolean visible = elementNs != diagramNs; 
                npArguments.put("pathVisible", Boolean.valueOf(visible));
                pathVisible = visible;
                renderingChanged();
                damage();
            }
            // it is done
        }
        // either layer or owner was null
    }

    /*
     * @see org.tigris.gef.presentation.Fig#classNameAndBounds()
     */
    public String classNameAndBounds() {
        return getClass().getName()
            + "[" + getX() + ", " + getY() + ", "
            + getWidth() + ", " + getHeight() + "]"
            + "pathVisible=" + isPathVisible() + ";"
            + "stereotypeView=" + getStereotypeView() + ";";
    }

    /**
     * Implementations of this method should register/unregister the fig for all
     * (model)events. For FigNodeModelElement only the fig itself is registered
     * as listening to (all) events fired by the owner itself. 
     * But for, for example,
     * FigClass the fig must also register for events fired by the operations
     * and attributes of the owner. <p>
     * 
     * An explanation of the original 
     * purpose of this method is given in issue 1321.<p>
     * 
     * This function is used by the modelChanged()
     * function.<p>
     * 
     * In certain cases, it is imperative that indeed ALL listeners are 
     * updated, since they are ALL removed 
     * by a call to removeElementListener. <p>
     * 
     *  IF this method is called with both the oldOwner and the 
     *  newOwner equal and not null, 
     *  AND we listen only to the owner itself,
     *  THEN we can safely ignore the call, but 
     *  ELSE we need to update the listeners of the related elements, 
     *  since the related elements may have been replaced. 
     * 
     * @param newOwner null, or the owner of this. 
     *          The former means that all listeners have to be removed. 
     * @param oldOwner null, or the previous owner
     *          The former means that all listeners have to be set.
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner == newOwner) {
            return;
        }
        if (oldOwner != null) {
            removeElementListener(oldOwner);
        }
        if (newOwner != null) {
            addElementListener(newOwner);
        }

    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationChanged(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationChanged(ArgoNotationEvent event) {
        if (getOwner() == null) return;
        initNotationProviders(getOwner());
        try {
            renderingChanged();
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationAdded(ArgoNotationEvent event) {
        // Default is to do nothing
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationRemoved(ArgoNotationEvent event) {
        // Default is to do nothing
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderAdded(ArgoNotationEvent event) {
        // Default is to do nothing
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderRemoved(ArgoNotationEvent event) {
        // Default is to do nothing
    }

    /**
     * Rerenders the entire fig.
     * <p>
     * This is may be an expensive operation for subclasses which are complex,
     * so should be used sparingly. This functionality was originally the
     * functionality of modelChanged but modelChanged takes the event now into
     * account. <p>
     * TODO: Does this have to be public?
     */
    public void renderingChanged() {
        updateNameText();
        updateStereotypeText();
        updateStereotypeIcon();        
        updateBounds();
        damage();
    }

    protected void updateStereotypeIcon() {
	if (getOwner() == null) {
	    LOG.warn("Owner of [" + this.toString() + "/" + this.getClass()
		    + "] is null.");
	    LOG.warn("I return...");
	    return;
	}

	if (stereotypeFigProfileIcon != null) {
	    for (Object fig : getFigs()) {
		((Fig) fig).setVisible(fig != stereotypeFigProfileIcon);
	    }

	    this.removeFig(stereotypeFigProfileIcon);
	    stereotypeFigProfileIcon = null;
	}
	
	if (originalNameFig != null) {
	    this.setNameFig(originalNameFig);
	    originalNameFig = null;
	}
	
	for (Fig icon : floatingStereotypes) {
            this.removeFig(icon);
        }
        floatingStereotypes.clear();
	
	
	int practicalView = getPracticalView();
	Object modelElement = getOwner();
	Collection stereos = Model.getFacade().getStereotypes(modelElement);
	 
	Fig stereoFig = getStereotypeFig();
        if (stereoFig instanceof FigStereotypesCompartment) {
            boolean hiding = 
                practicalView == DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON;
            ((FigStereotypesCompartment) stereoFig)
                    .setHidingStereotypesWithIcon(hiding);
        }

        if (practicalView == DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON) {

            Image replaceIcon = null;

            if (stereos.size() == 1) {
                Object stereo = stereos.iterator().next();
                // TODO: Should we not use getProject here?
                replaceIcon = ProjectManager.getManager().getCurrentProject()
                        .getProfileConfiguration().getFigNodeStrategy()
                        .getIconForStereotype(stereo);
            }

            if (replaceIcon != null) {
                stereotypeFigProfileIcon = new FigProfileIcon(replaceIcon,
                        getName());
                stereotypeFigProfileIcon.setOwner(getOwner());

                stereotypeFigProfileIcon.setLocation(getBigPort()
                        .getLocation());
                addFig(stereotypeFigProfileIcon);

                originalNameFig = this.getNameFig();
                final FigText labelFig =
                    stereotypeFigProfileIcon.getLabelFig();
                setNameFig(labelFig);

                labelFig.addPropertyChangeListener(this);

                getBigPort().
                setBounds(stereotypeFigProfileIcon.getBounds());

                for (Object fig : getFigs()) {
                    ((Fig) fig).setVisible(fig == stereotypeFigProfileIcon);
                }

            }
	} else if (practicalView
	        == DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON) {
            int i = this.getX() + this.getWidth() - ICON_WIDTH - 2;

            for (Object stereo : stereos) {
                // TODO: Should we not use getProject here?
                Image icon = ProjectManager.getManager().getCurrentProject()
                        .getProfileConfiguration().getFigNodeStrategy()
                        .getIconForStereotype(stereo);
                if (icon != null) {
                    FigImage fimg = new FigImage(i,
                            this.getBigPort().getY() + 2, icon);
                    fimg.setSize(ICON_WIDTH,
                            (icon.getHeight(null) * ICON_WIDTH)
                                    / icon.getWidth(null));

                    addFig(fimg);
                    floatingStereotypes.add(fimg);

                    i -= ICON_WIDTH - 2;
                }
            }

            updateSmallIcons(this.getWidth());
        }

        // TODO: This is a redundant invocation
	updateStereotypeText();
	
        damage();
	calcBounds();
	updateEdges();
	updateBounds();
	redraw();
    }

    /*
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

    /*
     * Necessary since GEF contains some errors regarding the hit subject.
     * 
     * @see org.tigris.gef.presentation.Fig#hit(Rectangle)
     */
    @Override
    public boolean hit(Rectangle r) {
        int cornersHit = countCornersContained(r.x, r.y, r.width, r.height);
        if (_filled) {
            return cornersHit > 0 || intersects(r);
        }
        return (cornersHit > 0 && cornersHit < 4) || intersects(r);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#removeFromDiagram()
     */
    @Override
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
     * e.g. FigClassAssociationClass uses this to delegate the remove to
     * its attached FigAssociationClass.
     * @return the fig that handles the remove request
     */
    protected Fig getRemoveDelegate() {
        return this;
    }
    
    /**
     * If you override this method, make sure to remove all listeners:
     * If you don't, objects in a deleted project will still receive events.
     */
    protected void removeFromDiagramImpl() {
        if (notationProviderName != null) { //This test needed for a FigPool
            notationProviderName.cleanListener(this, getOwner());
        }
        ArgoEventPump.removeListener(this);
        removeAllElementListeners();
        setShadowSize(0);
        super.removeFromDiagram();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#postLoad()
     */
    @Override
    public void postLoad() {
        ArgoEventPump.removeListener(this);
        ArgoEventPump.addListener(this);
        for (Object fig : getFigs()) {
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
     * Get the Fig containing the stereotype.
     * <p>
     * TODO: Should return FigStereotypesCompartment or at the very least
     * a FigGroup
     *
     * @return the stereotype Fig
     */
    protected Fig getStereotypeFig() {
        return stereotypeFig;
    }

    /**
     * @deprecated by MVW in V0.25.4. Use ProjectSettings instead.
     * @return the diagram font
     */
    @Deprecated
    public Font getLabelFont() {
        return getProject().getProjectSettings().getFontPlain();
    }

    /**
     * @deprecated by MVW in V0.25.4. Use ProjectSettings instead.
     * @return the italic diagram font
     */
    @Deprecated
    public Font getItalicLabelFont() {
        return getProject().getProjectSettings().getFontItalic();
    }

    /**
     * @param bp the bigPort, which is the port where edges connect to this node
     */
    protected void setBigPort(Fig bp) {
        this.bigPort = bp;
    }

    /**
     * @return the fig which is the port where edges connect to this node
     */
    public Fig getBigPort() {
        return bigPort;
    }

    /**
     * @return Returns the checkSize.
     */
    protected boolean isCheckSize() {
        return checkSize;
    }

    /*
     * @see org.tigris.gef.presentation.FigNode#isDragConnectable()
     */
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
     * TODO: Move this in FigGroup (in GEF).
     * 
     * @param scb The suppressCalcBounds to set.
     */
    protected void setSuppressCalcBounds(boolean scb) {
        this.suppressCalcBounds = scb;
    }
    
    /**
     * Set visibility of figure. If the field {@link #invisibleAllowed} is not
     * <code>true</code> and this method is passed a parameter of
     * <code>false</code> it will throw an IllegalArgumentException.
     * 
     * @param visible
     *            new visibility - <code>true</code> = visible.
     * 
     * @see org.tigris.gef.presentation.Fig#setVisible(boolean)
     */
    public void setVisible(boolean visible) {
        if (!visible && !invisibleAllowed) {
            throw new IllegalArgumentException(
                    "Visibility of a FigNode should never be false");
        }
    }

    /**
     * To redraw each element correctly when changing its location
     * with X and Y additions. Also manages relocation of enclosed
     * Figs.
     *
     * @param xInc the increment in the x direction
     * @param yInc the increment in the y direction
     */
    public void displace (int xInc, int yInc) {
        List<Fig> figsVector;
        Rectangle rFig = getBounds();
        setLocation(rFig.x + xInc, rFig.y + yInc);
        figsVector = ((List<Fig>) getEnclosedFigs().clone());
        if (!figsVector.isEmpty()) {
            for (int i = 0; i < figsVector.size(); i++) {
                ((FigNodeModelElement) figsVector.get(i))
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

    public void setDiElement(DiElement element) {
        this.diElement = element;
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

    /**
     * Determine if this node can be edited.
     * @return editable state
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * By default a node is directly editable by simply selecting
     * that node and starting to type.
     * Should a subclass of FigNodeModelElement not desire this behaviour
     * then it should call setEditable(false) in its constructor.
     * 
     * @param canEdit new state, false = editing disabled.
     */
    protected void setEditable(boolean canEdit) {
        this.editable = canEdit;
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
     * Add a listener for a given property name and remember the registration.
     * 
     * @param element
     *            element to listen for changes on
     * @param property
     *            name of property to listen for changes of
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener,
     *      Object, String)
     */
    protected void addElementListener(Object element, String property) {
        listeners.add(new Object[] {element, property});
        Model.getPump().addModelEventListener(this, element, property);
    }

    /**
     * Add a listener for an array of property names and remember the
     * registration.
     * 
     * @param element
     *            element to listen for changes on
     * @param property
     *            array of property names (Strings) to listen for changes of
     * @see org.argouml.model.ModelEventPump#addModelEventListener(PropertyChangeListener,
     *      Object, String)
     */
    protected void addElementListener(Object element, String[] property) {
        listeners.add(new Object[] {element, property});
        Model.getPump().addModelEventListener(this, element, property);
    }
    
    /**
     * Remove an element listener and remember the registration.
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
        for (Object[] listener : listeners) {
            Object property = listener[1];
            if (property == null) {
                Model.getPump().removeModelEventListener(this, listener[0]);
            } else if (property instanceof String[]) {
                Model.getPump().removeModelEventListener(this, listener[0],
                        (String[]) property);
            } else if (property instanceof String) {
                Model.getPump().removeModelEventListener(this, listener[0],
                        (String) property);
            } else {
                throw new RuntimeException(
                        "Internal error in removeAllElementListeners");
            }
        }
        listeners.clear();
    }

    protected HashMap<String, Object> getNotationArguments() {
        return npArguments;
    }

    protected void putNotationArgument(String key, Object value) {
        if (notationProviderName != null) {
            npArguments.put(key, value);
        }
    }

    /*
     * This optional method is not implemented.  It will throw an
     * {@link UnsupportedOperationException} if used.  Figs are 
     * added to a GraphModel which is, in turn, owned by a project.<p>
     * 
     * This method is identical to the one in ArgoFigGroup.
     */
    public void setProject(Project project) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * This method is identical to the one in ArgoFigGroup.
     * 
     * @return the project
     * @see org.argouml.uml.diagram.ui.ArgoFig#getProject()
     */
    public Project getProject() {
        return ArgoFigUtil.getProject(this);
    }
    
    /**
     * Determine if this Fig is the sole selected target in
     * the TargetManager
     * @return true if this is the sole target.
     */
    protected boolean isSingleTarget() {
	return TargetManager.getInstance().getSingleModelTarget()
		== getOwner();
    }

    
    /**
     * @return current stereotype view
     */
    public int getStereotypeView() {
        return stereotypeView;
    }

    /**
     * Return a stereotype view which is most practical for the current
     * conditions. If the current mode is set to <code>BigIcon</code> mode and
     * the model element has zero or more than one stereotype the practical view
     * should be the textual view.
     * 
     * @return current practical stereotype view
     */
    private int getPracticalView() {
        // TODO assert modelElement != null???
	int practicalView = getStereotypeView();
        Object modelElement = getOwner();

        if (modelElement != null) {
            Collection stereos = Model.getFacade().getStereotypes(modelElement);

            if (getStereotypeView() 
                    == DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON
                    && (stereos == null 
                            || stereos.size() != 1 
                            ||  (stereos.size() == 1 
                                    && getProject().getProfileConfiguration()
                                    .getFigNodeStrategy().getIconForStereotype(
                                            stereos.iterator().next()) 
                                            == null))) {
                practicalView = DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL;
            }
        }
        return practicalView;
    }
    
    /**
     * Sets the stereotype view.
     * 
     * @param s the stereotype view to be set
     */
    public void setStereotypeView(int s) {
        this.stereotypeView = s;
        try {
            renderingChanged();
        } catch (Exception e) {
            // TODO: Why is this ignored?
        }
    }
    
    /**
     * Sets the bounds of this classifier. Takes the stereotype view into 
     * consideration.<br>
     * 
     * Do not override this method, override 
     * {@link #setStandardBounds(int, int, int, int)} instead.
     * 
     * {@inheritDoc}
     */
    protected void setBoundsImpl(final int x, final int y, final int w,
            final int h) {

        if (getPracticalView() == DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON) {
            if (stereotypeFigProfileIcon != null) {
                stereotypeFigProfileIcon.setBounds(stereotypeFigProfileIcon
                        .getX(), stereotypeFigProfileIcon.getY(), w, h);
                // FigClass calls setBoundsImpl before we set
                // the stereotypeFigProfileIcon
            }
        } else {
	    setStandardBounds(x, y, w, h);
	    if (getStereotypeView()
	            == DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON) {
	        updateSmallIcons(w);
	    }
	}
    }

    private void updateSmallIcons(int wid) {
        int i = this.getX() + wid - ICON_WIDTH - 2;

        for (Fig ficon : floatingStereotypes) {
            ficon.setLocation(i, this.getBigPort().getY() + 2);
            i -= ICON_WIDTH - 2;
        }

        getNameFig().setRightMargin(
                floatingStereotypes.size() * (ICON_WIDTH + 5));
    }
    
    /**
     * Replaces {@link #setBoundsImpl(int, int, int, int)}.
     * 
     * @param x Desired X coordinate of upper left corner
     * @param y Desired Y coordinate of upper left corner
     * @param w Desired width of the FigClass
     * @param h Desired height of the FigClass
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setStandardBounds(final int x, final int y,
            final int w, final int h) {

    }
    
    /**
     * Handles diagram font changing.
     * @param e the event or null
     * @see org.argouml.application.events.ArgoDiagramAppearanceEventListener#diagramFontChanged(org.argouml.application.events.ArgoDiagramAppearanceEvent)
     */
    public void diagramFontChanged(ArgoDiagramAppearanceEvent e) {
        updateFont();
        updateBounds();
        damage();
    }

    /**
     * This function should, for all FigTexts, 
     * recalculate the font-style (plain, bold, italic, bold/italic),
     * and apply it by calling FigText.setFont(). <p>
     * 
     * If the "deepUpdateFont" function does not 
     * work for a subclass, then override this method.
     */
    protected void updateFont() {
        int style = getNameFigFontStyle();
        Font f = getProject().getProjectSettings().getFont(style);
        nameFig.setFont(f);
        deepUpdateFont(this);
    }

    /**
     * Determines the font style based on the UML model. 
     * Overrule this in Figs that have to show bold or italic based on the 
     * UML model they represent. 
     * E.g. abstract classes show their name in italic.
     * 
     * @return the font style for the nameFig.
     */
    protected int getNameFigFontStyle() {
    	showBoldName = false;
    	Project p = getProject();

        /**
         *  When and why p could be NULL?
         *  See issue 4911.
         */
        if (p != null) {
            ProjectSettings ps = p.getProjectSettings();
            showBoldName = ps.getShowBoldNamesValue();
        }
        
        return showBoldName ? Font.BOLD : Font.PLAIN;
    }

    /**
     * Changes the font for all Figs contained in the given FigGroup. <p>
     * 
     *  TODO: In fact, there is a design error in this method:
     *  E.g. for a class, if the name is Italic since the class is abstract,
     *  then the classes features should be in Plain font.
     *  This problem can be fixed by implementing 
     *  the updateFont() method in all subclasses.
     *  
     * @param fg the FigGroup to change the font of.
     */
    private void deepUpdateFont(FigGroup fg) {
        boolean changed = false;
        List<Fig> figs = fg.getFigs();
        for (Fig f : figs) {
            if (f instanceof ArgoFigText) {
                ((ArgoFigText) f).diagramFontChanged(null);
                changed = true;
            }
            if (f instanceof FigGroup) {
                deepUpdateFont((FigGroup) f);
            }
        }
        if (changed) {
            fg.calcBounds();
        }
    }
}
