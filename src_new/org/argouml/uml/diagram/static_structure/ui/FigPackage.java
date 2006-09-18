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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigStereotypesCompartment;
import org.argouml.uml.diagram.ui.StereotypeContainer;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.ui.VisibilityContainer;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Geometry;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import org.tigris.gef.undo.UndoableAction;

/**
 * Class to display graphics for a UML package in a class diagram.
 * <p>
 * 
 * The "tab" of the Package Fig is build of 2 pieces: 
 * the stereotypes at the top, and the name below it. 
 * Both are not transparent, and have a line border. 
 * And there is a blinder for the line in the middle.
 */
public class FigPackage extends FigNodeModelElement
    implements StereotypeContainer, VisibilityContainer {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(FigPackage.class);

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int MIN_HEIGHT = 21;

    private int width = 140;
    private int height = 100;
    private int indentX = 50;
    //private int indentY = 20;
    private int textH = 20;

    /**
     * The total height of the tab.
     */
    private int tabHeight = 20;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigText body;

    /**
     * Flag that indicates if the stereotype should be shown even if
     * it is specified or not.
     */
    private boolean stereotypeVisible = true;

    /**
     * Flag that indicates if the visibility should be shown in front
     * of the name.
     */
    private boolean visibilityVisible;

    /**
     * A rectangle to blank out the line that would otherwise appear at the
     * bottom of the stereotype text box.
     */
    private FigRect stereoLineBlinder;

    /**
     * The main constructor.
     *
     * @param node the UML package
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     */
    public FigPackage(Object node, int x, int y) {
        setBigPort(
            new PackagePortFigRect(0, 0, width, height, indentX, tabHeight));

        //
        // Create a Body that reacts to double-clicks and jumps to a diagram.
        //
        body = new FigPackageFigText(0, textH, width, height - textH);

        body.setEditable(false);

        getNameFig().setBounds(0, 0, width - indentX, textH + 2);
        getNameFig().setJustification(FigText.JUSTIFY_LEFT);

        getBigPort().setFilled(false);
        getBigPort().setLineWidth(0);

        // Set properties of the stereotype box. Make it 1 pixel higher than
        // before, so it overlaps the name box, and the blanking takes out both
        // lines. Initially not set to be displayed, but this will be changed
        // when we try to render it, if we find we have a stereotype.

        getStereotypeFig().setFilled(true);
        getStereotypeFig().setLineWidth(1);
        getStereotypeFig().setHeight(STEREOHEIGHT + 1);
        getStereotypeFig().setVisible(false);

        // A thin rectangle to overlap the boundary line between stereotype
        // and name. This is just 2 pixels high, and we rely on the line
        // thickness, so the rectangle does not need to be filled. Whether to
        // display is linked to whether to display the stereotype.

        // TODO: Do we really still need this? - Bob
        stereoLineBlinder =
            new FigRect(11, 10 + STEREOHEIGHT, 58, 2, Color.white, Color.white);
        stereoLineBlinder.setLineWidth(1);
        stereoLineBlinder.setVisible(false);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(stereoLineBlinder);
        addFig(body);

        setBlinkPorts(false); //make port invisble unless mouse enters
        
        setLocation(x, y);
        
        // TODO: Why do we need to do this? - Bob
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
        
        updateEdges();

        Project p = ProjectManager.getManager().getCurrentProject();
        visibilityVisible = p.getProjectSettings().getShowVisibilityValue();
        setOwner(node);
    }

    /**
     * Contructor that hooks the fig into the UML element.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigPackage(GraphModel gm, Object node) {
        this(node, 0, 0);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#initNotationProviders(java.lang.Object)
     */
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isAPackage(own)) {
            npArguments.put("visibilityVisible",
                Boolean.valueOf(isVisibilityVisible()));
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new Package";
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigPackage figClone = (FigPackage) super.clone();
        Iterator thisIter = this.getFigs().iterator();
        while (thisIter.hasNext()) {
            Fig thisFig = (Fig) thisIter.next();
            if (thisFig == stereoLineBlinder) {
                figClone.stereoLineBlinder = (FigRect) thisFig;
            }
            if (thisFig == body) {
                figClone.body = (FigText) thisFig;
            }
        }
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        super.setLineColor(col);
        getStereotypeFig().setLineColor(col);
        getNameFig().setLineColor(col);
        body.setLineColor(col);
        stereoLineBlinder.setLineColor(stereoLineBlinder.getFillColor());
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return body.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        super.setFillColor(col);
        getStereotypeFig().setFillColor(col);
        getNameFig().setFillColor(col);
        body.setFillColor(col);
        stereoLineBlinder.setLineColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return body.getFillColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        getStereotypeFig().setFilled(f);
        getNameFig().setFilled(f);
        body.setFilled(f);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return body.getFilled();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        getNameFig().setLineWidth(w);
        body.setLineWidth(w);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return body.getLineWidth();
    }

    ////////////////////////////////////////////////////////////////

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     *
     * Called to update the graphics.
     */
    public void renderingChanged() {
        super.renderingChanged();
        updateStereotypeText();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateStereotypeText()
     */
    protected void updateStereotypeText() {
        Object modelElement = getOwner();

        if (modelElement == null) {
            return;
        }

        Rectangle rect = getBounds();

        /* check if stereotype is defined */
        if (Model.getFacade().getStereotypes(modelElement).isEmpty()) {
            if (getStereotypeFig().isVisible()) {
                stereoLineBlinder.setVisible(false);
                getStereotypeFig().setVisible(false);
            }
        } else {
            /* we got stereotype */
            getStereotypeFig().setOwner(getOwner());
            ((FigStereotypesCompartment) getStereotypeFig()).populate();
            if (!stereotypeVisible) {
                stereoLineBlinder.setVisible(false);
                getStereotypeFig().setVisible(false);
            } else if (!getStereotypeFig().isVisible()) {
                if (stereotypeVisible) {
                    stereoLineBlinder.setVisible(true);
                    getStereotypeFig().setVisible(true);
                }
            }
        }

        forceRepaintShadow();
        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * USED BY PGML.tee.
     * @return the class name and bounds together with compartment
     * visibility.
     */
    public String classNameAndBounds() {
        return super.classNameAndBounds()
                + "stereotypeVisible=" + isStereotypeVisible()
                + ";"
                + "visibilityVisible=" + isVisibilityVisible();
    }


    ////////////////////////////////////////////////////////////////
    // user interaction methods

    /**
     * Handles changes of the model.
     * If the visibility is changed via the properties panel, then
     * the display of it on the diagram has to follow the change.
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        boolean damage = false;
        // visibility
        if (mee == null
                || Model.getFacade().isAPackage(mee.getSource())
                || (mee.getSource() == getOwner()
                && mee.getPropertyName().equals("visibility"))) {
            renderingChanged();
            damage = true;
        }
        if (mee != null && Model.getFacade().getStereotypes(getOwner())
                .contains(mee.getSource())) {
            updateStereotypeText();
            damage = true;
        }
        if (damage) {
            damage();
        }
    }

    ////////////////////////////////////////////////////////////////
    // accessor methods

    /**
     * @see org.tigris.gef.presentation.Fig#getUseTrapRect()
     */
    public boolean getUseTrapRect() {
        return true;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        // Use "aSize" to build up the minimum size. Start with the size of the
        // name compartment and build up.

        Dimension aSize = getNameFig().getMinimumSize();

        int w = aSize.width + indentX;
        if (aSize.height < MIN_HEIGHT) {
            aSize.height = MIN_HEIGHT;
        }

        int minWidth = Math.max(0, w + 1 + getShadowSize());
        if (aSize.width < minWidth) {
            aSize.width = minWidth;
        }

        // If we have a stereotype displayed, then allow some space for that
        // (width and height)

        if (getStereotypeFig().isVisible()) {
            aSize.width =
		Math.max(aSize.width,
			 getStereotypeFig().getMinimumSize().width);
            aSize.height += STEREOHEIGHT;
        }
        // we want at least some of the package body to be displayed
        aSize.height = Math.max(aSize.height, 60);
        // And now aSize has the answer
        return aSize;
    }

    /**
     * Sets the bounds, but the size will be at least the one returned by
     * {@link #getMinimumSize()}.<p>
     *
     * If the required height is bigger, then the additional height is
     * equally distributed among all figs (i.e. compartments), such that the
     * cumulated height of all visible figs equals the demanded height<p>.
     *
     * Some of this has "magic numbers" hardcoded in. In particular there is
     * a knowledge that the minimum height of a name compartment is 21
     * pixels. This height is needed to be able to display the "Clarifier"
     * inside the name compartment.
     *
     * @param xa  Desired X coordinate of upper left corner
     *
     * @param ya  Desired Y coordinate of upper left corner
     *
     * @param w  Desired width of the FigClass
     *
     * @param h  Desired height of the FigClass
     */
    protected void setBoundsImpl(int xa, int ya, int w, int h) {
        // Save our old boundaries (needed later), and get minimum size
        // info. "aSize" will be used to maintain a running calculation of our
        // size at various points.

        Rectangle oldBounds = getBounds();
        Dimension aSize = getMinimumSize();

        int newW = Math.max(w, aSize.width);
        int newH = h;

        // First compute all nessessary height data. Easy if we want less than
        // the minimum

        if (newH <= aSize.height) {
            // Just use the mimimum
            newH = aSize.height;
        }

        // Now resize all sub-figs, including not displayed figs. Start by the
        // name. We override the getMinimumSize if it is less than our view (21
        // pixels hardcoded!). Add in the shared extra, plus in this case the
        // correction.

        int minHeight = getNameFig().getMinimumSize().height;

        if (minHeight < MIN_HEIGHT) {
            minHeight = MIN_HEIGHT;
        }

        // Now sort out the stereotype display. If the stereotype is displayed,
        // move the upper boundary of the name compartment up and set new
        // bounds for the name and the stereotype compartments and the
        // stereoLineBlinder that blanks out the line between the two

        int currentY = ya;

        Dimension stereoMin = getStereotypeFig().getMinimumSize();
        if (getStereotypeFig().isVisible()) {
            currentY += stereoMin.height;
        }

        getStereotypeFig().setBounds(xa, ya,
                newW - indentX, stereoMin.height + 1);
        int nameWidth = newW - indentX;
        if (nameWidth < stereoMin.width + 1) {
            nameWidth = stereoMin.width + 2;
        }
        stereoLineBlinder.setBounds(
                xa + 1,
                ya + stereoMin.height,
                nameWidth - 2,
                2);
        getNameFig().setBounds(xa, currentY, nameWidth + 1, minHeight);

        // Advance currentY to where the start of the body box is,
        // remembering that it overlaps the next box by 1 pixel. Calculate the
        // size of the attribute box, and update the Y pointer past it if it is
        // displayed.

        currentY += minHeight - 1; // -1 for 1 pixel overlap
        body.setBounds(xa, currentY, newW, newH + ya - currentY);

        tabHeight = currentY - ya;
        // set bounds of big box

        getBigPort().setBounds(xa, ya, newW, newH);

        // Now force calculation of the bounds of the figure, update the edges
        // and trigger anyone who's listening to see if the "bounds" property
        // has changed.

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /**
     * Build a collection of menu items relevant for a right-click
     * popup menu on a Package.
     *
     * @param     me     a mouse event
     * @return          a collection of menu items
     */
    public Vector getPopUpActions(MouseEvent me) {
        Vector popUpActions = super.getPopUpActions(me);

        // Show ...
        ArgoJMenu showMenu = new ArgoJMenu("menu.popup.show");
        /* Only show the menuitems if they make sense: */
        Editor ce = Globals.curEditor();
        Vector figs = ce.getSelectionManager().getFigs();
        Iterator i = figs.iterator();
        boolean sOn = false;
        boolean sOff = false;
        boolean vOn = false;
        boolean vOff = false;
        while (i.hasNext()) {
            Fig f = (Fig) i.next();
            if (f instanceof StereotypeContainer) {
                boolean v = ((StereotypeContainer) f).isStereotypeVisible();
                if (v) {
                    sOn = true;
                } else {
                    sOff = true;
                }
                v = ((VisibilityContainer) f).isVisibilityVisible();
                if (v) {
                    vOn = true;
                } else {
                    vOff = true;
                }
            }
        }

        if (sOn) {
            showMenu.add(new HideStereotypeAction());
        }

        if (sOff) {
            showMenu.add(new ShowStereotypeAction());
        }

        if (vOn) {
            showMenu.add(new HideVisibilityAction());
        }

        if (vOff) {
            showMenu.add(new ShowVisibilityAction());
        }

        popUpActions.insertElementAt(showMenu,
            popUpActions.size() - getPopupAddOffset());

        // Modifier ...
        popUpActions.insertElementAt(buildModifierPopUp(ABSTRACT | LEAF | ROOT),
                popUpActions.size() - getPopupAddOffset());

        // Visibility ...
        popUpActions.insertElementAt(buildVisibilityPopUp(),
                popUpActions.size() - getPopupAddOffset());

        return popUpActions;
    }

    /**
     * Change the visibility of the stereotypes of all Figs.
     *
     * @param value true if it needs to become visible
     */
    private void doStereotype(boolean value) {
        Editor ce = Globals.curEditor();
        Vector figs = ce.getSelectionManager().getFigs();
        Iterator i = figs.iterator();
        while (i.hasNext()) {
            Fig f = (Fig) i.next();
            if (f instanceof StereotypeContainer) {
                ((StereotypeContainer) f).setStereotypeVisible(value);
            }
            if (f instanceof FigNodeModelElement) {
                ((FigNodeModelElement) f).forceRepaintShadow();
                ((FigNodeModelElement) f).renderingChanged();
            }
            f.damage();
        }
    }

    /**
     * Change the visibility of the Visibility of all Figs.
     *
     * @param value true if it needs to become visible
     */
    private void doVisibility(boolean value) {
        Editor ce = Globals.curEditor();
        Vector figs = ce.getSelectionManager().getFigs();
        Iterator i = figs.iterator();
        while (i.hasNext()) {
            Fig f = (Fig) i.next();
            if (f instanceof VisibilityContainer) {
                ((VisibilityContainer) f).setVisibilityVisible(value);
            }
            f.damage();
        }
    }

    class FigPackageFigText extends FigText {
        /**
	 * The constructor.
         *
	 * @param xa
	 * @param ya
	 * @param w
	 * @param h
	 */
	public FigPackageFigText(int xa, int ya, int w, int h) {
	    super(xa, ya, w, h);
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(
         *         java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent me) {

	    String lsDefaultName = "main";

	    if (me.getClickCount() >= 2) {
		Object lPkg = /*(MPackage)*/ FigPackage.this.getOwner();
		if (lPkg != null) {
		    Object lNS = lPkg;

		    Project lP =
			ProjectManager.getManager().getCurrentProject();

		    Vector diags = lP.getDiagrams();
		    Enumeration diagEnum = diags.elements();
		    UMLDiagram lFirst = null;
		    while (diagEnum.hasMoreElements()) {
			UMLDiagram lDiagram =
			    (UMLDiagram) diagEnum.nextElement();
			Object lDiagramNS =
			    /*(MNamespace)*/lDiagram.getNamespace();
			if ((lNS == null && lDiagramNS == null)
			    || (lNS.equals(lDiagramNS))) {
			    /* save first */
			    if (lFirst == null) {
				lFirst = lDiagram;
			    }

			    if (lDiagram.getName() != null
				&& lDiagram.getName().startsWith(
				        lsDefaultName)) {
			        me.consume();
				super.mouseClicked(me);
				TargetManager.getInstance().setTarget(lDiagram);
				return;
			    }
			}
		    } /*while*/

		    /* If we get here then we didn't get the
		     * default diagram.
                     */
		    if (lFirst != null) {
			me.consume();
			super.mouseClicked(me);

			TargetManager.getInstance().setTarget(lFirst);
			return;
		    }

		    /* Try to create a new class diagram.
                     */
		    me.consume();
		    super.mouseClicked(me);
		    try {
                        // TODO:  Do this using an Action 
                        // to break the dependency cycle?
                        // Increases largest cycle from 95 to 258
//		        createClassDiagram(lNS, lsDefaultName, lP);
		    } catch (Exception ex) {
		        LOG.error(ex);
		    }

		    return;

		} /*if package */
	    } /* if doubleclicks */
	    super.mouseClicked(me);
	}

        /**
         * The UID.
         */
        private static final long serialVersionUID = -1355316218065323634L;
    }

    private void createClassDiagram(
            Object namespace,
            String defaultName,
            Project project) throws PropertyVetoException {

//        String namespaceDescr;
//        if (namespace != null
//                && Model.getFacade().getName(namespace) != null) {
//            namespaceDescr = Model.getFacade().getName(namespace);
//        } else {
//            namespaceDescr = Translator.localize("misc.name.anon");
//        }
//
//        String dialogText = "Add new class diagram to " + namespaceDescr + "?";
//        int option =
//            JOptionPane.showConfirmDialog(
//                null,
//                dialogText,
//                "Add new class diagram?",
//                JOptionPane.YES_NO_OPTION);
//
//        if (option == JOptionPane.YES_OPTION) {
//
//            ArgoDiagram classDiagram =
//                DiagramFactory.getInstance().
//                    createDiagram(UMLClassDiagram.class, namespace, null);
//
//            String diagramName = defaultName + "_" + classDiagram.getName();
//
//            project.addMember(classDiagram);
//
//            TargetManager.getInstance().setTarget(classDiagram);
//            /* change prefix */
//            classDiagram.setName(diagramName);
//            ExplorerEventAdaptor.getInstance().structureChanged();
//        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.StereotypeContainer#isStereotypeVisible()
     */
    public boolean isStereotypeVisible() {
        return stereotypeVisible;
    }

    /**
     * @see org.argouml.uml.diagram.ui.StereotypeContainer#setStereotypeVisible(boolean)
     */
    public void setStereotypeVisible(boolean isVisible) {
        stereotypeVisible = isVisible;
        renderingChanged();
        damage();
    }

    /**
     * @see org.argouml.uml.diagram.ui.VisibilityContainer#isVisibilityVisible()
     */
    public boolean isVisibilityVisible() {
        return visibilityVisible;
    }

    /**
     * @see org.argouml.uml.diagram.ui.VisibilityContainer#setVisibilityVisible(boolean)
     */
    public void setVisibilityVisible(boolean isVisible) {
        visibilityVisible = isVisible;
        if (notationProviderName != null) {
            npArguments.put("visibilityVisible",
                Boolean.valueOf(isVisible));
        }
        renderingChanged();
        damage();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {

        /* The following 2 lines should be retained for reference.
         * They represent the better way of editing on the diagram, which
         * 1. would work for different notations, and
         * 2. would indicate to the user that he can edit more aspects
         * of the modelelement than the name alone.
         * But: it is different behaviour, which I (MVW)
         * do not know if it is acceptable.
         */

//        String s = GeneratorDisplay.getInstance().generate(getOwner());
//        ft.setText(s);

        if (ft == getNameFig()) {
            showHelp("parsing.help.fig-package");
        }
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getClosestPoint(java.awt.Point)
     */
    public Point getClosestPoint(Point anotherPt) {
        Rectangle r = getBounds();
        int[] xs = {
            r.x, r.x + r.width - indentX, r.x + r.width - indentX,
            r.x + r.width,   r.x + r.width,  r.x,            r.x,
        };
        int[] ys = {
            r.y, r.y,                     r.y + tabHeight,
            r.y + tabHeight, r.y + r.height, r.y + r.height, r.y,
        };
        Point p =
            Geometry.ptClosestTo(
                xs,
                ys,
                7,
                anotherPt);
        return p;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3617092272529451041L;

    private class HideStereotypeAction extends UndoableAction {
        /**
         * The key for the action name.
         */
        private static final String ACTION_KEY =
            "menu.popup.show.hide-stereotype";

        /**
         * Constructor.
         */
        HideStereotypeAction() {
            super(Translator.localize(ACTION_KEY),
                    ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
        }
        /**
         * @see java.awt.event.ActionListener#actionPerformed(
         *         java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            doStereotype(false);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID =
            1999499813643610674L;
    }

    private class ShowStereotypeAction extends UndoableAction {
        /**
         * The key for the action name.
         */
        private static final String ACTION_KEY =
            "menu.popup.show.show-stereotype";

        /**
         * Constructor.
         */
        ShowStereotypeAction() {
            super(Translator.localize(ACTION_KEY),
                    ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
        }
        /**
         * @see java.awt.event.ActionListener#actionPerformed(
         *         java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            doStereotype(true);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID =
            -4327161642276705610L;
    }

    private class HideVisibilityAction extends UndoableAction {
        /**
         * The key for the action name.
         */
        private static final String ACTION_KEY =
            "menu.popup.show.hide-visibility";

        /**
         * Constructor.
         */
        HideVisibilityAction() {
            super(Translator.localize(ACTION_KEY),
                    ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
        }
        /**
         * @see java.awt.event.ActionListener#actionPerformed(
         *         java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            doVisibility(false);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID =
            8574809709777267866L;
    }

    private class ShowVisibilityAction extends UndoableAction {
        /**
         * The key for the action name.
         */
        private static final String ACTION_KEY =
            "menu.popup.show.show-visibility";

        /**
         * Constructor.
         */
        ShowVisibilityAction() {
            super(Translator.localize(ACTION_KEY),
                    ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
        }
        /**
         * @see java.awt.event.ActionListener#actionPerformed(
         *         java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent ae) {
            super.actionPerformed(ae);
            doVisibility(true);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID =
            7722093402948975834L;
    }

} /* end class FigPackage */

/**
 * The bigport needs to overrule the getClosestPoint,
 * because it is the port of this FigNode.
 *
 * @author mvw@tigris.org
 */
class PackagePortFigRect extends FigRect {
    private int indentX;
    private int tabHeight;

    /**
     * The constructor.
     *
     * @param x The x.
     * @param y The y.
     * @param w The width.
     * @param h The height.
     * @param ix The indent.
     * @param th The tab height.
     */
    public PackagePortFigRect(int x, int y, int w, int h, int ix, int th) {
        super(x, y, w, h, null, null);
        this.indentX = ix;
        tabHeight = th;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getClosestPoint(java.awt.Point)
     */
    public Point getClosestPoint(Point anotherPt) {
        Rectangle r = getBounds();
        int[] xs = {
            r.x, r.x + r.width - indentX, r.x + r.width - indentX,
            r.x + r.width,   r.x + r.width,  r.x,            r.x,
        };
        int[] ys = {
            r.y, r.y,                     r.y + tabHeight,
            r.y + tabHeight, r.y + r.height, r.y + r.height, r.y,
        };
        Point p =
            Geometry.ptClosestTo(
                xs,
                ys,
                7,
                anotherPt);
        return p;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7083102131363598065L;
}
