// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import org.argouml.application.api.Argo;
import org.argouml.application.api.QuadrantPanel;
import org.argouml.ui.explorer.ExplorerTree;
import org.argouml.ui.explorer.PerspectiveComboBox;
import org.argouml.ui.explorer.PerspectiveManager;
import org.argouml.ui.explorer.ExplorerTreeModel;

import org.tigris.toolbar.ToolBar;

/**
 * The upper-left pane of the main Argo/UML window, contains a tree view
 * of the UML model.
 *
 * <p>The model can be viewed from different tree "Perspectives".
 *
 * <p>The public interface of this class provides the following:
 * <ol>
 *  <li>model changed notification via forceUpdate()</li>
 * </ol>
 *
 * <p>Perspectives are now built in the Perspective Manager.
 *
 * $Id$
 */
public class NavigatorPane
    extends JPanel
    implements QuadrantPanel
{

    protected transient Logger cat = Logger.getLogger(this.getClass());

    private static final String BUNDLE = "statusmsg";
    
    /** for collecting user statistics */
    public static int _clicksInNavPane = 0;
    /** for collecting user statistics */
    public static int _navPerspectivesChanged = 0;

    /** to be romved once forceUpdate() is also removed */
    ExplorerTree tree;
    
    ////////////////////////////////////////////////////////////////
    // constructors

    private static NavigatorPane INSTANCE = null;

	private static boolean instanceSet = false;
    
    /** Don't automatically instantiate the instance.
     * 
     * @return the singleton
     */
	public static NavigatorPane getInstance() {
		if (! instanceSet) {
			INSTANCE = new NavigatorPane();
			instanceSet = true;
		}
		return INSTANCE;
	}
    
	/** Allow setting of the navigator pane instance.
	 * Currently this is only applicable for unit tests.
	 * 
	 * @param pane
	 * @deprecated without replacement - this is a temporary hack until the model is cleaned up
	 */
	public static void setInstance(NavigatorPane pane) {
		INSTANCE = pane;
		instanceSet = true;
	}
    
    /**
     * Constructs a new navigator panel.
     * 
     * <p>This panel consists of a Combobox to select a navigation
     * perspective, a JTree to display the UML model, some history
     * (back and forward arrows) buttons that are currently disabled,
     * and a configuration dialog to tailor the perspectives (but this
     * is not saved).
     */
    private NavigatorPane() {
        this(SplashScreen.getDoSplash());
    }
    
    /**
     * Constructs a new navigator panel.
     * 
     * <p>This panel consists of a Combobox to select a navigation
     * perspective, a JTree to display the UML model, some history
     * (back and forward arrows) buttons that are currently disabled,
     * and a configuration dialog to tailor the perspectives (but this
     * is not saved).
     * @deprecated 0.15 delete in 0.16 use NavigatorPane.getInstance()
     * instead.
     */
    public NavigatorPane(boolean doSplash) {
        
        JComboBox combo = new PerspectiveComboBox();
        tree = new DnDNavigatorTree();
        ToolBar toolbar = new ToolBar();
        
        toolbar.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        toolbar.setFloatable(false);
        toolbar.add(combo);
        
        combo.addItemListener((ExplorerTreeModel)tree.getModel());

        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(tree), BorderLayout.CENTER);

        if (doSplash) {
            SplashScreen splash = SplashScreen.getInstance();
	    splash.getStatusBar().showStatus(Argo.localize(
                    BUNDLE, 
		    "statusmsg.bar.making-navigator-pane-perspectives"));
            splash.getStatusBar().showProgress(25);
        }

        PerspectiveManager.getInstance().loadDefaultPerspectives();
    }

    ////////////////////////////////////////////////////////////////
    // methods

    /**
     * Does nothing.
     *
     * Notification from Argo that the model has changed and
     * the Tree view needs updating.
     *
     * TODO: More specific information needs to be provided, it is 
     * expesive to update the whole tree.
     *
     * @see org.argouml.uml.ui.ActionRemoveFromModel
     * @see org.argouml.uml.ui.ActionAddDiagram
     * @see org.argouml.uml.ui.foundation.core.PropPanelGeneralization
     * @see org.argouml.uml.ui.UMLReflectionListModel
     */
    public void forceUpdate() {
    }

    /** sets minimum size to 120,100 */
    public Dimension getMinimumSize() {
        return new Dimension(120, 100);
    }

    /** QuadrantPanel implementation */
    public int getQuadrant() {
        return Q_TOP_LEFT;
    }

} /* end class NavigatorPane */
