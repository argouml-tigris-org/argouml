// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


package uci.gef;

import java.util.*;
import java.awt.*;
import java.beans.*;

import uci.gef.*;
import uci.graph.*;
import uci.ui.*;

/** A diagram is just combination of a GraphModel, a Layer, and a
    title. The GraphModel stores the connected graph representation,
    without any graphics. The Layer stores all the Figs. */
public class Diagram {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected String _name = "no title set";
  protected String _comments = "(no comments given)";
  protected LayerPerspective _lay;
  protected ToolBar _toolBar;
  public transient Vector vetoListeners;

  ////////////////////////////////////////////////////////////////
  // constructors

  public Diagram() {
    this("untitled");
  }

  public Diagram(String name) {
    this(name, new DefaultGraphModel());
  }

  public Diagram(String name, GraphModel gm) {
    this(name, gm, new LayerPerspective(name, gm));
  }

  public Diagram(String name, GraphModel gm, LayerPerspective lay) {
    _name = name;
    _lay = lay;
    setGraphModel(gm);
    initToolBar();
  }

  protected void initToolBar() {
    _toolBar = new PaletteFig();
  }
  
  ////////////////////////////////////////////////////////////////
  // accessors

  public ToolBar getToolBar() { return _toolBar; }
  public void setToolBar(ToolBar tb) { _toolBar = tb; }

  public String getComments() { return _comments; }
  public void setComments(String c) throws PropertyVetoException {
    fireVetoableChange("comments", _comments, c);
    _comments = c;
  }

  public String getName() { return _name; }
  public void setName(String n) throws PropertyVetoException {
    fireVetoableChange("name", _name, n);
    _name = n;
  }

  public void setGraphModel(GraphModel gm) { getLayer().setGraphModel(gm); }
  public GraphModel getGraphModel() { return getLayer().getGraphModel(); }

  public LayerPerspective getLayer() { return _lay; }
  public void setLayer(LayerPerspective lay) { _lay = lay; }


  ////////////////////////////////////////////////////////////////
  // VetoableChangeSupport

  public synchronized void
  addVetoableChangeListener(VetoableChangeListener listener) {
    if (vetoListeners == null)
      vetoListeners = new Vector();
    vetoListeners.removeElement(listener);
    vetoListeners.addElement(listener);
  }

  public synchronized void
  removeVetoableChangeListener(VetoableChangeListener listener) {
    if (vetoListeners == null) return;
    vetoListeners.removeElement(listener);
  }

  public void fireVetoableChange(String propertyName, 
				 boolean oldValue, boolean newValue) 
       throws PropertyVetoException {
	 fireVetoableChange(propertyName,
			    new Boolean(oldValue),
			    new Boolean(newValue));
  }

  public void fireVetoableChange(String propertyName, 
				 int oldValue, int newValue) 
       throws PropertyVetoException {
	 fireVetoableChange(propertyName,
			    new Integer(oldValue),
			    new Integer(newValue));
  }



  public void fireVetoableChange(String propertyName, 
				 Object oldValue, Object newValue) 
       throws PropertyVetoException {
	 if (vetoListeners == null) return;
    if (oldValue != null && oldValue.equals(newValue)) return;
    PropertyChangeEvent evt =
      new PropertyChangeEvent(this,
			      propertyName, oldValue, newValue);
    try {
      for (int i = 0; i < vetoListeners.size(); i++) {
	VetoableChangeListener target = 
	  (VetoableChangeListener) vetoListeners.elementAt(i);
	target.vetoableChange(evt);
      }
    } catch (PropertyVetoException veto) {
      // Create an event to revert everyone to the old value.
      evt = new PropertyChangeEvent(this, propertyName, newValue, oldValue);
      for (int i = 0; i < vetoListeners.size(); i++) {
	try {
	  VetoableChangeListener target =
	    (VetoableChangeListener) vetoListeners.elementAt(i);
	  target.vetoableChange(evt);
	} catch (PropertyVetoException ex) {
	  // We just ignore exceptions that occur during reversions.
	}
      }
      // And now rethrow the PropertyVetoException.
      throw veto;
    }    
  }

} /* end class Diagram */
