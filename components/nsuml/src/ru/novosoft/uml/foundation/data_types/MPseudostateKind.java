/* Novosoft UML API for Java. Version 0.4.19
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

package ru.novosoft.uml.foundation.data_types;
import java.util.HashMap;

public final class MPseudostateKind implements java.io.Serializable
{
  private final transient int _value;
  private final String _name;
  private final static HashMap _name2literal = new HashMap();
  private final static HashMap _value2literal = new HashMap();
  private MPseudostateKind(int n, String name)
  {
    this._value = n;
    this._name = name;
    _name2literal.put(name,this);
    _value2literal.put(new Integer(n),this);
  }
  public static MPseudostateKind forName(String name)
  {
    return (MPseudostateKind)_name2literal.get(name);
  }
  public static MPseudostateKind forValue(int n)
  {
    return (MPseudostateKind)_value2literal.get(new Integer(n));
  }
  public int getValue()
  {
    return _value;
  }
  public String getName()
  {
    return _name;
  }
  protected Object readResolve() throws java.io.ObjectStreamException
  {
    return forName(_name);
  }
  public static final int _JUNCTION = 0;
  public static final MPseudostateKind JUNCTION = new MPseudostateKind(0,"junction");
  public static final int _BRANCH = 1;
  public static final MPseudostateKind BRANCH = new MPseudostateKind(1,"branch");
  public static final int _FORK = 2;
  public static final MPseudostateKind FORK = new MPseudostateKind(2,"fork");
  public static final int _JOIN = 3;
  public static final MPseudostateKind JOIN = new MPseudostateKind(3,"join");
  public static final int _DEEP_HISTORY = 4;
  public static final MPseudostateKind DEEP_HISTORY = new MPseudostateKind(4,"deepHistory");
  public static final int _SHALLOW_HISTORY = 5;
  public static final MPseudostateKind SHALLOW_HISTORY = new MPseudostateKind(5,"shallowHistory");
  public static final int _FINAL = 6;
  public static final MPseudostateKind FINAL = new MPseudostateKind(6,"final");
  public static final int _INITIAL = 7;
  public static final MPseudostateKind INITIAL = new MPseudostateKind(7,"initial");
}
