/**
 * LayerPerspectiveMutable.java
 *
 * A LayerPerspective with an underlying MutableGraphModel.
 * As figures are added and removed the underlying MutableGraphModel is
 * updated. 
 * 
 * @author Eugenio Alvarez
 * Data Access Technologies
 *
 */

package uci.gef;

import java.util.*;
import java.awt.*;
import uci.graph.*;


public class LayerPerspectiveMutable extends LayerPerspective {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The underlying connected graph to be visualized. */
  MutableGraphModel _mgm;

  ////////////////////////////////////////////////////////////////
  // constructors

  public LayerPerspectiveMutable(String name, MutableGraphModel mgm) {
    super(name,(GraphModel)mgm);
    _mgm = mgm;
  }


  ////////////////////////////////////////////////////////////////
  // accessors

  public GraphModel getGraphModel() {
    return(GraphModel)getMutableGraphModel(); 
  }
  public void setGraphModel(GraphModel gm) {
    setMutableGraphModel((MutableGraphModel)gm);
  }

  public MutableGraphModel getMutableGraphModel() { return _mgm; }
  public void setMutableGraphModel(MutableGraphModel mgm) {
    super.setGraphModel((GraphModel)mgm);
    _mgm = mgm;
  }    

  ////////////////////////////////////////////////////////////////
  // Layer API

  public void add(Fig f) { 
    Object owner = f.getOwner();
    super.add(f);
    if ( owner != null && _mgm.canAddNode(owner))
      _mgm.addNode(owner);
    // FigEdges are added by the underlying MutableGraphModel.
  }

  public void remove(Fig f) { 
    super.remove(f);
    Object owner = f.getOwner();
    if (owner != null) {
      if (f instanceof FigEdge && _mgm.containsEdge(owner)) 
        _mgm.removeEdge(owner); 
      else if (_mgm.containsNode(owner)) 
        _mgm.removeNode(owner);
    }
  }

} /* end class LayerPerspectiveMutable */

