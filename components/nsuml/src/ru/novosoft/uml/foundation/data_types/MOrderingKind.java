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

public final class MOrderingKind implements java.io.Serializable
{
  private final transient int _value;
  private final String _name;
  private final static HashMap _name2literal = new HashMap();
  private final static HashMap _value2literal = new HashMap();
  private MOrderingKind(int n, String name)
  {
    this._value = n;
    this._name = name;
    _name2literal.put(name,this);
    _value2literal.put(new Integer(n),this);
  }
  public static MOrderingKind forName(String name)
  {
    return (MOrderingKind)_name2literal.get(name);
  }
  public static MOrderingKind forValue(int n)
  {
    return (MOrderingKind)_value2literal.get(new Integer(n));
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
  public static final int _SORTED = 0;
  public static final MOrderingKind SORTED = new MOrderingKind(0,"sorted");
  public static final int _ORDERED = 1;
  public static final MOrderingKind ORDERED = new MOrderingKind(1,"ordered");
  public static final int _UNORDERED = 2;
  public static final MOrderingKind UNORDERED = new MOrderingKind(2,"unordered");
}
