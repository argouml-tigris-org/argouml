package uci.uml.ui;

import java.util.*;
import java.beans.*;

import uci.uml.Model_Management.*;
import uci.uml.Foundation.Core.*;

public class Trash {
  public static Trash TheTrash = new Trash();

  public Vector _contents = new Vector();
  public Model Trash_Model = new Model("Trash ");

  private Trash() { }

  public void addItemFrom(Object obj, Vector places) {
    TrashItem ti = new TrashItem(obj, places);
    if (_contents.contains(ti)) return;
    _contents.addElement(ti);
    if (obj instanceof ModelElement) {
      ModelElement me = (ModelElement) obj;
      try { Trash_Model.addOwnedElement(me.getElementOwnership()); }
      catch (PropertyVetoException pve) {
	System.out.println("Trash had a PropertyVetoException");
      }
    }
  }

  public void removeItem(Object obj) {
    _contents.removeElement(new TrashItem(obj, null));
  }
  
} /* end class Trash */

class TrashItem {

  Object _item;
  Vector _places;

  TrashItem(Object item, Vector places) {
    _item = item;
    _places = places;
  }

  public boolean equals(Object o) {
    if (o instanceof TrashItem) {
      TrashItem ti = (TrashItem) o;
      return ti._item == _item;
    }
    return false;
  }

  public int hashCode() { return _item.hashCode(); }
  
} /* end class TrashItem */
