// $Id: AbstractArgoJPanel.java 12660 2007-05-26 16:22:57Z tfmorris $
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.tigris.swidgets.Orientable;
import org.tigris.swidgets.Orientation;

/**
 * A subclass of JPanel that can act as a tab in the DetailsPane or
 * MultiEditorPane. Added functionality:<p>
 *
 * Spawning: When the tab is double-clicked, this JPanel will generate a
 * separate window of the same size and with the same contents. This is almost
 * like "tearing off" a tab.<p>
 *
 * TODO: Spawning of windows disabled in spawn()<p>
 *
 * Title: This JPanel keeps track of its own title.<p>
 *
 * Orientation: This JPanel is Orientable.<p>
 *
 * Cloning: This JPanel may be cloned.<p>
 *
 * This class used to be named TabSpawnable.
 * Renamed since it is not a Tab, but a Panel, and being spawnable is
 * not any more its main purpose.
 */
public abstract class AbstractArgoJPanel extends JPanel
    implements Cloneable, Orientable {
    /**
     * Logger.
     */
    private static final Logger LOG =
            Logger.getLogger(AbstractArgoJPanel.class);

    private static final int OVERLAPP = 30;

    private String title = "untitled";

    /**
     * if true, remove tab from parent JTabbedPane.
     */
    private boolean tear = false;

    private Orientation orientation;

    /**
     * @return the orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }
    ////////////////////////////////////////////////////////////////
    // constructor

    /**
     * The constructor.
     *
     */
    public AbstractArgoJPanel() {
        this("untitled", false);
    }

    /**
     * The constructor.
     *
     * @param tag the name
     */
    // TODO: Investigate in what cases is the tag a localized string
    // and in what cases is it a key to be localized?
    public AbstractArgoJPanel(String tag) {
        this(tag, false);
    }

    /**
     * The constructor.
     *
     * @param tag The name (to be localized).
     * @param t if true, remove tab from parent JTabbedPane
     */
    // TODO: Investigate in what cases is the tag a localized string
    // and in what cases is it a key to be localized? - Harold Braun via Linus 20070521
    // Answer: There are three cases: 1) unlocalized string, 2) localized
    // string, and 3) property/tag name.  Of the 100+ subclasses, the
    // overwhelming majority fall into category #1.  Category #3 includes
    // most of the Tab* panels.  Category #2 includes the PropPanels for 
    // Generalization and all the diagrams, as well as TabDocumentation
    // and TabStereotype.  The breakdown is approximately 80+/10/6.  The
    // analysis is easy.  Fixing the 90+ property panels will be more work.
    // - tfm - 20070517
    public AbstractArgoJPanel(String tag, boolean t) {
        setTitle(tag);
        tear = t;
    }

    /**
     * This is not a real clone since it doesn't copy anything from the object
     * it is cloning. The {@link #spawn} method copies the title and in
     * some cases also the Target.
     *
     * @return the new object or null if not possible.
     */
    public Object clone() {
        try {
            return this.getClass().newInstance();
        } catch (Exception ex) {
            LOG.error("exception in clone()", ex);
        }
        return null;
    }

    /*
     * @see org.tigris.swidgets.Orientable#setOrientation(Orientation)
     */
    public void setOrientation(Orientation o) {
        this.orientation = o;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return The key (to be localized) for the title of the panel.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param t The key (to be localized) of the title.
     */
    public void setTitle(String t) {
        title = t;
    }

    ////////////////////////////////////////////////////////////////
    // actions

    /**
     * This should take its inspiration from
     * {@link org.tigris.gef.base.CmdSpawn}.<p>
     *
     * The spawned/cloned tab will be a JFrame. Currently this feature is
     * disabled for ArgoUML, except for the find dialog.
     * Code should behave though as if spawning might work at a
     * later stage.
     *
     * @return a copy of the frame or null if not clone-able.
     */
    public AbstractArgoJPanel spawn() {

        JDialog f = new JDialog(ArgoFrame.getInstance());
        f.getContentPane().setLayout(new BorderLayout());
        f.setTitle(Translator.localize(title));
        AbstractArgoJPanel newPanel = (AbstractArgoJPanel) clone();
        if (newPanel == null) {
	    return null; //failed to clone
	}

//        if (newPanel instanceof TabToDo) {
//            TabToDo me = (TabToDo) this;
//            TabToDo it = (TabToDo) newPanel;
//            it.setTarget(me.getTarget());
//        } else if (newPanel instanceof TabModelTarget) {
//            TabModelTarget me = (TabModelTarget) this;
//            TabModelTarget it = (TabModelTarget) newPanel;
//            it.setTarget(me.getTarget());
//        } else if (newPanel instanceof TabDiagram) {
//            TabDiagram me = (TabDiagram) this;
//            TabDiagram it = (TabDiagram) newPanel;
//            it.setTarget(me.getTarget());
//        }

        newPanel.setTitle(Translator.localize(title));

        f.getContentPane().add(newPanel, BorderLayout.CENTER);
        Rectangle bounds = getBounds();
        bounds.height += OVERLAPP * 2;
        f.setBounds(bounds);

        Point loc = new Point(0, 0);
        SwingUtilities.convertPointToScreen(loc, this);
        loc.y -= OVERLAPP;
        f.setLocation(loc);
        f.setVisible(true);

        if (tear && (getParent() instanceof JTabbedPane)) {
	    ((JTabbedPane) getParent()).remove(this);
	}

        return newPanel;

    }

} /* end class AbstractArgoJPanel */
