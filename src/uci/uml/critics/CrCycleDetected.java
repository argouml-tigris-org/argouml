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

// File: CrCycleDetected.java
// Classes: CrCycleDetected
// Original Author: jrobbins@ics.uci.edu
// $Id$

package jargo.softarch;

import java.util.*;
import jargo.kernel.*;
import uci.util.*;

/** This critic detects cycles among components in C2 architectures.
 *  Cycles are not prohibited in the C2 style, but they are rare
 *  enough that there is a good chance that the Designer made one
 *  without knowing it, so he might like to know. <A HREF=
 *  "../Critics.html#CrCycleDetected">Critic description</A>. */

public class CrCycleDetected extends C2Critic {

  public CrCycleDetected() {
    setHeadline("Cycle Detected");
    setDescription("The C2 style generally does not allow cycles \n" +
		   "in the conceptual architecture");
    setDecisionCategory(C2.decCONCEPTUAL_TOPOLOGY);
    setMoreInfoURL(C2.C2CriticsURL + "#CrCycleDetected");
  }

  public boolean predicate(DesignMaterial dm, Designer dsgr) {
    if (dm instanceof C2BrickDM) {
      C2BrickDM brick = (C2BrickDM) dm;
      Set above = (new Set(brick)).reachable(new upwardTrans());
      //System.out.println("dm = " + dm.toString() +
      //"\n top   = " + brick.top().toString() +
      //"\n bot   = " + brick.bot().toString() +
      //"\n above = " + above.toString());
      boolean res = above.contains(brick) ||
	super.predicate(dm, dsgr);
      //System.out.println("Cycle = " + res);
      return res;
    }
    else return super.predicate(dm, dsgr);
  }

  /** Find exactly which other C2BricksDM's are involved in this
   *  cycle. */
  public ToDoItem toDoItem(DesignMaterial dm, Designer dsgr) {
    C2BrickDM brick = (C2BrickDM) dm;
    Set offs = computeOffenders(brick);
    return new ToDoItem(this, offs, dsgr);
  }

  // Needs-More-Work: only compute this when the user clicks on the ToDoItem
  protected Set computeOffenders(C2BrickDM brick) {
    Set offs = new Set(brick);
    Set above = offs.reachable(new upwardTrans());
    Enumeration aboves = above.elements();
    while (aboves.hasMoreElements()) {
      C2BrickDM b = (C2BrickDM) aboves.nextElement();
      Set trans = (new Set(b)).reachable(new upwardTrans());
      if (trans.contains(brick)) offs.addElement(b);
    }
    return offs;
  }

  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    Set offs = i.getOffenders();
    C2BrickDM dm = (C2BrickDM) offs.firstElement();
    if (!predicate(dm, dsgr)) return false;
    Set newOffs = computeOffenders(dm);
    boolean res = offs.equals(newOffs);
//     System.out.println("offs="+ offs.toString() +
// 		       " newOffs="+ newOffs.toString() +
// 		       " res = " + res);
    return res;
  }

} /* end class CrCycleDetected */


class upwardTrans implements ChildGenerator {
  public Enumeration gen(Object o) {
    C2BrickDM brick = (C2BrickDM) o;
    Enumeration bottomPortsAbove = brick.topConns().elements();
    return new Enum(bottomPortsAbove, FunctorPortOwner.theInstance(),
		    null, null);
  }
} /* end class upwardTrans */

