/* NovoSoft UML API for Java. Version 0.4.19
 * Copyright (C) 1999, 2000, NovoSoft.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text of license can be also found 
 * at http://www.gnu.org/copyleft/lgpl.html
 */

package ru.novosoft.uml;

//import com.sun.java.util.collections.*;
import java.util.*;

/** this class is not yet well done */

public class MElementEvent extends java.util.EventObject
{
  public final static int ELEMENT_REMOVED = 0;
  public final static int ELEMENT_RECOVERED = 1;
  public final static int ATTRIBUTE_SET = 2;
  public final static int REFERENCE_SET = 3;
  public final static int BAG_ROLE_SET = 4;
  public final static int BAG_ROLE_ADDED = 5;
  public final static int BAG_ROLE_REMOVED = 6;
  public final static int LIST_ROLE_SET = 7;
  public final static int LIST_ROLE_ADDED = 8;
  public final static int LIST_ROLE_ITEM_SET = 9;
  public final static int LIST_ROLE_REMOVED = 10;

  public MElementEvent(MBase e, int type)
  {
    this(e, null, type, null, null, null, null, -1);
  }
  public MElementEvent(MBase e, String name, int type, Object oldVal, Object newVal)
  {
    this(e, name, type, oldVal, newVal, null, null, -1);
  }

  public MElementEvent(MBase e, String name, int type,  Collection newVal, Object added, Object removed)
  {
    this(e, name, type, null, newVal, added, removed, -1);
  }

  public MElementEvent(MBase e, String name, int type,  Collection newVal, Object added, Object removed, int pos)
  {
    this(e, name, type, null, newVal, added, removed, pos);
  }

  public MElementEvent(MBase e, String nm, int t, Object oldVal, Object newVal, Object added, Object removed, int pos)
  {
    super(e);
    type = t;
    name = nm;
    oldValue = oldVal;
    newValue = newVal;
    addedValue = added;
    removedValue = removed;
    position = pos;
  }

  String name;
  int type;
  Object oldValue;
  Object newValue;
  Object addedValue;
  Object removedValue;
  int position;

  public String getName()
  {
    return name;
  }

  public int getType()
  {
    return type;
  }

  public Object getOldValue()
  {
    return oldValue;
  }

  public Object getNewValue()
  {
    return newValue;
  }

  public Object getAddedValue()
  {
    return addedValue;
  }

  public Object getRemovedValue()
  {
    return removedValue;
  }

  public int getPosition()
  {
    return position;
  }

}
