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
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.argouml.application.api.Notation;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/** Class to display graphics for a UML package in a class diagram. */

public class FigPackage extends FigNodeModelElement {
    private static final Logger LOG = Logger.getLogger(FigPackage.class);

    ////////////////////////////////////////////////////////////////
    // constants

    private int x = 0;
    private int y = 0;
    private int width = 140;
    private int height = 100;
    private int indentX = 50;
    //private int indentY = 20;
    private int textH = 20;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private FigText body;

    /**
     * Flags that indicates if the stereotype should be shown even if
     * it is specified or not.
     */
    private boolean showStereotype = true;

    /**
     * <p>A rectangle to blank out the line that would otherwise appear at the
     *   bottom of the stereotype text box.</p>
     */
    private FigRect stereoLineBlinder;

    /**
     * The main constructor
     */
    public FigPackage() {
        setBigPort(new FigRect(x, y, width, height, null, null));

        //
        // Create a Body that reacts to double-clicks and jumps to a diagram.
        //
        body = new FigPackageFigText(x, y + textH, width, height - textH);

        body.setEditable(false);

        getNameFig().setBounds(x, y, width - indentX, textH + 2);
        getNameFig().setJustification(FigText.JUSTIFY_LEFT);

        getBigPort().setFilled(false);
        getBigPort().setLineWidth(0);

        // Set properties of the stereotype box. Make it 1 pixel higher than
        // before, so it overlaps the name box, and the blanking takes out both
        // lines. Initially not set to be displayed, but this will be changed
        // when we try to render it, if we find we have a stereotype.

        getStereotypeFigText().setExpandOnly(true);
        getStereotypeFig().setFilled(true);
        getStereotypeFig().setLineWidth(1);
        getStereotypeFigText().setEditable(false);
        getStereotypeFig().setHeight(STEREOHEIGHT + 1);
        getStereotypeFig().setVisible(false);

        // A thin rectangle to overlap the boundary line between stereotype
        // and name. This is just 2 pixels high, and we rely on the line
        // thickness, so the rectangle does not need to be filled. Whether to
        // display is linked to whether to display the stereotype.

        stereoLineBlinder =
            new FigRect(11, 10 + STEREOHEIGHT, 58, 2, Color.white, Color.white);
        stereoLineBlinder.setLineWidth(1);
        stereoLineBlinder.setVisible(false);

        // Mark this as newly created. This is to get round the problem with
        // creating figs for loaded classes that had stereotypes. They are
        // saved with their dimensions INCLUDING the stereotype, but since we
        // pretend the stereotype is not visible, we add height the first time
        // we render such a class. This is a complete fudge, and really we
        // ought to address how class objects with stereotypes are saved. But
        // that will be hard work.

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(getStereotypeFig());
        addFig(getNameFig());
        addFig(stereoLineBlinder);
        addFig(body);

        setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
        updateEdges();
    }

    /**
     * Contructor that hooks the fig into the UML element
     * @param gm ignored
     * @param node the UML element
     */
    public FigPackage(GraphModel gm, Object node) {
        this();
        setOwner(node);

        // If this figure is created for an existing package node in the
        // metamodel, set the figure's name according to this node. This is
        // used when the user click's on 'add to diagram' in the navpane.
        // Don't know if this should rather be done in one of the super
        // classes, since similar code is used in FigClass.java etc.
        // Andreas Rueckert <a_rueckert@gmx.net>
        if (ModelFacade.isAPackage(node)
	        && ModelFacade.getName(node) != null) {
            getNameFig().setText(ModelFacade.getName(node));
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
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.setStereotypeFig((FigText) it.next());
        figClone.setNameFig((FigText) it.next());
        figClone.stereoLineBlinder = (FigRect) it.next();
        figClone.body = (FigText) it.next();
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
        getStereotypeFig().setLineWidth(w);
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
        Object me = /*(MModelElement)*/ getOwner();

        if (me == null) {
            return;
        }

        Rectangle rect = getBounds();

        Object stereo = null;
        if (ModelFacade.getStereotypes(me).size() > 0) {
            stereo = ModelFacade.getStereotypes(me).iterator().next();
        }

        /* check if stereotype is defined */
        if ((stereo == null)
                || (ModelFacade.getName(stereo) == null)
                || (ModelFacade.getName(stereo).length() == 0)) {
            if (getStereotypeFig().isVisible()) {
                stereoLineBlinder.setVisible(false);
                getStereotypeFig().setVisible(false);
                //                rect.y      += STEREOHEIGHT;
                //                rect.height -= STEREOHEIGHT;
            }
        } else {
            /* we got stereotype */
            setStereotype(Notation.generateStereotype(this, stereo));

            if (!showStereotype) {
                stereoLineBlinder.setVisible(false);
                getStereotypeFig().setVisible(false);
            } else if (!getStereotypeFig().isVisible()) {
                if (showStereotype) {
                    stereoLineBlinder.setVisible(true);
                    getStereotypeFig().setVisible(true);

                    // Only adjust the stereotype height if we are not
                    // newly created. This gets round the problem of
                    // loading classes with stereotypes defined, which
                    // have the height already including the
                    // stereotype.

                    // if (!newlyCreated) {
                        // rect.y -= STEREOHEIGHT; rect.height +=
                        // STEREOHEIGHT;
                    // }
                }
            }
        }

        // Whatever happened we are no longer newly created, so clear the
        // flag. Then set the bounds for the rectangle we have defined.

        setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    ////////////////////////////////////////////////////////////////
    // user interaction methods


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
        if (aSize.height < 21) {
            aSize.height = 21;
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
     * <p>Sets the bounds, but the size will be at least the one returned by
     *   {@link #getMinimumSize()}.</p>
     *
     * <p>If the required height is bigger, then the additional height is
     *   equally distributed among all figs (i.e. compartments), such that the
     *   cumulated height of all visible figs equals the demanded height<p>.
     *
     * <p>Some of this has "magic numbers" hardcoded in. In particular there is
     *   a knowledge that the minimum height of a name compartment is 21
     *   pixels.</p>
     *
     * @param xa  Desired X coordinate of upper left corner
     *
     * @param ya  Desired Y coordinate of upper left corner
     *
     * @param w  Desired width of the FigClass
     *
     * @param h  Desired height of the FigClass
     */
    public void setBounds(int xa, int ya, int w, int h) {
        // Save our old boundaries (needed later), and get minimum size
        // info. "aSize will be used to maintain a running calculation of our
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

        if (minHeight < 21) {
            minHeight = 21;
        }

        // Now sort out the stereotype display. If the stereotype is displayed,
        // move the upper boundary of the name compartment up and set new
        // bounds for the name and the stereotype compatments and the
        // stereoLineBlinder that blanks out the line between the two

        int currentY = ya;

        if (getStereotypeFig().isVisible()) {
            currentY += STEREOHEIGHT;
        }

        getNameFig().setBounds(xa, currentY, newW - indentX, minHeight);
        getStereotypeFig().setBounds(xa, ya, newW - indentX, STEREOHEIGHT + 1);
        stereoLineBlinder.setBounds(
				     xa + 1,
				     ya + STEREOHEIGHT,
				     newW - 2 - indentX,
				     2);

        // Advance currentY to where the start of the body box is,
        // remembering that it overlaps the next box by 1 pixel. Calculate the
        // size of the attribute box, and update the Y pointer past it if it is
        // displayed.

        currentY += minHeight - 1; // -1 for 1 pixel overlap
        body.setBounds(xa, currentY, newW, newH + ya - currentY);

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
        Object mpackage = /*(MPackage)*/ getOwner();

        // Show ...
        ArgoJMenu showMenu = new ArgoJMenu("menu.popup.show");
        if (!showStereotype) {
            showMenu.add(new UMLAction(
                    Translator.localize("menu.popup.show.show-stereotype"),
                    UMLAction.NO_ICON)
	    {
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(
		 *         java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
		    showStereotype = true;
		    renderingChanged();
		    damage();
		}
	    });
        } else {
            showMenu.add(new UMLAction(
                    Translator.localize("menu.popup.show.hide-stereotype"),
                    UMLAction.NO_ICON)
	    {
		/**
		 * @see java.awt.event.ActionListener#actionPerformed(
		 *         java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent ae) {
		    showStereotype = false;
		    renderingChanged();
		    damage();
		}
	    });
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


    class FigPackageFigText extends FigText {
	public FigPackageFigText(int xa, int ya, int w, int h) {
	    super(xa, ya, w, h);
	}

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

		    /* if we get here then we didnt get the
		     * default diagram*/
		    if (lFirst != null) {
			me.consume();
			super.mouseClicked(me);

			TargetManager.getInstance().setTarget(lFirst);
			return;
		    } else {
			/* try to create a new class diagram */
			me.consume();
			super.mouseClicked(me);
			try {
			    String nameSpace;
			    if (lNS != null
				    && ModelFacade.getName(lNS) != null)
				nameSpace = ModelFacade.getName(lNS);
			    else
				nameSpace = "(anon)";

			    String dialogText =
				"Add new class diagram to "
				+ nameSpace
				+ "?";
			    int option =
				JOptionPane.showConfirmDialog(
					null,
					dialogText,
					"Add new class diagram?",
					JOptionPane.YES_NO_OPTION);
			    if (option == JOptionPane.YES_OPTION) {
				ArgoDiagram lNew =
				    new UMLClassDiagram(lNS);
				String diagramName =
				    lsDefaultName + "_" + lNew.getName();

				lP.addMember(lNew);

				TargetManager.getInstance().setTarget(lNew);
				/* change prefix */
				lNew.setName(diagramName);
				ExplorerEventAdaptor.getInstance()
				                    .structureChanged();
			    }
			} catch (Exception ex) {
			    LOG.error(ex);
			}

			return;
		    } /*if new*/

		} /*if package */
	    } /* if doubleclicks */
	    super.mouseClicked(me);
	}
    }
} /* end class FigPackage */
