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

public final class MMultiplicityRangeEditor
{
  public int lower;
  public int upper;


  public int getUpper()
  {
    return upper;
  }

  public void setUpper(int arg)
  {
    upper = arg;
  }


  public int getLower()
  {
    return lower;
  }

  public void setLower(int arg)
  {
    lower = arg;
  }

  public MMultiplicityRange toMultiplicityRange()
  {
    return new MMultiplicityRange(lower, upper);
  }
}
