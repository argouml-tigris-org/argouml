// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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
