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
import java.util.Iterator;
import java.util.Vector;

import org.argouml.application.api.Notation;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.FigAttributesCompartment;
import org.argouml.uml.diagram.ui.FigCompartment;
import org.argouml.uml.diagram.ui.FigEmptyRect;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionAddAttribute;
import org.argouml.uml.diagram.ui.FigOperationsCompartment;
import org.argouml.uml.diagram.ui.HasAttributesCompartment;
import org.argouml.uml.diagram.ui.HasOperationsCompartment;
import org.argouml.uml.generator.ParserDisplay;
import org.argouml.uml.diagram.ui.ActionAddOperation;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.ActionEdgesDisplay;
import org.argouml.uml.diagram.ui.ActionModifier;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;

/**
 * <p>Class to display graphics for a UML Class in a diagram.</p>
 */

public class FigClass extends FigNodeModelElement 
        implements HasAttributesCompartment, HasOperationsCompartment{


    ////////////////////////////////////////////////////////////////
    // constants

    ////////////////////////////////////////////////////////////////
    // instance variables

    //These are the positions of child figs inside this fig
    //They mst be added in the constructor in this order.
    private static final int BLINDER_POSN = 3;
    private static final int OPERATIONS_POSN = 4;
    private static final int ATTRIBUTES_POSN = 5;
    
    /**
     * <p>Manages residency of a class within a component on a deployment
     *   diagram. Not clear why it is public, or even why it is an instance
     *   variable (rather than local to the method).</p>
     *
     * not currently used. should be lazily instantiated in future.
     */
//    public Object resident =
//	UmlFactory.getFactory().getCore().createElementResidence();

    /**
     * <p>Text highlighted by mouse actions on the diagram.</p>
     */
    private CompartmentFigText highlightedFigText = null;

    /**
     * <p>Flag to indicate that we have just been created. This is to fix the
     *   problem with loading classes that have stereotypes already
     *   defined.</p>
     */
    private boolean newlyCreated = false;

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
        FigCompartment attrVec = 
            new FigAttributesCompartment(10, 30, 60, ROWHEIGHT + 2);

        // this rectangle marks the operation section; all operations
        // are inside it
        FigCompartment operVec = 
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

        // A thin rectangle to overlap the boundary line between stereotype
        // and name. This is just 2 pixels high, and we rely on the line
        // thickness, so the rectangle does not need to be filled. Whether to
        // display is linked to whether to display the stereotype.
        FigRect stereoLineBlinder =
	    new FigRect(11, 10 + STEREOHEIGHT, 58, 2,
			Color.white, Color.white);
        stereoLineBlinder.setLineWidth(1);
        stereoLineBlinder.setVisible(false);

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
        addFig(stereoLineBlinder);
        addFig(operVec);
        addFig(attrVec);

        setSuppressCalcBounds(false);
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
        if ((ModelFacade.isAClassifier(node))
                && (ModelFacade.getName(node) != null)) {
            setName(ModelFacade.getName(node));
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
     * popup menu on a Package.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        ArgoJMenu addMenu = new ArgoJMenu(BUNDLE, "menu.popup.add");
        addMenu.add(ActionAddAttribute.getSingleton());
        addMenu.add(ActionAddOperation.getSingleton());
        addMenu.add(ActionAddNote.getSingleton());
        popUpActions.insertElementAt(addMenu,
            popUpActions.size() - POPUP_ADD_OFFSET);

        ArgoJMenu showMenu = new ArgoJMenu(BUNDLE, "menu.popup.show");
        if (isAttributesVisible() && isOperationsVisible()) {
            showMenu.add(ActionCompartmentDisplay.HideAllCompartments);
        } else if (!isAttributesVisible() && !isOperationsVisible()) {
            showMenu.add(ActionCompartmentDisplay.ShowAllCompartments);
        }

        if (isAttributesVisible()) {
            showMenu.add(ActionCompartmentDisplay.HideAttrCompartment);
        } else {
            showMenu.add(ActionCompartmentDisplay.ShowAttrCompartment);
        }

        if (isOperationsVisible()) {
            showMenu.add(ActionCompartmentDisplay.HideOperCompartment);
        } else {
            showMenu.add(ActionCompartmentDisplay.ShowOperCompartment);
        }

        showMenu.add(ActionEdgesDisplay.getShowEdges());
        showMenu.add(ActionEdgesDisplay.getHideEdges());

        popUpActions.insertElementAt(showMenu,
            popUpActions.size() - POPUP_ADD_OFFSET);

        Object mclass = /*(MClass)*/ getOwner();
        ArgoJMenu modifierMenu = new ArgoJMenu(BUNDLE, "menu.popup.modifiers");

        modifierMenu.addCheckItem(
		new ActionModifier("Public",
				   "visibility", "getVisibility",
				   "setVisibility",
				   /*(MClass)*/ mclass,
				   (Class) ModelFacade.VISIBILITYKIND,
				   ModelFacade.PUBLIC_VISIBILITYKIND,
				   null));
        modifierMenu.addCheckItem(
		new ActionModifier("Abstract",
				   "isAbstract", "isAbstract", "setAbstract",
				   mclass));
        modifierMenu.addCheckItem(
		new ActionModifier("Leaf",
				   "isLeaf", "isLeaf", "setLeaf", mclass));
        modifierMenu.addCheckItem(
		new ActionModifier("Root",
				   "isRoot", "isRoot", "setRoot", mclass));
        modifierMenu.addCheckItem(
		new ActionModifier("Active",
				   "isActive", "isActive", "setActive",
				   mclass));

        popUpActions.insertElementAt(modifierMenu,
            popUpActions.size() - POPUP_ADD_OFFSET);
        // end of block

        return popUpActions;
    }

    /**
     * @return The vector of graphics for operations (if any). 
     * First one is the rectangle for the entire operations box.
     */
    public FigGroup getOperationsFig() {
        return (FigGroup) getFigAt(OPERATIONS_POSN);
    }
    
    /**
     * @return The vector of graphics for operations (if any). 
     * First one is the rectangle for the entire operations box.
     */
    public FigGroup getAttributesFig() {
        return (FigGroup) getFigAt(ATTRIBUTES_POSN);
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
                * Math.max(1, getAttributesFig().getFigs(null).size() - 1) + 2)
                * rect.height
                / getMinimumSize().height);
        } else {
            h = 0;
        }
        if (getAttributesFig().isVisible()) {
            if (!isVisible) {  // hide compartment
                damage();
                Iterator it = getAttributesFig().getFigs(null).iterator();
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
                Iterator it = getAttributesFig().getFigs(null).iterator();
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
                * Math.max(1, getOperationsFig().getFigs(null).size() - 1) + 2)
    	        * rect.height
    	        / getMinimumSize().height)
    	    : 0;
        if (isOperationsVisible()) { // if displayed
            if (!isVisible) {
                damage();
                Iterator it = getOperationsFig().getFigs(null).iterator();
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
                Iterator it = getOperationsFig().getFigs(null).iterator();
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

        Dimension aSize = getNameFig().getMinimumSize();
        int h = aSize.height;
        int w = aSize.width;

        // Ensure that the minimum height of the name compartment is at least
        // 21 pixels (hardcoded).

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

            Iterator it = getAttributesFig().getFigs(null).iterator();
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
		        getAttributesFig().getFigs(null).size() - 1) + 1;
        }

        // Allow space for each of the operations we have

        if (isOperationsVisible()) {

            // Loop through all the operations, to find the widest (remember
            // the first fig is the box for the whole lot, so ignore it).

            Iterator it = getOperationsFig().getFigs(null).iterator();
            it.next(); // ignore

            while (it.hasNext()) {
                int elemWidth =
		    ((FigText) it.next()).getMinimumSize().width + 2;
                aSize.width = Math.max(aSize.width, elemWidth);
            }

            aSize.height +=
		ROWHEIGHT * Math.max(1, 
		        getOperationsFig().getFigs(null).size() - 1) + 1;
        }

        // we want to maintain a minimum width for the class
        aSize.width = Math.max(60, aSize.width);

        // And now aSize has the answer

        return aSize;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color lColor) {
        super.setFillColor(lColor);
        getFigAt(BLINDER_POSN).setLineColor(lColor);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color lColor) {
        super.setLineColor(lColor);
        getFigAt(BLINDER_POSN)
            .setLineColor(getFigAt(BLINDER_POSN).getFillColor());
    }

    /**
     * @see org.tigris.gef.presentation.Fig#translate(int, int)
     */
    public void translate(int dx, int dy) {
        super.translate(dx, dy);
        Editor ce = Globals.curEditor();
        Selection sel = ce.getSelectionManager().findSelectionFor(this);
        if (sel instanceof SelectionClass)
	    ((SelectionClass) sel).hideButtons();
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
                int i = new Vector(getAttributesFig().getFigs(null))
                                            .indexOf(ft);
                FigGroup fg = getAttributesFig();
                if (i == -1) {
                    i = new Vector(getOperationsFig().getFigs(null))
                                            .indexOf(ft);
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
        int i = new Vector(getAttributesFig().getFigs(null)).indexOf(ft);
        if (i != -1) {
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
                ProjectBrowser.getInstance().getStatusBar()
		    .showStatus("Error: " + pe + " at " + pe.getErrorOffset());
            }
            return;
        }
        i = new Vector(getOperationsFig().getFigs(null)).indexOf(ft);
        if (i != -1) {
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
                ProjectBrowser.getInstance().getStatusBar()
		    .showStatus("Error: " + pe + " at " + pe.getErrorOffset());
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
        Vector v = new Vector(fgVec.getFigs(null));
        if (i >= v.size() || !((FigText) v.elementAt(i)).isVisible()) {
            return null;
        }
        do {
            i--;
            while (i < 1) {
                fgVec = (fgVec == getAttributesFig()) 
                    ? getOperationsFig() 
                    : getAttributesFig();
                v = new Vector(fgVec.getFigs(null));
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
    protected FigText getNextVisibleFeature(FigGroup fgVec, FigText ft, int i)
    {
        if (fgVec == null || i < 1) {
            return null;
        }
        FigText ft2 = null;
        // TODO: come GEF V 0.12 use getFigs returning an array
        Vector v = new Vector(fgVec.getFigs(null));
        if (i >= v.size() || !((FigText) v.elementAt(i)).isVisible()) {
            return null;
        }
        do {
            i++;
            while (i >= v.size()) {
                fgVec = (fgVec == getAttributesFig()) 
                    ? getOperationsFig() 
                    : getAttributesFig();
                v = new Vector(fgVec.getFigs(null));
                i = 1;
            }
            ft2 = (FigText) v.elementAt(i);
            if (!ft2.isVisible())
                ft2 = null;
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
            ActionAddAttribute.getSingleton().actionPerformed(null);
        } else {
            ActionAddOperation.getSingleton().actionPerformed(null);
        }
        // TODO: When available use getFigs() returning array
        ft = (CompartmentFigText) new Vector(fg.getFigs(null)).lastElement();
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
        Vector v = new Vector(getAttributesFig().getFigs(null));
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
        v = new Vector(getOperationsFig().getFigs(null));
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
     * renderingChanged.<p>
     *
     * @see FigNodeModelElement#modelChanged(MElementEvent)
     */
    protected void modelChanged(MElementEvent mee) {
        super.modelChanged(mee);

        if (getOwner() == null) {
            return;
        }
        Object cls = /*(MClass)*/ getOwner();
        // attributes
        if (mee == null
                || ModelFacade.isAAttribute(mee.getSource())
                || (mee.getSource() == getOwner()
		&& mee.getName().equals("feature"))) {
            updateAttributes();
            damage();
        }
        // operations
        if (mee == null
                || ModelFacade.isAOperation(mee.getSource())
                || ModelFacade.isAParameter(mee.getSource())
                || (mee.getSource() == getOwner()
		&& mee.getName().equals("feature"))) {
            updateOperations();
            damage();
        }
        if (mee != null && mee.getName().equals("parameter") 
                && ModelFacade.isAOperation(mee.getSource())) {
            if (mee.getAddedValue() != null) {
                UmlModelEventPump.getPump().addModelEventListener(this, 
                        mee.getAddedValue(), new String[] {
                            "name", "kind", "type", "defaultValue"});
            }
            if (mee.getRemovedValue() != null) {
                UmlModelEventPump.getPump().addModelEventListener(this, 
                        mee.getRemovedValue());
            }
        }
        if (mee == null || mee.getName().equals("isAbstract")) {
            updateAbstract();
            damage();
        }
        if (mee == null || mee.getName().equals("stereotype")) {
            updateStereotypeText();
            updateAttributes();
            updateOperations();
            damage();
        }
        if (mee != null && mee.getSource()
                .equals(ModelFacade.getStereoType(getOwner()))) {
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
        if (ModelFacade.getStereotypes(me).size() > 0) {
            stereo = ModelFacade.getStereotypes(me).iterator().next();
        }

        if ((stereo == null)
                || (ModelFacade.getName(stereo) == null)
                || (ModelFacade.getName(stereo).length() == 0))	{

            if (getStereotypeFig().isVisible()) {
                getFigAt(BLINDER_POSN).setVisible(false);
                getStereotypeFig().setVisible(false);
                rect.y += STEREOHEIGHT;
                rect.height -= STEREOHEIGHT;
                setBounds(rect.x, rect.y, rect.width, rect.height);
                calcBounds();
            }
        } else {
            setStereotype(Notation.generateStereotype(this, stereo));

            if (!getStereotypeFig().isVisible()) {
                getFigAt(BLINDER_POSN).setVisible(true);
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
        Dimension aSize = 
            isCheckSize() ? getMinimumSize() : new Dimension(w, h);

        int newW = Math.max(w, aSize.width);
        int newH = h;

        int extraEach = 0;
        int heightCorrection = 0;

        // First compute all nessessary height data. Easy if we want less than
        // the minimum

        if (newH <= aSize.height) {

            // Just use the mimimum

            newH = aSize.height;

        } else {

            // Split the extra amongst the number of displayed compartments

            int displayedFigs = 1; //this is for getNameFig()

            if (getAttributesFig().isVisible()) {
                displayedFigs++;
            }

            if (isOperationsVisible()) {
                displayedFigs++;
            }

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

        if (getStereotypeFig().isVisible()) {
            currentY += STEREOHEIGHT;
        }

        getNameFig().setBounds(x, currentY, newW, height);
        getStereotypeFig().setBounds(x, y, newW, STEREOHEIGHT + 1);
        getFigAt(BLINDER_POSN).setBounds(x + 1, y + STEREOHEIGHT, newW - 2, 2);

        // Advance currentY to where the start of the attribute box is,
        // remembering that it overlaps the next box by 1 pixel. Calculate the
        // size of the attribute box, and update the Y pointer past it if it is
        // displayed.

        currentY += height - 1; // -1 for 1 pixel overlap

        int na = (isAttributesVisible())
    	    ? Math.max(1, getAttributesFig().getFigs(null).size() - 1)
    	    : 0;
        int no = (isOperationsVisible())
    	    ? Math.max(1, getOperationsFig().getFigs(null).size() - 1)
    	    : 0;
        if (isCheckSize()) {
            height = ROWHEIGHT * na + 2 + extraEach;
        } else if (newH > currentY - y && na + no > 0) {
            height = (newH + y - currentY) * na / (na + no) + 1;
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
            Vector v = new Vector(getAttributesFig().getFigs(null));
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
            Vector v = new Vector(getOperationsFig().getFigs(null));
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

    /**
     * Updates the attributes in the fig. Called from modelchanged if there is
     * a modelevent effecting the attributes and from renderingChanged in all
     * cases.
     */
    protected void updateAttributes() {
        Object cls = /*(MClassifier)*/ getOwner();
        Fig attrPort = ((FigAttributesCompartment) getFigAt(ATTRIBUTES_POSN))
            .getBigPort();
        int xpos = attrPort.getX();
        int ypos = attrPort.getY();
        int acounter = 1;
        Collection strs = ModelFacade.getStructuralFeatures(cls);
        if (strs != null) {
            Iterator iter = strs.iterator();
            // TODO: in future version of GEF call getFigs returning array
            Vector figs = new Vector(getAttributesFig().getFigs(null));
            CompartmentFigText attr;
            while (iter.hasNext()) {
                Object sf = /*(MStructuralFeature)*/ iter.next();
                // update the listeners
		// UmlModelEventPump.getPump().removeModelEventListener(this,
		// sf);
                UmlModelEventPump.getPump().addModelEventListener(this, sf);
                if (figs.size() <= acounter) {
                    attr =
			new FigFeature(xpos + 1,
				       ypos + 1 + (acounter - 1) * ROWHEIGHT,
				       0,
				       ROWHEIGHT - 2,
				       attrPort);
                    // bounds not relevant here
                    attr.setFilled(false);
                    attr.setLineWidth(0);
                    attr.setFont(getLabelFont());
                    attr.setTextColor(Color.black);
                    attr.setJustification(FigText.JUSTIFY_LEFT);
                    attr.setMultiLine(false);
                    getAttributesFig().addFig(attr);
                } else {
                    attr = (CompartmentFigText) figs.elementAt(acounter);
                }
                attr.setText(Notation.generate(this, sf));
                attr.setOwner(sf);
                // underline, if static
                attr.setUnderline(ModelFacade.CLASSIFIER_SCOPEKIND
				  .equals(ModelFacade.getOwnerScope(sf)));
                acounter++;
            }
            if (figs.size() > acounter) {
                //cleanup of unused attribute FigText's
                for (int i = figs.size() - 1; i >= acounter; i--) {
                    getAttributesFig().removeFig((Fig) figs.elementAt(i));
                }
            }
        }
        Rectangle rect = getBounds();
        updateFigGroupSize(getAttributesFig(), xpos, ypos, 0, 0);
        // ouch ugly but that's for a next refactoring
        // TODO: make setBounds, calcBounds and updateBounds consistent
        setBounds(rect.x, rect.y, rect.width, rect.height);
        damage();
    }

    /**
     * Updates the operations box. Called from modelchanged if there is
     * a modelevent effecting the attributes and from renderingChanged in all
     * cases.
     */
    protected void updateOperations() {
        Object cls = /*(MClassifier)*/ getOwner();
        Fig operPort = ((FigOperationsCompartment) getFigAt(OPERATIONS_POSN))
            .getBigPort();

        int xpos = operPort.getX();
        int ypos = operPort.getY();
        int ocounter = 1;
        Collection behs = ModelFacade.getOperations(cls);
        if (behs != null) {
            Iterator iter = behs.iterator();
            // TODO: in future version of GEF call getFigs returning array
            Vector figs = new Vector(getOperationsFig().getFigs(null));
            CompartmentFigText oper;
            while (iter.hasNext()) {
                Object bf = /*(MBehavioralFeature)*/ iter.next();
                // update the listeners
		// UmlModelEventPump.getPump().removeModelEventListener(this,
		// bf);
                UmlModelEventPump.getPump().addModelEventListener(this, bf);
                if (figs.size() <= ocounter) {
                    oper =
                        new FigFeature(xpos + 1,
                        ypos + 1 + (ocounter - 1) * ROWHEIGHT,
                        0,
                        ROWHEIGHT - 2,
                        operPort);
                    // bounds not relevant here
                    oper.setFilled(false);
                    oper.setLineWidth(0);
                    oper.setFont(getLabelFont());
                    oper.setTextColor(Color.black);
                    oper.setJustification(FigText.JUSTIFY_LEFT);
                    oper.setMultiLine(false);
                    getOperationsFig().addFig(oper);
                } else {
                    oper = (CompartmentFigText) figs.elementAt(ocounter);
                }
                oper.setText(Notation.generate(this, bf));
                oper.setOwner(bf);
                // underline, if static
                oper.setUnderline(ModelFacade.CLASSIFIER_SCOPEKIND
				  .equals(ModelFacade.getOwnerScope(bf)));
                // italics, if abstract
                //oper.setItalic(((MOperation)bf).isAbstract()); //
                //does not properly work (GEF bug?)
                if (ModelFacade.isAbstract(bf)) {
                    oper.setFont(getItalicLabelFont());
                } else {
                    oper.setFont(getLabelFont());
                }
                oper.damage();
                ocounter++;
            }
            if (figs.size() > ocounter) {
                //cleanup of unused operation FigText's
                for (int i = figs.size() - 1; i >= ocounter; i--) {
                    getOperationsFig().removeFig((Fig) figs.elementAt(i));
                }
            }
        }
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
     * Updates the name if modelchanged receives an "isAbstract" event
     */
    protected void updateAbstract() {
        Rectangle rect = getBounds();
        if (getOwner() == null) {
            return;
        }
        Object cls = /*(MClass)*/ getOwner();
        if (ModelFacade.isAbstract(cls)) {
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
            Iterator it = ModelFacade.getFeatures(cl).iterator();
            while (it.hasNext()) {
                Object feat = /*(MFeature)*/ it.next();
                if (ModelFacade.isAOperation(feat)) {
                    Object oper = /*(MOperation)*/ feat;
                    Iterator it2 = ModelFacade.getParameters(oper).iterator();
                    while (it2.hasNext()) {
                        Object param = /*(MParameter)*/ it2.next();
                        UmlModelEventPump.getPump()
			    .removeModelEventListener(this, param);
                    }
                }
            }
        }
        if (newOwner != null) { // add the listeners to the newOwner
            Object cl = /*(MClass)*/ newOwner;
            Iterator it = ModelFacade.getFeatures(cl).iterator();
            while (it.hasNext()) {
                Object feat = /*(MFeature)*/ it.next();
                if (ModelFacade.isAOperation(feat)) {
                    Object oper = /*(MOperation)*/ feat;
                    Iterator it2 = ModelFacade.getParameters(oper).iterator();
                    while (it2.hasNext()) {
                        Object param = /*(MParameter)*/ it2.next();
                        // UmlModelEventPump.getPump()
                        // .removeModelEventListener(this, param);
                        UmlModelEventPump.getPump()
			    .addModelEventListener(this, param);
                    }
                }
            }
        }

        super.updateListeners(newOwner);
    }

} /* end class FigClass */
