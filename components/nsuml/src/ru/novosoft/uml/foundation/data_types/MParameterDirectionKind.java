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

public final class MParameterDirectionKind implements java.io.Serializable
{
  private final transient int _value;
  private final String _name;
  private final static HashMap _name2literal = new HashMap();
  private final static HashMap _value2literal = new HashMap();
  private MParameterDirectionKind(int n, String name)
  {
    this._value = n;
    this._name = name;
    _name2literal.put(name,this);
    _value2literal.put(new Integer(n),this);
  }
  public static MParameterDirectionKind forName(String name)
  {
    return (MParameterDirectionKind)_name2literal.get(name);
  }
  public static MParameterDirectionKind forValue(int n)
  {
    return (MParameterDirectionKind)_value2literal.get(new Integer(n));
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
  public static final int _RETURN = 0;
  public static final MParameterDirectionKind RETURN = new MParameterDirectionKind(0,"return");
  public static final int _OUT = 1;
  public static final MParameterDirectionKind OUT = new MParameterDirectionKind(1,"out");
  public static final int _INOUT = 2;
  public static final MParameterDirectionKind INOUT = new MParameterDirectionKind(2,"inout");
  public static final int _IN = 3;
  public static final MParameterDirectionKind IN = new MParameterDirectionKind(3,"in");
}
