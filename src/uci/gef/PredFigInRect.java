// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: PredFigInRect.java
// Classes: PredFigInRect
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import uci.util.*;

/** Predicate that returns true for Fig's that intersect
 *  the rectangle given in the constructor. */
public class PredFigInRect implements Predicate {

  ////////////////////////////////////////////////////////////////
  // instance variables

  Rectangle _r;

  ////////////////////////////////////////////////////////////////
  // constructor

  public PredFigInRect(Rectangle r) { _r = r; }

  ////////////////////////////////////////////////////////////////
  // Predicate API

  public boolean predicate(Object o) {
    if (!(o instanceof Fig)) return false;
    return ((Fig)o).intersects(_r);
  }

} /* end class PredFigInRect */
