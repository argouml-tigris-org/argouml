// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: FigNodeModelElement.java
// Classes: FigNodeModelElement
// Original Author: abonner

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
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
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.SwingUtilities;

import org.apache.log4j.Category;
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
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.ui.ActionGoToCritique;
import org.argouml.ui.Clarifier;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.UUIDManager;
import org.argouml.uml.generator.ParserDisplay;
import org.argouml.uml.ui.ActionDeleteFromDiagram;
import org.argouml.uml.ui.ActionProperties;
import org.argouml.util.Trash;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;

/** Abstract class to display diagram icons for UML ModelElements that
 *  look like nodes and that have editiable names and can be
 *  resized. */

public abstract class FigNodeModelElement
    extends FigNode
    implements
        VetoableChangeListener,
        DelayedVChangeListener,
        MouseListener,
        KeyListener,
        PropertyChangeListener,
        MElementListener,
        NotationContext,
        ArgoNotationEventListener {

    protected static Category cat =
        Category.getInstance(FigNodeModelElement.class);
    ////////////////////////////////////////////////////////////////
    // constants

    public static Font LABEL_FONT;
    public static Font ITALIC_LABEL_FONT;
    public final int MARGIN = 2;

    protected static final int ROWHEIGHT = 17;
    // min. 17, used to calculate y pos of FigText items in a compartment
    protected static final int STEREOHEIGHT = 18;
    protected boolean checkSize = true;
    // Needed for loading. Warning: if false, a too small size might look bad!

    static {
        LABEL_FONT =
            new javax.swing.plaf.metal.DefaultMetalTheme().getSubTextFont();
        ITALIC_LABEL_FONT =
            new Font(LABEL_FONT.getFamily(), Font.ITALIC, LABEL_FONT.getSize());
    }

    ////////////////////////////////////////////////////////////////
    // instance variables

    protected FigRect _bigPort;
    public FigText _name; // Scope problem in jdk1.2
    public FigText _stereo; // Scope problem in jdk1.2
    protected Vector _enclosedFigs = new Vector();
    protected Fig _encloser = null;
    protected boolean _readyToEdit = true;
    protected boolean suppressCalcBounds = false;
    public int _shadowSize =
        Configuration.getInteger(Notation.KEY_DEFAULT_SHADOW_WIDTH, 1);
    private ItemUID _id;
    
    /**
     * A set of object arrays consisting of a sender of events and the event
     * types this object is interested in. The eventSenders are a cache to 
     * improve performance when this fig is disabled/enabled as interested listener
     * to the events maintained in the _eventSenders set.
     */
    private Set _eventSenders = new HashSet();

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigNodeModelElement() {
        // this rectangle marks the whole interface figure; everything is inside it:
        _bigPort = new FigRect(10, 10, 0, 0, Color.cyan, Color.cyan) {
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.black);
                if (_shadowSize > 0) {
                    g.fillRect(
                        _x + _shadowSize,
                        _y + _h,
                        _w - _shadowSize,
                        _shadowSize);
                    g.fillRect(_x + _w, _y + _shadowSize, _shadowSize, _h);
                }
            }
        };

        _name = new FigText(10, 10, 90, 21, true);
        _name.setFont(LABEL_FONT);
        _name.setTextColor(Color.black);
        // _name.setFilled(false);
        _name.setMultiLine(false);
        _name.setAllowsTab(false);
        _name.setText(placeString());

        _stereo = new FigText(10, 10, 90, 15, true);
        _stereo.setFont(LABEL_FONT);
        _stereo.setTextColor(Color.black);
        _stereo.setFilled(false);
        _stereo.setLineWidth(0);
        //_stereo.setLineColor(Color.black);
        _stereo.setEditable(false);

        _readyToEdit = false;
        ArgoEventPump.addListener(ArgoEvent.ANY_NOTATION_EVENT, this);
    }

    /** Partially construct a new FigNode.  This method creates the
     *  _name element that holds the name of the model element and adds
     *  itself as a listener. */
    public FigNodeModelElement(GraphModel gm, Object node) {
        this();
        setOwner(node);
        _name.setText(placeString());
        _readyToEdit = false;
        ArgoEventPump.addListener(ArgoEvent.ANY_NOTATION_EVENT, this);
    }

    public void finalize() {
        ArgoEventPump.removeListener(ArgoEvent.ANY_NOTATION_EVENT, this);
    }

    /** Reply text to be shown while placing node in diagram */
    public String placeString() {
        if (getOwner() instanceof MModelElement) {
            MModelElement me = (MModelElement)getOwner();
            String placeString = me.getName();
            if (placeString == null)
                placeString = "new " + me.getUMLClassName();
            return placeString;
        }
        return "";
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void setItemUID(ItemUID id) {
        _id = id;
    }

    public ItemUID getItemUID() {
        return _id;
    }

    public FigText getNameFig() {
        return _name;
    }

    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        ToDoList list = Designer.TheDesigner.getToDoList();
        Vector items = (Vector)list.elementsForOffender(getOwner()).clone();
        if (items != null && items.size() > 0) {
            JMenu critiques = new JMenu("Critiques");
            ToDoItem itemUnderMouse = hitClarifier(me.getX(), me.getY());
            if (itemUnderMouse != null)
                critiques.add(new ActionGoToCritique(itemUnderMouse));
            critiques.addSeparator();
            int size = items.size();
            for (int i = 0; i < size; i++) {
                ToDoItem item = (ToDoItem)items.elementAt(i);
                if (item == itemUnderMouse)
                    continue;
                critiques.add(new ActionGoToCritique(item));
            }
            popUpActions.insertElementAt(critiques, 0);
        }
        popUpActions.addElement(ActionProperties.SINGLETON);
        popUpActions.addElement(ActionDeleteFromDiagram.SINGLETON);
        return popUpActions;
    }

    ////////////////////////////////////////////////////////////////
    // Fig API

    public Fig getEnclosingFig() {
        return _encloser;
    }

    public void setEnclosingFig(Fig f) {
        super.setEnclosingFig(f);
        if (!(getOwner() instanceof MModelElement))
            return;
        MModelElement elem = (MModelElement)getOwner();
        if (f == null
            && getEnclosingFig() != null) { // move from a package to the diagram
            // TODO: we should print "from blahblah package
            // TODO: we should take into account 'playing around with the classes' on some diagram
            return;
        }
        if (f == null
            && getEnclosingFig() == null) { // placement on the diagram
            return;
        }
        Object fOwner = f.getOwner();
        if (fOwner instanceof MNamespace) { // placed it on a package
            if (CoreHelper
                .getHelper()
                .isValidNamespace(elem, (MNamespace)fOwner))
                 ((MNamespace)fOwner).addOwnedElement(elem);
            return;
        }
        _encloser = f;

    }

    public Vector getEnclosedFigs() {
        return _enclosedFigs;
    }

    /** Update the order of this fig and the order of the
     *    figs that are inside of this fig */
    public void elementOrdering(Vector figures) {
        int size = figures.size();
        getLayer().bringToFront(this);
        if (figures != null && (size > 0)) {
            for (int i = 0; i < size; i++) {
                Object o = figures.elementAt(i);
                if (o instanceof FigNodeModelElement
                    && o != getEnclosingFig()) {
                    FigNodeModelElement fignode = (FigNodeModelElement)o;
                    Vector enclosed = fignode.getEnclosedFigs();
                    fignode.elementOrdering(enclosed);
                }
            }
        }
    }

    public Selection makeSelection() {
        return new SelectionNodeClarifiers(this);
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
        int iconX = _x;
        int iconY = _y - 10;
        ToDoList list = Designer.theDesigner().getToDoList();
        Vector items = list.elementsForOffender(getOwner());
        int size = items.size();
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem)items.elementAt(i);
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier)icon).setFig(this);
                ((Clarifier)icon).setToDoItem(item);
            }
            icon.paintIcon(null, g, iconX, iconY);
            iconX += icon.getIconWidth();
        }
        items = list.elementsForOffender(this);
        size = items.size();
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem)items.elementAt(i);
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier)icon).setFig(this);
                ((Clarifier)icon).setToDoItem(item);
            }
            icon.paintIcon(null, g, iconX, iconY);
            iconX += icon.getIconWidth();
        }
    }

    public ToDoItem hitClarifier(int x, int y) {
        int iconX = _x;
        ToDoList list = Designer.theDesigner().getToDoList();
        Vector items = list.elementsForOffender(getOwner());
        int size = items.size();
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem)items.elementAt(i);
            Icon icon = item.getClarifier();
            int width = icon.getIconWidth();
            if (y >= _y - 15
                && y <= _y + 10
                && x >= iconX
                && x <= iconX + width)
                return item;
            iconX += width;
        }
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem)items.elementAt(i);
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier)icon).setFig(this);
                ((Clarifier)icon).setToDoItem(item);
                if (((Clarifier)icon).hit(x, y))
                    return item;
            }
        }
        items = list.elementsForOffender(this);
        size = items.size();
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem)items.elementAt(i);
            Icon icon = item.getClarifier();
            int width = icon.getIconWidth();
            if (y >= _y - 15
                && y <= _y + 10
                && x >= iconX
                && x <= iconX + width)
                return item;
            iconX += width;
        }
        for (int i = 0; i < size; i++) {
            ToDoItem item = (ToDoItem)items.elementAt(i);
            Icon icon = item.getClarifier();
            if (icon instanceof Clarifier) {
                ((Clarifier)icon).setFig(this);
                ((Clarifier)icon).setToDoItem(item);
                if (((Clarifier)icon).hit(x, y))
                    return item;
            }
        }
        return null;
    }

    public String getTipString(MouseEvent me) {
        ToDoItem item = hitClarifier(me.getX(), me.getY());
        String tip = "";
        if (item != null)
            tip = item.getHeadline() + " ";
        else if (getOwner() != null)
            tip = getOwner().toString();
        else
            tip = toString();
        if (tip != null && tip.length() > 0 && !tip.endsWith(" "))
            tip += " ";
        return tip;
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    public void vetoableChange(PropertyChangeEvent pce) {
        cat.debug("in vetoableChange");
        Object src = pce.getSource();
        if (src == getOwner()) {
            DelayedChangeNotify delayedNotify =
                new DelayedChangeNotify(this, pce);
            SwingUtilities.invokeLater(delayedNotify);
        } else
            cat.debug(
                "FigNodeModelElement got vetoableChange"
                    + " from non-owner:"
                    + src);
    }

    public void delayedVetoableChange(PropertyChangeEvent pce) {
        cat.debug("in delayedVetoableChange");
        Object src = pce.getSource();
        startTrans();
        // update any text, colors, fonts, etc.
        renderingChanged();
        endTrans();
    }

    protected void updateBounds() {
        if (!checkSize)
            return;
        Rectangle bbox = getBounds();
        Dimension minSize = getMinimumSize();
        bbox.width = Math.max(bbox.width, minSize.width);
        bbox.height = Math.max(bbox.height, minSize.height);
        setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    }

    public void propertyChange(PropertyChangeEvent pve) {
        Object src = pve.getSource();
        String pName = pve.getPropertyName();
        if (pName.equals("editing")
            && Boolean.FALSE.equals(pve.getNewValue())) {
            cat.debug("finished editing");
            try {
                startTrans();
                //parse the text that was edited
                textEdited((FigText)src);
                // resize the FigNode to accomodate the new text
                Rectangle bbox = getBounds();
                Dimension minSize = getMinimumSize();
                bbox.width = Math.max(bbox.width, minSize.width);
                bbox.height = Math.max(bbox.height, minSize.height);
                setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
                endTrans();
            } catch (PropertyVetoException ex) {
                cat.error(
                    "could not parse the text entered. PropertyVetoException",
                    ex);
            }
        } else
            super.propertyChange(pve);
    }

    /** This method is called after the user finishes editing a text
     *  field that is in the FigNodeModelElement.  Determine which field
     *  and update the model.  This class handles the name, subclasses
     *  should override to handle other text elements. */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        if (ft == _name) {
            MModelElement me = (MModelElement)getOwner();
            if (me == null)
                return;
            try {
                ParserDisplay.SINGLETON.parseModelElement(
                    me,
                    ft.getText().trim());
                ProjectBrowser.getInstance().getStatusBar().showStatus("");
                updateNameText();
            } catch (ParseException pe) {
                ProjectBrowser.getInstance().getStatusBar().showStatus(
                    "Error: " + pe + " at " + pe.getErrorOffset());
            }
            //me.setName(ft.getText());
        }
    }

    ////////////////////////////////////////////////////////////////
    // event handlers - MouseListener implementation

    /** If the user double clicks on any part of this FigNode, pass it
     *  down to one of the internal Figs. This allows the user to
     *  initiate direct text editing. */
    public void mouseClicked(MouseEvent me) {
        if (!_readyToEdit) {
            if (getOwner() instanceof MModelElement) {
                MModelElement own = (MModelElement)getOwner();
                own.setName("");
                _readyToEdit = true;
            } else {
                cat.debug("not ready to edit name");
                return;
            }
        }
        if (me.isConsumed())
            return;
        if (me.getClickCount() >= 2
            && !(me.isPopupTrigger()
                || me.getModifiers() == InputEvent.BUTTON3_MASK)) {
            if (getOwner() == null)
                return;
            Rectangle r = new Rectangle(me.getX() - 2, me.getY() - 2, 4, 4);
            Fig f = hitFig(r);
            if (f instanceof MouseListener)
                 ((MouseListener)f).mouseClicked(me);
            else if (f instanceof FigGroup) {
                //this enables direct text editing for sub figs of a FigGroup object:
                Fig f2 = ((FigGroup)f).hitFig(r);
                if (f2 instanceof MouseListener)
                     ((MouseListener)f2).mouseClicked(me);
                else
                    createFeatureIn((FigGroup)f, me);
            }
        }
        me.consume();
    }

    public void keyPressed(KeyEvent ke) {
        if (!_readyToEdit) {
            if (getOwner() instanceof MModelElement) {
                MModelElement me = (MModelElement)getOwner();
                me.setName("");
                _readyToEdit = true;
            } else {
                cat.debug("not ready to edit name");
                return;
            }
        }
        if (ke.isConsumed())
            return;
        if (getOwner() == null)
            return;
        _name.keyPressed(ke);
        //ke.consume();
        //     MModelElement me = (MModelElement) getOwner();
        //     if (me == null) return;
        //     try { me.setName(new Name(_name.getText())); }
        //     catch (PropertyVetoException pve) { }
    }

    /** not used, do nothing. */
    public void keyReleased(KeyEvent ke) {
    }

    public void keyTyped(KeyEvent ke) {
        //     if (ke.isConsumed()) return;
        //     _name.keyTyped(ke);
        //     //ke.consume();
        //     MModelElement me = (MModelElement) getOwner();
        //     if (me == null) return;
        //     try { me.setName(new Name(_name.getText())); }
        //     catch (PropertyVetoException pve) { }  
    }

    ////////////////////////////////////////////////////////////////
    // internal methods

    /** This is called aftern any part of the UML MModelElement has
     *  changed. This method automatically updates the name FigText.
     *  Subclasses should override and update other parts.
     */
    protected void modelChanged(MElementEvent mee) {
        if (mee == null)
            throw new IllegalArgumentException("event may never be null with modelchanged");
        MModelElement me = (MModelElement)getOwner();
        if (me == null)
            return;
        if ("name".equals(mee.getName()) && mee.getSource() == getOwner()) {
            updateNameText();
            damage();
        }
        if ((mee.getSource() == getOwner()
            && mee.getName().equals("stereotype"))) {
            updateStereotypeText();
            damage();
        }
    }

    protected void createFeatureIn(FigGroup fg, InputEvent me) {
        // must be overridden to make sense
        // (I didn't want to make it abstract because it might not be required)
    }

    public void propertySet(MElementEvent mee) {
        //if (_group != null) _group.propertySet(mee);        
        modelChanged(mee);
    }
    public void listRoleItemSet(MElementEvent mee) {
        //if (_group != null) _group.listRoleItemSet(mee);
        modelChanged(mee);
    }
    public void recovered(MElementEvent mee) {
        //if (_group != null) _group.recovered(mee);
    }
    public void removed(MElementEvent mee) {
        cat.debug("deleting: " + this +mee);
        Object o = mee.getSource();
        if (o == getOwner()) {
            delete();
        } else if (isPartlyOwner(o)) {
            updateBounds();
            damage();
            return;
        }

    }

    protected boolean isPartlyOwner(Object o) {
        if (o == null)
            return false;
        if (o == getOwner())
            return true;
        Iterator it = getFigs().iterator();
        while (it.hasNext()) {
            Fig fig = (Fig)it.next();
            if (isPartlyOwner(fig, o))
                return true;
        }
        return false;
    }

    protected boolean isPartlyOwner(Fig fig, Object o) {
        if (o == null)
            return false;
        if (o == fig.getOwner())
            return true;
        if (fig instanceof FigGroup) {
            Iterator it = ((FigGroup)fig).getFigs().iterator();
            while (it.hasNext()) {
                Fig fig2 = (Fig)it.next();
                if (isPartlyOwner(fig2, o))
                    return true;
            }
        }
        return false;
    }
    public void roleAdded(MElementEvent mee) {
        //if (_group != null) _group.roleAdded(mee);
        modelChanged(mee);
    }
    public void roleRemoved(MElementEvent mee) {
        //if (_group != null) _group.roleRemoved(mee);
        modelChanged(mee);
    }

    public void dispose() {
        Object own = getOwner();
        if (own != null) {
            Trash.SINGLETON.addItemFrom(own, null);
            if (own instanceof MModelElement) {
                UmlFactory.getFactory().delete((MModelElement)own);
            }
        }
        Iterator it = getFigs().iterator();
        while (it.hasNext()) {
            ((Fig)it.next()).dispose();
        }
        super.dispose();
    }

    public void setOwner(Object own) {
        Object oldOwner = getOwner();
        updateListeners(own);
        super.setOwner(own);
        if (own instanceof MModelElement) {
            MModelElement me = (MModelElement)own;
            if (me.getUUID() == null)
                me.setUUID(UUIDManager.SINGLETON.getNewUUID());
        }
        _readyToEdit = true;
        if (own != null)
            renderingChanged();
        updateBounds();
        bindPort(own, _bigPort);

    }

    /**
     * Updates the text of the sterotype FigText. Override in subclasses to get
     * wanted behaviour.
     * TODO remove all 'misuses' of the stereotype figtexts (like in FigInterface)
     */
    protected void updateStereotypeText() {
        _stereo.setText(
            Notation.generate(this, ModelFacade.getStereoType(getOwner())));
    }

    /**
     * Updates the text of the name FigText.
     */
    protected void updateNameText() {
        if (_readyToEdit) {
            if (getOwner() == null)
                return;
            MModelElement owner = (MModelElement)getOwner();
            String nameStr =
                Notation.generate(this, ((MModelElement)getOwner()).getName());
            _name.setText(nameStr);
            updateBounds();
        }
    }

    /**
     * Implementations of this method should register/unregister the fig for all
     * (model)events. For FigNodeModelElement only the fig itself is registred
     * as listening to events fired by the owner itself. But for, for example,
     * FigClass the fig must also register for events fired by the operations
     * and attributes of the owner.
     * @param newOwner
     */
    protected void updateListeners(Object newOwner) {
        Object oldOwner = getOwner();
        if (oldOwner != null)
            UmlModelEventPump.getPump().removeModelEventListener(
                this,
                (MBase)oldOwner);
        if (newOwner != null) {
            UmlModelEventPump.getPump().addModelEventListener(
                this,
                (MBase)newOwner);
        }

    }

    /** This default implementation simply requests the default notation.
     *
     * @return null
     */
    public NotationName getContextNotation() {
        return null;
    }

    public void notationChanged(ArgoNotationEvent event) {
        renderingChanged();
    }

    public void notationAdded(ArgoNotationEvent event) {
    }
    public void notationRemoved(ArgoNotationEvent event) {
    }
    public void notationProviderAdded(ArgoNotationEvent event) {
    }
    public void notationProviderRemoved(ArgoNotationEvent event) {
    }

    /**
     * Rerenders the fig if needed. This functionality was originally the functionality
     * of modelChanged but modelChanged takes the event now into account.
     */
    public void renderingChanged() {
        updateNameText();
        updateStereotypeText();
        updateBounds();
        damage();
    }

    public void calcBounds() {
        if (suppressCalcBounds)
            return;
        super.calcBounds();
    }

    public void enableSizeChecking(boolean flag) {
        checkSize = flag;
    }

    /** returns the new size of the FigGroup (either attributes or operations)
      after calculation new bounds for all sub-figs, considering their
      minimal sizes; FigGroup need not be displayed; no update event is fired */
    protected Dimension getUpdatedSize(
        FigGroup fg,
        int x,
        int y,
        int w,
        int h) {
        int newW = w;
        int n = fg.getFigs().size() - 1;
        int newH = checkSize ? Math.max(h, ROWHEIGHT * Math.max(1, n) + 2) : h;
        int step = (n > 0) ? (newH - 1) / n : 0;
        // width step between FigText objects
        //int maxA = Toolkit.getDefaultToolkit().getFontMetrics(LABEL_FONT).getMaxAscent();

        //set new bounds for all included figs
        Enumeration figs = fg.elements();
        Fig bigPort = (Fig)figs.nextElement();
        Fig fi;
        int fw, yy = y;
        while (figs.hasMoreElements()) {
            fi = (Fig)figs.nextElement();
            fw = fi.getMinimumSize().width;
            if (!checkSize && fw > newW - 2)
                fw = newW - 2;
            fi.setBounds(x + 1, yy + 1, fw, Math.min(ROWHEIGHT, step) - 2);
            if (checkSize && newW < fw + 2)
                newW = fw + 2;
            yy += step;
        }
        bigPort.setBounds(x, y, newW, newH);
        // rectangle containing all following FigText objects
        fg.calcBounds();
        return new Dimension(newW, newH);
    }

    public void setShadowSize(int size) {
        _shadowSize = size;
    }

    public int getShadowSize() {
        return _shadowSize;
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
     * @see org.tigris.gef.presentation.Fig#delete()
     */
    public void delete() {

        Object own = getOwner();
        if (own instanceof MClassifier) {
            MClassifier cls = (MClassifier)own;
            Iterator it = cls.getFeatures().iterator();
            while (it.hasNext()) {
                MFeature feature = (MFeature)it.next();
                if (feature instanceof MOperation) {
                    MOperation oper = (MOperation)feature;
                    Iterator it2 = oper.getParameters().iterator();
                    while (it2.hasNext()) {
                        UmlModelEventPump.getPump().removeModelEventListener(
                            this,
                            (MParameter)it2.next());
                    }
                }
                UmlModelEventPump.getPump().removeModelEventListener(
                    this,
                    feature);
            }
        }
        if (own instanceof MBase) {
            UmlModelEventPump.getPump().removeModelEventListener(
                this,
                (MBase)own);
        }
        super.delete();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#damage()
     */
    public void damage() {
        updateEdges();
        super.damage();
    }
    
    /**
     * If the diagram this fig is part of is selected, this method is called so 
     * this fig is (re)registred as model event listener to it's owner and the 
     * fig is repainted.  
     *
     */
    public void setAsTarget() {
        Object owner = getOwner();        
        setOwner(null);
        setOwner(owner);
        damage();
    }
    
    public void removeAsTarget() {
        // disableEventSenders();
    }
    
   

} /* end class FigNodeModelElement */

