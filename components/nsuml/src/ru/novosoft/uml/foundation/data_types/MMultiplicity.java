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

//import com.sun.java.util.collections.*;
import java.util.*;

import java.util.StringTokenizer;
public final class MMultiplicity implements java.io.Serializable
{
  final List ranges;

  public static final int N = -1;
  public static final MMultiplicity M0_1 = new MMultiplicity(0,1);
  public static final MMultiplicity M1_1 = new MMultiplicity(1,1);
  public static final MMultiplicity M0_N = new MMultiplicity(0,N);
  public static final MMultiplicity M1_N = new MMultiplicity(1,N);

  public MMultiplicity(String str)
  {
    this.ranges = Collections.unmodifiableList(parseRanges(str));
  }

  public MMultiplicity(int lower, int upper)
  {
    List l = new ArrayList();
    l.add(new MMultiplicityRange(lower, upper));
    this.ranges = Collections.unmodifiableList(l);
  }

  public MMultiplicity(List ranges)
  {
    this.ranges = Collections.unmodifiableList(new ArrayList(ranges));
  }

  public List getRanges()
  {
    return ranges;
  }

  public int getUpper()
  {
    return ((MMultiplicityRange)(ranges.get(ranges.size()-1))).getUpper();
  }

  public int getLower()
  {
    return ((MMultiplicityRange)(ranges.get(0))).getLower();
  }

  public String toString()
  {
    String rc = "";
    Iterator i = ranges.iterator();
    boolean first = true;
    while (i.hasNext())
    {
      if (first)
      {
        first = false;
      }
      else
      {
        rc+=",";
      }
      rc+=i.next();
    }
    return rc;
  }

  public boolean equals(Object o)
  {
    if (null == o)
    {
      return false;
    }

    return toString().equals(o.toString());
  }

  public int hashCode()
  {
    return toString().hashCode();
  }

  private static List parseRanges(String str)
  {
    StringTokenizer stk = new StringTokenizer(str,",");
    if(! stk.hasMoreTokens())
    {
      throw new IllegalArgumentException("empty multiplicity");
    }
    List rc = new ArrayList();
    while(stk.hasMoreTokens())
    {
      rc.add(new MMultiplicityRange(stk.nextToken()));
    }
    return rc;
  }
}
