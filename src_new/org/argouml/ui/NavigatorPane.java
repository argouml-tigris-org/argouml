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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.QuadrantPanel;
import org.argouml.i18n.Translator;
import org.argouml.ui.explorer.ExplorerTree;
import org.argouml.ui.explorer.ExplorerTreeModel;
import org.argouml.ui.explorer.ExportExplorer;
import org.argouml.ui.explorer.NameOrder;
import org.argouml.ui.explorer.PerspectiveComboBox;
import org.argouml.ui.explorer.PerspectiveManager;
import org.argouml.ui.explorer.TypeThenNameOrder;
import org.argouml.ui.explorer.PerspectiveConfigurator;
import org.argouml.uml.ui.UMLAction;

import org.tigris.toolbar.ToolBar;

/**
 * The upper-left pane of the main ArgoUML window, contains a tree view
 * of the UML model. Currently named "Explorer" instead of "Navigator".
 *
 * <p>The model can be viewed from different tree "Perspectives".
 *
 * <p>Perspectives are now built in the Perspective Manager.
 *
 * @stereotype singleton
 */
public class NavigatorPane
    extends JPanel
    implements QuadrantPanel {
    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The NavigatorPane instance. This can be a NavigatorPane or 
     * <tt>null</tt>.
     */
    private static NavigatorPane theInstance = null;
    private static boolean theInstanceIsSet = false;

    /** 
     * Don't automatically instantiate the instance.
     * 
     * @return the singleton or <tt>null</tt> if the NavigatorPane was 
     * 	       explicitly set to <tt>null</tt>.
     */
    public static NavigatorPane getInstance() {
	if (!theInstanceIsSet) {
	    theInstance = new NavigatorPane();
	    theInstanceIsSet = true;
	}
	return theInstance;
    }
    
    /**
     * Allow setting of the navigator pane instance.
     * Currently this is only applicable for unit tests that sets it to 
     * <tt>null</tt>.
     * 
     * @param pane A new NavigatorPane or <tt>null</tt>.
     * @deprecated without replacement - this is a temporary hack
     * until the model is cleaned up
     */
    public static void setInstance(NavigatorPane pane) {
	theInstance = pane;
	theInstanceIsSet = true;
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
     * perspective, a combobox to select ordering,
     * a JTree to display the UML model, 
     * and a configuration dialog to tailor the perspectives.
     */
    private NavigatorPane(boolean doSplash) {
        
        JComboBox combo = new PerspectiveComboBox();
        JComboBox orderByCombo = new JComboBox();
        ExplorerTree tree = new ExportExplorer(); //DnDExplorerTree();
        ToolBar toolbar = new ToolBar();
        
        toolbar.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        toolbar.setFloatable(false);
        toolbar.add(new ActionPerspectiveConfig());
        toolbar.add(combo);
        
        ToolBar toolbar2 = new ToolBar();
        
        toolbar2.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        toolbar2.setFloatable(false);
        
        orderByCombo.addItem(new TypeThenNameOrder());
        orderByCombo.addItem(new NameOrder());

        toolbar2.add(orderByCombo);

        JPanel toolbarpanel = new JPanel();
        toolbarpanel.setLayout(new BorderLayout());
        toolbarpanel.add(toolbar, BorderLayout.NORTH);
        toolbarpanel.add(toolbar2, BorderLayout.SOUTH);
        
        setLayout(new BorderLayout());
        add(toolbarpanel, BorderLayout.NORTH);
        add(new JScrollPane(tree), BorderLayout.CENTER);

        if (doSplash) {
            SplashScreen splash = SplashScreen.getInstance();
	    splash.getStatusBar().showStatus(Translator.localize(
		    "statusmsg.bar.making-navigator-pane-perspectives"));
            splash.getStatusBar().showProgress(25);
        }
        
        combo.addItemListener((ExplorerTreeModel) tree.getModel());
        orderByCombo.addItemListener((ExplorerTreeModel) tree.getModel());
        PerspectiveManager.getInstance().loadUserPerspectives();
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
     */
    public void forceUpdate() {
    }

    /**
     * @see java.awt.Component#getMinimumSize()
     *
     * sets minimum size to 120,100
     */
    public Dimension getMinimumSize() {
        return new Dimension(120, 100);
    }

    /**
     * @see org.argouml.application.api.QuadrantPanel#getQuadrant()
     */
    public int getQuadrant() {
        return Q_TOP_LEFT;
    }
    
    class ActionPerspectiveConfig extends UMLAction {
        
        public ActionPerspectiveConfig() {
	    super("action.configure-perspectives");
	}
        
        public void actionPerformed(ActionEvent ae) {
            
            PerspectiveConfigurator ncd =
		new PerspectiveConfigurator(ProjectBrowser.getInstance());
            ncd.setVisible(true);
        }
    }
    
} /* end class NavigatorPane */
