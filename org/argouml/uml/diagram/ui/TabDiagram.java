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

// $Id$

package org.argouml.uml.diagram.ui;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Category;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.TabSpawnable;
import org.argouml.uml.ui.TabModelTarget;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModeSelect;
import org.tigris.gef.event.GraphSelectionEvent;
import org.tigris.gef.event.GraphSelectionListener;
import org.tigris.gef.event.ModeChangeEvent;
import org.tigris.gef.event.ModeChangeListener;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.ui.ToolBar;


/**
 * The TabDiagram is the tab in the multieditorpane that holds a diagram. The 
 * TabDiagram consists of a JGraph (with the figs) and of a toolbar. It is
 * possible to spawn objects of this class into a dialog via the spawn method of
 * its parent.
 */
public class TabDiagram extends TabSpawnable
implements TabModelTarget, GraphSelectionListener, ModeChangeListener {
    
    protected static Category cat = 
        Category.getInstance(TabDiagram.class);
    
  ////////////////////////////////////////////////////////////////
  // instance variables
    
  /**
   * the diagram object
   */
  protected UMLDiagram _target;
  
  /**
   * The GEF JGraph in where the figs are painted
   */
  protected JGraph _jgraph;
  
  /**
   * used but there doesn't appear to be a purpose.
   */
  protected boolean _shouldBeEnabled = true;
  
  /**
   * the GEF toolbar that is positioned just above
   * the diagram. is added to diagramPanel
   */
  protected ToolBar _toolBar;

  ////////////////////////////////////////////////////////////////
  // constructors
  
  /**
   * calls the other constructor.
   */
  public TabDiagram() {
        this("Diagram");
    }

    public TabDiagram(String tag) {
        super(tag);
        setLayout(new BorderLayout());
        _jgraph = new JGraph();
        _jgraph.setDrawingSize((612 - 30) * 2, (792 - 55 - 20) * 2);
        // TODO: should update to size of diagram contents

        Globals.setStatusBar(ProjectBrowser.TheInstance);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        p.add(_jgraph, BorderLayout.CENTER);
        add(p, BorderLayout.CENTER);
        _jgraph.addGraphSelectionListener(this);
        _jgraph.addModeChangeListener(this);
    }

    /**
     * The clone method that should clone the JGraph with it's contents and
     * the toolbar with it's contents. Since both JGraph as the toolbar are
     * coming from the GEF framework, cloning them will be hard work and should
     * actually not be placed here but in a clone method of the JGraph and the
     * Toolbar.
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        // next statement gives us a clone JGraph but not a cloned Toolbar
        TabDiagram newPanel = new TabDiagram();
        // next statement moves the toolbar to the newPanel
        if (_target != null) {
            newPanel.setTarget(_target);
        }
        newPanel.setToolBar(_target.getToolBar());           
        return newPanel;
        
    }

    /**
     * Sets the target of the tab. The target should allways be an instance of
     * UMLDiagram
     * @param t
     */
    public void setTarget(Object t) {
        if (!(t instanceof UMLDiagram)) {
            _shouldBeEnabled = false;
            cat.warn("target is null in set target or not an instance of UMLDiagram");
            return;
        }
        UMLDiagram target = (UMLDiagram)t;
        _shouldBeEnabled = true;
        setToolBar(target.getToolBar());
        _jgraph.setDiagram(target);
        _target = target;                
    }
    
    public Object getTarget() {
        return _target;
    }
    
    public ToolBar getToolBar() {
        return _toolBar;
    }

    public void refresh() {
        setTarget(_target);
    }

    public boolean shouldBeEnabled() {
        return _shouldBeEnabled;
    }

  ////////////////////////////////////////////////////////////////
  // accessors
    

  

  
 


    public JGraph getJGraph() {
        return _jgraph;
    }

    public void setVisible(boolean b) {
        super.setVisible(b);
        getJGraph().setVisible(b);
    }

    ////////////////////////////////////////////////////////////////
    // events

    public void selectionChanged(GraphSelectionEvent gse) {
        Vector sels = gse.getSelections();
        ProjectBrowser pb = ProjectBrowser.TheInstance;

        if (sels.size() == 1)
            pb.setTarget(sels.elementAt(0));
        else
            pb.setTarget(null);
    }

    public void removeGraphSelectionListener(GraphSelectionListener listener) {
        _jgraph.removeGraphSelectionListener(listener);
    }

    public void modeChange(ModeChangeEvent mce) {
        cat.debug("TabDiagram got mode change event");
        if (!Globals.getSticky() && Globals.mode() instanceof ModeSelect) {
            if (_target instanceof UMLDiagram)
                _target.getToolBar().unpressAllButtons();
        }
    }

    public void removeModeChangeListener(ModeChangeListener listener) {
        _jgraph.removeModeChangeListener(listener);
    }
    
    /**
     * Sets the toolbar. Adds the toolbar to the north borderlayout position of
     * the diagram.
     * @param toolbar
     */
    public void setToolBar(ToolBar toolbar) {
        if (!Arrays.asList(getComponents()).contains(toolbar) ) {
            if (_target != null) {
                remove(((UMLDiagram)getTarget()).getToolBar());
            }    
            add(toolbar, BorderLayout.NORTH);
            
            invalidate();
            validate();
            repaint();
        }
    }

}
