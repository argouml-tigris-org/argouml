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
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.api.Notation;
import org.argouml.language.helpers.NotationHelper;
import org.argouml.model.Model;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ActionAddNote;
import org.argouml.uml.diagram.ui.ActionAddOperation;
import org.argouml.uml.diagram.ui.ActionCompartmentDisplay;
import org.argouml.uml.diagram.ui.CompartmentFigText;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigOperationsCompartment;
import org.argouml.uml.diagram.ui.OperationsCompartmentContainer;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML Interface in a diagram.
 */
public class FigInterface extends FigNodeModelElement
        implements OperationsCompartmentContainer {

    private static final Logger LOG = Logger.getLogger(FigInterface.class);

    ////////////////////////////////////////////////////////////////
    // constants

    //These are the positions of child figs inside this fig
    //They mst be added in the constructor in this order.
    private static final int BLINDER_POSN = 3;
    private static final int OPERATIONS_POSN = 4;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * The vector of graphics for operations (if any). First one is the
     * rectangle for the entire operations box.<p>
     */
    private FigOperationsCompartment operVec;

    /**
     * A rectangle to blank out the line that would otherwise appear at the
     * bottom of the stereotype text box.<p>
     */
    private FigRect stereoLineBlinder;

    /**
     * Manages residency of an interface within a component on a deployment
     * diagram. Not clear why it is an instance
     * variable (rather than local to the method).<p>
     */
    private Object resident =
        Model.getCoreFactory().createElementResidence();

    /**
     * Text highlighted by mouse actions on the diagram.<p>
     */
    private CompartmentFigText highlightedFigText = null;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main constructor for a {@link FigInterface}.
     *
     * Parent {@link FigNodeModelElement} will have created the main
     * box {@link #getBigPort()} and its name {@link #getNameFig()}
     * and stereotype (@link #getStereotypeFig()}. This constructor
     * creates a box for the operations.<p>
     *
     * The properties of all these graphic elements are adjusted
     * appropriately. The main boxes are all filled and have outlines.<p>
     *
     * For reasons I don't understand the stereotype is created in a box
     * with lines. So we have to created a blanking rectangle to overlay the
     * bottom line, and avoid three compartments showing.<p>
     *
     * <em>Warning</em>. Much of the graphics positioning is hard coded. The
     * overall figure is placed at location (10,10). The name compartment (in
     * the parent {@link FigNodeModelElement} is 21 pixels high. The
     * stereotype compartment is created 15 pixels high in the parent, but we
     * change it to 19 pixels, 1 more than ({@link #STEREOHEIGHT} here. The
     * operations box is created at 19 pixels, 2 more than
     * {@link #ROWHEIGHT}.<p>
     *
     * CAUTION: This constructor (with no arguments) is the only one
     * that does enableSizeChecking(false), all others must set it true.
     * This is because this constructor is the only one called when loading
     * a project. In this case, the parsed size must be maintained.<p>
     */
    public FigInterface() {

        // Set name box. Note the upper line will be blanked out if there is
        // eventually a stereotype above.
        getNameFig().setLineWidth(1);
        getNameFig().setFilled(true);

        operVec =
            new FigOperationsCompartment(10, 31 + ROWHEIGHT, 60, ROWHEIGHT + 2);

        // Set properties of the stereotype box. Make it 1 pixel higher than
        // before, so it overlaps the name box, and the blanking takes out both
        // lines. Initially not set to be displayed, but this will be changed
        // when we try to render it, if we find we have a stereotype.
        setStereotype(NotationHelper.getLeftGuillemot()
		      + "Interface" + NotationHelper.getRightGuillemot());
        getStereotypeFigText().setExpandOnly(true);
        getStereotypeFig().setFilled(true);
        getStereotypeFig().setLineWidth(1);
        getStereotypeFigText().setEditable(false);
        getStereotypeFig().setHeight(STEREOHEIGHT + 1);
        // +1 to have 1 pixel overlap with getNameFig()
        getStereotypeFig().setVisible(true);

        // A thin rectangle to overlap the boundary line between stereotype
        // and name. This is just 2 pixels high, and we rely on the line
        // thickness, so the rectangle does not need to be filled. Whether to
        // display is linked to whether to display the stereotype.
        stereoLineBlinder =
	    new FigRect(11, 10 + STEREOHEIGHT, 58, 2,
			Color.white, Color.white);
        stereoLineBlinder.setLineWidth(1);
        stereoLineBlinder.setVisible(true);

        // Put all the bits together, suppressing bounds calculations until
        // we're all done for efficiency.
        enableSizeChecking(false);
        setSuppressCalcBounds(true);
        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(stereoLineBlinder);
        addFig(operVec);
        setSuppressCalcBounds(false);

        // Set the bounds of the figure to the total of the above (hardcoded)
        enableSizeChecking(true);
        setBounds(10, 10, 60, 21 + ROWHEIGHT);
    }

    /**
     * Constructor for use if this figure is created for an
     * existing interface node in the metamodel.<p>
     *
     * Set the figure's name according to this node. This is used when the
     * user click's on 'add to diagram' in the navpane.  Don't know if this
     * should rather be done in one of the super classes, since similar code
     * is used in FigClass.java etc.  Andreas Rueckert
     * &lt;a_rueckert@gmx.net&gt;<p>
     *
     * @param gm   Not actually used in the current implementation
     *
     * @param node The UML object being placed.
     */
    public FigInterface(GraphModel gm, Object node) {
        this();
        setOwner(node);
        enableSizeChecking(true);
        if (Model.getFacade().isAInterface(node)
	        && (Model.getFacade().getName(node) != null)) {
            getNameFig().setText(Model.getFacade().getName(node));
	}
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new Interface";
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigInterface figClone = (FigInterface) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.setStereotypeFig((FigText) it.next());
        figClone.setNameFig((FigText) it.next());
        figClone.stereoLineBlinder = (FigRect) it.next();
        figClone.operVec = (FigOperationsCompartment) it.next();
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionInterface(this);
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on an Interface.
     *
     * @param     me     a mouse event
     * @return           a collection of menu items
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        // Add ...
        ArgoJMenu addMenu = new ArgoJMenu("menu.popup.add");
        addMenu.add(new ActionAddOperation());
        addMenu.add(new ActionAddNote());
        popUpActions.insertElementAt(addMenu,
            popUpActions.size() - POPUP_ADD_OFFSET);

        // Show ...
        ArgoJMenu showMenu = new ArgoJMenu("menu.popup.show");
        if (operVec.isVisible()) {
            showMenu.add(ActionCompartmentDisplay.hideOperCompartment());
        } else {
            showMenu.add(ActionCompartmentDisplay.showOperCompartment());
        }
        popUpActions.insertElementAt(showMenu,
            popUpActions.size() - POPUP_ADD_OFFSET);

        // Modifier ...
        popUpActions.insertElementAt(buildModifierPopUp(ABSTRACT | LEAF | ROOT),
                popUpActions.size() - POPUP_ADD_OFFSET);

        // Visibility ...
        popUpActions.insertElementAt(buildVisibilityPopUp(),
                popUpActions.size() - POPUP_ADD_OFFSET);

        return popUpActions;
    }

    /**
     * Getter for operVec.
     *
     * @return operVec
     */
    public FigGroup getOperationsFig() {
        return operVec;
    }

    /**
     * Getter for operVec.
     *
     * @return operVec
     */
    public Rectangle getOperationsBounds() {
        return operVec.getBounds();
    }

    /**
     * Returns the status of the operation field.
     * @return true if the operations are visible, false otherwise
     */
    public boolean isOperationsVisible() {
        return operVec.isVisible();
    }

    /**
     * @param isVisible true will show the operations compartiment
     */
    public void setOperationsVisible(boolean isVisible) {
        Rectangle rect = getBounds();
        int h =
	    isCheckSize()
	    ? ((ROWHEIGHT * Math.max(1, operVec.getFigs().size() - 1) + 2)
	       * rect.height
	       / getMinimumSize().height)
	    : 0;
        if (operVec.isVisible()) {
            if (!isVisible) {
                damage();
                Iterator it = operVec.getFigs().iterator();
                while (it.hasNext()) {
		    ((Fig) (it.next())).setVisible(false);
                }
                operVec.setVisible(false);
                setBounds(rect.x, rect.y, rect.width, rect.height - h);
            }
        } else {
            if (isVisible) {
                Iterator it = operVec.getFigs().iterator();
                while (it.hasNext()) {
		    ((Fig) (it.next())).setVisible(true);
                }
                operVec.setVisible(true);
                setBounds(rect.x, rect.y, rect.width, rect.height + h);
                damage();
            }
        }
    }

    /**
     * Gets the minimum size permitted for an interface on the diagram.<p>
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

        // Allow space for each of the operations we have

        if (operVec.isVisible()) {

            // Loop through all the operations, to find the widest (remember
            // the first fig is the box for the whole lot, so ignore it).

            Iterator it = operVec.getFigs().iterator();
            it.next(); // ignore

            while (it.hasNext()) {
                int elemWidth =
		    ((FigText) it.next()).getMinimumSize().width + 2;
                aSize.width = Math.max(aSize.width, elemWidth);
            }
            aSize.height +=
		ROWHEIGHT * Math.max(1, operVec.getFigs().size() - 1) + 1;
        }

        // we want to maintain a minimum width for Interfaces
        aSize.width = Math.max(60, aSize.width);

        // And now aSize has the answer

        return aSize;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color lColor) {
        super.setFillColor(lColor);
        stereoLineBlinder.setLineColor(lColor);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color lColor) {
        super.setLineColor(lColor);
        stereoLineBlinder.setLineColor(stereoLineBlinder.getFillColor());
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
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
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
        //display op properties if necessary:
        Rectangle r = new Rectangle(me.getX() - 1, me.getY() - 1, 2, 2);
        Fig f = hitFig(r);
        if (f == operVec && operVec.getHeight() > 0) {
            // TODO: in future version of GEF call getFigs returning array
            Vector v = new Vector(operVec.getFigs());
            i = (v.size() - 1)
		* (me.getY() - f.getY() - 3)
		/ operVec.getHeight();
            if (i >= 0 && i < v.size() - 1) {
                me.consume();
                f = (Fig) v.elementAt(i + 1);
                ((CompartmentFigText) f).setHighlighted(true);
                highlightedFigText = (CompartmentFigText) f;
                TargetManager.getInstance().setTarget(f);
            }
        }
    }

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
                // TODO: in future version of GEF call getFigs returning array
                int i = new Vector(operVec.getFigs()).indexOf(ft);
                if (i != -1) {
                    if (key == KeyEvent.VK_UP) {
                        ft =
			    (CompartmentFigText)
			    getPreviousVisibleFeature(ft, i);
                    } else {
                        ft =
			    (CompartmentFigText)
			    getNextVisibleFeature(ft, i);
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

    /**
     * @see org.tigris.gef.presentation.Fig#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig encloser) {
        Fig oldEncloser = getEnclosingFig();
        super.setEnclosingFig(encloser);
        if (!(Model.getFacade().isAModelElement(getOwner())))
            return;
        /* If this fig is not visible, do not adapt the UML model! 
         * This is used for deleting. See issue 3042. */
        if  (!isVisible())
            return; 
        Object me = /*(MModelElement)*/ getOwner();
        Object m = null;

        try {
            // If moved into an Package
            if (encloser != null
                    && oldEncloser != encloser
                    && Model.getFacade().isAPackage(encloser.getOwner())) {
                Model.getCoreHelper().setNamespace(me,
					 /*(MNamespace)*/ encloser.getOwner());
            }

            // If default Namespace is not already set
            if (Model.getFacade().getNamespace(me) == null
		    && (TargetManager.getInstance().getTarget()
		       instanceof UMLDiagram)) {
                m = /*(MNamespace)*/
		    ((UMLDiagram) TargetManager.getInstance().getTarget())
		    .getNamespace();
                Model.getCoreHelper().setNamespace(me, m);
            }
        } catch (Exception e) {
            LOG.error("could not set package due to:" + e
		      + "' at " + encloser, e);
        }

        // The next if-clause is important for the Deployment-diagram
        // it detects if the enclosing fig is a component, in this case
        // the ImplementationLocation will be set for the owning MInterface
        if (encloser != null
	        && (Model.getFacade().isAComponent(encloser.getOwner()))) {
            Object component = /*(MComponent)*/ encloser.getOwner();
            Object in = /*(MInterface)*/ getOwner();
            Model.getCoreHelper().setImplementationLocation(resident,
                    component);
            Model.getCoreHelper().setResident(resident, in);
        } else {
            Model.getCoreHelper().setImplementationLocation(resident, null);
            Model.getCoreHelper().setResident(resident, null);
        }
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
        // TODO: in future version of GEF call getFigs returning array
        int i = new Vector(operVec.getFigs()).indexOf(ft);
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
     * @param ft the figtext holding the feature
     * @param i the index (?)
     * @return the figtext
     */
    protected FigText getPreviousVisibleFeature(FigText ft, int i) {
        FigText ft2 = null;
        // TODO: in future version of GEF call getFigs returning array
        Vector v = new Vector(operVec.getFigs());
        if (i < 1 || i >= v.size()
                || !((FigText) v.elementAt(i)).isVisible()) {
            return null;
        }

        do {
            i--;
            if (i < 1) {
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
     * @param ft the figtext holding the feature
     * @param i the index (?)
     * @return the figtext
     */
    protected FigText getNextVisibleFeature(FigText ft, int i) {
        FigText ft2 = null;
        Vector v = new Vector(operVec.getFigs());
        if (i < 1 || i >= v.size()
                || !((FigText) v.elementAt(i)).isVisible()) {
            return null;
        }

        do {
            i++;
            if (i >= v.size()) {
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
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    public String classNameAndBounds() {
        return super.classNameAndBounds()
            + "operationsVisible=" + isOperationsVisible();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#createFeatureIn(
     *         org.tigris.gef.presentation.FigGroup, java.awt.event.InputEvent)
     */
    protected void createFeatureIn(FigGroup fg, InputEvent ie) {
        Object cls = /*(MClassifier)*/ getOwner();
        if (cls == null) {
            return;
        }
        new ActionAddOperation().actionPerformed(null);
        // TODO: in future version of GEF call getFigs returning array
        CompartmentFigText ft =
            (CompartmentFigText) new Vector(fg.getFigs()).lastElement();
        if (ft != null) {
            ft.startTextEditor(ie);
            ft.setHighlighted(true);
            highlightedFigText = ft;
        }
        ie.consume();
    }

    /**
     * @return the FigText for the compartment
     */
    protected CompartmentFigText unhighlight() {
        CompartmentFigText ft;
        // TODO: in future version of GEF call getFigs returning array
        Vector v = new Vector(operVec.getFigs());
        int i;
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
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        if (getOwner() == null) {
            return;
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
        super.modelChanged(mee);

    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        super.renderingChanged();

        updateOperations();

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
     * @param w  Desired width of the FigInterface
     *
     * @param h  Desired height of the FigInterface
     */
    public void setBounds(int x, int y, int w, int h) {

        // Save our old boundaries (needed later), and get minimum size
        // info. "aSize will be used to maintain a running calculation of our
        // size at various points.

        // "extraEach" is the extra height per displayed fig if requested
        // height is greater than minimal. "heightCorrection" is the height
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

            if (operVec.isVisible()) {
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
        stereoLineBlinder.setBounds(x + 1, y + STEREOHEIGHT, newW - 2, 2);

        // Advance currentY to where the start of the attribute box is,
        // remembering that it overlaps the next box by 1 pixel. Calculate the
        // size of the attribute box, and update the Y pointer past it if it is
        // displayed.

        currentY += height - 1; // -1 for 1 pixel overlap

        // Finally update the bounds of the operations box

        aSize =
	    updateFigGroupSize(operVec, x, currentY,
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
     * Updates the operations box. Called from modelchanged if there is
     * a modelevent effecting the attributes and from renderingChanged in all
     * cases.
     */
    protected void updateOperations() {
        Object cls = /*(MClassifier)*/ getOwner();
        if (cls == null) {
            return;
        }

        Fig operPort =
            ((FigOperationsCompartment) getFigAt(OPERATIONS_POSN)).getBigPort();

        int xpos = operPort.getX();
        int ypos = operPort.getY();
        int ocounter = 1;
        Collection behs = Model.getFacade().getOperations(cls);
        if (behs != null) {
            Iterator iter = behs.iterator();
            // TODO: in future version of GEF call getFigs returning array
            Vector figs = new Vector(operVec.getFigs());
            CompartmentFigText oper;
            while (iter.hasNext()) {
        	Object behavioralFeature =
        	    /*(MBehavioralFeature)*/ iter.next();
        	// update the listeners
        	Model.getPump().removeModelEventListener(this,
                    behavioralFeature);
        	Model.getPump().addModelEventListener(this,
                    behavioralFeature);
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
        	    operVec.addFig(oper);
        	} else {
        	    oper = (CompartmentFigText) figs.elementAt(ocounter);
        	}
        	oper.setText(Notation.generate(this, behavioralFeature));
        	oper.setOwner(behavioralFeature);
        	// underline, if static
        	oper.setUnderline(Model.getScopeKind().getClassifier()
        			  .equals(Model.getFacade()
        				  .getOwnerScope(behavioralFeature)));
        	// italics, if abstract
        	//oper.setItalic(((MOperation)bf).isAbstract());
        	//// does not properly work (GEF bug?)
        	if (Model.getFacade().isAbstract(behavioralFeature)) {
        	    oper.setFont(getItalicLabelFont());
        	} else {
        	    oper.setFont(getLabelFont());
        	}
        	ocounter++;
            }
            if (figs.size() > ocounter) {
        	//cleanup of unused operation FigText's
        	for (int i = figs.size() - 1; i >= ocounter; i--) {
        	    operVec.removeFig((Fig) figs.elementAt(i));
        	}
            }
        }
        Rectangle rect = getBounds();
        updateFigGroupSize(operVec, xpos, ypos, 0, 0);
        // ouch ugly but that's for a next refactoring
        // TODO: make setBounds, calcBounds and updateBounds consistent
        setBounds(rect.x, rect.y, rect.width, rect.height);
        damage();
    }

    /**
     * @see
     * org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        Rectangle rect = getBounds();
        setStereotype(NotationHelper.getLeftGuillemot()
		      + "Interface"
		      + NotationHelper.getRightGuillemot());
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

} /* end class FigInterface */
