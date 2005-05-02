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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.argouml.application.api.Notation;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ActionAddAttribute;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionAddOperation;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.ActionEdgesDisplay;
import org.argouml.uml.diagram.ui.AttributesCompartmentContainer;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.FigAttributesCompartment;
import org.argouml.uml.diagram.ui.FigCompartment;
import org.argouml.uml.diagram.ui.FigEmptyRect;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigOperationsCompartment;
import org.argouml.uml.diagram.ui.OperationsCompartmentContainer;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Class in a diagram.<p>
 */
public class FigClass extends FigNodeModelElement
        implements AttributesCompartmentContainer,
        OperationsCompartmentContainer {


    ////////////////////////////////////////////////////////////////
    // constants

    //These are the positions of child figs inside this fig
    //They mst be added in the constructor in this order.
    private static final int OPERATIONS_POSN = 3;
    private static final int ATTRIBUTES_POSN = 4;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * Text highlighted by mouse actions on the diagram.<p>
     */
    private CompartmentFigText highlightedFigText = null;

    /**
     * Flag to indicate that we have just been created. This is to fix the
     * problem with loading classes that have stereotypes already
     * defined.<p>
     */
    private boolean newlyCreated = false;

    /**
     * <p>Manages residency of a class within a component on a deployment
     *   diagram.</p>
     */
    private Object resident =
            Model.getCoreFactory().createElementResidence();

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main constructor for a {@link FigClass}.<p>
     *
     * Parent {@link FigNodeModelElement} will have created the main
     * box {@link #getBigPort()} and its name {@link #getNameFig()}
     * and stereotype (@link #getStereotypeFig()}. This constructor
     * creates a box for the attributes and operations.<p>
     *
     * The properties of all these graphic elements are adjusted
     * appropriately. The main boxes are all filled and have
     * outlines.<p>
     *
     * For reasons I don't understand the stereotype is created in a
     * box with lines. So we have to created a blanking rectangle to
     * overlay the bottom line, and avoid four compartments
     * showing.<p>
     *
     * There is some complex logic to allow for the possibility that
     * stereotypes may not be displayed (unlike operations and
     * attributes this is not a standard thing for UML). Some care is
     * needed to ensure that additional space is not added, each time
     * a stereotyped class is loaded.<p>
     *
     * There is a particular problem when loading diagrams with
     * stereotyped classes. Because we create a FigClass indicating
     * the stereotype is not displayed, we then add extra space for
     * such classes when they are first rendered. This ought to be
     * fixed by correctly saving the class dimensions, but that needs
     * more work. The solution here is to use a simple flag to
     * indicate the FigClass has just been created.<p>
     *
     * <em>Warning</em>. Much of the graphics positioning is hard
     * coded. The overall figure is placed at location (10,10). The
     * name compartment (in the parent {@link FigNodeModelElement} is
     * 21 pixels high. The stereotype compartment is created 15 pixels
     * high in the parent, but we change it to 19 pixels, 1 more than
     * ({@link #STEREOHEIGHT} here. The attribute and operations boxes
     * are created at 19 pixels, 2 more than {@link #ROWHEIGHT}.<p>
     *
     * CAUTION: This constructor (with no arguments) is the only one
     * that does enableSizeChecking(false), all others must set it
     * true.  This is because this constructor is the only one called
     * when loading a project. In this case, the parsed size must be
     * maintained.<p>
     */
    public FigClass() {

        // Set name box. Note the upper line will be blanked out if there is
        // eventually a stereotype above.
        getNameFig().setLineWidth(1);
        getNameFig().setFilled(true);

        // Attributes inside. First one is the attribute box itself.
        FigCompartment attributesFigCompartment =
            new FigAttributesCompartment(10, 30, 60, ROWHEIGHT + 2);

        // this rectangle marks the operation section; all operations
        // are inside it
        FigCompartment operationsFigCompartment =
            new FigOperationsCompartment(10, 31 + ROWHEIGHT, 60, ROWHEIGHT + 2);

        // Set properties of the stereotype box. Make it 1 pixel higher than
        // before, so it overlaps the name box, and the blanking takes out both
        // lines. Initially not set to be displayed, but this will be changed
        // when we try to render it, if we find we have a stereotype.
        getStereotypeFigText().setExpandOnly(true);
        getStereotypeFig().setFilled(true);
        getStereotypeFig().setLineWidth(1);
        getStereotypeFigText().setEditable(false);
        getStereotypeFig().setHeight(STEREOHEIGHT + 1);
        // +1 to have 1 pixel overlap with getNameFig()
        getStereotypeFig().setVisible(false);

        FigEmptyRect bigPort = new FigEmptyRect(10, 10, 0, 0);
        bigPort.setLineWidth(1);
        bigPort.setLineColor(Color.black);
        setBigPort(bigPort);

        // Mark this as newly created. This is to get round the problem with
        // creating figs for loaded classes that had stereotypes. They are
        // saved with their dimensions INCLUDING the stereotype, but since we
        // pretend the stereotype is not visible, we add height the first time
        // we render such a class. This is a complete fudge, and really we
        // ought to address how class objects with stereotypes are saved. But
        // that will be hard work.
        newlyCreated = true;

        // Put all the bits together, suppressing bounds calculations until
        // we're all done for efficiency.
        enableSizeChecking(false);
        setSuppressCalcBounds(true);
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(bigPort);
        addFig(operationsFigCompartment);
        addFig(attributesFigCompartment);

        setSuppressCalcBounds(false);
        // Set the bounds of the figure to the total of the above (hardcoded)
        setBounds(10, 10, 60, 22 + 2 * ROWHEIGHT);
    }

    /**
     * Constructor for use if this figure is created for an existing class
     * node in the metamodel.<p>
     *
     * Set the figure's name according to this node. This is used when the
     * user click's on 'add to diagram' in the navpane.  Don't know if this
     * should rather be done in one of the super classes, since similar code
     * is used in FigInterface.java etc.  Andreas Rueckert
     * &lt;a_rueckert@gmx.net&gt;<p>
     *
     * @param gm   Not actually used in the current implementation
     *
     * @param node The UML object being placed.
     */
    public FigClass(GraphModel gm, Object node) {
        this();
        enableSizeChecking(true);
        setOwner(node);
        if ((Model.getFacade().isAClassifier(node))
                && (Model.getFacade().getName(node) != null)) {
            setName(Model.getFacade().getName(node));
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new Class";
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionClass(this);
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on a Class.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     *
     * @see org.tigris.gef.ui.PopupGenerator#getPopUpActions(java.awt.event.MouseEvent)
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        // Add...
        ArgoJMenu addMenu = new ArgoJMenu("menu.popup.add");
        addMenu.add(new ActionAddAttribute());
        addMenu.add(new ActionAddOperation());
        addMenu.add(new ActionAddNote());
        popUpActions.insertElementAt(addMenu,
            popUpActions.size() - POPUP_ADD_OFFSET);

        // Show ...
        ArgoJMenu showMenu = new ArgoJMenu("menu.popup.show");
        if (isAttributesVisible() && isOperationsVisible()) {
            showMenu.add(ActionCompartmentDisplay.hideAllCompartments());
        } else if (!isAttributesVisible() && !isOperationsVisible()) {
            showMenu.add(ActionCompartmentDisplay.showAllCompartments());
        }
        if (isAttributesVisible()) {
            showMenu.add(ActionCompartmentDisplay.hideAttrCompartment());
        } else {
            showMenu.add(ActionCompartmentDisplay.showAttrCompartment());
        }
        if (isOperationsVisible()) {
            showMenu.add(ActionCompartmentDisplay.hideOperCompartment());
        } else {
            showMenu.add(ActionCompartmentDisplay.showOperCompartment());
        }
        showMenu.add(ActionEdgesDisplay.getShowEdges());
        showMenu.add(ActionEdgesDisplay.getHideEdges());
        popUpActions.insertElementAt(showMenu,
            popUpActions.size() - POPUP_ADD_OFFSET);

        // Modifiers ...
        popUpActions.insertElementAt(
                buildModifierPopUp(ABSTRACT | LEAF | ROOT | ACTIVE),
                popUpActions.size() - POPUP_ADD_OFFSET);

        // Visibility ...
        popUpActions.insertElementAt(buildVisibilityPopUp(),
                popUpActions.size() - POPUP_ADD_OFFSET);

        return popUpActions;
    }

    /**
     * @return The bounds of the operations compartment.
     */
    public Rectangle getOperationsBounds() {
        return ((FigGroup) getFigAt(OPERATIONS_POSN)).getBounds();
    }

    /**
     * @return The bounds of the attributes compartment.
     */
    public Rectangle getAttributesBounds() {
        return ((FigGroup) getFigAt(ATTRIBUTES_POSN)).getBounds();
    }

    /**
     * @return The vector of graphics for operations (if any).
     * First one is the rectangle for the entire operations box.
     */
    private FigOperationsCompartment getOperationsFig() {
        return (FigOperationsCompartment) getFigAt(OPERATIONS_POSN);
    }

    /**
     * @return The vector of graphics for operations (if any).
     * First one is the rectangle for the entire operations box.
     */
    private FigAttributesCompartment getAttributesFig() {
        return (FigAttributesCompartment) getFigAt(ATTRIBUTES_POSN);
    }

    /**
     * Returns the status of the operation field.
     * @return true if the operations are visible, false otherwise
     */
    public boolean isOperationsVisible() {
        return getOperationsFig().isVisible();
    }

    /**
     * Returns the status of the attribute field.
     * @return true if the attributes are visible, false otherwise
     */
    public boolean isAttributesVisible() {
        return getAttributesFig().isVisible();
    }

    /**
     * @param isVisible true if the attribute compartment is visible
     */
    public void setAttributesVisible(boolean isVisible) {
        Rectangle rect = getBounds();
        int h;
    	if (isCheckSize()) {
    	    h = ((ROWHEIGHT
                * Math.max(1, getAttributesFig().getFigs().size() - 1) + 2)
                * rect.height
                / getMinimumSize().height);
        } else {
            h = 0;
        }
        if (getAttributesFig().isVisible()) {
            if (!isVisible) {  // hide compartment
                damage();
                Iterator it = getAttributesFig().getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(false);
                }
                getAttributesFig().setVisible(false);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
			  (int) aSize.getWidth(), (int) aSize.getHeight());
            }
        } else {
            if (isVisible) { // show compartement
                Iterator it = getAttributesFig().getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(true);
                }
                getAttributesFig().setVisible(true);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
			  (int) aSize.getWidth(), (int) aSize.getHeight());
                damage();
            }
        }
    }

    /**
     * @param isVisible true if the operation compartment is visible
     */
    public void setOperationsVisible(boolean isVisible) {
        Rectangle rect = getBounds();
        int h =
    	    isCheckSize()
    	    ? ((ROWHEIGHT
                * Math.max(1, getOperationsFig().getFigs().size() - 1) + 2)
    	        * rect.height
    	        / getMinimumSize().height)
    	    : 0;
        if (isOperationsVisible()) { // if displayed
            if (!isVisible) {
                damage();
                Iterator it = getOperationsFig().getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(false);
                }
                getOperationsFig().setVisible(false);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
			  (int) aSize.getWidth(), (int) aSize.getHeight());
            }
        } else {
            if (isVisible) {
                Iterator it = getOperationsFig().getFigs().iterator();
                while (it.hasNext()) {
                    ((Fig) (it.next())).setVisible(true);
                }
                getOperationsFig().setVisible(true);
                Dimension aSize = this.getMinimumSize();
                setBounds(rect.x, rect.y,
                    (int) aSize.getWidth(), (int) aSize.getHeight());
                damage();
            }
        }
    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    public String classNameAndBounds() {
        return super.classNameAndBounds()
            + "operationsVisible=" + isOperationsVisible() + ";"
            + "attributesVisible=" + isAttributesVisible();
    }

    /**
     * Gets the minimum size permitted for a class on the diagram.<p>
     *
     * Parts of this are hardcoded, notably the fact that the name
     * compartment has a minimum height of 21 pixels.<p>
     *
     * @return  the size of the minimum bounding box.
     */
    public Dimension getMinimumSize() {

        // Use "aSize" to build up the minimum size. Start with the size of the
        // name compartment and build up.

        Dimension aSize = getNameFig().getMinimumSize();
        if (aSize.height < 21) {
            aSize.height = 21;
        }

        // If we have a stereotype displayed, then allow some space for that
        // (width and height)

        if (getStereotypeFig().isVisible()) {
            aSize.width =
		Math.max(aSize.width,
			 getStereotypeFig().getMinimumSize().width);
            aSize.height += STEREOHEIGHT;
        }

        // Allow space for each of the attributes we have

        if (getAttributesFig().isVisible()) {

            // Loop through all the attributes, to find the widest (remember
            // the first fig is the box for the whole lot, so ignore it).

            Iterator it = getAttributesFig().getFigs().iterator();
            it.next(); // Ignore first element

            while (it.hasNext()) {
                int elemWidth =
		    ((FigText) it.next()).getMinimumSize().width + 2;
                aSize.width = Math.max(aSize.width, elemWidth);
            }

            // Height allows one row for each attribute (remember to ignore the
            // first element.

            aSize.height +=
		ROWHEIGHT * Math.max(1,
		        getAttributesFig().getFigs().size() - 1) + 1;
        }

        // Allow space for each of the operations we have

        if (isOperationsVisible()) {

            // Loop through all the operations, to find the widest (remember
            // the first fig is the box for the whole lot, so ignore it).

            Iterator it = getOperationsFig().getFigs().iterator();
            it.next(); // ignore

            while (it.hasNext()) {
                int elemWidth =
		    ((FigText) it.next()).getMinimumSize().width + 2;
                aSize.width = Math.max(aSize.width, elemWidth);
            }

            aSize.height +=
		ROWHEIGHT * Math.max(1,
		        getOperationsFig().getFigs().size() - 1) + 1;
        }

        // we want to maintain a minimum width for the class
        aSize.width = Math.max(60, aSize.width);

        // And now aSize has the answer

        return aSize;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#translate(int, int)
     */
    public void translate(int dx, int dy) {
        super.translate(dx, dy);
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionClass) {
            ((SelectionClass) sel).hideButtons();
        }
    }

    ////////////////////////////////////////////////////////////////
    // user interaction methods

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me) {
        super.mouseExited(me);
        unhighlight();
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            CompartmentFigText ft = unhighlight();
            if (ft != null) {
                int i = new Vector(getAttributesFig().getFigs()).indexOf(ft);
                FigGroup fg = getAttributesFig();
                if (i == -1) {
                    i = new Vector(getOperationsFig().getFigs()).indexOf(ft);
                    fg = getOperationsFig();
                }
                if (i != -1) {
                    if (key == KeyEvent.VK_UP) {
                        ft = (CompartmentFigText)
			    getPreviousVisibleFeature(fg, ft, i);
                    } else {
                        ft = (CompartmentFigText)
			    getNextVisibleFeature(fg, ft, i);
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

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        Object cls = /*(MClassifier)*/ getOwner();
        if (cls == null) {
            return;
        }
        int i = new Vector(getAttributesFig().getFigs()).indexOf(ft);
        if (i != -1) {
            String msg = "statusmsg.bar.error.parsing.attribute";
            highlightedFigText = (CompartmentFigText) ft;
            highlightedFigText.setHighlighted(true);
            try {
                ParserDisplay.SINGLETON
		    .parseAttributeFig(cls,
				       /*(MAttribute)*/
				       highlightedFigText.getOwner(),
				       highlightedFigText.getText().trim());
                ProjectBrowser.getInstance().getStatusBar().showStatus("");
            } catch (ParseException pe) {
                Object[] args = {pe.getLocalizedMessage(), 
                    new Integer(pe.getErrorOffset())};
                ProjectBrowser.getInstance().getStatusBar().showStatus(
                        Translator.messageFormat(msg, args));
            }
            return;
        }
        i = new Vector(getOperationsFig().getFigs()).indexOf(ft);
        if (i != -1) {
            String msg = "statusmsg.bar.error.parsing.operation";
            highlightedFigText = (CompartmentFigText) ft;
            highlightedFigText.setHighlighted(true);
            try {
                ParserDisplay.SINGLETON
		    .parseOperationFig(cls,
				       /*(MOperation)*/
				       highlightedFigText.getOwner(),
				       highlightedFigText.getText().trim());
                ProjectBrowser.getInstance().getStatusBar().showStatus("");
            } catch (ParseException pe) {
                Object[] args = {pe.getLocalizedMessage(), 
                    new Integer(pe.getErrorOffset())};
                ProjectBrowser.getInstance().getStatusBar().showStatus(
                        Translator.messageFormat(msg, args));
            }
            return;
        }
    }

    /**
     * @param fgVec the FigGroup
     * @param ft    the Figtext
     * @param i     get the fig before fig i
     * @return the FigText
     */
    protected FigText getPreviousVisibleFeature(FigGroup fgVec,
						FigText ft, int i) {
        if (fgVec == null || i < 1) {
            return null;
        }
        FigText ft2 = null;
        // TODO: come GEF V 0.12 use getFigs returning an array
        Vector v = new Vector(fgVec.getFigs());
        if (i >= v.size() || !((FigText) v.elementAt(i)).isVisible()) {
            return null;
        }
        do {
            i--;
            while (i < 1) {
                if (fgVec == getAttributesFig()) {
                    fgVec = getOperationsFig();
                } else {
                    fgVec = getAttributesFig();
                }
                v = new Vector(fgVec.getFigs());
                i = v.size() - 1;
            }
            ft2 = (FigText) v.elementAt(i);
            if (!ft2.isVisible()) {
                ft2 = null;
            }
        } while (ft2 == null);
        return ft2;
    }

    /**
     * @param fgVec the FigGroup
     * @param ft    the Figtext
     * @param i     get the fig after fig i
     * @return the FigText
     */
    protected FigText getNextVisibleFeature(FigGroup fgVec, FigText ft, int i) {
        if (fgVec == null || i < 1) {
            return null;
        }
        FigText ft2 = null;
        // TODO: come GEF V 0.12 use getFigs returning an array
        Vector v = new Vector(fgVec.getFigs());
        if (i >= v.size() || !((FigText) v.elementAt(i)).isVisible()) {
            return null;
        }
        do {
            i++;
            while (i >= v.size()) {
                if (fgVec == getAttributesFig()) {
                    fgVec = getOperationsFig();
                } else {
                    fgVec = getAttributesFig();
                }
                v = new Vector(fgVec.getFigs());
                i = 1;
            }
            ft2 = (FigText) v.elementAt(i);
            if (!ft2.isVisible()) {
                ft2 = null;
            }
        } while (ft2 == null);
        return ft2;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#createFeatureIn(
     * org.tigris.gef.presentation.FigGroup, java.awt.event.InputEvent)
     */
    protected void createFeatureIn(FigGroup fg, InputEvent ie) {
        CompartmentFigText ft = null;
        Object cls = /*(MClassifier)*/ getOwner();
        if (cls == null) {
            return;
        }
        if (fg == getAttributesFig()) {
            (new ActionAddAttribute()).actionPerformed(null);
        } else {
            (new ActionAddOperation()).actionPerformed(null);
        }
        // TODO: When available use getFigs() returning array
        ft = (CompartmentFigText) new Vector(fg.getFigs()).lastElement();
        if (ft != null) {
            ft.startTextEditor(ie);
            ft.setHighlighted(true);
            highlightedFigText = ft;
        }
        ie.consume();
    }

    /**
     * @return the compartment
     */
    protected CompartmentFigText unhighlight() {
        CompartmentFigText ft;
        // TODO: in future version of GEF call getFigs returning array
        Vector v = new Vector(getAttributesFig().getFigs());
        int i;
        for (i = 1; i < v.size(); i++) {
            ft = (CompartmentFigText) v.elementAt(i);
            if (ft.isHighlighted()) {
                ft.setHighlighted(false);
                highlightedFigText = null;
                return ft;
            }
        }
        // TODO: in future version of GEF call getFigs returning array
        v = new Vector(getOperationsFig().getFigs());
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
     * Handles changes of the model. Takes into account the event that
     * occured. If you need to update the whole fig, consider using
     * renderingChanged.
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        if (getOwner() == null) {
            return;
        }

        // attributes
        if (mee == null
                || Model.getFacade().isAAttribute(mee.getSource())
                || (mee.getSource() == getOwner()
		&& mee.getPropertyName().equals("feature"))) {
            updateAttributes();
            damage();
        }
        // operations
        if (mee == null
                || Model.getFacade().isAOperation(mee.getSource())
                || Model.getFacade().isAParameter(mee.getSource())
                || (mee.getSource() == getOwner()
                        && mee.getPropertyName().equals("feature"))) {
            updateOperations();
            damage();
        }
        if (mee != null && mee.getPropertyName().equals("parameter")
                && Model.getFacade().isAOperation(mee.getSource())) {
            /* Copy the lists, since we will alter them below. */
            ArrayList oldP = new ArrayList((List) mee.getOldValue());
            ArrayList newP = new ArrayList((List) mee.getNewValue());
            if (oldP.size() != newP.size()) {
                if (newP.containsAll(oldP)) {
                    // the list grew bigger somehow... (parameter added)
                    newP.removeAll(oldP);
                    /* Ensure we will get an event for the name change of
                     * the newly created attribute: */
                    Model.getPump().addModelEventListener(this, newP.get(0),
                        new String[] {"name", "kind", "type", "defaultValue"});
                } else {
                    // the list shrunk somehow... (parameter removed)
                    oldP.removeAll(newP);
                    Model.getPump().removeModelEventListener(this, oldP.get(0));
                }
            }
        }
        if (mee == null || mee.getPropertyName().equals("isAbstract")) {
            updateAbstract();
            damage();
        }
        if (mee == null || mee.getPropertyName().equals("stereotype")) {
            updateStereotypeText();
            updateAttributes();
            updateOperations();
            damage();
        }
        if (mee != null && Model.getFacade().getStereotypes(getOwner())
                                .contains(mee.getSource())) {
            updateStereotypeText();
            damage();
        }
        // name updating
        super.modelChanged(mee);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {

        Object me = /*(MModelElement)*/ getOwner();

        if (me == null) {
            return;
        }

        Rectangle rect = getBounds();
        Object stereo = null;
        if (Model.getFacade().getStereotypes(me).size() > 0) {
            stereo = Model.getFacade().getStereotypes(me).iterator().next();
        }

        if ((stereo == null)
                || (Model.getFacade().getName(stereo) == null)
                || (Model.getFacade().getName(stereo).length() == 0))	{

            if (getStereotypeFig().isVisible()) {
                getStereotypeFig().setVisible(false);
                rect.y += STEREOHEIGHT;
                rect.height -= STEREOHEIGHT;
                setBounds(rect.x, rect.y, rect.width, rect.height);
                calcBounds();
            }
        } else {
            setStereotype(Notation.generateStereotype(this, stereo));

            if (!getStereotypeFig().isVisible()) {
                getStereotypeFig().setVisible(true);

                // Only adjust the stereotype height if we are not newly
                // created. This gets round the problem of loading classes with
                // stereotypes defined, which have the height already including
                // the stereotype.

                if (!newlyCreated) {
                    rect.y -= STEREOHEIGHT;
                    rect.height += STEREOHEIGHT;
                    setBounds(rect.x, rect.y, rect.width, rect.height);
                    calcBounds();
                }
            }
        }

        // Whatever happened we are no longer newly created, so clear the
        // flag. Then set the bounds for the rectangle we have defined.
        newlyCreated = false;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig encloser) {
        if (encloser == null
                || (encloser != null
                && !Model.getFacade().isAInstance(encloser.getOwner()))) {
            super.setEnclosingFig(encloser);
        }
        if (!(Model.getFacade().isAModelElement(getOwner())))
            return;
        if (encloser != null
                && (Model.getFacade().isAComponent(encloser.getOwner()))) {
            Object component = /*(MComponent)*/ encloser.getOwner();
            Object in = /*(MInterface)*/ getOwner();
            Model.getCoreHelper()
                    .setImplementationLocation(resident, component);
            Model.getCoreHelper().setResident(resident, in);
        } else {
            Model.getCoreHelper().setImplementationLocation(resident, null);
            Model.getCoreHelper().setResident(resident, null);
        }

    }

    /**
     * Sets the bounds, but the size will be at least the one returned by
     * {@link #getMinimumSize()}, unless checking of size is disabled.<p>
     *
     * If the required height is bigger, then the additional height is
     * equally distributed among all figs (i.e. compartments), such that the
     * cumulated height of all visible figs equals the demanded height<p>.
     *
     * Some of this has "magic numbers" hardcoded in. In particular there is
     * a knowledge that the minimum height of a name compartment is 21
     * pixels.<p>
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

        getNameFig().setLineWidth(0);
        getNameFig().setLineColor(Color.red);
        
        int stereotypeHeight = 0;
        if (getStereotypeFig().isVisible()) {
            stereotypeHeight = STEREOHEIGHT;
        }
        
        Rectangle oldBounds = getBounds();
        Dimension aSize =
            isCheckSize() ? getMinimumSize() : new Dimension(w, h);

        int newW = Math.max(w, aSize.width);
        int newH = h;

        int extraEach = 0;
        int heightCorrection = 0;

        // First compute all nessessary height data. Easy if we want less than
        // the minimum

        int displayedFigs = 1; //this is for getNameFig()

        if (getAttributesFig().isVisible()) {
            displayedFigs++;
        }

        if (isOperationsVisible()) {
            displayedFigs++;
        }

        if (newH <= aSize.height) {

            // Just use the mimimum

            newH = aSize.height;

        } else {
            // Calculate how much each, plus a correction to put in the name
            // comparment if the result is rounded
            extraEach = (newH - aSize.height) / displayedFigs;
            heightCorrection =
                (newH - aSize.height) - (extraEach * displayedFigs);
        }

        // Now resize all sub-figs, including not displayed figs. Start by the
        // name. We override the getMinimumSize if it is less than our view (21
        // pixels hardcoded!). Add in the shared extra, plus in this case the
        // correction.

        int height = getNameFig().getMinimumSize().height;

        if (height < 21) {
            height = 21;
        }

        height += extraEach + heightCorrection;

        // Now sort out the stereotype display. If the stereotype is displayed,
        // move the upper boundary of the name compartment up and set new
        // bounds for the name and the stereotype compatments and the
        // stereoLineBlinder that blanks out the line between the two

        int currentY = y;

        currentY += stereotypeHeight;

        getStereotypeFig().setBounds(x, y, newW, STEREOHEIGHT + 1);

        if (displayedFigs == 1) {
            height = newH;
            getNameFig().setBounds(x, y + stereotypeHeight,
                                   newW, height - stereotypeHeight);
        } else {
            getNameFig().setBounds(x, y + stereotypeHeight, newW, height);
        }

        // Advance currentY to where the start of the attribute box is,
        // remembering that it overlaps the next box by 1 pixel. Calculate the
        // size of the attribute box, and update the Y pointer past it if it is
        // displayed.

        currentY += height - 1; // -1 for 1 pixel overlap

        int attributeCount = (isAttributesVisible())
    	    ? Math.max(1, getAttributesFig().getFigs().size() - 1)
    	    : 0;
        int operationCount = (isOperationsVisible())
    	    ? Math.max(1, getOperationsFig().getFigs().size() - 1)
    	    : 0;
        if (isCheckSize()) {
            height = ROWHEIGHT * attributeCount + 2 + extraEach;
        } else if (newH > currentY - y && attributeCount + operationCount > 0) {
            height = (newH + y - currentY) * attributeCount
                        / (attributeCount + operationCount) + 1;
        } else {
            height = 1;
        }
        aSize = updateFigGroupSize(getAttributesFig(), x, currentY,
                newW, height);

        if (getAttributesFig().isVisible()) {
            currentY += aSize.height - 1; // -1 for 1 pixel overlap
        }

        // Finally update the bounds of the operations box

        aSize =
	    updateFigGroupSize(getOperationsFig(), x, currentY,
                newW, newH + y - currentY);

        // set bounds of big box

        getBigPort().setBounds(x, y, newW, newH);

        // Now force calculation of the bounds of the figure, update the edges
        // and trigger anyone who's listening to see if the "bounds" property
        // has changed.

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }



    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {

        if (me.isConsumed()) {
            return;
        }
        super.mouseClicked(me);
	if (me.isShiftDown()
                && TargetManager.getInstance().getTargets().size() > 0) {
	    return;
        }

        int i = 0;
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionClass) {
	    ((SelectionClass) sel).hideButtons();
        }
        unhighlight();
        //display attr/op properties if necessary:
        Rectangle r = new Rectangle(me.getX() - 1, me.getY() - 1, 2, 2);
        Fig f = hitFig(r);
        if (f == getAttributesFig() && getAttributesFig().getHeight() > 0) {
            // TODO: in future version of GEF call getFigs returning array
            Vector v = new Vector(getAttributesFig().getFigs());
            i = (v.size() - 1) * (me.getY() - f.getY() - 3)
                / getAttributesFig().getHeight();
            if (i >= 0 && i < v.size() - 1) {
                f = (Fig) v.elementAt(i + 1);
                ((CompartmentFigText) f).setHighlighted(true);
                highlightedFigText = (CompartmentFigText) f;
                TargetManager.getInstance().setTarget(f);
            }
        } else if (f == getOperationsFig()
                     && getOperationsFig().getHeight() > 0) {
            // TODO: in future version of GEF call getFigs returning array
            Vector v = new Vector(getOperationsFig().getFigs());
            i = (v.size() - 1) * (me.getY() - f.getY() - 3)
                / getOperationsFig().getHeight();
            if (i >= 0 && i < v.size() - 1) {
                f = (Fig) v.elementAt(i + 1);
                ((CompartmentFigText) f).setHighlighted(true);
                highlightedFigText = (CompartmentFigText) f;
                TargetManager.getInstance().setTarget(f);
            }
        }
    }
//    /**
//     * Updates the attributes in the fig. Called from modelchanged if there is
//     * a modelevent effecting the attributes and from renderingChanged in all
//     * cases.
//     */
//    protected void updateAttributes() {
//        Object cls = /*(MClassifier)*/ getOwner();
//        Fig attrPort = ((FigAttributesCompartment) getFigAt(ATTRIBUTES_POSN))
//            .getBigPort();
//        int xpos = attrPort.getX();
//        int ypos = attrPort.getY();
//        int acounter = 1;
//        Collection strs = Model.getFacade().getStructuralFeatures(cls);
//        if (strs != null) {
//            Iterator iter = strs.iterator();
//            // TODO: in future version of GEF call getFigs returning array
//            Vector figs = new Vector(getAttributesFig().getFigs());
//            CompartmentFigText attr;
//            while (iter.hasNext()) {
//                Object sf = /*(MStructuralFeature)*/ iter.next();
//                // update the listeners
//		// Model.getPump().removeModelEventListener(this, sf);
//                // Model.getPump().addModelEventListener(this, sf); //??
//                if (figs.size() <= acounter) {
//                    attr =
//			new FigFeature(xpos + 1,
//				       ypos + 1 + (acounter - 1) * ROWHEIGHT,
//				       0,
//				       ROWHEIGHT - 2,
//				       attrPort);
//                    // bounds not relevant here
//                    attr.setFilled(false);
//                    attr.setLineWidth(0);
//                    attr.setFont(getLabelFont());
//                    attr.setTextColor(Color.black);
//                    attr.setJustification(FigText.JUSTIFY_LEFT);
//                    attr.setMultiLine(false);
//                    getAttributesFig().addFig(attr);
//                } else {
//                    attr = (CompartmentFigText) figs.elementAt(acounter);
//                }
//                attr.setText(Notation.generate(this, sf));
//                attr.setOwner(sf); //TODO: update the model again here?
//                /* This causes another event, and modelChanged() called,
//                 * and updateAttributes() called again...
//                 */
//
//                // underline, if static
//                attr.setUnderline(Model.getScopeKind().getClassifier()
//				  .equals(Model.getFacade().getOwnerScope(sf)));
//                acounter++;
//            }
//            if (figs.size() > acounter) {
//                //cleanup of unused attribute FigText's
//                for (int i = figs.size() - 1; i >= acounter; i--) {
//                    getAttributesFig().removeFig((Fig) figs.elementAt(i));
//                }
//            }
//        }
//        Rectangle rect = getBounds();
//        updateFigGroupSize(getAttributesFig(), xpos, ypos, 0, 0);
//        // ouch ugly but that's for a next refactoring
//        // TODO: make setBounds, calcBounds and updateBounds consistent
//        setBounds(rect.x, rect.y, rect.width, rect.height);
//        damage();
//    }

    /**
     * Updates the attributes in the fig. Called from modelchanged if there is
     * a modelevent effecting the attributes and from renderingChanged in all
     * cases.
     */
    protected void updateAttributes() {
        if (!isAttributesVisible()) {
            return;
        }
        FigAttributesCompartment attributesCompartment = (FigAttributesCompartment) getAttributesFig();
        attributesCompartment.populate();
        Fig attrPort = attributesCompartment.getBigPort();
        int xpos = attrPort.getX();
        int ypos = attrPort.getY();
        
        Rectangle rect = getBounds();
        updateFigGroupSize(getAttributesFig(), xpos, ypos, 0, 0);
        // ouch ugly but that's for a next refactoring
        // TODO: make setBounds, calcBounds and updateBounds consistent
        setBounds(rect.x, rect.y, rect.width, rect.height);
        damage();
    }

//    /**
//     * Updates the operations box. Called from modelchanged if there is
//     * a modelevent effecting the attributes and from renderingChanged in all
//     * cases.
//     */
//    protected void updateOperations() {
//        Object cls = /*(MClassifier)*/ getOwner();
//        Fig operPort = ((FigOperationsCompartment) getFigAt(OPERATIONS_POSN))
//            .getBigPort();
//
//        int xpos = operPort.getX();
//        int ypos = operPort.getY();
//        int ocounter = 1;
//        Collection behs = Model.getFacade().getOperations(cls);
//        if (behs != null) {
//            Iterator iter = behs.iterator();
//            // TODO: in future version of GEF call getFigs returning array
//            Vector figs = new Vector(getOperationsFig().getFigs());
//            CompartmentFigText oper;
//            while (iter.hasNext()) {
//                Object bf = /*(MBehavioralFeature)*/ iter.next();
//                // update the listeners
//		// Model.getPump().removeModelEventListener(this, bf);
//                // Model.getPump().addModelEventListener(this, bf);
//                if (figs.size() <= ocounter) {
//                    oper =
//                        new FigFeature(xpos + 1,
//                        ypos + 1 + (ocounter - 1) * ROWHEIGHT,
//                        0,
//                        ROWHEIGHT - 2,
//                        operPort);
//                    // bounds not relevant here
//                    oper.setFilled(false);
//                    oper.setLineWidth(0);
//                    oper.setFont(getLabelFont());
//                    oper.setTextColor(Color.black);
//                    oper.setJustification(FigText.JUSTIFY_LEFT);
//                    oper.setMultiLine(false);
//                    getOperationsFig().addFig(oper);
//                } else {
//                    oper = (CompartmentFigText) figs.elementAt(ocounter);
//                }
//                oper.setText(Notation.generate(this, bf));
//                oper.setOwner(bf); //TODO: update the model again here?
//                /* This causes another event, and modelChanged() called,
//                 * and updateOperations() called again...
//                 */
//
//                // underline, if static
//                oper.setUnderline(Model.getScopeKind().getClassifier()
//				  .equals(Model.getFacade().getOwnerScope(bf)));
//                // italics, if abstract
//                //oper.setItalic(((MOperation)bf).isAbstract()); //
//                //does not properly work (GEF bug?)
//                if (Model.getFacade().isAbstract(bf)) {
//                    oper.setFont(getItalicLabelFont());
//                } else {
//                    oper.setFont(getLabelFont());
//                }
//                oper.damage();
//                ocounter++;
//            }
//            if (figs.size() > ocounter) {
//                //cleanup of unused operation FigText's
//                for (int i = figs.size() - 1; i >= ocounter; i--) {
//                    getOperationsFig().removeFig((Fig) figs.elementAt(i));
//                }
//            }
//        }
//        Rectangle rect = getBounds();
//        updateFigGroupSize(getOperationsFig(), xpos, ypos, 0, 0);
//        // ouch ugly but that's for a next refactoring
//        // TODO: make setBounds, calcBounds and updateBounds consistent
//        setBounds(rect.x, rect.y, rect.width, rect.height);
//        damage();
//    }

    /**
     * Updates the operations box. Called from modelchanged if there is
     * a modelevent effecting the attributes and from renderingChanged in all
     * cases.
     */
    protected void updateOperations() {
        if (!isOperationsVisible()) {
            return;
        }
        FigOperationsCompartment operationsCompartment = ((FigOperationsCompartment) getFigAt(OPERATIONS_POSN));
        operationsCompartment.populate();
        Fig operPort = operationsCompartment.getBigPort();

        int xpos = operPort.getX();
        int ypos = operPort.getY();

        Rectangle rect = getBounds();
        updateFigGroupSize(getOperationsFig(), xpos, ypos, 0, 0);
        // ouch ugly but that's for a next refactoring
        // TODO: make setBounds, calcBounds and updateBounds consistent
        setBounds(rect.x, rect.y, rect.width, rect.height);
        damage();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        if (getOwner() != null) {
            updateAttributes();
            updateOperations();
            updateAbstract();
        }
        super.renderingChanged();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateNameText()
     */
    protected void updateNameText() {

        super.updateNameText();
        calcBounds();
        setBounds(getBounds());

        // setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Updates the name if modelchanged receives an "isAbstract" event.
     */
    protected void updateAbstract() {
        Rectangle rect = getBounds();
        if (getOwner() == null) {
            return;
        }
        Object cls = /*(MClass)*/ getOwner();
        if (Model.getFacade().isAbstract(cls)) {
            getNameFig().setFont(getItalicLabelFont());
	} else {
            getNameFig().setFont(getLabelFont());
	}
        super.updateNameText();
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * @see FigNodeModelElement#updateListeners(Object)
     */
    protected void updateListeners(Object newOwner) {
        Object oldOwner = getOwner();
        if (oldOwner != null && oldOwner != newOwner) {
	    // remove the listeners if the owner is changed
            Object cl = /*(MClass)*/ oldOwner;
            Iterator it = Model.getFacade().getFeatures(cl).iterator();
            while (it.hasNext()) {
                Object feat = /*(MFeature)*/ it.next();
                Model.getPump().removeModelEventListener(this, feat); //MVW
                if (Model.getFacade().isAOperation(feat)) {
                    Object oper = /*(MOperation)*/ feat;
                    Iterator it2 = 
                        Model.getFacade().getParameters(oper).iterator();
                    while (it2.hasNext()) {
                        Object param = /*(MParameter)*/ it2.next();
                        Model.getPump()
			    .removeModelEventListener(this, param);
                    }
                }
            }
        }
        if (newOwner != null) { // add the listeners to the newOwner
            Object cl = /*(MClass)*/ newOwner;
            Iterator it = Model.getFacade().getFeatures(cl).iterator();
            while (it.hasNext()) {
                Object feat = /*(MFeature)*/ it.next();
                Model.getPump().addModelEventListener(this, feat); //MVW
                if (Model.getFacade().isAOperation(feat)) {
                    Object oper = /*(MOperation)*/ feat;
                    Iterator it2 = 
                        Model.getFacade().getParameters(oper).iterator();
                    while (it2.hasNext()) {
                        Object param = /*(MParameter)*/ it2.next();
                        // UmlModelEventPump.getPump()
                        // .removeModelEventListener(this, param);
                        Model.getPump()
			    .addModelEventListener(this, param);
                    }
                }
            }
        }

        super.updateListeners(newOwner);
    }

} /* end class FigClass */
