// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.cognitive.ui.TabToDoTarget;
import org.argouml.swingext.Orientable;
import org.argouml.swingext.Orientation;
import org.argouml.uml.diagram.ui.TabDiagram;
import org.argouml.uml.ui.TabModelTarget;

/** A subclass of JPanel that can act as a tab in the DetailsPane or
 *  MultiEditorPane.  When the tab is double-clicked, this JPanel will
 *  generate a separate window of the same size and with the same
 *  contents.  This is almost like "tearing off" a tab.
 */
public class TabSpawnable extends JPanel implements Cloneable, Orientable {
        
    private Category cat = Category.getInstance(TabSpawnable.class);
    
    public final int OVERLAPP = 30;
  
    private static final String BUNDLE = "UMLMenu";

    ////////////////////////////////////////////////////////////////
    // instance variables
    
    String _title = "untitled";
  
    /**
     * if true, remove tab from parent JTabbedPane
     */
    boolean _tear = false;
    protected Orientation orientation;

    ////////////////////////////////////////////////////////////////
    // constructor

    public TabSpawnable() { this("untitled", false); }

    public TabSpawnable(String tag) { this(tag, false); }

    public TabSpawnable(String tag, boolean tear) {
	setTitle(tag);
	_tear = tear;
    }

    /** This is not a real clone since it doesn't copy anything from the
     * object it is cloning.
     * The
     * @see #spawn
     * method copies the title and in some cases when we are a
     * @see TabToDoTarget
     * or
     * @see TabModelTarget
     * also the Target.
     *
     * @return the new object or null if not possible.
     */
    public Object clone() {
	try { return this.getClass().newInstance(); }
	catch (Exception ex) {
	    cat.error("exception in clone()", ex);
	}
	return null;
    }
  
    /*
     * Set the orientation of the property panel
     * @param orientation the new orientation for this preoprty panel
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }


    ////////////////////////////////////////////////////////////////
    // accessors

    public String getTitle() { return _title; }
    public void setTitle(String t) { _title = t; }


    ////////////////////////////////////////////////////////////////
    // actions
  
    /**
     * this should take its inspiration from org.tigris.gef.base.CmdSpawn
     *
     * <p>The spawned/cloned tab will be a JFrame.
     *
     * @return a copy of the frame or null if not clone-able.
     */
    public TabSpawnable spawn() {

	JDialog  f = new JDialog(ProjectBrowser.getInstance());    
	f.getContentPane().setLayout(new BorderLayout());
	f.setTitle(Argo.localize(BUNDLE, _title));
	TabSpawnable newPanel = (TabSpawnable) clone();
	if (newPanel == null)
	    return null; //failed to clone

	if (newPanel instanceof TabToDoTarget) {
	    TabToDoTarget me = (TabToDoTarget) this;
	    TabToDoTarget it = (TabToDoTarget) newPanel;
	    it.setTarget(me.getTarget());
	}
	else if (newPanel instanceof TabModelTarget) {
	    TabModelTarget me = (TabModelTarget) this;
	    TabModelTarget it = (TabModelTarget) newPanel;
	    it.setTarget(me.getTarget());
	}
	else if (newPanel instanceof TabDiagram) {
	    TabDiagram me = (TabDiagram) this;
	    TabDiagram it = (TabDiagram) newPanel;
	    it.setTarget(me.getTarget());
	}
    
	newPanel.setTitle(Argo.localize(BUNDLE, _title));
    
	f.getContentPane().add(newPanel, BorderLayout.CENTER);
	Rectangle bounds = getBounds();
	bounds.height += OVERLAPP * 2;
	f.setBounds(bounds);

	Point loc = new Point(0, 0);
	SwingUtilities.convertPointToScreen(loc, this);
	loc.y -= OVERLAPP;
	f.setLocation(loc);
	f.setVisible(true);

	if (_tear && (getParent() instanceof JTabbedPane))
	    ((JTabbedPane) getParent()).remove(this);

	return newPanel;
    }
  
} /* end class TabSpawnable */


