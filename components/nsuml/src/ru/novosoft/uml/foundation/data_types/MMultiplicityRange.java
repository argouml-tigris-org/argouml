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

package ru.novosoft.uml.foundation.data_types;
import java.util.*;


public final class MMultiplicityRange implements java.io.Serializable
{
  public final int lower;
  public final int upper;

  public MMultiplicityRange(String str)
  {
    StringTokenizer stk = new StringTokenizer(str,". ");
    if(! stk.hasMoreTokens())
    {
      throw new IllegalArgumentException("empty multiplicity range");
    }
    lower = s2b(stk.nextToken());
    if(! stk.hasMoreTokens())
    {
      upper = lower;
    }
    else
    {
      upper = s2b(stk.nextToken());
      if(stk.hasMoreTokens())
      {
        throw new IllegalArgumentException("illegal range specification");
      }
    }
  }

  public MMultiplicityRange(int lower, int upper)
  {
    this.lower = lower;
    this.upper = upper;
  }

  public int getUpper()
  {
    return upper;
  }

  public int getLower()
  {
    return lower;
  }

  public String toString()
  {
    if(lower == upper)
      return b2s(lower);
    else 
      return b2s(lower)+".."+b2s(upper);
  }

  static String b2s(int i)
  {
    if(i == MMultiplicity.N)
      return "*";
    else
      return ""+i;
  }
  static int s2b(String b)
  {
    try
    {
      if(b.equals("n") || b.equals("*"))
        return MMultiplicity.N;
      else
        return Integer.parseInt(b);
    }
    catch(Exception ex)
    {
      throw new IllegalArgumentException("illegal range bound");
    }
  }
}
