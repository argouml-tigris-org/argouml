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




package uci.gef;

import java.util.*;
import java.awt.*;
import java.beans.*;

import uci.graph.*;
import uci.ui.*;
import uci.util.*;

/** A diagram is just combination of a GraphModel, a Layer, and a
    title. The GraphModel stores the connected graph representation,
    without any graphics. The Layer stores all the Figs. */
public class Diagram implements java.io.Serializable, GraphListener {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected String _name = "no title set";
  protected String _comments = "(no comments given)";
  protected LayerPerspective _lay;
  protected transient ToolBar _toolBar;
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
    //initToolBar();
  }

  protected void initToolBar() {
    _toolBar = new PaletteFig();
  }

  public void initialize(Object owner) {
    /* do nothing by default */
  }
  
  ////////////////////////////////////////////////////////////////
  // accessors

  public ToolBar getToolBar() {
    if (_toolBar == null) initToolBar();
    return _toolBar;
  }
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

  public String getClassAndModelID() {
    return getClass().getName();
  }

  public GraphModel getGraphModel() { return getLayer().getGraphModel(); }
  public void setGraphModel(GraphModel gm) {
    GraphModel oldGM = getLayer().getGraphModel();
    if (oldGM != null) oldGM.removeGraphEventListener(this);
    getLayer().setGraphModel(gm);
    gm.addGraphEventListener(this);
  }

  public LayerPerspective getLayer() { return _lay; }
  public void setLayer(LayerPerspective lay) { _lay = lay; }

  public int countContained(VectorSet owners) {
    int count = 0;
    int numOwners = owners.size();
    Vector nodes = getNodes();
    for (int i = 0; i < nodes.size(); i++)
      for (int j = 0; j < numOwners; j++)
	if (nodes.elementAt(i) == owners.elementAt(j)) count++;
    Vector edges = getEdges();
    for (int i = 0; i < edges.size(); i++)
      for (int j = 0; j < numOwners; j++)
	if (edges.elementAt(i) == owners.elementAt(j)) count++;
    Vector figs = getLayer().getContents();
    for (int i = 0; i < figs.size(); i++)
      for (int j = 0; j < numOwners; j++)
	if (figs.elementAt(i) == owners.elementAt(j)) count++;
    return count;
  }

  public Vector getNodes() {
    // needs-more-work: should just do getGraphModel().getNodes()
    // but that is not updated when the diagram is loaded
    Vector res = new Vector();
    Vector figs = getLayer().getContents();
    int size = figs.size();
    for (int i = 0; i < size; i++)
      if (figs.elementAt(i) instanceof FigNode)
	res.addElement(((FigNode)figs.elementAt(i)).getOwner());
    return res;
  }

  public Vector getEdges() {
    // needs-more-work: should just do getGraphModel().getEdges()
    // but that is not updated when the diagram is loaded
    Vector res = new Vector();
    Vector figs = getLayer().getContents();
    int size = figs.size();
    for (int i = 0; i < size; i++)
      if (figs.elementAt(i) instanceof FigEdge)
	res.addElement(((FigEdge)figs.elementAt(i)).getOwner());
    return res;
  }

  ////////////////////////////////////////////////////////////////
  // accessors on the Layer
  
  public void add(Fig f) { _lay.add(f); }
  public void remove(Fig f) { _lay.remove(f); }
  public void removeAll(Fig f) { _lay.removeAll(); }
  public Enumeration elements() { return _lay.elements(); }
  public Fig hit(Rectangle r) { return _lay.hit(r); }
  public Enumeration elementsIn(Rectangle r) { return _lay.elementsIn(r); }
  public Fig presentationFor(Object obj) { return _lay.presentationFor(obj); }
  public void sendToBack(Fig f) { _lay.sendToBack(f); }
  public void bringForward(Fig f) { _lay.bringForward(f); }
  public void sendBackward(Fig f) { _lay.sendBackward(f); }
  public void bringToFront(Fig f) { _lay.bringToFront(f); }
  public void reorder(Fig f, int function) { _lay.reorder(f, function); }

  ////////////////////////////////////////////////////////////////
  // graph event handlers

  public void nodeAdded(GraphEvent e) {
    try { fireVetoableChange("nodeAdded", null, null); }
    catch (PropertyVetoException pve) { }
  }
  public void edgeAdded(GraphEvent e) {
    try { fireVetoableChange("edgeAdded", null, null); }
    catch (PropertyVetoException pve) { }
  }

  public void nodeRemoved(GraphEvent e) {
    try { fireVetoableChange("nodeRemoved", null, null); }
    catch (PropertyVetoException pve) { }
  }

  public void edgeRemoved(GraphEvent e) {
    try { fireVetoableChange("edgeRemoved", null, null); }
    catch (PropertyVetoException pve) { }
  }

  public void graphChanged(GraphEvent e) {
    try { fireVetoableChange("graphChanged", null, null); }
    catch (PropertyVetoException pve) { }
  }


  ////////////////////////////////////////////////////////////////
  // VetoableChangeSupport


  public void preSave() { _lay.preSave(); }
  public void postSave() { _lay.postSave(); }
  public void postLoad() { _lay.postLoad(); }

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

  static final long serialVersionUID = -3642645497287401439L;

} /* end class Diagram */
