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

// File: FigClass.java
// Classes: FigClass
// Original Author: abonner

// $Id$

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Fix for ever
// increasing vertical size of classes with stereotypes (issue 745).

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JMenu;

import org.apache.log4j.Category;
import org.argouml.application.api.Notation;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.generator.ParserDisplay;
import org.argouml.uml.ui.ActionAddAttribute;
import org.argouml.uml.ui.ActionAddNote;
import org.argouml.uml.ui.ActionAddOperation;
import org.argouml.uml.ui.ActionCompartmentDisplay;
import org.argouml.uml.ui.ActionModifier;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MElementResidence;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/**
 * <p>Class to display graphics for a UML Class in a diagram.</p>
 */

public class FigClass extends FigNodeModelElement {
    protected static Category cat = Category.getInstance(FigClass.class);

    ////////////////////////////////////////////////////////////////
    // constants

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * <p>The vector of graphics for attributes (if any). First one is the
     *   rectangle for the entire attributes box.</p>
     */
    protected FigGroup _attrVec;

    /**
     * <p>The vector of graphics for operations (if any). First one is the
     *   rectangle for the entire operations box.</p>
     */
    protected FigGroup _operVec;

    /**
     * <p>The rectangle for the entire attribute box.</p>
     */
    protected FigRect _attrBigPort;

    /**
     * <p>The rectangle for the entire operations box.</p>
     */
    protected FigRect _operBigPort;

    /**
     * <p>A rectangle to blank out the line that would otherwise appear at the
     *   bottom of the stereotype text box.</p>
     */
    protected FigRect _stereoLineBlinder;

    /**
     * <p>Manages residency of a class within a component on a deployment
     *   diagram. Not clear why it is public, or even why it is an instance
     *   variable (rather than local to the method).</p>
     */
    public MElementResidence resident =
        UmlFactory.getFactory().getCore().createElementResidence();

    /**
     * <p>Text highlighted by mouse actions on the diagram.</p>
     */
    protected CompartmentFigText highlightedFigText = null;

    /**
     * <p>Flag to indicate that we have just been created. This is to fix the
     *   problem with loading classes that have stereotypes already
     *   defined.</p>
     */
    private boolean _newlyCreated = false;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * <p>Main constructor for a {@link FigClass}.</p>
     *
     * <p>Parent {@link FigNodeModelElement} will have created the main box
     *   {@link #_bigPort} and its name {@link #_name} and stereotype (@link
     *   #_stereo}. This constructor creates a box for the attributes and
     *   operations.</p>
     *
     * <p>The properties of all these graphic elements are adjusted
     *   appropriately. The main boxes are all filled and have outlines.</p>
     *
     * <p>For reasons I don't understand the stereotype is created in a box
     *   with lines. So we have to created a blanking rectangle to overlay the
     *   bottom line, and avoid four compartments showing.</p>
     *
     * <p>There is some complex logic to allow for the possibility that
     *   stereotypes may not be displayed (unlike operations and attributes
     *   this is not a standard thing for UML). Some care is needed to ensure
     *   that additional space is not added, each time a stereotyped class is
     *   loaded.</p>
     *
     * <p>There is a particular problem when loading diagrams with stereotyped
     *   classes. Because we create a FigClass indicating the stereotype is not
     *   displayed, we then add extra space for such classes when they are
     *   first rendered. This ought to be fixed by correctly saving the class
     *   dimensions, but that needs more work. The solution here is to use a
     *   simple flag to indicate the FigClass has just been created.</p>
     *
     * <p><em>Warning</em>. Much of the graphics positioning is hard coded. The
     *   overall figure is placed at location (10,10). The name compartment (in
     *   the parent {@link FigNodeModelElement} is 21 pixels high. The
     *   stereotype compartment is created 15 pixels high in the parent, but we
     *   change it to 19 pixels, 1 more than ({@link #STEREOHEIGHT} here. The
     *   attribute and operations boxes are created at 19 pixels, 2 more than
     *   {@link #ROWHEIGHT}.</p>
     *
     * <p>CAUTION: This constructor (with no arguments) is the only one
     *   that does enableSizeChecking(false), all others must set it true.
     *   This is because this constructor is the only one called when loading
     *   a project. In this case, the parsed size must be maintained.</p>
     */

    public FigClass() {

        // Set name box. Note the upper line will be blanked out if there is
        // eventually a stereotype above.
        _name.setLineWidth(1);
        _name.setFilled(true);

        // this rectangle marks the attribute section; all attributes are inside it
        _attrBigPort =
            new FigRect(10, 30, 60, ROWHEIGHT + 2, Color.black, Color.white);
        _attrBigPort.setFilled(true);
        _attrBigPort.setLineWidth(1);

        // Attributes inside. First one is the attribute box itself.
        _attrVec = new FigGroup();
        _attrVec.setFilled(true);
        _attrVec.setLineWidth(1);
        _attrVec.addFig(_attrBigPort);

        // this rectangle marks the operation section; all operations are inside it
        _operBigPort =
            new FigRect(
                10,
                31 + ROWHEIGHT,
                60,
                ROWHEIGHT + 2,
                Color.black,
                Color.white);
        _operBigPort.setFilled(true);
        _operBigPort.setLineWidth(1);

        _operVec = new FigGroup();
        _operVec.setFilled(true);
        _operVec.setLineWidth(1);
        _operVec.addFig(_operBigPort);

        // Set properties of the stereotype box. Make it 1 pixel higher than
        // before, so it overlaps the name box, and the blanking takes out both
        // lines. Initially not set to be displayed, but this will be changed
        // when we try to render it, if we find we have a stereotype.
        _stereo.setExpandOnly(true);
        _stereo.setFilled(true);
        _stereo.setLineWidth(1);
        _stereo.setEditable(false);
        _stereo.setHeight(STEREOHEIGHT + 1);
        // +1 to have 1 pixel overlap with _name
        _stereo.setDisplayed(false);

        // A thin rectangle to overlap the boundary line between stereotype
        // and name. This is just 2 pixels high, and we rely on the line
        // thickness, so the rectangle does not need to be filled. Whether to
        // display is linked to whether to display the stereotype.
        _stereoLineBlinder =
            new FigRect(11, 10 + STEREOHEIGHT, 58, 2, Color.white, Color.white);
        _stereoLineBlinder.setLineWidth(1);
        _stereoLineBlinder.setDisplayed(false);

        // Mark this as newly created. This is to get round the problem with
        // creating figs for loaded classes that had stereotypes. They are
        // saved with their dimensions INCLUDING the stereotype, but since we
        // pretend the stereotype is not visible, we add height the first time
        // we render such a class. This is a complete fudge, and really we
        // ought to address how class objects with stereotypes are saved. But
        // that will be hard work.
        _newlyCreated = true;

        // Put all the bits together, suppressing bounds calculations until
        // we're all done for efficiency.
        enableSizeChecking(false);
        suppressCalcBounds = true;
        addFig(_bigPort);
        // addFig(_namespace);
        addFig(_stereo);
        addFig(_name);
        addFig(_stereoLineBlinder);
        addFig(_attrVec);
        addFig(_operVec);
        suppressCalcBounds = false;

        // Set the bounds of the figure to the total of the above (hardcoded)
        setBounds(10, 10, 60, 22 + 2 * ROWHEIGHT);
    }

    /**
     * <p>Constructor for use if this figure is created for an existing class
     *   node in the metamodel.</p>
     *
     * <p>Set the figure's name according to this node. This is used when the
     *   user click's on 'add to diagram' in the navpane.  Don't know if this
     *   should rather be done in one of the super classes, since similar code
     *   is used in FigInterface.java etc.  Andreas Rueckert
     *   &lt;a_rueckert@gmx.net&gt;</p>
     *
     * @param gm   Not actually used in the current implementation
     *
     * @param node The UML object being placed.
     */
    public FigClass(GraphModel gm, Object node) {
        this();
        enableSizeChecking(true);
        setOwner(node);
        if ((node instanceof MClassifier)
            && (((MClassifier) node).getName() != null))
            _name.setText(((MModelElement) node).getName());
    }

    public String placeString() {
        return "new Class";
    }

    public Object clone() {
        FigClass figClone = (FigClass) super.clone();
        Vector v = figClone.getFigs();
        figClone._bigPort = (FigRect) v.elementAt(0);
        figClone._stereo = (FigText) v.elementAt(1);
        figClone._name = (FigText) v.elementAt(2);
        figClone._stereoLineBlinder = (FigRect) v.elementAt(3);
        figClone._attrVec = (FigGroup) v.elementAt(4);
        figClone._operVec = (FigGroup) v.elementAt(5);
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public Selection makeSelection() {
        return new SelectionClass(this);
    }

    /**
     * Build a collection of menu items relevant for a right-click popup menu on a Package.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);
        JMenu addMenu = new JMenu("Add");
        addMenu.add(ActionAddAttribute.SINGLETON);
        addMenu.add(ActionAddOperation.SINGLETON);
        addMenu.add(ActionAddNote.SINGLETON);
        popUpActions.insertElementAt(addMenu, popUpActions.size() - 1);
        JMenu showMenu = new JMenu("Show");
        if (_attrVec.isDisplayed() && _operVec.isDisplayed())
            showMenu.add(ActionCompartmentDisplay.HideAllCompartments);
        else if (!_attrVec.isDisplayed() && !_operVec.isDisplayed())
            showMenu.add(ActionCompartmentDisplay.ShowAllCompartments);

        if (_attrVec.isDisplayed())
            showMenu.add(ActionCompartmentDisplay.HideAttrCompartment);
        else
            showMenu.add(ActionCompartmentDisplay.ShowAttrCompartment);

        if (_operVec.isDisplayed())
            showMenu.add(ActionCompartmentDisplay.HideOperCompartment);
        else
            showMenu.add(ActionCompartmentDisplay.ShowOperCompartment);

        popUpActions.insertElementAt(showMenu, popUpActions.size() - 1);

        // Block added by BobTarling 7-Jan-2001
        MClass mclass = (MClass) getOwner();
        ArgoJMenu modifierMenu = new ArgoJMenu("Modifiers");

        modifierMenu.addCheckItem(
            new ActionModifier(
                "Public",
                "visibility",
                "getVisibility",
                "setVisibility",
                mclass,
                MVisibilityKind.class,
                MVisibilityKind.PUBLIC,
                null));
        modifierMenu.addCheckItem(
            new ActionModifier(
                "Abstract",
                "isAbstract",
                "isAbstract",
                "setAbstract",
                mclass));
        modifierMenu.addCheckItem(
            new ActionModifier("Leaf", "isLeaf", "isLeaf", "setLeaf", mclass));
        modifierMenu.addCheckItem(
            new ActionModifier("Root", "isRoot", "isRoot", "setRoot", mclass));
        modifierMenu.addCheckItem(
            new ActionModifier(
                "Active",
                "isActive",
                "isActive",
                "setActive",
                mclass));

        popUpActions.insertElementAt(modifierMenu, popUpActions.size() - 1);
        // end of block

        return popUpActions;
    }

    public FigGroup getOperationsFig() {
        return _operVec;
    }
    public FigGroup getAttributesFig() {
        return _attrVec;
    }

    /**
     * Returns the status of the operation field.
     * @return true if the operations are visible, false otherwise
     */
    public boolean isOperationVisible() {
        return _operVec.isDisplayed();
    }

    /**
     * Returns the status of the attribute field.
     * @return true if the attributes are visible, false otherwise
     */
    public boolean isAttributeVisible() {
        return _attrVec.isDisplayed();
    }

    public void setAttributeVisible(boolean isVisible) {
        Rectangle rect = getBounds();
        int h =
            checkSize
                ? (ROWHEIGHT * Math.max(1, _attrVec.getFigs().size() - 1) + 2)
                    * rect.height
                    / getMinimumSize().height
                : 0;
        if (_attrVec.isDisplayed()) {
            if (!isVisible) {
                damage();
                Enumeration enum = _attrVec.getFigs().elements();
                while (enum.hasMoreElements())
                     ((Fig) (enum.nextElement())).setDisplayed(false);
                _attrVec.setDisplayed(false);
                setBounds(rect.x, rect.y, rect.width, rect.height - h);
            }
        } else {
            if (isVisible) {
                Enumeration enum = _attrVec.getFigs().elements();
                while (enum.hasMoreElements())
                     ((Fig) (enum.nextElement())).setDisplayed(true);
                _attrVec.setDisplayed(true);
                setBounds(rect.x, rect.y, rect.width, rect.height + h);
                damage();
            }
        }
    }

    public void setOperationVisible(boolean isVisible) {
        Rectangle rect = getBounds();
        int h =
            checkSize
                ? (ROWHEIGHT * Math.max(1, _operVec.getFigs().size() - 1) + 2)
                    * rect.height
                    / getMinimumSize().height
                : 0;
        if (_operVec.isDisplayed()) {
            if (!isVisible) {
                damage();
                Enumeration enum = _operVec.getFigs().elements();
                while (enum.hasMoreElements())
                     ((Fig) (enum.nextElement())).setDisplayed(false);
                _operVec.setDisplayed(false);
                setBounds(rect.x, rect.y, rect.width, rect.height - h);
            }
        } else {
            if (isVisible) {
                Enumeration enum = _operVec.getFigs().elements();
                while (enum.hasMoreElements())
                     ((Fig) (enum.nextElement())).setDisplayed(true);
                _operVec.setDisplayed(true);
                setBounds(rect.x, rect.y, rect.width, rect.height + h);
                damage();
            }
        }
    }

    /**
     * <p>Gets the minimum size permitted for a class on the diagram.</p>
     *
     * <p>Parts of this are hardcoded, notably the fact that the name
     *   compartment has a minimum height of 21 pixels.</p>
     *
     * @return  the size of the minimum bounding box.
     */
    public Dimension getMinimumSize() {

        // Use "aSize" to build up the minimum size. Start with the size of the
        // name compartment and build up.

        Dimension aSize = _name.getMinimumSize();
        int h = aSize.height;
        int w = aSize.width;

        // Ensure that the minimum height of the name compartment is at least
        // 21 pixels (hardcoded).

        if (aSize.height < 21) {
            aSize.height = 21;
        }

        // If we have a stereotype displayed, then allow some space for that
        // (width and height)

        if (_stereo.isDisplayed()) {
            aSize.width = Math.max(aSize.width, _stereo.getMinimumSize().width);
            aSize.height += STEREOHEIGHT;
        }

        // Allow space for each of the attributes we have

        if (_attrVec.isDisplayed()) {

            // Loop through all the attributes, to find the widest (remember
            // the first fig is the box for the whole lot, so ignore it).

            Enumeration enum = _attrVec.getFigs().elements();
            enum.nextElement(); // ignore

            while (enum.hasMoreElements()) {
                int elemWidth =
                    ((FigText) enum.nextElement()).getMinimumSize().width + 2;
                aSize.width = Math.max(aSize.width, elemWidth);
            }

            // Height allows one row for each attribute (remember to ignore the
            // first element.

            aSize.height += ROWHEIGHT
                * Math.max(1, _attrVec.getFigs().size() - 1)
                + 1;
        }

        // Allow space for each of the operations we have

        if (_operVec.isDisplayed()) {

            // Loop through all the operations, to find the widest (remember
            // the first fig is the box for the whole lot, so ignore it).

            Enumeration enum = _operVec.getFigs().elements();
            enum.nextElement(); // ignore

            while (enum.hasMoreElements()) {
                int elemWidth =
                    ((FigText) enum.nextElement()).getMinimumSize().width + 2;
                aSize.width = Math.max(aSize.width, elemWidth);
            }

            aSize.height += ROWHEIGHT
                * Math.max(1, _operVec.getFigs().size() - 1)
                + 1;
        }

        // we want to maintain a minimum width for the class
        aSize.width = Math.max(60, aSize.width);

        // And now aSize has the answer

        return aSize;
    }

    public void setFillColor(Color lColor) {
        super.setFillColor(lColor);
        _stereoLineBlinder.setLineColor(lColor);
    }

    public void setLineColor(Color lColor) {
        super.setLineColor(lColor);
        _stereoLineBlinder.setLineColor(_stereoLineBlinder.getFillColor());
    }

    public void translate(int dx, int dy) {
        super.translate(dx, dy);
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionClass)
             ((SelectionClass) sel).hideButtons();
    }

    ////////////////////////////////////////////////////////////////
    // user interaction methods

    public void mouseExited(MouseEvent me) {
        super.mouseExited(me);
        unhighlight();
    }

    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            CompartmentFigText ft = unhighlight();
            if (ft != null) {
                int i = _attrVec.getFigs().indexOf(ft);
                FigGroup fg = _attrVec;
                if (i == -1) {
                    i = _operVec.getFigs().indexOf(ft);
                    fg = _operVec;
                }
                if (i != -1) {
                    if (key == KeyEvent.VK_UP) {
                        ft =
                            (CompartmentFigText) getPreviousVisibleFeature(fg,
                                ft,
                                i);
                    } else {
                        ft =
                            (CompartmentFigText) getNextVisibleFeature(fg,
                                ft,
                                i);
                    }
                    if (ft != null) {
                        ft.setHighlighted(true);
                        highlightedFigText = ft;
                        return;
                    }
                }
            }
        } else if (key == KeyEvent.VK_ENTER && highlightedFigText != null) {
            highlightedFigText.startTextEditor(ke);
            ke.consume();
            return;
        }
        super.keyPressed(ke);
    }

    ////////////////////////////////////////////////////////////////
    // internal methods

    protected void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        MClassifier cls = (MClassifier) getOwner();
        if (cls == null)
            return;
        int i = _attrVec.getFigs().indexOf(ft);
        if (i != -1) {
            highlightedFigText = (CompartmentFigText) ft;
            highlightedFigText.setHighlighted(true);
            try {
                ParserDisplay.SINGLETON.parseAttributeFig(
                    cls,
                    (MAttribute) highlightedFigText.getOwner(),
                    highlightedFigText.getText().trim());
                ProjectBrowser.TheInstance.getStatusBar().showStatus("");
            } catch (ParseException pe) {
                ProjectBrowser.TheInstance.getStatusBar().showStatus(
                    "Error: " + pe + " at " + pe.getErrorOffset());
            }
            return;
        }
        i = _operVec.getFigs().indexOf(ft);
        if (i != -1) {
            highlightedFigText = (CompartmentFigText) ft;
            highlightedFigText.setHighlighted(true);
            try {
                ParserDisplay.SINGLETON.parseOperationFig(
                    cls,
                    (MOperation) highlightedFigText.getOwner(),
                    highlightedFigText.getText().trim());
                ProjectBrowser.TheInstance.getStatusBar().showStatus("");
            } catch (ParseException pe) {
                ProjectBrowser.TheInstance.getStatusBar().showStatus(
                    "Error: " + pe + " at " + pe.getErrorOffset());
            }
            return;
        }
    }

    protected FigText getPreviousVisibleFeature(
        FigGroup fgVec,
        FigText ft,
        int i) {
        if (fgVec == null || i < 1)
            return null;
        FigText ft2 = null;
        Vector v = fgVec.getFigs();
        if (i >= v.size() || !((FigText) v.elementAt(i)).isDisplayed())
            return null;
        do {
            i--;
            while (i < 1) {
                fgVec = (fgVec == _attrVec) ? _operVec : _attrVec;
                v = fgVec.getFigs();
                i = v.size() - 1;
            }
            ft2 = (FigText) v.elementAt(i);
            if (!ft2.isDisplayed())
                ft2 = null;
        }
        while (ft2 == null);
        return ft2;
    }

    protected FigText getNextVisibleFeature(
        FigGroup fgVec,
        FigText ft,
        int i) {
        if (fgVec == null || i < 1)
            return null;
        FigText ft2 = null;
        Vector v = fgVec.getFigs();
        if (i >= v.size() || !((FigText) v.elementAt(i)).isDisplayed())
            return null;
        do {
            i++;
            while (i >= v.size()) {
                fgVec = (fgVec == _attrVec) ? _operVec : _attrVec;
                v = fgVec.getFigs();
                i = 1;
            }
            ft2 = (FigText) v.elementAt(i);
            if (!ft2.isDisplayed())
                ft2 = null;
        }
        while (ft2 == null);
        return ft2;
    }

    protected void createFeatureIn(FigGroup fg, InputEvent ie) {
        CompartmentFigText ft = null;
        MClassifier cls = (MClassifier) getOwner();
        if (cls == null)
            return;
        if (fg == _attrVec)
            ActionAddAttribute.SINGLETON.actionPerformed(null);
        else
            ActionAddOperation.SINGLETON.actionPerformed(null);
        ft = (CompartmentFigText) fg.getFigs().lastElement();
        if (ft != null) {
            ft.startTextEditor(ie);
            ft.setHighlighted(true);
            highlightedFigText = ft;
        }
    }

    protected CompartmentFigText unhighlight() {
        CompartmentFigText ft;
        Vector v = _attrVec.getFigs();
        int i;
        for (i = 1; i < v.size(); i++) {
            ft = (CompartmentFigText) v.elementAt(i);
            if (ft.isHighlighted()) {
                ft.setHighlighted(false);
                highlightedFigText = null;
                return ft;
            }
        }
        v = _operVec.getFigs();
        for (i = 1; i < v.size(); i++) {
            ft = (CompartmentFigText) v.elementAt(i);
            if (ft.isHighlighted()) {
                ft.setHighlighted(false);
                highlightedFigText = null;
                return ft;
            }
        }
        return null;
    }

    /**
     * Handles changes of the model. Takes into account the event that occured. If
     * you need to update the whole fig, consider using renderingChanged.
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(MElementEvent)
     */
    protected void modelChanged(MElementEvent mee) {
        if (getOwner() == null)
            return;
        MClass cls = (MClass) getOwner();
        // attributes
        if (mee == null
            || mee.getSource() instanceof MAttribute
            || (mee.getSource() == getOwner()
                && mee.getName().equals("feature"))) {
            updateAttributes();
            damage();
        }
        // operations
        if (mee == null
            || mee.getSource() instanceof MOperation
            || mee.getSource() instanceof MParameter
            || (mee.getSource() == getOwner()
                && mee.getName().equals("feature"))) {
            updateOperations();
            damage();
        }
        if (mee == null || mee.getName().equals("isAbstract")) {
            updateAbstract();
            damage();
        }
        // name updating
        super.modelChanged(mee);

    }

    protected void updateStereotypeText() {

        MModelElement me = (MModelElement) getOwner();

        if (me == null) {
            return;
        }

        Rectangle rect = getBounds();
        MStereotype stereo = me.getStereotype();

        if ((stereo == null)
            || (stereo.getName() == null)
            || (stereo.getName().length() == 0)) {

            if (_stereo.isDisplayed()) {
                _stereoLineBlinder.setDisplayed(false);
                _stereo.setDisplayed(false);
                rect.y += STEREOHEIGHT;
                rect.height -= STEREOHEIGHT;
                setBounds(rect.x, rect.y, rect.width, rect.height);
                calcBounds();
            }
        } else {
            _stereo.setText(Notation.generateStereotype(this, stereo));

            if (!_stereo.isDisplayed()) {
                _stereoLineBlinder.setDisplayed(true);
                _stereo.setDisplayed(true);

                // Only adjust the stereotype height if we are not newly
                // created. This gets round the problem of loading classes with
                // stereotypes defined, which have the height already including
                // the stereotype.

                if (!_newlyCreated) {
                    rect.y -= STEREOHEIGHT;
                    rect.height += STEREOHEIGHT;
                    setBounds(rect.x, rect.y, rect.width, rect.height);
                    calcBounds();
                }

            }
        }

        // Whatever happened we are no longer newly created, so clear the
        // flag. Then set the bounds for the rectangle we have defined.

        _newlyCreated = false;

    }

    /**
     * <p>Sets the bounds, but the size will be at least the one returned by
     *   {@link #getMinimumSize()}, unless checking of size is disabled.</p>
     *
     * <p>If the required height is bigger, then the additional height is
     *   equally distributed among all figs (i.e. compartments), such that the
     *   cumulated height of all visible figs equals the demanded height<p>.
     *
     * <p>Some of this has "magic numbers" hardcoded in. In particular there is
     *   a knowledge that the minimum height of a name compartment is 21
     *   pixels.</p>
     *
     * @param x  Desired X coordinate of upper left corner
     *
     * @param y  Desired Y coordinate of upper left corner
     *
     * @param w  Desired width of the FigClass
     *
     * @param h  Desired height of the FigClass
     */
    public void setBounds(int x, int y, int w, int h) {

        // Save our old boundaries (needed later), and get minimum size
        // info. "aSize will be used to maintain a running calculation of our
        // size at various points.

        // "extra_each" is the extra height per displayed fig if requested
        // height is greater than minimal. "height_correction" is the height
        // correction due to rounded division result, will be added to the name
        // compartment

        Rectangle oldBounds = getBounds();
        Dimension aSize = checkSize ? getMinimumSize() : new Dimension(w, h);

        int newW = Math.max(w, aSize.width);
        int newH = h;

        int extra_each = 0;
        int height_correction = 0;

        // First compute all nessessary height data. Easy if we want less than
        // the minimum

        if (newH <= aSize.height) {

            // Just use the mimimum

            newH = aSize.height;

        } else {

            // Split the extra amongst the number of displayed compartments

            int displayedFigs = 1; //this is for _name

            if (_attrVec.isDisplayed()) {
                displayedFigs++;
            }

            if (_operVec.isDisplayed()) {
                displayedFigs++;
            }

            // Calculate how much each, plus a correction to put in the name
            // comparment if the result is rounded

            extra_each = (newH - aSize.height) / displayedFigs;
            height_correction =
                (newH - aSize.height) - (extra_each * displayedFigs);
        }

        // Now resize all sub-figs, including not displayed figs. Start by the
        // name. We override the getMinimumSize if it is less than our view (21
        // pixels hardcoded!). Add in the shared extra, plus in this case the
        // correction.

        int height = _name.getMinimumSize().height;

        if (height < 21) {
            height = 21;
        }

        height += extra_each + height_correction;

        // Now sort out the stereotype display. If the stereotype is displayed,
        // move the upper boundary of the name compartment up and set new
        // bounds for the name and the stereotype compatments and the
        // stereoLineBlinder that blanks out the line between the two

        int currentY = y;

        if (_stereo.isDisplayed()) {
            currentY += STEREOHEIGHT;
        }

        _name.setBounds(x, currentY, newW, height);
        _stereo.setBounds(x, y, newW, STEREOHEIGHT + 1);
        _stereoLineBlinder.setBounds(x + 1, y + STEREOHEIGHT, newW - 2, 2);

        // Advance currentY to where the start of the attribute box is,
        // remembering that it overlaps the next box by 1 pixel. Calculate the
        // size of the attribute box, and update the Y pointer past it if it is
        // displayed.

        currentY += height - 1; // -1 for 1 pixel overlap

        int na =
            (_attrVec.isDisplayed())
                ? Math.max(1, _attrVec.getFigs().size() - 1)
                : 0;
        int no =
            (_operVec.isDisplayed())
                ? Math.max(1, _operVec.getFigs().size() - 1)
                : 0;
        if (checkSize) {
            height = ROWHEIGHT * na + 2 + extra_each;
        } else if (newH > currentY - y && na + no > 0) {
            height = (newH + y - currentY) * na / (na + no) + 1;
        } else {
            height = 1;
        }
        aSize = getUpdatedSize(_attrVec, x, currentY, newW, height);

        if (_attrVec.isDisplayed()) {
            currentY += aSize.height - 1; // -1 for 1 pixel overlap
        }

        // Finally update the bounds of the operations box

        aSize =
            getUpdatedSize(_operVec, x, currentY, newW, newH + y - currentY);

        // set bounds of big box

        _bigPort.setBounds(x, y, newW, newH);

        // Now force calculation of the bounds of the figure, update the edges
        // and trigger anyone who's listening to see if the "bounds" property
        // has changed.

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(Object)
     */
    public void setOwner(Object own) {
        // set the listeners for the operations, features and parameters
        // TODO figure out if this is needed (when is setOwner called?)
        if (own != null) {
            MClass cl = (MClass) own;
            Iterator it = cl.getFeatures().iterator();
            while (it.hasNext()) {
                MFeature feat = (MFeature) it.next();
                if (feat instanceof MOperation) {
                    MOperation oper = (MOperation) feat;
                    Iterator it2 = oper.getParameters().iterator();
                    while (it2.hasNext()) {
                        MParameter param = (MParameter) it2.next();
                        // UmlModelEventPump.getPump().removeModelEventListener(this, param);
                        UmlModelEventPump.getPump().addModelEventListener(
                            this,
                            param);
                    }
                }
                // UmlModelEventPump.getPump().removeModelEventListener(this, feat);
                UmlModelEventPump.getPump().addModelEventListener(this, feat);
            }
        }
        super.setOwner(own);
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {

        if (me.isConsumed())
            return;
        super.mouseClicked(me);

        boolean targetIsSet = false;
        int i = 0;
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionClass)
             ((SelectionClass) sel).hideButtons();
        unhighlight();
        //display attr/op properties if necessary:
        Rectangle r = new Rectangle(me.getX() - 1, me.getY() - 1, 2, 2);
        Fig f = hitFig(r);
        if (f == _attrVec && _attrVec.getHeight() > 0) {
            Vector v = _attrVec.getFigs();
            i =
                (v.size() - 1)
                    * (me.getY() - f.getY() - 3)
                    / _attrVec.getHeight();
            if (i >= 0 && i < v.size() - 1) {
                targetIsSet = true;
                //    me.consume();
                f = (Fig) v.elementAt(i + 1);
                ((CompartmentFigText) f).setHighlighted(true);
                highlightedFigText = (CompartmentFigText) f;
                ProjectBrowser.TheInstance.setTarget(f);
            }
        } else if (f == _operVec && _operVec.getHeight() > 0) {
            Vector v = _operVec.getFigs();
            i =
                (v.size() - 1)
                    * (me.getY() - f.getY() - 3)
                    / _operVec.getHeight();
            if (i >= 0 && i < v.size() - 1) {
                targetIsSet = true;
                //    me.consume();
                f = (Fig) v.elementAt(i + 1);
                ((CompartmentFigText) f).setHighlighted(true);
                highlightedFigText = (CompartmentFigText) f;
                ProjectBrowser.TheInstance.setTarget(f);
            }
        }
        if (targetIsSet == false)
            ProjectBrowser.TheInstance.setTarget(getOwner());

    }

    /**
     * Updates the attributes in the fig. Called from modelchanged if there is 
     * a modelevent effecting the attributes and from renderingChanged in all 
     * cases.
     */
    protected void updateAttributes() {
        MClassifier cls = (MClassifier) getOwner();
        int xpos = _attrBigPort.getX();
        int ypos = _attrBigPort.getY();
        int acounter = 1;
        Collection strs = UmlHelper.getHelper().getCore().getAttributes(cls);
        if (strs != null) {
            Iterator iter = strs.iterator();
            Vector figs = _attrVec.getFigs();
            CompartmentFigText attr;
            while (iter.hasNext()) {
                MStructuralFeature sf = (MStructuralFeature) iter.next();
                // update the listeners
                UmlModelEventPump.getPump().removeModelEventListener(this, sf);
                UmlModelEventPump.getPump().addModelEventListener(this, sf);
                if (figs.size() <= acounter) {
                    attr =
                        new FigFeature(
                            xpos + 1,
                            ypos + 1 + (acounter - 1) * ROWHEIGHT,
                            0,
                            ROWHEIGHT - 2,
                            _attrBigPort);
                    // bounds not relevant here
                    attr.setFilled(false);
                    attr.setLineWidth(0);
                    attr.setFont(LABEL_FONT);
                    attr.setTextColor(Color.black);
                    attr.setJustification(FigText.JUSTIFY_LEFT);
                    attr.setMultiLine(false);
                    _attrVec.addFig(attr);
                } else {
                    attr = (CompartmentFigText) figs.elementAt(acounter);
                }
                attr.setText(Notation.generate(this, sf));
                attr.setOwner(sf);
                // underline, if static
                attr.setUnderline(
                    MScopeKind.CLASSIFIER.equals(sf.getOwnerScope()));
                acounter++;
            }
            if (figs.size() > acounter) {
                //cleanup of unused attribute FigText's
                for (int i = figs.size() - 1; i >= acounter; i--)
                    _attrVec.removeFig((Fig) figs.elementAt(i));
            }
        }
        Rectangle rect = getBounds();
        getUpdatedSize(_attrVec, xpos, ypos, 0, 0);
        // ouch ugly but that's for a next refactoring
        // TODO make setBounds, calcBounds and updateBounds consistent
        setBounds(rect.x, rect.y, rect.width, rect.height);
        damage();
    }

    /**
     * Updates the operations box. Called from modelchanged if there is 
     * a modelevent effecting the attributes and from renderingChanged in all 
     * cases.
     */
    protected void updateOperations() {
        MClassifier cls = (MClassifier) getOwner();
        int xpos = _operBigPort.getX();
        int ypos = _operBigPort.getY();
        int ocounter = 1;
        Collection behs = UmlHelper.getHelper().getCore().getOperations(cls);
        if (behs != null) {
            Iterator iter = behs.iterator();
            Vector figs = _operVec.getFigs();
            CompartmentFigText oper;
            while (iter.hasNext()) {
                MBehavioralFeature bf = (MBehavioralFeature) iter.next();
                // update the listeners
                UmlModelEventPump.getPump().removeModelEventListener(this, bf);
                UmlModelEventPump.getPump().addModelEventListener(this, bf);
                if (figs.size() <= ocounter) {
                    oper =
                        new FigFeature(
                            xpos + 1,
                            ypos + 1 + (ocounter - 1) * ROWHEIGHT,
                            0,
                            ROWHEIGHT - 2,
                            _operBigPort);
                    // bounds not relevant here
                    oper.setFilled(false);
                    oper.setLineWidth(0);
                    oper.setFont(LABEL_FONT);
                    oper.setTextColor(Color.black);
                    oper.setJustification(FigText.JUSTIFY_LEFT);
                    oper.setMultiLine(false);
                    _operVec.addFig(oper);
                } else {
                    oper = (CompartmentFigText) figs.elementAt(ocounter);
                }
                oper.setText(Notation.generate(this, bf));
                oper.setOwner(bf);
                // underline, if static
                oper.setUnderline(
                    MScopeKind.CLASSIFIER.equals(bf.getOwnerScope()));
                // italics, if abstract
                //oper.setItalic(((MOperation)bf).isAbstract()); // does not properly work (GEF bug?)
                if (((MOperation) bf).isAbstract())
                    oper.setFont(ITALIC_LABEL_FONT);
                else
                    oper.setFont(LABEL_FONT);
                ocounter++;
            }
            if (figs.size() > ocounter) {
                //cleanup of unused operation FigText's
                for (int i = figs.size() - 1; i >= ocounter; i--)
                    _operVec.removeFig((Fig) figs.elementAt(i));
            }
        }
        Rectangle rect = getBounds();
        getUpdatedSize(_operVec, xpos, ypos, 0, 0);
        // ouch ugly but that's for a next refactoring
        // TODO make setBounds, calcBounds and updateBounds consistent
        setBounds(rect.x, rect.y, rect.width, rect.height);
        damage();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        updateAttributes();
        updateOperations();
        updateAbstract();
        super.renderingChanged();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {
        Rectangle rect = getBounds();
        super.updateNameText();

        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Updates the name if modelchanged receives an "isAbstract" event
     */
    protected void updateAbstract() {
        Rectangle rect = getBounds();
        if (getOwner() == null)
            return;
        MClass cls = (MClass) getOwner();
        if (cls.isAbstract())
            _name.setFont(ITALIC_LABEL_FONT);
        else
            _name.setFont(LABEL_FONT);
        super.updateNameText();
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

} /* end class FigClass */
